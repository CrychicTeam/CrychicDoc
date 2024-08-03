package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.MultipleDinosaurEggsBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AdvancedPathNavigateNoTeleport;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalBreedEggsGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalJoinPackGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalLayEggGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.SubterranodonFleeGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.SubterranodonFlightGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.SubterranodonFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.util.FlyingMount;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import com.github.alexmodguy.alexscaves.server.entity.util.PackAnimal;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.AdvancedPathNavigate;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SubterranodonEntity extends DinosaurEntity implements PackAnimal, FlyingAnimal, KeybindUsingMount, FlyingMount {

    private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(SubterranodonEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HOVERING = SynchedEntityData.defineId(SubterranodonEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> METER_AMOUNT = SynchedEntityData.defineId(SubterranodonEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(SubterranodonEntity.class, EntityDataSerializers.INT);

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private float flyProgress;

    private float prevFlyProgress;

    private float flapAmount;

    private float prevFlapAmount;

    private float hoverProgress;

    private float prevHoverProgress;

    private float flightPitch = 0.0F;

    private float prevFlightPitch = 0.0F;

    private float flightRoll = 0.0F;

    private float prevFlightRoll = 0.0F;

    private float tailYaw;

    private float prevTailYaw;

    private boolean isLandNavigator;

    private SubterranodonEntity priorPackMember;

    private SubterranodonEntity afterPackMember;

    public int timeFlying;

    public Vec3 lastFlightTargetPos;

    public boolean resetFlightAIFlag = false;

    public boolean landingFlag;

    public boolean slowRidden;

    private int controlUpTicks = 0;

    private int controlDownTicks = 0;

    private AABB flightCollisionBox;

    private int timeVehicle;

    public float prevAttackProgress;

    public float attackProgress;

    private double lastStepX = 0.0;

    private double lastStepZ = 0.0;

    public SubterranodonEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.switchNavigator(true);
        this.tailYaw = this.f_20883_;
        this.prevTailYaw = this.f_20883_;
    }

    public static boolean checkSubterranodonSpawnRules(EntityType<? extends Animal> type, LevelAccessor levelAccessor, MobSpawnType mobType, BlockPos pos, RandomSource randomSource) {
        BlockState below = levelAccessor.m_8055_(pos.below());
        return (below.m_204336_(ACTagRegistry.DINOSAURS_SPAWNABLE_ON) || below.m_60713_(ACBlockRegistry.PEWEN_BRANCH.get()) || below.m_204336_(BlockTags.LEAVES)) && levelAccessor.m_6425_(pos).isEmpty() && levelAccessor.m_6425_(pos.below()).isEmpty();
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, 1.4, false));
        this.f_21345_.addGoal(3, new SubterranodonFollowOwnerGoal(this, 1.2, 5.0F, 2.0F, true));
        this.f_21345_.addGoal(4, new AnimalJoinPackGoal(this, 30, 5));
        this.f_21345_.addGoal(5, new AnimalBreedEggsGoal(this, 1.0));
        this.f_21345_.addGoal(6, new TemptGoal(this, 1.1, Ingredient.of(Items.COD, Items.COOKED_COD, ACItemRegistry.COOKED_TRILOCARIS_TAIL.get(), ACItemRegistry.TRILOCARIS_TAIL.get()), false));
        this.f_21345_.addGoal(7, new AnimalLayEggGoal(this, 100, 1.0));
        this.f_21345_.addGoal(8, new SubterranodonFleeGoal(this));
        this.f_21345_.addGoal(9, new SubterranodonFlightGoal(this));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(11, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setFlying(compound.getBoolean("Flying"));
        this.timeFlying = compound.getInt("TimeFlying");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Flying", this.isFlying());
        compound.putInt("TimeFlying", this.timeFlying);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(FLYING, false);
        this.f_19804_.define(HOVERING, false);
        this.f_19804_.define(METER_AMOUNT, 1.0F);
        this.f_19804_.define(ATTACK_TICK, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FLYING_SPEED, 1.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MAX_HEALTH, 20.0);
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = this.createMultithreadedPathFinder(false);
            this.isLandNavigator = true;
        } else {
            this.f_21342_ = new SubterranodonEntity.FlightMoveHelper(this);
            this.f_21344_ = this.createMultithreadedPathFinder(true);
            this.isLandNavigator = false;
        }
    }

    private PathNavigation createMultithreadedPathFinder(boolean flying) {
        return new AdvancedPathNavigateNoTeleport(this, this.m_9236_(), flying ? AdvancedPathNavigate.MovementType.FLYING : AdvancedPathNavigate.MovementType.WALKING);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevFlyProgress = this.flyProgress;
        this.prevHoverProgress = this.hoverProgress;
        this.prevAttackProgress = this.attackProgress;
        this.prevFlapAmount = this.flapAmount;
        this.prevFlightPitch = this.flightPitch;
        this.prevFlightRoll = this.flightRoll;
        this.prevTailYaw = this.tailYaw;
        if (this.isFlying() && this.flyProgress < 5.0F) {
            this.flyProgress++;
        }
        if (!this.isFlying() && this.flyProgress > 0.0F) {
            this.flyProgress--;
        }
        if (this.isHovering() && this.hoverProgress < 5.0F) {
            this.hoverProgress++;
        }
        if (!this.isHovering() && this.hoverProgress > 0.0F) {
            this.hoverProgress--;
        }
        if (this.f_19797_ % 100 == 0 && this.m_21223_() < this.m_21233_()) {
            this.m_5634_(2.0F);
        }
        float yMov = (float) this.m_20184_().y;
        if (!(yMov > 0.0F) && !this.isHovering()) {
            if (yMov <= 0.05F && this.flapAmount > 0.0F) {
                this.flapAmount -= 0.5F;
            }
        } else if (this.flapAmount < 5.0F) {
            this.flapAmount++;
        }
        if (this.isFlying()) {
            if (this.timeFlying % 10 == 0 && (this.flapAmount > 0.0F || this.controlUpTicks > 0)) {
                this.m_216990_(ACSoundRegistry.SUBTERRANODON_FLAP.get());
            }
            this.timeFlying++;
            if (this.isLandNavigator) {
                this.switchNavigator(false);
            }
            if (this.m_20184_().y < 0.0 && this.m_6084_()) {
                this.m_20256_(this.m_20184_().multiply(1.0, 0.6, 1.0));
            }
            if (this.isDancing() || this.m_20159_()) {
                this.setHovering(false);
                this.setFlying(false);
            }
            if (!this.m_9236_().isClientSide && this.m_20096_()) {
                LivingEntity target = this.m_5448_();
                if (target != null && target.isAlive()) {
                    this.setHovering(false);
                    this.setFlying(false);
                }
            }
        } else {
            this.timeFlying = 0;
            if (!this.isLandNavigator) {
                this.switchNavigator(true);
            }
        }
        if (this.m_20160_() && !this.m_6162_()) {
            this.setFlying(true);
            Entity rider = this.getControllingPassenger();
            if (rider != null) {
                this.flightCollisionBox = this.m_20191_().expandTowards(0.0, (double) (-0.5F - rider.getBbHeight()), 0.0);
                if (this.isRiderInWall()) {
                    this.m_20256_(this.m_20184_().add(0.0, 0.2, 0.0));
                }
            }
        }
        if (!this.m_9236_().isClientSide) {
            this.setHovering(this.isHoveringFromServer() && this.isFlying());
            if (this.isHovering() && this.isFlying() && this.m_6084_() && !this.m_20160_()) {
                if (this.timeFlying < 30) {
                    this.m_20256_(this.m_20184_().add(0.0, 0.075, 0.0));
                }
                if (this.landingFlag) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.3, 0.0));
                }
            }
            if (!this.isHovering() && this.isFlying() && this.timeFlying > 40 && this.m_20096_()) {
                this.setFlying(false);
            }
        } else {
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
                if (AlexsCaves.PROXY.isKeyDown(0) && !AlexsCaves.PROXY.isKeyDown(1) && this.controlUpTicks < 2 && this.getMeterAmount() > 0.1F && this.getMeterAmount() > 0.1F) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 0));
                    this.controlUpTicks = 5;
                }
                if (AlexsCaves.PROXY.isKeyDown(1) && !AlexsCaves.PROXY.isKeyDown(0) && this.controlDownTicks < 2) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 1));
                    this.controlDownTicks = 5;
                }
            }
        }
        if (this.controlDownTicks > 0) {
            this.controlDownTicks--;
        } else if (this.controlUpTicks > 0) {
            this.controlUpTicks--;
        }
        if (this.m_20160_()) {
            this.timeVehicle++;
        } else {
            this.timeVehicle = 0;
        }
        if (this.getMeterAmount() < 1.0F && this.controlUpTicks == 0) {
            this.setMeterAmount(this.getMeterAmount() + (this.slowRidden ? 0.002F : 0.001F));
        }
        if (this.f_19804_.get(ATTACK_TICK) > 0) {
            this.f_19804_.set(ATTACK_TICK, this.f_19804_.get(ATTACK_TICK) - 1);
            if (this.attackProgress < 5.0F) {
                this.attackProgress++;
            }
        } else {
            LivingEntity target = this.m_5448_();
            if (this.attackProgress == 5.0F && target != null && (double) this.m_20270_(target) < 3.0 + (double) target.m_20205_() && this.m_142582_(target)) {
                target.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                this.m_216990_(ACSoundRegistry.SUBTERRANODON_ATTACK.get());
            }
            if (this.attackProgress > 0.0F) {
                this.attackProgress--;
            }
        }
        this.tickRotation(Mth.clamp(yMov, -1.0F, 1.0F) * (-180.0F / (float) Math.PI));
        this.lastStepX = this.f_19854_;
        this.lastStepZ = this.f_19856_;
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

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 deltaIn) {
        float f = player.f_20902_ < 0.0F ? 0.5F : 1.0F;
        return new Vec3((double) (player.f_20900_ * 0.25F), this.controlUpTicks > 0 ? 1.0 : (this.controlDownTicks > 0 ? -1.0 : 0.0), (double) (player.f_20902_ * 0.5F * f));
    }

    @Override
    protected void tickRidden(Player player, Vec3 vec3) {
        super.m_274498_(player, vec3);
        this.slowRidden = player.f_20902_ < 0.3F || this.timeVehicle < 10 || this.m_20096_();
        if (player.f_20902_ != 0.0F || player.f_20900_ != 0.0F) {
            this.m_19915_(player.m_146908_(), player.m_146909_() * 0.25F);
            this.m_6710_(null);
        }
    }

    @Override
    protected float getFlyingSpeed() {
        return this.m_6113_();
    }

    @Override
    protected float getRiddenSpeed(Player rider) {
        return (float) this.m_21133_(Attributes.MOVEMENT_SPEED);
    }

    public boolean isRiderInWall() {
        Entity rider = this.getControllingPassenger();
        if (rider != null && !rider.noPhysics) {
            float f = rider.getDimensions(Pose.STANDING).width * 0.8F;
            AABB aabb = AABB.ofSize(rider.position().add(0.0, 0.5, 0.0), (double) f, 1.0E-6, (double) f);
            return BlockPos.betweenClosedStream(aabb).anyMatch(state -> {
                BlockState blockstate = this.m_9236_().getBlockState(state);
                return !blockstate.m_60795_() && blockstate.m_60828_(this.m_9236_(), state) && Shapes.joinIsNotEmpty(blockstate.m_60812_(this.m_9236_(), state).move((double) state.m_123341_(), (double) state.m_123342_(), (double) state.m_123343_()), Shapes.create(aabb), BooleanOp.AND);
            });
        } else {
            return false;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.f_19804_.set(ATTACK_TICK, 7);
        return true;
    }

    private boolean isHoveringFromServer() {
        return this.m_20160_() ? this.slowRidden : this.landingFlag || this.timeFlying < 30;
    }

    private void tickRotation(float yMov) {
        this.flightPitch = Mth.approachDegrees(this.flightPitch, yMov, 10.0F);
        float threshold = 1.0F;
        boolean flag = false;
        if (this.isFlying() && this.f_19859_ - this.m_146908_() > threshold) {
            this.flightRoll += 10.0F;
            flag = true;
        }
        if (this.isFlying() && this.f_19859_ - this.m_146908_() < -threshold) {
            this.flightRoll -= 10.0F;
            flag = true;
        }
        if (!flag) {
            if (this.flightRoll > 0.0F) {
                this.flightRoll = Math.max(this.flightRoll - 5.0F, 0.0F);
            }
            if (this.flightRoll < 0.0F) {
                this.flightRoll = Math.min(this.flightRoll + 5.0F, 0.0F);
            }
        }
        this.flightRoll = Mth.clamp(this.flightRoll, -60.0F, 60.0F);
        this.tailYaw = Mth.approachDegrees(this.tailYaw, this.f_20883_, 8.0F);
    }

    @Override
    public boolean isFlying() {
        return this.f_19804_.get(FLYING);
    }

    public void setFlying(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(FLYING, flying);
    }

    public boolean isHovering() {
        return this.f_19804_.get(HOVERING);
    }

    public void setHovering(boolean flying) {
        if (flying && this.m_6162_()) {
            flying = false;
        }
        this.f_19804_.set(HOVERING, flying);
    }

    @Override
    public boolean hasRidingMeter() {
        return true;
    }

    @Override
    public float getMeterAmount() {
        return this.f_19804_.get(METER_AMOUNT);
    }

    public void setMeterAmount(float flightPower) {
        this.f_19804_.set(METER_AMOUNT, flightPower);
    }

    public float getFlapAmount(float partialTick) {
        return (this.prevFlapAmount + (this.flapAmount - this.prevFlapAmount) * partialTick) * 0.2F;
    }

    public float getFlyProgress(float partialTick) {
        return (this.prevFlyProgress + (this.flyProgress - this.prevFlyProgress) * partialTick) * 0.2F;
    }

    public float getHoverProgress(float partialTick) {
        return (this.prevHoverProgress + (this.hoverProgress - this.prevHoverProgress) * partialTick) * 0.2F;
    }

    public float getBiteProgress(float partialTick) {
        return (this.prevAttackProgress + (this.attackProgress - this.prevAttackProgress) * partialTick) * 0.2F;
    }

    public float getFlightPitch(float partialTick) {
        return this.prevFlightPitch + (this.flightPitch - this.prevFlightPitch) * partialTick;
    }

    public float getFlightRoll(float partialTick) {
        return this.prevFlightRoll + (this.flightRoll - this.prevFlightRoll) * partialTick;
    }

    public float getTailYaw(float partialTick) {
        return this.prevTailYaw + (this.tailYaw - this.prevTailYaw) * partialTick;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public void resetPackFlags() {
        this.resetFlightAIFlag = true;
    }

    @Override
    public PackAnimal getPriorPackMember() {
        return this.priorPackMember;
    }

    @Override
    public PackAnimal getAfterPackMember() {
        return this.afterPackMember;
    }

    @Override
    public void setPriorPackMember(PackAnimal animal) {
        this.priorPackMember = (SubterranodonEntity) animal;
    }

    @Override
    public void setAfterPackMember(PackAnimal animal) {
        this.afterPackMember = (SubterranodonEntity) animal;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ACEntityRegistry.SUBTERRANODON.get().create(serverLevel);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(3.0, 3.0, 3.0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0;
    }

    @Override
    public BlockState createEggBlockState() {
        return (BlockState) ACBlockRegistry.SUBTERRANODON_EGG.get().defaultBlockState().m_61124_(MultipleDinosaurEggsBlock.EGGS, 1 + this.f_19796_.nextInt(3));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult prev = super.mobInteract(player, hand);
        if (prev != InteractionResult.SUCCESS) {
            ItemStack itemStack = player.m_21120_(hand);
            if (!this.m_21824_() && (itemStack.is(ACItemRegistry.TRILOCARIS_TAIL.get()) || itemStack.is(ACItemRegistry.COOKED_TRILOCARIS_TAIL.get()))) {
                this.m_142075_(player, hand, itemStack);
                if (this.m_217043_().nextInt(3) == 0) {
                    this.m_21828_(player);
                    this.m_147271_();
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return prev;
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this)) {
            if (type == 0) {
                if (this.controlUpTicks != 10) {
                    this.setMeterAmount(Math.max(this.getMeterAmount() - 0.075F, 0.0F));
                }
                this.controlUpTicks = 10;
            }
            if (type == 1) {
                this.controlDownTicks = 10;
            }
        }
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
            float flight = this.getFlyProgress(1.0F) - this.getHoverProgress(1.0F);
            Vec3 seatOffset = new Vec3(0.0, 0.0, (double) (0.2F - 1.5F * flight)).xRot((float) Math.toRadians((double) this.m_146909_())).yRot((float) Math.toRadians((double) (-this.f_20883_)));
            double targetY = this.m_20186_() - (double) passenger.getBbHeight() - 0.5 + (double) (0.25F * flight);
            passenger.setYBodyRot(this.f_20883_);
            passenger.fallDistance = 0.0F;
            this.clampRotation(living, 105.0F);
            moveFunction.accept(passenger, this.m_20185_() + seatOffset.x, targetY, this.m_20189_() + seatOffset.z);
            return;
        }
        super.m_19956_(passenger, moveFunction);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        return new Vec3(this.m_20185_(), this.m_20191_().minY, this.m_20189_());
    }

    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.m_146895_();
        return entity instanceof Player ? (Player) entity : null;
    }

    @Override
    public boolean tamesFromHatching() {
        return true;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.lastStepX, 0.0, this.m_20189_() - this.lastStepZ);
        float f2 = Math.min(f1 * 4.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    public Vec3 collide(Vec3 movement) {
        if (this.flightCollisionBox != null && !this.m_146899_() && this.m_20160_()) {
            AABB aabb = this.flightCollisionBox;
            List<VoxelShape> list = this.m_9236_().m_183134_(this, aabb.expandTowards(movement));
            Vec3 vec3 = movement.lengthSqr() == 0.0 ? movement : m_198894_(this, movement, aabb, this.m_9236_(), list);
            boolean flag = movement.x != vec3.x;
            boolean flag1 = movement.y != vec3.y;
            boolean flag2 = movement.z != vec3.z;
            boolean flag3 = this.m_20096_() || flag1 && movement.y < 0.0;
            if (this.getStepHeight() > 0.0F && flag3 && (flag || flag2)) {
                Vec3 vec31 = m_198894_(this, new Vec3(movement.x, (double) this.getStepHeight(), movement.z), aabb, this.m_9236_(), list);
                Vec3 vec32 = m_198894_(this, new Vec3(0.0, (double) this.getStepHeight(), 0.0), aabb.expandTowards(movement.x, 0.0, movement.z), this.m_9236_(), list);
                if (vec32.y < (double) this.getStepHeight()) {
                    Vec3 vec33 = m_198894_(this, new Vec3(movement.x, 0.0, movement.z), aabb.move(vec32), this.m_9236_(), list).add(vec32);
                    if (vec33.horizontalDistanceSqr() > vec31.horizontalDistanceSqr()) {
                        vec31 = vec33;
                    }
                }
                if (vec31.horizontalDistanceSqr() > vec3.horizontalDistanceSqr()) {
                    return vec31.add(m_198894_(this, new Vec3(0.0, -vec31.y + movement.y, 0.0), aabb.move(vec31), this.m_9236_(), list));
                }
            }
            return vec3;
        } else {
            return super.m_20272_(movement);
        }
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
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.COD) || stack.is(Items.COOKED_COD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.SUBTERRANODON_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.SUBTERRANODON_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.SUBTERRANODON_DEATH.get();
    }

    class FlightMoveHelper extends MoveControl {

        private final SubterranodonEntity parentEntity;

        public FlightMoveHelper(SubterranodonEntity bird) {
            super(bird);
            this.parentEntity = bird;
        }

        @Override
        public void tick() {
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                Vec3 vector3d = new Vec3(this.f_24975_ - this.parentEntity.m_20185_(), this.f_24976_ - this.parentEntity.m_20186_(), this.f_24977_ - this.parentEntity.m_20189_());
                double d5 = vector3d.length();
                if (d5 < this.parentEntity.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().scale(0.5));
                } else {
                    float hoverSlow = this.parentEntity.isHoveringFromServer() && !this.parentEntity.landingFlag ? 0.2F : 1.0F;
                    this.parentEntity.m_20256_(this.parentEntity.m_20184_().add(vector3d.scale(this.f_24978_ * 0.1 / d5).multiply((double) hoverSlow, 1.0, (double) hoverSlow)));
                    Vec3 vector3d1 = this.parentEntity.m_20184_();
                    float f = -((float) Mth.atan2(vector3d1.x, vector3d1.z)) * 180.0F / (float) Math.PI;
                    this.parentEntity.m_146922_(Mth.approachDegrees(this.parentEntity.m_146908_(), f, 20.0F));
                    this.parentEntity.f_20883_ = this.parentEntity.m_146908_();
                }
            }
        }
    }
}