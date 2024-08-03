package net.minecraft.world.level.block;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FrogspawnBlock extends Block {

    private static final int MIN_TADPOLES_SPAWN = 2;

    private static final int MAX_TADPOLES_SPAWN = 5;

    private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;

    private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.5, 16.0);

    private static int minHatchTickDelay = 3600;

    private static int maxHatchTickDelay = 12000;

    public FrogspawnBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return mayPlaceOn(levelReader1, blockPos2.below());
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        level1.m_186460_(blockPos2, this, getFrogspawnHatchDelay(level1.getRandom()));
    }

    private static int getFrogspawnHatchDelay(RandomSource randomSource0) {
        return randomSource0.nextInt(minHatchTickDelay, maxHatchTickDelay);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!this.canSurvive(blockState0, serverLevel1, blockPos2)) {
            this.destroyBlock(serverLevel1, blockPos2);
        } else {
            this.hatchFrogspawn(serverLevel1, blockPos2, randomSource3);
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (entity3.getType().equals(EntityType.FALLING_BLOCK)) {
            this.destroyBlock(level1, blockPos2);
        }
    }

    private static boolean mayPlaceOn(BlockGetter blockGetter0, BlockPos blockPos1) {
        FluidState $$2 = blockGetter0.getFluidState(blockPos1);
        FluidState $$3 = blockGetter0.getFluidState(blockPos1.above());
        return $$2.getType() == Fluids.WATER && $$3.getType() == Fluids.EMPTY;
    }

    private void hatchFrogspawn(ServerLevel serverLevel0, BlockPos blockPos1, RandomSource randomSource2) {
        this.destroyBlock(serverLevel0, blockPos1);
        serverLevel0.m_5594_(null, blockPos1, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
        this.spawnTadpoles(serverLevel0, blockPos1, randomSource2);
    }

    private void destroyBlock(Level level0, BlockPos blockPos1) {
        level0.m_46961_(blockPos1, false);
    }

    private void spawnTadpoles(ServerLevel serverLevel0, BlockPos blockPos1, RandomSource randomSource2) {
        int $$3 = randomSource2.nextInt(2, 6);
        for (int $$4 = 1; $$4 <= $$3; $$4++) {
            Tadpole $$5 = EntityType.TADPOLE.create(serverLevel0);
            if ($$5 != null) {
                double $$6 = (double) blockPos1.m_123341_() + this.getRandomTadpolePositionOffset(randomSource2);
                double $$7 = (double) blockPos1.m_123343_() + this.getRandomTadpolePositionOffset(randomSource2);
                int $$8 = randomSource2.nextInt(1, 361);
                $$5.m_7678_($$6, (double) blockPos1.m_123342_() - 0.5, $$7, (float) $$8, 0.0F);
                $$5.m_21530_();
                serverLevel0.addFreshEntity($$5);
            }
        }
    }

    private double getRandomTadpolePositionOffset(RandomSource randomSource0) {
        double $$1 = (double) (Tadpole.HITBOX_WIDTH / 2.0F);
        return Mth.clamp(randomSource0.nextDouble(), $$1, 1.0 - $$1);
    }

    @VisibleForTesting
    public static void setHatchDelay(int int0, int int1) {
        minHatchTickDelay = int0;
        maxHatchTickDelay = int1;
    }

    @VisibleForTesting
    public static void setDefaultHatchDelay() {
        minHatchTickDelay = 3600;
        maxHatchTickDelay = 12000;
    }
}