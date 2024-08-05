package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

public class EntityGust extends Entity {

    protected static final EntityDataAccessor<Boolean> VERTICAL = SynchedEntityData.defineId(EntityGust.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Float> X_DIR = SynchedEntityData.defineId(EntityGust.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> Y_DIR = SynchedEntityData.defineId(EntityGust.class, EntityDataSerializers.FLOAT);

    protected static final EntityDataAccessor<Float> Z_DIR = SynchedEntityData.defineId(EntityGust.class, EntityDataSerializers.FLOAT);

    private Entity pushedEntity = null;

    public EntityGust(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityGust(Level worldIn) {
        this(AMEntityRegistry.GUST.get(), worldIn);
    }

    public EntityGust(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.GUST.get(), world);
    }

    @Override
    public void push(Entity entityIn) {
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

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ > 300) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        for (int i = 0; i < 1 + this.f_19796_.nextInt(1); i++) {
            this.m_9236_().addParticle(AMParticleRegistry.GUSTER_SAND_SPIN.get(), this.m_20185_() + (double) (0.5F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20186_() + (double) (0.5F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20189_() + (double) (0.5F * (this.f_19796_.nextFloat() - 0.5F)), this.m_20185_(), this.m_20186_() + 0.5, this.m_20189_());
        }
        Vec3 vector3d = new Vec3((double) this.f_19804_.get(X_DIR).floatValue(), (double) this.f_19804_.get(Y_DIR).floatValue(), (double) this.f_19804_.get(Z_DIR).floatValue());
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && this.f_19797_ > 4) {
            this.onImpact(raytraceresult);
        }
        List<Entity> list = this.m_9236_().m_45976_(Entity.class, this.m_20191_().inflate(0.1));
        if (this.pushedEntity != null && this.m_20270_(this.pushedEntity) > 2.0F) {
            this.pushedEntity = null;
        }
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        if (this.m_20186_() > (double) this.m_9236_().m_151558_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        this.updateRotation();
        if (this.m_20072_()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            this.m_20256_(vector3d);
            this.m_20256_(this.m_20184_().add(0.0, -0.06F, 0.0));
            this.m_6034_(d0, d1, d2);
            if (this.pushedEntity != null) {
                this.pushedEntity.setDeltaMovement(this.m_20184_().add(0.0, 0.063, 0.0));
            }
            for (Entity e : list) {
                e.setDeltaMovement(this.m_20184_().add(0.0, 0.068, 0.0));
                if (e.getDeltaMovement().y < 0.0) {
                    e.setDeltaMovement(e.getDeltaMovement().multiply(1.0, 0.0, 1.0));
                }
                e.fallDistance = 0.0F;
            }
        }
    }

    public void setGustDir(float x, float y, float z) {
        this.f_19804_.set(X_DIR, x);
        this.f_19804_.set(Y_DIR, y);
        this.f_19804_.set(Z_DIR, z);
    }

    public float getGustDir(int xyz) {
        return this.f_19804_.get(xyz == 2 ? Z_DIR : (xyz == 1 ? Y_DIR : X_DIR));
    }

    protected void onEntityHit(EntityHitResult result) {
        Entity entity = result.getEntity();
        if (entity instanceof EntityGust other) {
            double avgX = (other.m_20185_() + this.m_20185_()) / 2.0;
            double avgY = (other.m_20186_() + this.m_20186_()) / 2.0;
            double avgZ = (other.m_20189_() + this.m_20189_()) / 2.0;
            other.m_6034_(avgX, avgY, avgZ);
            other.setGustDir(other.getGustDir(0) + this.getGustDir(0), other.getGustDir(1) + this.getGustDir(1), other.getGustDir(2) + this.getGustDir(2));
            if (this.m_6084_() && other.m_6084_()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        } else if (entity != null) {
            this.pushedEntity = entity;
        }
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        return !p_230298_1_.isSpectator();
    }

    protected void onHitBlock(BlockHitResult p_230299_1_) {
        if (p_230299_1_.getBlockPos() != null) {
            BlockPos pos = p_230299_1_.getBlockPos();
            if (this.m_9236_().m_46801_(pos) && !this.m_9236_().isClientSide) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(VERTICAL, false);
        this.f_19804_.define(X_DIR, 0.0F);
        this.f_19804_.define(Y_DIR, 0.0F);
        this.f_19804_.define(Z_DIR, 0.0F);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putBoolean("VerticalTornado", this.getVertical());
        compound.putFloat("GustDirX", this.f_19804_.get(X_DIR));
        compound.putFloat("GustDirY", this.f_19804_.get(Y_DIR));
        compound.putFloat("GustDirZ", this.f_19804_.get(Z_DIR));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.f_19804_.set(X_DIR, compound.getFloat("GustDirX"));
        this.f_19804_.set(Y_DIR, compound.getFloat("GustDirX"));
        this.f_19804_.set(Z_DIR, compound.getFloat("GustDirX"));
        this.setVertical(compound.getBoolean("VerticalTornado"));
    }

    public void setVertical(boolean vertical) {
        this.f_19804_.set(VERTICAL, vertical);
    }

    public boolean getVertical() {
        return this.f_19804_.get(VERTICAL);
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

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_();
        float f = Mth.sqrt((float) (vector3d.x * vector3d.x + vector3d.z * vector3d.z));
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
    }
}