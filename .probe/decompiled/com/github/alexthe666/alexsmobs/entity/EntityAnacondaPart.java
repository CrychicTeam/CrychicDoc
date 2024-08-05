package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.util.AnacondaPartIndex;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.google.common.collect.ImmutableList;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

public class EntityAnacondaPart extends LivingEntity implements IHurtableMultipart {

    private static final EntityDataAccessor<Integer> BODYINDEX = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> BODY_TYPE = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> TARGET_YAW = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Optional<UUID>> CHILD_UUID = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> PARENT_UUID = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> SWELL = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.FLOAT);

    public EntityDimensions multipartSize;

    private float strangleProgess;

    private float prevSwell;

    private float prevStrangleProgess;

    private int headEntityId = -1;

    private double prevHeight = 0.0;

    private static final EntityDataAccessor<Boolean> YELLOW = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SHEDDING = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> BABY = SynchedEntityData.defineId(EntityAnacondaPart.class, EntityDataSerializers.BOOLEAN);

    public EntityAnacondaPart(EntityType t, Level world) {
        super(t, world);
        this.multipartSize = t.getDimensions();
    }

    public EntityAnacondaPart(EntityType t, LivingEntity parent) {
        super(t, parent.m_9236_());
        this.setParent(parent);
    }

    @Override
    public InteractionResult interact(Player player0, InteractionHand interactionHand1) {
        return this.getParent() == null ? super.m_6096_(player0, interactionHand1) : this.getParent().interact(player0, interactionHand1);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean isNoGravity() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevStrangleProgess = this.strangleProgess;
        this.prevSwell = this.getSwell();
        this.f_19817_ = false;
        this.m_20256_(Vec3.ZERO);
        if (this.f_19797_ > 1) {
            Entity parent = this.getParent();
            this.m_6210_();
            if (!this.m_9236_().isClientSide) {
                if (parent == null) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
                if (parent == null) {
                    if (this.f_19797_ > 20) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    if (parent instanceof LivingEntity livingEntityParent && (livingEntityParent.hurtTime > 0 || livingEntityParent.deathTime > 0)) {
                        AlexsMobs.sendMSGToAll(new MessageHurtMultipart(this.m_19879_(), parent.getId(), 0.0F));
                        this.f_20916_ = livingEntityParent.hurtTime;
                        this.f_20919_ = livingEntityParent.deathTime;
                    }
                    if (parent.isRemoved()) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                }
                if (this.getSwell() > 0.0F) {
                    float swellInc = 0.25F;
                    if (parent instanceof EntityAnaconda || parent instanceof EntityAnacondaPart && ((EntityAnacondaPart) parent).getSwell() == 0.0F) {
                        if (this.getChild() != null) {
                            EntityAnacondaPart child = (EntityAnacondaPart) this.getChild();
                            if (child.getPartType() == AnacondaPartIndex.TAIL) {
                                if (this.getSwell() == 0.25F) {
                                    this.feedAnaconda();
                                }
                            } else {
                                child.setSwell(child.getSwell() + 0.25F);
                            }
                        }
                        this.setSwell(this.getSwell() - 0.25F);
                    }
                }
            }
        }
    }

    private void feedAnaconda() {
        Entity e = this.getParent();
        while (e instanceof EntityAnacondaPart) {
            e = ((EntityAnacondaPart) e).getParent();
        }
        if (e instanceof EntityAnaconda) {
            ((EntityAnaconda) e).feed();
        }
    }

    public Vec3 tickMultipartPosition(int headId, AnacondaPartIndex parentIndex, Vec3 parentPosition, float parentXRot, float parentYRot, float ourYRot, boolean doHeight) {
        Vec3 parentButt = parentPosition.add(this.calcOffsetVec(-parentIndex.getBackOffset() * this.m_6134_(), parentXRot, parentYRot));
        Vec3 ourButt = parentButt.add(this.calcOffsetVec((-this.getPartType().getBackOffset() - 0.5F * this.m_20205_()) * this.m_6134_(), this.m_146909_(), ourYRot));
        Vec3 avg = new Vec3((parentButt.x + ourButt.x) / 2.0, (parentButt.y + ourButt.y) / 2.0, (parentButt.z + ourButt.z) / 2.0);
        double d0 = parentButt.x - ourButt.x;
        double d2 = parentButt.z - ourButt.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        double hgt = doHeight ? this.getLowPartHeight(parentButt.x, parentButt.y, parentButt.z) + this.getHighPartHeight(ourButt.x, ourButt.y, ourButt.z) : 0.0;
        if (Math.abs(hgt - this.prevHeight) > 0.2F) {
            this.prevHeight = hgt;
        }
        double partYDest = Mth.clamp((double) this.m_6134_() * this.prevHeight, -0.6F, 0.6F);
        float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
        float rawAngle = Mth.wrapDegrees((float) (-(Mth.atan2(partYDest, d3) * 180.0F / (float) Math.PI)));
        float f2 = this.limitAngle(this.m_146909_(), rawAngle, 10.0F);
        this.m_146926_(f2);
        this.m_146922_(f);
        this.f_20885_ = f;
        this.m_7678_(avg.x, avg.y, avg.z, f, f2);
        this.headEntityId = headId;
        return avg;
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

    public boolean isOpaqueBlockAt(double x, double y, double z) {
        if (this.f_19794_) {
            return false;
        } else {
            double d = 1.0;
            Vec3 vec3 = new Vec3(x, y, z);
            AABB axisAlignedBB = AABB.ofSize(vec3, 1.0, 1.0E-6, 1.0);
            return this.m_9236_().m_45556_(axisAlignedBB).filter(Predicate.not(BlockBehaviour.BlockStateBase::m_60795_)).anyMatch(p_185969_ -> {
                BlockPos blockpos = AMBlockPos.fromVec3(vec3);
                return p_185969_.m_60828_(this.m_9236_(), blockpos) && Shapes.joinIsNotEmpty(p_185969_.m_60812_(this.m_9236_(), blockpos).move(vec3.x, vec3.y, vec3.z), Shapes.create(axisAlignedBB), BooleanOp.AND);
            });
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public boolean isFluidAt(double x, double y, double z) {
        return this.f_19794_ ? false : !this.m_9236_().getFluidState(AMBlockPos.fromCoords(x, y, z)).isEmpty();
    }

    public boolean hurtHeadId(DamageSource source, float f) {
        if (this.headEntityId != -1) {
            Entity e = this.m_9236_().getEntity(this.headEntityId);
            if (e instanceof EntityAnaconda) {
                return e.hurt(source, f);
            }
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        return this.hurtHeadId(source, damage);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CHILD_UUID, Optional.empty());
        this.f_19804_.define(PARENT_UUID, Optional.empty());
        this.f_19804_.define(BODYINDEX, 0);
        this.f_19804_.define(BODY_TYPE, AnacondaPartIndex.NECK.ordinal());
        this.f_19804_.define(TARGET_YAW, 0.0F);
        this.f_19804_.define(SWELL, 0.0F);
        this.f_19804_.define(YELLOW, false);
        this.f_19804_.define(SHEDDING, false);
        this.f_19804_.define(BABY, false);
    }

    @Override
    public void pushEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> !entity.is(parent) && !(entity instanceof EntityAnacondaPart) && !(entity instanceof EntityAnaconda) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot0, ItemStack itemStack1) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
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

    public Entity getParent() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getParentId();
            if (id != null) {
                return ((ServerLevel) this.m_9236_()).getEntity(id);
            }
        }
        return null;
    }

    public void setParent(Entity entity) {
        this.setParentId(entity.getUUID());
    }

    @Nullable
    public UUID getParentId() {
        return (UUID) this.f_19804_.get(PARENT_UUID).orElse(null);
    }

    public void setParentId(@Nullable UUID uniqueId) {
        this.f_19804_.set(PARENT_UUID, Optional.ofNullable(uniqueId));
    }

    public Entity getChild() {
        if (!this.m_9236_().isClientSide) {
            UUID id = this.getChildId();
            if (id != null) {
                return ((ServerLevel) this.m_9236_()).getEntity(id);
            }
        }
        return null;
    }

    @Nullable
    public UUID getChildId() {
        return (UUID) this.f_19804_.get(CHILD_UUID).orElse(null);
    }

    public void setChildId(@Nullable UUID uniqueId) {
        this.f_19804_.set(CHILD_UUID, Optional.ofNullable(uniqueId));
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
        compound.putInt("BodyModel", this.getPartType().ordinal());
        compound.putInt("BodyIndex", this.getBodyIndex());
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
        this.setPartType(AnacondaPartIndex.fromOrdinal(compound.getInt("BodyModel")));
        this.setBodyIndex(compound.getInt("BodyIndex"));
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.getParent() == entity;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public int getBodyIndex() {
        return this.f_19804_.get(BODYINDEX);
    }

    public void setBodyIndex(int index) {
        this.f_19804_.set(BODYINDEX, index);
    }

    public AnacondaPartIndex getPartType() {
        return AnacondaPartIndex.fromOrdinal(this.f_19804_.get(BODY_TYPE));
    }

    public void setPartType(AnacondaPartIndex index) {
        this.f_19804_.set(BODY_TYPE, index.ordinal());
    }

    public void setTargetYaw(float f) {
        this.f_19804_.set(TARGET_YAW, f);
    }

    public void setSwell(float f) {
        this.f_19804_.set(SWELL, f);
    }

    public float getSwell() {
        return Math.min(this.f_19804_.get(SWELL), 5.0F);
    }

    public float getSwellLerp(float partialTick) {
        return this.prevSwell + (Math.max(this.getSwell(), 0.0F) - this.prevSwell) * partialTick;
    }

    @Override
    public float getYRot() {
        return super.m_146908_();
    }

    public void setStrangleProgress(float f) {
        this.strangleProgess = f;
    }

    public float getStrangleProgress(float partialTick) {
        return this.prevStrangleProgess + (this.strangleProgess - this.prevStrangleProgess) * partialTick;
    }

    public void copyDataFrom(EntityAnaconda anaconda) {
        this.f_19804_.set(YELLOW, anaconda.isYellow());
        this.f_19804_.set(SHEDDING, anaconda.isShedding());
        this.f_19804_.set(BABY, anaconda.m_6162_());
    }

    public boolean isYellow() {
        return this.f_19804_.get(YELLOW);
    }

    public boolean isShedding() {
        return this.f_19804_.get(SHEDDING);
    }

    @Override
    public boolean isBaby() {
        return this.f_19804_.get(BABY);
    }
}