package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;

public abstract class FadeInOutInstruction extends TickingInstruction {

    protected static final int fadeTime = 5;

    public FadeInOutInstruction(int duration) {
        super(false, duration + 10);
    }

    protected abstract void show(PonderScene var1);

    protected abstract void hide(PonderScene var1);

    protected abstract void applyFade(PonderScene var1, float var2);

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        this.show(scene);
        this.applyFade(scene, 0.0F);
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        int elapsed = this.totalTicks - this.remainingTicks;
        if (elapsed < 5) {
            float fade = (float) elapsed / 5.0F;
            this.applyFade(scene, fade * fade);
        } else if (this.remainingTicks < 5) {
            float fade = (float) this.remainingTicks / 5.0F;
            this.applyFade(scene, fade * fade);
        } else {
            this.applyFade(scene, 1.0F);
        }
        if (this.remainingTicks == 0) {
            this.applyFade(scene, 0.0F);
            this.hide(scene);
        }
    }
}