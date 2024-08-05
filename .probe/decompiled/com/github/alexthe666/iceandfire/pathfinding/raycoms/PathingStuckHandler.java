package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.phys.Vec3;

public class PathingStuckHandler implements IStuckHandler {

    private static final double MIN_TARGET_DIST = 3.0;

    private final List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);

    private static final int MIN_TP_DELAY = 2400;

    private static final int MIN_DIST_FOR_TP = 10;

    private int teleportRange = 0;

    private int timePerBlockDistance = 100;

    private int stuckLevel = 0;

    private int globalTimeout = 0;

    private BlockPos prevDestination = BlockPos.ZERO;

    private boolean canBreakBlocks = false;

    private boolean canPlaceLadders = false;

    private boolean canBuildLeafBridges = false;

    private boolean canTeleportGoal = false;

    private boolean takeDamageOnCompleteStuck = false;

    private float damagePct = 0.2F;

    private int completeStuckBlockBreakRange = 0;

    private boolean hadPath = false;

    private int lastPathIndex = -1;

    private int progressedNodes = 0;

    private int delayBeforeActions = 1200;

    private int delayToNextUnstuckAction = this.delayBeforeActions;

    private BlockPos moveAwayStartPos = BlockPos.ZERO;

    private final Random rand = new Random();

    private PathingStuckHandler() {
    }

    public static PathingStuckHandler createStuckHandler() {
        return new PathingStuckHandler();
    }

    @Override
    public void checkStuck(AbstractAdvancedPathNavigate navigator) {
        if (navigator.getDesiredPos() != null && !navigator.getDesiredPos().equals(BlockPos.ZERO)) {
            double distanceToGoal = navigator.getOurEntity().m_20182_().distanceTo(new Vec3((double) navigator.getDesiredPos().m_123341_(), (double) navigator.getDesiredPos().m_123342_(), (double) navigator.getDesiredPos().m_123343_()));
            if (distanceToGoal < 3.0) {
                this.resetGlobalStuckTimers();
            } else {
                if (this.prevDestination.equals(navigator.getDesiredPos())) {
                    this.globalTimeout++;
                    if ((double) this.globalTimeout > Math.max(2400.0, (double) this.timePerBlockDistance * Math.max(10.0, distanceToGoal))) {
                        this.completeStuckAction(navigator);
                    }
                } else {
                    this.resetGlobalStuckTimers();
                }
                this.prevDestination = navigator.getDesiredPos();
                if (navigator.m_26570_() != null && !navigator.m_26570_().isDone()) {
                    if (navigator.m_26570_().getNextNodeIndex() == this.lastPathIndex) {
                        this.tryUnstuck(navigator);
                    } else if (this.lastPathIndex != -1 && navigator.m_26570_().getTarget().m_123331_(this.prevDestination) < 25.0) {
                        this.progressedNodes = navigator.m_26570_().getNextNodeIndex() > this.lastPathIndex ? this.progressedNodes + 1 : this.progressedNodes - 1;
                        if (this.progressedNodes > 5 && (navigator.m_26570_().getEndNode() == null || !this.moveAwayStartPos.equals(navigator.m_26570_().getEndNode().asBlockPos()))) {
                            this.resetStuckTimers();
                        }
                    }
                } else {
                    this.lastPathIndex = -1;
                    this.progressedNodes = 0;
                    if (!this.hadPath) {
                        this.tryUnstuck(navigator);
                    }
                }
                this.lastPathIndex = navigator.m_26570_() != null ? navigator.m_26570_().getNextNodeIndex() : -1;
                this.hadPath = navigator.m_26570_() != null && !navigator.m_26570_().isDone();
            }
        }
    }

    private void resetGlobalStuckTimers() {
        this.globalTimeout = 0;
        this.prevDestination = BlockPos.ZERO;
        this.resetStuckTimers();
    }

    private void completeStuckAction(AbstractAdvancedPathNavigate navigator) {
        BlockPos desired = navigator.getDesiredPos();
        Level world = navigator.getOurEntity().m_9236_();
        Mob entity = navigator.getOurEntity();
        if (this.canTeleportGoal) {
            BlockPos tpPos = findAround(world, desired, 10, 10, (posworld, pos) -> SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.below()), pos.below()) == SurfaceType.WALKABLE && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos), pos) == SurfaceType.DROPABLE && SurfaceType.getSurfaceType(posworld, posworld.getBlockState(pos.above()), pos.above()) == SurfaceType.DROPABLE);
            if (tpPos != null) {
                entity.m_6021_((double) tpPos.m_123341_() + 0.5, (double) tpPos.m_123342_(), (double) tpPos.m_123343_() + 0.5);
            }
        }
        if (this.takeDamageOnCompleteStuck) {
            entity.m_6469_(new DamageSource(entity.m_9236_().damageSources().inWall().typeHolder(), entity), entity.m_21233_() * this.damagePct);
        }
        if (this.completeStuckBlockBreakRange > 0) {
            Direction facing = getFacing(entity.m_20183_(), navigator.getDesiredPos());
            for (int i = 1; i <= this.completeStuckBlockBreakRange; i++) {
                if (!world.m_46859_(new BlockPos(entity.m_20183_()).relative(facing, i)) || !world.m_46859_(new BlockPos(entity.m_20183_()).relative(facing, i).above())) {
                    this.breakBlocksAhead(world, new BlockPos(entity.m_20183_()).relative(facing, i - 1), facing);
                    break;
                }
            }
        }
        navigator.m_26573_();
        this.resetGlobalStuckTimers();
    }

    private void tryUnstuck(AbstractAdvancedPathNavigate navigator) {
        if (this.delayToNextUnstuckAction-- <= 0) {
            this.delayToNextUnstuckAction = 50;
            if (this.stuckLevel == 0) {
                this.stuckLevel++;
                this.delayToNextUnstuckAction = 100;
                navigator.m_26573_();
            } else if (this.stuckLevel == 1) {
                this.stuckLevel++;
                this.delayToNextUnstuckAction = 200;
                navigator.m_26573_();
                navigator.moveAwayFromXYZ(new BlockPos(navigator.getOurEntity().m_20183_()), 10.0, 1.0, false);
                navigator.getPathingOptions().setCanClimb(false);
                this.moveAwayStartPos = navigator.getOurEntity().m_20183_();
            } else {
                if (this.stuckLevel == 2 && this.teleportRange > 0 && this.hadPath) {
                    int index = Math.min(navigator.m_26570_().getNextNodeIndex() + this.teleportRange, navigator.m_26570_().getNodeCount() - 1);
                    Node togo = navigator.m_26570_().getNode(index);
                    navigator.getOurEntity().m_6021_((double) togo.x + 0.5, (double) togo.y, (double) togo.z + 0.5);
                    this.delayToNextUnstuckAction = 300;
                }
                if (this.stuckLevel >= 3 && this.stuckLevel <= 5) {
                    if (this.canPlaceLadders && this.rand.nextBoolean()) {
                        this.delayToNextUnstuckAction = 200;
                        this.placeLadders(navigator);
                    } else if (this.canBuildLeafBridges && this.rand.nextBoolean()) {
                        this.delayToNextUnstuckAction = 100;
                        this.placeLeaves(navigator);
                    }
                }
                if (this.stuckLevel >= 6 && this.stuckLevel <= 8 && this.canBreakBlocks) {
                    this.delayToNextUnstuckAction = 200;
                    this.breakBlocks(navigator);
                }
                this.chanceStuckLevel();
                if (this.stuckLevel == 9) {
                    this.completeStuckAction(navigator);
                    this.resetStuckTimers();
                }
            }
        }
    }

    private void chanceStuckLevel() {
        this.stuckLevel++;
        if (this.stuckLevel > 1 && this.rand.nextInt(6) == 0) {
            this.stuckLevel -= 2;
        }
    }

    private void resetStuckTimers() {
        this.delayToNextUnstuckAction = this.delayBeforeActions;
        this.lastPathIndex = -1;
        this.progressedNodes = 0;
        this.stuckLevel = 0;
        this.moveAwayStartPos = BlockPos.ZERO;
    }

    private void breakBlocksAhead(Level world, BlockPos start, Direction facing) {
        if (!world.m_46859_(start.above(3))) {
            this.setAirIfPossible(world, start.above(3));
        } else if (!world.m_46859_(start.above().relative(facing))) {
            this.setAirIfPossible(world, start.above().relative(facing));
        } else {
            if (!world.m_46859_(start.relative(facing))) {
                this.setAirIfPossible(world, start.relative(facing));
            }
        }
    }

    private void setAirIfPossible(Level world, BlockPos pos) {
        Block blockAtPos = world.getBlockState(pos).m_60734_();
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }

    private void placeLadders(AbstractAdvancedPathNavigate navigator) {
        Level world = navigator.getOurEntity().m_9236_();
        Mob entity = navigator.getOurEntity();
        BlockPos entityPos = entity.m_20183_();
        while (world.getBlockState(entityPos).m_60734_() == Blocks.LADDER) {
            entityPos = entityPos.above();
        }
        this.tryPlaceLadderAt(world, entityPos);
        this.tryPlaceLadderAt(world, entityPos.above());
        this.tryPlaceLadderAt(world, entityPos.above(2));
    }

    private void placeLeaves(AbstractAdvancedPathNavigate navigator) {
        Level world = navigator.getOurEntity().m_9236_();
        Mob entity = navigator.getOurEntity();
        Direction badFacing = getFacing(entity.m_20183_(), navigator.getDesiredPos()).getOpposite();
        for (Direction dir : this.directions) {
            if (dir != badFacing && world.m_46859_(entity.m_20183_().below().relative(dir))) {
                world.setBlockAndUpdate(entity.m_20183_().below().relative(dir), Blocks.ACACIA_LEAVES.defaultBlockState());
            }
        }
    }

    public static Direction getFacing(BlockPos pos, BlockPos neighbor) {
        BlockPos vector = neighbor.subtract(pos);
        return Direction.getNearest((float) vector.m_123341_(), (float) vector.m_123342_(), (float) (-vector.m_123343_()));
    }

    private void breakBlocks(AbstractAdvancedPathNavigate navigator) {
        Level world = navigator.getOurEntity().m_9236_();
        Mob entity = navigator.getOurEntity();
        Direction facing = getFacing(entity.m_20183_(), navigator.getDesiredPos());
        this.breakBlocksAhead(world, entity.m_20183_(), facing);
    }

    private void tryPlaceLadderAt(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.m_60734_() != Blocks.LADDER && !state.m_60815_() && world.getFluidState(pos).isEmpty()) {
            for (Direction dir : this.directions) {
                BlockState toPlace = (BlockState) Blocks.LADDER.defaultBlockState().m_61124_(LadderBlock.FACING, dir.getOpposite());
                if (world.getBlockState(pos.relative(dir)).m_280296_() && Blocks.LADDER.m_7898_(toPlace, world, pos)) {
                    world.setBlockAndUpdate(pos, toPlace);
                    break;
                }
            }
        }
    }

    public PathingStuckHandler withBlockBreaks() {
        this.canBreakBlocks = true;
        return this;
    }

    public PathingStuckHandler withPlaceLadders() {
        this.canPlaceLadders = true;
        return this;
    }

    public PathingStuckHandler withBuildLeafBridges() {
        this.canBuildLeafBridges = true;
        return this;
    }

    public PathingStuckHandler withTeleportSteps(int steps) {
        this.teleportRange = steps;
        return this;
    }

    public PathingStuckHandler withTeleportOnFullStuck() {
        this.canTeleportGoal = true;
        return this;
    }

    public PathingStuckHandler withTakeDamageOnStuck(float damagePct) {
        this.damagePct = damagePct;
        this.takeDamageOnCompleteStuck = true;
        return this;
    }

    public PathingStuckHandler withTimePerBlockDistance(int time) {
        this.timePerBlockDistance = time;
        return this;
    }

    public PathingStuckHandler withDelayBeforeStuckActions(int delay) {
        this.delayBeforeActions = delay;
        return this;
    }

    public PathingStuckHandler withCompleteStuckBlockBreak(int range) {
        this.completeStuckBlockBreakRange = range;
        return this;
    }

    public static BlockPos findAround(Level world, BlockPos start, int vRange, int hRange, BiPredicate<BlockGetter, BlockPos> predicate) {
        if (vRange < 1 && hRange < 1) {
            return null;
        } else if (predicate.test(world, start)) {
            return start;
        } else {
            int y = 0;
            int y_offset = 1;
            for (int i = 0; i < hRange + 2; i++) {
                for (int steps = 1; steps <= vRange; steps++) {
                    BlockPos temp = start.offset(-steps, y, -steps);
                    for (int x = 0; x <= steps; x++) {
                        temp = temp.offset(1, 0, 0);
                        if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int z = 0; z <= steps; z++) {
                        temp = temp.offset(0, 0, 1);
                        if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int xx = 0; xx <= steps; xx++) {
                        temp = temp.offset(-1, 0, 0);
                        if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                    for (int zx = 0; zx <= steps; zx++) {
                        temp = temp.offset(0, 0, -1);
                        if (predicate.test(world, temp)) {
                            return temp;
                        }
                    }
                }
                y += y_offset;
                y_offset = y_offset > 0 ? y_offset + 1 : y_offset - 1;
                y_offset *= -1;
                if (world.m_151558_() <= start.m_123342_() + y) {
                    return null;
                }
            }
            return null;
        }
    }
}