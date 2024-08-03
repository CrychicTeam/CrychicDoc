package lio.playeranimatorapi.mixin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.lio.shadowed.eliotlash.mclib.utils.Interpolations;
import net.liopyu.liolib.core.animatable.GeoAnimatable;
import net.liopyu.liolib.core.animatable.model.CoreGeoBone;
import net.liopyu.liolib.core.animatable.model.CoreGeoModel;
import net.liopyu.liolib.core.animation.AnimatableManager;
import net.liopyu.liolib.core.animation.AnimationController;
import net.liopyu.liolib.core.animation.AnimationProcessor;
import net.liopyu.liolib.core.animation.AnimationState;
import net.liopyu.liolib.core.animation.EasingType;
import net.liopyu.liolib.core.keyframe.AnimationPoint;
import net.liopyu.liolib.core.keyframe.BoneAnimationQueue;
import net.liopyu.liolib.core.state.BoneSnapshot;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AnimationProcessor.class })
public abstract class AnimationProcessorMixin_LioLibOnly<T extends GeoAnimatable> {

    @Shadow(remap = false)
    public boolean reloadAnimations;

    @Shadow(remap = false)
    @Final
    private Map<String, CoreGeoBone> bones;

    @Shadow(remap = false)
    protected abstract Map<String, BoneSnapshot> updateBoneSnapshots(Map<String, BoneSnapshot> var1);

    @Shadow(remap = false)
    protected abstract void resetBoneTransformationMarkers();

    @Shadow(remap = false)
    public abstract Collection<CoreGeoBone> getRegisteredBones();

