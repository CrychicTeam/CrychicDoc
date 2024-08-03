package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CropBlock extends BushBlock implements BonemealableBlock {

    public static final int MAX_AGE = 7;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) };

    protected CropBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(this.getAgeProperty(), 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE_BY_AGE[this.getAge(blockState0)];
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60713_(Blocks.FARMLAND);
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(this.getAgeProperty());
    }

    public BlockState getStateForAge(int int0) {
        return (BlockState) this.m_49966_().m_61124_(this.getAgeProperty(), int0);
    }

    public final boolean isMaxAge(BlockState blockState0) {
        return this.getAge(blockState0) >= this.getMaxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState0) {
        return !this.isMaxAge(blockState0);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (serverLevel1.m_45524_(blockPos2, 0) >= 9) {
            int $$4 = this.getAge(blockState0);
            if ($$4 < this.getMaxAge()) {
                float $$5 = getGrowthSpeed(this, serverLevel1, blockPos2);
                if (randomSource3.nextInt((int) (25.0F / $$5) + 1) == 0) {
                    serverLevel1.m_7731_(blockPos2, this.getStateForAge($$4 + 1), 2);
                }
            }
        }
    }

    public void growCrops(Level level0, BlockPos blockPos1, BlockState blockState2) {
        int $$3 = this.getAge(blockState2) + this.getBonemealAgeIncrease(level0);
        int $$4 = this.getMaxAge();
        if ($$3 > $$4) {
            $$3 = $$4;
        }
        level0.setBlock(blockPos1, this.getStateForAge($$3), 2);
    }

    protected int getBonemealAgeIncrease(Level level0) {
        return Mth.nextInt(level0.random, 2, 5);
    }

    protected static float getGrowthSpeed(Block block0, BlockGetter blockGetter1, BlockPos blockPos2) {
        float $$3 = 1.0F;
        BlockPos $$4 = blockPos2.below();
        for (int $$5 = -1; $$5 <= 1; $$5++) {
            for (int $$6 = -1; $$6 <= 1; $$6++) {
                float $$7 = 0.0F;
                BlockState $$8 = blockGetter1.getBlockState($$4.offset($$5, 0, $$6));
                if ($$8.m_60713_(Blocks.FARMLAND)) {
                    $$7 = 1.0F;
                    if ((Integer) $$8.m_61143_(FarmBlock.MOISTURE) > 0) {
                        $$7 = 3.0F;
                    }
                }
                if ($$5 != 0 || $$6 != 0) {
                    $$7 /= 4.0F;
                }
                $$3 += $$7;
            }
        }
        BlockPos $$9 = blockPos2.north();
        BlockPos $$10 = blockPos2.south();
        BlockPos $$11 = blockPos2.west();
        BlockPos $$12 = blockPos2.east();
        boolean $$13 = blockGetter1.getBlockState($$11).m_60713_(block0) || blockGetter1.getBlockState($$12).m_60713_(block0);
        boolean $$14 = blockGetter1.getBlockState($$9).m_60713_(block0) || blockGetter1.getBlockState($$10).m_60713_(block0);
        if ($$13 && $$14) {
            $$3 /= 2.0F;
        } else {
            boolean $$15 = blockGetter1.getBlockState($$11.north()).m_60713_(block0) || blockGetter1.getBlockState($$12.north()).m_60713_(block0) || blockGetter1.getBlockState($$12.south()).m_60713_(block0) || blockGetter1.getBlockState($$11.south()).m_60713_(block0);
            if ($$15) {
                $$3 /= 2.0F;
            }
        }
        return $$3;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return (levelReader1.m_45524_(blockPos2, 0) >= 8 || levelReader1.m_45527_(blockPos2)) && super.canSurvive(blockState0, levelReader1, blockPos2);
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (entity3 instanceof Ravager && level1.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level1.m_46953_(blockPos2, true, entity3);
        }
        super.m_7892_(blockState0, level1, blockPos2, entity3);
    }

    protected ItemLike getBaseSeedId() {
        return Items.WHEAT_SEEDS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return new ItemStack(this.getBaseSeedId());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        return !this.isMaxAge(blockState2);
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        this.growCrops(serverLevel0, blockPos2, blockState3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE);
    }
}