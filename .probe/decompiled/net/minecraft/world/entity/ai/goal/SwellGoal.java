package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;

public class SwellGoal extends Goal {

    private final Creeper creeper;

    @Nullable
    private LivingEntity target;

    public SwellGoal(Creeper creeper0) {
        this.creeper = creeper0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = this.creeper.m_5448_();
        return this.creeper.getSwellDir() > 0 || $$0 != null && this.creeper.m_20280_($$0) < 9.0;
    }

    @Override
    public void start() {
        this.creeper.m_21573_().stop();
        this.target = this.creeper.m_5448_();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.target == null) {
            this.creeper.setSwellDir(-1);
        } else if (this.creeper.m_20280_(this.target) > 49.0) {
            this.creeper.setSwellDir(-1);
        } else if (!this.creeper.m_21574_().hasLineOfSight(this.target)) {
            this.creeper.setSwellDir(-1);
        } else {
            this.creeper.setSwellDir(1);
        }
    }
}