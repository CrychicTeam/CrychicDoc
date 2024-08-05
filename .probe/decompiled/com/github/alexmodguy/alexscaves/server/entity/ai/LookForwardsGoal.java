package com.github.alexmodguy.alexscaves.server.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class LookForwardsGoal extends Goal {

    private Mob mob;

    public LookForwardsGoal(Mob mob) {
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.m_5616_(this.mob.m_146908_());
    }
}