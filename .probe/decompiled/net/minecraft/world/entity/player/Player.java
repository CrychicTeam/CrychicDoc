package net.minecraft.world.entity.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.slf4j.Logger;

public abstract class Player extends LivingEntity {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int MAX_NAME_LENGTH = 16;

    public static final int MAX_HEALTH = 20;

    public static final int SLEEP_DURATION = 100;

    public static final int WAKE_UP_DURATION = 10;

    public static final int ENDER_SLOT_OFFSET = 200;

    public static final float CROUCH_BB_HEIGHT = 1.5F;

    public static final float SWIMMING_BB_WIDTH = 0.6F;

    public static final float SWIMMING_BB_HEIGHT = 0.6F;

    public static final float DEFAULT_EYE_HEIGHT = 1.62F;

    public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F);

    private static final Map<Pose, EntityDimensions> POSES = ImmutableMap.builder().put(Pose.STANDING, STANDING_DIMENSIONS).put(Pose.SLEEPING, f_20910_).put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F)).put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F)).put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F)).build();

    private static final int FLY_ACHIEVEMENT_SPEED = 25;

    private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_SCORE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);

    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

    protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

    private long timeEntitySatOnShoulder;

    private final Inventory inventory = new Inventory(this);

    protected PlayerEnderChestContainer enderChestInventory = new PlayerEnderChestContainer();

    public final InventoryMenu inventoryMenu;

    public AbstractContainerMenu containerMenu;

    protected FoodData foodData = new FoodData();

    protected int jumpTriggerTime;

    public float oBob;

    public float bob;

    public int takeXpDelay;

    public double xCloakO;

    public double yCloakO;

    public double zCloakO;

    public double xCloak;

    public double yCloak;

    public double zCloak;

    private int sleepCounter;

    protected boolean wasUnderwater;

    private final Abilities abilities = new Abilities();

    public int experienceLevel;

    public int totalExperience;

    public float experienceProgress;

    protected int enchantmentSeed;

    protected final float defaultFlySpeed = 0.02F;

    private int lastLevelUpTime;

    private final GameProfile gameProfile;

    private boolean reducedDebugInfo;

    private ItemStack lastItemInMainHand = ItemStack.EMPTY;

    private final ItemCooldowns cooldowns = this.createItemCooldowns();

    private Optional<GlobalPos> lastDeathLocation = Optional.empty();

    @Nullable
    public FishingHook fishing;

    protected float hurtDir;

    public Player(Level level0, BlockPos blockPos1, float float2, GameProfile gameProfile3) {
        super(EntityType.PLAYER, level0);
        this.m_20084_(UUIDUtil.getOrCreatePlayerUUID(gameProfile3));
        this.gameProfile = gameProfile3;
        this.inventoryMenu = new InventoryMenu(this.inventory, !level0.isClientSide, this);
        this.containerMenu = this.inventoryMenu;
        this.m_7678_((double) blockPos1.m_123341_() + 0.5, (double) (blockPos1.m_123342_() + 1), (double) blockPos1.m_123343_() + 0.5, float2, 0.0F);
        this.f_20896_ = 180.0F;
    }

    public boolean blockActionRestricted(Level level0, BlockPos blockPos1, GameType gameType2) {
        if (!gameType2.isBlockPlacingRestricted()) {
            return false;
        } else if (gameType2 == GameType.SPECTATOR) {
            return true;
        } else if (this.mayBuild()) {
            return false;
        } else {
            ItemStack $$3 = this.m_21205_();
            return $$3.isEmpty() || !$$3.hasAdventureModeBreakTagForBlock(level0.registryAccess().registryOrThrow(Registries.BLOCK), new BlockInWorld(level0, blockPos1, false));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes().add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.1F).add(Attributes.ATTACK_SPEED).add(Attributes.LUCK);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_PLAYER_ABSORPTION_ID, 0.0F);
        this.f_19804_.define(DATA_SCORE_ID, 0);
        this.f_19804_.define(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0);
        this.f_19804_.define(DATA_PLAYER_MAIN_HAND, (byte) 1);
        this.f_19804_.define(DATA_SHOULDER_LEFT, new CompoundTag());
        this.f_19804_.define(DATA_SHOULDER_RIGHT, new CompoundTag());
    }

    @Override
    public void tick() {
        this.f_19794_ = this.isSpectator();
        if (this.isSpectator()) {
            this.m_6853_(false);
        }
        if (this.takeXpDelay > 0) {
            this.takeXpDelay--;
        }
        if (this.m_5803_()) {
            this.sleepCounter++;
            if (this.sleepCounter > 100) {
                this.sleepCounter = 100;
            }
            if (!this.m_9236_().isClientSide && this.m_9236_().isDay()) {
                this.stopSleepInBed(false, true);
            }
        } else if (this.sleepCounter > 0) {
            this.sleepCounter++;
            if (this.sleepCounter >= 110) {
                this.sleepCounter = 0;
            }
        }
        this.updateIsUnderwater();
        super.tick();
        if (!this.m_9236_().isClientSide && this.containerMenu != null && !this.containerMenu.stillValid(this)) {
            this.closeContainer();
            this.containerMenu = this.inventoryMenu;
        }
        this.moveCloak();
        if (!this.m_9236_().isClientSide) {
            this.foodData.tick(this);
            this.awardStat(Stats.PLAY_TIME);
            this.awardStat(Stats.TOTAL_WORLD_TIME);
            if (this.m_6084_()) {
                this.awardStat(Stats.TIME_SINCE_DEATH);
            }
            if (this.m_20163_()) {
                this.awardStat(Stats.CROUCH_TIME);
            }
            if (!this.m_5803_()) {
                this.awardStat(Stats.TIME_SINCE_REST);
            }
        }
        int $$0 = 29999999;
        double $$1 = Mth.clamp(this.m_20185_(), -2.9999999E7, 2.9999999E7);
        double $$2 = Mth.clamp(this.m_20189_(), -2.9999999E7, 2.9999999E7);
        if ($$1 != this.m_20185_() || $$2 != this.m_20189_()) {
            this.m_6034_($$1, this.m_20186_(), $$2);
        }
        this.f_20922_++;
        ItemStack $$3 = this.m_21205_();
        if (!ItemStack.matches(this.lastItemInMainHand, $$3)) {
            if (!ItemStack.isSameItem(this.lastItemInMainHand, $$3)) {
                this.resetAttackStrengthTicker();
            }
            this.lastItemInMainHand = $$3.copy();
        }
        this.turtleHelmetTick();
        this.cooldowns.tick();
        this.updatePlayerPose();
    }

    public boolean isSecondaryUseActive() {
        return this.m_6144_();
    }

    protected boolean wantsToStopRiding() {
        return this.m_6144_();
    }

    protected boolean isStayingOnGroundSurface() {
        return this.m_6144_();
    }

    protected boolean updateIsUnderwater() {
        this.wasUnderwater = this.m_204029_(FluidTags.WATER);
        return this.wasUnderwater;
    }

    private void turtleHelmetTick() {
        ItemStack $$0 = this.getItemBySlot(EquipmentSlot.HEAD);
        if ($$0.is(Items.TURTLE_HELMET) && !this.m_204029_(FluidTags.WATER)) {
            this.m_7292_(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
        }
    }

    protected ItemCooldowns createItemCooldowns() {
        return new ItemCooldowns();
    }

    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double $$0 = this.m_20185_() - this.xCloak;
        double $$1 = this.m_20186_() - this.yCloak;
        double $$2 = this.m_20189_() - this.zCloak;
        double $$3 = 10.0;
        if ($$0 > 10.0) {
            this.xCloak = this.m_20185_();
            this.xCloakO = this.xCloak;
        }
        if ($$2 > 10.0) {
            this.zCloak = this.m_20189_();
            this.zCloakO = this.zCloak;
        }
        if ($$1 > 10.0) {
            this.yCloak = this.m_20186_();
            this.yCloakO = this.yCloak;
        }
        if ($$0 < -10.0) {
            this.xCloak = this.m_20185_();
            this.xCloakO = this.xCloak;
        }
        if ($$2 < -10.0) {
            this.zCloak = this.m_20189_();
            this.zCloakO = this.zCloak;
        }
        if ($$1 < -10.0) {
            this.yCloak = this.m_20186_();
            this.yCloakO = this.yCloak;
        }
        this.xCloak += $$0 * 0.25;
        this.zCloak += $$2 * 0.25;
        this.yCloak += $$1 * 0.25;
    }

    protected void updatePlayerPose() {
        if (this.m_20175_(Pose.SWIMMING)) {
            Pose $$0;
            if (this.m_21255_()) {
                $$0 = Pose.FALL_FLYING;
            } else if (this.m_5803_()) {
                $$0 = Pose.SLEEPING;
            } else if (this.isSwimming()) {
                $$0 = Pose.SWIMMING;
            } else if (this.m_21209_()) {
                $$0 = Pose.SPIN_ATTACK;
            } else if (this.m_6144_() && !this.abilities.flying) {
                $$0 = Pose.CROUCHING;
            } else {
                $$0 = Pose.STANDING;
            }
            Pose $$6;
            if (this.isSpectator() || this.m_20159_() || this.m_20175_($$0)) {
                $$6 = $$0;
            } else if (this.m_20175_(Pose.CROUCHING)) {
                $$6 = Pose.CROUCHING;
            } else {
                $$6 = Pose.SWIMMING;
            }
            this.m_20124_($$6);
        }
    }

    @Override
    public int getPortalWaitTime() {
        return this.abilities.invulnerable ? 1 : 80;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.PLAYER_SWIM;
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.PLAYER_SPLASH;
    }

    @Override
    protected SoundEvent getSwimHighSpeedSplashSound() {
        return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
    }

    @Override
    public int getDimensionChangingDelay() {
        return 10;
    }

    @Override
    public void playSound(SoundEvent soundEvent0, float float1, float float2) {
        this.m_9236_().playSound(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), soundEvent0, this.getSoundSource(), float1, float2);
    }

    public void playNotifySound(SoundEvent soundEvent0, SoundSource soundSource1, float float2, float float3) {
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.PLAYERS;
    }

    @Override
    protected int getFireImmuneTicks() {
        return 20;
    }

    @Override
    public void handleEntityEvent(byte byte0) {
        if (byte0 == 9) {
            this.m_8095_();
        } else if (byte0 == 23) {
            this.reducedDebugInfo = false;
        } else if (byte0 == 22) {
            this.reducedDebugInfo = true;
        } else if (byte0 == 43) {
            this.addParticlesAroundSelf(ParticleTypes.CLOUD);
        } else {
            super.handleEntityEvent(byte0);
        }
    }

    private void addParticlesAroundSelf(ParticleOptions particleOptions0) {
        for (int $$1 = 0; $$1 < 5; $$1++) {
            double $$2 = this.f_19796_.nextGaussian() * 0.02;
            double $$3 = this.f_19796_.nextGaussian() * 0.02;
            double $$4 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(particleOptions0, this.m_20208_(1.0), this.m_20187_() + 1.0, this.m_20262_(1.0), $$2, $$3, $$4);
        }
    }

    protected void closeContainer() {
        this.containerMenu = this.inventoryMenu;
    }

    protected void doCloseContainer() {
    }

    @Override
    public void rideTick() {
        if (!this.m_9236_().isClientSide && this.wantsToStopRiding() && this.m_20159_()) {
            this.m_8127_();
            this.m_20260_(false);
        } else {
            double $$0 = this.m_20185_();
            double $$1 = this.m_20186_();
            double $$2 = this.m_20189_();
            super.rideTick();
            this.oBob = this.bob;
            this.bob = 0.0F;
            this.checkRidingStatistics(this.m_20185_() - $$0, this.m_20186_() - $$1, this.m_20189_() - $$2);
        }
    }

    @Override
    protected void serverAiStep() {
        super.serverAiStep();
        this.m_21203_();
        this.f_20885_ = this.m_146908_();
    }

    @Override
    public void aiStep() {
        if (this.jumpTriggerTime > 0) {
            this.jumpTriggerTime--;
        }
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            if (this.m_21223_() < this.m_21233_() && this.f_19797_ % 20 == 0) {
                this.m_5634_(1.0F);
            }
            if (this.foodData.needsFood() && this.f_19797_ % 10 == 0) {
                this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
            }
        }
        this.inventory.tick();
        this.oBob = this.bob;
        super.aiStep();
        this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED));
        float $$1;
        if (this.m_20096_() && !this.m_21224_() && !this.isSwimming()) {
            $$1 = Math.min(0.1F, (float) this.m_20184_().horizontalDistance());
        } else {
            $$1 = 0.0F;
        }
        this.bob = this.bob + ($$1 - this.bob) * 0.4F;
        if (this.m_21223_() > 0.0F && !this.isSpectator()) {
            AABB $$2;
            if (this.m_20159_() && !this.m_20202_().isRemoved()) {
                $$2 = this.m_20191_().minmax(this.m_20202_().getBoundingBox()).inflate(1.0, 0.0, 1.0);
            } else {
                $$2 = this.m_20191_().inflate(1.0, 0.5, 1.0);
            }
            List<Entity> $$4 = this.m_9236_().m_45933_(this, $$2);
            List<Entity> $$5 = Lists.newArrayList();
            for (int $$6 = 0; $$6 < $$4.size(); $$6++) {
                Entity $$7 = (Entity) $$4.get($$6);
                if ($$7.getType() == EntityType.EXPERIENCE_ORB) {
                    $$5.add($$7);
                } else if (!$$7.isRemoved()) {
                    this.touch($$7);
                }
            }
            if (!$$5.isEmpty()) {
                this.touch(Util.getRandom($$5, this.f_19796_));
            }
        }
        this.playShoulderEntityAmbientSound(this.getShoulderEntityLeft());
        this.playShoulderEntityAmbientSound(this.getShoulderEntityRight());
        if (!this.m_9236_().isClientSide && (this.f_19789_ > 0.5F || this.m_20069_()) || this.abilities.flying || this.m_5803_() || this.f_146808_) {
            this.removeEntitiesOnShoulder();
        }
    }

    private void playShoulderEntityAmbientSound(@Nullable CompoundTag compoundTag0) {
        if (compoundTag0 != null && (!compoundTag0.contains("Silent") || !compoundTag0.getBoolean("Silent")) && this.m_9236_().random.nextInt(200) == 0) {
            String $$1 = compoundTag0.getString("id");
            EntityType.byString($$1).filter(p_36280_ -> p_36280_ == EntityType.PARROT).ifPresent(p_289488_ -> {
                if (!Parrot.imitateNearbyMobs(this.m_9236_(), this)) {
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), Parrot.getAmbient(this.m_9236_(), this.m_9236_().random), this.getSoundSource(), 1.0F, Parrot.getPitch(this.m_9236_().random));
                }
            });
        }
    }

    private void touch(Entity entity0) {
        entity0.playerTouch(this);
    }

    public int getScore() {
        return this.f_19804_.get(DATA_SCORE_ID);
    }

    public void setScore(int int0) {
        this.f_19804_.set(DATA_SCORE_ID, int0);
    }

    public void increaseScore(int int0) {
        int $$1 = this.getScore();
        this.f_19804_.set(DATA_SCORE_ID, $$1 + int0);
    }

    public void startAutoSpinAttack(int int0) {
        this.f_20938_ = int0;
        if (!this.m_9236_().isClientSide) {
            this.removeEntitiesOnShoulder();
            this.m_21155_(4, true);
        }
    }

    @Override
    public void die(DamageSource damageSource0) {
        super.die(damageSource0);
        this.m_20090_();
        if (!this.isSpectator()) {
            this.m_6668_(damageSource0);
        }
        if (damageSource0 != null) {
            this.m_20334_((double) (-Mth.cos((this.getHurtDir() + this.m_146908_()) * (float) (Math.PI / 180.0)) * 0.1F), 0.1F, (double) (-Mth.sin((this.getHurtDir() + this.m_146908_()) * (float) (Math.PI / 180.0)) * 0.1F));
        } else {
            this.m_20334_(0.0, 0.1, 0.0);
        }
        this.awardStat(Stats.DEATHS);
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
        this.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
        this.m_20095_();
        this.m_146868_(false);
        this.setLastDeathLocation(Optional.of(GlobalPos.of(this.m_9236_().dimension(), this.m_20183_())));
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (!this.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAll();
        }
    }

    protected void destroyVanishingCursedItems() {
        for (int $$0 = 0; $$0 < this.inventory.getContainerSize(); $$0++) {
            ItemStack $$1 = this.inventory.getItem($$0);
            if (!$$1.isEmpty() && EnchantmentHelper.hasVanishingCurse($$1)) {
                this.inventory.removeItemNoUpdate($$0);
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return damageSource0.type().effects().sound();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    @Nullable
    public ItemEntity drop(ItemStack itemStack0, boolean boolean1) {
        return this.drop(itemStack0, false, boolean1);
    }

    @Nullable
    public ItemEntity drop(ItemStack itemStack0, boolean boolean1, boolean boolean2) {
        if (itemStack0.isEmpty()) {
            return null;
        } else {
            if (this.m_9236_().isClientSide) {
                this.m_6674_(InteractionHand.MAIN_HAND);
            }
            double $$3 = this.m_20188_() - 0.3F;
            ItemEntity $$4 = new ItemEntity(this.m_9236_(), this.m_20185_(), $$3, this.m_20189_(), itemStack0);
            $$4.setPickUpDelay(40);
            if (boolean2) {
                $$4.setThrower(this.m_20148_());
            }
            if (boolean1) {
                float $$5 = this.f_19796_.nextFloat() * 0.5F;
                float $$6 = this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                $$4.m_20334_((double) (-Mth.sin($$6) * $$5), 0.2F, (double) (Mth.cos($$6) * $$5));
            } else {
                float $$7 = 0.3F;
                float $$8 = Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0));
                float $$9 = Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
                float $$10 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
                float $$11 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
                float $$12 = this.f_19796_.nextFloat() * (float) (Math.PI * 2);
                float $$13 = 0.02F * this.f_19796_.nextFloat();
                $$4.m_20334_((double) (-$$10 * $$9 * 0.3F) + Math.cos((double) $$12) * (double) $$13, (double) (-$$8 * 0.3F + 0.1F + (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.1F), (double) ($$11 * $$9 * 0.3F) + Math.sin((double) $$12) * (double) $$13);
            }
            return $$4;
        }
    }

    public float getDestroySpeed(BlockState blockState0) {
        float $$1 = this.inventory.getDestroySpeed(blockState0);
        if ($$1 > 1.0F) {
            int $$2 = EnchantmentHelper.getBlockEfficiency(this);
            ItemStack $$3 = this.m_21205_();
            if ($$2 > 0 && !$$3.isEmpty()) {
                $$1 += (float) ($$2 * $$2 + 1);
            }
        }
        if (MobEffectUtil.hasDigSpeed(this)) {
            $$1 *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
        }
        if (this.m_21023_(MobEffects.DIG_SLOWDOWN)) {
            $$1 *= switch(this.m_21124_(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 ->
                    0.3F;
                case 1 ->
                    0.09F;
                case 2 ->
                    0.0027F;
                default ->
                    8.1E-4F;
            };
        }
        if (this.m_204029_(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
            $$1 /= 5.0F;
        }
        if (!this.m_20096_()) {
            $$1 /= 5.0F;
        }
        return $$1;
    }

    public boolean hasCorrectToolForDrops(BlockState blockState0) {
        return !blockState0.m_60834_() || this.inventory.getSelected().isCorrectToolForDrops(blockState0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        this.m_20084_(UUIDUtil.getOrCreatePlayerUUID(this.gameProfile));
        ListTag $$1 = compoundTag0.getList("Inventory", 10);
        this.inventory.load($$1);
        this.inventory.selected = compoundTag0.getInt("SelectedItemSlot");
        this.sleepCounter = compoundTag0.getShort("SleepTimer");
        this.experienceProgress = compoundTag0.getFloat("XpP");
        this.experienceLevel = compoundTag0.getInt("XpLevel");
        this.totalExperience = compoundTag0.getInt("XpTotal");
        this.enchantmentSeed = compoundTag0.getInt("XpSeed");
        if (this.enchantmentSeed == 0) {
            this.enchantmentSeed = this.f_19796_.nextInt();
        }
        this.setScore(compoundTag0.getInt("Score"));
        this.foodData.readAdditionalSaveData(compoundTag0);
        this.abilities.loadSaveData(compoundTag0);
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue((double) this.abilities.getWalkingSpeed());
        if (compoundTag0.contains("EnderItems", 9)) {
            this.enderChestInventory.fromTag(compoundTag0.getList("EnderItems", 10));
        }
        if (compoundTag0.contains("ShoulderEntityLeft", 10)) {
            this.setShoulderEntityLeft(compoundTag0.getCompound("ShoulderEntityLeft"));
        }
        if (compoundTag0.contains("ShoulderEntityRight", 10)) {
            this.setShoulderEntityRight(compoundTag0.getCompound("ShoulderEntityRight"));
        }
        if (compoundTag0.contains("LastDeathLocation", 10)) {
            this.setLastDeathLocation(GlobalPos.CODEC.parse(NbtOps.INSTANCE, compoundTag0.get("LastDeathLocation")).resultOrPartial(LOGGER::error));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        NbtUtils.addCurrentDataVersion(compoundTag0);
        compoundTag0.put("Inventory", this.inventory.save(new ListTag()));
        compoundTag0.putInt("SelectedItemSlot", this.inventory.selected);
        compoundTag0.putShort("SleepTimer", (short) this.sleepCounter);
        compoundTag0.putFloat("XpP", this.experienceProgress);
        compoundTag0.putInt("XpLevel", this.experienceLevel);
        compoundTag0.putInt("XpTotal", this.totalExperience);
        compoundTag0.putInt("XpSeed", this.enchantmentSeed);
        compoundTag0.putInt("Score", this.getScore());
        this.foodData.addAdditionalSaveData(compoundTag0);
        this.abilities.addSaveData(compoundTag0);
        compoundTag0.put("EnderItems", this.enderChestInventory.createTag());
        if (!this.getShoulderEntityLeft().isEmpty()) {
            compoundTag0.put("ShoulderEntityLeft", this.getShoulderEntityLeft());
        }
        if (!this.getShoulderEntityRight().isEmpty()) {
            compoundTag0.put("ShoulderEntityRight", this.getShoulderEntityRight());
        }
        this.getLastDeathLocation().flatMap(p_219745_ -> GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, p_219745_).resultOrPartial(LOGGER::error)).ifPresent(p_219756_ -> compoundTag0.put("LastDeathLocation", p_219756_));
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        if (super.m_6673_(damageSource0)) {
            return true;
        } else if (damageSource0.is(DamageTypeTags.IS_DROWNING)) {
            return !this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DROWNING_DAMAGE);
        } else if (damageSource0.is(DamageTypeTags.IS_FALL)) {
            return !this.m_9236_().getGameRules().getBoolean(GameRules.RULE_FALL_DAMAGE);
        } else if (damageSource0.is(DamageTypeTags.IS_FIRE)) {
            return !this.m_9236_().getGameRules().getBoolean(GameRules.RULE_FIRE_DAMAGE);
        } else {
            return damageSource0.is(DamageTypeTags.IS_FREEZING) ? !this.m_9236_().getGameRules().getBoolean(GameRules.RULE_FREEZE_DAMAGE) : false;
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.isInvulnerableTo(damageSource0)) {
            return false;
        } else if (this.abilities.invulnerable && !damageSource0.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        } else {
            this.f_20891_ = 0;
            if (this.m_21224_()) {
                return false;
            } else {
                if (!this.m_9236_().isClientSide) {
                    this.removeEntitiesOnShoulder();
                }
                if (damageSource0.scalesWithDifficulty()) {
                    if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL) {
                        float1 = 0.0F;
                    }
                    if (this.m_9236_().m_46791_() == Difficulty.EASY) {
                        float1 = Math.min(float1 / 2.0F + 1.0F, float1);
                    }
                    if (this.m_9236_().m_46791_() == Difficulty.HARD) {
                        float1 = float1 * 3.0F / 2.0F;
                    }
                }
                return float1 == 0.0F ? false : super.hurt(damageSource0, float1);
            }
        }
    }

    @Override
    protected void blockUsingShield(LivingEntity livingEntity0) {
        super.blockUsingShield(livingEntity0);
        if (livingEntity0.canDisableShield()) {
            this.disableShield(true);
        }
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return !this.getAbilities().invulnerable && super.canBeSeenAsEnemy();
    }

    public boolean canHarmPlayer(Player player0) {
        Team $$1 = this.m_5647_();
        Team $$2 = player0.m_5647_();
        if ($$1 == null) {
            return true;
        } else {
            return !$$1.isAlliedTo($$2) ? true : $$1.isAllowFriendlyFire();
        }
    }

    @Override
    protected void hurtArmor(DamageSource damageSource0, float float1) {
        this.inventory.hurtArmor(damageSource0, float1, Inventory.ALL_ARMOR_SLOTS);
    }

    @Override
    protected void hurtHelmet(DamageSource damageSource0, float float1) {
        this.inventory.hurtArmor(damageSource0, float1, Inventory.HELMET_SLOT_ONLY);
    }

    @Override
    protected void hurtCurrentlyUsedShield(float float0) {
        if (this.f_20935_.is(Items.SHIELD)) {
            if (!this.m_9236_().isClientSide) {
                this.awardStat(Stats.ITEM_USED.get(this.f_20935_.getItem()));
            }
            if (float0 >= 3.0F) {
                int $$1 = 1 + Mth.floor(float0);
                InteractionHand $$2 = this.m_7655_();
                this.f_20935_.hurtAndBreak($$1, this, p_219739_ -> p_219739_.m_21190_($$2));
                if (this.f_20935_.isEmpty()) {
                    if ($$2 == InteractionHand.MAIN_HAND) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                    }
                    this.f_20935_ = ItemStack.EMPTY;
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.m_9236_().random.nextFloat() * 0.4F);
                }
            }
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource0, float float1) {
        if (!this.isInvulnerableTo(damageSource0)) {
            float1 = this.m_21161_(damageSource0, float1);
            float1 = this.m_6515_(damageSource0, float1);
            float var7 = Math.max(float1 - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (float1 - var7));
            float $$3 = float1 - var7;
            if ($$3 > 0.0F && $$3 < 3.4028235E37F) {
                this.awardStat(Stats.DAMAGE_ABSORBED, Math.round($$3 * 10.0F));
            }
            if (var7 != 0.0F) {
                this.causeFoodExhaustion(damageSource0.getFoodExhaustion());
                this.m_21231_().recordDamage(damageSource0, var7);
                this.m_21153_(this.m_21223_() - var7);
                if (var7 < 3.4028235E37F) {
                    this.awardStat(Stats.DAMAGE_TAKEN, Math.round(var7 * 10.0F));
                }
                this.m_146850_(GameEvent.ENTITY_DAMAGE);
            }
        }
    }

    @Override
    protected boolean onSoulSpeedBlock() {
        return !this.abilities.flying && super.onSoulSpeedBlock();
    }

    public boolean isTextFilteringEnabled() {
        return false;
    }

    public void openTextEdit(SignBlockEntity signBlockEntity0, boolean boolean1) {
    }

    public void openMinecartCommandBlock(BaseCommandBlock baseCommandBlock0) {
    }

    public void openCommandBlock(CommandBlockEntity commandBlockEntity0) {
    }

    public void openStructureBlock(StructureBlockEntity structureBlockEntity0) {
    }

    public void openJigsawBlock(JigsawBlockEntity jigsawBlockEntity0) {
    }

    public void openHorseInventory(AbstractHorse abstractHorse0, Container container1) {
    }

    public OptionalInt openMenu(@Nullable MenuProvider menuProvider0) {
        return OptionalInt.empty();
    }

    public void sendMerchantOffers(int int0, MerchantOffers merchantOffers1, int int2, int int3, boolean boolean4, boolean boolean5) {
    }

    public void openItemGui(ItemStack itemStack0, InteractionHand interactionHand1) {
    }

    public InteractionResult interactOn(Entity entity0, InteractionHand interactionHand1) {
        if (this.isSpectator()) {
            if (entity0 instanceof MenuProvider) {
                this.openMenu((MenuProvider) entity0);
            }
            return InteractionResult.PASS;
        } else {
            ItemStack $$2 = this.m_21120_(interactionHand1);
            ItemStack $$3 = $$2.copy();
            InteractionResult $$4 = entity0.interact(this, interactionHand1);
            if ($$4.consumesAction()) {
                if (this.abilities.instabuild && $$2 == this.m_21120_(interactionHand1) && $$2.getCount() < $$3.getCount()) {
                    $$2.setCount($$3.getCount());
                }
                return $$4;
            } else {
                if (!$$2.isEmpty() && entity0 instanceof LivingEntity) {
                    if (this.abilities.instabuild) {
                        $$2 = $$3;
                    }
                    InteractionResult $$5 = $$2.interactLivingEntity(this, (LivingEntity) entity0, interactionHand1);
                    if ($$5.consumesAction()) {
                        this.m_9236_().m_214171_(GameEvent.ENTITY_INTERACT, entity0.position(), GameEvent.Context.of(this));
                        if ($$2.isEmpty() && !this.abilities.instabuild) {
                            this.m_21008_(interactionHand1, ItemStack.EMPTY);
                        }
                        return $$5;
                    }
                }
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35;
    }

    @Override
    public void removeVehicle() {
        super.m_6038_();
        this.f_19851_ = 0;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.m_5803_();
    }

    @Override
    public boolean isAffectedByFluids() {
        return !this.abilities.flying;
    }

    @Override
    protected Vec3 maybeBackOffFromEdge(Vec3 vec0, MoverType moverType1) {
        if (!this.abilities.flying && vec0.y <= 0.0 && (moverType1 == MoverType.SELF || moverType1 == MoverType.PLAYER) && this.isStayingOnGroundSurface() && this.isAboveGround()) {
            double $$2 = vec0.x;
            double $$3 = vec0.z;
            double $$4 = 0.05;
            while ($$2 != 0.0 && this.m_9236_().m_45756_(this, this.m_20191_().move($$2, (double) (-this.m_274421_()), 0.0))) {
                if ($$2 < 0.05 && $$2 >= -0.05) {
                    $$2 = 0.0;
                } else if ($$2 > 0.0) {
                    $$2 -= 0.05;
                } else {
                    $$2 += 0.05;
                }
            }
            while ($$3 != 0.0 && this.m_9236_().m_45756_(this, this.m_20191_().move(0.0, (double) (-this.m_274421_()), $$3))) {
                if ($$3 < 0.05 && $$3 >= -0.05) {
                    $$3 = 0.0;
                } else if ($$3 > 0.0) {
                    $$3 -= 0.05;
                } else {
                    $$3 += 0.05;
                }
            }
            while ($$2 != 0.0 && $$3 != 0.0 && this.m_9236_().m_45756_(this, this.m_20191_().move($$2, (double) (-this.m_274421_()), $$3))) {
                if ($$2 < 0.05 && $$2 >= -0.05) {
                    $$2 = 0.0;
                } else if ($$2 > 0.0) {
                    $$2 -= 0.05;
                } else {
                    $$2 += 0.05;
                }
                if ($$3 < 0.05 && $$3 >= -0.05) {
                    $$3 = 0.0;
                } else if ($$3 > 0.0) {
                    $$3 -= 0.05;
                } else {
                    $$3 += 0.05;
                }
            }
            vec0 = new Vec3($$2, vec0.y, $$3);
        }
        return vec0;
    }

    private boolean isAboveGround() {
        return this.m_20096_() || this.f_19789_ < this.m_274421_() && !this.m_9236_().m_45756_(this, this.m_20191_().move(0.0, (double) (this.f_19789_ - this.m_274421_()), 0.0));
    }

    public void attack(Entity entity0) {
        if (entity0.isAttackable()) {
            if (!entity0.skipAttackInteraction(this)) {
                float $$1 = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
                float $$2;
                if (entity0 instanceof LivingEntity) {
                    $$2 = EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entity0).getMobType());
                } else {
                    $$2 = EnchantmentHelper.getDamageBonus(this.m_21205_(), MobType.UNDEFINED);
                }
                float $$4 = this.getAttackStrengthScale(0.5F);
                $$1 *= 0.2F + $$4 * $$4 * 0.8F;
                $$2 *= $$4;
                this.resetAttackStrengthTicker();
                if ($$1 > 0.0F || $$2 > 0.0F) {
                    boolean $$5 = $$4 > 0.9F;
                    boolean $$6 = false;
                    int $$7 = 0;
                    $$7 += EnchantmentHelper.getKnockbackBonus(this);
                    if (this.m_20142_() && $$5) {
                        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, this.getSoundSource(), 1.0F, 1.0F);
                        $$7++;
                        $$6 = true;
                    }
                    boolean $$8 = $$5 && this.f_19789_ > 0.0F && !this.m_20096_() && !this.m_6147_() && !this.m_20069_() && !this.m_21023_(MobEffects.BLINDNESS) && !this.m_20159_() && entity0 instanceof LivingEntity;
                    $$8 = $$8 && !this.m_20142_();
                    if ($$8) {
                        $$1 *= 1.5F;
                    }
                    $$1 += $$2;
                    boolean $$9 = false;
                    double $$10 = (double) (this.f_19787_ - this.f_19867_);
                    if ($$5 && !$$8 && !$$6 && this.m_20096_() && $$10 < (double) this.getSpeed()) {
                        ItemStack $$11 = this.m_21120_(InteractionHand.MAIN_HAND);
                        if ($$11.getItem() instanceof SwordItem) {
                            $$9 = true;
                        }
                    }
                    float $$12 = 0.0F;
                    boolean $$13 = false;
                    int $$14 = EnchantmentHelper.getFireAspect(this);
                    if (entity0 instanceof LivingEntity) {
                        $$12 = ((LivingEntity) entity0).getHealth();
                        if ($$14 > 0 && !entity0.isOnFire()) {
                            $$13 = true;
                            entity0.setSecondsOnFire(1);
                        }
                    }
                    Vec3 $$15 = entity0.getDeltaMovement();
                    boolean $$16 = entity0.hurt(this.m_269291_().playerAttack(this), $$1);
                    if ($$16) {
                        if ($$7 > 0) {
                            if (entity0 instanceof LivingEntity) {
                                ((LivingEntity) entity0).knockback((double) ((float) $$7 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                            } else {
                                entity0.push((double) (-Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * (float) $$7 * 0.5F), 0.1, (double) (Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * (float) $$7 * 0.5F));
                            }
                            this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
                            this.m_6858_(false);
                        }
                        if ($$9) {
                            float $$17 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * $$1;
                            for (LivingEntity $$19 : this.m_9236_().m_45976_(LivingEntity.class, entity0.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
                                if ($$19 != this && $$19 != entity0 && !this.m_7307_($$19) && (!($$19 instanceof ArmorStand) || !((ArmorStand) $$19).isMarker()) && this.m_20280_($$19) < 9.0) {
                                    $$19.knockback(0.4F, (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                                    $$19.hurt(this.m_269291_().playerAttack(this), $$17);
                                }
                            }
                            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_SWEEP, this.getSoundSource(), 1.0F, 1.0F);
                            this.sweepAttack();
                        }
                        if (entity0 instanceof ServerPlayer && entity0.hurtMarked) {
                            ((ServerPlayer) entity0).connection.send(new ClientboundSetEntityMotionPacket(entity0));
                            entity0.hurtMarked = false;
                            entity0.setDeltaMovement($$15);
                        }
                        if ($$8) {
                            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_CRIT, this.getSoundSource(), 1.0F, 1.0F);
                            this.crit(entity0);
                        }
                        if (!$$8 && !$$9) {
                            if ($$5) {
                                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_STRONG, this.getSoundSource(), 1.0F, 1.0F);
                            } else {
                                this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0F, 1.0F);
                            }
                        }
                        if ($$2 > 0.0F) {
                            this.magicCrit(entity0);
                        }
                        this.m_21335_(entity0);
                        if (entity0 instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity) entity0, this);
                        }
                        EnchantmentHelper.doPostDamageEffects(this, entity0);
                        ItemStack $$20 = this.m_21205_();
                        Entity $$21 = entity0;
                        if (entity0 instanceof EnderDragonPart) {
                            $$21 = ((EnderDragonPart) entity0).parentMob;
                        }
                        if (!this.m_9236_().isClientSide && !$$20.isEmpty() && $$21 instanceof LivingEntity) {
                            $$20.hurtEnemy((LivingEntity) $$21, this);
                            if ($$20.isEmpty()) {
                                this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                            }
                        }
                        if (entity0 instanceof LivingEntity) {
                            float $$22 = $$12 - ((LivingEntity) entity0).getHealth();
                            this.awardStat(Stats.DAMAGE_DEALT, Math.round($$22 * 10.0F));
                            if ($$14 > 0) {
                                entity0.setSecondsOnFire($$14 * 4);
                            }
                            if (this.m_9236_() instanceof ServerLevel && $$22 > 2.0F) {
                                int $$23 = (int) ((double) $$22 * 0.5);
                                ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, entity0.getX(), entity0.getY(0.5), entity0.getZ(), $$23, 0.1, 0.0, 0.1, 0.2);
                            }
                        }
                        this.causeFoodExhaustion(0.1F);
                    } else {
                        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource(), 1.0F, 1.0F);
                        if ($$13) {
                            entity0.clearFire();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void doAutoAttackOnTouch(LivingEntity livingEntity0) {
        this.attack(livingEntity0);
    }

    public void disableShield(boolean boolean0) {
        float $$1 = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (boolean0) {
            $$1 += 0.75F;
        }
        if (this.f_19796_.nextFloat() < $$1) {
            this.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.m_5810_();
            this.m_9236_().broadcastEntityEvent(this, (byte) 30);
        }
    }

    public void crit(Entity entity0) {
    }

    public void magicCrit(Entity entity0) {
    }

    public void sweepAttack() {
        double $$0 = (double) (-Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)));
        double $$1 = (double) Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
        if (this.m_9236_() instanceof ServerLevel) {
            ((ServerLevel) this.m_9236_()).sendParticles(ParticleTypes.SWEEP_ATTACK, this.m_20185_() + $$0, this.m_20227_(0.5), this.m_20189_() + $$1, 0, $$0, 0.0, $$1, 0.0);
        }
    }

    public void respawn() {
    }

    @Override
    public void remove(Entity.RemovalReason entityRemovalReason0) {
        super.remove(entityRemovalReason0);
        this.inventoryMenu.removed(this);
        if (this.containerMenu != null && this.hasContainerOpen()) {
            this.doCloseContainer();
        }
    }

    public boolean isLocalPlayer() {
        return false;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Abilities getAbilities() {
        return this.abilities;
    }

    public void updateTutorialInventoryAction(ItemStack itemStack0, ItemStack itemStack1, ClickAction clickAction2) {
    }

    public boolean hasContainerOpen() {
        return this.containerMenu != this.inventoryMenu;
    }

    public Either<Player.BedSleepingProblem, Unit> startSleepInBed(BlockPos blockPos0) {
        this.m_5802_(blockPos0);
        this.sleepCounter = 0;
        return Either.right(Unit.INSTANCE);
    }

    public void stopSleepInBed(boolean boolean0, boolean boolean1) {
        super.stopSleeping();
        if (this.m_9236_() instanceof ServerLevel && boolean1) {
            ((ServerLevel) this.m_9236_()).updateSleepingPlayerList();
        }
        this.sleepCounter = boolean0 ? 0 : 100;
    }

    @Override
    public void stopSleeping() {
        this.stopSleepInBed(true, true);
    }

    public static Optional<Vec3> findRespawnPositionAndUseSpawnBlock(ServerLevel serverLevel0, BlockPos blockPos1, float float2, boolean boolean3, boolean boolean4) {
        BlockState $$5 = serverLevel0.m_8055_(blockPos1);
        Block $$6 = $$5.m_60734_();
        if ($$6 instanceof RespawnAnchorBlock && (boolean3 || (Integer) $$5.m_61143_(RespawnAnchorBlock.CHARGE) > 0) && RespawnAnchorBlock.canSetSpawn(serverLevel0)) {
            Optional<Vec3> $$7 = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, serverLevel0, blockPos1);
            if (!boolean3 && !boolean4 && $$7.isPresent()) {
                serverLevel0.m_7731_(blockPos1, (BlockState) $$5.m_61124_(RespawnAnchorBlock.CHARGE, (Integer) $$5.m_61143_(RespawnAnchorBlock.CHARGE) - 1), 3);
            }
            return $$7;
        } else if ($$6 instanceof BedBlock && BedBlock.canSetSpawn(serverLevel0)) {
            return BedBlock.findStandUpPosition(EntityType.PLAYER, serverLevel0, blockPos1, (Direction) $$5.m_61143_(BedBlock.f_54117_), float2);
        } else if (!boolean3) {
            return Optional.empty();
        } else {
            boolean $$8 = $$6.isPossibleToRespawnInThis($$5);
            BlockState $$9 = serverLevel0.m_8055_(blockPos1.above());
            boolean $$10 = $$9.m_60734_().isPossibleToRespawnInThis($$9);
            return $$8 && $$10 ? Optional.of(new Vec3((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.1, (double) blockPos1.m_123343_() + 0.5)) : Optional.empty();
        }
    }

    public boolean isSleepingLongEnough() {
        return this.m_5803_() && this.sleepCounter >= 100;
    }

    public int getSleepTimer() {
        return this.sleepCounter;
    }

    public void displayClientMessage(Component component0, boolean boolean1) {
    }

    public void awardStat(ResourceLocation resourceLocation0) {
        this.awardStat(Stats.CUSTOM.get(resourceLocation0));
    }

    public void awardStat(ResourceLocation resourceLocation0, int int1) {
        this.awardStat(Stats.CUSTOM.get(resourceLocation0), int1);
    }

    public void awardStat(Stat<?> stat0) {
        this.awardStat(stat0, 1);
    }

    public void awardStat(Stat<?> stat0, int int1) {
    }

    public void resetStat(Stat<?> stat0) {
    }

    public int awardRecipes(Collection<Recipe<?>> collectionRecipe0) {
        return 0;
    }

    public void triggerRecipeCrafted(Recipe<?> recipe0, List<ItemStack> listItemStack1) {
    }

    public void awardRecipesByKey(ResourceLocation[] resourceLocation0) {
    }

    public int resetRecipes(Collection<Recipe<?>> collectionRecipe0) {
        return 0;
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        this.awardStat(Stats.JUMP);
        if (this.m_20142_()) {
            this.causeFoodExhaustion(0.2F);
        } else {
            this.causeFoodExhaustion(0.05F);
        }
    }

    @Override
    public void travel(Vec3 vec0) {
        double $$1 = this.m_20185_();
        double $$2 = this.m_20186_();
        double $$3 = this.m_20189_();
        if (this.isSwimming() && !this.m_20159_()) {
            double $$4 = this.m_20154_().y;
            double $$5 = $$4 < -0.2 ? 0.085 : 0.06;
            if ($$4 <= 0.0 || this.f_20899_ || !this.m_9236_().getBlockState(BlockPos.containing(this.m_20185_(), this.m_20186_() + 1.0 - 0.1, this.m_20189_())).m_60819_().isEmpty()) {
                Vec3 $$6 = this.m_20184_();
                this.m_20256_($$6.add(0.0, ($$4 - $$6.y) * $$5, 0.0));
            }
        }
        if (this.abilities.flying && !this.m_20159_()) {
            double $$7 = this.m_20184_().y;
            super.travel(vec0);
            Vec3 $$8 = this.m_20184_();
            this.m_20334_($$8.x, $$7 * 0.6, $$8.z);
            this.m_183634_();
            this.m_20115_(7, false);
        } else {
            super.travel(vec0);
        }
        this.checkMovementStatistics(this.m_20185_() - $$1, this.m_20186_() - $$2, this.m_20189_() - $$3);
    }

    @Override
    public void updateSwimming() {
        if (this.abilities.flying) {
            this.m_20282_(false);
        } else {
            super.m_5844_();
        }
    }

    protected boolean freeAt(BlockPos blockPos0) {
        return !this.m_9236_().getBlockState(blockPos0).m_60828_(this.m_9236_(), blockPos0);
    }

    @Override
    public float getSpeed() {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    public void checkMovementStatistics(double double0, double double1, double double2) {
        if (!this.m_20159_()) {
            if (this.isSwimming()) {
                int $$3 = Math.round((float) Math.sqrt(double0 * double0 + double1 * double1 + double2 * double2) * 100.0F);
                if ($$3 > 0) {
                    this.awardStat(Stats.SWIM_ONE_CM, $$3);
                    this.causeFoodExhaustion(0.01F * (float) $$3 * 0.01F);
                }
            } else if (this.m_204029_(FluidTags.WATER)) {
                int $$4 = Math.round((float) Math.sqrt(double0 * double0 + double1 * double1 + double2 * double2) * 100.0F);
                if ($$4 > 0) {
                    this.awardStat(Stats.WALK_UNDER_WATER_ONE_CM, $$4);
                    this.causeFoodExhaustion(0.01F * (float) $$4 * 0.01F);
                }
            } else if (this.m_20069_()) {
                int $$5 = Math.round((float) Math.sqrt(double0 * double0 + double2 * double2) * 100.0F);
                if ($$5 > 0) {
                    this.awardStat(Stats.WALK_ON_WATER_ONE_CM, $$5);
                    this.causeFoodExhaustion(0.01F * (float) $$5 * 0.01F);
                }
            } else if (this.m_6147_()) {
                if (double1 > 0.0) {
                    this.awardStat(Stats.CLIMB_ONE_CM, (int) Math.round(double1 * 100.0));
                }
            } else if (this.m_20096_()) {
                int $$6 = Math.round((float) Math.sqrt(double0 * double0 + double2 * double2) * 100.0F);
                if ($$6 > 0) {
                    if (this.m_20142_()) {
                        this.awardStat(Stats.SPRINT_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.1F * (float) $$6 * 0.01F);
                    } else if (this.m_6047_()) {
                        this.awardStat(Stats.CROUCH_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.0F * (float) $$6 * 0.01F);
                    } else {
                        this.awardStat(Stats.WALK_ONE_CM, $$6);
                        this.causeFoodExhaustion(0.0F * (float) $$6 * 0.01F);
                    }
                }
            } else if (this.m_21255_()) {
                int $$7 = Math.round((float) Math.sqrt(double0 * double0 + double1 * double1 + double2 * double2) * 100.0F);
                this.awardStat(Stats.AVIATE_ONE_CM, $$7);
            } else {
                int $$8 = Math.round((float) Math.sqrt(double0 * double0 + double2 * double2) * 100.0F);
                if ($$8 > 25) {
                    this.awardStat(Stats.FLY_ONE_CM, $$8);
                }
            }
        }
    }

    private void checkRidingStatistics(double double0, double double1, double double2) {
        if (this.m_20159_()) {
            int $$3 = Math.round((float) Math.sqrt(double0 * double0 + double1 * double1 + double2 * double2) * 100.0F);
            if ($$3 > 0) {
                Entity $$4 = this.m_20202_();
                if ($$4 instanceof AbstractMinecart) {
                    this.awardStat(Stats.MINECART_ONE_CM, $$3);
                } else if ($$4 instanceof Boat) {
                    this.awardStat(Stats.BOAT_ONE_CM, $$3);
                } else if ($$4 instanceof Pig) {
                    this.awardStat(Stats.PIG_ONE_CM, $$3);
                } else if ($$4 instanceof AbstractHorse) {
                    this.awardStat(Stats.HORSE_ONE_CM, $$3);
                } else if ($$4 instanceof Strider) {
                    this.awardStat(Stats.STRIDER_ONE_CM, $$3);
                }
            }
        }
    }

    @Override
    public boolean causeFallDamage(float float0, float float1, DamageSource damageSource2) {
        if (this.abilities.mayfly) {
            return false;
        } else {
            if (float0 >= 2.0F) {
                this.awardStat(Stats.FALL_ONE_CM, (int) Math.round((double) float0 * 100.0));
            }
            return super.causeFallDamage(float0, float1, damageSource2);
        }
    }

    public boolean tryToStartFallFlying() {
        if (!this.m_20096_() && !this.m_21255_() && !this.m_20069_() && !this.m_21023_(MobEffects.LEVITATION)) {
            ItemStack $$0 = this.getItemBySlot(EquipmentSlot.CHEST);
            if ($$0.is(Items.ELYTRA) && ElytraItem.isFlyEnabled($$0)) {
                this.startFallFlying();
                return true;
            }
        }
        return false;
    }

    public void startFallFlying() {
        this.m_20115_(7, true);
    }

    public void stopFallFlying() {
        this.m_20115_(7, true);
        this.m_20115_(7, false);
    }

    @Override
    protected void doWaterSplashEffect() {
        if (!this.isSpectator()) {
            super.m_5841_();
        }
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
        if (this.m_20069_()) {
            this.m_280447_();
            this.m_280568_(blockState1);
        } else {
            BlockPos $$2 = this.m_276951_(blockPos0);
            if (!blockPos0.equals($$2)) {
                BlockState $$3 = this.m_9236_().getBlockState($$2);
                if ($$3.m_204336_(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
                    this.m_276961_($$3, blockState1);
                } else {
                    super.m_7355_($$2, $$3);
                }
            } else {
                super.m_7355_(blockPos0, blockState1);
            }
        }
    }

    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return new LivingEntity.Fallsounds(SoundEvents.PLAYER_SMALL_FALL, SoundEvents.PLAYER_BIG_FALL);
    }

    @Override
    public boolean killedEntity(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        this.awardStat(Stats.ENTITY_KILLED.get(livingEntity1.m_6095_()));
        return true;
    }

    @Override
    public void makeStuckInBlock(BlockState blockState0, Vec3 vec1) {
        if (!this.abilities.flying) {
            super.m_7601_(blockState0, vec1);
        }
    }

    public void giveExperiencePoints(int int0) {
        this.increaseScore(int0);
        this.experienceProgress = this.experienceProgress + (float) int0 / (float) this.getXpNeededForNextLevel();
        this.totalExperience = Mth.clamp(this.totalExperience + int0, 0, Integer.MAX_VALUE);
        while (this.experienceProgress < 0.0F) {
            float $$1 = this.experienceProgress * (float) this.getXpNeededForNextLevel();
            if (this.experienceLevel > 0) {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 1.0F + $$1 / (float) this.getXpNeededForNextLevel();
            } else {
                this.giveExperienceLevels(-1);
                this.experienceProgress = 0.0F;
            }
        }
        while (this.experienceProgress >= 1.0F) {
            this.experienceProgress = (this.experienceProgress - 1.0F) * (float) this.getXpNeededForNextLevel();
            this.giveExperienceLevels(1);
            this.experienceProgress = this.experienceProgress / (float) this.getXpNeededForNextLevel();
        }
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }

    public void onEnchantmentPerformed(ItemStack itemStack0, int int1) {
        this.experienceLevel -= int1;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }
        this.enchantmentSeed = this.f_19796_.nextInt();
    }

    public void giveExperienceLevels(int int0) {
        this.experienceLevel += int0;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experienceProgress = 0.0F;
            this.totalExperience = 0;
        }
        if (int0 > 0 && this.experienceLevel % 5 == 0 && (float) this.lastLevelUpTime < (float) this.f_19797_ - 100.0F) {
            float $$1 = this.experienceLevel > 30 ? 1.0F : (float) this.experienceLevel / 30.0F;
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_LEVELUP, this.getSoundSource(), $$1 * 0.75F, 1.0F);
            this.lastLevelUpTime = this.f_19797_;
        }
    }

    public int getXpNeededForNextLevel() {
        if (this.experienceLevel >= 30) {
            return 112 + (this.experienceLevel - 30) * 9;
        } else {
            return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2;
        }
    }

    public void causeFoodExhaustion(float float0) {
        if (!this.abilities.invulnerable) {
            if (!this.m_9236_().isClientSide) {
                this.foodData.addExhaustion(float0);
            }
        }
    }

    public Optional<WardenSpawnTracker> getWardenSpawnTracker() {
        return Optional.empty();
    }

    public FoodData getFoodData() {
        return this.foodData;
    }

    public boolean canEat(boolean boolean0) {
        return this.abilities.invulnerable || boolean0 || this.foodData.needsFood();
    }

    public boolean isHurt() {
        return this.m_21223_() > 0.0F && this.m_21223_() < this.m_21233_();
    }

    public boolean mayBuild() {
        return this.abilities.mayBuild;
    }

    public boolean mayUseItemAt(BlockPos blockPos0, Direction direction1, ItemStack itemStack2) {
        if (this.abilities.mayBuild) {
            return true;
        } else {
            BlockPos $$3 = blockPos0.relative(direction1.getOpposite());
            BlockInWorld $$4 = new BlockInWorld(this.m_9236_(), $$3, false);
            return itemStack2.hasAdventureModePlaceTagForBlock(this.m_9236_().registryAccess().registryOrThrow(Registries.BLOCK), $$4);
        }
    }

    @Override
    public int getExperienceReward() {
        if (!this.m_9236_().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && !this.isSpectator()) {
            int $$0 = this.experienceLevel * 7;
            return $$0 > 100 ? 100 : $$0;
        } else {
            return 0;
        }
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return true;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return this.abilities.flying || this.m_20096_() && this.m_20163_() ? Entity.MovementEmission.NONE : Entity.MovementEmission.ALL;
    }

    public void onUpdateAbilities() {
    }

    @Override
    public Component getName() {
        return Component.literal(this.gameProfile.getName());
    }

    public PlayerEnderChestContainer getEnderChestInventory() {
        return this.enderChestInventory;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot0) {
        if (equipmentSlot0 == EquipmentSlot.MAINHAND) {
            return this.inventory.getSelected();
        } else if (equipmentSlot0 == EquipmentSlot.OFFHAND) {
            return this.inventory.offhand.get(0);
        } else {
            return equipmentSlot0.getType() == EquipmentSlot.Type.ARMOR ? this.inventory.armor.get(equipmentSlot0.getIndex()) : ItemStack.EMPTY;
        }
    }

    @Override
    protected boolean doesEmitEquipEvent(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0.getType() == EquipmentSlot.Type.ARMOR;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
        this.m_181122_(itemStack1);
        if (equipmentSlot0 == EquipmentSlot.MAINHAND) {
            this.m_238392_(equipmentSlot0, this.inventory.items.set(this.inventory.selected, itemStack1), itemStack1);
        } else if (equipmentSlot0 == EquipmentSlot.OFFHAND) {
            this.m_238392_(equipmentSlot0, this.inventory.offhand.set(0, itemStack1), itemStack1);
        } else if (equipmentSlot0.getType() == EquipmentSlot.Type.ARMOR) {
            this.m_238392_(equipmentSlot0, this.inventory.armor.set(equipmentSlot0.getIndex(), itemStack1), itemStack1);
        }
    }

    public boolean addItem(ItemStack itemStack0) {
        return this.inventory.add(itemStack0);
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return Lists.newArrayList(new ItemStack[] { this.m_21205_(), this.m_21206_() });
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.inventory.armor;
    }

    public boolean setEntityOnShoulder(CompoundTag compoundTag0) {
        if (this.m_20159_() || !this.m_20096_() || this.m_20069_() || this.f_146808_) {
            return false;
        } else if (this.getShoulderEntityLeft().isEmpty()) {
            this.setShoulderEntityLeft(compoundTag0);
            this.timeEntitySatOnShoulder = this.m_9236_().getGameTime();
            return true;
        } else if (this.getShoulderEntityRight().isEmpty()) {
            this.setShoulderEntityRight(compoundTag0);
            this.timeEntitySatOnShoulder = this.m_9236_().getGameTime();
            return true;
        } else {
            return false;
        }
    }

    protected void removeEntitiesOnShoulder() {
        if (this.timeEntitySatOnShoulder + 20L < this.m_9236_().getGameTime()) {
            this.respawnEntityOnShoulder(this.getShoulderEntityLeft());
            this.setShoulderEntityLeft(new CompoundTag());
            this.respawnEntityOnShoulder(this.getShoulderEntityRight());
            this.setShoulderEntityRight(new CompoundTag());
        }
    }

    private void respawnEntityOnShoulder(CompoundTag compoundTag0) {
        if (!this.m_9236_().isClientSide && !compoundTag0.isEmpty()) {
            EntityType.create(compoundTag0, this.m_9236_()).ifPresent(p_289491_ -> {
                if (p_289491_ instanceof TamableAnimal) {
                    ((TamableAnimal) p_289491_).setOwnerUUID(this.f_19820_);
                }
                p_289491_.setPos(this.m_20185_(), this.m_20186_() + 0.7F, this.m_20189_());
                ((ServerLevel) this.m_9236_()).addWithUUID(p_289491_);
            });
        }
    }

    @Override
    public abstract boolean isSpectator();

    @Override
    public boolean canBeHitByProjectile() {
        return !this.isSpectator() && super.m_271807_();
    }

    @Override
    public boolean isSwimming() {
        return !this.abilities.flying && !this.isSpectator() && super.m_6069_();
    }

    public abstract boolean isCreative();

    @Override
    public boolean isPushedByFluid() {
        return !this.abilities.flying;
    }

    public Scoreboard getScoreboard() {
        return this.m_9236_().getScoreboard();
    }

    @Override
    public Component getDisplayName() {
        MutableComponent $$0 = PlayerTeam.formatNameForTeam(this.m_5647_(), this.getName());
        return this.decorateDisplayNameComponent($$0);
    }

    private MutableComponent decorateDisplayNameComponent(MutableComponent mutableComponent0) {
        String $$1 = this.getGameProfile().getName();
        return mutableComponent0.withStyle(p_289490_ -> p_289490_.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + $$1 + " ")).withHoverEvent(this.m_20190_()).withInsertion($$1));
    }

    @Override
    public String getScoreboardName() {
        return this.getGameProfile().getName();
    }

    @Override
    public float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        switch(pose0) {
            case SWIMMING:
            case FALL_FLYING:
            case SPIN_ATTACK:
                return 0.4F;
            case CROUCHING:
                return 1.27F;
            default:
                return 1.62F;
        }
    }

    @Override
    public void setAbsorptionAmount(float float0) {
        if (float0 < 0.0F) {
            float0 = 0.0F;
        }
        this.m_20088_().set(DATA_PLAYER_ABSORPTION_ID, float0);
    }

    @Override
    public float getAbsorptionAmount() {
        return this.m_20088_().get(DATA_PLAYER_ABSORPTION_ID);
    }

    public boolean isModelPartShown(PlayerModelPart playerModelPart0) {
        return (this.m_20088_().get(DATA_PLAYER_MODE_CUSTOMISATION) & playerModelPart0.getMask()) == playerModelPart0.getMask();
    }

    @Override
    public SlotAccess getSlot(int int0) {
        if (int0 >= 0 && int0 < this.inventory.items.size()) {
            return SlotAccess.forContainer(this.inventory, int0);
        } else {
            int $$1 = int0 - 200;
            return $$1 >= 0 && $$1 < this.enderChestInventory.m_6643_() ? SlotAccess.forContainer(this.enderChestInventory, $$1) : super.getSlot(int0);
        }
    }

    public boolean isReducedDebugInfo() {
        return this.reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean boolean0) {
        this.reducedDebugInfo = boolean0;
    }

    @Override
    public void setRemainingFireTicks(int int0) {
        super.m_7311_(this.abilities.invulnerable ? Math.min(int0, 1) : int0);
    }

    @Override
    public HumanoidArm getMainArm() {
        return this.f_19804_.get(DATA_PLAYER_MAIN_HAND) == 0 ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
    }

    public void setMainArm(HumanoidArm humanoidArm0) {
        this.f_19804_.set(DATA_PLAYER_MAIN_HAND, (byte) (humanoidArm0 == HumanoidArm.LEFT ? 0 : 1));
    }

    public CompoundTag getShoulderEntityLeft() {
        return this.f_19804_.get(DATA_SHOULDER_LEFT);
    }

    protected void setShoulderEntityLeft(CompoundTag compoundTag0) {
        this.f_19804_.set(DATA_SHOULDER_LEFT, compoundTag0);
    }

    public CompoundTag getShoulderEntityRight() {
        return this.f_19804_.get(DATA_SHOULDER_RIGHT);
    }

    protected void setShoulderEntityRight(CompoundTag compoundTag0) {
        this.f_19804_.set(DATA_SHOULDER_RIGHT, compoundTag0);
    }

    public float getCurrentItemAttackStrengthDelay() {
        return (float) (1.0 / this.m_21133_(Attributes.ATTACK_SPEED) * 20.0);
    }

    public float getAttackStrengthScale(float float0) {
        return Mth.clamp(((float) this.f_20922_ + float0) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    public void resetAttackStrengthTicker() {
        this.f_20922_ = 0;
    }

    public ItemCooldowns getCooldowns() {
        return this.cooldowns;
    }

    @Override
    protected float getBlockSpeedFactor() {
        return !this.abilities.flying && !this.m_21255_() ? super.getBlockSpeedFactor() : 1.0F;
    }

    public float getLuck() {
        return (float) this.m_21133_(Attributes.LUCK);
    }

    public boolean canUseGameMasterBlocks() {
        return this.abilities.instabuild && this.m_8088_() >= 2;
    }

    @Override
    public boolean canTakeItem(ItemStack itemStack0) {
        EquipmentSlot $$1 = Mob.m_147233_(itemStack0);
        return this.getItemBySlot($$1).isEmpty();
    }

    @Override
    public EntityDimensions getDimensions(Pose pose0) {
        return (EntityDimensions) POSES.getOrDefault(pose0, STANDING_DIMENSIONS);
    }

    @Override
    public ImmutableList<Pose> getDismountPoses() {
        return ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING);
    }

    @Override
    public ItemStack getProjectile(ItemStack itemStack0) {
        if (!(itemStack0.getItem() instanceof ProjectileWeaponItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> $$1 = ((ProjectileWeaponItem) itemStack0.getItem()).getSupportedHeldProjectiles();
            ItemStack $$2 = ProjectileWeaponItem.getHeldProjectile(this, $$1);
            if (!$$2.isEmpty()) {
                return $$2;
            } else {
                $$1 = ((ProjectileWeaponItem) itemStack0.getItem()).getAllSupportedProjectiles();
                for (int $$3 = 0; $$3 < this.inventory.getContainerSize(); $$3++) {
                    ItemStack $$4 = this.inventory.getItem($$3);
                    if ($$1.test($$4)) {
                        return $$4;
                    }
                }
                return this.abilities.instabuild ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
    }

    @Override
    public ItemStack eat(Level level0, ItemStack itemStack1) {
        this.getFoodData().eat(itemStack1.getItem(), itemStack1);
        this.awardStat(Stats.ITEM_USED.get(itemStack1.getItem()));
        level0.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, level0.random.nextFloat() * 0.1F + 0.9F);
        if (this instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) this, itemStack1);
        }
        return super.eat(level0, itemStack1);
    }

    @Override
    protected boolean shouldRemoveSoulSpeed(BlockState blockState0) {
        return this.abilities.flying || super.shouldRemoveSoulSpeed(blockState0);
    }

    @Override
    public Vec3 getRopeHoldPosition(float float0) {
        double $$1 = 0.22 * (this.getMainArm() == HumanoidArm.RIGHT ? -1.0 : 1.0);
        float $$2 = Mth.lerp(float0 * 0.5F, this.m_146909_(), this.f_19860_) * (float) (Math.PI / 180.0);
        float $$3 = Mth.lerp(float0, this.f_20884_, this.f_20883_) * (float) (Math.PI / 180.0);
        if (this.m_21255_() || this.m_21209_()) {
            Vec3 $$4 = this.m_20252_(float0);
            Vec3 $$5 = this.m_20184_();
            double $$6 = $$5.horizontalDistanceSqr();
            double $$7 = $$4.horizontalDistanceSqr();
            float $$10;
            if ($$6 > 0.0 && $$7 > 0.0) {
                double $$8 = ($$5.x * $$4.x + $$5.z * $$4.z) / Math.sqrt($$6 * $$7);
                double $$9 = $$5.x * $$4.z - $$5.z * $$4.x;
                $$10 = (float) (Math.signum($$9) * Math.acos($$8));
            } else {
                $$10 = 0.0F;
            }
            return this.m_20318_(float0).add(new Vec3($$1, -0.11, 0.85).zRot(-$$10).xRot(-$$2).yRot(-$$3));
        } else if (this.m_6067_()) {
            return this.m_20318_(float0).add(new Vec3($$1, 0.2, -0.15).xRot(-$$2).yRot(-$$3));
        } else {
            double $$12 = this.m_20191_().getYsize() - 1.0;
            double $$13 = this.m_6047_() ? -0.2 : 0.07;
            return this.m_20318_(float0).add(new Vec3($$1, $$12, $$13).yRot(-$$3));
        }
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    public boolean isScoping() {
        return this.m_6117_() && this.m_21211_().is(Items.SPYGLASS);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public Optional<GlobalPos> getLastDeathLocation() {
        return this.lastDeathLocation;
    }

    public void setLastDeathLocation(Optional<GlobalPos> optionalGlobalPos0) {
        this.lastDeathLocation = optionalGlobalPos0;
    }

    @Override
    public float getHurtDir() {
        return this.hurtDir;
    }

    @Override
    public void animateHurt(float float0) {
        super.animateHurt(float0);
        this.hurtDir = float0;
    }

    @Override
    public boolean canSprint() {
        return true;
    }

    @Override
    protected float getFlyingSpeed() {
        if (this.abilities.flying && !this.m_20159_()) {
            return this.m_20142_() ? this.abilities.getFlyingSpeed() * 2.0F : this.abilities.getFlyingSpeed();
        } else {
            return this.m_20142_() ? 0.025999999F : 0.02F;
        }
    }

    public static enum BedSleepingProblem {

        NOT_POSSIBLE_HERE,
        NOT_POSSIBLE_NOW(Component.translatable("block.minecraft.bed.no_sleep")),
        TOO_FAR_AWAY(Component.translatable("block.minecraft.bed.too_far_away")),
        OBSTRUCTED(Component.translatable("block.minecraft.bed.obstructed")),
        OTHER_PROBLEM,
        NOT_SAFE(Component.translatable("block.minecraft.bed.not_safe"));

        @Nullable
        private final Component message;

        private BedSleepingProblem() {
            this.message = null;
        }

        private BedSleepingProblem(Component p_36422_) {
            this.message = p_36422_;
        }

        @Nullable
        public Component getMessage() {
            return this.message;
        }
    }
}