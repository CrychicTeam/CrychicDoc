package io.redspace.ironsspellbooks.entity.spells.firebolt;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireboltProjectile extends AbstractMagicProjectile {

    public FireboltProjectile(EntityType<? extends FireboltProjectile> entityType, Level level) {
        super(entityType, level);
        this.m_20242_(true);
    }

    public FireboltProjectile(Level levelIn, LivingEntity shooter) {
        this(EntityRegistry.FIREBOLT_PROJECTILE.get(), levelIn);
        this.m_5602_(shooter);
    }

    @Override
    public float getSpeed() {
        return 1.75F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.of(SoundEvents.FIREWORK_ROCKET_BLAST);
    }

    @Override
    protected void doImpactSound(SoundEvent sound) {
        this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), sound, SoundSource.NEUTRAL, 2.0F, 1.2F + Utils.random.nextFloat() * 0.2F);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.m_8060_(blockHitResult);
        this.m_146870_();
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity target = entityHitResult.getEntity();
        DamageSources.applyDamage(target, this.getDamage(), SpellRegistry.FIREBOLT_SPELL.get().getDamageSource(this, this.m_19749_()));
        this.m_146870_();
    }

    @Override
    public void impactParticles(double x, double y, double z) {
        MagicManager.spawnParticles(this.f_19853_, ParticleTypes.LAVA, x, y, z, 5, 0.1, 0.1, 0.1, 0.25, true);
    }

    @Override
    public void trailParticles() {
        for (int i = 0; i < 1; i++) {
            float yHeading = -((float) (Mth.atan2(this.m_20184_().z, this.m_20184_().x) * 180.0F / (float) Math.PI) + 90.0F);
            float radius = 0.25F;
            int steps = 6;
            for (int j = 0; j < steps; j++) {
                float offset = 1.0F / (float) steps * (float) i;
                double radians = (double) (((float) this.f_19797_ + offset) / 7.5F * 360.0F * (float) (Math.PI / 180.0));
                Vec3 swirl = new Vec3(Math.cos(radians) * (double) radius, Math.sin(radians) * (double) radius, 0.0).yRot(yHeading * (float) (Math.PI / 180.0));
                double x = this.m_20185_() + swirl.x;
                double y = this.m_20186_() + swirl.y + (double) (this.m_20206_() / 2.0F);
                double z = this.m_20189_() + swirl.z;
                Vec3 jitter = Utils.getRandomVec3(0.05F);
                this.f_19853_.addParticle(ParticleHelper.EMBERS, x, y, z, jitter.x, jitter.y, jitter.z);
            }
        }
    }
}