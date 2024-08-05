package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class AbstractGolem extends PathfinderMob {

    protected AbstractGolem(EntityType<? extends AbstractGolem> entityTypeExtendsAbstractGolem0, Level level1) {
        super(entityTypeExtendsAbstractGolem0, level1);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return false;
    }
}