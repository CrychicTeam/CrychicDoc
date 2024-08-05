package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpinAttackModel extends GeoModel<AbstractSpellCastingMob> {

    private static final ResourceLocation FIRE_TEXTURE = new ResourceLocation("irons_spellbooks", "textures/entity/fire_riptide.png");

    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("textures/entity/trident_riptide.png");

    private static final ResourceLocation MODEL = new ResourceLocation("irons_spellbooks", "geo/spin_attack_model.geo.json");

    public ResourceLocation getTextureResource(AbstractSpellCastingMob object) {
        SpinAttackType spinAttackType = ClientMagicData.getSyncedSpellData(object).getSpinAttackType();
        return switch(spinAttackType) {
            case FIRE ->
                FIRE_TEXTURE;
            default ->
                DEFAULT_TEXTURE;
        };
    }

    public ResourceLocation getAnimationResource(AbstractSpellCastingMob animatable) {
        return null;
    }

    public ResourceLocation getModelResource(AbstractSpellCastingMob object) {
        return MODEL;
    }
}