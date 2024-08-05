package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;

public class WrappedGoal extends Goal {

    private final Goal goal;

    private final int priority;

    private boolean isRunning;

    public WrappedGoal(int int0, Goal goal1) {
        this.priority = int0;
        this.goal = goal1;
    }

    public boolean canBeReplacedBy(WrappedGoal wrappedGoal0) {
        return this.isInterruptable() && wrappedGoal0.getPriority() < this.getPriority();
    }

    @Override
    public boolean canUse() {
        return this.goal.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.goal.canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return this.goal.isInterruptable();
    }

    @Override
    public void start() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.goal.start();
        }
    }

    @Override
    public void stop() {
        if (this.isRunning) {
            this.isRunning = false;
            this.goal.stop();
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return this.goal.requiresUpdateEveryTick();
    }

    @Override
    protected int adjustedTickDelay(int int0) {
        return this.goal.adjustedTickDelay(int0);
    }

    @Override
    public void tick() {
        this.goal.tick();
    }

    @Override
    public void setFlags(EnumSet<Goal.Flag> enumSetGoalFlag0) {
        this.goal.setFlags(enumSetGoalFlag0);
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return this.goal.getFlags();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public int getPriority() {
        return this.priority;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public boolean equals(@Nullable Object object0) {
        if (this == object0) {
            return true;
        } else {
            return object0 != null && this.getClass() == object0.getClass() ? this.goal.equals(((WrappedGoal) object0).goal) : false;
        }
    }

    public int hashCode() {
        return this.goal.hashCode();
    }
}