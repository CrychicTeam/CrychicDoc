package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.state.BoneSnapshot;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class AbstractSpellCastingMobModel extends DefaultedEntityGeoModel<AbstractSpellCastingMob> {

    protected TransformStack transformStack = new TransformStack();

    BoneSnapshot testsnapshot1;

    BoneSnapshot testsnapshot2;

    public AbstractSpellCastingMobModel() {
        super(IronsSpellbooks.id("spellcastingmob"));
    }

    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return AbstractSpellCastingMob.modelResource;
    }

    public abstract ResourceLocation getTextureResource(AbstractSpellCastingMob var1);

    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return AbstractSpellCastingMob.animationInstantCast;
    }

    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (!Minecraft.getInstance().isPaused() && entity.shouldBeExtraAnimated()) {
            float partialTick = animationState.getPartialTick();
            CoreGeoBone head = this.getAnimationProcessor().getBone("head");
            CoreGeoBone body = this.getAnimationProcessor().getBone("body");
            CoreGeoBone torso = this.getAnimationProcessor().getBone("torso");
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
            if (!entity.isAnimating() || entity.shouldAlwaysAnimateHead()) {
                this.transformStack.pushRotation(head, Mth.lerp(partialTick, -entity.f_19860_, -entity.m_146909_()) * (float) (Math.PI / 180.0), Mth.lerp(partialTick, Mth.wrapDegrees(-entity.f_20886_ + entity.f_20884_) * (float) (Math.PI / 180.0), Mth.wrapDegrees(-entity.f_20885_ + entity.f_20883_) * (float) (Math.PI / 180.0)), 0.0F);
            }
            WalkAnimationState walkAnimationState = entity.f_267362_;
            float pLimbSwingAmount = 0.0F;
            float pLimbSwing = 0.0F;
            if (entity.m_6084_()) {
                pLimbSwingAmount = walkAnimationState.speed(partialTick);
                pLimbSwing = walkAnimationState.position(partialTick);
                if (entity.m_6162_()) {
                    pLimbSwing *= 3.0F;
                }
                if (pLimbSwingAmount > 1.0F) {
                    pLimbSwingAmount = 1.0F;
                }
            }
            float f = 1.0F;
            if (entity.m_21256_() > 4) {
                f = (float) entity.m_20184_().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }
            if (f < 1.0F) {
                f = 1.0F;
            }
            if (entity.m_20159_() && entity.m_20202_().shouldRiderSit()) {
                this.transformStack.pushRotation(rightLeg, 1.4137167F, (float) (-Math.PI / 10), -0.07853982F);
                this.transformStack.pushRotation(leftLeg, 1.4137167F, (float) (Math.PI / 10), 0.07853982F);
            } else if (!entity.isAnimating() || entity.shouldAlwaysAnimateLegs()) {
                float strength = 0.75F;
                Vec3 facing = entity.m_20156_().multiply(1.0, 0.0, 1.0).normalize();
                Vec3 momentum = entity.m_20184_().multiply(1.0, 0.0, 1.0).normalize();
                Vec3 facingOrth = new Vec3(-facing.z, 0.0, facing.x);
                float directionForward = (float) facing.dot(momentum);
                float directionSide = (float) facingOrth.dot(momentum) * 0.35F;
                float rightLateral = -Mth.sin(pLimbSwing * 0.6662F) * 4.0F * pLimbSwingAmount;
                float leftLateral = -Mth.sin(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * pLimbSwingAmount;
                this.transformStack.pushPosition(rightLeg, rightLateral * directionSide, Mth.cos(pLimbSwing * 0.6662F) * 4.0F * strength * pLimbSwingAmount, rightLateral * directionForward);
                this.transformStack.pushRotation(rightLeg, Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * strength, 0.0F, 0.0F);
                this.transformStack.pushPosition(leftLeg, leftLateral * directionSide, Mth.cos(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * strength * pLimbSwingAmount, leftLateral * directionForward);
                this.transformStack.pushRotation(leftLeg, Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount * strength, 0.0F, 0.0F);
                if (entity.bobBodyWhileWalking()) {
                    this.transformStack.pushPosition(body, 0.0F, Mth.abs(Mth.cos((pLimbSwing * 1.2662F - (float) (Math.PI / 2)) * 0.5F)) * 2.0F * strength * pLimbSwingAmount, 0.0F);
                }
            }
            if (!entity.isAnimating()) {
                this.transformStack.pushRotationWithBase(rightArm, Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f, 0.0F, 0.0F);
                this.transformStack.pushRotationWithBase(leftArm, Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f, 0.0F, 0.0F);
                this.bobBone(rightArm, entity.f_19797_, 1.0F);
                this.bobBone(leftArm, entity.f_19797_, -1.0F);
                if (entity.isDrinkingPotion()) {
                    this.transformStack.pushRotation(entity.m_21526_() ? leftArm : rightArm, 0.61086524F, (float) (entity.m_21526_() ? -25 : 25) * (float) (Math.PI / 180.0), (float) (entity.m_21526_() ? 15 : -15) * (float) (Math.PI / 180.0));
                }
            } else if (entity.shouldPointArmsWhileCasting() && entity.isCasting()) {
                if (this.testsnapshot1 == null) {
                    IronsSpellbooks.LOGGER.debug("setting bone snapshot");
                    this.testsnapshot1 = rightArm.saveSnapshot();
                    this.testsnapshot2 = leftArm.saveSnapshot();
                }
                this.transformStack.pushRotation(rightArm, -entity.m_146909_() * (float) (Math.PI / 180.0) + this.testsnapshot1.getRotX(), this.testsnapshot1.getRotY(), this.testsnapshot1.getRotZ());
                this.transformStack.pushRotation(leftArm, -entity.m_146909_() * (float) (Math.PI / 180.0) + this.testsnapshot2.getRotX(), this.testsnapshot2.getRotY(), this.testsnapshot2.getRotZ());
            } else if (this.testsnapshot1 != null) {
                IronsSpellbooks.LOGGER.debug("removing bone snapshot");
                this.testsnapshot1 = null;
                this.testsnapshot2 = null;
            }
            this.transformStack.popStack();
        }
    }

    protected void bobBone(CoreGeoBone bone, int offset, float multiplier) {
        float z = multiplier * (Mth.cos((float) offset * 0.09F) * 0.05F + 0.05F);
        float x = multiplier * Mth.sin((float) offset * 0.067F) * 0.05F;
        this.transformStack.pushRotation(bone, x, 0.0F, z);
    }
}