package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EntityBlobfish extends WaterAnimal implements FlyingAnimal, Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityBlobfish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> BLOBFISH_SCALE = SynchedEntityData.defineId(EntityBlobfish.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> DEPRESSURIZED = SynchedEntityData.defineId(EntityBlobfish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLIMED = SynchedEntityData.defineId(EntityBlobfish.class, EntityDataSerializers.BOOLEAN);

    public float squishFactor;

    public float prevSquishFactor;

    public float squishAmount;

    private boolean wasOnGround;

    protected EntityBlobfish(EntityType type, Level world) {
        super(type, world);
        this.f_21342_ = new AquaticMoveController(this, 1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 3.0);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.blobfishSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    protected void handleAirSupply(int p_209207_1_) {
        if (this.m_6084_() && !this.m_20072_() && !this.isSlimed()) {
            this.m_20301_(p_209207_1_ - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().drown(), this.f_19796_.nextInt(2) == 0 ? 1.0F : 0.0F);
            }
        } else {
            this.m_20301_(2000);
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose p_213348_1_, EntityDimensions p_213348_2_) {
        return p_213348_2_.height * 0.65F;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.fromBucket() || this.isSlimed();
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(BLOBFISH_SCALE, 1.0F);
        this.f_19804_.define(DEPRESSURIZED, false);
        this.f_19804_.define(SLIMED, false);
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return super.m_6972_(poseIn).scale(this.getBlobfishScale());
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean p_203706_1_) {
        this.f_19804_.set(FROM_BUCKET, p_203706_1_);
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("Depressurized", this.isDepressurized());
        compound.putBoolean("Slimed", this.isSlimed());
        compound.putFloat("BlobfishScale", this.getBlobfishScale());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setDepressurized(compound.getBoolean("Depressurized"));
        this.setSlimed(compound.getBoolean("Slimed"));
        this.setBlobfishScale(compound.getFloat("BlobfishScale"));
    }

    private boolean hasClearance() {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (int l1 = 0; l1 < 10; l1++) {
            BlockState blockstate = this.m_9236_().getBlockState(blockpos$mutable.set(this.m_20185_(), this.m_20186_() + (double) l1, this.m_20189_()));
            if (!blockstate.m_60819_().is(FluidTags.WATER) && !blockstate.m_280296_()) {
                return false;
            }
        }
        return true;
    }

    public float getBlobfishScale() {
        return this.f_19804_.get(BLOBFISH_SCALE);
    }

    public void setBlobfishScale(float scale) {
        this.f_19804_.set(BLOBFISH_SCALE, scale);
    }

    public boolean isDepressurized() {
        return this.f_19804_.get(DEPRESSURIZED);
    }

    public void setDepressurized(boolean depressurized) {
        this.f_19804_.set(DEPRESSURIZED, depressurized);
    }

    public boolean isSlimed() {
        return this.f_19804_.get(SLIMED);
    }

    public void setSlimed(boolean slimed) {
        this.f_19804_.set(SLIMED, slimed);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(3, new AnimalAISwimBottom(this, 1.0, 7));
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack lvt_3_1_ = player.m_21120_(hand);
        if (lvt_3_1_.getItem() == Items.SLIME_BALL && this.m_6084_() && !this.isSlimed()) {
            this.setSlimed(true);
            for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, lvt_3_1_), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
            lvt_3_1_.shrink(1);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos p_180429_1_, BlockState p_180429_2_) {
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.BLOBFISH_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.putFloat("BucketScale", this.getBlobfishScale());
        compound.putBoolean("Slimed", this.isSlimed());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("BucketScale")) {
            this.setBlobfishScale(compound.getFloat("BucketScale"));
        }
        if (compound.contains("Slimed")) {
            this.setSlimed(compound.getBoolean("Slimed"));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setBlobfishScale(0.75F + this.f_19796_.nextFloat() * 0.5F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSquishFactor = this.squishFactor;
        this.squishFactor = this.squishFactor + (this.squishAmount - this.squishFactor) * 0.5F;
        float f2 = -((float) this.m_20184_().y * 2.2F * (180.0F / (float) Math.PI));
        this.m_146926_(f2);
        if (!this.m_20069_()) {
            if (this.m_20096_()) {
                if (!this.wasOnGround) {
                    this.squishAmount = -0.35F;
                }
            } else if (this.wasOnGround) {
                this.squishAmount = 2.0F;
            }
        }
        this.wasOnGround = this.m_20096_();
        this.alterSquishAmount();
        boolean clear = this.hasClearance();
        if (clear) {
            if (this.isDepressurized()) {
                this.setDepressurized(false);
            }
        } else if (!this.isDepressurized()) {
            this.setDepressurized(true);
        }
    }

    protected void alterSquishAmount() {
        this.squishAmount *= 0.6F;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    public static boolean canBlobfishSpawn(EntityType<EntityBlobfish> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || pos.m_123342_() <= AMConfig.blobfishSpawnHeight && iServerWorld.m_6425_(pos).is(FluidTags.WATER) && iServerWorld.m_6425_(pos.above()).is(FluidTags.WATER);
    }

    @Override
    public boolean isFlying() {
        return false;
    }
}