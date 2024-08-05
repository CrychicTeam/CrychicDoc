package com.mna.effects.harmful;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EffectManaBurn extends MobEffect {

    public EffectManaBurn() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        if (entityLivingBaseIn instanceof Player) {
            entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                float mana = m.getCastingResource().getAmount();
                m.getCastingResource().consume(entityLivingBaseIn, (float) (amplifier * 25));
                float newMana = m.getCastingResource().getAmount();
                float delta = mana - newMana;
                entityLivingBaseIn.hurt(entityLivingBaseIn.m_269291_().indirectMagic(source, indirectSource), delta / 25.0F);
            });
        }
    }
}