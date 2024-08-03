package com.mna.effects.beneficial;

import com.mna.api.capabilities.resource.ICastingResource;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EffectInstantMana extends MobEffect {

    public EffectInstantMana(int liquidColorIn) {
        super(MobEffectCategory.BENEFICIAL, liquidColorIn);
    }

    @Override
    public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        if (entityLivingBaseIn instanceof Player) {
            entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                ICastingResource cr = m.getCastingResource();
                if (cr.artificialRestore()) {
                    float restored = (float) (amplifier + 1) * 0.2F * cr.getMaxAmount();
                    cr.restore(restored);
                }
            });
        }
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }
}