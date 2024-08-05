package com.github.alexthe666.alexsmobs.entity.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TameableAIRide extends Goal {

    private final PathfinderMob tameableEntity;

    private LivingEntity player;

    private final double speed;

    private final boolean strafe;

    public TameableAIRide(PathfinderMob dragon, double speed) {
        this(dragon, speed, true);
    }

    public TameableAIRide(PathfinderMob dragon, double speed, boolean strafe) {
        this.tameableEntity = dragon;
        this.speed = speed;
        this.strafe = strafe;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.tameableEntity.m_6688_() instanceof Player && this.tameableEntity.m_20160_()) {
            this.player = (Player) this.tameableEntity.m_6688_();
            return true;
        } else {
            this.tameableEntity.m_6858_(false);
            return false;
        }
    }

    @Override
    public void start() {
        this.tameableEntity.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.tameableEntity.m_274367_(1.0F);
        this.tameableEntity.m_21573_().stop();
        this.tameableEntity.m_6710_(null);
        double x = this.tameableEntity.m_20185_();
        double y = this.tameableEntity.m_20186_();
        double z = this.tameableEntity.m_20189_();
        if (this.strafe) {
            this.tameableEntity.f_20900_ = this.player.xxa * 0.15F;
        }
        if (this.shouldMoveForward() && this.tameableEntity.m_20160_()) {
            this.tameableEntity.m_6858_(true);
            Vec3 lookVec = this.player.m_20154_();
            if (this.shouldMoveBackwards()) {
                lookVec = lookVec.yRot((float) Math.PI);
            }
            x += lookVec.x * 10.0;
            z += lookVec.z * 10.0;
            y += this.modifyYPosition(lookVec.y);
            this.tameableEntity.m_21566_().setWantedPosition(x, y, z, this.speed);
        } else {
            this.tameableEntity.m_6858_(false);
        }
    }

    public double modifyYPosition(double lookVecY) {
        return this.tameableEntity instanceof FlyingAnimal ? lookVecY * 10.0 : 0.0;
    }

    public boolean shouldMoveForward() {
        return this.player.zza != 0.0F;
    }

    public boolean shouldMoveBackwards() {
        return this.player.zza < 0.0F;
    }
}