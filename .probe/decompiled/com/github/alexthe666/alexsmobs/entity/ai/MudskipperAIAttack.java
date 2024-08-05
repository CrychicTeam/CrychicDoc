package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMudBall;
import com.github.alexthe666.alexsmobs.entity.EntityMudskipper;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.gameevent.GameEvent;

public class MudskipperAIAttack extends Goal {

    private final EntityMudskipper entity;

    private int shootCooldown = 0;

    private boolean strafed = false;

    public MudskipperAIAttack(EntityMudskipper mob) {
        this.entity = mob;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.entity.m_5448_() != null && this.entity.m_5448_().isAlive();
    }

    @Override
    public void stop() {
        this.shootCooldown = 0;
        this.strafed = false;
    }

    @Override
    public void tick() {
        Entity target = this.entity.m_5448_();
        boolean keepFollowing = true;
        if (this.shootCooldown > 0) {
            this.shootCooldown--;
        }
        if (this.entity.m_21574_().hasLineOfSight(target)) {
            float dist = this.entity.m_20270_(target);
            if (dist < this.entity.m_20205_() + target.getBbWidth() + 3.0F) {
                keepFollowing = false;
                this.entity.m_21391_(target, 360.0F, 360.0F);
                this.entity.m_21566_().strafe(-3.0F, 0.0F);
                this.strafed = true;
            }
            if (dist < 8.0F && this.shootCooldown == 0) {
                EntityMudBall mudball = new EntityMudBall(this.entity.m_9236_(), this.entity);
                double d0 = target.getX() - mudball.m_20185_();
                double d1 = target.getY(0.3F) - mudball.m_20186_();
                double d2 = target.getZ() - mudball.m_20189_();
                float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.4F;
                mudball.shoot(d0, d1 + (double) f, d2, 1.0F, 10.0F);
                if (!this.entity.m_20067_()) {
                    this.entity.m_146850_(GameEvent.PROJECTILE_SHOOT);
                    this.entity.m_9236_().playSound(null, this.entity.m_20185_(), this.entity.m_20186_(), this.entity.m_20189_(), AMSoundRegistry.MUDSKIPPER_SPIT.get(), this.entity.m_5720_(), 1.0F, 1.0F + (this.entity.m_217043_().nextFloat() - this.entity.m_217043_().nextFloat()) * 0.2F);
                }
                this.entity.m_9236_().m_7967_(mudball);
                this.shootCooldown = 10 + this.entity.m_217043_().nextInt(10);
                this.entity.openMouth(10);
            }
        }
        if (keepFollowing) {
            if (this.strafed) {
                this.entity.m_21566_().strafe(0.0F, 0.0F);
                this.strafed = false;
            }
            this.entity.m_21573_().moveTo(target, 1.5);
        } else {
            this.entity.m_21573_().stop();
        }
    }
}