package io.redspace.ironsspellbooks.entity.spells.sunbeam;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class Sunbeam extends AoeEntity implements AntiMagicSusceptible {

    public Sunbeam(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius((float) (this.m_20191_().getXsize() * 0.5));
        this.m_20242_(true);
    }

    public Sunbeam(Level level) {
        this(EntityRegistry.SUNBEAM.get(), level);
    }

    @Override
    public void tick() {
        if (this.f_19797_ == 4) {
            this.checkHits();
            if (!this.f_19853_.isClientSide) {
                MagicManager.spawnParticles(this.f_19853_, ParticleTypes.FIREWORK, this.m_20185_(), this.m_20186_(), this.m_20189_(), 9, (double) (this.getRadius() * 0.7F), 0.2F, (double) (this.getRadius() * 0.7F), 1.0, true);
            }
        }
        if (this.f_19797_ > 6) {
            this.m_146870_();
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public void ambientParticles() {
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }

    @Override
    public void onAntiMagic(MagicData magicData) {
        this.m_146870_();
    }
}