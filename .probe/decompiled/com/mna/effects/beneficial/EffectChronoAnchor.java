package com.mna.effects.beneficial;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EffectChronoAnchor extends MobEffect implements INoCreeperLingering {

    public EffectChronoAnchor() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Player) {
            IPlayerMagic magic = (IPlayerMagic) entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            if (magic != null) {
                magic.getChronoAnchorData().fromPlayer((Player) entityLivingBaseIn);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        if (entityLivingBaseIn instanceof Player) {
            entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.getChronoAnchorData().revert((Player) entityLivingBaseIn);
                m.setNeedsChronoExhaustion();
            });
        }
    }
}