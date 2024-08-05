package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class Spider extends Monster {

    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE);

    private static final float SPIDER_SPECIAL_EFFECT_CHANCE = 0.1F;

    public Spider(EntityType<? extends Spider> entityTypeExtendsSpider0, Level level1) {
        super(entityTypeExtendsSpider0, level1);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.f_21345_.addGoal(4, new Spider.SpiderAttackGoal(this));
        this.f_21345_.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(6, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new Spider.SpiderTargetGoal(this, Player.class));
        this.f_21346_.addGoal(3, new Spider.SpiderTargetGoal(this, IronGolem.class));
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) (this.m_20206_() * 0.5F);
    }

    @Override
    protected PathNavigation createNavigation(Level level0) {
        return new WallClimberNavigation(this, level0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (!this.m_9236_().isClientSide) {
            this.setClimbing(this.f_19862_);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.SPIDER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void makeStuckInBlock(BlockState blockState0, Vec3 vec1) {
        if (!blockState0.m_60713_(Blocks.COBWEB)) {
            super.m_7601_(blockState0, vec1);
        }
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance mobEffectInstance0) {
        return mobEffectInstance0.getEffect() == MobEffects.POISON ? false : super.m_7301_(mobEffectInstance0);
    }

    public boolean isClimbing() {
        return (this.f_19804_.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean boolean0) {
        byte $$1 = this.f_19804_.get(DATA_FLAGS_ID);
        if (boolean0) {
            $$1 = (byte) ($$1 | 1);
        } else {
            $$1 = (byte) ($$1 & -2);
        }
        this.f_19804_.set(DATA_FLAGS_ID, $$1);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        spawnGroupData3 = super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        if ($$5.nextInt(100) == 0) {
            Skeleton $$6 = EntityType.SKELETON.create(this.m_9236_());
            if ($$6 != null) {
                $$6.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), 0.0F);
                $$6.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, null, null);
                $$6.m_20329_(this);
            }
        }
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new Spider.SpiderEffectsGroupData();
            if (serverLevelAccessor0.m_46791_() == Difficulty.HARD && $$5.nextFloat() < 0.1F * difficultyInstance1.getSpecialMultiplier()) {
                ((Spider.SpiderEffectsGroupData) spawnGroupData3).setRandomEffect($$5);
            }
        }
        if (spawnGroupData3 instanceof Spider.SpiderEffectsGroupData $$7) {
            MobEffect $$8 = $$7.effect;
            if ($$8 != null) {
                this.m_7292_(new MobEffectInstance($$8, -1));
            }
        }
        return spawnGroupData3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.65F;
    }

    static class SpiderAttackGoal extends MeleeAttackGoal {

        public SpiderAttackGoal(Spider spider0) {
            super(spider0, 1.0, true);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.f_25540_.m_20160_();
        }

        @Override
        public boolean canContinueToUse() {
            float $$0 = this.f_25540_.m_213856_();
            if ($$0 >= 0.5F && this.f_25540_.m_217043_().nextInt(100) == 0) {
                this.f_25540_.m_6710_(null);
                return false;
            } else {
                return super.canContinueToUse();
            }
        }

        @Override
        protected double getAttackReachSqr(LivingEntity livingEntity0) {
            return (double) (4.0F + livingEntity0.m_20205_());
        }
    }

    public static class SpiderEffectsGroupData implements SpawnGroupData {

        @Nullable
        public MobEffect effect;

        public void setRandomEffect(RandomSource randomSource0) {
            int $$1 = randomSource0.nextInt(5);
            if ($$1 <= 1) {
                this.effect = MobEffects.MOVEMENT_SPEED;
            } else if ($$1 <= 2) {
                this.effect = MobEffects.DAMAGE_BOOST;
            } else if ($$1 <= 3) {
                this.effect = MobEffects.REGENERATION;
            } else if ($$1 <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }
        }
    }

    static class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

        public SpiderTargetGoal(Spider spider0, Class<T> classT1) {
            super(spider0, classT1, true);
        }

        @Override
        public boolean canUse() {
            float $$0 = this.f_26135_.m_213856_();
            return $$0 >= 0.5F ? false : super.canUse();
        }
    }
}