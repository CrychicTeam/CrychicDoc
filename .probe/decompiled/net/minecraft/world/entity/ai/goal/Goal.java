package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.util.Mth;

public abstract class Goal {

    private final EnumSet<Goal.Flag> flags = EnumSet.noneOf(Goal.Flag.class);

    public abstract boolean canUse();

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public boolean isInterruptable() {
        return true;
    }

    public void start() {
    }

    public void stop() {
    }

    public boolean requiresUpdateEveryTick() {
        return false;
    }

    public void tick() {
    }

    public void setFlags(EnumSet<Goal.Flag> enumSetGoalFlag0) {
        this.flags.clear();
        this.flags.addAll(enumSetGoalFlag0);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    public EnumSet<Goal.Flag> getFlags() {
        return this.flags;
    }

    protected int adjustedTickDelay(int int0) {
        return this.requiresUpdateEveryTick() ? int0 : reducedTickDelay(int0);
    }

    protected static int reducedTickDelay(int int0) {
        return Mth.positiveCeilDiv(int0, 2);
    }

    public static enum Flag {

        MOVE, LOOK, JUMP, TARGET
    }
}