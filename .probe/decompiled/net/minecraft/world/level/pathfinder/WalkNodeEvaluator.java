package net.minecraft.world.level.pathfinder;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WalkNodeEvaluator extends NodeEvaluator {

    public static final double SPACE_BETWEEN_WALL_POSTS = 0.5;

    private static final double DEFAULT_MOB_JUMP_HEIGHT = 1.125;

    private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap();

    private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();

    @Override
    public void prepare(PathNavigationRegion pathNavigationRegion0, Mob mob1) {
        super.prepare(pathNavigationRegion0, mob1);
        mob1.onPathfindingStart();
    }

    @Override
    public void done() {
        this.f_77313_.onPathfindingDone();
        this.pathTypesByPosCache.clear();
        this.collisionCache.clear();
        super.done();
    }

    @Override
    public Node getStart() {
        BlockPos.MutableBlockPos $$0 = new BlockPos.MutableBlockPos();
        int $$1 = this.f_77313_.m_146904_();
        BlockState $$2 = this.f_77312_.getBlockState($$0.set(this.f_77313_.m_20185_(), (double) $$1, this.f_77313_.m_20189_()));
        if (!this.f_77313_.m_203441_($$2.m_60819_())) {
            if (this.m_77361_() && this.f_77313_.m_20069_()) {
                while (true) {
                    if (!$$2.m_60713_(Blocks.WATER) && $$2.m_60819_() != Fluids.WATER.getSource(false)) {
                        $$1--;
                        break;
                    }
                    $$2 = this.f_77312_.getBlockState($$0.set(this.f_77313_.m_20185_(), (double) (++$$1), this.f_77313_.m_20189_()));
                }
            } else if (this.f_77313_.m_20096_()) {
                $$1 = Mth.floor(this.f_77313_.m_20186_() + 0.5);
            } else {
                BlockPos $$3 = this.f_77313_.m_20183_();
                while ((this.f_77312_.getBlockState($$3).m_60795_() || this.f_77312_.getBlockState($$3).m_60647_(this.f_77312_, $$3, PathComputationType.LAND)) && $$3.m_123342_() > this.f_77313_.m_9236_().m_141937_()) {
                    $$3 = $$3.below();
                }
                $$1 = $$3.above().m_123342_();
            }
        } else {
            while (this.f_77313_.m_203441_($$2.m_60819_())) {
                $$2 = this.f_77312_.getBlockState($$0.set(this.f_77313_.m_20185_(), (double) (++$$1), this.f_77313_.m_20189_()));
            }
            $$1--;
        }
        BlockPos $$4 = this.f_77313_.m_20183_();
        if (!this.canStartAt($$0.set($$4.m_123341_(), $$1, $$4.m_123343_()))) {
            AABB $$5 = this.f_77313_.m_20191_();
            if (this.canStartAt($$0.set($$5.minX, (double) $$1, $$5.minZ)) || this.canStartAt($$0.set($$5.minX, (double) $$1, $$5.maxZ)) || this.canStartAt($$0.set($$5.maxX, (double) $$1, $$5.minZ)) || this.canStartAt($$0.set($$5.maxX, (double) $$1, $$5.maxZ))) {
                return this.getStartNode($$0);
            }
        }
        return this.getStartNode(new BlockPos($$4.m_123341_(), $$1, $$4.m_123343_()));
    }

    protected Node getStartNode(BlockPos blockPos0) {
        Node $$1 = this.m_77349_(blockPos0);
        $$1.type = this.getBlockPathType(this.f_77313_, $$1.asBlockPos());
        $$1.costMalus = this.f_77313_.getPathfindingMalus($$1.type);
        return $$1;
    }

    protected boolean canStartAt(BlockPos blockPos0) {
        BlockPathTypes $$1 = this.getBlockPathType(this.f_77313_, blockPos0);
        return $$1 != BlockPathTypes.OPEN && this.f_77313_.getPathfindingMalus($$1) >= 0.0F;
    }

    @Override
    public Target getGoal(double double0, double double1, double double2) {
        return this.m_230615_(this.m_5676_(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2)));
    }

    @Override
    public int getNeighbors(Node[] node0, Node node1) {
        int $$2 = 0;
        int $$3 = 0;
        BlockPathTypes $$4 = this.getCachedBlockType(this.f_77313_, node1.x, node1.y + 1, node1.z);
        BlockPathTypes $$5 = this.getCachedBlockType(this.f_77313_, node1.x, node1.y, node1.z);
        if (this.f_77313_.getPathfindingMalus($$4) >= 0.0F && $$5 != BlockPathTypes.STICKY_HONEY) {
            $$3 = Mth.floor(Math.max(1.0F, this.f_77313_.m_274421_()));
        }
        double $$6 = this.getFloorLevel(new BlockPos(node1.x, node1.y, node1.z));
        Node $$7 = this.findAcceptedNode(node1.x, node1.y, node1.z + 1, $$3, $$6, Direction.SOUTH, $$5);
        if (this.isNeighborValid($$7, node1)) {
            node0[$$2++] = $$7;
        }
        Node $$8 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z, $$3, $$6, Direction.WEST, $$5);
        if (this.isNeighborValid($$8, node1)) {
            node0[$$2++] = $$8;
        }
        Node $$9 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z, $$3, $$6, Direction.EAST, $$5);
        if (this.isNeighborValid($$9, node1)) {
            node0[$$2++] = $$9;
        }
        Node $$10 = this.findAcceptedNode(node1.x, node1.y, node1.z - 1, $$3, $$6, Direction.NORTH, $$5);
        if (this.isNeighborValid($$10, node1)) {
            node0[$$2++] = $$10;
        }
        Node $$11 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z - 1, $$3, $$6, Direction.NORTH, $$5);
        if (this.isDiagonalValid(node1, $$8, $$10, $$11)) {
            node0[$$2++] = $$11;
        }
        Node $$12 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z - 1, $$3, $$6, Direction.NORTH, $$5);
        if (this.isDiagonalValid(node1, $$9, $$10, $$12)) {
            node0[$$2++] = $$12;
        }
        Node $$13 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z + 1, $$3, $$6, Direction.SOUTH, $$5);
        if (this.isDiagonalValid(node1, $$8, $$7, $$13)) {
            node0[$$2++] = $$13;
        }
        Node $$14 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z + 1, $$3, $$6, Direction.SOUTH, $$5);
        if (this.isDiagonalValid(node1, $$9, $$7, $$14)) {
            node0[$$2++] = $$14;
        }
        return $$2;
    }

    protected boolean isNeighborValid(@Nullable Node node0, Node node1) {
        return node0 != null && !node0.closed && (node0.costMalus >= 0.0F || node1.costMalus < 0.0F);
    }

    protected boolean isDiagonalValid(Node node0, @Nullable Node node1, @Nullable Node node2, @Nullable Node node3) {
        if (node3 == null || node2 == null || node1 == null) {
            return false;
        } else if (node3.closed) {
            return false;
        } else if (node2.y > node0.y || node1.y > node0.y) {
            return false;
        } else if (node1.type != BlockPathTypes.WALKABLE_DOOR && node2.type != BlockPathTypes.WALKABLE_DOOR && node3.type != BlockPathTypes.WALKABLE_DOOR) {
            boolean $$4 = node2.type == BlockPathTypes.FENCE && node1.type == BlockPathTypes.FENCE && (double) this.f_77313_.m_20205_() < 0.5;
            return node3.costMalus >= 0.0F && (node2.y < node0.y || node2.costMalus >= 0.0F || $$4) && (node1.y < node0.y || node1.costMalus >= 0.0F || $$4);
        } else {
            return false;
        }
    }

    private static boolean doesBlockHavePartialCollision(BlockPathTypes blockPathTypes0) {
        return blockPathTypes0 == BlockPathTypes.FENCE || blockPathTypes0 == BlockPathTypes.DOOR_WOOD_CLOSED || blockPathTypes0 == BlockPathTypes.DOOR_IRON_CLOSED;
    }

    private boolean canReachWithoutCollision(Node node0) {
        AABB $$1 = this.f_77313_.m_20191_();
        Vec3 $$2 = new Vec3((double) node0.x - this.f_77313_.m_20185_() + $$1.getXsize() / 2.0, (double) node0.y - this.f_77313_.m_20186_() + $$1.getYsize() / 2.0, (double) node0.z - this.f_77313_.m_20189_() + $$1.getZsize() / 2.0);
        int $$3 = Mth.ceil($$2.length() / $$1.getSize());
        $$2 = $$2.scale((double) (1.0F / (float) $$3));
        for (int $$4 = 1; $$4 <= $$3; $$4++) {
            $$1 = $$1.move($$2);
            if (this.hasCollisions($$1)) {
                return false;
            }
        }
        return true;
    }

    protected double getFloorLevel(BlockPos blockPos0) {
        return (this.m_77361_() || this.isAmphibious()) && this.f_77312_.getFluidState(blockPos0).is(FluidTags.WATER) ? (double) blockPos0.m_123342_() + 0.5 : getFloorLevel(this.f_77312_, blockPos0);
    }

    public static double getFloorLevel(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockPos $$2 = blockPos1.below();
        VoxelShape $$3 = blockGetter0.getBlockState($$2).m_60812_(blockGetter0, $$2);
        return (double) $$2.m_123342_() + ($$3.isEmpty() ? 0.0 : $$3.max(Direction.Axis.Y));
    }

    protected boolean isAmphibious() {
        return false;
    }

    @Nullable
    protected Node findAcceptedNode(int int0, int int1, int int2, int int3, double double4, Direction direction5, BlockPathTypes blockPathTypes6) {
        Node $$7 = null;
        BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos();
        double $$9 = this.getFloorLevel($$8.set(int0, int1, int2));
        if ($$9 - double4 > this.getMobJumpHeight()) {
            return null;
        } else {
            BlockPathTypes $$10 = this.getCachedBlockType(this.f_77313_, int0, int1, int2);
            float $$11 = this.f_77313_.getPathfindingMalus($$10);
            double $$12 = (double) this.f_77313_.m_20205_() / 2.0;
            if ($$11 >= 0.0F) {
                $$7 = this.getNodeAndUpdateCostToMax(int0, int1, int2, $$10, $$11);
            }
            if (doesBlockHavePartialCollision(blockPathTypes6) && $$7 != null && $$7.costMalus >= 0.0F && !this.canReachWithoutCollision($$7)) {
                $$7 = null;
            }
            if ($$10 != BlockPathTypes.WALKABLE && (!this.isAmphibious() || $$10 != BlockPathTypes.WATER)) {
                if (($$7 == null || $$7.costMalus < 0.0F) && int3 > 0 && ($$10 != BlockPathTypes.FENCE || this.m_255100_()) && $$10 != BlockPathTypes.UNPASSABLE_RAIL && $$10 != BlockPathTypes.TRAPDOOR && $$10 != BlockPathTypes.POWDER_SNOW) {
                    $$7 = this.findAcceptedNode(int0, int1 + 1, int2, int3 - 1, double4, direction5, blockPathTypes6);
                    if ($$7 != null && ($$7.type == BlockPathTypes.OPEN || $$7.type == BlockPathTypes.WALKABLE) && this.f_77313_.m_20205_() < 1.0F) {
                        double $$13 = (double) (int0 - direction5.getStepX()) + 0.5;
                        double $$14 = (double) (int2 - direction5.getStepZ()) + 0.5;
                        AABB $$15 = new AABB($$13 - $$12, this.getFloorLevel($$8.set($$13, (double) (int1 + 1), $$14)) + 0.001, $$14 - $$12, $$13 + $$12, (double) this.f_77313_.m_20206_() + this.getFloorLevel($$8.set((double) $$7.x, (double) $$7.y, (double) $$7.z)) - 0.002, $$14 + $$12);
                        if (this.hasCollisions($$15)) {
                            $$7 = null;
                        }
                    }
                }
                if (!this.isAmphibious() && $$10 == BlockPathTypes.WATER && !this.m_77361_()) {
                    if (this.getCachedBlockType(this.f_77313_, int0, int1 - 1, int2) != BlockPathTypes.WATER) {
                        return $$7;
                    }
                    while (int1 > this.f_77313_.m_9236_().m_141937_()) {
                        $$10 = this.getCachedBlockType(this.f_77313_, int0, --int1, int2);
                        if ($$10 != BlockPathTypes.WATER) {
                            return $$7;
                        }
                        $$7 = this.getNodeAndUpdateCostToMax(int0, int1, int2, $$10, this.f_77313_.getPathfindingMalus($$10));
                    }
                }
                if ($$10 == BlockPathTypes.OPEN) {
                    int $$16 = 0;
                    int $$17 = int1;
                    while ($$10 == BlockPathTypes.OPEN) {
                        if (--int1 < this.f_77313_.m_9236_().m_141937_()) {
                            return this.getBlockedNode(int0, $$17, int2);
                        }
                        if ($$16++ >= this.f_77313_.getMaxFallDistance()) {
                            return this.getBlockedNode(int0, int1, int2);
                        }
                        $$10 = this.getCachedBlockType(this.f_77313_, int0, int1, int2);
                        $$11 = this.f_77313_.getPathfindingMalus($$10);
                        if ($$10 != BlockPathTypes.OPEN && $$11 >= 0.0F) {
                            $$7 = this.getNodeAndUpdateCostToMax(int0, int1, int2, $$10, $$11);
                            break;
                        }
                        if ($$11 < 0.0F) {
                            return this.getBlockedNode(int0, int1, int2);
                        }
                    }
                }
                if (doesBlockHavePartialCollision($$10) && $$7 == null) {
                    $$7 = this.m_5676_(int0, int1, int2);
                    $$7.closed = true;
                    $$7.type = $$10;
                    $$7.costMalus = $$10.getMalus();
                }
                return $$7;
            } else {
                return $$7;
            }
        }
    }

    private double getMobJumpHeight() {
        return Math.max(1.125, (double) this.f_77313_.m_274421_());
    }

    private Node getNodeAndUpdateCostToMax(int int0, int int1, int int2, BlockPathTypes blockPathTypes3, float float4) {
        Node $$5 = this.m_5676_(int0, int1, int2);
        $$5.type = blockPathTypes3;
        $$5.costMalus = Math.max($$5.costMalus, float4);
        return $$5;
    }

    private Node getBlockedNode(int int0, int int1, int int2) {
        Node $$3 = this.m_5676_(int0, int1, int2);
        $$3.type = BlockPathTypes.BLOCKED;
        $$3.costMalus = -1.0F;
        return $$3;
    }

    private boolean hasCollisions(AABB aABB0) {
        return this.collisionCache.computeIfAbsent(aABB0, p_192973_ -> !this.f_77312_.m_45756_(this.f_77313_, aABB0));
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3, Mob mob4) {
        EnumSet<BlockPathTypes> $$5 = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes $$6 = BlockPathTypes.BLOCKED;
        $$6 = this.getBlockPathTypes(blockGetter0, int1, int2, int3, $$5, $$6, mob4.m_20183_());
        if ($$5.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else if ($$5.contains(BlockPathTypes.UNPASSABLE_RAIL)) {
            return BlockPathTypes.UNPASSABLE_RAIL;
        } else {
            BlockPathTypes $$7 = BlockPathTypes.BLOCKED;
            for (BlockPathTypes $$8 : $$5) {
                if (mob4.getPathfindingMalus($$8) < 0.0F) {
                    return $$8;
                }
                if (mob4.getPathfindingMalus($$8) >= mob4.getPathfindingMalus($$7)) {
                    $$7 = $$8;
                }
            }
            return $$6 == BlockPathTypes.OPEN && mob4.getPathfindingMalus($$7) == 0.0F && this.f_77315_ <= 1 ? BlockPathTypes.OPEN : $$7;
        }
    }

    public BlockPathTypes getBlockPathTypes(BlockGetter blockGetter0, int int1, int int2, int int3, EnumSet<BlockPathTypes> enumSetBlockPathTypes4, BlockPathTypes blockPathTypes5, BlockPos blockPos6) {
        for (int $$7 = 0; $$7 < this.f_77315_; $$7++) {
            for (int $$8 = 0; $$8 < this.f_77316_; $$8++) {
                for (int $$9 = 0; $$9 < this.f_77317_; $$9++) {
                    int $$10 = $$7 + int1;
                    int $$11 = $$8 + int2;
                    int $$12 = $$9 + int3;
                    BlockPathTypes $$13 = this.getBlockPathType(blockGetter0, $$10, $$11, $$12);
                    $$13 = this.evaluateBlockPathType(blockGetter0, blockPos6, $$13);
                    if ($$7 == 0 && $$8 == 0 && $$9 == 0) {
                        blockPathTypes5 = $$13;
                    }
                    enumSetBlockPathTypes4.add($$13);
                }
            }
        }
        return blockPathTypes5;
    }

    protected BlockPathTypes evaluateBlockPathType(BlockGetter blockGetter0, BlockPos blockPos1, BlockPathTypes blockPathTypes2) {
        boolean $$3 = this.m_77357_();
        if (blockPathTypes2 == BlockPathTypes.DOOR_WOOD_CLOSED && this.m_77360_() && $$3) {
            blockPathTypes2 = BlockPathTypes.WALKABLE_DOOR;
        }
        if (blockPathTypes2 == BlockPathTypes.DOOR_OPEN && !$$3) {
            blockPathTypes2 = BlockPathTypes.BLOCKED;
        }
        if (blockPathTypes2 == BlockPathTypes.RAIL && !(blockGetter0.getBlockState(blockPos1).m_60734_() instanceof BaseRailBlock) && !(blockGetter0.getBlockState(blockPos1.below()).m_60734_() instanceof BaseRailBlock)) {
            blockPathTypes2 = BlockPathTypes.UNPASSABLE_RAIL;
        }
        return blockPathTypes2;
    }

    protected BlockPathTypes getBlockPathType(Mob mob0, BlockPos blockPos1) {
        return this.getCachedBlockType(mob0, blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_());
    }

    protected BlockPathTypes getCachedBlockType(Mob mob0, int int1, int int2, int int3) {
        return (BlockPathTypes) this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(int1, int2, int3), p_265015_ -> this.getBlockPathType(this.f_77312_, int1, int2, int3, mob0));
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3) {
        return getBlockPathTypeStatic(blockGetter0, new BlockPos.MutableBlockPos(int1, int2, int3));
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter blockGetter0, BlockPos.MutableBlockPos blockPosMutableBlockPos1) {
        int $$2 = blockPosMutableBlockPos1.m_123341_();
        int $$3 = blockPosMutableBlockPos1.m_123342_();
        int $$4 = blockPosMutableBlockPos1.m_123343_();
        BlockPathTypes $$5 = getBlockPathTypeRaw(blockGetter0, blockPosMutableBlockPos1);
        if ($$5 == BlockPathTypes.OPEN && $$3 >= blockGetter0.m_141937_() + 1) {
            BlockPathTypes $$6 = getBlockPathTypeRaw(blockGetter0, blockPosMutableBlockPos1.set($$2, $$3 - 1, $$4));
            $$5 = $$6 != BlockPathTypes.WALKABLE && $$6 != BlockPathTypes.OPEN && $$6 != BlockPathTypes.WATER && $$6 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if ($$6 == BlockPathTypes.DAMAGE_FIRE) {
                $$5 = BlockPathTypes.DAMAGE_FIRE;
            }
            if ($$6 == BlockPathTypes.DAMAGE_OTHER) {
                $$5 = BlockPathTypes.DAMAGE_OTHER;
            }
            if ($$6 == BlockPathTypes.STICKY_HONEY) {
                $$5 = BlockPathTypes.STICKY_HONEY;
            }
            if ($$6 == BlockPathTypes.POWDER_SNOW) {
                $$5 = BlockPathTypes.DANGER_POWDER_SNOW;
            }
            if ($$6 == BlockPathTypes.DAMAGE_CAUTIOUS) {
                $$5 = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }
        if ($$5 == BlockPathTypes.WALKABLE) {
            $$5 = checkNeighbourBlocks(blockGetter0, blockPosMutableBlockPos1.set($$2, $$3, $$4), $$5);
        }
        return $$5;
    }

    public static BlockPathTypes checkNeighbourBlocks(BlockGetter blockGetter0, BlockPos.MutableBlockPos blockPosMutableBlockPos1, BlockPathTypes blockPathTypes2) {
        int $$3 = blockPosMutableBlockPos1.m_123341_();
        int $$4 = blockPosMutableBlockPos1.m_123342_();
        int $$5 = blockPosMutableBlockPos1.m_123343_();
        for (int $$6 = -1; $$6 <= 1; $$6++) {
            for (int $$7 = -1; $$7 <= 1; $$7++) {
                for (int $$8 = -1; $$8 <= 1; $$8++) {
                    if ($$6 != 0 || $$8 != 0) {
                        blockPosMutableBlockPos1.set($$3 + $$6, $$4 + $$7, $$5 + $$8);
                        BlockState $$9 = blockGetter0.getBlockState(blockPosMutableBlockPos1);
                        if ($$9.m_60713_(Blocks.CACTUS) || $$9.m_60713_(Blocks.SWEET_BERRY_BUSH)) {
                            return BlockPathTypes.DANGER_OTHER;
                        }
                        if (isBurningBlock($$9)) {
                            return BlockPathTypes.DANGER_FIRE;
                        }
                        if (blockGetter0.getFluidState(blockPosMutableBlockPos1).is(FluidTags.WATER)) {
                            return BlockPathTypes.WATER_BORDER;
                        }
                        if ($$9.m_60713_(Blocks.WITHER_ROSE) || $$9.m_60713_(Blocks.POINTED_DRIPSTONE)) {
                            return BlockPathTypes.DAMAGE_CAUTIOUS;
                        }
                    }
                }
            }
        }
        return blockPathTypes2;
    }

    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockState $$2 = blockGetter0.getBlockState(blockPos1);
        Block $$3 = $$2.m_60734_();
        if ($$2.m_60795_()) {
            return BlockPathTypes.OPEN;
        } else if ($$2.m_204336_(BlockTags.TRAPDOORS) || $$2.m_60713_(Blocks.LILY_PAD) || $$2.m_60713_(Blocks.BIG_DRIPLEAF)) {
            return BlockPathTypes.TRAPDOOR;
        } else if ($$2.m_60713_(Blocks.POWDER_SNOW)) {
            return BlockPathTypes.POWDER_SNOW;
        } else if ($$2.m_60713_(Blocks.CACTUS) || $$2.m_60713_(Blocks.SWEET_BERRY_BUSH)) {
            return BlockPathTypes.DAMAGE_OTHER;
        } else if ($$2.m_60713_(Blocks.HONEY_BLOCK)) {
            return BlockPathTypes.STICKY_HONEY;
        } else if ($$2.m_60713_(Blocks.COCOA)) {
            return BlockPathTypes.COCOA;
        } else if (!$$2.m_60713_(Blocks.WITHER_ROSE) && !$$2.m_60713_(Blocks.POINTED_DRIPSTONE)) {
            FluidState $$4 = blockGetter0.getFluidState(blockPos1);
            if ($$4.is(FluidTags.LAVA)) {
                return BlockPathTypes.LAVA;
            } else if (isBurningBlock($$2)) {
                return BlockPathTypes.DAMAGE_FIRE;
            } else if ($$3 instanceof DoorBlock $$5) {
                if ((Boolean) $$2.m_61143_(DoorBlock.OPEN)) {
                    return BlockPathTypes.DOOR_OPEN;
                } else {
                    return $$5.type().canOpenByHand() ? BlockPathTypes.DOOR_WOOD_CLOSED : BlockPathTypes.DOOR_IRON_CLOSED;
                }
            } else if ($$3 instanceof BaseRailBlock) {
                return BlockPathTypes.RAIL;
            } else if ($$3 instanceof LeavesBlock) {
                return BlockPathTypes.LEAVES;
            } else if (!$$2.m_204336_(BlockTags.FENCES) && !$$2.m_204336_(BlockTags.WALLS) && (!($$3 instanceof FenceGateBlock) || (Boolean) $$2.m_61143_(FenceGateBlock.OPEN))) {
                if (!$$2.m_60647_(blockGetter0, blockPos1, PathComputationType.LAND)) {
                    return BlockPathTypes.BLOCKED;
                } else {
                    return $$4.is(FluidTags.WATER) ? BlockPathTypes.WATER : BlockPathTypes.OPEN;
                }
            } else {
                return BlockPathTypes.FENCE;
            }
        } else {
            return BlockPathTypes.DAMAGE_CAUTIOUS;
        }
    }

    public static boolean isBurningBlock(BlockState blockState0) {
        return blockState0.m_204336_(BlockTags.FIRE) || blockState0.m_60713_(Blocks.LAVA) || blockState0.m_60713_(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(blockState0) || blockState0.m_60713_(Blocks.LAVA_CAULDRON);
    }
}