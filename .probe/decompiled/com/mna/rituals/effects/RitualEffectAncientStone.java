package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.api.sound.SFX;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RitualEffectAncientStone extends RitualEffectCreateEssence {

    public RitualEffectAncientStone(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        return context.getCenter().m_123342_() > 0 ? Component.translatable("ritual.mna.ancient_stone.too_high") : null;
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.EARTH_RUMBLE;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        for (RitualBlockPos pos : context.getAllPositions()) {
            if (pos != null) {
                BlockPos cur = pos.getBlockPos();
                BlockState state = context.getLevel().getBlockState(cur.below());
                context.getLevel().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double) cur.m_123341_() + Math.random(), (double) cur.m_123342_(), (double) cur.m_123343_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
            }
        }
        return true;
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_EARTH.get());
    }
}