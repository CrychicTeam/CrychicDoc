package net.liopyu.entityjs.entities.nonliving.vanilla;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.builders.nonliving.vanilla.BoatJSBuilder;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BoatEntityJS extends Boat implements IAnimatableJSNL {

    protected final BoatJSBuilder builder;

    private final AnimatableInstanceCache getAnimatableInstanceCache;

    private boolean inputLeft;

    private boolean inputRight;

    private boolean inputUp;

    private boolean inputDown;

    private int deltaRotation;

    private float invFriction;

    private float outOfControlTicks;

    private int lerpSteps;

    private double lerpX;

    private double lerpY;

    private double lerpZ;

    private double lerpYRot;

    private double lerpXRot;

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

    private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(BoatEntityJS.class, EntityDataSerializers.INT);

    public BoatEntityJS(BoatJSBuilder builder, EntityType<? extends Boat> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.builder = builder;
        this.getAnimatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public BaseEntityBuilder<?> getBuilder() {
        return this.builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.getAnimatableInstanceCache;
    }

    public String entityName() {
        return this.m_6095_().toString();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_ID_BUBBLE_TIME, 0);
    }

    private boolean checkInWater() {
        AABB aabb = this.m_20191_();
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.minY);
        int l = Mth.ceil(aabb.minY + 0.001);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k1 = i; k1 < j; k1++) {
            for (int l1 = k; l1 < l; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    FluidState fluidstate = this.m_9236_().getFluidState(blockpos$mutableblockpos);
                    if (this.canBoatInFluid(fluidstate)) {
                        float f = (float) l1 + fluidstate.getHeight(this.m_9236_(), blockpos$mutableblockpos);
                        this.waterLevel = Math.max((double) f, this.waterLevel);
                        flag |= aabb.minY < (double) f;
                    }
                }
            }
        }
        return flag;
    }

    @Nullable
    private Boat.Status isUnderwater() {
        AABB aabb = this.m_20191_();
        double d0 = aabb.maxY + 0.001;
        int i = Mth.floor(aabb.minX);
        int j = Mth.ceil(aabb.maxX);
        int k = Mth.floor(aabb.maxY);
        int l = Mth.ceil(d0);
        int i1 = Mth.floor(aabb.minZ);
        int j1 = Mth.ceil(aabb.maxZ);
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int k1 = i; k1 < j; k1++) {
            for (int l1 = k; l1 < l; l1++) {
                for (int i2 = i1; i2 < j1; i2++) {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    FluidState fluidstate = this.m_9236_().getFluidState(blockpos$mutableblockpos);
                    if (this.canBoatInFluid(fluidstate) && d0 < (double) ((float) blockpos$mutableblockpos.m_123342_() + fluidstate.getHeight(this.m_9236_(), blockpos$mutableblockpos))) {
                        if (!fluidstate.isSource()) {
                            return Boat.Status.UNDER_FLOWING_WATER;
                        }
                        flag = true;
                    }
                }
            }
        }
        return flag ? Boat.Status.UNDER_WATER : null;
    }

    private Boat.Status getStatus() {
        Boat.Status boat$status = this.isUnderwater();
        if (boat$status != null) {
            this.waterLevel = this.m_20191_().maxY;
            return boat$status;
        } else if (this.checkInWater()) {
            return Boat.Status.IN_WATER;
        } else {
            float f = this.m_38377_();
            if (f > 0.0F) {
                this.landFriction = f;
                return Boat.Status.ON_LAND;
            } else {
                return Boat.Status.IN_AIR;
            }
        }
    }

    private void tickLerp() {
        if (this.m_6109_()) {
            this.lerpSteps = 0;
            this.m_217006_(this.m_20185_(), this.m_20186_(), this.m_20189_());
        }
        if (this.lerpSteps > 0) {
            double d0 = this.m_20185_() + (this.lerpX - this.m_20185_()) / (double) this.lerpSteps;
            double d1 = this.m_20186_() + (this.lerpY - this.m_20186_()) / (double) this.lerpSteps;
            double d2 = this.m_20189_() + (this.lerpZ - this.m_20189_()) / (double) this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double) this.m_146908_());
            this.m_146922_(this.m_146908_() + (float) d3 / (float) this.lerpSteps);
            this.m_146926_(this.m_146909_() + (float) (this.lerpXRot - (double) this.m_146909_()) / (float) this.lerpSteps);
            this.lerpSteps--;
            this.m_6034_(d0, d1, d2);
            this.m_19915_(this.m_146908_(), this.m_146909_());
        }
    }

    private void floatBoat() {
        double d0 = -0.04F;
        double d1 = this.m_20068_() ? 0.0 : -0.04F;
        double d2 = 0.0;
        this.invFriction = 0.05F;
        if (this.oldStatus == Boat.Status.IN_AIR && this.status != Boat.Status.IN_AIR && this.status != Boat.Status.ON_LAND) {
            this.waterLevel = this.m_20227_(1.0);
            this.m_6034_(this.m_20185_(), (double) (this.m_38371_() - this.m_20206_()) + 0.101, this.m_20189_());
            this.m_20256_(this.m_20184_().multiply(1.0, 0.0, 1.0));
            this.lastYd = 0.0;
            this.status = Boat.Status.IN_WATER;
        } else {
            if (this.status == Boat.Status.IN_WATER) {
                d2 = (this.waterLevel - this.m_20186_()) / (double) this.m_20206_();
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4;
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.UNDER_WATER) {
                d2 = 0.01F;
                this.invFriction = 0.45F;
            } else if (this.status == Boat.Status.IN_AIR) {
                this.invFriction = 0.9F;
            } else if (this.status == Boat.Status.ON_LAND) {
                this.invFriction = this.landFriction;
                if (this.getControllingPassenger() instanceof Player) {
                    this.landFriction /= 2.0F;
                }
            }
            Vec3 vec3 = this.m_20184_();
            this.m_20334_(vec3.x * (double) this.invFriction, vec3.y + d1, vec3.z * (double) this.invFriction);
            this.deltaRotation = this.deltaRotation * (int) this.invFriction;
            if (d2 > 0.0) {
                Vec3 vec31 = this.m_20184_();
                this.m_20334_(vec31.x, (vec31.y + d2 * 0.06153846016296973) * 0.75, vec31.z);
            }
        }
    }

    private int getBubbleTime() {
        return this.f_19804_.get(DATA_ID_BUBBLE_TIME);
    }

    @Override
    public void setBubbleTime(int pBubbleTime) {
        this.f_19804_.set(DATA_ID_BUBBLE_TIME, pBubbleTime);
    }

    private void tickBubbleColumn() {
        if (this.m_9236_().isClientSide) {
            int k = this.getBubbleTime();
            if (k > 0) {
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
            int k = this.getBubbleTime();
            if (k > 0) {
                this.setBubbleTime(--k);
                int j = 60 - k - 1;
                if (j > 0 && k == 0) {
                    this.setBubbleTime(0);
                    Vec3 vec3 = this.m_20184_();
                    if (this.bubbleColumnDirectionIsDown) {
                        this.m_20256_(vec3.add(0.0, -0.7, 0.0));
                        this.m_20153_();
                    } else {
                        this.m_20334_(vec3.x, this.m_146862_(p_150274_ -> p_150274_ instanceof Player) ? 2.7 : 0.6, vec3.z);
                    }
                }
                this.isAboveBubbleColumn = false;
            }
        }
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
        if (this.m_38385_() > 0) {
            this.m_38354_(this.m_38385_() - 1);
        }
        if (this.m_38384_() > 0.0F) {
            this.m_38311_(this.m_38384_() - 1.0F);
        }
        if (this.builder.tick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
        super.m_6075_();
        this.tickLerp();
        if (this.m_6109_()) {
            if (!(this.m_146895_() instanceof Player)) {
                this.m_38339_(false, false);
            }
            this.floatBoat();
            if (this.m_9236_().isClientSide) {
                this.controlBoat();
                this.m_9236_().sendPacketToServer(new ServerboundPaddleBoatPacket(this.m_38313_(0), this.m_38313_(1)));
            }
            this.move(MoverType.SELF, this.m_20184_());
        } else {
            this.m_20256_(Vec3.ZERO);
        }
        this.tickBubbleColumn();
        this.m_20101_();
        List<Entity> list = this.m_9236_().getEntities(this, this.m_20191_().inflate(0.2F, -0.01F, 0.2F), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.m_9236_().isClientSide && !(this.getControllingPassenger() instanceof Player);
            for (int j = 0; j < list.size(); j++) {
                Entity entity = (Entity) list.get(j);
                if (!entity.hasPassenger(this)) {
                    if (flag && this.m_20197_().size() < this.m_213801_() && !entity.isPassenger() && this.m_271938_(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.m_7334_(entity);
                    }
                }
            }
        }
    }

    @Override
    public void setInput(boolean pInputLeft, boolean pInputRight, boolean pInputUp, boolean pInputDown) {
        this.inputLeft = pInputLeft;
        this.inputRight = pInputRight;
        this.inputUp = pInputUp;
        this.inputDown = pInputDown;
    }

    @Override
    public void controlBoat() {
        if (this.m_20160_()) {
            float f = 0.0F;
            if (this.inputLeft) {
                this.deltaRotation--;
            }
            if (this.inputRight) {
                this.deltaRotation++;
            }
            if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
                if (this.builder.turningBoatSpeed != null) {
                    Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.turningBoatSpeed.apply(this), "float");
                    if (obj != null) {
                        f += (Float) obj;
                    } else {
                        EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for turningBoatSpeed from entity: " + this.entityName() + ". Value: " + obj + ". Must be a float. Defaulting to 0.005");
                        f += 0.005F;
                    }
                } else {
                    f += 0.005F;
                }
            }
            this.m_146922_(this.m_146908_() + (float) this.deltaRotation);
            if (this.inputUp) {
                if (this.builder.forwardBoatSpeed != null) {
                    Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.forwardBoatSpeed.apply(this), "float");
                    if (obj != null) {
                        f += (Float) obj;
                    } else {
                        EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for forwardBoatSpeed from entity: " + this.entityName() + ". Value: " + obj + ". Must be a float. Defaulting to 0.04");
                        f += 0.04F;
                    }
                } else {
                    f += 0.04F;
                }
            }
            if (this.inputDown) {
                if (this.builder.backwardsBoatSpeed != null) {
                    Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.backwardsBoatSpeed.apply(this), "float");
                    if (obj != null) {
                        f -= (Float) obj;
                    } else {
                        EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for backwardsBoatSpeed from entity: " + this.entityName() + ". Value: " + obj + ". Must be a float. Defaulting to 0.005");
                        f -= 0.005F;
                    }
                } else {
                    f -= 0.005F;
                }
            }
            this.m_20256_(this.m_20184_().add((double) (Mth.sin(-this.m_146908_() * (float) (Math.PI / 180.0)) * f), 0.0, (double) (Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * f)));
            this.m_38339_(this.inputRight && !this.inputLeft || this.inputUp, this.inputLeft && !this.inputRight || this.inputUp);
        }
    }

    @Override
    public Item getDropItem() {
        if (this.builder.getDropItem != null) {
            Object obj = this.builder.getDropItem.apply(this);
            if (obj instanceof Item) {
                return (Item) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for getDropItem in builder: " + obj + ". Must be an Item. Defaulting to super method: " + super.getDropItem());
        }
        return super.getDropItem();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.builder.onHurt != null) {
            ContextUtils.EntityHurtContext context = new ContextUtils.EntityHurtContext(this, pSource, pAmount);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.lerpTo(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        if (this.builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(x, y, z, yaw, pitch, posRotationIncrements, teleport, this);
            EntityJSHelperClass.consumerCallback(this.builder.lerpTo, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: lerpTo.");
        }
    }

    @Override
    public void move(MoverType pType, Vec3 pPos) {
        super.m_6478_(pType, pPos);
        if (this.builder.move != null) {
            ContextUtils.MovementContext context = new ContextUtils.MovementContext(pType, pPos, this);
            EntityJSHelperClass.consumerCallback(this.builder.move, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: move.");
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.builder != null && this.builder.playerTouch != null) {
            ContextUtils.EntityPlayerContext context = new ContextUtils.EntityPlayerContext(player, this);
            EntityJSHelperClass.consumerCallback(this.builder.playerTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: playerTouch.");
        } else {
            super.m_6123_(player);
        }
    }

    public void onRemovedFromWorld() {
        if (this.builder != null && this.builder.onRemovedFromWorld != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onRemovedFromWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onRemovedFromWorld.");
        }
        super.onRemovedFromWorld();
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.EThunderHitContext context = new ContextUtils.EThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource damageSource) {
        if (this.builder.onFall != null) {
            ContextUtils.EEntityFallDamageContext context = new ContextUtils.EEntityFallDamageContext(this, damageMultiplier, distance, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.m_142535_(distance, damageMultiplier, damageSource);
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.m_6858_(sprinting);
    }

    @Override
    public void stopRiding() {
        super.m_8127_();
        if (this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    @Override
    public void rideTick() {
        super.m_6083_();
        if (this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    @Override
    public void onClientRemoval() {
        if (this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    @Override
    public void lavaHurt() {
        if (this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    @Override
    protected void onFlap() {
        if (this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        if (this.builder.shouldRenderAtSqrDistance != null) {
            ContextUtils.EntitySqrDistanceContext context = new ContextUtils.EntitySqrDistanceContext(distance, this);
            Object obj = this.builder.shouldRenderAtSqrDistance.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid shouldRenderAtSqrDistance for arrow builder: " + obj + ". Must be a boolean. Defaulting to super method: " + super.m_6783_(distance));
        }
        return super.m_6783_(distance);
    }

    @Override
    public boolean isAttackable() {
        return this.builder.isAttackable;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity var10000;
        if (this.m_146895_() instanceof LivingEntity entity) {
            var10000 = entity;
        } else {
            var10000 = null;
        }
        return var10000;
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.builder.canCollideWith != null) {
            ContextUtils.ECollidingEntityContext context = new ContextUtils.ECollidingEntityContext(this, pEntity);
            Object obj = this.builder.canCollideWith.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canCollideWith from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canCollideWith(pEntity));
        }
        return super.canCollideWith(pEntity);
    }

    @Override
    protected float getBlockJumpFactor() {
        if (this.builder.setBlockJumpFactor == null) {
            return super.m_20098_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setBlockJumpFactor.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setBlockJumpFactor from entity: " + this.entityName() + ". Value: " + this.builder.setBlockJumpFactor.apply(this) + ". Must be a float. Defaulting to " + super.m_20098_());
                return super.m_20098_();
            }
        }
    }

    @Override
    public boolean isPushable() {
        return this.builder.isPushable;
    }

    @Override
    protected float getBlockSpeedFactor() {
        if (this.builder.blockSpeedFactor == null) {
            return super.m_6041_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.blockSpeedFactor.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for blockSpeedFactor from entity: " + this.entityName() + ". Value: " + this.builder.blockSpeedFactor.apply(this) + ". Must be a float, defaulting to " + super.m_6041_());
                return super.m_6041_();
            }
        }
    }

    @Override
    protected void positionRider(Entity pPassenger, Entity.MoveFunction pCallback) {
        if (this.builder.positionRider != null) {
            ContextUtils.PositionRiderContext context = new ContextUtils.PositionRiderContext(this, pPassenger, pCallback);
            EntityJSHelperClass.consumerCallback(this.builder.positionRider, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: positionRider.");
        } else {
            super.positionRider(pPassenger, pCallback);
        }
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        if (this.builder.canAddPassenger == null) {
            return super.canAddPassenger(entity);
        } else {
            ContextUtils.EPassengerEntityContext context = new ContextUtils.EPassengerEntityContext(entity, this);
            Object obj = this.builder.canAddPassenger.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAddPassenger from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.canAddPassenger(entity));
                return super.canAddPassenger(entity);
            }
        }
    }

    @Override
    protected boolean isFlapping() {
        if (this.builder.isFlapping != null) {
            Object obj = this.builder.isFlapping.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFlapping from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142039_());
        }
        return super.m_142039_();
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.repositionEntityAfterLoad, () -> super.m_6093_());
    }

    @Override
    protected float nextStep() {
        if (this.builder.nextStep != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.nextStep.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for nextStep from entity: " + this.entityName() + ". Value: " + this.builder.nextStep.apply(this) + ". Must be a float, defaulting to " + super.m_6059_());
        }
        return super.m_6059_();
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return this.builder.setSwimSplashSound == null ? super.m_5509_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSplashSound));
    }

    @Override
    protected SoundEvent getSwimSound() {
        return this.builder.setSwimSound == null ? super.m_5501_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSound));
    }

    @Override
    public boolean canFreeze() {
        if (this.builder.canFreeze != null) {
            Object obj = this.builder.canFreeze.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFreeze from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142079_());
        }
        return super.m_142079_();
    }

    @Override
    public boolean isFreezing() {
        if (this.builder.isFreezing != null) {
            Object obj = this.builder.isFreezing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFreezing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_203117_());
        }
        return super.m_203117_();
    }

    @Override
    public boolean isCurrentlyGlowing() {
        if (this.builder.isCurrentlyGlowing != null && !this.m_9236_().isClientSide()) {
            Object obj = this.builder.isCurrentlyGlowing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isCurrentlyGlowing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142038_());
        }
        return super.m_142038_();
    }

    @Override
    public boolean dampensVibrations() {
        if (this.builder.dampensVibrations != null) {
            Object obj = this.builder.dampensVibrations.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for dampensVibrations from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_213854_());
        }
        return super.m_213854_();
    }

    @Override
    public boolean showVehicleHealth() {
        if (this.builder.showVehicleHealth != null) {
            Object obj = this.builder.showVehicleHealth.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for showVehicleHealth from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_20152_());
        }
        return super.m_20152_();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        if (this.builder.isInvulnerableTo != null) {
            ContextUtils.EDamageContext context = new ContextUtils.EDamageContext(this, damageSource0);
            Object obj = this.builder.isInvulnerableTo.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isInvulnerableTo from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6673_(damageSource0));
        }
        return super.m_6673_(damageSource0);
    }

    @Override
    public boolean canChangeDimensions() {
        if (this.builder.canChangeDimensions != null) {
            Object obj = this.builder.canChangeDimensions.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canChangeDimensions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6072_());
        }
        return super.m_6072_();
    }

    @Override
    public boolean mayInteract(@NotNull Level level0, @NotNull BlockPos blockPos1) {
        if (this.builder.mayInteract != null) {
            ContextUtils.EMayInteractContext context = new ContextUtils.EMayInteractContext(level0, blockPos1, this);
            Object obj = this.builder.mayInteract.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for mayInteract from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142265_(level0, blockPos1));
        }
        return super.m_142265_(level0, blockPos1);
    }

    public boolean canTrample(@NotNull BlockState state, @NotNull BlockPos pos, float fallDistance) {
        if (this.builder.canTrample != null) {
            ContextUtils.ECanTrampleContext context = new ContextUtils.ECanTrampleContext(state, pos, fallDistance, this);
            Object obj = this.builder.canTrample.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTrample from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canTrample(state, pos, fallDistance));
        }
        return super.canTrample(state, pos, fallDistance);
    }

    @Override
    public int getMaxFallDistance() {
        if (this.builder.setMaxFallDistance == null) {
            return super.m_6056_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setMaxFallDistance.apply(this), "integer");
            if (obj != null) {
                return (Integer) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setMaxFallDistance from entity: " + this.entityName() + ". Value: " + this.builder.setMaxFallDistance.apply(this) + ". Must be an integer. Defaulting to " + super.m_6056_());
                return super.m_6056_();
            }
        }
    }
}