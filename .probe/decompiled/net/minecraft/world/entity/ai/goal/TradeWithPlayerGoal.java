package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

public class TradeWithPlayerGoal extends Goal {

    private final AbstractVillager mob;

    public TradeWithPlayerGoal(AbstractVillager abstractVillager0) {
        this.mob = abstractVillager0;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.m_6084_()) {
            return false;
        } else if (this.mob.m_20069_()) {
            return false;
        } else if (!this.mob.m_20096_()) {
            return false;
        } else if (this.mob.f_19864_) {
            return false;
        } else {
            Player $$0 = this.mob.getTradingPlayer();
            if ($$0 == null) {
                return false;
            } else {
                return this.mob.m_20280_($$0) > 16.0 ? false : $$0.containerMenu != null;
            }
        }
    }

    @Override
    public void start() {
        this.mob.m_21573_().stop();
    }

    @Override
    public void stop() {
        this.mob.setTradingPlayer(null);
    }
}