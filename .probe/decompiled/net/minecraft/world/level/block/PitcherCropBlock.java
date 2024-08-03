package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PitcherCropBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

    public static final int MAX_AGE = 4;

    private static final int DOUBLE_PLANT_AGE_INTERSECTION = 3;

    private static final int BONEMEAL_INCREASE = 1;

    private static final VoxelShape FULL_UPPER_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);

    private static final VoxelShape FULL_LOWER_SHAPE = Block.box(3.0, -1.0, 3.0, 13.0, 16.0, 13.0);

    private static final VoxelShape COLLISION_SHAPE_BULB = Block.box(5.0, -1.0, 5.0, 11.0, 3.0, 11.0);

    private static final VoxelShape COLLISION_SHAPE_CROP = Block.box(3.0, -1.0, 3.0, 13.0, 5.0, 13.0);

    private static final VoxelShape[] UPPER_SHAPE_BY_AGE = new VoxelShape[] { Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0), FULL_UPPER_SHAPE };

    private static final VoxelShape[] LOWER_SHAPE_BY_AGE = new VoxelShape[] { COLLISION_SHAPE_BULB, Block.box(3.0, -1.0, 3.0, 13.0, 14.0, 13.0), FULL_LOWER_SHAPE, FULL_LOWER_SHAPE, FULL_LOWER_SHAPE };

    public PitcherCropBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    private boolean isMaxAge(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(AGE) >= 4;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return blockState0.m_61143_(f_52858_) == DoubleBlockHalf.LOWER && !this.isMaxAge(blockState0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return this.m_49966_();
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : blockState0;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        if ((Integer) blockState0.m_61143_(AGE) == 0) {
            return COLLISION_SHAPE_BULB;
        } else {
            return blockState0.m_61143_(f_52858_) == DoubleBlockHalf.LOWER ? COLLISION_SHAPE_CROP : super.m_5939_(blockState0, blockGetter1, blockPos2, collisionContext3);
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return !isLower(blockState0) ? super.canSurvive(blockState0, levelReader1, blockPos2) : this.mayPlaceOn(levelReader1.m_8055_(blockPos2.below()), levelReader1, blockPos2.below()) && sufficientLight(levelReader1, blockPos2) && ((Integer) blockState0.m_61143_(AGE) < 3 || isUpper(levelReader1.m_8055_(blockPos2.above())));
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60713_(Blocks.FARMLAND);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
        super.createBlockStateDefinition(stateDefinitionBuilderBlockBlockState0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return blockState0.m_61143_(f_52858_) == DoubleBlockHalf.UPPER ? UPPER_SHAPE_BY_AGE[Math.min(Math.abs(4 - ((Integer) blockState0.m_61143_(AGE) + 1)), UPPER_SHAPE_BY_AGE.length - 1)] : LOWER_SHAPE_BY_AGE[blockState0.m_61143_(AGE)];
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (entity3 instanceof Ravager && level1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level1.m_46953_(blockPos2, true, entity3);
        }
        super.m_7892_(blockState0, level1, blockPos2, entity3);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return false;
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        float $$4 = CropBlock.getGrowthSpeed(this, serverLevel1, blockPos2);
        boolean $$5 = randomSource3.nextInt((int) (25.0F / $$4) + 1) == 0;
        if ($$5) {
            this.grow(serverLevel1, blockState0, blockPos2, 1);
        }
    }

    private void grow(ServerLevel serverLevel0, BlockState blockState1, BlockPos blockPos2, int int3) {
        int $$4 = Math.min((Integer) blockState1.m_61143_(AGE) + int3, 4);
        if (this.canGrow(serverLevel0, blockPos2, blockState1, $$4)) {
            serverLevel0.m_7731_(blockPos2, (BlockState) blockState1.m_61124_(AGE, $$4), 2);
            if ($$4 >= 3) {
                BlockPos $$5 = blockPos2.above();
                serverLevel0.m_7731_($$5, m_182453_(serverLevel0, blockPos2, (BlockState) ((BlockState) this.m_49966_().m_61124_(AGE, $$4)).m_61124_(f_52858_, DoubleBlockHalf.UPPER)), 3);
            }
        }
    }

    private static boolean canGrowInto(LevelReader levelReader0, BlockPos blockPos1) {
        BlockState $$2 = levelReader0.m_8055_(blockPos1);
        return $$2.m_60795_() || $$2.m_60713_(Blocks.PITCHER_CROP);
    }

    private static boolean sufficientLight(LevelReader levelReader0, BlockPos blockPos1) {
        return levelReader0.m_45524_(blockPos1, 0) >= 8 || levelReader0.m_45527_(blockPos1);
    }

    private static boolean isLower(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.PITCHER_CROP) && blockState0.m_61143_(f_52858_) == DoubleBlockHalf.LOWER;
    }

    private static boolean isUpper(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.PITCHER_CROP) && blockState0.m_61143_(f_52858_) == DoubleBlockHalf.UPPER;
    }

    private boolean canGrow(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, int int3) {
        return !this.isMaxAge(blockState2) && sufficientLight(levelReader0, blockPos1) && (int3 < 3 || canGrowInto(levelReader0, blockPos1.above()));
    }

    @Nullable
    private PitcherCropBlock.PosAndState getLowerHalf(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
        if (isLower(blockState2)) {
            return new PitcherCropBlock.PosAndState(blockPos1, blockState2);
        } else {
            BlockPos $$3 = blockPos1.below();
            BlockState $$4 = levelReader0.m_8055_($$3);
            return isLower($$4) ? new PitcherCropBlock.PosAndState($$3, $$4) : null;
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        PitcherCropBlock.PosAndState $$4 = this.getLowerHalf(levelReader0, blockPos1, blockState2);
        return $$4 == null ? false : this.canGrow(levelReader0, $$4.pos, $$4.state, (Integer) $$4.state.m_61143_(AGE) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        PitcherCropBlock.PosAndState $$4 = this.getLowerHalf(serverLevel0, blockPos2, blockState3);
        if ($$4 != null) {
            this.grow(serverLevel0, $$4.state, $$4.pos, 1);
        }
    }

    static record PosAndState(BlockPos f_289994_, BlockState f_289993_) {

        private final BlockPos pos;

        private final BlockState state;

        PosAndState(BlockPos f_289994_, BlockState f_289993_) {
            this.pos = f_289994_;
            this.state = f_289993_;
        }
    }
}