package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.Mob;

public class OpenDoorGoal extends DoorInteractGoal {

    private final boolean closeDoor;

    private int forgetTime;

    public OpenDoorGoal(Mob mob0, boolean boolean1) {
        super(mob0);
        this.f_25189_ = mob0;
        this.closeDoor = boolean1;
    }

    @Override
    public boolean canContinueToUse() {
        return this.closeDoor && this.forgetTime > 0 && super.canContinueToUse();
    }

    @Override
    public void start() {
        this.forgetTime = 20;
        this.m_25195_(true);
    }

    @Override
    public void stop() {
        this.m_25195_(false);
    }

    @Override
    public void tick() {
        this.forgetTime--;
        super.tick();
    }
}