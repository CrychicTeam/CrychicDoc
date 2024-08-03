package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

public class LookAtTradingPlayerGoal extends LookAtPlayerGoal {

    private final AbstractVillager villager;

    public LookAtTradingPlayerGoal(AbstractVillager abstractVillager0) {
        super(abstractVillager0, Player.class, 8.0F);
        this.villager = abstractVillager0;
    }

    @Override
    public boolean canUse() {
        if (this.villager.isTrading()) {
            this.f_25513_ = this.villager.getTradingPlayer();
            return true;
        } else {
            return false;
        }
    }
}