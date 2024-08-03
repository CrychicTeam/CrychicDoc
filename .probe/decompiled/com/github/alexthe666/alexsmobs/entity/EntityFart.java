package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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

public class EntityFart extends Entity {

    private UUID ownerUUID;

    private int ownerNetworkId;

    private boolean leftOwner;

    public EntityFart(EntityType p_i50162_1_, Level p_i50162_2_) {
        super(p_i50162_1_, p_i50162_2_);
    }

    public EntityFart(Level worldIn, LivingEntity p_i47273_2_, boolean right) {
        this(AMEntityRegistry.FART.get(), worldIn);
        this.setShooter(p_i47273_2_);
        float rot = p_i47273_2_.yHeadRot + (float) (right ? 60 : -60);
        this.m_6034_(p_i47273_2_.m_20185_() - (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.sin(rot * (float) (Math.PI / 180.0)), p_i47273_2_.m_20188_() - 0.2F, p_i47273_2_.m_20189_() + (double) p_i47273_2_.m_20205_() * 0.5 * (double) Mth.cos(rot * (float) (Math.PI / 180.0)));
    }

    public EntityFart(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(AMEntityRegistry.FART.get(), world);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vector3d = this.m_20184_();
        HitResult raytraceresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
            this.onImpact(raytraceresult);
        }
        this.updateRotation();
        double d0 = this.m_20185_() + vector3d.x;
        double d1 = this.m_20186_() + vector3d.y;
        double d2 = this.m_20189_() + vector3d.z;
        this.m_20256_(vector3d.scale(0.95F));
        this.m_6034_(d0, d1, d2);
        if (this.f_19797_ > 30) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    protected void onImpact(HitResult result) {
        HitResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult) result);
        } else if (raytraceresult$type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) result);
        }
    }

    protected void onEntityHit(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
            for (int i = 0; i < 10 + this.f_19796_.nextInt(6); i++) {
                this.m_9236_().addParticle(AMParticleRegistry.SMELLY.get(), living.m_20208_(1.0), living.m_20187_(), living.m_20262_(1.0), 0.0, 0.0, 0.0);
            }
            for (Mob nearby : this.m_9236_().m_45976_(Mob.class, living.m_20191_().inflate(15.0))) {
                if (nearby != living && nearby.m_19879_() != living.m_19879_() && !nearby.m_20148_().equals(living.m_20148_()) && !nearby.m_7307_(living) && !living.m_7307_(nearby) && !(living instanceof IHurtableMultipart)) {
                    nearby.m_6703_(living);
                    nearby.setTarget(living);
                }
            }
        }
    }

    protected void onHitBlock(BlockHitResult result) {
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected boolean canHitEntity(Entity hit) {
        if (!hit.isSpectator() && hit.isAlive() && hit.isPickable()) {
            Entity entity = this.getShooter();
            return entity == null || this.leftOwner || !entity.isPassengerOfSameVehicle(hit);
        } else {
            return false;
        }
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

    protected void updateRotation() {
        Vec3 vector3d = this.m_20184_().normalize();
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146926_(lerpRotation(this.f_19860_, (float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI)));
        this.m_146922_(lerpRotation(this.f_19859_, (float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI)));
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.leftOwner = compound.getBoolean("LeftOwner");
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

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = new Vec3(x, y, z).normalize().add(this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy, this.f_19796_.nextGaussian() * 0.0075F * (double) inaccuracy).scale((double) velocity);
        this.m_20256_(vector3d);
        float f = Mth.sqrt((float) vector3d.horizontalDistanceSqr());
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, (double) f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
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
}