package com.mna.effects.harmful;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EffectHeatwave extends SimpleHarmfulEffect implements INoCreeperLingering {

    public static final String ID_MANA_REGEN_SLOW = "705bf838-ed4b-4d88-8a27-c3ac20ecae82";

    @Override
    public void addAttributeModifiers(LivingEntity living, AttributeMap attrs, int amp) {
        if (living instanceof Player) {
            living.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addRegenerationModifier("705bf838-ed4b-4d88-8a27-c3ac20ecae82", 1.0F - 0.1F * (float) (amp + 1)));
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap attrs, int amp) {
        if (living instanceof Player) {
            living.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeRegenerationModifier("705bf838-ed4b-4d88-8a27-c3ac20ecae82"));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player) {
            ((Player) pLivingEntity).causeFoodExhaustion(0.005F * (float) (pAmplifier + 1));
        }
    }
}