package net.minecraft.world.level.block;

import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TripWireBlock extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    public static final BooleanProperty DISARMED = BlockStateProperties.DISARMED;

    public static final BooleanProperty NORTH = PipeBlock.NORTH;

    public static final BooleanProperty EAST = PipeBlock.EAST;

    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;

    public static final BooleanProperty WEST = PipeBlock.WEST;

    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = CrossCollisionBlock.PROPERTY_BY_DIRECTION;

    protected static final VoxelShape AABB = Block.box(0.0, 1.0, 0.0, 16.0, 2.5, 16.0);

    protected static final VoxelShape NOT_ATTACHED_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    private static final int RECHECK_PERIOD = 10;

    private final TripWireHookBlock hook;

    public TripWireBlock(TripWireHookBlock tripWireHookBlock0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWERED, false)).m_61124_(ATTACHED, false)).m_61124_(DISARMED, false)).m_61124_(NORTH, false)).m_61124_(EAST, false)).m_61124_(SOUTH, false)).m_61124_(WEST, false));
        this.hook = tripWireHookBlock0;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return blockState0.m_61143_(ATTACHED) ? AABB : NOT_ATTACHED_AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockGetter $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(NORTH, this.shouldConnectTo($$1.getBlockState($$2.north()), Direction.NORTH))).m_61124_(EAST, this.shouldConnectTo($$1.getBlockState($$2.east()), Direction.EAST))).m_61124_(SOUTH, this.shouldConnectTo($$1.getBlockState($$2.south()), Direction.SOUTH))).m_61124_(WEST, this.shouldConnectTo($$1.getBlockState($$2.west()), Direction.WEST));
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getAxis().isHorizontal() ? (BlockState) blockState0.m_61124_((Property) PROPERTY_BY_DIRECTION.get(direction1), this.shouldConnectTo(blockState2, direction1)) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            this.updateSource(level1, blockPos2, blockState0);
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!boolean4 && !blockState0.m_60713_(blockState3.m_60734_())) {
            this.updateSource(level1, blockPos2, (BlockState) blockState0.m_61124_(POWERED, true));
        }
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide && !player3.m_21205_().isEmpty() && player3.m_21205_().is(Items.SHEARS)) {
            level0.setBlock(blockPos1, (BlockState) blockState2.m_61124_(DISARMED, true), 4);
            level0.m_142346_(player3, GameEvent.SHEAR, blockPos1);
        }
        super.playerWillDestroy(level0, blockPos1, blockState2, player3);
    }

    private void updateSource(Level level0, BlockPos blockPos1, BlockState blockState2) {
        for (Direction $$3 : new Direction[] { Direction.SOUTH, Direction.WEST }) {
            for (int $$4 = 1; $$4 < 42; $$4++) {
                BlockPos $$5 = blockPos1.relative($$3, $$4);
                BlockState $$6 = level0.getBlockState($$5);
                if ($$6.m_60713_(this.hook)) {
                    if ($$6.m_61143_(TripWireHookBlock.FACING) == $$3.getOpposite()) {
                        this.hook.calculateState(level0, $$5, $$6, false, true, $$4, blockState2);
                    }
                    break;
                }
                if (!$$6.m_60713_(this)) {
                    break;
                }
            }
        }
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide) {
            if (!(Boolean) blockState0.m_61143_(POWERED)) {
                this.checkPressed(level1, blockPos2);
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) serverLevel1.m_8055_(blockPos2).m_61143_(POWERED)) {
            this.checkPressed(serverLevel1, blockPos2);
        }
    }

    private void checkPressed(Level level0, BlockPos blockPos1) {
        BlockState $$2 = level0.getBlockState(blockPos1);
        boolean $$3 = (Boolean) $$2.m_61143_(POWERED);
        boolean $$4 = false;
        List<? extends Entity> $$5 = level0.m_45933_(null, $$2.m_60808_(level0, blockPos1).bounds().move(blockPos1));
        if (!$$5.isEmpty()) {
            for (Entity $$6 : $$5) {
                if (!$$6.isIgnoringBlockTriggers()) {
                    $$4 = true;
                    break;
                }
            }
        }
        if ($$4 != $$3) {
            $$2 = (BlockState) $$2.m_61124_(POWERED, $$4);
            level0.setBlock(blockPos1, $$2, 3);
            this.updateSource(level0, blockPos1, $$2);
        }
        if ($$4) {
            level0.m_186460_(new BlockPos(blockPos1), this, 10);
        }
    }

    public boolean shouldConnectTo(BlockState blockState0, Direction direction1) {
        return blockState0.m_60713_(this.hook) ? blockState0.m_61143_(TripWireHookBlock.FACING) == direction1.getOpposite() : blockState0.m_60713_(this);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(EAST, (Boolean) blockState0.m_61143_(WEST))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(NORTH))).m_61124_(WEST, (Boolean) blockState0.m_61143_(EAST));
            case COUNTERCLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(EAST))).m_61124_(EAST, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(WEST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(NORTH));
            case CLOCKWISE_90:
                return (BlockState) ((BlockState) ((BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(WEST))).m_61124_(EAST, (Boolean) blockState0.m_61143_(NORTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(EAST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(SOUTH));
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        switch(mirror1) {
            case LEFT_RIGHT:
                return (BlockState) ((BlockState) blockState0.m_61124_(NORTH, (Boolean) blockState0.m_61143_(SOUTH))).m_61124_(SOUTH, (Boolean) blockState0.m_61143_(NORTH));
            case FRONT_BACK:
                return (BlockState) ((BlockState) blockState0.m_61124_(EAST, (Boolean) blockState0.m_61143_(WEST))).m_61124_(WEST, (Boolean) blockState0.m_61143_(EAST));
            default:
                return super.m_6943_(blockState0, mirror1);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(POWERED, ATTACHED, DISARMED, NORTH, EAST, WEST, SOUTH);
    }
}