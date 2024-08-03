package snownee.kiwi.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class WrappedBlockGetter implements BlockAndTintGetter {

    protected BlockAndTintGetter delegate;

    public void setLevel(BlockAndTintGetter level) {
        this.delegate = level;
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.delegate.m_7702_(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos p_180495_1_) {
        return this.delegate.m_8055_(p_180495_1_);
    }

    @Override
    public FluidState getFluidState(BlockPos p_204610_1_) {
        return this.delegate.m_6425_(p_204610_1_);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getShade(Direction p_230487_1_, boolean p_230487_2_) {
        return this.delegate.getShade(p_230487_1_, p_230487_2_);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.delegate.getLightEngine();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver) {
        return this.delegate.getBlockTint(pos, colorResolver);
    }

    @Override
    public int getHeight() {
        return this.delegate.m_141928_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.delegate.m_141937_();
    }
}