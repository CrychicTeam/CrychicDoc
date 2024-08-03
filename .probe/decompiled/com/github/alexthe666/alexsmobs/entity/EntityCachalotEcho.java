package com.github.alexthe666.alexsmobs.entity;

import java.util.UUID;
import javax.annotation.Nullable;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class EntityCachalotEcho extends Entity {

    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(EntityCachalotEcho.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> FASTER_ANIM = SynchedEntityData.defineId(EntityCachalotEcho.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> GREEN = SynchedEntityData.defineId(EntityCachalotEcho.class, EntityDataSerializers.BOOLEAN);

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    private boolean playerLaunched = false;

    public EntityCachalotEcho(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityCachalotEcho(Level worldIn, EntityCachalotWhale p_i47273_2_) {
        this(AMEntityRegistry.CACHALOT_ECHO.get(), worldIn);
        this.setShooter(p_i47273_2_);
    }

    public EntityCachalotEcho(Level worldIn, LivingEntity p_i47273_2_, boolean right, boolean green) {
        this(AMEntityRegistry.CACHALOT_ECHO.get(), worldIn);
        this.setShooter(p_i47273_2_);
        float rot = p_i47273_2_.yHeadRot + (float) (right ? 90 : -90);
        this.playerLaunched = true;
        this.setGreen(green);
        this.setFasterAnimation(true);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), p_i47273_2_.m_20186_() + 1.0, p_i47273_2_.m_20189_() + (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
    }

    @OnlyIn(Dist.CLIENT)
    public EntityCachalotEcho(Level worldIn, double x, double y, double z, double p_i47274_8_, double p_i47274_10_, double p_i47274_12_) {
        this(AMEntityRegistry.CACHALOT_ECHO.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20334_(p_i47274_8_, p_i47274_10_, p_i47274_12_);
    }

    public EntityCachalotEcho(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.CACHALOT_ECHO.get(), world);
    }

    protected static float lerpRotation(float p_234614_0_, float p_234614_1_) {
        while (p_234614_1_ - p_234614_0_ < -180.0F) {
            p_234614_0_ -= 360.0F;
        }
        while (p_234614_1_ - p_234614_0_ >= 180.0F) {
            p_234614_0_ += 360.0F;
        }
        return Mth.lerp(0.2F, p_234614_0_, p_234614_1_);
    }

    public boolean isReturning() {
        return this.f_19804_.get(RETURNING);
    }

    public void setReturning(boolean returning) {
        this.f_19804_.set(RETURNING, returning);
    }

    public boolean isFasterAnimation() {
        return this.f_19804_.get(FASTER_ANIM);
    }

    public void setFasterAnimation(boolean anim) {
        this.f_19804_.set(FASTER_ANIM, anim);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        double yMot = (double) Mth.sqrt((float) (this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z));
        this.m_146926_((float) (Mth.atan2(this.m_20184_().y, yMot) * 180.0F / (float) Math.PI));
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        if (this.isReturning() && this.getOwner() instanceof EntityCachalotWhale whale && whale.headPart.m_20270_(this) < whale.headPart.m_20205_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            whale.recieveEcho();
        }
        if (!this.playerLaunched && !this.m_9236_().isClientSide && !this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (this.f_19797_ > 100) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        this.updateRotation();
        if (this.playerLaunched) {
            this.f_19794_ = true;
        }
        this.m_20256_(vector3d.scale(0.99F));
        this.m_20242_(true);
        this.m_6034_(d0, d1, d2);
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI) - 90.0F);
    }

    protected void onEntityHit(EntityHitResult result) {
        Entity entity = this.getOwner();
        if (this.isReturning()) {
            EntityCachalotWhale whale = null;
            if (entity instanceof EntityCachalotWhale var11 && (result.getEntity() instanceof EntityCachalotWhale || result.getEntity() instanceof EntityCachalotPart)) {
                var11.recieveEcho();
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else if (result.getEntity() != entity && !result.getEntity().is(entity)) {
            this.setReturning(true);
            if (entity instanceof EntityCachalotWhale) {
                Vec3 vec = ((EntityCachalotWhale) entity).getReturnEchoVector();
                double d0 = vec.x() - this.m_20185_();
                double d1 = vec.y() - this.m_20186_();
                double d2 = vec.z() - this.m_20189_();
                this.m_20256_(Vec3.ZERO);
                EntityCachalotEcho echo = new EntityCachalotEcho(this.m_9236_(), (EntityCachalotWhale) entity);
                echo.m_20359_(this);
                this.m_142687_(Entity.RemovalReason.DISCARDED);
                echo.setReturning(true);
                echo.shoot(d0, d1, d2, 1.0F, 0.0F);
                if (!this.m_9236_().isClientSide) {
                    this.m_9236_().m_7967_(echo);
                }
            }
        }
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        if (!this.m_9236_().isClientSide && !this.playerLaunched) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(RETURNING, false);
        this.f_19804_.define(FASTER_ANIM, false);
        this.f_19804_.define(GREEN, false);
    }

    public void setShooter(@Nullable Entity entityIn) {
        if (entityIn != null) {
            this.ownerUUID = entityIn.getUUID();
            this.ownerNetworkId = entityIn.getId();
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            return ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
        } else {
            return this.ownerNetworkId != 0 ? this.m_9236_().getEntity(this.ownerNetworkId) : null;
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
        compound.putBoolean("Green", this.isGreen());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.setGreen(compound.getBoolean("Green"));
        this.leftOwner = compound.getBoolean("LeftOwner");
    }

    private boolean checkLeftOwner() {
        Entity entity = this.getOwner();
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
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075 * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075 * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075 * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) this.horizontalMag(vector3d));
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    private double horizontalMag(Vec3 vector3d) {
        return vector3d.x * vector3d.x + vector3d.z * vector3d.z;
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f3 = p_234612_3_ * (float) (Math.PI / 180.0);
        float f0 = Mth.cos(p_234612_2_ * (float) (Math.PI / 180.0));
        float f = -Mth.sin(f3) * f0;
        float f1 = -Mth.sin((p_234612_2_ + p_234612_4_) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(f3) * f0;
        this.shoot((double) f, (double) f1, (double) f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.getDeltaMovement();
        this.m_20256_(this.m_20184_().add(vector3d.x, p_234612_1_.onGround() ? 0.0 : vector3d.y, vector3d.z));
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (!this.playerLaunched) {
            if (raytraceresult$type == HitResult.Type.ENTITY) {
                this.onEntityHit((EntityHitResult) result);
            } else if (raytraceresult$type == HitResult.Type.BLOCK) {
                this.onHitBlock((BlockHitResult) result);
            }
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

    protected boolean canHitEntity(Entity p_230298_1_) {
        if (this.playerLaunched) {
            return false;
        } else if (this.isReturning()) {
            return p_230298_1_ instanceof EntityCachalotPart || p_230298_1_ instanceof EntityCachalotWhale;
        } else if (p_230298_1_ instanceof EntityCachalotPart) {
            return false;
        } else if (!p_230298_1_.isSpectator() && p_230298_1_.isAlive() && p_230298_1_.isPickable()) {
            Entity entity = this.getOwner();
            return entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(p_230298_1_);
        } else {
            return false;
        }
    }

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) this.horizontalMag(vector3d));
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
    }

    public boolean isGreen() {
        return this.f_19804_.get(GREEN);
    }

    public void setGreen(boolean bool) {
        this.f_19804_.set(GREEN, bool);
    }
}