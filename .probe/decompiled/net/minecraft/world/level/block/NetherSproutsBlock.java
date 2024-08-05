package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetherSproutsBlock extends BushBlock {

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0);

    public NetherSproutsBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_204336_(BlockTags.NYLIUM) || blockState0.m_60713_(Blocks.SOUL_SOIL) || super.mayPlaceOn(blockState0, blockGetter1, blockPos2);
    }
}