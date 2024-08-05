package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SonicAttackGoal extends Goal {

    private static final int WAIT = 200;

    private static final int DELAY = 34;

    private final AbstractGolemEntity<?, ?> warden;

    private final int lv;

    private int attackTime = 200;

    private Vec3 targetPos;

    public SonicAttackGoal(AbstractGolemEntity<?, ?> warden, int lv) {
        this.warden = warden;
        this.lv = lv;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.warden.m_5448_();
        return this.targetPos != null && this.attackTime <= 34 ? true : livingentity != null && livingentity.isAlive() && this.warden.canAttack(livingentity) && this.warden.m_20280_(livingentity) < 256.0 && (this.warden.m_21573_().isStuck() || this.warden.m_20280_(livingentity) > 4.0);
    }

    @Override
    public void start() {
        this.attackTime = 200;
        this.targetPos = null;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.attackTime--;
        LivingEntity le = this.warden.m_5448_();
        if (le != null) {
            if (this.attackTime == 34) {
                this.warden.m_5496_(SoundEvents.WARDEN_SONIC_CHARGE, 3.0F, 1.0F);
            }
            if (this.attackTime <= 34) {
                this.targetPos = le.m_146892_();
            }
        }
        if (this.attackTime <= 0 && this.targetPos != null) {
            Vec3 src = this.warden.m_20182_().add(0.0, 1.6F, 0.0);
            Vec3 dst = this.targetPos.subtract(src);
            Vec3 dir = dst.normalize();
            for (int i = 1; i < 17; i++) {
                Vec3 vec33 = src.add(dir.scale((double) i));
                if (this.warden.m_9236_() instanceof ServerLevel level) {
                    level.sendParticles(ParticleTypes.SONIC_BOOM, vec33.x, vec33.y, vec33.z, 1, 0.0, 0.0, 0.0, 0.0);
                }
            }
            this.warden.m_5496_(SoundEvents.WARDEN_SONIC_BOOM, 3.0F, 1.0F);
            AttributeInstance attr = this.warden.m_21051_((Attribute) GolemTypes.GOLEM_SWEEP.get());
            List<LivingEntity> target = new ArrayList();
            if (attr != null && attr.getValue() > 0.0) {
                AABB aabb = new AABB(src, src.add(dir.scale(16.0)));
                for (Entity e : this.warden.m_9236_().m_45933_(this.warden, aabb)) {
                    if (e instanceof LivingEntity) {
                        LivingEntity x = (LivingEntity) e;
                        if (this.warden.canAttack(x)) {
                            target.add(x);
                        }
                    }
                }
            }
            if (le != null && !target.contains(le)) {
                target.add(le);
            }
            for (LivingEntity ex : target) {
                ex.hurt(this.warden.m_9236_().damageSources().sonicBoom(this.warden), (float) (10 * this.lv));
                double d1 = 0.5 * (1.0 - ex.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                double d0 = 2.5 * (1.0 - ex.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                ex.m_5997_(dir.x() * d0, dir.y() * d1, dir.z() * d0);
            }
            this.attackTime = 200;
            this.targetPos = null;
        }
        super.tick();
    }
}