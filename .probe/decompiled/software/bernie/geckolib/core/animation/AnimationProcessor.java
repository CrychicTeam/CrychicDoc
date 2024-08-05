package software.bernie.geckolib.core.animation;

import com.eliotlash.mclib.utils.Interpolations;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreBakedGeoModel;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoModel;
import software.bernie.geckolib.core.keyframe.AnimationPoint;
import software.bernie.geckolib.core.keyframe.BoneAnimationQueue;
import software.bernie.geckolib.core.state.BoneSnapshot;

public class AnimationProcessor<T extends GeoAnimatable> {

    private final Map<String, CoreGeoBone> bones = new Object2ObjectOpenHashMap();

    private final CoreGeoModel<T> model;

    public boolean reloadAnimations = false;

    public AnimationProcessor(CoreGeoModel<T> model) {
        this.model = model;
    }

    public Queue<AnimationProcessor.QueuedAnimation> buildAnimationQueue(T animatable, RawAnimation rawAnimation) {
        LinkedList<AnimationProcessor.QueuedAnimation> animations = new LinkedList();
        boolean error = false;
        for (RawAnimation.Stage stage : rawAnimation.getAnimationStages()) {
            Animation animation;
            if (stage.animationName() == "internal.wait") {
                animation = Animation.generateWaitAnimation((double) stage.additionalTicks());
            } else {
                animation = this.model.getAnimation(animatable, stage.animationName());
            }
            if (animation == null) {
                System.out.println("Unable to find animation: " + stage.animationName() + " for " + animatable.getClass().getSimpleName());
                error = true;
            } else {
                animations.add(new AnimationProcessor.QueuedAnimation(animation, stage.loopType()));
            }
        }
        return error ? null : animations;
    }

