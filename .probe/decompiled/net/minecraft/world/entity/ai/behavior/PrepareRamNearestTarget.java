package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class PrepareRamNearestTarget<E extends PathfinderMob> extends Behavior<E> {

    public static final int TIME_OUT_DURATION = 160;

    private final ToIntFunction<E> getCooldownOnFail;

    private final int minRamDistance;

    private final int maxRamDistance;

    private final float walkSpeed;

    private final TargetingConditions ramTargeting;

    private final int ramPrepareTime;

    private final Function<E, SoundEvent> getPrepareRamSound;

    private Optional<Long> reachedRamPositionTimestamp = Optional.empty();

    private Optional<PrepareRamNearestTarget.RamCandidate> ramCandidate = Optional.empty();

    public PrepareRamNearestTarget(ToIntFunction<E> toIntFunctionE0, int int1, int int2, float float3, TargetingConditions targetingConditions4, int int5, Function<E, SoundEvent> functionESoundEvent6) {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_ABSENT), 160);
        this.getCooldownOnFail = toIntFunctionE0;
        this.minRamDistance = int1;
        this.maxRamDistance = int2;
        this.walkSpeed = float3;
        this.ramTargeting = targetingConditions4;
        this.ramPrepareTime = int5;
        this.getPrepareRamSound = functionESoundEvent6;
    }

    protected void start(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        Brain<?> $$3 = pathfinderMob1.m_6274_();
        $$3.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(p_186049_ -> p_186049_.findClosest(p_147789_ -> this.ramTargeting.test(pathfinderMob1, p_147789_))).ifPresent(p_147778_ -> this.chooseRamPosition(pathfinderMob1, p_147778_));
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
        Brain<?> $$3 = e1.m_6274_();
        if (!$$3.hasMemoryValue(MemoryModuleType.RAM_TARGET)) {
            serverLevel0.broadcastEntityEvent(e1, (byte) 59);
            $$3.setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, this.getCooldownOnFail.applyAsInt(e1));
        }
    }

    protected boolean canStillUse(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        return this.ramCandidate.isPresent() && ((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getTarget().isAlive();
    }

    protected void tick(ServerLevel serverLevel0, E e1, long long2) {
        if (!this.ramCandidate.isEmpty()) {
            e1.m_6274_().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getStartPosition(), this.walkSpeed, 0));
            e1.m_6274_().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getTarget(), true));
            boolean $$3 = !((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getTarget().m_20183_().equals(((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getTargetPosition());
            if ($$3) {
                serverLevel0.broadcastEntityEvent(e1, (byte) 59);
                e1.m_21573_().stop();
                this.chooseRamPosition(e1, ((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).target);
            } else {
                BlockPos $$4 = e1.m_20183_();
                if ($$4.equals(((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getStartPosition())) {
                    serverLevel0.broadcastEntityEvent(e1, (byte) 58);
                    if (this.reachedRamPositionTimestamp.isEmpty()) {
                        this.reachedRamPositionTimestamp = Optional.of(long2);
                    }
                    if (long2 - (Long) this.reachedRamPositionTimestamp.get() >= (long) this.ramPrepareTime) {
                        e1.m_6274_().setMemory(MemoryModuleType.RAM_TARGET, this.getEdgeOfBlock($$4, ((PrepareRamNearestTarget.RamCandidate) this.ramCandidate.get()).getTargetPosition()));
                        serverLevel0.m_6269_(null, e1, (SoundEvent) this.getPrepareRamSound.apply(e1), SoundSource.NEUTRAL, 1.0F, e1.m_6100_());
                        this.ramCandidate = Optional.empty();
                    }
                }
            }
        }
    }

    private Vec3 getEdgeOfBlock(BlockPos blockPos0, BlockPos blockPos1) {
        double $$2 = 0.5;
        double $$3 = 0.5 * (double) Mth.sign((double) (blockPos1.m_123341_() - blockPos0.m_123341_()));
        double $$4 = 0.5 * (double) Mth.sign((double) (blockPos1.m_123343_() - blockPos0.m_123343_()));
        return Vec3.atBottomCenterOf(blockPos1).add($$3, 0.0, $$4);
    }

    private Optional<BlockPos> calculateRammingStartPosition(PathfinderMob pathfinderMob0, LivingEntity livingEntity1) {
        BlockPos $$2 = livingEntity1.m_20183_();
        if (!this.isWalkableBlock(pathfinderMob0, $$2)) {
            return Optional.empty();
        } else {
            List<BlockPos> $$3 = Lists.newArrayList();
            BlockPos.MutableBlockPos $$4 = $$2.mutable();
            for (Direction $$5 : Direction.Plane.HORIZONTAL) {
                $$4.set($$2);
                for (int $$6 = 0; $$6 < this.maxRamDistance; $$6++) {
                    if (!this.isWalkableBlock(pathfinderMob0, $$4.move($$5))) {
                        $$4.move($$5.getOpposite());
                        break;
                    }
                }
                if ($$4.m_123333_($$2) >= this.minRamDistance) {
                    $$3.add($$4.immutable());
                }
            }
            PathNavigation $$7 = pathfinderMob0.m_21573_();
            return $$3.stream().sorted(Comparator.comparingDouble(pathfinderMob0.m_20183_()::m_123331_)).filter(p_147753_ -> {
                Path $$2x = $$7.createPath(p_147753_, 0);
                return $$2x != null && $$2x.canReach();
            }).findFirst();
        }
    }

    private boolean isWalkableBlock(PathfinderMob pathfinderMob0, BlockPos blockPos1) {
        return pathfinderMob0.m_21573_().isStableDestination(blockPos1) && pathfinderMob0.m_21439_(WalkNodeEvaluator.getBlockPathTypeStatic(pathfinderMob0.m_9236_(), blockPos1.mutable())) == 0.0F;
    }

    private void chooseRamPosition(PathfinderMob pathfinderMob0, LivingEntity livingEntity1) {
        this.reachedRamPositionTimestamp = Optional.empty();
        this.ramCandidate = this.calculateRammingStartPosition(pathfinderMob0, livingEntity1).map(p_289369_ -> new PrepareRamNearestTarget.RamCandidate(p_289369_, livingEntity1.m_20183_(), livingEntity1));
    }

    public static class RamCandidate {

        private final BlockPos startPosition;

        private final BlockPos targetPosition;

        final LivingEntity target;

        public RamCandidate(BlockPos blockPos0, BlockPos blockPos1, LivingEntity livingEntity2) {
            this.startPosition = blockPos0;
            this.targetPosition = blockPos1;
            this.target = livingEntity2;
        }

        public BlockPos getStartPosition() {
            return this.startPosition;
        }

        public BlockPos getTargetPosition() {
            return this.targetPosition;
        }

        public LivingEntity getTarget() {
            return this.target;
        }
    }
}