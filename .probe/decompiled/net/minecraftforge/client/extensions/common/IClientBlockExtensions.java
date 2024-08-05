package net.minecraftforge.client.extensions.common;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector3d;

public interface IClientBlockExtensions {

    IClientBlockExtensions DEFAULT = new IClientBlockExtensions() {
    };

    static IClientBlockExtensions of(BlockState state) {
        return of(state.m_60734_());
    }

    static IClientBlockExtensions of(Block block) {
        return block.getRenderPropertiesInternal() instanceof IClientBlockExtensions e ? e : DEFAULT;
    }

    default boolean addHitEffects(BlockState state, Level level, HitResult target, ParticleEngine manager) {
        return false;
    }

    default boolean addDestroyEffects(BlockState state, Level Level, BlockPos pos, ParticleEngine manager) {
        return !state.m_245147_();
    }

    default Vector3d getFogColor(BlockState state, LevelReader level, BlockPos pos, Entity entity, Vector3d originalColor, float partialTick) {
        FluidState fluidState = level.m_6425_(pos);
        if (fluidState.is(FluidTags.WATER)) {
            float f12 = 0.0F;
            if (entity instanceof LivingEntity ent) {
                f12 = (float) EnchantmentHelper.getRespiration(ent) * 0.2F;
                if (ent.hasEffect(MobEffects.WATER_BREATHING)) {
                    f12 = f12 * 0.3F + 0.6F;
                }
            }
            return new Vector3d((double) (0.02F + f12), (double) (0.02F + f12), (double) (0.2F + f12));
        } else {
            return fluidState.is(FluidTags.LAVA) ? new Vector3d(0.6F, 0.1F, 0.0) : originalColor;
        }
    }

    default boolean areBreakingParticlesTinted(BlockState state, ClientLevel level, BlockPos pos) {
        return !state.m_60713_(Blocks.GRASS_BLOCK);
    }
}