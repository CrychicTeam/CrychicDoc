package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class WaterBoltEntity extends Projectile {

    private static final EntityDataAccessor<Optional<UUID>> ARC_TOWARDS_ENTITY_UUID = SynchedEntityData.defineId(WaterBoltEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> BUBBLING = SynchedEntityData.defineId(WaterBoltEntity.class, EntityDataSerializers.BOOLEAN);

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private Vec3[] trailPositions = new Vec3[64];

    private int trailPointer = -1;

    private boolean spawnedSplash = false;

    private int wooshSoundTime = 0;

    private int dieIn = -1;

    private boolean playedSplashSound;

    public boolean ricochet = false;

    public float seekAmount = 0.3F;

    public WaterBoltEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public WaterBoltEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.WATER_BOLT.get(), level);
    }

    public WaterBoltEntity(Level level, LivingEntity shooter) {
        this(ACEntityRegistry.WATER_BOLT.get(), level);
        float f = shooter instanceof Player ? 0.3F : 0.1F;
        this.m_6034_(shooter.m_20185_(), shooter.m_20188_() - (double) f, shooter.m_20189_());
        this.m_5602_(shooter);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(BUBBLING, false);
        this.m_20088_().define(ARC_TOWARDS_ENTITY_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.m_9236_().isClientSide) {
            Entity arcTowards = this.getArcingTowards();
            if (arcTowards != null && (this.f_19797_ > 3 || (double) this.seekAmount > 0.3) && this.dieIn == -1 && this.m_20270_(arcTowards) > 1.5F && (this.f_19797_ < 20 || (double) this.seekAmount > 0.3 && this.f_19797_ < 40)) {
                Vec3 arcVec = arcTowards.position().add(0.0, (double) (0.85F * arcTowards.getBbHeight()), 0.0).subtract(this.m_20182_()).normalize();
                float prevDeltaScale = 1.0F - (this.seekAmount - 0.3F) * 0.3F;
                this.m_20256_(this.m_20184_().scale((double) prevDeltaScale).add(arcVec.scale((double) this.seekAmount)));
            }
        } else {
            for (int j = 0; j < 3 + this.f_19796_.nextInt(2); j++) {
                this.m_9236_().addParticle(!this.m_20072_() && !this.isBubbling() ? ParticleTypes.FALLING_WATER : ParticleTypes.BUBBLE_COLUMN_UP, this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), 0.0, -0.1F, 0.0);
            }
        }
        if (this.wooshSoundTime <= 0) {
            this.wooshSoundTime = 30 + this.m_9236_().random.nextInt(30);
            this.m_216990_(ACSoundRegistry.SEA_STAFF_WOOSH.get());
        }
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() + vec3.x;
        double d1 = this.m_20186_() + vec3.y;
        double d2 = this.m_20189_() + vec3.z;
        this.m_37283_();
        float f = 0.99F;
        float f1 = 0.06F;
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_) && !this.m_20072_()) {
            this.m_146870_();
        } else {
            this.m_20256_(vec3.scale(0.9F));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.07F, 0.0));
            }
            if (this.m_9236_().isClientSide) {
                if (this.lSteps > 0) {
                    double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                    double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                    double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                    this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                    this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                    this.lSteps--;
                    this.m_6034_(d5, d6, d7);
                } else {
                    this.m_20090_();
                }
            } else {
                this.m_6034_(d0, d1, d2);
            }
        }
        Vec3 trailAt = this.m_20182_().add(0.0, (double) (this.m_20206_() / 2.0F), 0.0);
        if (this.trailPointer == -1) {
            Vec3 backAt = trailAt;
            for (int i = 0; i < this.trailPositions.length; i++) {
                this.trailPositions[i] = backAt;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = trailAt;
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, x$0 -> this.m_5603_(x$0));
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.m_6532_(hitresult);
        }
        if (this.dieIn > 0) {
            this.dieIn--;
            if (this.dieIn == 0) {
                this.m_146870_();
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        super.m_142687_(removalReason);
        if (!this.spawnedSplash && this.m_9236_() instanceof ServerLevel serverLevel) {
            this.spawnedSplash = true;
            BlockPos pos = this.m_20183_().above();
            while (this.m_9236_().m_46859_(pos) && pos.m_123342_() > this.m_9236_().m_141937_()) {
                pos = pos.below();
            }
            serverLevel.sendParticles(ACParticleRegistry.BIG_SPLASH.get(), this.m_20185_(), (double) ((float) pos.m_123342_() + 1.5F), this.m_20189_(), 0, 1.3F, 1.0, 0.0, 1.0);
        }
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.m_213877_()) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale((double) partialTick));
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

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.playedSplashSound) {
            this.playedSplashSound = true;
            this.m_216990_(ACSoundRegistry.SEA_STAFF_HIT.get());
        }
        if (!this.m_9236_().isClientSide && !this.m_150171_(hitResult.getEntity())) {
            this.damageMobs();
            if (this.dieIn == -1) {
                this.dieIn = 5;
            }
        }
    }

    private void damageMobs() {
        Entity owner = this.m_19749_();
        DamageSource source = this.m_269291_().mobProjectile(this, owner instanceof LivingEntity living1 ? living1 : null);
        AABB bashBox = this.m_20191_().inflate(2.0, 2.0, 2.0);
        Entity lastHitMob = null;
        for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, bashBox)) {
            if (!this.m_7307_(entity) && !(entity instanceof DeepOneBaseEntity)) {
                lastHitMob = entity;
                if (entity.hurt(source, 3.0F) && this.isBubbling()) {
                    entity.addEffect(new MobEffectInstance(ACEffectRegistry.BUBBLED.get(), 200));
                    if (!entity.m_9236_().isClientSide) {
                        AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.m_19879_(), this.m_19879_(), 1, 200));
                    }
                }
            }
        }
        if (this.ricochet && lastHitMob != null) {
            this.ricochet = false;
            this.onRicochetHit(lastHitMob);
        }
    }

    private void onRicochetHit(Entity hitBy) {
        Entity owner = this.m_19749_();
        AABB searchBox = hitBy.getBoundingBox().inflate(32.0, 32.0, 32.0);
        Entity ricochetTo = null;
        for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, searchBox)) {
            if (!this.m_7307_(entity) && !(entity instanceof DeepOneBaseEntity) && !entity.m_7306_(hitBy) && (owner == null || !entity.m_7306_(owner) && !entity.m_7307_(owner)) && (double) entity.m_20270_(hitBy) > 3.0 && (ricochetTo == null || ricochetTo.distanceTo(hitBy) > entity.m_20270_(hitBy))) {
                ricochetTo = entity;
            }
        }
        if (ricochetTo != null && owner instanceof LivingEntity living) {
            WaterBoltEntity bolt = new WaterBoltEntity(this.m_9236_(), living);
            bolt.m_20359_(this);
            bolt.setArcingTowards(ricochetTo.getUUID());
            Vec3 arcVec = ricochetTo.position().add(0.0, (double) (0.3F + 1.0F * ricochetTo.getBbHeight()), 0.0).subtract(this.m_20182_()).normalize();
            bolt.m_20256_(bolt.m_20184_().add(arcVec));
            bolt.setBubbling(this.isBubbling());
            this.m_9236_().m_7967_(bolt);
        }
    }

    public boolean isBubbling() {
        return this.f_19804_.get(BUBBLING);
    }

    public void setBubbling(boolean bool) {
        this.f_19804_.set(BUBBLING, bool);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.playedSplashSound) {
            this.playedSplashSound = true;
            this.m_216990_(ACSoundRegistry.SEA_STAFF_HIT.get());
        }
        if (!this.m_9236_().isClientSide) {
            this.damageMobs();
            if (this.dieIn == -1) {
                this.dieIn = 5;
            }
        }
    }

    public Entity getArcingTowards() {
        UUID id = (UUID) this.f_19804_.get(ARC_TOWARDS_ENTITY_UUID).orElse(null);
        return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setBubbling(tag.getBoolean("Bubbling"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putBoolean("Bubbling", this.isBubbling());
    }

    public void setArcingTowards(@Nullable UUID arcingTowards) {
        this.f_19804_.set(ARC_TOWARDS_ENTITY_UUID, Optional.ofNullable(arcingTowards));
    }

    public boolean hasTrail() {
        return this.trailPointer != -1;
    }
}