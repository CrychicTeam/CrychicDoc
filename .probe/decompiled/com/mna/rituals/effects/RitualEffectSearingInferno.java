package com.mna.rituals.effects;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.items.ItemInit;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class RitualEffectSearingInferno extends RitualEffectCreateEssence {

    public RitualEffectSearingInferno(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Holder<Biome> centerBiome = context.getLevel().m_204166_(context.getCenter());
        return centerBiome.isBound() && centerBiome.is(BiomeTags.IS_NETHER) ? null : Component.translatable("ritual.mna.searing_inferno.not_nether");
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.FIRE;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        Vec3 center = new Vec3((double) context.getCenter().m_123341_() + 0.5, (double) context.getCenter().m_123342_() + 0.1, (double) context.getCenter().m_123343_() + 0.5);
        for (float i = 0.0F; i < 360.0F; i += 3.0F) {
            double y = Math.random() * 5.0;
            double radius = 1.0 + 2.0 * Math.pow(0.2, y);
            context.getLevel().addParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get()), center.x, center.y + y, center.z, 0.1 + Math.random() * 0.1, 0.0 + Math.random() * 0.1, radius);
        }
        return true;
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_FIRE.get());
    }
}