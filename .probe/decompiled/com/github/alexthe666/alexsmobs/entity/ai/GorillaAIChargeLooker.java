package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGorilla;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GorillaAIChargeLooker extends Goal {

    private final EntityGorilla gorilla;

    private final double range = 20.0;

    private Player starer;

    private final double speed;

    private int runDelay = 0;

    public GorillaAIChargeLooker(EntityGorilla gorilla, double speed) {
        this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        this.gorilla = gorilla;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        if (this.gorilla.isSilverback() && !this.gorilla.m_21824_() && this.runDelay-- == 0) {
            this.runDelay = 100 + this.gorilla.m_217043_().nextInt(200);
            List<Player> playerList = this.gorilla.m_9236_().m_6443_(Player.class, this.gorilla.m_20191_().inflate(20.0, 20.0, 20.0), EntitySelector.NO_SPECTATORS);
            Player closestPlayer = null;
            for (Player player : playerList) {
                if (this.isLookingAtMe(player) && (closestPlayer == null || player.m_20270_(this.gorilla) < closestPlayer.m_20270_(this.gorilla))) {
                    closestPlayer = player;
                }
            }
            this.starer = closestPlayer;
            return this.starer != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.starer != null && this.gorilla.m_6084_();
    }

    @Override
    public void stop() {
        this.starer = null;
        this.gorilla.m_6858_(false);
        this.runDelay = 300 + this.gorilla.m_217043_().nextInt(200);
    }

    @Override
    public void tick() {
        this.gorilla.setOrderedToSit(false);
        this.gorilla.poundChestCooldown = 50;
        if (this.gorilla.m_20270_(this.starer) > 1.0F + this.starer.m_20205_() + this.gorilla.m_20205_()) {
            this.gorilla.getNavigation().moveTo(this.starer, this.speed);
            this.gorilla.m_6858_(!this.gorilla.isSitting() && !this.gorilla.isStanding());
        } else {
            this.gorilla.getNavigation().stop();
            this.gorilla.m_21391_(this.starer, 180.0F, 30.0F);
            this.gorilla.m_6858_(false);
            if (this.gorilla.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.gorilla.setStanding(true);
                this.gorilla.maxStandTime = 45;
                this.gorilla.setAnimation(EntityGorilla.ANIMATION_POUNDCHEST);
            }
            if (this.gorilla.getAnimation() == EntityGorilla.ANIMATION_POUNDCHEST && this.gorilla.getAnimationTick() >= 10) {
                this.stop();
            }
        }
    }

    private boolean isLookingAtMe(Player player) {
        Vec3 vec3 = player.m_20252_(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.gorilla.m_20185_() - player.m_20185_(), this.gorilla.m_20188_() - player.m_20188_(), this.gorilla.m_20189_() - player.m_20189_());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > 1.0 - 0.025 / d0 && player.m_142582_(this.gorilla);
    }
}