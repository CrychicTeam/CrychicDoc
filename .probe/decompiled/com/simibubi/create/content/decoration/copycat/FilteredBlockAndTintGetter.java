package com.simibubi.create.content.decoration.copycat;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.data.ModelDataManager;
import org.jetbrains.annotations.Nullable;

public class FilteredBlockAndTintGetter implements BlockAndTintGetter {

    private BlockAndTintGetter wrapped;

    private Predicate<BlockPos> filter;

    public FilteredBlockAndTintGetter(BlockAndTintGetter wrapped, Predicate<BlockPos> filter) {
        this.wrapped = wrapped;
        this.filter = filter;
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.m_7702_(pPos) : null;
    }

    @Override
    public BlockState getBlockState(BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.m_8055_(pPos) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public FluidState getFluidState(BlockPos pPos) {
        return this.filter.test(pPos) ? this.wrapped.m_6425_(pPos) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return this.wrapped.m_141928_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.wrapped.m_141937_();
    }

    @Override
    public float getShade(Direction pDirection, boolean pShade) {
        return this.wrapped.getShade(pDirection, pShade);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.wrapped.getLightEngine();
    }

    @Override
    public int getBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver) {
        return this.wrapped.getBlockTint(pBlockPos, pColorResolver);
    }

    @Nullable
    public ModelDataManager getModelDataManager() {
        return this.wrapped.getModelDataManager();
    }
}