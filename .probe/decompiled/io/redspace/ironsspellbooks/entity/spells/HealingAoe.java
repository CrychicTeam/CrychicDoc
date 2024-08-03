package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class HealingAoe extends AoeEntity implements AntiMagicSusceptible {

    public HealingAoe(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public HealingAoe(Level level) {
        this(EntityRegistry.HEALING_AOE.get(), level);
    }

    @Override
    public void applyEffect(LivingEntity target) {
        if (this.m_19749_() instanceof LivingEntity owner && Utils.shouldHealEntity(owner, target)) {
            float healAmount = this.getDamage();
            MinecraftForge.EVENT_BUS.post(new SpellHealEvent((LivingEntity) this.m_19749_(), target, healAmount, SchoolRegistry.HOLY.get()));
            target.heal(healAmount);
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return !pTarget.isSpectator() && pTarget.isAlive() && pTarget.isPickable();
    }

    @Override
    public float getParticleCount() {
        return 0.15F;
    }

    @Override
    public void ambientParticles() {
        if (this.f_19853_.isClientSide) {
            int color = PotionUtils.getColor(Potion.byName("healing"));
            double d0 = (double) (color >> 16 & 0xFF) / 255.0;
            double d1 = (double) (color >> 8 & 0xFF) / 255.0;
            double d2 = (double) (color >> 0 & 0xFF) / 255.0;
            float f = this.getParticleCount();
            f = Mth.clamp(f * this.getRadius(), f / 4.0F, f * 10.0F);
            for (int i = 0; (float) i < f; i++) {
                if (f - (float) i < 1.0F && this.f_19796_.nextFloat() > f - (float) i) {
                    return;
                }
                float r = this.getRadius();
                Vec3 pos;
                if (this.isCircular()) {
                    float distance = (1.0F - this.f_19796_.nextFloat() * this.f_19796_.nextFloat()) * r;
                    pos = new Vec3(0.0, 0.0, (double) distance).yRot(this.f_19796_.nextFloat() * 360.0F);
                } else {
                    pos = new Vec3(Utils.getRandomScaled((double) (r * 0.85F)), 0.2F, Utils.getRandomScaled((double) (r * 0.85F)));
                }
                this.f_19853_.addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + pos.x, this.m_20186_() + pos.y + (double) this.particleYOffset(), this.m_20189_() + pos.z, d0, d1, d2);
            }
        }
    }

    @Override
    protected Vec3 getInflation() {
        return new Vec3(0.0, 1.0, 0.0);
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