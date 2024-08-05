package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectWellspringSight extends MobEffect implements INoCreeperLingering {

    public EffectWellspringSight() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.m_9236_().isClientSide() && entity instanceof ServerPlayer && entity.m_9236_().getGameTime() % 100L == 0L) {
            ServerMessageDispatcher.sendWellspringSyncMessage((ServerLevel) entity.m_9236_(), (ServerPlayer) entity);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}