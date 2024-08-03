package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCombJelly extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityCombJelly.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> JELLYPITCH = SynchedEntityData.defineId(EntityCombJelly.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityCombJelly.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> JELLY_SCALE = SynchedEntityData.defineId(EntityCombJelly.class, EntityDataSerializers.FLOAT);

    public float prevOnLandProgress;

    public float onLandProgress;

    private BlockPos moveTarget;

    public float prevjellyPitch = 0.0F;

    public float spin;

    public float prevSpin;

    protected EntityCombJelly(EntityType<? extends WaterAnimal> animal, Level level) {
        super(animal, level);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.terrapinSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canCombJellySpawn(EntityType<EntityCombJelly> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_46801_(pos) && iServerWorld.m_46801_(pos.above()) && isLightLevelOk(pos, iServerWorld);
    }

    private static boolean isLightLevelOk(BlockPos pos, ServerLevelAccessor iServerWorld) {
        float time = iServerWorld.m_46942_(1.0F);
        int light = iServerWorld.m_46803_(pos);
        return light <= 4 && time > 0.27F && time <= 0.8F;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(JELLYPITCH, 0.0F);
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(JELLY_SCALE, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.COMB_JELLY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.COMB_JELLY_HURT.get();
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public float getJellyPitch() {
        return Mth.clamp(this.f_19804_.get(JELLYPITCH), -90.0F, 90.0F);
    }

    public void setJellyPitch(float pitch) {
        this.f_19804_.set(JELLYPITCH, Mth.clamp(pitch, -90.0F, 90.0F));
    }

    public float getJellyScale() {
        return this.f_19804_.get(JELLY_SCALE);
    }

    public void setJellyScale(float scale) {
        this.f_19804_.set(JELLY_SCALE, scale);
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

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.COMB_JELLY_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        this.prevjellyPitch = this.getJellyPitch();
        this.prevSpin = this.spin;
        if (!this.m_20069_() && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (this.m_20069_() && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20069_()) {
                this.m_20242_(true);
                if (this.moveTarget == null || this.f_19796_.nextInt(120) == 0 || this.m_20275_((double) ((float) this.moveTarget.m_123341_() + 0.5F), (double) ((float) this.moveTarget.m_123342_() + 0.5F), (double) ((float) this.moveTarget.m_123343_() + 0.5F)) < 5.0 || this.f_19797_ % 10 == 0 && !this.canBlockPosBeSeen(this.moveTarget)) {
                    BlockPos randPos = this.m_20183_().offset(this.f_19796_.nextInt(10) - 5, this.f_19796_.nextInt(6) - 3, this.f_19796_.nextInt(10) - 5);
                    if (this.m_9236_().getFluidState(randPos).is(Fluids.WATER) && this.m_9236_().getFluidState(randPos.above()).is(Fluids.WATER)) {
                        this.moveTarget = randPos;
                    }
                }
                if (this.m_204036_(FluidTags.WATER) < (double) this.m_20206_()) {
                    this.moveTarget = null;
                    this.m_20256_(this.m_20184_().add(0.0, -0.02, 0.0));
                }
                if (this.moveTarget != null) {
                    double d0 = (double) ((float) this.moveTarget.m_123341_() + 0.5F) - this.m_20185_();
                    double d1 = (double) ((float) this.moveTarget.m_123342_() + 0.5F) - this.m_20186_();
                    double d2 = (double) ((float) this.moveTarget.m_123343_() + 0.5F) - this.m_20189_();
                    double d3 = (double) Mth.sqrt((float) (d0 * d0 + d1 * d1 + d2 * d2));
                    float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                    this.m_146922_(this.rotlerp(this.m_146908_(), f, 1.0F));
                    this.f_20883_ = this.m_146908_();
                    float movSpeed = 0.004F;
                    Vec3 movingVec = new Vec3(d0 / d3, d1 / d3, d2 / d3).normalize();
                    this.m_20256_(this.m_20184_().add(movingVec.scale(0.004F)));
                }
                float dist = (float) ((Math.abs(this.m_20184_().x()) + Math.abs(this.m_20184_().z())) * 30.0);
                this.incrementJellyPitch(dist);
                if (this.f_19862_) {
                    this.m_20256_(this.m_20184_().add(0.0, 0.2F, 0.0));
                }
                if (this.getJellyPitch() > 0.0F) {
                    float decrease = Math.min(0.5F, this.getJellyPitch());
                    this.decrementJellyPitch(decrease);
                }
                if (this.getJellyPitch() < 0.0F) {
                    float decrease = Math.min(0.5F, -this.getJellyPitch());
                    this.incrementJellyPitch(decrease);
                }
            } else {
                this.m_20242_(false);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putFloat("JellyScale", this.getJellyScale());
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setJellyScale(compound.getFloat("JellyScale"));
        this.setVariant(compound.getInt("Variant"));
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        double x = (double) ((float) pos.m_123341_() + 0.5F);
        double y = (double) ((float) pos.m_123342_() + 0.5F);
        double z = (double) ((float) pos.m_123343_() + 0.5F);
        HitResult result = this.m_9236_().m_45547_(new ClipContext(this.m_146892_(), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().multiply(0.9, 0.6, 0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compoundnbt = bucket.getOrCreateTag();
        compoundnbt.putFloat("BucketScale", this.getJellyScale());
        compoundnbt.putInt("BucketVariantTag", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("BucketScale")) {
            this.setJellyScale(compound.getFloat("BucketScale"));
        }
        if (compound.contains("BucketVariantTag")) {
            this.setVariant(compound.getInt("BucketVariantTag"));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.f_19796_.nextInt(3));
        this.setJellyScale(0.8F + this.f_19796_.nextFloat() * 0.4F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void incrementJellyPitch(float pitch) {
        this.f_19804_.set(JELLYPITCH, this.getJellyPitch() + pitch);
    }

    public void decrementJellyPitch(float pitch) {
        this.f_19804_.set(JELLYPITCH, this.getJellyPitch() - pitch);
    }

    @Override
    protected float rotlerp(float float0, float float1, float float2) {
        float f = Mth.wrapDegrees(float1 - float0);
        if (f > float2) {
            f = float2;
        }
        if (f < -float2) {
            f = -float2;
        }
        float f1 = float0 + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }
        return f1;
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }
}