    @Inject(method = { "tickAnimation" }, at = { @At("HEAD") }, cancellable = true, remap = false)
    private void inject(T animatable, CoreGeoModel<T> model, AnimatableManager<T> animatableManager, double animTime, AnimationState<T> event, boolean crashWhenCantFindBone, CallbackInfo ci) {
        if (animatable instanceof AbstractClientPlayer) {
            List<CoreGeoBone> disabledBones = new ArrayList();
            Map<String, BoneSnapshot> boneSnapshots = this.updateBoneSnapshots(animatableManager.getBoneSnapshotCollection());
            Iterator var9 = animatableManager.getAnimationControllers().values().iterator();
            addDisabled(disabledBones, "body", model);
            addDisabled(disabledBones, "head", model);
            addDisabled(disabledBones, "torso", model);
            addDisabled(disabledBones, "right_arm", model);
            addDisabled(disabledBones, "left_arm", model);
            addDisabled(disabledBones, "rightItem", model);
            addDisabled(disabledBones, "leftItem", model);
            addDisabled(disabledBones, "right_leg", model);
            addDisabled(disabledBones, "left_leg", model);
            while (var9.hasNext()) {
                AnimationController<T> controller = (AnimationController<T>) var9.next();
                if (this.reloadAnimations) {
                    controller.forceAnimationReset();
                    controller.getBoneAnimationQueues().clear();
                }
                ((AnimationControllerAccessor_LioLibOnly) controller).setIsJustStarting(animatableManager.isFirstTick());
                event.withController(controller);
                controller.process(model, event, this.bones, boneSnapshots, animTime, crashWhenCantFindBone);
                for (BoneAnimationQueue boneAnimation : controller.getBoneAnimationQueues().values()) {
                    CoreGeoBone bone = boneAnimation.bone();
                    if (!disabledBones.contains(bone)) {
                        BoneSnapshot saveSnapshot = (BoneSnapshot) boneSnapshots.get(bone.getName());
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
                        EasingType easingType = (EasingType) ((AnimationControllerAccessor_LioLibOnly) controller).getOverrideEasingTypeFunction().apply(animatable);
                        if (rotXPoint != null && rotYPoint != null && rotZPoint != null) {
                            bone.setRotX((float) EasingType.lerpWithOverride(rotXPoint, easingType) + initialSnapshot.getRotX());
                            bone.setRotY((float) EasingType.lerpWithOverride(rotYPoint, easingType) + initialSnapshot.getRotY());
                            bone.setRotZ((float) EasingType.lerpWithOverride(rotZPoint, easingType) + initialSnapshot.getRotZ());
                            saveSnapshot.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                            saveSnapshot.startRotAnim();
                            bone.markRotationAsChanged();
                        }
                        if (posXPoint != null && posYPoint != null && posZPoint != null) {
                            bone.setPosX((float) EasingType.lerpWithOverride(posXPoint, easingType));
                            bone.setPosY((float) EasingType.lerpWithOverride(posYPoint, easingType));
                            bone.setPosZ((float) EasingType.lerpWithOverride(posZPoint, easingType));
                            saveSnapshot.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                            saveSnapshot.startPosAnim();
                            bone.markPositionAsChanged();
                        }
                        if (scaleXPoint != null && scaleYPoint != null && scaleZPoint != null) {
                            bone.setScaleX((float) EasingType.lerpWithOverride(scaleXPoint, easingType));
                            bone.setScaleY((float) EasingType.lerpWithOverride(scaleYPoint, easingType));
                            bone.setScaleZ((float) EasingType.lerpWithOverride(scaleZPoint, easingType));
                            saveSnapshot.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                            saveSnapshot.startScaleAnim();
                            bone.markScaleAsChanged();
                        }
                    }
                }
            }
            this.reloadAnimations = false;
            double resetTickLength = animatable.getBoneResetTime();
            for (CoreGeoBone bone : this.getRegisteredBones()) {
                if (!disabledBones.contains(bone)) {
                    if (!bone.hasRotationChanged()) {
                        BoneSnapshot initialSnapshotx = bone.getInitialSnapshot();
                        BoneSnapshot saveSnapshotx = (BoneSnapshot) boneSnapshots.get(bone.getName());
                        if (saveSnapshotx.isRotAnimInProgress()) {
                            saveSnapshotx.stopRotAnim(animTime);
                        }
                        double percentageReset = Math.min((animTime - saveSnapshotx.getLastResetRotationTick()) / resetTickLength, 1.0);
                        bone.setRotX((float) Interpolations.lerp((double) saveSnapshotx.getRotX(), (double) initialSnapshotx.getRotX(), percentageReset));
                        bone.setRotY((float) Interpolations.lerp((double) saveSnapshotx.getRotY(), (double) initialSnapshotx.getRotY(), percentageReset));
                        bone.setRotZ((float) Interpolations.lerp((double) saveSnapshotx.getRotZ(), (double) initialSnapshotx.getRotZ(), percentageReset));
                        if (percentageReset >= 1.0) {
                            saveSnapshotx.updateRotation(bone.getRotX(), bone.getRotY(), bone.getRotZ());
                        }
                    }
                    if (!bone.hasPositionChanged()) {
                        BoneSnapshot initialSnapshotxx = bone.getInitialSnapshot();
                        BoneSnapshot saveSnapshotxx = (BoneSnapshot) boneSnapshots.get(bone.getName());
                        if (saveSnapshotxx.isPosAnimInProgress()) {
                            saveSnapshotxx.stopPosAnim(animTime);
                        }
                        double percentageReset = Math.min((animTime - saveSnapshotxx.getLastResetPositionTick()) / resetTickLength, 1.0);
                        bone.setPosX((float) Interpolations.lerp((double) saveSnapshotxx.getOffsetX(), (double) initialSnapshotxx.getOffsetX(), percentageReset));
                        bone.setPosY((float) Interpolations.lerp((double) saveSnapshotxx.getOffsetY(), (double) initialSnapshotxx.getOffsetY(), percentageReset));
                        bone.setPosZ((float) Interpolations.lerp((double) saveSnapshotxx.getOffsetZ(), (double) initialSnapshotxx.getOffsetZ(), percentageReset));
                        if (percentageReset >= 1.0) {
                            saveSnapshotxx.updateOffset(bone.getPosX(), bone.getPosY(), bone.getPosZ());
                        }
                    }
                    if (!bone.hasScaleChanged()) {
                        BoneSnapshot initialSnapshotxxx = bone.getInitialSnapshot();
                        BoneSnapshot saveSnapshotxxx = (BoneSnapshot) boneSnapshots.get(bone.getName());
                        if (saveSnapshotxxx.isScaleAnimInProgress()) {
                            saveSnapshotxxx.stopScaleAnim(animTime);
                        }
                        double percentageReset = Math.min((animTime - saveSnapshotxxx.getLastResetScaleTick()) / resetTickLength, 1.0);
                        bone.setScaleX((float) Interpolations.lerp((double) saveSnapshotxxx.getScaleX(), (double) initialSnapshotxxx.getScaleX(), percentageReset));
                        bone.setScaleY((float) Interpolations.lerp((double) saveSnapshotxxx.getScaleY(), (double) initialSnapshotxxx.getScaleY(), percentageReset));
                        bone.setScaleZ((float) Interpolations.lerp((double) saveSnapshotxxx.getScaleZ(), (double) initialSnapshotxxx.getScaleZ(), percentageReset));
                        if (percentageReset >= 1.0) {
                            saveSnapshotxxx.updateScale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());
                        }
                    }
                }
            }
            this.resetBoneTransformationMarkers();
            ((AnimatableManagerAccessor_LioLibOnly) animatableManager).callFinishFirstTick();
            ci.cancel();
        }
    }

    @Unique
    private static void addDisabled(List<CoreGeoBone> list, String bone, CoreGeoModel model) {
        if (model.getBone(bone).isPresent()) {
            list.add((CoreGeoBone) model.getBone(bone).get());
        }
    }
}