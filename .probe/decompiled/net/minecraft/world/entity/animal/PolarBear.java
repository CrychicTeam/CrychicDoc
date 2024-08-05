package net.minecraft.world.entity.animal;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class PolarBear extends Animal implements NeutralMob {

    private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);

    private static final float STAND_ANIMATION_TICKS = 6.0F;

    private float clientSideStandAnimationO;

    private float clientSideStandAnimation;

    private int warningSoundTicks;

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private int remainingPersistentAngerTime;

    @Nullable
    private UUID persistentAngerTarget;

    public PolarBear(EntityType<? extends PolarBear> entityTypeExtendsPolarBear0, Level level1) {
        super(entityTypeExtendsPolarBear0, level1);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.POLAR_BEAR.create(serverLevel0);
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PolarBear.PolarBearMeleeAttackGoal());
        this.f_21345_.addGoal(1, new PolarBear.PolarBearPanicGoal());
        this.f_21345_.addGoal(4, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new PolarBear.PolarBearHurtByTargetGoal());
        this.f_21346_.addGoal(2, new PolarBear.PolarBearAttackPlayersGoal());
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::m_21674_));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Fox.class, 10, true, true, null));
        this.f_21346_.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.FOLLOW_RANGE, 20.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> entityTypePolarBear0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        Holder<Biome> $$5 = levelAccessor1.m_204166_(blockPos3);
        return !$$5.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS) ? m_218104_(entityTypePolarBear0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4) : m_186209_(levelAccessor1, blockPos3) && levelAccessor1.m_8055_(blockPos3.below()).m_204336_(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.m_147285_(this.m_9236_(), compoundTag0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        this.m_21678_(compoundTag0);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    public void setRemainingPersistentAngerTime(int int0) {
        this.remainingPersistentAngerTime = int0;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID0) {
        this.persistentAngerTarget = uUID0;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_6162_() ? SoundEvents.POLAR_BEAR_AMBIENT_BABY : SoundEvents.POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.POLAR_BEAR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        this.m_5496_(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.m_5496_(SoundEvents.POLAR_BEAR_WARNING, 1.0F, this.m_6100_());
            this.warningSoundTicks = 40;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_STANDING_ID, false);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.m_9236_().isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.m_6210_();
            }
            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }
        if (this.warningSoundTicks > 0) {
            this.warningSoundTicks--;
        }
        if (!this.m_9236_().isClientSide) {
            this.m_21666_((ServerLevel) this.m_9236_(), true);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        if (this.clientSideStandAnimation > 0.0F) {
            float $$1 = this.clientSideStandAnimation / 6.0F;
            float $$2 = 1.0F + $$1;
            return super.m_6972_(pose0).scale(1.0F, $$2);
        } else {
            return super.m_6972_(pose0);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        boolean $$1 = entity0.hurt(this.m_269291_().mobAttack(this), (float) ((int) this.m_21133_(Attributes.ATTACK_DAMAGE)));
        if ($$1) {
            this.m_19970_(this, entity0);
        }
        return $$1;
    }

    public boolean isStanding() {
        return this.f_19804_.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean boolean0) {
        this.f_19804_.set(DATA_STANDING_ID, boolean0);
    }

    public float getStandingAnimationScale(float float0) {
        return Mth.lerp(float0, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        if (spawnGroupData3 == null) {
            spawnGroupData3 = new AgeableMob.AgeableMobGroupData(1.0F);
        }
        return super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    class PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {

        public PolarBearAttackPlayersGoal() {
            super(PolarBear.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (PolarBear.this.m_6162_()) {
                return false;
            } else {
                if (super.canUse()) {
                    for (PolarBear $$1 : PolarBear.this.m_9236_().m_45976_(PolarBear.class, PolarBear.this.m_20191_().inflate(8.0, 4.0, 8.0))) {
                        if ($$1.m_6162_()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        @Override
        protected double getFollowDistance() {
            return super.m_7623_() * 0.5;
        }
    }

    class PolarBearHurtByTargetGoal extends HurtByTargetGoal {

        public PolarBearHurtByTargetGoal() {
            super(PolarBear.this);
        }

        @Override
        public void start() {
            super.start();
            if (PolarBear.this.m_6162_()) {
                this.m_26047_();
                this.m_8041_();
            }
        }

        @Override
        protected void alertOther(Mob mob0, LivingEntity livingEntity1) {
            if (mob0 instanceof PolarBear && !mob0.m_6162_()) {
                super.alertOther(mob0, livingEntity1);
            }
        }
    }

    class PolarBearMeleeAttackGoal extends MeleeAttackGoal {

        public PolarBearMeleeAttackGoal() {
            super(PolarBear.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity livingEntity0, double double1) {
            double $$2 = this.getAttackReachSqr(livingEntity0);
            if (double1 <= $$2 && this.m_25564_()) {
                this.m_25563_();
                this.f_25540_.m_7327_(livingEntity0);
                PolarBear.this.setStanding(false);
            } else if (double1 <= $$2 * 2.0) {
                if (this.m_25564_()) {
                    PolarBear.this.setStanding(false);
                    this.m_25563_();
                }
                if (this.m_25565_() <= 10) {
                    PolarBear.this.setStanding(true);
                    PolarBear.this.playWarningSound();
                }
            } else {
                this.m_25563_();
                PolarBear.this.setStanding(false);
            }
        }

        @Override
        public void stop() {
            PolarBear.this.setStanding(false);
            super.stop();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity livingEntity0) {
            return (double) (4.0F + livingEntity0.m_20205_());
        }
    }

    class PolarBearPanicGoal extends PanicGoal {

        public PolarBearPanicGoal() {
            super(PolarBear.this, 2.0);
        }

        @Override
        protected boolean shouldPanic() {
            return this.f_25684_.m_21188_() != null && this.f_25684_.m_6162_() || this.f_25684_.m_6060_();
        }
    }
}