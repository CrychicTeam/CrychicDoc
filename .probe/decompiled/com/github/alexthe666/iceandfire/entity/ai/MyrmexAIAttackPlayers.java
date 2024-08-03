package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class MyrmexAIAttackPlayers extends NearestAttackableTargetGoal {

    private final EntityMyrmexBase myrmex;

    public MyrmexAIAttackPlayers(final EntityMyrmexBase myrmex) {
        super(myrmex, Player.class, 10, true, true, new Predicate<Player>() {

            public boolean test(Player entity) {
                return entity != null && (myrmex.getHive() == null || myrmex.getHive().isPlayerReputationLowEnoughToFight(entity.m_20148_()));
            }
        });
        this.myrmex = myrmex;
    }

    @Override
    public boolean canUse() {
        return this.myrmex.shouldHaveNormalAI() && super.canUse();
    }
}