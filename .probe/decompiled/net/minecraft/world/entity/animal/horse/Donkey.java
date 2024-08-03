package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public class Donkey extends AbstractChestedHorse {

    public Donkey(EntityType<? extends Donkey> entityTypeExtendsDonkey0, Level level1) {
        super(entityTypeExtendsDonkey0, level1);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.DONKEY_AMBIENT;
    }

    @Override
    protected SoundEvent getAngrySound() {
        return SoundEvents.DONKEY_ANGRY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DONKEY_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.DONKEY_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.DONKEY_HURT;
    }

    @Override
    public boolean canMate(Animal animal0) {
        if (animal0 == this) {
            return false;
        } else {
            return !(animal0 instanceof Donkey) && !(animal0 instanceof Horse) ? false : this.m_30628_() && ((AbstractHorse) animal0).canParent();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        EntityType<? extends AbstractHorse> $$2 = ageableMob1 instanceof Horse ? EntityType.MULE : EntityType.DONKEY;
        AbstractHorse $$3 = $$2.create(serverLevel0);
        if ($$3 != null) {
            this.m_149508_(ageableMob1, $$3);
        }
        return $$3;
    }
}