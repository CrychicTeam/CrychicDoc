package software.bernie.geckolib.core.animation;

import com.eliotlash.mclib.math.Constant;
import com.eliotlash.mclib.math.IValue;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.keyframe.AnimationPoint;
import software.bernie.geckolib.core.keyframe.BoneAnimation;
import software.bernie.geckolib.core.keyframe.BoneAnimationQueue;
import software.bernie.geckolib.core.keyframe.Keyframe;
import software.bernie.geckolib.core.keyframe.KeyframeLocation;
import software.bernie.geckolib.core.keyframe.KeyframeStack;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.ParticleKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.KeyFrameData;
import software.bernie.geckolib.core.keyframe.event.data.ParticleKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.core.object.Axis;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.state.BoneSnapshot;

public class AnimationController<T extends GeoAnimatable> {

    protected final T animatable;

    protected final String name;

    protected final AnimationController.AnimationStateHandler<T> stateHandler;

    protected final Map<String, BoneAnimationQueue> boneAnimationQueues = new Object2ObjectOpenHashMap();

    protected final Map<String, BoneSnapshot> boneSnapshots = new Object2ObjectOpenHashMap();

    protected Queue<AnimationProcessor.QueuedAnimation> animationQueue = new LinkedList();

    protected boolean isJustStarting = false;

    protected boolean needsAnimationReload = false;

    protected boolean shouldResetTick = false;

    private boolean justStopped = true;

    protected boolean justStartedTransition = false;

    protected AnimationController.SoundKeyframeHandler<T> soundKeyframeHandler = null;

    protected AnimationController.ParticleKeyframeHandler<T> particleKeyframeHandler = null;

    protected AnimationController.CustomKeyframeHandler<T> customKeyframeHandler = null;

    protected final Map<String, RawAnimation> triggerableAnimations = new Object2ObjectOpenHashMap(0);

    protected RawAnimation triggeredAnimation = null;

    protected boolean handlingTriggeredAnimations = false;

    protected double transitionLength;

    protected RawAnimation currentRawAnimation;

    protected AnimationProcessor.QueuedAnimation currentAnimation;

    protected AnimationController.State animationState = AnimationController.State.STOPPED;

    protected double tickOffset;

    protected double lastPollTime = -1.0;

    protected Function<T, Double> animationSpeedModifier = animatablex -> 1.0;

    protected Function<T, EasingType> overrideEasingTypeFunction = animatablex -> null;

    private final Set<KeyFrameData> executedKeyFrames = new ObjectOpenHashSet();

    protected CoreGeoModel<T> lastModel;

    public AnimationController(T animatable, AnimationController.AnimationStateHandler<T> animationHandler) {
        this(animatable, "base_controller", 0, animationHandler);
    }

    public AnimationController(T animatable, String name, AnimationController.AnimationStateHandler<T> animationHandler) {
        this(animatable, name, 0, animationHandler);
    }

    public AnimationController(T animatable, int transitionTickTime, AnimationController.AnimationStateHandler<T> animationHandler) {
        this(animatable, "base_controller", transitionTickTime, animationHandler);
    }

    public AnimationController(T animatable, String name, int transitionTickTime, AnimationController.AnimationStateHandler<T> animationHandler) {
        this.animatable = animatable;
        this.name = name;
        this.transitionLength = (double) transitionTickTime;
        this.stateHandler = animationHandler;
    }

    public AnimationController<T> setSoundKeyframeHandler(AnimationController.SoundKeyframeHandler<T> soundHandler) {
        this.soundKeyframeHandler = soundHandler;
        return this;
    }

    public AnimationController<T> setParticleKeyframeHandler(AnimationController.ParticleKeyframeHandler<T> particleHandler) {
        this.particleKeyframeHandler = particleHandler;
        return this;
    }

    public AnimationController<T> setCustomInstructionKeyframeHandler(AnimationController.CustomKeyframeHandler<T> customInstructionHandler) {
        this.customKeyframeHandler = customInstructionHandler;
        return this;
    }

    public AnimationController<T> setAnimationSpeedHandler(Function<T, Double> speedModFunction) {
        this.animationSpeedModifier = speedModFunction;
        return this;
    }

    public AnimationController<T> setAnimationSpeed(double speed) {
        return this.setAnimationSpeedHandler(animatable -> speed);
    }

    public AnimationController<T> setOverrideEasingType(EasingType easingTypeFunction) {
        return this.setOverrideEasingTypeFunction(animatable -> easingTypeFunction);
    }

