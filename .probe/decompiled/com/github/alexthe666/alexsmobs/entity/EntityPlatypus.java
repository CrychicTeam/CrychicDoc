package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockReptileEgg;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.PlatypusAIDigForItems;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityPlatypus extends Animal implements ISemiAquatic, ITargetsDroppedItems, Bucketable {

    private static final EntityDataAccessor<Boolean> SENSING = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SENSING_VISUAL = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FEDORA = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityPlatypus.class, EntityDataSerializers.BOOLEAN);

    public float prevInWaterProgress;

    public float inWaterProgress;

    public float prevDigProgress;

    public float digProgress;

    public boolean superCharged = false;

    private boolean isLandNavigator;

    private int swimTimer = -1000;

    protected EntityPlatypus(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(false);
    }

    public static boolean canPlatypusSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.PLATYPUS_SPAWNS);
        return (worldIn.m_8055_(pos.below()).m_60734_() == Blocks.DIRT || spawnBlock) && pos.m_123342_() < worldIn.m_5736_() + 4;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.platypusSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 16.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == AMItemRegistry.LOBSTER_TAIL.get() || item == AMItemRegistry.COOKED_LOBSTER_TAIL.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.PLATYPUS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.PLATYPUS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.PLATYPUS_HURT.get();
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.PLATYPUS_BUCKET.get());
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
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        CompoundTag compound = bucket.getOrCreateTag();
        compound.put("PlatypusData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("PlatypusData")) {
            this.readAdditionalSaveData(compound.getCompound("PlatypusData"));
        }
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        boolean redstone = itemstack.getItem() == Items.REDSTONE || itemstack.getItem() == Items.REDSTONE_BLOCK;
        if (itemstack.getItem() == AMItemRegistry.FEDORA.get() && !this.hasFedora()) {
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setFedora(true);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if (redstone && !this.isSensing()) {
            this.superCharged = itemstack.getItem() == Items.REDSTONE_BLOCK;
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.setSensing(true);
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreathAirGoal(this));
        this.f_21345_.addGoal(1, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(1, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(2, new EntityPlatypus.MateGoal(this, 1.0));
        this.f_21345_.addGoal(2, new EntityPlatypus.LayEggGoal(this, 1.0));
        this.f_21345_.addGoal(2, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(3, new PanicGoal(this, 1.1));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(Items.REDSTONE, Items.REDSTONE_BLOCK), false) {

            @Override
            public void start() {
                super.start();
                EntityPlatypus.this.setSensingVisual(true);
            }

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityPlatypus.this.isSensing();
            }

            @Override
            public void stop() {
                super.stop();
                EntityPlatypus.this.setSensingVisual(false);
            }
        });
        this.f_21345_.addGoal(5, new TemptGoal(this, 1.1, Ingredient.of(AMTagRegistry.PLATYPUS_FOODSTUFFS), false) {

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityPlatypus.this.isSensing();
            }
        });
        this.f_21345_.addGoal(5, new PlatypusAIDigForItems(this));
        this.f_21345_.addGoal(6, new SemiAquaticAIRandomSwimming(this, 1.0, 30));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false, false, 40, 15) {

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityPlatypus.this.isSensing();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !EntityPlatypus.this.isSensing();
            }
        });
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.hurt(source, amount);
        if (prev && source.getDirectEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) source.getDirectEntity();
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100));
        }
        return prev;
    }

    public boolean isPerry() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("perry");
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        return this.getMaxAirSupply();
    }

    public void spawnGroundEffects() {
        float radius = 0.3F;
        for (int i1 = 0; i1 < 3; i1++) {
            double motionX = this.m_217043_().nextGaussian() * 0.07;
            double motionY = this.m_217043_().nextGaussian() * 0.07;
            double motionZ = this.m_217043_().nextGaussian() * 0.07;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraY = 0.8F;
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos ground = this.m_20099_();
            BlockState state = this.m_9236_().getBlockState(ground);
            if (state.m_280296_() && this.m_9236_().isClientSide) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, this.m_20185_() + extraX, (double) ground.m_123342_() + extraY, this.m_20189_() + extraZ, motionX, motionY, motionZ);
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.getMaxAirSupply());
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_21515_() && this.m_20069_()) {
            this.m_19920_(this.m_6113_(), travelVector);
            this.m_6478_(MoverType.SELF, this.m_20184_());
            this.m_20256_(this.m_20184_().scale(0.9));
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DIGGING, false);
        this.f_19804_.define(SENSING, false);
        this.f_19804_.define(SENSING_VISUAL, false);
        this.f_19804_.define(FEDORA, false);
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(HAS_EGG, false);
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.hasFedora()) {
            this.m_19998_(AMItemRegistry.FEDORA.get());
        }
    }

    public boolean isSensing() {
        return this.f_19804_.get(SENSING);
    }

    public void setSensing(boolean sensing) {
        this.f_19804_.set(SENSING, sensing);
    }

    public boolean isSensingVisual() {
        return this.f_19804_.get(SENSING_VISUAL);
    }

    public void setSensingVisual(boolean sensing) {
        this.f_19804_.set(SENSING_VISUAL, sensing);
    }

    public boolean hasFedora() {
        return this.f_19804_.get(FEDORA);
    }

    public void setFedora(boolean sensing) {
        this.f_19804_.set(FEDORA, sensing);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Fedora", this.hasFedora());
        compound.putBoolean("Sensing", this.isSensing());
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFedora(compound.getBoolean("Fedora"));
        this.setSensing(compound.getBoolean("Sensing"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.setHasEgg(compound.getBoolean("HasEgg"));
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
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.fromBucket() || this.m_8077_();
    }

    @Override
    public boolean removeWhenFarAway(double dist) {
        return !this.fromBucket() && !this.requiresCustomPersistence();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevInWaterProgress = this.inWaterProgress;
        this.prevDigProgress = this.digProgress;
        boolean dig = this.isDigging() && this.m_20072_();
        if (dig && this.digProgress < 5.0F) {
            this.digProgress++;
        }
        if (!dig && this.digProgress > 0.0F) {
            this.digProgress--;
        }
        if (this.m_20072_()) {
            if (this.inWaterProgress < 5.0F) {
                this.inWaterProgress++;
            }
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else {
            if (this.inWaterProgress > 0.0F) {
                this.inWaterProgress--;
            }
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (this.m_20096_() && this.isDigging()) {
            this.spawnGroundEffects();
        }
        if (this.inWaterProgress > 0.0F) {
            this.m_274367_(1.0F);
        } else {
            this.m_274367_(0.6F);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20069_()) {
                this.swimTimer++;
            } else {
                this.swimTimer--;
            }
        }
        if (this.m_6084_() && (this.isSensing() || this.isSensingVisual())) {
            for (int j = 0; j < 2; j++) {
                float radius = this.m_20205_() * 0.65F;
                float angle = (float) (Math.PI / 180.0) * this.f_20883_;
                double extraX = (double) (radius * (1.5F + this.f_19796_.nextFloat() * 0.3F) * Mth.sin((float) Math.PI + angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().x * 2.0;
                double extraZ = (double) (radius * (1.5F + this.f_19796_.nextFloat() * 0.3F) * Mth.cos(angle) + (this.f_19796_.nextFloat() - 0.5F)) + this.m_20184_().z * 2.0;
                double actualX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double actualZ = (double) (radius * Mth.cos(angle));
                double motX = actualX - extraX;
                double motZ = actualZ - extraZ;
                this.m_9236_().addParticle(AMParticleRegistry.PLATYPUS_SENSE.get(), this.m_20185_() + extraX, (double) (this.m_20206_() * 0.3F) + this.m_20186_(), this.m_20189_() + extraZ, motX * 0.1F, 0.0, motZ * 0.1F);
            }
        }
    }

    public boolean isDigging() {
        return this.f_19804_.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.f_19804_.set(DIGGING, digging);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink(this, 1.2F, 1.6F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public boolean shouldEnterWater() {
        return (this.m_21188_() != null || this.swimTimer <= -1000 || this.isSensing()) && !this.hasEgg();
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 600 && !this.isSensing() || this.hasEgg();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isDigging();
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.PLATYPUS.get().create(serverWorld);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return !this.isSensing() && stack.is(AMTagRegistry.PLATYPUS_FOODSTUFFS);
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
        if (e.getItem().getItem() != Items.REDSTONE && e.getItem().getItem() != Items.REDSTONE_BLOCK) {
            this.m_5634_(6.0F);
        } else {
            this.superCharged = e.getItem().getItem() == Items.REDSTONE_BLOCK;
            this.setSensing(true);
        }
    }

    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    private void setHasEgg(boolean hasEgg) {
        this.f_19804_.set(HAS_EGG, hasEgg);
    }

    static class LayEggGoal extends MoveToBlockGoal {

        private final EntityPlatypus turtle;

        private int digTime;

        LayEggGoal(EntityPlatypus turtle, double speedIn) {
            super(turtle, speedIn, 16);
            this.turtle = turtle;
        }

        @Override
        public void stop() {
            this.digTime = 0;
        }

        @Override
        public boolean canUse() {
            return this.turtle.hasEgg() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.turtle.hasEgg();
        }

        @Override
        public double acceptedDistance() {
            return (double) this.turtle.m_20205_() + 0.5;
        }

        @Override
        public void tick() {
            super.tick();
            if (!this.turtle.m_20069_() && this.m_25625_()) {
                BlockPos blockpos = this.turtle.m_20183_();
                Level world = this.turtle.m_9236_();
                this.turtle.m_146850_(GameEvent.BLOCK_PLACE);
                world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                world.setBlock(this.f_25602_.above(), (BlockState) AMBlockRegistry.PLATYPUS_EGG.get().defaultBlockState().m_61124_(BlockReptileEgg.EGGS, this.turtle.f_19796_.nextInt(3) + 1), 3);
                this.turtle.setHasEgg(false);
                this.turtle.setDigging(false);
                this.turtle.m_27601_(600);
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return worldIn.isEmptyBlock(pos.above()) && BlockReptileEgg.isProperHabitat(worldIn, pos);
        }
    }

    static class MateGoal extends BreedGoal {

        private final EntityPlatypus platypus;

        MateGoal(EntityPlatypus platypus, double speedIn) {
            super(platypus, speedIn);
            this.platypus = platypus;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.platypus.hasEgg();
        }

        @Override
        protected void breed() {
            ServerPlayer serverplayerentity = this.f_25113_.getLoveCause();
            if (serverplayerentity == null && this.f_25115_.getLoveCause() != null) {
                serverplayerentity = this.f_25115_.getLoveCause();
            }
            if (serverplayerentity != null) {
                serverplayerentity.m_36220_(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.f_25113_, this.f_25115_, this.f_25113_);
            }
            this.platypus.setHasEgg(true);
            this.f_25113_.resetLove();
            this.f_25115_.resetLove();
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                RandomSource random = this.f_25113_.m_217043_();
                this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), random.nextInt(7) + 1));
            }
        }
    }
}