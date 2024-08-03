package yesman.epicfight.api.animation.types;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class AimAnimation extends StaticAnimation {

    public StaticAnimation lookUp;

    public StaticAnimation lookDown;

    public StaticAnimation lying;

    public AimAnimation(float convertTime, boolean repeatPlay, String path1, String path2, String path3, String path4, Armature armature) {
        super(convertTime, repeatPlay, path1, armature);
        this.lookUp = new StaticAnimation(convertTime, repeatPlay, path2, armature, true);
        this.lookDown = new StaticAnimation(convertTime, repeatPlay, path3, armature, true);
        this.lying = new StaticAnimation(convertTime, repeatPlay, path4, armature, true);
    }

    public AimAnimation(boolean repeatPlay, String path1, String path2, String path3, String path4, Armature armature) {
        this(0.15F, repeatPlay, path1, path2, path3, path4, armature);
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        super.tick(entitypatch);
        ClientAnimator animator = entitypatch.getClientAnimator();
        Layer layer = animator.getCompositeLayer(this.getPriority());
        AnimationPlayer player = layer.animationPlayer;
        if (player.getElapsedTime() >= this.totalTime - 0.06F) {
            layer.pause();
        }
    }

    @Override
    public Pose getPoseByTime(LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
        if (!entitypatch.isFirstPerson()) {
            LivingMotion livingMotion = entitypatch.getCurrentLivingMotion();
            if (livingMotion != LivingMotions.SWIM && livingMotion != LivingMotions.FLY && livingMotion != LivingMotions.CREATIVE_FLY) {
                float pitch = entitypatch.getOriginal().m_5686_(Minecraft.getInstance().getFrameTime());
                StaticAnimation interpolateAnimation = pitch > 0.0F ? this.lookDown : this.lookUp;
                Pose pose1 = super.getPoseByTime(entitypatch, time, partialTicks);
                Pose pose2 = interpolateAnimation.getPoseByTime(entitypatch, time, partialTicks);
                this.modifyPose(this, pose2, entitypatch, time, partialTicks);
                return Pose.interpolatePose(pose1, pose2, Math.abs(pitch) / 90.0F);
            } else {
                Pose pose = this.lying.getPoseByTime(entitypatch, time, partialTicks);
                this.modifyPose(this, pose, entitypatch, time, partialTicks);
                return pose;
            }
        } else {
            return super.getPoseByTime(entitypatch, time, partialTicks);
        }
    }

    @Override
    public void modifyPose(DynamicAnimation animation, Pose pose, LivingEntityPatch<?> entitypatch, float time, float partialTicks) {
        if (!entitypatch.isFirstPerson()) {
            JointTransform chest = pose.getOrDefaultTransform("Chest");
            JointTransform head = pose.getOrDefaultTransform("Head");
            float f = 90.0F;
            float ratio = (f - Math.abs(entitypatch.getOriginal().m_146909_())) / f;
            float yawOffset = entitypatch.getOriginal().m_20202_() != null ? entitypatch.getOriginal().getYHeadRot() : entitypatch.getOriginal().yBodyRot;
            MathUtils.mulQuaternion(QuaternionUtils.YP.rotationDegrees(Mth.wrapDegrees(yawOffset - entitypatch.getOriginal().getYHeadRot()) * ratio), head.rotation(), head.rotation());
            chest.frontResult(JointTransform.getRotation(QuaternionUtils.YP.rotationDegrees(Mth.wrapDegrees(entitypatch.getOriginal().getYHeadRot() - yawOffset) * ratio)), OpenMatrix4f::mulAsOriginFront);
        }
    }

    @Override
    public <V> StaticAnimation addProperty(AnimationProperty.StaticAnimationProperty<V> propertyType, V value) {
        super.addProperty(propertyType, value);
        this.lookDown.addProperty(propertyType, value);
        this.lookUp.addProperty(propertyType, value);
        this.lying.addProperty(propertyType, value);
        return this;
    }

    @Override
    public void loadAnimation(ResourceManager resourceManager) {
        load(resourceManager, this);
        load(resourceManager, this.lookUp);
        load(resourceManager, this.lookDown);
        load(resourceManager, this.lying);
    }

    @Override
    public boolean isClientAnimation() {
        return true;
    }
}