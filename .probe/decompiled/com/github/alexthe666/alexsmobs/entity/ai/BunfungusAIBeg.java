package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BunfungusAIBeg extends Goal {

    private static final TargetingConditions ENTITY_PREDICATE = TargetingConditions.forNonCombat().range(32.0);

    protected final EntityBunfungus jerboa;

    private final double speed;

    protected Player closestPlayer;

    private int delayTemptCounter;

    private boolean isRunning;

    public BunfungusAIBeg(EntityBunfungus jerboa, double speed) {
        this.jerboa = jerboa;
        this.speed = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.delayTemptCounter > 0) {
            this.delayTemptCounter--;
            return false;
        } else {
            this.closestPlayer = this.jerboa.m_9236_().m_45946_(ENTITY_PREDICATE, this.jerboa);
            return this.closestPlayer == null ? false : this.isFood(this.closestPlayer.m_21205_()) || this.isFood(this.closestPlayer.m_21206_());
        }
    }

    private boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.CARROT;
    }

    @Override
    public boolean canContinueToUse() {
        return this.jerboa.m_21205_().isEmpty() && this.canUse();
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.jerboa.m_21573_().stop();
        this.delayTemptCounter = 100;
        this.jerboa.setBegging(false);
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.jerboa.setSleeping(false);
        this.jerboa.m_21563_().setLookAt(this.closestPlayer, (float) (this.jerboa.m_8085_() + 20), (float) this.jerboa.m_8132_());
        if (this.jerboa.m_20280_(this.closestPlayer) < 12.0) {
            this.jerboa.m_21573_().stop();
            this.jerboa.setBegging(true);
        } else {
            this.jerboa.m_21573_().moveTo(this.closestPlayer, this.speed);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}