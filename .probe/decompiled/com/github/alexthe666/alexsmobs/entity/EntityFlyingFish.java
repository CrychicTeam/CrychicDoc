package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.SwimmerJumpPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
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
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityFlyingFish extends WaterAnimal implements FlyingAnimal, Bucketable {

    private static final EntityDataAccessor<Boolean> GLIDING = SynchedEntityData.defineId(EntityFlyingFish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityFlyingFish.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityFlyingFish.class, EntityDataSerializers.BOOLEAN);

    public float prevOnLandProgress;

    public float onLandProgress;

    public float prevFlyProgress;

    public float flyProgress;

    private int glideIn = this.f_19796_.nextInt(75) + 50;

    protected EntityFlyingFish(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 15.0F);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntityFlyingFish.GlideGoal(this));
        this.f_21345_.addGoal(3, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalAIRandomSwimming(this, 1.0, 12, 5));
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.fromBucket() && !this.m_8077_();
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new SwimmerJumpPathNavigator(this, worldIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(GLIDING, false);
        this.f_19804_.define(VARIANT, 0);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 6.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.flyingFishSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev && source.getEntity() != null) {
            double range = 15.0;
            this.glideIn = 0;
            for (EntityFlyingFish fsh : this.m_9236_().m_45976_(this.getClass(), this.m_20191_().inflate(range, range / 2.0, range))) {
                fsh.glideIn = 0;
            }
        }
        return prev;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        this.prevFlyProgress = this.flyProgress;
        boolean onLand = !this.m_20072_() && this.m_20096_();
        if (onLand && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (!onLand && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        if (this.isGliding()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
            if (!this.m_20072_() && this.m_20184_().y < 0.0) {
                this.m_20256_(this.m_20184_().multiply(1.0, 0.5, 1.0));
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.glideIn > 0) {
            this.glideIn--;
        }
        this.f_20883_ = this.m_146908_();
        float f2 = (float) (-((double) ((float) this.m_20184_().y * 3.0F) * 180.0F / (float) Math.PI));
        if (this.isGliding()) {
            f2 = -f2;
        }
        this.m_146926_(this.rotlerp(this.m_146909_(), f2, 9.0F));
        if (!this.m_20072_() && this.m_6084_() && this.m_20096_() && this.f_19796_.nextFloat() < 0.05F) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(SoundEvents.COD_FLOP, this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    protected float rotlerp(float current, float target, float maxChange) {
        float f = Mth.wrapDegrees(target - current);
        if (f > maxChange) {
            f = maxChange;
        }
        if (f < -maxChange) {
            f = -maxChange;
        }
        return current + f;
    }

    @Override
    protected void handleAirSupply(int i) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(i - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.hurt(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(1000);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            float f = 0.6F;
            this.m_20256_(this.m_20184_().multiply(0.9, (double) f, 0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public boolean isGliding() {
        return this.f_19804_.get(GLIDING);
    }

    public void setGliding(boolean flying) {
        this.f_19804_.set(GLIDING, flying);
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    @Override
    public boolean isFlying() {
        return true;
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
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setVariant(compound.getInt("Variant"));
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.FLYING_FISH_BUCKET.get());
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
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("Variant")) {
            this.setVariant(compound.getInt("Variant"));
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance diff, MobSpawnType spawnType, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        int i;
        if (data instanceof EntityFlyingFish.FlyingFishGroupData) {
            i = ((EntityFlyingFish.FlyingFishGroupData) data).variant;
        } else {
            i = this.f_19796_.nextInt(3);
            data = new EntityFlyingFish.FlyingFishGroupData(i);
        }
        this.setVariant(i);
        return super.m_6518_(world, diff, spawnType, data, tag);
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    public static class FlyingFishGroupData extends AgeableMob.AgeableMobGroupData {

        public final int variant;

        FlyingFishGroupData(int variant) {
            super(true);
            this.variant = variant;
        }
    }

    private class GlideGoal extends Goal {

        private final EntityFlyingFish fish;

        private final Level level;

        private BlockPos surface;

        private BlockPos glide;

        public GlideGoal(EntityFlyingFish fish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.fish = fish;
            this.level = fish.m_9236_();
        }

        @Override
        public boolean canUse() {
            if (!this.fish.m_20072_()) {
                return false;
            } else {
                if (this.fish.glideIn == 0 || this.fish.m_217043_().nextInt(80) == 0) {
                    BlockPos found = this.findSurfacePos();
                    if (found != null) {
                        BlockPos glideTo = this.findGlideToPos(this.fish.m_20183_(), found);
                        if (glideTo != null) {
                            this.surface = found;
                            this.glide = glideTo;
                            this.fish.glideIn = 0;
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        private BlockPos findSurfacePos() {
            BlockPos fishPos = this.fish.m_20183_();
            for (int i = 0; i < 15; i++) {
                BlockPos offset = fishPos.offset(this.fish.f_19796_.nextInt(16) - 8, 0, this.fish.f_19796_.nextInt(16) - 8);
                while (this.level.m_46801_(offset) && offset.m_123342_() < this.level.m_151558_()) {
                    offset = offset.above();
                }
                if (!this.level.m_46801_(offset) && this.level.m_46801_(offset.below()) && this.fish.canSeeBlock(offset)) {
                    return offset;
                }
            }
            return null;
        }

        private BlockPos findGlideToPos(BlockPos fishPos, BlockPos surface) {
            Vec3 sub = Vec3.atLowerCornerOf(surface.subtract(fishPos)).normalize();
            for (double scale = EntityFlyingFish.this.f_19796_.nextDouble() * 8.0 + 1.0; scale > 2.0; scale--) {
                Vec3 scaled = sub.scale(scale);
                BlockPos at = surface.offset((int) scaled.x, 0, (int) scaled.z);
                if (!this.level.m_46801_(at) && this.level.m_46801_(at.below()) && this.fish.canSeeBlock(at)) {
                    return at;
                }
            }
            return null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.surface != null && this.glide != null && (!this.fish.m_20096_() || this.fish.m_20072_());
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            this.surface = null;
            this.glide = null;
            this.fish.glideIn = EntityFlyingFish.this.f_19796_.nextInt(75) + 150;
            this.fish.setGliding(false);
        }

        @Override
        public void tick() {
            if (this.fish.m_20072_() && this.fish.m_20238_(Vec3.atCenterOf(this.surface)) > 3.0) {
                this.fish.m_21573_().moveTo((double) ((float) this.surface.m_123341_() + 0.5F), (double) ((float) this.surface.m_123342_() + 1.0F), (double) ((float) this.surface.m_123343_() + 0.5F), 1.2F);
                if (this.fish.isGliding()) {
                    this.stop();
                }
            } else {
                this.fish.m_21573_().stop();
                Vec3 face = Vec3.atCenterOf(this.glide).subtract(Vec3.atCenterOf(this.surface));
                if (face.length() < 0.2F) {
                    face = this.fish.m_20154_();
                }
                Vec3 target = face.normalize().scale(0.1F);
                double y = 0.0;
                if (!this.fish.isGliding()) {
                    y = (double) (0.4F + EntityFlyingFish.this.f_19796_.nextFloat() * 0.2F);
                } else if (this.fish.isGliding() && this.fish.m_20072_()) {
                    this.stop();
                }
                Vec3 move = this.fish.m_20184_().add(target.x, y, target.y);
                this.fish.m_20256_(move);
                double d0 = move.horizontalDistance();
                this.fish.m_146926_((float) (-Mth.atan2(move.y, d0) * 180.0F / (float) Math.PI));
                this.fish.m_146922_((float) Mth.atan2(move.z, move.x) * (180.0F / (float) Math.PI) - 90.0F);
                this.fish.setGliding(true);
            }
        }
    }
}