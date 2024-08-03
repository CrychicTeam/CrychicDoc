package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;

public class FloatGoal extends Goal {

    private final Mob mob;

    public FloatGoal(Mob mob0) {
        this.mob = mob0;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP));
        mob0.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        return this.mob.m_20069_() && this.mob.m_204036_(FluidTags.WATER) > this.mob.m_20204_() || this.mob.m_20077_();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mob.m_217043_().nextFloat() < 0.8F) {
            this.mob.getJumpControl().jump();
        }
    }
}