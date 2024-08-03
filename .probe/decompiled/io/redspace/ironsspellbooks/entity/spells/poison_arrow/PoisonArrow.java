package io.redspace.ironsspellbooks.entity.spells.poison_arrow;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.entity.spells.poison_cloud.PoisonCloud;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PoisonArrow extends AbstractMagicProjectile {

    private static final EntityDataAccessor<Boolean> IN_GROUND = SynchedEntityData.defineId(PoisonArrow.class, EntityDataSerializers.BOOLEAN);

    public int shakeTime;

    protected boolean hasEmittedPoison;

    protected boolean inGround;

    protected float aoeDamage;

    public PoisonArrow(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public PoisonArrow(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.POISON_ARROW.get(), levelIn);
        this.m_5602_(shooter);
    }

    @Override
    public void tick() {
        if (this.shakeTime > 0) {
            this.shakeTime--;
        }
        if (!this.inGround) {
            super.tick();
        } else {
            if (this.f_19797_ > 300) {
                this.m_146870_();
                return;
            }
            if (this.shouldFall()) {
                this.inGround = false;
                this.m_20256_(this.m_20184_().normalize().scale(0.05F));
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.m_20088_().define(IN_GROUND, false);
    }

    public void setAoeDamage(float damage) {
        this.aoeDamage = damage;
    }

    public float getAoeDamage() {
        return this.aoeDamage;
    }

    private boolean shouldFall() {
        return this.inGround && this.m_9236_().m_45772_(new AABB(this.m_20182_(), this.m_20182_()).inflate(0.06));
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.m_8060_(pResult);
        Vec3 vec3 = pResult.m_82450_().subtract(this.m_20185_(), this.m_20186_(), this.m_20189_());
        this.m_20256_(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05F);
        this.m_20343_(this.m_20185_() - vec31.x, this.m_20186_() - vec31.y, this.m_20189_() - vec31.z);
        this.m_5496_(SoundEvents.ARROW_HIT, 1.0F, 1.2F / (this.f_19796_.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shakeTime = 7;
        if (!this.m_9236_().isClientSide && !this.hasEmittedPoison) {
            this.createPoisonCloud(pResult.m_82450_());
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        if (!this.m_9236_().isClientSide) {
            Entity entity = entityHitResult.getEntity();
            boolean hit = DamageSources.applyDamage(entity, this.getDamage(), SpellRegistry.POISON_ARROW_SPELL.get().getDamageSource(this, this.m_19749_()));
            boolean ignore = entity.getType() == EntityType.ENDERMAN;
            if (hit) {
                if (!ignore) {
                    if (!this.m_9236_().isClientSide && !this.hasEmittedPoison) {
                        this.createPoisonCloud(entity.position());
                    }
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.setArrowCount(livingEntity.getArrowCount() + 1);
                    }
                }
                this.m_146870_();
            } else {
                this.m_20256_(this.m_20184_().scale(-0.1));
                this.m_146922_(this.m_146908_() + 180.0F);
                this.f_19859_ += 180.0F;
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("inGround", this.inGround);
        tag.putBoolean("hasEmittedPoison", this.hasEmittedPoison);
        tag.putFloat("aoeDamage", this.aoeDamage);
    }

    public void createPoisonCloud(Vec3 location) {
        if (!this.m_9236_().isClientSide) {
            PoisonCloud cloud = new PoisonCloud(this.m_9236_());
            cloud.m_5602_(this.m_19749_());
            cloud.setDuration(200);
            cloud.setDamage(this.aoeDamage);
            cloud.m_20219_(location);
            this.m_9236_().m_7967_(cloud);
            this.hasEmittedPoison = true;
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.inGround = tag.getBoolean("inGround");
        this.hasEmittedPoison = tag.getBoolean("hasEmittedPoison");
        this.aoeDamage = tag.getFloat("aoeDamage");
    }

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20182_().subtract(this.m_20184_().scale(2.0));
        this.m_9236_().addParticle(ParticleHelper.ACID, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.m_9236_(), ParticleHelper.ACID, x, y, z, 15, 0.03, 0.03, 0.03, 0.2, true);
    }

    @Override
    public float getSpeed() {
        return 2.5F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }
}