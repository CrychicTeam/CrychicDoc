package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.CreatureAITargetItems;
import com.github.alexthe666.alexsmobs.entity.ai.ElephantAIFollowCaravan;
import com.github.alexthe666.alexsmobs.entity.ai.ElephantAIForageLeaves;
import com.github.alexthe666.alexsmobs.entity.ai.ElephantAIVillagerRide;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;

public class EntityElephant extends TamableAnimal implements ITargetsDroppedItems, IAnimatedEntity {

    public static final Animation ANIMATION_TRUMPET_0 = Animation.create(20);

    public static final Animation ANIMATION_TRUMPET_1 = Animation.create(30);

    public static final Animation ANIMATION_CHARGE_PREPARE = Animation.create(25);

    public static final Animation ANIMATION_STOMP = Animation.create(20);

    public static final Animation ANIMATION_FLING = Animation.create(25);

    public static final Animation ANIMATION_EAT = Animation.create(30);

    public static final Animation ANIMATION_BREAKLEAVES = Animation.create(20);

    protected static final EntityDimensions TUSKED_SIZE = EntityDimensions.fixed(3.7F, 3.75F);

    private static final EntityDataAccessor<Boolean> TUSKED = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> STANDING = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> CHESTED = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CARPET_COLOR = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> TRADER = SynchedEntityData.defineId(EntityElephant.class, EntityDataSerializers.BOOLEAN);

    public static final Map<DyeColor, Item> DYE_COLOR_ITEM_MAP = Util.make(Maps.newHashMap(), map -> {
        map.put(DyeColor.WHITE, Items.WHITE_CARPET);
        map.put(DyeColor.ORANGE, Items.ORANGE_CARPET);
        map.put(DyeColor.MAGENTA, Items.MAGENTA_CARPET);
        map.put(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_CARPET);
        map.put(DyeColor.YELLOW, Items.YELLOW_CARPET);
        map.put(DyeColor.LIME, Items.LIME_CARPET);
        map.put(DyeColor.PINK, Items.PINK_CARPET);
        map.put(DyeColor.GRAY, Items.GRAY_CARPET);
        map.put(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_CARPET);
        map.put(DyeColor.CYAN, Items.CYAN_CARPET);
        map.put(DyeColor.PURPLE, Items.PURPLE_CARPET);
        map.put(DyeColor.BLUE, Items.BLUE_CARPET);
        map.put(DyeColor.BROWN, Items.BROWN_CARPET);
        map.put(DyeColor.GREEN, Items.GREEN_CARPET);
        map.put(DyeColor.RED, Items.RED_CARPET);
        map.put(DyeColor.BLACK, Items.BLACK_CARPET);
    });

    private static final ResourceLocation TRADER_LOOT = new ResourceLocation("alexsmobs", "gameplay/trader_elephant_chest");

    public boolean forcedSit = false;

    public float prevSitProgress;

    public float sitProgress;

    public float prevStandProgress;

    public float standProgress;

    public int maxStandTime = 75;

    public boolean aiItemFlag = false;

    public SimpleContainer elephantInventory;

    private int animationTick;

    private Animation currentAnimation;

    private final boolean hasTuskedAttributes = false;

    private int standingTime = 0;

    @Nullable
    private EntityElephant caravanHead;

    @Nullable
    private EntityElephant caravanTail;

    private boolean hasChestVarChanged = false;

    private boolean hasChargedSpeed = false;

    private boolean charging;

    private int chargeCooldown = 0;

    private int chargingTicks = 0;

    @Nullable
    private UUID blossomThrowerUUID = null;

    private int despawnDelay = 47999;

    protected EntityElephant(EntityType type, Level world) {
        super(type, world);
        this.initElephantInventory();
        this.m_274367_(1.1F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 85.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.9F).add(Attributes.ATTACK_DAMAGE, 10.0).add(Attributes.MOVEMENT_SPEED, 0.35F);
    }

