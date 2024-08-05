package io.redspace.ironsspellbooks.entity.mobs.wizards.alchemist;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class ApothecaristModel extends AbstractSpellCastingMobModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/apothecarist.png");

    public static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/piglin_casting_mob.geo.json");

    private static final float tilt = (float) (Math.PI / 18);

    private static final Vector3f forward = new Vector3f(0.0F, 0.0F, Mth.sin((float) (Math.PI / 18)) * -12.0F);

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        float partialTick = animationState.getPartialTick();
        CoreGeoBone leftEar = this.getAnimationProcessor().getBone("left_ear");
        CoreGeoBone rightEar = this.getAnimationProcessor().getBone("right_ear");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone body = this.getAnimationProcessor().getBone("body");
        CoreGeoBone torso = this.getAnimationProcessor().getBone("torso");
        CoreGeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        CoreGeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
        this.transformStack.pushPosition(head, forward);
        this.transformStack.pushPosition(rightArm, forward);
        this.transformStack.pushPosition(leftArm, forward);
        this.transformStack.pushPosition(torso, forward);
        this.transformStack.pushRotation(torso, (float) (-Math.PI / 18), 0.0F, 0.0F);
        this.transformStack.pushPosition(rightLeg, forward);
        this.transformStack.pushPosition(leftLeg, new Vector3f(0.0F, 0.0F, 1.0F));
        if (entity.f_20913_ > 0) {
            float rot = Mth.lerp(((float) entity.f_20913_ - partialTick) / 10.0F, 0.0F, (float) Math.PI);
            this.transformStack.pushRotation(rightArm, rot, 0.0F, 0.0F);
        }
        if (leftEar != null && rightEar != null) {
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
            float r = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f;
            r *= 0.3F;
            r += 0.25132743F;
            this.transformStack.pushRotation(leftEar, 0.0F, 0.0F, -r);
            this.transformStack.pushRotation(rightEar, 0.0F, 0.0F, r);
        }
        super.setCustomAnimations(entity, instanceId, animationState);
    }
}