    public AnimationController<T> setOverrideEasingTypeFunction(Function<T, EasingType> easingType) {
        this.overrideEasingTypeFunction = easingType;
        return this;
    }

    public AnimationController<T> triggerableAnim(String name, RawAnimation animation) {
        this.triggerableAnimations.put(name, animation);
        return this;
    }

    public AnimationController<T> receiveTriggeredAnimations() {
        this.handlingTriggeredAnimations = true;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public AnimationProcessor.QueuedAnimation getCurrentAnimation() {
        return this.currentAnimation;
    }

    public AnimationController.State getAnimationState() {
        return this.animationState;
    }

    public Map<String, BoneAnimationQueue> getBoneAnimationQueues() {
        return this.boneAnimationQueues;
    }

    public double getAnimationSpeed() {
        return (Double) this.animationSpeedModifier.apply(this.animatable);
    }

    public void forceAnimationReset() {
        this.needsAnimationReload = true;
    }

    public void stop() {
        this.animationState = AnimationController.State.STOPPED;
    }

    @Deprecated(forRemoval = true)
    public void setTransitionLength(int ticks) {
        this.transitionLength = (double) ticks;
    }

    public AnimationController<T> transitionLength(int ticks) {
        this.setTransitionLength(ticks);
        return this;
    }

    public boolean hasAnimationFinished() {
        return this.currentRawAnimation != null && this.animationState == AnimationController.State.STOPPED;
    }

    public RawAnimation getCurrentRawAnimation() {
        return this.currentRawAnimation;
    }

    public boolean isPlayingTriggeredAnimation() {
        return this.triggeredAnimation != null && !this.hasAnimationFinished();
    }

    public void setAnimation(RawAnimation rawAnimation) {
        if (rawAnimation != null && !rawAnimation.getAnimationStages().isEmpty()) {
            if (this.needsAnimationReload || !rawAnimation.equals(this.currentRawAnimation)) {
                if (this.lastModel != null) {
                    Queue<AnimationProcessor.QueuedAnimation> animations = this.lastModel.getAnimationProcessor().buildAnimationQueue(this.animatable, rawAnimation);
                    if (animations != null) {
                        this.animationQueue = animations;
                        this.currentRawAnimation = rawAnimation;
                        this.shouldResetTick = true;
                        this.animationState = AnimationController.State.TRANSITIONING;
                        this.justStartedTransition = true;
                        this.needsAnimationReload = false;
                        return;
                    }
                }
                this.stop();
            }
        } else {
            this.stop();
        }
    }

    public boolean tryTriggerAnimation(String animName) {
        RawAnimation anim = (RawAnimation) this.triggerableAnimations.get(animName);
        if (anim == null) {
            return false;
        } else {
            this.triggeredAnimation = anim;
            if (this.animationState == AnimationController.State.STOPPED) {
                this.animationState = AnimationController.State.TRANSITIONING;
                this.shouldResetTick = true;
                this.justStartedTransition = true;
            }
            return true;
        }
    }

    protected PlayState handleAnimationState(AnimationState<T> state) {
        if (this.triggeredAnimation != null) {
            if (this.currentRawAnimation != this.triggeredAnimation) {
                this.currentAnimation = null;
            }
            this.setAnimation(this.triggeredAnimation);
            if (!this.hasAnimationFinished() && (!this.handlingTriggeredAnimations || this.stateHandler.handle(state) == PlayState.CONTINUE)) {
                return PlayState.CONTINUE;
            }
            this.triggeredAnimation = null;
            this.needsAnimationReload = true;
        }
        return this.stateHandler.handle(state);
    }

    public void process(CoreGeoModel<T> model, AnimationState<T> state, Map<String, CoreGeoBone> bones, Map<String, BoneSnapshot> snapshots, double seekTime, boolean crashWhenCantFindBone) {
        double adjustedTick = this.adjustTick(seekTime);
        this.lastModel = model;
        if (this.animationState == AnimationController.State.TRANSITIONING && adjustedTick >= this.transitionLength) {
            this.shouldResetTick = true;
            this.animationState = AnimationController.State.RUNNING;
            adjustedTick = this.adjustTick(seekTime);
        }
        PlayState playState = this.handleAnimationState(state);
        if (playState != PlayState.STOP && (this.currentAnimation != null || !this.animationQueue.isEmpty())) {
            this.createInitialQueues(bones.values());
            if (this.justStartedTransition && (this.shouldResetTick || this.justStopped)) {
                this.justStopped = false;
                adjustedTick = this.adjustTick(seekTime);
                if (this.currentAnimation == null) {
                    this.animationState = AnimationController.State.TRANSITIONING;
                }
            } else if (this.currentAnimation == null) {
                this.shouldResetTick = true;
                this.animationState = AnimationController.State.TRANSITIONING;
                this.justStartedTransition = true;
                this.needsAnimationReload = false;
                adjustedTick = this.adjustTick(seekTime);
            } else if (this.animationState != AnimationController.State.TRANSITIONING) {
                this.animationState = AnimationController.State.RUNNING;
            }
            if (this.getAnimationState() == AnimationController.State.RUNNING) {
                this.processCurrentAnimation(adjustedTick, seekTime, crashWhenCantFindBone);
            } else if (this.animationState == AnimationController.State.TRANSITIONING) {
                if (this.lastPollTime != seekTime && (adjustedTick == 0.0 || this.isJustStarting)) {
                    this.justStartedTransition = false;
                    this.lastPollTime = seekTime;
                    this.currentAnimation = (AnimationProcessor.QueuedAnimation) this.animationQueue.poll();
                    this.resetEventKeyFrames();
                    if (this.currentAnimation == null) {
                        return;
                    }
                    this.saveSnapshotsForAnimation(this.currentAnimation, snapshots);
                }
                if (this.currentAnimation != null) {
                    MolangParser.INSTANCE.setValue("query.anim_time", () -> 0.0);
                    for (BoneAnimation boneAnimation : this.currentAnimation.animation().boneAnimations()) {
                        BoneAnimationQueue boneAnimationQueue = (BoneAnimationQueue) this.boneAnimationQueues.get(boneAnimation.boneName());
                        BoneSnapshot boneSnapshot = (BoneSnapshot) this.boneSnapshots.get(boneAnimation.boneName());
                        CoreGeoBone bone = (CoreGeoBone) bones.get(boneAnimation.boneName());
                        if (boneSnapshot != null) {
                            if (bone == null) {
                                if (crashWhenCantFindBone) {
                                    throw new RuntimeException("Could not find bone: " + boneAnimation.boneName());
                                }
                            } else {
                                KeyframeStack<Keyframe<IValue>> rotationKeyFrames = boneAnimation.rotationKeyFrames();
                                KeyframeStack<Keyframe<IValue>> positionKeyFrames = boneAnimation.positionKeyFrames();
                                KeyframeStack<Keyframe<IValue>> scaleKeyFrames = boneAnimation.scaleKeyFrames();
                                if (!rotationKeyFrames.xKeyframes().isEmpty()) {
                                    boneAnimationQueue.addNextRotation(null, adjustedTick, this.transitionLength, boneSnapshot, bone.getInitialSnapshot(), this.getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), 0.0, true, Axis.X), this.getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), 0.0, true, Axis.Y), this.getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), 0.0, true, Axis.Z));
                                }
                                if (!positionKeyFrames.xKeyframes().isEmpty()) {
                                    boneAnimationQueue.addNextPosition(null, adjustedTick, this.transitionLength, boneSnapshot, this.getAnimationPointAtTick(positionKeyFrames.xKeyframes(), 0.0, false, Axis.X), this.getAnimationPointAtTick(positionKeyFrames.yKeyframes(), 0.0, false, Axis.Y), this.getAnimationPointAtTick(positionKeyFrames.zKeyframes(), 0.0, false, Axis.Z));
                                }
                                if (!scaleKeyFrames.xKeyframes().isEmpty()) {
                                    boneAnimationQueue.addNextScale(null, adjustedTick, this.transitionLength, boneSnapshot, this.getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), 0.0, false, Axis.X), this.getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), 0.0, false, Axis.Y), this.getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), 0.0, false, Axis.Z));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            this.animationState = AnimationController.State.STOPPED;
            this.justStopped = true;
        }
    }

    private void processCurrentAnimation(double adjustedTick, double seekTime, boolean crashWhenCantFindBone) {
        if (adjustedTick >= this.currentAnimation.animation().length()) {
            if (this.currentAnimation.loopType().shouldPlayAgain(this.animatable, this, this.currentAnimation.animation())) {
                if (this.animationState != AnimationController.State.PAUSED) {
                    this.shouldResetTick = true;
                    adjustedTick = this.adjustTick(seekTime);
                    this.resetEventKeyFrames();
                }
            } else {
                AnimationProcessor.QueuedAnimation nextAnimation = (AnimationProcessor.QueuedAnimation) this.animationQueue.peek();
                this.resetEventKeyFrames();
                if (nextAnimation == null) {
                    this.animationState = AnimationController.State.STOPPED;
                    return;
                }
                this.animationState = AnimationController.State.TRANSITIONING;
                this.shouldResetTick = true;
                adjustedTick = this.adjustTick(seekTime);
                this.currentAnimation = (AnimationProcessor.QueuedAnimation) this.animationQueue.poll();
            }
        }
        double finalAdjustedTick = adjustedTick;
        MolangParser.INSTANCE.setMemoizedValue("query.anim_time", () -> finalAdjustedTick / 20.0);
        for (BoneAnimation boneAnimation : this.currentAnimation.animation().boneAnimations()) {
            BoneAnimationQueue boneAnimationQueue = (BoneAnimationQueue) this.boneAnimationQueues.get(boneAnimation.boneName());
            if (boneAnimationQueue == null) {
                if (crashWhenCantFindBone) {
                    throw new RuntimeException("Could not find bone: " + boneAnimation.boneName());
                }
            } else {
                KeyframeStack<Keyframe<IValue>> rotationKeyFrames = boneAnimation.rotationKeyFrames();
                KeyframeStack<Keyframe<IValue>> positionKeyFrames = boneAnimation.positionKeyFrames();
                KeyframeStack<Keyframe<IValue>> scaleKeyFrames = boneAnimation.scaleKeyFrames();
                if (!rotationKeyFrames.xKeyframes().isEmpty()) {
                    boneAnimationQueue.addRotations(this.getAnimationPointAtTick(rotationKeyFrames.xKeyframes(), adjustedTick, true, Axis.X), this.getAnimationPointAtTick(rotationKeyFrames.yKeyframes(), adjustedTick, true, Axis.Y), this.getAnimationPointAtTick(rotationKeyFrames.zKeyframes(), adjustedTick, true, Axis.Z));
                }
                if (!positionKeyFrames.xKeyframes().isEmpty()) {
                    boneAnimationQueue.addPositions(this.getAnimationPointAtTick(positionKeyFrames.xKeyframes(), adjustedTick, false, Axis.X), this.getAnimationPointAtTick(positionKeyFrames.yKeyframes(), adjustedTick, false, Axis.Y), this.getAnimationPointAtTick(positionKeyFrames.zKeyframes(), adjustedTick, false, Axis.Z));
                }
                if (!scaleKeyFrames.xKeyframes().isEmpty()) {
                    boneAnimationQueue.addScales(this.getAnimationPointAtTick(scaleKeyFrames.xKeyframes(), adjustedTick, false, Axis.X), this.getAnimationPointAtTick(scaleKeyFrames.yKeyframes(), adjustedTick, false, Axis.Y), this.getAnimationPointAtTick(scaleKeyFrames.zKeyframes(), adjustedTick, false, Axis.Z));
                }
            }
        }
        adjustedTick += this.transitionLength;
        for (SoundKeyframeData keyframeData : this.currentAnimation.animation().keyFrames().sounds()) {
            if (adjustedTick >= keyframeData.getStartTick() && this.executedKeyFrames.add(keyframeData)) {
                if (this.soundKeyframeHandler == null) {
                    System.out.println("Sound Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + this.getName() + ", but no keyframe handler registered");
                    break;
                }
                this.soundKeyframeHandler.handle(new SoundKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeData));
            }
        }
        for (ParticleKeyframeData keyframeDatax : this.currentAnimation.animation().keyFrames().particles()) {
            if (adjustedTick >= keyframeDatax.getStartTick() && this.executedKeyFrames.add(keyframeDatax)) {
                if (this.particleKeyframeHandler == null) {
                    System.out.println("Particle Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + this.getName() + ", but no keyframe handler registered");
                    break;
                }
                this.particleKeyframeHandler.handle(new ParticleKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeDatax));
            }
        }
        for (CustomInstructionKeyframeData keyframeDataxx : this.currentAnimation.animation().keyFrames().customInstructions()) {
            if (adjustedTick >= keyframeDataxx.getStartTick() && this.executedKeyFrames.add(keyframeDataxx)) {
                if (this.customKeyframeHandler == null) {
                    System.out.println("Custom Instruction Keyframe found for " + this.animatable.getClass().getSimpleName() + " -> " + this.getName() + ", but no keyframe handler registered");
                    break;
                }
                this.customKeyframeHandler.handle(new CustomInstructionKeyframeEvent<>(this.animatable, adjustedTick, this, keyframeDataxx));
            }
        }
        if (this.transitionLength == 0.0 && this.shouldResetTick && this.animationState == AnimationController.State.TRANSITIONING) {
            this.currentAnimation = (AnimationProcessor.QueuedAnimation) this.animationQueue.poll();
        }
    }

    private void createInitialQueues(Collection<CoreGeoBone> modelRendererList) {
        this.boneAnimationQueues.clear();
        for (CoreGeoBone modelRenderer : modelRendererList) {
            this.boneAnimationQueues.put(modelRenderer.getName(), new BoneAnimationQueue(modelRenderer));
        }
    }

    private void saveSnapshotsForAnimation(AnimationProcessor.QueuedAnimation animation, Map<String, BoneSnapshot> snapshots) {
        for (BoneSnapshot snapshot : snapshots.values()) {
            if (animation.animation().boneAnimations() != null) {
                for (BoneAnimation boneAnimation : animation.animation().boneAnimations()) {
                    if (boneAnimation.boneName().equals(snapshot.getBone().getName())) {
                        this.boneSnapshots.put(boneAnimation.boneName(), BoneSnapshot.copy(snapshot));
                        break;
                    }
                }
            }
        }
    }

    protected double adjustTick(double tick) {
        if (!this.shouldResetTick) {
            return (Double) this.animationSpeedModifier.apply(this.animatable) * Math.max(tick - this.tickOffset, 0.0);
        } else {
            if (this.getAnimationState() != AnimationController.State.STOPPED) {
                this.tickOffset = tick;
            }
            this.shouldResetTick = false;
            return 0.0;
        }
    }

    private AnimationPoint getAnimationPointAtTick(List<Keyframe<IValue>> frames, double tick, boolean isRotation, Axis axis) {
        KeyframeLocation<Keyframe<IValue>> location = this.getCurrentKeyFrameLocation(frames, tick);
        Keyframe<IValue> currentFrame = location.keyframe();
        double startValue = currentFrame.startValue().get();
        double endValue = currentFrame.endValue().get();
        if (isRotation) {
            if (!(currentFrame.startValue() instanceof Constant)) {
                startValue = Math.toRadians(startValue);
                if (axis == Axis.X || axis == Axis.Y) {
                    startValue *= -1.0;
                }
            }
            if (!(currentFrame.endValue() instanceof Constant)) {
                endValue = Math.toRadians(endValue);
                if (axis == Axis.X || axis == Axis.Y) {
                    endValue *= -1.0;
                }
            }
        }
        return new AnimationPoint(currentFrame, location.startTick(), currentFrame.length(), startValue, endValue);
    }

    private KeyframeLocation<Keyframe<IValue>> getCurrentKeyFrameLocation(List<Keyframe<IValue>> frames, double ageInTicks) {
        double totalFrameTime = 0.0;
        for (Keyframe<IValue> frame : frames) {
            totalFrameTime += frame.length();
            if (totalFrameTime > ageInTicks) {
                return new KeyframeLocation<>(frame, ageInTicks - (totalFrameTime - frame.length()));
            }
        }
        return new KeyframeLocation<>((Keyframe<IValue>) frames.get(frames.size() - 1), ageInTicks);
    }

    private void resetEventKeyFrames() {
        this.executedKeyFrames.clear();
    }

    @FunctionalInterface
    public interface AnimationStateHandler<A extends GeoAnimatable> {

        PlayState handle(AnimationState<A> var1);
    }

    @FunctionalInterface
    public interface CustomKeyframeHandler<A extends GeoAnimatable> {

        void handle(CustomInstructionKeyframeEvent<A> var1);
    }

    @FunctionalInterface
    public interface ParticleKeyframeHandler<A extends GeoAnimatable> {

        void handle(ParticleKeyframeEvent<A> var1);
    }

    @FunctionalInterface
    public interface SoundKeyframeHandler<A extends GeoAnimatable> {

        void handle(SoundKeyframeEvent<A> var1);
    }

    public static enum State {

        RUNNING, TRANSITIONING, PAUSED, STOPPED
    }
}