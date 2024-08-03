package io.redspace.ironsspellbooks.entity.spells.guiding_bolt;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class GuidingBoltProjectile extends AbstractMagicProjectile {

    public GuidingBoltProjectile(EntityType<? extends GuidingBoltProjectile> entityType, Level level) {
        super(entityType, level);
        this.m_20242_(true);
    }

    public GuidingBoltProjectile(EntityType<? extends GuidingBoltProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        this.m_5602_(shooter);
    }

    public GuidingBoltProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.GUIDING_BOLT.get(), levelIn, shooter);
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleHelper.WISP, x, y, z, 25, 0.0, 0.0, 0.0, 0.18, true);
    }

    @Override
    public float getSpeed() {
        return 1.0F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundRegistry.GUIDING_BOLT_IMPACT.get());
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), sound, SoundSource.NEUTRAL, 2.0F, 0.9F + Utils.random.nextFloat() * 0.4F);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.m_8060_(blockHitResult);
        this.m_146870_();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (DamageSources.applyDamage(entityHitResult.getEntity(), this.damage, SpellRegistry.GUIDING_BOLT_SPELL.get().getDamageSource(this, this.m_19749_())) && entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.GUIDING_BOLT.get(), 300));
        }
        this.m_146870_();
    }

    @Override
    public void trailParticles() {
    }
}