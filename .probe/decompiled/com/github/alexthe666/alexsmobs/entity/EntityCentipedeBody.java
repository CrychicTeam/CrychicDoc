package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class EntityCentipedeBody extends Mob implements IHurtableMultipart {

    private static final EntityDataAccessor<Integer> BODYINDEX = SynchedEntityData.defineId(EntityCentipedeBody.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> BODY_XROT = SynchedEntityData.defineId(EntityCentipedeBody.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityCentipedeBody.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityCentipedeBody.class, EntityDataSerializers.OPTIONAL_UUID);

    public EntityDimensions multipartSize;

    protected float radius;

    protected float angleYaw;

    protected float damageMultiplier = 1.0F;

    private double prevHeight = 0.0;

    protected EntityCentipedeBody(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.multipartSize = type.getDimensions();
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.getParent() != null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.f_19817_ = false;
        this.m_20256_(Vec3.ZERO);
        if (this.f_19797_ > 1) {
            Entity parent = this.getParent();
            this.m_6210_();
            if (parent != null && !this.m_9236_().isClientSide) {
                if (parent instanceof LivingEntity parentEntity && (parentEntity.hurtTime > 0 || parentEntity.deathTime > 0)) {
                    AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), 0.0F));
                    this.f_20916_ = parentEntity.hurtTime;
                    this.f_20919_ = parentEntity.deathTime;
                }
                if (parent.isRemoved()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            } else if (!this.m_9236_().isClientSide && this.f_19797_ > 20) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public EntityCentipedeBody(EntityType t, LivingEntity parent, float radius, float angleYaw, float offsetY) {
        super(t, parent.m_9236_());
        this.setParent(parent);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getParentId() != null) {
            compound.putUUID("ParentUUID", this.getParentId());
        }
        if (this.getChildId() != null) {
            compound.putUUID("ChildUUID", this.getChildId());
        }
        compound.putInt("BodyIndex", this.getBodyIndex());
        compound.putFloat("PartAngle", this.angleYaw);
        compound.putFloat("PartRadius", this.radius);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("ParentUUID")) {
            this.setParentId(compound.getUUID("ParentUUID"));
        }
        if (compound.hasUUID("ChildUUID")) {
            this.setChildId(compound.getUUID("ChildUUID"));
        }
        this.setBodyIndex(compound.getInt("BodyIndex"));
        this.angleYaw = compound.getFloat("PartAngle");
        this.radius = compound.getFloat("PartRadius");
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(BODYINDEX, 0);
        this.f_19804_.define(BODY_XROT, 0.0F);
    }

    public Entity getParent() {
        UUID id = this.getParentId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    public Entity getChild() {
        UUID id = this.getChildId();
        return id != null && !this.m_9236_().isClientSide ? ((ServerLevel) this.m_9236_()).getEntity(id) : null;
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity parent = this.getParent();
        boolean prev = parent != null && parent.hurt(source, damage * this.damageMultiplier);
        if (prev && !this.m_9236_().isClientSide) {
            AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), damage * this.damageMultiplier));
        }
        return prev;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && !(entity instanceof EntityCentipedeBody) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public boolean startRiding(Entity entityIn) {
        return !(entityIn instanceof AbstractMinecart) && !(entityIn instanceof Boat) ? super.m_20329_(entityIn) : false;
    }

    public int getBodyIndex() {
        return this.f_19804_.get(BODYINDEX);
    }

    public void setBodyIndex(int index) {
        this.f_19804_.set(BODYINDEX, index);
    }

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ARMOR, 6.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public Vec3 tickMultipartPosition(int headId, float parentOffset, Vec3 parentPosition, float parentXRot, float ourYRot, boolean doHeight) {
        float yDif = doHeight ? 1.0F - 0.95F * (float) Math.min(Math.abs(parentPosition.y - this.m_20186_()), 1.0) : 1.0F;
        Vec3 parentFront = parentPosition.add(this.calcOffsetVec(yDif * parentOffset * this.m_6134_(), parentXRot, ourYRot));
        Vec3 parentButt = parentPosition.add(this.calcOffsetVec(yDif * -parentOffset * this.m_6134_(), parentXRot, ourYRot));
        Vec3 ourButt = parentButt.add(this.calcOffsetVec((yDif * -this.getBackOffset() - 0.5F * this.m_20205_()) * this.m_6134_(), this.getXRot(), ourYRot));
        Vec3 avg = new Vec3((parentButt.x + ourButt.x) / 2.0, (parentButt.y + ourButt.y) / 2.0, (parentButt.z + ourButt.z) / 2.0);
        double d0 = parentButt.x - ourButt.x;
        double d2 = parentButt.z - ourButt.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        double hgt = doHeight ? this.getLowPartHeight(parentButt.x, parentButt.y, parentButt.z) + this.getHighPartHeight(ourButt.x, ourButt.y, ourButt.z) : 0.0;
        if (Math.abs(this.prevHeight - hgt) > 0.2) {
            this.prevHeight = hgt;
        }
        if (!this.isOpaqueBlockAt(parentFront.x, parentFront.y + 0.4F, parentFront.z) && Math.abs(this.prevHeight) > 1.0) {
            this.prevHeight = 0.0;
        }
        double partYDest = Mth.clamp(this.prevHeight, -0.4F, 0.4F);
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
        float rawAngle = Mth.wrapDegrees((float) (-(Mth.atan2(partYDest, d3) * 180.0F / (float) Math.PI)));
        float f2 = this.limitAngle(this.getXRot(), rawAngle, 10.0F);
        this.m_146926_(f2);
        this.f_19804_.set(BODY_XROT, f2);
        this.m_146922_(f);
        this.f_20885_ = f;
        this.m_7678_(avg.x, avg.y, avg.z, f, f2);
        return avg;
    }

    @Override
    public float getXRot() {
        return this.f_19804_.get(BODY_XROT);
    }

    public double getLowPartHeight(double x, double yIn, double z) {
        if (this.isFluidAt(x, yIn, z)) {
            return 0.0;
        } else {
            double checkAt = 0.0;
            while (checkAt > -3.0 && !this.isOpaqueBlockAt(x, yIn + checkAt, z)) {
                checkAt -= 0.2;
            }
            return checkAt;
        }
    }

    public double getHighPartHeight(double x, double yIn, double z) {
        if (this.isFluidAt(x, yIn, z)) {
            return 0.0;
        } else {
            double checkAt = 0.0;
            while (checkAt <= 3.0 && this.isOpaqueBlockAt(x, yIn + checkAt, z)) {
                checkAt += 0.2;
            }
            return checkAt;
        }
    }

    public boolean isFluidAt(double x, double y, double z) {
        return this.f_19794_ ? false : !this.m_9236_().getFluidState(AMBlockPos.fromCoords(x, y, z)).isEmpty();
    }

    public boolean isOpaqueBlockAt(double x, double y, double z) {
        if (this.f_19794_) {
            return false;
        } else {
            float f = 1.0F;
            Vec3 vec3 = new Vec3(x, y, z);
            AABB axisalignedbb = AABB.ofSize(vec3, 1.0, 1.0E-6, 1.0);
            return this.m_9236_().m_45556_(axisalignedbb).filter(Predicate.not(BlockBehaviour.BlockStateBase::m_60795_)).anyMatch(p_185969_ -> {
                BlockPos blockpos = AMBlockPos.fromVec3(vec3);
                return p_185969_.m_60828_(this.m_9236_(), blockpos) && Shapes.joinIsNotEmpty(p_185969_.m_60812_(this.m_9236_(), blockpos).move(vec3.x, vec3.y, vec3.z), Shapes.create(axisalignedbb), BooleanOp.AND);
            });
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    public float getBackOffset() {
        return 0.5F;
    }

    @Override
    public void onAttackedFromServer(LivingEntity parent, float damage, DamageSource damageSource) {
        if (parent.deathTime > 0) {
            this.f_20919_ = parent.deathTime;
        }
        if (parent.hurtTime > 0) {
            this.f_20916_ = parent.hurtTime;
        }
    }
}