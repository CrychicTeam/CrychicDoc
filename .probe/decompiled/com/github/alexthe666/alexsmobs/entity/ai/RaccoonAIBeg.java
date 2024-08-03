package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityRaccoon;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class RaccoonAIBeg extends Goal {

    private static final TargetingConditions ENTITY_PREDICATE = TargetingConditions.forNonCombat().range(32.0);

    protected final EntityRaccoon raccoon;

    private final double speed;

    protected Player closestPlayer;

    private int delayTemptCounter;

    private boolean isRunning;

    public RaccoonAIBeg(EntityRaccoon raccoon, double speed) {
        this.raccoon = raccoon;
        this.speed = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.delayTemptCounter > 0) {
            this.delayTemptCounter--;
            return false;
        } else if (!this.raccoon.m_21205_().isEmpty()) {
            return false;
        } else {
            this.closestPlayer = this.raccoon.m_9236_().m_45946_(ENTITY_PREDICATE, this.raccoon);
            return this.closestPlayer == null ? false : EntityRaccoon.isRaccoonFood(this.closestPlayer.m_21205_()) || EntityRaccoon.isRaccoonFood(this.closestPlayer.m_21206_());
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.raccoon.m_21205_().isEmpty() && this.canUse();
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.raccoon.m_21573_().stop();
        this.delayTemptCounter = 100;
        this.raccoon.setBegging(false);
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.raccoon.m_21563_().setLookAt(this.closestPlayer, (float) (this.raccoon.m_8085_() + 20), (float) this.raccoon.m_8132_());
        if (this.raccoon.m_20280_(this.closestPlayer) < 12.0) {
            this.raccoon.m_21573_().stop();
            this.raccoon.setBegging(true);
        } else {
            this.raccoon.m_21573_().moveTo(this.closestPlayer, this.speed);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}