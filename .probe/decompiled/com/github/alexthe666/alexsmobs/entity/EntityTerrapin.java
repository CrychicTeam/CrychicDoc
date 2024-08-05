package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockTerrapinEgg;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.util.TerrapinTypes;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityTerrapinEgg;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
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
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityTerrapin extends Animal implements ISemiAquatic, Bucketable {

    private static final EntityDataAccessor<Integer> TURTLE_TYPE = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SHELL_TYPE = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SKIN_TYPE = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TURTLE_COLOR = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SHELL_COLOR = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> SKIN_COLOR = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> RETREATED = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SPINNING = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityTerrapin.class, EntityDataSerializers.BOOLEAN);

    public float clientSpin = 0.0F;

    public int spinCounter = 0;

    public float prevSwimProgress;

    public float swimProgress;

    public float prevRetreatProgress;

    public float retreatProgress;

    public float prevSpinProgress;

    public float spinProgress;

    private int maxRollTime = 50;

    private boolean isLandNavigator;

    private int swimTimer = -1000;

    private int hideInShellTimer = 0;

    private Vec3 spinDelta;

    private float spinYRot;

    private int changeSpinAngleCooldown = 0;

    private LivingEntity lastLauncher = null;

    private TileEntityTerrapinEgg.ParentData partnerData;

    protected EntityTerrapin(EntityType animal, Level level) {
        super(animal, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.ARMOR, 10.0).add(Attributes.MOVEMENT_SPEED, 0.1F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.TERRAPIN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.TERRAPIN_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.terrapinSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canTerrapinSpawn(EntityType<EntityTerrapin> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return reason == MobSpawnType.SPAWNER || iServerWorld.m_8055_(pos).m_60819_().is(Fluids.WATER);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BreathAirGoal(this));
        this.f_21345_.addGoal(1, new EntityTerrapin.MateGoal(this, 1.0));
        this.f_21345_.addGoal(1, new EntityTerrapin.LayEggGoal(this, 1.0));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.1, Ingredient.of(Items.SEAGRASS), false));
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new SemiAquaticAIRandomSwimming(this, 1.0, 30));
        this.f_21345_.addGoal(6, new PanicGoal(this, 1.1));
        this.f_21345_.addGoal(7, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSwimProgress = this.swimProgress;
        this.prevRetreatProgress = this.retreatProgress;
        this.prevSpinProgress = this.spinProgress;
        boolean inWaterOrBubble = this.m_20072_();
        boolean spinning = this.isSpinning();
        boolean retreated = this.hasRetreated();
        if (inWaterOrBubble) {
            if (this.swimProgress < 5.0F) {
                this.swimProgress++;
            }
        } else if (this.swimProgress > 0.0F) {
            this.swimProgress--;
        }
        if (spinning) {
            if (this.spinProgress < 5.0F) {
                this.spinProgress++;
            }
        } else if (this.spinProgress > 0.0F) {
            this.spinProgress--;
        }
        if (retreated) {
            if (this.retreatProgress < 5.0F) {
                this.retreatProgress++;
            }
        } else if (this.retreatProgress > 0.0F) {
            this.retreatProgress--;
        }
        if (spinning) {
            this.handleSpin();
            if (this.m_6084_() && this.spinCounter > 5 && !this.m_6162_()) {
                for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(0.3F))) {
                    if (!this.m_7307_(entity) && !(entity instanceof EntityTerrapin)) {
                        entity.hurt(this.m_269291_().mobAttack((LivingEntity) (this.lastLauncher == null ? this : this.lastLauncher)), 4.0F + this.f_19796_.nextFloat() * 4.0F);
                    }
                }
            }
            if (!this.m_6084_()) {
                this.setSpinning(false);
            }
            if (this.f_19862_) {
                if (this.changeSpinAngleCooldown == 0) {
                    this.changeSpinAngleCooldown = 10;
                    float f = this.collideDirectionAndSound().getAxis() == Direction.Axis.X ? this.spinYRot - 180.0F : 180.0F - this.spinYRot;
                    f += (float) (this.f_19796_.nextInt(40) - 20);
                    this.m_146922_(f);
                    this.copySpinDelta(f, Vec3.ZERO);
                } else {
                    this.maxRollTime -= 30;
                }
            }
            if (this.changeSpinAngleCooldown > 0) {
                this.changeSpinAngleCooldown--;
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20072_() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.m_20072_() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.m_20069_()) {
                this.swimTimer = Math.max(0, this.swimTimer + 1);
            } else {
                this.swimTimer = Math.min(0, this.swimTimer - 1);
                for (Player player : this.m_9236_().m_45976_(Player.class, this.m_20191_().inflate(0.0, 0.15F, 0.0))) {
                    if ((player.f_20899_ || !player.m_20096_()) && player.m_20186_() > this.m_20188_()) {
                        if (!this.hasRetreated()) {
                            this.hideInShellTimer = this.hideInShellTimer + 40 + this.f_19796_.nextInt(40);
                        } else if (!this.isSpinning()) {
                            this.lastLauncher = player;
                            int spin = 100 + this.f_19796_.nextInt(100);
                            this.hideInShellTimer = spin;
                            this.m_146922_(player.m_6080_());
                            this.spinFor(spin);
                        }
                    }
                }
            }
            if (this.swimProgress > 0.0F) {
                this.m_274367_(1.0F);
            } else {
                this.m_274367_(0.6F);
            }
            if (this.hideInShellTimer > 0) {
                this.hideInShellTimer--;
            }
            this.setRetreated(this.hideInShellTimer > 0 && !this.isSpinning());
        }
    }

    private Direction collideDirectionAndSound() {
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, entity -> false);
        if (raytraceresult instanceof BlockHitResult) {
            BlockState state = this.m_9236_().getBlockState(((BlockHitResult) raytraceresult).getBlockPos());
            if (state != null && !this.m_20067_()) {
            }
            return ((BlockHitResult) raytraceresult).getDirection();
        } else {
            return Direction.DOWN;
        }
    }

    private boolean isMoving() {
        return this.m_20184_().lengthSqr() > 0.02;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigation(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink(this, 2.5F, 1.15F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(TURTLE_TYPE, 0);
        this.f_19804_.define(SHELL_TYPE, 0);
        this.f_19804_.define(SKIN_TYPE, 0);
        this.f_19804_.define(SHELL_COLOR, 0);
        this.f_19804_.define(SKIN_COLOR, 0);
        this.f_19804_.define(TURTLE_COLOR, 0);
        this.f_19804_.define(RETREATED, false);
        this.f_19804_.define(SPINNING, false);
        this.f_19804_.define(HAS_EGG, false);
        this.f_19804_.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("TurtleType", this.getTurtleTypeOrdinal());
        compound.putInt("ShellType", this.getShellType());
        compound.putInt("SkinType", this.getSkinType());
        compound.putInt("TurtleColor", this.getTurtleColor());
        compound.putInt("ShellColor", this.getShellColor());
        compound.putInt("SkinColor", this.getSkinColor());
        compound.putBoolean("HasEgg", this.hasEgg());
        compound.putBoolean("Bucketed", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTurtleTypeOrdinal(compound.getInt("TurtleType"));
        this.setShellType(compound.getInt("ShellType"));
        this.setSkinType(compound.getInt("SkinType"));
        this.setTurtleColor(compound.getInt("TurtleColor"));
        this.setShellColor(compound.getInt("ShellColor"));
        this.setSkinColor(compound.getInt("SkinColor"));
        this.setHasEgg(compound.getBoolean("HasEgg"));
        this.setFromBucket(compound.getBoolean("Bucketed"));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.isSpinning()) {
            super.m_7355_(pos, state);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Blocks.SEAGRASS.asItem();
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
    public boolean removeWhenFarAway(double d) {
        return !this.fromBucket() && !this.m_8077_();
    }

    private int getTurtleTypeOrdinal() {
        return Mth.clamp(this.f_19804_.get(TURTLE_TYPE), 0, TerrapinTypes.values().length - 1);
    }

    private void setTurtleTypeOrdinal(int i) {
        this.f_19804_.set(TURTLE_TYPE, i);
    }

    public int getShellType() {
        return this.f_19804_.get(SHELL_TYPE);
    }

    public void setShellType(int i) {
        this.f_19804_.set(SHELL_TYPE, i);
    }

    public int getSkinType() {
        return this.f_19804_.get(SKIN_TYPE);
    }

    public void setSkinType(int i) {
        this.f_19804_.set(SKIN_TYPE, i);
    }

    public int getShellColor() {
        return this.f_19804_.get(SHELL_COLOR);
    }

    public void setShellColor(int i) {
        this.f_19804_.set(SHELL_COLOR, i);
    }

    public int getSkinColor() {
        return this.f_19804_.get(SKIN_COLOR);
    }

    public void setSkinColor(int i) {
        this.f_19804_.set(SKIN_COLOR, i);
    }

    public int getTurtleColor() {
        return this.f_19804_.get(TURTLE_COLOR);
    }

    public void setTurtleColor(int i) {
        this.f_19804_.set(TURTLE_COLOR, i);
    }

    public TerrapinTypes getTurtleType() {
        return TerrapinTypes.values()[this.getTurtleTypeOrdinal()];
    }

    public void setTurtleType(TerrapinTypes type) {
        this.setTurtleTypeOrdinal(type.ordinal());
    }

    public boolean isSpinning() {
        return this.f_19804_.get(SPINNING);
    }

    public void setSpinning(boolean b) {
        this.f_19804_.set(SPINNING, b);
    }

    public boolean hasRetreated() {
        return this.f_19804_.get(RETREATED);
    }

    public void setRetreated(boolean b) {
        this.f_19804_.set(RETREATED, b);
    }

    public boolean hasEgg() {
        return this.f_19804_.get(HAS_EGG);
    }

    private void setHasEgg(boolean hasEgg) {
        this.f_19804_.set(HAS_EGG, hasEgg);
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int currentAir) {
        return this.getMaxAirSupply();
    }

    @Override
    public void push(Entity entity) {
        if (!this.m_20072_() && !(entity instanceof EntityTerrapin)) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(this.m_20184_()));
        } else {
            super.m_7334_(entity);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.m_20072_() ? super.m_5829_() : this.m_6084_();
    }

    private void spinFor(int time) {
        this.maxRollTime = time;
        this.setSpinning(true);
    }

    private void copySpinDelta(float spinRot, Vec3 motionIn) {
        float f = spinRot * (float) (Math.PI / 180.0);
        float f1 = this.m_6162_() ? 0.3F : 0.5F;
        this.spinYRot = spinRot;
        this.spinDelta = new Vec3(motionIn.x + (double) (-Mth.sin(f) * f1), 0.0, motionIn.z + (double) (Mth.cos(f) * f1));
        this.m_20256_(this.spinDelta.add(0.0, 0.0, 0.0));
    }

    private void handleSpin() {
        this.setRetreated(true);
        this.spinCounter++;
        if (!this.m_9236_().isClientSide) {
            if (this.spinCounter > this.maxRollTime) {
                this.setSpinning(false);
                this.hideInShellTimer = 10 + this.f_19796_.nextInt(30);
                this.spinCounter = 0;
            } else {
                Vec3 vec3 = this.m_20184_();
                if (this.spinCounter == 1) {
                    this.copySpinDelta(this.m_146908_(), vec3);
                } else {
                    this.m_146922_(this.spinYRot);
                    this.m_5616_(this.spinYRot);
                    this.m_5618_(this.spinYRot);
                    this.m_20334_(this.spinDelta.x, vec3.y, this.spinDelta.z);
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.m_20301_(this.getMaxAirSupply());
        this.setTurtleType(TerrapinTypes.getRandomType(this.f_19796_));
        this.setShellType(this.f_19796_.nextInt(7));
        this.setSkinType(this.f_19796_.nextInt(4));
        this.setTurtleColor(TerrapinTypes.generateRandomColor(this.f_19796_));
        this.setShellColor(TerrapinTypes.generateRandomColor(this.f_19796_));
        this.setSkinColor(TerrapinTypes.generateRandomColor(this.f_19796_));
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return AMEntityRegistry.TERRAPIN.get().create(serverLevel0);
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isSpinning() || this.hasRetreated();
    }

    @Override
    public boolean shouldEnterWater() {
        return this.m_5448_() == null && !this.shouldLeaveWater() && this.swimTimer <= -1000;
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 600 || this.hasEgg();
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.shouldStopMoving()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
            super.m_7023_(travelVector);
        } else {
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
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.TERRAPIN_BUCKET.get());
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
        compound.put("TerrapinData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("TerrapinData")) {
            this.readAdditionalSaveData(compound.getCompound("TerrapinData"));
        }
    }

    @Nonnull
    @Override
    public InteractionResult mobInteract(@Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.getItem() == Items.SEAGRASS) {
            this.m_21530_();
        }
        return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * (this.isSpinning() ? 4.0F : 32.0F), 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public boolean isKoopa() {
        String s = ChatFormatting.stripFormatting(this.m_7755_().getString());
        return s != null && s.toLowerCase().contains("koopa");
    }

    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    static class LayEggGoal extends MoveToBlockGoal {

        private final EntityTerrapin turtle;

        private int digTime;

        LayEggGoal(EntityTerrapin turtle, double speedIn) {
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
            BlockPos blockpos = this.turtle.m_20183_();
            this.turtle.swimTimer = 1000;
            if (!this.turtle.m_20069_() && this.m_25625_()) {
                Level world = this.turtle.m_9236_();
                this.turtle.m_146850_(GameEvent.BLOCK_PLACE);
                world.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                world.setBlock(this.f_25602_.above(), (BlockState) AMBlockRegistry.TERRAPIN_EGG.get().defaultBlockState().m_61124_(BlockTerrapinEgg.EGGS, this.turtle.f_19796_.nextInt(1) + 3), 3);
                if (world.getBlockEntity(this.f_25602_.above()) instanceof TileEntityTerrapinEgg eggTe) {
                    eggTe.parent1 = new TileEntityTerrapinEgg.ParentData(this.turtle.getTurtleType(), this.turtle.getShellType(), this.turtle.getSkinType(), this.turtle.getTurtleColor(), this.turtle.getShellColor(), this.turtle.getSkinColor());
                    eggTe.parent2 = this.turtle.partnerData == null ? eggTe.parent1 : this.turtle.partnerData;
                }
                this.turtle.setHasEgg(false);
                this.turtle.m_27601_(600);
            }
        }

        @Override
        protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
            return worldIn.isEmptyBlock(pos.above()) && BlockTerrapinEgg.isProperHabitat(worldIn, pos);
        }
    }

    static class MateGoal extends BreedGoal {

        private final EntityTerrapin turtle;

        MateGoal(EntityTerrapin turtle, double speedIn) {
            super(turtle, speedIn);
            this.turtle = turtle;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.turtle.hasEgg();
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
            if (this.f_25115_ instanceof EntityTerrapin terrapin) {
                this.turtle.partnerData = new TileEntityTerrapinEgg.ParentData(terrapin.getTurtleType(), terrapin.getShellType(), terrapin.getSkinType(), terrapin.getTurtleColor(), terrapin.getShellColor(), terrapin.getSkinColor());
            }
            this.turtle.setHasEgg(true);
            this.f_25113_.resetLove();
            this.f_25115_.resetLove();
            this.f_25113_.m_146762_(6000);
            this.f_25115_.m_146762_(6000);
            RandomSource random = this.f_25113_.m_217043_();
            if (this.f_25114_.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.f_25114_.m_7967_(new ExperienceOrb(this.f_25114_, this.f_25113_.m_20185_(), this.f_25113_.m_20186_(), this.f_25113_.m_20189_(), random.nextInt(7) + 1));
            }
        }
    }
}