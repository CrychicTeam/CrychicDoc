package io.redspace.ironsspellbooks.entity.mobs.keeper;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.WalkAnimationState;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class KeeperModel extends AbstractSpellCastingMobModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/keeper/keeper.png");

    public static final ResourceLocation modelResource = new ResourceLocation("irons_spellbooks", "geo/citadel_keeper.geo.json");

    private int lastTick;

    private float legTween = 1.0F;

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return modelResource;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (!Minecraft.getInstance().isPaused()) {
            float partialTick = animationState.getPartialTick();
            CoreGeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
            CoreGeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
            CoreGeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
            CoreGeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
            CoreGeoBone body = this.getAnimationProcessor().getBone("body");
            boolean tick = this.lastTick != entity.f_19797_;
            this.lastTick = entity.f_19797_;
            WalkAnimationState walkAnimationState = entity.f_267362_;
            float pLimbSwingAmount = 0.0F;
            float pLimbSwing = 0.0F;
            if (entity.m_6084_()) {
                pLimbSwingAmount = walkAnimationState.speed(partialTick);
                pLimbSwing = walkAnimationState.position(partialTick);
                if (pLimbSwingAmount > 1.0F) {
                    pLimbSwingAmount = 1.0F;
                }
                if (entity.f_20916_ > 0) {
                    pLimbSwingAmount *= 0.25F;
                }
            }
            if (!entity.m_20159_() || !entity.m_20202_().shouldRiderSit()) {
                float strength = 0.75F;
                rightLeg.updatePosition(0.0F, Mth.cos(pLimbSwing * 0.6662F) * 4.0F * strength * pLimbSwingAmount, -Mth.sin(pLimbSwing * 0.6662F) * 4.0F * pLimbSwingAmount);
                leftLeg.updatePosition(0.0F, Mth.cos(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * strength * pLimbSwingAmount, -Mth.sin(pLimbSwing * 0.6662F - (float) Math.PI) * 4.0F * pLimbSwingAmount);
                body.updatePosition(0.0F, Mth.abs(Mth.cos((pLimbSwing * 1.2662F - (float) (Math.PI / 2)) * 0.5F)) * 2.0F * strength * pLimbSwingAmount, 0.0F);
                if (tick) {
                    if (entity.isAnimating() && !entity.shouldAlwaysAnimateLegs()) {
                        this.legTween = Mth.lerp(0.9F, 1.0F, 0.0F);
                    } else {
                        this.legTween = Mth.lerp(0.9F, 0.0F, 1.0F);
                    }
                }
                rightLeg.setRotX(Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * this.legTween * strength);
                leftLeg.setRotX(Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount * this.legTween * strength);
            }
        }
    }

    protected static void updatePosition(CoreGeoBone bone, float x, float y, float z) {
        bone.setPosX(x);
        bone.setPosY(y);
        bone.setPosZ(z);
    }

    protected static void updateRotation(CoreGeoBone bone, float x, float y, float z) {
        bone.setRotX(x);
        bone.setRotY(y);
        bone.setRotZ(z);
    }
}