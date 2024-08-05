package net.minecraft.world.level.block;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;

public class DetectorRailBlock extends BaseRailBlock {

    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final int PRESSED_CHECK_PERIOD = 20;

    public DetectorRailBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(true, blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWERED, false)).m_61124_(SHAPE, RailShape.NORTH_SOUTH)).m_61124_(f_152149_, false));
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (!level1.isClientSide) {
            if (!(Boolean) blockState0.m_61143_(POWERED)) {
                this.checkPressed(level1, blockPos2, blockState0);
            }
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(POWERED)) {
            this.checkPressed(serverLevel1, blockPos2, blockState0);
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
            return direction3 == Direction.UP ? 15 : 0;
        }
    }

    private void checkPressed(Level level0, BlockPos blockPos1, BlockState blockState2) {
        if (this.m_7898_(blockState2, level0, blockPos1)) {
            boolean $$3 = (Boolean) blockState2.m_61143_(POWERED);
            boolean $$4 = false;
            List<AbstractMinecart> $$5 = this.getInteractingMinecartOfType(level0, blockPos1, AbstractMinecart.class, p_153125_ -> true);
            if (!$$5.isEmpty()) {
                $$4 = true;
            }
            if ($$4 && !$$3) {
                BlockState $$6 = (BlockState) blockState2.m_61124_(POWERED, true);
                level0.setBlock(blockPos1, $$6, 3);
                this.updatePowerToConnected(level0, blockPos1, $$6, true);
                level0.updateNeighborsAt(blockPos1, this);
                level0.updateNeighborsAt(blockPos1.below(), this);
                level0.setBlocksDirty(blockPos1, blockState2, $$6);
            }
            if (!$$4 && $$3) {
                BlockState $$7 = (BlockState) blockState2.m_61124_(POWERED, false);
                level0.setBlock(blockPos1, $$7, 3);
                this.updatePowerToConnected(level0, blockPos1, $$7, false);
                level0.updateNeighborsAt(blockPos1, this);
                level0.updateNeighborsAt(blockPos1.below(), this);
                level0.setBlocksDirty(blockPos1, blockState2, $$7);
            }
            if ($$4) {
                level0.m_186460_(blockPos1, this, 20);
            }
            level0.updateNeighbourForOutputSignal(blockPos1, this);
        }
    }

    protected void updatePowerToConnected(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        RailState $$4 = new RailState(level0, blockPos1, blockState2);
        for (BlockPos $$6 : $$4.getConnections()) {
            BlockState $$7 = level0.getBlockState($$6);
            level0.neighborChanged($$7, $$6, $$7.m_60734_(), blockPos1, false);
        }
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState3.m_60713_(blockState0.m_60734_())) {
            BlockState $$5 = this.m_49389_(blockState0, level1, blockPos2, boolean4);
            this.checkPressed(level1, blockPos2, $$5);
        }
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        if ((Boolean) blockState0.m_61143_(POWERED)) {
            List<MinecartCommandBlock> $$3 = this.getInteractingMinecartOfType(level1, blockPos2, MinecartCommandBlock.class, p_153123_ -> true);
            if (!$$3.isEmpty()) {
                return ((MinecartCommandBlock) $$3.get(0)).getCommandBlock().getSuccessCount();
            }
            List<AbstractMinecart> $$4 = this.getInteractingMinecartOfType(level1, blockPos2, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!$$4.isEmpty()) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) $$4.get(0));
            }
        }
        return 0;
    }

    private <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level level0, BlockPos blockPos1, Class<T> classT2, Predicate<Entity> predicateEntity3) {
        return level0.m_6443_(classT2, this.getSearchBB(blockPos1), predicateEntity3);
    }

    private AABB getSearchBB(BlockPos blockPos0) {
        double $$1 = 0.2;
        return new AABB((double) blockPos0.m_123341_() + 0.2, (double) blockPos0.m_123342_(), (double) blockPos0.m_123343_() + 0.2, (double) (blockPos0.m_123341_() + 1) - 0.2, (double) (blockPos0.m_123342_() + 1) - 0.2, (double) (blockPos0.m_123343_() + 1) - 0.2);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        switch(rotation1) {
            case CLOCKWISE_180:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                }
            case COUNTERCLOCKWISE_90:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            case CLOCKWISE_90:
                switch((RailShape) blockState0.m_61143_(SHAPE)) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.EAST_WEST);
                    case EAST_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_SOUTH);
                }
            default:
                return blockState0;
        }
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        RailShape $$2 = (RailShape) blockState0.m_61143_(SHAPE);
        switch(mirror1) {
            case LEFT_RIGHT:
                switch($$2) {
                    case ASCENDING_NORTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_SOUTH);
                    case ASCENDING_SOUTH:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_NORTH);
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    default:
                        return super.m_6943_(blockState0, mirror1);
                }
            case FRONT_BACK:
                switch($$2) {
                    case ASCENDING_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_WEST);
                    case ASCENDING_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.ASCENDING_EAST);
                    case ASCENDING_NORTH:
                    case ASCENDING_SOUTH:
                    default:
                        break;
                    case SOUTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_WEST);
                    case SOUTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.SOUTH_EAST);
                    case NORTH_WEST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_EAST);
                    case NORTH_EAST:
                        return (BlockState) blockState0.m_61124_(SHAPE, RailShape.NORTH_WEST);
                }
        }
        return super.m_6943_(blockState0, mirror1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(SHAPE, POWERED, f_152149_);
    }
}