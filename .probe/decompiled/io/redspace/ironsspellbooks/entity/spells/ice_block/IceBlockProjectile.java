package io.redspace.ironsspellbooks.entity.spells.ice_block;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IceBlockProjectile extends AbstractMagicProjectile implements GeoEntity {

    private UUID targetUUID;

    private Entity cachedTarget;

    private List<Entity> victims;

    int airTime;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public IceBlockProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.victims = new ArrayList();
        this.m_20242_(true);
    }

    public IceBlockProjectile(Level pLevel, LivingEntity owner, LivingEntity target) {
        this(EntityRegistry.ICE_BLOCK_PROJECTILE.get(), pLevel);
        this.m_5602_(owner);
        this.setTarget(target);
    }

    public void setAirTime(int airTimeInTicks) {
        this.airTime = airTimeInTicks;
    }

    public void setTarget(@Nullable Entity pOwner) {
        if (pOwner != null) {
            this.targetUUID = pOwner.getUUID();
            this.cachedTarget = pOwner;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.cachedTarget != null && !this.cachedTarget.isRemoved()) {
            return this.cachedTarget;
        } else if (this.targetUUID != null && this.f_19853_ instanceof ServerLevel) {
            this.cachedTarget = ((ServerLevel) this.f_19853_).getEntity(this.targetUUID);
            return this.cachedTarget;
        } else {
            return null;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.targetUUID != null) {
            tag.putUUID("Target", this.targetUUID);
        }
        if (this.airTime > 0) {
            tag.putInt("airTime", this.airTime);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Target")) {
            this.targetUUID = tag.getUUID("Target");
        }
        if (tag.contains("airTime")) {
            this.airTime = tag.getInt("airTime");
        }
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; i++) {
            Vec3 random = new Vec3(Utils.getRandomScaled((double) (this.m_20205_() * 0.5F)), 0.0, Utils.getRandomScaled((double) (this.m_20205_() * 0.5F)));
            this.f_19853_.addParticle(ParticleTypes.SNOWFLAKE, this.m_20185_() + random.x, this.m_20186_(), this.m_20189_() + random.z, 0.0, -0.05, 0.0);
        }
    }

    private void doFallingDamage(Entity target) {
        if (!this.f_19853_.isClientSide) {
            if (this.canHitEntity(target) && !this.victims.contains(target)) {
                boolean flag = DamageSources.applyDamage(target, this.getDamage() / 2.0F, SpellRegistry.ICE_BLOCK_SPELL.get().getDamageSource(this, this.m_19749_()));
                if (flag) {
                    this.victims.add(target);
                    target.invulnerableTime = 0;
                }
            }
        }
    }

    private void doImpactDamage() {
        float explosionRadius = 3.5F;
        this.f_19853_.m_45933_(this, this.m_20191_().inflate((double) explosionRadius)).forEach(entity -> {
            if (this.canHitEntity(entity)) {
                double distance = entity.distanceToSqr(this.m_20182_());
                if (distance < (double) (explosionRadius * explosionRadius)) {
                    double p = 1.0 - Math.pow(Math.sqrt(distance) / (double) explosionRadius, 3.0);
                    float damage = (float) ((double) this.damage * p);
                    DamageSources.applyDamage(entity, damage, SpellRegistry.ICE_BLOCK_SPELL.get().getDamageSource(this, this.m_19749_()));
                }
            }
        });
    }

    @Override
    public void tick() {
        this.f_19803_ = false;
        this.f_19854_ = this.m_20185_();
        this.f_19855_ = this.m_20186_();
        this.f_19856_ = this.m_20189_();
        this.f_19790_ = this.m_20185_();
        this.f_19791_ = this.m_20186_();
        this.f_19792_ = this.m_20189_();
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
        if (!this.f_19853_.isClientSide) {
            if (this.airTime <= 0) {
                if (this.m_20096_()) {
                    this.doImpactDamage();
                    this.m_5496_(SoundRegistry.ICE_BLOCK_IMPACT.get(), 2.5F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
                    this.impactParticles(this.m_20185_(), this.m_20186_(), this.m_20189_());
                    this.m_146870_();
                } else {
                    this.f_19853_.m_45933_(this, this.m_20191_().inflate(0.35)).forEach(this::doFallingDamage);
                }
            }
            if (this.airTime-- > 0) {
                boolean tooHigh = false;
                this.m_20256_(this.m_20184_().multiply(0.95F, 0.75, 0.95F));
                if (this.getTarget() != null) {
                    Entity target = this.getTarget();
                    Vec3 diff = target.position().subtract(this.m_20182_());
                    if (diff.horizontalDistanceSqr() > 1.0) {
                        this.m_20256_(this.m_20184_().add(diff.multiply(1.0, 0.0, 1.0).normalize().scale(0.025F)));
                    }
                    if (this.m_20186_() - target.getY() > 3.5) {
                        tooHigh = true;
                    }
                } else if (this.airTime % 3 == 0) {
                    HitResult ground = Utils.raycastForBlock(this.f_19853_, this.m_20182_(), this.m_20182_().subtract(0.0, 3.5, 0.0), ClipContext.Fluid.ANY);
                    if (ground.getType() == HitResult.Type.MISS) {
                        tooHigh = true;
                    } else if (Math.abs(this.m_20182_().y - ground.getLocation().y) < 4.0) {
                    }
                }
                if (tooHigh) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.005, 0.0));
                } else {
                    this.m_20256_(this.m_20184_().add(0.0, 0.01, 0.0));
                }
                if (this.airTime == 0) {
                    this.m_20334_(0.0, 0.5, 0.0);
                }
            } else {
                this.m_20334_(0.0, this.m_20184_().y - 0.15, 0.0);
            }
        } else {
            this.trailParticles();
        }
        this.m_6478_(MoverType.SELF, this.m_20184_());
    }

    @Override
    public void setXRot(float pXRot) {
    }

    @Override
    public void setYRot(float pYRot) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return pTarget != this.m_19749_() && super.canHitEntity(pTarget);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleTypes.SNOWFLAKE, x, y, z, 50, 0.8, 0.1, 0.8, 0.2, false);
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.SNOWFLAKE, x, y, z, 25, 0.5, 0.1, 0.5, 0.3, false);
    }

    @Override
    public float getSpeed() {
        return 0.0F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}