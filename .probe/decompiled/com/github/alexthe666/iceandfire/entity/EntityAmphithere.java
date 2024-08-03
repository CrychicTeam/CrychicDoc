package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.AmphithereAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.AmphithereAIFleePlayer;
import com.github.alexthe666.iceandfire.entity.ai.AmphithereAIFollowOwner;
import com.github.alexthe666.iceandfire.entity.ai.AmphithereAIHurtByTarget;
import com.github.alexthe666.iceandfire.entity.ai.AmphithereAITargetItems;
import com.github.alexthe666.iceandfire.entity.ai.EntityAIWatchClosestIgnoreRider;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.entity.util.IDragonFlute;
import com.github.alexthe666.iceandfire.entity.util.IFlapable;
import com.github.alexthe666.iceandfire.entity.util.IFlyingMount;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IPhasesThroughBlock;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.github.alexthe666.iceandfire.pathfinding.PathNavigateFlyingCreature;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityAmphithere extends TamableAnimal implements ISyncMount, IAnimatedEntity, IPhasesThroughBlock, IFlapable, IDragonFlute, IFlyingMount, IHasCustomizableAttributes, ICustomMoveController {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> FLAP_TICKS = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Byte> CONTROL_STATE = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityAmphithere.class, EntityDataSerializers.INT);

    public static Animation ANIMATION_BITE = Animation.create(15);

    public static Animation ANIMATION_BITE_RIDER = Animation.create(15);

    public static Animation ANIMATION_WING_BLAST = Animation.create(30);

    public static Animation ANIMATION_TAIL_WHIP = Animation.create(30);

    public static Animation ANIMATION_SPEAK = Animation.create(10);

    public float flapProgress;

    public float groundProgress = 0.0F;

    public float sitProgress = 0.0F;

    public float diveProgress = 0.0F;

    public IFChainBuffer roll_buffer;

    public IFChainBuffer tail_buffer;

    public IFChainBuffer pitch_buffer;

    @Nullable
    public BlockPos orbitPos = null;

    public float orbitRadius = 0.0F;

    public boolean isFallen;

    public BlockPos homePos;

    public boolean hasHomePosition = false;

    protected EntityAmphithere.FlightBehavior flightBehavior = EntityAmphithere.FlightBehavior.WANDER;

    protected int ticksCircling = 0;

    private int animationTick;

    private Animation currentAnimation;

    private int flapTicks = 0;

    private int flightCooldown = 0;

    private int ticksFlying = 0;

    private boolean isFlying;

    private boolean changedFlightBehavior = false;

    private int ticksStill = 0;

    private int ridingTime = 0;

    private boolean isSitting;

    private int navigatorType = 0;

    public EntityAmphithere(EntityType<EntityAmphithere> type, Level worldIn) {
        super(type, worldIn);
        if (worldIn.isClientSide) {
            this.roll_buffer = new IFChainBuffer();
            this.pitch_buffer = new IFChainBuffer();
            this.tail_buffer = new IFChainBuffer();
        }
        this.m_274367_(1.0F);
        this.switchNavigator(0);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, int x, int z, RandomSource rand) {
        BlockPos pos = new BlockPos(x, entity.getBlockY(), z);
        for (int yDown = 0; yDown < 6 + rand.nextInt(6); yDown++) {
            if (!world.m_46859_(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static boolean canAmphithereSpawnOn(EntityType<EntityAmphithere> parrotIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos p_223317_3_, RandomSource random) {
        BlockState blockState = worldIn.m_8055_(p_223317_3_.below());
        Block block = blockState.m_60734_();
        return blockState.m_204336_(BlockTags.LEAVES) || block == Blocks.GRASS_BLOCK || blockState.m_204336_(BlockTags.LOGS) || block == Blocks.AIR;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        if (worldIn.m_45784_(this) && !worldIn.containsAnyLiquid(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            if (blockpos.m_123342_() < worldIn.getSeaLevel()) {
                return false;
            } else {
                BlockState blockstate = worldIn.m_8055_(blockpos.below());
                return blockstate.m_60713_(Blocks.GRASS_BLOCK) || blockstate.m_204336_(BlockTags.LEAVES);
            }
        } else {
            return false;
        }
    }

    public static BlockPos getPositionInOrbit(EntityAmphithere entity, Level world, BlockPos orbit, RandomSource rand) {
        float possibleOrbitRadius = entity.orbitRadius + 10.0F;
        float radius = 10.0F;
        if (entity.getCommand() == 2) {
            if (entity.m_269323_() != null) {
                orbit = entity.m_269323_().m_20183_().above(7);
                radius = 5.0F;
            }
        } else if (entity.hasHomePosition) {
            orbit = entity.homePos.above(30);
            radius = 30.0F;
        }
        float angle = (float) (Math.PI / 180.0) * possibleOrbitRadius;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        BlockPos radialPos = BlockPos.containing((double) orbit.m_123341_() + extraX, (double) orbit.m_123342_(), (double) orbit.m_123343_() + extraZ);
        entity.orbitRadius = possibleOrbitRadius;
        return radialPos;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos) {
        if (this.isFlying()) {
            return this.m_9236_().m_46859_(pos) ? 10.0F : 0.0F;
        } else {
            return super.m_21692_(pos);
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack != null && itemstack.is(IafItemTags.BREED_AMPITHERE)) {
            if (this.m_146764_() == 0 && !this.m_27593_()) {
                this.setOrderedToSit(false);
                this.m_27595_(player);
                this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
            }
            return InteractionResult.SUCCESS;
        } else if (itemstack != null && itemstack.is(IafItemTags.HEAL_AMPITHERE) && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(5.0F);
            this.m_5496_(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            if (!player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        } else {
            if (super.m_6071_(player, hand) == InteractionResult.PASS) {
                if (itemstack != null && itemstack.getItem() == IafItemRegistry.DRAGON_STAFF.get() && this.m_21830_(player)) {
                    if (player.m_6144_()) {
                        BlockPos pos = this.m_20183_();
                        this.homePos = pos;
                        this.hasHomePosition = true;
                        player.displayClientMessage(Component.translatable("amphithere.command.new_home", this.homePos.m_123341_(), this.homePos.m_123342_(), this.homePos.m_123343_()), true);
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }
                if (player.m_6144_() && this.m_21830_(player)) {
                    if (player.m_21120_(hand).isEmpty()) {
                        this.setCommand(this.getCommand() + 1);
                        if (this.getCommand() > 2) {
                            this.setCommand(0);
                        }
                        player.displayClientMessage(Component.translatable("amphithere.command." + this.getCommand()), true);
                        this.m_5496_(SoundEvents.ZOMBIE_INFECT, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                    return InteractionResult.SUCCESS;
                }
                if ((!this.m_21824_() || this.m_21830_(player)) && !this.m_6162_() && itemstack.isEmpty()) {
                    player.m_20329_(this);
                    return InteractionResult.SUCCESS;
                }
            }
            return super.m_6071_(player, hand);
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AmphithereAIAttackMelee(this, 1.0, true));
        this.f_21345_.addGoal(2, new AmphithereAIFollowOwner(this, 1.0, 10.0F, 2.0F));
        this.f_21345_.addGoal(3, new AmphithereAIFleePlayer(this, 32.0F, 0.8, 1.8));
        this.f_21345_.addGoal(3, new EntityAmphithere.AIFlyWander());
        this.f_21345_.addGoal(3, new EntityAmphithere.AIFlyCircle());
        this.f_21345_.addGoal(3, new EntityAmphithere.AILandWander(this, 1.0));
        this.f_21345_.addGoal(4, new EntityAIWatchClosestIgnoreRider(this, LivingEntity.class, 6.0F));
        this.f_21345_.addGoal(4, new BreedGoal(this, 1.0));
        this.f_21346_.addGoal(1, new OwnerHurtTargetGoal(this));
        this.f_21346_.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new AmphithereAIHurtByTarget(this, false, new Class[0]));
        this.f_21346_.addGoal(3, new AmphithereAITargetItems(this, false));
    }

    public boolean isStill() {
        return Math.abs(this.m_20184_().x) < 0.05 && Math.abs(this.m_20184_().z) < 0.05;
    }

    protected void switchNavigator(int navigatorType) {
        if (navigatorType == 0) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new WallClimberNavigation(this, this.m_9236_());
            this.navigatorType = 0;
        } else if (navigatorType == 1) {
            this.f_21342_ = new EntityAmphithere.FlyMoveHelper(this);
            this.f_21344_ = new PathNavigateFlyingCreature(this, this.m_9236_());
            this.navigatorType = 1;
        } else {
            this.f_21342_ = new FlyingMoveControl(this, 20, false);
            this.f_21344_ = new PathNavigateFlyingCreature(this, this.m_9236_());
            this.navigatorType = 2;
        }
    }

    public boolean onLeaves() {
        BlockState state = this.m_9236_().getBlockState(this.m_20183_().below());
        return state.m_60734_() instanceof LeavesBlock;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float damage) {
        if (!this.m_21824_() && this.isFlying() && !this.m_20096_() && source.is(DamageTypeTags.IS_PROJECTILE) && !this.m_9236_().isClientSide) {
            this.isFallen = true;
        }
        return source.getEntity() instanceof LivingEntity && source.getEntity().isPassengerOfSameVehicle(this) && this.m_21824_() && this.m_21830_((LivingEntity) source.getEntity()) ? false : super.m_6469_(source, damage);
    }

    @Override
    public void positionRider(@NotNull Entity passenger, @NotNull Entity.MoveFunction callback) {
        super.m_19956_(passenger, callback);
        if (this.m_20363_(passenger) && this.m_21824_()) {
            this.m_5618_(passenger.getYRot());
            this.m_5616_(passenger.getYHeadRot());
        }
        if (!this.m_9236_().isClientSide && !this.m_21824_() && passenger instanceof Player && this.getAnimation() == NO_ANIMATION && this.f_19796_.nextInt(15) == 0) {
            this.setAnimation(ANIMATION_BITE_RIDER);
        }
        if (!this.m_9236_().isClientSide && this.getAnimation() == ANIMATION_BITE_RIDER && this.getAnimationTick() == 6 && !this.m_21824_()) {
            passenger.hurt(this.m_9236_().damageSources().mobAttack(this), 1.0F);
        }
        float pitch_forward = 0.0F;
        if (this.m_146909_() > 0.0F && this.isFlying()) {
            pitch_forward = this.m_146909_() / 45.0F * 0.45F;
        } else {
            pitch_forward = 0.0F;
        }
        float scaled_ground = this.groundProgress * 0.1F;
        float radius = (this.m_21824_() ? 0.5F : 0.3F) - scaled_ground * 0.5F + pitch_forward;
        float angle = (float) (Math.PI / 180.0) * this.f_20883_;
        double extraX = (double) (radius * Mth.sin((float) (Math.PI + (double) angle)));
        double extraZ = (double) (radius * Mth.cos(angle));
        passenger.setPos(this.m_20185_() + extraX, this.m_20186_() + 0.7F - (double) (scaled_ground * 0.14F) + (double) pitch_forward, this.m_20189_() + extraZ);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(IafItemTags.BREED_AMPITHERE);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.m_9236_().m_46791_() == Difficulty.PEACEFUL && this.m_5448_() instanceof Player) {
            this.m_6710_(null);
        }
        if (this.m_20069_() && this.f_20899_) {
            this.m_20334_(this.m_20184_().x, this.m_20184_().y + 0.1, this.m_20184_().z);
        }
        if (this.m_6162_() && this.m_5448_() != null) {
            this.m_6710_(null);
        }
        if (this.m_27593_()) {
            this.setFlying(false);
        }
        if (this.isOrderedToSit() && this.m_5448_() != null) {
            this.m_6710_(null);
        }
        boolean flapping = this.isFlapping();
        boolean flying = this.isFlying() && this.isOverAir() || this.isOverAir() && !this.onLeaves();
        boolean diving = flying && this.m_20184_().y <= -0.1F || this.isFallen;
        boolean sitting = this.isOrderedToSit() && !this.isFlying();
        boolean notGrounded = flying || this.getAnimation() == ANIMATION_WING_BLAST;
        if (!this.m_9236_().isClientSide) {
            if (this.isOrderedToSit() && (this.getCommand() != 1 || this.getControllingPassenger() != null)) {
                this.setOrderedToSit(false);
            }
            if (!this.isOrderedToSit() && this.getCommand() == 1 && this.getControllingPassenger() == null) {
                this.setOrderedToSit(true);
            }
            if (this.isOrderedToSit()) {
                this.m_21573_().stop();
            }
            if (flying) {
                this.ticksFlying++;
            } else {
                this.ticksFlying = 0;
            }
        }
        if (this.isFlying() && this.m_20096_()) {
            this.setFlying(false);
        }
        if (sitting && this.sitProgress < 20.0F) {
            this.sitProgress += 0.5F;
        } else if (!sitting && this.sitProgress > 0.0F) {
            this.sitProgress -= 0.5F;
        }
        if (this.flightCooldown > 0) {
            this.flightCooldown--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.flightBehavior == EntityAmphithere.FlightBehavior.CIRCLE) {
                this.ticksCircling++;
            } else {
                this.ticksCircling = 0;
            }
        }
        if (this.getUntamedRider() != null && !this.m_21824_()) {
            this.ridingTime++;
        }
        if (this.getUntamedRider() == null) {
            this.ridingTime = 0;
        }
        if (!this.m_21824_() && this.ridingTime > IafConfig.amphithereTameTime && this.getUntamedRider() != null && this.getUntamedRider() instanceof Player) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 45);
            this.m_21828_((Player) this.getUntamedRider());
            if (this.m_5448_() == this.getUntamedRider()) {
                this.m_6710_(null);
            }
        }
        if (this.isStill()) {
            this.ticksStill++;
        } else {
            this.ticksStill = 0;
        }
        if (!this.isFlying() && !this.m_6162_() && (this.m_20096_() && this.f_19796_.nextInt(200) == 0 && this.flightCooldown == 0 && this.m_20197_().isEmpty() && !this.m_21525_() && this.canMove() || this.m_20186_() < -1.0)) {
            this.m_20334_(this.m_20184_().x, this.m_20184_().y + 0.5, this.m_20184_().z);
            this.setFlying(true);
        }
        if (this.getControllingPassenger() != null && this.isFlying() && !this.m_20096_()) {
            if (this.getControllingPassenger().m_146909_() > 25.0F && this.m_20184_().y > -1.0) {
                this.m_20334_(this.m_20184_().x, this.m_20184_().y - 0.1, this.m_20184_().z);
            }
            if (this.getControllingPassenger().m_146909_() < -25.0F && this.m_20184_().y < 1.0) {
                this.m_20334_(this.m_20184_().x, this.m_20184_().y + 0.1, this.m_20184_().z);
            }
        }
        if (notGrounded && this.groundProgress > 0.0F) {
            this.groundProgress -= 2.0F;
        } else if (!notGrounded && this.groundProgress < 20.0F) {
            this.groundProgress += 2.0F;
        }
        if (diving && this.diveProgress < 20.0F) {
            this.diveProgress++;
        } else if (!diving && this.diveProgress > 0.0F) {
            this.diveProgress--;
        }
        if (this.isFallen && this.flightBehavior != EntityAmphithere.FlightBehavior.NONE) {
            this.flightBehavior = EntityAmphithere.FlightBehavior.NONE;
        }
        if (this.flightBehavior == EntityAmphithere.FlightBehavior.NONE && this.getControllingPassenger() == null && this.isFlying()) {
            this.m_20334_(this.m_20184_().x, this.m_20184_().y - 0.3, this.m_20184_().z);
        }
        if (this.isFlying() && !this.m_20096_() && this.isFallen && this.getControllingPassenger() == null) {
            this.m_20334_(this.m_20184_().x, this.m_20184_().y - 0.2, this.m_20184_().z);
            this.m_146926_(Math.max(this.m_146909_() + 5.0F, 75.0F));
        }
        if (this.isFallen && this.m_20096_()) {
            this.setFlying(false);
            if (this.m_21824_()) {
                this.flightCooldown = 50;
            } else {
                this.flightCooldown = 12000;
            }
            this.isFallen = false;
        }
        if (flying && this.isOverAir()) {
            if (this.getRidingPlayer() == null && this.navigatorType != 1) {
                this.switchNavigator(1);
            }
            if (this.getRidingPlayer() != null && this.navigatorType != 2) {
                this.switchNavigator(2);
            }
        }
        if (!flying && this.navigatorType != 0) {
            this.switchNavigator(0);
        }
        if ((this.hasHomePosition || this.getCommand() == 2) && this.flightBehavior == EntityAmphithere.FlightBehavior.WANDER) {
            this.flightBehavior = EntityAmphithere.FlightBehavior.CIRCLE;
        }
        if (flapping && this.flapProgress < 10.0F) {
            this.flapProgress++;
        } else if (!flapping && this.flapProgress > 0.0F) {
            this.flapProgress--;
        }
        if (this.flapTicks > 0) {
            this.flapTicks--;
        }
        if (this.m_9236_().isClientSide) {
            if (!this.m_20096_()) {
                if (this.m_20160_()) {
                    this.roll_buffer.calculateChainFlapBufferHead(40.0F, 1, 2.0F, 0.5F, this);
                } else {
                    this.f_20883_ = this.m_146908_();
                    this.roll_buffer.calculateChainFlapBuffer(70.0F, 1, 2.0F, 0.5F, this);
                }
                this.pitch_buffer.calculateChainPitchBuffer(90.0F, 10, 10.0F, 0.5F, this);
            }
            this.tail_buffer.calculateChainSwingBuffer(70.0F, 20, 5.0F, this);
        }
        if (this.changedFlightBehavior) {
            this.changedFlightBehavior = false;
        }
        if (!flapping && (this.m_20184_().y > 0.15F || this.m_20184_().y > 0.0 && this.f_19797_ % 200 == 0) && this.isOverAir()) {
            this.flapWings();
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean isFlapping() {
        return this.flapTicks > 0;
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
        this.setOrderedToSit(command == 1);
    }

    @Override
    public void flapWings() {
        this.flapTicks = 20;
    }

    @Override
    public boolean isOrderedToSit() {
        if (this.m_9236_().isClientSide) {
            boolean isSitting = (this.f_19804_.<Byte>get(f_21798_) & 1) != 0;
            this.isSitting = isSitting;
            return isSitting;
        } else {
            return this.isSitting;
        }
    }

    @Override
    public void setOrderedToSit(boolean sitting) {
        if (!this.m_9236_().isClientSide) {
            this.isSitting = sitting;
        }
        byte b0 = this.f_19804_.<Byte>get(f_21798_);
        if (sitting) {
            this.f_19804_.set(f_21798_, (byte) (b0 | 1));
        } else {
            this.f_19804_.set(f_21798_, (byte) (b0 & -2));
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player && this.m_5448_() != passenger) {
                Player player = (Player) passenger;
                if (this.m_21824_() && this.m_21805_() != null && this.m_21805_().equals(player.m_20148_())) {
                    return player;
                }
            }
        }
        return null;
    }

    @Nullable
    public Entity getUntamedRider() {
        for (Entity passenger : this.m_20197_()) {
            if (passenger instanceof Player) {
                return passenger;
            }
        }
        return null;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
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

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, IafConfig.amphithereMaxHealth).add(Attributes.MOVEMENT_SPEED, 0.4).add(Attributes.ATTACK_DAMAGE, IafConfig.amphithereAttackStrength).add(Attributes.FLYING_SPEED, IafConfig.amphithereFlightSpeed).add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public void setConfigurableAttributes() {
        this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(IafConfig.amphithereMaxHealth);
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.amphithereAttackStrength);
        this.m_21051_(Attributes.FLYING_SPEED).setBaseValue(IafConfig.amphithereFlightSpeed);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(VARIANT, 0);
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(FLAP_TICKS, 0);
        this.f_19804_.define(CONTROL_STATE, (byte) 0);
        this.f_19804_.define(COMMAND, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("FlightCooldown", this.flightCooldown);
        compound.putInt("RidingTime", this.ridingTime);
        compound.putBoolean("HasHomePosition", this.hasHomePosition);
        if (this.homePos != null && this.hasHomePosition) {
            compound.putInt("HomeAreaX", this.homePos.m_123341_());
            compound.putInt("HomeAreaY", this.homePos.m_123342_());
            compound.putInt("HomeAreaZ", this.homePos.m_123343_());
        }
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFlying(compound.getBoolean("Flying"));
        this.flightCooldown = compound.getInt("FlightCooldown");
        this.ridingTime = compound.getInt("RidingTime");
        this.hasHomePosition = compound.getBoolean("HasHomePosition");
        if (this.hasHomePosition && compound.getInt("HomeAreaX") != 0 && compound.getInt("HomeAreaY") != 0 && compound.getInt("HomeAreaZ") != 0) {
            this.homePos = new BlockPos(compound.getInt("HomeAreaX"), compound.getInt("HomeAreaY"), compound.getInt("HomeAreaZ"));
        }
        this.setCommand(compound.getInt("Command"));
        this.setConfigurableAttributes();
    }

    public boolean getCanSpawnHere() {
        int i = Mth.floor(this.m_20185_());
        int j = Mth.floor(this.m_20191_().minY);
        int k = Mth.floor(this.m_20189_());
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.m_9236_().getBlockState(blockpos.below()).m_60734_();
        return this.m_9236_().m_46861_(blockpos.above());
    }

    @Override
    public void tick() {
        super.m_8119_();
        LivingEntity target = this.m_5448_();
        if (target != null && this.getAnimation() == ANIMATION_BITE && this.getAnimationTick() == 7) {
            double dist = this.m_20280_(target);
            if (dist < 10.0) {
                target.knockback(0.6F, (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_WING_BLAST && this.getAnimationTick() == 5) {
            this.m_5496_(IafSoundRegistry.AMPHITHERE_GUST, 1.0F, 1.0F);
        }
        if ((this.getAnimation() == ANIMATION_BITE || this.getAnimation() == ANIMATION_BITE_RIDER) && this.getAnimationTick() == 1) {
            this.m_5496_(IafSoundRegistry.AMPHITHERE_BITE, 1.0F, 1.0F);
        }
        if (target != null && this.getAnimation() == ANIMATION_WING_BLAST && this.getAnimationTick() > 5 && this.getAnimationTick() < 22) {
            double dist = this.m_20280_(target);
            if (dist < 25.0) {
                target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue() / 2));
                target.f_19812_ = true;
                if (!(this.f_19796_.nextDouble() < this.m_21051_(Attributes.KNOCKBACK_RESISTANCE).getValue())) {
                    this.f_19812_ = true;
                    double d1 = target.m_20185_() - this.m_20185_();
                    double d0;
                    for (d0 = target.m_20189_() - this.m_20189_(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                        d1 = (Math.random() - Math.random()) * 0.01;
                    }
                    Vec3 Vector3d = this.m_20184_();
                    Vec3 Vector3d1 = new Vec3(d0, 0.0, d1).normalize().scale(0.5);
                    this.m_20334_(Vector3d.x / 2.0 - Vector3d1.x, this.m_20096_() ? Math.min(0.4, Vector3d.y / 2.0 + 0.5) : Vector3d.y, Vector3d.z / 2.0 - Vector3d1.z);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_TAIL_WHIP && target != null && this.getAnimationTick() == 7) {
            double dist = this.m_20280_(target);
            if (dist < 10.0) {
                target.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
                target.f_19812_ = true;
                float f = Mth.sqrt(0.5F);
                double d1 = target.m_20185_() - this.m_20185_();
                double d0;
                for (d0 = target.m_20189_() - this.m_20189_(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                    d1 = (Math.random() - Math.random()) * 0.01;
                }
                Vec3 Vector3d = this.m_20184_();
                Vec3 Vector3d1 = new Vec3(d0, 0.0, d1).normalize().scale(0.5);
                this.m_20334_(Vector3d.x / 2.0 - Vector3d1.x, this.m_20096_() ? Math.min(0.4, Vector3d.y / 2.0 + 0.5) : Vector3d.y, Vector3d.z / 2.0 - Vector3d1.z);
            }
        }
        if (this.isGoingUp() && !this.m_9236_().isClientSide && !this.isFlying()) {
            this.m_20256_(this.m_20184_().add(0.0, 1.0, 0.0));
            this.setFlying(true);
        }
        if (!this.isOverAir() && this.isFlying() && this.ticksFlying > 25) {
            this.setFlying(false);
        }
        if (this.dismountIAF() && this.isFlying() && this.m_20096_()) {
            this.setFlying(false);
        }
        if (this.getUntamedRider() != null && this.getUntamedRider().isShiftKeyDown()) {
            if (this.getUntamedRider() instanceof LivingEntity rider) {
                EntityDataProvider.getCapability(rider).ifPresent(data -> data.miscData.setDismounted(true));
            }
            this.getUntamedRider().stopRiding();
        }
        if (this.attack() && this.getControllingPassenger() != null && this.getControllingPassenger() instanceof Player) {
            LivingEntity riderTarget = DragonUtils.riderLookingAtEntity(this, (Player) this.getControllingPassenger(), 2.5);
            if (this.getAnimation() != ANIMATION_BITE) {
                this.setAnimation(ANIMATION_BITE);
            }
            if (riderTarget != null) {
                riderTarget.hurt(this.m_9236_().damageSources().mobAttack(this), (float) ((int) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (target != null && this.m_21830_(target)) {
            this.m_6710_(null);
        }
        if (target != null && this.m_20096_() && this.isFlying() && this.ticksFlying > 40) {
            this.setFlying(false);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getAnimation() != ANIMATION_BITE && this.getAnimation() != ANIMATION_TAIL_WHIP && this.getAnimation() != ANIMATION_WING_BLAST && this.getControllingPassenger() == null) {
            if (this.f_19796_.nextBoolean()) {
                this.setAnimation(ANIMATION_BITE);
            } else {
                this.setAnimation(!this.m_217043_().nextBoolean() && !this.isFlying() ? ANIMATION_TAIL_WHIP : ANIMATION_WING_BLAST);
            }
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public Player getRidingPlayer() {
        LivingEntity var2 = this.getControllingPassenger();
        return var2 instanceof Player ? (Player) var2 : null;
    }

    @Override
    public boolean isFlying() {
        return this.m_9236_().isClientSide ? (this.isFlying = this.f_19804_.get(FLYING)) : this.isFlying;
    }

    public void setFlying(boolean flying) {
        this.f_19804_.set(FLYING, flying);
        if (!this.m_9236_().isClientSide) {
            this.isFlying = flying;
        }
    }

    public int getVariant() {
        return this.f_19804_.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.f_19804_.set(VARIANT, variant);
    }

    @Override
    public boolean isGoingUp() {
        return (this.f_19804_.get(CONTROL_STATE) & 1) == 1;
    }

    @Override
    public boolean isGoingDown() {
        return (this.f_19804_.get(CONTROL_STATE) >> 1 & 1) == 1;
    }

    public boolean attack() {
        return (this.f_19804_.get(CONTROL_STATE) >> 2 & 1) == 1;
    }

    public boolean dismountIAF() {
        return (this.f_19804_.get(CONTROL_STATE) >> 3 & 1) == 1;
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
    }

    @Override
    public void dismount(boolean dismount) {
        this.setStateField(3, dismount);
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

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.AMPHITHERE_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.AMPHITHERE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.AMPHITHERE_DIE;
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
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_BITE, ANIMATION_BITE_RIDER, ANIMATION_WING_BLAST, ANIMATION_TAIL_WHIP, ANIMATION_SPEAK };
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_8032_();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.m_6677_(source);
    }

    public boolean isBlinking() {
        return this.f_19797_ % 50 > 40;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageableEntity) {
        EntityAmphithere amphithere = new EntityAmphithere(IafEntityRegistry.AMPHITHERE.get(), this.m_9236_());
        amphithere.setVariant(this.getVariant());
        return amphithere;
    }

    @Override
    public int getExperienceReward() {
        return 10;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setVariant(this.m_217043_().nextInt(5));
        return spawnDataIn;
    }

    @Override
    public boolean canPhaseThroughBlock(LevelAccessor world, BlockPos pos) {
        return world.m_8055_(pos).m_60734_() instanceof LeavesBlock;
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return !this.isFlying() && !this.isHovering() ? (float) this.m_21133_(Attributes.MOVEMENT_SPEED) * 0.5F : (float) this.m_21133_(Attributes.FLYING_SPEED) * 2.0F;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.m_6109_()) {
            if (this.m_20069_()) {
                this.m_19920_(0.02F, travelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.8F));
            } else if (this.m_20077_()) {
                this.m_19920_(0.02F, travelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.5));
            } else if (!this.isFlying() && !this.isHovering()) {
                super.m_7023_(travelVector);
            } else {
                this.m_19920_(0.1F, travelVector);
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().scale(0.9));
            }
        } else {
            super.m_7023_(travelVector);
        }
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        super.m_274498_(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.m_19915_(vec2.y, vec2.x);
        this.f_19859_ = this.f_20883_ = this.f_20885_ = this.m_146908_();
        if (this.m_6109_()) {
            Vec3 vec3 = this.m_20184_();
            float vertical = this.isGoingUp() ? 0.2F : (this.isGoingDown() ? -0.2F : 0.0F);
            if (!this.isFlying() && !this.isHovering()) {
                vertical = (float) travelVector.y;
            }
            this.m_20256_(vec3.add(0.0, (double) vertical, 0.0));
        }
    }

    @NotNull
    @Override
    protected Vec3 getRiddenInput(Player player, @NotNull Vec3 travelVector) {
        float f = player.f_20900_ * 0.5F;
        float f1 = player.f_20902_;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        return new Vec3((double) f, 0.0, (double) f1);
    }

    protected Vec2 getRiddenRotation(LivingEntity entity) {
        return new Vec2(entity.m_146909_() * 0.5F, entity.m_146908_());
    }

    public boolean canMove() {
        return this.getControllingPassenger() == null && this.sitProgress == 0.0F && !this.isOrderedToSit();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 45) {
            this.playEffect();
        } else {
            super.handleEntityEvent(id);
        }
    }

    protected void playEffect() {
        for (int i = 0; i < 7; i++) {
            double d0 = this.f_19796_.nextGaussian() * 0.02;
            double d1 = this.f_19796_.nextGaussian() * 0.02;
            double d2 = this.f_19796_.nextGaussian() * 0.02;
            this.m_9236_().addParticle(ParticleTypes.HEART, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + 0.5 + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), d0, d1, d2);
        }
    }

    @Override
    public void onHearFlute(Player player) {
        if (!this.m_20096_() && this.m_21824_()) {
            this.isFallen = true;
        }
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public double getFlightSpeedModifier() {
        return 0.555;
    }

    @Override
    public boolean fliesLikeElytra() {
        return !this.m_20096_();
    }

    private boolean isOverAir() {
        return this.m_9236_().m_46859_(this.m_20183_().below());
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 Vector3d1 = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
        return this.m_9236_().m_45547_(new ClipContext(Vector3d, Vector3d1, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    class AIFlyCircle extends Goal {

        BlockPos target;

        public AIFlyCircle() {
        }

        @Override
        public boolean canUse() {
            if (EntityAmphithere.this.flightBehavior != EntityAmphithere.FlightBehavior.CIRCLE || !EntityAmphithere.this.canMove()) {
                return false;
            } else if (EntityAmphithere.this.isFlying()) {
                EntityAmphithere.this.orbitPos = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.m_146903_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.m_146907_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.f_19796_);
                this.target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.orbitPos, EntityAmphithere.this.f_19796_);
                return true;
            } else {
                return false;
            }
        }

        protected boolean isDirectPathBetweenPoints() {
            return EntityAmphithere.this.canBlockPosBeSeen(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            if (!this.isDirectPathBetweenPoints()) {
                this.target = EntityAmphithere.getPositionInOrbit(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.orbitPos, EntityAmphithere.this.f_19796_);
            }
            if (EntityAmphithere.this.m_9236_().m_46859_(this.target)) {
                EntityAmphithere.this.f_21342_.setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.25);
                if (EntityAmphithere.this.m_5448_() == null) {
                    EntityAmphithere.this.m_21563_().setLookAt((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 180.0F, 20.0F);
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class AIFlyWander extends Goal {

        BlockPos target;

        public AIFlyWander() {
        }

        @Override
        public boolean canUse() {
            if (EntityAmphithere.this.flightBehavior != EntityAmphithere.FlightBehavior.WANDER || !EntityAmphithere.this.canMove()) {
                return false;
            } else if (!EntityAmphithere.this.isFlying()) {
                return false;
            } else {
                this.target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.m_146903_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.m_146907_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.f_19796_);
                EntityAmphithere.this.orbitPos = null;
                return !EntityAmphithere.this.m_21566_().hasWanted() || EntityAmphithere.this.ticksStill >= 50;
            }
        }

        protected boolean isDirectPathBetweenPoints(Entity e) {
            return EntityAmphithere.this.canBlockPosBeSeen(this.target);
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            if (!this.isDirectPathBetweenPoints(EntityAmphithere.this)) {
                this.target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.m_146903_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.m_146907_() + EntityAmphithere.this.f_19796_.nextInt(30) - 15, EntityAmphithere.this.f_19796_);
            }
            if (EntityAmphithere.this.m_9236_().m_46859_(this.target)) {
                EntityAmphithere.this.f_21342_.setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.25);
                if (EntityAmphithere.this.m_5448_() == null) {
                    EntityAmphithere.this.m_21563_().setLookAt((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 180.0F, 20.0F);
                }
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class AILandWander extends WaterAvoidingRandomStrollGoal {

        public AILandWander(PathfinderMob creature, double speed) {
            super(creature, speed, 10.0F);
        }

        @Override
        public boolean canUse() {
            return this.f_25725_.m_20096_() && super.m_8036_() && ((EntityAmphithere) this.f_25725_).canMove();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    public static enum FlightBehavior {

        CIRCLE, WANDER, NONE
    }

    class FlyMoveHelper extends MoveControl {

        public FlyMoveHelper(EntityAmphithere entity) {
            super(entity);
            this.f_24978_ = 1.75;
        }

        @Override
        public void tick() {
            if (EntityAmphithere.this.canMove()) {
                if (EntityAmphithere.this.f_19862_) {
                    EntityAmphithere.this.m_146922_(EntityAmphithere.this.m_146908_() + 180.0F);
                    this.f_24978_ = 0.1F;
                    BlockPos target = EntityAmphithere.getPositionRelativetoGround(EntityAmphithere.this, EntityAmphithere.this.m_9236_(), EntityAmphithere.this.m_146903_() + EntityAmphithere.this.f_19796_.nextInt(15) - 7, EntityAmphithere.this.m_146907_() + EntityAmphithere.this.f_19796_.nextInt(15) - 7, EntityAmphithere.this.f_19796_);
                    this.f_24975_ = (double) target.m_123341_();
                    this.f_24976_ = (double) target.m_123342_();
                    this.f_24977_ = (double) target.m_123343_();
                }
                if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                    double d0 = this.f_24975_ - EntityAmphithere.this.m_20185_();
                    double d1 = this.f_24976_ - EntityAmphithere.this.m_20186_();
                    double d2 = this.f_24977_ - EntityAmphithere.this.m_20189_();
                    double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                    d3 = (double) Mth.sqrt((float) d3);
                    if (d3 < 6.0 && EntityAmphithere.this.m_5448_() == null) {
                        if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == EntityAmphithere.FlightBehavior.WANDER && EntityAmphithere.this.f_19796_.nextInt(30) == 0) {
                            EntityAmphithere.this.flightBehavior = EntityAmphithere.FlightBehavior.CIRCLE;
                            EntityAmphithere.this.changedFlightBehavior = true;
                        }
                        if (!EntityAmphithere.this.changedFlightBehavior && EntityAmphithere.this.flightBehavior == EntityAmphithere.FlightBehavior.CIRCLE && EntityAmphithere.this.f_19796_.nextInt(5) == 0 && EntityAmphithere.this.ticksCircling > 150) {
                            EntityAmphithere.this.flightBehavior = EntityAmphithere.FlightBehavior.WANDER;
                            EntityAmphithere.this.changedFlightBehavior = true;
                        }
                        if (EntityAmphithere.this.hasHomePosition && EntityAmphithere.this.flightBehavior != EntityAmphithere.FlightBehavior.NONE || EntityAmphithere.this.getCommand() == 2) {
                            EntityAmphithere.this.flightBehavior = EntityAmphithere.FlightBehavior.CIRCLE;
                        }
                    }
                    if (d3 < 1.0 && EntityAmphithere.this.m_5448_() == null) {
                        this.f_24981_ = MoveControl.Operation.WAIT;
                        EntityAmphithere.this.m_20256_(EntityAmphithere.this.m_20184_().multiply(0.5, 0.5, 0.5));
                    } else {
                        EntityAmphithere.this.m_20256_(EntityAmphithere.this.m_20184_().add(d0 / d3 * 0.5 * this.f_24978_, d1 / d3 * 0.5 * this.f_24978_, d2 / d3 * 0.5 * this.f_24978_));
                        float f1 = (float) (-(Mth.atan2(d1, d3) * (180.0 / Math.PI)));
                        EntityAmphithere.this.m_146926_(f1);
                        if (EntityAmphithere.this.m_5448_() == null) {
                            EntityAmphithere.this.m_146922_(-((float) Mth.atan2(EntityAmphithere.this.m_20184_().x, EntityAmphithere.this.m_20184_().z)) * (180.0F / (float) Math.PI));
                            EntityAmphithere.this.f_20883_ = EntityAmphithere.this.m_146908_();
                        } else {
                            double d4 = EntityAmphithere.this.m_5448_().m_20185_() - EntityAmphithere.this.m_20185_();
                            double d5 = EntityAmphithere.this.m_5448_().m_20189_() - EntityAmphithere.this.m_20189_();
                            EntityAmphithere.this.m_146922_(-((float) Mth.atan2(d4, d5)) * (180.0F / (float) Math.PI));
                            EntityAmphithere.this.f_20883_ = EntityAmphithere.this.m_146908_();
                        }
                    }
                }
            }
        }
    }
}