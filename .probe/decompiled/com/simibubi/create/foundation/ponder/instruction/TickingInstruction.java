package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;

public abstract class TickingInstruction extends PonderInstruction {

    private boolean blocking;

    protected int totalTicks;

    protected int remainingTicks;

    public TickingInstruction(boolean blocking, int ticks) {
        this.blocking = blocking;
        this.remainingTicks = this.totalTicks = ticks;
    }

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        this.remainingTicks = this.totalTicks;
    }

    protected void firstTick(PonderScene scene) {
    }

    @Override
    public void onScheduled(PonderScene scene) {
        super.onScheduled(scene);
        if (this.isBlocking()) {
            scene.addToSceneTime(this.totalTicks);
        }
    }

    @Override
    public void tick(PonderScene scene) {
        if (this.remainingTicks == this.totalTicks) {
            this.firstTick(scene);
        }
        if (this.remainingTicks > 0) {
            this.remainingTicks--;
        }
    }

    @Override
    public boolean isComplete() {
        return this.remainingTicks == 0;
    }

    @Override
    public boolean isBlocking() {
        return this.blocking;
    }
}