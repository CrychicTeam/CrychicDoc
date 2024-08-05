package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.block.IDragonProof;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.client.model.util.LegSolverQuadruped;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIEscort;
import com.github.alexthe666.iceandfire.entity.ai.DragonAILookIdle;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIMate;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIReturnToRoost;
import com.github.alexthe666.iceandfire.entity.ai.DragonAITarget;
import com.github.alexthe666.iceandfire.entity.ai.DragonAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.DragonAITargetNonTamed;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIWander;
import com.github.alexthe666.iceandfire.entity.ai.DragonAIWatchClosest;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.EntityUtil;
import com.github.alexthe666.iceandfire.entity.util.HomePosition;
import com.github.alexthe666.iceandfire.entity.util.IAnimalFear;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.entity.util.IDragonFlute;
import com.github.alexthe666.iceandfire.entity.util.IDropArmor;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IMultipartEntity;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.entity.util.ReversedBuffer;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemDragonArmor;
import com.github.alexthe666.iceandfire.item.ItemSummoningCrystal;
import com.github.alexthe666.iceandfire.message.MessageDragonSetBurnBlock;
import com.github.alexthe666.iceandfire.message.MessageStartRidingMob;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.IPassabilityNavigator;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.PathingStuckHandler;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.ICustomSizeNavigator;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public abstract class EntityDragonBase extends TamableAnimal implements IPassabilityNavigator, ISyncMount, IFlyingMount, IMultipartEntity, IAnimatedEntity, IDragonFlute, IDeadMob, IVillagerFear, IAnimalFear, IDropArmor, IHasCustomizableAttributes, ICustomSizeNavigator, ICustomMoveController, ContainerListener {

    public static final int FLIGHT_CHANCE_PER_TICK = 1500;

    protected static final EntityDataAccessor<Boolean> SWIMMING = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");

    private static final EntityDataAccessor<Integer> HUNGER = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> AGE_TICKS = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> GENDER = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FIREBREATHING = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOVERING = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> MODEL_DEAD = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DEATH_STAGE = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> TACKLE = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> AGINGDISABLED = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> DRAGON_PITCH = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> CRYSTAL_BOUND = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<String> CUSTOM_POSE = SynchedEntityData.defineId(EntityDragonBase.class, EntityDataSerializers.STRING);

    public static Animation ANIMATION_FIRECHARGE;

    public static Animation ANIMATION_EAT;

    public static Animation ANIMATION_SPEAK;

    public static Animation ANIMATION_BITE;

    public static Animation ANIMATION_SHAKEPREY;

    public static Animation ANIMATION_WINGBLAST;

    public static Animation ANIMATION_ROAR;

    public static Animation ANIMATION_EPIC_ROAR;

    public static Animation ANIMATION_TAILWHACK;

    public DragonType dragonType;

    public double minimumDamage;

    public double maximumDamage;

    public double minimumHealth;

    public double maximumHealth;

    public double minimumSpeed;

    public double maximumSpeed;

    public double minimumArmor;

    public double maximumArmor;

    public float sitProgress;

    public float sleepProgress;

    public float hoverProgress;

    public float flyProgress;

    public float fireBreathProgress;

    public float diveProgress;

    public float prevDiveProgress;

    public float prevFireBreathProgress;

    public int fireStopTicks;

    public int flyTicks;

    public float modelDeadProgress;

    public float prevModelDeadProgress;

    public float ridingProgress;

    public float tackleProgress;

    public boolean isSwimming;

    public float prevSwimProgress;

    public float swimProgress;

    public int ticksSwiming;

    public int swimCycle;

    public float[] prevAnimationProgresses = new float[10];

    public boolean isDaytime;

    public int flightCycle;

    public HomePosition homePos;

    public boolean hasHomePosition = false;

    public IFChainBuffer roll_buffer;

    public IFChainBuffer pitch_buffer;

    public IFChainBuffer pitch_buffer_body;

    public ReversedBuffer turn_buffer;

    public ChainBuffer tail_buffer;

    public int spacebarTicks;

    public static final float[] growth_stage_1 = new float[] { 1.0F, 3.0F };

    public static final float[] growth_stage_2 = new float[] { 3.0F, 7.0F };

    public static final float[] growth_stage_3 = new float[] { 7.0F, 12.5F };

    public static final float[] growth_stage_4 = new float[] { 12.5F, 20.0F };

    public static final float[] growth_stage_5 = new float[] { 20.0F, 30.0F };

    public float[][] growth_stages = new float[][] { growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5 };

    public LegSolverQuadruped legSolver;

    public int walkCycle;

    public BlockPos burningTarget;

    public int burnProgress;

    public double burnParticleX;

    public double burnParticleY;

    public double burnParticleZ;

    public float prevDragonPitch;

    public IafDragonAttacks.Air airAttack;

    public IafDragonAttacks.Ground groundAttack;

    public boolean usingGroundAttack = true;

    public IafDragonLogic logic;

    public int hoverTicks;

    public int tacklingTicks;

    public int ticksStill;

    public int navigatorType;

    public SimpleContainer dragonInventory;

    public String prevArmorResLoc = "0|0|0|0";

    public String armorResLoc = "0|0|0|0";

    public IafDragonFlightManager flightManager;

    public boolean lookingForRoostAIFlag = false;

    protected int flyHovering;

    protected boolean hasHadHornUse = false;

    protected int fireTicks;

    protected int blockBreakCounter;

    private int prevFlightCycle;

    private boolean isModelDead;

    private int animationTick;

    private Animation currentAnimation;

    private float lastScale;

    private EntityDragonPart headPart;

    private EntityDragonPart neckPart;

    private EntityDragonPart rightWingUpperPart;

    private EntityDragonPart rightWingLowerPart;

    private EntityDragonPart leftWingUpperPart;

    private EntityDragonPart leftWingLowerPart;

    private EntityDragonPart tail1Part;

    private EntityDragonPart tail2Part;

    private EntityDragonPart tail3Part;

    private EntityDragonPart tail4Part;

    private boolean isOverAir;

    private LazyOptional<?> itemHandler = null;

    public boolean allowLocalMotionControl = true;

    public boolean allowMousePitchControl = true;

    protected boolean gliding = false;

    protected float glidingSpeedBonus = 0.0F;

    protected float riderWalkingExtraY = 0.0F;

    public EntityDragonBase(EntityType t, Level world, DragonType type, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
        super(t, world);
        this.dragonType = type;
        this.minimumDamage = minimumDamage;
        this.maximumDamage = maximumDamage;
        this.minimumHealth = minimumHealth;
        this.maximumHealth = maximumHealth;
        this.minimumSpeed = minimumSpeed;
        this.maximumSpeed = maximumSpeed;
        this.minimumArmor = 1.0;
        this.maximumArmor = 20.0;
        ANIMATION_EAT = Animation.create(20);
        this.createInventory();
        if (world.isClientSide) {
            this.roll_buffer = new IFChainBuffer();
            this.pitch_buffer = new IFChainBuffer();
            this.pitch_buffer_body = new IFChainBuffer();
            this.turn_buffer = new ReversedBuffer();
            this.tail_buffer = new ChainBuffer();
        }
        this.legSolver = new LegSolverQuadruped(0.3F, 0.35F, 0.2F, 1.45F, 1.0F);
        this.flightManager = new IafDragonFlightManager(this);
        this.logic = this.createDragonLogic();
        this.f_19811_ = true;
        this.switchNavigator(0);
        this.randomizeAttacks();
        this.resetParts(1.0F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.FOLLOW_RANGE, (double) Math.min(2048, IafConfig.dragonTargetSearchLength)).add(Attributes.ARMOR, 4.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) Math.min(2048, IafConfig.dragonTargetSearchLength));
    }

    @NotNull
    @Override
    public BlockPos getRestrictCenter() {
        return this.homePos == null ? super.m_21534_() : this.homePos.getPosition();
    }

    @Override
    public float getRestrictRadius() {
        return (float) IafConfig.dragonWanderFromHomeDistance;
    }

    public String getHomeDimensionName() {
        return this.homePos == null ? "" : this.homePos.getDimension();
    }

    @Override
    public boolean hasRestriction() {
        return this.hasHomePosition && this.getHomeDimensionName().equals(DragonUtils.getDimensionName(this.m_9236_())) || super.m_21536_();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new DragonAIMate(this, 1.0));
        this.f_21345_.addGoal(3, new DragonAIReturnToRoost(this, 1.0));
        this.f_21345_.addGoal(4, new DragonAIEscort(this, 1.0));
        this.f_21345_.addGoal(5, new DragonAIAttackMelee(this, 1.5, false));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.0, Ingredient.of(IafItemTags.TEMPT_DRAGON), false));
        this.f_21345_.addGoal(7, new DragonAIWander(this, 1.0));
        this.f_21345_.addGoal(8, new DragonAIWatchClosest(this, LivingEntity.class, 6.0F));
        this.f_21345_.addGoal(8, new DragonAILookIdle(this));
        this.f_21346_.addGoal(1, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(4, new DragonAITargetItems(this, 60, false, false, true));
        this.f_21346_.addGoal(5, new DragonAITargetNonTamed(this, LivingEntity.class, false, (Predicate) entity -> {
            if (entity instanceof Player player) {
                return !player.isCreative();
            } else {
                return this.m_217043_().nextInt(100) <= this.getHunger() ? false : entity.m_6095_() != this.m_6095_() && DragonUtils.canHostilesTarget(entity) && DragonUtils.isAlive(entity) && this.shouldTarget(entity);
            }
        }));
        this.f_21346_.addGoal(6, new DragonAITarget(this, LivingEntity.class, true, (Predicate) entity -> DragonUtils.canHostilesTarget(entity) && entity.m_6095_() != this.m_6095_() && this.shouldTarget(entity) && DragonUtils.isAlive(entity)));
        this.f_21346_.addGoal(7, new DragonAITargetItems(this, false));
    }

    protected abstract boolean shouldTarget(Entity var1);

    public void resetParts(float scale) {
        this.removeParts();
        this.headPart = new EntityDragonPart(this, 1.55F * scale, 0.0F, 0.6F * scale, 0.5F * scale, 0.35F * scale, 1.5F);
        this.headPart.m_20359_(this);
        this.headPart.setParent(this);
        this.neckPart = new EntityDragonPart(this, 0.85F * scale, 0.0F, 0.7F * scale, 0.5F * scale, 0.2F * scale, 1.0F);
        this.neckPart.m_20359_(this);
        this.neckPart.setParent(this);
        this.rightWingUpperPart = new EntityDragonPart(this, scale, 90.0F, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        this.rightWingUpperPart.m_20359_(this);
        this.rightWingUpperPart.setParent(this);
        this.rightWingLowerPart = new EntityDragonPart(this, 1.4F * scale, 100.0F, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        this.rightWingLowerPart.m_20359_(this);
        this.rightWingLowerPart.setParent(this);
        this.leftWingUpperPart = new EntityDragonPart(this, scale, -90.0F, 0.5F * scale, 0.85F * scale, 0.3F * scale, 0.5F);
        this.leftWingUpperPart.m_20359_(this);
        this.leftWingUpperPart.setParent(this);
        this.leftWingLowerPart = new EntityDragonPart(this, 1.4F * scale, -100.0F, 0.3F * scale, 0.85F * scale, 0.2F * scale, 0.5F);
        this.leftWingLowerPart.m_20359_(this);
        this.leftWingLowerPart.setParent(this);
        this.tail1Part = new EntityDragonPart(this, -0.75F * scale, 0.0F, 0.6F * scale, 0.35F * scale, 0.35F * scale, 1.0F);
        this.tail1Part.m_20359_(this);
        this.tail1Part.setParent(this);
        this.tail2Part = new EntityDragonPart(this, -1.15F * scale, 0.0F, 0.45F * scale, 0.35F * scale, 0.35F * scale, 1.0F);
        this.tail2Part.m_20359_(this);
        this.tail2Part.setParent(this);
        this.tail3Part = new EntityDragonPart(this, -1.5F * scale, 0.0F, 0.35F * scale, 0.35F * scale, 0.35F * scale, 1.0F);
        this.tail3Part.m_20359_(this);
        this.tail3Part.setParent(this);
        this.tail4Part = new EntityDragonPart(this, -1.95F * scale, 0.0F, 0.25F * scale, 0.45F * scale, 0.3F * scale, 1.5F);
        this.tail4Part.m_20359_(this);
        this.tail4Part.setParent(this);
    }

    public void removeParts() {
        if (this.headPart != null) {
            this.headPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.headPart = null;
        }
        if (this.neckPart != null) {
            this.neckPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.neckPart = null;
        }
        if (this.rightWingUpperPart != null) {
            this.rightWingUpperPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.rightWingUpperPart = null;
        }
        if (this.rightWingLowerPart != null) {
            this.rightWingLowerPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.rightWingLowerPart = null;
        }
        if (this.leftWingUpperPart != null) {
            this.leftWingUpperPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.leftWingUpperPart = null;
        }
        if (this.leftWingLowerPart != null) {
            this.leftWingLowerPart.m_142687_(Entity.RemovalReason.DISCARDED);
            this.leftWingLowerPart = null;
        }
        if (this.tail1Part != null) {
            this.tail1Part.m_142687_(Entity.RemovalReason.DISCARDED);
            this.tail1Part = null;
        }
        if (this.tail2Part != null) {
            this.tail2Part.m_142687_(Entity.RemovalReason.DISCARDED);
            this.tail2Part = null;
        }
        if (this.tail3Part != null) {
            this.tail3Part.m_142687_(Entity.RemovalReason.DISCARDED);
            this.tail3Part = null;
        }
        if (this.tail4Part != null) {
            this.tail4Part.m_142687_(Entity.RemovalReason.DISCARDED);
            this.tail4Part = null;
        }
    }

    public void updateParts() {
        EntityUtil.updatePart(this.headPart, this);
        EntityUtil.updatePart(this.neckPart, this);
        EntityUtil.updatePart(this.rightWingUpperPart, this);
        EntityUtil.updatePart(this.rightWingLowerPart, this);
        EntityUtil.updatePart(this.leftWingUpperPart, this);
        EntityUtil.updatePart(this.leftWingLowerPart, this);
        EntityUtil.updatePart(this.tail1Part, this);
        EntityUtil.updatePart(this.tail2Part, this);
        EntityUtil.updatePart(this.tail3Part, this);
        EntityUtil.updatePart(this.tail4Part, this);
    }

    protected void updateBurnTarget() {
        if (this.burningTarget != null && !this.isSleeping() && !this.isModelDead() && !this.isBaby()) {
            float maxDist = (float) (115 * this.getDragonStage());
            if (this.m_9236_().getBlockEntity(this.burningTarget) instanceof TileEntityDragonforgeInput forge && forge.isAssembled() && this.m_20275_((double) this.burningTarget.m_123341_() + 0.5, (double) this.burningTarget.m_123342_() + 0.5, (double) this.burningTarget.m_123343_() + 0.5) < (double) maxDist && this.canPositionBeSeen((double) this.burningTarget.m_123341_() + 0.5, (double) this.burningTarget.m_123342_() + 0.5, (double) this.burningTarget.m_123343_() + 0.5)) {
                this.m_21563_().setLookAt((double) this.burningTarget.m_123341_() + 0.5, (double) this.burningTarget.m_123342_() + 0.5, (double) this.burningTarget.m_123343_() + 0.5, 180.0F, 180.0F);
                this.breathFireAtPos(this.burningTarget);
                this.setBreathingFire(true);
                return;
            }
            if (!this.m_9236_().isClientSide) {
                IceAndFire.sendMSGToAll(new MessageDragonSetBurnBlock(this.m_19879_(), true, this.burningTarget));
            }
            this.burningTarget = null;
        }
    }

    protected abstract void breathFireAtPos(BlockPos var1);

    protected PathingStuckHandler createStuckHandler() {
        return PathingStuckHandler.createStuckHandler();
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level worldIn) {
        return this.createNavigator(worldIn, AdvancedPathNavigate.MovementType.WALKING);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type) {
        return this.createNavigator(worldIn, type, this.createStuckHandler());
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler) {
        return this.createNavigator(worldIn, type, stuckHandler, 4.0F, 4.0F);
    }

    protected PathNavigation createNavigator(Level worldIn, AdvancedPathNavigate.MovementType type, PathingStuckHandler stuckHandler, float width, float height) {
        AdvancedPathNavigate newNavigator = new AdvancedPathNavigate(this, this.m_9236_(), type, width, height);
        this.f_21344_ = newNavigator;
        newNavigator.setCanFloat(true);
        newNavigator.m_26575_().setCanOpenDoors(true);
        return newNavigator;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.f_21342_ = new IafDragonFlightManager.GroundMoveHelper(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.WALKING, this.createStuckHandler().withTeleportSteps(5));
            this.navigatorType = 0;
            this.setFlying(false);
            this.setHovering(false);
        } else if (navigatorType == 1) {
            this.f_21342_ = new IafDragonFlightManager.FlightMoveHelper(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.FLYING);
            this.navigatorType = 1;
        } else {
            this.f_21342_ = new IafDragonFlightManager.PlayerFlightMoveHelper<>(this);
            this.f_21344_ = this.createNavigator(this.m_9236_(), AdvancedPathNavigate.MovementType.FLYING);
            this.navigatorType = 2;
        }
    }

    @Override
    public boolean canRide(@NotNull Entity rider) {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        this.breakBlocks(false);
    }

    @Override
    public void checkDespawn() {
        if (IafConfig.canDragonsDespawn) {
            super.m_6043_();
        }
    }

    public boolean canDestroyBlock(BlockPos pos, BlockState state) {
        return state.m_60734_().canEntityDestroy(state, this.m_9236_(), pos, this);
    }

    @Override
    public boolean isMobDead() {
        return this.isModelDead();
    }

    @Override
    public int getMaxHeadYRot() {
        return 30 * this.getDragonStage() / 5;
    }

    public void openInventory(Player player) {
        if (!this.m_9236_().isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, this.getMenuProvider());
        }
        IceAndFire.PROXY.setReferencedMob(this);
    }

    public MenuProvider getMenuProvider() {
        return new SimpleMenuProvider((containerId, playerInventory, player) -> new ContainerDragon(containerId, this.dragonInventory, playerInventory, this), this.m_5446_());
    }

    @Override
    public int getAmbientSoundInterval() {
        return 90;
    }

    @Override
    protected void tickDeath() {
        this.f_20919_ = 0;
        this.setModelDead(true);
        this.m_20153_();
        if (this.getDeathStage() >= this.getAgeInDays() / 5) {
            this.remove(Entity.RemovalReason.KILLED);
            for (int k = 0; k < 40; k++) {
                double d2 = this.f_19796_.nextGaussian() * 0.02;
                double d0 = this.f_19796_.nextGaussian() * 0.02;
                double d1 = this.f_19796_.nextGaussian() * 0.02;
                if (this.m_9236_().isClientSide) {
                    this.m_9236_().addParticle(ParticleTypes.CLOUD, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d2, d0, d1);
                }
            }
            this.spawnDeathParticles();
        }
    }

    protected void spawnDeathParticles() {
    }

    protected void spawnBabyParticles() {
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
        this.removeParts();
        super.m_142687_(reason);
    }

    @Override
    public int getExperienceReward() {
        return switch(this.getDragonStage()) {
            case 2 ->
                20;
            case 3 ->
                150;
            case 4 ->
                300;
            case 5 ->
                650;
            default ->
                5;
        };
    }

    public int getArmorOrdinal(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemDragonArmor armorItem ? armorItem.type.ordinal() + 1 : 0;
    }

    @Override
    public boolean isNoAi() {
        return this.isModelDead() || super.m_21525_();
    }

    public boolean isAiDisabled() {
        return super.m_21525_();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(HUNGER, 0);
        this.f_19804_.define(AGE_TICKS, 0);
        this.f_19804_.define(GENDER, false);
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(SLEEPING, false);
        this.f_19804_.define(FIREBREATHING, false);
        this.f_19804_.define(HOVERING, false);
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(DEATH_STAGE, 0);
        this.f_19804_.define(MODEL_DEAD, false);
        this.f_19804_.define(CONTROL_STATE, (byte) 0);
        this.f_19804_.define(TACKLE, false);
        this.f_19804_.define(AGINGDISABLED, false);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(DRAGON_PITCH, 0.0F);
        this.f_19804_.define(CRYSTAL_BOUND, false);
        this.f_19804_.define(CUSTOM_POSE, "");
    }

    @Override
    public boolean isGoingUp() {
        return (this.f_19804_.get(CONTROL_STATE) & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (this.f_19804_.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean isAttacking() {
        return (this.f_19804_.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    public boolean isStriking() {
        return (this.f_19804_.get(CONTROL_STATE) >> 3 & 1) == 1;
    }

    public boolean isDismounting() {
        return (this.f_19804_.get(CONTROL_STATE) >> 4 & 1) == 1;
    }

    @Override
    public void up(boolean up) {
        this.setStateField(0, up);
    }

    @Override
    public void down(boolean down) {
        this.setStateField(1, down);
    }

    @Override
    public void attack(boolean attack) {
        this.setStateField(2, attack);
    }

    @Override
    public void strike(boolean strike) {
        this.setStateField(3, strike);
    }

    @Override
    public void dismount(boolean dismount) {
        this.setStateField(4, dismount);
    }

    private void setStateField(int i, boolean newState) {
        byte prevState = this.f_19804_.get(CONTROL_STATE);
        if (newState) {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState | 1 << i));
        } else {
            this.f_19804_.set(CONTROL_STATE, (byte) (prevState & ~(1 << i)));
        }
    }

    @Override
    public byte getControlState() {
        return this.f_19804_.get(CONTROL_STATE);
    }

    @Override
    public void setControlState(byte state) {
        this.f_19804_.set(CONTROL_STATE, state);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    public float getDragonPitch() {
        return this.f_19804_.get(DRAGON_PITCH);
    }

    public void setDragonPitch(float pitch) {
        this.f_19804_.set(DRAGON_PITCH, pitch);
    }

    public void incrementDragonPitch(float pitch) {
        this.f_19804_.set(DRAGON_PITCH, this.getDragonPitch() + pitch);
    }

    public void decrementDragonPitch(float pitch) {
        this.f_19804_.set(DRAGON_PITCH, this.getDragonPitch() - pitch);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Hunger", this.getHunger());
        compound.putInt("AgeTicks", this.getAgeInTicks());
        compound.putBoolean("Gender", this.isMale());
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("TamedDragon", this.m_21824_());
        compound.putBoolean("FireBreathing", this.isBreathingFire());
        compound.putBoolean("AttackDecision", this.usingGroundAttack);
        compound.putBoolean("Hovering", this.isHovering());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("DeathStage", this.getDeathStage());
        compound.putBoolean("ModelDead", this.isModelDead());
        compound.putFloat("DeadProg", this.modelDeadProgress);
        compound.putBoolean("Tackle", this.isTackling());
        compound.putBoolean("HasHomePosition", this.hasHomePosition);
        compound.putString("CustomPose", this.getCustomPose());
        if (this.homePos != null && this.hasHomePosition) {
            this.homePos.write(compound);
        }
        compound.putBoolean("AgingDisabled", this.isAgingDisabled());
        compound.putInt("Command", this.getCommand());
        if (this.dragonInventory != null) {
            ListTag nbttaglist = new ListTag();
            for (int i = 0; i < this.dragonInventory.getContainerSize(); i++) {
                ItemStack itemstack = this.dragonInventory.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag CompoundNBT = new CompoundTag();
                    CompoundNBT.putByte("Slot", (byte) i);
                    itemstack.save(CompoundNBT);
                    nbttaglist.add(CompoundNBT);
                }
            }
            compound.put("Items", nbttaglist);
        }
        compound.putBoolean("CrystalBound", this.isBoundToCrystal());
        if (this.m_8077_()) {
            compound.putString("CustomName", Component.Serializer.toJson(this.m_7770_()));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHunger(compound.getInt("Hunger"));
        this.setAgeInTicks(compound.getInt("AgeTicks"));
        this.setGender(compound.getBoolean("Gender"));
        this.setVariant(compound.getInt("Variant"));
        this.setInSittingPose(compound.getBoolean("Sleeping"));
        this.m_7105_(compound.getBoolean("TamedDragon"));
        this.setBreathingFire(compound.getBoolean("FireBreathing"));
        this.usingGroundAttack = compound.getBoolean("AttackDecision");
        this.setHovering(compound.getBoolean("Hovering"));
        this.setFlying(compound.getBoolean("Flying"));
        this.setDeathStage(compound.getInt("DeathStage"));
        this.setModelDead(compound.getBoolean("ModelDead"));
        this.modelDeadProgress = compound.getFloat("DeadProg");
        this.setCustomPose(compound.getString("CustomPose"));
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (this.hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            this.homePos = new HomePosition(compound, this.m_9236_());
        }
        this.setTackling(compound.getBoolean("Tackle"));
        this.setAgingDisabled(compound.getBoolean("AgingDisabled"));
        this.setCommand(compound.getInt("Command"));
        if (this.dragonInventory != null) {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (Tag inbt : nbttaglist) {
                CompoundTag CompoundNBT = (CompoundTag) inbt;
                int j = CompoundNBT.getByte("Slot") & 255;
                if (j <= 4) {
                    this.dragonInventory.setItem(j, ItemStack.of(CompoundNBT));
                }
            }
        } else {
            ListTag nbttaglist = compound.getList("Items", 10);
            this.createInventory();
            for (Tag inbtx : nbttaglist) {
                CompoundTag CompoundNBT = (CompoundTag) inbtx;
                int j = CompoundNBT.getByte("Slot") & 255;
                this.dragonInventory.setItem(j, ItemStack.of(CompoundNBT));
            }
        }
        this.setCrystalBound(compound.getBoolean("CrystalBound"));
        if (compound.contains("CustomName", 8) && !compound.getString("CustomName").startsWith("TextComponent")) {
            this.m_6593_(Component.Serializer.fromJson(compound.getString("CustomName")));
        }
        this.setConfigurableAttributes();
        this.updateAttributes();
    }

    public int getContainerSize() {
        return 5;
    }

    protected void createInventory() {
        SimpleContainer tempInventory = this.dragonInventory;
        this.dragonInventory = new SimpleContainer(this.getContainerSize());
        if (tempInventory != null) {
            tempInventory.removeListener(this);
            int i = Math.min(tempInventory.getContainerSize(), this.dragonInventory.getContainerSize());
            for (int j = 0; j < i; j++) {
                ItemStack itemstack = tempInventory.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.dragonInventory.setItem(j, itemstack.copy());
                }
            }
        }
        this.dragonInventory.addListener(this);
        this.updateContainerEquipment();
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.dragonInventory));
    }

    protected void updateContainerEquipment() {
        if (!this.m_9236_().isClientSide) {
            this.updateAttributes();
        }
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return this.isAlive() && capability == ForgeCapabilities.ITEM_HANDLER && this.itemHandler != null ? this.itemHandler.cast() : super.getCapability(capability, facing);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.itemHandler != null) {
            LazyOptional<?> oldHandler = this.itemHandler;
            this.itemHandler = null;
            oldHandler.invalidate();
        }
    }

    public boolean hasInventoryChanged(Container pInventory) {
        return this.dragonInventory != pInventory;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player player && this.m_5448_() != passenger && this.m_21824_() && this.m_21805_() != null && this.m_21805_().equals(player.m_20148_())) {
                return player;
            }
        }
        return null;
    }

    public boolean isRidingPlayer(Player player) {
        return this.getRidingPlayer() != null && player != null && this.getRidingPlayer().m_20148_().equals(player.m_20148_());
    }

    @Nullable
    @Override
    public Player getRidingPlayer() {
        LivingEntity var2 = this.getControllingPassenger();
        return var2 instanceof Player ? (Player) var2 : null;
    }

    protected void updateAttributes() {
        this.prevArmorResLoc = this.armorResLoc;
        int armorHead = this.getArmorOrdinal(this.getItemBySlot(EquipmentSlot.HEAD));
        int armorNeck = this.getArmorOrdinal(this.getItemBySlot(EquipmentSlot.CHEST));
        int armorLegs = this.getArmorOrdinal(this.getItemBySlot(EquipmentSlot.LEGS));
        int armorFeet = this.getArmorOrdinal(this.getItemBySlot(EquipmentSlot.FEET));
        this.armorResLoc = this.dragonType.getName() + "|" + armorHead + "|" + armorNeck + "|" + armorLegs + "|" + armorFeet;
        IceAndFire.PROXY.updateDragonArmorRender(this.armorResLoc);
        double age = 125.0;
        if (this.getAgeInDays() <= 125) {
            age = (double) this.getAgeInDays();
        }
        double healthStep = (this.maximumHealth - this.minimumHealth) / 125.0;
        double attackStep = (this.maximumDamage - this.minimumDamage) / 125.0;
        double speedStep = (this.maximumSpeed - this.minimumSpeed) / 125.0;
        double armorStep = (this.maximumArmor - this.minimumArmor) / 125.0;
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) Math.round(this.minimumHealth + healthStep * age));
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) Math.round(this.minimumDamage + attackStep * age));
        this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue(this.minimumSpeed + speedStep * age);
        double baseValue = this.minimumArmor + armorStep * (double) this.getAgeInDays();
        this.m_21051_(Attributes.ARMOR).setBaseValue(baseValue);
        if (!this.m_9236_().isClientSide) {
            this.m_21051_(Attributes.ARMOR).removeModifier(ARMOR_MODIFIER_UUID);
            this.m_21051_(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(ARMOR_MODIFIER_UUID, "Dragon armor bonus", this.calculateArmorModifier(), AttributeModifier.Operation.ADDITION));
        }
        this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue((double) Math.min(2048, IafConfig.dragonTargetSearchLength));
    }

    public int getHunger() {
        return this.f_19804_.get(HUNGER);
    }

    public void setHunger(int hunger) {
        this.f_19804_.set(HUNGER, Mth.clamp(hunger, 0, 100));
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    public int getAgeInDays() {
        return this.f_19804_.get(AGE_TICKS) / 24000;
    }

    public void setAgeInDays(int age) {
        this.f_19804_.set(AGE_TICKS, age * 24000);
    }

    public int getAgeInTicks() {
        return this.f_19804_.get(AGE_TICKS);
    }

    public void setAgeInTicks(int age) {
        this.f_19804_.set(AGE_TICKS, age);
    }

    public int getDeathStage() {
        return this.f_19804_.get(DEATH_STAGE);
    }

    public void setDeathStage(int stage) {
        this.f_19804_.set(DEATH_STAGE, stage);
    }

    public boolean isMale() {
        return this.f_19804_.get(GENDER);
    }

    public boolean isModelDead() {
        return this.m_9236_().isClientSide ? (this.isModelDead = this.f_19804_.get(MODEL_DEAD)) : this.isModelDead;
    }

    public void setModelDead(boolean modeldead) {
        this.f_19804_.set(MODEL_DEAD, modeldead);
        if (!this.m_9236_().isClientSide) {
            this.isModelDead = modeldead;
        }
    }

    @Override
    public boolean isHovering() {
        return this.f_19804_.get(HOVERING);
    }

    public void setHovering(boolean hovering) {
        this.f_19804_.set(HOVERING, hovering);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
    }

    public boolean useFlyingPathFinder() {
        return this.isFlying() && this.getControllingPassenger() == null;
    }

    public void setGender(boolean male) {
        this.f_19804_.set(GENDER, male);
    }

    @Override
    public boolean isSleeping() {
        return this.f_19804_.get(SLEEPING);
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 43;
    }

    public boolean isBreathingFire() {
        return this.f_19804_.get(FIREBREATHING);
    }

    public void setBreathingFire(boolean breathing) {
        this.f_19804_.set(FIREBREATHING, breathing);
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return this.m_20197_().size() < 2;
    }

    @Override
    public boolean isOrderedToSit() {
        return (this.f_19804_.<Byte>get(f_21798_) & 1) != 0;
    }

    @Override
    public void setInSittingPose(boolean sleeping) {
        this.f_19804_.set(SLEEPING, sleeping);
        if (sleeping) {
            this.m_21573_().stop();
        }
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        byte b0 = this.f_19804_.<Byte>get(f_21798_);
        if (sitting) {
            this.f_19804_.set(f_21798_, (byte) (b0 | 1));
            this.m_21573_().stop();
        } else {
            this.f_19804_.set(f_21798_, (byte) (b0 & -2));
        }
    }

    public String getCustomPose() {
        return this.f_19804_.get(CUSTOM_POSE);
    }

    public void setCustomPose(String customPose) {
        this.f_19804_.set(CUSTOM_POSE, customPose);
        this.modelDeadProgress = 20.0F;
    }

    public void riderShootFire(Entity controller) {
    }

    private double calculateArmorModifier() {
        double val = 1.0;
        EquipmentSlot[] slots = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : slots) {
            switch(this.getArmorOrdinal(this.getItemBySlot(slot))) {
                case 1:
                    val += 2.0;
                    break;
                case 2:
                case 4:
                    val += 3.0;
                    break;
                case 3:
                    val += 5.0;
                    break;
                case 5:
                case 6:
                case 8:
                    val += 10.0;
                    break;
                case 7:
                    val++;
            }
        }
        return val;
    }

    public boolean canMove() {
        return !this.isOrderedToSit() && !this.isSleeping() && this.getControllingPassenger() == null && !this.m_20159_() && !this.isModelDead() && this.sleepProgress == 0.0F && this.getAnimation() != ANIMATION_SHAKEPREY;
    }

    public boolean isFuelingForge() {
        return this.burningTarget != null && this.m_9236_().getBlockEntity(this.burningTarget) instanceof TileEntityDragonforgeInput;
    }

    @Override
    public boolean isAlive() {
        return this.isModelDead() ? !this.m_213877_() : super.m_6084_();
    }

    @NotNull
    @Override
    public InteractionResult interactAt(Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        int lastDeathStage = Math.min(this.getAgeInDays() / 5, 25);
        if (stack.getItem() == IafItemRegistry.DRAGON_DEBUG_STICK.get()) {
            this.logic.debug();
            return InteractionResult.SUCCESS;
        } else if (!this.isModelDead() || this.getDeathStage() >= lastDeathStage || !player.mayBuild()) {
            return super.m_7111_(player, vec, hand);
        } else if (!this.m_9236_().isClientSide && !stack.isEmpty() && stack.getItem() != null && stack.getItem() == Items.GLASS_BOTTLE && this.getDeathStage() < lastDeathStage / 2 && IafConfig.dragonDropBlood) {
            if (!player.isCreative()) {
                stack.shrink(1);
            }
            this.setDeathStage(this.getDeathStage() + 1);
            player.getInventory().add(new ItemStack(this.getBloodItem(), 1));
            return InteractionResult.SUCCESS;
        } else {
            if (!this.m_9236_().isClientSide && stack.isEmpty() && IafConfig.dragonDropSkull) {
                if (this.getDeathStage() >= lastDeathStage - 1) {
                    ItemStack skull = this.getSkull().copy();
                    skull.setTag(new CompoundTag());
                    skull.getTag().putInt("Stage", this.getDragonStage());
                    skull.getTag().putInt("DragonType", 0);
                    skull.getTag().putInt("DragonAge", this.getAgeInDays());
                    this.setDeathStage(this.getDeathStage() + 1);
                    if (!this.m_9236_().isClientSide) {
                        this.m_5552_(skull, 1.0F);
                    }
                    this.remove(Entity.RemovalReason.DISCARDED);
                } else if (this.getDeathStage() == lastDeathStage / 2 - 1 && IafConfig.dragonDropHeart) {
                    ItemStack heart = new ItemStack(this.getHeartItem(), 1);
                    ItemStack egg = new ItemStack(this.getVariantEgg(this.f_19796_.nextInt(4)), 1);
                    if (!this.m_9236_().isClientSide) {
                        this.m_5552_(heart, 1.0F);
                        if (!this.isMale() && this.getDragonStage() > 3) {
                            this.m_5552_(egg, 1.0F);
                        }
                    }
                    this.setDeathStage(this.getDeathStage() + 1);
                } else {
                    this.setDeathStage(this.getDeathStage() + 1);
                    ItemStack drop = this.getRandomDrop();
                    if (!drop.isEmpty() && !this.m_9236_().isClientSide) {
                        this.m_5552_(drop, 1.0F);
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.m_21205_();
        if (stack == ItemStack.EMPTY) {
            stack = player.m_21120_(hand);
        }
        if (stack.getItem() == IafItemRegistry.DRAGON_DEBUG_STICK.get()) {
            this.logic.debug();
            return InteractionResult.SUCCESS;
        } else {
            if (!this.isModelDead()) {
                if (stack.getItem() == IafItemRegistry.CREATIVE_DRAGON_MEAL.get()) {
                    this.m_7105_(true);
                    this.m_21828_(player);
                    this.setHunger(this.getHunger() + 20);
                    this.m_5634_(Math.min(this.m_21223_(), (float) ((int) (this.m_21233_() / 2.0F))));
                    this.playSound(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                    this.spawnItemCrackParticles(stack.getItem());
                    this.spawnItemCrackParticles(Items.BONE);
                    this.spawnItemCrackParticles(Items.BONE_MEAL);
                    this.eatFoodBonus(stack);
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (this.m_6898_(stack) && this.shouldDropLoot()) {
                    this.m_146762_(0);
                    this.m_142075_(player, InteractionHand.MAIN_HAND, stack);
                    this.m_27595_(player);
                    return InteractionResult.SUCCESS;
                }
                if (this.m_21830_(player)) {
                    if (stack.getItem() == this.getSummoningCrystal() && !ItemSummoningCrystal.hasDragon(stack)) {
                        this.setCrystalBound(true);
                        CompoundTag compound = stack.getOrCreateTag();
                        CompoundTag dragonTag = new CompoundTag();
                        dragonTag.putUUID("DragonUUID", this.m_20148_());
                        if (this.m_7770_() != null) {
                            dragonTag.putString("CustomName", this.m_7770_().getString());
                        }
                        compound.put("Dragon", dragonTag);
                        this.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
                        player.m_6674_(hand);
                        return InteractionResult.SUCCESS;
                    }
                    this.m_21828_(player);
                    if (stack.getItem() == IafItemRegistry.DRAGON_HORN.get()) {
                        return super.m_6071_(player, hand);
                    }
                    if (stack.isEmpty() && !player.m_6144_()) {
                        if (!this.m_9236_().isClientSide) {
                            int dragonStage = this.getDragonStage();
                            if (dragonStage < 2) {
                                if (player.m_20197_().size() >= 3) {
                                    return InteractionResult.FAIL;
                                }
                                this.m_7998_(player, true);
                                IceAndFire.sendMSGToAll(new MessageStartRidingMob(this.m_19879_(), true, true));
                            } else if (dragonStage > 2 && !player.m_20159_()) {
                                player.m_20260_(false);
                                player.m_7998_(this, true);
                                IceAndFire.sendMSGToAll(new MessageStartRidingMob(this.m_19879_(), true, false));
                                this.setInSittingPose(false);
                            }
                            this.m_21573_().stop();
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (stack.isEmpty() && player.m_6144_()) {
                        this.openInventory(player);
                        return InteractionResult.SUCCESS;
                    }
                    int itemFoodAmount = FoodUtils.getFoodPoints(stack, true, this.dragonType.isPiscivore());
                    if (itemFoodAmount > 0 && (this.getHunger() < 100 || this.m_21223_() < this.m_21233_())) {
                        this.setHunger(this.getHunger() + itemFoodAmount);
                        this.m_21153_(Math.min(this.m_21233_(), (float) ((int) (this.m_21223_() + (float) (itemFoodAmount / 10)))));
                        this.playSound(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                        this.spawnItemCrackParticles(stack.getItem());
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    Item stackItem = stack.getItem();
                    if (stackItem == IafItemRegistry.DRAGON_MEAL.get()) {
                        this.growDragon(1);
                        this.setHunger(this.getHunger() + 20);
                        this.m_5634_(Math.min(this.m_21223_(), (float) ((int) (this.m_21233_() / 2.0F))));
                        this.playSound(SoundEvents.GENERIC_EAT, this.m_6121_(), this.m_6100_());
                        this.spawnItemCrackParticles(stackItem);
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.BONE_MEAL);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (stackItem == IafItemRegistry.SICKLY_DRAGON_MEAL.get() && !this.isAgingDisabled()) {
                        this.setHunger(this.getHunger() + 20);
                        this.m_5634_(this.m_21233_());
                        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, this.m_6121_(), this.m_6100_());
                        this.spawnItemCrackParticles(stackItem);
                        this.spawnItemCrackParticles(Items.BONE);
                        this.spawnItemCrackParticles(Items.BONE_MEAL);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.spawnItemCrackParticles(Items.POISONOUS_POTATO);
                        this.setAgingDisabled(true);
                        this.eatFoodBonus(stack);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.SUCCESS;
                    }
                    if (stackItem == IafItemRegistry.DRAGON_STAFF.get()) {
                        if (player.m_6144_()) {
                            if (this.hasHomePosition) {
                                this.hasHomePosition = false;
                                player.displayClientMessage(Component.translatable("dragon.command.remove_home"), true);
                                return InteractionResult.SUCCESS;
                            }
                            BlockPos pos = this.m_20183_();
                            this.homePos = new HomePosition(pos, this.m_9236_());
                            this.hasHomePosition = true;
                            player.displayClientMessage(Component.translatable("dragon.command.new_home", pos.m_123341_(), pos.m_123342_(), pos.m_123343_(), this.homePos.getDimension()), true);
                            return InteractionResult.SUCCESS;
                        }
                        this.playSound(SoundEvents.ZOMBIE_INFECT, this.m_6121_(), this.m_6100_());
                        if (!this.m_9236_().isClientSide) {
                            this.setCommand(this.getCommand() + 1);
                            if (this.getCommand() > 2) {
                                this.setCommand(0);
                            }
                        }
                        String commandText = "stand";
                        if (this.getCommand() == 1) {
                            commandText = "sit";
                        } else if (this.getCommand() == 2) {
                            commandText = "escort";
                        }
                        player.displayClientMessage(Component.translatable("dragon.command." + commandText), true);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return super.m_6071_(player, hand);
        }
    }

    public abstract ItemLike getHeartItem();

    public abstract Item getBloodItem();

    public abstract Item getFleshItem();

    public ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    private ItemStack getRandomDrop() {
        ItemStack stack = this.getItemFromLootTable();
        if (stack.getItem() == IafItemRegistry.DRAGON_BONE.get()) {
            this.playSound(SoundEvents.SKELETON_AMBIENT, 1.0F, 1.0F);
        } else {
            this.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
        }
        return stack;
    }

    public boolean canPositionBeSeen(double x, double y, double z) {
        HitResult result = this.m_9236_().m_45547_(new ClipContext(new Vec3(this.m_20185_(), this.m_20186_() + (double) this.m_20192_(), this.m_20189_()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0 || result.getType() == HitResult.Type.MISS;
    }

    public abstract ResourceLocation getDeadLootTable();

    public ItemStack getItemFromLootTable() {
        LootTable loottable = this.m_9236_().getServer().getServerResources().managers().getLootData().m_278676_(this.getDeadLootTable());
        LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, this.m_20182_()).withParameter(LootContextParams.DAMAGE_SOURCE, this.m_9236_().damageSources().generic());
        ObjectListIterator var3 = loottable.getRandomItems(lootparams$builder.create(LootContextParamSets.ENTITY)).iterator();
        return var3.hasNext() ? (ItemStack) var3.next() : ItemStack.EMPTY;
    }

    public void eatFoodBonus(ItemStack stack) {
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    public void growDragon(int ageInDays) {
        if (!this.isAgingDisabled()) {
            this.setAgeInDays(this.getAgeInDays() + ageInDays);
            this.m_20011_(this.m_20191_());
            if (this.m_9236_().isClientSide && this.getAgeInDays() % 25 == 0) {
                for (int i = 0; (float) i < this.getRenderSize() * 4.0F; i++) {
                    float f = (float) ((double) this.m_217043_().nextFloat() * (this.m_20191_().maxX - this.m_20191_().minX) + this.m_20191_().minX);
                    float f1 = (float) ((double) this.m_217043_().nextFloat() * (this.m_20191_().maxY - this.m_20191_().minY) + this.m_20191_().minY);
                    float f2 = (float) ((double) this.m_217043_().nextFloat() * (this.m_20191_().maxZ - this.m_20191_().minZ) + this.m_20191_().minZ);
                    double motionX = this.m_217043_().nextGaussian() * 0.07;
                    double motionY = this.m_217043_().nextGaussian() * 0.07;
                    double motionZ = this.m_217043_().nextGaussian() * 0.07;
                    this.m_9236_().addParticle(ParticleTypes.HAPPY_VILLAGER, (double) f, (double) f1, (double) f2, motionX, motionY, motionZ);
                }
            }
            if (this.getDragonStage() >= 2) {
                this.m_6038_();
            }
            this.updateAttributes();
        }
    }

    public void spawnItemCrackParticles(Item item) {
        for (int i = 0; i < 15; i++) {
            double motionX = this.m_217043_().nextGaussian() * 0.07;
            double motionY = this.m_217043_().nextGaussian() * 0.07;
            double motionZ = this.m_217043_().nextGaussian() * 0.07;
            Vec3 headVec = this.getHeadPosition();
            if (!this.m_9236_().isClientSide) {
                ((ServerLevel) this.m_9236_()).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)), headVec.x, headVec.y, headVec.z, 1, motionX, motionY, motionZ, 0.1);
            } else {
                this.m_9236_().addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(item)), headVec.x, headVec.y, headVec.z, motionX, motionY, motionZ);
            }
        }
    }

    public boolean isTimeToWake() {
        return this.m_9236_().isDay() || this.getCommand() == 2;
    }

    private boolean isStuck() {
        boolean skip = this.isChained() || this.m_21824_();
        if (skip) {
            return false;
        } else {
            boolean checkNavigation = this.ticksStill > 80 && this.canMove() && !this.isHovering();
            if (checkNavigation) {
                PathNavigation navigation = this.m_21573_();
                Path path = navigation.getPath();
                if (!navigation.isDone() && (path == null || path.getEndNode() != null || this.m_20183_().m_123331_(path.getEndNode().asBlockPos()) > 15.0)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected boolean isOverAir() {
        return this.isOverAir;
    }

    private boolean isOverAirLogic() {
        return this.m_9236_().m_46859_(BlockPos.containing((double) this.m_146903_(), this.m_20191_().minY - 1.0, (double) this.m_146907_()));
    }

    public boolean isDiving() {
        return false;
    }

    public boolean isBeyondHeight() {
        return this.m_20186_() > (double) this.m_9236_().m_151558_() ? true : this.m_20186_() > (double) IafConfig.maxDragonFlight;
    }

    private int calculateDownY() {
        if (this.m_21573_().getPath() != null) {
            Path path = this.m_21573_().getPath();
            Vec3 p = path.getEntityPosAtNode(this, Math.min(path.getNodeCount() - 1, path.getNextNodeIndex() + 1));
            if (p.y < this.m_20186_() - 1.0) {
                return -1;
            }
        }
        return 1;
    }

    public void breakBlock(BlockPos position) {
        if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, (double) position.m_123341_(), (double) position.m_123342_(), (double) position.m_123343_()))) {
            BlockState state = this.m_9236_().getBlockState(position);
            float hardness = IafConfig.dragonGriefing != 1 && this.getDragonStage() > 3 ? 5.0F : 2.0F;
            if (this.isBreakable(position, state, hardness, this)) {
                this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                if (!this.m_9236_().isClientSide()) {
                    this.m_9236_().m_46961_(position, !state.m_204336_(IafBlockTags.DRAGON_BLOCK_BREAK_NO_DROPS) && (double) this.f_19796_.nextFloat() <= IafConfig.dragonBlockBreakingDropChance);
                }
            }
        }
    }

    public void breakBlocks(boolean force) {
        boolean doBreak = force;
        if (this.blockBreakCounter > 0 || IafConfig.dragonBreakBlockCooldown == 0) {
            this.blockBreakCounter--;
            if (this.blockBreakCounter == 0 || IafConfig.dragonBreakBlockCooldown == 0) {
                doBreak = true;
            }
        }
        if (doBreak && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) && DragonUtils.canGrief(this) && !this.isModelDead() && this.getDragonStage() >= 3 && (this.canMove() || this.getControllingPassenger() != null)) {
            int bounds = 1;
            int flightModifier = this.isFlying() && this.m_5448_() != null ? -1 : 1;
            int yMinus = this.calculateDownY();
            BlockPos.betweenClosedStream((int) Math.floor(this.m_20191_().minX) - 1, (int) Math.floor(this.m_20191_().minY) + yMinus, (int) Math.floor(this.m_20191_().minZ) - 1, (int) Math.floor(this.m_20191_().maxX) + 1, (int) Math.floor(this.m_20191_().maxY) + 1 + flightModifier, (int) Math.floor(this.m_20191_().maxZ) + 1).forEach(this::breakBlock);
        }
    }

    protected boolean isBreakable(BlockPos pos, BlockState state, float hardness, EntityDragonBase entity) {
        return state.m_280555_() && !state.m_60795_() && state.m_60819_().isEmpty() && !state.m_60808_(this.m_9236_(), pos).isEmpty() && state.m_60800_(this.m_9236_(), pos) >= 0.0F && state.m_60800_(this.m_9236_(), pos) <= hardness && DragonUtils.canDragonBreak(state, entity) && this.canDestroyBlock(pos, state);
    }

    @Override
    public boolean isBlockExplicitlyPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return !this.isModelDead() && this.getDragonStage() >= 3 && DragonUtils.canGrief(this) && (double) pos.m_123342_() >= this.m_20186_() ? this.isBreakable(pos, state, IafConfig.dragonGriefing != 1 && this.getDragonStage() > 3 ? 5.0F : 2.0F, this) : false;
    }

    @Override
    public boolean isBlockExplicitlyNotPassable(BlockState state, BlockPos pos, BlockPos entityPos) {
        return false;
    }

    public void spawnGroundEffects() {
        if (this.m_9236_().isClientSide) {
            for (int i = 0; (float) i < this.getRenderSize(); i++) {
                for (int i1 = 0; i1 < 20; i1++) {
                    float radius = 0.75F * (0.7F * this.getRenderSize() / 3.0F) * -3.0F;
                    float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1 * 1.0F;
                    double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
                    double extraY = 0.8F;
                    double extraZ = (double) (radius * Mth.cos(angle));
                    BlockPos ground = this.getGround(BlockPos.containing(this.m_20185_() + extraX, this.m_20186_() + 0.8F - 1.0, this.m_20189_() + extraZ));
                    BlockState BlockState = this.m_9236_().getBlockState(ground);
                    if (BlockState.m_60795_()) {
                        double motionX = this.m_217043_().nextGaussian() * 0.07;
                        double motionY = this.m_217043_().nextGaussian() * 0.07;
                        double motionZ = this.m_217043_().nextGaussian() * 0.07;
                        this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockState), true, this.m_20185_() + extraX, (double) ground.m_123342_() + 0.8F, this.m_20189_() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    private BlockPos getGround(BlockPos blockPos) {
        while (this.m_9236_().m_46859_(blockPos) && blockPos.m_123342_() > 1) {
            blockPos = blockPos.below();
        }
        return blockPos;
    }

    public boolean isActuallyBreathingFire() {
        return this.fireTicks > 20 && this.isBreathingFire();
    }

    public boolean doesWantToLand() {
        return this.flyTicks > 6000 || this.isGoingDown() || this.flyTicks > 40 && this.flyProgress == 0.0F || this.isChained() && this.flyTicks > 100;
    }

    public abstract String getVariantName(int var1);

    public boolean shouldRiderSit() {
        return this.getControllingPassenger() != null;
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger)) {
            if (this.getControllingPassenger() != null && this.getControllingPassenger().m_20148_().equals(passenger.getUUID())) {
                if (this.isModelDead()) {
                    passenger.stopRiding();
                }
                this.m_146922_(passenger.getYRot());
                this.m_5616_(passenger.getYHeadRot());
                this.m_146926_(passenger.getXRot());
                Vec3 riderPos = this.getRiderPosition();
                passenger.setPos(riderPos.x, riderPos.y + (double) passenger.getBbHeight(), riderPos.z);
            } else {
                this.updatePreyInMouth(passenger);
            }
        }
    }

    private float bob(float speed, float degree, boolean bounce, float f, float f1) {
        double a = (double) (Mth.sin(f * speed) * f1 * degree);
        float bob = (float) (a - (double) (f1 * degree));
        if (bounce) {
            bob = (float) (-Math.abs(a));
        }
        return bob * this.getRenderSize() / 3.0F;
    }

    protected void updatePreyInMouth(Entity prey) {
        if (this.getAnimation() != ANIMATION_SHAKEPREY) {
            this.setAnimation(ANIMATION_SHAKEPREY);
        }
        if (this.getAnimation() == ANIMATION_SHAKEPREY && this.getAnimationTick() > 55 && prey != null) {
            float baseDamage = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue();
            float damage = baseDamage * 2.0F;
            boolean didDamage = prey.hurt(this.m_9236_().damageSources().mobAttack(this), damage);
            if (didDamage && IafConfig.canDragonsHealFromBiting) {
                this.m_5634_(damage * 0.5F);
            }
            if (!(prey instanceof Player)) {
                this.setHunger(this.getHunger() + 1);
            }
            prey.stopRiding();
        } else {
            this.f_20883_ = this.m_146908_();
            float modTick_0 = (float) (this.getAnimationTick() - 25);
            float modTick_1 = this.getAnimationTick() > 25 && this.getAnimationTick() < 55 ? 8.0F * Mth.clamp(Mth.sin((float) (Math.PI + (double) modTick_0 * 0.25)), -0.8F, 0.8F) : 0.0F;
            float modTick_2 = this.getAnimationTick() > 30 ? 10.0F : (float) Math.max(0, this.getAnimationTick() - 20);
            float radius = 0.75F * (0.6F * this.getRenderSize() / 3.0F) * -3.0F;
            float angle = (float) (Math.PI / 180.0) * this.f_20883_ + 3.15F + modTick_1 * 2.0F * 0.015F;
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            double extraY = modTick_2 == 0.0F ? 0.0 : 0.035F * ((double) (this.getRenderSize() / 3.0F) + (double) modTick_2 * 0.5 * (double) (this.getRenderSize() / 3.0F));
            prey.setPos(this.m_20185_() + extraX, this.m_20186_() + extraY, this.m_20189_() + extraZ);
        }
    }

    public int getDragonStage() {
        int age = this.getAgeInDays();
        if (age >= 100) {
            return 5;
        } else if (age >= 75) {
            return 4;
        } else if (age >= 50) {
            return 3;
        } else {
            return age >= 25 ? 2 : 1;
        }
    }

    public boolean isTeen() {
        return this.getDragonStage() < 4 && this.getDragonStage() > 2;
    }

    @Override
    public boolean shouldDropLoot() {
        return this.getDragonStage() >= 4;
    }

    @Override
    public boolean isBaby() {
        return this.getDragonStage() < 2;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setGender(this.m_217043_().nextBoolean());
        int age = this.m_217043_().nextInt(80) + 1;
        this.growDragon(age);
        this.setVariant(new Random().nextInt(4));
        this.setInSittingPose(false);
        double healthStep = (this.maximumHealth - this.minimumHealth) / 125.0;
        this.m_5634_((float) Math.round(this.minimumHealth + healthStep * (double) age));
        this.usingGroundAttack = true;
        this.setHunger(50);
        return spawnDataIn;
    }

    @Override
    public boolean hurt(@NotNull DamageSource dmg, float i) {
        if (this.isModelDead() && dmg != this.m_9236_().damageSources().fellOutOfWorld()) {
            return false;
        } else if (this.m_20160_() && dmg.getEntity() != null && this.getControllingPassenger() != null && dmg.getEntity() == this.getControllingPassenger()) {
            return false;
        } else if ((dmg.type().msgId().contains("arrow") || this.m_20202_() != null && dmg.getEntity() != null && dmg.getEntity().is(this.m_20202_())) && this.m_20159_()) {
            return false;
        } else if (!dmg.is(DamageTypes.IN_WALL) && !dmg.is(DamageTypes.FALLING_BLOCK) && !dmg.is(DamageTypes.CRAMMING)) {
            if (!this.m_9236_().isClientSide && dmg.getEntity() != null && this.m_217043_().nextInt(4) == 0) {
                this.roar();
            }
            if (i > 0.0F && this.isSleeping()) {
                this.setInSittingPose(false);
                if (!this.m_21824_() && dmg.getEntity() instanceof Player) {
                    this.setTarget((Player) dmg.getEntity());
                }
            }
            return super.m_6469_(dmg, i);
        } else {
            return false;
        }
    }

    @Override
    public void refreshDimensions() {
        super.m_6210_();
        float scale = Math.min(this.getRenderSize() * 0.35F, 7.0F);
        if (scale != this.lastScale) {
            this.resetParts(this.getRenderSize() / 3.0F);
        }
        this.lastScale = scale;
    }

    public float getStepHeight() {
        return Math.max(1.2F, 1.2F + (float) (Math.min(this.getAgeInDays(), 125) - 25) * 1.8F / 100.0F);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.refreshDimensions();
        this.updateParts();
        this.prevDragonPitch = this.getDragonPitch();
        this.m_9236_().getProfiler().push("dragonLogic");
        this.m_274367_(this.getStepHeight());
        this.isOverAir = this.isOverAirLogic();
        this.logic.updateDragonCommon();
        if (this.isModelDead()) {
            if (!this.m_9236_().isClientSide && this.m_9236_().m_46859_(BlockPos.containing((double) this.m_146903_(), this.m_20191_().minY, (double) this.m_146907_())) && this.m_20186_() > -1.0) {
                this.move(MoverType.SELF, new Vec3(0.0, -0.2F, 0.0));
            }
            this.setBreathingFire(false);
            float dragonPitch = this.getDragonPitch();
            if (dragonPitch > 0.0F) {
                dragonPitch = Math.min(0.0F, dragonPitch - 5.0F);
                this.setDragonPitch(dragonPitch);
            }
            if (dragonPitch < 0.0F) {
                this.setDragonPitch(Math.max(0.0F, dragonPitch + 5.0F));
            }
        } else if (this.m_9236_().isClientSide) {
            this.logic.updateDragonClient();
        } else {
            this.logic.updateDragonServer();
            this.logic.updateDragonAttack();
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().push("dragonFlight");
        if (this.useFlyingPathFinder() && !this.m_9236_().isClientSide) {
            this.flightManager.update();
        }
        this.m_9236_().getProfiler().pop();
        this.m_9236_().getProfiler().pop();
        if (!this.m_9236_().isClientSide() && IafConfig.dragonDigWhenStuck && this.isStuck()) {
            this.breakBlocks(true);
            this.resetStuck();
        }
    }

    private void resetStuck() {
        this.ticksStill = 0;
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        this.prevModelDeadProgress = this.modelDeadProgress;
        this.prevDiveProgress = this.diveProgress;
        this.prevAnimationProgresses[0] = this.sitProgress;
        this.prevAnimationProgresses[1] = this.sleepProgress;
        this.prevAnimationProgresses[2] = this.hoverProgress;
        this.prevAnimationProgresses[3] = this.flyProgress;
        this.prevAnimationProgresses[4] = this.fireBreathProgress;
        this.prevAnimationProgresses[5] = this.ridingProgress;
        this.prevAnimationProgresses[6] = this.tackleProgress;
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.setTarget(null);
        }
        if (this.isModelDead()) {
            if (this.m_20160_()) {
                this.m_20153_();
            }
            this.setHovering(false);
            this.setFlying(false);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.animationTick > this.getAnimation().getDuration() && !this.m_9236_().isClientSide) {
            this.animationTick = 0;
        }
    }

    @NotNull
    @Override
    public EntityDimensions getDimensions(@NotNull Pose poseIn) {
        return this.m_6095_().getDimensions().scale(this.getScale());
    }

    @Override
    public float getScale() {
        return Math.min(this.getRenderSize() * 0.35F, 7.0F);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public float getRenderSize() {
        int stage = this.getDragonStage() - 1;
        float step = (this.growth_stages[stage][1] - this.growth_stages[stage][0]) / 25.0F;
        return this.getAgeInDays() > 125 ? this.growth_stages[stage][0] + step * 25.0F : this.growth_stages[stage][0] + step * (float) this.getAgeFactor();
    }

    private int getAgeFactor() {
        return this.getDragonStage() > 1 ? this.getAgeInDays() - 25 * (this.getDragonStage() - 1) : this.getAgeInDays();
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        this.m_21563_().setLookAt(entityIn, 30.0F, 30.0F);
        if (!this.isTackling() && !this.isModelDead()) {
            boolean flag = entityIn.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            if (flag) {
                this.m_19970_(this, entityIn);
            }
            return flag;
        } else {
            return false;
        }
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.isAlive()) {
            this.m_8127_();
        } else {
            this.m_20334_(0.0, 0.0, 0.0);
            this.tick();
            if (this.m_20159_()) {
                this.updateRiding(entity);
            }
        }
    }

    public void updateRiding(Entity riding) {
        if (riding != null && riding.hasPassenger(this) && riding instanceof Player) {
            int i = riding.getPassengers().indexOf(this);
            float radius = (i == 2 ? -0.2F : 0.5F) + (float) (((Player) riding).m_21255_() ? 2 : 0);
            float angle = (float) (Math.PI / 180.0) * ((Player) riding).f_20883_ + (float) (i == 1 ? 90 : (i == 0 ? -90 : 0));
            double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
            double extraZ = (double) (radius * Mth.cos(angle));
            double extraY = (riding.isShiftKeyDown() ? 1.2 : 1.4) + (i == 2 ? 0.4 : 0.0);
            this.f_20885_ = ((Player) riding).f_20885_;
            this.m_146922_(((Player) riding).f_20885_);
            this.m_6034_(riding.getX() + extraX, riding.getY() + extraY, riding.getZ() + extraZ);
            if ((this.getControlState() == 16 || ((Player) riding).m_21255_()) && !riding.isPassenger()) {
                this.m_8127_();
                if (this.m_9236_().isClientSide) {
                    IceAndFire.sendMSGToServer(new MessageStartRidingMob(this.m_19879_(), false, true));
                }
            }
        }
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.isModelDead() ? NO_ANIMATION : this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (!this.isModelDead()) {
            this.currentAnimation = animation;
        }
    }

    @Override
    public void playAmbientSound() {
        if (!this.isSleeping() && !this.isModelDead() && !this.m_9236_().isClientSide) {
            if (this.getAnimation() == NO_ANIMATION) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.m_8032_();
        }
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (!this.isModelDead()) {
            if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide) {
                this.setAnimation(ANIMATION_SPEAK);
            }
            super.m_6677_(source);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { IAnimatedEntity.NO_ANIMATION, ANIMATION_EAT };
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal instanceof EntityDragonBase dragon && otherAnimal != this && otherAnimal.getClass() == this.getClass()) {
            return this.isMale() && !dragon.isMale() || !this.isMale() && dragon.isMale();
        }
        return false;
    }

    public EntityDragonEgg createEgg(EntityDragonBase ageable) {
        EntityDragonEgg dragon = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), this.m_9236_());
        dragon.setEggType(EnumDragonEgg.byMetadata(new Random().nextInt(4) + this.getStartMetaForType()));
        dragon.m_6034_((double) Mth.floor(this.m_20185_()) + 0.5, (double) (Mth.floor(this.m_20186_()) + 1), (double) Mth.floor(this.m_20189_()) + 0.5);
        return dragon;
    }

    public int getStartMetaForType() {
        return 0;
    }

    public boolean isTargetBlocked(Vec3 target) {
        if (target != null) {
            BlockHitResult rayTrace = this.m_9236_().m_45547_(new ClipContext(this.m_20182_().add(0.0, (double) this.m_20192_(), 0.0), target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            BlockPos sidePos = rayTrace.getBlockPos();
            return !this.m_9236_().m_46859_(sidePos) ? true : rayTrace.getType() == HitResult.Type.BLOCK;
        } else {
            return false;
        }
    }

    private double getFlySpeed() {
        return (double) ((2 + this.getAgeInDays() / 125 * 2) * (this.isTackling() ? 2 : 1));
    }

    public boolean isTackling() {
        return this.f_19804_.get(TACKLE);
    }

    public void setTackling(boolean tackling) {
        this.f_19804_.set(TACKLE, tackling);
    }

    public boolean isAgingDisabled() {
        return this.f_19804_.get(AGINGDISABLED);
    }

    public void setAgingDisabled(boolean isAgingDisabled) {
        this.f_19804_.set(AGINGDISABLED, isAgingDisabled);
    }

    public boolean isBoundToCrystal() {
        return this.f_19804_.get(CRYSTAL_BOUND);
    }

    public void setCrystalBound(boolean crystalBound) {
        this.f_19804_.set(CRYSTAL_BOUND, crystalBound);
    }

    public float getDistanceSquared(Vec3 Vector3d) {
        float f = (float) (this.m_20185_() - Vector3d.x);
        float f1 = (float) (this.m_20186_() - Vector3d.y);
        float f2 = (float) (this.m_20189_() - Vector3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    public abstract Item getVariantScale(int var1);

    public abstract Item getVariantEgg(int var1);

    public abstract Item getSummoningCrystal();

    @Override
    public boolean isImmobile() {
        return this.m_21223_() <= 0.0F || this.isOrderedToSit() && !this.m_20160_() || this.isModelDead() || this.m_20159_();
    }

    @Override
    public boolean isInWater() {
        return super.m_20069_() && this.m_204036_(FluidTags.WATER) > (double) Mth.floor((float) this.getDragonStage() / 2.0F);
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.getAnimation() == ANIMATION_SHAKEPREY || !this.canMove() && !this.m_20160_() || this.isOrderedToSit()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            pTravelVector = new Vec3(0.0, 0.0, 0.0);
        }
        if (this.allowLocalMotionControl && this.getControllingPassenger() != null) {
            LivingEntity rider = this.getControllingPassenger();
            if (rider == null) {
                super.m_7023_(pTravelVector);
            } else if (this.isHovering() || this.isFlying()) {
                double forward = (double) rider.zza;
                double strafing = (double) rider.xxa;
                double vertical = 0.0;
                float speed = (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
                float airSpeedModifier = (float) (5.2F + 1.0 * Mth.map((double) speed, this.minimumSpeed, this.maximumSpeed, 0.0, 1.5));
                speed *= airSpeedModifier;
                if (forward > 0.0) {
                    this.setFlying(true);
                    this.setHovering(false);
                }
                if (this.isAttacking() && this.m_146909_() > -5.0F && this.m_20184_().length() > 1.0) {
                    this.setTackling(true);
                } else {
                    this.setTackling(false);
                }
                this.gliding = this.allowMousePitchControl && rider.m_20142_();
                if (!this.gliding) {
                    speed += this.glidingSpeedBonus;
                    forward *= rider.zza > 0.0F ? 1.0 : 0.5;
                    strafing *= 0.4F;
                    if (this.isGoingUp() && !this.isGoingDown()) {
                        vertical = 1.0;
                    } else if (this.isGoingDown() && !this.isGoingUp()) {
                        vertical = -1.0;
                    } else if (this.m_6109_()) {
                    }
                } else {
                    speed *= 1.5F;
                    strafing *= 0.1F;
                    this.glidingSpeedBonus = (float) Mth.clamp((double) this.glidingSpeedBonus + this.m_20184_().y * -0.05, -0.8, 1.5);
                    speed += this.glidingSpeedBonus;
                    forward = (double) Mth.abs(Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0)));
                    vertical = (double) Mth.abs(Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0)));
                    if (this.isGoingUp() && !this.isGoingDown()) {
                        vertical = Math.max(vertical, 0.5);
                    } else if (this.isGoingDown() && !this.isGoingUp()) {
                        vertical = Math.min(vertical, -0.5);
                    } else if (this.isGoingUp() && this.isGoingDown()) {
                        vertical = 0.0;
                    } else if (this.m_146909_() < 0.0F) {
                        vertical *= 1.0;
                    } else if (this.m_146909_() > 0.0F) {
                        vertical *= -1.0;
                    } else if (this.m_6109_()) {
                    }
                }
                this.glidingSpeedBonus = this.glidingSpeedBonus - (float) ((double) this.glidingSpeedBonus * 0.01);
                if (this.m_6109_()) {
                    float flyingSpeed = speed * 0.1F;
                    this.m_7910_(flyingSpeed);
                    this.m_19920_(flyingSpeed, new Vec3(strafing, vertical, forward));
                    this.move(MoverType.SELF, this.m_20184_());
                    this.m_20256_(this.m_20184_().multiply(new Vec3(0.9, 0.9, 0.9)));
                    Vec3 currentMotion = this.m_20184_();
                    if (this.f_19862_) {
                        currentMotion = new Vec3(currentMotion.x, 0.1, currentMotion.z);
                    }
                    this.m_20256_(currentMotion);
                    this.m_267651_(false);
                } else {
                    this.m_20256_(Vec3.ZERO);
                }
                this.m_146872_();
                this.updatePitch(this.f_19791_ - this.m_20186_());
            } else if (!this.isInWater() && !this.m_20077_()) {
                double forwardx = (double) rider.zza;
                double strafingx = (double) (rider.xxa * 0.5F);
                double verticalx = pTravelVector.y;
                float speedx = (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
                float groundSpeedModifier = (float) (1.8F * this.getFlightSpeedModifier());
                speedx *= groundSpeedModifier;
                forwardx *= (double) speedx;
                forwardx *= rider.m_20142_() ? 1.2F : 1.0;
                forwardx *= rider.zza > 0.0F ? 1.0 : 0.2F;
                if (this.m_6109_()) {
                    this.m_7910_(speedx);
                    super.m_7023_(new Vec3(strafingx, verticalx, forwardx));
                } else {
                    this.m_20256_(Vec3.ZERO);
                }
                this.m_146872_();
                this.updatePitch(this.f_19791_ - this.m_20186_());
            } else {
                double forwardx = (double) rider.zza;
                double strafingx = (double) rider.xxa;
                double verticalx = 0.0;
                float speedx = (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
                if (this.isGoingUp() && !this.isGoingDown()) {
                    verticalx = 0.5;
                } else if (this.isGoingDown() && !this.isGoingUp()) {
                    verticalx = -0.5;
                }
                this.m_7910_(speedx);
                this.m_21564_((float) forwardx);
                super.m_7023_(pTravelVector.add(strafingx, verticalx, forwardx));
            }
        } else {
            super.m_7023_(pTravelVector);
        }
    }

    protected void updatePitch(double verticalDelta) {
        if (this.isOverAir() && !this.m_20159_()) {
            if (!this.isHovering()) {
                this.incrementDragonPitch((float) verticalDelta * 10.0F);
            }
            this.setDragonPitch(Mth.clamp(this.getDragonPitch(), -60.0F, 40.0F));
            float plateau = 2.0F;
            float planeDist = (float) ((Math.abs(this.m_20184_().x) + Math.abs(this.m_20184_().z)) * 6.0);
            if (this.getDragonPitch() > 2.0F) {
                this.decrementDragonPitch(planeDist * Math.abs(this.getDragonPitch()) / 90.0F);
            }
            if (this.getDragonPitch() < -2.0F) {
                this.incrementDragonPitch(planeDist * Math.abs(this.getDragonPitch()) / 90.0F);
            }
            if (this.getDragonPitch() > 2.0F) {
                this.decrementDragonPitch(1.0F);
            } else if (this.getDragonPitch() < -2.0F) {
                this.incrementDragonPitch(1.0F);
            }
            if (this.getControllingPassenger() == null && this.getDragonPitch() < -45.0F && planeDist < 3.0F && this.isFlying() && !this.isHovering()) {
                this.setHovering(true);
            }
        } else if (Mth.abs(this.getDragonPitch()) < 1.0F) {
            this.setDragonPitch(0.0F);
        } else {
            this.setDragonPitch(this.getDragonPitch() / 1.5F);
        }
    }

    public void updateRider() {
        Entity controllingPassenger = this.getControllingPassenger();
        if (controllingPassenger instanceof Player rider) {
            this.ticksStill = 0;
            this.hoverTicks = 0;
            this.flyTicks = 0;
            if (this.isGoingUp()) {
                if (!this.isFlying() && !this.isHovering()) {
                    this.spacebarTicks += 2;
                }
            } else if (this.isDismounting() && (this.isFlying() || this.isHovering())) {
                this.setCommand(2);
            }
            if (this.spacebarTicks > 0) {
                this.spacebarTicks--;
            }
            if (this.spacebarTicks > 20 && this.m_269323_() != null && this.m_20197_().contains(this.m_269323_()) && !this.isFlying() && !this.isHovering() && !this.isInWater()) {
                this.setHovering(true);
                this.spacebarTicks = 0;
                this.glidingSpeedBonus = 0.0F;
            }
            if (this.isFlying() || this.isHovering()) {
                if (rider.f_20902_ > 0.0F) {
                    this.setFlying(true);
                    this.setHovering(false);
                } else {
                    this.setFlying(false);
                    this.setHovering(true);
                }
                if (!this.isOverAir() && this.isFlying() && rider.m_146909_() > 10.0F && !this.isInWater()) {
                    this.setHovering(false);
                    this.setFlying(false);
                }
                if (!this.isOverAir() && this.isGoingDown() && !this.isInWater()) {
                    this.setFlying(false);
                    this.setHovering(false);
                }
            }
            if (this.isTackling()) {
                this.tacklingTicks++;
                if (this.tacklingTicks == 40) {
                    this.tacklingTicks = 0;
                }
                if (!this.isFlying() && this.m_20096_()) {
                    this.tacklingTicks = 0;
                    this.setTackling(false);
                }
                List<Entity> victims = this.m_9236_().getEntities(this, this.m_20191_().expandTowards(2.0, 2.0, 2.0), potentialVictim -> potentialVictim != rider && potentialVictim instanceof LivingEntity);
                victims.forEach(victim -> this.logic.attackTarget(victim, rider, (float) (this.getDragonStage() * 3)));
            }
            if (this.isStriking() && this.getControllingPassenger() != null && this.getDragonStage() > 1) {
                this.setBreathingFire(true);
                this.riderShootFire(this.getControllingPassenger());
                this.fireStopTicks = 10;
            }
            if (this.isAttacking() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
                LivingEntity target = DragonUtils.riderLookingAtEntity(this, this.getControllingPassenger(), (double) this.getDragonStage() + (this.m_20191_().maxX - this.m_20191_().minX));
                if (this.getAnimation() != ANIMATION_BITE) {
                    this.setAnimation(ANIMATION_BITE);
                }
                if (target != null && !DragonUtils.hasSameOwner(this, target)) {
                    int damage = (int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue();
                    boolean didDamage = this.logic.attackTarget(target, rider, (float) damage);
                    if (didDamage && IafConfig.canDragonsHealFromBiting) {
                        this.m_5634_((float) damage * 0.1F);
                    }
                }
            }
            if (this.getControllingPassenger() != null && this.getControllingPassenger().m_6144_()) {
                EntityDataProvider.getCapability(this.getControllingPassenger()).ifPresent(data -> data.miscData.setDismounted(true));
                this.getControllingPassenger().stopRiding();
            }
            if (this.m_5448_() != null && !this.m_20197_().isEmpty() && this.m_269323_() != null && this.m_20197_().contains(this.m_269323_())) {
                this.setTarget(null);
            }
            if (this.m_146900_().m_60819_().isSource() && this.isInWater() && !this.isGoingUp()) {
                this.setFlying(false);
                this.setHovering(false);
            }
        } else if (controllingPassenger instanceof EntityDreadQueen) {
            Player ridingPlayer = this.getRidingPlayer();
            if (ridingPlayer != null) {
                if (this.isGoingUp()) {
                    if (!this.isFlying() && !this.isHovering()) {
                        this.spacebarTicks += 2;
                    }
                } else if (this.isDismounting() && (this.isFlying() || this.isHovering())) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
                    this.setFlying(false);
                    this.setHovering(false);
                }
            }
            if (!this.isDismounting() && (this.isFlying() || this.isHovering())) {
                this.m_20256_(this.m_20184_().add(0.0, 0.01, 0.0));
            }
            if (this.isStriking() && this.getControllingPassenger() != null && this.getDragonStage() > 1) {
                this.setBreathingFire(true);
                this.riderShootFire(this.getControllingPassenger());
                this.fireStopTicks = 10;
            }
            if (this.isAttacking() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
                LivingEntity targetx = DragonUtils.riderLookingAtEntity(this, this.getControllingPassenger(), (double) this.getDragonStage() + (this.m_20191_().maxX - this.m_20191_().minX));
                if (this.getAnimation() != ANIMATION_BITE) {
                    this.setAnimation(ANIMATION_BITE);
                }
                if (targetx != null && !DragonUtils.hasSameOwner(this, targetx)) {
                    this.logic.attackTarget(targetx, ridingPlayer, (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                }
            }
            if (this.getControllingPassenger() != null && this.getControllingPassenger().m_6144_()) {
                EntityDataProvider.getCapability(this.getControllingPassenger()).ifPresent(data -> data.miscData.setDismounted(true));
                this.getControllingPassenger().stopRiding();
            }
            if (this.isFlying()) {
                if (!this.isHovering() && this.getControllingPassenger() != null && !this.m_20096_() && Math.max(Math.abs(this.m_20184_().x()), Math.abs(this.m_20184_().z())) < 0.1F) {
                    this.setHovering(true);
                    this.setFlying(false);
                }
            } else if (this.isHovering() && this.getControllingPassenger() != null && !this.m_20096_() && Math.max(Math.abs(this.m_20184_().x()), Math.abs(this.m_20184_().z())) > 0.1F) {
                this.setFlying(true);
                this.usingGroundAttack = false;
                this.setHovering(false);
            }
            if (this.spacebarTicks > 0) {
                this.spacebarTicks--;
            }
            if (this.spacebarTicks > 20 && this.m_269323_() != null && this.m_20197_().contains(this.m_269323_()) && !this.isFlying() && !this.isHovering()) {
                this.setHovering(true);
            }
            if (this.m_20160_() && !this.isOverAir() && this.isFlying() && !this.isHovering() && this.flyTicks > 40) {
                this.setFlying(false);
            }
        }
    }

    @Override
    public void move(@NotNull MoverType pType, @NotNull Vec3 pPos) {
        if (this.isOrderedToSit() && !this.m_20160_()) {
            pPos = new Vec3(0.0, pPos.y(), 0.0);
        }
        if (this.m_20160_()) {
            if (this.m_6109_()) {
                if (this.f_19862_) {
                    this.m_20256_(this.m_20184_().multiply(0.6F, 1.0, 0.6F));
                }
                super.m_6478_(pType, pPos);
            } else {
                super.m_6478_(pType, pPos);
            }
            this.m_20242_(this.isHovering() || this.isFlying());
        } else {
            this.m_20242_(false);
            super.m_6478_(pType, pPos);
        }
    }

    public void updateCheckPlayer() {
        double checkLength = this.m_20191_().getSize() * 3.0;
        Player player = this.m_9236_().m_45930_(this, checkLength);
        if (this.isSleeping() && player != null && !this.m_21830_(player) && !player.isCreative()) {
            this.setInSittingPose(false);
            this.setOrderedToSit(false);
            this.setTarget(player);
        }
    }

    public boolean isDirectPathBetweenPoints(Vec3 vec1, Vec3 vec2) {
        BlockHitResult rayTrace = this.m_9236_().m_45547_(new ClipContext(vec1, new Vec3(vec2.x, vec2.y + (double) this.m_20206_() * 0.5, vec2.z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return rayTrace.getType() != HitResult.Type.BLOCK;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        super.die(cause);
        this.setHunger(this.getHunger() + FoodUtils.getFoodPoints(this));
    }

    @Override
    public void onHearFlute(Player player) {
        if (this.m_21824_() && this.m_21830_(player) && (this.isFlying() || this.isHovering())) {
            this.setFlying(false);
            this.setHovering(false);
        }
    }

    public abstract SoundEvent getRoarSound();

    public void roar() {
        if (!EntityGorgon.isStoneMob(this) && !this.isModelDead()) {
            if (this.f_19796_.nextBoolean()) {
                if (this.getAnimation() != ANIMATION_EPIC_ROAR) {
                    this.setAnimation(ANIMATION_EPIC_ROAR);
                    this.playSound(this.getRoarSound(), this.m_6121_() + 3.0F + (float) Math.max(0, this.getDragonStage() - 2), this.m_6100_() * 0.7F);
                }
                if (this.getDragonStage() > 3) {
                    int size = (this.getDragonStage() - 3) * 30;
                    for (Entity entity : this.m_9236_().m_45933_(this, this.m_20191_().expandTowards((double) size, (double) size, (double) size))) {
                        boolean isStrongerDragon = entity instanceof EntityDragonBase && ((EntityDragonBase) entity).getDragonStage() >= this.getDragonStage();
                        if (entity instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity) entity;
                            if (!isStrongerDragon) {
                                if (!this.m_21830_(living) && !this.isOwnersPet(living)) {
                                    if (living.getItemBySlot(EquipmentSlot.HEAD).getItem() != IafItemRegistry.EARPLUGS.get()) {
                                        living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 50 * size));
                                    }
                                } else {
                                    living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 50 * size));
                                }
                            }
                        }
                    }
                }
            } else {
                if (this.getAnimation() != ANIMATION_ROAR) {
                    this.setAnimation(ANIMATION_ROAR);
                    this.playSound(this.getRoarSound(), this.m_6121_() + 2.0F + (float) Math.max(0, this.getDragonStage() - 3), this.m_6100_());
                }
                if (this.getDragonStage() > 3) {
                    int size = (this.getDragonStage() - 3) * 30;
                    for (Entity entityx : this.m_9236_().m_45933_(this, this.m_20191_().expandTowards((double) size, (double) size, (double) size))) {
                        boolean isStrongerDragon = entityx instanceof EntityDragonBase && ((EntityDragonBase) entityx).getDragonStage() >= this.getDragonStage();
                        if (entityx instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity) entityx;
                            if (!isStrongerDragon) {
                                if (!this.m_21830_(living) && !this.isOwnersPet(living)) {
                                    living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 30 * size));
                                } else {
                                    living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30 * size));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isOwnersPet(LivingEntity living) {
        return this.m_21824_() && this.m_269323_() != null && living instanceof TamableAnimal && ((TamableAnimal) living).m_269323_() != null && this.m_269323_().m_7306_(((TamableAnimal) living).m_269323_());
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        HitResult movingobjectposition = this.m_9236_().m_45547_(new ClipContext(vec1, vec2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return movingobjectposition.getType() != HitResult.Type.BLOCK;
    }

    public boolean shouldRenderEyes() {
        return !this.isSleeping() && !this.isModelDead() && !this.isBlinking() && !EntityGorgon.isStoneMob(this);
    }

    @Override
    public boolean shouldAnimalsFear(Entity entity) {
        return DragonUtils.canTameDragonAttack(this, entity);
    }

    @Override
    public void dropArmor() {
    }

    public boolean isChained() {
        AtomicBoolean isChained = new AtomicBoolean(false);
        EntityDataProvider.getCapability(this).ifPresent(data -> isChained.set(data.chainData.getChainedTo().isEmpty()));
        return isChained.get();
    }

    @Override
    protected void dropFromLootTable(@NotNull DamageSource damageSourceIn, boolean attackedRecently) {
    }

    public HitResult rayTraceRider(Entity rider, double blockReachDistance, float partialTicks) {
        Vec3 Vector3d = rider.getEyePosition(partialTicks);
        Vec3 Vector3d1 = rider.getViewVector(partialTicks);
        Vec3 Vector3d2 = Vector3d.add(Vector3d1.x * blockReachDistance, Vector3d1.y * blockReachDistance, Vector3d1.z * blockReachDistance);
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, Vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
    }

    protected float getRideHeightBase() {
        return 0.00223789F * Mth.square(this.getRenderSize()) + 0.23313718F * this.getRenderSize() - 1.7179043F;
    }

    protected float getRideHorizontalBase() {
        return 0.00336283F * Mth.square(this.getRenderSize()) + 0.19342425F * this.getRenderSize() - 0.026221339F;
    }

    public Vec3 getRiderPosition() {
        float extraXZ = 0.0F;
        float extraY = 0.0F;
        float pitchXZ = 0.0F;
        float pitchY = 0.0F;
        float dragonPitch = this.getDragonPitch();
        if (dragonPitch > 0.0F) {
            pitchXZ = Math.min(dragonPitch / 90.0F, 0.2F);
            pitchY = -(dragonPitch / 90.0F) * 0.6F;
        } else if (dragonPitch < 0.0F) {
            pitchXZ = Math.max(dragonPitch / 90.0F, -0.5F);
            pitchY = dragonPitch / 90.0F * 0.03F;
        }
        extraXZ += pitchXZ * this.getRenderSize();
        extraY += pitchY * this.getRenderSize();
        float linearFactor = Mth.map((float) Math.max(this.getAgeInDays() - 50, 0), 0.0F, 75.0F, 0.0F, 1.0F);
        LivingEntity rider = this.getControllingPassenger();
        if (rider != null && rider.m_146909_() < 0.0F) {
            extraY += (float) Mth.map((double) rider.m_146909_(), 60.0, -40.0, -0.1, 0.1);
        }
        if (!this.isHovering() && !this.isFlying()) {
            if (rider != null && rider.zza > 0.0F) {
                float MAX_RAISE_HEIGHT = 1.1F * linearFactor + this.getRideHeightBase() * 0.1F;
                this.riderWalkingExtraY = Math.min(MAX_RAISE_HEIGHT, this.riderWalkingExtraY + 0.1F);
            } else {
                this.riderWalkingExtraY = Math.max(0.0F, this.riderWalkingExtraY - 0.15F);
            }
            extraY += this.riderWalkingExtraY;
        } else {
            extraY += 1.1F * linearFactor;
            extraY += this.getRideHeightBase() * 0.6F;
        }
        float xzMod = this.getRideHorizontalBase() + extraXZ;
        float yMod = this.getRideHeightBase() + extraY;
        float headPosX = (float) (this.m_20185_() + (double) (xzMod * Mth.cos((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
        float headPosY = (float) (this.m_20186_() + (double) yMod);
        float headPosZ = (float) (this.m_20189_() + (double) (xzMod * Mth.sin((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
        return new Vec3((double) headPosX, (double) headPosY, (double) headPosZ);
    }

    @NotNull
    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        return passenger.isInWall() ? this.m_20182_().add(0.0, 1.0, 0.0) : this.getRiderPosition().add(0.0, (double) passenger.m_20206_(), 0.0);
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.setDeathStage(this.getAgeInDays() / 5);
        this.setModelDead(false);
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (this.isModelDead()) {
            return true;
        } else {
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

    public Vec3 getHeadPosition() {
        float sitProg = this.sitProgress * 0.015F;
        float deadProg = this.modelDeadProgress * -0.02F;
        float hoverProg = this.hoverProgress * 0.03F;
        float flyProg = this.flyProgress * 0.01F;
        int tick;
        if (this.getAnimationTick() < 10) {
            tick = this.getAnimationTick();
        } else if (this.getAnimationTick() > 50) {
            tick = 60 - this.getAnimationTick();
        } else {
            tick = 10;
        }
        float epicRoarProg = this.getAnimation() == ANIMATION_EPIC_ROAR ? (float) tick * 0.1F : 0.0F;
        float sleepProg = this.sleepProgress * -0.025F;
        float pitchMulti = 0.0F;
        float pitchAdjustment = 0.0F;
        float pitchMinus = 0.0F;
        float dragonPitch = -this.getDragonPitch();
        if (this.isFlying() || this.isHovering()) {
            pitchMulti = Mth.sin((float) Math.toRadians((double) dragonPitch));
            pitchAdjustment = 1.2F;
            pitchMulti *= 2.1F * Math.abs(dragonPitch) / 90.0F;
            if (pitchMulti > 0.0F) {
                pitchMulti *= 1.5F - pitchMulti * 0.5F;
            }
            if (pitchMulti < 0.0F) {
                pitchMulti *= 1.3F - pitchMulti * 0.1F;
            }
            pitchMinus = 0.3F * Math.abs(dragonPitch / 90.0F);
            if (dragonPitch >= 0.0F) {
                pitchAdjustment = 0.6F * Math.abs(dragonPitch / 90.0F);
                pitchMinus = 0.95F * Math.abs(dragonPitch / 90.0F);
            }
        }
        float flightXz = 1.0F + flyProg + hoverProg;
        float xzMod = 1.7F * this.getRenderSize() * 0.3F * flightXz + this.getRenderSize() * (0.3F * Mth.sin((float) ((double) (dragonPitch + 90.0F) * Math.PI / 180.0)) * pitchAdjustment - pitchMinus - hoverProg * 0.45F);
        float headPosX = (float) (this.m_20185_() + (double) (xzMod * Mth.cos((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
        float headPosY = (float) (this.m_20186_() + (double) ((0.7F + sitProg + hoverProg + deadProg + epicRoarProg + sleepProg + flyProg + pitchMulti) * this.getRenderSize() * 0.3F));
        float headPosZ = (float) (this.m_20189_() + (double) (xzMod * Mth.sin((float) ((double) (this.m_146908_() + 90.0F) * Math.PI / 180.0))));
        return new Vec3((double) headPosX, (double) headPosY, (double) headPosZ);
    }

    public abstract void stimulateFire(double var1, double var3, double var5, int var7);

    public void randomizeAttacks() {
        this.airAttack = IafDragonAttacks.Air.values()[this.m_217043_().nextInt(IafDragonAttacks.Air.values().length)];
        this.groundAttack = IafDragonAttacks.Ground.values()[this.m_217043_().nextInt(IafDragonAttacks.Ground.values().length)];
    }

    @Override
    public boolean shouldBlockExplode(@NotNull Explosion explosionIn, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, BlockState blockStateIn, float explosionPower) {
        return !(blockStateIn.m_60734_() instanceof IDragonProof) && DragonUtils.canDragonBreak(blockStateIn, this);
    }

    public void tryScorchTarget() {
        LivingEntity entity = this.m_5448_();
        if (entity != null) {
            float distX = (float) (entity.m_20185_() - this.m_20185_());
            float distZ = (float) (entity.m_20189_() - this.m_20189_());
            if (this.isBreathingFire()) {
                if (this.isActuallyBreathingFire()) {
                    this.m_146922_(this.f_20883_);
                    if (this.f_19797_ % 5 == 0) {
                        this.playSound(IafSoundRegistry.FIREDRAGON_BREATH, 4.0F, 1.0F);
                    }
                    this.stimulateFire(this.m_20185_() + (double) (distX * (float) this.fireTicks / 40.0F), entity.m_20186_(), this.m_20189_() + (double) (distZ * (float) this.fireTicks / 40.0F), 1);
                }
            } else {
                this.setBreathingFire(true);
            }
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity LivingEntityIn) {
        super.m_6710_(LivingEntityIn);
        this.flightManager.onSetAttackTarget(LivingEntityIn);
    }

    @Override
    public boolean wantsToAttack(@NotNull LivingEntity target, @NotNull LivingEntity owner) {
        if (this.m_21824_() && target instanceof TamableAnimal tamableTarget) {
            UUID targetOwner = tamableTarget.getOwnerUUID();
            if (targetOwner != null && targetOwner.equals(this.m_21805_())) {
                return false;
            }
        }
        return super.wantsToAttack(target, owner);
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return super.canAttack(target) && DragonUtils.isAlive(target);
    }

    public boolean isPart(Entity entityHit) {
        return this.headPart != null && this.headPart.m_7306_(entityHit) || this.neckPart != null && this.neckPart.m_7306_(entityHit) || this.leftWingLowerPart != null && this.leftWingLowerPart.m_7306_(entityHit) || this.rightWingLowerPart != null && this.rightWingLowerPart.m_7306_(entityHit) || this.leftWingUpperPart != null && this.leftWingUpperPart.m_7306_(entityHit) || this.rightWingUpperPart != null && this.rightWingUpperPart.m_7306_(entityHit) || this.tail1Part != null && this.tail1Part.m_7306_(entityHit) || this.tail2Part != null && this.tail2Part.m_7306_(entityHit) || this.tail3Part != null && this.tail3Part.m_7306_(entityHit) || this.tail4Part != null && this.tail4Part.m_7306_(entityHit);
    }

    @Override
    public double getFlightSpeedModifier() {
        return IafConfig.dragonFlightSpeedMod;
    }

    public boolean isAllowedToTriggerFlight() {
        return (this.hasFlightClearance() && this.m_20096_() || this.isInWater()) && !this.isOrderedToSit() && this.m_20197_().isEmpty() && !this.isBaby() && !this.isSleeping() && this.canMove();
    }

    public BlockPos getEscortPosition() {
        return this.m_269323_() != null ? new BlockPos(this.m_269323_().m_20183_()) : this.m_20183_();
    }

    public boolean shouldTPtoOwner() {
        return this.m_269323_() != null && this.m_20270_(this.m_269323_()) > 10.0F;
    }

    public boolean isSkeletal() {
        return this.getDeathStage() >= this.getAgeInDays() / 5 / 2;
    }

    @Override
    public boolean save(@NotNull CompoundTag compound) {
        return this.m_20086_(compound);
    }

    @Override
    public void playSound(@NotNull SoundEvent soundIn, float volume, float pitch) {
        if (soundIn != SoundEvents.GENERIC_EAT && soundIn != this.m_7515_() && soundIn != this.m_7975_(this.m_9236_().damageSources().generic()) && soundIn != this.m_5592_() && soundIn != this.getRoarSound()) {
            super.m_5496_(soundIn, volume, pitch);
        } else if (!this.m_20067_() && this.headPart != null) {
            this.m_9236_().playSound(null, this.headPart.m_20185_(), this.headPart.m_20186_(), this.headPart.m_20189_(), soundIn, this.getSoundSource(), volume, pitch);
        }
    }

    @NotNull
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean hasFlightClearance() {
        BlockPos topOfBB = BlockPos.containing((double) this.m_146903_(), this.m_20191_().maxY, (double) this.m_146907_());
        for (int i = 1; i < 4; i++) {
            if (!this.m_9236_().m_46859_(topOfBB.above(i))) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return switch(slotIn) {
            case OFFHAND ->
                this.dragonInventory.getItem(0);
            case HEAD ->
                this.dragonInventory.getItem(1);
            case CHEST ->
                this.dragonInventory.getItem(2);
            case LEGS ->
                this.dragonInventory.getItem(3);
            case FEET ->
                this.dragonInventory.getItem(4);
            default ->
                super.m_6844_(slotIn);
        };
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, @NotNull ItemStack stack) {
        switch(slotIn) {
            case OFFHAND:
                this.dragonInventory.setItem(0, stack);
                break;
            case HEAD:
                this.dragonInventory.setItem(1, stack);
                break;
            case CHEST:
                this.dragonInventory.setItem(2, stack);
                break;
            case LEGS:
                this.dragonInventory.setItem(3, stack);
                break;
            case FEET:
                this.dragonInventory.setItem(4, stack);
                break;
            default:
                super.m_6844_(slotIn);
        }
    }

    public SoundEvent getBabyFireSound() {
        return SoundEvents.FIRE_EXTINGUISH;
    }

    protected boolean isPlayingAttackAnimation() {
        return this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_SHAKEPREY || this.getAnimation() == ANIMATION_WINGBLAST || this.getAnimation() == ANIMATION_TAILWHACK;
    }

    protected IafDragonLogic createDragonLogic() {
        return new IafDragonLogic(this);
    }

    protected int getFlightChancePerTick() {
        return 1500;
    }

    public void onRemovedFromWorld() {
        if (IafConfig.chunkLoadSummonCrystal && this.isBoundToCrystal()) {
            DragonPosWorldData data = DragonPosWorldData.get(this.m_9236_());
            if (data != null) {
                data.addDragon(this.m_20148_(), this.m_20183_());
            }
        }
        super.onRemovedFromWorld();
    }

    @Override
    public int maxSearchNodes() {
        return (int) this.m_21051_(Attributes.FOLLOW_RANGE).getValue();
    }

    @Override
    public boolean isSmallerThanBlock() {
        return false;
    }

    @Override
    public float getXZNavSize() {
        return Math.max(1.4F, this.m_20205_() / 2.0F);
    }

    @Override
    public int getYNavSize() {
        return Mth.ceil(this.m_20206_());
    }

    @Override
    public void containerChanged(@NotNull Container invBasic) {
        if (!this.m_9236_().isClientSide) {
            this.updateAttributes();
        }
    }

    @NotNull
    @Override
    public Vec3 handleRelativeFrictionAndCalculateMovement(@NotNull Vec3 pDeltaMovement, float pFriction) {
        return this.f_21342_ instanceof IafDragonFlightManager.PlayerFlightMoveHelper ? pDeltaMovement : super.m_21074_(pDeltaMovement, pFriction);
    }
}