    public void tickAnimation(T animatable, CoreGeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> state, boolean crashWhenCantFindBone) {
        Map<String, BoneSnapshot> boneSnapshots = this.updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());
        for (AnimationController<T> controller : animatableManager.getAnimationControllers().values()) {
            if (this.reloadAnimations) {
                controller.forceAnimationReset();
                controller.getBoneAnimationQueues().clear();
            }
            controller.isJustStarting = animatableManager.isFirstTick();
            state.withController(controller);
            controller.process(model, state, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);
            for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
                CoreGeoBone bone = boneAnimation.bone();
                BoneSnapshot snapshot = (BoneSnapshot) boneSnapshots.get(bone.getName());
                BoneSnapshot initialSnapshot = bone.getInitialSnapshot();
                AnimationPoint rotXPoint = (AnimationPoint) boneAnimation.rotationXQueue().poll();
                AnimationPoint rotYPoint = (AnimationPoint) boneAnimation.rotationYQueue().poll();
                AnimationPoint rotZPoint = (AnimationPoint) boneAnimation.rotationZQueue().poll();
                AnimationPoint posXPoint = (AnimationPoint) boneAnimation.positionXQueue().poll();
                AnimationPoint posYPoint = (AnimationPoint) boneAnimation.positionYQueue().poll();
                AnimationPoint posZPoint = (AnimationPoint) boneAnimation.positionZQueue().poll();
                AnimationPoint scaleXPoint = (AnimationPoint) boneAnimation.scaleXQueue().poll();
                AnimationPoint scaleYPoint = (AnimationPoint) boneAnimation.scaleYQueue().poll();
                AnimationPoint scaleZPoint = (AnimationPoint) boneAnimation.scaleZQueue().poll();
                EasingType easingType = (EasingType) controller.overrideEasingTypeFunction.apply(animatable);
                if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
                    bone.setRotX((float) EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
                    bone.setRotY((float) EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
                    bone.setRotZ((float) EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
                    snapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                    snapshot.startRotAnim();
                    bone.markRotationAsChanged();
                }
                if (posXPoint != null && posYPoint != null && posZPoint != null) {
                    bone.setPosX((float) EasingType.lerpWithOverride(posXPoint, easingType));
                    bone.setPosY((float) EasingType.lerpWithOverride(posYPoint, easingType));
                    bone.setPosZ((float) EasingType.lerpWithOverride(posZPoint, easingType));
                    snapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                    snapshot.startPosAnim();
                    bone.markPositionAsChanged();
                }
                if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
                    bone.setScaleX((float) EasingType.lerpWithOverride(scaleXPoint, easingType));
                    bone.setScaleY((float) EasingType.lerpWithOverride(scaleYPoint, easingType));
                    bone.setScaleZ((float) EasingType.lerpWithOverride(scaleZPoint, easingType));
                    snapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                    snapshot.startScaleAnim();
                    bone.markScaleAsChanged();
                }
            }
        }
        this.reloadAnimations = false;
        double resetTickLength = animatable.getBoneResetTime();
        for (CoreGeoBone bonex : this.getRegisteredBones()) {
            if (!bonex.hasRotationChanged()) {
                BoneSnapshot initialSnapshotx = bonex.getInitialSnapshot();
                BoneSnapshot saveSnapshot = (BoneSnapshot) boneSnapshots.get(bonex.getName());
                if (saveSnapshot.isRotAnimInProgress()) {
                    saveSnapshot.stopRotAnim(animTime);
                }
                double percentageReset = Math.min((animTime - saveSnapshot.getLastResetRotationTick()) / resetTickLength, 1.0);
                bonex.setRotX((float) Interpolations.lerp((double) saveSnapshot.getRotX(), (double) initialSnapshotx.getRotX(), percentageReset));
                bonex.setRotY((float) Interpolations.lerp((double) saveSnapshot.getRotY(), (double) initialSnapshotx.getRotY(), percentageReset));
                bonex.setRotZ((float) Interpolations.lerp((double) saveSnapshot.getRotZ(), (double) initialSnapshotx.getRotZ(), percentageReset));
                if (percentageReset >= 1.0) {
                    saveSnapshot.updateRotation(bonex.getRotX(), bonex.getRotY(), bonex.getRotZ());
                }
            }
            if (!bonex.hasPositionChanged()) {
                BoneSnapshot initialSnapshotxx = bonex.getInitialSnapshot();
                BoneSnapshot saveSnapshotx = (BoneSnapshot) boneSnapshots.get(bonex.getName());
                if (saveSnapshotx.isPosAnimInProgress()) {
                    saveSnapshotx.stopPosAnim(animTime);
                }
                double percentageReset = Math.min((animTime - saveSnapshotx.getLastResetPositionTick()) / resetTickLength, 1.0);
                bonex.setPosX((float) Interpolations.lerp((double) saveSnapshotx.getOffsetX(), (double) initialSnapshotxx.getOffsetX(), percentageReset));
                bonex.setPosY((float) Interpolations.lerp((double) saveSnapshotx.getOffsetY(), (double) initialSnapshotxx.getOffsetY(), percentageReset));
                bonex.setPosZ((float) Interpolations.lerp((double) saveSnapshotx.getOffsetZ(), (double) initialSnapshotxx.getOffsetZ(), percentageReset));
                if (percentageReset >= 1.0) {
                    saveSnapshotx.updateOffset(bonex.getPosX(), bonex.getPosY(), bonex.getPosZ());
                }
            }
            if (!bonex.hasScaleChanged()) {
                BoneSnapshot initialSnapshotxxx = bonex.getInitialSnapshot();
                BoneSnapshot saveSnapshotxx = (BoneSnapshot) boneSnapshots.get(bonex.getName());
                if (saveSnapshotxx.isScaleAnimInProgress()) {
                    saveSnapshotxx.stopScaleAnim(animTime);
                }
                double percentageReset = Math.min((animTime - saveSnapshotxx.getLastResetScaleTick()) / resetTickLength, 1.0);
                bonex.setScaleX((float) Interpolations.lerp((double) saveSnapshotxx.getScaleX(), (double) initialSnapshotxxx.getScaleX(), percentageReset));
                bonex.setScaleY((float) Interpolations.lerp((double) saveSnapshotxx.getScaleY(), (double) initialSnapshotxxx.getScaleY(), percentageReset));
                bonex.setScaleZ((float) Interpolations.lerp((double) saveSnapshotxx.getScaleZ(), (double) initialSnapshotxxx.getScaleZ(), percentageReset));
                if (percentageReset >= 1.0) {
                    saveSnapshotxx.updateScale(bonex.getScaleX(), bonex.getScaleY(), bonex.getScaleZ());
                }
            }
        }
        this.resetBoneTransformationMarkers();
        animatableManager.finishFirstTick();
    }

    private void resetBoneTransformationMarkers() {
        this.getRegisteredBones().forEach(CoreGeoBone::resetStateChanges);
    }

    private Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> snapshots) {
        for (CoreGeoBone bone : this.getRegisteredBones()) {
            if (!snapshots.containsKey(bone.getName())) {
                snapshots.put(bone.getName(), BoneSnapshot.copy(bone.getInitialSnapshot()));
            }
        }
        return snapshots;
    }

    public CoreGeoBone getBone(String boneName) {
        return (CoreGeoBone) this.bones.get(boneName);
    }

    public void registerGeoBone(CoreGeoBone bone) {
        bone.saveInitialSnapshot();
        this.bones.put(bone.getName(), bone);
        for (CoreGeoBone child : bone.getChildBones()) {
            this.registerGeoBone(child);
        }
    }

    public void setActiveModel(CoreBakedGeoModel model) {
        this.bones.clear();
        for (CoreGeoBone bone : model.getBones()) {
            this.registerGeoBone(bone);
        }
    }

    public Collection<CoreGeoBone> getRegisteredBones() {
        return this.bones.values();
    }

    public void preAnimationSetup(T animatable, double animTime) {
        this.model.applyMolangQueries(animatable, animTime);
    }

    public static record QueuedAnimation(Animation animation, Animation.LoopType loopType) {
    }
}