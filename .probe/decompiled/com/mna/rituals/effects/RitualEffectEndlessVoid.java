package com.mna.rituals.effects;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.items.ItemInit;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RitualEffectEndlessVoid extends RitualEffectCreateEssence {

    public RitualEffectEndlessVoid(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        BlockState state = context.getLevel().getBlockState(context.getCenter());
        boolean semiActiveRitual = false;
        if (state.m_60734_() == BlockInit.CHALK_RUNE.get()) {
            semiActiveRitual = (Boolean) state.m_61143_(ChalkRuneBlock.ACTIVATED);
        }
        for (int i = 0; i < context.getRecipe().getLowerBound(); i++) {
            for (int j = 0; j < context.getRecipe().getLowerBound(); j++) {
                int lightLevel = context.getLevel().m_46803_(context.getCenter().offset(-i, 0, j));
                if (semiActiveRitual) {
                    lightLevel -= 6;
                }
                if (lightLevel > 0) {
                    return Component.translatable("ritual.mna.endless_void.too_bright");
                }
            }
        }
        return null;
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.ENDER;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        Vec3 center = new Vec3((double) context.getCenter().m_123341_() + 0.5, (double) context.getCenter().m_123342_() + 0.1, (double) context.getCenter().m_123343_() + 0.5);
        double radius = (double) context.getRecipe().getLowerBound();
        for (float i = 0.0F; i < 360.0F; i += 15.0F) {
            double angleR = Math.toRadians((double) i);
            double offsetX = Math.cos(angleR) * radius;
            double offsetZ = Math.sin(angleR) * radius;
            Vec3 start = center.add(offsetX, 0.0, offsetZ);
            context.getLevel().addParticle(new MAParticleType(ParticleInit.ENDER.get()), start.x, start.y, start.z, center.x, center.y, center.z);
        }
        return true;
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_ENDER.get());
    }
}