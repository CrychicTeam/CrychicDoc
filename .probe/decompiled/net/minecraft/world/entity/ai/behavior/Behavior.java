package net.minecraft.world.entity.ai.behavior;

import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public abstract class Behavior<E extends LivingEntity> implements BehaviorControl<E> {

    public static final int DEFAULT_DURATION = 60;

    protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;

    private Behavior.Status status = Behavior.Status.STOPPED;

    private long endTimestamp;

    private final int minDuration;

    private final int maxDuration;

    public Behavior(Map<MemoryModuleType<?>, MemoryStatus> mapMemoryModuleTypeMemoryStatus0) {
        this(mapMemoryModuleTypeMemoryStatus0, 60);
    }

    public Behavior(Map<MemoryModuleType<?>, MemoryStatus> mapMemoryModuleTypeMemoryStatus0, int int1) {
        this(mapMemoryModuleTypeMemoryStatus0, int1, int1);
    }

    public Behavior(Map<MemoryModuleType<?>, MemoryStatus> mapMemoryModuleTypeMemoryStatus0, int int1, int int2) {
        this.minDuration = int1;
        this.maxDuration = int2;
        this.entryCondition = mapMemoryModuleTypeMemoryStatus0;
    }

    @Override
    public Behavior.Status getStatus() {
        return this.status;
    }

    @Override
    public final boolean tryStart(ServerLevel serverLevel0, E e1, long long2) {
        if (this.hasRequiredMemories(e1) && this.checkExtraStartConditions(serverLevel0, e1)) {
            this.status = Behavior.Status.RUNNING;
            int $$3 = this.minDuration + serverLevel0.m_213780_().nextInt(this.maxDuration + 1 - this.minDuration);
            this.endTimestamp = long2 + (long) $$3;
            this.start(serverLevel0, e1, long2);
            return true;
        } else {
            return false;
        }
    }

    protected void start(ServerLevel serverLevel0, E e1, long long2) {
    }

    @Override
    public final void tickOrStop(ServerLevel serverLevel0, E e1, long long2) {
        if (!this.timedOut(long2) && this.canStillUse(serverLevel0, e1, long2)) {
            this.tick(serverLevel0, e1, long2);
        } else {
            this.doStop(serverLevel0, e1, long2);
        }
    }

    protected void tick(ServerLevel serverLevel0, E e1, long long2) {
    }

    @Override
    public final void doStop(ServerLevel serverLevel0, E e1, long long2) {
        this.status = Behavior.Status.STOPPED;
        this.stop(serverLevel0, e1, long2);
    }

    protected void stop(ServerLevel serverLevel0, E e1, long long2) {
    }

    protected boolean canStillUse(ServerLevel serverLevel0, E e1, long long2) {
        return false;
    }

    protected boolean timedOut(long long0) {
        return long0 > this.endTimestamp;
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, E e1) {
        return true;
    }

    @Override
    public String debugString() {
        return this.getClass().getSimpleName();
    }

    protected boolean hasRequiredMemories(E e0) {
        for (Entry<MemoryModuleType<?>, MemoryStatus> $$1 : this.entryCondition.entrySet()) {
            MemoryModuleType<?> $$2 = (MemoryModuleType<?>) $$1.getKey();
            MemoryStatus $$3 = (MemoryStatus) $$1.getValue();
            if (!e0.getBrain().checkMemory($$2, $$3)) {
                return false;
            }
        }
        return true;
    }

    public static enum Status {

        STOPPED, RUNNING
    }
}