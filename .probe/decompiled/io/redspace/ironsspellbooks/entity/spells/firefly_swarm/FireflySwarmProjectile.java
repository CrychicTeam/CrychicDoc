package io.redspace.ironsspellbooks.entity.spells.firefly_swarm;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireflySwarmProjectile extends PathfinderMob implements AntiMagicSusceptible {

    static final int maxLife = 200;

    public static final float radius = 2.0F;

    UUID targetUUID;

    Entity cachedTarget;

    UUID ownerUUID;

    Entity cachedOwner;

    Entity nextTarget;

    float damage;

    public FireflySwarmProjectile(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.f_21342_ = new FlyingMoveControl(this, 15, true);
        this.f_19794_ = true;
        this.m_20242_(true);
    }

    public FireflySwarmProjectile(Level level, @Nullable Entity owner, @Nullable Entity target, float damage) {
        this(EntityRegistry.FIREFLY_SWARM.get(), level);
        this.setOwner(owner);
        this.setTarget(target);
        this.damage = damage;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FlyingPathNavigation(this, pLevel);
    }

    @Override
    public void tick() {
        if (this.f_19853_.isClientSide) {
            for (int i = 0; i < 2; i++) {
                Vec3 motion = Utils.getRandomVec3(0.05F).add(this.m_20184_());
                Vec3 spawn = Utils.getRandomVec3(0.25);
                this.f_19853_.addParticle(ParticleHelper.FIREFLY, this.m_20185_() + spawn.x, this.m_20186_() + (double) (this.m_20206_() * 0.5F) + spawn.z, this.m_20189_() + spawn.z, motion.x, motion.y, motion.z);
            }
        }
        super.m_8119_();
        if (this.f_19797_ > 200) {
            this.m_146870_();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.m_8024_();
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.f_21344_.moveTo(target, 7.0);
        }
        if (this.f_19797_ % 8 == 0) {
            if (this.f_19853_.m_186437_(this, this.m_20191_().move(0.0, -1.0, 0.0))) {
                this.m_20256_(this.m_20184_().add(0.0, 0.02, 0.0));
            } else {
                this.m_20256_(this.m_20184_().add(0.0, -0.008, 0.0));
            }
        }
        if (!this.f_21342_.hasWanted()) {
            this.m_20256_(this.m_20184_().multiply(0.95F, 1.0, 0.95F));
        }
        if ((this.f_19797_ & 7) == 0) {
            float fade = 1.0F - Mth.clamp((float) (this.f_19797_ - 200 + 40) / 200.0F, 0.0F, 1.0F);
            this.m_5496_(SoundRegistry.FIREFLY_SWARM_IDLE.get(), 0.25F * fade, 0.95F + Utils.random.nextFloat() * 0.1F);
        }
        if (this.f_19797_ % 15 == 0) {
            float inflate = 2.0F - this.m_20205_() * 0.5F;
            this.f_19853_.getEntities(this, this.m_20191_().inflate((double) inflate), this::canHitEntity).forEach(entity -> {
                if (this.canHitEntity(entity)) {
                    boolean hit = DamageSources.applyDamage(entity, this.damage, SpellRegistry.FIREFLY_SWARM_SPELL.get().getDamageSource(this, this.getOwner()));
                    if (hit) {
                        this.m_5496_(SoundRegistry.FIREFLY_SWARM_ATTACK.get(), 0.75F, 0.9F + Utils.random.nextFloat() * 0.2F);
                        if (target == null) {
                            this.setTarget(entity);
                        } else if (target != entity) {
                            this.nextTarget = entity;
                        }
                    }
                }
            });
            if (this.getTarget() == null || this.getTarget().isDeadOrDying()) {
                this.setTarget(this.nextTarget);
                if (this.nextTarget != null && this.nextTarget.isRemoved()) {
                    this.nextTarget = null;
                }
            }
        }
    }

    protected boolean canHitEntity(Entity target) {
        if (!target.isSpectator() && target.isAlive() && target.isPickable()) {
            Entity owner = this.getOwner();
            return owner != target && !DamageSources.isFriendlyFireBetween(owner, target);
        } else {
            return false;
        }
    }

    public void setOwner(@Nullable Entity owner) {
        if (owner != null) {
            this.ownerUUID = owner.getUUID();
            this.cachedOwner = owner;
        }
    }

    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.f_19853_ instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel) this.f_19853_).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getFireflyTarget() instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    public void setTarget(@Nullable Entity target) {
        if (target != null) {
            this.targetUUID = target.getUUID();
            this.cachedTarget = target;
        }
    }

    @Nullable
    public Entity getFireflyTarget() {
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
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.m_7380_(pCompound);
        if (this.targetUUID != null) {
            pCompound.putUUID("Target", this.targetUUID);
        }
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        pCompound.putInt("Age", this.f_19797_);
        pCompound.putFloat("Damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.hasUUID("Target")) {
            this.targetUUID = pCompound.getUUID("Target");
        }
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }
        this.f_19797_ = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.m_146870_();
    }
}