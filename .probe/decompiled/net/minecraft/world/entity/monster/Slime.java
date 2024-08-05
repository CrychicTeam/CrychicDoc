package net.minecraft.world.entity.monster;

import com.google.common.annotations.VisibleForTesting;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.phys.Vec3;

public class Slime extends Mob implements Enemy {

    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);

    public static final int MIN_SIZE = 1;

    public static final int MAX_SIZE = 127;

    public float targetSquish;

    public float squish;

    public float oSquish;

    private boolean wasOnGround;

    public Slime(EntityType<? extends Slime> entityTypeExtendsSlime0, Level level1) {
        super(entityTypeExtendsSlime0, level1);
        this.m_252801_();
        this.f_21342_ = new Slime.SlimeMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new Slime.SlimeFloatGoal(this));
        this.f_21345_.addGoal(2, new Slime.SlimeAttackGoal(this));
        this.f_21345_.addGoal(3, new Slime.SlimeRandomDirectionGoal(this));
        this.f_21345_.addGoal(5, new Slime.SlimeKeepOnJumpingGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, p_289461_ -> Math.abs(p_289461_.m_20186_() - this.m_20186_()) <= 4.0));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ID_SIZE, 1);
    }

    @VisibleForTesting
    public void setSize(int int0, boolean boolean1) {
        int $$2 = Mth.clamp(int0, 1, 127);
        this.f_19804_.set(ID_SIZE, $$2);
        this.m_20090_();
        this.refreshDimensions();
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) ($$2 * $$2));
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue((double) (0.2F + 0.1F * (float) $$2));
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) $$2);
        if (boolean1) {
            this.m_21153_(this.m_21233_());
        }
        this.f_21364_ = $$2;
    }

    public int getSize() {
        return this.f_19804_.get(ID_SIZE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("Size", this.getSize() - 1);
        compoundTag0.putBoolean("wasOnGround", this.wasOnGround);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        this.setSize(compoundTag0.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compoundTag0);
        this.wasOnGround = compoundTag0.getBoolean("wasOnGround");
    }

    public boolean isTiny() {
        return this.getSize() <= 1;
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getSize() > 0;
    }

    @Override
    public void tick() {
        this.squish = this.squish + (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if (this.m_20096_() && !this.wasOnGround) {
            int $$0 = this.getSize();
            for (int $$1 = 0; $$1 < $$0 * 8; $$1++) {
                float $$2 = this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                float $$3 = this.f_19796_.nextFloat() * 0.5F + 0.5F;
                float $$4 = Mth.sin($$2) * (float) $$0 * 0.5F * $$3;
                float $$5 = Mth.cos($$2) * (float) $$0 * 0.5F * $$3;
                this.m_9236_().addParticle(this.getParticleType(), this.m_20185_() + (double) $$4, this.m_20186_(), this.m_20189_() + (double) $$5, 0.0, 0.0, 0.0);
            }
            this.m_5496_(this.getSquishSound(), this.getSoundVolume(), ((this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetSquish = -0.5F;
        } else if (!this.m_20096_() && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }
        this.wasOnGround = this.m_20096_();
        this.decreaseSquish();
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    protected int getJumpDelay() {
        return this.f_19796_.nextInt(20) + 10;
    }

    @Override
    public void refreshDimensions() {
        double $$0 = this.m_20185_();
        double $$1 = this.m_20186_();
        double $$2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_($$0, $$1, $$2);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor0) {
        if (ID_SIZE.equals(entityDataAccessor0)) {
            this.refreshDimensions();
            this.m_146922_(this.f_20885_);
            this.f_20883_ = this.f_20885_;
            if (this.m_20069_() && this.f_19796_.nextInt(20) == 0) {
                this.m_5841_();
            }
        }
        super.m_7350_(entityDataAccessor0);
    }

    @Override
    public EntityType<? extends Slime> getType() {
        return super.m_6095_();
    }

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        int $$1 = this.getSize();
        if (!this.m_9236_().isClientSide && $$1 > 1 && this.m_21224_()) {
            Component $$2 = this.m_7770_();
            boolean $$3 = this.m_21525_();
            float $$4 = (float) $$1 / 4.0F;
            int $$5 = $$1 / 2;
            int $$6 = 2 + this.f_19796_.nextInt(3);
            for (int $$7 = 0; $$7 < $$6; $$7++) {
                float $$8 = ((float) ($$7 % 2) - 0.5F) * $$4;
                float $$9 = ((float) ($$7 / 2) - 0.5F) * $$4;
                Slime $$10 = this.getType().create(this.m_9236_());
                if ($$10 != null) {
                    if (this.m_21532_()) {
                        $$10.m_21530_();
                    }
                    $$10.m_6593_($$2);
                    $$10.m_21557_($$3);
                    $$10.m_20331_(this.m_20147_());
                    $$10.setSize($$5, true);
                    $$10.m_7678_(this.m_20185_() + (double) $$8, this.m_20186_() + 0.5, this.m_20189_() + (double) $$9, this.f_19796_.nextFloat() * 360.0F, 0.0F);
                    this.m_9236_().m_7967_($$10);
                }
            }
        }
        super.m_142687_(entityRemovalReason0);
    }

    @Override
    public void push(Entity entity0) {
        super.m_7334_(entity0);
        if (entity0 instanceof IronGolem && this.isDealsDamage()) {
            this.dealDamage((LivingEntity) entity0);
        }
    }

    @Override
    public void playerTouch(Player player0) {
        if (this.isDealsDamage()) {
            this.dealDamage(player0);
        }
    }

    protected void dealDamage(LivingEntity livingEntity0) {
        if (this.m_6084_()) {
            int $$1 = this.getSize();
            if (this.m_20280_(livingEntity0) < 0.6 * (double) $$1 * 0.6 * (double) $$1 && this.m_142582_(livingEntity0) && livingEntity0.hurt(this.m_269291_().mobAttack(this), this.getAttackDamage())) {
                this.m_5496_(SoundEvents.SLIME_ATTACK, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                this.m_19970_(this, livingEntity0);
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return 0.625F * entityDimensions1.height;
    }

    protected boolean isDealsDamage() {
        return !this.isTiny() && this.m_21515_();
    }

    protected float getAttackDamage() {
        return (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return this.isTiny() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
    }

    public static boolean checkSlimeSpawnRules(EntityType<Slime> entityTypeSlime0, LevelAccessor levelAccessor1, MobSpawnType mobSpawnType2, BlockPos blockPos3, RandomSource randomSource4) {
        if (levelAccessor1.getDifficulty() != Difficulty.PEACEFUL) {
            if (levelAccessor1.m_204166_(blockPos3).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && blockPos3.m_123342_() > 50 && blockPos3.m_123342_() < 70 && randomSource4.nextFloat() < 0.5F && randomSource4.nextFloat() < levelAccessor1.m_46940_() && levelAccessor1.m_46803_(blockPos3) <= randomSource4.nextInt(8)) {
                return m_217057_(entityTypeSlime0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
            }
            if (!(levelAccessor1 instanceof WorldGenLevel)) {
                return false;
            }
            ChunkPos $$5 = new ChunkPos(blockPos3);
            boolean $$6 = WorldgenRandom.seedSlimeChunk($$5.x, $$5.z, ((WorldGenLevel) levelAccessor1).getSeed(), 987234911L).nextInt(10) == 0;
            if (randomSource4.nextInt(10) == 0 && $$6 && blockPos3.m_123342_() < 40) {
                return m_217057_(entityTypeSlime0, levelAccessor1, mobSpawnType2, blockPos3, randomSource4);
            }
        }
        return false;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F * (float) this.getSize();
    }

    @Override
    public int getMaxHeadXRot() {
        return 0;
    }

    protected boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }

    @Override
    protected void jumpFromGround() {
        Vec3 $$0 = this.m_20184_();
        this.m_20334_($$0.x, (double) this.m_6118_(), $$0.z);
        this.f_19812_ = true;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        RandomSource $$5 = serverLevelAccessor0.m_213780_();
        int $$6 = $$5.nextInt(3);
        if ($$6 < 2 && $$5.nextFloat() < 0.5F * difficultyInstance1.getSpecialMultiplier()) {
            $$6++;
        }
        int $$7 = 1 << $$6;
        this.setSize($$7, true);
        return super.finalizeSpawn(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
    }

    float getSoundPitch() {
        float $$0 = this.isTiny() ? 1.4F : 0.8F;
        return ((this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F) * $$0;
    }

    protected SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return super.m_6972_(pose0).scale(0.255F * (float) this.getSize());
    }

    static class SlimeAttackGoal extends Goal {

        private final Slime slime;

        private int growTiredTimer;

        public SlimeAttackGoal(Slime slime0) {
            this.slime = slime0;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity $$0 = this.slime.m_5448_();
            if ($$0 == null) {
                return false;
            } else {
                return !this.slime.m_6779_($$0) ? false : this.slime.m_21566_() instanceof Slime.SlimeMoveControl;
            }
        }

        @Override
        public void start() {
            this.growTiredTimer = m_186073_(300);
            super.start();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity $$0 = this.slime.m_5448_();
            if ($$0 == null) {
                return false;
            } else {
                return !this.slime.m_6779_($$0) ? false : --this.growTiredTimer > 0;
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity $$0 = this.slime.m_5448_();
            if ($$0 != null) {
                this.slime.m_21391_($$0, 10.0F, 10.0F);
            }
            if (this.slime.m_21566_() instanceof Slime.SlimeMoveControl $$1) {
                $$1.setDirection(this.slime.m_146908_(), this.slime.isDealsDamage());
            }
        }
    }

    static class SlimeFloatGoal extends Goal {

        private final Slime slime;

        public SlimeFloatGoal(Slime slime0) {
            this.slime = slime0;
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
            slime0.m_21573_().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return (this.slime.m_20069_() || this.slime.m_20077_()) && this.slime.m_21566_() instanceof Slime.SlimeMoveControl;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.slime.m_217043_().nextFloat() < 0.8F) {
                this.slime.m_21569_().jump();
            }
            if (this.slime.m_21566_() instanceof Slime.SlimeMoveControl $$0) {
                $$0.setWantedMovement(1.2);
            }
        }
    }

    static class SlimeKeepOnJumpingGoal extends Goal {

        private final Slime slime;

        public SlimeKeepOnJumpingGoal(Slime slime0) {
            this.slime = slime0;
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !this.slime.m_20159_();
        }

        @Override
        public void tick() {
            if (this.slime.m_21566_() instanceof Slime.SlimeMoveControl $$0) {
                $$0.setWantedMovement(1.0);
            }
        }
    }

    static class SlimeMoveControl extends MoveControl {

        private float yRot;

        private int jumpDelay;

        private final Slime slime;

        private boolean isAggressive;

        public SlimeMoveControl(Slime slime0) {
            super(slime0);
            this.slime = slime0;
            this.yRot = 180.0F * slime0.m_146908_() / (float) Math.PI;
        }

        public void setDirection(float float0, boolean boolean1) {
            this.yRot = float0;
            this.isAggressive = boolean1;
        }

        public void setWantedMovement(double double0) {
            this.f_24978_ = double0;
            this.f_24981_ = MoveControl.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), this.yRot, 90.0F));
            this.f_24974_.f_20885_ = this.f_24974_.m_146908_();
            this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
            if (this.f_24981_ != MoveControl.Operation.MOVE_TO) {
                this.f_24974_.setZza(0.0F);
            } else {
                this.f_24981_ = MoveControl.Operation.WAIT;
                if (this.f_24974_.m_20096_()) {
                    this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }
                        this.slime.m_21569_().jump();
                        if (this.slime.doPlayJumpSound()) {
                            this.slime.m_5496_(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                        }
                    } else {
                        this.slime.f_20900_ = 0.0F;
                        this.slime.f_20902_ = 0.0F;
                        this.f_24974_.setSpeed(0.0F);
                    }
                } else {
                    this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }

    static class SlimeRandomDirectionGoal extends Goal {

        private final Slime slime;

        private float chosenDegrees;

        private int nextRandomizeTime;

        public SlimeRandomDirectionGoal(Slime slime0) {
            this.slime = slime0;
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return this.slime.m_5448_() == null && (this.slime.m_20096_() || this.slime.m_20069_() || this.slime.m_20077_() || this.slime.m_21023_(MobEffects.LEVITATION)) && this.slime.m_21566_() instanceof Slime.SlimeMoveControl;
        }

        @Override
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.m_183277_(40 + this.slime.m_217043_().nextInt(60));
                this.chosenDegrees = (float) this.slime.m_217043_().nextInt(360);
            }
            if (this.slime.m_21566_() instanceof Slime.SlimeMoveControl $$0) {
                $$0.setDirection(this.chosenDegrees, false);
            }
        }
    }
}