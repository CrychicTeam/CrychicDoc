package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class MyrmexAITradePlayer extends Goal {

    private final EntityMyrmexBase myrmex;

    public MyrmexAITradePlayer(EntityMyrmexBase myrmex) {
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.myrmex.m_6084_()) {
            return false;
        } else if (this.myrmex.m_20069_()) {
            return false;
        } else if (!this.myrmex.m_20096_()) {
            return false;
        } else if (this.myrmex.f_19864_) {
            return false;
        } else {
            Player PlayerEntity = this.myrmex.getTradingPlayer();
            if (PlayerEntity == null) {
                return false;
            } else if (this.myrmex.m_20280_(PlayerEntity) > 16.0) {
                return false;
            } else {
                return this.myrmex.getHive() != null && !this.myrmex.getHive().isPlayerReputationTooLowToTrade(PlayerEntity.m_20148_()) ? false : PlayerEntity.containerMenu != null;
            }
        }
    }

    @Override
    public void tick() {
        this.myrmex.m_21573_().stop();
    }

    @Override
    public void stop() {
        this.myrmex.setTradingPlayer(null);
    }
}