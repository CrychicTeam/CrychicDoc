package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityCaiman;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CaimanAIMelee extends Goal {

    private final EntityCaiman caiman;

    private int grabTime = 0;

    public CaimanAIMelee(EntityCaiman caiman) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.caiman = caiman;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.caiman.m_5448_();
        return target != null && target.isAlive();
    }

    @Override
    public void stop() {
        this.caiman.setHeldMobId(-1);
        this.grabTime = 0;
    }

    @Override
    public void tick() {
        if (this.grabTime < 0) {
            this.grabTime++;
        }
        LivingEntity target = this.caiman.m_5448_();
        if (target != null) {
            double bbWidth = (double) (this.caiman.m_20205_() + target.m_20205_()) / 2.0;
            double dist = (double) this.caiman.m_20270_(target);
            boolean flag = false;
            if (dist < bbWidth + 2.0 && this.grabTime >= 0) {
                if (this.grabTime % 25 == 0) {
                    target.hurt(this.caiman.m_21824_() ? this.caiman.m_269291_().drown() : this.caiman.m_269291_().mobAttack(this.caiman), (float) this.caiman.m_21133_(Attributes.ATTACK_DAMAGE));
                }
                this.grabTime++;
                Vec3 shakePreyPos = this.caiman.getShakePreyPos();
                Vec3 minus = new Vec3(shakePreyPos.x - target.m_20185_(), 0.0, shakePreyPos.z - target.m_20189_()).normalize();
                target.m_20256_(target.m_20184_().multiply(0.6F, 0.6F, 0.6F).add(minus.scale(0.35F)));
                flag = true;
                if (this.grabTime > this.getGrabDuration()) {
                    this.grabTime = -10;
                }
            }
            this.caiman.setHeldMobId(flag ? target.m_19879_() : -1);
            if (dist > bbWidth && !flag) {
                this.caiman.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                this.caiman.m_21573_().moveTo(target, 1.2F);
            }
        }
    }

    private int getGrabDuration() {
        return this.caiman.m_21824_() && this.caiman.tameAttackFlag ? 300 : 2;
    }
}