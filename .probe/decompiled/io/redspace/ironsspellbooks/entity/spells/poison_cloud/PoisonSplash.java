package io.redspace.ironsspellbooks.entity.spells.poison_cloud;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AoeEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PoisonSplash extends AoeEntity {

    boolean playedParticles;

    public PoisonSplash(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setRadius((float) (this.m_20191_().getXsize() * 0.5));
    }

    public PoisonSplash(Level level) {
        this(EntityRegistry.POISON_SPLASH.get(), level);
    }

    @Override
    public void tick() {
        if (!this.playedParticles) {
            this.playedParticles = true;
            if (this.m_9236_().isClientSide) {
                for (int i = 0; i < 150; i++) {
                    Vec3 pos = new Vec3(Utils.getRandomScaled(0.5), Utils.getRandomScaled(0.2F), (double) (this.f_19796_.nextFloat() * this.getRadius())).yRot(this.f_19796_.nextFloat() * 360.0F);
                    Vec3 motion = new Vec3(Utils.getRandomScaled(0.06F), this.f_19796_.nextDouble() * -0.8 - 0.5, Utils.getRandomScaled(0.06F));
                    this.m_9236_().addParticle(ParticleHelper.ACID, this.m_20185_() + pos.x, this.m_20186_() + pos.y + this.m_20191_().getYsize(), this.m_20189_() + pos.z, motion.x, motion.y, motion.z);
                }
            } else {
                MagicManager.spawnParticles(this.m_9236_(), ParticleHelper.POISON_CLOUD, this.m_20185_(), this.m_20186_() + this.m_20191_().getYsize(), this.m_20189_(), 9, (double) (this.getRadius() * 0.7F), 0.2F, (double) (this.getRadius() * 0.7F), 1.0, true);
            }
        }
        if (this.f_19797_ == 4) {
            this.checkHits();
            if (!this.m_9236_().isClientSide) {
                MagicManager.spawnParticles(this.m_9236_(), ParticleHelper.POISON_CLOUD, this.m_20185_(), this.m_20186_(), this.m_20189_(), 9, (double) (this.getRadius() * 0.7F), 0.2F, (double) (this.getRadius() * 0.7F), 1.0, true);
            }
            this.createPoisonCloud();
        }
        if (this.f_19797_ > 6) {
            this.m_146870_();
        }
    }

    public void createPoisonCloud() {
        if (!this.m_9236_().isClientSide) {
            PoisonCloud cloud = new PoisonCloud(this.m_9236_());
            cloud.m_5602_(this.m_19749_());
            cloud.setDuration(this.getEffectDuration());
            cloud.setDamage(this.getDamage() * 0.1F);
            cloud.m_20219_(this.m_20182_());
            this.m_9236_().m_7967_(cloud);
        }
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (DamageSources.applyDamage(target, this.getDamage(), SpellRegistry.POISON_SPLASH_SPELL.get().getDamageSource(this, this.m_19749_()))) {
            target.addEffect(new MobEffectInstance(MobEffects.POISON, this.getEffectDuration(), 0));
        }
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
}