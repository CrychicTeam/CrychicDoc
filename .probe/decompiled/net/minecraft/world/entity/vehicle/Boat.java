package net.minecraft.world.entity.vehicle;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import java.util.function.IntFunction;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Boat extends Entity implements VariantHolder<Boat.Type> {

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public static final int PADDLE_LEFT = 0;

    public static final int PADDLE_RIGHT = 1;

    private static final int TIME_TO_EJECT = 60;

    private static final float PADDLE_SPEED = (float) (Math.PI / 8);

    public static final double PADDLE_SOUND_TIME = (float) (Math.PI / 4);

    public static final int BUBBLE_TIME = 60;

    private final float[] paddlePositions = new float[2];

    private float invFriction;

    private float outOfControlTicks;

    private float deltaRotation;

    private int lerpSteps;

    private double lerpX;

    private double lerpY;

    private double lerpZ;

    private double lerpYRot;

    private double lerpXRot;

    private boolean inputLeft;

    private boolean inputRight;

    private boolean inputUp;

    private boolean inputDown;

    private double waterLevel;

    private float landFriction;

    private Boat.Status status;

    private Boat.Status oldStatus;

    private double lastYd;

    private boolean isAboveBubbleColumn;

    private boolean bubbleColumnDirectionIsDown;

    private float bubbleMultiplier;

    private float bubbleAngle;

    private float bubbleAngleO;

    public Boat(EntityType<? extends Boat> entityTypeExtendsBoat0, Level level1) {
        super(entityTypeExtendsBoat0, level1);
        this.f_19850_ = true;
    }

    public Boat(Level level0, double double1, double double2, double double3) {
        this(EntityType.BOAT, level0);
        this.m_6034_(double1, double2, double3);
        this.f_19854_ = double1;
        this.f_19855_ = double2;
        this.f_19856_ = double3;
    }

    @Override
    protected float getEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(DATA_ID_HURT, 0);
        this.f_19804_.define(DATA_ID_HURTDIR, 1);
        this.f_19804_.define(DATA_ID_DAMAGE, 0.0F);
        this.f_19804_.define(DATA_ID_TYPE, Boat.Type.OAK.ordinal());
        this.f_19804_.define(DATA_ID_PADDLE_LEFT, false);
        this.f_19804_.define(DATA_ID_PADDLE_RIGHT, false);
        this.f_19804_.define(DATA_ID_BUBBLE_TIME, 0);
    }

    @Override
    public boolean canCollideWith(Entity entity0) {
        return canVehicleCollide(this, entity0);
    }

    public static boolean canVehicleCollide(Entity entity0, Entity entity1) {
        return (entity1.canBeCollidedWith() || entity1.isPushable()) && !entity0.isPassengerOfSameVehicle(entity1);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected Vec3 getRelativePortalPosition(Direction.Axis directionAxis0, BlockUtil.FoundRectangle blockUtilFoundRectangle1) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(directionAxis0, blockUtilFoundRectangle1));
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getVariant() == Boat.Type.BAMBOO ? 0.25 : -0.1;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_6673_(damageSource0)) {
            return false;
        } else if (!this.m_9236_().isClientSide && !this.m_213877_()) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + float1 * 10.0F);
            this.m_5834_();
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
            boolean $$2 = damageSource0.getEntity() instanceof Player && ((Player) damageSource0.getEntity()).getAbilities().instabuild;
            if ($$2 || this.getDamage() > 40.0F) {
                if (!$$2 && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(damageSource0);
                }
                this.m_146870_();
            }
            return true;
        } else {
            return true;
        }
    }

    protected void destroy(DamageSource damageSource0) {
        this.m_19998_(this.getDropItem());
    }

    @Override
    public void onAboveBubbleCol(boolean boolean0) {
        if (!this.m_9236_().isClientSide) {
            this.isAboveBubbleColumn = true;
            this.bubbleColumnDirectionIsDown = boolean0;
            if (this.getBubbleTime() == 0) {
                this.setBubbleTime(60);
            }
        }
        this.m_9236_().addParticle(ParticleTypes.SPLASH, this.m_20185_() + (double) this.f_19796_.nextFloat(), this.m_20186_() + 0.7, this.m_20189_() + (double) this.f_19796_.nextFloat(), 0.0, 0.0, 0.0);
        if (this.f_19796_.nextInt(20) == 0) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_5509_(), this.m_5720_(), 1.0F, 0.8F + 0.4F * this.f_19796_.nextFloat(), false);
            this.m_146852_(GameEvent.SPLASH, this.getControllingPassenger());
        }
    }

    @Override
    public void push(Entity entity0) {
        if (entity0 instanceof Boat) {
            if (entity0.getBoundingBox().minY < this.m_20191_().maxY) {
                super.push(entity0);
            }
        } else if (entity0.getBoundingBox().minY <= this.m_20191_().minY) {
            super.push(entity0);
        }
    }

    public Item getDropItem() {
        return switch(this.getVariant()) {
            case SPRUCE ->
                Items.SPRUCE_BOAT;
            case BIRCH ->
                Items.BIRCH_BOAT;
            case JUNGLE ->
                Items.JUNGLE_BOAT;
            case ACACIA ->
                Items.ACACIA_BOAT;
            case CHERRY ->
                Items.CHERRY_BOAT;
            case DARK_OAK ->
                Items.DARK_OAK_BOAT;
            case MANGROVE ->
                Items.MANGROVE_BOAT;
            case BAMBOO ->
                Items.BAMBOO_RAFT;
            default ->
                Items.OAK_BOAT;
        };
    }

    @Override
    public void animateHurt(float float0) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public void lerpTo(double double0, double double1, double double2, float float3, float float4, int int5, boolean boolean6) {
        this.lerpX = double0;
        this.lerpY = double1;
        this.lerpZ = double2;
        this.lerpYRot = (double) float3;
        this.lerpXRot = (double) float4;
        this.lerpSteps = 10;
    }

    @Override
    public Direction getMotionDirection() {
        return this.m_6350_().getClockWise();
    }

    @Override
    public void tick() {
        this.oldStatus = this.status;
        this.status = this.getStatus();
        if (this.status != Boat.Status.UNDER_WATER && this.status != Boat.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        } else {
            this.outOfControlTicks++;
        }
        if (!this.m_9236_().isClientSide && this.outOfControlTicks >= 60.0F) {
            this.m_20153_();
        }
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }
        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }
        super.tick();
        this.tickLerp();
        if (this.m_6109_()) {
            if (!(this.m_146895_() instanceof Player)) {
                this.setPaddleState(false, false);
            }
            this.floatBoat();
            if (this.m_9236_().isClientSide) {
                this.controlBoat();
                this.m_9236_().sendPacketToServer(new ServerboundPaddleBoatPacket(this.getPaddleState(0), this.getPaddleState(1)));
            }
            this.m_6478_(MoverType.SELF, this.m_20184_());
        } else {
            this.m_20256_(Vec3.ZERO);
        }
        this.tickBubbleColumn();
        for (int $$0 = 0; $$0 <= 1; $$0++) {
            if (this.getPaddleState($$0)) {
                if (!this.m_20067_() && (double) (this.paddlePositions[$$0] % (float) (Math.PI * 2)) <= (float) (Math.PI / 4) && (double) ((this.paddlePositions[$$0] + (float) (Math.PI / 8)) % (float) (Math.PI * 2)) >= (float) (Math.PI / 4)) {
                    SoundEvent $$1 = this.getPaddleSound();
                    if ($$1 != null) {
                        Vec3 $$2 = this.m_20252_(1.0F);
                        double $$3 = $$0 == 1 ? -$$2.z : $$2.z;
                        double $$4 = $$0 == 1 ? $$2.x : -$$2.x;
                        this.m_9236_().playSound(null, this.m_20185_() + $$3, this.m_20186_(), this.m_20189_() + $$4, $$1, this.m_5720_(), 1.0F, 0.8F + 0.4F * this.f_19796_.nextFloat());
                    }
                }
                this.paddlePositions[$$0] = this.paddlePositions[$$0] + (float) (Math.PI / 8);
            } else {
                this.paddlePositions[$$0] = 0.0F;
            }
        }
        this.m_20101_();
        List<Entity> $$5 = this.m_9236_().getEntities(this, this.m_20191_().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
        if (!$$5.isEmpty()) {
            boolean $$6 = !this.m_9236_().isClientSide && !(this.getControllingPassenger() instanceof Player);
            for (int $$7 = 0; $$7 < $$5.size(); $$7++) {
                Entity $$8 = (Entity) $$5.get($$7);
                if (!$$8.hasPassenger(this)) {
                    if ($$6 && this.m_20197_().size() < this.getMaxPassengers() && !$$8.isPassenger() && this.hasEnoughSpaceFor($$8) && $$8 instanceof LivingEntity && !($$8 instanceof WaterAnimal) && !($$8 instanceof Player)) {
                        $$8.startRiding(this);
                    } else {
                        this.push($$8);
                    }
                }
            }
        }
    }

    private void tickBubbleColumn() {
        if (this.m_9236_().isClientSide) {
            int $$0 = this.getBubbleTime();
            if ($$0 > 0) {
                this.bubbleMultiplier += 0.05F;
            } else {
                this.bubbleMultiplier -= 0.1F;
            }
            this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0F, 1.0F);
            this.bubbleAngleO = this.bubbleAngle;
            this.bubbleAngle = 10.0F * (float) Math.sin((double) (0.5F * (float) this.m_9236_().getGameTime())) * this.bubbleMultiplier;
        } else {
            if (!this.isAboveBubbleColumn) {
                this.setBubbleTime(0);
            }
            int $$1 = this.getBubbleTime();
            if ($$1 > 0) {
                this.setBubbleTime(--$$1);
                int $$2 = 60 - $$1 - 1;
                if ($$2 > 0 && $$1 == 0) {
                    this.setBubbleTime(0);
                    Vec3 $$3 = this.m_20184_();
                    if (this.bubbleColumnDirectionIsDown) {
                        this.m_20256_($$3.add(0.0, -0.7, 0.0));
                        this.m_20153_();
                    } else {
                        this.m_20334_($$3.x, this.m_146862_(p_150274_ -> p_150274_ instanceof Player) ? 2.7 : 0.6, $$3.z);
                    }
                }
                this.isAboveBubbleColumn = false;
            }
        }
    }

    @Nullable
    protected SoundEvent getPaddleSound() {
        switch(this.getStatus()) {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER:
                return SoundEvents.BOAT_PADDLE_WATER;
            case ON_LAND:
                return SoundEvents.BOAT_PADDLE_LAND;
            case IN_AIR:
            default:
                return null;
        }
    }

    private void tickLerp() {
        if (this.m_6109_()) {
            this.lerpSteps = 0;
            this.m_217006_(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
        if (this.lerpSteps > 0) {
            double $$0 = this.m_20185_() + (this.lerpX - this.m_20185_()) / (double) this.lerpSteps;
            double $$1 = this.m_20186_() + (this.lerpY - this.m_20186_()) / (double) this.lerpSteps;
            double $$2 = this.m_20189_() + (this.lerpZ - this.m_20189_()) / (double) this.lerpSteps;
            double $$3 = Mth.wrapDegrees(this.lerpYRot - (double) this.m_146908_());
            this.m_146922_(this.m_146908_() + (float) $$3 / (float) this.lerpSteps);
            this.m_146926_(this.m_146909_() + (float) (this.lerpXRot - (double) this.m_146909_()) / (float) this.lerpSteps);
            this.lerpSteps--;
            this.m_6034_($$0, $$1, $$2);
            this.m_19915_(this.m_146908_(), this.m_146909_());
        }
    }

    public void setPaddleState(boolean boolean0, boolean boolean1) {
        this.f_19804_.set(DATA_ID_PADDLE_LEFT, boolean0);
        this.f_19804_.set(DATA_ID_PADDLE_RIGHT, boolean1);
    }

    public float getRowingTime(int int0, float float1) {
        return this.getPaddleState(int0) ? Mth.clampedLerp(this.paddlePositions[int0] - (float) (Math.PI / 8), this.paddlePositions[int0], float1) : 0.0F;
    }

    private Boat.Status getStatus() {
        Boat.Status $$0 = this.isUnderwater();
        if ($$0 != null) {
            this.waterLevel = this.m_20191_().maxY;
            return $$0;
        } else if (this.checkInWater()) {
            return Boat.Status.IN_WATER;
        } else {
            float $$1 = this.getGroundFriction();
            if ($$1 > 0.0F) {
                this.landFriction = $$1;
                return Boat.Status.ON_LAND;
            } else {
                return Boat.Status.IN_AIR;
            }
        }
    }

    public float getWaterLevelAbove() {
        AABB $$0 = this.m_20191_();
        int $$1 = Mth.floor($$0.minX);
        int $$2 = Mth.ceil($$0.maxX);
        int $$3 = Mth.floor($$0.maxY);
        int $$4 = Mth.ceil($$0.maxY - this.lastYd);
        int $$5 = Mth.floor($$0.minZ);
        int $$6 = Mth.ceil($$0.maxZ);
        BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
        label39: for (int $$8 = $$3; $$8 < $$4; $$8++) {
            float $$9 = 0.0F;
            for (int $$10 = $$1; $$10 < $$2; $$10++) {
                for (int $$11 = $$5; $$11 < $$6; $$11++) {
                    $$7.set($$10, $$8, $$11);
                    FluidState $$12 = this.m_9236_().getFluidState($$7);
                    if ($$12.is(FluidTags.WATER)) {
                        $$9 = Math.max($$9, $$12.getHeight(this.m_9236_(), $$7));
                    }
                    if ($$9 >= 1.0F) {
                        continue label39;
                    }
                }
            }
            if ($$9 < 1.0F) {
                return (float) $$7.m_123342_() + $$9;
            }
        }
        return (float) ($$4 + 1);
    }

    public float getGroundFriction() {
        AABB $$0 = this.m_20191_();
        AABB $$1 = new AABB($$0.minX, $$0.minY - 0.001, $$0.minZ, $$0.maxX, $$0.minY, $$0.maxZ);
        int $$2 = Mth.floor($$1.minX) - 1;
        int $$3 = Mth.ceil($$1.maxX) + 1;
        int $$4 = Mth.floor($$1.minY) - 1;
        int $$5 = Mth.ceil($$1.maxY) + 1;
        int $$6 = Mth.floor($$1.minZ) - 1;
        int $$7 = Mth.ceil($$1.maxZ) + 1;
        VoxelShape $$8 = Shapes.create($$1);
        float $$9 = 0.0F;
        int $$10 = 0;
        BlockPos.MutableBlockPos $$11 = new BlockPos.MutableBlockPos();
        for (int $$12 = $$2; $$12 < $$3; $$12++) {
            for (int $$13 = $$6; $$13 < $$7; $$13++) {
                int $$14 = ($$12 != $$2 && $$12 != $$3 - 1 ? 0 : 1) + ($$13 != $$6 && $$13 != $$7 - 1 ? 0 : 1);
                if ($$14 != 2) {
                    for (int $$15 = $$4; $$15 < $$5; $$15++) {
                        if ($$14 <= 0 || $$15 != $$4 && $$15 != $$5 - 1) {
                            $$11.set($$12, $$15, $$13);
                            BlockState $$16 = this.m_9236_().getBlockState($$11);
                            if (!($$16.m_60734_() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty($$16.m_60812_(this.m_9236_(), $$11).move((double) $$12, (double) $$15, (double) $$13), $$8, BooleanOp.AND)) {
                                $$9 += $$16.m_60734_().getFriction();
                                $$10++;
                            }
                        }
                    }
                }
            }
        }
        return $$9 / (float) $$10;
    }

    private boolean checkInWater() {
        AABB $$0 = this.m_20191_();
        int $$1 = Mth.floor($$0.minX);
        int $$2 = Mth.ceil($$0.maxX);
        int $$3 = Mth.floor($$0.minY);
        int $$4 = Mth.ceil($$0.minY + 0.001);
        int $$5 = Mth.floor($$0.minZ);
        int $$6 = Mth.ceil($$0.maxZ);
        boolean $$7 = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos();
        for (int $$9 = $$1; $$9 < $$2; $$9++) {
            for (int $$10 = $$3; $$10 < $$4; $$10++) {
                for (int $$11 = $$5; $$11 < $$6; $$11++) {
                    $$8.set($$9, $$10, $$11);
                    FluidState $$12 = this.m_9236_().getFluidState($$8);
                    if ($$12.is(FluidTags.WATER)) {
                        float $$13 = (float) $$10 + $$12.getHeight(this.m_9236_(), $$8);
                        this.waterLevel = Math.max((double) $$13, this.waterLevel);
                        $$7 |= $$0.minY < (double) $$13;
                    }
                }
            }
        }
        return $$7;
    }

    @Nullable
    private Boat.Status isUnderwater() {
        AABB $$0 = this.m_20191_();
        double $$1 = $$0.maxY + 0.001;
        int $$2 = Mth.floor($$0.minX);
        int $$3 = Mth.ceil($$0.maxX);
        int $$4 = Mth.floor($$0.maxY);
        int $$5 = Mth.ceil($$1);
        int $$6 = Mth.floor($$0.minZ);
        int $$7 = Mth.ceil($$0.maxZ);
        boolean $$8 = false;
        BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
        for (int $$10 = $$2; $$10 < $$3; $$10++) {
            for (int $$11 = $$4; $$11 < $$5; $$11++) {
                for (int $$12 = $$6; $$12 < $$7; $$12++) {
                    $$9.set($$10, $$11, $$12);
                    FluidState $$13 = this.m_9236_().getFluidState($$9);
                    if ($$13.is(FluidTags.WATER) && $$1 < (double) ((float) $$9.m_123342_() + $$13.getHeight(this.m_9236_(), $$9))) {
                        if (!$$13.isSource()) {
                            return Boat.Status.UNDER_FLOWING_WATER;
                        }
                        $$8 = true;
                    }
                }
            }
        }
        return $$8 ? Boat.Status.UNDER_WATER : null;
    }

    private void floatBoat() {
        double $$0 = -0.04F;
        double $$1 = this.m_20068_() ? 0.0 : -0.04F;
        double $$2 = 0.0;
        this.invFriction = 0.05F;
        if (this.oldStatus == Boat.Status.IN_AIR && this.status != Boat.Status.IN_AIR && this.status != Boat.Status.ON_LAND) {
            this.waterLevel = this.m_20227_(1.0);
            this.m_6034_(this.m_20185_(), (double) (this.getWaterLevelAbove() - this.m_20206_()) + 0.101, this.m_20189_());
            this.m_20256_(this.m_20184_().multiply(1.0, 0.0, 1.0));
            this.lastYd = 0.0;
            this.status = Boat.Status.IN_WATER;
        } else {
            if (this.status == Boat.Status.IN_WATER) {
                $$2 = (this.waterLevel - this.m_20186_()) / (double) this.m_20206_();
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.UNDER_FLOWING_WATER) {
                $$1 = -7.0E-4;
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.UNDER_WATER) {
                $$2 = 0.01F;
                this.invFriction = 0.45F;
            } else if (this.status == Boat.Status.IN_AIR) {
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.ON_LAND) {
                this.invFriction = this.landFriction;
                if (this.getControllingPassenger() instanceof Player) {
                    this.landFriction /= 2.0F;
                }
            }
            Vec3 $$3 = this.m_20184_();
            this.m_20334_($$3.x * (double) this.invFriction, $$3.y + $$1, $$3.z * (double) this.invFriction);
            this.deltaRotation = this.deltaRotation * this.invFriction;
            if ($$2 > 0.0) {
                Vec3 $$4 = this.m_20184_();
                this.m_20334_($$4.x, ($$4.y + $$2 * 0.06153846016296973) * 0.75, $$4.z);
            }
        }
    }

    private void controlBoat() {
        if (this.m_20160_()) {
            float $$0 = 0.0F;
            if (this.inputLeft) {
                this.deltaRotation--;
            }
            if (this.inputRight) {
                this.deltaRotation++;
            }
            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                $$0 += 0.005F;
            }
            this.m_146922_(this.m_146908_() + this.deltaRotation);
            if (this.inputUp) {
                $$0 += 0.04F;
            }
            if (this.inputDown) {
                $$0 -= 0.005F;
            }
            this.m_20256_(this.m_20184_().add((double) (Mth.sin(-this.m_146908_() * (float) (Math.PI / 180.0)) * $$0), 0.0, (double) (Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * $$0)));
            this.setPaddleState(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp);
        }
    }

    protected float getSinglePassengerXOffset() {
        return 0.0F;
    }

    public boolean hasEnoughSpaceFor(Entity entity0) {
        return entity0.getBbWidth() < this.m_20205_();
    }

    @Override
    protected void positionRider(Entity entity0, Entity.MoveFunction entityMoveFunction1) {
        if (this.m_20363_(entity0)) {
            float $$2 = this.getSinglePassengerXOffset();
            float $$3 = (float) ((this.m_213877_() ? 0.01F : this.getPassengersRidingOffset()) + entity0.getMyRidingOffset());
            if (this.m_20197_().size() > 1) {
                int $$4 = this.m_20197_().indexOf(entity0);
                if ($$4 == 0) {
                    $$2 = 0.2F;
                } else {
                    $$2 = -0.6F;
                }
                if (entity0 instanceof Animal) {
                    $$2 += 0.2F;
                }
            }
            Vec3 $$5 = new Vec3((double) $$2, 0.0, 0.0).yRot(-this.m_146908_() * (float) (Math.PI / 180.0) - (float) (Math.PI / 2));
            entityMoveFunction1.accept(entity0, this.m_20185_() + $$5.x, this.m_20186_() + (double) $$3, this.m_20189_() + $$5.z);
            entity0.setYRot(entity0.getYRot() + this.deltaRotation);
            entity0.setYHeadRot(entity0.getYHeadRot() + this.deltaRotation);
            this.clampRotation(entity0);
            if (entity0 instanceof Animal && this.m_20197_().size() == this.getMaxPassengers()) {
                int $$6 = entity0.getId() % 2 == 0 ? 90 : 270;
                entity0.setYBodyRot(((Animal) entity0).f_20883_ + (float) $$6);
                entity0.setYHeadRot(entity0.getYHeadRot() + (float) $$6);
            }
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Vec3 $$1 = m_19903_((double) (this.m_20205_() * Mth.SQRT_OF_TWO), (double) livingEntity0.m_20205_(), livingEntity0.m_146908_());
        double $$2 = this.m_20185_() + $$1.x;
        double $$3 = this.m_20189_() + $$1.z;
        BlockPos $$4 = BlockPos.containing($$2, this.m_20191_().maxY, $$3);
        BlockPos $$5 = $$4.below();
        if (!this.m_9236_().m_46801_($$5)) {
            List<Vec3> $$6 = Lists.newArrayList();
            double $$7 = this.m_9236_().m_45573_($$4);
            if (DismountHelper.isBlockFloorValid($$7)) {
                $$6.add(new Vec3($$2, (double) $$4.m_123342_() + $$7, $$3));
            }
            double $$8 = this.m_9236_().m_45573_($$5);
            if (DismountHelper.isBlockFloorValid($$8)) {
                $$6.add(new Vec3($$2, (double) $$5.m_123342_() + $$8, $$3));
            }
            UnmodifiableIterator var14 = livingEntity0.getDismountPoses().iterator();
            while (var14.hasNext()) {
                Pose $$9 = (Pose) var14.next();
                for (Vec3 $$10 : $$6) {
                    if (DismountHelper.canDismountTo(this.m_9236_(), $$10, livingEntity0, $$9)) {
                        livingEntity0.m_20124_($$9);
                        return $$10;
                    }
                }
            }
        }
        return super.getDismountLocationForPassenger(livingEntity0);
    }

    protected void clampRotation(Entity entity0) {
        entity0.setYBodyRot(this.m_146908_());
        float $$1 = Mth.wrapDegrees(entity0.getYRot() - this.m_146908_());
        float $$2 = Mth.clamp($$1, -105.0F, 105.0F);
        entity0.yRotO += $$2 - $$1;
        entity0.setYRot(entity0.getYRot() + $$2 - $$1);
        entity0.setYHeadRot(entity0.getYRot());
    }

    @Override
    public void onPassengerTurned(Entity entity0) {
        this.clampRotation(entity0);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        compoundTag0.putString("Type", this.getVariant().getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.contains("Type", 8)) {
            this.setVariant(Boat.Type.byName(compoundTag0.getString("Type")));
        }
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        if (player0.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else if (this.outOfControlTicks < 60.0F) {
            if (!this.m_9236_().isClientSide) {
                return player0.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected void checkFallDamage(double double0, boolean boolean1, BlockState blockState2, BlockPos blockPos3) {
        this.lastYd = this.m_20184_().y;
        if (!this.m_20159_()) {
            if (boolean1) {
                if (this.f_19789_ > 3.0F) {
                    if (this.status != Boat.Status.ON_LAND) {
                        this.m_183634_();
                        return;
                    }
                    this.m_142535_(this.f_19789_, 1.0F, this.m_269291_().fall());
                    if (!this.m_9236_().isClientSide && !this.m_213877_()) {
                        this.m_6074_();
                        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int $$4 = 0; $$4 < 3; $$4++) {
                                this.m_19998_(this.getVariant().getPlanks());
                            }
                            for (int $$5 = 0; $$5 < 2; $$5++) {
                                this.m_19998_(Items.STICK);
                            }
                        }
                    }
                }
                this.m_183634_();
            } else if (!this.m_9236_().getFluidState(this.m_20183_().below()).is(FluidTags.WATER) && double0 < 0.0) {
                this.f_19789_ -= (float) double0;
            }
        }
    }

    public boolean getPaddleState(int int0) {
        return this.f_19804_.get(int0 == 0 ? DATA_ID_PADDLE_LEFT : DATA_ID_PADDLE_RIGHT) && this.getControllingPassenger() != null;
    }

    public void setDamage(float float0) {
        this.f_19804_.set(DATA_ID_DAMAGE, float0);
    }

    public float getDamage() {
        return this.f_19804_.get(DATA_ID_DAMAGE);
    }

    public void setHurtTime(int int0) {
        this.f_19804_.set(DATA_ID_HURT, int0);
    }

    public int getHurtTime() {
        return this.f_19804_.get(DATA_ID_HURT);
    }

    private void setBubbleTime(int int0) {
        this.f_19804_.set(DATA_ID_BUBBLE_TIME, int0);
    }

    private int getBubbleTime() {
        return this.f_19804_.get(DATA_ID_BUBBLE_TIME);
    }

    public float getBubbleAngle(float float0) {
        return Mth.lerp(float0, this.bubbleAngleO, this.bubbleAngle);
    }

    public void setHurtDir(int int0) {
        this.f_19804_.set(DATA_ID_HURTDIR, int0);
    }

    public int getHurtDir() {
        return this.f_19804_.get(DATA_ID_HURTDIR);
    }

    public void setVariant(Boat.Type boatType0) {
        this.f_19804_.set(DATA_ID_TYPE, boatType0.ordinal());
    }

    public Boat.Type getVariant() {
        return Boat.Type.byId(this.f_19804_.get(DATA_ID_TYPE));
    }

    @Override
    protected boolean canAddPassenger(Entity entity0) {
        return this.m_20197_().size() < this.getMaxPassengers() && !this.m_204029_(FluidTags.WATER);
    }

    protected int getMaxPassengers() {
        return 2;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.m_146895_() instanceof LivingEntity $$0 ? $$0 : null;
    }

    public void setInput(boolean boolean0, boolean boolean1, boolean boolean2, boolean boolean3) {
        this.inputLeft = boolean0;
        this.inputRight = boolean1;
        this.inputUp = boolean2;
        this.inputDown = boolean3;
    }

    @Override
    public boolean isUnderWater() {
        return this.status == Boat.Status.UNDER_WATER || this.status == Boat.Status.UNDER_FLOWING_WATER;
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    public static enum Status {

        IN_WATER, UNDER_WATER, UNDER_FLOWING_WATER, ON_LAND, IN_AIR
    }

    public static enum Type implements StringRepresentable {

        OAK(Blocks.OAK_PLANKS, "oak"),
        SPRUCE(Blocks.SPRUCE_PLANKS, "spruce"),
        BIRCH(Blocks.BIRCH_PLANKS, "birch"),
        JUNGLE(Blocks.JUNGLE_PLANKS, "jungle"),
        ACACIA(Blocks.ACACIA_PLANKS, "acacia"),
        CHERRY(Blocks.CHERRY_PLANKS, "cherry"),
        DARK_OAK(Blocks.DARK_OAK_PLANKS, "dark_oak"),
        MANGROVE(Blocks.MANGROVE_PLANKS, "mangrove"),
        BAMBOO(Blocks.BAMBOO_PLANKS, "bamboo");

        private final String name;

        private final Block planks;

        public static final StringRepresentable.EnumCodec<Boat.Type> CODEC = StringRepresentable.fromEnum(Boat.Type::values);

        private static final IntFunction<Boat.Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private Type(Block p_38427_, String p_38428_) {
            this.name = p_38428_;
            this.planks = p_38427_;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        public static Boat.Type byId(int p_38431_) {
            return (Boat.Type) BY_ID.apply(p_38431_);
        }

        public static Boat.Type byName(String p_38433_) {
            return (Boat.Type) CODEC.byName(p_38433_, OAK);
        }
    }
}