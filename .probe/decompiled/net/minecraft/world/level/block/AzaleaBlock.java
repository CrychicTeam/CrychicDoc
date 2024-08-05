package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.grower.AzaleaTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AzaleaBlock extends BushBlock implements BonemealableBlock {

    private static final AzaleaTreeGrower TREE_GROWER = new AzaleaTreeGrower();

    private static final VoxelShape SHAPE = Shapes.or(Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0), Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0));

    protected AzaleaBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60713_(Blocks.CLAY) || super.mayPlaceOn(blockState0, blockGetter1, blockPos2);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return levelReader0.m_6425_(blockPos1.above()).isEmpty();
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return (double) level0.random.nextFloat() < 0.45;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        TREE_GROWER.m_213817_(serverLevel0, serverLevel0.getChunkSource().getGenerator(), blockPos2, blockState3, randomSource1);
    }
}