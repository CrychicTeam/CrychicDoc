package io.redspace.ironsspellbooks.entity.spells.lightning_lance;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class LightningLanceProjectile extends AbstractMagicProjectile {

    @Override
    public void trailParticles() {
        Vec3 vec3 = this.m_20182_().subtract(this.m_20184_());
        this.f_19853_.addParticle(ParticleHelper.ELECTRICITY, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.ELECTRICITY, x, y, z, 75, 0.1, 0.1, 0.1, 2.0, true);
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.ELECTRICITY, x, y, z, 75, 0.1, 0.1, 0.1, 0.5, false);
    }

    @Override
    public float getSpeed() {
        return 3.0F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }

    public LightningLanceProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(false);
    }

    public LightningLanceProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.LIGHTNING_LANCE_PROJECTILE.get(), levelIn);
        this.m_5602_(shooter);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        DamageSources.applyDamage(entityHitResult.getEntity(), this.damage, SpellRegistry.LIGHTNING_LANCE_SPELL.get().getDamageSource(this, this.m_19749_()));
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (!this.f_19853_.isClientSide) {
            this.m_5496_(SoundEvents.TRIDENT_THUNDER, 6.0F, 0.65F);
        }
        super.onHit(pResult);
        this.m_146870_();
    }

    public int getAge() {
        return this.f_19797_;
    }
}