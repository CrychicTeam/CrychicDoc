package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.projectile.AbstractArrow;

public abstract class AbstractDragonSittingPhase extends AbstractDragonPhaseInstance {

    public AbstractDragonSittingPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public float onHurt(DamageSource damageSource0, float float1) {
        if (damageSource0.getDirectEntity() instanceof AbstractArrow) {
            damageSource0.getDirectEntity().setSecondsOnFire(1);
            return 0.0F;
        } else {
            return super.onHurt(damageSource0, float1);
        }
    }
}