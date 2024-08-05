package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class SeaPigEntity extends WaterAnimal implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SeaPigEntity.class, EntityDataSerializers.BOOLEAN);

    private float digestProgress;

    private float prevDigestProgress;

    private float squishProgress;

    private float prevSquishProgress;

    public static final ResourceLocation DIGESTION_LOOT_TABLE = new ResourceLocation("alexscaves", "gameplay/sea_pig_digestion");

    public SeaPigEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.f_21342_ = new SmoothSwimmingMoveControl(this, 85, 10, 0.75F, 0.5F, false);
        this.m_20301_(40);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.05).add(Attributes.MAX_HEALTH, 8.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new SeaPigEntity.WanderGoal());
        this.f_21345_.addGoal(2, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    public static boolean checkSeaPigSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        return level.m_6425_(pos).is(FluidTags.WATER) && pos.m_123342_() < level.m_5736_() - 25;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader worldIn) {
        return worldIn.m_6425_(pos.below()).isEmpty() && worldIn.m_6425_(pos).is(FluidTags.WATER) ? 10.0F : 0.0F;
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevDigestProgress = this.digestProgress;
        this.prevSquishProgress = this.squishProgress;
        if (this.isDigesting() && this.digestProgress < 1.0F) {
            if (this.digestProgress == 0.0F) {
                this.m_216990_(ACSoundRegistry.SEA_PIG_EAT.get());
            }
            this.digestProgress += 0.05F;
            if (this.digestProgress >= 1.0F) {
                this.digestProgress = 0.0F;
                this.prevDigestProgress = 0.0F;
                this.digestItem();
            }
        }
        boolean grounded = this.m_20096_() && !this.m_20072_();
        if (grounded && this.squishProgress < 5.0F) {
            this.squishProgress++;
        }
        if (!grounded && this.squishProgress > 0.0F) {
            this.squishProgress--;
        }
    }

    @Override
    protected void playSwimSound(float f) {
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    protected void handleAirSupply(int prevAir) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(prevAir - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(40);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            Vec3 delta = this.m_20184_();
            this.m_6478_(MoverType.SELF, delta);
            delta = delta.scale(0.8);
            if (!this.f_20899_ && (!this.f_19862_ || !this.m_9236_().getBlockState(this.m_20183_().above()).m_60819_().is(FluidTags.WATER))) {
                delta = delta.add(0.0, -0.03F, 0.0);
            } else {
                delta = delta.add(0.0, 0.03F, 0.0);
            }
            this.m_20256_(delta.scale(0.8));
        } else {
            super.m_7023_(travelVector);
        }
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

    private void digestItem() {
        if (!this.m_9236_().isClientSide) {
            LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(DIGESTION_LOOT_TABLE);
            List<ItemStack> items = loottable.getRandomItems(new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.PIGLIN_BARTER));
            items.forEach(this::m_19983_);
        }
        this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
        if (itemstack.is(ACTagRegistry.SEA_PIG_DIGESTS) && !this.isDigesting() && !type.consumesAction()) {
            ItemStack copy = itemstack.copy();
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            copy.setCount(1);
            this.m_21008_(InteractionHand.MAIN_HAND, copy);
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    public float getDigestProgress(float partialTick) {
        return Math.min(1.0F, this.prevDigestProgress + (this.digestProgress - this.prevDigestProgress) * partialTick);
    }

    public float getSquishProgress(float partialTicks) {
        return (this.prevSquishProgress + (this.squishProgress - this.prevSquishProgress) * partialTicks) * 0.2F;
    }

    public boolean isDigesting() {
        return this.m_21120_(InteractionHand.MAIN_HAND).is(ACTagRegistry.SEA_PIG_DIGESTS);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, flying ? this.m_20186_() - this.f_19855_ : 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 128.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
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
        return new ItemStack(ACItemRegistry.SEA_PIG_BUCKET.get());
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.SEA_PIG_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.SEA_PIG_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.SEA_PIG_DEATH.get();
    }

    private class WanderGoal extends Goal {

        private double x;

        private double y;

        private double z;

        public WanderGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (SeaPigEntity.this.m_217043_().nextInt(50) != 0) {
                return false;
            } else {
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
            double dist = SeaPigEntity.this.m_20275_(this.x, this.y, this.z);
            return dist > 4.0;
        }

        @Override
        public void tick() {
            SeaPigEntity.this.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
        }

        public BlockPos findWaterBlock() {
            BlockPos result = null;
            RandomSource random = SeaPigEntity.this.m_217043_();
            int range = 10;
            for (int i = 0; i < 15; i++) {
                BlockPos blockPos = SeaPigEntity.this.m_20183_().offset(random.nextInt(range) - range / 2, random.nextInt(range) - range / 2, random.nextInt(range) - range / 2);
                if (SeaPigEntity.this.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && blockPos.m_123342_() > SeaPigEntity.this.m_9236_().m_141937_() + 1) {
                    result = blockPos;
                }
            }
            return result;
        }

        @Nullable
        protected Vec3 getPosition() {
            BlockPos water = this.findWaterBlock();
            if (!SeaPigEntity.this.m_20072_()) {
                return water == null ? DefaultRandomPos.getPos(SeaPigEntity.this, 7, 3) : Vec3.atCenterOf(water);
            } else if (water == null) {
                return null;
            } else {
                while (SeaPigEntity.this.m_9236_().getFluidState(water.below()).is(FluidTags.WATER) && water.m_123342_() > SeaPigEntity.this.m_9236_().m_141937_() + 1) {
                    water = water.below();
                }
                water = water.above();
                return Vec3.atCenterOf(water);
            }
        }
    }
}