package net.minecraft.world.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SpectralArrow extends AbstractArrow {

    private int duration = 200;

    public SpectralArrow(EntityType<? extends SpectralArrow> entityTypeExtendsSpectralArrow0, Level level1) {
        super(entityTypeExtendsSpectralArrow0, level1);
    }

    public SpectralArrow(Level level0, LivingEntity livingEntity1) {
        super(EntityType.SPECTRAL_ARROW, livingEntity1, level0);
    }

    public SpectralArrow(Level level0, double double1, double double2, double double3) {
        super(EntityType.SPECTRAL_ARROW, double1, double2, double3, level0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide && !this.f_36703_) {
            this.m_9236_().addParticle(ParticleTypes.INSTANT_EFFECT, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    protected void doPostHurtEffects(LivingEntity livingEntity0) {
        super.doPostHurtEffects(livingEntity0);
        MobEffectInstance $$1 = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        livingEntity0.addEffect($$1, this.m_150173_());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        if (compoundTag0.contains("Duration")) {
            this.duration = compoundTag0.getInt("Duration");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putInt("Duration", this.duration);
    }
}