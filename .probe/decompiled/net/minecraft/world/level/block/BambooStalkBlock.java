package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooStalkBlock extends Block implements BonemealableBlock {

    protected static final float SMALL_LEAVES_AABB_OFFSET = 3.0F;

    protected static final float LARGE_LEAVES_AABB_OFFSET = 5.0F;

    protected static final float COLLISION_AABB_OFFSET = 1.5F;

    protected static final VoxelShape SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);

    protected static final VoxelShape LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;

    public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;

    public static final int MAX_HEIGHT = 16;

    public static final int STAGE_GROWING = 0;

    public static final int STAGE_DONE_GROWING = 1;

    public static final int AGE_THIN_BAMBOO = 0;

    public static final int AGE_THICK_BAMBOO = 1;

    public BambooStalkBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0)).m_61124_(LEAVES, BambooLeaves.NONE)).m_61124_(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE, LEAVES, STAGE);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        VoxelShape $$4 = blockState0.m_61143_(LEAVES) == BambooLeaves.LARGE ? LARGE_SHAPE : SMALL_SHAPE;
        Vec3 $$5 = blockState0.m_60824_(blockGetter1, blockPos2);
        return $$4.move($$5.x, $$5.y, $$5.z);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Vec3 $$4 = blockState0.m_60824_(blockGetter1, blockPos2);
        return COLLISION_SHAPE.move($$4.x, $$4.y, $$4.z);
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        FluidState $$1 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        if (!$$1.isEmpty()) {
            return null;
        } else {
            BlockState $$2 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos().below());
            if ($$2.m_204336_(BlockTags.BAMBOO_PLANTABLE_ON)) {
                if ($$2.m_60713_(Blocks.BAMBOO_SAPLING)) {
                    return (BlockState) this.m_49966_().m_61124_(AGE, 0);
                } else if ($$2.m_60713_(Blocks.BAMBOO)) {
                    int $$3 = $$2.m_61143_(AGE) > 0 ? 1 : 0;
                    return (BlockState) this.m_49966_().m_61124_(AGE, $$3);
                } else {
                    BlockState $$4 = blockPlaceContext0.m_43725_().getBlockState(blockPlaceContext0.getClickedPos().above());
                    return $$4.m_60713_(Blocks.BAMBOO) ? (BlockState) this.m_49966_().m_61124_(AGE, (Integer) $$4.m_61143_(AGE)) : Blocks.BAMBOO_SAPLING.defaultBlockState();
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!blockState0.m_60710_(serverLevel1, blockPos2)) {
            serverLevel1.m_46961_(blockPos2, true);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(STAGE) == 0;
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(STAGE) == 0) {
            if (randomSource3.nextInt(3) == 0 && serverLevel1.m_46859_(blockPos2.above()) && serverLevel1.m_45524_(blockPos2.above(), 0) >= 9) {
                int $$4 = this.getHeightBelowUpToMax(serverLevel1, blockPos2) + 1;
                if ($$4 < 16) {
                    this.growBamboo(blockState0, serverLevel1, blockPos2, randomSource3, $$4);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.below()).m_204336_(BlockTags.BAMBOO_PLANTABLE_ON);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (!blockState0.m_60710_(levelAccessor3, blockPos4)) {
            levelAccessor3.scheduleTick(blockPos4, this, 1);
        }
        if (direction1 == Direction.UP && blockState2.m_60713_(Blocks.BAMBOO) && (Integer) blockState2.m_61143_(AGE) > (Integer) blockState0.m_61143_(AGE)) {
            levelAccessor3.m_7731_(blockPos4, (BlockState) blockState0.m_61122_(AGE), 2);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        int $$4 = this.getHeightAboveUpToMax(levelReader0, blockPos1);
        int $$5 = this.getHeightBelowUpToMax(levelReader0, blockPos1);
        return $$4 + $$5 + 1 < 16 && (Integer) levelReader0.m_8055_(blockPos1.above($$4)).m_61143_(STAGE) != 1;
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        int $$4 = this.getHeightAboveUpToMax(serverLevel0, blockPos2);
        int $$5 = this.getHeightBelowUpToMax(serverLevel0, blockPos2);
        int $$6 = $$4 + $$5 + 1;
        int $$7 = 1 + randomSource1.nextInt(2);
        for (int $$8 = 0; $$8 < $$7; $$8++) {
            BlockPos $$9 = blockPos2.above($$4);
            BlockState $$10 = serverLevel0.m_8055_($$9);
            if ($$6 >= 16 || (Integer) $$10.m_61143_(STAGE) == 1 || !serverLevel0.m_46859_($$9.above())) {
                return;
            }
            this.growBamboo($$10, serverLevel0, $$9, randomSource1, $$6);
            $$4++;
            $$6++;
        }
    }

    @Override
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        return player1.m_21205_().getItem() instanceof SwordItem ? 1.0F : super.m_5880_(blockState0, player1, blockGetter2, blockPos3);
    }

    protected void growBamboo(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3, int int4) {
        BlockState $$5 = level1.getBlockState(blockPos2.below());
        BlockPos $$6 = blockPos2.below(2);
        BlockState $$7 = level1.getBlockState($$6);
        BambooLeaves $$8 = BambooLeaves.NONE;
        if (int4 >= 1) {
            if (!$$5.m_60713_(Blocks.BAMBOO) || $$5.m_61143_(LEAVES) == BambooLeaves.NONE) {
                $$8 = BambooLeaves.SMALL;
            } else if ($$5.m_60713_(Blocks.BAMBOO) && $$5.m_61143_(LEAVES) != BambooLeaves.NONE) {
                $$8 = BambooLeaves.LARGE;
                if ($$7.m_60713_(Blocks.BAMBOO)) {
                    level1.setBlock(blockPos2.below(), (BlockState) $$5.m_61124_(LEAVES, BambooLeaves.SMALL), 3);
                    level1.setBlock($$6, (BlockState) $$7.m_61124_(LEAVES, BambooLeaves.NONE), 3);
                }
            }
        }
        int $$9 = blockState0.m_61143_(AGE) != 1 && !$$7.m_60713_(Blocks.BAMBOO) ? 0 : 1;
        int $$10 = (int4 < 11 || !(randomSource3.nextFloat() < 0.25F)) && int4 != 15 ? 0 : 1;
        level1.setBlock(blockPos2.above(), (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(AGE, $$9)).m_61124_(LEAVES, $$8)).m_61124_(STAGE, $$10), 3);
    }

    protected int getHeightAboveUpToMax(BlockGetter blockGetter0, BlockPos blockPos1) {
        int $$2 = 0;
        while ($$2 < 16 && blockGetter0.getBlockState(blockPos1.above($$2 + 1)).m_60713_(Blocks.BAMBOO)) {
            $$2++;
        }
        return $$2;
    }

    protected int getHeightBelowUpToMax(BlockGetter blockGetter0, BlockPos blockPos1) {
        int $$2 = 0;
        while ($$2 < 16 && blockGetter0.getBlockState(blockPos1.below($$2 + 1)).m_60713_(Blocks.BAMBOO)) {
            $$2++;
        }
        return $$2;
    }
}