package com.mna.entities.constructs.animated;

import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.FluidParameterRegistry;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructFleeTarget;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.events.construct.ConstructSprayEffectEvent;
import com.mna.api.events.construct.ConstructSprayTargetingEvent;
import com.mna.api.items.DynamicItemFilter;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.ai.LerpLeap;
import com.mna.entities.attributes.AttributeInit;
import com.mna.entities.constructs.ai.ConstructCollectItems;
import com.mna.entities.constructs.ai.ConstructCommandFollowAndGuard;
import com.mna.entities.constructs.ai.ConstructCommandFollowLodestar;
import com.mna.entities.constructs.ai.ConstructCommandReturnToTable;
import com.mna.entities.constructs.ai.ConstructDuel;
import com.mna.entities.constructs.ai.ConstructMove;
import com.mna.entities.constructs.ai.base.ConstructDoorInteractGoal;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.entities.constructs.movement.ConstructMoveControl;
import com.mna.entities.constructs.movement.ConstructPathNavigator;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.items.ItemInit;
import com.mna.items.constructs.parts.arms.ConstructPartAxeArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartAxeArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartBladeArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartBladeArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartFluidNozzleLeft;
import com.mna.items.constructs.parts.arms.ConstructPartFluidNozzleRight;
import com.mna.items.constructs.parts.arms.ConstructPartHammerArmLeft;
import com.mna.items.constructs.parts.arms.ConstructPartHammerArmRight;
import com.mna.items.constructs.parts.arms.ConstructPartManaCannonLeft;
import com.mna.items.constructs.parts.arms.ConstructPartManaCannonRight;
import com.mna.items.constructs.parts.head.ConstructPartHornHead;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.particles.types.movers.ParticleVelocityMover;
import com.mna.spells.SpellCaster;
import com.mna.spells.components.ComponentFling;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.EntityUtil;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.SummonUtils;
import com.mna.tools.math.MathUtils;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.EasingType;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Construct extends AbstractGolem implements IAnimPacketSync<Construct>, GeoEntity, AnimationController.CustomKeyframeHandler<Construct>, PlayerRideableJumping, IFluidHandler, IConstruct<Construct>, IItemHandlerModifiable {

    private static final String NBT_OWNER = "owner";

    private static final float PART_DROP_CHANCE = 0.85F;

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> MANA = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> MAX_MANA = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> BUOYANCY = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> STEERABLE = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<String> FLUIDTYPE = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> FLUIDAMT = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> FLUID_CAP = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> AWAY_TYPE = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<BlockPos> FISHING_POS = SynchedEntityData.defineId(Construct.class, EntityDataSerializers.BLOCK_POS);

    private static final int COOLDOWN_HAMMER_LEAP = 0;

    private static final int COOLDOWN_AXE_LEAP = 0;

    private static final int COOLDOWN_DUAL_SHOT = 0;

    public static final int AWAY_TYPE_NONE = 0;

    public static final int AWAY_TYPE_MINING = 1;

    public static final int AWAY_TYPE_ADVENTURING = 2;

    public static final int AWAY_TYPE_HUNTING = 3;

    public static final float REQUIRED_INTELLIGENCE_FOR_PLAYER_LOOT = 36.0F;

    public static final float REQUIRED_PERCEPTION_FOR_PLAYER_LOOT = 28.0F;

    IConstructConstruction constructCapabilities;

    ConstructDiagnostics constructDiagnostics;

    private boolean needsSync = true;

    private boolean requestingDiagnostics = false;

    private Player cached_owner;

    protected AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    protected boolean constructJumping;

    private ConstructMoodlets moodlets;

    private NonNullList<ItemStack> inventory;

    private String cached_team_name;

    private final Map<Integer, Integer> cooldowns = Maps.newHashMap();

    private ConstructAITask<?> current;

    private static final float MANA_PER_RANGED_ATTACK = 50.0F;

    private static final int FLUID_PER_RANGED_ATTACK = 20;

    private static final float FLUID_SPRAY_RANGE = 10.0F;

    private int chargeCounter = 0;

    private int attackDelay = 0;

    protected float jumpPower;

    private int hornDelay = 0;

    private double lastYd;

    private Fluid __cachedFluid = Fluids.EMPTY;

    private boolean clearAnimCache;

    public final float yaw_rnd;

    public final float pitch_rnd;

    public final float roll_rnd;

    private boolean dancing = false;

    private int danceTicks = 0;

    private int danceIndex = 0;

    private int defeatedCounter = 0;

    private ISpellDefinition currentSpell;

    private boolean ANIM_PACKET = false;

    private int actionFlags = 0;

    private boolean adventure_leave_pfx = false;

    private boolean adventure_return_pfx = false;

    private boolean mine_leave_pfx = false;

    private boolean mine_return_pfx = false;

    private int pfxCounter = 0;

    private boolean redirectingDamage = false;

    public Construct(EntityType<? extends AbstractGolem> type, Level worldIn) {
        super(type, worldIn);
        this.constructCapabilities = new ConstructConstruction();
        this.constructDiagnostics = new ConstructDiagnostics();
        this.moodlets = new ConstructMoodlets();
        this.inventory = NonNullList.createWithCapacity(0);
        this.f_21342_ = new ConstructMoveControl(this, 85, 10, 1.0F, 1.0F, true);
        this.yaw_rnd = worldIn.random.nextFloat() * 360.0F;
        this.pitch_rnd = -2.0F + worldIn.random.nextFloat() * 4.0F;
        this.roll_rnd = -2.0F + worldIn.random.nextFloat() * 4.0F;
    }

    public Construct(Level worldIn) {
        this(EntityInit.ANIMATED_CONSTRUCT.get(), worldIn);
    }

    private void recalculateAll() {
        if (this.constructCapabilities != null) {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue((double) this.constructCapabilities.calculateMaxHealth());
            this.m_21051_(Attributes.MOVEMENT_SPEED).setBaseValue((double) this.constructCapabilities.calculateSpeed());
            this.m_21051_(Attributes.FLYING_SPEED).setBaseValue(this.canFly() ? 1.0 : (double) this.constructCapabilities.calculateSpeed());
            this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((double) this.constructCapabilities.calculateDamage());
            this.m_21051_(Attributes.ATTACK_SPEED).setBaseValue((double) this.constructCapabilities.calculateAttackRate());
            this.m_21051_(Attributes.ATTACK_KNOCKBACK).setBaseValue((double) this.constructCapabilities.calculateKnockback());
            this.m_21051_(Attributes.KNOCKBACK_RESISTANCE).setBaseValue((double) this.constructCapabilities.calculateKnockbackResistance());
            this.m_21051_(Attributes.ARMOR).setBaseValue((double) this.constructCapabilities.calculateArmor());
            this.m_21051_(Attributes.ARMOR_TOUGHNESS).setBaseValue((double) this.constructCapabilities.calculateToughness());
            this.m_21051_(Attributes.FOLLOW_RANGE).setBaseValue(32.0);
            this.m_21051_(AttributeInit.PERCEPTION_DISTANCE.get()).setBaseValue((double) this.constructCapabilities.calculatePerception());
            this.m_21051_(AttributeInit.INTELLIGENCE.get()).setBaseValue((double) this.constructCapabilities.calculateIntelligence());
            this.m_21051_(AttributeInit.RANGED_DAMAGE.get()).setBaseValue((double) this.constructCapabilities.calculateRangedDamage());
            this.f_19804_.set(MAX_MANA, this.constructCapabilities.calculateMana());
            this.f_19804_.set(BUOYANCY, this.constructCapabilities.calculateBuoyancy());
            this.f_19804_.set(FLUID_CAP, this.constructCapabilities.calculateFluidCapacity());
            this.f_21344_.setCanFloat(this.canSwim());
            if (this.f_21344_ instanceof ConstructPathNavigator) {
                ((ConstructPathNavigator) this.f_21344_).setEvaluatorFlags(this.canFly(), this.canSwim());
                if (this.canSwim()) {
                    this.m_21441_(BlockPathTypes.WATER, 8.0F);
                    this.m_21441_(BlockPathTypes.WATER_BORDER, 8.0F);
                } else {
                    this.m_21441_(BlockPathTypes.WATER, 16.0F);
                    this.m_21441_(BlockPathTypes.WATER_BORDER, 16.0F);
                }
            }
            int inventorySize = this.constructCapabilities.calculateInventorySize();
            NonNullList<ItemStack> newInv = NonNullList.createWithCapacity(inventorySize);
            for (int i = 0; i < inventorySize; i++) {
                newInv.add(i, ItemStack.EMPTY);
            }
            for (int i = 0; i < Math.min(this.inventory.size(), newInv.size()); i++) {
                newInv.set(i, this.inventory.get(i).copy());
            }
            this.inventory = newInv;
            if (this.canSwim()) {
                this.m_21051_(ForgeMod.SWIM_SPEED.get()).setBaseValue((double) this.constructCapabilities.calculateSpeed());
            }
            this.registerHammerLeap();
            this.registerAxeLeap();
        }
    }

    public void setConstructParts(@Nonnull ItemStack head, @Nonnull ItemStack torso, @Nonnull ItemStack left_arm, @Nonnull ItemStack right_arm, @Nonnull ItemStack legs) {
        this.constructCapabilities.setPart(head, false);
        this.constructCapabilities.setPart(torso, false);
        this.constructCapabilities.setPart(left_arm, false);
        this.constructCapabilities.setPart(right_arm, false);
        this.constructCapabilities.setPart(legs, true);
        this.recalculateAll();
    }

    public void setConstructParts(IConstructConstruction construction) {
        this.constructCapabilities = construction.copy();
        this.recalculateAll();
    }

    @Override
    protected PathNavigation createNavigation(Level world) {
        return new ConstructPathNavigator(this, world);
    }

    @Override
    public void tick() {
        if (this.f_19794_) {
            this.m_20334_(0.0, 0.0, 0.0);
        }
        this.cooldowns.keySet().forEach(c -> {
            int cd = (Integer) this.cooldowns.get(c);
            if (cd > 0) {
                this.cooldowns.put(c, cd - 1);
            }
        });
        super.m_8119_();
        this.updateSwingTime();
        this.moodlets.tick();
        if (this.hornDelay > 0) {
            this.hornDelay--;
        }
        if (this.attackDelay > 0) {
            this.attackDelay--;
        }
        if (this.defeatedCounter > 0) {
            this.defeatedCounter--;
            if (this.m_9236_().getGameTime() % 5L == 0L) {
                this.m_5634_(1.0F);
            }
            if (this.defeatedCounter == 15) {
                this.actionFlags = Construct.ActionFlags.DEFEATED.clear(this.actionFlags);
                this.actionFlags = Construct.ActionFlags.DEFEATED_END.set(this.actionFlags);
            } else if (this.defeatedCounter == 1) {
                this.resetActions();
                this.m_21557_(false);
            }
        }
        if (!ManaAndArtifice.instance.proxy.checkConstructDanceMixPlaying()) {
            if (this.dancing) {
                this.dancing = false;
                this.danceTicks = 0;
                this.getMoodlets().clearMoodlet(8);
            }
        } else {
            this.dancing = true;
            this.setHappy(10000);
            this.danceTicks++;
            if (this.danceTicks % 200 == 0) {
                this.danceIndex = (int) (Math.random() * 3.0);
            }
        }
        if (this.f_19797_ % 40 == 0) {
            int numWoodParts = this.constructCapabilities.getPartsForMaterial(ConstructMaterial.WOOD).size();
            this.m_5634_(1.0F + 0.5F * (float) numWoodParts);
            if (!this.m_9236_().isClientSide() && this.m_9236_().dimensionType().ultraWarm()) {
                int numWickerParts = this.constructCapabilities.getPartsForMaterial(ConstructMaterial.WICKERWOOD).size();
                int totalParts = numWoodParts + numWickerParts;
                if (totalParts > 1) {
                    this.m_20254_(totalParts * 5);
                }
            }
        }
        Player owner = this.getOwner();
        if (!(this.m_21223_() < this.m_21233_() * 0.25F) && (owner == null || !this.m_21574_().hasLineOfSight(owner) || !(owner.m_21223_() < owner.m_21233_() * 0.25F))) {
            this.getMoodlets().clearMoodlet(2);
        } else {
            this.setConcerned(250);
        }
        if (this.m_5448_() != null && !this.m_5448_().isAlive()) {
            this.setTarget(null);
        }
        if (this.m_9236_().isClientSide()) {
            if (this.mine_leave_pfx) {
                this.spawnMineLeaveParticles();
            } else if (this.mine_return_pfx) {
                this.spawnMineReturnParticles();
            } else if (this.adventure_leave_pfx) {
                this.spawnAdventureLeaveParticles();
            } else if (this.adventure_return_pfx) {
                this.spawnAdventureReturnParticles();
            }
            if (!Construct.ActionFlags.MINE_ENTER.isFlagSet(this.actionFlags)) {
                this.mine_leave_pfx = false;
            }
            if (!Construct.ActionFlags.MINE_LEAVE.isFlagSet(this.actionFlags)) {
                this.mine_return_pfx = false;
            }
            if (!Construct.ActionFlags.ADVENTURE_ENTER.isFlagSet(this.actionFlags)) {
                this.adventure_leave_pfx = false;
            }
            if (!Construct.ActionFlags.ADVENTURE_LEAVE.isFlagSet(this.actionFlags)) {
                this.adventure_return_pfx = false;
            }
            if (Construct.ActionFlags.EAT.isFlagSet(this.actionFlags) && ManaAndArtifice.instance.proxy.getGameTicks() % 5L == 0L) {
                this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GENERIC_EAT, this.m_5720_(), 1.0F, 1.0F);
            }
            if (this.canFly() && !this.m_20096_() && !this.isAway() && !Construct.ActionFlags.ADVENTURE_LEAVE.isFlagSet(this.actionFlags)) {
                this.spawnRocketParticles();
            }
        }
        if (this.needsSync) {
            this.needsSync = false;
            if (!this.m_9236_().isClientSide()) {
                ServerMessageDispatcher.sendEntityStateMessage(this);
            } else {
                ClientMessageDispatcher.sendAnimatedConstructSyncRequestMessage(this, false);
            }
        }
        if (this.m_9236_().isClientSide() && this.requestingDiagnostics && (this.f_19797_ % 20 == 0 || this.getDiagnostics().needsUpdate())) {
            ClientMessageDispatcher.sendAnimatedConstructSyncRequestMessage(this, true);
        }
    }

    @Override
    protected void customServerAiStep() {
        if (!this.m_9236_().isClientSide() && this.getCurrentCommand() != null && this.getCurrentCommand().isFinished() && this.getCurrentCommand().isOneOff()) {
            ConstructCommandFollowAndGuard cmd = (ConstructCommandFollowAndGuard) ConstructTasks.FOLLOW_DEFEND.instantiateTask(this);
            UUID follow = this.getCurrentCommand().getOneOffFollowTarget();
            Player followTarget = follow != null ? this.m_9236_().m_46003_(follow) : this.getOwner();
            cmd.setFollowTarget(followTarget);
            this.setCurrentCommand(followTarget, cmd, true);
        }
        super.m_8024_();
    }

    @Override
    public void setRecordPlayingNearby(BlockPos position, boolean playing) {
        if (playing && !this.dancing) {
            this.dancing = true;
            this.danceTicks = 0;
            this.setHappy(10000);
        } else if (!playing) {
            this.dancing = false;
            this.getMoodlets().clearMoodlet(8);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.m_6084_()) {
            if (this.m_20160_() && this.canRiderInteract()) {
                if (this.m_20069_() && (double) this.getWaterLevelAbove() - this.m_20186_() > 0.5) {
                    if (this.m_5842_()) {
                        if (this.m_20184_().y() < 0.0) {
                            this.m_20256_(this.m_20184_().add(0.0, (double) (0.01F * this.getBuoyancy()), 0.0));
                        }
                    } else {
                        this.m_20256_(this.m_20184_().add(0.0, 0.01 * (double) this.getBuoyancy(), 0.0));
                    }
                    this.lastYd = 0.0;
                }
                LivingEntity livingentity = this.getControllingPassenger();
                this.m_146922_(livingentity.m_146908_());
                this.f_19859_ = this.m_146908_();
                this.m_146926_(livingentity.m_146909_() * 0.5F);
                this.m_19915_(this.m_146908_(), this.m_146909_());
                this.f_20883_ = this.m_146908_();
                this.f_20885_ = this.f_20883_;
                float f = livingentity.xxa * 0.5F;
                float f1 = livingentity.zza;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                }
                if (this.jumpPower > 0.0F && !this.isJumping() && this.m_20096_()) {
                    double d0 = this.getJumpStrength() * (double) this.jumpPower * (double) this.m_20098_();
                    double d1;
                    if (this.m_21023_(MobEffects.JUMP)) {
                        d1 = d0 + (double) ((float) (this.m_21124_(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }
                    float horizontalScale = 1.8F;
                    Vec3 vector3d = this.m_20184_();
                    this.m_20334_(vector3d.x * (double) horizontalScale, d1, vector3d.z * (double) horizontalScale);
                    this.setJumping(true);
                    this.f_19812_ = true;
                    ForgeHooks.onLivingJump(this);
                    if (f1 > 0.0F) {
                        float f2 = Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0));
                        float f3 = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0));
                        this.m_20256_(this.m_20184_().add((double) (-0.4F * f2 * this.jumpPower), 0.0, (double) (0.4F * f3 * this.jumpPower)));
                    }
                    this.jumpPower = 0.0F;
                }
                if (this.m_6109_()) {
                    if (!this.m_20096_()) {
                        this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.05F);
                    } else {
                        this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.35F);
                    }
                    super.m_7023_(new Vec3((double) f, travelVector.y, (double) f1));
                } else if (livingentity instanceof Player) {
                    this.m_20256_(Vec3.ZERO);
                }
                if (this.m_20096_()) {
                    this.jumpPower = 0.0F;
                    this.setJumping(false);
                }
                this.m_267651_(false);
            } else {
                this.jumpPower = 0.02F;
                super.m_7023_(travelVector);
            }
        }
    }

    @Override
    protected float getFlyingSpeed() {
        return this.getControllingPassenger() instanceof Player ? this.m_6113_() * 0.1F : 0.04F;
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        this.setRiderPosition(pPassenger, pCallback);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.lastYd = this.m_20184_().y;
        super.m_7840_(y, onGroundIn, state, pos);
    }

    @Override
    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 deltaMovement, float pFriction) {
        float frictionInfluencedSpeed = this.m_20096_() ? this.m_6113_() * (0.21600002F / (pFriction * pFriction * pFriction)) : this.getFlyingSpeed();
        this.m_19920_(frictionInfluencedSpeed, deltaMovement);
        this.m_20256_(this.handleOnClimbable(this.m_20184_()));
        this.m_6478_(MoverType.SELF, this.m_20184_());
        Vec3 vec3 = this.m_20184_();
        if ((this.f_19862_ || this.f_20899_) && (this.m_6147_() || this.m_146900_().m_60713_(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(this))) {
            vec3 = new Vec3(vec3.x, 0.2, vec3.z);
        }
        return vec3;
    }

    private Vec3 handleOnClimbable(Vec3 vec0) {
        if (this.m_6147_()) {
            this.m_183634_();
            double d0 = Mth.clamp(vec0.x, -0.15F, 0.15F);
            double d1 = Mth.clamp(vec0.z, -0.15F, 0.15F);
            double d2 = Math.max(vec0.y, -0.15F);
            vec0 = new Vec3(d0, d2, d1);
        }
        return vec0;
    }

    public float getStepHeight() {
        return 1.5F;
    }

    public boolean isAway() {
        return Construct.ActionFlags.AWAY.isFlagSet(this.actionFlags);
    }

    public int getAwayType() {
        return this.f_19804_.get(AWAY_TYPE);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity0) {
        if (this.isAway()) {
            return false;
        } else if (entity0 instanceof Player) {
            return this.playerCanCommand((Player) entity0) ? false : this.current instanceof ConstructCommandFollowAndGuard;
        } else {
            return false;
        }
    }

    @Override
    public void push(Entity pEntity) {
        if (!this.isAway()) {
            super.m_7334_(pEntity);
        }
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        if (!this.isAway()) {
            super.m_5997_(pX, pY, pZ);
        }
    }

    @Override
    public float getWaterSlowDown() {
        return this.canSwim() ? 0.98F : super.m_6108_();
    }

    @Override
    public boolean isAffectedByFluids() {
        return this.f_19804_.get(BUOYANCY) < 0.0F ? false : super.m_6129_();
    }

    public void jumpInFluid(FluidType type) {
        if (this.canFly() && this.m_20069_()) {
            this.m_6135_();
        }
        super.jumpInFluid(type);
    }

    @Override
    protected float getJumpPower() {
        return this.canFly() && this.m_20069_() ? 0.12F * this.m_20098_() : 0.42F * this.m_20098_();
    }

    @Override
    protected boolean canAddPassenger(Entity entityIn) {
        if (this.isAway() || Construct.ActionFlags.MINE_ENTER.isFlagSet(this.actionFlags) || Construct.ActionFlags.MINE_LEAVE.isFlagSet(this.actionFlags) || Construct.ActionFlags.ADVENTURE_ENTER.isFlagSet(this.actionFlags) || Construct.ActionFlags.ADVENTURE_LEAVE.isFlagSet(this.actionFlags)) {
            return false;
        } else if (this.m_20197_().size() != 0) {
            return false;
        } else {
            return !(entityIn instanceof Player) ? false : this.getConstructData().isCapabilityEnabled(ConstructCapability.CARRY_PLAYER) && this.playerCanCommand((Player) entityIn);
        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 vec0, LivingEntity livingEntity1) {
        double d0 = this.m_20185_() + vec0.x;
        double d1 = this.m_20191_().minY;
        double d2 = this.m_20189_() + vec0.z;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        UnmodifiableIterator var10 = livingEntity1.getDismountPoses().iterator();
        while (var10.hasNext()) {
            Pose pose = (Pose) var10.next();
            blockpos$mutableblockpos.set(d0, d1, d2);
            double d3 = this.m_20191_().maxY + 0.75;
            do {
                double d4 = this.m_9236_().m_45573_(blockpos$mutableblockpos);
                if ((double) blockpos$mutableblockpos.m_123342_() + d4 > d3) {
                    break;
                }
                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = livingEntity1.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double) blockpos$mutableblockpos.m_123342_() + d4, d2);
                    if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity1, aabb.move(vec3))) {
                        livingEntity1.m_20124_(pose);
                        return vec3;
                    }
                }
                blockpos$mutableblockpos.move(Direction.UP);
            } while (!((double) blockpos$mutableblockpos.m_123342_() < d3));
        }
        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Vec3 vec3 = m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), this.m_146908_() + (livingEntity0.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F));
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, livingEntity0);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = m_19903_((double) this.m_20205_(), (double) livingEntity0.m_20205_(), this.m_146908_() + (livingEntity0.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F));
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, livingEntity0);
            return vec33 != null ? vec33 : this.m_20182_();
        }
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return this.f_19804_.get(BUOYANCY) >= 1.0F;
    }

    public boolean canRiderInteract() {
        return this.f_19804_.get(STEERABLE);
    }

    @Override
    public boolean isPushable() {
        return this.isAway() ? false : !this.m_20160_();
    }

    @Override
    public boolean isPushedByFluid() {
        return this.isAway() ? false : this.getConstructData().getAffinityScore(Affinity.WATER) < 4;
    }

    @Override
    protected boolean isImmobile() {
        return this.isAway() ? false : super.m_6107_() && this.m_20160_();
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.75;
    }

    public boolean isJumping() {
        return this.constructJumping;
    }

    public double getJumpStrength() {
        return 1.25;
    }

    @Nullable
    @Override
    public Player getOwner() {
        if (this.f_19804_.<Optional<UUID>>get(OWNER_UUID) != null && this.f_19804_.get(OWNER_UUID).isPresent()) {
            if (this.cached_owner == null || !this.cached_owner.m_6084_()) {
                this.cached_owner = this.m_9236_().m_46003_((UUID) this.f_19804_.get(OWNER_UUID).get());
            }
            return this.cached_owner;
        } else {
            return null;
        }
    }

    @Override
    public String getScoreboardName() {
        if (this.cached_team_name == null) {
            this.cached_team_name = super.m_6302_();
        }
        Player owner = this.getOwner();
        if (owner != null) {
            this.cached_team_name = owner.getScoreboardName();
        }
        return this.cached_team_name;
    }

    @Override
    public ConstructAITask<?> getCurrentCommand() {
        return this.current;
    }

    @Override
    public int getCarrySize() {
        return Math.max(1, 2 * this.constructCapabilities.getAffinityScore(Affinity.ENDER));
    }

    @Override
    public int getIntelligence() {
        return (int) this.m_21051_(AttributeInit.INTELLIGENCE.get()).getValue();
    }

    @Override
    public InteractionHand[] getEmptyHands() {
        ArrayList<InteractionHand> emptyHands = new ArrayList();
        this.constructCapabilities.getPart(ConstructSlot.LEFT_ARM).ifPresent(p -> {
            if (p.getModelTypeMutex() == 1 && this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
                emptyHands.add(InteractionHand.MAIN_HAND);
            }
        });
        this.constructCapabilities.getPart(ConstructSlot.RIGHT_ARM).ifPresent(p -> {
            if (p.getModelTypeMutex() == 1 && this.m_21120_(InteractionHand.OFF_HAND).isEmpty()) {
                emptyHands.add(InteractionHand.OFF_HAND);
            }
        });
        return (InteractionHand[]) emptyHands.toArray(new InteractionHand[0]);
    }

    @Override
    public InteractionHand[] getCarryingHands() {
        return this.getCarryingHands((Predicate<ItemStack>) null);
    }

    @Override
    public InteractionHand[] getCarryingHands(Predicate<ItemStack> filter) {
        ArrayList<InteractionHand> carryingHands = new ArrayList();
        this.constructCapabilities.getPart(ConstructSlot.LEFT_ARM).ifPresent(p -> {
            if (p.getModelTypeMutex() == 1) {
                ItemStack left = this.m_21120_(InteractionHand.MAIN_HAND);
                if (!left.isEmpty() && (filter == null || filter.test(left))) {
                    carryingHands.add(InteractionHand.MAIN_HAND);
                }
            }
        });
        this.constructCapabilities.getPart(ConstructSlot.RIGHT_ARM).ifPresent(p -> {
            if (p.getModelTypeMutex() == 1) {
                ItemStack right = this.m_21120_(InteractionHand.OFF_HAND);
                if (!right.isEmpty() && (filter == null || filter.test(right))) {
                    carryingHands.add(InteractionHand.OFF_HAND);
                }
            }
        });
        return (InteractionHand[]) carryingHands.toArray(new InteractionHand[0]);
    }

    @Override
    public InteractionHand[] getCarryingHands(DynamicItemFilter filter) {
        return this.getCarryingHands(is -> filter.matches(is));
    }

    @Override
    public boolean hasItem(DynamicItemFilter _filter) {
        InteractionHand[] hands = this.getCarryingHands(is -> _filter.matches(is));
        return hands.length > 0 ? true : InventoryUtilities.hasStackInInventory(_filter, this);
    }

    @Override
    public boolean hasItem(ItemStack stack, boolean checkDurability, boolean compareNBT) {
        InteractionHand[] hands = this.getCarryingHands(is -> {
            if (checkDurability && is.getDamageValue() != stack.getDamageValue()) {
                return false;
            } else {
                return compareNBT && !ManaAndArtificeMod.getItemHelper().AreTagsEqual(is, stack) ? false : is.getItem() == stack.getItem();
            }
        });
        return hands.length > 0 ? true : InventoryUtilities.hasStackInInventory(stack, checkDurability, compareNBT, this);
    }

    @Override
    public boolean hasItem(ResourceLocation tag, int quantity) {
        List<Item> items = MATags.getItemTagContents(tag);
        if (items.size() == 0) {
            return false;
        } else {
            int count = 0;
            ItemStack stack = this.m_21120_(InteractionHand.MAIN_HAND);
            if (items.contains(stack.getItem())) {
                count += stack.getCount();
            }
            stack = this.m_21120_(InteractionHand.OFF_HAND);
            if (items.contains(stack.getItem())) {
                count += stack.getCount();
            }
            if (count >= quantity) {
                return true;
            } else if (!this.constructCapabilities.areCapabilitiesEnabled(ConstructCapability.ITEM_STORAGE)) {
                return false;
            } else {
                for (int i = 0; i < this.getSlots(); i++) {
                    stack = this.getStackInSlot(i);
                    if (items.contains(stack.getItem())) {
                        count += stack.getCount();
                        if (count >= quantity) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isBlocking() {
        return this.getConstructData().isCapabilityEnabled(ConstructCapability.BLOCK) && !this.f_20911_;
    }

    public void onShieldBlock() {
        this.attackDelay -= 20;
    }

    @Override
    public IConstructConstruction getConstructData() {
        return this.constructCapabilities;
    }

    public boolean canSwim() {
        return this.f_19804_.get(BUOYANCY) >= 1.0F;
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        if (this.ANIM_PACKET) {
            nbt.putByte("anim", (byte) 1);
            if (this.f_20912_ != null) {
                nbt.putByte("hand", (byte) this.f_20912_.ordinal());
            }
            nbt.putInt("actionFlags", this.actionFlags);
            nbt.putInt("defeatedCounter", this.defeatedCounter);
        } else if (!this.requestingDiagnostics) {
            this.constructCapabilities.WriteNBT(nbt);
        } else {
            if (this.constructDiagnostics.needsUpdate()) {
                nbt.put("diag", this.constructDiagnostics.writeToNBT());
            }
            nbt.put("inv", ContainerHelper.saveAllItems(new CompoundTag(), this.inventory));
            nbt.put("hat", this.constructCapabilities.getHat().save(new CompoundTag()));
            nbt.put("banner", this.constructCapabilities.getBanner().save(new CompoundTag()));
        }
        this.moodlets.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    public ConstructDiagnostics getDiagnostics() {
        return this.constructDiagnostics;
    }

    @Override
    public Optional<InteractionHand> getHandWithCapability(ConstructCapability capability) {
        ArrayList<InteractionHand> hands = new ArrayList();
        this.constructCapabilities.getPart(ConstructSlot.LEFT_ARM).ifPresent(p -> {
            if (Arrays.asList(p.getEnabledCapabilities()).contains(capability)) {
                hands.add(InteractionHand.MAIN_HAND);
            }
        });
        this.constructCapabilities.getPart(ConstructSlot.RIGHT_ARM).ifPresent(p -> {
            if (Arrays.asList(p.getEnabledCapabilities()).contains(capability)) {
                hands.add(InteractionHand.OFF_HAND);
            }
        });
        if (hands.size() > 0) {
            Random r = new Random();
            InteractionHand h = (InteractionHand) hands.get(r.nextInt(hands.size()));
            return Optional.of(h);
        } else {
            return Optional.empty();
        }
    }

    public boolean handHasCapability(InteractionHand hand, ConstructCapability capability) {
        Optional<ItemConstructPart> part = Optional.empty();
        if (hand == InteractionHand.MAIN_HAND) {
            part = this.constructCapabilities.getPart(ConstructSlot.LEFT_ARM);
        } else {
            part = this.constructCapabilities.getPart(ConstructSlot.RIGHT_ARM);
        }
        return !part.isPresent() ? false : Arrays.asList(((ItemConstructPart) part.get()).getEnabledCapabilities()).contains(capability);
    }

    @Override
    public boolean canManaCannonAttack() {
        return this.getMana() >= 50.0F && this.getConstructData().areCapabilitiesEnabled(ConstructCapability.RANGED_ATTACK, ConstructCapability.STORE_MANA) && this.m_5448_() != null;
    }

    @Override
    public boolean canFluidSpray() {
        boolean baseline = this.getStoredFluidAmount() >= 20 && this.getConstructData().areCapabilitiesEnabled(ConstructCapability.FLUID_STORE, ConstructCapability.FLUID_DISPENSE);
        return baseline && FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).checkPredicate(this.m_5448_(), SummonUtils.isTargetFriendly(this.m_5448_(), this.getOwner()), this);
    }

    @Override
    public boolean canDualSweep() {
        if (this.getIntelligence() < 16) {
            return false;
        } else {
            Optional<ItemConstructPart> leftArm = this.getConstructData().getPart(ConstructSlot.LEFT_ARM);
            Optional<ItemConstructPart> rightArm = this.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            return leftArm.isPresent() && rightArm.isPresent() && leftArm.get() instanceof ConstructPartBladeArmLeft && rightArm.get() instanceof ConstructPartBladeArmRight;
        }
    }

    @Override
    public boolean expandFluidRange() {
        if (this.getIntelligence() < 16) {
            return false;
        } else {
            Optional<ItemConstructPart> leftArm = this.getConstructData().getPart(ConstructSlot.LEFT_ARM);
            Optional<ItemConstructPart> rightArm = this.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            return leftArm.isPresent() && rightArm.isPresent() && leftArm.get() instanceof ConstructPartFluidNozzleLeft && rightArm.get() instanceof ConstructPartFluidNozzleRight;
        }
    }

    @Override
    public Optional<LivingEntity> getDualCannonTarget() {
        if (this.m_5448_() == null || this.getIntelligence() < 16 || this.isOnCooldown(0)) {
            return Optional.empty();
        } else if (this.getMana() < 100.0F) {
            return Optional.empty();
        } else {
            Optional<ItemConstructPart> leftArm = this.getConstructData().getPart(ConstructSlot.LEFT_ARM);
            Optional<ItemConstructPart> rightArm = this.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            return leftArm.isPresent() && rightArm.isPresent() && leftArm.get() instanceof ConstructPartManaCannonLeft && rightArm.get() instanceof ConstructPartManaCannonRight ? this.m_9236_().getEntities(this, this.m_5448_().m_20191_().inflate(6.0), e -> e != this.m_5448_() && e.isAlive() && e instanceof LivingEntity && this.m_21574_().hasLineOfSight(e) && !this.isAlliedTo(e)).stream().map(e -> (LivingEntity) e).findFirst() : Optional.empty();
        }
    }

    @Override
    public boolean canSpellCast() {
        ISpellDefinition[] spells = this.getCastableSpells();
        if (spells.length == 0) {
            return false;
        } else {
            float minManaCost = Float.MAX_VALUE;
            for (ISpellDefinition spell : spells) {
                if (spell.getManaCost() < minManaCost) {
                    minManaCost = spell.getManaCost();
                }
            }
            return this.getMana() >= minManaCost && this.getConstructData().areCapabilitiesEnabled(ConstructCapability.STORE_MANA, ConstructCapability.CAST_SPELL);
        }
    }

    @Override
    public ISpellDefinition[] getCastableSpells() {
        return this.getConstructData().getCastableSpells();
    }

    @Override
    public float getMaxMana() {
        return this.f_19804_.get(MAX_MANA);
    }

    @Override
    public boolean isFishing() {
        return Construct.ActionFlags.FISH.isFlagSet(this.actionFlags);
    }

    @Override
    public boolean isRangedAttacking() {
        return Construct.ActionFlags.RANGED_ATTACK.isFlagSet(this.actionFlags);
    }

    @Override
    public float getMana() {
        return this.f_19804_.get(MANA);
    }

    @Override
    public float getManaPct() {
        return this.getMaxMana() == 0.0F ? 0.0F : MathUtils.clamp01(this.getMana() / Math.max(this.getMaxMana(), 1.0F));
    }

    public float getBuoyancy() {
        return this.f_19804_.get(BUOYANCY);
    }

    public float getWaterLevelAbove() {
        AABB axisalignedbb = this.m_20191_();
        int i = Mth.floor(axisalignedbb.minX);
        int j = Mth.ceil(axisalignedbb.maxX);
        int k = Mth.floor(axisalignedbb.maxY);
        int l = Mth.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = Mth.floor(axisalignedbb.minZ);
        int j1 = Mth.ceil(axisalignedbb.maxZ);
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        label39: for (int k1 = k; k1 < l; k1++) {
            float f = 0.0F;
            for (int l1 = i; l1 < j; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    blockpos$mutable.set(l1, k1, i2);
                    FluidState fluidstate = this.m_9236_().getFluidState(blockpos$mutable);
                    if (fluidstate.is(FluidTags.WATER)) {
                        f = Math.max(f, fluidstate.getHeight(this.m_9236_(), blockpos$mutable));
                    }
                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }
            if (f < 1.0F) {
                return (float) blockpos$mutable.m_123342_() + f;
            }
        }
        return (float) (l + 1);
    }

    public ConstructMoodlets getMoodlets() {
        return this.moodlets;
    }

    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance inst) {
        return inst.getEffect() != MobEffects.WITHER && inst.getEffect() != MobEffects.BAD_OMEN && inst.getEffect() != MobEffects.POISON && inst.getEffect() != MobEffects.WITHER && inst.getEffect() != MobEffects.HUNGER && inst.getEffect() != EffectInit.ASPHYXIATE.get() && inst.getEffect() != EffectInit.CHOOSING_WELLSPRING.get() && inst.getEffect() != EffectInit.CHRONO_ANCHOR.get() && inst.getEffect() != EffectInit.COLD_DARK.get() && inst.getEffect() != EffectInit.ELDRIN_FLIGHT.get() && inst.getEffect() != EffectInit.LIFT.get() && inst.getEffect() != EffectInit.MIST_FORM.get() && inst.getEffect() != EffectInit.WELLSPRING_SIGHT.get() && inst.getEffect() != EffectInit.EARTHQUAKE.get() && inst.getEffect() != EffectInit.LACERATE.get() && inst.getEffect() != EffectInit.SOAR.get() && inst.getEffect() != EffectInit.SPIDER_CLIMBING.get() ? super.m_7301_(inst) : false;
    }

    @Nullable
    public BlockPos getFishingTarget() {
        return this.f_19804_.get(FISHING_POS);
    }

    public boolean canFly() {
        return this.constructCapabilities == null ? false : this.constructCapabilities.isCapabilityEnabled(ConstructCapability.FLY);
    }

    @Override
    public boolean canTakeItem(ItemStack stack) {
        return !this.m_21531_() ? false : InventoryUtilities.hasRoomFor(this, stack);
    }

    public boolean isRenderDisabled() {
        return this.isAway();
    }

    public boolean playerCanCommand(Player player) {
        if (player == null) {
            return false;
        } else {
            return !this.f_19804_.get(OWNER_UUID).isPresent() || ((UUID) this.f_19804_.get(OWNER_UUID).get()).equals(player.m_20148_()) ? true : this.isAlliedTo(player);
        }
    }

    @Override
    public boolean isDefeated() {
        return this.defeatedCounter > 0;
    }

    @Override
    public boolean isDueling() {
        return this.f_21345_.getRunningGoals().anyMatch(g -> {
            if (g.getGoal() instanceof ConstructCommandFollowLodestar) {
                Optional<ConstructAITask<?>> cur = ((ConstructCommandFollowLodestar) g.getGoal()).currentCommand();
                if (cur.isPresent() && cur.get() instanceof ConstructDuel) {
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public int getAttackDelay() {
        return this.attackDelay;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    protected boolean isOnCooldown(int id) {
        return (Integer) this.cooldowns.getOrDefault(id, 0) > 0;
    }

    protected void setCooldown(int id, int ticks) {
        this.cooldowns.put(id, ticks);
    }

    @Override
    public void setHat(ItemStack stack) {
        ItemStack curHat = this.constructCapabilities.getHat();
        if (!curHat.isEmpty() && !this.m_9236_().isClientSide()) {
            ItemEntity item = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), curHat);
            this.m_9236_().m_7967_(item);
        }
        this.constructCapabilities.setHat(stack);
        this.setHappy(200);
        this.needsSync = true;
    }

    @Override
    public void setBanner(ItemStack stack) {
        this.constructCapabilities.setBanner(stack);
        this.needsSync = true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onPlayerJump(int jumpPowerIn) {
        if (jumpPowerIn < 0) {
            jumpPowerIn = 0;
        }
        if (jumpPowerIn >= 90) {
            this.jumpPower = 1.0F;
        } else {
            this.jumpPower = 0.4F + 0.4F * (float) jumpPowerIn / 90.0F;
        }
    }

    public void setOwner(UUID playerID) {
        this.f_19804_.set(OWNER_UUID, Optional.of(playerID));
    }

    @Override
    public boolean setCurrentCommand(Player player, ConstructAITask<?> cmd) {
        return this.setCurrentCommand(player, cmd, false);
    }

    public boolean setCurrentCommand(Player player, ConstructAITask<?> cmd, boolean skipOwnershipCheck) {
        if (!skipOwnershipCheck && !this.playerCanCommand(player)) {
            this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.notowner", player != null ? player.getDisplayName().getString() : "UNKNOWN").getString(), null);
            return false;
        } else if (cmd == null) {
            return false;
        } else {
            if (this.current != null) {
                this.current.stop();
                this.f_21345_.removeGoal(this.current);
            }
            this.resetActions();
            ConstructAITask<?> myCommand = cmd.duplicate();
            if (cmd.isOneOff()) {
                myCommand.setOneOff(player);
            }
            myCommand.setConstruct(this);
            myCommand.copyConnections(cmd);
            myCommand.onTaskSet();
            this.current = myCommand;
            this.f_21345_.addGoal(4, myCommand);
            if (this.current != null && this.current.allowSteeringMountedConstructsDuringTask()) {
                this.f_19804_.set(STEERABLE, true);
            } else {
                this.f_19804_.set(STEERABLE, false);
            }
            return true;
        }
    }

    public void setRequestingDiagnostics(boolean requesting) {
        this.requestingDiagnostics = requesting;
    }

    @Override
    public void setJumping(boolean jumping) {
        this.constructJumping = jumping;
        this.f_20899_ = jumping;
    }

    public void adjustMana(float amount) {
        float mana = this.getMana();
        mana += amount;
        if (mana > this.getMaxMana()) {
            mana = this.getMaxMana();
        }
        this.f_19804_.set(MANA, mana);
    }

    @Override
    public void setMining(boolean mining) {
        if (mining) {
            this.m_20153_();
            this.actionFlags = Construct.ActionFlags.MINE_ENTER.set(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.MINE_LEAVE.clear(this.actionFlags);
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("away", 120, true, this::setAwayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("flag", 60, true, (s, b) -> this.f_19804_.set(AWAY_TYPE, 1)));
        } else {
            this.actionFlags = Construct.ActionFlags.MINE_ENTER.clear(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.MINE_LEAVE.set(this.actionFlags);
            this.setAwayCallback("away", false);
        }
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void setAdventuring(boolean adventuring) {
        if (adventuring) {
            this.m_20153_();
            this.actionFlags = Construct.ActionFlags.ADVENTURE_ENTER.set(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.ADVENTURE_LEAVE.clear(this.actionFlags);
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("away", 140, true, this::setAwayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("flag", 100, true, (s, b) -> this.f_19804_.set(AWAY_TYPE, 2)));
        } else {
            this.actionFlags = Construct.ActionFlags.ADVENTURE_ENTER.clear(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.ADVENTURE_LEAVE.set(this.actionFlags);
            this.setAwayCallback("away", false);
        }
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void setHunting(boolean hunting) {
        if (hunting) {
            this.m_20153_();
            this.actionFlags = Construct.ActionFlags.ADVENTURE_ENTER.set(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.ADVENTURE_LEAVE.clear(this.actionFlags);
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("away", 140, true, this::setAwayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("flag", 100, true, (s, b) -> this.f_19804_.set(AWAY_TYPE, 3)));
        } else {
            this.actionFlags = Construct.ActionFlags.ADVENTURE_ENTER.clear(this.actionFlags);
            this.actionFlags = Construct.ActionFlags.ADVENTURE_LEAVE.set(this.actionFlags);
            this.setAwayCallback("away", false);
        }
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    public void setAwayCallback(String identifier, boolean away) {
        if (away) {
            this.m_20153_();
            this.actionFlags = Construct.ActionFlags.AWAY.set(this.actionFlags);
            this.ANIM_PACKET = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.ANIM_PACKET = false;
        } else {
            this.actionFlags = Construct.ActionFlags.AWAY.clear(this.actionFlags);
            this.f_19804_.set(AWAY_TYPE, 0);
        }
    }

    @Override
    protected void pushEntities() {
        if (!this.isAway()) {
            super.m_6138_();
        }
    }

    @Override
    public void setWatering() {
        if (!Construct.ActionFlags.WATER.isFlagSet(this.actionFlags)) {
            this.actionFlags = Construct.ActionFlags.WATER.set(this.actionFlags);
            this.ANIM_PACKET = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.ANIM_PACKET = false;
        }
    }

    @Override
    public void resetActions() {
        this.actionFlags = 0;
        this.f_19804_.set(AWAY_TYPE, 0);
        if (!this.m_9236_().isClientSide()) {
            this.ANIM_PACKET = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.ANIM_PACKET = false;
        }
    }

    @Override
    public void setTarget(LivingEntity entitylivingbaseIn) {
        if (entitylivingbaseIn == null && !this.m_9236_().isClientSide()) {
            this.resetAttackState();
        }
        if (entitylivingbaseIn != this) {
            if (entitylivingbaseIn != this.getOwner() || FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).checkPredicate(entitylivingbaseIn, true, this)) {
                if (entitylivingbaseIn == null) {
                    this.getMoodlets().clearMoodlet(1);
                    this.needsSync = true;
                } else if (SummonUtils.isTargetFriendly(entitylivingbaseIn, this.getOwner())) {
                    this.setConcerned(9999);
                } else {
                    this.setAngry(9999);
                }
                super.m_6710_(entitylivingbaseIn);
            }
        }
    }

    @Override
    public void setHappy(int strength) {
        this.moodlets.addMoodlet(8, strength);
        this.needsSync = true;
    }

    @Override
    public void setAngry(int strength) {
        this.moodlets.addMoodlet(1, strength);
        this.needsSync = true;
    }

    @Override
    public void setConcerned(int strength) {
        this.moodlets.addMoodlet(2, strength);
        this.needsSync = true;
    }

    @Override
    public void setConfused(int strength) {
        this.moodlets.addMoodlet(4, strength);
        this.needsSync = true;
    }

    @Override
    public void setUnimpressed(int strength) {
        this.moodlets.addMoodlet(32, strength);
        this.needsSync = true;
    }

    @Override
    public void setFishing(BlockPos pos) {
        this.f_19804_.set(FISHING_POS, pos);
        this.actionFlags = Construct.ActionFlags.FISH.set(this.actionFlags);
        Optional<InteractionHand> fishHand = this.getHandWithCapability(ConstructCapability.FISH);
        if (fishHand.isPresent()) {
            this.f_20912_ = (InteractionHand) fishHand.get();
        }
        if (!this.m_9236_().isClientSide()) {
            this.playSound(SoundEvents.FISHING_BOBBER_THROW, 1.0F, 1.0F);
        }
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void playSound(SoundEvent pSound, float pVolume, float pPitch) {
        if (this.m_9236_().isClientSide()) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), pSound, this.m_5720_(), pVolume, pPitch, false);
        } else {
            this.m_9236_().playSound(null, this.m_20183_(), pSound, this.m_5720_(), pVolume, pPitch);
        }
    }

    @Override
    public void setEating(InteractionHand hand) {
        this.actionFlags = Construct.ActionFlags.EAT.set(this.actionFlags);
        this.f_20912_ = hand;
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void stopFishing() {
        if (!this.m_9236_().isClientSide()) {
            this.playSound(SoundEvents.FISHING_BOBBER_RETRIEVE, 1.0F, 1.0F);
        }
        this.actionFlags = Construct.ActionFlags.FISH.clear(this.actionFlags);
        this.actionFlags = Construct.ActionFlags.FISH_END.set(this.actionFlags);
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void setDefeated(int duration) {
        this.f_21345_.getRunningGoals().forEach(g -> {
            if (g.getGoal() instanceof ConstructDuel) {
                ((ConstructDuel) g.getGoal()).forceFail();
            }
        });
        this.m_21557_(true);
        this.setTarget(null);
        this.defeatedCounter = duration;
        this.actionFlags = Construct.ActionFlags.DEFEATED.set(this.actionFlags);
        this.moodlets.addMoodlet(4, duration);
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    @Override
    public void setDualSweeping() {
        if (!this.m_9236_().isClientSide()) {
            this.actionFlags = Construct.ActionFlags.DUAL_SWEEP.set(this.actionFlags);
            this.ANIM_PACKET = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.ANIM_PACKET = false;
        }
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    public void spawnRocketParticles() {
        double speed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
        Vec3 particlePosition = this.m_20182_().add(0.0, 0.025F, 0.0);
        Vec3 forward = this.m_20156_();
        Vec3 offset = forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize().scale(0.3F);
        Vec3 particleVelocity;
        if (speed > 0.1F && this.m_20184_().y < speed / 4.0) {
            particleVelocity = this.m_20184_().scale(-1.0);
            particlePosition = particlePosition.add(-forward.x * 0.7F, 0.55, -forward.z * 0.7F);
        } else {
            particleVelocity = new Vec3(0.0, -0.05F, 0.0);
        }
        MAParticleType particleType = ParticleInit.FLAME.get();
        float particleScale = 0.15F;
        if (this.m_20069_()) {
            particleType = ParticleInit.WATER.get();
            particleScale = 0.05F;
        }
        Vec3i color = new Vec3i(10, 30, 65);
        if (this.constructCapabilities.getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.FLY) == ConstructMaterial.OBSIDIAN) {
            color = new Vec3i(170, 60, 10);
        }
        int numParticles = 2;
        for (int i = 0; i < numParticles; i++) {
            Vec3 jitter = new Vec3(this.m_9236_().getRandom().nextGaussian(), 0.0, this.m_9236_().getRandom().nextGaussian()).normalize().scale(0.0125F);
            this.m_9236_().addParticle(new MAParticleType(particleType).setColor(color.getX(), color.getY(), color.getZ()).setScale(particleScale), particlePosition.x + offset.x + jitter.x * 3.0, particlePosition.y, particlePosition.z + offset.z + jitter.z * 3.0, particleVelocity.x + jitter.x, particleVelocity.y, particleVelocity.z + jitter.z);
        }
        for (int i = 0; i < numParticles; i++) {
            Vec3 jitter = new Vec3(this.m_9236_().getRandom().nextGaussian(), 0.0, this.m_9236_().getRandom().nextGaussian()).normalize().scale(0.0125F);
            this.m_9236_().addParticle(new MAParticleType(particleType).setColor(color.getX(), color.getY(), color.getZ()).setScale(particleScale), particlePosition.x - offset.x + jitter.x * 3.0, particlePosition.y, particlePosition.z - offset.z + jitter.z * 3.0, particleVelocity.x + jitter.x, particleVelocity.y, particleVelocity.z + jitter.z);
        }
    }

    public void spawnMineReturnParticles() {
        BlockState bp = this.m_9236_().getBlockState(this.m_20183_().below());
        if (!bp.m_60795_()) {
            Vec3 feetPos = this.m_20182_();
            for (int i = 0; i < 20; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(new ItemStack(bp.m_60734_())).setMover(new ParticleVelocityMover(-0.2 + Math.random() * 0.4, 0.2F, -0.2 + Math.random() * 0.4, true)).setGravity(0.05F), feetPos.x - 0.5 + Math.random(), feetPos.y, feetPos.z - 0.5 + Math.random(), 0.0, 0.0, 0.0);
            }
        }
    }

    public void spawnMineLeaveParticles() {
        BlockState bp = this.m_9236_().getBlockState(this.m_20183_().below());
        if (!bp.m_60795_()) {
            Vec3 feetPos = this.m_20182_();
            for (int i = 0; i < 10; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(new ItemStack(bp.m_60734_())).setMover(new ParticleVelocityMover(-0.2 + Math.random() * 0.4, 0.2F, -0.2 + Math.random() * 0.4, true)).setGravity(0.05F), feetPos.x - 0.5 + Math.random(), feetPos.y, feetPos.z - 0.5 + Math.random(), 0.0, 0.0, 0.0);
            }
        }
    }

    public void spawnAdventureLeaveParticles() {
        this.pfxCounter++;
        Vec3 fwd = this.m_20156_();
        Vec3 feetPos = this.m_20182_().add(fwd.scale((double) this.pfxCounter));
        if (this.pfxCounter <= 25) {
            for (int i = 0; i < 10; i++) {
                Vec3 particleOffset = fwd.scale(Math.random());
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.COZY_SMOKE.get()), feetPos.x + particleOffset.x, feetPos.y + particleOffset.y, feetPos.z + particleOffset.z, 0.0, 0.02, 0.0);
            }
        }
    }

    public void spawnAdventureReturnParticles() {
        this.pfxCounter++;
        Vec3 fwd = this.m_20156_();
        Vec3 feetPos = this.m_20182_().add(fwd.scale((double) Math.min(-25 + this.pfxCounter, 0)));
        if (this.pfxCounter <= 25) {
            for (int i = 0; i < 10; i++) {
                Vec3 particleOffset = fwd.scale(Math.random());
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.COZY_SMOKE.get()), feetPos.x + particleOffset.x, feetPos.y + particleOffset.y, feetPos.z + particleOffset.z, 0.0, 0.02, 0.0);
            }
        }
    }

    @Override
    public void handleStartJump(int jumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    private void setRiderPosition(Entity entity, Entity.MoveFunction callback) {
        if (this.m_20363_(entity)) {
            Vec3 fwd = Vec3.directionFromRotation(this.m_20155_()).normalize().scale(-0.25);
            double d0 = this.m_20186_() + this.getPassengersRidingOffset();
            callback.accept(entity, this.m_20185_() + fwd.x, d0, this.m_20189_() + fwd.z);
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack mainHand = player.m_21205_();
        ItemStack offHand = player.m_21206_();
        if (offHand.getItem() == ItemInit.BELL_OF_BIDDING.get() || mainHand.getItem() == ItemInit.BELL_OF_BIDDING.get() || offHand.getItem() == ItemInit.__DEBUG.get() || mainHand.getItem() == ItemInit.__DEBUG.get()) {
            return InteractionResult.PASS;
        } else if (mainHand.getItem() instanceof BannerItem) {
            this.getConstructData().setBanner(mainHand);
            return InteractionResult.sidedSuccess(!player.m_9236_().isClientSide());
        } else if (offHand.getItem() instanceof BannerItem) {
            this.getConstructData().setBanner(offHand);
            return InteractionResult.sidedSuccess(!player.m_9236_().isClientSide());
        } else if (this.getOwner() == player && FluidUtil.interactWithFluidHandler(player, hand, this)) {
            return InteractionResult.sidedSuccess(!player.m_9236_().isClientSide());
        } else if (this.getOwner() == player && player.m_6047_() && player.m_21120_(hand).getItem() == Items.BUCKET) {
            this.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE);
            return InteractionResult.sidedSuccess(!player.m_9236_().isClientSide());
        } else if (this.canAddPassenger(player)) {
            this.mountTo(player);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected void mountTo(Player player) {
        if (!this.m_9236_().isClientSide()) {
            player.m_146926_(this.m_146909_());
            player.m_146922_(this.m_146908_());
            player.m_20329_(this);
            if (this.current != null && this.current.allowSteeringMountedConstructsDuringTask()) {
                this.f_19804_.set(STEERABLE, true);
            } else {
                this.f_19804_.set(STEERABLE, false);
            }
        }
    }

    @Override
    protected void doPush(Entity entityIn) {
        if (!(entityIn instanceof Construct) && !this.isAway()) {
            super.m_7324_(entityIn);
        }
    }

    public void soundHorn() {
        if (this.hornDelay <= 0) {
            if (this.m_9236_().isClientSide()) {
                ClientMessageDispatcher.dispatchConstructHorn(this.m_19879_());
            } else {
                Optional<ItemConstructPart> head = this.getConstructData().getPart(ConstructSlot.HEAD);
                if (head.isPresent() && head.get() instanceof ConstructPartHornHead) {
                    this.playSound(SFX.Entity.Construct.HORN, 1.0F, (float) (0.9 + Math.random() * 0.2));
                    this.hornDelay = 100;
                }
            }
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSourceIn) {
        super.m_6668_(damageSourceIn);
        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            List<ItemStack> drops = new ArrayList();
            for (ConstructSlot slot : ConstructSlot.values()) {
                this.constructCapabilities.getPart(slot).ifPresent(p -> {
                    if (!this.m_9236_().isClientSide()) {
                        if (Math.random() > 0.85F) {
                            drops.add(new ItemStack(p));
                        } else {
                            drops.addAll(p.getMaterial().getDeathLootMaterialDrops(this, damageSourceIn));
                        }
                    }
                });
            }
            drops.forEach(drop -> {
                ItemEntity itementity = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), drop);
                if (damageSourceIn.is(DamageTypeTags.IS_EXPLOSION)) {
                    itementity.m_20334_(-0.5 + Math.random(), Math.random() * 0.5, -0.5 + Math.random());
                }
                itementity.setDefaultPickUpDelay();
                this.m_9236_().m_7967_(itementity);
            });
            this.dropAllItems();
        }
    }

    @Override
    protected void updateSwingTime() {
        int i = 16;
        if (this.f_20911_) {
            this.f_20913_++;
            if (this.f_20913_ >= i) {
                this.f_20913_ = 0;
                this.f_20911_ = false;
            }
        } else {
            this.f_20913_ = 0;
        }
        this.f_20921_ = (float) this.f_20913_ / (float) i;
    }

    public void resetAttackState() {
        this.chargeCounter = 0;
        this.actionFlags = 0;
        this.m_5810_();
        this.ANIM_PACKET = true;
        ServerMessageDispatcher.sendEntityStateMessage(this);
        this.ANIM_PACKET = false;
    }

    public boolean isFollowing(Player player) {
        if (this.current instanceof ConstructCommandFollowAndGuard cmd && cmd.getFollowTarget() != null && cmd.getFollowTarget().m_19879_() == player.m_19879_()) {
            return true;
        }
        return false;
    }

    public void moveTo(Vec3 position, Player player) {
        if (!this.playerCanCommand(player)) {
            this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.notowner", player.getDisplayName().getString()).getString(), null);
        } else {
            ConstructAITask<?> inst = ConstructTasks.MOVE.instantiateTask(this);
            ((ConstructMove) inst).setDesiredLocation(BlockPos.containing(position));
            this.setCurrentCommand(player, inst);
        }
    }

    public void eatItem(Player player) {
        if (!this.playerCanCommand(player)) {
            this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.notowner", player.getDisplayName().getString()).getString(), null);
        } else {
            ConstructAITask<?> inst = ConstructTasks.EAT_ITEM.instantiateTask(this);
            inst.setOneOff(player);
            this.setCurrentCommand(player, inst);
        }
    }

    public void pickupItem(BlockPos target, Player player) {
        if (!this.playerCanCommand(player)) {
            this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.notowner", player.getDisplayName().getString()).getString(), null);
        } else {
            ConstructAITask<?> inst = ConstructTasks.GATHER_ITEMS.instantiateTask(this);
            inst.setOneOff(player);
            ((ConstructCollectItems) inst).setArea(new AABB(target));
            this.setCurrentCommand(player, inst);
        }
    }

    @Override
    public void lookTowards(Vec3 pos) {
        this.lookTowards(pos, 15.0F);
    }

    @Override
    public void lookTowards(Vec3 pos, float maxAngle) {
        Vec3 myPos = this.m_20182_();
        Vec3 desiredHeading = pos.subtract(myPos).normalize();
        Vec3 offset = MathUtils.rotateTowards(this.m_20184_(), desiredHeading, maxAngle).normalize();
        this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.m_20182_().add(offset));
    }

    @Override
    public List<LivingEntity> getValidAttackTargets(@Nullable LivingEntity defending) {
        double targetRange = this.m_21051_(AttributeInit.PERCEPTION_DISTANCE.get()).getValue();
        return (List<LivingEntity>) this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(targetRange, targetRange, targetRange)).stream().filter(e -> {
            Player owner = this.getOwner();
            if (e != this && e.isAlive()) {
                if (!this.m_21574_().hasLineOfSight(e)) {
                    return false;
                } else if (e == owner || this.isAlliedTo(defending, e)) {
                    return FluidParameterRegistry.INSTANCE.forFluid(this.getStoredFluid()).checkPredicate(e, true, this);
                } else if (e instanceof IFactionEnemy && owner != null) {
                    IPlayerProgression progression = (IPlayerProgression) owner.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    return progression != null && progression.getAlliedFaction() != null && progression.getAlliedFaction() == ((IFactionEnemy) e).getFaction() ? false : progression.getAlliedFaction() != null;
                } else if (e instanceof Player && !this.isAlliedTo(defending, e)) {
                    return true;
                } else {
                    LivingEntity guardTarget = (LivingEntity) (defending != null ? defending : owner);
                    if (e instanceof Mob && guardTarget != null) {
                        Mob mob = (Mob) e;
                        LivingEntity target = mob.getTarget();
                        if (target != null && (target == guardTarget || !target.m_7307_(guardTarget))) {
                            return true;
                        }
                        LivingEntity lastHurtBy = mob.m_21188_();
                        if (lastHurtBy != null && (lastHurtBy == guardTarget || !lastHurtBy.m_7307_(guardTarget))) {
                            return true;
                        }
                    }
                    return e instanceof Enemy && !(e instanceof IFactionEnemy) ? true : FluidParameterRegistry.INSTANCE.forFluid(this.getStoredFluid()).checkPredicate(e, true, this);
                }
            } else {
                return false;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean validateFriendlyTarget(LivingEntity target) {
        if (Construct.ActionFlags.SPRAY.isFlagSet(this.actionFlags) && this.__cachedFluid != null) {
            return FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).checkPredicate(target, true, this);
        } else {
            return (Construct.ActionFlags.CAST_SPELL_SWING.isFlagSet(this.actionFlags) || Construct.ActionFlags.CAST_SPELL_CHANNEL.isFlagSet(this.actionFlags)) && this.currentSpell != null ? !this.currentSpell.isHarmful() : false;
        }
    }

    public void consumeManaForRangedAttack() {
        this.adjustMana(-50.0F);
    }

    public void consumeFluidForSpray() {
        this.drain(20, IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public boolean performRangedAttack(LivingEntity attackTarget) {
        if (!this.m_21574_().hasLineOfSight(attackTarget)) {
            this.m_21573_().moveTo(attackTarget, this.m_21133_(Attributes.MOVEMENT_SPEED));
            return false;
        } else {
            this.f_19804_.set(STEERABLE, false);
            this.m_21563_().setLookAt(attackTarget, 30.0F, 30.0F);
            this.chargeCounter++;
            this.m_21573_().stop();
            boolean isFriendly = SummonUtils.isTargetFriendly(attackTarget, this.getOwner());
            if (this.chargeCounter == 1) {
                this.actionFlags = Construct.ActionFlags.RANGED_ATTACK.set(this.actionFlags);
                if (this.canSpellCast()) {
                    this.setupSpellCast(Math.random() < 0.5);
                } else if (this.canManaCannonAttack()) {
                    if (this.getDualCannonTarget().isPresent()) {
                        this.actionFlags = Construct.ActionFlags.DUAL_SHOOT.set(this.actionFlags);
                    } else {
                        this.getHandWithCapability(ConstructCapability.RANGED_ATTACK).ifPresent(h -> this.f_20912_ = h);
                    }
                } else if (this.canFluidSpray() && FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).checkPredicate(attackTarget, isFriendly, this)) {
                    this.actionFlags = Construct.ActionFlags.SPRAY.set(this.actionFlags);
                    if (this.expandFluidRange()) {
                        this.actionFlags = Construct.ActionFlags.DUAL_SPRAY.set(this.actionFlags);
                    } else {
                        this.getHandWithCapability(ConstructCapability.FLUID_DISPENSE).ifPresent(h -> this.f_20912_ = h);
                    }
                }
                this.ANIM_PACKET = true;
                ServerMessageDispatcher.sendEntityStateMessage(this);
                this.ANIM_PACKET = false;
                return false;
            } else if (isFriendly && !this.validateFriendlyTarget(attackTarget)) {
                this.setTarget(null);
                return true;
            } else if (Construct.ActionFlags.SPRAY.isFlagSet(this.actionFlags)) {
                return this.performFluidAttack(attackTarget);
            } else if (!Construct.ActionFlags.CAST_SPELL_SWING.isFlagSet(this.actionFlags) && !Construct.ActionFlags.CAST_SPELL_CHANNEL.isFlagSet(this.actionFlags)) {
                return this.performManaCannonAttack(attackTarget);
            } else {
                this.chargeCounter--;
                return this.performSpellAttack(attackTarget);
            }
        }
    }

    @Override
    public boolean setupSpellCast(boolean mainHand) {
        this.actionFlags = Construct.ActionFlags.RANGED_ATTACK.set(this.actionFlags);
        ISpellDefinition[] spells = this.getCastableSpells();
        if (this.canSpellCast() && spells.length != 0) {
            int idx = mainHand ? 0 : 1;
            ISpellDefinition selection = spells[idx % spells.length];
            if (this.getMana() < selection.getManaCost()) {
                this.resetSpellCast();
                return false;
            } else {
                List<SpellReagent> reagents = selection.getReagents(null, this.f_20912_, null);
                HashMap<Item, Boolean> missing = new HashMap();
                for (SpellReagent reagent : reagents) {
                    if (!this.hasItem(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT())) {
                        missing.put(reagent.getReagentStack().getItem(), reagent.getOptional());
                    }
                }
                List<Entry<Item, Boolean>> missingRequired = (List<Entry<Item, Boolean>>) missing.entrySet().stream().filter(e -> !(Boolean) e.getValue()).collect(Collectors.toList());
                if (missingRequired.size() > 0) {
                    this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.cast.missing_reagent").getString(), ConstructTasks.CAST_SPELL_AT_TARGET.getIconTexture());
                    this.resetSpellCast();
                    return false;
                } else {
                    CompoundTag tmp = new CompoundTag();
                    selection.writeToNBT(tmp);
                    this.currentSpell = SpellRecipe.fromNBT(tmp);
                    if (this.currentSpell.isChanneled()) {
                        this.actionFlags = Construct.ActionFlags.CAST_SPELL_CHANNEL.set(this.actionFlags);
                    } else {
                        this.actionFlags = Construct.ActionFlags.CAST_SPELL_SWING.set(this.actionFlags);
                    }
                    this.getHandWithCapability(ConstructCapability.CAST_SPELL).ifPresent(h -> this.f_20912_ = h);
                    this.ANIM_PACKET = true;
                    ServerMessageDispatcher.sendEntityStateMessage(this);
                    this.ANIM_PACKET = false;
                    return true;
                }
            }
        } else {
            this.resetSpellCast();
            return false;
        }
    }

    @Override
    public boolean startSpellCast(@Nullable SpellTarget targetHint) {
        SpellSource source = new SpellSource(this, this.f_20912_);
        ItemStack spellStack = new ItemStack(ItemInit.SPELL.get());
        this.currentSpell.writeToNBT(spellStack.getOrCreateTag());
        SpellCaster.applyAdjusters(spellStack, this, this.currentSpell, SpellCastStage.CALCULATING_MANA_COST);
        List<SpellReagent> reagents = this.currentSpell.getReagents(null, this.f_20912_, null);
        HashMap<Item, Boolean> missing = new HashMap();
        boolean allReagentsConsumed = true;
        ArrayList<Pair<IItemHandler, Direction>> myInv = new ArrayList();
        myInv.add(new Pair(this, Direction.UP));
        for (SpellReagent reagent : reagents) {
            if (!this.hasItem(reagent.getReagentStack(), !reagent.getIgnoreDurability(), reagent.getCompareNBT())) {
                missing.put(reagent.getReagentStack().getItem(), reagent.getOptional());
            } else if (reagent.getConsume()) {
                boolean found = false;
                InteractionHand[] hands = this.getCarryingHands(is -> {
                    if (!reagent.getIgnoreDurability() && is.getDamageValue() != reagent.getReagentStack().getDamageValue()) {
                        return false;
                    } else {
                        return reagent.getCompareNBT() && !ManaAndArtificeMod.getItemHelper().AreTagsEqual(is, reagent.getReagentStack()) ? false : is.getItem() == reagent.getReagentStack().getItem();
                    }
                });
                if (hands.length > 0) {
                    ItemStack handStack = this.m_21120_(hands[0]);
                    handStack.shrink(1);
                    if (handStack.isEmpty()) {
                        this.m_21008_(hands[0], ItemStack.EMPTY);
                    } else {
                        this.m_21008_(hands[0], handStack);
                    }
                    found = true;
                } else {
                    found = InventoryUtilities.consumeAcrossInventories(reagent.getReagentStack(), reagent.getIgnoreDurability(), reagent.getCompareNBT(), false, myInv);
                }
                allReagentsConsumed &= found;
            }
        }
        if (!allReagentsConsumed) {
            this.resetSpellCast();
            return false;
        } else {
            float discountFactor = 1.0F - 0.1F * (float) this.getConstructData().getAffinityScore(Affinity.ARCANE);
            this.adjustMana(-this.currentSpell.getManaCost() * discountFactor);
            if (this.currentSpell.isChanneled()) {
                this.m_21008_(this.f_20912_, spellStack);
                this.m_6672_(this.f_20912_);
            }
            SpellCaster.Affect(spellStack, this.currentSpell, this.m_9236_(), source, targetHint);
            return true;
        }
    }

    @Override
    public boolean tickSpellCast() {
        this.chargeCounter++;
        if (this.chargeCounter > 80) {
            this.resetSpellCast();
            return false;
        } else {
            if (this.currentSpell.isChanneled()) {
                if (this.chargeCounter >= 60) {
                    this.resetSpellCast();
                    return false;
                }
                if (this.chargeCounter >= 5) {
                    float discountFactor = 1.0F - 0.1F * (float) this.getConstructData().getAffinityScore(Affinity.ARCANE);
                    this.adjustMana(-this.currentSpell.getManaCost() * discountFactor);
                }
            } else if (this.chargeCounter >= 20) {
                this.resetSpellCast();
                return false;
            }
            return true;
        }
    }

    @Override
    public void resetSpellCast() {
        this.m_5810_();
        this.resetAttackState();
        this.f_19804_.set(STEERABLE, true);
    }

    private boolean performFluidAttack(LivingEntity attackTarget) {
        if (this.chargeCounter == 2) {
            this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).spraySound(), SoundSource.NEUTRAL, 0.25F, (float) (0.9 + Math.random() * 0.1));
        } else {
            if (this.chargeCounter == 15) {
                this.consumeFluidForSpray();
                this.resetAttackState();
                this.f_19804_.set(STEERABLE, true);
                return true;
            }
            if (this.chargeCounter >= 5 && this.chargeCounter % 5 == 0) {
                float range = 10.0F;
                float angle = 45.0F;
                if (this.expandFluidRange()) {
                    range *= 1.5F;
                    angle = 60.0F;
                }
                Vec3 forward = this.m_5448_().m_20182_().subtract(this.m_20182_()).normalize();
                EntityUtil.getEntitiesWithinCone(this.m_9236_(), this.m_20182_(), forward, range, -angle, angle, e -> {
                    if (!(e instanceof LivingEntity)) {
                        return false;
                    } else {
                        boolean isFriendly = this.isAlliedTo(e);
                        if (e == this.getOwner() && !FluidParameterRegistry.INSTANCE.forFluid(this.__cachedFluid).checkPredicate(e, isFriendly, this)) {
                            return false;
                        } else {
                            ConstructSprayTargetingEvent targetEvt = new ConstructSprayTargetingEvent(this, e, this.__cachedFluid, e == this.getOwner(), isFriendly);
                            MinecraftForge.EVENT_BUS.post(targetEvt);
                            return !targetEvt.isCanceled();
                        }
                    }
                }).stream().map(e -> e).forEach(e -> {
                    ConstructSprayEffectEvent effectEvt = new ConstructSprayEffectEvent(this, e, this.__cachedFluid, e == this.getOwner(), SummonUtils.isTargetFriendly(e, this.getOwner()));
                    MinecraftForge.EVENT_BUS.post(effectEvt);
                });
            }
        }
        return false;
    }

    private boolean performSpellAttack(LivingEntity attackTarget) {
        if (this.chargeCounter > 80) {
            this.resetAttackState();
            this.m_5810_();
            this.f_19804_.set(STEERABLE, true);
            return true;
        } else if (this.chargeCounter == 2) {
            this.m_7618_(EntityAnchorArgument.Anchor.EYES, attackTarget.m_146892_());
            this.startSpellCast(new SpellTarget(attackTarget));
            return false;
        } else {
            this.tickSpellCast();
            return false;
        }
    }

    private boolean performManaCannonAttack(LivingEntity attackTarget) {
        if (this.chargeCounter == 20) {
            this.getDualCannonTarget().ifPresent(eTgt -> {
                this.spawnManaCannonShot(eTgt);
                this.setCooldown(0, (int) (500.0F * this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.RANGED_ATTACK).getCooldownMultiplierFor(ConstructCapability.RANGED_ATTACK)));
            });
            this.spawnManaCannonShot(attackTarget);
        } else if (this.chargeCounter == 40) {
            this.resetAttackState();
            this.f_19804_.set(STEERABLE, true);
            return true;
        }
        return false;
    }

    private void spawnManaCannonShot(LivingEntity target) {
        Affinity aff = this.getConstructData().getRandomContainedAffinity();
        SpellProjectile esp = new SpellProjectile(this, this.m_9236_());
        esp.setForcedDamageAffinityAndTarget(aff, (float) this.m_21051_(AttributeInit.RANGED_DAMAGE.get()).getValue(), target);
        esp.setHomingStrength(MathUtils.clamp01((float) (this.getIntelligence() - 16) / 24.0F));
        this.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
        esp.shoot(this, this.m_20156_(), 1.0F, 0.0F);
        this.m_9236_().m_7967_(esp);
        this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SFX.Spell.Cast.ForAffinity(aff), SoundSource.NEUTRAL, 0.25F, (float) (0.9 + Math.random() * 0.1));
        this.consumeManaForRangedAttack();
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof Construct) {
            Optional<UUID> theirOwnerID = ((Construct) pEntity).f_19804_.get(OWNER_UUID);
            Optional<UUID> myOwnerID = this.f_19804_.get(OWNER_UUID);
            if (theirOwnerID.isPresent() && myOwnerID.isPresent()) {
                Player myOwner = this.getOwner();
                Player theirOwner = ((Construct) pEntity).getOwner();
                if (myOwner != null && theirOwner != null) {
                    return myOwner.m_7307_(theirOwner);
                }
                if (((UUID) myOwnerID.get()).equals(theirOwnerID.get())) {
                    return true;
                }
            }
        }
        return SummonUtils.isTargetFriendly(pEntity, this.getOwner());
    }

    public boolean isAlliedTo(@Nullable LivingEntity defending, Entity pEntity) {
        return defending == null ? this.isAlliedTo(pEntity) : SummonUtils.isTargetFriendly(pEntity, defending);
    }

    @Override
    public void die(DamageSource cause) {
        if (this.isDueling()) {
            this.m_21153_(1.0F);
            Component opponentName = cause.getEntity() != null ? cause.getEntity().getCustomName() : null;
            if (opponentName == null) {
                opponentName = Component.translatable("mna.constructs.feedback.target.no_name");
            }
            this.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.duel.defeat", opponentName), ConstructTasks.DUEL.getIconTexture());
            this.setDefeated(600);
        } else {
            super.m_6667_(cause);
            if (!(this.current instanceof ConstructCommandReturnToTable)) {
                this.constructDiagnostics.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.death").getString(), null, false);
            } else {
                this.constructDiagnostics.pushDiagnosticMessage(Component.translatable("mna.constructs.feedback.return_to_table").getString(), null, false);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isAway()) {
            return false;
        } else if (source.is(MATags.DamageTypes.CONSTRUCT_IMMUNE)) {
            return false;
        } else if (this.getControllingPassenger() != null && source.getEntity() == this.getControllingPassenger()) {
            return false;
        } else if (source.getEntity() == null || this.getOwner() == null || source.getEntity() != this.getOwner() && !this.isAlliedTo(source.getDirectEntity())) {
            if (this.getControllingPassenger() != null && !this.redirectingDamage) {
                this.redirectingDamage = true;
                this.getControllingPassenger().hurt(source, amount);
                this.redirectingDamage = false;
                return false;
            } else if (this.defeatedCounter > 0) {
                return false;
            } else {
                int numWoodParts = this.constructCapabilities.getPartsForMaterial(ConstructMaterial.WOOD).size();
                int numWickerwoodParts = this.constructCapabilities.getPartsForMaterial(ConstructMaterial.WICKERWOOD).size();
                int burnableParts = numWoodParts + numWickerwoodParts;
                if (burnableParts > 0 && source.is(DamageTypeTags.IS_FIRE)) {
                    amount *= 2.0F;
                }
                float resistance = 0.0F;
                if (source.is(DamageTypeTags.IS_EXPLOSION)) {
                    resistance += this.constructCapabilities.calculateExplosionResistance();
                }
                amount -= amount * resistance;
                resistance = 0.0F;
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_EARTH)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.EARTH);
                }
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_WIND)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.WIND);
                }
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_FIRE)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.FIRE);
                }
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_WATER)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.WATER);
                }
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_ARCANE)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.ARCANE);
                }
                if (source.is(MATags.DamageTypes.CONSTRUCT_RESIST_ENDER)) {
                    resistance += (float) this.getConstructData().getAffinityScore(Affinity.ENDER);
                }
                amount -= resistance;
                if (this.getConstructData().isCapabilityEnabled(ConstructCapability.BLOCK) && source.getEntity() == this.m_5448_()) {
                    this.attackDelay += 20;
                }
                return amount <= 0.0F ? false : super.m_6469_(source, amount);
            }
        } else {
            this.setUnimpressed(60);
            return false;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (super.m_7327_(entityIn)) {
            if (this.constructCapabilities.getAffinityScore(Affinity.FIRE) > 0) {
                entityIn.setSecondsOnFire(this.constructCapabilities.getAffinityScore(Affinity.FIRE));
            }
            int water = this.constructCapabilities.getAffinityScore(Affinity.WATER);
            if (water > 0 && entityIn instanceof LivingEntity living) {
                int amp = (int) Math.ceil((double) ((float) water / 2.0F));
                int dur = water * 20;
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, dur, amp));
            }
            if (this.constructCapabilities.isCapabilityEnabled(ConstructCapability.TAUNT) && entityIn instanceof Mob) {
                ((Mob) entityIn).setTarget(this);
                if (Math.random() < 0.01) {
                    this.playSound(SFX.Entity.Construct.HORN, 1.0F, (float) (0.9 + Math.random() * 0.2));
                }
            }
            if (this.getConstructData().isCapabilityEnabled(ConstructCapability.BLOCK)) {
                this.attackDelay = 10 + (int) (20.0 * Math.random());
            }
            if (!this.m_9236_().isClientSide() && (float) this.constructCapabilities.calculateIntelligence() >= 36.0F && (float) this.constructCapabilities.calculatePerception() >= 28.0F && entityIn instanceof LivingEntity living) {
                living.setLastHurtByPlayer(FakePlayerFactory.getMinecraft((ServerLevel) this.m_9236_()));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        if (this.constructCapabilities != null) {
            this.constructCapabilities.WriteNBT(compound);
        }
        compound.putString("owner", this.f_19804_.get(OWNER_UUID) != null && this.f_19804_.get(OWNER_UUID).isPresent() ? ((UUID) this.f_19804_.get(OWNER_UUID).get()).toString() : "");
        if (this.current != null) {
            compound.put("currentGoal", this.current.writeNBT());
        }
        compound.putFloat("mana", this.f_19804_.get(MANA));
        compound.putString("fluidType", this.f_19804_.get(FLUIDTYPE));
        compound.putInt("fluidAmt", this.f_19804_.get(FLUIDAMT));
        if (this.cached_team_name != null) {
            compound.putString("alliedTeam", this.cached_team_name);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        this.constructCapabilities.ReadNBT(compound);
        if (compound.contains("owner")) {
            try {
                this.f_19804_.set(OWNER_UUID, Optional.of(UUID.fromString(compound.getString("owner"))));
            } catch (Exception var5) {
                ManaAndArtifice.LOGGER.error("Error loading construct owner UUID.  It is now unclaimed and can be claimed by any player.");
            }
        }
        this.recalculateAll();
        if (compound.contains("mana")) {
            this.f_19804_.set(MANA, compound.getFloat("mana"));
        }
        if (compound.contains("fluidType")) {
            String fluidType = compound.getString("fluidType");
            ResourceLocation fLoc = new ResourceLocation(fluidType);
            this.setStoredFluid(ForgeRegistries.FLUIDS.getValue(fLoc));
        }
        if (compound.contains("fluidAmt")) {
            this.setStoredFluidAmount(compound.getInt("fluidAmt"));
        }
        if (compound.contains("alliedTeam")) {
            this.cached_team_name = compound.getString("alliedTeam");
        }
        if (compound.contains("currentGoal")) {
            CompoundTag goalData = compound.getCompound("currentGoal");
            ConstructTask action = ConstructAITask.getTypeFromNBT(goalData);
            ConstructAITask<?> inst = action.instantiateTask(this);
            if (inst != null) {
                this.current = inst;
                this.current.setConstruct(this);
                this.current.readNBT(goalData);
                if (this.current != null && this.current.allowSteeringMountedConstructsDuringTask()) {
                    this.f_19804_.set(STEERABLE, true);
                } else {
                    this.f_19804_.set(STEERABLE, false);
                }
                this.f_21345_.addGoal(4, this.current);
            }
        }
        this.m_21557_(false);
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.FLYING_SPEED, 0.35F).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.ATTACK_SPEED, 20.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0).add(AttributeInit.PERCEPTION_DISTANCE.get(), 8.0).add(AttributeInit.INTELLIGENCE.get(), 8.0).add(AttributeInit.RANGED_DAMAGE.get(), 0.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(OWNER_UUID, Optional.empty());
        this.f_19804_.define(MANA, 0.0F);
        this.f_19804_.define(MAX_MANA, 0.0F);
        this.f_19804_.define(STEERABLE, false);
        this.f_19804_.define(BUOYANCY, 0.0F);
        this.f_19804_.define(FLUID_CAP, 0);
        this.f_19804_.define(FLUIDAMT, 0);
        this.f_19804_.define(AWAY_TYPE, 0);
        this.f_19804_.define(FLUIDTYPE, "");
        this.f_19804_.define(FISHING_POS, BlockPos.ZERO);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        super.m_7350_(data);
        String id = this.f_19804_.get(FLUIDTYPE);
        if (id != null && !id.isEmpty()) {
            ResourceLocation rLoc = new ResourceLocation(id);
            this.__cachedFluid = ForgeRegistries.FLUIDS.getValue(rLoc);
        } else {
            this.__cachedFluid = null;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new Construct.ConstructSwimGoal());
        this.f_21345_.addGoal(0, new Construct.ConstructOpenDoors(this, true));
        this.f_21345_.addGoal(0, new ConstructFleeTarget(this, 12.0F, 0.5, 0.5));
    }

    private void registerHammerLeap() {
        Optional<ItemConstructPart> leftArm = this.getConstructData().getPart(ConstructSlot.LEFT_ARM);
        if (leftArm.isPresent() && leftArm.get() instanceof ConstructPartHammerArmLeft) {
            Optional<ItemConstructPart> rightArm = this.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            if (rightArm.isPresent() && rightArm.get() instanceof ConstructPartHammerArmRight) {
                if (this.getIntelligence() >= 16) {
                    float speed = this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.SMITH).getCooldownMultiplierFor(ConstructCapability.SMITH) * 0.65F;
                    float damage = this.getConstructData().calculateDamage() / 2.0F;
                    this.f_21345_.addGoal(1, new LerpLeap<>(this, 10, 5, e -> {
                        if (this.m_5448_() == null || this.isAlliedTo(this.m_5448_()) || !this.m_5448_().m_20096_()) {
                            return false;
                        } else if (this.isOnCooldown(0)) {
                            return false;
                        } else {
                            int yCount = 0;
                            for (BlockPos pos = this.m_20183_().above(); this.m_9236_().m_46859_(pos) && yCount < 5; pos = pos.above()) {
                                yCount++;
                            }
                            return yCount < 5 ? false : this.m_5448_() != null && this.m_5448_().isAlive() && this.m_5448_().m_20270_(this) < 4.0F && this.m_21574_().hasLineOfSight(this.m_5448_());
                        }
                    }, evt -> {
                        boolean sendFlags = false;
                        switch(evt) {
                            case DAMAGE:
                                this.m_9236_().getEntities(this, this.m_20191_().inflate(4.0), tgt -> tgt instanceof LivingEntity && tgt.isAlive() && tgt != this && !this.isAlliedTo(tgt)).stream().map(e -> (LivingEntity) e).forEach(target -> {
                                    float mX = (float) (this.m_20185_() - target.m_20185_());
                                    float mZ = (float) (this.m_20189_() - target.m_20189_());
                                    float speed2 = this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.SMITH).getCooldownMultiplierFor(ConstructCapability.SMITH) * 0.65F;
                                    ComponentFling.flingTarget(target, new Vec3((double) mX, (double) speed, (double) mZ), speed2, 3.0F);
                                    target.hurt(this.m_269291_().mobAttack(this), damage);
                                });
                                if (!this.m_9236_().isClientSide()) {
                                    ServerMessageDispatcher.sendParticleEffect(this.m_9236_().dimension(), 32.0F, this.m_20185_(), this.m_20186_(), this.m_20189_(), SpawnParticleEffectMessage.ParticleTypes.CONSTRUCT_HAMMER_SMASH);
                                }
                                break;
                            case LAND:
                                this.actionFlags = Construct.ActionFlags.LEAP_LAND.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case LEAP:
                                this.actionFlags = Construct.ActionFlags.LEAP_AIRBORNE.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case START:
                                this.actionFlags = Construct.ActionFlags.LEAP_START.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case STOP:
                            default:
                                this.actionFlags = 0;
                                sendFlags = true;
                                this.setCooldown(0, (int) (500.0F * this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.SMITH).getCooldownMultiplierFor(ConstructCapability.SMITH)));
                        }
                        if (sendFlags && !this.m_9236_().isClientSide()) {
                            this.ANIM_PACKET = true;
                            ServerMessageDispatcher.sendEntityStateMessage(this);
                            this.ANIM_PACKET = false;
                        }
                    }));
                }
            }
        }
    }

    private void registerAxeLeap() {
        Optional<ItemConstructPart> leftArm = this.getConstructData().getPart(ConstructSlot.LEFT_ARM);
        if (leftArm.isPresent() && leftArm.get() instanceof ConstructPartAxeArmLeft) {
            Optional<ItemConstructPart> rightArm = this.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            if (rightArm.isPresent() && rightArm.get() instanceof ConstructPartAxeArmRight) {
                if (this.getIntelligence() >= 16) {
                    float damage = this.getConstructData().calculateDamage() * 3.0F;
                    this.f_21345_.addGoal(1, new LerpLeap<>(this, 10, 5, e -> {
                        if (this.m_5448_() == null || this.isAlliedTo(this.m_5448_())) {
                            return false;
                        } else if (this.isOnCooldown(0)) {
                            return false;
                        } else {
                            int yCount = 0;
                            for (BlockPos pos = this.m_20183_(); this.m_9236_().m_46859_(pos) && yCount < 5; pos = pos.above()) {
                                yCount++;
                            }
                            if (yCount < 3) {
                                return false;
                            } else {
                                double dist = (double) this.m_5448_().m_20270_(this);
                                return this.m_5448_() != null && this.m_5448_().isAlive() && dist >= 4.0 && dist <= 16.0 && this.m_21574_().hasLineOfSight(this.m_5448_());
                            }
                        }
                    }, evt -> {
                        boolean sendFlags = false;
                        switch(evt) {
                            case DAMAGE:
                                if (this.m_5448_() != null) {
                                    this.m_5448_().hurt(this.m_269291_().mobAttack(this), damage);
                                    CompoundTag meta = new CompoundTag();
                                    meta.putInt("entityID", this.m_5448_().m_19879_());
                                    ServerMessageDispatcher.sendParticleEffect(this.m_9236_().dimension(), 32.0F, this.m_20185_(), this.m_20186_(), this.m_20189_(), SpawnParticleEffectMessage.ParticleTypes.CONSTRUCT_AXE_SMASH, meta);
                                }
                                break;
                            case LAND:
                                this.actionFlags = Construct.ActionFlags.LEAP_LAND.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case LEAP:
                                this.actionFlags = Construct.ActionFlags.LEAP_AIRBORNE.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case START:
                                this.actionFlags = Construct.ActionFlags.LEAP_START.set(this.actionFlags);
                                sendFlags = true;
                                break;
                            case STOP:
                            default:
                                this.actionFlags = 0;
                                sendFlags = true;
                                this.setCooldown(0, (int) (500.0F * this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(ConstructCapability.CHOP_WOOD).getCooldownMultiplierFor(ConstructCapability.CHOP_WOOD)));
                        }
                        if (sendFlags && !this.m_9236_().isClientSide()) {
                            this.ANIM_PACKET = true;
                            ServerMessageDispatcher.sendEntityStateMessage(this);
                            this.ANIM_PACKET = false;
                        }
                    }));
                }
            }
        }
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        if (nbt.contains("anim")) {
            if (nbt.contains("hand")) {
                this.f_20912_ = InteractionHand.values()[nbt.getByte("hand")];
            }
            this.clearAnimCache = Construct.ActionFlags.ADVENTURE_ENTER.isFlagSet(this.actionFlags);
            this.actionFlags = nbt.getInt("actionFlags");
            this.defeatedCounter = nbt.getInt("defeatedCounter");
        } else {
            this.constructCapabilities.ReadNBT(nbt);
            this.recalculateAll();
        }
        if (nbt.contains("diag")) {
            this.constructDiagnostics.readFromNBT(nbt.getCompound("diag"));
        }
        if (nbt.contains("inv")) {
            for (int i = 0; i < 9; i++) {
                this.setStackInSlot(i, ItemStack.EMPTY);
            }
            ContainerHelper.loadAllItems(nbt.getCompound("inv"), this.inventory);
        }
        if (nbt.contains("hat")) {
            this.constructCapabilities.setHat(ItemStack.of(nbt.getCompound("hat")));
        }
        if (nbt.contains("banner")) {
            this.constructCapabilities.setBanner(ItemStack.of(nbt.getCompound("banner")));
        }
        this.moodlets.readFromNBT(nbt);
        if (ManaAndArtifice.instance.proxy.checkConstructDanceMixPlaying()) {
            this.setHappy(10000);
        }
        if (this.isAway()) {
            this.mine_leave_pfx = false;
            this.mine_return_pfx = false;
            this.adventure_leave_pfx = false;
            this.adventure_return_pfx = false;
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (!this.m_9236_().isClientSide() && this.getCurrentCommand() instanceof ConstructCommandFollowLodestar) {
            ((ConstructCommandFollowLodestar) this.getCurrentCommand()).releaseMutexes();
        }
    }

    @Nullable
    public Fluid getStoredFluid() {
        return this.__cachedFluid;
    }

    public void setStoredFluid(Fluid fluid) {
        if (fluid == null) {
            fluid = Fluids.EMPTY;
        }
        if (this.__cachedFluid != fluid) {
            this.f_19804_.set(FLUIDTYPE, ForgeRegistries.FLUIDS.getKey(fluid).toString());
            this.__cachedFluid = fluid;
        }
    }

    @Override
    public int getStoredFluidAmount() {
        return this.f_19804_.get(FLUIDAMT);
    }

    @Override
    public float getFluidPct() {
        int capacity = this.getTankCapacity(1);
        int amount = this.getStoredFluidAmount();
        return (float) amount / (float) capacity;
    }

    public void setStoredFluidAmount(int amount) {
        amount = Math.max(0, Math.min(amount, this.getTankCapacity(1)));
        this.f_19804_.set(FLUIDAMT, amount);
    }

    public boolean hasStoredFluid() {
        return this.__cachedFluid.isSame(Fluids.EMPTY);
    }

    @Override
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        return this.drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.__cachedFluid != null && !this.__cachedFluid.isSame(Fluids.EMPTY)) {
            int amountRemoved = maxDrain;
            int fluidAmount = this.getStoredFluidAmount();
            Fluid fluid = this.__cachedFluid;
            if (fluidAmount < maxDrain) {
                amountRemoved = fluidAmount;
            }
            fluidAmount -= amountRemoved;
            if (fluidAmount <= 0) {
                if (action == IFluidHandler.FluidAction.EXECUTE) {
                    this.setStoredFluidAmount(0);
                    this.setStoredFluid(Fluids.EMPTY);
                    this.needsSync = true;
                }
            } else if (action == IFluidHandler.FluidAction.EXECUTE) {
                this.setStoredFluidAmount(fluidAmount);
                this.needsSync = true;
            }
            return new FluidStack(fluid, amountRemoved);
        } else {
            return new FluidStack(Fluids.EMPTY, 0);
        }
    }

    @Override
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.__cachedFluid != null && !this.__cachedFluid.isSame(Fluids.EMPTY) && !this.__cachedFluid.isSame(resource.getFluid())) {
            return 0;
        } else {
            int amountAdded = resource.getAmount();
            int resultingAmount = this.getStoredFluidAmount();
            if (resultingAmount + amountAdded > this.getTankCapacity(1)) {
                amountAdded = this.getTankCapacity(1) - resultingAmount;
            }
            resultingAmount += amountAdded;
            if (action == IFluidHandler.FluidAction.EXECUTE) {
                this.setStoredFluid(resource.getFluid());
                this.setStoredFluidAmount(resultingAmount);
            }
            return amountAdded;
        }
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        Fluid fluid = this.getStoredFluid();
        return fluid != null ? new FluidStack(fluid, this.getStoredFluidAmount()) : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.f_19804_.get(FLUID_CAP);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.getFluidInTank(1).isEmpty() || this.getFluidInTank(1).getFluid().isSame(stack.getFluid());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        AnimationController<Construct> controller = new AnimationController<>(this, state -> this.handleAnimState(state));
        controller.setCustomInstructionKeyframeHandler(this);
        registrar.add(controller);
    }

    private PlayState handleAnimState(AnimationState<Construct> state) {
        if (!this.isAddedToWorld()) {
            return PlayState.STOP;
        } else {
            boolean swim = false;
            if (this.canSwim()) {
                BlockPos bp = this.m_20183_();
                FluidState flState = this.m_9236_().getFluidState(bp);
                FluidState flStateBelow = this.m_9236_().getFluidState(bp.below());
                if ((double) flState.getOwnHeight() > 0.8 || (double) flStateBelow.getOwnHeight() > 0.8) {
                    swim = true;
                }
            }
            if (Construct.ActionFlags.LEAP_START.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.leap").thenLoop("animation.construct.airborne_leaping"));
            } else if (Construct.ActionFlags.LEAP_AIRBORNE.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.airborne_leaping"));
            } else if (Construct.ActionFlags.LEAP_LAND.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.leap_land").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.DUAL_SWEEP.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.dual_sweep").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.DUAL_SHOOT.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.shoot_dual").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.DUAL_SPRAY.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.spray_both").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.DEFEATED.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.defeated_enter").thenLoop("animation.construct.defeated_loop"));
            } else if (Construct.ActionFlags.DEFEATED_END.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.defeated_exit"));
            } else if (Construct.ActionFlags.RANGED_ATTACK.isFlagSet(this.actionFlags)) {
                if (Construct.ActionFlags.SPRAY.isFlagSet(this.actionFlags)) {
                    return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.spray_right")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.spray_left"));
                } else if (Construct.ActionFlags.CAST_SPELL_SWING.isFlagSet(this.actionFlags)) {
                    return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_right").thenLoop("animation.construct.idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_left").thenLoop("animation.construct.idle"));
                } else if (Construct.ActionFlags.CAST_SPELL_CHANNEL.isFlagSet(this.actionFlags)) {
                    return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.channel_cast_right")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.channel_cast_left"));
                } else {
                    return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.shoot_right")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.shoot_left"));
                }
            } else if (Construct.ActionFlags.WATER.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.sprinkler"));
            } else if (Construct.ActionFlags.EAT.isFlagSet(this.actionFlags)) {
                return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.eat_right")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.eat_left"));
            } else if (Construct.ActionFlags.FISH.isFlagSet(this.actionFlags)) {
                return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_right").thenLoop("animation.construct.fish_loop_right")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_left").thenLoop("animation.construct.fish_loop_left"));
            } else if (Construct.ActionFlags.FISH_END.isFlagSet(this.actionFlags)) {
                return this.f_20912_ != null && this.f_20912_ != InteractionHand.MAIN_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.fish_end_right").thenLoop("animation.construct.idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.fish_end_left").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.MINE_ENTER.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.drill_enter").thenLoop("animation.construct.drill_loop"));
            } else if (Construct.ActionFlags.MINE_LEAVE.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.drill_leave"));
            } else if (Construct.ActionFlags.ADVENTURE_ENTER.isFlagSet(this.actionFlags)) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.adventure_leave").thenLoop("animation.construct.idle"));
            } else if (Construct.ActionFlags.ADVENTURE_LEAVE.isFlagSet(this.actionFlags)) {
                if (this.clearAnimCache) {
                    state.getController().forceAnimationReset();
                    this.clearAnimCache = false;
                }
                state.getController().setOverrideEasingType(EasingType.LINEAR);
                state.getController().transitionLength(1);
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.adventure_return"));
            } else if (this.f_20911_) {
                if (this.f_20912_ == InteractionHand.MAIN_HAND) {
                    return state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_left"));
                } else {
                    return this.f_20912_ == InteractionHand.OFF_HAND ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.swing_right")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.construct.dual_sweep"));
                }
            } else if (!this.m_20096_() && !this.canFly() && !this.m_20072_()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.airborne"));
            } else {
                double speed = new Vec3(this.m_20185_() - this.f_19790_, 0.0, this.m_20189_() - this.f_19792_).length();
                state.getController().transitionLength(5);
                if (this.canFly() && !this.m_20096_()) {
                    return speed > 0.1F && this.m_20184_().y < speed / 4.0 ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.fly_moving")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.fly_idle"));
                } else if (speed > 0.1F) {
                    if (swim) {
                        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.swim_loop"));
                    } else {
                        return this.getMoodlets().getStrongestMoodlet() == 8 ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.run_happy")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.run"));
                    }
                } else if (speed > 0.02F) {
                    return swim ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.swim_loop")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.walk"));
                } else if (this.dancing) {
                    if (this.danceTicks < 148) {
                        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.dance_1"));
                    } else if (this.danceTicks < 432) {
                        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.dance_2"));
                    } else {
                        switch(this.danceIndex) {
                            case 0:
                                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.dance_3"));
                            case 1:
                                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.dance_4"));
                            case 2:
                            default:
                                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.dance_5"));
                        }
                    }
                } else {
                    return swim ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.swim_idle")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.construct.idle"));
                }
            }
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<Construct> event) {
        String instructions = event.getKeyframeData().getInstructions();
        if (instructions.equals("hide")) {
            this.clearAnimCache = true;
            this.mine_leave_pfx = false;
            this.mine_return_pfx = false;
            this.adventure_leave_pfx = false;
            this.adventure_return_pfx = false;
        } else if (instructions.equals("show")) {
            this.clearAnimCache = false;
        } else if (instructions.equals("mine_enter_pfx_start")) {
            this.mine_leave_pfx = true;
            this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SFX.Spell.Impact.AoE.EARTH, this.m_5720_(), 1.0F, 1.0F);
        } else if (instructions.equals("drill_leave_sound")) {
            this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SFX.Spell.Impact.AoE.EARTH, this.m_5720_(), 1.0F, 1.0F);
        } else if (instructions.equals("mine_leave_pfx_start")) {
            this.mine_return_pfx = true;
        } else if (instructions.equals("mine_leave_pfx_stop")) {
            this.mine_return_pfx = false;
        } else if (instructions.equals("adventure_leave")) {
            this.adventure_leave_pfx = true;
            this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SFX.Spell.Impact.Single.WIND, this.m_5720_(), 1.0F, 1.0F);
            this.pfxCounter = 0;
        } else if (instructions.equals("adventure_return")) {
            this.adventure_return_pfx = true;
            this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SFX.Spell.Impact.Single.WIND, this.m_5720_(), 1.0F, 1.0F);
            this.pfxCounter = 0;
        } else if (instructions.equals("adventure_return_stop")) {
            this.m_9236_().playSound(ManaAndArtifice.instance.proxy.getClientPlayer(), this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SoundEvents.ARROW_HIT, this.m_5720_(), 1.0F, 1.0F);
            this.adventure_return_pfx = false;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnSmoothParticles(float partialTick) {
        if (Construct.ActionFlags.SPRAY.isFlagSet(this.actionFlags) && this.m_9236_().isClientSide() && this.getStoredFluid() != Fluids.EMPTY) {
            FluidParameterRegistry.FluidParameter param = FluidParameterRegistry.INSTANCE.forFluid(this.getStoredFluid());
            float angle = (float) (Math.toRadians(60.0) * Math.sin((double) partialTick * Math.PI));
            for (int i = 0; i < param.getParticleQuantity(); i++) {
                Vec3 look = this.m_20171_(this.m_146909_(), this.m_6080_());
                look = look.yRot(angle - (float) Math.toRadians(40.0));
                Vec3 particlePos = this.m_20182_().add(0.0, 1.0, 0.0).add(look);
                Vec3 vel = param.adjustVelocity(look);
                this.m_9236_().addParticle(param.particle(), particlePos.x(), particlePos.y(), particlePos.z(), vel.x, vel.y, vel.z);
            }
            if (this.expandFluidRange()) {
                angle = (float) (Math.toRadians(60.0) * -Math.sin((double) partialTick * Math.PI));
                for (int i = 0; i < param.getParticleQuantity(); i++) {
                    Vec3 look = this.m_20171_(this.m_146909_(), this.m_6080_());
                    look = look.yRot(angle + (float) Math.toRadians(40.0));
                    Vec3 particlePos = this.m_20182_().add(0.0, 1.0, 0.0).add(look);
                    Vec3 vel = param.adjustVelocity(look);
                    this.m_9236_().addParticle(param.particle(), particlePos.x(), particlePos.y(), particlePos.z(), vel.x, vel.y, vel.z);
                }
            }
        } else if (Construct.ActionFlags.WATER.isFlagSet(this.actionFlags)) {
            for (int i = 0; i < 20; i++) {
                this.m_9236_().addParticle(ParticleTypes.SPLASH, this.m_20182_().x, this.m_20182_().y + 0.9F + Math.random(), this.m_20182_().z, -0.5 + Math.random(), 0.0, -0.5 + Math.random());
            }
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot >= 0 && slot < this.inventory.size()) {
            ItemStack stack = this.getStackInSlot(slot);
            if (stack.isEmpty() || stack.getCount() < amount) {
                return ItemStack.EMPTY;
            } else if (!simulate) {
                return stack.split(amount);
            } else {
                ItemStack output = stack.copy();
                output.setCount(amount);
                return output;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.constructCapabilities != null ? this.constructCapabilities.calculateInventoryStackLimit() : 1;
    }

    @Override
    public int getSlots() {
        return this.constructCapabilities != null ? this.constructCapabilities.calculateInventorySize() : 0;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (slot >= 0 && slot < this.inventory.size()) {
            ItemStack existing = this.getStackInSlot(slot);
            if (existing.isEmpty()) {
                if (!simulate) {
                    this.inventory.set(slot, stack);
                }
                return ItemStack.EMPTY;
            } else if (existing.isDamaged()) {
                return stack;
            } else if (existing.hasTag() && !ManaAndArtificeMod.getItemHelper().AreTagsEqual(stack, existing)) {
                return stack;
            } else {
                int countToInsert = Math.min(stack.getCount(), existing.getMaxStackSize() - existing.getCount());
                ItemStack clone = stack.copy();
                clone.shrink(countToInsert);
                if (!simulate) {
                    existing.setCount(existing.getCount() + countToInsert);
                }
                return clone;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot >= 0 && slot < this.inventory.size();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
        }
    }

    @Override
    public void dropAllItems() {
        if (!this.m_21205_().isEmpty()) {
            this.m_19983_(this.m_21205_());
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (!this.m_21206_().isEmpty()) {
            this.m_19983_(this.m_21206_());
            this.m_21008_(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
        if (!this.getConstructData().getHat().isEmpty()) {
            this.setHat(ItemStack.EMPTY);
        }
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack invStack = this.getStackInSlot(i);
            if (!invStack.isEmpty()) {
                this.m_19983_(invStack);
                this.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void SpawnSmashParticles(Level world, Vec3 position) {
        for (int i = 0; i < 10; i++) {
            double offsetX = -0.5 + Math.random();
            double offsetZ = -0.5 + Math.random();
            world.addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.02F).setScale(0.1F), position.x + offsetX, position.y, position.z + offsetZ, -offsetX, 0.2 + 0.4 * Math.random(), -offsetZ);
        }
        for (int i = 0; i < 10; i++) {
            double offsetX = -0.5 + Math.random();
            double offsetZ = -0.5 + Math.random();
            world.addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.02F).setScale(0.1F), position.x + offsetX, position.y, position.z + offsetZ, -offsetX / 2.0, 0.2 + 0.4 * Math.random(), -offsetZ / 2.0);
        }
        for (int i = 0; i < 10; i++) {
            double offsetX = -0.5 + Math.random();
            double offsetZ = -0.5 + Math.random();
            world.addParticle(new MAParticleType(ParticleInit.DUST.get()).setGravity(0.02F).setScale(0.2F), position.x + offsetX, position.y, position.z + offsetZ, 0.0, 0.2 + 0.4 * Math.random(), 0.0);
        }
        world.playLocalSound(position.x, position.y, position.z, SFX.Spell.Impact.AoE.EARTH, SoundSource.PLAYERS, 1.0F, 0.9F + (float) Math.random() * 0.2F, false);
    }

    @OnlyIn(Dist.CLIENT)
    public static void SpawnCritParticles(Level world, Vec3 position, CompoundTag tag) {
        if (tag != null) {
            int entityID = tag.getInt("entityID");
            Entity e = world.getEntity(entityID);
            if (e != null) {
                Minecraft mc = Minecraft.getInstance();
                mc.particleEngine.createTrackingEmitter(e, ParticleTypes.CRIT);
            }
        }
    }

    static enum ActionFlags {

        RANGED_ATTACK(1),
        MINE_ENTER(2),
        MINE_LEAVE(4),
        CRUSH(8),
        SPRAY(16),
        WATER(32),
        FISH(64),
        FISH_END(128),
        EAT(256),
        ADVENTURE_ENTER(512),
        ADVENTURE_LEAVE(1024),
        AWAY(2048),
        CAST_SPELL_SWING(4096),
        CAST_SPELL_CHANNEL(8192),
        DEFEATED(16384),
        DEFEATED_END(32768),
        LEAP_START(65536),
        LEAP_AIRBORNE(131072),
        LEAP_LAND(262144),
        DUAL_SWEEP(524288),
        DUAL_SHOOT(1048576),
        DUAL_SPRAY(2097152);

        private final int flag;

        private ActionFlags(int flag) {
            this.flag = flag;
        }

        public boolean isFlagSet(int mutex) {
            return (mutex & this.flag) != 0;
        }

        int set(int actionFlags) {
            return actionFlags | this.flag;
        }

        int clear(int actionFlags) {
            return actionFlags & ~this.flag;
        }
    }

    class ConstructOpenDoors extends ConstructDoorInteractGoal {

        private final boolean closeDoor;

        public ConstructOpenDoors(Construct construct, boolean shouldClose) {
            super(construct);
            this.mob = construct;
            this.closeDoor = shouldClose;
        }

        @Override
        public boolean canContinueToUse() {
            return this.closeDoor && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.setOpen(true);
        }

        @Override
        public void stop() {
            this.setOpen(false);
        }

        @Override
        public void tick() {
            super.tick();
        }
    }

    class ConstructSwimGoal extends Goal {

        public ConstructSwimGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP));
            Construct.this.m_21573_().setCanFloat(true);
        }

        @Override
        public boolean canUse() {
            return !Construct.this.canSwim() ? false : Construct.this.m_20069_() && Construct.this.getFluidTypeHeight(ForgeMod.WATER_TYPE.get()) > Construct.this.m_20204_() || Construct.this.m_20077_();
        }

        @Override
        public void tick() {
            if (Construct.this.m_217043_().nextFloat() < 0.8F) {
                Construct.this.f_21343_.jump();
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}