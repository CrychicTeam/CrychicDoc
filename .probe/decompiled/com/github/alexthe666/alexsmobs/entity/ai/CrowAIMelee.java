package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CrowAIMelee extends Goal {

    private final EntityCrow crow;

    float circlingTime = 0.0F;

    float circleDistance = 1.0F;

    float yLevel = 2.0F;

    boolean clockwise = false;

    private int maxCircleTime;

    public CrowAIMelee(EntityCrow crow) {
        this.crow = crow;
    }

    @Override
    public boolean canUse() {
        return this.crow.m_5448_() != null && !this.crow.isSitting() && this.crow.getCommand() != 3;
    }

    @Override
    public void start() {
        this.clockwise = this.crow.m_217043_().nextBoolean();
        this.yLevel = (float) this.crow.m_217043_().nextInt(2);
        this.circlingTime = 0.0F;
        this.maxCircleTime = 20 + this.crow.m_217043_().nextInt(100);
        this.circleDistance = 1.0F + this.crow.m_217043_().nextFloat() * 3.0F;
    }

    @Override
    public void stop() {
        this.clockwise = this.crow.m_217043_().nextBoolean();
        this.yLevel = (float) this.crow.m_217043_().nextInt(2);
        this.circlingTime = 0.0F;
        this.maxCircleTime = 20 + this.crow.m_217043_().nextInt(100);
        this.circleDistance = 1.0F + this.crow.m_217043_().nextFloat() * 3.0F;
        if (this.crow.m_20096_()) {
            this.crow.setFlying(false);
        }
    }

    @Override
    public void tick() {
        LivingEntity target = this.crow.m_5448_();
        if (target != null) {
            if (this.circlingTime > (float) this.maxCircleTime) {
                this.crow.m_21566_().setWantedPosition(target.m_20185_(), target.m_20186_() + (double) (target.m_20192_() / 2.0F), target.m_20189_(), 1.3F);
                if (this.crow.m_20270_(target) < 2.0F) {
                    this.crow.peck();
                    if (target.getMobType() == MobType.UNDEAD) {
                        target.hurt(target.m_269291_().generic(), 4.0F);
                    } else {
                        target.hurt(target.m_269291_().generic(), 1.0F);
                    }
                    this.stop();
                }
            } else {
                Vec3 circlePos = this.getVultureCirclePos(target.m_20182_());
                if (circlePos == null) {
                    circlePos = target.m_20182_();
                }
                this.crow.setFlying(true);
                this.crow.m_21566_().setWantedPosition(circlePos.x(), circlePos.y() + (double) target.m_20192_() + 0.2F, circlePos.z(), 1.0);
            }
        }
        if (this.crow.isFlying()) {
            this.circlingTime++;
        }
    }

    public Vec3 getVultureCirclePos(Vec3 target) {
        float angle = 0.13962634F * (this.clockwise ? -this.circlingTime : this.circlingTime);
        double extraX = (double) (this.circleDistance * Mth.sin(angle));
        double extraZ = (double) (this.circleDistance * Mth.cos(angle));
        Vec3 pos = new Vec3(target.x() + extraX, target.y() + (double) this.yLevel, target.z() + extraZ);
        return this.crow.m_9236_().m_46859_(AMBlockPos.fromVec3(pos)) ? pos : null;
    }
}