    @Nullable
    public static DyeColor getCarpetColor(ItemStack stack) {
        Block lvt_1_1_ = Block.byItem(stack.getItem());
        return lvt_1_1_ instanceof WoolCarpetBlock ? ((WoolCarpetBlock) lvt_1_1_).getColor() : null;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.ELEPHANT_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.ELEPHANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.ELEPHANT_DIE.get();
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.elephantSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    private void initElephantInventory() {
        SimpleContainer animalchest = this.elephantInventory;
        this.elephantInventory = new SimpleContainer(54) {

            @Override
            public boolean stillValid(Player player) {
                return EntityElephant.this.m_6084_() && !EntityElephant.this.f_19817_;
            }
        };
        if (animalchest != null) {
            int i = Math.min(animalchest.getContainerSize(), this.elephantInventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = animalchest.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.elephantInventory.setItem(j, itemstack.copy());
                }
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn);
    }

    @Override
    public int getMaxHeadYRot() {
        return super.m_8085_();
    }

    @Override
    protected boolean isImmobile() {
        return super.m_6107_() || this.isSitting() || this.getAnimation() == ANIMATION_CHARGE_PREPARE && this.getAnimationTick() < 10;
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.f_21345_.addGoal(2, new EntityElephant.PanicGoal());
        this.f_21345_.addGoal(2, new ElephantAIVillagerRide(this, 1.0));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(AMItemRegistry.ACACIA_BLOSSOM.get()), false));
        this.f_21345_.addGoal(5, new ElephantAIForageLeaves(this));
        this.f_21345_.addGoal(6, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(7, new ElephantAIFollowCaravan(this, 0.5));
        this.f_21345_.addGoal(8, new AvoidEntityGoal(this, Bee.class, 6.0F, 1.0, 1.2));
        this.f_21345_.addGoal(9, new EntityElephant.AIWalkIdle(this, 0.5));
        this.f_21346_.addGoal(1, new EntityElephant.HurtByTargetGoal().m_26044_(new Class[0]));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(4, new CreatureAITargetItems(this, false));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return this.m_21824_() && item == AMItemRegistry.ACACIA_BLOSSOM.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(AMSoundRegistry.ELEPHANT_WALK.get(), 0.2F, 1.0F);
        } else {
            super.m_7355_(pos, state);
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player) {
                return (LivingEntity) passenger;
            }
        }
        return null;
    }

    @Nullable
    public AbstractVillager getControllingVillager() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof AbstractVillager) {
                return (AbstractVillager) passenger;
            }
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TUSKED, false);
        this.f_19804_.define(SITTING, false);
        this.f_19804_.define(STANDING, false);
        this.f_19804_.define(CHESTED, false);
        this.f_19804_.define(TRADER, false);
        this.f_19804_.define(CARPET_COLOR, -1);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        this.prevStandProgress = this.standProgress;
        if (this.isSitting()) {
            if (this.sitProgress < 5.0F) {
                this.sitProgress++;
            }
        } else if (this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.isStanding()) {
            if (this.standProgress < 5.0F) {
                this.standProgress += 0.5F;
            }
        } else if (this.standProgress > 0.0F) {
            this.standProgress -= 0.5F;
        }
        if (this.isStanding() && ++this.standingTime > this.maxStandTime) {
            this.setStanding(false);
            this.standingTime = 0;
            this.maxStandTime = 75 + this.f_19796_.nextInt(50);
        }
        if (this.isSitting() && this.isStanding()) {
            this.setStanding(false);
        }
        if (this.hasChestVarChanged && this.elephantInventory != null && !this.isChested()) {
            for (int i = 3; i < 18; i++) {
                if (!this.elephantInventory.getItem(i).isEmpty()) {
                    if (!this.m_9236_().isClientSide) {
                        this.m_5552_(this.elephantInventory.getItem(i), 1.0F);
                    }
                    this.elephantInventory.removeItemNoUpdate(i);
                }
            }
            this.hasChestVarChanged = false;
        }
        if (this.isTusked() && !this.m_6162_()) {
            this.m_6210_();
        }
        if (this.charging) {
            this.chargingTicks++;
        }
        if (!this.m_21205_().isEmpty() && this.canTargetItem(this.m_21205_())) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_EAT);
            }
            if (this.getAnimation() == ANIMATION_EAT && this.getAnimationTick() == 17) {
                this.eatItemEffect(this.m_21205_());
                if (this.m_21205_().getItem() == AMItemRegistry.ACACIA_BLOSSOM.get() && !this.m_21824_() && (!this.isTusked() || this.m_6162_()) && this.blossomThrowerUUID != null) {
                    if (this.f_19796_.nextInt(3) != 0) {
                        this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                    } else {
                        this.m_7105_(true);
                        this.m_21816_(this.blossomThrowerUUID);
                        Player player = this.m_9236_().m_46003_(this.blossomThrowerUUID);
                        if (player != null) {
                            this.m_21828_(player);
                        }
                        for (Entity passenger : this.m_20197_()) {
                            passenger.removeVehicle();
                        }
                        this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                    }
                }
                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                this.m_5634_(10.0F);
            }
        }
        if (this.chargeCooldown > 0) {
            this.chargeCooldown--;
        }
        if (this.charging) {
            this.chargingTicks++;
        } else {
            this.chargingTicks = 0;
        }
        if (this.getAnimation() == ANIMATION_CHARGE_PREPARE) {
            this.f_20883_ = this.m_146908_();
            if (this.getAnimationTick() == 20) {
                this.charging = true;
            }
        }
        if (this.getControllingPassenger() != null && this.charging && this.chargingTicks > 100) {
            this.charging = false;
            this.chargeCooldown = 200;
        }
        LivingEntity target = this.m_5448_();
        double maxAttackMod = 0.0;
        if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            Player rider = (Player) this.getControllingPassenger();
            if (rider.m_21214_() != null && !this.isAlliedTo(rider.m_21214_())) {
                UUID preyUUID = rider.m_21214_().m_20148_();
                if (!this.m_20148_().equals(preyUUID)) {
                    target = rider.m_21214_();
                    maxAttackMod = 4.0;
                }
            }
        }
        if (!this.m_9236_().isClientSide && target != null) {
            if (this.m_20270_(target) > this.m_20205_() * 0.5F + 0.5F && this.getControllingPassenger() == null && this.isTusked() && this.m_142582_(target) && this.getAnimation() == NO_ANIMATION && !this.charging && this.chargeCooldown == 0) {
                this.setAnimation(ANIMATION_CHARGE_PREPARE);
            }
            if (this.getAnimation() == ANIMATION_CHARGE_PREPARE && this.getControllingPassenger() == null) {
                this.m_21391_(target, 360.0F, 30.0F);
                this.f_20883_ = this.m_146908_();
                if (this.getAnimationTick() == 20) {
                    this.charging = true;
                }
            }
            if ((double) this.m_20270_(target) < 10.0 && this.charging) {
                this.setAnimation(ANIMATION_FLING);
            }
            if ((double) this.m_20270_(target) < 2.1 && this.charging) {
                target.knockback(1.0, target.m_20185_() - this.m_20185_(), target.m_20189_() - this.m_20189_());
                target.f_19812_ = true;
                target.m_20256_(target.m_20184_().add(0.0, 0.7F, 0.0));
                target.hurt(this.m_269291_().mobAttack(this), 2.4F * (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
                this.launch(target, true);
                this.charging = false;
                this.chargeCooldown = 400;
            }
            double dist = (double) this.m_20270_(target);
            if (dist < 4.5 + maxAttackMod && this.getAnimation() == ANIMATION_FLING && this.getAnimationTick() == 15) {
                target.knockback(1.0, target.m_20185_() - this.m_20185_(), target.m_20189_() - this.m_20189_());
                target.m_20256_(target.m_20184_().add(0.0, 0.3F, 0.0));
                this.launch(target, false);
                target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
            if (dist < 4.5 + maxAttackMod && this.getAnimation() == ANIMATION_STOMP && this.getAnimationTick() == 17) {
                target.knockback(0.3F, target.m_20185_() - this.m_20185_(), target.m_20189_() - this.m_20189_());
                target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue());
            }
        }
        if (!this.m_9236_().isClientSide && this.m_5448_() == null && this.getControllingPassenger() == null) {
            this.charging = false;
        }
        if (this.charging && !this.hasChargedSpeed) {
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.65F);
            this.hasChargedSpeed = true;
        }
        if (!this.charging && this.hasChargedSpeed) {
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(0.35F);
            this.hasChargedSpeed = false;
        }
        if (!this.m_9236_().isClientSide && this.m_217043_().nextInt(400) == 0 && this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_TRUMPET_0 : ANIMATION_TRUMPET_1);
        }
        if (this.getAnimation() == ANIMATION_TRUMPET_0 && this.getAnimationTick() == 8 || this.getAnimation() == ANIMATION_TRUMPET_1 && this.getAnimationTick() == 4) {
            this.m_146850_(GameEvent.ENTITY_ROAR);
            this.m_5496_(AMSoundRegistry.ELEPHANT_TRUMPET.get(), this.m_6121_(), this.m_6100_());
        }
        if (this.m_6084_() && this.charging) {
            for (Entity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(1.0))) {
                if ((!this.m_21824_() || !this.isAlliedTo(entity)) && (this.m_21824_() || !(entity instanceof EntityElephant)) && entity != this) {
                    entity.hurt(this.m_269291_().mobAttack(this), 8.0F + this.f_19796_.nextFloat() * 8.0F);
                    this.launch(entity, true);
                }
            }
            this.m_274367_(2.0F);
        } else {
            this.m_274367_(1.1F);
        }
        if (!this.m_21824_() && this.isTrader() && !this.m_9236_().isClientSide) {
            this.tryDespawn();
        }
        if (this.m_5448_() != null && !this.m_5448_().isAlive()) {
            this.m_6710_(null);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_6162_() && this.m_20192_() > this.m_20206_()) {
            this.m_6210_();
        }
    }

    private boolean canDespawn() {
        return !this.m_21824_() && this.isTrader();
    }

    private void tryDespawn() {
        if (this.canDespawn()) {
            if (this.getControllingVillager() instanceof WanderingTrader) {
                int riderDelay = ((WanderingTrader) this.getControllingVillager()).getDespawnDelay();
                if (riderDelay > 0) {
                    this.despawnDelay = riderDelay;
                }
            }
            this.despawnDelay--;
            if (this.despawnDelay <= 0) {
                this.m_21455_(true, false);
                this.elephantInventory.clearContent();
                if (this.getControllingVillager() != null) {
                    this.getControllingVillager().m_142687_(Entity.RemovalReason.DISCARDED);
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    private void launch(Entity e, boolean huge) {
        if (e.onGround()) {
            double d0 = e.getX() - this.m_20185_();
            double d1 = e.getZ() - this.m_20189_();
            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
            float f = huge ? 2.0F : 0.5F;
            e.push(d0 / d2 * (double) f, huge ? 0.5 : 0.2F, d1 / d2 * (double) f);
        }
    }

    private void eatItemEffect(ItemStack heldItemMainhand) {
        this.m_146850_(GameEvent.EAT);
        this.m_5496_(SoundEvents.STRIDER_EAT, this.m_6100_(), this.m_6121_());
        for (int i = 0; i < 8 + this.f_19796_.nextInt(3); i++) {
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            float radius = this.m_20205_() * 0.65F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            ParticleOptions data = new ItemParticleOption(ParticleTypes.ITEM, heldItemMainhand);
            if (heldItemMainhand.getItem() instanceof BlockItem) {
                data = new BlockParticleOption(ParticleTypes.BLOCK, ((BlockItem) heldItemMainhand.getItem()).getBlock().defaultBlockState());
            }
            this.m_9236_().addParticle(data, this.m_20185_() + extraX, this.m_20186_() + (double) (this.m_20206_() * 0.6F), this.m_20189_() + extraZ, d0, d1, d2);
        }
    }

    private boolean isChargePlayer(Entity controllingPassenger) {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAnimation() == NO_ANIMATION && !this.charging) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_FLING : ANIMATION_STOMP);
        }
        return true;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        boolean owner = this.m_21824_() && this.m_21830_(player);
        InteractionResult type = super.m_6071_(player, hand);
        if (this.isChested() && player.m_6144_()) {
            this.openGUI(player);
            return InteractionResult.SUCCESS;
        } else if (this.canTargetItem(stack) && this.m_21205_().isEmpty()) {
            ItemStack rippedStack = stack.copy();
            rippedStack.setCount(1);
            stack.shrink(1);
            this.m_21008_(InteractionHand.MAIN_HAND, rippedStack);
            if (rippedStack.getItem() == AMItemRegistry.ACACIA_BLOSSOM.get()) {
                this.blossomThrowerUUID = player.m_20148_();
            }
            return InteractionResult.SUCCESS;
        } else if (owner && stack.is(ItemTags.WOOL_CARPETS)) {
            DyeColor color = getCarpetColor(stack);
            if (color != this.getColor()) {
                if (this.getColor() != null) {
                    this.m_19998_(this.getCarpetItemBeingWorn());
                }
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                this.m_5496_(SoundEvents.LLAMA_SWAG, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
                if (!this.m_9236_().isClientSide && player instanceof ServerPlayer serverPlayer) {
                    AMAdvancementTriggerRegistry.ELEPHANT_SWAG.trigger(serverPlayer);
                }
                stack.shrink(1);
                this.setColor(color);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else if (owner && this.getColor() != null && stack.getItem() == Items.SHEARS) {
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SHEEP_SHEAR, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            if (this.getColor() != null) {
                this.m_19998_(this.getCarpetItemBeingWorn());
            }
            this.setColor(null);
            return InteractionResult.SUCCESS;
        } else if (owner && !this.isChested() && stack.is(Tags.Items.CHESTS_WOODEN)) {
            this.setChested(true);
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.DONKEY_CHEST, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else if (owner && this.isChested() && stack.getItem() == Items.SHEARS) {
            this.m_146850_(GameEvent.ENTITY_INTERACT);
            this.m_5496_(SoundEvents.SHEEP_SHEAR, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            this.m_19998_(Blocks.CHEST);
            for (int i = 0; i < this.elephantInventory.getContainerSize(); i++) {
                this.m_19983_(this.elephantInventory.getItem(i));
            }
            this.elephantInventory.clearContent();
            this.setChested(false);
            return InteractionResult.SUCCESS;
        } else if (owner && !this.m_6162_() && type != InteractionResult.CONSUME) {
            if (!this.m_9236_().isClientSide) {
                player.m_20329_(this);
            }
            return InteractionResult.SUCCESS;
        } else {
            return type;
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isTusked() && !this.m_6162_() ? TUSKED_SIZE : super.m_6972_(poseIn);
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_TRUMPET_0, ANIMATION_TRUMPET_1, ANIMATION_CHARGE_PREPARE, ANIMATION_STOMP, ANIMATION_FLING, ANIMATION_EAT, ANIMATION_BREAKLEAVES };
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public Item getCarpetItemBeingWorn() {
        return this.getColor() != null ? (Item) DYE_COLOR_ITEM_MAP.get(this.getColor()) : Items.AIR;
    }

    @Override
    protected void dropEquipment() {
        super.m_5907_();
        if (this.isChested()) {
            if (!this.m_9236_().isClientSide) {
                this.m_19998_(Blocks.CHEST);
            }
            for (int i = 0; i < this.elephantInventory.getContainerSize(); i++) {
                this.m_19983_(this.elephantInventory.getItem(i));
            }
            this.elephantInventory.clearContent();
            this.setChested(false);
        }
        if (!this.isTrader() && this.getColor() != null) {
            if (!this.m_9236_().isClientSide) {
                this.m_19998_(this.getCarpetItemBeingWorn());
            }
            this.setColor(null);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableEntity) {
        EntityElephant baby = AMEntityRegistry.ELEPHANT.get().create(serverWorld);
        baby.setTusked(this.getNearestTusked(this.m_9236_(), 15.0) == null || this.f_19796_.nextInt(2) == 0);
        return baby;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Tusked", this.isTusked());
        compound.putBoolean("ElephantSitting", this.isSitting());
        compound.putBoolean("Standing", this.isStanding());
        compound.putBoolean("Chested", this.isChested());
        compound.putBoolean("Trader", this.isTrader());
        compound.putBoolean("ForcedToSit", this.forcedSit);
        compound.putBoolean("Tamed", this.m_21824_());
        compound.putInt("ChargeCooldown", this.chargeCooldown);
        compound.putInt("Carpet", this.f_19804_.get(CARPET_COLOR));
        compound.putInt("DespawnDelay", this.despawnDelay);
        if (this.elephantInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.elephantInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.elephantInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() == MobEffects.WITHER ? false : super.m_7301_(potioneffectIn);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.m_7105_(compound.getBoolean("Tamed"));
        this.setTusked(compound.getBoolean("Tusked"));
        this.setStanding(compound.getBoolean("Standing"));
        this.setOrderedToSit(compound.getBoolean("ElephantSitting"));
        this.setChested(compound.getBoolean("Chested"));
        this.setTrader(compound.getBoolean("Trader"));
        this.forcedSit = compound.getBoolean("ForcedToSit");
        this.chargeCooldown = compound.getInt("ChargeCooldown");
        this.f_19804_.set(CARPET_COLOR, compound.getInt("Carpet"));
        if (this.elephantInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initElephantInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.elephantInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.initElephantInventory();
            for (int i = 0; i < nbttaglist.size(); i++) {
                CompoundTag CompoundNBT = nbttaglist.getCompound(i);
                int j = CompoundNBT.getByte("Slot") & 255;
                this.initElephantInventory();
                this.elephantInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
        if (compound.contains("DespawnDelay", 99)) {
            this.despawnDelay = compound.getInt("DespawnDelay");
        }
    }

    public boolean isChested() {
        return Boolean.valueOf(this.f_19804_.get(CHESTED));
    }

    public void setChested(boolean chested) {
        this.f_19804_.set(CHESTED, chested);
        this.hasChestVarChanged = true;
    }

    public boolean setSlot(int inventorySlot, @Nullable ItemStack itemStackIn) {
        int j = inventorySlot - 500 + 2;
        if (j >= 0 && j < this.elephantInventory.getContainerSize()) {
            this.elephantInventory.setItem(j, itemStackIn);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (this.elephantInventory != null && !this.m_9236_().isClientSide) {
            for (int i = 0; i < this.elephantInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.elephantInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    this.m_5552_(itemstack, 0.0F);
                }
            }
        }
    }

    public boolean isStanding() {
        return this.f_19804_.get(STANDING);
    }

    public void setStanding(boolean standing) {
        this.f_19804_.set(STANDING, standing);
    }

    public boolean isSitting() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    @Nullable
    public DyeColor getColor() {
        int lvt_1_1_ = this.f_19804_.get(CARPET_COLOR);
        return lvt_1_1_ == -1 ? null : DyeColor.byId(lvt_1_1_);
    }

    public void setColor(@Nullable DyeColor color) {
        this.f_19804_.set(CARPET_COLOR, color == null ? -1 : color.getId());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn instanceof AgeableMob.AgeableMobGroupData lvt_6_1_) {
            if (lvt_6_1_.getGroupSize() == 0) {
                this.setTusked(true);
            }
        } else {
            this.setTusked(this.m_217043_().nextBoolean());
        }
        return super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    public EntityElephant getNearestTusked(LevelAccessor world, double dist) {
        List<? extends EntityElephant> list = world.m_45976_(this.getClass(), this.m_20191_().inflate(dist, dist / 2.0, dist));
        if (list.isEmpty()) {
            return null;
        } else {
            EntityElephant elephant1 = null;
            double d0 = Double.MAX_VALUE;
            for (EntityElephant elephant : list) {
                if (elephant.isTusked()) {
                    double d1 = this.m_20280_(elephant);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        elephant1 = elephant;
                    }
                }
            }
            return elephant1;
        }
    }

    public boolean isTusked() {
        return this.f_19804_.get(TUSKED);
    }

    public void setTusked(boolean tusked) {
        boolean prev = this.isTusked();
        if (!prev && tusked) {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(110.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(15.0);
            this.m_21153_(150.0F);
        } else {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(85.0);
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(10.0);
        }
        this.f_19804_.set(TUSKED, tusked);
    }

    @Override
    public boolean canTargetItem(ItemStack stack) {
        return stack.is(AMTagRegistry.ELEPHANT_FOODSTUFFS) || stack.getItem() == AMItemRegistry.ACACIA_BLOSSOM.get();
    }

    @Override
    public void onGetItem(ItemEntity e) {
        ItemStack duplicate = e.getItem().copy();
        duplicate.setCount(1);
        if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty() && !this.m_9236_().isClientSide) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
        }
        Entity itemThrower = e.getOwner();
        if (duplicate.getItem() == AMItemRegistry.ACACIA_BLOSSOM.get() && itemThrower != null) {
            this.blossomThrowerUUID = itemThrower.getUUID();
        } else {
            this.blossomThrowerUUID = null;
        }
        this.m_21008_(InteractionHand.MAIN_HAND, duplicate);
        this.aiItemFlag = false;
    }

    @Override
    public void onFindTarget(ItemEntity e) {
        this.aiItemFlag = true;
    }

    public void addElephantLoot(@Nullable Player player, int seed) {
        if (this.m_9236_().getServer() != null) {
            LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(TRADER_LOOT);
            LootParams.Builder lootcontext$builder = new LootParams.Builder((ServerLevel) this.m_9236_());
            loottable.fill(this.elephantInventory, lootcontext$builder.create(LootContextParamSets.EMPTY), (long) seed);
        }
    }

    public void leaveCaravan() {
        if (this.caravanHead != null) {
            this.caravanHead.caravanTail = null;
        }
        this.caravanHead = null;
    }

    public void joinCaravan(EntityElephant caravanHeadIn) {
        this.caravanHead = caravanHeadIn;
        this.caravanHead.caravanTail = this;
    }

    public boolean hasCaravanTrail() {
        return this.caravanTail != null;
    }

    public boolean inCaravan() {
        return this.caravanHead != null;
    }

    @Nullable
    public EntityElephant getCaravanHead() {
        return this.caravanHead;
    }

    @Override
    public double getMaxDistToItem() {
        return 5.0;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.m_20363_(passenger)) {
            float standAdd = -0.3F * this.standProgress;
            float scale = this.m_6162_() ? 0.5F : (this.isTusked() ? 1.1F : 1.0F);
            float sitAdd = -0.065F * this.sitProgress;
            float scaleY = scale * (2.4F * sitAdd - 0.4F * standAdd);
            if (passenger instanceof AbstractVillager villager) {
                scaleY -= 0.3F;
            }
            float radius = scale * (0.5F + standAdd);
            float angle = (float) (Math.PI / 180.0) * this.f_20883_;
            if (this.getAnimation() == ANIMATION_CHARGE_PREPARE) {
                float sinWave = Mth.sin((float) (Math.PI * (double) ((float) this.getAnimationTick() / 25.0F)));
                radius += sinWave * 0.2F * scale;
            }
            if (this.getAnimation() == ANIMATION_STOMP) {
                float sinWave = Mth.sin((float) (Math.PI * (double) ((float) this.getAnimationTick() / 20.0F)));
                radius -= sinWave * 1.0F * scale;
                scaleY += sinWave * 0.7F * scale;
            }
            double extraX = (double) (radius * Mth.sin((float) Math.PI + angle));
            double extraZ = (double) (radius * Mth.cos(angle));
            passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + this.getPassengersRidingOffset() + (double) scaleY + passenger.getMyRidingOffset(), this.m_20189_() + extraZ);
        }
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        if (player.f_20902_ != 0.0F) {
            float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
            return new Vec3((double) (player.f_20900_ * 0.25F), 0.0, (double) (player.f_20902_ * 0.5F * f));
        } else {
            this.m_6858_(false);
            return Vec3.ZERO;
        }
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
            this.m_274367_(1.0F);
            this.m_21573_().stop();
            this.m_6710_(null);
            this.m_6858_(true);
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public double getPassengersRidingOffset() {
        float scale = this.m_6162_() ? 0.5F : (this.isTusked() ? 1.1F : 1.0F);
        float f = Math.min(0.25F, this.f_267362_.speed());
        float f1 = this.f_267362_.position();
        float sitAdd = 0.0F;
        float standAdd = 0.0F;
        return (double) this.m_20206_() - 0.05F - (double) scale * ((double) (0.1F * Mth.cos(f1 * 1.4F) * 1.4F * f) + (double) sitAdd + (double) standAdd);
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
    public void travel(Vec3 vec3d) {
        if (this.isSitting()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            vec3d = Vec3.ZERO;
        }
        super.m_7023_(vec3d);
    }

    public void openGUI(Player playerEntity) {
        if (!this.m_9236_().isClientSide && !this.m_20363_(playerEntity)) {
            NetworkHooks.openScreen((ServerPlayer) playerEntity, new MenuProvider() {

                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
                    return ChestMenu.sixRows(p_createMenu_1_, p_createMenu_2_, EntityElephant.this.elephantInventory);
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("entity.alexsmobs.elephant.chest");
                }
            });
        }
    }

    public boolean isTrader() {
        return this.f_19804_.get(TRADER);
    }

    public void setTrader(boolean trader) {
        this.f_19804_.set(TRADER, trader);
    }

    public boolean triggerCharge(ItemStack stack) {
        if (this.getControllingPassenger() != null && this.chargeCooldown == 0 && !this.charging && this.getAnimation() == NO_ANIMATION && this.isTusked()) {
            this.setAnimation(ANIMATION_CHARGE_PREPARE);
            this.eatItemEffect(stack);
            this.m_5634_(2.0F);
            return true;
        } else {
            return false;
        }
    }

    public boolean canSpawnWithTraderHere() {
        return this.m_9236_().isLoaded(this.m_20183_()) && this.m_6914_(this.m_9236_()) && this.m_9236_().m_46859_(this.m_20183_().above(4));
    }

    private class AIWalkIdle extends RandomStrollGoal {

        public AIWalkIdle(EntityElephant e, double v) {
            super(e, v);
        }

        @Override
        public boolean canUse() {
            this.f_25730_ = !EntityElephant.this.isTusked() && EntityElephant.this.inCaravan() ? 120 : 50;
            return super.canUse();
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            return LandRandomPos.getPos(this.f_25725_, !EntityElephant.this.isTusked() && EntityElephant.this.inCaravan() ? 10 : 25, 7);
        }
    }

    class HurtByTargetGoal extends net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal {

        public HurtByTargetGoal() {
            super(EntityElephant.this);
        }

        @Override
        public void start() {
            if (!EntityElephant.this.m_6162_() && EntityElephant.this.isTusked()) {
                super.start();
            } else {
                this.m_26047_();
                this.m_8041_();
            }
        }

        @Override
        protected void alertOther(Mob mobIn, LivingEntity targetIn) {
            if (mobIn instanceof EntityElephant && (!mobIn.m_6162_() || !((EntityElephant) mobIn).isTusked())) {
                super.alertOther(mobIn, targetIn);
            }
        }
    }

    class PanicGoal extends net.minecraft.world.entity.ai.goal.PanicGoal {

        public PanicGoal() {
            super(EntityElephant.this, 1.0);
        }

        @Override
        public boolean canUse() {
            return (EntityElephant.this.m_6162_() || !EntityElephant.this.isTusked() || EntityElephant.this.m_6060_()) && super.canUse();
        }
    }
}