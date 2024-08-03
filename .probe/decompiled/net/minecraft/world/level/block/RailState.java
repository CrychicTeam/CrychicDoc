package net.minecraft.world.level.block;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class RailState {

    private final Level level;

    private final BlockPos pos;

    private final BaseRailBlock block;

    private BlockState state;

    private final boolean isStraight;

    private final List<BlockPos> connections = Lists.newArrayList();

    public RailState(Level level0, BlockPos blockPos1, BlockState blockState2) {
        this.level = level0;
        this.pos = blockPos1;
        this.state = blockState2;
        this.block = (BaseRailBlock) blockState2.m_60734_();
        RailShape $$3 = (RailShape) blockState2.m_61143_(this.block.getShapeProperty());
        this.isStraight = this.block.isStraight();
        this.updateConnections($$3);
    }

    public List<BlockPos> getConnections() {
        return this.connections;
    }

    private void updateConnections(RailShape railShape0) {
        this.connections.clear();
        switch(railShape0) {
            case NORTH_SOUTH:
                this.connections.add(this.pos.north());
                this.connections.add(this.pos.south());
                break;
            case EAST_WEST:
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.east());
                break;
            case ASCENDING_EAST:
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.east().above());
                break;
            case ASCENDING_WEST:
                this.connections.add(this.pos.west().above());
                this.connections.add(this.pos.east());
                break;
            case ASCENDING_NORTH:
                this.connections.add(this.pos.north().above());
                this.connections.add(this.pos.south());
                break;
            case ASCENDING_SOUTH:
                this.connections.add(this.pos.north());
                this.connections.add(this.pos.south().above());
                break;
            case SOUTH_EAST:
                this.connections.add(this.pos.east());
                this.connections.add(this.pos.south());
                break;
            case SOUTH_WEST:
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.south());
                break;
            case NORTH_WEST:
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.north());
                break;
            case NORTH_EAST:
                this.connections.add(this.pos.east());
                this.connections.add(this.pos.north());
        }
    }

    private void removeSoftConnections() {
        for (int $$0 = 0; $$0 < this.connections.size(); $$0++) {
            RailState $$1 = this.getRail((BlockPos) this.connections.get($$0));
            if ($$1 != null && $$1.connectsTo(this)) {
                this.connections.set($$0, $$1.pos);
            } else {
                this.connections.remove($$0--);
            }
        }
    }

    private boolean hasRail(BlockPos blockPos0) {
        return BaseRailBlock.isRail(this.level, blockPos0) || BaseRailBlock.isRail(this.level, blockPos0.above()) || BaseRailBlock.isRail(this.level, blockPos0.below());
    }

    @Nullable
    private RailState getRail(BlockPos blockPos0) {
        BlockState $$2 = this.level.getBlockState(blockPos0);
        if (BaseRailBlock.isRail($$2)) {
            return new RailState(this.level, blockPos0, $$2);
        } else {
            BlockPos $$1 = blockPos0.above();
            $$2 = this.level.getBlockState($$1);
            if (BaseRailBlock.isRail($$2)) {
                return new RailState(this.level, $$1, $$2);
            } else {
                $$1 = blockPos0.below();
                $$2 = this.level.getBlockState($$1);
                return BaseRailBlock.isRail($$2) ? new RailState(this.level, $$1, $$2) : null;
            }
        }
    }

    private boolean connectsTo(RailState railState0) {
        return this.hasConnection(railState0.pos);
    }

    private boolean hasConnection(BlockPos blockPos0) {
        for (int $$1 = 0; $$1 < this.connections.size(); $$1++) {
            BlockPos $$2 = (BlockPos) this.connections.get($$1);
            if ($$2.m_123341_() == blockPos0.m_123341_() && $$2.m_123343_() == blockPos0.m_123343_()) {
                return true;
            }
        }
        return false;
    }

    protected int countPotentialConnections() {
        int $$0 = 0;
        for (Direction $$1 : Direction.Plane.HORIZONTAL) {
            if (this.hasRail(this.pos.relative($$1))) {
                $$0++;
            }
        }
        return $$0;
    }

    private boolean canConnectTo(RailState railState0) {
        return this.connectsTo(railState0) || this.connections.size() != 2;
    }

    private void connectTo(RailState railState0) {
        this.connections.add(railState0.pos);
        BlockPos $$1 = this.pos.north();
        BlockPos $$2 = this.pos.south();
        BlockPos $$3 = this.pos.west();
        BlockPos $$4 = this.pos.east();
        boolean $$5 = this.hasConnection($$1);
        boolean $$6 = this.hasConnection($$2);
        boolean $$7 = this.hasConnection($$3);
        boolean $$8 = this.hasConnection($$4);
        RailShape $$9 = null;
        if ($$5 || $$6) {
            $$9 = RailShape.NORTH_SOUTH;
        }
        if ($$7 || $$8) {
            $$9 = RailShape.EAST_WEST;
        }
        if (!this.isStraight) {
            if ($$6 && $$8 && !$$5 && !$$7) {
                $$9 = RailShape.SOUTH_EAST;
            }
            if ($$6 && $$7 && !$$5 && !$$8) {
                $$9 = RailShape.SOUTH_WEST;
            }
            if ($$5 && $$7 && !$$6 && !$$8) {
                $$9 = RailShape.NORTH_WEST;
            }
            if ($$5 && $$8 && !$$6 && !$$7) {
                $$9 = RailShape.NORTH_EAST;
            }
        }
        if ($$9 == RailShape.NORTH_SOUTH) {
            if (BaseRailBlock.isRail(this.level, $$1.above())) {
                $$9 = RailShape.ASCENDING_NORTH;
            }
            if (BaseRailBlock.isRail(this.level, $$2.above())) {
                $$9 = RailShape.ASCENDING_SOUTH;
            }
        }
        if ($$9 == RailShape.EAST_WEST) {
            if (BaseRailBlock.isRail(this.level, $$4.above())) {
                $$9 = RailShape.ASCENDING_EAST;
            }
            if (BaseRailBlock.isRail(this.level, $$3.above())) {
                $$9 = RailShape.ASCENDING_WEST;
            }
        }
        if ($$9 == null) {
            $$9 = RailShape.NORTH_SOUTH;
        }
        this.state = (BlockState) this.state.m_61124_(this.block.getShapeProperty(), $$9);
        this.level.setBlock(this.pos, this.state, 3);
    }

    private boolean hasNeighborRail(BlockPos blockPos0) {
        RailState $$1 = this.getRail(blockPos0);
        if ($$1 == null) {
            return false;
        } else {
            $$1.removeSoftConnections();
            return $$1.canConnectTo(this);
        }
    }

    public RailState place(boolean boolean0, boolean boolean1, RailShape railShape2) {
        BlockPos $$3 = this.pos.north();
        BlockPos $$4 = this.pos.south();
        BlockPos $$5 = this.pos.west();
        BlockPos $$6 = this.pos.east();
        boolean $$7 = this.hasNeighborRail($$3);
        boolean $$8 = this.hasNeighborRail($$4);
        boolean $$9 = this.hasNeighborRail($$5);
        boolean $$10 = this.hasNeighborRail($$6);
        RailShape $$11 = null;
        boolean $$12 = $$7 || $$8;
        boolean $$13 = $$9 || $$10;
        if ($$12 && !$$13) {
            $$11 = RailShape.NORTH_SOUTH;
        }
        if ($$13 && !$$12) {
            $$11 = RailShape.EAST_WEST;
        }
        boolean $$14 = $$8 && $$10;
        boolean $$15 = $$8 && $$9;
        boolean $$16 = $$7 && $$10;
        boolean $$17 = $$7 && $$9;
        if (!this.isStraight) {
            if ($$14 && !$$7 && !$$9) {
                $$11 = RailShape.SOUTH_EAST;
            }
            if ($$15 && !$$7 && !$$10) {
                $$11 = RailShape.SOUTH_WEST;
            }
            if ($$17 && !$$8 && !$$10) {
                $$11 = RailShape.NORTH_WEST;
            }
            if ($$16 && !$$8 && !$$9) {
                $$11 = RailShape.NORTH_EAST;
            }
        }
        if ($$11 == null) {
            if ($$12 && $$13) {
                $$11 = railShape2;
            } else if ($$12) {
                $$11 = RailShape.NORTH_SOUTH;
            } else if ($$13) {
                $$11 = RailShape.EAST_WEST;
            }
            if (!this.isStraight) {
                if (boolean0) {
                    if ($$14) {
                        $$11 = RailShape.SOUTH_EAST;
                    }
                    if ($$15) {
                        $$11 = RailShape.SOUTH_WEST;
                    }
                    if ($$16) {
                        $$11 = RailShape.NORTH_EAST;
                    }
                    if ($$17) {
                        $$11 = RailShape.NORTH_WEST;
                    }
                } else {
                    if ($$17) {
                        $$11 = RailShape.NORTH_WEST;
                    }
                    if ($$16) {
                        $$11 = RailShape.NORTH_EAST;
                    }
                    if ($$15) {
                        $$11 = RailShape.SOUTH_WEST;
                    }
                    if ($$14) {
                        $$11 = RailShape.SOUTH_EAST;
                    }
                }
            }
        }
        if ($$11 == RailShape.NORTH_SOUTH) {
            if (BaseRailBlock.isRail(this.level, $$3.above())) {
                $$11 = RailShape.ASCENDING_NORTH;
            }
            if (BaseRailBlock.isRail(this.level, $$4.above())) {
                $$11 = RailShape.ASCENDING_SOUTH;
            }
        }
        if ($$11 == RailShape.EAST_WEST) {
            if (BaseRailBlock.isRail(this.level, $$6.above())) {
                $$11 = RailShape.ASCENDING_EAST;
            }
            if (BaseRailBlock.isRail(this.level, $$5.above())) {
                $$11 = RailShape.ASCENDING_WEST;
            }
        }
        if ($$11 == null) {
            $$11 = railShape2;
        }
        this.updateConnections($$11);
        this.state = (BlockState) this.state.m_61124_(this.block.getShapeProperty(), $$11);
        if (boolean1 || this.level.getBlockState(this.pos) != this.state) {
            this.level.setBlock(this.pos, this.state, 3);
            for (int $$18 = 0; $$18 < this.connections.size(); $$18++) {
                RailState $$19 = this.getRail((BlockPos) this.connections.get($$18));
                if ($$19 != null) {
                    $$19.removeSoftConnections();
                    if ($$19.canConnectTo(this)) {
                        $$19.connectTo(this);
                    }
                }
            }
        }
        return this;
    }

    public BlockState getState() {
        return this.state;
    }
}