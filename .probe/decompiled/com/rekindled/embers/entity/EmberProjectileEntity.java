package com.rekindled.embers.entity;

import com.rekindled.embers.api.projectile.IProjectileEffect;
import com.rekindled.embers.api.projectile.IProjectilePreset;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.Misc;
import java.awt.Color;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class EmberProjectileEntity extends Projectile {

    public static final EntityDataAccessor<Float> value = SynchedEntityData.defineId(EmberProjectileEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Boolean> dead = SynchedEntityData.defineId(EmberProjectileEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> lifetime = SynchedEntityData.defineId(EmberProjectileEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> color = SynchedEntityData.defineId(EmberProjectileEntity.class, EntityDataSerializers.INT);

    public IProjectileEffect effect;

    private IProjectilePreset preset;

    double gravity;

    int homingTime;

    double homingRange;

    int homingIndex;

    int homingModulo;

    Entity homingTarget;

    Predicate<Entity> homingPredicate;

    public EmberProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_19794_ = true;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(value, 0.0F);
        this.m_20088_().define(dead, false);
        this.m_20088_().define(lifetime, 160);
        this.m_20088_().define(color, new Color(255, 64, 16).getRGB());
    }

    public void shootFromRotation(Entity shooter, float x, float y, float z, float velocity, float inaccuracy, double value) {
        this.f_19815_ = EntityDimensions.scalable((float) value / 10.0F, (float) value / 10.0F);
        this.m_20088_().set(EmberProjectileEntity.value, (float) value);
        this.m_5602_(shooter);
        super.shoot((double) x, (double) y, (double) z, velocity, inaccuracy);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy, double value) {
        this.f_19815_ = EntityDimensions.scalable((float) value / 10.0F, (float) value / 10.0F);
        this.m_20088_().set(EmberProjectileEntity.value, (float) value);
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setColor(int red, int green, int blue, int alpha) {
        this.m_20088_().set(color, new Color(red * alpha / 255, green * alpha / 255, blue * alpha / 255).getRGB());
    }

    public void setHoming(int time, double range, int index, int modulo, Predicate<Entity> predicate) {
        this.homingTime = time;
        this.homingRange = range;
        this.homingIndex = index;
        this.homingModulo = modulo;
        this.homingPredicate = predicate;
    }

    public void setPreset(IProjectilePreset preset) {
        this.preset = preset;
    }

    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public void setLifetime(int lifetime) {
        this.m_20088_().set(EmberProjectileEntity.lifetime, lifetime);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        this.m_20088_().set(value, nbt.getFloat("value"));
        this.m_20088_().set(color, nbt.getInt("color"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putFloat("value", this.m_20088_().get(value));
        nbt.putInt("color", this.m_20088_().get(color));
    }

    @Override
    public void tick() {
        super.tick();
        int lifetime = this.m_20088_().get(EmberProjectileEntity.lifetime);
        this.m_20088_().set(EmberProjectileEntity.lifetime, lifetime - 1);
        if (lifetime <= 0) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        if (!this.m_20088_().get(dead)) {
            this.m_20088_().set(value, this.m_20088_().get(value) - 0.025F);
            if (this.m_20088_().get(value) <= 0.0F) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            Vec3 currPosVec = this.m_20182_();
            Vec3 newPosVector = currPosVec.add(this.m_20184_());
            HitResult raytraceresult = this.m_9236_().m_45547_(new ClipContext(currPosVec, newPosVector.add(this.m_20184_().normalize().scale(1.5)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS) {
                newPosVector = raytraceresult.getLocation();
            }
            HitResult hitEntity = ProjectileUtil.getEntityHitResult(this.m_9236_(), this, currPosVec, newPosVector, this.m_20191_().expandTowards(this.m_20184_()).inflate(1.0), x$0 -> this.m_5603_(x$0));
            if (hitEntity != null) {
                newPosVector = hitEntity.getLocation();
                raytraceresult = hitEntity;
            }
            this.m_6478_(MoverType.SELF, newPosVector.subtract(currPosVec));
            this.m_20256_(this.m_20184_().add(0.0, this.gravity, 0.0));
            if (raytraceresult != null && raytraceresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }
            this.handleHoming(lifetime, this.m_9236_());
            if (this.m_9236_().isClientSide()) {
                double deltaX = this.m_20185_() - currPosVec.x;
                double deltaY = this.m_20186_() - currPosVec.y;
                double deltaZ = this.m_20189_() - currPosVec.z;
                double dist = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 10.0);
                GlowParticleOptions options = new GlowParticleOptions(Misc.colorFromInt(this.m_20088_().get(color)), this.m_20088_().get(value) / 1.75F, 24);
                for (double i = 0.0; i < dist; i++) {
                    double coeff = i / dist;
                    this.m_9236_().addAlwaysVisibleParticle(options, true, currPosVec.x + deltaX * coeff, currPosVec.y + deltaY * coeff, currPosVec.z + deltaZ * coeff, (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)), (double) (0.125F * (this.f_19796_.nextFloat() - 0.5F)));
                }
            }
        } else {
            this.m_20256_(Vec3.ZERO);
        }
    }

    private void handleHoming(int lifetime, Level world) {
        if (this.homingTime > 0) {
            if (!this.isTargetInvalid(this.homingTarget)) {
                double targetX = this.homingTarget.getX();
                double targetY = this.homingTarget.getY() + (double) (this.homingTarget.getBbHeight() / 2.0F);
                double targetZ = this.homingTarget.getZ();
                Vec3 targetVector = new Vec3(targetX - this.m_20185_(), targetY - this.m_20186_(), targetZ - this.m_20189_());
                double length = targetVector.length();
                targetVector = targetVector.scale(0.3 / length);
                double weight = 0.0;
                if (length <= this.homingRange) {
                    weight = 0.9 * ((this.homingRange - length) / this.homingRange);
                }
                Vec3 delta = this.m_20184_();
                this.m_20334_((0.9 - weight) * delta.x + (0.1 + weight) * targetVector.x, (0.9 - weight) * delta.y + (0.1 + weight) * targetVector.y, (0.9 - weight) * delta.z + (0.1 + weight) * targetVector.z);
                this.homingTime--;
            } else if (lifetime % 5 == 0) {
                AABB homingAABB = new AABB(this.m_20185_() - this.homingRange, this.m_20186_() - this.homingRange, this.m_20189_() - this.homingRange, this.m_20185_() + this.homingRange, this.m_20186_() + this.homingRange, this.m_20189_() + this.homingRange);
                List<Entity> entities = world.getEntities(this, homingAABB, this.homingPredicate);
                Entity badTarget = null;
                for (Entity entity : entities) {
                    long leastSignificantBits = entity.getUUID().getLeastSignificantBits() & 65535L;
                    if (leastSignificantBits % (long) this.homingModulo == (long) (this.homingIndex % this.homingModulo)) {
                        this.homingTarget = entity;
                    }
                    badTarget = entity;
                }
                if (this.homingTarget == null) {
                    this.homingTarget = badTarget;
                }
            }
        }
    }

    private boolean isTargetInvalid(Entity entity) {
        return entity == null || entity.isRemoved();
    }

    @Override
    public void onHit(HitResult raytraceresult) {
        super.onHit(raytraceresult);
        if (this.m_9236_().isClientSide()) {
            GlowParticleOptions options = new GlowParticleOptions(Misc.colorFromInt(this.m_20088_().get(color)), this.m_20088_().get(value), 24);
            float dist = this.m_20088_().get(value) * 0.25F;
            for (double i = 0.0; i < 40.0; i++) {
                this.m_9236_().addAlwaysVisibleParticle(options, true, this.m_20185_(), this.m_20186_(), this.m_20189_(), (double) (dist * (this.f_19796_.nextFloat() - 0.5F)), (double) (dist * (this.f_19796_.nextFloat() - 0.5F)), (double) (dist * (this.f_19796_.nextFloat() - 0.5F)));
            }
        } else {
            this.m_216990_((double) this.m_20088_().get(value).floatValue() > 7.0 ? EmbersSounds.FIREBALL_BIG_HIT.get() : EmbersSounds.FIREBALL_HIT.get());
            this.m_20088_().set(lifetime, 20);
            this.m_20088_().set(dead, true);
            this.m_20256_(Vec3.ZERO);
            if (this.effect != null) {
                this.effect.onHit(this.m_9236_(), raytraceresult, this.preset);
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}