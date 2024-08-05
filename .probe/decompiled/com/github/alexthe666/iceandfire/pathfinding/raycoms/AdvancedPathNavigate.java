package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorFly;
import com.github.alexthe666.iceandfire.pathfinding.NodeProcessorWalk;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveAwayFromLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobMoveToLocation;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.PathJobRandomPos;
import com.github.alexthe666.iceandfire.util.WorldUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AdvancedPathNavigate extends AbstractAdvancedPathNavigate {

    public static final double MIN_Y_DISTANCE = 0.001;

    public static final int MAX_SPEED_ALLOWED = 2;

    public static final double MIN_SPEED_ALLOWED = 0.1;

    @Nullable
    private PathResult<AbstractPathJob> pathResult;

    private long pathStartTime = 0L;

    private final BlockPos spawnedPos = BlockPos.ZERO;

    private BlockPos desiredPos;

    private int desiredPosTimeout = 0;

    private IStuckHandler stuckHandler;

    private boolean isSneaking = true;

    private double swimSpeedFactor = 1.0;

    private float width = 1.0F;

    private float height = 1.0F;

    public AdvancedPathNavigate(Mob entity, Level world) {
        this(entity, world, AdvancedPathNavigate.MovementType.WALKING);
    }

    public AdvancedPathNavigate(Mob entity, Level world, AdvancedPathNavigate.MovementType type) {
        this(entity, world, type, 1.0F, 1.0F);
    }

    public AdvancedPathNavigate(Mob entity, Level world, AdvancedPathNavigate.MovementType type, float width, float height) {
        this(entity, world, type, width, height, PathingStuckHandler.createStuckHandler().withTeleportSteps(6).withTeleportOnFullStuck());
    }

    public AdvancedPathNavigate(Mob entity, Level world, AdvancedPathNavigate.MovementType type, float width, float height, PathingStuckHandler stuckHandler) {
        super(entity, world);
        switch(type) {
            case FLYING:
                this.f_26508_ = new NodeProcessorFly();
                this.getPathingOptions().setIsFlying(true);
                break;
            case WALKING:
                this.f_26508_ = new NodeProcessorWalk();
                break;
            case CLIMBING:
                this.f_26508_ = new NodeProcessorWalk();
                this.getPathingOptions().setCanClimb(true);
        }
        this.f_26508_.setCanPassDoors(true);
        this.getPathingOptions().setEnterDoors(true);
        this.f_26508_.setCanOpenDoors(true);
        this.getPathingOptions().setCanOpenDoors(true);
        this.f_26508_.setCanFloat(true);
        this.getPathingOptions().setCanSwim(true);
        this.width = width;
        this.height = height;
        this.stuckHandler = stuckHandler;
    }

    @Override
    public BlockPos getDestination() {
        return this.destination;
    }

    @Nullable
    @Override
    public PathResult moveAwayFromXYZ(BlockPos avoid, double range, double speedFactor, boolean safeDestination) {
        BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);
        return this.setPathJob(new PathJobMoveAwayFromLocation(this.ourEntity.m_9236_(), start, avoid, (int) range, (int) this.ourEntity.m_21051_(Attributes.FOLLOW_RANGE).getValue(), this.ourEntity), null, speedFactor, safeDestination);
    }

    @Nullable
    @Override
    public PathResult moveToRandomPos(double range, double speedFactor) {
        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobRandomPos) {
            return this.pathResult;
        } else {
            this.desiredPos = BlockPos.ZERO;
            int theRange = (int) ((double) this.f_26494_.m_217043_().nextInt((int) range) + range / 2.0);
            BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);
            return this.setPathJob(new PathJobRandomPos(this.ourEntity.m_9236_(), start, theRange, (int) this.ourEntity.m_21051_(Attributes.FOLLOW_RANGE).getValue(), this.ourEntity), null, speedFactor, true);
        }
    }

    @Nullable
    @Override
    public PathResult moveToRandomPosAroundX(int range, double speedFactor, BlockPos pos) {
        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobRandomPos && ((PathJobRandomPos) this.pathResult.getJob()).posAndRangeMatch(range, pos)) {
            return this.pathResult;
        } else {
            this.desiredPos = BlockPos.ZERO;
            return this.setPathJob(new PathJobRandomPos(this.ourEntity.m_9236_(), AbstractPathJob.prepareStart(this.ourEntity), 3, (int) this.ourEntity.m_21051_(Attributes.FOLLOW_RANGE).getValue(), range, this.ourEntity, pos), pos, speedFactor, true);
        }
    }

    @Override
    public PathResult moveToRandomPos(int range, double speedFactor, Tuple<BlockPos, BlockPos> corners, AbstractAdvancedPathNavigate.RestrictionType restrictionType) {
        if (this.pathResult != null && this.pathResult.getJob() instanceof PathJobRandomPos) {
            return this.pathResult;
        } else {
            this.desiredPos = BlockPos.ZERO;
            int theRange = this.f_26494_.m_217043_().nextInt(range) + range / 2;
            BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);
            return this.setPathJob(new PathJobRandomPos(this.ourEntity.m_9236_(), start, theRange, (int) this.ourEntity.m_21051_(Attributes.FOLLOW_RANGE).getValue(), this.ourEntity, corners.getA(), corners.getB(), restrictionType), null, speedFactor, true);
        }
    }

    @Nullable
    public PathResult setPathJob(AbstractPathJob job, BlockPos dest, double speedFactor, boolean safeDestination) {
        this.stop();
        this.destination = dest;
        this.originalDestination = dest;
        if (safeDestination) {
            this.desiredPos = dest;
            if (dest != null) {
                this.desiredPosTimeout = 1000;
            }
        }
        this.walkSpeedFactor = speedFactor;
        if (!(speedFactor > 2.0) && !(speedFactor < 0.1)) {
            job.setPathingOptions(this.getPathingOptions());
            this.pathResult = job.getResult();
            this.pathResult.startJob(Pathfinding.getExecutor());
            return this.pathResult;
        } else {
            IceAndFire.LOGGER.error("Tried to set a bad speed:" + speedFactor + " for entity:" + this.ourEntity, new Exception());
            return null;
        }
    }

    @Override
    public boolean isDone() {
        return (this.pathResult == null || this.pathResult.isFinished() && this.pathResult.getStatus() != PathFindingStatus.CALCULATION_COMPLETE) && super.m_26571_();
    }

    @Override
    public void tick() {
        if (this.f_26508_ instanceof NodeProcessorWalk) {
            ((NodeProcessorWalk) this.f_26508_).setEntitySize(this.width, this.height);
        } else {
            ((NodeProcessorFly) this.f_26508_).setEntitySize(this.width, this.height);
        }
        if (this.desiredPosTimeout > 0 && this.desiredPosTimeout-- <= 0) {
            this.desiredPos = null;
        }
        if (this.pathResult != null) {
            if (!this.pathResult.isFinished()) {
                return;
            }
            if (this.pathResult.getStatus() == PathFindingStatus.CALCULATION_COMPLETE) {
                try {
                    this.processCompletedCalculationResult();
                } catch (ExecutionException | InterruptedException var4) {
                    IceAndFire.LOGGER.catching(var4);
                }
            }
        }
        int oldIndex = this.isDone() ? 0 : this.m_26570_().getNextNodeIndex();
        if (this.isSneaking) {
            this.isSneaking = false;
            this.f_26494_.m_20260_(false);
        }
        this.ourEntity.setYya(0.0F);
        if (this.handleLadders(oldIndex)) {
            this.followThePath();
            this.stuckHandler.checkStuck(this);
        } else if (this.handleRails()) {
            this.stuckHandler.checkStuck(this);
        } else {
            this.f_26498_++;
            if (!this.isDone()) {
                if (this.canUpdatePath()) {
                    this.followThePath();
                } else if (this.f_26496_ != null && !this.f_26496_.isDone()) {
                    Vec3 vector3d = this.getTempMobPos();
                    Vec3 vector3d1 = this.f_26496_.getNextEntityPos(this.f_26494_);
                    if (vector3d.y > vector3d1.y && !this.f_26494_.m_20096_() && Mth.floor(vector3d.x) == Mth.floor(vector3d1.x) && Mth.floor(vector3d.z) == Mth.floor(vector3d1.z)) {
                        this.f_26496_.advance();
                    }
                }
                DebugPackets.sendPathFindingPacket(this.f_26495_, this.f_26494_, this.f_26496_, this.f_26505_);
                if (!this.isDone()) {
                    Vec3 vector3d2 = this.f_26496_.getNextEntityPos(this.f_26494_);
                    BlockPos blockpos = BlockPos.containing(vector3d2);
                    if (isEntityBlockLoaded(this.f_26495_, blockpos)) {
                        this.f_26494_.getMoveControl().setWantedPosition(vector3d2.x, this.f_26495_.getBlockState(blockpos.below()).m_60795_() ? vector3d2.y : getSmartGroundY(this.f_26495_, blockpos), vector3d2.z, this.f_26497_);
                    }
                }
            }
            if (this.f_26506_) {
                this.recomputePath();
            }
            if (this.pathResult != null && this.isDone()) {
                this.pathResult.setStatus(PathFindingStatus.COMPLETE);
                this.pathResult = null;
            }
            if (this.f_26494_ instanceof TamableAnimal) {
                if (((TamableAnimal) this.f_26494_).isTame()) {
                    return;
                }
                if (this.f_26494_ instanceof EntityDragonBase) {
                    if (((EntityDragonBase) this.f_26494_).isChained()) {
                        return;
                    }
                    if (((EntityDragonBase) this.f_26494_).m_21825_()) {
                        return;
                    }
                }
            }
            this.stuckHandler.checkStuck(this);
        }
    }

    public static double getSmartGroundY(BlockGetter world, BlockPos pos) {
        BlockPos blockpos = pos.below();
        VoxelShape voxelshape = world.getBlockState(blockpos).m_60816_(world, blockpos);
        return !voxelshape.isEmpty() && !(voxelshape.max(Direction.Axis.Y) < 1.0) ? (double) blockpos.m_123342_() + voxelshape.max(Direction.Axis.Y) : (double) pos.m_123342_();
    }

    @Nullable
    @Override
    public PathResult moveToXYZ(double x, double y, double z, double speedFactor) {
        int newX = Mth.floor(x);
        int newY = (int) y;
        int newZ = Mth.floor(z);
        if (this.pathResult == null || !(this.pathResult.getJob() instanceof PathJobMoveToLocation) || !this.pathResult.isComputing() && (this.destination == null || !isEqual(this.destination, newX, newY, newZ)) && (this.originalDestination == null || !isEqual(this.originalDestination, newX, newY, newZ))) {
            BlockPos start = AbstractPathJob.prepareStart(this.ourEntity);
            this.desiredPos = new BlockPos(newX, newY, newZ);
            return this.setPathJob(new PathJobMoveToLocation(this.ourEntity.m_9236_(), start, this.desiredPos, (int) this.ourEntity.m_21051_(Attributes.FOLLOW_RANGE).getValue(), this.ourEntity), this.desiredPos, speedFactor, true);
        } else {
            return this.pathResult;
        }
    }

    @Override
    public boolean tryMoveToBlockPos(BlockPos pos, double speedFactor) {
        this.moveToXYZ((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), speedFactor);
        return true;
    }

    @NotNull
    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        return new PathFinder(new WalkNodeEvaluator(), p_179679_1_);
    }

    @Override
    protected boolean canUpdatePath() {
        if (this.ourEntity.m_20202_() != null) {
            PathPointExtended pEx = (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex());
            if (pEx.isRailsExit()) {
                Entity entity = this.ourEntity.m_20202_();
                this.ourEntity.m_8127_();
                entity.remove(Entity.RemovalReason.DISCARDED);
            } else if (!pEx.isOnRails()) {
                if (this.destination == null || this.f_26494_.m_20275_((double) this.destination.m_123341_(), (double) this.destination.m_123342_(), (double) this.destination.m_123343_()) > 2.0) {
                    this.ourEntity.m_8127_();
                }
            } else if ((Math.abs((double) pEx.f_77271_ - this.f_26494_.m_20185_()) > 7.0 || Math.abs((double) pEx.f_77273_ - this.f_26494_.m_20189_()) > 7.0) && this.ourEntity.m_20202_() != null) {
                Entity entity = this.ourEntity.m_20202_();
                this.ourEntity.m_8127_();
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        return true;
    }

    @NotNull
    @Override
    protected Vec3 getTempMobPos() {
        return this.ourEntity.m_20182_();
    }

    @Override
    public Path createPath(@NotNull BlockPos pos, int accuracy) {
        return null;
    }

    @Override
    protected boolean canMoveDirectly(@NotNull Vec3 start, @NotNull Vec3 end) {
        return super.m_183431_(start, end);
    }

    public double getSpeedFactor() {
        if (this.ourEntity.m_20069_()) {
            this.f_26497_ = this.walkSpeedFactor * this.swimSpeedFactor;
            return this.f_26497_;
        } else {
            this.f_26497_ = this.walkSpeedFactor;
            return this.walkSpeedFactor;
        }
    }

    @Override
    public void setSpeedModifier(double speedFactor) {
        if (!(speedFactor > 2.0) && !(speedFactor < 0.1)) {
            this.walkSpeedFactor = speedFactor;
        } else {
            IceAndFire.LOGGER.debug("Tried to set a bad speed:" + speedFactor + " for entity:" + this.ourEntity);
        }
    }

    @Override
    public boolean moveTo(double x, double y, double z, double speedFactor) {
        if (x == 0.0 && y == 0.0 && z == 0.0) {
            return false;
        } else {
            this.moveToXYZ(x, y, z, speedFactor);
            return true;
        }
    }

    @Override
    public boolean moveTo(Entity entityIn, double speedFactor) {
        return this.tryMoveToBlockPos(entityIn.blockPosition(), speedFactor);
    }

    @Override
    protected void trimPath() {
    }

    @Override
    public boolean moveTo(@Nullable Path path, double speedFactor) {
        if (path == null) {
            this.stop();
            return false;
        } else {
            this.pathStartTime = this.f_26495_.getGameTime();
            return super.m_26536_(this.convertPath(path), speedFactor);
        }
    }

    private Path convertPath(Path path) {
        int pathLength = path.getNodeCount();
        Path tempPath = null;
        if (pathLength > 0 && !(path.getNode(0) instanceof PathPointExtended)) {
            PathPointExtended[] newPoints = new PathPointExtended[pathLength];
            for (int i = 0; i < pathLength; i++) {
                Node point = path.getNode(i);
                if (!(point instanceof PathPointExtended)) {
                    newPoints[i] = new PathPointExtended(new BlockPos(point.x, point.y, point.z));
                } else {
                    newPoints[i] = (PathPointExtended) point;
                }
            }
            tempPath = new Path(Arrays.asList(newPoints), path.getTarget(), path.canReach());
            PathPointExtended finalPoint = newPoints[pathLength - 1];
            this.destination = new BlockPos(finalPoint.f_77271_, finalPoint.f_77272_, finalPoint.f_77273_);
        }
        return tempPath == null ? path : tempPath;
    }

    private boolean processCompletedCalculationResult() throws InterruptedException, ExecutionException {
        ((AbstractPathJob) this.pathResult.getJob()).synchToClient(this.f_26494_);
        this.moveTo(this.pathResult.getPath(), this.getSpeedFactor());
        if (this.pathResult != null) {
            this.pathResult.setStatus(PathFindingStatus.IN_PROGRESS_FOLLOWING);
        }
        return false;
    }

    private boolean handleLadders(int oldIndex) {
        if (!this.isDone()) {
            PathPointExtended pEx = (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex());
            PathPointExtended pExNext = this.m_26570_().getNodeCount() > this.m_26570_().getNextNodeIndex() + 1 ? (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex() + 1) : null;
            BlockPos pos = new BlockPos(pEx.f_77271_, pEx.f_77272_, pEx.f_77273_);
            if (pEx.isOnLadder() && pExNext != null && (pEx.f_77272_ != pExNext.f_77272_ || this.f_26494_.m_20186_() > (double) pEx.f_77272_) && this.f_26495_.getBlockState(pos).isLadder(this.f_26495_, pos, this.ourEntity)) {
                return this.handlePathPointOnLadder(pEx);
            }
            if (this.ourEntity.m_20069_()) {
                return this.handleEntityInWater(oldIndex, pEx);
            }
            if (this.f_26495_.random.nextInt(10) == 0) {
                if (!pEx.isOnLadder() && pExNext != null && pExNext.isOnLadder()) {
                    this.f_26497_ = this.getSpeedFactor() / 4.0;
                } else {
                    this.f_26497_ = this.getSpeedFactor();
                }
            }
        }
        return false;
    }

    private BlockPos findBlockUnderEntity(Entity parEntity) {
        int blockX = (int) Math.round(parEntity.getX());
        int blockY = Mth.floor(parEntity.getY() - 0.2);
        int blockZ = (int) Math.round(parEntity.getZ());
        return new BlockPos(blockX, blockY, blockZ);
    }

    private boolean handleRails() {
        if (!this.isDone()) {
            PathPointExtended pEx = (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex());
            PathPointExtended pExNext = this.m_26570_().getNodeCount() > this.m_26570_().getNextNodeIndex() + 1 ? (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex() + 1) : null;
            if (pExNext != null && pEx.f_77271_ == pExNext.f_77271_ && pEx.f_77273_ == pExNext.f_77273_) {
                pExNext = this.m_26570_().getNodeCount() > this.m_26570_().getNextNodeIndex() + 2 ? (PathPointExtended) this.m_26570_().getNode(this.m_26570_().getNextNodeIndex() + 2) : null;
            }
            if (pEx.isOnRails() || pEx.isRailsExit()) {
                return this.handlePathOnRails(pEx, pExNext);
            }
        }
        return false;
    }

    private boolean handlePathOnRails(PathPointExtended pEx, PathPointExtended pExNext) {
        return false;
    }

    private boolean handlePathPointOnLadder(PathPointExtended pEx) {
        Vec3 vec3 = this.m_26570_().getNextEntityPos(this.ourEntity);
        BlockPos entityPos = new BlockPos(this.ourEntity.m_20183_());
        if (vec3.distanceToSqr(this.ourEntity.m_20185_(), vec3.y, this.ourEntity.m_20189_()) < 0.6 && Math.abs(vec3.y - (double) entityPos.m_123342_()) <= 2.0) {
            double newSpeed = 0.3;
            switch(pEx.getLadderFacing()) {
                case NORTH:
                    vec3 = vec3.add(0.0, 0.0, 0.4);
                    break;
                case SOUTH:
                    vec3 = vec3.add(0.0, 0.0, -0.4);
                    break;
                case WEST:
                    vec3 = vec3.add(0.4, 0.0, 0.0);
                    break;
                case EAST:
                    vec3 = vec3.add(-0.4, 0.0, 0.0);
                    break;
                case UP:
                    vec3 = vec3.add(0.0, 1.0, 0.0);
                    break;
                default:
                    newSpeed = 0.0;
                    this.f_26494_.m_20260_(true);
                    this.isSneaking = true;
                    this.ourEntity.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 0.2);
            }
            if (!(newSpeed > 0.0)) {
                if (this.f_26495_.getBlockState(entityPos.below()).isLadder(this.f_26495_, entityPos.below(), this.ourEntity)) {
                    this.ourEntity.setYya(-0.5F);
                    return true;
                }
                return false;
            }
            if (!(this.f_26495_.getBlockState(this.ourEntity.m_20183_()).m_60734_() instanceof LadderBlock)) {
                this.ourEntity.m_20256_(this.ourEntity.m_20184_().add(0.0, 0.1, 0.0));
            }
            this.ourEntity.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, newSpeed);
        }
        return false;
    }

    private boolean handleEntityInWater(int oldIndex, PathPointExtended pEx) {
        int curIndex = this.m_26570_().getNextNodeIndex();
        if (curIndex > 0 && curIndex + 1 < this.m_26570_().getNodeCount() && this.m_26570_().getNode(curIndex - 1).y != pEx.f_77272_) {
            oldIndex = curIndex + 1;
        }
        this.m_26570_().setNextNodeIndex(oldIndex);
        Vec3 vec3d = this.m_26570_().getNextEntityPos(this.ourEntity);
        if (vec3d.distanceToSqr(new Vec3(this.ourEntity.m_20185_(), vec3d.y, this.ourEntity.m_20189_())) < 0.1 && Math.abs(this.ourEntity.m_20186_() - vec3d.y) < 0.5) {
            this.m_26570_().advance();
            if (this.isDone()) {
                return true;
            }
            vec3d = this.m_26570_().getNextEntityPos(this.ourEntity);
        }
        this.ourEntity.getMoveControl().setWantedPosition(vec3d.x, vec3d.y, vec3d.z, this.getSpeedFactor());
        return false;
    }

    @Override
    protected void followThePath() {
        this.getSpeedFactor();
        int curNode = this.f_26496_.getNextNodeIndex();
        int curNodeNext = curNode + 1;
        if (curNodeNext < this.f_26496_.getNodeCount()) {
            if (!(this.f_26496_.getNode(curNode) instanceof PathPointExtended)) {
                this.f_26496_ = this.convertPath(this.f_26496_);
            }
            PathPointExtended pEx = (PathPointExtended) this.f_26496_.getNode(curNode);
            PathPointExtended pExNext = (PathPointExtended) this.f_26496_.getNode(curNodeNext);
            if (pEx.isOnLadder() && pEx.getLadderFacing() == Direction.DOWN && !pExNext.isOnLadder()) {
                Vec3 vec3 = this.getTempMobPos();
                if (vec3.y - (double) pEx.f_77272_ < 0.001) {
                    this.f_26496_.setNextNodeIndex(curNodeNext);
                }
                return;
            }
        }
        this.f_26505_ = Math.max(1.2F, this.f_26494_.m_20205_());
        boolean wentAhead = false;
        boolean isTracking = AbstractPathJob.trackingMap.containsValue(this.ourEntity.m_20148_());
        int maxDropHeight = 3;
        HashSet<BlockPos> reached = new HashSet();
        for (int i = this.f_26496_.getNextNodeIndex(); i < Math.min(this.f_26496_.getNodeCount(), this.f_26496_.getNextNodeIndex() + 4); i++) {
            Vec3 next = this.f_26496_.getEntityPosAtNode(this.f_26494_, i);
            if (Math.abs(this.f_26494_.m_20185_() - next.x) < (double) this.f_26505_ - Math.abs(this.f_26494_.m_20186_() - next.y) * 0.1 && Math.abs(this.f_26494_.m_20189_() - next.z) < (double) this.f_26505_ - Math.abs(this.f_26494_.m_20186_() - next.y) * 0.1 && (Math.abs(this.f_26494_.m_20186_() - next.y) <= Math.min(1.0, Math.ceil((double) (this.f_26494_.m_20206_() / 2.0F))) || Math.abs(this.f_26494_.m_20186_() - next.y) <= Math.ceil((double) (this.f_26494_.m_20205_() / 2.0F)) * (double) maxDropHeight)) {
                this.f_26496_.advance();
                wentAhead = true;
                if (isTracking) {
                    Node point = this.f_26496_.getNode(i);
                    reached.add(new BlockPos(point.x, point.y, point.z));
                }
            }
        }
        if (isTracking) {
            AbstractPathJob.synchToClient(reached, this.ourEntity);
            reached.clear();
        }
        if (this.f_26496_.isDone()) {
            this.onPathFinish();
        } else if (!wentAhead) {
            if (curNode < this.f_26496_.getNodeCount() && curNode > 1) {
                Vec3 curr = this.f_26496_.getEntityPosAtNode(this.f_26494_, curNode - 1);
                Vec3 next = this.f_26496_.getEntityPosAtNode(this.f_26494_, curNode);
                Vec3i currI = new Vec3i((int) Math.round(curr.x), (int) Math.round(curr.y), (int) Math.round(curr.z));
                Vec3i nextI = new Vec3i((int) Math.round(next.x), (int) Math.round(next.y), (int) Math.round(next.z));
                if (this.f_26494_.m_20183_().m_123314_(currI, 2.0) && this.f_26494_.m_20183_().m_123314_(nextI, 2.0)) {
                    for (int currentIndex = curNode - 1; currentIndex > 0; currentIndex--) {
                        Vec3 tempoPos = this.f_26496_.getEntityPosAtNode(this.f_26494_, currentIndex);
                        Vec3i tempoPosI = new Vec3i((int) Math.round(tempoPos.x), (int) Math.round(tempoPos.y), (int) Math.round(tempoPos.z));
                        if (this.f_26494_.m_20183_().m_123314_(tempoPosI, 1.0)) {
                            this.f_26496_.setNextNodeIndex(currentIndex);
                        } else if (isTracking) {
                            reached.add(new BlockPos(tempoPosI));
                        }
                    }
                }
                if (isTracking) {
                    AbstractPathJob.synchToClient(reached, this.ourEntity);
                    reached.clear();
                }
            }
        }
    }

    private void onPathFinish() {
        this.stop();
    }

    @Override
    public void recomputePath() {
    }

    @Override
    protected void doStuckDetection(@NotNull Vec3 positionVec3) {
    }

    public boolean entityOnAndBelowPath(Entity entity, Vec3 slack) {
        Path path = this.m_26570_();
        if (path == null) {
            return false;
        } else {
            int closest = path.getNextNodeIndex();
            for (int i = 0; i < path.getNodeCount() - 1; i++) {
                if (closest + i < path.getNodeCount()) {
                    Node currentPoint = path.getNode(closest + i);
                    if (this.entityNearAndBelowPoint(currentPoint, entity, slack)) {
                        return true;
                    }
                }
                if (closest - i >= 0) {
                    Node currentPoint = path.getNode(closest - i);
                    if (this.entityNearAndBelowPoint(currentPoint, entity, slack)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private boolean entityNearAndBelowPoint(Node currentPoint, Entity entity, Vec3 slack) {
        return Math.abs((double) currentPoint.x - entity.getX()) < slack.x() && (double) currentPoint.y - entity.getY() + slack.y() > 0.0 && Math.abs((double) currentPoint.z - entity.getZ()) < slack.z();
    }

    @Override
    public void stop() {
        if (this.pathResult != null) {
            this.pathResult.cancel();
            this.pathResult.setStatus(PathFindingStatus.CANCELLED);
            this.pathResult = null;
        }
        this.destination = null;
        super.m_26573_();
    }

    @Nullable
    @Override
    public PathResult moveToLivingEntity(Entity e, double speed) {
        return this.moveToXYZ(e.getX(), e.getY(), e.getZ(), speed);
    }

    @Nullable
    @Override
    public PathResult moveAwayFromLivingEntity(Entity e, double distance, double speed) {
        return this.moveAwayFromXYZ(new BlockPos(e.blockPosition()), distance, speed, true);
    }

    @Override
    public void setCanFloat(boolean canSwim) {
        super.m_7008_(canSwim);
        this.getPathingOptions().setCanSwim(canSwim);
    }

    @Override
    public BlockPos getDesiredPos() {
        return this.desiredPos;
    }

    @Override
    public void setStuckHandler(IStuckHandler stuckHandler) {
        this.stuckHandler = stuckHandler;
    }

    @Override
    public void setSwimSpeedFactor(double factor) {
        this.swimSpeedFactor = factor;
    }

    public static boolean isEqual(BlockPos coords, int x, int y, int z) {
        return coords.m_123341_() == x && coords.m_123342_() == y && coords.m_123343_() == z;
    }

    public static boolean isEntityBlockLoaded(LevelAccessor world, BlockPos pos) {
        return WorldUtil.isEntityBlockLoaded(world, pos);
    }

    public static enum MovementType {

        WALKING, FLYING, CLIMBING
    }
}