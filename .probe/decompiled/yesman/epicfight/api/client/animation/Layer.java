package yesman.epicfight.api.client.animation;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.types.ConcurrentLinkAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.LayerOffAnimation;
import yesman.epicfight.api.animation.types.LinkAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@OnlyIn(Dist.CLIENT)
public class Layer {

    protected DynamicAnimation nextAnimation;

    protected final LinkAnimation linkAnimation;

    protected final ConcurrentLinkAnimation concurrentLinkAnimation;

    protected final LayerOffAnimation layerOffAnimation;

    protected final Layer.Priority priority;

    protected boolean disabled;

    protected boolean paused;

    public final AnimationPlayer animationPlayer = new AnimationPlayer();

    public Layer(Layer.Priority priority) {
        this.linkAnimation = new LinkAnimation();
        this.concurrentLinkAnimation = new ConcurrentLinkAnimation();
        this.layerOffAnimation = new LayerOffAnimation(priority);
        this.priority = priority;
        this.disabled = true;
    }

    public void playAnimation(StaticAnimation nextAnimation, LivingEntityPatch<?> entitypatch, float convertTimeModifier) {
        Pose lastPose = entitypatch.getArmature().getPose(1.0F);
        this.animationPlayer.getAnimation().end(entitypatch, nextAnimation, this.animationPlayer.isEnd());
        this.resume();
        nextAnimation.begin(entitypatch);
        if (!nextAnimation.isMetaAnimation()) {
            this.setLinkAnimation(nextAnimation, entitypatch, lastPose, convertTimeModifier);
            this.linkAnimation.putOnPlayer(this.animationPlayer);
            entitypatch.updateEntityState();
            this.nextAnimation = nextAnimation;
        }
    }

    public void playAnimationInstant(DynamicAnimation nextAnimation, LivingEntityPatch<?> entitypatch) {
        this.animationPlayer.getAnimation().end(entitypatch, nextAnimation, this.animationPlayer.isEnd());
        this.resume();
        nextAnimation.begin(entitypatch);
        nextAnimation.putOnPlayer(this.animationPlayer);
        entitypatch.updateEntityState();
        this.nextAnimation = null;
    }

    protected void playLivingAnimation(StaticAnimation nextAnimation, LivingEntityPatch<?> entitypatch) {
        this.animationPlayer.getAnimation().end(entitypatch, nextAnimation, this.animationPlayer.isEnd());
        this.resume();
        nextAnimation.begin(entitypatch);
        if (!nextAnimation.isMetaAnimation()) {
            this.concurrentLinkAnimation.acceptFrom(this.animationPlayer.getAnimation().getRealAnimation(), nextAnimation, this.animationPlayer.getElapsedTime());
            this.concurrentLinkAnimation.putOnPlayer(this.animationPlayer);
            entitypatch.updateEntityState();
            this.nextAnimation = nextAnimation;
        }
    }

    protected void setLinkAnimation(DynamicAnimation nextAnimation, LivingEntityPatch<?> entitypatch, Pose lastPose, float convertTimeModifier) {
        nextAnimation.setLinkAnimation(lastPose, convertTimeModifier, entitypatch, this.linkAnimation);
    }

