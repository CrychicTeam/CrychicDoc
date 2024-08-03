package com.mna.entities.ai;

import com.mna.tools.SummonUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class NeverTargetOwnerGoal extends Goal {

    private final Mob goalOwner;

    public NeverTargetOwnerGoal(Mob mobIn) {
        this.goalOwner = mobIn;
    }

    @Override
    public boolean canUse() {
        return this.goalOwner.getTarget() == SummonUtils.getSummoner(this.goalOwner);
    }

    @Override
    public void start() {
        this.goalOwner.setTarget(null);
    }
}