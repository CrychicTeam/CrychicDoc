package net.minecraft.world.entity.ai.navigation;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class PathNavigation {

    private static final int MAX_TIME_RECOMPUTE = 20;

    private static final int STUCK_CHECK_INTERVAL = 100;

    private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;

    protected final Mob mob;

    protected final Level level;

    @Nullable
    protected Path path;

    protected double speedModifier;

    protected int tick;

    protected int lastStuckCheck;

    protected Vec3 lastStuckCheckPos = Vec3.ZERO;

    protected Vec3i timeoutCachedNode = Vec3i.ZERO;

    protected long timeoutTimer;

    protected long lastTimeoutCheck;

    protected double timeoutLimit;

    protected float maxDistanceToWaypoint = 0.5F;

    protected boolean hasDelayedRecomputation;

    protected long timeLastRecompute;

    protected NodeEvaluator nodeEvaluator;

    @Nullable
    private BlockPos targetPos;

    private int reachRange;

    private float maxVisitedNodesMultiplier = 1.0F;

    private final PathFinder pathFinder;

    private boolean isStuck;

    public PathNavigation(Mob mob0, Level level1) {
        this.mob = mob0;
        this.level = level1;
        int $$2 = Mth.floor(mob0.m_21133_(Attributes.FOLLOW_RANGE) * 16.0);
        this.pathFinder = this.createPathFinder($$2);
    }

    public void resetMaxVisitedNodesMultiplier() {
        this.maxVisitedNodesMultiplier = 1.0F;
    }

    public void setMaxVisitedNodesMultiplier(float float0) {
        this.maxVisitedNodesMultiplier = float0;
    }

    @Nullable
    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    protected abstract PathFinder createPathFinder(int var1);

    public void setSpeedModifier(double double0) {
        this.speedModifier = double0;
    }

    public void recomputePath() {
        if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
            if (this.targetPos != null) {
                this.path = null;
                this.path = this.createPath(this.targetPos, this.reachRange);
                this.timeLastRecompute = this.level.getGameTime();
                this.hasDelayedRecomputation = false;
            }
        } else {
            this.hasDelayedRecomputation = true;
        }
    }

    @Nullable
    public final Path createPath(double double0, double double1, double double2, int int3) {
        return this.createPath(BlockPos.containing(double0, double1, double2), int3);
    }

    @Nullable
    public Path createPath(Stream<BlockPos> streamBlockPos0, int int1) {
        return this.createPath((Set<BlockPos>) streamBlockPos0.collect(Collectors.toSet()), 8, false, int1);
    }

    @Nullable
    public Path createPath(Set<BlockPos> setBlockPos0, int int1) {
        return this.createPath(setBlockPos0, 8, false, int1);
    }

    @Nullable
    public Path createPath(BlockPos blockPos0, int int1) {
        return this.createPath(ImmutableSet.of(blockPos0), 8, false, int1);
    }

    @Nullable
    public Path createPath(BlockPos blockPos0, int int1, int int2) {
        return this.createPath(ImmutableSet.of(blockPos0), 8, false, int1, (float) int2);
    }

    @Nullable
    public Path createPath(Entity entity0, int int1) {
        return this.createPath(ImmutableSet.of(entity0.blockPosition()), 16, true, int1);
    }

    @Nullable
    protected Path createPath(Set<BlockPos> setBlockPos0, int int1, boolean boolean2, int int3) {
        return this.createPath(setBlockPos0, int1, boolean2, int3, (float) this.mob.m_21133_(Attributes.FOLLOW_RANGE));
    }

    @Nullable
    protected Path createPath(Set<BlockPos> setBlockPos0, int int1, boolean boolean2, int int3, float float4) {
        if (setBlockPos0.isEmpty()) {
            return null;
        } else if (this.mob.m_20186_() < (double) this.level.m_141937_()) {
            return null;
        } else if (!this.canUpdatePath()) {
            return null;
        } else if (this.path != null && !this.path.isDone() && setBlockPos0.contains(this.targetPos)) {
            return this.path;
        } else {
            this.level.getProfiler().push("pathfind");
            BlockPos $$5 = boolean2 ? this.mob.m_20183_().above() : this.mob.m_20183_();
            int $$6 = (int) (float4 + (float) int1);
            PathNavigationRegion $$7 = new PathNavigationRegion(this.level, $$5.offset(-$$6, -$$6, -$$6), $$5.offset($$6, $$6, $$6));
            Path $$8 = this.pathFinder.findPath($$7, this.mob, setBlockPos0, float4, int3, this.maxVisitedNodesMultiplier);
            this.level.getProfiler().pop();
            if ($$8 != null && $$8.getTarget() != null) {
                this.targetPos = $$8.getTarget();
                this.reachRange = int3;
                this.resetStuckTimeout();
            }
            return $$8;
        }
    }

    public boolean moveTo(double double0, double double1, double double2, double double3) {
        return this.moveTo(this.createPath(double0, double1, double2, 1), double3);
    }

    public boolean moveTo(Entity entity0, double double1) {
        Path $$2 = this.createPath(entity0, 1);
        return $$2 != null && this.moveTo($$2, double1);
    }

    public boolean moveTo(@Nullable Path path0, double double1) {
        if (path0 == null) {
            this.path = null;
            return false;
        } else {
            if (!path0.sameAs(this.path)) {
                this.path = path0;
            }
            if (this.isDone()) {
                return false;
            } else {
                this.trimPath();
                if (this.path.getNodeCount() <= 0) {
                    return false;
                } else {
                    this.speedModifier = double1;
                    Vec3 $$2 = this.getTempMobPos();
                    this.lastStuckCheck = this.tick;
                    this.lastStuckCheckPos = $$2;
                    return true;
                }
            }
        }
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public void tick() {
        this.tick++;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }
        if (!this.isDone()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                Vec3 $$0 = this.getTempMobPos();
                Vec3 $$1 = this.path.getNextEntityPos(this.mob);
                if ($$0.y > $$1.y && !this.mob.m_20096_() && Mth.floor($$0.x) == Mth.floor($$1.x) && Mth.floor($$0.z) == Mth.floor($$1.z)) {
                    this.path.advance();
                }
            }
            DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
            if (!this.isDone()) {
                Vec3 $$2 = this.path.getNextEntityPos(this.mob);
                this.mob.getMoveControl().setWantedPosition($$2.x, this.getGroundY($$2), $$2.z, this.speedModifier);
            }
        }
    }

    protected double getGroundY(Vec3 vec0) {
        BlockPos $$1 = BlockPos.containing(vec0);
        return this.level.getBlockState($$1.below()).m_60795_() ? vec0.y : WalkNodeEvaluator.getFloorLevel(this.level, $$1);
    }

    protected void followThePath() {
        Vec3 $$0 = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.m_20205_() > 0.75F ? this.mob.m_20205_() / 2.0F : 0.75F - this.mob.m_20205_() / 2.0F;
        Vec3i $$1 = this.path.getNextNodePos();
        double $$2 = Math.abs(this.mob.m_20185_() - ((double) $$1.getX() + 0.5));
        double $$3 = Math.abs(this.mob.m_20186_() - (double) $$1.getY());
        double $$4 = Math.abs(this.mob.m_20189_() - ((double) $$1.getZ() + 0.5));
        boolean $$5 = $$2 < (double) this.maxDistanceToWaypoint && $$4 < (double) this.maxDistanceToWaypoint && $$3 < 1.0;
        if ($$5 || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection($$0)) {
            this.path.advance();
        }
        this.doStuckDetection($$0);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 vec0) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 $$1 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!vec0.closerThan($$1, 2.0)) {
                return false;
            } else if (this.canMoveDirectly(vec0, this.path.getNextEntityPos(this.mob))) {
                return true;
            } else {
                Vec3 $$2 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 $$3 = $$1.subtract(vec0);
                Vec3 $$4 = $$2.subtract(vec0);
                double $$5 = $$3.lengthSqr();
                double $$6 = $$4.lengthSqr();
                boolean $$7 = $$6 < $$5;
                boolean $$8 = $$5 < 0.5;
                if (!$$7 && !$$8) {
                    return false;
                } else {
                    Vec3 $$9 = $$3.normalize();
                    Vec3 $$10 = $$4.normalize();
                    return $$10.dot($$9) < 0.0;
                }
            }
        }
    }

    protected void doStuckDetection(Vec3 vec0) {
        if (this.tick - this.lastStuckCheck > 100) {
            float $$1 = this.mob.m_6113_() >= 1.0F ? this.mob.m_6113_() : this.mob.m_6113_() * this.mob.m_6113_();
            float $$2 = $$1 * 100.0F * 0.25F;
            if (vec0.distanceToSqr(this.lastStuckCheckPos) < (double) ($$2 * $$2)) {
                this.isStuck = true;
                this.stop();
            } else {
                this.isStuck = false;
            }
            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = vec0;
        }
        if (this.path != null && !this.path.isDone()) {
            Vec3i $$3 = this.path.getNextNodePos();
            long $$4 = this.level.getGameTime();
            if ($$3.equals(this.timeoutCachedNode)) {
                this.timeoutTimer = this.timeoutTimer + ($$4 - this.lastTimeoutCheck);
            } else {
                this.timeoutCachedNode = $$3;
                double $$5 = vec0.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.mob.m_6113_() > 0.0F ? $$5 / (double) this.mob.m_6113_() * 20.0 : 0.0;
            }
            if (this.timeoutLimit > 0.0 && (double) this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutPath();
            }
            this.lastTimeoutCheck = $$4;
        }
    }

    private void timeoutPath() {
        this.resetStuckTimeout();
        this.stop();
    }

    private void resetStuckTimeout() {
        this.timeoutCachedNode = Vec3i.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = 0.0;
        this.isStuck = false;
    }

    public boolean isDone() {
        return this.path == null || this.path.isDone();
    }

    public boolean isInProgress() {
        return !this.isDone();
    }

    public void stop() {
        this.path = null;
    }

    protected abstract Vec3 getTempMobPos();

    protected abstract boolean canUpdatePath();

    protected boolean isInLiquid() {
        return this.mob.m_20072_() || this.mob.m_20077_();
    }

    protected void trimPath() {
        if (this.path != null) {
            for (int $$0 = 0; $$0 < this.path.getNodeCount(); $$0++) {
                Node $$1 = this.path.getNode($$0);
                Node $$2 = $$0 + 1 < this.path.getNodeCount() ? this.path.getNode($$0 + 1) : null;
                BlockState $$3 = this.level.getBlockState(new BlockPos($$1.x, $$1.y, $$1.z));
                if ($$3.m_204336_(BlockTags.CAULDRONS)) {
                    this.path.replaceNode($$0, $$1.cloneAndMove($$1.x, $$1.y + 1, $$1.z));
                    if ($$2 != null && $$1.y >= $$2.y) {
                        this.path.replaceNode($$0 + 1, $$1.cloneAndMove($$2.x, $$1.y + 1, $$2.z));
                    }
                }
            }
        }
    }

    protected boolean canMoveDirectly(Vec3 vec0, Vec3 vec1) {
        return false;
    }

    public boolean canCutCorner(BlockPathTypes blockPathTypes0) {
        return blockPathTypes0 != BlockPathTypes.DANGER_FIRE && blockPathTypes0 != BlockPathTypes.DANGER_OTHER && blockPathTypes0 != BlockPathTypes.WALKABLE_DOOR;
    }

    protected static boolean isClearForMovementBetween(Mob mob0, Vec3 vec1, Vec3 vec2, boolean boolean3) {
        Vec3 $$4 = new Vec3(vec2.x, vec2.y + (double) mob0.m_20206_() * 0.5, vec2.z);
        return mob0.m_9236_().m_45547_(new ClipContext(vec1, $$4, ClipContext.Block.COLLIDER, boolean3 ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, mob0)).getType() == HitResult.Type.MISS;
    }

    public boolean isStableDestination(BlockPos blockPos0) {
        BlockPos $$1 = blockPos0.below();
        return this.level.getBlockState($$1).m_60804_(this.level, $$1);
    }

    public NodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }

    public void setCanFloat(boolean boolean0) {
        this.nodeEvaluator.setCanFloat(boolean0);
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    public boolean shouldRecomputePath(BlockPos blockPos0) {
        if (this.hasDelayedRecomputation) {
            return false;
        } else if (this.path != null && !this.path.isDone() && this.path.getNodeCount() != 0) {
            Node $$1 = this.path.getEndNode();
            Vec3 $$2 = new Vec3(((double) $$1.x + this.mob.m_20185_()) / 2.0, ((double) $$1.y + this.mob.m_20186_()) / 2.0, ((double) $$1.z + this.mob.m_20189_()) / 2.0);
            return blockPos0.m_203195_($$2, (double) (this.path.getNodeCount() - this.path.getNextNodeIndex()));
        } else {
            return false;
        }
    }

    public float getMaxDistanceToWaypoint() {
        return this.maxDistanceToWaypoint;
    }

    public boolean isStuck() {
        return this.isStuck;
    }
}