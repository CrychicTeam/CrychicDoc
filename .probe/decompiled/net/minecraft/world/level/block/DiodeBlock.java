package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;

public abstract class DiodeBlock extends HorizontalDirectionalBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    protected DiodeBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return m_49936_(levelReader1, blockPos2.below());
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!this.isLocked(serverLevel1, blockPos2, blockState0)) {
            boolean $$4 = (Boolean) blockState0.m_61143_(POWERED);
            boolean $$5 = this.shouldTurnOn(serverLevel1, blockPos2, blockState0);
            if ($$4 && !$$5) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(POWERED, false), 2);
            } else if (!$$4) {
                serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(POWERED, true), 2);
                if (!$$5) {
                    serverLevel1.m_186464_(blockPos2, this, this.getDelay(blockState0), TickPriority.VERY_HIGH);
                }
            }
        }
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_60746_(blockGetter1, blockPos2, direction3);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        if (!(Boolean) blockState0.m_61143_(POWERED)) {
            return 0;
        } else {
            return blockState0.m_61143_(f_54117_) == direction3 ? this.getOutputSignal(blockGetter1, blockPos2, blockState0) : 0;
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        if (blockState0.m_60710_(level1, blockPos2)) {
            this.checkTickOnNeighbor(level1, blockPos2, blockState0);
        } else {
            BlockEntity $$6 = blockState0.m_155947_() ? level1.getBlockEntity(blockPos2) : null;
            m_49892_(blockState0, level1, blockPos2, $$6);
            level1.removeBlock(blockPos2, false);
            for (Direction $$7 : Direction.values()) {
                level1.updateNeighborsAt(blockPos2.relative($$7), this);
            }
        }
    }

    protected void checkTickOnNeighbor(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (!this.isLocked(level0, blockPos1, blockState2)) {
            boolean $$3 = (Boolean) blockState2.m_61143_(POWERED);
            boolean $$4 = this.shouldTurnOn(level0, blockPos1, blockState2);
            if ($$3 != $$4 && !level0.m_183326_().willTickThisTick(blockPos1, this)) {
                TickPriority $$5 = TickPriority.HIGH;
                if (this.shouldPrioritize(level0, blockPos1, blockState2)) {
                    $$5 = TickPriority.EXTREMELY_HIGH;
                } else if ($$3) {
                    $$5 = TickPriority.VERY_HIGH;
                }
                level0.m_186464_(blockPos1, this, this.getDelay(blockState2), $$5);
            }
        }
    }

    public boolean isLocked(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2) {
        return false;
    }

    protected boolean shouldTurnOn(Level level0, BlockPos blockPos1, BlockState blockState2) {
        return this.getInputSignal(level0, blockPos1, blockState2) > 0;
    }

    protected int getInputSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(f_54117_);
        BlockPos $$4 = blockPos1.relative($$3);
        int $$5 = level0.m_277185_($$4, $$3);
        if ($$5 >= 15) {
            return $$5;
        } else {
            BlockState $$6 = level0.getBlockState($$4);
            return Math.max($$5, $$6.m_60713_(Blocks.REDSTONE_WIRE) ? (Integer) $$6.m_61143_(RedStoneWireBlock.POWER) : 0);
        }
    }

    protected int getAlternateSignal(SignalGetter signalGetter0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(f_54117_);
        Direction $$4 = $$3.getClockWise();
        Direction $$5 = $$3.getCounterClockWise();
        boolean $$6 = this.sideInputDiodesOnly();
        return Math.max(signalGetter0.getControlInputSignal(blockPos1.relative($$4), $$4, $$6), signalGetter0.getControlInputSignal(blockPos1.relative($$5), $$5, $$6));
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(f_54117_, blockPlaceContext0.m_8125_().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (this.shouldTurnOn(level0, blockPos1, blockState2)) {
            level0.m_186460_(blockPos1, this, 1);
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        this.updateNeighborsInFront(level1, blockPos2, blockState0);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
            this.updateNeighborsInFront(level1, blockPos2, blockState0);
        }
    }

    protected void updateNeighborsInFront(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(f_54117_);
        BlockPos $$4 = blockPos1.relative($$3.getOpposite());
        level0.neighborChanged($$4, this, blockPos1);
        level0.updateNeighborsAtExceptFromFacing($$4, this, $$3);
    }

    protected boolean sideInputDiodesOnly() {
        return false;
    }

    protected int getOutputSignal(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return 15;
    }

    public static boolean isDiode(BlockState blockState0) {
        return blockState0.m_60734_() instanceof DiodeBlock;
    }

    public boolean shouldPrioritize(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = ((Direction) blockState2.m_61143_(f_54117_)).getOpposite();
        BlockState $$4 = blockGetter0.getBlockState(blockPos1.relative($$3));
        return isDiode($$4) && $$4.m_61143_(f_54117_) != $$3;
    }

    protected abstract int getDelay(BlockState var1);
}