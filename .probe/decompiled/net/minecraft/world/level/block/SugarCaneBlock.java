package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SugarCaneBlock extends Block {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    protected static final float AABB_OFFSET = 6.0F;

    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    protected SugarCaneBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_46859_(blockPos2.above())) {
            int $$4 = 1;
            while (serverLevel1.m_8055_(blockPos2.below($$4)).m_60713_(this)) {
                $$4++;
            }
            if ($$4 < 3) {
                int $$5 = (Integer) blockState0.m_61143_(AGE);
                if ($$5 == 15) {
                    serverLevel1.m_46597_(blockPos2.above(), this.m_49966_());
                    serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(AGE, 0), 4);
                } else {
                    serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(AGE, $$5 + 1), 4);
                }
            }
        }
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
        BlockState $$3 = levelReader1.m_8055_(blockPos2.below());
        if ($$3.m_60713_(this)) {
            return true;
        } else {
            if ($$3.m_204336_(BlockTags.DIRT) || $$3.m_204336_(BlockTags.SAND)) {
                BlockPos $$4 = blockPos2.below();
                for (Direction $$5 : Direction.Plane.HORIZONTAL) {
                    BlockState $$6 = levelReader1.m_8055_($$4.relative($$5));
                    FluidState $$7 = levelReader1.m_6425_($$4.relative($$5));
                    if ($$7.is(FluidTags.WATER) || $$6.m_60713_(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }
}