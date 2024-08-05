package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;

public class ExtendedEvokerFang extends EvokerFangs implements AntiMagicSusceptible {

    private final float damage;

    private boolean sentSpikeEvent;

    private int warmupDelayTicks;

    private boolean attackStarted;

    public ExtendedEvokerFang(Level pLevel, double pX, double pY, double pZ, float pYRot, int pWarmupDelay, LivingEntity pOwner, float damage) {
        super(pLevel, pX, pY, pZ, pYRot, pWarmupDelay, pOwner);
        this.warmupDelayTicks = pWarmupDelay;
        if (this.warmupDelayTicks < 0) {
            this.warmupDelayTicks = 0;
        }
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    @Override
    public void tick() {
        this.m_6075_();
        if (this.warmupDelayTicks == 0) {
            this.attackStarted = true;
            this.m_9236_().broadcastEntityEvent(this, (byte) 4);
        }
        if (this.attackStarted && this.warmupDelayTicks == -8) {
            if (this.m_9236_().isClientSide) {
                for (int i = 0; i < 12; i++) {
                    double d0 = this.m_20185_() + (this.f_19796_.nextDouble() * 2.0 - 1.0) * (double) this.m_20205_() * 0.5;
                    double d1 = this.m_20186_() + 0.05 + this.f_19796_.nextDouble();
                    double d2 = this.m_20189_() + (this.f_19796_.nextDouble() * 2.0 - 1.0) * (double) this.m_20205_() * 0.5;
                    double d3 = (this.f_19796_.nextDouble() * 2.0 - 1.0) * 0.3;
                    double d4 = 0.3 + this.f_19796_.nextDouble() * 0.3;
                    double d5 = (this.f_19796_.nextDouble() * 2.0 - 1.0) * 0.3;
                    this.m_9236_().addParticle(ParticleTypes.CRIT, d0, d1 + 1.0, d2, d3, d4, d5);
                }
            } else {
                for (LivingEntity livingentity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(0.4, 0.0, 0.4))) {
                    this.dealDamageTo(livingentity);
                }
            }
        }
        if (--this.warmupDelayTicks < -22) {
            this.m_146870_();
        }
    }

    private void dealDamageTo(LivingEntity pTarget) {
        LivingEntity livingentity = this.m_19749_();
        if (pTarget.isAlive() && !pTarget.m_20147_() && pTarget != livingentity) {
            AbstractSpell spell = SpellRegistry.FANG_STRIKE_SPELL.get();
            DamageSources.applyDamage(pTarget, this.damage, spell.getDamageSource(this, this.m_19749_()));
        }
    }

    @Override
    public float getAnimationProgress(float pPartialTicks) {
        if (!this.attackStarted) {
            return 0.0F;
        } else {
            int lifeTicks = this.warmupDelayTicks + 22;
            int i = lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float) i - pPartialTicks) / 20.0F;
        }
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.m_146870_();
    }
}