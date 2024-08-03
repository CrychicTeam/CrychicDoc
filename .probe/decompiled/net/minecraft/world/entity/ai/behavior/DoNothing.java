package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class DoNothing implements BehaviorControl<LivingEntity> {

    private final int minDuration;

    private final int maxDuration;

    private Behavior.Status status = Behavior.Status.STOPPED;

    private long endTimestamp;

    public DoNothing(int int0, int int1) {
        this.minDuration = int0;
        this.maxDuration = int1;
    }

    @Override
    public Behavior.Status getStatus() {
        return this.status;
    }

    @Override
    public final boolean tryStart(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        this.status = Behavior.Status.RUNNING;
        int $$3 = this.minDuration + serverLevel0.m_213780_().nextInt(this.maxDuration + 1 - this.minDuration);
        this.endTimestamp = long2 + (long) $$3;
        return true;
    }

    @Override
    public final void tickOrStop(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        if (long2 > this.endTimestamp) {
            this.doStop(serverLevel0, livingEntity1, long2);
        }
    }

    @Override
    public final void doStop(ServerLevel serverLevel0, LivingEntity livingEntity1, long long2) {
        this.status = Behavior.Status.STOPPED;
    }

    @Override
    public String debugString() {
        return this.getClass().getSimpleName();
    }
}