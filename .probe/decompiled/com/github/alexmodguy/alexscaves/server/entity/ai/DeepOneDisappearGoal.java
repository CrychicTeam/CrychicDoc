package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import java.util.EnumSet;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class DeepOneDisappearGoal extends Goal {

    private DeepOneBaseEntity deepOne;

    private int bombCooldown = 0;

    private int dissapearIn = 20;

    public DeepOneDisappearGoal(DeepOneBaseEntity deepOne) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.deepOne = deepOne;
    }

    @Override
    public boolean canUse() {
        Player player = this.deepOne.getCorneringPlayer();
        return player != null && !player.isSpectator() && player.m_6084_() && this.deepOne.m_20270_(player) < 10.0F && !this.deepOne.isTradingLocked();
    }

    @Override
    public void stop() {
        this.deepOne.setCorneredBy(null);
    }

    @Override
    public void start() {
        this.deepOne.setDeepOneSwimming(false);
        this.bombCooldown = 10;
        this.dissapearIn = 20;
    }

    @Override
    public void tick() {
        Player player = this.deepOne.getCorneringPlayer();
        if (player != null) {
            this.deepOne.m_21563_().setLookAt(player.m_20185_(), player.m_20188_(), player.m_20189_(), 20.0F, (float) this.deepOne.m_8132_());
            this.bombCooldown--;
            if (player.m_21023_(MobEffects.BLINDNESS)) {
                if (this.dissapearIn-- < 0) {
                    this.deepOne.m_142687_(Entity.RemovalReason.KILLED);
                }
            } else if (this.bombCooldown < 0 && !this.deepOne.isDeepOneSwimming() && this.deepOne.startDisappearBehavior(player)) {
                this.bombCooldown = 10;
                this.dissapearIn = 0;
            }
        }
    }
}