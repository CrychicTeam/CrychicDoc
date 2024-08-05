package com.sihenzhang.crockpot.entity;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class VoltGoat extends Animal implements ChargeableMob, NeutralMob {

    private static final EntityDataAccessor<Integer> DATA_REMAINING_CHARGE_TIME = SynchedEntityData.defineId(VoltGoat.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(VoltGoat.class, EntityDataSerializers.INT);

    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    private static final int PERSISTENT_CHARGE_TIME = 48000;

    @Nullable
    private UUID persistentAngerTarget;

    @Nullable
    private UUID lastLightningBolt;

    public VoltGoat(EntityType<? extends VoltGoat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_21441_(BlockPathTypes.POWDER_SNOW, -1.0F);
        this.m_21441_(BlockPathTypes.DANGER_POWDER_SNOW, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new VoltGoat.VoltGoatChargeGoal());
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new VoltGoat.VoltGoatPanicGoal(1.5));
        this.f_21345_.addGoal(3, new MeleeAttackGoal(this, 1.5, true));
        this.f_21345_.addGoal(4, new AvoidEntityGoal(this, Player.class, 16.0F, 1.4, 1.5));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.25));
        this.f_21345_.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new VoltGoat.ChargedVoltGoatAttackablePlayerGoal(this, true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Player.class, true, this::m_21674_));
        this.f_21346_.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_REMAINING_CHARGE_TIME, 0);
        this.f_19804_.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    public int getRemainingPersistentChargeTime() {
        return this.f_19804_.get(DATA_REMAINING_CHARGE_TIME);
    }

    @Override
    public void setRemainingPersistentChargeTime(int pRemainingPersistentChargeTime) {
        this.f_19804_.set(DATA_REMAINING_CHARGE_TIME, pRemainingPersistentChargeTime);
    }

    @Override
    public void startPersistentChargeTimer() {
        this.setRemainingPersistentChargeTime(48000);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.f_19804_.get(DATA_REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.f_19804_.set(DATA_REMAINING_ANGER_TIME, pRemainingPersistentAngerTime);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.f_19796_));
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return super.m_5639_(pFallDistance, pDamageMultiplier) - 10;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GOAT_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GOAT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GOAT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        this.m_5496_(SoundEvents.GOAT_STEP, 0.15F, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentChargeSaveData(pCompound);
        this.m_21678_(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readPersistentChargeSaveData(pCompound);
        this.m_147285_(this.m_9236_(), pCompound);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return CrockPotEntities.VOLT_GOAT.get().create(pLevel);
    }

    @Override
    protected void customServerAiStep() {
        this.updatePersistentCharge();
        this.m_21666_((ServerLevel) this.m_9236_(), true);
        super.customServerAiStep();
    }

    @Override
    public int getMaxHeadYRot() {
        return 15;
    }

    @Override
    public void setYHeadRot(float pRotation) {
        int maxHeadYRot = this.getMaxHeadYRot();
        float f = Mth.degreesDifference(this.f_20883_, pRotation);
        float f1 = Mth.clamp(f, (float) (-maxHeadYRot), (float) maxHeadYRot);
        super.m_5616_(this.f_20883_ + f1);
    }

    @Override
    public SoundEvent getEatingSound(ItemStack pStack) {
        return SoundEvents.GOAT_EAT;
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stackInHand = pPlayer.m_21120_(pHand);
        if (stackInHand.is(Items.BUCKET) && !this.m_6162_()) {
            pPlayer.playSound(SoundEvents.GOAT_MILK, 1.0F, 1.0F);
            ItemStack milkBucket = ItemUtils.createFilledResult(stackInHand, pPlayer, Items.MILK_BUCKET.getDefaultInstance());
            pPlayer.m_21008_(pHand, milkBucket);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            InteractionResult interactionResult = super.mobInteract(pPlayer, pHand);
            if (interactionResult.consumesAction() && this.m_6898_(stackInHand)) {
                this.m_9236_().playSound(null, this, this.getEatingSound(stackInHand), SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(this.m_9236_().random, 0.8F, 1.2F));
            }
            return interactionResult;
        }
    }

    public void setLastLightningBolt(UUID lastLightningBolt) {
        this.lastLightningBolt = lastLightningBolt;
    }

    @Override
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
        UUID uuid = pLightning.m_20148_();
        if (!uuid.equals(this.lastLightningBolt)) {
            this.startPersistentChargeTimer();
            this.setLastLightningBolt(uuid);
        }
    }

    public static boolean checkVoltGoatSpawnRules(EntityType<? extends Animal> pVoltGoat, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.m_8055_(pPos.below()).m_204336_(CrockPotBlockTags.VOLT_GOATS_SPAWNABLE_ON) && m_186209_(pLevel, pPos);
    }

    static class ChargedVoltGoatAttackablePlayerGoal extends NearestAttackableTargetGoal<Player> {

        private int timestamp;

        public ChargedVoltGoatAttackablePlayerGoal(VoltGoat pMob, boolean pMustSee) {
            super(pMob, Player.class, pMustSee);
        }

        @Override
        public boolean canUse() {
            return this.voltGoatCanTarget() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.voltGoatCanTarget() && super.m_8045_();
        }

        @Override
        public void start() {
            this.timestamp = this.f_26135_.f_19797_;
            super.start();
        }

        @Override
        public void stop() {
            if (this.f_26135_.m_21213_() < this.timestamp) {
                VoltGoat voltGoat = (VoltGoat) this.f_26135_;
                voltGoat.m_21662_();
            }
            super.m_8041_();
        }

        private boolean voltGoatCanTarget() {
            VoltGoat voltGoat = (VoltGoat) this.f_26135_;
            return voltGoat.m_7090_();
        }
    }

    class VoltGoatChargeGoal extends Goal {

        @Override
        public boolean canUse() {
            return VoltGoat.this.m_7090_();
        }

        @Override
        public void start() {
            VoltGoat.this.m_7292_(new MobEffectInstance(CrockPotEffects.CHARGE.get(), -1, 0, false, false));
        }

        @Override
        public void stop() {
            VoltGoat.this.m_21195_(CrockPotEffects.CHARGE.get());
        }
    }

    class VoltGoatPanicGoal extends PanicGoal {

        public VoltGoatPanicGoal(double pSpeedModifier) {
            super(VoltGoat.this, pSpeedModifier);
        }

        @Override
        protected boolean shouldPanic() {
            return this.f_25684_.m_203117_() || this.f_25684_.m_6060_();
        }
    }
}