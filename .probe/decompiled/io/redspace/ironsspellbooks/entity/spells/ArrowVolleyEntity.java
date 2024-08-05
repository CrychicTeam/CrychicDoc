package io.redspace.ironsspellbooks.entity.spells;

import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.small_magic_arrow.SmallMagicArrow;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ArrowVolleyEntity extends AbstractMagicProjectile {

    int rows;

    int arrowsPerRow;

    int delay = 5;

    public ArrowVolleyEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_20242_(true);
        this.f_19794_ = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_19853_.isClientSide) {
            if (this.f_19797_ % this.delay == 0) {
                int arrows = this.arrowsPerRow;
                float speed = 0.85F;
                Vec3 motion = Vec3.directionFromRotation(this.m_146909_() - (float) this.f_19797_ / 5.0F * 7.0F, this.m_146908_()).normalize().scale((double) speed);
                Vec3 orth = new Vec3((double) (-Mth.cos(-this.m_146908_() * (float) (Math.PI / 180.0) - (float) Math.PI)), 0.0, (double) Mth.sin(-this.m_146908_() * (float) (Math.PI / 180.0) - (float) Math.PI));
                for (int i = 0; i < arrows; i++) {
                    float distance = ((float) i - (float) arrows * 0.5F) * 0.7F;
                    SmallMagicArrow arrow = new SmallMagicArrow(this.f_19853_, this.m_19749_());
                    arrow.setDamage(this.getDamage());
                    Vec3 spawn = this.m_20182_().add(orth.scale((double) distance));
                    arrow.m_146884_(spawn);
                    arrow.shoot(motion.add(Utils.getRandomVec3(0.04F)));
                    arrow.m_5602_(this.m_19749_());
                    this.f_19853_.m_7967_(arrow);
                    MagicManager.spawnParticles(this.f_19853_, ParticleTypes.FIREWORK, spawn.x, spawn.y, spawn.z, 2, 0.1, 0.1, 0.1, 0.05, false);
                }
                this.f_19853_.playSound(null, this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 3.0F, 1.1F + Utils.random.nextFloat() * 0.3F);
                this.f_19853_.playSound(null, this.m_20182_().x, this.m_20182_().y, this.m_20182_().z, SoundRegistry.BOW_SHOOT.get(), SoundSource.NEUTRAL, 2.0F, (float) Utils.random.nextIntBetweenInclusive(16, 20) * 0.1F);
            } else if (this.f_19797_ > this.rows * this.delay) {
                this.m_146870_();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("rows", this.rows);
        tag.putInt("arrowsPerRow", this.arrowsPerRow);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.rows = tag.getInt("rows");
        this.arrowsPerRow = tag.getInt("arrowsPerRow");
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setArrowsPerRow(int arrowsPerRow) {
        this.arrowsPerRow = arrowsPerRow;
    }

    @Override
    public void trailParticles() {
    }

    @Override
    public void impactParticles(double x, double y, double z) {
    }

    @Override
    public float getSpeed() {
        return 0.0F;
    }

    @Override
    public Optional<SoundEvent> getImpactSound() {
        return Optional.empty();
    }
}