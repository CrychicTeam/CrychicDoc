package com.mna.rituals.effects;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public class RitualEffectUntamedWind extends RitualEffectCreateEssence {

    public RitualEffectUntamedWind(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        return context.getCenter().m_123342_() < 133 ? Component.translatable("ritual.mna.untamed_wind.too_low") : null;
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.AIR;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        int radius = context.getRecipe().getLowerBound();
        BlockPos start = context.getCenter().west(radius);
        for (int i = 0; i < 13; i++) {
            context.getLevel().addParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), (double) start.m_123341_(), (double) start.m_123342_() + Math.random() * 3.0, (double) (start.m_123343_() - radius) + Math.random() * (double) radius * 2.0, 0.5 + Math.random() * 0.5, 0.0, 0.0);
        }
        return true;
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_AIR.get());
    }
}