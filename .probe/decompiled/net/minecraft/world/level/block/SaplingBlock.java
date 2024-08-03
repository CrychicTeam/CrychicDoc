package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SaplingBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    protected static final float AABB_OFFSET = 6.0F;

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);

    private final AbstractTreeGrower treeGrower;

    protected SaplingBlock(AbstractTreeGrower abstractTreeGrower0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.treeGrower = abstractTreeGrower0;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(STAGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_46803_(blockPos2.above()) >= 9 && randomSource3.nextInt(7) == 0) {
            this.advanceTree(serverLevel1, blockPos2, blockState0, randomSource3);
        }
    }

    public void advanceTree(ServerLevel serverLevel0, BlockPos blockPos1, BlockState blockState2, RandomSource randomSource3) {
        if ((Integer) blockState2.m_61143_(STAGE) == 0) {
            serverLevel0.m_7731_(blockPos1, (BlockState) blockState2.m_61122_(STAGE), 4);
        } else {
            this.treeGrower.growTree(serverLevel0, serverLevel0.getChunkSource().getGenerator(), blockPos1, blockState2, randomSource3);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return (double) level0.random.nextFloat() < 0.45;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.advanceTree(serverLevel0, blockPos2, blockState3, randomSource1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(STAGE);
    }
}