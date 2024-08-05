package com.mna.entities.summon;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.ISummonTargetPredicate;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.ai.RetaliateOnAttackGoal;
import com.mna.tools.SummonUtils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class InsectSwarm extends FlyingMob implements ISummonTargetPredicate {

    public InsectSwarm(EntityType<? extends FlyingMob> type, Level world) {
        super(type, world);
        this.f_21342_ = new InsectSwarm.MoveHelperController(this);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide()) {
            if (this.m_5448_() != null && (this.m_5448_().hasEffect(MobEffects.POISON) || !this.m_5448_().isAlive())) {
                this.m_6710_(null);
            }
            LivingEntity owner = SummonUtils.getSummoner(this);
            if (owner == null || !owner.isAlive()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(5, new InsectSwarm.SwarmFlyGoal(this));
        this.f_21346_.addGoal(1, new RetaliateOnAttackGoal(this));
    }

    @Override
    public boolean canBeAffected(MobEffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != MobEffects.POISON && pPotioneffect.getEffect() != MobEffects.WITHER ? super.m_7301_(pPotioneffect) : false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (pEntity instanceof LivingEntity) {
            ((LivingEntity) pEntity).addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1));
        }
        return true;
    }

    public void spawnParticle() {
        if (!ManaAndArtifice.instance.proxy.isGamePaused()) {
            float height = this.m_6095_().getHeight();
            float width = this.m_6095_().getWidth();
            float offsetAvg = (height + width) / 2.0F;
            float halfOffsetAvg = offsetAvg / 2.0F;
            for (int i = 0; i < 2; i++) {
                Vec3 pos = new Vec3(this.m_20185_() - (double) halfOffsetAvg + Math.random() * (double) offsetAvg, this.m_20186_() + Math.random() * (double) offsetAvg, this.m_20189_() - (double) halfOffsetAvg + Math.random() * (double) offsetAvg);
                Vec3 vel = pos.subtract(this.m_20182_()).subtract(0.0, (double) height * Math.random(), 0.0).normalize().scale(-0.05F);
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(69, 66, 30), pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
            }
        }
    }

    @Override
    public boolean shouldSummonTarget(Entity candidate, boolean isFriendly) {
        return !isFriendly && candidate instanceof LivingEntity ? ((LivingEntity) candidate).canBeAffected(new MobEffectInstance(MobEffects.POISON)) && !((LivingEntity) candidate).hasEffect(MobEffects.POISON) : false;
    }

    static class MoveHelperController extends MoveControl {

        private final InsectSwarm parentEntity;

        private int courseChangeCooldown;

        public MoveHelperController(InsectSwarm parent) {
            super(parent);
            this.parentEntity = parent;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO && this.courseChangeCooldown-- <= 0) {
                this.courseChangeCooldown = this.courseChangeCooldown + this.parentEntity.m_217043_().nextInt(5) + 2;
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d0 = vector3d.length();
                vector3d = vector3d.normalize();
                if (this.isCollided(vector3d, Mth.ceil(d0))) {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(0.1)));
                } else {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                }
            }
        }

        private boolean isCollided(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class SwarmFlyGoal extends Goal {

        private final InsectSwarm parentEntity;

        public SwarmFlyGoal(InsectSwarm pixie) {
            this.parentEntity = pixie;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movementcontroller = this.parentEntity.m_21566_();
            if (!movementcontroller.hasWanted()) {
                return true;
            } else if (this.parentEntity.m_5448_() == null) {
                double d0 = movementcontroller.getWantedX() - this.parentEntity.m_20185_();
                double d1 = movementcontroller.getWantedY() - this.parentEntity.m_20186_();
                double d2 = movementcontroller.getWantedZ() - this.parentEntity.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 4.0 || d3 > 3600.0;
            } else {
                double dist = this.parentEntity.m_5448_().m_20280_(this.parentEntity);
                return !this.parentEntity.m_21574_().hasLineOfSight(this.parentEntity.m_5448_()) || dist > 25.0 || dist < 4.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            if (this.parentEntity.m_5448_() == null) {
                boolean setRandom = true;
                if (SummonUtils.isSummon(this.parentEntity)) {
                    LivingEntity summoner = SummonUtils.getSummoner(this.parentEntity);
                    if (summoner != null && summoner.m_20270_(this.parentEntity) > 16.0F) {
                        setRandom = false;
                        this.parentEntity.m_21566_().setWantedPosition(summoner.m_20185_(), summoner.m_20188_(), summoner.m_20189_(), this.parentEntity.m_21133_(Attributes.MOVEMENT_SPEED));
                    }
                }
                if (setRandom) {
                    RandomSource random = this.parentEntity.m_217043_();
                    double d0 = this.parentEntity.m_20185_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    double d1 = this.parentEntity.m_20186_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    double d2 = this.parentEntity.m_20189_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                    int count = 0;
                    int y = (int) d1;
                    boolean ground = false;
                    while (count < 16) {
                        BlockPos bpTestPos = BlockPos.containing(d0, (double) y, d2);
                        if (this.parentEntity.m_9236_().m_46859_(bpTestPos)) {
                            count++;
                            y--;
                        } else {
                            if (!this.parentEntity.m_9236_().m_46801_(bpTestPos)) {
                                ground = true;
                                break;
                            }
                            count++;
                            y++;
                            ground = true;
                        }
                    }
                    if (!ground) {
                        d1 -= 16.0;
                    }
                    this.parentEntity.m_21566_().setWantedPosition(d0, d1, d2, 1.0);
                }
            } else {
                Vec3 target = this.parentEntity.m_5448_().m_146892_();
                this.parentEntity.m_21566_().setWantedPosition(target.x, target.y, target.z, 1.0);
            }
        }
    }
}