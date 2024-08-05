package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.TephraExplosion;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.Optional;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class TephraEntity extends Projectile {

    private static final EntityDataAccessor<Optional<UUID>> ARC_TOWARDS_ENTITY_UUID = SynchedEntityData.defineId(TephraEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Float> MAX_SCALE = SynchedEntityData.defineId(TephraEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(TephraEntity.class, EntityDataSerializers.FLOAT);

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private float prevScale;

    private boolean playedSpawnSound = false;

    private int dieIn = -1;

    private int clipFor = 5;

    public TephraEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public TephraEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.TEPHRA.get(), level);
    }

    public TephraEntity(Level level, LivingEntity shooter) {
        this(ACEntityRegistry.TEPHRA.get(), level);
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
        this.m_20088_().define(ARC_TOWARDS_ENTITY_UUID, Optional.empty());
        this.m_20088_().define(MAX_SCALE, 1.0F);
        this.m_20088_().define(SCALE, 0.1F);
    }

    @Override
    public void tick() {
        super.tick();
        this.prevScale = this.getScale();
        if (!this.playedSpawnSound) {
            this.playedSpawnSound = true;
            this.m_5496_(ACSoundRegistry.TEPHRA_WHISTLE.get(), 8.0F, Mth.clamp(2.0F - this.getMaxScale() * 0.5F, 0.5F, 2.0F));
        }
        if (!this.m_9236_().isClientSide) {
            Entity arcTowards = this.getArcingTowards();
            if (arcTowards != null && this.f_19797_ > 3 && this.dieIn == -1 && this.m_20270_(arcTowards) > 1.5F && this.f_19797_ < 20) {
                Vec3 arcVec = arcTowards.position().add(0.0, 0.3 * (double) arcTowards.getBbHeight(), 0.0).subtract(this.m_20182_()).normalize();
                this.m_20256_(this.m_20184_().add(arcVec.scale(0.1F)));
            }
            this.setScale(Mth.approach(this.getScale(), this.getMaxScale(), 0.1F));
        } else {
            for (int j = 0; j < 1 + this.f_19796_.nextInt(2); j++) {
                Vec3 delta = new Vec3((double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F), (double) (this.f_19796_.nextFloat() - 0.5F)).scale(0.025F);
                this.m_9236_().addAlwaysVisibleParticle(ACParticleRegistry.TEPHRA.get(), true, this.m_20208_((double) this.getScale()) + this.m_20184_().x, this.m_20187_() + this.m_20184_().y, this.m_20262_((double) this.getScale()) + this.m_20184_().z, delta.x, delta.y, delta.z);
            }
        }
        Vec3 vec3 = this.m_20184_();
        double d0 = this.m_20185_() + vec3.x;
        double d1 = this.m_20186_() + vec3.y;
        double d2 = this.m_20189_() + vec3.z;
        this.m_37283_();
        if (this.m_9236_().m_45556_(this.m_20191_()).noneMatch(BlockBehaviour.BlockStateBase::m_60795_) && !this.m_20072_()) {
            this.m_146870_();
        } else {
            this.m_20256_(vec3.scale(0.9F));
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.1F, 0.0));
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
        if (this.clipFor > 0) {
            this.clipFor--;
            this.f_19794_ = true;
        } else {
            this.f_19794_ = false;
        }
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
        if (!this.m_9236_().isClientSide && !this.m_150171_(hitResult.getEntity()) && !this.f_19794_) {
            this.explode();
        }
    }

    private void explode() {
        Explosion.BlockInteraction blockinteraction = ForgeEventFactory.getMobGriefingEvent(this.m_9236_(), this) ? (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY) : Explosion.BlockInteraction.KEEP;
        TephraExplosion explosion = new TephraExplosion(this.m_9236_(), this, this.m_20185_(), this.m_20227_(0.5), this.m_20189_(), 1.0F + this.getMaxScale(), blockinteraction);
        explosion.explode();
        explosion.finalizeExplosion(true);
        this.m_146870_();
    }

    public float getMaxScale() {
        return this.f_19804_.get(MAX_SCALE);
    }

    public void setMaxScale(float scale) {
        this.f_19804_.set(MAX_SCALE, scale);
    }

    public float getScale() {
        return this.f_19804_.get(SCALE);
    }

    public void setScale(float scale) {
        this.f_19804_.set(SCALE, scale);
    }

    public float getLerpedScale(float partialTicks) {
        return this.prevScale + (this.getScale() - this.prevScale) * partialTicks;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.m_9236_().isClientSide && !this.f_19794_) {
            this.explode();
        }
    }

    public Entity getArcingTowards() {
        UUID id = (UUID) this.f_19804_.get(ARC_TOWARDS_ENTITY_UUID).orElse(null);
        return id == null ? null : ((ServerLevel) this.m_9236_()).getEntity(id);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setScale(tag.getFloat("Scale"));
        this.setMaxScale(tag.getFloat("MaxScale"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("Scale", this.getScale());
        compoundTag.putFloat("MaxScale", this.getMaxScale());
    }

    public void setArcingTowards(@Nullable UUID arcingTowards) {
        this.f_19804_.set(ARC_TOWARDS_ENTITY_UUID, Optional.ofNullable(arcingTowards));
    }
}