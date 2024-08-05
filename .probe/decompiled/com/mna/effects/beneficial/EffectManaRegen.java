package com.mna.effects.beneficial;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectManaRegen extends MobEffect implements INoCreeperLingering {

    public static final float PCT_REDUCTION_PER_LEVEL = 0.05F;

    public static final String IDENTIFIER = "d56fa65a-b26b-43ba-9bb2-ab12f3e7ab9e";

    public EffectManaRegen() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addRegenerationModifier("d56fa65a-b26b-43ba-9bb2-ab12f3e7ab9e", -0.05F * (float) (amplifier + 1)));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeRegenerationModifier("d56fa65a-b26b-43ba-9bb2-ab12f3e7ab9e"));
    }
}