    public void update(LivingEntityPatch<?> entitypatch) {
        if (this.paused) {
            this.animationPlayer.setElapsedTime(this.animationPlayer.getElapsedTime());
        } else {
            this.animationPlayer.tick(entitypatch);
        }
        if (this.isBaseLayer()) {
            entitypatch.updateEntityState();
            entitypatch.updateMotion(true);
        }
        this.animationPlayer.getAnimation().tick(entitypatch);
        if (!this.paused && this.animationPlayer.isEnd()) {
            if (this.nextAnimation != null) {
                this.animationPlayer.getAnimation().end(entitypatch, this.nextAnimation, true);
                if (!(this.animationPlayer.getAnimation() instanceof LinkAnimation) && !(this.nextAnimation instanceof LinkAnimation)) {
                    this.nextAnimation.begin(entitypatch);
                }
                this.nextAnimation.putOnPlayer(this.animationPlayer);
                this.nextAnimation = null;
            } else if (this.animationPlayer.getAnimation() instanceof LayerOffAnimation) {
                this.animationPlayer.getAnimation().end(entitypatch, Animations.DUMMY_ANIMATION, true);
            } else {
                this.off(entitypatch);
            }
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
        this.disabled = false;
    }

    protected boolean isDisabled() {
        return this.disabled;
    }

    protected boolean isBaseLayer() {
        return false;
    }

    public void copyLayerTo(Layer layer, float playbackTime) {
        DynamicAnimation animation;
        if (this.animationPlayer.getAnimation() == this.linkAnimation) {
            this.linkAnimation.copyTo(layer.linkAnimation);
            animation = layer.linkAnimation;
        } else {
            animation = this.animationPlayer.getAnimation();
        }
        layer.animationPlayer.setPlayAnimation(animation);
        layer.animationPlayer.setElapsedTime(this.animationPlayer.getPrevElapsedTime() + playbackTime, this.animationPlayer.getElapsedTime() + playbackTime);
        layer.nextAnimation = this.nextAnimation;
        layer.resume();
    }

    public Pose getEnabledPose(LivingEntityPatch<?> entitypatch, float partialTick) {
        Pose pose = this.animationPlayer.getCurrentPose(entitypatch, partialTick);
        DynamicAnimation animation = this.animationPlayer.getAnimation();
        pose.removeJointIf((Predicate<? super Entry<String, JointTransform>>) (entry -> !animation.isJointEnabled(entitypatch, this.priority, (String) entry.getKey())));
        return pose;
    }

    public void off(LivingEntityPatch<?> entitypatch) {
        if (!this.isDisabled() && !(this.animationPlayer.getAnimation() instanceof LayerOffAnimation)) {
            float convertTime = entitypatch.getClientAnimator().baseLayer.animationPlayer.getAnimation().getConvertTime();
            setLayerOffAnimation(this.animationPlayer.getAnimation(), this.getEnabledPose(entitypatch, 1.0F), this.layerOffAnimation, convertTime);
            this.playAnimationInstant(this.layerOffAnimation, entitypatch);
        }
    }

    public static void setLayerOffAnimation(DynamicAnimation currentAnimation, Pose currentPose, LayerOffAnimation offAnimation, float convertTime) {
        offAnimation.setLastAnimation(currentAnimation.getRealAnimation());
        offAnimation.setLastPose(currentPose);
        offAnimation.setTotalTime(convertTime);
    }

    public String toString() {
        return (this.isBaseLayer() ? "" : this.priority) + (this.isBaseLayer() ? " Base Layer : " : " Composite Layer : ") + this.animationPlayer.getAnimation() + " " + this.animationPlayer.getElapsedTime();
    }

    public static class BaseLayer extends Layer {

        protected Map<Layer.Priority, Layer> compositeLayers = Maps.newLinkedHashMap();

        protected Layer.Priority baseLayerPriority;

        public BaseLayer(Layer.Priority priority) {
            super(priority);
            this.compositeLayers.computeIfAbsent(Layer.Priority.LOWEST, Layer::new);
            this.compositeLayers.computeIfAbsent(Layer.Priority.MIDDLE, Layer::new);
            this.compositeLayers.computeIfAbsent(Layer.Priority.HIGHEST, Layer::new);
            this.baseLayerPriority = Layer.Priority.LOWEST;
        }

        @Override
        public void playAnimation(StaticAnimation nextAnimation, LivingEntityPatch<?> entitypatch, float convertTimeModifier) {
            Layer.Priority priority = nextAnimation.getPriority();
            this.baseLayerPriority = priority;
            this.offCompositeLayerLowerThan(entitypatch, nextAnimation);
            super.playAnimation(nextAnimation, entitypatch, convertTimeModifier);
        }

        @Override
        protected void playLivingAnimation(StaticAnimation nextAnimation, LivingEntityPatch<?> entitypatch) {
            this.animationPlayer.getAnimation().end(entitypatch, nextAnimation, this.animationPlayer.isEnd());
            this.resume();
            nextAnimation.begin(entitypatch);
            if (!nextAnimation.isMetaAnimation()) {
                this.concurrentLinkAnimation.acceptFrom(this.animationPlayer.getAnimation().getRealAnimation(), nextAnimation, this.animationPlayer.getElapsedTime());
                this.concurrentLinkAnimation.putOnPlayer(this.animationPlayer);
                entitypatch.updateEntityState();
                this.nextAnimation = nextAnimation;
            }
        }

        @Override
        public void update(LivingEntityPatch<?> entitypatch) {
            super.update(entitypatch);
            for (Layer layer : this.compositeLayers.values()) {
                layer.update(entitypatch);
            }
        }

        public void offCompositeLayerLowerThan(LivingEntityPatch<?> entitypatch, StaticAnimation nextAnimation) {
            for (Layer.Priority p : nextAnimation.getPriority().lowerEquals()) {
                if (p != Layer.Priority.LOWEST || nextAnimation.isMainFrameAnimation()) {
                    ((Layer) this.compositeLayers.get(p)).off(entitypatch);
                }
            }
        }

        public void disableLayer(Layer.Priority priority) {
            Layer layer = (Layer) this.compositeLayers.get(priority);
            layer.disabled = true;
            Animations.DUMMY_ANIMATION.putOnPlayer(layer.animationPlayer);
        }

        public Layer getLayer(Layer.Priority priority) {
            return (Layer) this.compositeLayers.get(priority);
        }

        @Override
        public void off(LivingEntityPatch<?> entitypatch) {
        }

        @Override
        protected boolean isDisabled() {
            return false;
        }

        @Override
        protected boolean isBaseLayer() {
            return true;
        }
    }

    public static enum LayerType {

        BASE_LAYER, COMPOSITE_LAYER
    }

    public static enum Priority {

        LOWEST, MIDDLE, HIGHEST;

        public Layer.Priority[] lowers() {
            return (Layer.Priority[]) Arrays.copyOfRange(values(), 0, this.ordinal());
        }

        public Layer.Priority[] uppers() {
            return (Layer.Priority[]) Arrays.copyOfRange(values(), this == LOWEST ? this.ordinal() : this.ordinal() + 1, 3);
        }

        public Layer.Priority[] lowerEquals() {
            return (Layer.Priority[]) Arrays.copyOfRange(values(), 0, this.ordinal() + 1);
        }
    }
}