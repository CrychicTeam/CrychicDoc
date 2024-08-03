package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CactusBlock extends Block {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    public static final int MAX_AGE = 15;

    protected static final int AABB_OFFSET = 1;

    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);

    protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    protected CactusBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockPos $$4 = blockPos2.above();
        if (serverLevel1.m_46859_($$4)) {
            int $$5 = 1;
            while (serverLevel1.m_8055_(blockPos2.below($$5)).m_60713_(this)) {
                $$5++;
            }
            if ($$5 < 3) {
                int $$6 = (Integer) blockState0.m_61143_(AGE);
                if ($$6 == 15) {
                    serverLevel1.m_46597_($$4, this.m_49966_());
                    BlockState $$7 = (BlockState) blockState0.m_61124_(AGE, 0);
                    serverLevel1.m_7731_(blockPos2, $$7, 4);
                    serverLevel1.neighborChanged($$7, $$4, this, blockPos2, false);
                } else {
                    serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(AGE, $$6 + 1), 4);
                }
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return OUTLINE_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        for (Direction $$3 : Direction.Plane.HORIZONTAL) {
            BlockState $$4 = levelReader1.m_8055_(blockPos2.relative($$3));
            if ($$4.m_280296_() || levelReader1.m_6425_(blockPos2.relative($$3)).is(FluidTags.LAVA)) {
                return false;
            }
        }
        BlockState $$5 = levelReader1.m_8055_(blockPos2.below());
        return ($$5.m_60713_(Blocks.CACTUS) || $$5.m_204336_(BlockTags.SAND)) && !levelReader1.m_8055_(blockPos2.above()).m_278721_();
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        entity3.hurt(level1.damageSources().cactus(), 1.0F);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}