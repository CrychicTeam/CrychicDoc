package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityDataRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.AllFluidsPathNavigator;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.DirectAquaticMoveControl;
import com.github.alexmodguy.alexscaves.server.entity.ai.LookAtLargeMobsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.TremorzillaAttackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.TremorzillaFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.TremorzillaWanderGoal;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.ActivatesSirens;
import com.github.alexmodguy.alexscaves.server.entity.util.KaijuMob;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import com.github.alexmodguy.alexscaves.server.entity.util.ShakesScreen;
import com.github.alexmodguy.alexscaves.server.entity.util.TremorzillaLegSolver;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACAdvancementTriggerRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.ITallWalker;
import com.google.common.base.Predicates;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class TremorzillaEntity extends DinosaurEntity implements KeybindUsingMount, IAnimatedEntity, ShakesScreen, KaijuMob, ActivatesSirens, ITallWalker {

    private static EntityDataAccessor<Optional<Vec3>> BEAM_END_POSITION = SynchedEntityData.defineId(TremorzillaEntity.class, ACEntityDataRegistry.OPTIONAL_VEC_3.get());

    private static final EntityDataAccessor<Boolean> SWIMMING = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CHARGE = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> MAX_BEAM_BREAK_LENGTH = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SPIKES_DOWN_PROGRESS = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> FIRING = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> TAME_ATTEMPTS = SynchedEntityData.defineId(TremorzillaEntity.class, EntityDataSerializers.INT);

    public static final Animation ANIMATION_SPEAK = Animation.create(20);

    public static final Animation ANIMATION_ROAR_1 = Animation.create(60);

    public static final Animation ANIMATION_ROAR_2 = Animation.create(60);

    public static final Animation ANIMATION_RIGHT_SCRATCH = Animation.create(35);

    public static final Animation ANIMATION_LEFT_SCRATCH = Animation.create(35);

    public static final Animation ANIMATION_RIGHT_TAIL = Animation.create(40);

    public static final Animation ANIMATION_LEFT_TAIL = Animation.create(40);

    public static final Animation ANIMATION_RIGHT_STOMP = Animation.create(35);

    public static final Animation ANIMATION_LEFT_STOMP = Animation.create(35);

    public static final Animation ANIMATION_BITE = Animation.create(25);

    public static final Animation ANIMATION_PREPARE_BREATH = Animation.create(20);

    public static final Animation ANIMATION_CHEW = Animation.create(35);

    private static final int MAX_CHARGE = 1000;

    private final TremorzillaPartEntity[] allParts;

    public final TremorzillaPartEntity tailPart1;

    public final TremorzillaPartEntity tailPart2;

    public final TremorzillaPartEntity tailPart3;

    public final TremorzillaPartEntity tailPart4;

    public final TremorzillaPartEntity tailPart5;

    private float[] yawBuffer = new float[128];

    private int yawPointer = -1;

    protected float tailXRot;

    protected float tailYRot;

    public TremorzillaLegSolver legSolver = new TremorzillaLegSolver(1.0F, 2.15F, 3.0F);

    private static final EntityDimensions SWIMMING_SIZE = new EntityDimensions(4.0F, 5.0F, true);

    private Animation currentAnimation;

    private int animationTick;

    private float lastYawBeforeWhip;

    protected boolean isLandNavigator;

    private double lastStompX = 0.0;

    private double lastStompZ = 0.0;

    private float prevScreenShakeAmount;

    private float screenShakeAmount;

    private float beamProgress;

    private float prevBeamProgress;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private int lastScareTimestamp;

    private int blockBreakCounter = 0;

    private int steamFromMouthFor = 0;

    private int roarCooldown = 0;

    public Vec3 beamServerTarget;

    public Vec3 prevClientBeamEndPosition;

    public Vec3 clientBeamEndPosition;

    public boolean wantsToUseBeamFromServer = false;

    private float prevClientSpikesDownAmount = 0.0F;

    private float clientSpikesDownAmount = 0.0F;

    private int beamTime = 0;

    private int maxBeamTime = 200;

    private int timeWithoutTarget = 0;

    public int timeSwimming;

    private boolean wasPreviouslyChild;

    private final Explosion dummyExplosion;

    private int chargeSoundCooldown = 0;

    private boolean makingBeamSoundOnClient = false;

    private Player lastFedPlayer = null;

    private int killCountFromBeam = 0;

    public TremorzillaEntity(EntityType entityType, Level level) {
        super(entityType, level);
        this.switchNavigator(true);
        this.tailPart1 = new TremorzillaPartEntity(this, this, 3.0F, 3.0F);
        this.tailPart2 = new TremorzillaPartEntity(this, this.tailPart1, 2.5F, 2.0F);
        this.tailPart3 = new TremorzillaPartEntity(this, this.tailPart2, 2.5F, 1.5F);
        this.tailPart4 = new TremorzillaPartEntity(this, this.tailPart3, 2.5F, 1.5F);
        this.tailPart5 = new TremorzillaPartEntity(this, this.tailPart4, 2.0F, 1.0F);
        this.allParts = new TremorzillaPartEntity[] { this.tailPart1, this.tailPart2, this.tailPart3, this.tailPart4, this.tailPart5 };
        this.m_21441_(BlockPathTypes.UNPASSABLE_RAIL, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_OTHER, 8.0F);
        this.m_21441_(BlockPathTypes.POWDER_SNOW, 8.0F);
        this.m_21441_(BlockPathTypes.LAVA, 0.0F);
        this.m_21441_(BlockPathTypes.DAMAGE_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.m_21441_(BlockPathTypes.WATER, 0.0F);
        this.m_21441_(BlockPathTypes.LEAVES, 0.0F);
        this.dummyExplosion = new Explosion(this.m_9236_(), null, this.m_20185_(), this.m_20186_(), this.m_20189_(), 10.0F, List.of());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AdvancedPathNavigateNoTeleport(this, level);
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21365_ = new LookControl(this);
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.createNavigation(this.m_9236_());
            this.isLandNavigator = true;
        } else {
            this.f_21365_ = new SmoothSwimmingLookControl(this, 10);
            this.f_21342_ = new DirectAquaticMoveControl(this, 0.8F, 40.0F);
            this.f_21344_ = new AllFluidsPathNavigator(this, this.m_9236_());
            this.isLandNavigator = false;
        }
    }

    @Override
    public int getMaxFallDistance() {
        return super.m_6056_() + 10;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(BEAM_END_POSITION, Optional.empty());
        this.f_19804_.define(SWIMMING, false);
        this.f_19804_.define(CHARGE, 1000);
        this.f_19804_.define(SPIKES_DOWN_PROGRESS, 0.0F);
        this.f_19804_.define(MAX_BEAM_BREAK_LENGTH, 100.0F);
        this.f_19804_.define(TAME_ATTEMPTS, 0);
        this.f_19804_.define(FIRING, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 500.0).add(Attributes.ARMOR, 10.0).add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ATTACK_DAMAGE, 30.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new TremorzillaAttackGoal(this));
        this.f_21345_.addGoal(2, new TremorzillaFollowOwnerGoal(this, 1.0, 14.0F, 6.0F));
        this.f_21345_.addGoal(3, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(4, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(5, new TemptGoal(this, 1.1, Ingredient.of(ACBlockRegistry.WASTE_DRUM.get(), ACBlockRegistry.NUCLEAR_BOMB.get()), false));
        this.f_21345_.addGoal(6, new TremorzillaWanderGoal(this));
        this.f_21345_.addGoal(7, new LookAtLargeMobsGoal(this, 3.0F, 30.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new OwnerHurtTargetGoal(this));
    }

    public boolean isFakeEntity() {
        return this.f_19803_;
    }

    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        this.legSolver.update(this, this.f_20883_, this.getScale());
        this.prevScreenShakeAmount = this.screenShakeAmount;
        this.prevBeamProgress = this.beamProgress;
        this.prevClientBeamEndPosition = this.clientBeamEndPosition;
        this.prevClientSpikesDownAmount = this.clientSpikesDownAmount;
        boolean water = this.isInFluidType();
        if (water && this.isLandNavigator) {
            this.switchNavigator(false);
        }
        if (!water && !this.isLandNavigator) {
            this.switchNavigator(true);
        }
        if (this.isTremorzillaSwimming()) {
            this.timeSwimming++;
            this.m_20301_(this.m_6062_());
        } else {
            this.timeSwimming = 0;
        }
        if (this.screenShakeAmount > 0.0F) {
            this.screenShakeAmount = Math.max(0.0F, this.screenShakeAmount - 0.34F);
        }
        if (this.isFiring() && this.beamProgress < 5.0F) {
            this.beamProgress++;
        }
        if (!this.isFiring() && this.beamProgress > 0.0F) {
            this.beamProgress--;
        }
        this.clientSpikesDownAmount = Mth.approach(this.clientSpikesDownAmount, this.getSpikesDownAmount(), 0.1F);
        Vec3 beamEnd = this.getBeamEndPosition();
        this.clientBeamEndPosition = beamEnd;
        if (this.isFiring()) {
            boolean flag = false;
            if (this.isFiring() && beamEnd != null) {
                Vec3 vec3 = beamEnd.subtract(this.getBeamShootFrom(1.0F));
                float beamYaw = -((float) Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float) Math.PI);
                if (Mth.degreesDifferenceAbs(beamYaw, Mth.wrapDegrees(this.f_20883_)) > 80.0F) {
                    flag = true;
                    this.m_146922_(Mth.approachDegrees(this.m_146908_(), beamYaw, 10.0F));
                    this.f_20883_ = Mth.approachDegrees(this.f_20884_, beamYaw, 10.0F);
                    this.lastYawBeforeWhip = beamYaw;
                }
            }
            if (!flag) {
                this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.lastYawBeforeWhip, 15.0F);
            }
        } else if (this.getAnimation() != ANIMATION_RIGHT_TAIL && this.getAnimation() != ANIMATION_LEFT_TAIL) {
            this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.m_146908_(), 4.0F);
            this.f_20885_ = Mth.approachDegrees(this.f_20886_, this.f_20885_, 2.0F);
            this.lastYawBeforeWhip = this.f_20883_;
        } else {
            float negative = this.getAnimation() == ANIMATION_RIGHT_TAIL ? -1.0F : 1.0F;
            float target = 0.0F;
            if (this.getAnimationTick() < 5) {
                float f = (float) this.getAnimationTick() / 5.0F;
                target = f * -10.0F;
            } else {
                float f = (float) (this.getAnimationTick() - 10) / 15.0F;
                target = Mth.clamp(f, 0.0F, 1.0F) * 170.0F;
            }
            if ((float) this.getAnimationTick() > 25.0F) {
                this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.lastYawBeforeWhip, 15.0F);
            } else {
                this.f_267362_.setSpeed(1.0F + AlexsCaves.PROXY.getPartialTicks());
                this.f_20883_ = Mth.approachDegrees(this.f_20884_, this.lastYawBeforeWhip + negative * target, 70.0F);
            }
        }
        if (this.screenShakeAmount > 0.0F) {
            this.screenShakeAmount = Math.max(0.0F, this.screenShakeAmount - 0.15F);
        }
        if (this.m_20096_() && !this.isInFluidType() && this.f_267362_.speed() > 0.1F && !this.m_6162_() && !this.m_21525_() && this.m_6084_()) {
            float f = (float) Math.cos((double) (this.f_267362_.position() * 0.25F - 1.5F));
            float f1 = (float) Math.cos((double) (this.f_267362_.position() * 0.25F - 1.0F));
            float f2 = (float) Math.sin((double) (this.f_267362_.position() * 0.25F - 1.0F));
            if (Math.abs(f) < 0.2F) {
                if ((double) this.screenShakeAmount <= 0.3) {
                    this.m_5496_(ACSoundRegistry.TREMORZILLA_STOMP.get(), 6.0F, 0.7F);
                }
                this.screenShakeAmount = 2.0F;
            }
            if (this.f_267362_.speed() > 0.5F && Math.abs(f1) < 0.1F) {
                this.stompEffect(f2 > 0.0F, 1.0F, 1.3F, 0.4F + this.f_267362_.speed(), 2.0F);
            }
        }
        this.tickMultipart();
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
            } else {
                this.m_20090_();
            }
            Player player = AlexsCaves.PROXY.getClientSidePlayer();
            if (player != null && player.m_20365_(this)) {
                if (AlexsCaves.PROXY.isKeyDown(2) && this.getMeterAmount() >= 1.0F) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 2));
                }
                if (AlexsCaves.PROXY.isKeyDown(3) && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 3));
                }
            }
            if (this.isFiring() && this.beamProgress > 0.0F && !this.makingBeamSoundOnClient) {
                AlexsCaves.PROXY.playWorldSound(this, (byte) 16);
                this.makingBeamSoundOnClient = true;
            }
            if (!this.isFiring() && this.makingBeamSoundOnClient) {
                AlexsCaves.PROXY.clearSoundCacheFor(this);
                this.makingBeamSoundOnClient = false;
            }
        } else {
            double waterHeight = this.getMaxFluidHeight();
            if (waterHeight > 0.0 && waterHeight < (double) (this.m_20206_() - 1.0F) && !this.f_19863_) {
                this.m_20256_(this.m_20184_().add(0.0, -0.02, 0.0));
            }
            this.setTremorzillaSwimming(waterHeight > 2.0);
        }
        if (this.m_6084_()) {
            if (this.isFiring()) {
                this.tickBreath();
            } else if (this.steamFromMouthFor > 0 && this.m_9236_().isClientSide) {
                this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.TREMORZILLA_STEAM.get(), true, this.m_20185_(), this.m_20188_(), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
            }
            if (!this.isFiring() && this.killCountFromBeam > 0) {
                if (this.killCountFromBeam > 20 && !this.m_9236_().isClientSide && this.m_20160_()) {
                    for (Entity passenger : this.m_20197_()) {
                        ACAdvancementTriggerRegistry.TREMORZILLA_KILL_BEAM.triggerForEntity(passenger);
                    }
                }
                this.killCountFromBeam = 0;
            }
            if ((this.getAnimation() == ANIMATION_RIGHT_SCRATCH || this.getAnimation() == ANIMATION_LEFT_SCRATCH) && this.getAnimationTick() == 18) {
                Vec3 center = new Vec3(0.0, (double) (5.0F * this.getScale()), (double) (6.0F * this.getScale())).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)).add(this.m_20182_());
                this.hurtEntitiesAround(center, 6.0F, 10.0F, 2.0F, false, true, true);
                if (!this.m_9236_().isClientSide) {
                    this.breakBlocksAround(center, 3.0F, false, false, 0.6F);
                }
            }
            if ((this.getAnimation() == ANIMATION_RIGHT_TAIL || this.getAnimation() == ANIMATION_LEFT_TAIL) && this.getAnimationTick() >= 10 && this.getAnimationTick() < 25) {
                this.hurtEntitiesAround(this.tailPart1.centeredPosition(), 4.0F, 10.0F, 2.0F, false, true, true);
                this.hurtEntitiesAround(this.tailPart2.centeredPosition(), 4.0F, 10.0F, 2.0F, false, true, true);
                this.hurtEntitiesAround(this.tailPart3.centeredPosition(), 4.0F, 10.0F, 2.0F, false, true, true);
                this.hurtEntitiesAround(this.tailPart4.centeredPosition(), 3.0F, 10.0F, 2.0F, false, true, true);
                this.hurtEntitiesAround(this.tailPart5.centeredPosition(), 3.0F, 10.0F, 2.0F, false, true, true);
                if (!this.m_9236_().isClientSide) {
                    this.breakBlocksAround(this.tailPart1.centeredPosition(), 2.0F, false, false, 0.6F);
                    this.breakBlocksAround(this.tailPart2.centeredPosition(), 2.0F, false, false, 0.6F);
                    this.breakBlocksAround(this.tailPart3.centeredPosition(), 2.0F, false, false, 0.6F);
                    this.breakBlocksAround(this.tailPart4.centeredPosition(), 1.0F, false, false, 0.6F);
                    this.breakBlocksAround(this.tailPart5.centeredPosition(), 1.0F, false, false, 0.6F);
                }
            }
            if ((this.getAnimation() == ANIMATION_LEFT_STOMP || this.getAnimation() == ANIMATION_RIGHT_STOMP) && this.getAnimationTick() == 18) {
                this.stompEffect(this.getAnimation() == ANIMATION_LEFT_STOMP, 2.0F, 5.0F, 1.2F, 10.0F);
                this.screenShakeAmount = 4.0F;
            }
            if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 10) {
                Vec3 center = new Vec3(0.0, (double) (7.0F * this.getScale()), (double) (5.0F * this.getScale())).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)).add(this.m_20182_());
                this.hurtEntitiesAround(center, 7.5F, 10.0F, 2.0F, false, true, true);
                if (!this.m_9236_().isClientSide) {
                    this.breakBlocksAround(center, 4.0F, false, false, 0.6F);
                }
            }
            if (this.getAnimation() == ANIMATION_ROAR_1 && this.getAnimationTick() > 10 && this.getAnimationTick() < 50 || this.getAnimation() == ANIMATION_ROAR_2 && this.getAnimationTick() > 15 && this.getAnimationTick() < 50) {
                this.screenShakeAmount = 8.0F;
                if (!this.m_9236_().isClientSide) {
                    this.scareMobs();
                }
            }
            if (this.getAnimation() == ANIMATION_SPEAK && this.getAnimationTick() == 5 && !this.isFiring()) {
                this.actuallyPlayAmbientSound();
            }
        }
        if (!this.m_9236_().isClientSide) {
            LivingEntity targetx = this.m_5448_();
            if (targetx != null && targetx.isAlive()) {
                this.timeWithoutTarget = 0;
            } else {
                this.timeWithoutTarget++;
            }
            if (this.wantsToUseBeamFromServer && (this.timeWithoutTarget > 100 && !this.m_20160_() || this.m_21825_())) {
                this.wantsToUseBeamFromServer = false;
            }
            if (!this.isFiring()) {
                if (this.wantsToUseBeamFromServer && this.isPowered()) {
                    float spikesThreshold = 0.95F;
                    if (this.getAnimation() == ANIMATION_PREPARE_BREATH && this.getSpikesDownAmount() >= spikesThreshold && this.getAnimationTick() > 15 && !this.isFiring()) {
                        this.maxBeamTime = 100 + this.f_19796_.nextInt(150);
                        this.beamServerTarget = this.createInitialBeamVec();
                        this.m_7618_(EntityAnchorArgument.Anchor.EYES, this.beamServerTarget);
                        this.setFiring(true);
                        this.setMaxBeamBreakLength(100.0F);
                    }
                    if (this.getSpikesDownAmount() >= spikesThreshold && this.getAnimation() == NO_ANIMATION && !this.isStunned()) {
                        this.setAnimation(ANIMATION_PREPARE_BREATH);
                        this.m_5496_(ACSoundRegistry.TREMORZILLA_BEAM_START.get(), 8.0F, 1.0F);
                    }
                    this.setSpikesDownAmount(Math.min(this.getSpikesDownAmount() + 0.005F, 1.0F));
                    if ((this.f_19797_ + this.m_19879_()) % 10 == 0 && this.m_9236_() instanceof ServerLevel serverLevel) {
                        this.getNearbySirens(serverLevel, 256).forEach(this::activateSiren);
                    }
                    float fx = calculateSpikesDownAmount(this.getSpikesDownAmount(), 6.0F);
                    if (Math.floor((double) (fx - 0.005F)) != Math.floor((double) fx) && this.chargeSoundCooldown <= 0 && fx <= 5.0F) {
                        float pitch = 0.7F + this.getSpikesDownAmount() * 0.7F;
                        this.m_5496_(fx > 4.0F ? ACSoundRegistry.TREMORZILLA_CHARGE_COMPLETE.get() : ACSoundRegistry.TREMORZILLA_CHARGE_NORMAL.get(), 8.0F, pitch);
                        this.chargeSoundCooldown = 19;
                    }
                    if (this.chargeSoundCooldown > 0) {
                        this.chargeSoundCooldown--;
                    }
                } else {
                    this.setSpikesDownAmount(Math.max(this.getSpikesDownAmount() - 0.05F, 0.0F));
                    if (this.m_20184_().horizontalDistance() < 0.05 && this.getAnimation() == NO_ANIMATION && !this.isDancing() && !this.m_21825_() && !this.m_21525_() && this.f_19796_.nextInt(800) == 0 && !this.m_20160_()) {
                        this.tryRoar();
                    }
                }
            } else {
                this.wantsToUseBeamFromServer = false;
                int iterateBy = 1;
                if (!this.m_20160_()) {
                    if (targetx != null && targetx.isAlive()) {
                        if (targetx.m_20270_(this) > 100.0F) {
                            iterateBy = 8;
                        }
                    } else {
                        iterateBy = 3;
                    }
                }
                this.beamTime += iterateBy;
                if (this.beamTime > this.maxBeamTime) {
                    this.beamTime = 0;
                    this.setFiring(false);
                    this.m_5496_(ACSoundRegistry.TREMORZILLA_BEAM_END.get(), 8.0F, 1.0F);
                    this.beamServerTarget = null;
                    this.setBeamEndPosition(null);
                    this.setCharge(0);
                } else {
                    if (!this.isStunned()) {
                        this.tickBeamTargeting();
                    }
                    this.setCharge(1000);
                }
            }
            if (!this.isPowered()) {
                this.setCharge(this.getCharge() + 1);
            }
            float healthAmount = this.m_21223_() / this.m_21233_();
            if (healthAmount <= 0.2F) {
                this.healEveryTick(10, 5.0F);
            } else if (healthAmount <= 0.5F) {
                this.healEveryTick(20, 3.0F);
            } else {
                this.healEveryTick(100, 2.0F);
            }
        }
        if (!this.isPowered()) {
            this.setSpikesDownAmount(0.0F);
        }
        if (this.steamFromMouthFor > 0) {
            this.steamFromMouthFor--;
        }
        if (this.roarCooldown > 0) {
            this.roarCooldown--;
        }
        if (this.wasPreviouslyChild != this.m_6162_()) {
            this.wasPreviouslyChild = this.m_6162_();
            this.m_6210_();
            for (TremorzillaPartEntity tremorzillaPartEntity : this.allParts) {
                tremorzillaPartEntity.m_6210_();
            }
        }
        if (this.m_21023_(ACEffectRegistry.IRRADIATED.get())) {
            MobEffectInstance instance = this.m_21124_(ACEffectRegistry.IRRADIATED.get());
            int level = instance == null ? 1 : 1 + instance.getAmplifier();
            this.m_5634_((float) (level * 12));
            this.m_21195_(ACEffectRegistry.IRRADIATED.get());
        }
        if (this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 2) {
            this.m_5496_(ACSoundRegistry.TREMORZILLA_BITE.get(), 4.0F, this.m_6100_());
        }
        if ((this.getAnimation() == ANIMATION_RIGHT_SCRATCH || this.getAnimation() == ANIMATION_LEFT_SCRATCH) && this.getAnimationTick() == 2) {
            this.m_5496_(ACSoundRegistry.TREMORZILLA_SCRATCH_ATTACK.get(), 4.0F, this.m_6100_());
        }
        if ((this.getAnimation() == ANIMATION_RIGHT_STOMP || this.getAnimation() == ANIMATION_LEFT_STOMP) && this.getAnimationTick() == 2) {
            this.m_5496_(ACSoundRegistry.TREMORZILLA_STOMP_ATTACK.get(), 4.0F, this.m_6100_());
        }
        if ((this.getAnimation() == ANIMATION_RIGHT_TAIL || this.getAnimation() == ANIMATION_LEFT_TAIL) && this.getAnimationTick() == 2) {
            this.m_5496_(ACSoundRegistry.TREMORZILLA_TAIL_ATTACK.get(), 4.0F, this.m_6100_());
        }
        if (this.getAnimation() == ANIMATION_CHEW && this.getAnimationTick() % 6 == 0 && this.getAnimationTick() <= 30) {
            this.m_5496_(ACSoundRegistry.TREMORZILLA_EAT.get(), 4.0F, this.m_6100_());
            if (this.m_9236_().isClientSide) {
                BlockParticleOption particleOption1 = new BlockParticleOption(ParticleTypes.BLOCK, ACBlockRegistry.BLOCK_OF_URANIUM.get().defaultBlockState());
                BlockParticleOption particleOption2 = new BlockParticleOption(ParticleTypes.BLOCK, ACBlockRegistry.WASTE_DRUM.get().defaultBlockState());
                for (int i = 0; i < 8; i++) {
                    Vec3 particlesPos = this.getBeamShootFrom(1.0F).add(new Vec3(this.f_19796_.nextBoolean() ? -0.8F : 0.8F, 2.0, (double) (2.5F + this.f_19796_.nextFloat())).scale((double) this.getScale()).xRot((float) Math.toRadians((double) (-this.m_146909_()))).yRot((float) Math.toRadians((double) (-this.m_6080_()))));
                    this.m_9236_().addAlwaysVisibleParticle(this.f_19796_.nextInt(3) == 0 ? particleOption2 : particleOption1, true, particlesPos.x, particlesPos.y, particlesPos.z, 0.0, 0.0, 0.0);
                }
            }
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == ANIMATION_CHEW && this.getAnimationTick() == 34 && this.lastFedPlayer != null) {
            if (!this.m_21824_()) {
                this.setTameAttempts(this.getTameAttempts() + 1);
                if (this.getTameAttempts() >= 4 && this.m_217043_().nextInt(3) == 0) {
                    this.m_21828_(this.lastFedPlayer);
                    this.m_147271_();
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                }
            }
            this.lastFedPlayer = null;
        }
        this.lastStompX = this.f_19854_;
        this.lastStompZ = this.f_19856_;
    }

    private double getMaxFluidHeight() {
        return this.getFluidTypeHeight(this.getMaxHeightFluidType());
    }

    private void healEveryTick(int i, float health) {
        if (this.f_19797_ % i == 0) {
            this.m_5634_(health);
        }
    }

    private void tickBeamTargeting() {
        LivingEntity target = this.m_5448_();
        Vec3 vec3 = this.beamServerTarget == null ? this.m_20182_() : this.beamServerTarget;
        Vec3 shootFrom = this.getBeamShootFrom(1.0F);
        if (this.m_20160_() && this.getControllingPassenger() instanceof Player player) {
            Vec3 riderPointing = player.m_20252_(1.0F).scale(100.0);
            Vec3 approach = shootFrom.add(riderPointing).subtract(vec3).scale(0.2F).add(vec3);
            this.beamServerTarget = approach;
        } else if (target != null && target.isAlive()) {
            float time = (float) this.beamTime / (float) this.maxBeamTime;
            float accuracy = 1.0F - Math.min(0.75F, time) / 0.75F;
            Vec3 position = target.m_20182_();
            Vec3 swingVec = new Vec3(Math.sin((double) ((float) this.f_19797_ * 0.2F)) * 6.0, 0.0, Math.cos((double) ((float) this.f_19797_ * 0.2F)) * -6.0).yRot((float) Math.toRadians((double) (-this.f_20883_))).scale((double) accuracy);
            Vec3 approach = position.add(swingVec).subtract(vec3).scale(0.1F).add(vec3);
            this.beamServerTarget = approach;
        } else {
            Vec3 newTarget = new Vec3(Math.sin((double) ((float) this.f_19797_ * 0.1F)) * 10.0, this.beamServerTarget.y - shootFrom.y, 6.0).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            Vec3 approach = shootFrom.add(newTarget).subtract(vec3).scale(0.1F).add(vec3);
            this.beamServerTarget = approach;
        }
    }

    private Vec3 createInitialBeamVec() {
        LivingEntity target = this.m_5448_();
        if (target != null && target.isAlive()) {
            Vec3 randomRot = new Vec3((double) (-100.0F + this.f_19796_.nextFloat() * 200.0F), 0.0, (double) (15.0F + 15.0F * this.f_19796_.nextFloat())).yRot((float) Math.toRadians((double) (-this.f_20883_ + 50.0F - this.f_19796_.nextFloat() * 100.0F)));
            Vec3 position = target instanceof KaijuMob ? target.m_146892_() : target.m_20182_();
            return position.add(randomRot);
        } else if (this.m_20160_()) {
            Vec3 vec3 = new Vec3(0.0, 0.0, 10.0).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            return this.getBeamShootFrom(1.0F).add(vec3);
        } else {
            Vec3 vec3 = new Vec3(0.0, this.f_19796_.nextBoolean() ? 100.0 : 20.0, 6.0).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            return this.getBeamShootFrom(1.0F).add(vec3);
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.getAnimation() != ANIMATION_LEFT_STOMP && this.getAnimation() != ANIMATION_RIGHT_STOMP && this.getAnimation() != ANIMATION_LEFT_TAIL && this.getAnimation() != ANIMATION_RIGHT_TAIL && (!this.isFiring() || this.m_20160_())) {
            if (this.isInFluidType() && (this.m_21515_() || this.m_20160_())) {
                this.m_19920_(this.m_6113_(), vec3d);
                Vec3 delta = this.m_20184_();
                this.m_6478_(MoverType.SELF, delta);
                if (this.f_19862_) {
                    delta = delta.add(0.0, 0.05, 0.0);
                }
                this.m_20256_(delta.scale(0.8));
                this.calculateEntityAnimation(false);
            } else {
                super.travel(vec3d);
            }
        } else {
            vec3d = Vec3.ZERO;
            super.travel(vec3d);
        }
    }

    @Override
    public int getHeadRotSpeed() {
        return 3;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.lastStompX, 0.0, this.m_20189_() - this.lastStompZ);
        float walkSpeed = 4.0F;
        if (this.m_20160_()) {
            walkSpeed = 1.5F;
        }
        float f2 = Math.min(f1 * walkSpeed, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            if (ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) && this.blockBreakCounter <= 0) {
                this.breakBlocksInBoundingBox(0.1F);
                this.blockBreakCounter = 10;
            }
            if (this.blockBreakCounter > 0) {
                this.blockBreakCounter--;
            }
        }
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION && !this.m_9236_().isClientSide && !this.isFiring()) {
            this.setAnimation(ANIMATION_SPEAK);
        }
    }

    public void actuallyPlayAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.m_5496_(soundevent, this.m_6121_(), this.m_6100_());
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
    }

    private void tickMultipart() {
        if (this.yawPointer == -1) {
            for (int i = 0; i < this.yawBuffer.length; i++) {
                this.yawBuffer[i] = this.f_20883_;
            }
        }
        if (++this.yawPointer == this.yawBuffer.length) {
            this.yawPointer = 0;
        }
        this.yawBuffer[this.yawPointer] = this.f_20883_;
        Vec3[] avector3d = new Vec3[this.allParts.length];
        for (int j = 0; j < this.allParts.length; j++) {
            avector3d[j] = new Vec3(this.allParts[j].m_20185_(), this.allParts[j].m_20186_(), this.allParts[j].m_20189_());
        }
        boolean tail = this.getAnimation() == ANIMATION_LEFT_TAIL || this.getAnimation() == ANIMATION_RIGHT_TAIL;
        float tailRotateSpeed = tail ? 25.0F : (this.isTremorzillaSwimming() ? 20.0F : 5.0F);
        this.tailXRot = this.wrapTailDegrees(Mth.approachDegrees(this.tailXRot, this.getTargetTailXRot(), tailRotateSpeed));
        this.tailYRot = this.wrapTailDegrees(Mth.approachDegrees(this.tailYRot, this.getTargetTailYRot(), tailRotateSpeed));
        Vec3 center = this.m_20182_().add(0.0, (double) (this.m_20206_() * 0.5F - this.getLegSolverBodyOffset()), 0.0);
        float tailXStep = this.tailXRot / 5.0F;
        float tailYStep = this.tailYRot / 5.0F;
        this.tailPart1.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, this.isTremorzillaSwimming() ? 0.0 : -4.0, -3.5).scale((double) this.getScale()), tailXStep, this.f_20883_ + tailYStep).add(center));
        this.tailPart2.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, -0.25, -3.25).scale((double) this.getScale()), tailXStep, this.f_20883_ + tailYStep * 2.0F).add(this.tailPart1.centeredPosition()));
        this.tailPart3.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, -2.5).scale((double) this.getScale()), tailXStep, this.f_20883_ + tailYStep * 3.0F).add(this.tailPart2.centeredPosition()));
        this.tailPart4.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, -2.5).scale((double) this.getScale()), tailXStep, this.f_20883_ + tailYStep * 4.0F).add(this.tailPart3.centeredPosition()));
        this.tailPart5.setPosCenteredY(this.rotateOffsetVec(new Vec3(0.0, 0.0, -2.0).scale((double) this.getScale()), tailXStep, this.f_20883_ + tailYStep * 5.0F).add(this.tailPart4.centeredPosition()));
        for (int l = 0; l < this.allParts.length; l++) {
            this.allParts[l].f_19854_ = avector3d[l].x;
            this.allParts[l].f_19855_ = avector3d[l].y;
            this.allParts[l].f_19856_ = avector3d[l].z;
            this.allParts[l].f_19790_ = avector3d[l].x;
            this.allParts[l].f_19791_ = avector3d[l].y;
            this.allParts[l].f_19792_ = avector3d[l].z;
        }
    }

    private float getTargetTailXRot() {
        if (this.getAnimation() != ANIMATION_LEFT_TAIL && this.getAnimation() != ANIMATION_RIGHT_TAIL) {
            return 0.0F;
        } else {
            return this.getAnimationTick() > 10 ? 45.0F : 0.0F;
        }
    }

    private float getTargetTailYRot() {
        float target = this.getYawFromBuffer(this.isTremorzillaSwimming() ? 2 : 20, 1.0F) - this.f_20883_;
        float swimAmount = this.m_20998_(1.0F);
        float swimAddition = (float) ((double) swimAmount * Math.sin((double) ((float) this.f_19797_ * 0.4F)) * 25.0);
        float swingAddition = (float) (Math.sin((double) ((float) this.f_19797_ * 0.03F)) * 10.0);
        if (this.m_21825_() && !this.isDancing()) {
            return target + 90.0F;
        } else if (this.getAnimation() != ANIMATION_LEFT_TAIL && this.getAnimation() != ANIMATION_RIGHT_TAIL) {
            return target + swimAddition + swingAddition;
        } else {
            return this.lastYawBeforeWhip - this.f_20883_ + (float) this.getAnimationTick() > 15.0F ? -70.0F : 70.0F;
        }
    }

    public float getLegSolverBodyOffset() {
        float swimAmount = this.m_20998_(1.0F);
        float heightBackLeft = this.legSolver.backLeft.getHeight(1.0F);
        float heightBackRight = this.legSolver.backRight.getHeight(1.0F);
        return Math.max(heightBackLeft, heightBackRight) * 0.8F * (1.0F - swimAmount);
    }

    protected Vec3 rotateOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(-xRot * (float) (Math.PI / 180.0)).yRot(-yRot * (float) (Math.PI / 180.0));
    }

    public boolean isStunned() {
        return this.m_21023_(ACEffectRegistry.STUNNED.get());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCharge(compound.getInt("Charge"));
        this.setSpikesDownAmount(compound.getFloat("SpikesDownAmount"));
        this.wantsToUseBeamFromServer = compound.getBoolean("ServerBeamTrigger");
        this.setTameAttempts(compound.getInt("TameAttempts"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Charge", this.getCharge());
        compound.putFloat("SpikesDownAmount", this.getSpikesDownAmount());
        compound.putBoolean("ServerBeamTrigger", this.wantsToUseBeamFromServer);
        compound.putInt("TameAttempts", this.getTameAttempts());
    }

    private void tickBreath() {
        if (this.m_9236_().isClientSide) {
            Vec3 endBeamPos = this.getClientBeamEndPosition(1.0F);
            if (endBeamPos != null) {
                Vec3 particleVec = endBeamPos.add((double) ((this.f_19796_.nextFloat() - 0.5F) * 3.0F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 3.0F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 3.0F));
                this.m_9236_().addAlwaysVisibleParticle(this.getAltSkin() == 2 ? ACParticleRegistry.TREMORZILLA_TECTONIC_EXPLOSION.get() : (this.getAltSkin() == 1 ? ACParticleRegistry.TREMORZILLA_RETRO_EXPLOSION.get() : ACParticleRegistry.TREMORZILLA_EXPLOSION.get()), true, particleVec.x, particleVec.y, particleVec.z, 0.0, 0.0, 0.0);
                this.m_9236_().addAlwaysVisibleParticle(this.getAltSkin() == 2 ? ACParticleRegistry.TREMORZILLA_TECTONIC_LIGHTNING.get() : (this.getAltSkin() == 1 ? ACParticleRegistry.TREMORZILLA_RETRO_LIGHTNING.get() : ACParticleRegistry.TREMORZILLA_LIGHTNING.get()), true, this.m_20185_(), this.m_20188_(), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
                if (this.m_217043_().nextFloat() < 0.3F) {
                    this.m_9236_().addAlwaysVisibleParticle(this.getAltSkin() == 2 ? ACParticleRegistry.TREMORZILLA_TECTONIC_PROTON.get() : (this.getAltSkin() == 1 ? ACParticleRegistry.TREMORZILLA_RETRO_PROTON.get() : ACParticleRegistry.TREMORZILLA_PROTON.get()), true, this.m_20185_(), this.m_20188_(), this.m_20189_(), (double) this.m_19879_(), 0.0, 0.0);
                }
            }
        } else {
            if (this.beamServerTarget != null) {
                Vec3 from = this.getBeamShootFrom(1.0F);
                Vec3 normalized = from.add(this.beamServerTarget.subtract(from).normalize().scale(100.0));
                this.setBeamEndPosition(this.m_9236_().m_45547_(new ClipContext(from, normalized, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).m_82450_());
            }
            Vec3 endBeamPos = this.getBeamEndPosition();
            boolean brokenClosestBlocks = false;
            float furthestBlockDist = 10.0F;
            if (endBeamPos != null && this.beamTime % 3 == 0) {
                Vec3 start = this.getBeamShootFrom(1.0F);
                Vec3 startClip = start;
                Vec3 viewVec = endBeamPos.subtract(start).normalize();
                float destructionScale = 5.0F;
                for (float walkThroughBeam = 1.0F; (double) walkThroughBeam < this.getMaxBeamBreakLength(); walkThroughBeam += destructionScale) {
                    startClip = startClip.add(viewVec.scale((double) (destructionScale * 1.5F)));
                    if (!brokenClosestBlocks) {
                        brokenClosestBlocks = this.breakBlocksAround(startClip, AlexsCaves.COMMON_CONFIG.devastatingTremorzillaBeam.get() ? destructionScale : destructionScale * 0.75F, false, true, 0.08F);
                        furthestBlockDist = (float) startClip.distanceTo(start);
                    }
                    this.hurtEntitiesAround(startClip, destructionScale + 1.0F, 20.0F, 1.0F, true, true, false);
                }
                this.hurtEntitiesAround(endBeamPos, 6.0F, 20.0F, 1.0F, true, true, false);
                if (AlexsCaves.COMMON_CONFIG.devastatingTremorzillaBeam.get() && this.beamTime % 6 == 0) {
                    this.breakBlocksAround(endBeamPos, 4.0F, false, true, 0.08F);
                }
            }
            if (brokenClosestBlocks) {
                this.setMaxBeamBreakLength((float) Math.max((double) furthestBlockDist, this.getMaxBeamBreakLength() - 5.0));
            }
        }
        this.steamFromMouthFor = 200;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.m_7350_(dataAccessor);
        if (SWIMMING.equals(dataAccessor)) {
            this.m_6210_();
        }
    }

    private void stompEffect(boolean left, float size, float hurtSize, float forwards, float damage) {
        float particleRadius = 0.3F + size * this.getScale();
        Vec3 center = this.m_20182_().add(new Vec3(left ? 2.2F : -2.2F, 0.0, (double) forwards).yRot(-this.f_20883_ * (float) (Math.PI / 180.0)));
        if (this.m_9236_().isClientSide) {
            for (int i = 0; i < 4; i++) {
                for (int i1 = 0; i1 < 10 + this.f_19796_.nextInt(10); i1++) {
                    double motionX = this.m_217043_().nextGaussian() * 0.07;
                    double motionY = 0.07 + this.m_217043_().nextGaussian() * 0.07;
                    double motionZ = this.m_217043_().nextGaussian() * 0.07;
                    float angle = (float) (Math.PI / 180.0) * this.f_20883_ + (float) i1;
                    double extraX = (double) (particleRadius * Mth.sin((float) (Math.PI + (double) angle)));
                    double extraY = 1.0;
                    double extraZ = (double) (particleRadius * Mth.cos(angle));
                    Vec3 groundedVec = ACMath.getGroundBelowPosition(this.m_9236_(), new Vec3((double) Mth.floor(center.x + extraX), (double) (Mth.floor(center.y + extraY) - 1), (double) Mth.floor(center.z + extraZ)));
                    BlockPos ground = BlockPos.containing(groundedVec.subtract(0.0, 0.5, 0.0));
                    BlockState state = this.m_9236_().getBlockState(ground);
                    if (state.m_280296_()) {
                        this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), true, center.x + extraX, (double) ground.m_123342_() + extraY, center.z + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }
        this.hurtEntitiesAround(center, particleRadius + hurtSize, damage, 0.5F, false, false, false);
    }

    public boolean hurtEntitiesAround(Vec3 center, float radius, float damageAmount, float knockbackAmount, boolean radioactive, boolean hurtsOtherKaiju, boolean stretchY) {
        AABB aabb = new AABB(center.subtract((double) radius, (double) radius, (double) radius), center.add((double) radius, (double) radius, (double) radius));
        if (stretchY) {
            aabb.setMinY(this.m_20186_() - 1.0);
            aabb.setMaxY(this.m_20188_() + 3.0);
        }
        boolean flag = false;
        DamageSource damageSource = radioactive ? ACDamageTypes.causeTremorzillaBeamDamage(this.m_9236_().registryAccess(), this) : this.m_269291_().mobAttack(this);
        for (LivingEntity living : this.m_9236_().m_6443_(LivingEntity.class, aabb, EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
            if (!living.m_7306_(this) && !this.m_7307_(living) && living.m_6095_() != this.m_6095_() && living.m_20275_(center.x, stretchY ? living.m_20186_() : center.y, center.z) <= (double) (radius * radius) && (!radioactive || this.canEntityBeHurtByBeam(living, center)) && (hurtsOtherKaiju || !(living instanceof KaijuMob)) && living.hurt(damageSource, damageAmount)) {
                flag = true;
                this.knockbackTarget(living, (double) knockbackAmount, this.m_20185_() - living.m_20185_(), this.m_20189_() - living.m_20189_(), !(living instanceof KaijuMob));
                if (radioactive) {
                    if (living.getHealth() <= 0.0F && living instanceof Enemy) {
                        this.killCountFromBeam++;
                    }
                    living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 6000, 2));
                }
            }
        }
        return flag;
    }

    private boolean canEntityBeHurtByBeam(LivingEntity living, Vec3 center) {
        return this.m_9236_().m_45547_(new ClipContext(center, living.m_20182_().add(0.0, (double) living.m_20206_() * 0.5, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    public void knockbackTarget(Entity target, double strength, double x, double z, boolean ignoreResistance) {
        LivingKnockBackEvent event = ForgeHooks.onLivingKnockBack(this, (float) strength, x, z);
        if (!event.isCanceled()) {
            strength = (double) event.getStrength();
            x = event.getRatioX();
            z = event.getRatioZ();
            if (!ignoreResistance) {
                strength *= 1.0 - this.m_21133_(Attributes.KNOCKBACK_RESISTANCE);
            }
            if (!(strength <= 0.0)) {
                this.f_19812_ = true;
                Vec3 vec3 = this.m_20184_();
                Vec3 vec31 = new Vec3(x, 0.0, z).normalize().scale(strength);
                target.setDeltaMovement(vec3.x / 2.0 - vec31.x, this.m_20096_() ? Math.min(0.4, vec3.y / 2.0 + strength) : vec3.y, vec3.z / 2.0 - vec31.z);
            }
        }
    }

    public boolean breakBlocksAround(Vec3 center, float radius, boolean square, boolean triggerExplosions, float dropChance) {
        if (!this.m_6162_() && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) && !this.m_9236_().isClientSide) {
            boolean flag = false;
            for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(center.x - (double) radius), Mth.floor(center.y - (double) radius), Mth.floor(center.z - (double) radius), Mth.floor(center.x + (double) radius), Mth.floor(center.y + (double) radius), Mth.floor(center.z + (double) radius))) {
                BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                boolean nuke = blockstate.m_60713_(ACBlockRegistry.NUCLEAR_BOMB.get());
                if (!blockstate.m_204336_(ACTagRegistry.NUKE_PROOF) && blockstate.m_280555_() && (blockstate.m_60734_().getExplosionResistance() <= 15.0F || nuke) && (square || blockpos.m_203198_(center.x, center.y, center.z) < (double) (radius * radius))) {
                    if (this.f_19796_.nextFloat() <= dropChance && !nuke) {
                        this.m_9236_().m_46961_(blockpos, true);
                    } else {
                        blockstate.onBlockExploded(this.m_9236_(), blockpos, this.dummyExplosion);
                    }
                    if (triggerExplosions && nuke) {
                        NuclearBombEntity bomb = ACEntityRegistry.NUCLEAR_BOMB.get().create(this.m_9236_());
                        bomb.m_6034_((double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_(), (double) blockpos.m_123343_() + 0.5);
                        bomb.setTime(300);
                        this.m_9236_().m_7967_(bomb);
                    }
                    flag = true;
                }
            }
            return flag;
        } else {
            return false;
        }
    }

    public boolean breakBlocksInBoundingBox(float dropChance) {
        if (!this.m_6162_() && ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) && !this.m_9236_().isClientSide) {
            boolean flag = false;
            AABB boundingBox = this.m_20191_().inflate(2.0);
            int swimUp = this.isTremorzillaSwimming() ? 3 : 1 - (int) this.getLegSolverBodyOffset();
            for (BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(boundingBox.minX), Mth.floor(boundingBox.minY + (double) swimUp), Mth.floor(boundingBox.minZ), Mth.floor(boundingBox.maxX), Mth.floor(boundingBox.maxY), Mth.floor(boundingBox.maxZ))) {
                BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                if (!blockstate.m_60795_() && !blockstate.m_204336_(ACTagRegistry.NUKE_PROOF) && !blockstate.m_60795_() && (blockstate.m_204336_(BlockTags.LEAVES) || blockpos.m_123342_() > this.m_146904_()) && blockstate.m_60734_().getExplosionResistance() <= 15.0F && (blockstate.m_60713_(Blocks.COBWEB) || !blockstate.m_60812_(this.m_9236_(), blockpos).isEmpty())) {
                    if (this.f_19796_.nextFloat() <= dropChance) {
                        this.m_9236_().m_46961_(blockpos, true);
                    } else {
                        this.m_9236_().setBlockAndUpdate(blockpos, Blocks.AIR.defaultBlockState());
                    }
                    flag = true;
                }
            }
            return flag;
        } else {
            return false;
        }
    }

    public void tryRoar() {
        if (this.roarCooldown == 0 && this.getAnimation() == NO_ANIMATION && !this.isFiring() && !this.isStunned() && !this.m_6162_()) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_ROAR_2 : ANIMATION_ROAR_1);
            this.m_5496_(ACSoundRegistry.TREMORZILLA_ROAR.get(), 8.0F, 1.0F);
            this.roarCooldown = 300 + this.f_19796_.nextInt(400);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return 60;
    }

    private float wrapTailDegrees(float f) {
        return f % 360.0F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    private void scareMobs() {
        if (this.f_19797_ - this.lastScareTimestamp > 5) {
            this.lastScareTimestamp = this.f_19797_;
        }
        for (LivingEntity e : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(64.0, 20.0, 64.0))) {
            if (!e.m_6095_().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR) && !this.m_7307_(e)) {
                if (e instanceof PathfinderMob) {
                    PathfinderMob mob = (PathfinderMob) e;
                    if (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose()) {
                        mob.m_6710_(null);
                        mob.m_6703_(null);
                        if (mob.m_20096_()) {
                            Vec3 randomShake = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), 0.0, (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.1F);
                            mob.m_20256_(mob.m_20184_().multiply(0.7F, 1.0, 0.7F).add(randomShake));
                        }
                        if (this.lastScareTimestamp == this.f_19797_) {
                            mob.m_21573_().stop();
                        }
                        if (mob.m_21573_().isDone()) {
                            Vec3 vec = LandRandomPos.getPosAway(mob, 30, 7, this.m_20182_());
                            if (vec != null) {
                                mob.m_21573_().moveTo(vec.x, vec.y, vec.z, 2.0);
                            }
                        }
                    }
                }
                if (this.m_21824_()) {
                    e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0, true, true));
                }
            }
        }
    }

    @Override
    public float getScreenShakeAmount(float partialTicks) {
        return this.m_6162_() ? 0.0F : this.prevScreenShakeAmount + (this.screenShakeAmount - this.prevScreenShakeAmount) * partialTicks;
    }

    @Override
    public double getShakeDistance() {
        return 64.0;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity entity) {
        if (!this.m_20365_(entity) && !entity.noPhysics && !this.f_19794_) {
            double d0 = entity.getX() - this.m_20185_();
            double d1 = entity.getZ() - this.m_20189_();
            double d2 = Mth.absMax(d0, d1);
            if (d2 >= 0.01F) {
                d2 = Math.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0 / d2;
                if (d3 > 1.0) {
                    d3 = 1.0;
                }
                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05F;
                d1 *= 0.05F;
                if (!entity.isVehicle() && (entity.isPushable() || entity instanceof KaijuMob)) {
                    entity.push(d0, 0.0, d1);
                }
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    public float getYawFromBuffer(int pointer, float partialTick) {
        int i = this.yawPointer - pointer & 127;
        int j = this.yawPointer - pointer - 1 & 127;
        float d0 = this.yawBuffer[j];
        float d1 = this.yawBuffer[i] - d0;
        return d0 + d1 * partialTick;
    }

    @Override
    public BlockState createEggBlockState() {
        return ACBlockRegistry.TREMORZILLA_EGG.get().defaultBlockState();
    }

    public boolean isMultipartEntity() {
        return true;
    }

    public PartEntity<?>[] getParts() {
        return this.allParts;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ACEntityRegistry.TREMORZILLA.get().create(serverLevel);
    }

    public static float calculateSpikesDownAmount(float progress, float spikeCount) {
        float scaledTo = progress * spikeCount;
        float remains = scaledTo % 1.0F;
        return (float) Mth.floor(scaledTo) + (float) Math.pow((double) remains, 5.0);
    }

    public static float calculateSpikesDownAmountAtIndex(float progress, float spikeCount, float spikeIndex) {
        return Mth.clamp(calculateSpikesDownAmount(progress, spikeCount) - spikeIndex, 0.0F, 1.0F);
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this)) {
            if (type == 2 && this.getMeterAmount() >= 1.0F && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null) && !this.wantsToUseBeamFromServer) {
                this.f_20883_ = keyPresser.getYHeadRot();
                this.m_146922_(keyPresser.getYHeadRot());
                this.wantsToUseBeamFromServer = true;
                this.maxBeamTime = 200;
            }
            if (type == 3 && (this.getAnimation() == NO_ANIMATION || this.getAnimation() == null)) {
                this.m_5616_(keyPresser.getYHeadRot());
                this.m_146926_(keyPresser.getXRot());
                float decision = this.m_217043_().nextFloat();
                if (decision < 0.33F) {
                    this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_LEFT_SCRATCH : ANIMATION_RIGHT_SCRATCH);
                } else if (decision < 0.66F && !this.m_6069_()) {
                    this.setAnimation(this.m_217043_().nextBoolean() ? ANIMATION_LEFT_STOMP : ANIMATION_RIGHT_STOMP);
                } else {
                    this.setAnimation(ANIMATION_BITE);
                }
            }
        }
    }

    @Override
    public float maxSitTicks() {
        return 20.0F;
    }

    private Stream<BlockPos> getNearbySirens(ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.NUCLEAR_SIREN.getKey()), Predicates.alwaysTrue(), this.m_20183_(), range, PoiManager.Occupancy.ANY);
    }

    private void activateSiren(BlockPos pos) {
        if (this.m_9236_().getBlockEntity(pos) instanceof NuclearSirenBlockEntity nuclearSirenBlock) {
            nuclearSirenBlock.setNearestNuclearBomb(this);
        }
    }

    @Override
    public boolean shouldStopBlaringSirens() {
        return !this.isPowered() || this.getSpikesDownAmount() <= 0.0F || this.m_213877_();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult prev = super.mobInteract(player, hand);
        if (prev != InteractionResult.SUCCESS) {
            ItemStack itemStack = player.m_21120_(hand);
            if (!this.m_21824_() && itemStack.is(ACBlockRegistry.WASTE_DRUM.get().asItem()) && this.getAnimation() == NO_ANIMATION) {
                this.m_142075_(player, hand, itemStack);
                this.setAnimation(ANIMATION_CHEW);
                this.lastFedPlayer = player;
                return InteractionResult.SUCCESS;
            }
        }
        return prev;
    }

    @Override
    public boolean canOwnerMount(Player player) {
        return !this.m_6162_();
    }

    @Override
    public boolean canOwnerCommand(Player ownerPlayer) {
        return ownerPlayer.m_6144_();
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity living) {
        return new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        this.m_6710_(null);
        if ((player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) && this.getAnimation() != ANIMATION_LEFT_TAIL && this.getAnimation() != ANIMATION_RIGHT_TAIL) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.m_5616_(player.m_6080_());
        }
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    public void setTameAttempts(int i) {
        this.f_19804_.set(TAME_ATTEMPTS, i);
    }

    public int getTameAttempts() {
        return this.f_19804_.get(TAME_ATTEMPTS);
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return true;
    }

    @Override
    protected float getBlockSpeedFactor() {
        return !this.isTremorzillaSwimming() && !this.m_6046_() ? super.m_6041_() : 1.0F;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
        if (this.isInFluidType()) {
            Vec3 lookVec = player.m_20154_();
            float y = (float) lookVec.y;
            return new Vec3((double) (player.f_20900_ * 0.25F), (double) y, (double) (player.f_20902_ * 0.8F * f));
        } else {
            return new Vec3((double) (player.f_20900_ * 0.35F), 0.0, (double) (player.f_20902_ * 0.8F * f));
        }
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            float swimAmount = this.m_20998_(1.0F);
            float walkSwing = (float) (Math.cos((double) (this.f_267362_.position() * 0.25F + 1.0F)) * 0.75 * (double) this.f_267362_.speed() - (double) (1.5F * this.f_267362_.speed())) * (1.0F - swimAmount);
            float animationExtraBack = 0.0F;
            if (this.getAnimation() == ANIMATION_ROAR_2) {
                animationExtraBack = 4.0F * ACMath.cullAnimationTick(this.getAnimationTick(), 1.0F, this.getAnimation(), 1.0F, 10, 60);
            }
            if (this.getAnimation() == ANIMATION_PREPARE_BREATH) {
                animationExtraBack = 4.0F * ACMath.cullAnimationTick(this.getAnimationTick(), 1.0F, this.getAnimation(), 1.0F, 0, 20);
            }
            Vec3 seatOffset = new Vec3(0.0, 2.0 - 6.5 * (double) swimAmount, (double) (1.0F + 6.0F * swimAmount - walkSwing - animationExtraBack)).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            passenger.setYBodyRot(this.f_20883_);
            passenger.fallDistance = 0.0F;
            if (!this.isFiring()) {
                this.clampRotation(living, 105.0F);
            }
            float heightBackLeft = this.legSolver.legs[0].getHeight(1.0F);
            float heightBackRight = this.legSolver.legs[1].getHeight(1.0F);
            float maxLegSolverHeight = (1.0F - ACMath.smin(1.0F - heightBackLeft, 1.0F - heightBackRight, 0.1F)) * 0.8F * (1.0F - swimAmount);
            moveFunction.accept(passenger, this.m_20185_() + seatOffset.x, this.m_20186_() + seatOffset.y + this.getPassengersRidingOffset() - (double) maxLegSolverHeight, this.m_20189_() + seatOffset.z);
            return;
        }
        super.m_19956_(passenger, moveFunction);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 8.25 * (double) this.getScale();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(6.0);
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
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.TREMORZILLA_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.TREMORZILLA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.TREMORZILLA_DEATH.get();
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_SPEAK, ANIMATION_ROAR_1, ANIMATION_ROAR_2, ANIMATION_RIGHT_SCRATCH, ANIMATION_LEFT_SCRATCH, ANIMATION_RIGHT_TAIL, ANIMATION_LEFT_TAIL, ANIMATION_RIGHT_STOMP, ANIMATION_LEFT_STOMP, ANIMATION_BITE, ANIMATION_PREPARE_BREATH, ANIMATION_CHEW };
    }

    @Override
    public boolean isVisuallySwimming() {
        return this.isTremorzillaSwimming();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.isTremorzillaSwimming() ? SWIMMING_SIZE.scale(this.getScale()) : super.m_6972_(poseIn);
    }

    public boolean isTremorzillaSwimming() {
        return this.f_19804_.get(SWIMMING);
    }

    public void setTremorzillaSwimming(boolean bool) {
        this.f_19804_.set(SWIMMING, bool);
    }

    public float getSpikesDownAmount() {
        return this.f_19804_.get(SPIKES_DOWN_PROGRESS);
    }

    public void setSpikesDownAmount(float spikesDownProgress) {
        this.f_19804_.set(SPIKES_DOWN_PROGRESS, spikesDownProgress);
    }

    public float getClientSpikeDownAmount(float partialTicks) {
        return this.prevClientSpikesDownAmount + (this.clientSpikesDownAmount - this.prevClientSpikesDownAmount) * partialTicks;
    }

    public boolean isFiring() {
        return this.f_19804_.get(FIRING);
    }

    public void setFiring(boolean firing) {
        this.f_19804_.set(FIRING, firing);
    }

    public float getBeamProgress(float partialTicks) {
        return (this.prevBeamProgress + (this.beamProgress - this.prevBeamProgress) * partialTicks) * 0.2F;
    }

    public int getCharge() {
        return this.f_19804_.get(CHARGE);
    }

    public void setCharge(int charge) {
        this.f_19804_.set(CHARGE, charge);
    }

    public boolean isPowered() {
        return this.getCharge() >= 1000;
    }

    @Nullable
    public Vec3 getBeamEndPosition() {
        return (Vec3) this.f_19804_.get(BEAM_END_POSITION).orElse(null);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.35F;
        }
        return super.m_6469_(source, amount);
    }

    @Override
    public boolean hasRidingMeter() {
        return true;
    }

    @Override
    public float getMeterAmount() {
        return (float) this.getCharge() / 1000.0F;
    }

    public void setBeamEndPosition(@Nullable Vec3 vec3) {
        this.f_19804_.set(BEAM_END_POSITION, Optional.ofNullable(vec3));
    }

    @Nullable
    public Vec3 getClientBeamEndPosition(float partialTicks) {
        return this.clientBeamEndPosition != null && this.prevClientBeamEndPosition != null ? this.prevClientBeamEndPosition.add(this.clientBeamEndPosition.subtract(this.prevClientBeamEndPosition).scale((double) partialTicks)) : null;
    }

    @Override
    public int getExperienceReward() {
        return 70;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ACBlockRegistry.NUCLEAR_BOMB.get().asItem());
    }

    public Vec3 getBodyRotViewVector(float partialTicks) {
        return this.m_20171_(this.m_5686_(partialTicks), this.f_20884_ + (this.f_20883_ - this.f_20884_) * partialTicks);
    }

    public void setMaxBeamBreakLength(float f) {
        this.f_19804_.set(MAX_BEAM_BREAK_LENGTH, f);
    }

    public double getMaxBeamBreakLength() {
        return (double) this.f_19804_.get(MAX_BEAM_BREAK_LENGTH).floatValue();
    }

    public float getStepHeight() {
        return 1.6F;
    }

    public Vec3 getBeamShootFrom(float partialTicks) {
        return this.m_20318_(partialTicks).add(0.0, (double) (7.5F * this.getScale()), 0.0);
    }

    @Override
    public int getMaxNavigableDistanceToGround() {
        return 4;
    }

    @Override
    public float getScale() {
        return this.m_6162_() ? 0.15F : 1.0F;
    }

    @Override
    public BlockState createEggBeddingBlockState() {
        return ACBlockRegistry.UNREFINED_WASTE.get().defaultBlockState();
    }
}