package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIFindWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILeaveWater;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalSwimMoveControllerSink;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.entity.ai.MudskipperAIAttack;
import com.github.alexthe666.alexsmobs.entity.ai.MudskipperAIDisplay;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticAIRandomSwimming;
import com.github.alexthe666.alexsmobs.entity.ai.SemiAquaticPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwnerWater;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class EntityMudskipper extends TamableAnimal implements IFollower, ISemiAquatic, Bucketable {

    public float prevSitProgress;

    public float sitProgress;

    public float prevSwimProgress;

    public float swimProgress;

    public float prevDisplayProgress;

    public float displayProgress;

    public float prevMudProgress;

    public float mudProgress;

    public float nextDisplayAngleFromServer;

    public float prevDisplayAngle;

    public boolean displayDirection;

    public int displayTimer = 0;

    public boolean instantlyTriggerDisplayAI = false;

    public int displayCooldown = 100 + this.f_19796_.nextInt(100);

    private static final EntityDataAccessor<Boolean> DISPLAYING = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> DISPLAY_ANGLE = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> DISPLAYER_UUID = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Integer> MOUTH_TICKS = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityMudskipper.class, EntityDataSerializers.INT);

    private boolean isLandNavigator;

    private int swimTimer = -1000;

    public EntityMudskipper(EntityType type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
        this.switchNavigator(true);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isOrderedToSit()) {
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
            } else {
                super.m_7023_(travelVector);
            }
        }
    }

    public static <T extends Mob> boolean canMudskipperSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockstate = worldIn.m_8055_(p_223317_3_.below());
        return blockstate.m_60713_(Blocks.MUD) || blockstate.m_60713_(Blocks.MUDDY_MANGROVE_ROOTS);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.mudskipperSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        BlockPos pos = AMBlockPos.fromCoords(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return !worldIn.m_8055_(pos).m_60828_(worldIn, pos);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new TameableAIFollowOwnerWater(this, 1.3, 4.0F, 2.0F, false));
        this.f_21345_.addGoal(2, new MudskipperAIAttack(this));
        this.f_21345_.addGoal(3, new AnimalAIFindWater(this));
        this.f_21345_.addGoal(3, new AnimalAILeaveWater(this));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.1, Ingredient.of(AMItemRegistry.LOBSTER_TAIL.get(), AMItemRegistry.COOKED_LOBSTER_TAIL.get()), false));
        this.f_21345_.addGoal(5, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(6, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(7, new MudskipperAIDisplay(this));
        this.f_21345_.addGoal(8, new SemiAquaticAIRandomSwimming(this, 1.0, 80));
        this.f_21345_.addGoal(9, new RandomStrollGoal(this, 1.0, 120));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this) {

            @Override
            public boolean canUse() {
                return EntityMudskipper.this.m_21824_() && super.canUse();
            }
        });
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DISPLAYING, false);
        this.f_19804_.define(FROM_BUCKET, false);
        this.f_19804_.define(DISPLAY_ANGLE, 0.0F);
        this.f_19804_.define(DISPLAYER_UUID, Optional.empty());
        this.f_19804_.define(MOUTH_TICKS, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 12.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", this.fromBucket());
        compound.putInt("DisplayCooldown", this.displayCooldown);
        compound.putInt("MudskipperCommand", this.getCommand());
        compound.putBoolean("MudskipperSitting", this.isOrderedToSit());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFromBucket(compound.getBoolean("FromBucket"));
        this.displayCooldown = compound.getInt("DisplayCooldown");
        this.setCommand(compound.getInt("MudskipperCommand"));
        this.setOrderedToSit(compound.getBoolean("MudskipperSitting"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSwimProgress = this.swimProgress;
        this.prevSitProgress = this.sitProgress;
        this.prevDisplayProgress = this.displayProgress;
        this.prevMudProgress = this.mudProgress;
        if (this.displayProgress < 5.0F && this.isDisplaying()) {
            this.displayProgress++;
        }
        if (this.displayProgress > 0.0F && !this.isDisplaying()) {
            this.displayProgress--;
        }
        if (this.sitProgress < 5.0F && this.isOrderedToSit()) {
            this.sitProgress++;
        }
        if (this.sitProgress > 0.0F && !this.isOrderedToSit()) {
            this.sitProgress--;
        }
        boolean mud = this.onMud();
        if (mud) {
            if (this.mudProgress < 1.0F) {
                this.mudProgress += 0.5F;
            }
        } else if (this.mudProgress > 0.0F) {
            this.mudProgress -= 0.5F;
        }
        boolean swim = !this.m_20096_() && this.m_20072_();
        if (this.swimProgress < 5.0F && swim) {
            this.swimProgress++;
        }
        if (this.swimProgress > 0.0F && !swim) {
            this.swimProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.m_20072_()) {
                this.swimTimer++;
            } else {
                this.swimTimer--;
            }
        }
        if (this.displayCooldown > 0) {
            this.displayCooldown--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.getDisplayAngle() < this.nextDisplayAngleFromServer) {
                this.setDisplayAngle(this.getDisplayAngle() + 1.0F);
            }
            if (this.getDisplayAngle() > this.nextDisplayAngleFromServer) {
                this.setDisplayAngle(this.getDisplayAngle() - 1.0F);
            }
        }
        if (this.isMouthOpen()) {
            this.openMouth(this.getMouthTicks() - 1);
        }
        if (this.m_20069_() && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!this.m_20069_() && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean prev = super.m_6469_(source, amount);
        if (prev && source.getDirectEntity() instanceof LivingEntity) {
            this.openMouth(10);
        }
        return prev;
    }

    public boolean isDisplaying() {
        return this.f_19804_.get(DISPLAYING);
    }

    public void setDisplaying(boolean display) {
        this.f_19804_.set(DISPLAYING, display);
    }

    public float getDisplayAngle() {
        return this.f_19804_.get(DISPLAY_ANGLE);
    }

    public void setDisplayAngle(float scale) {
        this.f_19804_.set(DISPLAY_ANGLE, scale);
    }

    public int getMouthTicks() {
        return this.f_19804_.get(MOUTH_TICKS);
    }

    public void openMouth(int time) {
        this.f_19804_.set(MOUTH_TICKS, time);
    }

    @Nullable
    public UUID getDisplayingPartnerUUID() {
        return (UUID) this.f_19804_.get(DISPLAYER_UUID).orElse(null);
    }

    public void setDisplayingPartnerUUID(@Nullable UUID uniqueId) {
        this.f_19804_.set(DISPLAYER_UUID, Optional.ofNullable(uniqueId));
    }

    @Nullable
    public Entity getDisplayingPartner() {
        UUID id = this.getDisplayingPartnerUUID();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setDisplayingPartner(@Nullable Entity jostlingPartner) {
        if (jostlingPartner == null) {
            this.setDisplayingPartnerUUID(null);
        } else {
            this.setDisplayingPartnerUUID(jostlingPartner.getUUID());
        }
    }

    public boolean canDisplayWith(EntityMudskipper mudskipper) {
        return !mudskipper.m_6162_() && !mudskipper.isOrderedToSit() && !mudskipper.shouldFollow() && mudskipper.m_20096_() && mudskipper.getDisplayingPartnerUUID() == null && mudskipper.displayCooldown == 0;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return AMEntityRegistry.MUDSKIPPER.get().create(serverLevel);
    }

    public boolean isMouthOpen() {
        return this.getMouthTicks() > 0;
    }

    public boolean onMud() {
        BlockState below = this.m_9236_().getBlockState(this.m_20099_());
        return below.m_60713_(Blocks.MUD);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.m_5496_(AMSoundRegistry.MUDSKIPPER_WALK.get(), 1.0F, 1.0F);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.MUDSKIPPER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.MUDSKIPPER_HURT.get();
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    @Override
    public boolean isOrderedToSit() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    @Override
    public boolean shouldEnterWater() {
        return (this.m_21188_() != null || this.swimTimer <= -1000) && !this.isDisplaying();
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.swimTimer > 200 || this.isDisplaying();
    }

    @Override
    public boolean shouldStopMoving() {
        return this.isOrderedToSit();
    }

    @Override
    public int getWaterSearchRange() {
        return 10;
    }

    @Override
    public boolean fromBucket() {
        return this.f_19804_.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean bucket) {
        this.f_19804_.set(FROM_BUCKET, bucket);
    }

    @Nonnull
    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(AMItemRegistry.MUDSKIPPER_BUCKET.get());
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
        compound.put("MudskipperData", platTag);
    }

    @Override
    public void loadFromBucketTag(@Nonnull CompoundTag compound) {
        if (compound.contains("MudskipperData")) {
            this.readAdditionalSaveData(compound.getCompound("MudskipperData"));
        }
    }

    @Nonnull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(AMTagRegistry.INSECT_ITEMS) || stack.getItem() == AMItemRegistry.LOBSTER_TAIL.get() || stack.getItem() == AMItemRegistry.COOKED_LOBSTER_TAIL.get();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (this.m_21824_() || item != AMItemRegistry.LOBSTER_TAIL.get() && item != AMItemRegistry.COOKED_LOBSTER_TAIL.get()) {
            if (!this.m_21824_() || !itemstack.is(AMTagRegistry.INSECT_ITEMS)) {
                InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                if (item != Items.WATER_BUCKET && interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player) && !this.isFood(itemstack)) {
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
                } else {
                    return (InteractionResult) Bucketable.bucketMobPickup(player, hand, this).orElse(type);
                }
            } else if (this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.openMouth(10);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            this.m_142075_(player, hand, itemstack);
            this.openMouth(10);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6121_(), this.m_6100_());
            if (this.m_217043_().nextInt(2) == 0) {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
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
}