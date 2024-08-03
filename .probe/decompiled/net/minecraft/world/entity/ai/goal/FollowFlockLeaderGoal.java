package net.minecraft.world.entity.ai.goal;

import com.mojang.datafixers.DataFixUtils;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;

public class FollowFlockLeaderGoal extends Goal {

    private static final int INTERVAL_TICKS = 200;

    private final AbstractSchoolingFish mob;

    private int timeToRecalcPath;

    private int nextStartTick;

    public FollowFlockLeaderGoal(AbstractSchoolingFish abstractSchoolingFish0) {
        this.mob = abstractSchoolingFish0;
        this.nextStartTick = this.nextStartTick(abstractSchoolingFish0);
    }

    protected int nextStartTick(AbstractSchoolingFish abstractSchoolingFish0) {
        return m_186073_(200 + abstractSchoolingFish0.m_217043_().nextInt(200) % 20);
    }

    @Override
    public boolean canUse() {
        if (this.mob.hasFollowers()) {
            return false;
        } else if (this.mob.isFollower()) {
            return true;
        } else if (this.nextStartTick > 0) {
            this.nextStartTick--;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            Predicate<AbstractSchoolingFish> $$0 = p_25258_ -> p_25258_.canBeFollowed() || !p_25258_.isFollower();
            List<? extends AbstractSchoolingFish> $$1 = this.mob.m_9236_().m_6443_(this.mob.getClass(), this.mob.m_20191_().inflate(8.0, 8.0, 8.0), $$0);
            AbstractSchoolingFish $$2 = (AbstractSchoolingFish) DataFixUtils.orElse($$1.stream().filter(AbstractSchoolingFish::m_27542_).findAny(), this.mob);
            $$2.addFollowers($$1.stream().filter(p_25255_ -> !p_25255_.isFollower()));
            return this.mob.isFollower();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isFollower() && this.mob.inRangeOfLeader();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        this.mob.stopFollowing();
    }

    @Override
    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.m_183277_(10);
            this.mob.pathToLeader();
        }
    }
}