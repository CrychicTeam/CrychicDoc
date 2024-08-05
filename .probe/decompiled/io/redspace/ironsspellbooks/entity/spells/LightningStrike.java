package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class LightningStrike extends AoeEntity {

    static final int chargeTime = 20;

    static final int vfxHeight = 15;

    public LightningStrike(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius(3.0F);
        this.setCircular();
    }

    public LightningStrike(Level level) {
        this(EntityRegistry.LIGHTNING_STRIKE.get(), level);
    }

    @Override
    public void tick() {
        if (!this.f_19853_.isClientSide) {
            if (this.f_19797_ == 1) {
                int total = 5;
                int light = Utils.random.nextInt(total);
                Vec3 location = this.m_20182_().add(0.0, 15.0, 0.0);
                MagicManager.spawnParticles(this.f_19853_, ParticleHelper.FOG_THUNDER_LIGHT, location.x, location.y, location.z, light, 1.0, 1.0, 1.0, 1.0, true);
                MagicManager.spawnParticles(this.f_19853_, ParticleHelper.FOG_THUNDER_DARK, location.x, location.y, location.z, total - light, 1.0, 1.0, 1.0, 1.0, true);
                MagicManager.spawnParticles(this.f_19853_, new ShockwaveParticleOptions(SchoolRegistry.LIGHTNING.get().getTargetingColor(), -1.5F, true), this.m_20185_(), this.m_20186_(), this.m_20189_(), 1, 0.0, 0.0, 0.0, 0.0, true);
            }
            if (this.f_19797_ == 20) {
                this.checkHits();
                MagicManager.spawnParticles(this.f_19853_, ParticleHelper.ELECTRIC_SPARKS, this.m_20185_(), this.m_20186_(), this.m_20189_(), 25, 0.2F, 0.2F, 0.2F, 0.25, true);
                MagicManager.spawnParticles(this.f_19853_, ParticleHelper.FIERY_SPARKS, this.m_20185_(), this.m_20186_(), this.m_20189_(), 5, 0.2F, 0.2F, 0.2F, 0.125, true);
                Vec3 bottom = this.m_20182_();
                Vec3 top = bottom.add(0.0, 15.0, 0.0);
                Vec3 middle = bottom.add(Utils.getRandomScaled(2.0), (double) Utils.random.nextIntBetweenInclusive(3, 12), Utils.getRandomScaled(2.0));
                MagicManager.spawnParticles(this.f_19853_, new ZapParticleOption(top), middle.x, middle.y, middle.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                MagicManager.spawnParticles(this.f_19853_, new ZapParticleOption(middle), this.m_20185_(), this.m_20186_(), this.m_20189_(), 1, 0.0, 0.0, 0.0, 0.0, true);
                if (Utils.random.nextFloat() < 0.3F) {
                    Vec3 split = middle.add(Utils.getRandomScaled(2.0), -Math.abs(Utils.getRandomScaled(2.0)), Utils.getRandomScaled(2.0));
                    MagicManager.spawnParticles(this.f_19853_, new ZapParticleOption(middle), split.x, split.y, split.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                }
                this.m_5496_(SoundRegistry.SMALL_LIGHTNING_STRIKE.get(), 2.0F, 0.8F + this.f_19796_.nextFloat() * 0.5F);
            }
            if (this.f_19797_ > 20) {
                this.m_146870_();
            }
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
        DamageSources.applyDamage(target, this.getDamage(), SpellRegistry.THUNDERSTORM_SPELL.get().getDamageSource(this, this.m_19749_()));
    }

    @Override
    public float getParticleCount() {
        return 0.0F;
    }

    @Override
    public Optional<ParticleOptions> getParticle() {
        return Optional.empty();
    }
}