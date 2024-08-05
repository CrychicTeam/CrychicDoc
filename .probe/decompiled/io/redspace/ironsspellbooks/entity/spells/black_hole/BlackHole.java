package io.redspace.ironsspellbooks.entity.spells.black_hole;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BlackHole extends Projectile implements AntiMagicSusceptible {

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(BlackHole.class, EntityDataSerializers.FLOAT);

    List<Entity> trackingEntities = new ArrayList();

    private int soundTick;

    private float damage;

    private static final int loopSoundDurationInTicks = 320;

    public BlackHole(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BlackHole(Level pLevel, LivingEntity owner) {
        this(EntityRegistry.BLACK_HOLE.get(), pLevel);
        this.m_5602_(owner);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
    }

    @Override
    public void refreshDimensions() {
        double d0 = this.m_20185_();
        double d1 = this.m_20186_();
        double d2 = this.m_20189_();
        super.m_6210_();
        this.m_6034_(d0, d1, d2);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.m_20088_().define(DATA_RADIUS, 5.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (this.getRadius() < 0.1F) {
                this.m_146870_();
            }
        }
        super.m_7350_(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.m_9236_().isClientSide) {
            this.m_20088_().set(DATA_RADIUS, Math.min(pRadius, 48.0F));
        }
    }

    public float getRadius() {
        return this.m_20088_().get(DATA_RADIUS);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Age", this.f_19797_);
        pCompound.putFloat("Damage", this.getDamage());
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.f_19797_ = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
        if (this.damage == 0.0F) {
            this.damage = 1.0F;
        }
        if (pCompound.getInt("Radius") > 0) {
            this.setRadius(pCompound.getFloat("Radius"));
        }
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        int update = Math.max((int) (this.getRadius() / 2.0F), 2);
        if (this.f_19797_ % update == 0) {
            this.updateTrackingEntities();
        }
        AABB bb = this.m_20191_();
        float radius = (float) bb.getXsize();
        boolean hitTick = this.f_19797_ % 10 == 0;
        for (Entity entity : this.trackingEntities) {
            if (entity != this.m_19749_() && !DamageSources.isFriendlyFireBetween(this.m_19749_(), entity)) {
                Vec3 center = bb.getCenter();
                float distance = (float) center.distanceTo(entity.position());
                if (!(distance > radius)) {
                    float f = 1.0F - distance / radius;
                    float scale = f * f * f * f * 0.25F;
                    Vec3 diff = center.subtract(entity.position()).scale((double) scale);
                    entity.push(diff.x, diff.y, diff.z);
                    if (hitTick && distance < 9.0F && this.m_5603_(entity)) {
                        DamageSources.applyDamage(entity, this.damage, SpellRegistry.BLACK_HOLE_SPELL.get().getDamageSource(this, this.m_19749_()));
                    }
                    entity.fallDistance = 0.0F;
                }
            }
        }
        if (!this.m_9236_().isClientSide) {
            if (this.f_19797_ > 640) {
                this.m_146870_();
                this.m_5496_(SoundRegistry.BLACK_HOLE_CAST.get(), this.getRadius() / 2.0F, 1.0F);
                MagicManager.spawnParticles(this.m_9236_(), ParticleHelper.UNSTABLE_ENDER, this.m_20185_(), this.m_20186_() + (double) this.getRadius(), this.m_20189_(), 200, 1.0, 1.0, 1.0, 1.0, true);
            } else if ((this.f_19797_ - 1) % 320 == 0) {
                this.m_5496_(SoundRegistry.BLACK_HOLE_LOOP.get(), this.getRadius() / 3.0F, 1.0F);
            }
        }
    }

    private void updateTrackingEntities() {
        this.trackingEntities = this.m_9236_().m_45933_(this, this.m_20191_().inflate(1.0));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}