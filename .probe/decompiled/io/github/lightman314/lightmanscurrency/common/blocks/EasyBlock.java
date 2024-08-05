package io.github.lightman314.lightmanscurrency.common.blocks;

import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EasyBlock extends Block {

    public EasyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected boolean isBlockOpaque(@Nonnull BlockState state) {
        return this.isBlockOpaque();
    }

    protected boolean isBlockOpaque() {
        return true;
    }

    @Nonnull
    @Override
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos) {
        return this.isBlockOpaque(state) ? super.m_7952_(state, level, pos) : Shapes.empty();
    }
}