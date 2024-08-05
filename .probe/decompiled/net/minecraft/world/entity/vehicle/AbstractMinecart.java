package net.minecraft.world.entity.vehicle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractMinecart extends Entity {

    private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_BLOCK = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_ID_CUSTOM_DISPLAY = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.BOOLEAN);

    private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, ImmutableList.of(0, 1, -1), Pose.CROUCHING, ImmutableList.of(0, 1, -1), Pose.SWIMMING, ImmutableList.of(0, 1));

    protected static final float WATER_SLOWDOWN_FACTOR = 0.95F;

    private boolean flipped;

    private boolean onRails;

    private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS = Util.make(Maps.newEnumMap(RailShape.class), p_38135_ -> {
        Vec3i $$1 = Direction.WEST.getNormal();
        Vec3i $$2 = Direction.EAST.getNormal();
        Vec3i $$3 = Direction.NORTH.getNormal();
        Vec3i $$4 = Direction.SOUTH.getNormal();
        Vec3i $$5 = $$1.below();
        Vec3i $$6 = $$2.below();
        Vec3i $$7 = $$3.below();
        Vec3i $$8 = $$4.below();
        p_38135_.put(RailShape.NORTH_SOUTH, Pair.of($$3, $$4));
        p_38135_.put(RailShape.EAST_WEST, Pair.of($$1, $$2));
        p_38135_.put(RailShape.ASCENDING_EAST, Pair.of($$5, $$2));
        p_38135_.put(RailShape.ASCENDING_WEST, Pair.of($$1, $$6));
        p_38135_.put(RailShape.ASCENDING_NORTH, Pair.of($$3, $$8));
        p_38135_.put(RailShape.ASCENDING_SOUTH, Pair.of($$7, $$4));
        p_38135_.put(RailShape.SOUTH_EAST, Pair.of($$4, $$2));
        p_38135_.put(RailShape.SOUTH_WEST, Pair.of($$4, $$1));
        p_38135_.put(RailShape.NORTH_WEST, Pair.of($$3, $$1));
        p_38135_.put(RailShape.NORTH_EAST, Pair.of($$3, $$2));
    });

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    protected AbstractMinecart(EntityType<?> entityType0, Level level1) {
        super(entityType0, level1);
        this.f_19850_ = true;
    }

    protected AbstractMinecart(EntityType<?> entityType0, Level level1, double double2, double double3, double double4) {
        this(entityType0, level1);
        this.m_6034_(double2, double3, double4);
        this.f_19854_ = double2;
        this.f_19855_ = double3;
        this.f_19856_ = double4;
    }

    public static AbstractMinecart createMinecart(Level level0, double double1, double double2, double double3, AbstractMinecart.Type abstractMinecartType4) {
        if (abstractMinecartType4 == AbstractMinecart.Type.CHEST) {
            return new MinecartChest(level0, double1, double2, double3);
        } else if (abstractMinecartType4 == AbstractMinecart.Type.FURNACE) {
            return new MinecartFurnace(level0, double1, double2, double3);
        } else if (abstractMinecartType4 == AbstractMinecart.Type.TNT) {
            return new MinecartTNT(level0, double1, double2, double3);
        } else if (abstractMinecartType4 == AbstractMinecart.Type.SPAWNER) {
            return new MinecartSpawner(level0, double1, double2, double3);
        } else if (abstractMinecartType4 == AbstractMinecart.Type.HOPPER) {
            return new MinecartHopper(level0, double1, double2, double3);
        } else {
            return (AbstractMinecart) (abstractMinecartType4 == AbstractMinecart.Type.COMMAND_BLOCK ? new MinecartCommandBlock(level0, double1, double2, double3) : new Minecart(level0, double1, double2, double3));
        }
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
        this.f_19804_.define(DATA_ID_DISPLAY_BLOCK, Block.getId(Blocks.AIR.defaultBlockState()));
        this.f_19804_.define(DATA_ID_DISPLAY_OFFSET, 6);
        this.f_19804_.define(DATA_ID_CUSTOM_DISPLAY, false);
    }

    @Override
    public boolean canCollideWith(Entity entity0) {
        return Boat.canVehicleCollide(this, entity0);
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
        return 0.0;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity0) {
        Direction $$1 = this.getMotionDirection();
        if ($$1.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(livingEntity0);
        } else {
            int[][] $$2 = DismountHelper.offsetsForDirection($$1);
            BlockPos $$3 = this.m_20183_();
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            ImmutableList<Pose> $$5 = livingEntity0.getDismountPoses();
            UnmodifiableIterator $$14 = $$5.iterator();
            while ($$14.hasNext()) {
                Pose $$6 = (Pose) $$14.next();
                EntityDimensions $$7 = livingEntity0.getDimensions($$6);
                float $$8 = Math.min($$7.width, 1.0F) / 2.0F;
                UnmodifiableIterator $$16 = ((ImmutableList) POSE_DISMOUNT_HEIGHTS.get($$6)).iterator();
                while ($$16.hasNext()) {
                    int $$9 = (Integer) $$16.next();
                    for (int[] $$10 : $$2) {
                        $$4.set($$3.m_123341_() + $$10[0], $$3.m_123342_() + $$9, $$3.m_123343_() + $$10[1]);
                        double $$11 = this.m_9236_().m_45564_(DismountHelper.nonClimbableShape(this.m_9236_(), $$4), () -> DismountHelper.nonClimbableShape(this.m_9236_(), $$4.m_7495_()));
                        if (DismountHelper.isBlockFloorValid($$11)) {
                            AABB $$12 = new AABB((double) (-$$8), 0.0, (double) (-$$8), (double) $$8, (double) $$7.height, (double) $$8);
                            Vec3 $$13 = Vec3.upFromBottomCenterOf($$4, $$11);
                            if (DismountHelper.canDismountTo(this.m_9236_(), livingEntity0, $$12.move($$13))) {
                                livingEntity0.m_20124_($$6);
                                return $$13;
                            }
                        }
                    }
                }
            }
            double $$14x = this.m_20191_().maxY;
            $$4.set((double) $$3.m_123341_(), $$14x, (double) $$3.m_123343_());
            UnmodifiableIterator var22 = $$5.iterator();
            while (var22.hasNext()) {
                Pose $$15 = (Pose) var22.next();
                double $$16 = (double) livingEntity0.getDimensions($$15).height;
                int $$17 = Mth.ceil($$14x - (double) $$4.m_123342_() + $$16);
                double $$18 = DismountHelper.findCeilingFrom($$4, $$17, p_289495_ -> this.m_9236_().getBlockState(p_289495_).m_60812_(this.m_9236_(), p_289495_));
                if ($$14x + $$16 <= $$18) {
                    livingEntity0.m_20124_($$15);
                    break;
                }
            }
            return super.getDismountLocationForPassenger(livingEntity0);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        if (this.m_9236_().isClientSide || this.m_213877_()) {
            return true;
        } else if (this.m_6673_(damageSource0)) {
            return false;
        } else {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.m_5834_();
            this.setDamage(this.getDamage() + float1 * 10.0F);
            this.m_146852_(GameEvent.ENTITY_DAMAGE, damageSource0.getEntity());
            boolean $$2 = damageSource0.getEntity() instanceof Player && ((Player) damageSource0.getEntity()).getAbilities().instabuild;
            if ($$2 || this.getDamage() > 40.0F) {
                this.m_20153_();
                if ($$2 && !this.m_8077_()) {
                    this.m_146870_();
                } else {
                    this.destroy(damageSource0);
                }
            }
            return true;
        }
    }

    @Override
    protected float getBlockSpeedFactor() {
        BlockState $$0 = this.m_9236_().getBlockState(this.m_20183_());
        return $$0.m_204336_(BlockTags.RAILS) ? 1.0F : super.getBlockSpeedFactor();
    }

    public void destroy(DamageSource damageSource0) {
        this.m_6074_();
        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack $$1 = new ItemStack(this.getDropItem());
            if (this.m_8077_()) {
                $$1.setHoverName(this.m_7770_());
            }
            this.m_19983_($$1);
        }
    }

    abstract Item getDropItem();

    @Override
    public void animateHurt(float float0) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    private static Pair<Vec3i, Vec3i> exits(RailShape railShape0) {
        return (Pair<Vec3i, Vec3i>) EXITS.get(railShape0);
    }

    @Override
    public Direction getMotionDirection() {
        return this.flipped ? this.m_6350_().getOpposite().getClockWise() : this.m_6350_().getClockWise();
    }

    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }
        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }
        this.m_146871_();
        this.m_20157_();
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double $$0 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double $$1 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double $$2 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                double $$3 = Mth.wrapDegrees(this.lyr - (double) this.m_146908_());
                this.m_146922_(this.m_146908_() + (float) $$3 / (float) this.lSteps);
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_($$0, $$1, $$2);
                this.m_19915_(this.m_146908_(), this.m_146909_());
            } else {
                this.m_20090_();
                this.m_19915_(this.m_146908_(), this.m_146909_());
            }
        } else {
            if (!this.m_20068_()) {
                double $$4 = this.m_20069_() ? -0.005 : -0.04;
                this.m_20256_(this.m_20184_().add(0.0, $$4, 0.0));
            }
            int $$5 = Mth.floor(this.m_20185_());
            int $$6 = Mth.floor(this.m_20186_());
            int $$7 = Mth.floor(this.m_20189_());
            if (this.m_9236_().getBlockState(new BlockPos($$5, $$6 - 1, $$7)).m_204336_(BlockTags.RAILS)) {
                $$6--;
            }
            BlockPos $$8 = new BlockPos($$5, $$6, $$7);
            BlockState $$9 = this.m_9236_().getBlockState($$8);
            this.onRails = BaseRailBlock.isRail($$9);
            if (this.onRails) {
                this.moveAlongTrack($$8, $$9);
                if ($$9.m_60713_(Blocks.ACTIVATOR_RAIL)) {
                    this.activateMinecart($$5, $$6, $$7, (Boolean) $$9.m_61143_(PoweredRailBlock.POWERED));
                }
            } else {
                this.comeOffTrack();
            }
            this.m_20101_();
            this.m_146926_(0.0F);
            double $$10 = this.f_19854_ - this.m_20185_();
            double $$11 = this.f_19856_ - this.m_20189_();
            if ($$10 * $$10 + $$11 * $$11 > 0.001) {
                this.m_146922_((float) (Mth.atan2($$11, $$10) * 180.0 / Math.PI));
                if (this.flipped) {
                    this.m_146922_(this.m_146908_() + 180.0F);
                }
            }
            double $$12 = (double) Mth.wrapDegrees(this.m_146908_() - this.f_19859_);
            if ($$12 < -170.0 || $$12 >= 170.0) {
                this.m_146922_(this.m_146908_() + 180.0F);
                this.flipped = !this.flipped;
            }
            this.m_19915_(this.m_146908_(), this.m_146909_());
            if (this.getMinecartType() == AbstractMinecart.Type.RIDEABLE && this.m_20184_().horizontalDistanceSqr() > 0.01) {
                List<Entity> $$13 = this.m_9236_().getEntities(this, this.m_20191_().inflate(0.2F, 0.0, 0.2F), EntitySelector.pushableBy(this));
                if (!$$13.isEmpty()) {
                    for (int $$14 = 0; $$14 < $$13.size(); $$14++) {
                        Entity $$15 = (Entity) $$13.get($$14);
                        if (!($$15 instanceof Player) && !($$15 instanceof IronGolem) && !($$15 instanceof AbstractMinecart) && !this.m_20160_() && !$$15.isPassenger()) {
                            $$15.startRiding(this);
                        } else {
                            $$15.push(this);
                        }
                    }
                }
            } else {
                for (Entity $$16 : this.m_9236_().m_45933_(this, this.m_20191_().inflate(0.2F, 0.0, 0.2F))) {
                    if (!this.m_20363_($$16) && $$16.isPushable() && $$16 instanceof AbstractMinecart) {
                        $$16.push(this);
                    }
                }
            }
            this.m_20073_();
            if (this.m_20077_()) {
                this.m_20093_();
                this.f_19789_ *= 0.5F;
            }
            this.f_19803_ = false;
        }
    }

    protected double getMaxSpeed() {
        return (this.m_20069_() ? 4.0 : 8.0) / 20.0;
    }

    public void activateMinecart(int int0, int int1, int int2, boolean boolean3) {
    }

    protected void comeOffTrack() {
        double $$0 = this.getMaxSpeed();
        Vec3 $$1 = this.m_20184_();
        this.m_20334_(Mth.clamp($$1.x, -$$0, $$0), $$1.y, Mth.clamp($$1.z, -$$0, $$0));
        if (this.m_20096_()) {
            this.m_20256_(this.m_20184_().scale(0.5));
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
        if (!this.m_20096_()) {
            this.m_20256_(this.m_20184_().scale(0.95));
        }
    }

    protected void moveAlongTrack(BlockPos blockPos0, BlockState blockState1) {
        this.m_183634_();
        double $$2 = this.m_20185_();
        double $$3 = this.m_20186_();
        double $$4 = this.m_20189_();
        Vec3 $$5 = this.getPos($$2, $$3, $$4);
        $$3 = (double) blockPos0.m_123342_();
        boolean $$6 = false;
        boolean $$7 = false;
        if (blockState1.m_60713_(Blocks.POWERED_RAIL)) {
            $$6 = (Boolean) blockState1.m_61143_(PoweredRailBlock.POWERED);
            $$7 = !$$6;
        }
        double $$8 = 0.0078125;
        if (this.m_20069_()) {
            $$8 *= 0.2;
        }
        Vec3 $$9 = this.m_20184_();
        RailShape $$10 = (RailShape) blockState1.m_61143_(((BaseRailBlock) blockState1.m_60734_()).getShapeProperty());
        switch($$10) {
            case ASCENDING_EAST:
                this.m_20256_($$9.add(-$$8, 0.0, 0.0));
                $$3++;
                break;
            case ASCENDING_WEST:
                this.m_20256_($$9.add($$8, 0.0, 0.0));
                $$3++;
                break;
            case ASCENDING_NORTH:
                this.m_20256_($$9.add(0.0, 0.0, $$8));
                $$3++;
                break;
            case ASCENDING_SOUTH:
                this.m_20256_($$9.add(0.0, 0.0, -$$8));
                $$3++;
        }
        $$9 = this.m_20184_();
        Pair<Vec3i, Vec3i> $$11 = exits($$10);
        Vec3i $$12 = (Vec3i) $$11.getFirst();
        Vec3i $$13 = (Vec3i) $$11.getSecond();
        double $$14 = (double) ($$13.getX() - $$12.getX());
        double $$15 = (double) ($$13.getZ() - $$12.getZ());
        double $$16 = Math.sqrt($$14 * $$14 + $$15 * $$15);
        double $$17 = $$9.x * $$14 + $$9.z * $$15;
        if ($$17 < 0.0) {
            $$14 = -$$14;
            $$15 = -$$15;
        }
        double $$18 = Math.min(2.0, $$9.horizontalDistance());
        $$9 = new Vec3($$18 * $$14 / $$16, $$9.y, $$18 * $$15 / $$16);
        this.m_20256_($$9);
        Entity $$19 = this.m_146895_();
        if ($$19 instanceof Player) {
            Vec3 $$20 = $$19.getDeltaMovement();
            double $$21 = $$20.horizontalDistanceSqr();
            double $$22 = this.m_20184_().horizontalDistanceSqr();
            if ($$21 > 1.0E-4 && $$22 < 0.01) {
                this.m_20256_(this.m_20184_().add($$20.x * 0.1, 0.0, $$20.z * 0.1));
                $$7 = false;
            }
        }
        if ($$7) {
            double $$23 = this.m_20184_().horizontalDistance();
            if ($$23 < 0.03) {
                this.m_20256_(Vec3.ZERO);
            } else {
                this.m_20256_(this.m_20184_().multiply(0.5, 0.0, 0.5));
            }
        }
        double $$24 = (double) blockPos0.m_123341_() + 0.5 + (double) $$12.getX() * 0.5;
        double $$25 = (double) blockPos0.m_123343_() + 0.5 + (double) $$12.getZ() * 0.5;
        double $$26 = (double) blockPos0.m_123341_() + 0.5 + (double) $$13.getX() * 0.5;
        double $$27 = (double) blockPos0.m_123343_() + 0.5 + (double) $$13.getZ() * 0.5;
        $$14 = $$26 - $$24;
        $$15 = $$27 - $$25;
        double $$28;
        if ($$14 == 0.0) {
            $$28 = $$4 - (double) blockPos0.m_123343_();
        } else if ($$15 == 0.0) {
            $$28 = $$2 - (double) blockPos0.m_123341_();
        } else {
            double $$30 = $$2 - $$24;
            double $$31 = $$4 - $$25;
            $$28 = ($$30 * $$14 + $$31 * $$15) * 2.0;
        }
        $$2 = $$24 + $$14 * $$28;
        $$4 = $$25 + $$15 * $$28;
        this.m_6034_($$2, $$3, $$4);
        double $$33 = this.m_20160_() ? 0.75 : 1.0;
        double $$34 = this.getMaxSpeed();
        $$9 = this.m_20184_();
        this.m_6478_(MoverType.SELF, new Vec3(Mth.clamp($$33 * $$9.x, -$$34, $$34), 0.0, Mth.clamp($$33 * $$9.z, -$$34, $$34)));
        if ($$12.getY() != 0 && Mth.floor(this.m_20185_()) - blockPos0.m_123341_() == $$12.getX() && Mth.floor(this.m_20189_()) - blockPos0.m_123343_() == $$12.getZ()) {
            this.m_6034_(this.m_20185_(), this.m_20186_() + (double) $$12.getY(), this.m_20189_());
        } else if ($$13.getY() != 0 && Mth.floor(this.m_20185_()) - blockPos0.m_123341_() == $$13.getX() && Mth.floor(this.m_20189_()) - blockPos0.m_123343_() == $$13.getZ()) {
            this.m_6034_(this.m_20185_(), this.m_20186_() + (double) $$13.getY(), this.m_20189_());
        }
        this.applyNaturalSlowdown();
        Vec3 $$35 = this.getPos(this.m_20185_(), this.m_20186_(), this.m_20189_());
        if ($$35 != null && $$5 != null) {
            double $$36 = ($$5.y - $$35.y) * 0.05;
            Vec3 $$37 = this.m_20184_();
            double $$38 = $$37.horizontalDistance();
            if ($$38 > 0.0) {
                this.m_20256_($$37.multiply(($$38 + $$36) / $$38, 1.0, ($$38 + $$36) / $$38));
            }
            this.m_6034_(this.m_20185_(), $$35.y, this.m_20189_());
        }
        int $$39 = Mth.floor(this.m_20185_());
        int $$40 = Mth.floor(this.m_20189_());
        if ($$39 != blockPos0.m_123341_() || $$40 != blockPos0.m_123343_()) {
            Vec3 $$41 = this.m_20184_();
            double $$42 = $$41.horizontalDistance();
            this.m_20334_($$42 * (double) ($$39 - blockPos0.m_123341_()), $$41.y, $$42 * (double) ($$40 - blockPos0.m_123343_()));
        }
        if ($$6) {
            Vec3 $$43 = this.m_20184_();
            double $$44 = $$43.horizontalDistance();
            if ($$44 > 0.01) {
                double $$45 = 0.06;
                this.m_20256_($$43.add($$43.x / $$44 * 0.06, 0.0, $$43.z / $$44 * 0.06));
            } else {
                Vec3 $$46 = this.m_20184_();
                double $$47 = $$46.x;
                double $$48 = $$46.z;
                if ($$10 == RailShape.EAST_WEST) {
                    if (this.isRedstoneConductor(blockPos0.west())) {
                        $$47 = 0.02;
                    } else if (this.isRedstoneConductor(blockPos0.east())) {
                        $$47 = -0.02;
                    }
                } else {
                    if ($$10 != RailShape.NORTH_SOUTH) {
                        return;
                    }
                    if (this.isRedstoneConductor(blockPos0.north())) {
                        $$48 = 0.02;
                    } else if (this.isRedstoneConductor(blockPos0.south())) {
                        $$48 = -0.02;
                    }
                }
                this.m_20334_($$47, $$46.y, $$48);
            }
        }
    }

    @Override
    public boolean isOnRails() {
        return this.onRails;
    }

    private boolean isRedstoneConductor(BlockPos blockPos0) {
        return this.m_9236_().getBlockState(blockPos0).m_60796_(this.m_9236_(), blockPos0);
    }

    protected void applyNaturalSlowdown() {
        double $$0 = this.m_20160_() ? 0.997 : 0.96;
        Vec3 $$1 = this.m_20184_();
        $$1 = $$1.multiply($$0, 0.0, $$0);
        if (this.m_20069_()) {
            $$1 = $$1.scale(0.95F);
        }
        this.m_20256_($$1);
    }

    @Nullable
    public Vec3 getPosOffs(double double0, double double1, double double2, double double3) {
        int $$4 = Mth.floor(double0);
        int $$5 = Mth.floor(double1);
        int $$6 = Mth.floor(double2);
        if (this.m_9236_().getBlockState(new BlockPos($$4, $$5 - 1, $$6)).m_204336_(BlockTags.RAILS)) {
            $$5--;
        }
        BlockState $$7 = this.m_9236_().getBlockState(new BlockPos($$4, $$5, $$6));
        if (BaseRailBlock.isRail($$7)) {
            RailShape $$8 = (RailShape) $$7.m_61143_(((BaseRailBlock) $$7.m_60734_()).getShapeProperty());
            double1 = (double) $$5;
            if ($$8.isAscending()) {
                double1 = (double) ($$5 + 1);
            }
            Pair<Vec3i, Vec3i> $$9 = exits($$8);
            Vec3i $$10 = (Vec3i) $$9.getFirst();
            Vec3i $$11 = (Vec3i) $$9.getSecond();
            double $$12 = (double) ($$11.getX() - $$10.getX());
            double $$13 = (double) ($$11.getZ() - $$10.getZ());
            double $$14 = Math.sqrt($$12 * $$12 + $$13 * $$13);
            $$12 /= $$14;
            $$13 /= $$14;
            double0 += $$12 * double3;
            double2 += $$13 * double3;
            if ($$10.getY() != 0 && Mth.floor(double0) - $$4 == $$10.getX() && Mth.floor(double2) - $$6 == $$10.getZ()) {
                double1 += (double) $$10.getY();
            } else if ($$11.getY() != 0 && Mth.floor(double0) - $$4 == $$11.getX() && Mth.floor(double2) - $$6 == $$11.getZ()) {
                double1 += (double) $$11.getY();
            }
            return this.getPos(double0, double1, double2);
        } else {
            return null;
        }
    }

    @Nullable
    public Vec3 getPos(double double0, double double1, double double2) {
        int $$3 = Mth.floor(double0);
        int $$4 = Mth.floor(double1);
        int $$5 = Mth.floor(double2);
        if (this.m_9236_().getBlockState(new BlockPos($$3, $$4 - 1, $$5)).m_204336_(BlockTags.RAILS)) {
            $$4--;
        }
        BlockState $$6 = this.m_9236_().getBlockState(new BlockPos($$3, $$4, $$5));
        if (BaseRailBlock.isRail($$6)) {
            RailShape $$7 = (RailShape) $$6.m_61143_(((BaseRailBlock) $$6.m_60734_()).getShapeProperty());
            Pair<Vec3i, Vec3i> $$8 = exits($$7);
            Vec3i $$9 = (Vec3i) $$8.getFirst();
            Vec3i $$10 = (Vec3i) $$8.getSecond();
            double $$11 = (double) $$3 + 0.5 + (double) $$9.getX() * 0.5;
            double $$12 = (double) $$4 + 0.0625 + (double) $$9.getY() * 0.5;
            double $$13 = (double) $$5 + 0.5 + (double) $$9.getZ() * 0.5;
            double $$14 = (double) $$3 + 0.5 + (double) $$10.getX() * 0.5;
            double $$15 = (double) $$4 + 0.0625 + (double) $$10.getY() * 0.5;
            double $$16 = (double) $$5 + 0.5 + (double) $$10.getZ() * 0.5;
            double $$17 = $$14 - $$11;
            double $$18 = ($$15 - $$12) * 2.0;
            double $$19 = $$16 - $$13;
            double $$20;
            if ($$17 == 0.0) {
                $$20 = double2 - (double) $$5;
            } else if ($$19 == 0.0) {
                $$20 = double0 - (double) $$3;
            } else {
                double $$22 = double0 - $$11;
                double $$23 = double2 - $$13;
                $$20 = ($$22 * $$17 + $$23 * $$19) * 2.0;
            }
            double0 = $$11 + $$17 * $$20;
            double1 = $$12 + $$18 * $$20;
            double2 = $$13 + $$19 * $$20;
            if ($$18 < 0.0) {
                double1++;
            } else if ($$18 > 0.0) {
                double1 += 0.5;
            }
            return new Vec3(double0, double1, double2);
        } else {
            return null;
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        AABB $$0 = this.m_20191_();
        return this.hasCustomDisplay() ? $$0.inflate((double) Math.abs(this.getDisplayOffset()) / 16.0) : $$0;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag0) {
        if (compoundTag0.getBoolean("CustomDisplayTile")) {
            this.setDisplayBlockState(NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), compoundTag0.getCompound("DisplayState")));
            this.setDisplayOffset(compoundTag0.getInt("DisplayOffset"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag0) {
        if (this.hasCustomDisplay()) {
            compoundTag0.putBoolean("CustomDisplayTile", true);
            compoundTag0.put("DisplayState", NbtUtils.writeBlockState(this.getDisplayBlockState()));
            compoundTag0.putInt("DisplayOffset", this.getDisplayOffset());
        }
    }

    @Override
    public void push(Entity entity0) {
        if (!this.m_9236_().isClientSide) {
            if (!entity0.noPhysics && !this.f_19794_) {
                if (!this.m_20363_(entity0)) {
                    double $$1 = entity0.getX() - this.m_20185_();
                    double $$2 = entity0.getZ() - this.m_20189_();
                    double $$3 = $$1 * $$1 + $$2 * $$2;
                    if ($$3 >= 1.0E-4F) {
                        $$3 = Math.sqrt($$3);
                        $$1 /= $$3;
                        $$2 /= $$3;
                        double $$4 = 1.0 / $$3;
                        if ($$4 > 1.0) {
                            $$4 = 1.0;
                        }
                        $$1 *= $$4;
                        $$2 *= $$4;
                        $$1 *= 0.1F;
                        $$2 *= 0.1F;
                        $$1 *= 0.5;
                        $$2 *= 0.5;
                        if (entity0 instanceof AbstractMinecart) {
                            double $$5 = entity0.getX() - this.m_20185_();
                            double $$6 = entity0.getZ() - this.m_20189_();
                            Vec3 $$7 = new Vec3($$5, 0.0, $$6).normalize();
                            Vec3 $$8 = new Vec3((double) Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)), 0.0, (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0))).normalize();
                            double $$9 = Math.abs($$7.dot($$8));
                            if ($$9 < 0.8F) {
                                return;
                            }
                            Vec3 $$10 = this.m_20184_();
                            Vec3 $$11 = entity0.getDeltaMovement();
                            if (((AbstractMinecart) entity0).getMinecartType() == AbstractMinecart.Type.FURNACE && this.getMinecartType() != AbstractMinecart.Type.FURNACE) {
                                this.m_20256_($$10.multiply(0.2, 1.0, 0.2));
                                this.m_5997_($$11.x - $$1, 0.0, $$11.z - $$2);
                                entity0.setDeltaMovement($$11.multiply(0.95, 1.0, 0.95));
                            } else if (((AbstractMinecart) entity0).getMinecartType() != AbstractMinecart.Type.FURNACE && this.getMinecartType() == AbstractMinecart.Type.FURNACE) {
                                entity0.setDeltaMovement($$11.multiply(0.2, 1.0, 0.2));
                                entity0.push($$10.x + $$1, 0.0, $$10.z + $$2);
                                this.m_20256_($$10.multiply(0.95, 1.0, 0.95));
                            } else {
                                double $$12 = ($$11.x + $$10.x) / 2.0;
                                double $$13 = ($$11.z + $$10.z) / 2.0;
                                this.m_20256_($$10.multiply(0.2, 1.0, 0.2));
                                this.m_5997_($$12 - $$1, 0.0, $$13 - $$2);
                                entity0.setDeltaMovement($$11.multiply(0.2, 1.0, 0.2));
                                entity0.push($$12 + $$1, 0.0, $$13 + $$2);
                            }
                        } else {
                            this.m_5997_(-$$1, 0.0, -$$2);
                            entity0.push($$1 / 4.0, 0.0, $$2 / 4.0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void lerpTo(double double0, double double1, double double2, float float3, float float4, int int5, boolean boolean6) {
        this.lx = double0;
        this.ly = double1;
        this.lz = double2;
        this.lyr = (double) float3;
        this.lxr = (double) float4;
        this.lSteps = int5 + 2;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double double0, double double1, double double2) {
        this.lxd = double0;
        this.lyd = double1;
        this.lzd = double2;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
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

    public void setHurtDir(int int0) {
        this.f_19804_.set(DATA_ID_HURTDIR, int0);
    }

    public int getHurtDir() {
        return this.f_19804_.get(DATA_ID_HURTDIR);
    }

    public abstract AbstractMinecart.Type getMinecartType();

    public BlockState getDisplayBlockState() {
        return !this.hasCustomDisplay() ? this.getDefaultDisplayBlockState() : Block.stateById(this.m_20088_().get(DATA_ID_DISPLAY_BLOCK));
    }

    public BlockState getDefaultDisplayBlockState() {
        return Blocks.AIR.defaultBlockState();
    }

    public int getDisplayOffset() {
        return !this.hasCustomDisplay() ? this.getDefaultDisplayOffset() : this.m_20088_().get(DATA_ID_DISPLAY_OFFSET);
    }

    public int getDefaultDisplayOffset() {
        return 6;
    }

    public void setDisplayBlockState(BlockState blockState0) {
        this.m_20088_().set(DATA_ID_DISPLAY_BLOCK, Block.getId(blockState0));
        this.setCustomDisplay(true);
    }

    public void setDisplayOffset(int int0) {
        this.m_20088_().set(DATA_ID_DISPLAY_OFFSET, int0);
        this.setCustomDisplay(true);
    }

    public boolean hasCustomDisplay() {
        return this.m_20088_().get(DATA_ID_CUSTOM_DISPLAY);
    }

    public void setCustomDisplay(boolean boolean0) {
        this.m_20088_().set(DATA_ID_CUSTOM_DISPLAY, boolean0);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(switch(this.getMinecartType()) {
            case FURNACE ->
                Items.FURNACE_MINECART;
            case CHEST ->
                Items.CHEST_MINECART;
            case TNT ->
                Items.TNT_MINECART;
            case HOPPER ->
                Items.HOPPER_MINECART;
            case COMMAND_BLOCK ->
                Items.COMMAND_BLOCK_MINECART;
            default ->
                Items.MINECART;
        });
    }

    public static enum Type {

        RIDEABLE,
        CHEST,
        FURNACE,
        TNT,
        SPAWNER,
        HOPPER,
        COMMAND_BLOCK
    }
}