package com.github.alexthe666.alexsmobs.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityMobProjectile extends Entity {

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    public EntityMobProjectile(EntityType type, Level level) {
        super(type, level);
    }

    public EntityMobProjectile(EntityType type, Level worldIn, Mob shooter) {
        this(type, worldIn);
        this.setShooter(shooter);
    }

    protected Vec3 calcOffsetVec(Vec3 offset, float xRot, float yRot) {
        return offset.xRot(xRot * (float) (Math.PI / 180.0)).yRot(-yRot * (float) (Math.PI / 180.0));
    }

    protected static float lerpRotation(float f, float f1) {
        while (f1 - f < -180.0F) {
            f -= 360.0F;
        }
        while (f1 - f >= 180.0F) {
            f += 360.0F;
        }
        return Mth.lerp(0.2F, f, f1);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        this.doBehavior();
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        this.updateRotation();
        if (!this.m_5830_() || this.m_20069_() && !this.removeInWater()) {
            if (this.m_20072_() && this.removeInWater()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                this.m_20256_(vector3d.scale(0.99F));
                this.m_6034_(d0, d1, d2);
            }
        } else {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    protected boolean removeInWater() {
        return true;
    }

    public abstract void doBehavior();

    protected void onEntityHit(EntityHitResult result) {
        Entity entity = this.getShooter();
        if (entity instanceof LivingEntity) {
            boolean var3 = result.getEntity().hurt(this.m_269291_().mobProjectile(this, (LivingEntity) entity), this.getDamage());
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected abstract float getDamage();

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockState blockstate = this.m_9236_().getBlockState(p_230299_1_.getBlockPos());
        if (!this.m_9236_().isClientSide) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Nullable
    public Entity getShooter() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
        } else {
            return this.ownerNetworkId != 0 ? this.m_9236_().getEntity(this.ownerNetworkId) : null;
        }
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.getUUID();
            this.ownerNetworkId = entityIn.getId();
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            compound.putBoolean("LeftOwner", true);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.leftOwner = compound.getBoolean("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getShooter();
        if (entity != null) {
            for (Entity entity1 : this.m_9236_().getEntities(this, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), p_234613_0_ -> !p_234613_0_.isSpectator() && p_234613_0_.isPickable())) {
                if (entity1.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -Mth.sin(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((p_234612_2_ + p_234612_4_) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(p_234612_3_ * (float) (Math.PI / 180.0)) * Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        this.shoot((double) f, (double) f1, (double) f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(vector3d.x, p_234612_1_.onGround() ? 0.0 : vector3d.y, vector3d.z));
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpMotion(double x, double y, double z) {
        this.m_20334_(x, y, z);
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            float f = Mth.sqrt((float) (x * x + z * z));
            this.m_146926_((float) (Mth.atan2(y, (double) f) * 180.0F / (float) Math.PI));
            this.m_146922_((float) (Mth.atan2(x, z) * 180.0F / (float) Math.PI));
            this.f_19860_ = this.m_146909_();
            this.f_19859_ = this.m_146908_();
            this.m_7678_(this.m_20185_(), this.m_20186_(), this.m_20189_(), this.m_146908_(), this.m_146909_());
        }
    }

    protected boolean canHitEntity(Entity inQuestion) {
        if (!inQuestion.isSpectator() && inQuestion.isAlive() && inQuestion.isPickable()) {
            Entity entity = this.getShooter();
            return (entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(inQuestion)) && (entity == null || inQuestion == null || !this.isSameTeam(entity, inQuestion));
        } else {
            return false;
        }
    }

    public boolean isSameTeam(Entity shooter, Entity entity) {
        if (!(shooter instanceof TamableAnimal tamableAnimal) || !tamableAnimal.isTame()) {
            return shooter.isAlliedTo(entity);
        }
        if (entity instanceof TamableAnimal alsoTameable && alsoTameable.isTame() && alsoTameable.getOwnerUUID() != null && tamableAnimal.getOwnerUUID() != null && tamableAnimal.getOwnerUUID().equals(alsoTameable.getOwnerUUID())) {
            return true;
        }
        return tamableAnimal.getOwnerUUID() != null && tamableAnimal.getOwnerUUID().equals(entity.getUUID()) || shooter.isAlliedTo(entity);
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) vector3d.horizontalDistance());
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
    }
}