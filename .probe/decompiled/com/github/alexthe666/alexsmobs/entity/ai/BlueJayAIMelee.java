package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class BlueJayAIMelee extends Goal {

    private final EntityBlueJay blueJay;

    float circlingTime = 0.0F;

    float circleDistance = 1.0F;

    float yLevel = 2.0F;

    boolean clockwise = false;

    private int maxCircleTime;

    public BlueJayAIMelee(EntityBlueJay blueJay) {
        this.blueJay = blueJay;
    }

    @Override
    public boolean canUse() {
        Entity entity = this.blueJay.m_5448_();
        return entity != null && entity.isAlive();
    }

    @Override
    public void start() {
        this.clockwise = this.blueJay.m_217043_().nextBoolean();
        this.yLevel = (float) this.blueJay.m_217043_().nextInt(2);
        this.circlingTime = 0.0F;
        this.maxCircleTime = 20 + this.blueJay.m_217043_().nextInt(20);
        this.circleDistance = 0.5F + this.blueJay.m_217043_().nextFloat() * 2.0F;
    }

    @Override
    public void stop() {
        this.clockwise = this.blueJay.m_217043_().nextBoolean();
        this.yLevel = (float) this.blueJay.m_217043_().nextInt(2);
        this.circlingTime = 0.0F;
        this.maxCircleTime = 20 + this.blueJay.m_217043_().nextInt(20);
        this.circleDistance = 0.5F + this.blueJay.m_217043_().nextFloat() * 2.0F;
        if (this.blueJay.m_20096_()) {
            this.blueJay.setFlying(false);
        }
    }

    @Override
    public void tick() {
        if (this.blueJay.isFlying()) {
            this.circlingTime++;
        }
        LivingEntity target = this.blueJay.m_5448_();
        if (target != null) {
            if (this.blueJay.m_20270_(target) < 3.0F) {
                this.blueJay.peck();
                target.hurt(target.m_269291_().generic(), 1.0F);
                this.stop();
            }
            if (this.circlingTime > (float) this.maxCircleTime) {
                this.blueJay.m_21566_().setWantedPosition(target.m_20185_(), target.m_20186_() + (double) (target.m_20192_() / 2.0F), target.m_20189_(), 1.6F);
            } else {
                Vec3 circlePos = this.getVultureCirclePos(target.m_20182_());
                if (circlePos == null) {
                    circlePos = target.m_20182_();
                }
                this.blueJay.setFlying(true);
                this.blueJay.m_21566_().setWantedPosition(circlePos.x(), circlePos.y() + (double) target.m_20192_() + 0.2F, circlePos.z(), 1.6F);
            }
        }
    }

    public Vec3 getVultureCirclePos(Vec3 target) {
        float angle = 0.2268928F * (this.clockwise ? -this.circlingTime : this.circlingTime);
        double extraX = (double) (this.circleDistance * Mth.sin(angle));
        double extraZ = (double) (this.circleDistance * Mth.cos(angle));
        Vec3 pos = new Vec3(target.x() + extraX, target.y() + (double) this.yLevel, target.z() + extraZ);
        return this.blueJay.m_9236_().m_46859_(AMBlockPos.fromVec3(pos)) ? pos : null;
    }
}