package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAITempt;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class EntityFlutter extends TamableAnimal implements IFollower, FlyingAnimal {

    private static final EntityDataAccessor<Float> FLUTTER_PITCH = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> POTTED = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TENTACLING = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SHOOTING = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SHAKING_HEAD_TICKS = SynchedEntityData.defineId(EntityFlutter.class, EntityDataSerializers.INT);

    public float prevFlyProgress;

    public float flyProgress;

    public float prevShootProgress;

    public float shootProgress;

    public float prevSitProgress;

    public float sitProgress;

    public float prevFlutterPitch;

    public float tentacleProgress;

    public float prevTentacleProgress;

    public float FlutterRotation;

    private float rotationVelocity;

    private int squishCooldown = 0;

    private float randomMotionSpeed;

    private boolean isLandNavigator;

    private int timeFlying;

    private List<String> flowersEaten = new ArrayList();

    private boolean hasPotStats = false;

    protected EntityFlutter(EntityType type, Level level) {
        super(type, level);
        this.rotationVelocity = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.5F;
        this.switchNavigator(false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.FLYING_SPEED, 0.8F).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.21F);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.requiresCustomPersistence() && !this.m_8077_();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.m_8023_() || this.m_8077_() || this.m_21824_() || this.isPotted();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.flutterSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canFlutterSpawnInLight(EntityType<? extends EntityFlutter> p_223325_0_, ServerLevelAccessor p_223325_1_, MobSpawnType p_223325_2_, BlockPos p_223325_3_, RandomSource p_223325_4_) {
        return m_217057_(p_223325_0_, p_223325_1_, p_223325_2_, p_223325_3_, p_223325_4_);
    }

    public static <T extends Mob> boolean canFlutterSpawn(EntityType<EntityFlutter> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = iServerWorld.m_8055_(pos.below());
        return reason == MobSpawnType.SPAWNER || !iServerWorld.m_45527_(pos) && blockstate.m_60713_(Blocks.MOSS_BLOCK) && pos.m_123342_() <= 64 && canFlutterSpawnInLight(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityFlutter.FlyAwayFromTarget(this));
        this.f_21345_.addGoal(2, new TameableAITempt(this, 1.1, Ingredient.of(Items.BONE_MEAL), false) {

            @Override
            public boolean shouldFollowAM(LivingEntity le) {
                return EntityFlutter.this.canEatFlower(le.getMainHandItem()) || EntityFlutter.this.canEatFlower(le.getOffhandItem()) || super.shouldFollowAM(le);
            }
        });
        this.f_21345_.addGoal(3, new FlyingAIFollowOwner(this, 1.3, 7.0F, 2.0F, false));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(5, new EntityFlutter.AIWalkIdle());
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new FlightMoveController(this, 1.0F, false, true);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FLUTTER_PITCH, 0.0F);
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(POTTED, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(TENTACLING, false);
        this.f_19804_.define(SHOOTING, false);
        this.f_19804_.define(SHAKING_HEAD_TICKS, 0);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isPotted() {
        return this.f_19804_.get(POTTED);
    }

    public void setPotted(boolean potted) {
        this.f_19804_.set(POTTED, potted);
    }

    public float getFlutterPitch() {
        return Mth.clamp(this.f_19804_.get(FLUTTER_PITCH), -90.0F, 90.0F);
    }

    public void setFlutterPitch(float pitch) {
        this.f_19804_.set(FLUTTER_PITCH, pitch);
    }

    public void incrementFlutterPitch(float pitch) {
        this.f_19804_.set(FLUTTER_PITCH, this.getFlutterPitch() + pitch);
    }

    public void decrementFlutterPitch(float pitch) {
        this.f_19804_.set(FLUTTER_PITCH, this.getFlutterPitch() - pitch);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.FLUTTER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.FLUTTER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.FLUTTER_HURT.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevShootProgress = this.shootProgress;
        this.prevFlyProgress = this.flyProgress;
        this.prevFlutterPitch = this.getFlutterPitch();
        this.prevSitProgress = this.sitProgress;
        float extraMotionSlow = 1.0F;
        float extraMotionSlowY = 1.0F;
        this.f_20883_ = this.m_146908_();
        this.f_20885_ = this.m_146908_();
        this.prevFlutterPitch = this.getFlutterPitch();
        this.prevTentacleProgress = this.tentacleProgress;
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.tentacleProgress < 5.0F && this.f_19804_.get(TENTACLING)) {
            this.tentacleProgress++;
        }
        if (this.tentacleProgress == 5.0F && !this.f_19804_.get(TENTACLING) && this.squishCooldown == 0 && this.isFlying()) {
            this.squishCooldown = 10;
            this.m_5496_(AMSoundRegistry.FLUTTER_FLAP.get(), this.m_6121_(), 1.5F * this.m_6100_());
        }
        if (this.tentacleProgress > 0.0F && !this.f_19804_.get(TENTACLING)) {
            this.tentacleProgress--;
        }
        this.FlutterRotation = this.FlutterRotation + this.rotationVelocity;
        if ((double) this.FlutterRotation > (float) (Math.PI * 2)) {
            if (this.m_9236_().isClientSide) {
                this.FlutterRotation = (float) (Math.PI * 2);
            } else {
                this.FlutterRotation = (float) ((double) this.FlutterRotation - (float) (Math.PI * 2));
                if (this.f_19796_.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0F / (this.f_19796_.nextFloat() + 1.0F) * 0.5F;
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 19);
            }
        }
        if (this.FlutterRotation < (float) Math.PI) {
            float f = this.FlutterRotation / (float) Math.PI;
            if ((double) f >= 0.95F) {
                this.f_19804_.set(TENTACLING, true);
                if (this.squishCooldown == 0 && this.isFlying()) {
                    this.squishCooldown = 10;
                    this.m_146850_(GameEvent.ENTITY_ROAR);
                    this.m_5496_(AMSoundRegistry.FLUTTER_FLAP.get(), 3.0F, 1.5F * this.m_6100_());
                }
                this.randomMotionSpeed = 0.8F;
            } else {
                this.f_19804_.set(TENTACLING, false);
                this.randomMotionSpeed = 0.01F;
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying() && this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (!this.isFlying() && !this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.isFlying()) {
                this.m_20334_(this.m_20184_().x * (double) this.randomMotionSpeed * (double) extraMotionSlow, this.m_20184_().y * (double) this.randomMotionSpeed * (double) extraMotionSlowY, this.m_20184_().z * (double) this.randomMotionSpeed * (double) extraMotionSlow);
                this.timeFlying++;
                if (this.m_20096_() && this.timeFlying > 20 || this.isSitting()) {
                    this.setFlying(false);
                }
            } else {
                this.timeFlying = 0;
            }
        }
        if (!this.m_20096_() && this.m_20184_().y < 0.0) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.8, 1.0));
        }
        if (this.isFlying()) {
            float dist = (float) ((Math.abs(this.m_20184_().x()) + Math.abs(this.m_20184_().z())) * 30.0);
            this.incrementFlutterPitch(-dist);
            if (this.f_19862_) {
                this.m_20256_(this.m_20184_().add(0.0, 0.2F, 0.0));
            }
        }
        if (this.getFlutterPitch() > 0.0F) {
            float decrease = Math.min(2.5F, this.getFlutterPitch());
            this.decrementFlutterPitch(decrease);
        }
        if (this.getFlutterPitch() < 0.0F) {
            float decrease = Math.min(2.5F, -this.getFlutterPitch());
            this.incrementFlutterPitch(decrease);
        }
        boolean shooting = this.f_19804_.get(SHOOTING);
        if (shooting && this.shootProgress < 5.0F) {
            this.shootProgress++;
        }
        if (!shooting && this.shootProgress > 0.0F) {
            this.shootProgress--;
        }
        if (shooting) {
            this.incrementFlutterPitch(-30.0F);
        }
        if (!this.m_9236_().isClientSide && shooting && this.shootProgress == 5.0F) {
            if (this.m_5448_() != null) {
                this.spit(this.m_5448_());
            }
            this.f_19804_.set(SHOOTING, false);
        }
        if (this.hasPotStats && !this.isPotted()) {
            this.hasPotStats = false;
            this.m_21051_(Attributes.ARMOR).setBaseValue(0.21);
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.21);
        }
        if (!this.hasPotStats && this.isPotted()) {
            this.hasPotStats = true;
            this.m_21051_(Attributes.ARMOR).setBaseValue(16.0);
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.18);
        }
        if (this.f_19804_.get(SHAKING_HEAD_TICKS) > 0) {
            this.f_19804_.set(SHAKING_HEAD_TICKS, this.f_19804_.get(SHAKING_HEAD_TICKS) - 1);
        }
        if (this.squishCooldown > 0) {
            this.squishCooldown--;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.BONE_MEAL && this.m_21824_();
    }

    private void spit(LivingEntity target) {
        EntityPollenBall llamaspitentity = new EntityPollenBall(this.m_9236_(), this);
        double d0 = target.m_20185_() - this.m_20185_();
        double d1 = target.m_20227_(0.3333333333333333) - llamaspitentity.m_20186_();
        double d2 = target.m_20189_() - this.m_20189_();
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
        llamaspitentity.shoot(d0, d1 + (double) f, d2, 0.5F, 13.0F);
        if (!this.m_20067_()) {
            this.m_146850_(GameEvent.PROJECTILE_SHOOT);
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.LLAMA_SPIT, this.m_5720_(), 1.0F, 1.0F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F);
        }
        this.m_9236_().m_7967_(llamaspitentity);
    }

    public boolean isShakingHead() {
        return this.f_19804_.get(SHAKING_HEAD_TICKS) > 0;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && this.canEatFlower(itemstack)) {
            this.m_142075_(player, hand, itemstack);
            this.flowersEaten.add(ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString());
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(AMSoundRegistry.FLUTTER_YES.get(), this.m_6121_(), this.m_6100_());
            if ((this.flowersEaten.size() <= 3 || this.m_217043_().nextInt(3) != 0) && this.flowersEaten.size() <= 6) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            } else {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (!this.m_21824_() && itemstack.is(ItemTags.FLOWERS)) {
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                this.m_5496_(AMSoundRegistry.FLUTTER_NO.get(), this.m_6121_(), this.m_6100_());
                this.f_19804_.set(SHAKING_HEAD_TICKS, 20);
            }
            if (this.m_21824_() && itemstack.is(ItemTags.FLOWERS) && this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.CAT_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack) || itemstack.is(ItemTags.FLOWERS)) {
                    return type;
                } else if (item == Items.FLOWER_POT && !this.isPotted()) {
                    this.setPotted(true);
                    return InteractionResult.SUCCESS;
                } else if (item == Items.SHEARS && this.isPotted()) {
                    this.setPotted(false);
                    this.m_19998_(Items.FLOWER_POT);
                    return InteractionResult.SUCCESS;
                } else if (this.isPotted() && player.m_6144_()) {
                    ItemStack fish = this.getFishBucket();
                    if (!player.addItem(fish)) {
                        player.drop(fish, false);
                    }
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                    return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
                } else {
                    this.setCommand(this.getCommand() + 1);
                    if (this.getCommand() == 3) {
                        this.setCommand(0);
                    }
                    player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                    boolean sit = this.getCommand() == 2;
                    if (sit) {
                        this.setOrderedToSit(true);
                        return InteractionResult.SUCCESS;
                    } else {
                        this.setOrderedToSit(false);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
    }

    @Override
    public void followEntity(TamableAnimal tameable, LivingEntity owner, double followSpeed) {
        if (this.m_20270_(owner) > 8.0F) {
            this.setFlying(true);
            this.m_21573_().moveTo(owner.m_20185_(), owner.m_20186_() + (double) owner.m_20206_(), owner.m_20189_(), followSpeed);
        } else if (this.isFlying() && !this.isOverWaterOrVoid()) {
            BlockPos vec = this.getFlutterGround(this.m_20183_());
            if (vec != null) {
                this.m_21566_().setWantedPosition((double) vec.m_123341_(), (double) vec.m_123342_(), (double) vec.m_123343_(), followSpeed);
            }
            if (this.m_20096_()) {
                this.setFlying(false);
            }
        } else {
            this.m_21573_().moveTo(owner, followSpeed);
        }
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isPotted() && !this.m_9236_().isClientSide) {
            this.m_19998_(Items.FLOWER_POT);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putBoolean("Potted", this.isPotted());
        compound.putInt("FlowersEaten", this.flowersEaten.size());
        for (int i = 0; i < this.flowersEaten.size(); i++) {
            compound.putString("FlowerEaten" + i, (String) this.flowersEaten.get(i));
        }
        compound.putInt("FlutterCommand", this.getCommand());
        compound.putBoolean("FlutterSitting", this.isSitting());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.setPotted(compound.getBoolean("Potted"));
        int flowerCount = compound.getInt("FlowersEaten");
        this.flowersEaten = new ArrayList();
        for (int i = 0; i < flowerCount; i++) {
            String s = compound.getString("FlowerEaten" + i);
            if (s != null) {
                this.flowersEaten.add(s);
            }
        }
        this.setCommand(compound.getInt("FlutterCommand"));
        this.setOrderedToSit(compound.getBoolean("FlutterSitting"));
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -63 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || position.m_123342_() < -63;
    }

    private BlockPos getFlutterGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() > -63 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return position.m_123342_() < -62 ? position.above(120 + this.f_19796_.nextInt(5)) : position;
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = (float) (1 + this.m_217043_().nextInt(3)) + radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + this.m_217043_().nextFloat() * neg * 0.2F;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getFlutterGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 3 + this.m_217043_().nextInt(2);
        BlockPos newPos = ground.above(distFromGround > 4 ? flightHeight : distFromGround - 2 + this.m_217043_().nextInt(4));
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, this.m_20186_(), fleePos.z() + extraZ);
        BlockPos ground = this.getFlutterGround(radialPos);
        if (ground.m_123342_() <= -63) {
            return Vec3.upFromBottomCenterOf(ground, (double) (110 + this.f_19796_.nextInt(20)));
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -63 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground.below()) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    protected ItemStack getFishBucket() {
        ItemStack stack = new ItemStack(AMItemRegistry.POTTED_FLUTTER.get());
        CompoundTag platTag = new CompoundTag();
        this.addAdditionalSaveData(platTag);
        stack.getOrCreateTag().put("FlutterData", platTag);
        if (this.m_8077_()) {
            stack.setHoverName(this.m_7770_());
        }
        return stack;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mobo) {
        EntityFlutter baby = AMEntityRegistry.FLUTTER.get().create(this.m_9236_());
        baby.m_21530_();
        return baby;
    }

    public boolean hasEatenFlower(ItemStack stack) {
        return this.flowersEaten != null && this.flowersEaten.contains(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
    }

    public boolean canEatFlower(ItemStack stack) {
        return !this.hasEatenFlower(stack) && stack.is(ItemTags.FLOWERS);
    }

    private void setupShooting() {
        this.f_19804_.set(SHOOTING, true);
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel world, Animal partner) {
        super.m_27563_(world, partner);
        for (int i = 0; i < 15 + this.f_19796_.nextInt(10); i++) {
            BlockPos nearby = this.m_20183_().offset(this.f_19796_.nextInt(16) - 8, this.f_19796_.nextInt(2), this.f_19796_.nextInt(16) - 8);
            if (world.m_8055_(nearby).m_60734_() == Blocks.AZALEA) {
                world.m_46597_(nearby, Blocks.FLOWERING_AZALEA.defaultBlockState());
                world.m_46796_(1505, nearby, 0);
            }
            if (world.m_8055_(nearby).m_60734_() == Blocks.AZALEA_LEAVES) {
                world.m_46597_(nearby, Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState());
                world.m_46796_(1505, nearby, 0);
            }
        }
    }

    private class AIWalkIdle extends Goal {

        protected final EntityFlutter phage;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        public AIWalkIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.phage = EntityFlutter.this;
        }

        @Override
        public boolean canUse() {
            if (this.phage.m_20160_() || this.phage.isSitting() || this.phage.shouldFollow() || this.phage.m_5448_() != null && this.phage.m_5448_().isAlive() || this.phage.m_20159_()) {
                return false;
            } else if (this.phage.m_217043_().nextInt(30) != 0 && !this.phage.isFlying() && !this.phage.m_20072_()) {
                return false;
            } else {
                if (this.phage.m_20096_() && !this.phage.m_20072_()) {
                    this.flightTarget = EntityFlutter.this.f_19796_.nextInt(4) == 0 && !this.phage.m_6162_();
                } else {
                    this.flightTarget = EntityFlutter.this.f_19796_.nextInt(5) > 0 && this.phage.timeFlying < 100 && !this.phage.m_6162_();
                }
                Vec3 lvt_1_1_ = this.getPosition();
                if (lvt_1_1_ == null) {
                    return false;
                } else {
                    this.x = lvt_1_1_.x;
                    this.y = lvt_1_1_.y;
                    this.z = lvt_1_1_.z;
                    return true;
                }
            }
        }

        @Override
        public void tick() {
            if (this.flightTarget) {
                this.phage.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.phage.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
            if (!this.flightTarget && EntityFlutter.this.isFlying() && this.phage.m_20096_()) {
                this.phage.setFlying(false);
            }
            if (EntityFlutter.this.isFlying() && this.phage.m_20096_() && this.phage.timeFlying > 40) {
                this.phage.setFlying(false);
            }
        }

        @javax.annotation.Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.phage.m_20182_();
            if (this.phage.isOverWaterOrVoid()) {
                this.flightTarget = true;
            }
            if (this.flightTarget) {
                return this.phage.timeFlying >= 180 && !this.phage.isOverWaterOrVoid() ? this.phage.getBlockGrounding(vector3d) : this.phage.getBlockInViewAway(vector3d, 0.0F);
            } else {
                return LandRandomPos.getPos(this.phage, 5, 5);
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.phage.isSitting()) {
                return false;
            } else {
                return this.flightTarget ? this.phage.isFlying() && this.phage.m_20275_(this.x, this.y, this.z) > 2.0 && !this.phage.m_6162_() : !this.phage.m_21573_().isDone() && !this.phage.m_20160_();
            }
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.phage.setFlying(true);
                this.phage.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.phage.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.phage.m_21573_().stop();
            super.stop();
        }
    }

    private class FlyAwayFromTarget extends Goal {

        private final EntityFlutter parentEntity;

        private int spitCooldown = 0;

        private BlockPos shootPos = null;

        public FlyAwayFromTarget(EntityFlutter entityFlutter) {
            this.parentEntity = entityFlutter;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return !this.parentEntity.isSitting() && this.parentEntity.m_5448_() != null && this.parentEntity.m_5448_().isAlive() && !this.parentEntity.m_6162_();
        }

        @Override
        public void tick() {
            if (this.spitCooldown > 0) {
                this.spitCooldown--;
            }
            if (this.parentEntity.m_5448_() != null) {
                this.parentEntity.setFlying(true);
                if (this.shootPos == null || this.parentEntity.m_20270_(this.parentEntity.m_5448_()) >= 10.0F || this.parentEntity.m_5448_().m_20275_((double) ((float) this.shootPos.m_123341_() + 0.5F), (double) this.shootPos.m_123342_(), (double) ((float) this.shootPos.m_123343_() + 0.5F)) < 4.0) {
                    this.shootPos = this.getShootFromPos(this.parentEntity.m_5448_());
                }
                if (this.shootPos != null) {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.shootPos.m_123341_() + 0.5, (double) this.shootPos.m_123342_() + 0.5, (double) this.shootPos.m_123343_() + 0.5, 1.5);
                }
                if (this.parentEntity.m_20270_(this.parentEntity.m_5448_()) < 25.0F) {
                    this.parentEntity.m_21391_(this.parentEntity.m_5448_(), 30.0F, 30.0F);
                    if (this.spitCooldown == 0) {
                        this.parentEntity.setupShooting();
                        this.spitCooldown = 10 + EntityFlutter.this.f_19796_.nextInt(10);
                    }
                    this.shootPos = null;
                }
            }
        }

        public BlockPos getShootFromPos(LivingEntity target) {
            float radius = (float) (3 + this.parentEntity.m_217043_().nextInt(5));
            float angle = (float) (Math.PI / 180.0) * (target.yHeadRot + 90.0F + (float) this.parentEntity.m_217043_().nextInt(180));
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = AMBlockPos.fromCoords(target.m_20185_() + extraX, target.m_20186_() + 2.0, target.m_20189_() + extraZ);
            return !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(radialPos)) ? radialPos : this.parentEntity.m_20183_().above((int) Math.ceil((double) (target.m_20206_() + 1.0F)));
        }
    }
}