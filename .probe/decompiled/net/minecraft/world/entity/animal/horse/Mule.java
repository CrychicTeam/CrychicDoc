package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Mule extends AbstractChestedHorse {

    public Mule(EntityType<? extends Mule> entityTypeExtendsMule0, Level level1) {
        super(entityTypeExtendsMule0, level1);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.MULE_AMBIENT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.MULE_ANGRY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.MULE_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.MULE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.MULE_HURT;
    }

    @Override
    protected void playChestEquipsSound() {
        this.m_5496_(SoundEvents.MULE_CHEST, 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        return EntityType.MULE.create(serverLevel0);
    }
}