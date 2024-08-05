package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.items.ItemInit;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;

public class RitualEffectDeepOcean extends RitualEffectCreateEssence {

    public RitualEffectDeepOcean(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        Holder<Biome> centerBiome = context.getLevel().m_204166_(context.getCenter());
        return centerBiome.isBound() && centerBiome.is(BiomeTags.IS_OCEAN) ? null : Component.translatable("ritual.mna.deep_ocean.not_ocean");
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.WATER;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        Vec3 center = new Vec3((double) context.getCenter().m_123341_() + 0.5, (double) (context.getCenter().m_123342_() + 3), (double) context.getCenter().m_123343_() + 0.5);
        double radius = (double) context.getRecipe().getLowerBound();
        for (int i = 0; i < 40; i++) {
            context.getLevel().addParticle(ParticleTypes.FALLING_WATER, center.x - radius + Math.random() * radius * 2.0, center.y, center.z - radius + Math.random() * radius * 2.0, 0.0, 0.0, 0.0);
        }
        return true;
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_WATER.get());
    }
}