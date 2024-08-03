package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class DeepOneTargetHostilePlayersGoal extends NearestAttackableTargetGoal {

    private DeepOneBaseEntity deepOne;

    public DeepOneTargetHostilePlayersGoal(DeepOneBaseEntity deepOne) {
        super(deepOne, Player.class, false, true);
        this.deepOne = deepOne;
    }

    @Override
    protected void findTarget() {
        this.f_26050_ = this.f_26135_.m_9236_().m_45982_(this.f_26135_.m_9236_().m_6443_(this.f_26048_, this.m_7255_(this.m_7623_()), targetEntity -> {
            if (targetEntity instanceof Player player && this.deepOne.getReactionTo(player) == DeepOneReaction.AGGRESSIVE && !player.isCreative()) {
                return true;
            }
            return false;
        }), this.f_26051_, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_());
    }
}