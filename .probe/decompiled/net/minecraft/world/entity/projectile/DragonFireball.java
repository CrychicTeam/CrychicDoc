package net.minecraft.world.entity.projectile;

import java.util.List;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class DragonFireball extends AbstractHurtingProjectile {

    public static final float SPLASH_RANGE = 4.0F;

    public DragonFireball(EntityType<? extends DragonFireball> entityTypeExtendsDragonFireball0, Level level1) {
        super(entityTypeExtendsDragonFireball0, level1);
    }

    public DragonFireball(Level level0, LivingEntity livingEntity1, double double2, double double3, double double4) {
        super(EntityType.DRAGON_FIREBALL, livingEntity1, double2, double3, double4, level0);
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (hitResult0.getType() != HitResult.Type.ENTITY || !this.m_150171_(((EntityHitResult) hitResult0).getEntity())) {
            if (!this.m_9236_().isClientSide) {
                List<LivingEntity> $$1 = this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(4.0, 2.0, 4.0));
                AreaEffectCloud $$2 = new AreaEffectCloud(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_());
                Entity $$3 = this.m_19749_();
                if ($$3 instanceof LivingEntity) {
                    $$2.setOwner((LivingEntity) $$3);
                }
                $$2.setParticle(ParticleTypes.DRAGON_BREATH);
                $$2.setRadius(3.0F);
                $$2.setDuration(600);
                $$2.setRadiusPerTick((7.0F - $$2.getRadius()) / (float) $$2.getDuration());
                $$2.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
                if (!$$1.isEmpty()) {
                    for (LivingEntity $$4 : $$1) {
                        double $$5 = this.m_20280_($$4);
                        if ($$5 < 16.0) {
                            $$2.m_6034_($$4.m_20185_(), $$4.m_20186_(), $$4.m_20189_());
                            break;
                        }
                    }
                }
                this.m_9236_().m_46796_(2006, this.m_20183_(), this.m_20067_() ? -1 : 1);
                this.m_9236_().m_7967_($$2);
                this.m_146870_();
            }
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource damageSource0, float float1) {
        return false;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }
}