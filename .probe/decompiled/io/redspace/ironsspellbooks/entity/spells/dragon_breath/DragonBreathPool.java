package io.redspace.ironsspellbooks.entity.spells.dragon_breath;

import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class DragonBreathPool extends AoeEntity {

    private DamageSource damageSource;

    public DragonBreathPool(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCircular();
        this.setRadius(1.8F);
        this.radiusOnUse = -0.15F;
        this.radiusPerTick = -0.02F;
    }

    public DragonBreathPool(Level level) {
        this(EntityRegistry.DRAGON_BREATH_POOL.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (this.damageSource == null) {
            this.damageSource = new DamageSource(DamageSources.getHolderFromResource(target, ISSDamageTypes.DRAGON_BREATH_POOL), this, this.m_19749_());
        }
        DamageSources.ignoreNextKnockback(target);
        target.hurt(this.damageSource, this.getDamage());
    }

    @Override
    public float getParticleCount() {
        return 3.0F;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.of(ParticleTypes.DRAGON_BREATH);
    }
}