package com.github.alexthe666.alexsmobs.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntitySquidGrapple extends Entity {

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntitySquidGrapple.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(EntitySquidGrapple.class, EntityDataSerializers.DIRECTION);

    private static final EntityDataAccessor<Boolean> WITHDRAWING = SynchedEntityData.defineId(EntitySquidGrapple.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Optional<BlockPos>> ATTACHED_POS = SynchedEntityData.defineId(EntitySquidGrapple.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private int ticksWithdrawing = 0;

    public EntitySquidGrapple(EntityType type, Level level) {
        super(type, level);
    }

    public EntitySquidGrapple(Level worldIn, LivingEntity player, boolean rightHand) {
        this(AMEntityRegistry.SQUID_GRAPPLE.get(), worldIn);
        this.setOwnerId(player.m_20148_());
        float rot = player.yHeadRot + (float) (rightHand ? 60 : -60);
        this.m_6034_(player.m_20185_() - (double) player.m_20205_() * 0.5 * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), player.m_20188_() - 0.2F, player.m_20189_() + (double) player.m_20205_() * 0.5 * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
    }

    public EntitySquidGrapple(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(AMEntityRegistry.SQUID_GRAPPLE.get(), level);
    }

    protected static float lerpRotation(float f2, float f3) {
        while (f3 - f2 < -180.0F) {
            f2 -= 360.0F;
        }
        while (f3 - f2 >= 180.0F) {
            f2 += 360.0F;
        }
        return Mth.lerp(0.2F, f2, f3);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) (vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        this.m_146922_(Mth.wrapDegrees((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI) + 180.0F));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public Direction getAttachmentFacing() {
        return this.f_19804_.get(ATTACHED_FACE);
    }

    public void setAttachmentFacing(Direction direction) {
        this.f_19804_.set(ATTACHED_FACE, direction);
    }

    @Nullable
    public UUID getOwnerId() {
        return (UUID) this.f_19804_.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID uniqueId) {
        this.f_19804_.set(OWNER_UUID, Optional.ofNullable(uniqueId));
    }

    public BlockPos getStuckToPos() {
        return (BlockPos) this.f_19804_.get(ATTACHED_POS).orElse(null);
    }

    public void setStuckToPos(BlockPos harvestedPos) {
        this.f_19804_.set(ATTACHED_POS, Optional.ofNullable(harvestedPos));
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(OWNER_UUID, Optional.empty());
        this.f_19804_.define(ATTACHED_FACE, Direction.DOWN);
        this.f_19804_.define(ATTACHED_POS, Optional.empty());
        this.f_19804_.define(WITHDRAWING, false);
    }

    public Entity getOwner() {
        UUID id = this.getOwnerId();
        if (id != null && !this.m_9236_().isClientSide) {
            return ((ServerLevel) this.m_9236_()).getEntity(id);
        } else {
            return this.getOwnerId() == null ? null : this.m_9236_().m_46003_(this.getOwnerId());
        }
    }

    public boolean isWithdrawing() {
        return this.f_19804_.get(WITHDRAWING);
    }

    public void setWithdrawing(boolean withdrawing) {
        this.f_19804_.set(WITHDRAWING, withdrawing);
    }

    @Override
    public void tick() {
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = this.m_146908_();
        Entity entity = this.getOwner();
        if (!this.m_9236_().isClientSide) {
            if (entity == null || !entity.isAlive()) {
                this.m_146870_();
            } else if (entity.isShiftKeyDown()) {
                this.setWithdrawing(true);
            }
        }
        if (this.isWithdrawing() && entity != null) {
            super.tick();
            this.ticksWithdrawing++;
            this.setStuckToPos(null);
            Vec3 withDrawTo = entity.getEyePosition().add(0.0, -0.2F, 0.0);
            if (withDrawTo.distanceTo(this.m_20182_()) > 1.2F && this.ticksWithdrawing < 200) {
                Vec3 move = new Vec3(withDrawTo.x - this.m_20185_(), withDrawTo.y - this.m_20186_(), withDrawTo.z - this.m_20189_());
                Vec3 vector3d = move.normalize().scale(1.2);
                this.m_20256_(vector3d.scale(0.99));
                double d0 = this.m_20185_() + vector3d.x;
                double d1 = this.m_20186_() + vector3d.y;
                double d2 = this.m_20189_() + vector3d.z;
                float f = Mth.sqrt((float) (move.x * move.x + move.z * move.z));
                if (!this.m_9236_().isClientSide) {
                    this.m_146922_(Mth.wrapDegrees((float) (-Mth.atan2(move.x, move.z) * 180.0F / (float) Math.PI)) - 180.0F);
                    this.m_146926_((float) (Mth.atan2(move.y, (double) f) * 180.0F / (float) Math.PI));
                    this.f_19859_ = this.m_146908_();
                    this.f_19860_ = this.m_146909_();
                }
                this.m_6034_(d0, d1, d2);
            } else {
                this.m_146870_();
            }
        } else if (!this.m_9236_().isClientSide && !this.m_9236_().m_46805_(this.m_20183_())) {
            this.m_146870_();
        } else if (this.getStuckToPos() == null) {
            super.tick();
            Vec3 vector3d = this.m_20184_();
            HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, newentity -> false);
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
                this.onImpact(raytraceresult);
            }
            this.m_20101_();
            double d0 = this.m_20185_() + vector3d.x;
            double d1 = this.m_20186_() + vector3d.y;
            double d2 = this.m_20189_() + vector3d.z;
            this.updateRotation();
            this.m_20256_(vector3d.scale(0.99));
            if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_) && !this.m_20069_()) {
                this.m_20256_(Vec3.ZERO);
            } else {
                this.m_6034_(d0, d1, d2);
            }
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.1F, 0.0));
            }
        } else {
            BlockState state = this.m_9236_().getBlockState(this.getStuckToPos());
            Vec3 vec3 = new Vec3((double) ((float) this.getStuckToPos().m_123341_() + 0.5F), (double) ((float) this.getStuckToPos().m_123342_() + 0.5F), (double) ((float) this.getStuckToPos().m_123343_() + 0.5F));
            Vec3 offset = new Vec3((double) ((float) this.getAttachmentFacing().getStepX() * 0.55F), (double) ((float) this.getAttachmentFacing().getStepY() * 0.55F), (double) ((float) this.getAttachmentFacing().getStepZ() * 0.55F));
            this.m_146884_(vec3.add(offset));
            float targetX = this.m_146909_();
            float targetY = this.m_146908_();
            switch(this.getAttachmentFacing()) {
                case UP:
                    targetX = 0.0F;
                    break;
                case DOWN:
                    targetX = 180.0F;
                    break;
                case NORTH:
                    targetX = -90.0F;
                    targetY = 0.0F;
                    break;
                case EAST:
                    targetX = -90.0F;
                    targetY = 90.0F;
                    break;
                case SOUTH:
                    targetX = -90.0F;
                    targetY = 180.0F;
                    break;
                case WEST:
                    targetX = -90.0F;
                    targetY = -90.0F;
            }
            this.m_146926_(targetX);
            this.m_146922_(targetY);
            if (entity != null && entity.distanceTo(this) > 2.0F) {
                float entitySwing = 1.0F;
                if (entity instanceof LivingEntity living) {
                    float detract = living.xxa * living.xxa + living.yya * living.yya + living.zza * living.zza;
                    entitySwing = (float) ((double) entitySwing - Math.min(1.0, Math.sqrt((double) detract) * 0.333F));
                }
                Vec3 move = new Vec3(this.m_20185_() - entity.getX(), this.m_20186_() - (double) entity.getEyeHeight() / 2.0 - entity.getY(), this.m_20189_() - entity.getZ());
                entity.setDeltaMovement(entity.getDeltaMovement().add(move.normalize().scale(0.2 * (double) entitySwing)));
                if (!entity.onGround()) {
                    entity.fallDistance = 0.0F;
                }
            }
            if (state.m_60795_()) {
                this.setWithdrawing(true);
            }
        }
    }

    protected float rotlerp(float in, float target, float maxShift) {
        float f = Mth.wrapDegrees(target - in);
        if (f > maxShift) {
            f = maxShift;
        }
        if (f < -maxShift) {
            f = -maxShift;
        }
        float f1 = in + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }
        return f1;
    }

    private void updateRotation() {
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (!this.m_9236_().isClientSide && raytraceresult$type == HitResult.Type.BLOCK && this.getStuckToPos() == null) {
            this.m_20256_(Vec3.ZERO);
            this.setStuckToPos(((BlockHitResult) result).getBlockPos());
            this.setAttachmentFacing(((BlockHitResult) result).getDirection());
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (this.getOwnerId() != null) {
            compound.putUUID("OwnerUUID", this.getOwnerId());
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("OwnerUUID")) {
            this.setOwnerId(compound.getUUID("OwnerUUID"));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}