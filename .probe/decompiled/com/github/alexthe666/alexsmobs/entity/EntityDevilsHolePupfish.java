package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.AquaticMoveController;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.alexsmobs.world.AMWorldData;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityDevilsHolePupfish extends WaterAnimal implements FlyingAnimal, Bucketable {

    public static final ResourceLocation PUPFISH_REWARD = new ResourceLocation("alexsmobs", "gameplay/pupfish_reward");

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityDevilsHolePupfish.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> PUPFISH_SCALE = SynchedEntityData.defineId(EntityDevilsHolePupfish.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> FEEDING_TIME = SynchedEntityData.defineId(EntityDevilsHolePupfish.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> BABY_AGE = SynchedEntityData.defineId(EntityDevilsHolePupfish.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<BlockPos>> FEEDING_POS = SynchedEntityData.defineId(EntityDevilsHolePupfish.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public float prevOnLandProgress;

    public float onLandProgress;

    public float prevFeedProgress;

    public float feedProgress;

    private EntityDevilsHolePupfish chasePartner;

    private int chaseTime = 0;

    private boolean chaseDriver;

    private boolean breedNextChase;

    private int chaseCooldown = 0;

    private int maxChaseTime = 300;

    protected EntityDevilsHolePupfish(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.f_21342_ = new AquaticMoveController(this, 1.0F, 15.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.DEVILS_HOLE_PUPFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.DEVILS_HOLE_PUPFISH_HURT.get();
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(1, new TryFindWaterGoal(this));
        this.f_21345_.addGoal(2, new EntityDevilsHolePupfish.EatMossGoal(this));
        this.f_21345_.addGoal(3, new EntityDevilsHolePupfish.ChaseGoal(this));
        this.f_21345_.addGoal(4, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(5, new AnimalAIRandomSwimming(this, 1.0, 12, 5));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 2.0).add(Attributes.MOVEMENT_SPEED, 0.34F);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.fromBucket();
    }

    public static boolean canPupfishSpawn(EntityType<EntityDevilsHolePupfish> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || isPupfishChunk(iServerWorld, pos) && iServerWorld.m_6425_(pos).is(FluidTags.WATER) && isInCave(iServerWorld, pos);
    }

    private static boolean isPupfishChunk(ServerLevelAccessor iServerWorld, BlockPos pos) {
        AMWorldData data = AMWorldData.get(iServerWorld.getLevel());
        return data != null && data.isInPupfishChunk(pos);
    }

    private static boolean isInCave(ServerLevelAccessor iServerWorld, BlockPos pos) {
        while (iServerWorld.m_6425_(pos).is(FluidTags.WATER)) {
            pos = pos.above();
        }
        return !iServerWorld.m_45527_(pos) && pos.m_123342_() < iServerWorld.m_5736_();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.devilsHolePupfishSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 6;
    }

    @Override
    public boolean isMaxGroupSizeReached(int sizeIn) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(PUPFISH_SCALE, 1.0F);
        this.f_19804_.define(FEEDING_TIME, 0);
        this.f_19804_.define(BABY_AGE, 0);
        this.f_19804_.define(FEEDING_POS, Optional.empty());
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOnLandProgress = this.onLandProgress;
        this.prevFeedProgress = this.feedProgress;
        if (this.chaseCooldown > 0) {
            this.chaseCooldown--;
        }
        boolean inWaterOrBubble = this.m_20072_();
        if (!inWaterOrBubble && this.onLandProgress < 5.0F) {
            this.onLandProgress++;
        }
        if (inWaterOrBubble && this.onLandProgress > 0.0F) {
            this.onLandProgress--;
        }
        int feedingTime = this.getFeedingTime();
        if (feedingTime > 0 && this.feedProgress < 5.0F) {
            this.feedProgress++;
        }
        if (feedingTime <= 0 && this.feedProgress > 0.0F) {
            this.feedProgress--;
        }
        if (this.isBaby()) {
            this.setBabyAge(this.getBabyAge() + 1);
        }
        BlockPos feedingPos = (BlockPos) this.f_19804_.get(FEEDING_POS).orElse(null);
        if (feedingPos == null) {
            float f2 = (float) (-((double) ((float) this.m_20184_().y * 2.2F) * 180.0F / (float) Math.PI));
            this.m_146926_(f2);
        } else if (this.getFeedingTime() > 0) {
            Vec3 face = Vec3.atCenterOf(feedingPos).subtract(this.m_20182_());
            double d0 = face.horizontalDistance();
            this.m_146926_((float) (-Mth.atan2(face.y, d0) * 180.0F / (float) Math.PI));
            this.m_146922_((float) Mth.atan2(face.z, face.x) * (180.0F / (float) Math.PI) - 90.0F);
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.m_146908_();
            BlockState state = this.m_9236_().getBlockState(feedingPos);
            if (this.f_19796_.nextInt(2) == 0 && !state.m_60795_()) {
                Vec3 mouth = new Vec3(0.0, (double) (this.m_20206_() * 0.5F), (double) (0.4F * this.getPupfishScale())).xRot(this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
                for (int i = 0; i < 4 + this.f_19796_.nextInt(2); i++) {
                    double motX = this.f_19796_.nextGaussian() * 0.02;
                    double motY = (double) (0.1F + this.f_19796_.nextFloat() * 0.2F);
                    double motZ = this.f_19796_.nextGaussian() * 0.02;
                    this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.m_20185_() + mouth.x, this.m_20186_() + mouth.y, this.m_20189_() + mouth.z, motX, motY, motZ);
                }
            }
        }
        if (!this.m_20072_() && this.m_6084_() && this.m_20096_() && this.f_19796_.nextFloat() < 0.5F) {
            this.m_20256_(this.m_20184_().add((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5, (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * 0.2F)));
            this.m_146922_(this.f_19796_.nextFloat() * 360.0F);
            this.m_5496_(SoundEvents.COD_FLOP, this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return super.m_6972_(poseIn).scale(this.getPupfishScale());
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean bucketed) {
        this.f_19804_.set(FROM_BUCKET, bucketed);
    }

    @Override
    public void saveToBucketTag(@Nonnull ItemStack bucket) {
        if (this.m_8077_()) {
            bucket.setHoverName(this.m_7770_());
        }
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.putFloat("BucketScale", this.getPupfishScale());
        compound.putFloat("BabyAge", (float) this.getBabyAge());
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("BucketScale")) {
            this.setPupfishScale(compound.getFloat("BucketScale"));
        }
        if (compound.contains("BabyAge")) {
            this.setBabyAge(compound.getInt("BabyAge"));
        }
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.DEVILS_HOLE_PUPFISH_BUCKET.get());
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public float getPupfishScale() {
        return this.f_19804_.get(PUPFISH_SCALE);
    }

    public void setPupfishScale(float scale) {
        this.f_19804_.set(PUPFISH_SCALE, scale);
    }

    public int getFeedingTime() {
        return this.f_19804_.get(FEEDING_TIME);
    }

    public void setFeedingTime(int feedingTime) {
        this.f_19804_.set(FEEDING_TIME, feedingTime);
    }

    public int getBabyAge() {
        return this.f_19804_.get(BABY_AGE);
    }

    public void setBabyAge(int babyAge) {
        this.f_19804_.set(BABY_AGE, babyAge);
    }

    @Override
    public boolean isBaby() {
        return this.getBabyAge() < 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("BreedNextChase", this.breedNextChase);
        compound.putFloat("PupfishScale", this.getPupfishScale());
        compound.putInt("BabyAge", this.getBabyAge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.breedNextChase = compound.getBoolean("BreedNextChase");
        this.setPupfishScale(compound.getFloat("PupfishScale"));
        this.setBabyAge(compound.getInt("BabyAge"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setPupfishScale(0.65F + this.f_19796_.nextFloat() * 0.35F);
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void handleAirSupply(int i) {
        if (this.m_6084_() && !this.m_20072_()) {
            this.m_20301_(i - 1);
            if (this.m_20146_() == -20) {
                this.m_20301_(0);
                this.m_6469_(this.m_269291_().dryOut(), 2.0F);
            }
        } else {
            this.m_20301_(this.getMaxAirSupply());
        }
    }

    @Override
    public int getMaxAirSupply() {
        return 600;
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
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean isFlying() {
        return false;
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    private static List<ItemStack> getFoodLoot(EntityDevilsHolePupfish pupfish) {
        LootTable loottable = pupfish.m_9236_().getServer().getLootData().m_278676_(PUPFISH_REWARD);
        return loottable.getRandomItems(new LootParams.Builder((ServerLevel) pupfish.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, pupfish).create(LootContextParamSets.PIGLIN_BARTER));
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return !this.fromBucket() && !this.m_8077_() && !this.isBaby();
    }

    private void spawnBabiesWith(EntityDevilsHolePupfish chasePartner) {
        EntityDevilsHolePupfish baby = AMEntityRegistry.DEVILS_HOLE_PUPFISH.get().create(this.m_9236_());
        baby.m_20359_(this);
        baby.setPupfishScale(0.65F + this.f_19796_.nextFloat() * 0.35F);
        baby.setBabyAge(-24000);
        this.m_9236_().m_7967_(baby);
    }

    @Nonnull
    @Override
    protected InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.m_6071_(player, hand));
    }

    private class ChaseGoal extends Goal {

        private final EntityDevilsHolePupfish pupfish;

        private final Predicate<Entity> validChasePartner;

        private int executionCooldown = 50;

        public ChaseGoal(EntityDevilsHolePupfish pupfish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.pupfish = pupfish;
            this.validChasePartner = pupfish1 -> {
                if (pupfish1 instanceof EntityDevilsHolePupfish otherFish && otherFish.m_19879_() != this.pupfish.m_19879_() && otherFish.chasePartner == null && otherFish.chaseCooldown <= 0) {
                    return true;
                }
                return false;
            };
        }

        @Override
        public boolean canUse() {
            if (!this.pupfish.m_20072_() || this.pupfish.chaseTime > this.pupfish.maxChaseTime || this.pupfish.chaseCooldown > 0) {
                return false;
            } else if (this.pupfish.chasePartner != null && this.pupfish.chasePartner.m_6084_()) {
                return true;
            } else {
                if (this.executionCooldown > 0) {
                    this.executionCooldown--;
                } else {
                    this.executionCooldown = 50 + EntityDevilsHolePupfish.this.f_19796_.nextInt(50);
                    if (this.pupfish.chasePartner == null || !this.pupfish.chasePartner.m_6084_()) {
                        List<EntityDevilsHolePupfish> list = this.pupfish.m_9236_().m_6443_(EntityDevilsHolePupfish.class, this.pupfish.m_20191_().inflate(10.0, 8.0, 10.0), EntitySelector.NO_SPECTATORS.and(this.validChasePartner));
                        list.sort(Comparator.comparingDouble(this.pupfish::m_20280_));
                        if (!list.isEmpty()) {
                            EntityDevilsHolePupfish closestPupfish = (EntityDevilsHolePupfish) list.get(0);
                            if (closestPupfish != null) {
                                this.pupfish.chasePartner = closestPupfish;
                                closestPupfish.chasePartner = this.pupfish;
                                this.pupfish.chaseDriver = true;
                                return true;
                            }
                        }
                        return false;
                    }
                }
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.pupfish.chasePartner != null && this.pupfish.chasePartner.m_6084_() && this.pupfish.chaseTime < this.pupfish.maxChaseTime;
        }

        @Override
        public void start() {
            this.pupfish.chaseDriver = !this.pupfish.chasePartner.chaseDriver;
            this.pupfish.chaseTime = 0;
            this.pupfish.maxChaseTime = 600;
        }

        @Override
        public void stop() {
            this.pupfish.chaseTime = 0;
            this.pupfish.chaseCooldown = 100 + EntityDevilsHolePupfish.this.f_19796_.nextInt(100);
            this.executionCooldown = 50 + EntityDevilsHolePupfish.this.f_19796_.nextInt(20);
            if (this.pupfish.breedNextChase) {
                this.pupfish.spawnBabiesWith(this.pupfish.chasePartner);
                this.pupfish.chasePartner.breedNextChase = false;
                this.pupfish.breedNextChase = false;
            }
            this.pupfish.chasePartner = null;
        }

        @Override
        public void tick() {
            this.pupfish.chaseTime++;
            if (this.pupfish.chasePartner != null && this.pupfish.chaseDriver) {
                float chaserSpeed = 1.2F + EntityDevilsHolePupfish.this.f_19796_.nextFloat() * 0.45F;
                float chasedSpeed = 0.2F + chaserSpeed * 0.7F;
                EntityDevilsHolePupfish flee = this.pupfish.chaseDriver ? this.pupfish.chasePartner : this.pupfish;
                EntityDevilsHolePupfish driver = this.pupfish.chaseDriver ? this.pupfish : this.pupfish.chasePartner;
                driver.m_21573_().moveTo(flee.m_20185_(), flee.m_20227_(0.5), flee.m_20189_(), (double) chaserSpeed);
                Vec3 from = flee.m_20182_().add((double) (EntityDevilsHolePupfish.this.f_19796_.nextFloat() - 0.5F), (double) (EntityDevilsHolePupfish.this.f_19796_.nextFloat() - 0.5F), (double) (EntityDevilsHolePupfish.this.f_19796_.nextFloat() - 0.5F)).subtract(driver.m_20182_()).normalize().scale((double) (2.0F + EntityDevilsHolePupfish.this.f_19796_.nextFloat() * 2.0F));
                Vec3 to = flee.m_20182_().add(from);
                flee.m_21573_().moveTo(to.x, to.y, to.z, (double) chasedSpeed);
                if (EntityDevilsHolePupfish.this.f_19796_.nextInt(50) == 0) {
                    this.pupfish.chaseDriver = !this.pupfish.chaseDriver;
                    this.pupfish.chasePartner.chaseDriver = !this.pupfish.chasePartner.chaseDriver;
                }
            }
        }
    }

    private class EatMossGoal extends Goal {

        private final int searchLength;

        private final int verticalSearchRange;

        protected BlockPos destinationBlock;

        private final EntityDevilsHolePupfish pupfish;

        private int runDelay = 70;

        private int maxFeedTime = 200;

        private EatMossGoal(EntityDevilsHolePupfish pupfish) {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.pupfish = pupfish;
            this.searchLength = 16;
            this.verticalSearchRange = 6;
        }

        @Override
        public boolean canContinueToUse() {
            return this.destinationBlock != null && this.isMossBlock(this.pupfish.m_9236_(), this.destinationBlock.mutable()) && this.isCloseToMoss(16.0);
        }

        public boolean isCloseToMoss(double dist) {
            return this.destinationBlock == null || this.pupfish.m_20238_(Vec3.atCenterOf(this.destinationBlock)) < dist * dist;
        }

        @Override
        public boolean canUse() {
            if (!this.pupfish.m_20072_()) {
                return false;
            } else if (this.runDelay > 0) {
                this.runDelay--;
                return false;
            } else {
                this.runDelay = 200 + this.pupfish.f_19796_.nextInt(150);
                return this.searchForDestination();
            }
        }

        @Override
        public void start() {
            this.maxFeedTime = 60 + EntityDevilsHolePupfish.this.f_19796_.nextInt(60);
        }

        @Override
        public void tick() {
            Vec3 vec = Vec3.atCenterOf(this.destinationBlock);
            if (vec != null) {
                this.pupfish.m_21573_().moveTo(vec.x, vec.y, vec.z, 1.0);
                if (this.pupfish.m_20238_(vec) < 1.15F) {
                    this.pupfish.f_19804_.set(EntityDevilsHolePupfish.FEEDING_POS, Optional.of(this.destinationBlock));
                    Vec3 face = vec.subtract(this.pupfish.m_20182_());
                    this.pupfish.m_20256_(this.pupfish.m_20184_().add(face.normalize().scale(0.1F)));
                    this.pupfish.setFeedingTime(this.pupfish.getFeedingTime() + 1);
                    if (this.pupfish.getFeedingTime() > this.maxFeedTime) {
                        this.destinationBlock = null;
                        if (EntityDevilsHolePupfish.this.f_19796_.nextInt(3) == 0) {
                            List<ItemStack> lootList = EntityDevilsHolePupfish.getFoodLoot(this.pupfish);
                            if (!lootList.isEmpty()) {
                                for (ItemStack stack : lootList) {
                                    ItemEntity e = this.pupfish.m_19983_(stack.copy());
                                    e.f_19812_ = true;
                                    e.m_20256_(e.m_20184_().multiply(0.2, 0.2, 0.2));
                                }
                            }
                        }
                        if (EntityDevilsHolePupfish.this.f_19796_.nextInt(3) == 0 && !this.pupfish.isBaby()) {
                            this.pupfish.breedNextChase = true;
                        }
                    }
                } else {
                    this.pupfish.f_19804_.set(EntityDevilsHolePupfish.FEEDING_POS, Optional.empty());
                }
            }
        }

        @Override
        public void stop() {
            this.pupfish.f_19804_.set(EntityDevilsHolePupfish.FEEDING_POS, Optional.empty());
            this.destinationBlock = null;
            this.pupfish.setFeedingTime(0);
        }

        protected boolean searchForDestination() {
            int lvt_1_1_ = this.searchLength;
            BlockPos lvt_3_1_ = this.pupfish.m_20183_();
            BlockPos.MutableBlockPos lvt_4_1_ = new BlockPos.MutableBlockPos();
            for (int lvt_5_1_ = -8; lvt_5_1_ <= 2; lvt_5_1_++) {
                for (int lvt_6_1_ = 0; lvt_6_1_ < lvt_1_1_; lvt_6_1_++) {
                    for (int lvt_7_1_ = 0; lvt_7_1_ <= lvt_6_1_; lvt_7_1_ = lvt_7_1_ > 0 ? -lvt_7_1_ : 1 - lvt_7_1_) {
                        for (int lvt_8_1_ = lvt_7_1_ < lvt_6_1_ && lvt_7_1_ > -lvt_6_1_ ? lvt_6_1_ : 0; lvt_8_1_ <= lvt_6_1_; lvt_8_1_ = lvt_8_1_ > 0 ? -lvt_8_1_ : 1 - lvt_8_1_) {
                            lvt_4_1_.setWithOffset(lvt_3_1_, lvt_7_1_, lvt_5_1_ - 1, lvt_8_1_);
                            if (this.isMossBlock(this.pupfish.m_9236_(), lvt_4_1_) && this.pupfish.canSeeBlock(lvt_4_1_)) {
                                this.destinationBlock = lvt_4_1_;
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        private boolean isMossBlock(Level world, BlockPos.MutableBlockPos pos) {
            return world.getBlockState(pos).m_204336_(AMTagRegistry.PUPFISH_EATABLES);
        }
    }
}