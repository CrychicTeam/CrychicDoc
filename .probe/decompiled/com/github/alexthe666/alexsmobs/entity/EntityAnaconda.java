package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.util.AnacondaPartIndex;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
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
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;

public class EntityAnaconda extends Animal implements ISemiAquatic {

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityAnaconda.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> CHILD_ID = SynchedEntityData.defineId(EntityAnaconda.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> STRANGLING = SynchedEntityData.defineId(EntityAnaconda.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> YELLOW = SynchedEntityData.defineId(EntityAnaconda.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SHEDTIME = SynchedEntityData.defineId(EntityAnaconda.class, EntityDataSerializers.INT);

    public final float[] ringBuffer = new float[64];

    public int ringBufferIndex = -1;

    private EntityAnacondaPart[] parts;

    private float prevStrangleProgress = 0.0F;

    private float strangleProgress = 0.0F;

    private int strangleTimer = 0;

    private int shedCooldown = 0;

    private int feedings = 0;

    private boolean isLandNavigator;

    private int swimTimer = -1000;

    private int passiveFor = 0;

    protected EntityAnaconda(EntityType t, Level world) {
        super(t, world);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(true);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ANACONDA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ANACONDA_HURT.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(AMSoundRegistry.ANACONDA_SLITHER.get(), 1.0F, 1.0F);
        } else {
            super.m_7355_(pos, state);
        }
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    public static boolean canAnacondaSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        boolean spawnBlock = worldIn.m_8055_(pos.below()).m_204336_(AMTagRegistry.ANACONDA_SPAWNS);
        return spawnBlock && pos.m_123342_() < worldIn.m_5736_() + 4;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.anacondaSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new AnimalSwimMoveControllerSink(this, 1.3F, 1.0F);
            this.f_21344_ = new SemiAquaticPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(2, new EntityAnaconda.AIMelee());
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.25, Ingredient.of(Items.CHICKEN, Items.COOKED_CHICKEN), false));
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 60, 1.0, 14, 7));
        this.f_21345_.addGoal(8, new SemiAquaticAIRandomSwimming(this, 1.5, 7));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 25.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new NearestAttackableTargetGoal(this, LivingEntity.class, 200, false, false, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.ANACONDA_TARGETS)));
        this.f_21346_.addGoal(2, new EntityAINearestTarget3D(this, Player.class, 110, false, true, null) {

            @Override
            public boolean canUse() {
                return !EntityAnaconda.this.m_6162_() && EntityAnaconda.this.passiveFor == 0 && EntityAnaconda.this.m_9236_().m_46791_() != Difficulty.PEACEFUL && !EntityAnaconda.this.m_27593_() && super.m_8036_();
            }
        });
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return this.m_6162_() ? 0.15F : 0.3F;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (this.isFood(itemstack)) {
            this.m_6710_(null);
            this.passiveFor = 3600 + this.f_19796_.nextInt(3600);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
        this.feedings = compound.getInt("Feedings");
        this.setSheddingTime(compound.getInt("ShedTime"));
        this.setYellow(compound.getBoolean("Yellow"));
        this.shedCooldown = compound.getInt("ShedCooldown");
        this.passiveFor = compound.getInt("PassiveFor");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
        compound.putInt("Feedings", this.feedings);
        compound.putInt("ShedTime", this.getSheddingTime());
        compound.putBoolean("Yellow", this.isYellow());
        compound.putInt("ShedCooldown", this.shedCooldown);
        compound.putInt("PassiveFor", this.passiveFor);
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        entities.stream().filter(entity -> !(entity instanceof EntityAnacondaPart) && entity.isPushable()).forEach(entity -> entity.push(this));
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(CHILD_ID, -1);
        this.f_19804_.define(STRANGLING, false);
        this.f_19804_.define(YELLOW, false);
        this.f_19804_.define(SHEDTIME, 0);
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    public int getSheddingTime() {
        return this.f_19804_.get(SHEDTIME);
    }

    public void setSheddingTime(int shedtime) {
        this.f_19804_.set(SHEDTIME, shedtime);
    }

    public boolean isStrangling() {
        return this.f_19804_.get(STRANGLING);
    }

    public void setStrangling(boolean running) {
        this.f_19804_.set(STRANGLING, running);
    }

    public boolean isYellow() {
        return this.f_19804_.get(YELLOW);
    }

    public void setYellow(boolean yellow) {
        this.f_19804_.set(YELLOW, yellow);
    }

    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 3;
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.m_45784_(this);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.passiveFor > 0) {
            this.passiveFor--;
        }
        if (this.m_20069_()) {
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
        } else if (!this.isLandNavigator) {
            this.switchNavigator(true);
        }
        this.prevStrangleProgress = this.strangleProgress;
        if (this.isStrangling()) {
            if (this.strangleProgress < 5.0F) {
                this.strangleProgress++;
            }
        } else if (this.strangleProgress > 0.0F) {
            this.strangleProgress--;
        }
        this.f_20883_ = this.m_146908_();
        this.f_20885_ = Mth.clamp(this.f_20885_, this.f_20883_ - 70.0F, this.f_20883_ + 70.0F);
        if (this.isStrangling()) {
            if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.m_5448_().isAlive()) {
                this.m_146926_(0.0F);
                LivingEntity target = this.m_5448_();
                float radius = this.m_5448_().m_20205_() * -0.5F;
                float angle = (float) (Math.PI / 180.0) * (target.yBodyRot - 45.0F);
                double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                double extraZ = (double) (radius * Mth.cos(angle));
                Vec3 targetVec = new Vec3(extraX + target.m_20185_(), target.m_20227_(1.0), extraZ + target.m_20189_());
                Vec3 moveVec = targetVec.subtract(this.m_20182_()).scale(1.0);
                this.m_20256_(moveVec);
                if (!target.m_20096_()) {
                    target.m_20256_(new Vec3(0.0, -0.08F, 0.0));
                } else {
                    target.m_20256_(Vec3.ZERO);
                }
                if (this.strangleTimer >= 40 && this.strangleTimer % 20 == 0) {
                    double health = (double) Mth.clamp(this.m_5448_().getMaxHealth(), 4.0F, 50.0F);
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) Math.max(4.0, 0.25 * health));
                }
                if (this.m_5448_() == null || !this.m_5448_().isAlive()) {
                    this.strangleTimer = 0;
                    this.setStrangling(false);
                }
            }
            this.f_19789_ = 0.0F;
            this.strangleTimer++;
            this.m_20242_(true);
        } else {
            this.m_20242_(false);
        }
        if (this.ringBufferIndex < 0) {
            for (int i = 0; i < this.ringBuffer.length; i++) {
                this.ringBuffer[i] = this.m_146908_();
            }
        }
        this.ringBufferIndex++;
        if (this.ringBufferIndex == this.ringBuffer.length) {
            this.ringBufferIndex = 0;
        }
        this.ringBuffer[this.ringBufferIndex] = this.m_146908_();
        if (!this.m_9236_().isClientSide) {
            int segments = 7;
            Entity child = this.getChild();
            if (child == null) {
                LivingEntity partParent = this;
                this.parts = new EntityAnacondaPart[7];
                AnacondaPartIndex partIndex = AnacondaPartIndex.HEAD;
                Vec3 prevPos = this.m_20182_();
                for (int i = 0; i < 7; i++) {
                    float prevReqRot = this.calcPartRotation(i) + this.getYawForPart(i);
                    float reqRot = this.calcPartRotation(i + 1) + this.getYawForPart(i);
                    EntityAnacondaPart part = new EntityAnacondaPart(AMEntityRegistry.ANACONDA_PART.get(), this);
                    part.setParent(partParent);
                    part.copyDataFrom(this);
                    part.setBodyIndex(i);
                    part.setPartType(AnacondaPartIndex.sizeAt(1 + i));
                    if (partParent == this) {
                        this.setChildId(part.m_20148_());
                        this.f_19804_.set(CHILD_ID, part.m_19879_());
                    }
                    if (partParent instanceof EntityAnacondaPart) {
                        ((EntityAnacondaPart) partParent).setChildId(part.m_20148_());
                    }
                    part.m_146884_(part.tickMultipartPosition(this.m_19879_(), partIndex, prevPos, this.m_146909_(), prevReqRot, reqRot, false));
                    partParent = part;
                    this.m_9236_().m_7967_(part);
                    this.parts[i] = part;
                    partIndex = part.getPartType();
                    prevPos = part.m_20182_();
                }
            }
            if (this.shouldReplaceParts() && this.getChild() instanceof EntityAnacondaPart) {
                this.parts = new EntityAnacondaPart[7];
                this.parts[0] = (EntityAnacondaPart) this.getChild();
                this.f_19804_.set(CHILD_ID, this.parts[0].m_19879_());
                for (int i = 1; i < this.parts.length && this.parts[i - 1].getChild() instanceof EntityAnacondaPart; i++) {
                    this.parts[i] = (EntityAnacondaPart) this.parts[i - 1].getChild();
                }
            }
            AnacondaPartIndex partIndex = AnacondaPartIndex.HEAD;
            Vec3 prev = this.m_20182_();
            float xRot = this.m_146909_();
            for (int i = 0; i < 7; i++) {
                if (this.parts[i] != null) {
                    float prevReqRotx = this.calcPartRotation(i) + this.getYawForPart(i);
                    float reqRotx = this.calcPartRotation(i + 1) + this.getYawForPart(i);
                    this.parts[i].setStrangleProgress(this.strangleProgress);
                    this.parts[i].copyDataFrom(this);
                    prev = this.parts[i].tickMultipartPosition(this.m_19879_(), partIndex, prev, xRot, prevReqRotx, reqRotx, true);
                    partIndex = this.parts[i].getPartType();
                    xRot = this.parts[i].m_146909_();
                }
            }
            if (this.m_20069_()) {
                this.swimTimer = Math.max(this.swimTimer + 1, 0);
            } else {
                this.swimTimer = Math.min(this.swimTimer - 1, 0);
            }
        }
        if (this.shedCooldown > 0) {
            this.shedCooldown--;
        }
        if (this.getSheddingTime() > 0) {
            this.setSheddingTime(this.getSheddingTime() - 1);
            if (this.getSheddingTime() == 0) {
                this.spawnItemAtOffset(new ItemStack(AMItemRegistry.SHED_SNAKE_SKIN.get()), 1.0F + this.f_19796_.nextFloat(), 0.2F);
                this.shedCooldown = 1000 + this.f_19796_.nextInt(2000);
            }
        }
    }

    private boolean shouldReplaceParts() {
        if (this.parts != null && this.parts[0] != null) {
            for (int i = 0; i < 7; i++) {
                if (this.parts[i] == null) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    private float getYawForPart(int i) {
        return this.getRingBuffer(4 + i * 2, 1.0F);
    }

    public float getRingBuffer(int bufferOffset, float partialTicks) {
        if (this.m_21224_()) {
            partialTicks = 0.0F;
        }
        partialTicks = 1.0F - partialTicks;
        int i = this.ringBufferIndex - bufferOffset & 63;
        int j = this.ringBufferIndex - bufferOffset - 1 & 63;
        float d0 = this.ringBuffer[i];
        float d1 = this.ringBuffer[j] - d0;
        return Mth.wrapDegrees(d0 + d1 * partialTicks);
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.75F : 1.0F;
    }

    @Override
    public boolean isPushable() {
        return !this.isStrangling();
    }

    public boolean shouldMove() {
        return !this.isStrangling();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.CHICKEN || stack.getItem() == Items.COOKED_CHICKEN;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.shouldMove()) {
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

    public float getStrangleProgress(float partialTick) {
        return this.prevStrangleProgress + (this.strangleProgress - this.prevStrangleProgress) * partialTick;
    }

    private float calcPartRotation(int i) {
        float f = 1.0F - this.strangleProgress * 0.2F;
        float strangleIntensity = (float) ((double) Mth.clamp((float) (this.strangleTimer * 3), 0.0F, 100.0F) * (1.0 + 0.2F * Math.sin((double) (0.15F * (float) this.strangleTimer))));
        return (float) (40.0 * -Math.sin((double) (this.f_19787_ * 3.0F - (float) i))) * f + this.strangleProgress * 0.2F * (float) i * strangleIntensity;
    }

    @Nullable
    public ItemEntity spawnItemAtOffset(ItemStack stack, float f, float f1) {
        if (stack.isEmpty()) {
            return null;
        } else if (this.m_9236_().isClientSide) {
            return null;
        } else {
            Vec3 vec = new Vec3(0.0, 0.0, (double) f).yRot(-f * (float) (Math.PI / 180.0));
            ItemEntity itementity = new ItemEntity(this.m_9236_(), this.m_20185_() + vec.x, this.m_20186_() + (double) f1, this.m_20189_() + vec.z, stack);
            itementity.setDefaultPickUpDelay();
            if (this.captureDrops() != null) {
                this.captureDrops().add(itementity);
            } else {
                this.m_9236_().m_7967_(itementity);
            }
            return itementity;
        }
    }

    @Override
    public boolean shouldEnterWater() {
        return this.m_5448_() == null && !this.shouldLeaveWater() && this.swimTimer <= -1000;
    }

    @Override
    public boolean shouldLeaveWater() {
        if (!this.m_20197_().isEmpty()) {
            return false;
        } else {
            return this.m_5448_() != null && !this.m_5448_().m_20069_() ? true : this.swimTimer > 600 || this.isShedding();
        }
    }

    @Override
    public boolean shouldStopMoving() {
        return !this.shouldMove();
    }

    @Override
    public int getWaterSearchRange() {
        return 12;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob mob) {
        EntityAnaconda anaconda = AMEntityRegistry.ANACONDA.get().create(serverWorld);
        anaconda.setYellow(this.isYellow());
        return anaconda;
    }

    @Override
    public void awardKillScore(Entity entity, int score, DamageSource src) {
        if (entity instanceof LivingEntity living) {
            CompoundTag emptyNbt = new CompoundTag();
            living.addAdditionalSaveData(emptyNbt);
            emptyNbt.putString("DeathLootTable", BuiltInLootTables.EMPTY.toString());
            living.readAdditionalSaveData(emptyNbt);
            if (this.getChild() instanceof EntityAnacondaPart) {
                ((EntityAnacondaPart) this.getChild()).setSwell(5.0F);
            }
        }
        super.m_5993_(entity, score, src);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean canAttack(LivingEntity livingEntity) {
        boolean prev = super.m_6779_(livingEntity);
        return !prev || this.passiveFor <= 0 || !(livingEntity instanceof Player) || this.m_21188_() != null && this.m_21188_().m_20148_().equals(livingEntity.m_20148_()) ? prev : false;
    }

    public void feed() {
        this.m_5634_(10.0F);
        this.feedings++;
        if (this.feedings >= 3 && this.feedings % 3 == 0 && this.shedCooldown <= 0) {
            this.setSheddingTime(this.m_217043_().nextInt(500) + 500);
        }
    }

    public boolean isShedding() {
        return this.getSheddingTime() > 0;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setYellow(this.f_19796_.nextBoolean());
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    private class AIMelee extends Goal {

        private final EntityAnaconda snake;

        private int jumpAttemptCooldown = 0;

        public AIMelee() {
            this.snake = EntityAnaconda.this;
        }

        @Override
        public boolean canUse() {
            return this.snake.m_5448_() != null && this.snake.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            if (this.jumpAttemptCooldown > 0) {
                this.jumpAttemptCooldown--;
            }
            LivingEntity target = this.snake.m_5448_();
            if (target != null && target.isAlive()) {
                if (this.jumpAttemptCooldown == 0 && this.snake.m_20270_(target) < 1.0F + target.m_20205_() && !this.snake.isStrangling()) {
                    target.hurt(this.snake.m_269291_().mobAttack(this.snake), 4.0F);
                    this.snake.setStrangling(target.m_20205_() <= 2.0F && !(target instanceof EntityAnaconda));
                    this.snake.m_5496_(AMSoundRegistry.ANACONDA_ATTACK.get(), this.snake.m_6121_(), this.snake.m_6100_());
                    this.jumpAttemptCooldown = 5 + EntityAnaconda.this.f_19796_.nextInt(5);
                }
                if (this.snake.isStrangling()) {
                    this.snake.m_21573_().stop();
                } else {
                    try {
                        this.snake.m_21573_().moveTo(target, 1.3F);
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.snake.setStrangling(false);
        }
    }
}