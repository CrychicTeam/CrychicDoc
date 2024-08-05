package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

public class AnimalPanic extends Behavior<PathfinderMob> {

    private static final int PANIC_MIN_DURATION = 100;

    private static final int PANIC_MAX_DURATION = 120;

    private static final int PANIC_DISTANCE_HORIZONTAL = 5;

    private static final int PANIC_DISTANCE_VERTICAL = 4;

    private static final Predicate<PathfinderMob> DEFAULT_SHOULD_PANIC_PREDICATE = p_289313_ -> p_289313_.m_21188_() != null || p_289313_.m_203117_() || p_289313_.m_6060_();

    private final float speedMultiplier;

    private final Predicate<PathfinderMob> shouldPanic;

    public AnimalPanic(float float0) {
        this(float0, DEFAULT_SHOULD_PANIC_PREDICATE);
    }

    public AnimalPanic(float float0, Predicate<PathfinderMob> predicatePathfinderMob1) {
        super(ImmutableMap.of(MemoryModuleType.IS_PANICKING, MemoryStatus.REGISTERED, MemoryModuleType.HURT_BY, MemoryStatus.VALUE_PRESENT), 100, 120);
        this.speedMultiplier = float0;
        this.shouldPanic = predicatePathfinderMob1;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, PathfinderMob pathfinderMob1) {
        return this.shouldPanic.test(pathfinderMob1);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        return true;
    }

    protected void start(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        pathfinderMob1.m_6274_().setMemory(MemoryModuleType.IS_PANICKING, true);
        pathfinderMob1.m_6274_().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    protected void stop(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        Brain<?> $$3 = pathfinderMob1.m_6274_();
        $$3.eraseMemory(MemoryModuleType.IS_PANICKING);
    }

    protected void tick(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        if (pathfinderMob1.m_21573_().isDone()) {
            Vec3 $$3 = this.getPanicPos(pathfinderMob1, serverLevel0);
            if ($$3 != null) {
                pathfinderMob1.m_6274_().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget($$3, this.speedMultiplier, 0));
            }
        }
    }

    @Nullable
    private Vec3 getPanicPos(PathfinderMob pathfinderMob0, ServerLevel serverLevel1) {
        if (pathfinderMob0.m_6060_()) {
            Optional<Vec3> $$2 = this.lookForWater(serverLevel1, pathfinderMob0).map(Vec3::m_82539_);
            if ($$2.isPresent()) {
                return (Vec3) $$2.get();
            }
        }
        return LandRandomPos.getPos(pathfinderMob0, 5, 4);
    }

    private Optional<BlockPos> lookForWater(BlockGetter blockGetter0, Entity entity1) {
        BlockPos $$2 = entity1.blockPosition();
        if (!blockGetter0.getBlockState($$2).m_60812_(blockGetter0, $$2).isEmpty()) {
            return Optional.empty();
        } else {
            Predicate<BlockPos> $$3;
            if (Mth.ceil(entity1.getBbWidth()) == 2) {
                $$3 = p_284705_ -> BlockPos.squareOutSouthEast(p_284705_).allMatch(p_196646_ -> blockGetter0.getFluidState(p_196646_).is(FluidTags.WATER));
            } else {
                $$3 = p_284707_ -> blockGetter0.getFluidState(p_284707_).is(FluidTags.WATER);
            }
            return BlockPos.findClosestMatch($$2, 5, 1, $$3);
        }
    }
}