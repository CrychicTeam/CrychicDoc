package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityFroststalker;
import com.mojang.datafixers.DataFixUtils;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class FroststalkerAIFollowLeader extends Goal {

    private static final int INTERVAL_TICKS = 200;

    private final EntityFroststalker mob;

    private int timeToRecalcPath;

    private int nextStartTick;

    public FroststalkerAIFollowLeader(EntityFroststalker froststalker) {
        this.mob = froststalker;
        this.nextStartTick = this.nextStartTick(froststalker);
    }

    protected int nextStartTick(EntityFroststalker froststalker) {
        return 100 + froststalker.m_217043_().nextInt(200) % 40;
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
            Predicate<EntityFroststalker> froststalkerPredicate = p_25258_ -> p_25258_.canBeFollowed() || !p_25258_.isFollower();
            float range = 60.0F;
            List<Player> playerList = this.mob.m_9236_().m_6443_(Player.class, this.mob.m_20191_().inflate((double) range, (double) range, (double) range), EntityFroststalker.VALID_LEADER_PLAYERS);
            Player closestPlayer = null;
            for (Player player : playerList) {
                if (closestPlayer == null || player.m_20270_(this.mob) < closestPlayer.m_20270_(this.mob)) {
                    closestPlayer = player;
                }
            }
            if (closestPlayer == null) {
                List<EntityFroststalker> list = this.mob.m_9236_().m_6443_(EntityFroststalker.class, this.mob.m_20191_().inflate((double) range, (double) range, (double) range), froststalkerPredicate);
                EntityFroststalker entityFroststalker = (EntityFroststalker) DataFixUtils.orElse(list.stream().filter(EntityFroststalker::canBeFollowed).findAny(), this.mob);
                entityFroststalker.addFollowers(list.stream().filter(p_25255_ -> !p_25255_.isFollower()));
            } else {
                this.mob.startFollowing(closestPlayer);
            }
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
            this.timeToRecalcPath = 10;
            this.mob.pathToLeader();
        }
    }
}