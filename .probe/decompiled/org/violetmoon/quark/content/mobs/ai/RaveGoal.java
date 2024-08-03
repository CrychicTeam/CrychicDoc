package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import org.violetmoon.quark.content.mobs.entity.Crab;

public class RaveGoal extends Goal {

    private final Crab crab;

    public RaveGoal(Crab crab) {
        this.crab = crab;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return this.crab.isRaving();
    }

    @Override
    public void start() {
        this.crab.m_21573_().stop();
    }
}