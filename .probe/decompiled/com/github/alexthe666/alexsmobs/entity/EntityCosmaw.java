package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAITemptDistance;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.EntityAINearestTarget3D;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.google.common.base.Predicates;
import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityCosmaw extends TamableAnimal implements ITargetsDroppedItems, FlyingAnimal, IFollower {

    private static final EntityDataAccessor<Float> COSMAW_PITCH = SynchedEntityData.defineId(EntityCosmaw.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(EntityCosmaw.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityCosmaw.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityCosmaw.class, EntityDataSerializers.INT);

    public float clutchProgress;

    public float prevClutchProgress;

    public float openProgress;

    public float prevOpenProgress;

    public float prevCosmawPitch;

    public float biteProgress;

    public float prevBiteProgress;

    private float stuckRot = (float) (this.f_19796_.nextInt(3) * 90);

    private UUID fishThrowerID;

    private int heldItemTime;

    private BlockPos lastSafeTpPosition;

    protected EntityCosmaw(EntityType<? extends TamableAnimal> type, Level lvl) {
        super(type, lvl);
        this.f_21342_ = new FlightMoveController(this, 1.0F, false, true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.cosmawSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    public static boolean canCosmawSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return !worldIn.m_8055_(pos.below()).m_60795_();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(COSMAW_PITCH, 0.0F);
        this.f_19804_.define(ATTACK_TICK, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
    }

    @Override
    protected void onBelowWorld() {
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.f_19804_.get(ATTACK_TICK) == 0 && this.biteProgress == 0.0F) {
            this.f_19804_.set(ATTACK_TICK, 5);
        }
        return true;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntityCosmaw.AIAttack());
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new FlyingAIFollowOwner(this, 1.3, 8.0F, 4.0F, false));
        this.f_21345_.addGoal(4, new EntityCosmaw.AIPickupOwner());
        this.f_21345_.addGoal(5, new BreedGoal(this, 1.2));
        this.f_21345_.addGoal(6, new AnimalAITemptDistance(this, 1.1, Ingredient.of(Items.CHORUS_FRUIT, AMItemRegistry.COSMIC_COD.get()), false, 25.0) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntityCosmaw.this.m_21205_().isEmpty();
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && EntityCosmaw.this.m_21205_().isEmpty();
            }
        });
        this.f_21345_.addGoal(7, new EntityCosmaw.RandomFlyGoal(this));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, true));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this) {

            @Override
            public boolean canUse() {
                LivingEntity livingentity = this.f_26135_.m_21188_();
                return livingentity != null && EntityCosmaw.this.m_21830_(livingentity) ? false : super.canUse();
            }
        });
        this.f_21346_.addGoal(3, new EntityAINearestTarget3D(this, EntityCosmicCod.class, 80, true, false, Predicates.alwaysTrue()));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.COSMAW_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.COSMAW_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.COSMAW_HURT.get();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return this.m_21824_() && stack.is(AMItemRegistry.COSMIC_COD.get());
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    public float getClampedCosmawPitch(float partialTick) {
        float f = this.prevCosmawPitch + (this.getCosmawPitch() - this.prevCosmawPitch) * partialTick;
        return Mth.clamp(f, -90.0F, 90.0F);
    }

    public float getCosmawPitch() {
        return this.f_19804_.get(COSMAW_PITCH);
    }

    public void setCosmawPitch(float pitch) {
        this.f_19804_.set(COSMAW_PITCH, pitch);
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
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float f = this.f_267362_.position();
            float f1 = this.f_267362_.speed();
            float bob = (float) (Math.sin((double) (f * 0.7F)) * (double) f1 * 0.0625 * 1.6F - (double) (f1 * 0.0625F * 1.6F));
            passenger.setPos(this.m_20185_(), this.m_20186_() - (double) bob + 0.3F - this.m_6048_(), this.m_20189_());
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("CosmawSitting", this.isSitting());
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("CosmawSitting"));
        this.setCommand(compound.getInt("Command"));
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevOpenProgress = this.openProgress;
        this.prevClutchProgress = this.clutchProgress;
        this.prevBiteProgress = this.biteProgress;
        this.prevCosmawPitch = this.getCosmawPitch();
        if (!this.m_9236_().isClientSide) {
            float f2 = -((float) this.m_20184_().y * (180.0F / (float) Math.PI));
            this.setCosmawPitch(this.getCosmawPitch() + 0.6F * (this.getCosmawPitch() + f2) - this.getCosmawPitch());
        }
        if (this.isMouthOpen()) {
            if (this.openProgress < 5.0F) {
                this.openProgress++;
            }
        } else if (this.openProgress > 0.0F) {
            this.openProgress--;
        }
        if (this.m_20160_()) {
            if (this.clutchProgress < 5.0F) {
                this.clutchProgress++;
            }
        } else if (this.clutchProgress > 0.0F) {
            this.clutchProgress--;
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            if (this.biteProgress < 5.0F) {
                this.biteProgress = Math.min(5.0F, this.biteProgress + 2.0F);
            } else {
                if (this.m_5448_() != null && (double) this.m_20270_(this.m_5448_()) < 3.3) {
                    if (this.m_5448_() instanceof EntityCosmicCod && !this.m_21824_()) {
                        EntityCosmicCod fish = (EntityCosmicCod) this.m_5448_();
                        CompoundTag fishNbt = new CompoundTag();
                        fish.addAdditionalSaveData(fishNbt);
                        fishNbt.putString("DeathLootTable", BuiltInLootTables.EMPTY.toString());
                        fish.readAdditionalSaveData(fishNbt);
                    }
                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21133_(Attributes.ATTACK_DAMAGE));
                }
                this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            }
        } else if (this.biteProgress > 0.0F) {
            this.biteProgress--;
        }
        if (!this.m_21205_().isEmpty()) {
            this.heldItemTime++;
            if (this.heldItemTime > 30 && this.canTargetItem(this.m_21205_())) {
                this.heldItemTime = 0;
                this.m_5634_(4.0F);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.DOLPHIN_EAT, this.m_6121_(), this.m_6100_());
                if (this.m_21205_().getItem() == AMItemRegistry.COSMIC_COD.get() && this.fishThrowerID != null && !this.m_21824_()) {
                    if (this.m_217043_().nextFloat() < 0.3F) {
                        this.m_7105_(true);
                        this.setCommand(1);
                        this.m_21816_(this.fishThrowerID);
                        Player player = this.m_9236_().m_46003_(this.fishThrowerID);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player, this);
                        }
                        this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.m_21205_().shrink(1);
            }
        } else {
            this.heldItemTime = 0;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ % 100 == 0 || this.lastSafeTpPosition == null) {
                BlockPos pos = this.getCosmawGround(this.m_20183_());
                if (pos.m_123342_() > 1) {
                    this.lastSafeTpPosition = pos;
                }
            }
            if (this.m_20160_()) {
                if (this.lastSafeTpPosition != null) {
                    double dist = this.m_20238_(Vec3.atCenterOf(this.lastSafeTpPosition));
                    float speed = 0.8F;
                    if (this.m_20186_() < -40.0) {
                        speed = 3.0F;
                    }
                    if (this.f_19863_ && dist > 14.0) {
                        this.m_146922_(this.stuckRot);
                        if (this.f_19796_.nextInt(50) == 0) {
                            this.stuckRot = Mth.wrapDegrees(this.stuckRot + 90.0F);
                        }
                        float angle = (float) (Math.PI / 180.0) * this.stuckRot;
                        double extraX = (double) (-2.0F * Mth.sin((float) Math.PI + angle));
                        double extraZ = (double) (-2.0F * Mth.cos(angle));
                        this.m_21566_().setWantedPosition(this.m_20185_() + extraX, this.m_20186_() + 2.0, this.m_20189_() + extraZ, (double) speed);
                    } else if ((double) this.lastSafeTpPosition.m_123342_() > this.m_20186_() + 2.3F) {
                        this.m_21566_().setWantedPosition(this.m_20185_(), this.m_20186_() + 2.0, this.m_20189_(), (double) speed);
                    } else {
                        this.m_21566_().setWantedPosition((double) this.lastSafeTpPosition.m_123341_(), (double) (this.lastSafeTpPosition.m_123342_() + 2), (double) this.lastSafeTpPosition.m_123343_(), (double) speed);
                    }
                    if (dist < 7.0 && this.getCosmawGround(this.m_20183_()).m_123342_() > 1) {
                        this.m_20153_();
                    }
                } else if (this.m_20186_() < 0.0) {
                    this.m_20184_().add(0.0, 0.75, 0.0);
                } else if (this.m_20186_() < 80.0) {
                    this.m_20184_().add(0.0, 0.1F, 0.0);
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        InteractionResult type = super.m_6071_(player, hand);
        InteractionResult interactionresult = stack.interactLivingEntity(player, this, hand);
        if (this.canTargetItem(stack) && this.m_21205_().isEmpty()) {
            ItemStack rippedStack = stack.copy();
            rippedStack.setCount(1);
            stack.shrink(1);
            this.m_21008_(InteractionHand.MAIN_HAND, rippedStack);
            if (rippedStack.getItem() == AMItemRegistry.COSMIC_COD.get()) {
                this.fishThrowerID = player.m_20148_();
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && this.m_21830_(player) && !this.m_6162_() && interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS) {
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
            return type;
        }
    }

    public boolean isMouthOpen() {
        return !this.m_21205_().isEmpty();
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new DirectPathNavigator(this, level, 0.5F);
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob parent) {
        return AMEntityRegistry.COSMAW.get().create(this.m_9236_());
    }

    private BlockPos getCosmawGround(BlockPos in) {
        BlockPos position = new BlockPos(in.m_123341_(), (int) this.m_20186_(), in.m_123343_());
        while (position.m_123342_() < 256 && !this.m_9236_().getFluidState(position).isEmpty()) {
            position = position.above();
        }
        while (position.m_123342_() > 1 && this.m_9236_().m_46859_(position)) {
            position = position.below();
        }
        return position;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.getItem() == AMItemRegistry.COSMIC_COD.get() || stack.getItem() == Items.CHORUS_FRUIT;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        Entity itemThrower = e.getOwner();
        if (e.getItem().getItem() == Items.PUMPKIN_SEEDS && !this.m_21824_() && itemThrower != null) {
            this.fishThrowerID = itemThrower.getUUID();
        } else {
            this.fishThrowerID = null;
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() != HitResult.Type.MISS;
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1 && !this.m_20160_();
    }

    private boolean shouldWander() {
        if (this.m_20160_()) {
            return false;
        } else if (this.m_21824_()) {
            int command = this.getCommand();
            if (command != 2 && !this.isSitting()) {
                return command == 1 && this.m_269323_() != null && this.m_20270_(this.m_269323_()) < 10.0F ? true : command == 0;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void push(Entity entity) {
        if (!this.m_21824_() || !(entity instanceof LivingEntity) || !this.m_21830_((LivingEntity) entity)) {
            super.m_7334_(entity);
        }
    }

    public boolean canRiderInteract() {
        return true;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    private class AIAttack extends Goal {

        public AIAttack() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntityCosmaw.this.m_5448_() != null && EntityCosmaw.this.m_5448_().isAlive();
        }

        @Override
        public void tick() {
            if ((double) EntityCosmaw.this.m_20270_(EntityCosmaw.this.m_5448_()) < 3.0 * (double) (EntityCosmaw.this.m_6162_() ? 0.5F : 1.0F)) {
                EntityCosmaw.this.doHurtTarget(EntityCosmaw.this.m_5448_());
            } else {
                EntityCosmaw.this.m_21573_().moveTo(EntityCosmaw.this.m_5448_(), 1.0);
            }
        }
    }

    private class AIPickupOwner extends Goal {

        private LivingEntity owner;

        @Override
        public boolean canUse() {
            if (EntityCosmaw.this.m_21824_() && EntityCosmaw.this.m_269323_() != null && !EntityCosmaw.this.isSitting() && !EntityCosmaw.this.m_269323_().m_20159_() && !EntityCosmaw.this.m_269323_().m_20096_() && EntityCosmaw.this.m_269323_().f_19789_ > 4.0F) {
                this.owner = EntityCosmaw.this.m_269323_();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            if (this.owner != null && (!this.owner.isFallFlying() || this.owner.m_20186_() < -30.0)) {
                double dist = (double) EntityCosmaw.this.m_20270_(this.owner);
                if (dist < 3.0 || this.owner.m_20186_() <= -50.0) {
                    this.owner.f_19789_ = 0.0F;
                    this.owner.m_20329_(EntityCosmaw.this);
                } else if (!(dist > 100.0) && !(this.owner.m_20186_() <= -20.0)) {
                    EntityCosmaw.this.m_21573_().moveTo(this.owner, 1.0 + Math.min(dist * 0.3F, 3.0));
                } else {
                    EntityCosmaw.this.m_6021_(this.owner.m_20185_(), this.owner.m_20186_() - 1.0, this.owner.m_20189_());
                }
            }
        }
    }

    static class RandomFlyGoal extends Goal {

        private final EntityCosmaw parentEntity;

        private BlockPos target = null;

        public RandomFlyGoal(EntityCosmaw mosquito) {
            this.parentEntity = mosquito;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.parentEntity.m_21573_().isDone() && this.parentEntity.shouldWander() && this.parentEntity.m_5448_() == null && this.parentEntity.m_217043_().nextInt(4) == 0) {
                this.target = this.getBlockInViewCosmaw();
                if (this.target != null) {
                    this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.parentEntity.shouldWander() && this.parentEntity.m_5448_() == null;
        }

        @Override
        public void stop() {
            this.target = null;
        }

        @Override
        public void tick() {
            if (this.target != null) {
                this.parentEntity.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 1.0);
                if (this.parentEntity.m_20238_(Vec3.atCenterOf(this.target)) < 4.0 || this.parentEntity.f_19862_) {
                    this.target = null;
                }
            }
        }

        public BlockPos getBlockInViewCosmaw() {
            float radius = (float) (5 + this.parentEntity.m_217043_().nextInt(10));
            float neg = this.parentEntity.m_217043_().nextBoolean() ? 1.0F : -1.0F;
            float renderYawOffset = this.parentEntity.m_146908_();
            float angle = (float) (Math.PI / 180.0) * renderYawOffset + 3.15F * this.parentEntity.m_217043_().nextFloat() * neg;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            BlockPos radialPos = AMBlockPos.fromCoords(this.parentEntity.m_20185_() + extraX, this.parentEntity.m_20186_(), this.parentEntity.m_20189_() + extraZ);
            BlockPos ground = this.parentEntity.getCosmawGround(radialPos);
            if (ground.m_123342_() <= 1) {
                ground = ground.above(70 + this.parentEntity.f_19796_.nextInt(4));
            } else {
                ground = ground.above(2 + this.parentEntity.f_19796_.nextInt(2));
            }
            return !this.parentEntity.isTargetBlocked(Vec3.atCenterOf(ground.above())) ? ground : null;
        }
    }
}