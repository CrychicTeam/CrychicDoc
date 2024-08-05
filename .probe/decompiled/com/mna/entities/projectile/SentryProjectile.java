package com.mna.entities.projectile;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.EntityInit;
import com.mna.tools.TeleportHelper;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class SentryProjectile extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<Byte> AFFINITY = SynchedEntityData.defineId(SentryProjectile.class, EntityDataSerializers.BYTE);

    private int ticksInAir;

    private Entity owner;

    public SentryProjectile(EntityType<? extends SentryProjectile> type, Level world) {
        super(EntityInit.SENTRY_PROJECTILE.get(), world);
        this.m_20242_(true);
    }

    public SentryProjectile(Level worldIn, double x, double y, double z, Affinity affinity) {
        super(EntityInit.SENTRY_PROJECTILE.get(), worldIn);
        this.m_6034_(x, y, z);
        this.setAffinity(affinity);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_213868_1_) {
        if (p_213868_1_.getEntity() instanceof LivingEntity livingentity) {
            this.entityHit(livingentity);
        }
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }

    protected void entityHit(LivingEntity living) {
        if (!this.m_9236_().isClientSide()) {
            switch(this.getAffinity()) {
                case EARTH:
                    living.hurt(living.m_269291_().generic(), 5.0F);
                    break;
                case ENDER:
                    this.teleportBackwards(living);
                    break;
                case FIRE:
                case HELLFIRE:
                case LIGHTNING:
                    living.hurt(living.m_269291_().inFire(), 5.0F);
                    living.m_20254_(3);
                    break;
                case WATER:
                case ICE:
                    living.hurt(living.m_269291_().magic(), 5.0F);
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3, false, false, true));
                    break;
                case WIND:
                    living.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60, 0, false, false, true));
                    break;
                case ARCANE:
                case UNKNOWN:
                default:
                    living.hurt(living.m_269291_().magic(), 15.0F);
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    private void teleportBackwards(LivingEntity living) {
        Vec3 targetPos = TeleportHelper.calculateBlinkPosition(10.0, living, this.m_20184_(), this.m_9236_());
        if (targetPos != null) {
            TeleportHelper.teleportEntity(living, this.m_9236_().dimension(), targetPos);
        }
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 Vector3d = new Vec3(x, y, z).normalize().scale((double) velocity);
        this.m_20256_(Vector3d);
        double f = Vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(Vector3d.x, Vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(Vector3d.y, f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    @Nullable
    protected EntityHitResult rayTraceEntities(Vec3 startVec, Vec3 endVec) {
        return ProjectileUtil.getEntityHitResult(this.m_9236_(), this, startVec, endVec, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), p_213871_1_ -> !p_213871_1_.isSpectator() && p_213871_1_.isAlive() && p_213871_1_.isPickable() && (p_213871_1_ != this.getShooter() || this.ticksInAir >= 5));
    }

    @Override
    public void tick() {
        Vec3 Vector3d = this.m_20184_();
        if (this.f_19860_ == 0.0F && this.f_19859_ == 0.0F) {
            double f = Vector3d.horizontalDistance();
            this.m_146922_((float) (Mth.atan2(Vector3d.x, Vector3d.z) * 180.0F / (float) Math.PI));
            this.m_146926_((float) (Mth.atan2(Vector3d.y, f) * 180.0F / (float) Math.PI));
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
        }
        if (this.m_20070_()) {
            this.m_20095_();
        }
        this.ticksInAir++;
        Vec3 Vector3d2 = this.m_20182_();
        Vec3 Vector3d3 = Vector3d2.add(Vector3d);
        HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(Vector3d2, Vector3d3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        if (raytraceresult.getType() != HitResult.Type.MISS) {
            Vector3d3 = raytraceresult.getLocation();
        }
        while (this.m_6084_()) {
            EntityHitResult entityraytraceresult = this.rayTraceEntities(Vector3d2, Vector3d3);
            if (entityraytraceresult != null) {
                raytraceresult = entityraytraceresult;
            }
            if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) raytraceresult).getEntity();
                Entity entity1 = this.getShooter();
                if (entity != null && entity1 != null && entity instanceof Player && entity1 instanceof Player && !((Player) entity1).canHarmPlayer((Player) entity)) {
                    raytraceresult = null;
                    entityraytraceresult = null;
                }
            }
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
                this.f_19812_ = true;
            }
            if (entityraytraceresult == null) {
                break;
            }
            raytraceresult = null;
        }
        Vector3d = this.m_20184_();
        double d3 = Vector3d.x;
        double d4 = Vector3d.y;
        double d0 = Vector3d.z;
        double d5 = this.m_20185_() + d3;
        double d1 = this.m_20186_() + d4;
        double d2 = this.m_20189_() + d0;
        double f1 = Vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(d3, d0) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(d4, f1) * 180.0F / (float) Math.PI));
        while (this.m_146909_() - this.f_19860_ < -180.0F) {
            this.f_19860_ -= 360.0F;
        }
        while (this.m_146909_() - this.f_19860_ >= 180.0F) {
            this.f_19860_ += 360.0F;
        }
        while (this.m_146908_() - this.f_19859_ < -180.0F) {
            this.f_19859_ -= 360.0F;
        }
        while (this.m_146908_() - this.f_19859_ >= 180.0F) {
            this.f_19859_ += 360.0F;
        }
        this.m_146926_(Mth.lerp(0.2F, this.f_19860_, this.m_146909_()));
        this.m_146922_(Mth.lerp(0.2F, this.f_19859_, this.m_146908_()));
        float f2 = 0.99F;
        if (this.m_20069_()) {
            for (int j = 0; j < 4; j++) {
                this.m_9236_().addParticle(ParticleTypes.BUBBLE, d5 - d3 * 0.25, d1 - d4 * 0.25, d2 - d0 * 0.25, d3, d4, d0);
            }
            f2 = this.getWaterDrag();
        }
        this.m_20256_(Vector3d.scale((double) f2));
        this.m_6034_(d5, d1, d2);
        this.m_20101_();
        if (this.f_19797_ > 200) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void onHit(HitResult raytraceResultIn) {
        HitResult.Type raytraceresult$type = raytraceResultIn.getType();
        if (raytraceresult$type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) raytraceResultIn);
        }
    }

    private Affinity getAffinity() {
        try {
            return Affinity.values()[this.f_19804_.get(AFFINITY)];
        } catch (Throwable var2) {
            return Affinity.UNKNOWN;
        }
    }

    private void setAffinity(Affinity affinity) {
        this.f_19804_.set(AFFINITY, (byte) affinity.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(AFFINITY, (byte) Affinity.ARCANE.ordinal());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("affinity", this.getAffinity().ordinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("affinity")) {
            try {
                this.setAffinity(Affinity.values()[compound.getInt("affinity")]);
            } catch (Throwable var3) {
                this.setAffinity(Affinity.UNKNOWN);
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

    @OnlyIn(Dist.CLIENT)
    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.m_6034_(x, y, z);
        this.m_19915_(yaw, pitch);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = this.m_20191_().getSize() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 256.0 * m_20150_();
        return distance < d0 * d0;
    }

    public void spawnParticles(int particleCount, float partialTick) {
        Vec3 basePos = this.m_20182_().add(this.m_20184_().scale((double) partialTick));
        float particle_spread = 0.05F;
        MAParticleType particle = null;
        particle = switch(this.getAffinity()) {
            case EARTH ->
                new MAParticleType(ParticleInit.DUST.get());
            case ENDER ->
                new MAParticleType(ParticleInit.ENDER_VELOCITY.get());
            case FIRE, HELLFIRE, LIGHTNING ->
                new MAParticleType(ParticleInit.FLAME.get());
            case WATER, ICE ->
                new MAParticleType(ParticleInit.WATER.get()).setScale(0.03F);
            case WIND ->
                new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.04F).setColor(30, 30, 30);
            default ->
                new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
        };
        for (int j = 0; j < particleCount; j++) {
            Vec3 velocity = new Vec3(0.0, 0.0, 0.0);
            Vec3 pos = basePos.add(this.m_20184_().scale(Math.random()));
            this.m_9236_().addParticle(particle, pos.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, pos.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 0.0F;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Nullable
    public Entity getShooter() {
        return this.owner;
    }

    @Override
    public void setOwner(@Nullable Entity entityIn) {
        this.owner = entityIn;
    }

    protected float getWaterDrag() {
        return 0.6F;
    }
}