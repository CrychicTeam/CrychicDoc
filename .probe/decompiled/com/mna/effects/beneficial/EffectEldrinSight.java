package com.mna.effects.beneficial;

import com.mna.ManaAndArtifice;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EffectEldrinSight extends EffectWellspringSight implements INoCreeperLingering {

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity.m_9236_().isClientSide() && entity == ManaAndArtifice.instance.proxy.getClientPlayer()) {
            Player player = ManaAndArtifice.instance.proxy.getClientPlayer();
            int durationRemaining = player.m_21124_(this).getDuration();
            player.m_9236_().getEntities(player, player.m_20191_().inflate(64.0), e -> e instanceof LivingEntity).forEach(e -> {
                boolean glow = ((LivingEntity) e).getEffect(MobEffects.GLOWING) != null;
                e.setSharedFlag(6, false);
                e.setGlowingTag(glow);
            });
            if (durationRemaining > 1) {
                player.m_9236_().getEntities(player, player.m_20191_().inflate(32.0), e -> e instanceof LivingEntity).forEach(e -> {
                    e.setSharedFlag(6, true);
                    e.setGlowingTag(true);
                });
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}