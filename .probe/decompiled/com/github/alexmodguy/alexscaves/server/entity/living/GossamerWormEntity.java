package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalRandomlySwimGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.VerticalSwimmingMoveControl;
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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

public class GossamerWormEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(GossamerWormEntity.class, EntityDataSerializers.BOOLEAN);

    public final GossamerWormPartEntity tail1Part;

    public final GossamerWormPartEntity tail2Part;

    public final GossamerWormPartEntity tail3Part;

    public final GossamerWormPartEntity tail4Part;

    public final GossamerWormPartEntity tail5Part;

    private final GossamerWormPartEntity[] allParts;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private float fakeYRot = 0.0F;

    private float[][] trailTransformations = new float[128][2];

    private int trailPointer = -1;

    private float squishProgress;

    private float prevSquishProgress;

    private BlockPos hurtPos = null;

    private int fleeFor = 0;

    public GossamerWormEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.tail1Part = new GossamerWormPartEntity(this, this, 1.1F, 0.5F);
        this.tail2Part = new GossamerWormPartEntity(this, this.tail1Part, 1.1F, 0.5F);
        this.tail3Part = new GossamerWormPartEntity(this, this.tail2Part, 1.0F, 0.5F);
        this.tail4Part = new GossamerWormPartEntity(this, this.tail3Part, 0.8F, 0.5F);
        this.tail5Part = new GossamerWormPartEntity(this, this.tail4Part, 0.6F, 0.5F);
        this.allParts = new GossamerWormPartEntity[] { this.tail1Part, this.tail2Part, this.tail3Part, this.tail4Part, this.tail5Part };
        this.f_21342_ = new VerticalSwimmingMoveControl(this, 0.8F, 4.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.fakeYRot = this.m_146908_();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new GossamerWormEntity.AvoidHurtGoal());
        this.f_21345_.addGoal(1, new AnimalRandomlySwimGoal(this, 3, 12, 20, 1.0));
    }

    @Override
    protected void playSwimSound(float f) {
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 0.5F * dimensions.height;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.1).add(Attributes.MAX_HEALTH, 10.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        super.m_142687_(removalReason);
        if (this.allParts != null) {
            for (PartEntity part : this.allParts) {
                part.m_142687_(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void tick() {
        this.prevFishPitch = this.fishPitch;
        this.prevSquishProgress = this.squishProgress;
        super.m_8119_();
        this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.f_20883_, (float) this.getHeadRotSpeed());
        this.fakeYRot = Mth.approachDegrees(this.fakeYRot, this.f_20883_, 10.0F);
        this.tickMultipart();
        float targetPitch = this.m_20072_() ? Mth.clamp((float) this.m_20184_().y * 25.0F, -1.4F, 1.4F) * (-180.0F / (float) Math.PI) : 0.0F;
        this.fishPitch = Mth.approachDegrees(this.fishPitch, targetPitch, 1.0F);
        if (this.fleeFor > 0) {
            this.fleeFor--;
            if (this.fleeFor == 0) {
                this.hurtPos = null;
            }
        }
        boolean grounded = !this.m_20072_();
        if (grounded && this.squishProgress < 5.0F) {
            this.squishProgress++;
        }
        if (!grounded && this.squishProgress > 0.0F) {
            this.squishProgress--;
        }
        if (grounded && this.m_20096_()) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.25, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
        }
    }

    private void tickMultipart() {
        if (this.trailPointer == -1) {
            this.fakeYRot = this.f_20883_;
            for (int i = 0; i < this.trailTransformations.length; i++) {
                this.trailTransformations[i][0] = this.fishPitch;
                this.trailTransformations[i][1] = this.fakeYRot;
            }
        }
        if (++this.trailPointer == this.trailTransformations.length) {
            this.trailPointer = 0;
        }
        this.trailTransformations[this.trailPointer][0] = this.fishPitch;
        this.trailTransformations[this.trailPointer][1] = this.fakeYRot;
        Vec3[] avector3d = new Vec3[this.allParts.length];
        for (int j = 0; j < this.allParts.length; j++) {
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
        }
        this.tail1Part.setToTransformation(new Vec3(0.0, 0.0, -1.0), this.getTrailTransformation(5, 0, 1.0F), this.getTrailTransformation(5, 1, 1.0F));
        this.tail2Part.setToTransformation(new Vec3(0.0, 0.0, -0.9F), this.getTrailTransformation(10, 0, 1.0F), this.getTrailTransformation(10, 1, 1.0F));
        this.tail3Part.setToTransformation(new Vec3(0.0, 0.0, -0.8F), this.getTrailTransformation(15, 0, 1.0F), this.getTrailTransformation(15, 1, 1.0F));
        this.tail4Part.setToTransformation(new Vec3(0.0, 0.0, -0.7F), this.getTrailTransformation(20, 0, 1.0F), this.getTrailTransformation(20, 1, 1.0F));
        this.tail5Part.setToTransformation(new Vec3(0.0, 0.0, -0.6F), this.getTrailTransformation(25, 0, 1.0F), this.getTrailTransformation(25, 1, 1.0F));
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    public float getSquishProgress(float partialTicks) {
        return (this.prevSquishProgress + (this.squishProgress - this.prevSquishProgress) * partialTicks) * 0.2F;
    }

    @Override
    public int getHeadRotSpeed() {
        return 4;
    }

    protected static float lerpRotation(float float0, float float1) {
        while (float1 - float0 < -180.0F) {
            float0 -= 360.0F;
        }
        while (float1 - float0 >= 180.0F) {
            float0 += 360.0F;
        }
        return Mth.lerp(0.2F, float0, float1);
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    public float getTrailTransformation(int pointer, int index, float partialTick) {
        if (this.m_213877_()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 127;
        int j = this.trailPointer - pointer - 1 & 127;
        float d0 = this.trailTransformations[j][index];
        float d1 = this.trailTransformations[i][index] - d0;
        return d0 + d1 * partialTick;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageValue) {
        boolean sup = super.m_6469_(damageSource, damageValue);
        if (sup) {
            this.fleeFor = 40 + this.f_19796_.nextInt(40);
            this.hurtPos = this.m_20183_();
        }
        return sup;
    }

    public static boolean checkGossamerWormSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.m_6425_(pos).is(FluidTags.WATER) && pos.m_123342_() < level.m_5736_() - 25 && randomSource.nextInt(3) == 0;
    }

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).isEmpty() && down.m_123342_() > world.m_141937_()) {
            down = down.below();
        }
        float f = (float) (this.m_20183_().m_123342_() - down.m_123342_());
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) ((float) down.m_123342_() + f * (this.f_19796_.nextFloat() * 0.33F + 0.33F)), (double) ((float) down.m_123343_() + 0.5F));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (reason == MobSpawnType.NATURAL) {
            this.doInitialPosing(worldIn);
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
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
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("FishBucketTag", platTag);
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
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean sit) {
        this.f_19804_.set(FROM_BUCKET, sit);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("FishBucketTag")) {
            this.readAdditionalSaveData(compound.getCompound("FishBucketTag"));
        }
        this.m_20301_(2000);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ACItemRegistry.GOSSAMER_WORM_BUCKET.get());
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.GOSSAMER_WORM_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.GOSSAMER_WORM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.GOSSAMER_WORM_DEATH.get();
    }

    class AvoidHurtGoal extends Goal {

        private Vec3 fleeTarget = null;

        protected AvoidHurtGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return GossamerWormEntity.this.hurtPos != null && GossamerWormEntity.this.fleeFor > 0;
        }

        @Override
        public void start() {
            this.fleeTarget = null;
        }

        @Override
        public void tick() {
            if ((this.fleeTarget == null || GossamerWormEntity.this.m_20238_(this.fleeTarget) < 6.0) && GossamerWormEntity.this.hurtPos != null) {
                this.fleeTarget = DefaultRandomPos.getPosAway(GossamerWormEntity.this, 16, 7, Vec3.atCenterOf(GossamerWormEntity.this.hurtPos));
            }
            if (this.fleeTarget != null) {
                GossamerWormEntity.this.m_21573_().moveTo(this.fleeTarget.x, this.fleeTarget.y, this.fleeTarget.z, 1.6F);
            }
        }
    }
}