package net.minecraft.world.level.block;

import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TripWireHookBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    protected static final int WIRE_DIST_MIN = 1;

    protected static final int WIRE_DIST_MAX = 42;

    private static final int RECHECK_PERIOD = 10;

    protected static final int AABB_OFFSET = 3;

    protected static final VoxelShape NORTH_AABB = Block.box(5.0, 0.0, 10.0, 11.0, 10.0, 16.0);

    protected static final VoxelShape SOUTH_AABB = Block.box(5.0, 0.0, 0.0, 11.0, 10.0, 6.0);

    protected static final VoxelShape WEST_AABB = Block.box(10.0, 0.0, 5.0, 16.0, 10.0, 11.0);

    protected static final VoxelShape EAST_AABB = Block.box(0.0, 0.0, 5.0, 6.0, 10.0, 11.0);

    public TripWireHookBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(POWERED, false)).m_61124_(ATTACHED, false));
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((Direction) blockState0.m_61143_(FACING)) {
            case EAST:
            default:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case NORTH:
                return NORTH_AABB;
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = (Direction) blockState0.m_61143_(FACING);
        BlockPos $$4 = blockPos2.relative($$3.getOpposite());
        BlockState $$5 = levelReader1.m_8055_($$4);
        return $$3.getAxis().isHorizontal() && $$5.m_60783_(levelReader1, $$4, $$3);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getOpposite() == blockState0.m_61143_(FACING) && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = (BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(ATTACHED, false);
        LevelReader $$2 = blockPlaceContext0.m_43725_();
        BlockPos $$3 = blockPlaceContext0.getClickedPos();
        Direction[] $$4 = blockPlaceContext0.getNearestLookingDirections();
        for (Direction $$5 : $$4) {
            if ($$5.getAxis().isHorizontal()) {
                Direction $$6 = $$5.getOpposite();
                $$1 = (BlockState) $$1.m_61124_(FACING, $$6);
                if ($$1.m_60710_($$2, $$3)) {
                    return $$1;
                }
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        this.calculateState(level0, blockPos1, blockState2, false, false, -1, null);
    }

    public void calculateState(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3, boolean boolean4, int int5, @Nullable BlockState blockState6) {
        Direction $$7 = (Direction) blockState2.m_61143_(FACING);
        boolean $$8 = (Boolean) blockState2.m_61143_(ATTACHED);
        boolean $$9 = (Boolean) blockState2.m_61143_(POWERED);
        boolean $$10 = !boolean3;
        boolean $$11 = false;
        int $$12 = 0;
        BlockState[] $$13 = new BlockState[42];
        for (int $$14 = 1; $$14 < 42; $$14++) {
            BlockPos $$15 = blockPos1.relative($$7, $$14);
            BlockState $$16 = level0.getBlockState($$15);
            if ($$16.m_60713_(Blocks.TRIPWIRE_HOOK)) {
                if ($$16.m_61143_(FACING) == $$7.getOpposite()) {
                    $$12 = $$14;
                }
                break;
            }
            if (!$$16.m_60713_(Blocks.TRIPWIRE) && $$14 != int5) {
                $$13[$$14] = null;
                $$10 = false;
            } else {
                if ($$14 == int5) {
                    $$16 = (BlockState) MoreObjects.firstNonNull(blockState6, $$16);
                }
                boolean $$17 = !(Boolean) $$16.m_61143_(TripWireBlock.DISARMED);
                boolean $$18 = (Boolean) $$16.m_61143_(TripWireBlock.POWERED);
                $$11 |= $$17 && $$18;
                $$13[$$14] = $$16;
                if ($$14 == int5) {
                    level0.m_186460_(blockPos1, this, 10);
                    $$10 &= $$17;
                }
            }
        }
        $$10 &= $$12 > 1;
        $$11 &= $$10;
        BlockState $$19 = (BlockState) ((BlockState) this.m_49966_().m_61124_(ATTACHED, $$10)).m_61124_(POWERED, $$11);
        if ($$12 > 0) {
            BlockPos $$20 = blockPos1.relative($$7, $$12);
            Direction $$21 = $$7.getOpposite();
            level0.setBlock($$20, (BlockState) $$19.m_61124_(FACING, $$21), 3);
            this.notifyNeighbors(level0, $$20, $$21);
            this.emitState(level0, $$20, $$10, $$11, $$8, $$9);
        }
        this.emitState(level0, blockPos1, $$10, $$11, $$8, $$9);
        if (!boolean3) {
            level0.setBlock(blockPos1, (BlockState) $$19.m_61124_(FACING, $$7), 3);
            if (boolean4) {
                this.notifyNeighbors(level0, blockPos1, $$7);
            }
        }
        if ($$8 != $$10) {
            for (int $$22 = 1; $$22 < $$12; $$22++) {
                BlockPos $$23 = blockPos1.relative($$7, $$22);
                BlockState $$24 = $$13[$$22];
                if ($$24 != null) {
                    level0.setBlock($$23, (BlockState) $$24.m_61124_(ATTACHED, $$10), 3);
                    if (!level0.getBlockState($$23).m_60795_()) {
                    }
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.calculateState(serverLevel1, blockPos2, blockState0, false, true, -1, null);
    }

    private void emitState(Level level0, BlockPos blockPos1, boolean boolean2, boolean boolean3, boolean boolean4, boolean boolean5) {
        if (boolean3 && !boolean5) {
            level0.playSound(null, blockPos1, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4F, 0.6F);
            level0.m_142346_(null, GameEvent.BLOCK_ACTIVATE, blockPos1);
        } else if (!boolean3 && boolean5) {
            level0.playSound(null, blockPos1, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4F, 0.5F);
            level0.m_142346_(null, GameEvent.BLOCK_DEACTIVATE, blockPos1);
        } else if (boolean2 && !boolean4) {
            level0.playSound(null, blockPos1, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4F, 0.7F);
            level0.m_142346_(null, GameEvent.BLOCK_ATTACH, blockPos1);
        } else if (!boolean2 && boolean4) {
            level0.playSound(null, blockPos1, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4F, 1.2F / (level0.random.nextFloat() * 0.2F + 0.9F));
            level0.m_142346_(null, GameEvent.BLOCK_DETACH, blockPos1);
        }
    }

    private void notifyNeighbors(Level level0, BlockPos blockPos1, Direction direction2) {
        level0.updateNeighborsAt(blockPos1, this);
        level0.updateNeighborsAt(blockPos1.relative(direction2.getOpposite()), this);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            boolean $$5 = (Boolean) blockState0.m_61143_(ATTACHED);
            boolean $$6 = (Boolean) blockState0.m_61143_(POWERED);
            if ($$5 || $$6) {
                this.calculateState(level1, blockPos2, blockState0, true, false, -1, null);
            }
            if ($$6) {
                level1.updateNeighborsAt(blockPos2, this);
                level1.updateNeighborsAt(blockPos2.relative(((Direction) blockState0.m_61143_(FACING)).getOpposite()), this);
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        if (!(Boolean) blockState0.m_61143_(POWERED)) {
            return 0;
        } else {
            return blockState0.m_61143_(FACING) == direction3 ? 15 : 0;
        }
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, POWERED, ATTACHED);
    }
}