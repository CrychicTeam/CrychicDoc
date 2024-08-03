package com.github.alexmodguy.alexscaves.server.entity.living;

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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TripodfishEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(TripodfishEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TripodfishEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(0.95F, 1.5F);

    private float landProgress;

    private float prevLandProgress;

    private float fishPitch = 0.0F;

    private float prevFishPitch = 0.0F;

    private float standProgress;

    private float prevStandProgress;

    private boolean hasStandingSize = false;

    private int timeSwimming;

    private int timeStanding;

    private int navigateTypeLength = 300;

    private BlockPos hurtPos = null;

    private int fleeFor = 0;

    public TripodfishEntity(EntityType type, Level level) {
        super(type, level);
        this.f_21342_ = new TripodfishEntity.MoveControl();
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new TripodfishEntity.AvoidHurtGoal());
        this.f_21345_.addGoal(2, new TripodfishEntity.WanderGoal());
        this.f_21345_.addGoal(3, new RandomLookAroundGoal(this) {

            @Override
            public boolean canUse() {
                return super.canUse() && !TripodfishEntity.this.isStanding();
            }
        });
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F) {

            @Override
            public boolean canUse() {
                return super.canUse() && !TripodfishEntity.this.isStanding();
            }
        });
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            if (this.m_5448_() == null && !this.isStanding()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
            this.m_6478_(MoverType.SELF, delta);
            this.m_20256_(delta.scale(this.isStanding() ? 0.3F : 0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void playSwimSound(float f) {
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 8.0);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isStanding() ? STANDING_SIZE.scale(this.m_6134_()) : super.m_6972_(poseIn);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevStandProgress = this.standProgress;
        this.prevFishPitch = this.fishPitch;
        this.prevLandProgress = this.landProgress;
        float pitchTarget = (float) this.m_20184_().y * 3.0F;
        if (this.isStanding()) {
            if (this.standProgress < 10.0F) {
                this.standProgress++;
            }
            if (!this.hasStandingSize) {
                this.hasStandingSize = true;
                this.m_6210_();
                this.navigateTypeLength = 400 + this.f_19796_.nextInt(400);
            }
            this.timeStanding++;
            this.timeSwimming = 0;
            pitchTarget = 0.0F;
            this.m_21573_().stop();
            if (!this.m_20096_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.05, 0.0).multiply(0.5, 1.0, 0.5));
            }
        } else {
            if (this.standProgress > 0.0F) {
                this.standProgress--;
            }
            this.timeStanding = 0;
            this.timeSwimming++;
            if (this.hasStandingSize) {
                this.hasStandingSize = false;
                double d = (double) ((float) ((double) (this.m_20206_() * 0.35F) + this.m_20186_()));
                this.m_6210_();
                this.m_6034_(this.m_20185_(), d, this.m_20189_());
                this.navigateTypeLength = 400 + this.f_19796_.nextInt(400);
            }
        }
        this.fishPitch = Mth.approachDegrees(this.fishPitch, Mth.clamp(pitchTarget, -1.4F, 1.4F) * (-180.0F / (float) Math.PI), 5.0F);
        boolean grounded = !this.m_20072_();
        if (grounded && this.landProgress < 5.0F) {
            this.landProgress++;
        }
        if (!grounded && this.landProgress > 0.0F) {
            this.landProgress--;
        }
        if (!this.m_20072_() && this.m_6084_() && this.m_20096_()) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(ACSoundRegistry.TRIPODFISH_FLOP.get(), this.m_6121_(), this.m_6100_());
        }
        if (this.fleeFor > 0) {
            this.fleeFor--;
            if (this.fleeFor == 0) {
                this.hurtPos = null;
            }
        }
    }

    public float getFishPitch(float partialTick) {
        return this.prevFishPitch + (this.fishPitch - this.prevFishPitch) * partialTick;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public static boolean checkTripodfishSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.m_6425_(pos).is(FluidTags.WATER) && pos.m_123342_() < level.m_5736_() - 30 && randomSource.nextBoolean();
    }

    public float getLandProgress(float partialTicks) {
        return (this.prevLandProgress + (this.landProgress - this.prevLandProgress) * partialTicks) * 0.2F;
    }

    public float getStandProgress(float partialTicks) {
        return (this.prevStandProgress + (this.standProgress - this.prevStandProgress) * partialTicks) * 0.1F;
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

    private void doInitialPosing(LevelAccessor world) {
        BlockPos down = this.m_20183_();
        while (!world.m_6425_(down).isEmpty() && down.m_123342_() > world.m_141937_()) {
            down = down.below();
        }
        this.m_6034_((double) ((float) down.m_123341_() + 0.5F), (double) (down.m_123342_() + 1), (double) ((float) down.m_123343_() + 0.5F));
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
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
        return new ItemStack(ACItemRegistry.TRIPODFISH_BUCKET.get());
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.TRIPODFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.TRIPODFISH_HURT.get();
    }

    class AvoidHurtGoal extends Goal {

        private Vec3 fleeTarget = null;

        protected AvoidHurtGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return TripodfishEntity.this.hurtPos != null && TripodfishEntity.this.fleeFor > 0;
        }

        @Override
        public void start() {
            TripodfishEntity.this.setStanding(false);
            this.fleeTarget = null;
        }

        @Override
        public void tick() {
            if ((this.fleeTarget == null || TripodfishEntity.this.m_20238_(this.fleeTarget) < 6.0) && TripodfishEntity.this.hurtPos != null) {
                this.fleeTarget = DefaultRandomPos.getPosAway(TripodfishEntity.this, 16, 7, Vec3.atCenterOf(TripodfishEntity.this.hurtPos));
            }
            if (this.fleeTarget != null) {
                TripodfishEntity.this.m_21573_().moveTo(this.fleeTarget.x, this.fleeTarget.y, this.fleeTarget.z, 1.6F);
            }
        }
    }

    private class MoveControl extends VerticalSwimmingMoveControl {

        private MoveControl() {
            super(TripodfishEntity.this, 0.5F, 60.0F);
        }

        @Override
        public void tick() {
            if (!TripodfishEntity.this.isStanding()) {
                super.tick();
            }
        }
    }

    private class WanderGoal extends Goal {

        private double x;

        private double y;

        private double z;

        private boolean wantsToStand;

        public WanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (TripodfishEntity.this.m_217043_().nextInt(100) != 0 && TripodfishEntity.this.isStanding() && TripodfishEntity.this.timeStanding < TripodfishEntity.this.navigateTypeLength) {
                return false;
            } else {
                if (TripodfishEntity.this.isStanding()) {
                    this.wantsToStand = false;
                } else {
                    this.wantsToStand = TripodfishEntity.this.timeSwimming > 300 || TripodfishEntity.this.m_217043_().nextFloat() < 0.2F;
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
            double dist = TripodfishEntity.this.m_20275_(this.x, this.y, this.z);
            return !TripodfishEntity.this.m_21573_().isDone() && dist > 9.0;
        }

        @Override
        public void start() {
            TripodfishEntity.this.setStanding(false);
            TripodfishEntity.this.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
        }

        @Override
        public void stop() {
            BlockPos ground = TripodfishEntity.this.m_20183_();
            int down;
            for (down = 0; TripodfishEntity.this.m_9236_().getFluidState(ground).is(FluidTags.WATER) && down < 3 && ground.m_123342_() > TripodfishEntity.this.m_9236_().m_141937_(); down++) {
                ground = ground.below();
            }
            if (this.wantsToStand && down <= 2) {
                TripodfishEntity.this.setStanding(true);
                TripodfishEntity.this.m_21573_().stop();
                TripodfishEntity.this.m_20256_(Vec3.ZERO);
            }
        }

        public BlockPos findWaterBlock() {
            BlockPos result = null;
            RandomSource random = TripodfishEntity.this.m_217043_();
            int range = 20;
            for (int i = 0; i < 15; i++) {
                BlockPos blockPos = TripodfishEntity.this.m_20183_().offset(random.nextInt(range) - range / 2, random.nextInt(range) - range / 2, random.nextInt(range) - range / 2);
                if (TripodfishEntity.this.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && blockPos.m_123342_() > TripodfishEntity.this.m_9236_().m_141937_()) {
                    result = blockPos;
                }
            }
            return result;
        }

        public boolean isTargetBlocked(Vec3 target) {
            Vec3 Vector3d = new Vec3(TripodfishEntity.this.m_20185_(), TripodfishEntity.this.m_20188_(), TripodfishEntity.this.m_20189_());
            return TripodfishEntity.this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, TripodfishEntity.this)).getType() != HitResult.Type.MISS;
        }

        @Nullable
        protected Vec3 getPosition() {
            BlockPos water = this.findWaterBlock();
            if (!TripodfishEntity.this.m_20072_()) {
                return water == null ? DefaultRandomPos.getPos(TripodfishEntity.this, 7, 3) : Vec3.atCenterOf(water);
            } else if (water == null) {
                return null;
            } else {
                while (TripodfishEntity.this.m_9236_().getFluidState(water.below()).is(FluidTags.WATER) && water.m_123342_() > TripodfishEntity.this.m_9236_().m_141937_() + 1) {
                    water = water.below();
                }
                BlockState seafloorState = TripodfishEntity.this.m_9236_().getBlockState(water.below());
                if (!this.wantsToStand || !seafloorState.m_60713_(Blocks.MAGMA_BLOCK) && (seafloorState.m_60819_().isEmpty() || seafloorState.m_60819_().is(FluidTags.WATER))) {
                    BlockPos above = water.above(this.wantsToStand ? 1 : 3 + TripodfishEntity.this.f_19796_.nextInt(3));
                    while (!TripodfishEntity.this.m_9236_().getFluidState(above).is(FluidTags.WATER) && above.m_123342_() > water.m_123342_()) {
                        above = above.below();
                    }
                    Vec3 vec3 = Vec3.atCenterOf(above);
                    return !this.isTargetBlocked(vec3) ? vec3 : null;
                } else {
                    return null;
                }
            }
        }
    }
}