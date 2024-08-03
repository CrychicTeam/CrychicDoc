package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;

public class FollowTemptation extends Behavior<PathfinderMob> {

    public static final int TEMPTATION_COOLDOWN = 100;

    public static final double CLOSE_ENOUGH_DIST = 2.5;

    private final Function<LivingEntity, Float> speedModifier;

    private final Function<LivingEntity, Double> closeEnoughDistance;

    public FollowTemptation(Function<LivingEntity, Float> functionLivingEntityFloat0) {
        this(functionLivingEntityFloat0, p_288784_ -> 2.5);
    }

    public FollowTemptation(Function<LivingEntity, Float> functionLivingEntityFloat0, Function<LivingEntity, Double> functionLivingEntityDouble1) {
        super(Util.make(() -> {
            Builder<MemoryModuleType<?>, MemoryStatus> $$0 = ImmutableMap.builder();
            $$0.put(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED);
            $$0.put(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED);
            $$0.put(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT);
            $$0.put(MemoryModuleType.IS_TEMPTED, MemoryStatus.REGISTERED);
            $$0.put(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_PRESENT);
            $$0.put(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
            $$0.put(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT);
            return $$0.build();
        }));
        this.speedModifier = functionLivingEntityFloat0;
        this.closeEnoughDistance = functionLivingEntityDouble1;
    }

    protected float getSpeedModifier(PathfinderMob pathfinderMob0) {
        return (Float) this.speedModifier.apply(pathfinderMob0);
    }

    private Optional<Player> getTemptingPlayer(PathfinderMob pathfinderMob0) {
        return pathfinderMob0.m_6274_().getMemory(MemoryModuleType.TEMPTING_PLAYER);
    }

    @Override
    protected boolean timedOut(long long0) {
        return false;
    }

    protected boolean canStillUse(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        return this.getTemptingPlayer(pathfinderMob1).isPresent() && !pathfinderMob1.m_6274_().hasMemoryValue(MemoryModuleType.BREED_TARGET) && !pathfinderMob1.m_6274_().hasMemoryValue(MemoryModuleType.IS_PANICKING);
    }

    protected void start(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        pathfinderMob1.m_6274_().setMemory(MemoryModuleType.IS_TEMPTED, true);
    }

    protected void stop(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        Brain<?> $$3 = pathfinderMob1.m_6274_();
        $$3.setMemory(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, 100);
        $$3.setMemory(MemoryModuleType.IS_TEMPTED, false);
        $$3.eraseMemory(MemoryModuleType.WALK_TARGET);
        $$3.eraseMemory(MemoryModuleType.LOOK_TARGET);
    }

    protected void tick(ServerLevel serverLevel0, PathfinderMob pathfinderMob1, long long2) {
        Player $$3 = (Player) this.getTemptingPlayer(pathfinderMob1).get();
        Brain<?> $$4 = pathfinderMob1.m_6274_();
        $$4.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker($$3, true));
        double $$5 = (Double) this.closeEnoughDistance.apply(pathfinderMob1);
        if (pathfinderMob1.m_20280_($$3) < Mth.square($$5)) {
            $$4.eraseMemory(MemoryModuleType.WALK_TARGET);
        } else {
            $$4.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker($$3, false), this.getSpeedModifier(pathfinderMob1), 2));
        }
    }
}