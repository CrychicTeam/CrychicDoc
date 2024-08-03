package net.minecraft.world.level.block;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SculkSensorBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkSensorBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final int ACTIVE_TICKS = 30;

    public static final int COOLDOWN_TICKS = 10;

    public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    private static final float[] RESONANCE_PITCH_BEND = Util.make(new float[16], p_277301_ -> {
        int[] $$1 = new int[] { 0, 0, 2, 4, 6, 7, 9, 10, 12, 14, 15, 18, 19, 21, 22, 24 };
        for (int $$2 = 0; $$2 < 16; $$2++) {
            p_277301_[$$2] = NoteBlock.getPitchFromNote($$1[$$2]);
        }
    });

    public SculkSensorBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(PHASE, SculkSensorPhase.INACTIVE)).m_61124_(POWER, 0)).m_61124_(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockPos $$1 = blockPlaceContext0.getClickedPos();
        FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState($$1);
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$2.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (getPhase(blockState0) != SculkSensorPhase.ACTIVE) {
            if (getPhase(blockState0) == SculkSensorPhase.COOLDOWN) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(PHASE, SculkSensorPhase.INACTIVE), 3);
                if (!(Boolean) blockState0.m_61143_(WATERLOGGED)) {
                    serverLevel1.m_5594_(null, blockPos2, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1.0F, serverLevel1.f_46441_.nextFloat() * 0.2F + 0.8F);
                }
            }
        } else {
            deactivate(serverLevel1, blockPos2, blockState0);
        }
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        if (!level0.isClientSide() && canActivate(blockState2) && entity3.getType() != EntityType.WARDEN && level0.getBlockEntity(blockPos1) instanceof SculkSensorBlockEntity $$5 && level0 instanceof ServerLevel $$6 && $$5.getVibrationUser().canReceiveVibration($$6, blockPos1, GameEvent.STEP, GameEvent.Context.of(blockState2))) {
            $$5.getListener().forceScheduleVibration($$6, GameEvent.STEP, GameEvent.Context.of(entity3), entity3.position());
        }
        super.m_141947_(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!level1.isClientSide() && !blockState0.m_60713_(blockState3.m_60734_())) {
            if ((Integer) blockState0.m_61143_(POWER) > 0 && !level1.m_183326_().m_183582_(blockPos2, this)) {
                level1.setBlock(blockPos2, (BlockState) blockState0.m_61124_(POWER, 0), 18);
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if (getPhase(blockState0) == SculkSensorPhase.ACTIVE) {
                updateNeighbours(level1, blockPos2, blockState0);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    private static void updateNeighbours(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Block $$3 = blockState2.m_60734_();
        level0.updateNeighborsAt(blockPos1, $$3);
        level0.updateNeighborsAt(blockPos1.below(), $$3);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new SculkSensorBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return !level0.isClientSide ? m_152132_(blockEntityTypeT2, BlockEntityType.SCULK_SENSOR, (p_281130_, p_281131_, p_281132_, p_281133_) -> VibrationSystem.Ticker.tick(p_281130_, p_281133_.getVibrationData(), p_281133_.getVibrationUser())) : null;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return (Integer) blockState0.m_61143_(POWER);
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return direction3 == Direction.UP ? blockState0.m_60746_(blockGetter1, blockPos2, direction3) : 0;
    }

    public static SculkSensorPhase getPhase(BlockState blockState0) {
        return (SculkSensorPhase) blockState0.m_61143_(PHASE);
    }

    public static boolean canActivate(BlockState blockState0) {
        return getPhase(blockState0) == SculkSensorPhase.INACTIVE;
    }

    public static void deactivate(Level level0, BlockPos blockPos1, BlockState blockState2) {
        level0.setBlock(blockPos1, (BlockState) ((BlockState) blockState2.m_61124_(PHASE, SculkSensorPhase.COOLDOWN)).m_61124_(POWER, 0), 3);
        level0.m_186460_(blockPos1, blockState2.m_60734_(), 10);
        updateNeighbours(level0, blockPos1, blockState2);
    }

    @VisibleForTesting
    public int getActiveTicks() {
        return 30;
    }

    public void activate(@Nullable Entity entity0, Level level1, BlockPos blockPos2, BlockState blockState3, int int4, int int5) {
        level1.setBlock(blockPos2, (BlockState) ((BlockState) blockState3.m_61124_(PHASE, SculkSensorPhase.ACTIVE)).m_61124_(POWER, int4), 3);
        level1.m_186460_(blockPos2, blockState3.m_60734_(), this.getActiveTicks());
        updateNeighbours(level1, blockPos2, blockState3);
        tryResonateVibration(entity0, level1, blockPos2, int5);
        level1.m_142346_(entity0, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, blockPos2);
        if (!(Boolean) blockState3.m_61143_(WATERLOGGED)) {
            level1.playSound(null, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level1.random.nextFloat() * 0.2F + 0.8F);
        }
    }

    public static void tryResonateVibration(@Nullable Entity entity0, Level level1, BlockPos blockPos2, int int3) {
        for (Direction $$4 : Direction.values()) {
            BlockPos $$5 = blockPos2.relative($$4);
            BlockState $$6 = level1.getBlockState($$5);
            if ($$6.m_204336_(BlockTags.VIBRATION_RESONATORS)) {
                level1.m_220407_(VibrationSystem.getResonanceEventByFrequency(int3), $$5, GameEvent.Context.of(entity0, $$6));
                float $$7 = RESONANCE_PITCH_BEND[int3];
                level1.playSound(null, $$5, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0F, $$7);
            }
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (getPhase(blockState0) == SculkSensorPhase.ACTIVE) {
            Direction $$4 = Direction.getRandom(randomSource3);
            if ($$4 != Direction.UP && $$4 != Direction.DOWN) {
                double $$5 = (double) blockPos2.m_123341_() + 0.5 + ($$4.getStepX() == 0 ? 0.5 - randomSource3.nextDouble() : (double) $$4.getStepX() * 0.6);
                double $$6 = (double) blockPos2.m_123342_() + 0.25;
                double $$7 = (double) blockPos2.m_123343_() + 0.5 + ($$4.getStepZ() == 0 ? 0.5 - randomSource3.nextDouble() : (double) $$4.getStepZ() * 0.6);
                double $$8 = (double) randomSource3.nextFloat() * 0.04;
                level1.addParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, $$5, $$6, $$7, 0.0, $$8, 0.0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(PHASE, POWER, WATERLOGGED);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if (level1.getBlockEntity(blockPos2) instanceof SculkSensorBlockEntity $$4) {
            return getPhase(blockState0) == SculkSensorPhase.ACTIVE ? $$4.getLastVibrationFrequency() : 0;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (boolean4) {
            this.m_220822_(serverLevel1, blockPos2, itemStack3, ConstantInt.of(5));
        }
    }
}