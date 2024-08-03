package io.redspace.ironsspellbooks.entity.spells.magic_missile;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class MagicMissileProjectile extends AbstractMagicProjectile {

    public MagicMissileProjectile(EntityType<? extends MagicMissileProjectile> entityType, Level level) {
        super(entityType, level);
        this.m_20242_(true);
    }

    public MagicMissileProjectile(EntityType<? extends MagicMissileProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.m_5602_(shooter);
    }

    public MagicMissileProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.MAGIC_MISSILE_PROJECTILE.get(), levelIn, shooter);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.UNSTABLE_ENDER, x, y, z, 25, 0.0, 0.0, 0.0, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 2.5F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.m_8060_(blockHitResult);
        this.m_146870_();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        DamageSources.applyDamage(entityHitResult.getEntity(), this.damage, SpellRegistry.MAGIC_MISSILE_SPELL.get().getDamageSource(this, this.m_19749_()));
        this.m_146870_();
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 2; i++) {
            double speed = 0.02;
            double dx = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dy = Utils.random.nextDouble() * 2.0 * speed - speed;
            double dz = Utils.random.nextDouble() * 2.0 * speed - speed;
            this.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, this.m_20185_() + dx, this.m_20186_() + dy, this.m_20189_() + dz, dx, dy, dz);
            if (this.f_19797_ > 1) {
                this.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, this.m_20185_() + dx - this.m_20184_().x / 2.0, this.m_20186_() + dy - this.m_20184_().y / 2.0, this.m_20189_() + dz - this.m_20184_().z / 2.0, dx, dy, dz);
            }
        }
    }
}