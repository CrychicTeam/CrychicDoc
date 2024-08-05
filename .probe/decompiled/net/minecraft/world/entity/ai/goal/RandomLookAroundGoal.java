package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.Mob;

public class RandomLookAroundGoal extends Goal {

    private final Mob mob;

    private double relX;

    private double relZ;

    private int lookTime;

    public RandomLookAroundGoal(Mob mob0) {
        this.mob = mob0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.m_217043_().nextFloat() < 0.02F;
    }

    @Override
    public boolean canContinueToUse() {
        return this.lookTime >= 0;
    }

    @Override
    public void start() {
        double $$0 = (Math.PI * 2) * this.mob.m_217043_().nextDouble();
        this.relX = Math.cos($$0);
        this.relZ = Math.sin($$0);
        this.lookTime = 20 + this.mob.m_217043_().nextInt(20);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.lookTime--;
        this.mob.getLookControl().setLookAt(this.mob.m_20185_() + this.relX, this.mob.m_20188_(), this.mob.m_20189_() + this.relZ);
    }
}