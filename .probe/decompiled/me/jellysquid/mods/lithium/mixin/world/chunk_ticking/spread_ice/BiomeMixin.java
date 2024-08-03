package me.jellysquid.mods.lithium.mixin.world.chunk_ticking.spread_ice;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Biome.class })
public class BiomeMixin {

    @Redirect(method = { "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"))
    private FluidState getNull(LevelReader instance, BlockPos blockPos) {
        return null;
    }

    @Redirect(method = { "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;getFluid()Lnet/minecraft/fluid/Fluid;"))
    private Fluid skipFluidCheck(FluidState fluidState) {
        return Fluids.WATER;
    }

    @Redirect(method = { "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
    private Block fluidCheckAndGetBlock(BlockState blockState) {
        return blockState.m_60819_().getType() == Fluids.WATER ? blockState.m_60734_() : null;
    }
}