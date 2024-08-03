package net.minecraft.world.level.block;

import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeavesBlock extends Block implements SimpleWaterloggedBlock {

    public static final int DECAY_DISTANCE = 7;

    public static final IntegerProperty DISTANCE = BlockStateProperties.DISTANCE;

    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final int TICK_DELAY = 1;

    public LeavesBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(DISTANCE, 7)).m_61124_(PERSISTENT, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(DISTANCE) == 7 && !(Boolean) blockState0.m_61143_(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (this.decaying(blockState0)) {
            m_49950_(blockState0, serverLevel1, blockPos2);
            serverLevel1.m_7471_(blockPos2, false);
        }
    }

    protected boolean decaying(BlockState blockState0) {
        return !(Boolean) blockState0.m_61143_(PERSISTENT) && (Integer) blockState0.m_61143_(DISTANCE) == 7;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        serverLevel1.m_7731_(blockPos2, updateDistance(blockState0, serverLevel1, blockPos2), 3);
    }

    @Override
    public int getLightBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return 1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        int $$6 = getDistanceAt(blockState2) + 1;
        if ($$6 != 1 || (Integer) blockState0.m_61143_(DISTANCE) != $$6) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return blockState0;
    }

    private static BlockState updateDistance(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        int $$3 = 7;
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        for (Direction $$5 : Direction.values()) {
            $$4.setWithOffset(blockPos2, $$5);
            $$3 = Math.min($$3, getDistanceAt(levelAccessor1.m_8055_($$4)) + 1);
            if ($$3 == 1) {
                break;
            }
        }
        return (BlockState) blockState0.m_61124_(DISTANCE, $$3);
    }

    private static int getDistanceAt(BlockState blockState0) {
        return getOptionalDistanceAt(blockState0).orElse(7);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState blockState0) {
        if (blockState0.m_204336_(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        } else {
            return blockState0.m_61138_(DISTANCE) ? OptionalInt.of((Integer) blockState0.m_61143_(DISTANCE)) : OptionalInt.empty();
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (level1.isRainingAt(blockPos2.above())) {
            if (randomSource3.nextInt(15) == 1) {
                BlockPos $$4 = blockPos2.below();
                BlockState $$5 = level1.getBlockState($$4);
                if (!$$5.m_60815_() || !$$5.m_60783_(level1, $$4, Direction.UP)) {
                    ParticleUtils.spawnParticleBelow(level1, blockPos2, randomSource3, ParticleTypes.DRIPPING_WATER);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(DISTANCE, PERSISTENT, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        BlockState $$2 = (BlockState) ((BlockState) this.m_49966_().m_61124_(PERSISTENT, true)).m_61124_(WATERLOGGED, $$1.getType() == Fluids.WATER);
        return updateDistance($$2, blockPlaceContext0.m_43725_(), blockPlaceContext0.getClickedPos());
    }
}