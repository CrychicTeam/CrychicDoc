package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class DeadKingModel extends AbstractSpellCastingMobModel {

    public static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation("irons_spellbooks", "textures/entity/dead_king/dead_king.png");

    public static final ResourceLocation TEXTURE_CORPSE = new ResourceLocation("irons_spellbooks", "textures/entity/dead_king/dead_king_resting.png");

    public static final ResourceLocation TEXTURE_ENRAGED = new ResourceLocation("irons_spellbooks", "textures/entity/dead_king/dead_king_enraged.png");

    public static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/dead_king.geo.json");

    @Override
    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        if (object instanceof DeadKingBoss boss) {
            return boss.isPhase(DeadKingBoss.Phases.FinalPhase) ? TEXTURE_ENRAGED : TEXTURE_NORMAL;
        } else {
            return TEXTURE_CORPSE;
        }
    }

    @Override
    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }

    @Override
    public void setCustomAnimations(AbstractSpellCastingMob entity, long instanceId, AnimationState<AbstractSpellCastingMob> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        CoreGeoBone jaw = this.getAnimationProcessor().getBone("jaw");
        CoreGeoBone hair1 = this.getAnimationProcessor().getBone("hair");
        CoreGeoBone hair2 = this.getAnimationProcessor().getBone("hair2");
        float f = (float) entity.f_19797_ + animationState.getPartialTick();
        if (jaw != null && hair1 != null && hair2 != null) {
            jaw.setRotX(Mth.sin(f * 0.05F) * 5.0F * (float) (Math.PI / 180.0));
            hair1.setRotX((Mth.sin(f * 0.1F) * 10.0F - 30.0F) * (float) (Math.PI / 180.0));
            hair2.setRotX(Mth.sin(f * 0.15F) * 15.0F * (float) (Math.PI / 180.0));
        }
    }
}