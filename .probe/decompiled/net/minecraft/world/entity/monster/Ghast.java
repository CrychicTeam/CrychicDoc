package net.minecraft.world.entity.monster;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Ghast extends FlyingMob implements Enemy {

    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);

    private int explosionPower = 1;

    public Ghast(EntityType<? extends Ghast> entityTypeExtendsGhast0, Level level1) {
        super(entityTypeExtendsGhast0, level1);
        this.f_21364_ = 5;
        this.f_21342_ = new Ghast.GhastMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(5, new Ghast.RandomFloatAroundGoal(this));
        this.f_21345_.addGoal(7, new Ghast.GhastLookGoal(this));
        this.f_21345_.addGoal(7, new Ghast.GhastShootFireballGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, p_289460_ -> Math.abs(p_289460_.m_20186_() - this.m_20186_()) <= 4.0));
    }

    public boolean isCharging() {
        return this.f_19804_.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean boolean0) {
        this.f_19804_.set(DATA_IS_CHARGING, boolean0);
    }

    public int getExplosionPower() {
        return this.explosionPower;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    private static boolean isReflectedFireball(DamageSource damageSource0) {
        return damageSource0.getDirectEntity() instanceof LargeFireball && damageSource0.getEntity() instanceof Player;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        return !isReflectedFireball(damageSource0) && super.m_6673_(damageSource0);
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (isReflectedFireball(damageSource0)) {
            super.m_6469_(damageSource0, 1000.0F);
            return true;
        } else {
            return this.isInvulnerableTo(damageSource0) ? false : super.m_6469_(damageSource0, float1);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_IS_CHARGING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 100.0);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    public static boolean checkGhastSpawnRules(EntityType<Ghast> entityTypeGhast0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        return levelAccessor1.getDifficulty() != Difficulty.PEACEFUL && randomSource4.nextInt(20) == 0 && m_217057_(entityTypeGhast0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7380_(compoundTag0);
        compoundTag0.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.m_7378_(compoundTag0);
        if (compoundTag0.contains("ExplosionPower", 99)) {
            this.explosionPower = compoundTag0.getByte("ExplosionPower");
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 2.6F;
    }

    static class GhastLookGoal extends Goal {

        private final Ghast ghast;

        public GhastLookGoal(Ghast ghast0) {
            this.ghast = ghast0;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.ghast.m_5448_() == null) {
                Vec3 $$0 = this.ghast.m_20184_();
                this.ghast.m_146922_(-((float) Mth.atan2($$0.x, $$0.z)) * (180.0F / (float) Math.PI));
                this.ghast.f_20883_ = this.ghast.m_146908_();
            } else {
                LivingEntity $$1 = this.ghast.m_5448_();
                double $$2 = 64.0;
                if ($$1.m_20280_(this.ghast) < 4096.0) {
                    double $$3 = $$1.m_20185_() - this.ghast.m_20185_();
                    double $$4 = $$1.m_20189_() - this.ghast.m_20189_();
                    this.ghast.m_146922_(-((float) Mth.atan2($$3, $$4)) * (180.0F / (float) Math.PI));
                    this.ghast.f_20883_ = this.ghast.m_146908_();
                }
            }
        }
    }

    static class GhastMoveControl extends MoveControl {

        private final Ghast ghast;

        private int floatDuration;

        public GhastMoveControl(Ghast ghast0) {
            super(ghast0);
            this.ghast = ghast0;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration = this.floatDuration + this.ghast.m_217043_().nextInt(5) + 2;
                    Vec3 $$0 = new Vec3(this.f_24975_ - this.ghast.m_20185_(), this.f_24976_ - this.ghast.m_20186_(), this.f_24977_ - this.ghast.m_20189_());
                    double $$1 = $$0.length();
                    $$0 = $$0.normalize();
                    if (this.canReach($$0, Mth.ceil($$1))) {
                        this.ghast.m_20256_(this.ghast.m_20184_().add($$0.scale(0.1)));
                    } else {
                        this.f_24981_ = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 vec0, int int1) {
            AABB $$2 = this.ghast.m_20191_();
            for (int $$3 = 1; $$3 < int1; $$3++) {
                $$2 = $$2.move(vec0);
                if (!this.ghast.m_9236_().m_45756_(this.ghast, $$2)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class GhastShootFireballGoal extends Goal {

        private final Ghast ghast;

        public int chargeTime;

        public GhastShootFireballGoal(Ghast ghast0) {
            this.ghast = ghast0;
        }

        @Override
        public boolean canUse() {
            return this.ghast.m_5448_() != null;
        }

        @Override
        public void start() {
            this.chargeTime = 0;
        }

        @Override
        public void stop() {
            this.ghast.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.ghast.m_5448_();
            if ($$0 != null) {
                double $$1 = 64.0;
                if ($$0.m_20280_(this.ghast) < 4096.0 && this.ghast.m_142582_($$0)) {
                    Level $$2 = this.ghast.m_9236_();
                    this.chargeTime++;
                    if (this.chargeTime == 10 && !this.ghast.m_20067_()) {
                        $$2.m_5898_(null, 1015, this.ghast.m_20183_(), 0);
                    }
                    if (this.chargeTime == 20) {
                        double $$3 = 4.0;
                        Vec3 $$4 = this.ghast.m_20252_(1.0F);
                        double $$5 = $$0.m_20185_() - (this.ghast.m_20185_() + $$4.x * 4.0);
                        double $$6 = $$0.m_20227_(0.5) - (0.5 + this.ghast.m_20227_(0.5));
                        double $$7 = $$0.m_20189_() - (this.ghast.m_20189_() + $$4.z * 4.0);
                        if (!this.ghast.m_20067_()) {
                            $$2.m_5898_(null, 1016, this.ghast.m_20183_(), 0);
                        }
                        LargeFireball $$8 = new LargeFireball($$2, this.ghast, $$5, $$6, $$7, this.ghast.getExplosionPower());
                        $$8.m_6034_(this.ghast.m_20185_() + $$4.x * 4.0, this.ghast.m_20227_(0.5) + 0.5, $$8.m_20189_() + $$4.z * 4.0);
                        $$2.m_7967_($$8);
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    this.chargeTime--;
                }
                this.ghast.setCharging(this.chargeTime > 10);
            }
        }
    }

    static class RandomFloatAroundGoal extends Goal {

        private final Ghast ghast;

        public RandomFloatAroundGoal(Ghast ghast0) {
            this.ghast = ghast0;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl $$0 = this.ghast.m_21566_();
            if (!$$0.hasWanted()) {
                return true;
            } else {
                double $$1 = $$0.getWantedX() - this.ghast.m_20185_();
                double $$2 = $$0.getWantedY() - this.ghast.m_20186_();
                double $$3 = $$0.getWantedZ() - this.ghast.m_20189_();
                double $$4 = $$1 * $$1 + $$2 * $$2 + $$3 * $$3;
                return $$4 < 1.0 || $$4 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource $$0 = this.ghast.m_217043_();
            double $$1 = this.ghast.m_20185_() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double $$2 = this.ghast.m_20186_() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double $$3 = this.ghast.m_20189_() + (double) (($$0.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghast.m_21566_().setWantedPosition($$1, $$2, $$3, 1.0);
        }
    }
}