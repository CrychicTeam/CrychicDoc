package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class WaveEntity extends Entity {

    private static final EntityDataAccessor<Boolean> SLAMMING = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> LIFESPAN = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> WAITING_TICKS = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> Y_ROT = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> WAVE_SCALE = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.FLOAT);

    @Nullable
    private LivingEntity owner;

    @Nullable
    private UUID ownerUUID;

    private float slamProgress;

    private float prevSlamProgress;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    public int activeWaveTicks;

    public WaveEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public float getStepHeight() {
        return 2.0F;
    }

    public WaveEntity(Level level, LivingEntity shooter) {
        this(ACEntityRegistry.WAVE.get(), level);
        this.setOwner(shooter);
    }

    public WaveEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.WAVE.get(), level);
    }

    public void setOwner(@Nullable LivingEntity living) {
        this.owner = living;
        this.ownerUUID = living == null ? null : living.m_20148_();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.m_9236_() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.m_9236_()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }
        return this.owner;
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(SLAMMING, false);
        this.m_20088_().define(LIFESPAN, 10);
        this.m_20088_().define(WAITING_TICKS, 0);
        this.m_20088_().define(Y_ROT, 0.0F);
        this.m_20088_().define(WAVE_SCALE, 1.0F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public float getSlamAmount(float partialTicks) {
        return (this.prevSlamProgress + (this.slamProgress - this.prevSlamProgress) * partialTicks) * 0.1F;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        if (tag.contains("Lifespan")) {
            this.setLifespan(tag.getInt("Lifespan"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("Lifespan", this.getLifespan());
    }

    @Override
    public float getYRot() {
        return this.f_19804_.get(Y_ROT);
    }

    @Override
    public void setYRot(float f) {
        this.f_19804_.set(Y_ROT, f);
    }

    public int getLifespan() {
        return this.f_19804_.get(LIFESPAN);
    }

    public void setLifespan(int time) {
        this.f_19804_.set(LIFESPAN, time);
    }

    public int getWaitingTicks() {
        return this.f_19804_.get(WAITING_TICKS);
    }

    public void setWaitingTicks(int time) {
        this.f_19804_.set(WAITING_TICKS, time);
    }

    public boolean isSlamming() {
        return this.f_19804_.get(SLAMMING);
    }

    public void setSlamming(boolean bool) {
        this.f_19804_.set(SLAMMING, bool);
    }

    public float getWaveScale() {
        return this.f_19804_.get(WAVE_SCALE);
    }

    public void setWaveScale(float waveScale) {
        this.f_19804_.set(WAVE_SCALE, waveScale);
    }

    private void spawnParticleAt(float yOffset, float zOffset, float xOffset, ParticleOptions particleType) {
        Vec3 vec3 = new Vec3((double) xOffset, (double) yOffset, (double) zOffset).yRot((float) Math.toRadians((double) (-this.getYRot())));
        this.m_9236_().addParticle(particleType, this.m_20185_() + vec3.x, this.m_20186_() + vec3.y, this.m_20189_() + vec3.z, this.m_20184_().x, 0.1F, this.m_20184_().z);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public void tick() {
        super.tick();
        this.prevSlamProgress = this.slamProgress;
        if (this.getWaitingTicks() > 0) {
            if (!this.m_9236_().isClientSide) {
                this.setWaitingTicks(this.getWaitingTicks() - 1);
            }
            this.m_6842_(true);
        } else {
            if (this.m_20145_()) {
                this.m_6842_(false);
            }
            if (this.isSlamming() && this.slamProgress < 10.0F) {
                this.slamProgress++;
            }
            if (this.isSlamming() && this.slamProgress == 10.0F) {
                this.m_146870_();
            }
            if (!this.m_20068_() && !this.m_20072_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.04F, 0.0));
            }
            float f = Math.min((float) this.activeWaveTicks / 10.0F, 1.0F);
            Vec3 directionVec = new Vec3(0.0, 0.0, (double) (f * f * 0.2F)).yRot((float) Math.toRadians((double) (-this.getYRot())));
            if (this.m_9236_().isClientSide) {
                if (this.lSteps > 0) {
                    double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                    double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                    double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                    this.setYRot(Mth.wrapDegrees((float) this.lyr));
                    this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                    this.lSteps--;
                    this.m_6034_(d5, d6, d7);
                } else {
                    this.m_20090_();
                }
                for (int particleCount = 0; (float) particleCount < this.getWaveScale(); particleCount++) {
                    for (int i = 0; i <= 4; i++) {
                        float xOffset = (float) i / 4.0F - 0.5F + (this.f_19796_.nextFloat() - 0.5F) * 0.2F;
                        this.spawnParticleAt((0.2F + this.f_19796_.nextFloat() * 0.2F) * this.getWaveScale(), 1.2F, xOffset * 1.2F * this.getWaveScale(), ACParticleRegistry.WATER_FOAM.get());
                        this.spawnParticleAt((0.2F + this.f_19796_.nextFloat() * 0.2F) * this.getWaveScale(), -0.2F, xOffset * 1.4F * this.getWaveScale(), ParticleTypes.SPLASH);
                    }
                }
            } else {
                this.m_20090_();
                this.m_19915_(this.getYRot(), this.m_146909_());
            }
            if (!this.m_9236_().isClientSide) {
                this.attackEntities(this.getSlamAmount(1.0F) * 2.0F + 1.0F + this.getWaveScale());
            }
            Vec3 vec3 = this.m_20184_().scale(0.9F).add(directionVec);
            this.m_6478_(MoverType.SELF, vec3);
            this.m_20256_(vec3.multiply(0.99F, 0.98F, 0.99F));
            if (this.activeWaveTicks > this.getLifespan() || this.activeWaveTicks > 10 && this.m_20184_().horizontalDistance() < 0.04) {
                this.setSlamming(true);
            }
            this.activeWaveTicks++;
        }
    }

    private void attackEntities(float scale) {
        AABB bashBox = this.m_20191_().inflate(0.5, 0.5, 0.5);
        DamageSource source = this.m_269291_().mobProjectile(this, this.owner);
        for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, bashBox)) {
            if (!this.m_7307_(entity) && !(entity instanceof DeepOneBaseEntity) && (this.owner == null || !this.owner.equals(entity) && !this.owner.m_7307_(entity))) {
                entity.hurt(source, scale + 1.0F);
                this.setSlamming(true);
                entity.knockback(0.1 + 0.5 * (double) scale, (double) Mth.sin(this.getYRot() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.getYRot() * (float) (Math.PI / 180.0))));
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (WAVE_SCALE.equals(dataAccessor)) {
            this.m_6210_();
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return super.getDimensions(pose).scale(this.getWaveScale());
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }
}