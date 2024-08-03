package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class Blaze extends Monster {

    private float allowedHeightOffset = 0.5F;

    private int nextHeightOffsetChangeTick;

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Blaze.class, EntityDataSerializers.BYTE);

    public Blaze(EntityType<? extends Blaze> entityTypeExtendsBlaze0, Level level1) {
        super(entityTypeExtendsBlaze0, level1);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.m_21441_(BlockPathTypes.LAVA, 8.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.f_21364_ = 10;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(4, new Blaze.BlazeAttackGoal(this));
        this.f_21345_.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0));
        this.f_21345_.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.MOVEMENT_SPEED, 0.23F).add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public void aiStep() {
        if (!this.m_20096_() && this.m_20184_().y < 0.0) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.6, 1.0));
        }
        if (this.m_9236_().isClientSide) {
            if (this.f_19796_.nextInt(24) == 0 && !this.m_20067_()) {
                this.m_9236_().playLocalSound(this.m_20185_() + 0.5, this.m_20186_() + 0.5, this.m_20189_() + 0.5, SoundEvents.BLAZE_BURN, this.m_5720_(), 1.0F + this.f_19796_.nextFloat(), this.f_19796_.nextFloat() * 0.7F + 0.3F, false);
            }
            for (int $$0 = 0; $$0 < 2; $$0++) {
                this.m_9236_().addParticle(ParticleTypes.LARGE_SMOKE, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), 0.0, 0.0, 0.0);
            }
        }
        super.aiStep();
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        this.nextHeightOffsetChangeTick--;
        if (this.nextHeightOffsetChangeTick <= 0) {
            this.nextHeightOffsetChangeTick = 100;
            this.allowedHeightOffset = (float) this.f_19796_.triangle(0.5, 6.891);
        }
        LivingEntity $$0 = this.m_5448_();
        if ($$0 != null && $$0.m_20188_() > this.m_20188_() + (double) this.allowedHeightOffset && this.m_6779_($$0)) {
            Vec3 $$1 = this.m_20184_();
            this.m_20256_(this.m_20184_().add(0.0, (0.3F - $$1.y) * 0.3F, 0.0));
            this.f_19812_ = true;
        }
        super.m_8024_();
    }

    @Override
    public boolean isOnFire() {
        return this.isCharged();
    }

    private boolean isCharged() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 1) != 0;
    }

    void setCharged(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean0) {
            $$1 = (byte) ($$1 | 1);
        } else {
            $$1 = (byte) ($$1 & -2);
        }
        this.f_19804_.set(DATA_FLAGS_ID, $$1);
    }

    static class BlazeAttackGoal extends Goal {

        private final Blaze blaze;

        private int attackStep;

        private int attackTime;

        private int lastSeen;

        public BlazeAttackGoal(Blaze blaze0) {
            this.blaze = blaze0;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = this.blaze.m_5448_();
            return $$0 != null && $$0.isAlive() && this.blaze.m_6779_($$0);
        }

        @Override
        public void start() {
            this.attackStep = 0;
        }

        @Override
        public void stop() {
            this.blaze.setCharged(false);
            this.lastSeen = 0;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.attackTime--;
            LivingEntity $$0 = this.blaze.m_5448_();
            if ($$0 != null) {
                boolean $$1 = this.blaze.m_21574_().hasLineOfSight($$0);
                if ($$1) {
                    this.lastSeen = 0;
                } else {
                    this.lastSeen++;
                }
                double $$2 = this.blaze.m_20280_($$0);
                if ($$2 < 4.0) {
                    if (!$$1) {
                        return;
                    }
                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.blaze.m_7327_($$0);
                    }
                    this.blaze.m_21566_().setWantedPosition($$0.m_20185_(), $$0.m_20186_(), $$0.m_20189_(), 1.0);
                } else if ($$2 < this.getFollowDistance() * this.getFollowDistance() && $$1) {
                    double $$3 = $$0.m_20185_() - this.blaze.m_20185_();
                    double $$4 = $$0.m_20227_(0.5) - this.blaze.m_20227_(0.5);
                    double $$5 = $$0.m_20189_() - this.blaze.m_20189_();
                    if (this.attackTime <= 0) {
                        this.attackStep++;
                        if (this.attackStep == 1) {
                            this.attackTime = 60;
                            this.blaze.setCharged(true);
                        } else if (this.attackStep <= 4) {
                            this.attackTime = 6;
                        } else {
                            this.attackTime = 100;
                            this.attackStep = 0;
                            this.blaze.setCharged(false);
                        }
                        if (this.attackStep > 1) {
                            double $$6 = Math.sqrt(Math.sqrt($$2)) * 0.5;
                            if (!this.blaze.m_20067_()) {
                                this.blaze.m_9236_().m_5898_(null, 1018, this.blaze.m_20183_(), 0);
                            }
                            for (int $$7 = 0; $$7 < 1; $$7++) {
                                SmallFireball $$8 = new SmallFireball(this.blaze.m_9236_(), this.blaze, this.blaze.m_217043_().triangle($$3, 2.297 * $$6), $$4, this.blaze.m_217043_().triangle($$5, 2.297 * $$6));
                                $$8.m_6034_($$8.m_20185_(), this.blaze.m_20227_(0.5) + 0.5, $$8.m_20189_());
                                this.blaze.m_9236_().m_7967_($$8);
                            }
                        }
                    }
                    this.blaze.m_21563_().setLookAt($$0, 10.0F, 10.0F);
                } else if (this.lastSeen < 5) {
                    this.blaze.m_21566_().setWantedPosition($$0.m_20185_(), $$0.m_20186_(), $$0.m_20189_(), 1.0);
                }
                super.tick();
            }
        }

        private double getFollowDistance() {
            return this.blaze.m_21133_(Attributes.FOLLOW_RANGE);
        }
    }
}