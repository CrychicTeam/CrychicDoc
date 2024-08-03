package org.violetmoon.quark.base.util;

import java.util.EnumSet;
import java.util.function.BooleanSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class IfFlagGoal extends Goal {

    private final Goal parent;

    private final BooleanSupplier isEnabled;

    public IfFlagGoal(Goal parent, BooleanSupplier isEnabled) {
        this.parent = parent;
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean canUse() {
        return this.isEnabled.getAsBoolean() && this.parent.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.isEnabled.getAsBoolean() && this.parent.canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return this.parent.isInterruptable();
    }

    @Override
    public void start() {
        this.parent.start();
    }

    @Override
    public void stop() {
        this.parent.stop();
    }

    @Override
    public void tick() {
        this.parent.tick();
    }

    @NotNull
    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return this.parent.getFlags();
    }
}