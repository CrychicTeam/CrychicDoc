package com.mna.entities.constructs.movement;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
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
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.Target;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ConstructNodeEvaluator extends NodeEvaluator {

    public static final double SPACE_BETWEEN_WALL_POSTS = 0.5;

    protected float oldWaterCost;

    private float oldWalkableCost;

    private float oldWaterBorderCost;

    private boolean amphibious = true;

    private boolean flying = false;

    private boolean isInWater = false;

    private final Long2ObjectMap<BlockPathTypes> pathTypeByPosCache = new Long2ObjectOpenHashMap();

    private final Object2BooleanMap<AABB> collisionCache = new Object2BooleanOpenHashMap();

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
        super.prepare(region, mob);
        this.isInWater = mob.m_20069_();
        if (this.flying && !this.isInWater) {
            this.prepare_flying(mob);
        } else {
            this.prepare_ground(mob);
        }
    }

    @Override
    public void done() {
        this.f_77313_.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        this.f_77313_.setPathfindingMalus(BlockPathTypes.WALKABLE, this.oldWalkableCost);
        this.f_77313_.setPathfindingMalus(BlockPathTypes.WATER_BORDER, this.oldWaterBorderCost);
        this.pathTypeByPosCache.clear();
        this.collisionCache.clear();
        super.done();
    }

    @Nullable
    @Override
    protected Node getNode(int x, int y, int z) {
        if (this.flying && !this.isInWater) {
            Node node = null;
            BlockPathTypes pathType = this.getCachedBlockPathType(x, y, z);
            float f = this.f_77313_.getPathfindingMalus(pathType);
            if (f >= 0.0F) {
                node = super.getNode(x, y, z);
                node.type = pathType;
                node.costMalus = Math.max(node.costMalus, f);
                if (pathType == BlockPathTypes.WALKABLE) {
                    node.costMalus++;
                }
            }
            return node;
        } else {
            return super.getNode(x, y, z);
        }
    }

    @Override
    public Node getStart() {
        return this.flying && !this.isInWater ? this.getStart_flying() : this.getStart_ground();
    }

    @Override
    public Target getGoal(double x, double y, double z) {
        return this.flying && !this.isInWater ? new Target(super.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z))) : new Target(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
    }

    @Override
    public int getNeighbors(Node[] node0, Node current) {
        return this.flying && !this.isInWater ? this.getNeighbors_flying(node0, current) : this.getNeighbors_ground(node0, current);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter, int x, int y, int z, Mob mob) {
        return this.flying && !this.isInWater ? this.getBlockPathType_flying(blockGetter, x, y, z, mob) : this.getBlockPathType_ground(blockGetter, x, y, z, mob);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter, int x, int y, int z) {
        return this.flying && !this.isInWater ? this.getBlockPathType_flying(blockGetter, x, y, z, this.f_77313_) : getBlockPathTypeStatic(blockGetter, new BlockPos.MutableBlockPos(x, y, z), this.f_77313_);
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    public void setAmphibious(boolean amphibious) {
        this.amphibious = amphibious;
    }

    private boolean hasPositiveMalus(BlockPos pos) {
        BlockPathTypes blockpathtypes = this.getBlockPathType(this.f_77313_, pos);
        return this.f_77313_.getPathfindingMalus(blockpathtypes) >= 0.0F;
    }

    protected boolean isNeighborValid(@Nullable Node neighbor, Node current) {
        return neighbor != null && !neighbor.closed && (neighbor.costMalus >= 0.0F || current.costMalus < 0.0F);
    }

    private boolean hasMalus(@Nullable Node node) {
        return node != null && node.costMalus >= 0.0F;
    }

    private boolean isOpen(@Nullable Node node) {
        return node != null && !node.closed;
    }

    protected boolean isDiagonalValid(Node current, @Nullable Node xOffsetNode, @Nullable Node zOffsetNode, @Nullable Node diagonalNode) {
        if (diagonalNode != null && zOffsetNode != null && xOffsetNode != null) {
            if (diagonalNode.closed) {
                return false;
            }
            if (zOffsetNode.y <= current.y && xOffsetNode.y <= current.y && xOffsetNode.type != BlockPathTypes.WALKABLE_DOOR && zOffsetNode.type != BlockPathTypes.WALKABLE_DOOR && diagonalNode.type != BlockPathTypes.WALKABLE_DOOR) {
                boolean canFitThrough = zOffsetNode.type == BlockPathTypes.FENCE && xOffsetNode.type == BlockPathTypes.FENCE && (double) this.f_77313_.m_20205_() < 0.5;
                return diagonalNode.costMalus >= 0.0F && (zOffsetNode.y < current.y || zOffsetNode.costMalus >= 0.0F || canFitThrough) && (xOffsetNode.y < current.y || xOffsetNode.costMalus >= 0.0F || canFitThrough);
            }
        }
        return false;
    }

    private boolean canReachWithoutCollision(Node target) {
        Vec3 delta = new Vec3((double) target.x - this.f_77313_.m_20185_(), (double) target.y - this.f_77313_.m_20186_(), (double) target.z - this.f_77313_.m_20189_());
        AABB mobBB = this.f_77313_.m_20191_();
        int i = Mth.ceil(delta.length() / mobBB.getSize());
        delta = delta.scale((double) (1.0F / (float) i));
        for (int j = 1; j <= i; j++) {
            mobBB = mobBB.move(delta);
            if (this.hasCollisions(mobBB)) {
                return false;
            }
        }
        return true;
    }

    protected double getFloorLevel(BlockPos pos) {
        return getFloorLevel(this.f_77312_, pos);
    }

    protected boolean isAmphibious() {
        return this.amphibious;
    }

    @Nullable
    protected Node findAcceptedNode(int x, int y, int z, int stepUpHeight, double floorLevel, Direction direction, BlockPathTypes pathType) {
        Node node = null;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        double positionFloorLevel = this.getFloorLevel(pos.set(x, y, z));
        if (positionFloorLevel - floorLevel > 1.125) {
            return null;
        } else {
            BlockPathTypes xyzPathType = this.getCachedBlockType(this.f_77313_, x, y, z);
            float pathfindingMalus = this.f_77313_.getPathfindingMalus(xyzPathType);
            double halfMobWidth = (double) this.f_77313_.m_20205_() / 2.0;
            if (pathfindingMalus >= 0.0F) {
                node = this.getNode(x, y, z);
                node.type = xyzPathType;
                node.costMalus = Math.max(node.costMalus, pathfindingMalus);
            }
            if (pathType == BlockPathTypes.FENCE && node != null && node.costMalus >= 0.0F && !this.canReachWithoutCollision(node)) {
                node = null;
            }
            if (xyzPathType != BlockPathTypes.WALKABLE && (!this.isAmphibious() || xyzPathType != BlockPathTypes.WATER)) {
                if ((node == null || node.costMalus < 0.0F) && stepUpHeight > 0 && xyzPathType != BlockPathTypes.FENCE && xyzPathType != BlockPathTypes.UNPASSABLE_RAIL && xyzPathType != BlockPathTypes.TRAPDOOR && xyzPathType != BlockPathTypes.POWDER_SNOW) {
                    node = this.findAcceptedNode(x, y + 1, z, stepUpHeight - 1, floorLevel, direction, pathType);
                    if (node != null && (node.type == BlockPathTypes.OPEN || node.type == BlockPathTypes.WALKABLE) && this.f_77313_.m_20205_() < 1.0F) {
                        double deltaX = (double) (x - direction.getStepX()) + 0.5;
                        double deltaZ = (double) (z - direction.getStepZ()) + 0.5;
                        AABB aabb = new AABB(deltaX - halfMobWidth, getFloorLevel(this.f_77312_, pos.set(deltaX, (double) (y + 1), deltaZ)) + 0.001, deltaZ - halfMobWidth, deltaX + halfMobWidth, (double) this.f_77313_.m_20206_() + getFloorLevel(this.f_77312_, pos.set((double) node.x, (double) node.y, (double) node.z)) - 0.002, deltaZ + halfMobWidth);
                        if (this.hasCollisions(aabb)) {
                            node = null;
                        }
                    }
                }
                if (!this.isAmphibious() && xyzPathType == BlockPathTypes.WATER && !this.m_77361_()) {
                    if (this.getCachedBlockType(this.f_77313_, x, y - 1, z) != BlockPathTypes.WATER) {
                        return node;
                    }
                    while (y > this.f_77313_.m_9236_().m_141937_()) {
                        xyzPathType = this.getCachedBlockType(this.f_77313_, x, --y, z);
                        if (xyzPathType != BlockPathTypes.WATER) {
                            return node;
                        }
                        node = this.getNode(x, y, z);
                        node.type = xyzPathType;
                        node.costMalus = Math.max(node.costMalus, this.f_77313_.getPathfindingMalus(xyzPathType));
                    }
                }
                if (xyzPathType == BlockPathTypes.OPEN) {
                    int j = 0;
                    int i = y;
                    while (xyzPathType == BlockPathTypes.OPEN) {
                        if (--y < this.f_77313_.m_9236_().m_141937_()) {
                            Node originalNode = this.getNode(x, i, z);
                            originalNode.type = BlockPathTypes.BLOCKED;
                            originalNode.costMalus = -1.0F;
                            return originalNode;
                        }
                        if (j++ >= this.f_77313_.getMaxFallDistance()) {
                            Node offsetNode = this.getNode(x, y, z);
                            offsetNode.type = BlockPathTypes.BLOCKED;
                            offsetNode.costMalus = -1.0F;
                            return offsetNode;
                        }
                        xyzPathType = this.getCachedBlockType(this.f_77313_, x, y, z);
                        pathfindingMalus = this.f_77313_.getPathfindingMalus(xyzPathType);
                        if (xyzPathType != BlockPathTypes.OPEN && pathfindingMalus >= 0.0F) {
                            node = this.getNode(x, y, z);
                            node.type = xyzPathType;
                            node.costMalus = Math.max(node.costMalus, pathfindingMalus);
                            break;
                        }
                        if (pathfindingMalus < 0.0F) {
                            Node offsetNode = this.getNode(x, y, z);
                            offsetNode.type = BlockPathTypes.BLOCKED;
                            offsetNode.costMalus = -1.0F;
                            return offsetNode;
                        }
                    }
                }
                if (xyzPathType == BlockPathTypes.FENCE) {
                    node = this.getNode(x, y, z);
                    node.closed = true;
                    node.type = xyzPathType;
                    node.costMalus = xyzPathType.getMalus();
                }
                return node;
            } else {
                return node;
            }
        }
    }

    private boolean hasCollisions(AABB boundingBox) {
        return this.collisionCache.computeIfAbsent(boundingBox, value -> !this.f_77312_.m_45756_(this.f_77313_, boundingBox));
    }

    public BlockPathTypes getBlockPathTypes(BlockGetter pLevel, int pXOffset, int pYOffset, int pZOffset, EnumSet<BlockPathTypes> pOutput, BlockPathTypes pFallbackPathType, BlockPos pPos) {
        for (int i = 0; i < this.f_77315_; i++) {
            for (int j = 0; j < this.f_77316_; j++) {
                for (int k = 0; k < this.f_77317_; k++) {
                    int l = i + pXOffset;
                    int i1 = j + pYOffset;
                    int j1 = k + pZOffset;
                    BlockPathTypes blockpathtypes = getBlockPathTypeStatic(pLevel, new BlockPos.MutableBlockPos(l, i1, j1), this.f_77313_);
                    blockpathtypes = this.evaluateBlockPathType(pLevel, pPos, blockpathtypes);
                    if (i == 0 && j == 0 && k == 0) {
                        pFallbackPathType = blockpathtypes;
                    }
                    pOutput.add(blockpathtypes);
                }
            }
        }
        return pFallbackPathType;
    }

    protected BlockPathTypes evaluateBlockPathType(BlockGetter pLevel, BlockPos pPos, BlockPathTypes pPathTypes) {
        boolean flag = this.m_77357_();
        if (pPathTypes == BlockPathTypes.DOOR_WOOD_CLOSED && this.m_77360_() && flag) {
            pPathTypes = BlockPathTypes.WALKABLE_DOOR;
        }
        if (pPathTypes == BlockPathTypes.DOOR_OPEN && !flag) {
            pPathTypes = BlockPathTypes.BLOCKED;
        }
        if (pPathTypes == BlockPathTypes.RAIL && !(pLevel.getBlockState(pPos).m_60734_() instanceof BaseRailBlock) && !(pLevel.getBlockState(pPos.below()).m_60734_() instanceof BaseRailBlock)) {
            pPathTypes = BlockPathTypes.UNPASSABLE_RAIL;
        }
        return pPathTypes;
    }

    private BlockPathTypes getBlockPathType(Mob mob, BlockPos position) {
        return this.getCachedBlockType(mob, position.m_123341_(), position.m_123342_(), position.m_123343_());
    }

    private BlockPathTypes getCachedBlockPathType(int pX, int pY, int pZ) {
        return (BlockPathTypes) this.pathTypeByPosCache.computeIfAbsent(BlockPos.asLong(pX, pY, pZ), p_265010_ -> this.getBlockPathType(this.f_77312_, pX, pY, pZ, this.f_77313_));
    }

    protected BlockPathTypes getCachedBlockType(Mob pEntity, int pX, int pY, int pZ) {
        return (BlockPathTypes) this.pathTypeByPosCache.computeIfAbsent(BlockPos.asLong(pX, pY, pZ), p_265015_ -> this.getBlockPathType(this.f_77312_, pX, pY, pZ, pEntity));
    }

    protected void prepare_ground(Mob mob) {
        this.oldWaterCost = mob.getPathfindingMalus(BlockPathTypes.WATER);
        mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.oldWalkableCost = mob.getPathfindingMalus(BlockPathTypes.WALKABLE);
        mob.setPathfindingMalus(BlockPathTypes.WALKABLE, 6.0F);
        this.oldWaterBorderCost = mob.getPathfindingMalus(BlockPathTypes.WATER_BORDER);
        mob.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0F);
    }

    protected void prepare_flying(Mob mob) {
        this.pathTypeByPosCache.clear();
        this.oldWaterCost = mob.getPathfindingMalus(BlockPathTypes.WATER);
    }

    protected Node getStart_ground() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.f_77313_.m_146904_();
        BlockState blockstate = this.f_77312_.getBlockState(blockpos$mutableblockpos.set(this.f_77313_.m_20185_(), (double) i, this.f_77313_.m_20189_()));
        if (!this.f_77313_.m_203441_(blockstate.m_60819_())) {
            if (this.m_77361_() && this.f_77313_.m_20069_()) {
                while (true) {
                    if (!blockstate.m_60713_(Blocks.WATER) && blockstate.m_60819_() != Fluids.WATER.getSource(false)) {
                        i--;
                        break;
                    }
                    blockstate = this.f_77312_.getBlockState(blockpos$mutableblockpos.set(this.f_77313_.m_20185_(), (double) (++i), this.f_77313_.m_20189_()));
                }
            } else if (this.f_77313_.m_20096_()) {
                i = Mth.floor(this.f_77313_.m_20186_() + 0.5);
            } else {
                BlockPos blockpos = this.f_77313_.m_20183_();
                while ((this.f_77312_.getBlockState(blockpos).m_60795_() || this.f_77312_.getBlockState(blockpos).m_60647_(this.f_77312_, blockpos, PathComputationType.LAND)) && blockpos.m_123342_() > this.f_77313_.m_9236_().m_141937_()) {
                    blockpos = blockpos.below();
                }
                i = blockpos.above().m_123342_();
            }
        } else {
            while (this.f_77313_.m_203441_(blockstate.m_60819_())) {
                blockstate = this.f_77312_.getBlockState(blockpos$mutableblockpos.set(this.f_77313_.m_20185_(), (double) (++i), this.f_77313_.m_20189_()));
            }
            i--;
        }
        BlockPos blockpos1 = this.f_77313_.m_20183_();
        BlockPathTypes blockpathtypes = this.getCachedBlockType(this.f_77313_, blockpos1.m_123341_(), i, blockpos1.m_123343_());
        if (this.f_77313_.getPathfindingMalus(blockpathtypes) < 0.0F) {
            AABB aabb = this.f_77313_.m_20191_();
            if (this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.minX, (double) i, aabb.minZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.minX, (double) i, aabb.maxZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.maxX, (double) i, aabb.minZ)) || this.hasPositiveMalus(blockpos$mutableblockpos.set(aabb.maxX, (double) i, aabb.maxZ))) {
                Node node = this.m_77349_(blockpos$mutableblockpos);
                node.type = this.getBlockPathType(this.f_77313_, node.asBlockPos());
                node.costMalus = this.f_77313_.getPathfindingMalus(node.type);
                return node;
            }
        }
        Node node1 = this.getNode(blockpos1.m_123341_(), i, blockpos1.m_123343_());
        node1.type = this.getBlockPathType(this.f_77313_, node1.asBlockPos());
        node1.costMalus = this.f_77313_.getPathfindingMalus(node1.type);
        return node1;
    }

    protected Node getStart_flying() {
        int i;
        if (this.m_77361_() && this.f_77313_.m_20069_()) {
            i = this.f_77313_.m_146904_();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(this.f_77313_.m_20185_(), (double) i, this.f_77313_.m_20189_());
            for (BlockState blockstate = this.f_77312_.getBlockState(blockpos$mutableblockpos); blockstate.m_60713_(Blocks.WATER); blockstate = this.f_77312_.getBlockState(blockpos$mutableblockpos)) {
                blockpos$mutableblockpos.set(this.f_77313_.m_20185_(), (double) (++i), this.f_77313_.m_20189_());
            }
        } else {
            i = Mth.floor(this.f_77313_.m_20186_() + 0.5);
        }
        BlockPos blockpos1 = this.f_77313_.m_20183_();
        BlockPathTypes blockpathtypes1 = this.getCachedBlockPathType(blockpos1.m_123341_(), i, blockpos1.m_123343_());
        if (this.f_77313_.getPathfindingMalus(blockpathtypes1) < 0.0F) {
            UnmodifiableIterator var4 = ImmutableSet.of(BlockPos.containing(this.f_77313_.m_20191_().minX, (double) i, this.f_77313_.m_20191_().minZ), BlockPos.containing(this.f_77313_.m_20191_().minX, (double) i, this.f_77313_.m_20191_().maxZ), BlockPos.containing(this.f_77313_.m_20191_().maxX, (double) i, this.f_77313_.m_20191_().minZ), BlockPos.containing(this.f_77313_.m_20191_().maxX, (double) i, this.f_77313_.m_20191_().maxZ)).iterator();
            while (var4.hasNext()) {
                BlockPos blockpos = (BlockPos) var4.next();
                BlockPathTypes blockpathtypes = this.getCachedBlockPathType(blockpos1.m_123341_(), i, blockpos1.m_123343_());
                if (this.f_77313_.getPathfindingMalus(blockpathtypes) >= 0.0F) {
                    return super.getNode(blockpos.m_123341_(), blockpos.m_123342_(), blockpos.m_123343_());
                }
            }
        }
        return super.getNode(blockpos1.m_123341_(), i, blockpos1.m_123343_());
    }

    protected int getNeighbors_ground(Node[] neighbors, Node current) {
        int numOpenNeighbors = 0;
        int stepUpHeight = 0;
        BlockPathTypes pathTypeAbove = this.getCachedBlockType(this.f_77313_, current.x, current.y + 1, current.z);
        BlockPathTypes pathTypeCurrent = this.getCachedBlockType(this.f_77313_, current.x, current.y, current.z);
        if (this.f_77313_.getPathfindingMalus(pathTypeAbove) >= 0.0F && pathTypeCurrent != BlockPathTypes.STICKY_HONEY) {
            stepUpHeight = Mth.floor(Math.max(1.0F, this.f_77313_.getStepHeight()));
        }
        double floorLevel = this.getFloorLevel(new BlockPos(current.x, current.y, current.z));
        Node zPos = this.findAcceptedNode(current.x, current.y, current.z + 1, stepUpHeight, floorLevel, Direction.SOUTH, pathTypeCurrent);
        if (this.isNeighborValid(zPos, current)) {
            neighbors[numOpenNeighbors++] = zPos;
        }
        Node xNeg = this.findAcceptedNode(current.x - 1, current.y, current.z, stepUpHeight, floorLevel, Direction.WEST, pathTypeCurrent);
        if (this.isNeighborValid(xNeg, current)) {
            neighbors[numOpenNeighbors++] = xNeg;
        }
        Node xPos = this.findAcceptedNode(current.x + 1, current.y, current.z, stepUpHeight, floorLevel, Direction.EAST, pathTypeCurrent);
        if (this.isNeighborValid(xPos, current)) {
            neighbors[numOpenNeighbors++] = xPos;
        }
        Node zNeg = this.findAcceptedNode(current.x, current.y, current.z - 1, stepUpHeight, floorLevel, Direction.NORTH, pathTypeCurrent);
        if (this.isNeighborValid(zNeg, current)) {
            neighbors[numOpenNeighbors++] = zNeg;
        }
        Node xNegZNeg = this.findAcceptedNode(current.x - 1, current.y, current.z - 1, stepUpHeight, floorLevel, Direction.NORTH, pathTypeCurrent);
        if (this.isDiagonalValid(current, xNeg, zNeg, xNegZNeg)) {
            neighbors[numOpenNeighbors++] = xNegZNeg;
        }
        Node xPosZNeg = this.findAcceptedNode(current.x + 1, current.y, current.z - 1, stepUpHeight, floorLevel, Direction.NORTH, pathTypeCurrent);
        if (this.isDiagonalValid(current, xPos, zNeg, xPosZNeg)) {
            neighbors[numOpenNeighbors++] = xPosZNeg;
        }
        Node xNegZPos = this.findAcceptedNode(current.x - 1, current.y, current.z + 1, stepUpHeight, floorLevel, Direction.SOUTH, pathTypeCurrent);
        if (this.isDiagonalValid(current, xNeg, zPos, xNegZPos)) {
            neighbors[numOpenNeighbors++] = xNegZPos;
        }
        Node xPosZPos = this.findAcceptedNode(current.x + 1, current.y, current.z + 1, stepUpHeight, floorLevel, Direction.SOUTH, pathTypeCurrent);
        if (this.isDiagonalValid(current, xPos, zPos, xPosZPos)) {
            neighbors[numOpenNeighbors++] = xPosZPos;
        }
        return numOpenNeighbors;
    }

    protected int getNeighbors_flying(Node[] neighbors, Node current) {
        int numOpenNeighbors = 0;
        Node zPos = this.getNode(current.x, current.y, current.z + 1);
        if (this.isOpen(zPos)) {
            neighbors[numOpenNeighbors++] = zPos;
        }
        Node xNeg = this.getNode(current.x - 1, current.y, current.z);
        if (this.isOpen(xNeg)) {
            neighbors[numOpenNeighbors++] = xNeg;
        }
        Node xPos = this.getNode(current.x + 1, current.y, current.z);
        if (this.isOpen(xPos)) {
            neighbors[numOpenNeighbors++] = xPos;
        }
        Node zNeg = this.getNode(current.x, current.y, current.z - 1);
        if (this.isOpen(zNeg)) {
            neighbors[numOpenNeighbors++] = zNeg;
        }
        Node yPos = this.getNode(current.x, current.y + 1, current.z);
        if (this.isOpen(yPos)) {
            neighbors[numOpenNeighbors++] = yPos;
        }
        Node yNeg = this.getNode(current.x, current.y - 1, current.z);
        if (this.isOpen(yNeg)) {
            neighbors[numOpenNeighbors++] = yNeg;
        }
        Node yPoszPos = this.getNode(current.x, current.y + 1, current.z + 1);
        if (this.isOpen(yPoszPos) && this.hasMalus(zPos) && this.hasMalus(yPos)) {
            neighbors[numOpenNeighbors++] = yPoszPos;
        }
        Node xNegYPos = this.getNode(current.x - 1, current.y + 1, current.z);
        if (this.isOpen(xNegYPos) && this.hasMalus(xNeg) && this.hasMalus(yPos)) {
            neighbors[numOpenNeighbors++] = xNegYPos;
        }
        Node xPosYPos = this.getNode(current.x + 1, current.y + 1, current.z);
        if (this.isOpen(xPosYPos) && this.hasMalus(xPos) && this.hasMalus(yPos)) {
            neighbors[numOpenNeighbors++] = xPosYPos;
        }
        Node yPosZNeg = this.getNode(current.x, current.y + 1, current.z - 1);
        if (this.isOpen(yPosZNeg) && this.hasMalus(zNeg) && this.hasMalus(yPos)) {
            neighbors[numOpenNeighbors++] = yPosZNeg;
        }
        Node yNegZPos = this.getNode(current.x, current.y - 1, current.z + 1);
        if (this.isOpen(yNegZPos) && this.hasMalus(zPos) && this.hasMalus(yNeg)) {
            neighbors[numOpenNeighbors++] = yNegZPos;
        }
        Node xNegYNeg = this.getNode(current.x - 1, current.y - 1, current.z);
        if (this.isOpen(xNegYNeg) && this.hasMalus(xNeg) && this.hasMalus(yNeg)) {
            neighbors[numOpenNeighbors++] = xNegYNeg;
        }
        Node xPosYNeg = this.getNode(current.x + 1, current.y - 1, current.z);
        if (this.isOpen(xPosYNeg) && this.hasMalus(xPos) && this.hasMalus(yNeg)) {
            neighbors[numOpenNeighbors++] = xPosYNeg;
        }
        Node yNegZNeg = this.getNode(current.x, current.y - 1, current.z - 1);
        if (this.isOpen(yNegZNeg) && this.hasMalus(zNeg) && this.hasMalus(yNeg)) {
            neighbors[numOpenNeighbors++] = yNegZNeg;
        }
        Node xPosZNeg = this.getNode(current.x + 1, current.y, current.z - 1);
        if (this.isOpen(xPosZNeg) && this.hasMalus(zNeg) && this.hasMalus(xPos)) {
            neighbors[numOpenNeighbors++] = xPosZNeg;
        }
        Node xPosZPos = this.getNode(current.x + 1, current.y, current.z + 1);
        if (this.isOpen(xPosZPos) && this.hasMalus(zPos) && this.hasMalus(xPos)) {
            neighbors[numOpenNeighbors++] = xPosZPos;
        }
        Node xNegZNeg = this.getNode(current.x - 1, current.y, current.z - 1);
        if (this.isOpen(xNegZNeg) && this.hasMalus(zNeg) && this.hasMalus(xNeg)) {
            neighbors[numOpenNeighbors++] = xNegZNeg;
        }
        Node xNegZPos = this.getNode(current.x - 1, current.y, current.z + 1);
        if (this.isOpen(xNegZPos) && this.hasMalus(zPos) && this.hasMalus(xNeg)) {
            neighbors[numOpenNeighbors++] = xNegZPos;
        }
        Node xposYPosZNeg = this.getNode(current.x + 1, current.y + 1, current.z - 1);
        if (this.isOpen(xposYPosZNeg) && this.hasMalus(xPosZNeg) && this.hasMalus(zNeg) && this.hasMalus(xPos) && this.hasMalus(yPos) && this.hasMalus(yPosZNeg) && this.hasMalus(xPosYPos)) {
            neighbors[numOpenNeighbors++] = xposYPosZNeg;
        }
        Node xPosYPosZPos = this.getNode(current.x + 1, current.y + 1, current.z + 1);
        if (this.isOpen(xPosYPosZPos) && this.hasMalus(xPosZPos) && this.hasMalus(zPos) && this.hasMalus(xPos) && this.hasMalus(yPos) && this.hasMalus(yPoszPos) && this.hasMalus(xPosYPos)) {
            neighbors[numOpenNeighbors++] = xPosYPosZPos;
        }
        Node xNegYPosZNeg = this.getNode(current.x - 1, current.y + 1, current.z - 1);
        if (this.isOpen(xNegYPosZNeg) && this.hasMalus(xNegZNeg) && this.hasMalus(zNeg) && this.hasMalus(xNeg) && this.hasMalus(yPos) && this.hasMalus(yPosZNeg) && this.hasMalus(xNegYPos)) {
            neighbors[numOpenNeighbors++] = xNegYPosZNeg;
        }
        Node xNegYPosZPos = this.getNode(current.x - 1, current.y + 1, current.z + 1);
        if (this.isOpen(xNegYPosZPos) && this.hasMalus(xNegZPos) && this.hasMalus(zPos) && this.hasMalus(xNeg) && this.hasMalus(yPos) && this.hasMalus(yPoszPos) && this.hasMalus(xNegYPos)) {
            neighbors[numOpenNeighbors++] = xNegYPosZPos;
        }
        Node xPosYNegZNeg = this.getNode(current.x + 1, current.y - 1, current.z - 1);
        if (this.isOpen(xPosYNegZNeg) && this.hasMalus(xPosZNeg) && this.hasMalus(zNeg) && this.hasMalus(xPos) && this.hasMalus(yNeg) && this.hasMalus(yNegZNeg) && this.hasMalus(xPosYNeg)) {
            neighbors[numOpenNeighbors++] = xPosYNegZNeg;
        }
        Node xPosYNegZPos = this.getNode(current.x + 1, current.y - 1, current.z + 1);
        if (this.isOpen(xPosYNegZPos) && this.hasMalus(xPosZPos) && this.hasMalus(zPos) && this.hasMalus(xPos) && this.hasMalus(yNeg) && this.hasMalus(yNegZPos) && this.hasMalus(xPosYNeg)) {
            neighbors[numOpenNeighbors++] = xPosYNegZPos;
        }
        Node xNegYNegZNeg = this.getNode(current.x - 1, current.y - 1, current.z - 1);
        if (this.isOpen(xNegYNegZNeg) && this.hasMalus(xNegZNeg) && this.hasMalus(zNeg) && this.hasMalus(xNeg) && this.hasMalus(yNeg) && this.hasMalus(yNegZNeg) && this.hasMalus(xNegYNeg)) {
            neighbors[numOpenNeighbors++] = xNegYNegZNeg;
        }
        Node xNegYNegZPos = this.getNode(current.x - 1, current.y - 1, current.z + 1);
        if (this.isOpen(xNegYNegZPos) && this.hasMalus(xNegZPos) && this.hasMalus(zPos) && this.hasMalus(xNeg) && this.hasMalus(yNeg) && this.hasMalus(yNegZPos) && this.hasMalus(xNegYNeg)) {
            neighbors[numOpenNeighbors++] = xNegYNegZPos;
        }
        return numOpenNeighbors;
    }

    protected BlockPathTypes getBlockPathType_ground(BlockGetter blockGetter, int x, int y, int z, Mob mob) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPathTypes blockpathtypes = getBlockPathTypeRaw(blockGetter, blockpos$mutableblockpos.set(x, y, z));
        if (blockpathtypes == BlockPathTypes.WATER) {
            for (Direction direction : Direction.values()) {
                BlockPathTypes blockpathtypes1 = getBlockPathTypeRaw(blockGetter, blockpos$mutableblockpos.set(x, y, z).move(direction));
                if (blockpathtypes1 == BlockPathTypes.BLOCKED) {
                    return BlockPathTypes.WATER_BORDER;
                }
            }
            return BlockPathTypes.WATER;
        } else {
            return WalkNodeEvaluator.getBlockPathTypeStatic(blockGetter, blockpos$mutableblockpos);
        }
    }

    protected BlockPathTypes getBlockPathType_flying(BlockGetter pLevel, int pX, int pY, int pZ, Mob pMob) {
        EnumSet<BlockPathTypes> enumset = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes blockpathtypes = BlockPathTypes.BLOCKED;
        BlockPos blockpos = pMob.m_20183_();
        blockpathtypes = this.getBlockPathTypes(pLevel, pX, pY, pZ, enumset, blockpathtypes, blockpos);
        if (enumset.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else {
            BlockPathTypes blockpathtypes1 = BlockPathTypes.BLOCKED;
            for (BlockPathTypes blockpathtypes2 : enumset) {
                if (pMob.getPathfindingMalus(blockpathtypes2) < 0.0F) {
                    return blockpathtypes2;
                }
                if (pMob.getPathfindingMalus(blockpathtypes2) >= pMob.getPathfindingMalus(blockpathtypes1)) {
                    blockpathtypes1 = blockpathtypes2;
                }
            }
            return blockpathtypes == BlockPathTypes.OPEN && pMob.getPathfindingMalus(blockpathtypes1) == 0.0F ? BlockPathTypes.OPEN : blockpathtypes1;
        }
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter getter, BlockPos.MutableBlockPos position, Mob mob) {
        int x = position.m_123341_();
        int y = position.m_123342_();
        int z = position.m_123343_();
        BlockPathTypes pathType = getBlockPathTypeRaw(getter, position);
        if (pathType == BlockPathTypes.OPEN && y >= getter.m_141937_() + 1) {
            BlockPathTypes pathTypeBelow = getBlockPathTypeRaw(getter, position.set(x, y - 1, z));
            pathType = pathTypeBelow != BlockPathTypes.WALKABLE && pathTypeBelow != BlockPathTypes.OPEN && pathTypeBelow != BlockPathTypes.WATER && pathTypeBelow != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (pathTypeBelow == BlockPathTypes.DAMAGE_FIRE) {
                pathType = BlockPathTypes.DAMAGE_FIRE;
            }
            if (pathTypeBelow == BlockPathTypes.DAMAGE_CAUTIOUS) {
                pathType = BlockPathTypes.DAMAGE_CAUTIOUS;
            }
            if (pathTypeBelow == BlockPathTypes.DAMAGE_OTHER) {
                pathType = BlockPathTypes.DAMAGE_OTHER;
            }
            if (pathTypeBelow == BlockPathTypes.STICKY_HONEY) {
                pathType = BlockPathTypes.STICKY_HONEY;
            }
        }
        if (pathType == BlockPathTypes.WALKABLE) {
            pathType = checkNeighbourBlocks(getter, position.set(x, y, z), pathType);
        }
        return pathType;
    }

    public static BlockPathTypes checkNeighbourBlocks(BlockGetter getter, BlockPos.MutableBlockPos position, BlockPathTypes pathType) {
        int x = position.m_123341_();
        int y = position.m_123342_();
        int z = position.m_123343_();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (i != 0 || k != 0) {
                        position.set(x + i, y + j, z + k);
                        BlockState blockstate = getter.getBlockState(position);
                        if (blockstate.m_60713_(Blocks.CACTUS)) {
                            return BlockPathTypes.DAMAGE_CAUTIOUS;
                        }
                        if (blockstate.m_60713_(Blocks.SWEET_BERRY_BUSH)) {
                            return BlockPathTypes.DANGER_OTHER;
                        }
                        if (isBurningBlock(blockstate)) {
                            return BlockPathTypes.DANGER_FIRE;
                        }
                        if (getter.getFluidState(position).is(FluidTags.WATER)) {
                            return BlockPathTypes.WATER_BORDER;
                        }
                    }
                }
            }
        }
        return pathType;
    }

    protected static BlockPathTypes getBlockPathTypeRaw(BlockGetter pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        BlockPathTypes type = blockstate.getBlockPathType(pLevel, pPos, null);
        if (type != null) {
            return type;
        } else {
            Block block = blockstate.m_60734_();
            if (blockstate.m_60795_()) {
                return BlockPathTypes.OPEN;
            } else if (blockstate.m_204336_(BlockTags.TRAPDOORS) || blockstate.m_60713_(Blocks.LILY_PAD) || blockstate.m_60713_(Blocks.BIG_DRIPLEAF)) {
                return BlockPathTypes.TRAPDOOR;
            } else if (blockstate.m_60713_(Blocks.POWDER_SNOW)) {
                return BlockPathTypes.POWDER_SNOW;
            } else if (blockstate.m_60713_(Blocks.CACTUS) || blockstate.m_60713_(Blocks.SWEET_BERRY_BUSH)) {
                return BlockPathTypes.DAMAGE_OTHER;
            } else if (blockstate.m_60713_(Blocks.HONEY_BLOCK)) {
                return BlockPathTypes.STICKY_HONEY;
            } else if (blockstate.m_60713_(Blocks.COCOA)) {
                return BlockPathTypes.COCOA;
            } else if (!blockstate.m_60713_(Blocks.WITHER_ROSE) && !blockstate.m_60713_(Blocks.POINTED_DRIPSTONE)) {
                FluidState fluidstate = pLevel.getFluidState(pPos);
                BlockPathTypes nonLoggableFluidPathType = fluidstate.getBlockPathType(pLevel, pPos, null, false);
                if (nonLoggableFluidPathType != null) {
                    return nonLoggableFluidPathType;
                } else if (fluidstate.is(FluidTags.LAVA)) {
                    return BlockPathTypes.LAVA;
                } else if (isBurningBlock(blockstate)) {
                    return BlockPathTypes.DAMAGE_FIRE;
                } else if (block instanceof DoorBlock doorblock) {
                    if ((Boolean) blockstate.m_61143_(DoorBlock.OPEN)) {
                        return BlockPathTypes.DOOR_OPEN;
                    } else {
                        return doorblock.type().canOpenByHand() ? BlockPathTypes.DOOR_WOOD_CLOSED : BlockPathTypes.DOOR_IRON_CLOSED;
                    }
                } else if (block instanceof BaseRailBlock) {
                    return BlockPathTypes.RAIL;
                } else if (block instanceof LeavesBlock) {
                    return BlockPathTypes.LEAVES;
                } else if (!blockstate.m_204336_(BlockTags.FENCES) && !blockstate.m_204336_(BlockTags.WALLS) && (!(block instanceof FenceGateBlock) || (Boolean) blockstate.m_61143_(FenceGateBlock.OPEN))) {
                    if (!blockstate.m_60647_(pLevel, pPos, PathComputationType.LAND)) {
                        return BlockPathTypes.BLOCKED;
                    } else {
                        BlockPathTypes loggableFluidPathType = fluidstate.getBlockPathType(pLevel, pPos, null, true);
                        if (loggableFluidPathType != null) {
                            return loggableFluidPathType;
                        } else {
                            return fluidstate.is(FluidTags.WATER) ? BlockPathTypes.WATER : BlockPathTypes.OPEN;
                        }
                    }
                } else {
                    return BlockPathTypes.FENCE;
                }
            } else {
                return BlockPathTypes.DAMAGE_CAUTIOUS;
            }
        }
    }

    public static boolean isBurningBlock(BlockState state) {
        return state.m_204336_(BlockTags.FIRE) || state.m_60713_(Blocks.LAVA) || state.m_60713_(Blocks.MAGMA_BLOCK) || CampfireBlock.isLitCampfire(state) || state.m_60713_(Blocks.LAVA_CAULDRON);
    }

    public static double getFloorLevel(BlockGetter blockGetter, BlockPos pos) {
        BlockPos below = pos.below();
        VoxelShape voxelshape = blockGetter.getBlockState(below).m_60812_(blockGetter, below);
        return (double) below.m_123342_() + (voxelshape.isEmpty() ? 0.0 : voxelshape.max(Direction.Axis.Y));
    }
}