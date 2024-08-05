package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.ai.SemiAquaticPathNavigator;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class TrilocarisEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TrilocarisEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(TrilocarisEntity.class, EntityDataSerializers.INT);

    private float groundProgress;

    private float prevGroundProgress;

    private float biteProgress;

    private float prevBiteProgress;

    private int timeSwimming = 0;

    public boolean crawling;

    private int lastStepSoundTimestamp = -1;

    public TrilocarisEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new SmoothSwimmingMoveControl(this, 85, 10, 1.0F, 0.65F, false);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new TrilocarisEntity.MeleeGoal());
        this.f_21345_.addGoal(1, new TrilocarisEntity.WanderGoal());
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    private static boolean isInCave(ServerLevelAccessor iServerWorld, BlockPos pos) {
        while (iServerWorld.m_6425_(pos).is(FluidTags.WATER)) {
            pos = pos.above();
        }
        return !iServerWorld.m_45527_(pos) && pos.m_123342_() < iServerWorld.m_5736_();
    }

    public static boolean checkTrilocarisSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        FluidState fluidState = level.m_6425_(pos);
        return fluidState.is(FluidTags.WATER) && fluidState.getAmount() >= 8 && isInCave(level, pos);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SemiAquaticPathNavigator(this, worldIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_6425_(pos.below()).isEmpty() && worldIn.m_6425_(pos).is(FluidTags.WATER) ? 10.0F : super.m_5610_(pos, worldIn);
    }

    @Override
    protected void handleAirSupply(int air) {
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(ATTACK_TICK, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevGroundProgress = this.groundProgress;
        this.prevBiteProgress = this.biteProgress;
        if (this.m_20096_() && this.groundProgress < 5.0F) {
            this.groundProgress++;
        }
        if (!this.m_20096_() && this.groundProgress > 0.0F) {
            this.groundProgress--;
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.biteProgress < 5.0F) {
                this.biteProgress++;
            }
        } else {
            if (this.biteProgress > 4.0F && this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < 1.3) {
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
            }
            if (this.biteProgress > 0.0F) {
                this.biteProgress--;
            }
        }
        if (!this.m_9236_().isClientSide) {
            float delta = (float) this.m_20184_().horizontalDistance();
            if (!this.crawling && this.m_20072_()) {
                this.timeSwimming++;
            } else {
                this.timeSwimming = 0;
                if (delta > 0.01F && this.f_19797_ - this.lastStepSoundTimestamp > 10) {
                    this.lastStepSoundTimestamp = this.f_19797_;
                    this.m_5496_(ACSoundRegistry.TRILOCARIS_STEP.get(), 0.2F, 1.0F);
                }
            }
        }
    }

    public float getGroundProgress(float partialTick) {
        return (this.prevGroundProgress + (this.groundProgress - this.prevGroundProgress) * partialTick) * 0.2F;
    }

    public float getBiteProgress(float partialTick) {
        return (this.prevBiteProgress + (this.biteProgress - this.prevBiteProgress) * partialTick) * 0.2F;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            boolean pulldown = false;
            if (this.crawling) {
                delta = delta.scale(0.8);
                if (!this.f_20899_ && !this.f_19862_) {
                    delta = delta.add(0.0, -0.05F, 0.0);
                } else {
                    delta = delta.add(0.0, 0.1F, 0.0);
                }
            }
            this.m_20256_(delta.scale(0.8));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(ACItemRegistry.TRILOCARIS_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean bucket) {
        this.f_19804_.set(FROM_BUCKET, bucket);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.f_19804_.set(ATTACK_TICK, 5);
        return super.m_7327_(entityIn);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float speedMod = !this.m_20096_() ? 4.0F : 16.0F;
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * speedMod, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.TRILOCARIS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.TRILOCARIS_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private class MeleeGoal extends Goal {

        private int duration = 0;

        private int cooldown = 0;

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return TrilocarisEntity.this.m_5448_() != null && TrilocarisEntity.this.m_5448_().isAlive() && this.duration < 300;
        }

        @Override
        public void tick() {
            this.duration++;
            LivingEntity target = TrilocarisEntity.this.m_5448_();
            if (target != null && target.isAlive()) {
                if (target.m_20072_()) {
                    TrilocarisEntity.this.m_21573_().moveTo(target, 1.0);
                }
                if (TrilocarisEntity.this.m_20270_(target) < 1.2F && this.cooldown == 0) {
                    TrilocarisEntity.this.doHurtTarget(target);
                    this.cooldown = 30;
                }
            }
            if (this.cooldown > 0) {
                this.cooldown--;
            }
        }

        @Override
        public void stop() {
            this.duration = 0;
            this.cooldown = 0;
            TrilocarisEntity.this.m_6703_(null);
            TrilocarisEntity.this.m_6710_(null);
        }
    }

    private class WanderGoal extends Goal {

        private double x;

        private double y;

        private double z;

        private boolean isCrawling;

        public WanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (TrilocarisEntity.this.m_217043_().nextInt(20) != 0 && TrilocarisEntity.this.crawling) {
                return false;
            } else {
                if (TrilocarisEntity.this.crawling) {
                    this.isCrawling = TrilocarisEntity.this.m_217043_().nextFloat() < 0.5F;
                } else {
                    this.isCrawling = TrilocarisEntity.this.timeSwimming > 300 || TrilocarisEntity.this.m_217043_().nextFloat() < 0.15F;
                }
                Vec3 target = this.getPosition();
                if (target == null) {
                    return false;
                } else {
                    this.x = target.x;
                    this.y = target.y;
                    this.z = target.z;
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            double dist = TrilocarisEntity.this.m_20275_(this.x, this.y, this.z);
            return dist > 4.0;
        }

        @Override
        public void tick() {
            TrilocarisEntity.this.crawling = this.isCrawling;
            TrilocarisEntity.this.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
        }

        public BlockPos findWaterBlock() {
            BlockPos result = null;
            RandomSource random = TrilocarisEntity.this.m_217043_();
            int range = 10;
            for (int i = 0; i < 15; i++) {
                BlockPos blockPos = TrilocarisEntity.this.m_20183_().offset(random.nextInt(range) - range / 2, random.nextInt(range) - range / 2, random.nextInt(range) - range / 2);
                if (TrilocarisEntity.this.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && blockPos.m_123342_() > TrilocarisEntity.this.m_9236_().m_141937_() + 1) {
                    result = blockPos;
                }
            }
            return result;
        }

        @Nullable
        protected Vec3 getPosition() {
            BlockPos water = this.findWaterBlock();
            if (TrilocarisEntity.this.m_20072_()) {
                if (water == null) {
                    return null;
                } else {
                    if (this.isCrawling) {
                        while (TrilocarisEntity.this.m_9236_().getFluidState(water.below()).is(FluidTags.WATER) && water.m_123342_() > TrilocarisEntity.this.m_9236_().m_141937_() + 1) {
                            water = water.below();
                        }
                        water = water.above();
                    }
                    return Vec3.atCenterOf(water);
                }
            } else {
                return water == null ? DefaultRandomPos.getPos(TrilocarisEntity.this, 7, 3) : Vec3.atCenterOf(water);
            }
        }
    }
}