package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAILootChests;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.ILootsChests;
import com.github.alexthe666.alexsmobs.entity.ai.RaccoonAIBeg;
import com.github.alexthe666.alexsmobs.entity.ai.RaccoonAIWash;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIDestroyTurtleEggs;
import com.github.alexthe666.alexsmobs.entity.ai.TameableAIFollowOwner;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.Container;
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
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidType;

public class EntityRaccoon extends TamableAnimal implements IAnimatedEntity, IFollower, ITargetsDroppedItems, ILootsChests {

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BEGGING = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> WASHING = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> WASH_POS = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CARPET_COLOR = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.INT);

    public float prevStandProgress;

    public float standProgress;

    public float prevBegProgress;

    public float begProgress;

    public float prevWashProgress;

    public float washProgress;

    public float prevSitProgress;

    public float sitProgress;

    public int maxStandTime = 75;

    private int standingTime = 0;

    private int stealCooldown = 0;

    public int lookForWaterBeforeEatingTimer = 0;

    private int animationTick;

    private Animation currentAnimation;

    private int pickupItemCooldown = 0;

    @Nullable
    private UUID eggThrowerUUID = null;

    public boolean forcedSit = false;

    public static final Animation ANIMATION_ATTACK = Animation.create(12);

    private static final TargetingConditions VILLAGER_STEAL_PREDICATE = TargetingConditions.forCombat().range(20.0).ignoreLineOfSight();

    private static final TargetingConditions IRON_GOLEM_PREDICATE = TargetingConditions.forCombat().range(20.0).ignoreLineOfSight();

    protected EntityRaccoon(EntityType type, Level world) {
        super(type, world);
        this.m_21441_(BlockPathTypes.WATER_BORDER, 0.0F);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.RACCOON_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.RACCOON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.RACCOON_HURT.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.raccoonSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(2, new RaccoonAIWash(this));
        this.f_21345_.addGoal(3, new TameableAIFollowOwner(this, 1.3, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(4, new FloatGoal(this));
        this.f_21345_.addGoal(5, new LeapAtTargetGoal(this, 0.4F));
        this.f_21345_.addGoal(6, new MeleeAttackGoal(this, 1.1, true));
        this.f_21345_.addGoal(7, new AnimalAILootChests(this, 16));
        this.f_21345_.addGoal(8, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(9, new RaccoonAIBeg(this, 0.65));
        this.f_21345_.addGoal(10, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(11, new EntityRaccoon.AIStealFromVillagers(this));
        this.f_21345_.addGoal(12, new EntityRaccoon.StrollGoal(200));
        this.f_21345_.addGoal(13, new TameableAIDestroyTurtleEggs(this, 1.0, 3));
        this.f_21345_.addGoal(14, new AnimalAIWanderRanged(this, 120, 1.0, 14, 7));
        this.f_21345_.addGoal(15, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(15, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new AnimalAIHurtByTargetNotBaby(this));
        this.f_21346_.addGoal(1, new CreatureAITargetItems(this, false));
        this.f_21346_.addGoal(3, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new OwnerHurtTargetGoal(this));
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (!(entityIn instanceof EntityBlueJay jay)) {
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
        } else {
            return jay.getRaccoonUUID() != null && jay.getRaccoonUUID().equals(this.m_20148_());
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_ATTACK);
        }
        return true;
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.getColor() != null) {
            if (!this.m_9236_().isClientSide) {
                this.m_19998_(this.getCarpetItemBeingWorn());
            }
            this.setColor(null);
        }
    }

    @Nullable
    public DyeColor getColor() {
        int lvt_1_1_ = this.f_19804_.get(CARPET_COLOR);
        return lvt_1_1_ == -1 ? null : DyeColor.byId(lvt_1_1_);
    }

    public void setColor(@Nullable DyeColor color) {
        this.f_19804_.set(CARPET_COLOR, color == null ? -1 : color.getId());
    }

    public Item getCarpetItemBeingWorn() {
        return this.getColor() != null ? (Item) EntityElephant.DYE_COLOR_ITEM_MAP.get(this.getColor()) : Items.AIR;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == Items.BREAD;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        boolean owner = this.m_21824_() && this.m_21830_(player);
        if (item == Items.GLOW_BERRIES && this.bondWithBlueJays(player.m_20148_())) {
            this.m_142075_(player, hand, itemstack);
            this.m_9236_().broadcastEntityEvent(this, (byte) 93);
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && !this.m_21205_().isEmpty()) {
            if (!this.m_9236_().isClientSide) {
                this.m_19983_(this.m_21205_().copy());
            }
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.pickupItemCooldown = 60;
            return InteractionResult.SUCCESS;
        } else if (owner && itemstack.is(ItemTags.WOOL_CARPETS)) {
            DyeColor color = EntityElephant.getCarpetColor(itemstack);
            if (color != this.getColor()) {
                if (this.getColor() != null) {
                    this.m_19998_(this.getCarpetItemBeingWorn());
                }
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                this.m_5496_(SoundEvents.LLAMA_SWAG, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                itemstack.shrink(1);
                this.setColor(color);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else if (owner && this.getColor() != null && itemstack.getItem() == Items.SHEARS) {
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SHEEP_SHEAR, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            if (this.getColor() != null) {
                this.m_19998_(this.getCarpetItemBeingWorn());
            }
            this.setColor(null);
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && isRaccoonFood(itemstack) && !this.isFood(itemstack) && this.m_21223_() < this.m_21233_()) {
            if (this.m_21205_().isEmpty()) {
                ItemStack copy = itemstack.copy();
                copy.setCount(1);
                this.m_21008_(InteractionHand.MAIN_HAND, copy);
                this.onEatItem();
                if (itemstack.hasCraftingRemainingItem()) {
                    this.m_19983_(itemstack.getCraftingRemainingItem());
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            } else {
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
            }
            this.m_142075_(player, hand, itemstack);
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && this.m_21824_() && this.m_21830_(player) && !isRaccoonFood(itemstack) && !player.m_6144_()) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.forcedSit = true;
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.forcedSit = false;
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            } else {
                return type;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("RacSitting", this.isSitting());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putInt("RacCommand", this.getCommand());
        compound.putInt("Carpet", this.f_19804_.get(CARPET_COLOR));
        compound.putInt("StealCooldown", this.stealCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOrderedToSit(compound.getBoolean("RacSitting"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.setCommand(compound.getInt("RacCommand"));
        this.f_19804_.set(CARPET_COLOR, compound.getInt("Carpet"));
        this.stealCooldown = compound.getInt("StealCooldown");
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    public static boolean isRaccoonFood(ItemStack stack) {
        return stack.isEdible() || stack.is(AMTagRegistry.RACCOON_FOODSTUFFS);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 9.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_6673_(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            this.setOrderedToSit(false);
            if (entity != null && this.m_21824_() && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                amount = (amount + 1.0F) / 4.0F;
            }
            return super.m_6469_(source, amount);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevStandProgress = this.standProgress;
        this.prevBegProgress = this.begProgress;
        this.prevWashProgress = this.washProgress;
        this.prevSitProgress = this.sitProgress;
        if (this.isStanding()) {
            if (this.standProgress < 5.0F) {
                this.standProgress++;
            }
        } else if (this.standProgress > 0.0F) {
            this.standProgress--;
        }
        if (this.isBegging()) {
            if (this.begProgress < 5.0F) {
                this.begProgress++;
            }
        } else if (this.begProgress > 0.0F) {
            this.begProgress--;
        }
        if (this.isWashing()) {
            if (this.washProgress < 5.0F) {
                this.washProgress++;
            }
        } else if (this.washProgress > 0.0F) {
            this.washProgress--;
        }
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.lookForWaterBeforeEatingTimer > 0) {
                this.lookForWaterBeforeEatingTimer--;
            } else if (!this.isWashing() && this.canTargetItem(this.m_21205_())) {
                this.onEatItem();
                if (this.m_21205_().hasCraftingRemainingItem()) {
                    this.m_19983_(this.m_21205_().getCraftingRemainingItem());
                }
                this.m_21205_().shrink(1);
            }
        }
        if (this.isWashing() && this.getWashPos() != null) {
            BlockPos washingPos = this.getWashPos();
            if (this.m_20275_((double) washingPos.m_123341_() + 0.5, (double) washingPos.m_123342_() + 0.5, (double) washingPos.m_123343_() + 0.5) < 3.0) {
                for (int j = 0; (float) j < 4.0F; j++) {
                    double d2 = this.f_19796_.nextDouble();
                    double d3 = this.f_19796_.nextDouble();
                    Vec3 vector3d = this.m_20184_();
                    this.m_9236_().addParticle(ParticleTypes.SPLASH, (double) washingPos.m_123341_() + d2, (double) ((float) washingPos.m_123342_() + 0.8F), (double) washingPos.m_123343_() + d3, vector3d.x, vector3d.y, vector3d.z);
                }
            } else {
                this.setWashing(false);
            }
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() != null && this.m_142582_(this.m_5448_()) && this.m_20270_(this.m_5448_()) < 4.0F && this.getAnimation() == ANIMATION_ATTACK && this.getAnimationTick() == 5) {
            float f1 = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add((double) (-Mth.sin(f1) * -0.06F), 0.0, (double) (Mth.cos(f1) * -0.06F)));
            this.m_5448_().knockback(0.35F, this.m_5448_().m_20185_() - this.m_20185_(), this.m_5448_().m_20189_() - this.m_20189_());
            this.m_5448_().hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
        }
        if (this.stealCooldown > 0) {
            this.stealCooldown--;
        }
        if (this.pickupItemCooldown > 0) {
            this.pickupItemCooldown--;
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void onEatItem() {
        this.m_5634_(10.0F);
        this.m_9236_().broadcastEntityEvent(this, (byte) 92);
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
    }

    public void postWashItem(ItemStack stack) {
        if (stack.getItem() == Items.EGG && this.eggThrowerUUID != null && !this.m_21824_()) {
            if (this.m_217043_().nextFloat() < 0.3F) {
                this.m_7105_(true);
                this.m_21816_(this.eggThrowerUUID);
                Player player = this.m_9236_().m_46003_(this.eggThrowerUUID);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer) player, this);
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 92) {
            for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.m_21120_(InteractionHand.MAIN_HAND)), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
        } else if (id == 93) {
            for (int i = 0; i < 6 + this.f_19796_.nextInt(3); i++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.GLOW_BERRIES)), this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + (double) (this.f_19796_.nextFloat() * this.m_20206_() * 0.5F), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_()) - (double) this.m_20205_() * 0.5, d0, d1, d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public boolean isBegging() {
        return this.f_19804_.get(BEGGING);
    }

    public void setBegging(boolean begging) {
        this.f_19804_.set(BEGGING, begging);
    }

    public boolean isWashing() {
        return this.f_19804_.get(WASHING);
    }

    public void setWashing(boolean washing) {
        this.f_19804_.set(WASHING, washing);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(BEGGING, false);
        this.f_19804_.define(WASHING, false);
        this.f_19804_.define(CARPET_COLOR, -1);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(WASH_POS, Optional.empty());
    }

    public BlockPos getWashPos() {
        return (BlockPos) this.f_19804_.get(WASH_POS).orElse(null);
    }

    public void setWashPos(BlockPos washingPos) {
        this.f_19804_.set(WASH_POS, Optional.ofNullable(washingPos));
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
        if (animation == ANIMATION_ATTACK) {
            this.maxStandTime = 15;
            this.setStanding(true);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_ATTACK };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        return AMEntityRegistry.RACCOON.get().create(serverWorld);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isSitting() || this.isWashing()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return isRaccoonFood(stack) && this.pickupItemCooldown == 0;
    }

    @Override
    public void onGetItem(ItemEntity e) {
        this.lookForWaterBeforeEatingTimer = 100;
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        Entity thrower = e.getOwner();
        if (e.getItem().is(Items.GLOW_BERRIES) && thrower != null && this.bondWithBlueJays(thrower.getUUID())) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 93);
        } else {
            this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        }
        if (e.getItem().getItem() == Items.EGG && thrower != null) {
            this.eggThrowerUUID = thrower.getUUID();
        } else {
            this.eggThrowerUUID = null;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.m_20206_() * 0.45;
    }

    private boolean bondWithBlueJays(UUID uuid) {
        AABB allyBox = this.m_20191_().inflate(48.0);
        boolean any = false;
        for (EntityBlueJay entity : this.m_9236_().m_45976_(EntityBlueJay.class, allyBox)) {
            if (entity.getFeedTime() > 0 && entity.getLastFeederUUID() != null && entity.getLastFeederUUID().equals(uuid)) {
                entity.setRaccoon(this);
                entity.setFeedTime(0);
                any = true;
            }
        }
        return any;
    }

    @Override
    public boolean isLootable(Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (this.shouldLootItem(inventory.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldLootItem(ItemStack stack) {
        return isRaccoonFood(stack);
    }

    public boolean isHoldingSugar() {
        return this.m_21205_().is(AMTagRegistry.RACOON_DISSOLVES);
    }

    public BlockPos getLightPosition() {
        BlockPos pos = AMBlockPos.fromVec3(this.m_20182_());
        return !this.m_9236_().getBlockState(pos).m_60815_() ? pos.above() : pos;
    }

    public boolean isRigby() {
        String name = ChatFormatting.stripFormatting(this.m_7755_().getString());
        if (name == null) {
            return false;
        } else {
            String lowercaseName = name.toLowerCase(Locale.ROOT);
            return lowercaseName.contains("rigby");
        }
    }

    private class AIStealFromVillagers extends Goal {

        EntityRaccoon raccoon;

        AbstractVillager target;

        int golemCheckTime = 0;

        int cooldown = 0;

        int fleeTime = 0;

        private AIStealFromVillagers(EntityRaccoon raccoon) {
            this.raccoon = raccoon;
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            } else if (this.raccoon != null && this.raccoon.stealCooldown == 0 && this.raccoon.m_21205_() != null && this.raccoon.m_21205_().isEmpty()) {
                AbstractVillager villager = this.getNearbyVillagers();
                if (!this.isGolemNearby() && villager != null) {
                    this.target = villager;
                }
                this.cooldown = 150;
                return this.target != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.target != null && this.raccoon != null;
        }

        @Override
        public void stop() {
            this.target = null;
            this.cooldown = 200 + EntityRaccoon.this.f_19796_.nextInt(200);
            this.golemCheckTime = 0;
            this.fleeTime = 0;
        }

        @Override
        public void tick() {
            if (this.target != null) {
                this.golemCheckTime++;
                if (this.fleeTime > 0) {
                    this.fleeTime--;
                    if (this.raccoon.m_21573_().isDone()) {
                        Vec3 fleevec = DefaultRandomPos.getPosAway(this.raccoon, 16, 7, this.raccoon.m_20182_());
                        if (fleevec != null) {
                            this.raccoon.m_21573_().moveTo(fleevec.x, fleevec.y, fleevec.z, 1.3F);
                        }
                    }
                    if (this.fleeTime == 0) {
                        this.stop();
                    }
                } else {
                    this.raccoon.m_21573_().moveTo(this.target, 1.0);
                    if (this.raccoon.m_20270_(this.target) < 1.7F) {
                        this.raccoon.setStanding(true);
                        this.raccoon.maxStandTime = 15;
                        MerchantOffers offers = this.target.getOffers();
                        if (offers != null && !offers.isEmpty() && offers.size() >= 1) {
                            MerchantOffer offer = (MerchantOffer) offers.get(offers.size() <= 1 ? 0 : this.raccoon.m_217043_().nextInt(offers.size() - 1));
                            if (offer != null) {
                                ItemStack stealStack = offer.getResult().getItem() == Items.EMERALD ? offer.getBaseCostA() : offer.getResult();
                                if (stealStack.isEmpty()) {
                                    this.stop();
                                } else {
                                    offer.increaseUses();
                                    ItemStack copy = stealStack.copy();
                                    copy.setCount(1);
                                    this.raccoon.m_21008_(InteractionHand.MAIN_HAND, copy);
                                    this.fleeTime = 60 + EntityRaccoon.this.f_19796_.nextInt(60);
                                    this.raccoon.m_21573_().stop();
                                    EntityRaccoon.this.lookForWaterBeforeEatingTimer = 120 + EntityRaccoon.this.f_19796_.nextInt(60);
                                    this.target.m_6469_(EntityRaccoon.this.m_269291_().generic(), 0.0F);
                                    this.raccoon.stealCooldown = 24000 + EntityRaccoon.this.f_19796_.nextInt(48000);
                                }
                            }
                        } else {
                            this.stop();
                        }
                    }
                    if (this.golemCheckTime % 30 == 0 && EntityRaccoon.this.f_19796_.nextBoolean() && this.isGolemNearby()) {
                        this.stop();
                    }
                }
            }
        }

        @Nullable
        private boolean isGolemNearby() {
            List<IronGolem> lvt_1_1_ = this.raccoon.m_9236_().m_45971_(IronGolem.class, EntityRaccoon.IRON_GOLEM_PREDICATE, this.raccoon, this.raccoon.m_20191_().inflate(25.0));
            return !lvt_1_1_.isEmpty();
        }

        @Nullable
        private AbstractVillager getNearbyVillagers() {
            List<AbstractVillager> lvt_1_1_ = this.raccoon.m_9236_().m_45971_(AbstractVillager.class, EntityRaccoon.VILLAGER_STEAL_PREDICATE, this.raccoon, this.raccoon.m_20191_().inflate(20.0));
            double lvt_2_1_ = 10000.0;
            AbstractVillager lvt_4_1_ = null;
            for (AbstractVillager lvt_6_1_ : lvt_1_1_) {
                if (lvt_6_1_.m_21223_() > 2.0F && !lvt_6_1_.getOffers().isEmpty() && this.raccoon.m_20280_(lvt_6_1_) < lvt_2_1_) {
                    lvt_4_1_ = lvt_6_1_;
                    lvt_2_1_ = this.raccoon.m_20280_(lvt_6_1_);
                }
            }
            return lvt_4_1_;
        }
    }

    class StrollGoal extends MoveThroughVillageGoal {

        public StrollGoal(int p_i50726_3_) {
            super(EntityRaccoon.this, 1.0, true, p_i50726_3_, () -> false);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.canFoxMove();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.canFoxMove();
        }

        private boolean canFoxMove() {
            return !EntityRaccoon.this.isWashing() && !EntityRaccoon.this.isSitting() && EntityRaccoon.this.m_5448_() == null;
        }
    }
}