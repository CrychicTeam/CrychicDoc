package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TorchflowerCropBlock extends CropBlock {

    public static final int MAX_AGE = 2;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    private static final float AABB_OFFSET = 3.0F;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0), Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0) };

    private static final int BONEMEAL_INCREASE = 1;

    public TorchflowerCropBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_AGE[this.m_52305_(blockState0)];
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return Items.TORCHFLOWER_SEEDS;
    }

    @Override
    public BlockState getStateForAge(int int0) {
        return int0 == 2 ? Blocks.TORCHFLOWER.defaultBlockState() : super.getStateForAge(int0);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(3) != 0) {
            super.randomTick(blockState0, serverLevel1, blockPos2, randomSource3);
        }
    }

    @Override
    protected int getBonemealAgeIncrease(Level level0) {
        return 1;
    }
}