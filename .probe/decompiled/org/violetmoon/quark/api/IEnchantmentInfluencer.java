package org.violetmoon.quark.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface IEnchantmentInfluencer {

    @Nullable
    float[] getEnchantmentInfluenceColor(BlockGetter var1, BlockPos var2, BlockState var3);

    default int getInfluenceStack(BlockGetter world, BlockPos pos, BlockState state) {
        return 1;
    }

    @Nullable
    default ParticleOptions getExtraParticleOptions(BlockGetter world, BlockPos pos, BlockState state) {
        return null;
    }

    default double getExtraParticleChance(BlockGetter world, BlockPos pos, BlockState state) {
        return 1.0;
    }

    boolean influencesEnchantment(BlockGetter var1, BlockPos var2, BlockState var3, Enchantment var4);

    boolean dampensEnchantment(BlockGetter var1, BlockPos var2, BlockState var3, Enchantment var4);
}