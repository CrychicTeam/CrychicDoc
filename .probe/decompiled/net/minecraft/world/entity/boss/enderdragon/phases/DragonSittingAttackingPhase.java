package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class DragonSittingAttackingPhase extends AbstractDragonSittingPhase {

    private static final int ROAR_DURATION = 40;

    private int attackingTicks;

    public DragonSittingAttackingPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doClientTick() {
        this.f_31176_.m_9236_().playLocalSound(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_(), SoundEvents.ENDER_DRAGON_GROWL, this.f_31176_.getSoundSource(), 2.5F, 0.8F + this.f_31176_.m_217043_().nextFloat() * 0.3F, false);
    }

    @Override
    public void doServerTick() {
        if (this.attackingTicks++ >= 40) {
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.SITTING_FLAMING);
        }
    }

    @Override
    public void begin() {
        this.attackingTicks = 0;
    }

    @Override
    public EnderDragonPhase<DragonSittingAttackingPhase> getPhase() {
        return EnderDragonPhase.SITTING_ATTACKING;
    }
}