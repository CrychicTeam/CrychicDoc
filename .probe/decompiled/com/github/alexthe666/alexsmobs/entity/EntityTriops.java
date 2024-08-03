package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAISwimBottom;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class EntityTriops extends WaterAnimal implements ITargetsDroppedItems, Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityTriops.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> TRIOPS_SCALE = SynchedEntityData.defineId(EntityTriops.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> BABY_AGE = SynchedEntityData.defineId(EntityTriops.class, EntityDataSerializers.INT);

    public float prevOnLandProgress;

    public float onLandProgress;

    public float prevSwimRot;

    public float swimRot;

    public boolean fedCarrot = false;

    public int breedCooldown = 0;

    public float tail1Yaw;

    public float prevTail1Yaw;

    public float tail2Yaw;

    public float prevTail2Yaw;

    public float moveDistance;

    private EntityTriops breedWith;

    private boolean pregnant;

    public EntityTriops(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 15.0F);
        this.tail1Yaw = this.m_146908_();
        this.prevTail1Yaw = this.m_146908_();
        this.tail2Yaw = this.m_146908_();
        this.prevTail2Yaw = this.m_146908_();
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(TRIOPS_SCALE, 1.0F);
        this.f_19804_.define(BABY_AGE, 0);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new EntityTriops.BreedGoal());
        this.f_21345_.addGoal(1, new EntityTriops.LayEggGoal());
        this.f_21345_.addGoal(2, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(3, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalAISwimBottom(this, 1.0, 7));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false, 10));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 5;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20072_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().multiply(0.9, 0.8, 0.9));
            if (this.m_5448_() == null) {
                this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
            }
            this.moveDistance = (float) ((double) this.moveDistance + travelVector.horizontalDistance());
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void playSwimSound(float f) {
        if (this.f_19796_.nextInt(2) == 0) {
            this.m_5496_(this.getSwimSound(), 0.2F, 1.3F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.4F);
        }
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean sit) {
        this.f_19804_.set(FROM_BUCKET, sit);
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket() || this.isBaby() || this.fedCarrot;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.isBaby() && !this.fromBucket() && !this.fedCarrot;
    }

    @Override
    protected void handleAirSupply(int i) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(i - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), this.f_19796_.nextInt(2) == 0 ? 1.0F : 0.0F);
            }
        } else {
            this.m_20301_(2000);
        }
    }

    public int getBabyAge() {
        return this.f_19804_.get(BABY_AGE);
    }

    public void setBabyAge(int babyAge) {
        this.f_19804_.set(BABY_AGE, babyAge);
    }

    public float getTriopsScale() {
        return this.f_19804_.get(TRIOPS_SCALE);
    }

    public void setTriopsScale(float scale) {
        this.f_19804_.set(TRIOPS_SCALE, scale);
    }

    @Override
    public boolean isBaby() {
        return this.getBabyAge() < 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("FedCarrot", this.fedCarrot);
        compound.putBoolean("Pregnant", this.pregnant);
        compound.putInt("BreedCooldown", this.breedCooldown);
        compound.putFloat("TriopsScale", this.getTriopsScale());
        compound.putInt("BabyAge", this.getBabyAge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.fedCarrot = compound.getBoolean("FedCarrot");
        this.pregnant = compound.getBoolean("Pregnant");
        this.breedCooldown = compound.getInt("BreedCooldown");
        this.setTriopsScale(compound.getFloat("TriopsScale"));
        this.setBabyAge(compound.getInt("BabyAge"));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setTriopsScale(0.9F + this.f_19796_.nextFloat() * 0.2F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        this.prevSwimRot = this.swimRot;
        this.prevTail1Yaw = this.tail1Yaw;
        this.prevTail2Yaw = this.tail2Yaw;
        boolean onLand = !this.m_20072_() && this.m_20096_();
        this.m_146926_(-((float) this.m_20184_().y * 2.2F * (180.0F / (float) Math.PI)));
        if (onLand && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (!onLand && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        if (this.breedCooldown > 0) {
            this.breedCooldown--;
        }
        this.tail1Yaw = Mth.approachDegrees(this.tail1Yaw, this.f_20883_, 7.0F);
        this.tail2Yaw = Mth.approachDegrees(this.tail2Yaw, this.tail1Yaw, 7.0F);
        if (this.onLandProgress == 0.0F) {
            float f = (float) (20.0 * Math.sin((double) this.f_267362_.position()) * (double) this.f_267362_.speed());
            this.swimRot = Mth.approachDegrees(this.swimRot, f, 2.0F);
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, this.m_20186_() - this.f_19855_, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 67) {
            for (int i = 0; i < 5; i++) {
                this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, this.m_20208_(0.5), this.m_20227_(0.8F), this.m_20262_(0.5), 0.0, 0.0, 0.0);
            }
        } else if (id == 68) {
            this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20185_(), this.m_20227_(0.8F), this.m_20189_(), 0.0, 0.0, 0.0);
        } else {
            super.m_7822_(id);
        }
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return (stack.is(Tags.Items.CROPS_CARROT) || stack.is(AMItemRegistry.MOSQUITO_LARVA.get())) && !this.fedCarrot;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack stack = e.getItem();
        if (stack.getItem().isEdible() && stack.getItem().getFoodProperties() != null) {
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6100_(), this.m_6121_());
            this.m_5634_(5.0F);
            if (!this.m_9236_().isClientSide && this.breedCooldown == 0 && !this.fedCarrot) {
                this.fedCarrot = true;
                this.m_9236_().broadcastEntityEvent(this, (byte) 67);
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        if (!type.consumesAction() && this.canTargetItem(itemstack) && !this.fedCarrot) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.CAT_EAT, this.m_6100_(), this.m_6121_());
            this.m_5634_(5.0F);
            if (itemstack.is(Tags.Items.CROPS_CARROT)) {
                if (!this.m_9236_().isClientSide && this.breedCooldown == 0) {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 67);
                }
                this.fedCarrot = true;
            }
            return InteractionResult.SUCCESS;
        } else {
            return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(type);
        }
    }

    public boolean isSearchingForMate() {
        return this.m_6084_() && this.m_20072_() && this.fedCarrot && this.breedCooldown <= 0;
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("TriopsTag", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("TriopsTag")) {
            this.readAdditionalSaveData(compound.getCompound("TriopsTag"));
        }
        this.m_20301_(2000);
    }

    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.TRIOPS_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TRIOPS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TRIOPS_HURT.get();
    }

    private class BreedGoal extends Goal {

        private final Predicate<Entity> validBreedPartner;

        private EntityTriops breedPartner;

        private int executionCooldown = 50;

        public BreedGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.validBreedPartner = shrimp -> {
                if (shrimp instanceof EntityTriops otherFish && otherFish.m_19879_() != EntityTriops.this.m_19879_() && otherFish.isSearchingForMate()) {
                    return true;
                }
                return false;
            };
        }

        @Override
        public boolean canUse() {
            if (EntityTriops.this.m_20072_() && EntityTriops.this.fedCarrot && EntityTriops.this.breedCooldown <= 0 && EntityTriops.this.breedWith == null) {
                if (this.executionCooldown > 0) {
                    this.executionCooldown--;
                } else {
                    this.executionCooldown = 50 + EntityTriops.this.f_19796_.nextInt(50);
                    List<EntityTriops> list = EntityTriops.this.m_9236_().m_6443_(EntityTriops.class, EntityTriops.this.m_20191_().inflate(10.0, 8.0, 10.0), EntitySelector.NO_SPECTATORS.and(this.validBreedPartner));
                    list.sort(Comparator.comparingDouble(EntityTriops.this::m_20280_));
                    if (!list.isEmpty()) {
                        EntityTriops closestPupfish = (EntityTriops) list.get(0);
                        if (closestPupfish != null) {
                            this.breedPartner = closestPupfish;
                            this.breedPartner.breedWith = EntityTriops.this;
                            return true;
                        }
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.breedPartner != null && !EntityTriops.this.pregnant && !this.breedPartner.pregnant && EntityTriops.this.breedWith == null && this.breedPartner.isSearchingForMate() && EntityTriops.this.isSearchingForMate();
        }

        @Override
        public void start() {
        }

        @Override
        public void stop() {
            EntityTriops.this.fedCarrot = false;
            EntityTriops.this.breedCooldown = 1200 + EntityTriops.this.f_19796_.nextInt(3600);
        }

        @Override
        public void tick() {
            EntityTriops.this.m_21573_().moveTo(this.breedPartner, 1.0);
            this.breedPartner.m_21573_().moveTo(EntityTriops.this, 1.0);
            if (EntityTriops.this.m_20270_(this.breedPartner) < 1.2F) {
                EntityTriops.this.m_9236_().broadcastEntityEvent(EntityTriops.this, (byte) 68);
                EntityTriops.this.pregnant = true;
            }
        }
    }

    class LayEggGoal extends Goal {

        private BlockPos eggPos;

        LayEggGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            this.eggPos = null;
        }

        @Override
        public boolean canUse() {
            if (EntityTriops.this.pregnant && EntityTriops.this.m_217043_().nextInt(30) == 0) {
                BlockPos egg = this.getEggLayPos();
                if (egg != null) {
                    this.eggPos = egg;
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.eggPos != null && EntityTriops.this.pregnant && EntityTriops.this.m_9236_().getBlockState(this.eggPos).m_60795_();
        }

        public boolean isValidPos(BlockPos pos) {
            BlockState state = EntityTriops.this.m_9236_().getBlockState(pos);
            FluidState stateBelow = EntityTriops.this.m_9236_().getFluidState(pos.below());
            return stateBelow.is(FluidTags.WATER) && state.m_60795_();
        }

        public BlockPos getEggLayPos() {
            for (int i = 0; i < 10; i++) {
                BlockPos offset = EntityTriops.this.m_20183_().offset(EntityTriops.this.m_217043_().nextInt(10) - 5, 10, EntityTriops.this.m_217043_().nextInt(10) - 5);
                while (EntityTriops.this.m_9236_().getBlockState(offset.below()).m_60795_() && offset.m_123342_() > EntityTriops.this.m_9236_().m_141937_()) {
                    offset = offset.below();
                }
                if (this.isValidPos(offset)) {
                    return offset;
                }
            }
            return null;
        }

        @Override
        public void tick() {
            super.tick();
            EntityTriops.this.m_21573_().moveTo((double) this.eggPos.m_123341_(), (double) this.eggPos.m_123342_(), (double) this.eggPos.m_123343_(), 1.0);
            if (EntityTriops.this.m_20238_(Vec3.atBottomCenterOf(this.eggPos)) < 2.0) {
                EntityTriops.this.pregnant = false;
                EntityTriops.this.m_9236_().setBlockAndUpdate(this.eggPos, AMBlockRegistry.TRIOPS_EGGS.get().defaultBlockState());
            }
        }
    }
}