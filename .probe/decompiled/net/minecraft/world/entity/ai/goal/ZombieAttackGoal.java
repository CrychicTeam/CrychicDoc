package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.monster.Zombie;

public class ZombieAttackGoal extends MeleeAttackGoal {

    private final Zombie zombie;

    private int raiseArmTicks;

    public ZombieAttackGoal(Zombie zombie0, double double1, boolean boolean2) {
        super(zombie0, double1, boolean2);
        this.zombie = zombie0;
    }

    @Override
    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.zombie.m_21561_(false);
    }

    @Override
    public void tick() {
        super.tick();
        this.raiseArmTicks++;
        if (this.raiseArmTicks >= 5 && this.m_25565_() < this.m_25566_() / 2) {
            this.zombie.m_21561_(true);
        } else {
            this.zombie.m_21561_(false);
        }
    }
}