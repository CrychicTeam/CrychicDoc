package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoDismount;
import com.github.alexthe666.alexsmobs.message.MessageMosquitoMountPlayer;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityBaldEagle extends TamableAnimal implements IFollower, IFalconry {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> TACKLING = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HAS_CAP = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> LAUNCHED = SynchedEntityData.defineId(EntityBaldEagle.class, EntityDataSerializers.BOOLEAN);

    private static final Ingredient TEMPT_ITEMS = Ingredient.of(Items.ROTTEN_FLESH, AMItemRegistry.FISH_OIL.get());

    public float prevAttackProgress;

    public float attackProgress;

    public float prevFlyProgress;

    public float flyProgress;

    public float prevTackleProgress;

    public float tackleProgress;

    public float prevSwoopProgress;

    public float swoopProgress;

    public float prevFlapAmount;

    public float flapAmount;

    public float birdPitch = 0.0F;

    public float prevBirdPitch = 0.0F;

    public float prevSitProgress;

    public float sitProgress;

    private boolean isLandNavigator;

    private int timeFlying;

    private BlockPos orbitPos = null;

    private double orbitDist = 5.0;

    private boolean orbitClockwise = false;

    private int passengerTimer = 0;

    private int launchTime = 0;

    private int lastPlayerControlTime = 0;

    private int returnControlTime = 0;

    private int tackleCapCooldown = 0;

    private boolean controlledFlag = false;

    private int chunkLoadCooldown;

    private int stillTicksCounter = 0;

    protected EntityBaldEagle(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 5.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    public static boolean canEagleSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.m_45524_(pos, 0) > 8;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this) {

            @Override
            public boolean canUse() {
                return super.canUse() && (EntityBaldEagle.this.m_20146_() < 30 || EntityBaldEagle.this.m_5448_() == null || !EntityBaldEagle.this.m_5448_().m_20072_() && EntityBaldEagle.this.m_20186_() > EntityBaldEagle.this.m_5448_().m_20186_());
            }
        });
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new FlyingAIFollowOwner(this, 1.0, 25.0F, 2.0F, false));
        this.f_21345_.addGoal(3, new EntityBaldEagle.AITackle());
        this.f_21345_.addGoal(4, new EntityBaldEagle.AILandOnGlove());
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.1, Ingredient.of(AMTagRegistry.BALD_EAGLE_TAMEABLES), false));
        this.f_21345_.addGoal(7, new TemptGoal(this, 1.1, Ingredient.of(ItemTags.FISHES), false));
        this.f_21345_.addGoal(8, new EntityBaldEagle.AIWanderIdle());
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F) {

            @Override
            public boolean canUse() {
                return EntityBaldEagle.this.returnControlTime == 0 && super.canUse();
            }
        });
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this) {

            @Override
            public boolean canUse() {
                return EntityBaldEagle.this.returnControlTime == 0 && super.canUse();
            }
        });
        this.f_21346_.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(3, new AnimalAIHurtByTargetNotBaby(this));
        this.f_21346_.addGoal(4, new EntityAINearestTarget3D(this, LivingEntity.class, 55, true, true, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.BALD_EAGLE_TARGETS)) {

            @Override
            public boolean canUse() {
                return super.m_8036_() && !EntityBaldEagle.this.isLaunched() && EntityBaldEagle.this.getCommand() == 0;
            }
        });
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.BALD_EAGLE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.BALD_EAGLE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.BALD_EAGLE_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.baldEagleSpawnRolls, this.m_217043_(), spawnReasonIn);
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
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.ROTTEN_FLESH;
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new GroundPathNavigatorWide(this, this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new EntityBaldEagle.MoveHelper(this);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public boolean save(CompoundTag compound) {
        String s = this.m_20078_();
        compound.putString("id", s);
        super.m_20223_(compound);
        return true;
    }

    @Override
    public boolean saveAsPassenger(CompoundTag compound) {
        if (!this.m_21824_()) {
            return super.m_20086_(compound);
        } else {
            String s = this.m_20078_();
            compound.putString("id", s);
            this.m_20240_(compound);
            return true;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("BirdSitting", this.isSitting());
        compound.putBoolean("Launched", this.isLaunched());
        compound.putBoolean("HasCap", this.hasCap());
        compound.putInt("EagleCommand", this.getCommand());
        compound.putInt("LaunchTime", this.launchTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("BirdSitting"));
        this.setLaunched(compound.getBoolean("Launched"));
        this.setCap(compound.getBoolean("HasCap"));
        this.setCommand(compound.getInt("EagleCommand"));
        this.launchTime = compound.getInt("LaunchTime");
    }

    @Override
    public void travel(Vec3 vec3d) {
        if ((this.shouldHoodedReturn() || !this.hasCap() || !this.m_21824_() || this.m_20159_()) && !this.isSitting()) {
            super.m_7023_(vec3d);
        } else {
            super.m_7023_(Vec3.ZERO);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.attackProgress == 0.0F && this.f_19804_.get(ATTACK_TICK) == 0 && entityIn.isAlive()) {
            double dist = this.isSitting() ? (double) (entityIn.getBbWidth() + 1.0F) : (double) (entityIn.getBbWidth() + 5.0F);
            if ((double) this.m_20270_(entityIn) < dist) {
                this.f_19804_.set(ATTACK_TICK, 5);
            }
        }
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(HAS_CAP, false);
        this.f_19804_.define(TACKLING, false);
        this.f_19804_.define(LAUNCHED, false);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public boolean isLaunched() {
        return this.f_19804_.get(LAUNCHED);
    }

    public void setLaunched(boolean flying) {
        this.f_19804_.set(LAUNCHED, flying);
    }

    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(FLYING, flying);
    }

    public boolean hasCap() {
        return this.f_19804_.get(HAS_CAP);
    }

    public void setCap(boolean cap) {
        this.f_19804_.set(HAS_CAP, cap);
    }

    public boolean isTackling() {
        return this.f_19804_.get(TACKLING);
    }

    public void setTackling(boolean tackling) {
        this.f_19804_.set(TACKLING, tackling);
    }

    @Override
    public void followEntity(TamableAnimal tameable, LivingEntity owner, double followSpeed) {
        if (this.m_20270_(owner) > 15.0F) {
            this.setFlying(true);
            this.m_21566_().setWantedPosition(owner.m_20185_(), owner.m_20186_() + (double) owner.m_20206_(), owner.m_20189_(), followSpeed);
        } else if (this.isFlying() && !this.isOverWaterOrVoid()) {
            BlockPos vec = this.getCrowGround(this.m_20183_());
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
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (itemstack.is(ItemTags.FISHES) && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(10.0F);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            return InteractionResult.CONSUME;
        } else if (itemstack.is(AMTagRegistry.BALD_EAGLE_TAMEABLES)) {
            if (itemstack.hasCraftingRemainingItem() && !player.getAbilities().instabuild) {
                this.m_19983_(itemstack.getCraftingRemainingItem());
            }
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            if (this.f_19796_.nextBoolean()) {
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                this.m_21828_(player);
                this.setCommand(1);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.CONSUME;
        } else {
            if (this.m_21824_() && !this.isFood(itemstack)) {
                if (!this.m_6162_() && item == AMItemRegistry.FALCONRY_HOOD.get()) {
                    if (!this.hasCap()) {
                        this.setCap(true);
                        if (!player.isCreative()) {
                            itemstack.shrink(1);
                        }
                        this.m_146850_(GameEvent.ENTITY_INTERACT);
                        this.m_5496_(SoundEvents.ARMOR_EQUIP_LEATHER, this.m_6121_(), this.m_6100_());
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    if (item == Items.SHEARS && this.hasCap()) {
                        this.m_146850_(GameEvent.ENTITY_INTERACT);
                        this.m_5496_(SoundEvents.SHEEP_SHEAR, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                        if (!this.m_9236_().isClientSide && player instanceof ServerPlayer) {
                            itemstack.hurt(1, this.f_19796_, (ServerPlayer) player);
                        }
                        this.m_19998_(AMItemRegistry.FALCONRY_HOOD.get());
                        this.setCap(false);
                        return InteractionResult.SUCCESS;
                    }
                    if (!this.m_6162_() && this.getRidingFalcons(player) <= 0 && (player.m_21120_(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get() || player.m_21120_(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get())) {
                        this.f_19851_ = 30;
                        this.setLaunched(false);
                        this.m_20153_();
                        this.m_7998_(player, true);
                        if (!this.m_9236_().isClientSide) {
                            AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.m_19879_(), player.m_19879_()));
                        }
                        return InteractionResult.SUCCESS;
                    }
                    InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                    if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS) {
                        this.setCommand((this.getCommand() + 1) % 3);
                        if (this.getCommand() == 3) {
                            this.setCommand(0);
                        }
                        player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                        boolean sit = this.getCommand() == 2;
                        if (sit) {
                            this.setOrderedToSit(true);
                            return InteractionResult.SUCCESS;
                        }
                        this.setOrderedToSit(false);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return type;
        }
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1 && !this.isLaunched();
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (!this.m_20159_() || entity.isAlive() && this.m_6084_()) {
            if (this.m_21824_() && entity instanceof LivingEntity && this.m_21830_((LivingEntity) entity)) {
                this.m_20334_(0.0, 0.0, 0.0);
                this.tick();
                if (this.m_20159_()) {
                    Entity mount = this.m_20202_();
                    if (mount instanceof Player) {
                        float yawAdd = 0.0F;
                        if (((Player) mount).m_21120_(InteractionHand.MAIN_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                            yawAdd = ((Player) mount).getMainArm() == HumanoidArm.LEFT ? 135.0F : -135.0F;
                        } else if (((Player) mount).m_21120_(InteractionHand.OFF_HAND).getItem() == AMItemRegistry.FALCONRY_GLOVE.get()) {
                            yawAdd = ((Player) mount).getMainArm() == HumanoidArm.LEFT ? -135.0F : 135.0F;
                        } else {
                            this.setCommand(2);
                            this.setOrderedToSit(true);
                            this.m_6038_();
                            this.m_20359_(mount);
                        }
                        float birdYaw = yawAdd * 0.5F;
                        this.f_20883_ = Mth.wrapDegrees(((LivingEntity) mount).yBodyRot + birdYaw);
                        this.m_146922_(Mth.wrapDegrees(mount.getYRot() + birdYaw));
                        this.f_20885_ = Mth.wrapDegrees(((LivingEntity) mount).yHeadRot + birdYaw);
                        float radius = 0.6F;
                        float angle = (float) (Math.PI / 180.0) * (((LivingEntity) mount).yBodyRot - 180.0F + yawAdd);
                        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
                        double extraZ = (double) (radius * Mth.cos(angle));
                        this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) (mount.getBbHeight() * 0.45F), mount.getY()), mount.getZ() + extraZ);
                    }
                    if (!mount.isAlive()) {
                        this.m_6038_();
                    }
                }
            } else {
                super.m_6083_();
            }
        } else {
            this.m_8127_();
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevAttackProgress = this.attackProgress;
        this.prevBirdPitch = this.birdPitch;
        this.prevTackleProgress = this.tackleProgress;
        this.prevFlyProgress = this.flyProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevSwoopProgress = this.swoopProgress;
        this.prevSitProgress = this.sitProgress;
        float yMot = -((float) this.m_20184_().y * (180.0F / (float) Math.PI));
        this.birdPitch = yMot;
        if (this.isFlying()) {
            if (this.flyProgress < 5.0F) {
                this.flyProgress++;
            }
        } else if (this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.isTackling()) {
            if (this.tackleProgress < 5.0F) {
                this.tackleProgress++;
            }
        } else if (this.tackleProgress > 0.0F) {
            this.tackleProgress--;
        }
        boolean sit = this.isSitting() || this.m_20159_();
        if (sit) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.isLaunched()) {
            this.launchTime++;
        } else {
            this.launchTime = 0;
        }
        if (this.lastPlayerControlTime > 0) {
            this.lastPlayerControlTime--;
        }
        if (this.lastPlayerControlTime <= 0) {
            this.controlledFlag = false;
        }
        if (yMot < 0.1F) {
            this.flapAmount = Math.min(-yMot * 0.2F, 1.0F);
            if (this.swoopProgress > 0.0F) {
                this.swoopProgress--;
            }
        } else {
            if (this.flapAmount > 0.0F) {
                this.flapAmount = this.flapAmount - Math.min(this.flapAmount, 0.1F);
            } else {
                this.flapAmount = 0.0F;
            }
            if (this.swoopProgress < yMot * 0.2F) {
                this.swoopProgress = Math.min(yMot * 0.2F, this.swoopProgress + 1.0F);
            }
        }
        if (this.isTackling()) {
            this.flapAmount = Math.min(2.0F, this.flapAmount + 0.2F);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.isFlying()) {
                if (this.isLandNavigator) {
                    this.switchNavigator(false);
                }
            } else if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
            if (this.tackleCapCooldown == 0 && this.isTackling() && !this.m_20160_() && (this.m_5448_() == null || !this.m_5448_().isAlive())) {
                this.setTackling(false);
            }
            if (!this.isFlying()) {
                this.timeFlying = 0;
                this.m_20242_(false);
            } else {
                this.timeFlying++;
                this.m_20242_(true);
                if ((this.isSitting() || this.m_20159_() || this.m_27593_()) && !this.isLaunched()) {
                    this.setFlying(false);
                }
                if (this.m_5448_() != null && this.m_5448_().m_20186_() < this.m_20185_() && !this.m_20160_()) {
                    this.m_20256_(this.m_20184_().multiply(1.0, 0.9, 1.0));
                }
            }
            if (this.m_20072_() && this.m_20160_()) {
                this.m_20256_(this.m_20184_().add(0.0, 0.1F, 0.0));
            }
            if (this.isSitting() && !this.isLaunched()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.1F, 0.0));
            }
            if (this.m_5448_() != null && this.m_20072_()) {
                this.timeFlying = 0;
                this.setFlying(true);
            }
            if (this.m_20096_() && this.timeFlying > 30 && this.isFlying() && !this.m_20072_()) {
                this.setFlying(false);
            }
        }
        int attackTick = this.f_19804_.get(ATTACK_TICK);
        if (attackTick > 0) {
            if (attackTick == 2 && this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < (double) this.m_5448_().m_20205_() + 2.0) {
                this.m_5448_().hurt(this.m_269291_().mobAttack(this), 2.0F);
            }
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else if (this.attackProgress > 0.0F) {
            this.attackProgress--;
        }
        if (this.m_20159_()) {
            this.setFlying(false);
            this.setTackling(false);
        }
        if (this.f_19851_ > 0) {
            this.f_19851_--;
        }
        if (this.returnControlTime > 0) {
            this.returnControlTime--;
        }
        if (this.tackleCapCooldown > 0) {
            this.tackleCapCooldown--;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return AMEntityRegistry.BALD_EAGLE.get().create(p_241840_1_);
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public Vec3 getBlockInViewAway(Vec3 fleePos, float radiusAdd) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24) - radiusAdd;
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = new BlockPos((int) (fleePos.x() + extraX), 0, (int) (fleePos.z() + extraZ));
        BlockPos ground = this.getCrowGround(radialPos);
        int distFromGround = (int) this.m_20186_() - ground.m_123342_();
        int flightHeight = 7 + this.m_217043_().nextInt(10);
        BlockPos newPos = ground.above(distFromGround > 8 ? flightHeight : this.m_217043_().nextInt(7) + 4);
        return !this.isTargetBlocked(Vec3.atCenterOf(newPos)) && this.m_20238_(Vec3.atCenterOf(newPos)) > 1.0 ? Vec3.atCenterOf(newPos) : null;
    }

    private BlockPos getCrowGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 320 && !this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.above();
        }
        while (position.m_123342_() > -64 && !this.m_9236_().getBlockState(position).m_280296_()) {
            position = position.below();
        }
        return position;
    }

    public Vec3 getBlockGrounding(Vec3 fleePos) {
        float radius = -9.45F - (float) this.m_217043_().nextInt(24);
        float neg = this.m_217043_().nextBoolean() ? 1.0F : -1.0F;
        float renderYawOffset = this.f_20883_;
        float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F + this.m_217043_().nextFloat() * neg;
        double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = AMBlockPos.fromCoords(fleePos.x() + extraX, this.m_20186_(), fleePos.z() + extraZ);
        BlockPos ground = this.getCrowGround(radialPos);
        if (ground.m_123342_() == -64) {
            return this.m_20182_();
        } else {
            ground = this.m_20183_();
            while (ground.m_123342_() > -64 && !this.m_9236_().getBlockState(ground).m_280296_()) {
                ground = ground.below();
            }
            return !this.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? Vec3.atCenterOf(ground) : null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    private Vec3 getOrbitVec(Vec3 vector3d, float gatheringCircleDist) {
        float angle = (float) (Math.PI / 180.0) * (float) this.orbitDist * (float) (this.orbitClockwise ? -this.f_19797_ : this.f_19797_);
        double extraX = (double) (gatheringCircleDist * Mth.sin(angle));
        double extraZ = (double) (gatheringCircleDist * Mth.cos(angle));
        if (this.orbitPos != null) {
            Vec3 pos = new Vec3((double) this.orbitPos.m_123341_() + extraX, (double) (this.orbitPos.m_123342_() + this.f_19796_.nextInt(2) - 2), (double) this.orbitPos.m_123343_() + extraZ);
            if (this.m_9236_().m_46859_(AMBlockPos.fromVec3(pos))) {
                return pos;
            }
        }
        return null;
    }

    private boolean isOverWaterOrVoid() {
        BlockPos position = this.m_20183_();
        while (position.m_123342_() > -64 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return !this.m_9236_().getFluidState(position).isEmpty() || position.m_123342_() <= -64;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float radius = 0.3F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (0.3F * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (0.3F * Mth.cos(angle));
            passenger.setYRot(this.f_20883_ + 90.0F);
            if (passenger instanceof LivingEntity living) {
                living.yBodyRot = this.f_20883_ + 90.0F;
            }
            float extraY = 0.0F;
            if (passenger instanceof AbstractFish && !passenger.isInWaterOrBubble()) {
                extraY = 0.1F;
            }
            moveFunc.accept(passenger, this.m_20185_() + extraX, this.m_20186_() - 0.3F + (double) extraY + (double) (passenger.getBbHeight() * 0.3F), this.m_20189_() + extraZ);
            this.passengerTimer++;
            if (this.m_6084_() && this.passengerTimer > 0 && this.passengerTimer % 40 == 0) {
                passenger.hurt(this.m_269291_().mobAttack(this), 1.0F);
            }
        }
    }

    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        return new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
    }

    public boolean shouldHoodedReturn() {
        return this.m_269323_() == null || this.m_269323_().isAlive() && !this.m_269323_().m_6144_() ? !this.m_6084_() || this.f_19817_ || this.launchTime > 12000 || this.f_19818_ > 0 || this.m_213877_() : true;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        if (this.lastPlayerControlTime == 0 && !this.m_20159_()) {
            super.m_142687_(reason);
        }
    }

    public void directFromPlayer(float rotationYaw, float rotationPitch, boolean loadChunk, Entity over) {
        Entity owner = this.m_269323_();
        if (owner != null && this.m_20270_(owner) > 150.0F) {
            this.returnControlTime = 100;
        }
        if (!(Math.abs(this.f_19854_ - this.m_20185_()) > 0.1F) && !(Math.abs(this.f_19855_ - this.m_20186_()) > 0.1F) && !(Math.abs(this.f_19856_ - this.m_20189_()) > 0.1F)) {
            this.stillTicksCounter++;
        } else {
            this.stillTicksCounter = 0;
        }
        int stillTPthreshold = AMConfig.falconryTeleportsBack ? 200 : 6000;
        this.setOrderedToSit(false);
        this.setLaunched(true);
        if (owner != null && (this.returnControlTime > 0 && AMConfig.falconryTeleportsBack || this.stillTicksCounter > stillTPthreshold && this.m_20270_(owner) > 30.0F)) {
            this.m_20359_(owner);
            this.returnControlTime = 0;
            this.stillTicksCounter = 0;
            this.launchTime = Math.max(this.launchTime, 12000);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.returnControlTime > 0 && owner != null) {
                this.m_21563_().setLookAt(owner, 30.0F, 30.0F);
            } else {
                this.f_20883_ = rotationYaw;
                this.m_146922_(rotationYaw);
                this.f_20885_ = rotationYaw;
                this.m_146926_(rotationPitch);
            }
            if (rotationPitch < 10.0F && this.m_20096_()) {
                this.setFlying(true);
            }
            float yawOffset = rotationYaw + 90.0F;
            float rad = 3.0F;
            float speed = 1.2F;
            if (this.returnControlTime > 0) {
                this.m_21566_().setWantedPosition(owner.getX(), owner.getY() + 10.0, owner.getZ(), 1.2F);
            } else {
                this.m_21566_().setWantedPosition(this.m_20185_() + 4.5 * Math.cos((double) (yawOffset * (float) (Math.PI / 180.0))), this.m_20186_() - 3.0 * Math.sin((double) (rotationPitch * (float) (Math.PI / 180.0))), this.m_20189_() + 3.0 * Math.sin((double) (yawOffset * (float) (Math.PI / 180.0))), 1.2F);
            }
            if (loadChunk) {
                this.loadChunkOnServer(this.m_20183_());
            }
            this.m_6703_(null);
            this.m_6710_(null);
            if (over == null) {
                List<Entity> list = this.m_9236_().getEntities(this, this.m_20191_().inflate(3.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR);
                Entity closest = null;
                for (Entity e : list) {
                    if (closest == null || this.m_20270_(e) < this.m_20270_(closest)) {
                        closest = e;
                    }
                }
                over = closest;
            }
        }
        if (over != null && over != owner && !this.isAlliedTo(over) && this.canFalconryAttack(over) && this.tackleCapCooldown == 0 && (double) this.m_20270_(over) <= (double) over.getBbWidth() + 4.0) {
            this.setTackling(true);
            if ((double) this.m_20270_(over) <= (double) over.getBbWidth() + 2.0) {
                float speedDamage = (float) Math.ceil(Mth.clamp(this.m_20184_().length() + 0.2, 0.0, 1.2) * 3.333);
                over.hurt(this.m_269291_().mobAttack(this), 5.0F + speedDamage + (float) this.f_19796_.nextInt(2));
                this.tackleCapCooldown = 22;
            }
        }
        this.lastPlayerControlTime = 10;
        this.controlledFlag = true;
    }

    @Override
    public float getHandOffset() {
        return 0.8F;
    }

    private boolean canFalconryAttack(Entity over) {
        return !(over instanceof ItemEntity) && (!(over instanceof LivingEntity) || !this.m_21830_((LivingEntity) over));
    }

    public void awardKillScore(LivingEntity entity, int score, DamageSource src) {
        if (this.isLaunched() && this.hasCap() && this.m_21824_() && this.m_269323_() != null && this.m_269323_() instanceof ServerPlayer && this.m_20270_(this.m_269323_()) >= 100.0F) {
            AMAdvancementTriggerRegistry.BALD_EAGLE_CHALLENGE.trigger((ServerPlayer) this.m_269323_());
        }
        super.m_5993_(entity, score, src);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow) && this.isLaunched()) {
                amount = (amount + 1.0F) / 4.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    public void loadChunkOnServer(BlockPos center) {
        if (!this.m_9236_().isClientSide) {
            ServerLevel serverWorld = (ServerLevel) this.m_9236_();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    ChunkPos pos = new ChunkPos(this.m_20183_().offset(i * 16, 0, j * 16));
                    serverWorld.setChunkForced(pos.x, pos.z, true);
                }
            }
        }
    }

    @Override
    public void onLaunch(Player player, Entity pointedEntity) {
        this.setLaunched(true);
        this.setOrderedToSit(false);
        this.setCommand(0);
        if (this.hasCap()) {
            this.setFlying(true);
            this.m_21566_().setWantedPosition(this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.1F);
            if (this.m_9236_().isClientSide) {
                AlexsMobs.sendMSGToServer(new MessageMosquitoDismount(this.m_19879_(), player.m_19879_()));
            }
            AlexsMobs.PROXY.setRenderViewEntity(this);
        } else {
            this.m_21573_().stop();
            this.m_21566_().setWantedPosition(this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.1F);
            if (pointedEntity != null && pointedEntity.isAlive() && !this.isAlliedTo(pointedEntity)) {
                this.setFlying(true);
                if (pointedEntity instanceof LivingEntity pointedLivingEntity) {
                    this.m_6710_(pointedLivingEntity);
                }
            } else {
                this.setFlying(false);
                this.setCommand(2);
                this.setOrderedToSit(true);
            }
        }
    }

    private class AILandOnGlove extends Goal {

        protected EntityBaldEagle eagle;

        private int seperateTime = 0;

        public AILandOnGlove() {
            this.eagle = EntityBaldEagle.this;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return this.eagle.isLaunched() && !this.eagle.controlledFlag && this.eagle.m_21824_() && !this.eagle.m_20159_() && !this.eagle.m_20160_() && (this.eagle.m_5448_() == null || !this.eagle.m_5448_().isAlive());
        }

        @Override
        public void tick() {
            if (this.eagle.m_20184_().lengthSqr() < 0.03) {
                this.seperateTime++;
            }
            LivingEntity owner = this.eagle.m_269323_();
            if (owner != null) {
                if (this.seperateTime > 200) {
                    this.seperateTime = 0;
                    this.eagle.m_20359_(owner);
                }
                this.eagle.setFlying(true);
                double d0 = this.eagle.m_20185_() - owner.m_20185_();
                double d2 = this.eagle.m_20189_() - owner.m_20189_();
                double xzDist = Math.sqrt(d0 * d0 + d2 * d2);
                double yAdd = xzDist > 14.0 ? 5.0 : 0.0;
                this.eagle.m_21566_().setWantedPosition(owner.m_20185_(), owner.m_20186_() + yAdd + (double) owner.m_20192_(), owner.m_20189_(), 1.0);
                if ((double) this.eagle.m_20270_(owner) < (double) owner.m_20205_() + 1.4) {
                    this.eagle.setLaunched(false);
                    if (this.eagle.getRidingFalcons(owner) <= 0) {
                        this.eagle.m_20329_(owner);
                        if (!this.eagle.m_9236_().isClientSide) {
                            AlexsMobs.sendMSGToAll(new MessageMosquitoMountPlayer(this.eagle.m_19879_(), owner.m_19879_()));
                        }
                    } else {
                        this.eagle.setCommand(2);
                        this.eagle.setOrderedToSit(true);
                    }
                }
            }
        }

        @Override
        public void stop() {
            this.seperateTime = 0;
        }
    }

    private class AITackle extends Goal {

        protected EntityBaldEagle eagle;

        private int circleTime;

        private int maxCircleTime = 10;

        public AITackle() {
            this.eagle = EntityBaldEagle.this;
        }

        @Override
        public boolean canUse() {
            return this.eagle.m_5448_() != null && !this.eagle.controlledFlag && !this.eagle.m_20160_();
        }

        @Override
        public void start() {
            this.eagle.orbitPos = null;
        }

        @Override
        public void stop() {
            this.circleTime = 0;
            this.maxCircleTime = 60 + EntityBaldEagle.this.f_19796_.nextInt(60);
        }

        @Override
        public void tick() {
            LivingEntity target = this.eagle.m_5448_();
            boolean smallPrey = target != null && target.m_20206_() < 1.0F && target.m_20205_() < 0.7F && !(target instanceof EntityBaldEagle) || target instanceof AbstractFish;
            if (this.eagle.orbitPos != null && this.circleTime < this.maxCircleTime) {
                this.circleTime++;
                this.eagle.setTackling(false);
                this.eagle.setFlying(true);
                if (target != null) {
                    int i = 0;
                    int up = 2 + this.eagle.m_217043_().nextInt(4);
                    for (this.eagle.orbitPos = target.m_20183_().above((int) target.m_20206_()); this.eagle.m_9236_().m_46859_(this.eagle.orbitPos) && i < up; this.eagle.orbitPos = this.eagle.orbitPos.above()) {
                        i++;
                    }
                }
                Vec3 vec = this.eagle.getOrbitVec(Vec3.ZERO, (float) (4 + EntityBaldEagle.this.f_19796_.nextInt(2)));
                if (vec != null) {
                    this.eagle.m_21566_().setWantedPosition(vec.x, vec.y, vec.z, 1.2F);
                }
            } else if (target != null) {
                if (!this.eagle.isFlying() && !this.eagle.m_20072_()) {
                    this.eagle.m_21573_().moveTo(target, 1.0);
                } else {
                    double d0 = this.eagle.m_20185_() - target.m_20185_();
                    double d2 = this.eagle.m_20189_() - target.m_20189_();
                    double xzDist = Math.sqrt(d0 * d0 + d2 * d2);
                    double yAddition = (double) target.m_20206_();
                    if (xzDist > 15.0) {
                        yAddition = 3.0;
                    }
                    this.eagle.setTackling(true);
                    this.eagle.m_21566_().setWantedPosition(target.m_20185_(), target.m_20186_() + yAddition, target.m_20189_(), this.eagle.m_20072_() ? 1.3F : 1.0);
                }
                if (this.eagle.m_20270_(target) < target.m_20205_() + 2.5F) {
                    if (this.eagle.isTackling()) {
                        if (smallPrey) {
                            this.eagle.setFlying(true);
                            this.eagle.timeFlying = 0;
                            float radius = 0.3F;
                            float angle = (float) (Math.PI / 180.0) * this.eagle.f_20883_;
                            double extraX = (double) (0.3F * Mth.sin((float) Math.PI + angle));
                            double extraZ = (double) (0.3F * Mth.cos(angle));
                            target.m_146922_(this.eagle.f_20883_ + 90.0F);
                            if (target instanceof LivingEntity) {
                                target.yBodyRot = this.eagle.f_20883_ + 90.0F;
                            }
                            target.m_6034_(this.eagle.m_20185_() + extraX, this.eagle.m_20186_() - 0.4F + (double) (target.m_20206_() * 0.45F), this.eagle.m_20189_() + extraZ);
                            target.m_7998_(this.eagle, true);
                        } else {
                            target.hurt(this.eagle.m_269291_().mobAttack(this.eagle), 5.0F);
                            this.eagle.setFlying(false);
                            this.eagle.orbitPos = target.m_20183_().above(2);
                            this.circleTime = 0;
                            this.maxCircleTime = 60 + EntityBaldEagle.this.f_19796_.nextInt(60);
                        }
                    } else {
                        this.eagle.doHurtTarget(target);
                    }
                } else if (this.eagle.m_20270_(target) > 12.0F || target.m_20072_()) {
                    this.eagle.setFlying(true);
                }
            }
            if (this.eagle.isLaunched()) {
                this.eagle.setFlying(true);
            }
        }
    }

    private class AIWanderIdle extends Goal {

        protected final EntityBaldEagle eagle;

        protected double x;

        protected double y;

        protected double z;

        private boolean flightTarget = false;

        private int orbitResetCooldown = 0;

        private int maxOrbitTime = 360;

        private int orbitTime = 0;

        public AIWanderIdle() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.eagle = EntityBaldEagle.this;
        }

        @Override
        public boolean canUse() {
            if (this.orbitResetCooldown < 0) {
                this.orbitResetCooldown++;
            }
            if ((this.eagle.m_5448_() == null || !this.eagle.m_5448_().isAlive() || this.eagle.m_20160_()) && !this.eagle.m_20159_() && !this.eagle.isSitting() && !this.eagle.controlledFlag) {
                if (this.eagle.m_217043_().nextInt(15) != 0 && !this.eagle.isFlying()) {
                    return false;
                } else {
                    if (this.eagle.m_6162_()) {
                        this.flightTarget = false;
                    } else if (this.eagle.m_20072_()) {
                        this.flightTarget = true;
                    } else if (this.eagle.hasCap()) {
                        this.flightTarget = false;
                    } else if (this.eagle.m_20096_()) {
                        this.flightTarget = EntityBaldEagle.this.f_19796_.nextBoolean();
                    } else {
                        if (this.orbitResetCooldown == 0 && EntityBaldEagle.this.f_19796_.nextInt(6) == 0) {
                            this.orbitResetCooldown = 400;
                            this.eagle.orbitPos = this.eagle.m_20183_();
                            this.eagle.orbitDist = (double) (4 + EntityBaldEagle.this.f_19796_.nextInt(5));
                            this.eagle.orbitClockwise = EntityBaldEagle.this.f_19796_.nextBoolean();
                            this.orbitTime = 0;
                            this.maxOrbitTime = (int) (360.0F + 360.0F * EntityBaldEagle.this.f_19796_.nextFloat());
                        }
                        this.flightTarget = this.eagle.m_20160_() || EntityBaldEagle.this.f_19796_.nextInt(7) > 0 && this.eagle.timeFlying < 700;
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
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            if (this.orbitResetCooldown > 0) {
                this.orbitResetCooldown--;
            }
            if (this.orbitResetCooldown < 0) {
                this.orbitResetCooldown++;
            }
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                if (this.orbitTime < this.maxOrbitTime && !this.eagle.m_20072_()) {
                    this.orbitTime++;
                } else {
                    this.orbitTime = 0;
                    this.eagle.orbitPos = null;
                    this.orbitResetCooldown = -400 - EntityBaldEagle.this.f_19796_.nextInt(400);
                }
            }
            if (this.eagle.f_19862_ && !this.eagle.m_20096_()) {
                this.stop();
            }
            if (this.flightTarget) {
                this.eagle.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else if (this.eagle.m_20096_() || !this.eagle.isFlying()) {
                this.eagle.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            } else if (!this.eagle.m_20072_()) {
                this.eagle.m_20256_(this.eagle.m_20184_().multiply(1.2F, 0.6F, 1.2F));
            }
            if (!this.flightTarget && this.eagle.m_20096_() && EntityBaldEagle.this.isFlying()) {
                this.eagle.setFlying(false);
                this.orbitTime = 0;
                this.eagle.orbitPos = null;
                this.orbitResetCooldown = -400 - EntityBaldEagle.this.f_19796_.nextInt(400);
            }
            if (this.eagle.timeFlying > 30 && EntityBaldEagle.this.isFlying() && (!EntityBaldEagle.this.m_9236_().m_46859_(this.eagle.m_20099_()) || this.eagle.m_20096_()) && !this.eagle.m_20072_()) {
                this.eagle.setFlying(false);
                this.orbitTime = 0;
                this.eagle.orbitPos = null;
                this.orbitResetCooldown = -400 - EntityBaldEagle.this.f_19796_.nextInt(400);
            }
        }

        @Nullable
        protected Vec3 getPosition() {
            Vec3 vector3d = this.eagle.m_20182_();
            if (this.eagle.m_21824_() && this.eagle.getCommand() == 1 && this.eagle.m_269323_() != null) {
                vector3d = this.eagle.m_269323_().m_20182_();
                this.eagle.orbitPos = this.eagle.m_269323_().m_20183_();
            }
            if (this.orbitResetCooldown > 0 && this.eagle.orbitPos != null) {
                return this.eagle.getOrbitVec(vector3d, (float) (4 + EntityBaldEagle.this.f_19796_.nextInt(2)));
            } else {
                if (this.eagle.m_20160_() || this.eagle.isOverWaterOrVoid()) {
                    this.flightTarget = true;
                }
                if (this.flightTarget) {
                    return this.eagle.timeFlying >= 500 && !this.eagle.m_20160_() && !this.eagle.isOverWaterOrVoid() ? this.eagle.getBlockGrounding(vector3d) : this.eagle.getBlockInViewAway(vector3d, 0.0F);
                } else {
                    return LandRandomPos.getPos(this.eagle, 10, 7);
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (this.eagle.isSitting()) {
                return false;
            } else {
                return this.flightTarget ? this.eagle.isFlying() && this.eagle.m_20275_(this.x, this.y, this.z) > 2.0 : !this.eagle.m_21573_().isDone() && !this.eagle.m_20160_();
            }
        }

        @Override
        public void start() {
            if (this.flightTarget) {
                this.eagle.setFlying(true);
                this.eagle.m_21566_().setWantedPosition(this.x, this.y, this.z, 1.0);
            } else {
                this.eagle.m_21573_().moveTo(this.x, this.y, this.z, 1.0);
            }
        }

        @Override
        public void stop() {
            this.eagle.m_21573_().stop();
            super.stop();
        }
    }

    static class MoveHelper extends MoveControl {

        private final EntityBaldEagle parentEntity;

        public MoveHelper(EntityBaldEagle bird) {
            super(bird);
            this.parentEntity = bird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < 0.3) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.05 / d5)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    this.parentEntity.m_146922_(-((float) Mth.atan2(vector3d1.x, vector3d1.z)) * (180.0F / (float) Math.PI));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }

        private boolean canReach(Vec3 p_220673_1_, int p_220673_2_) {
            AABB axisalignedbb = this.parentEntity.m_20191_();
            for (int i = 1; i < p_220673_2_; i++) {
                axisalignedbb = axisalignedbb.move(p_220673_1_);
                if (!this.parentEntity.m_9236_().m_45756_(this.parentEntity, axisalignedbb)) {
                    return false;
                }
            }
            return true;
        }
    }
}