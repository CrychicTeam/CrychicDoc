package net.minecraft.client.player;

import net.minecraft.client.Options;

public class KeyboardInput extends Input {

    private final Options options;

    public KeyboardInput(Options options0) {
        this.options = options0;
    }

    private static float calculateImpulse(boolean boolean0, boolean boolean1) {
        if (boolean0 == boolean1) {
            return 0.0F;
        } else {
            return boolean0 ? 1.0F : -1.0F;
        }
    }

    @Override
    public void tick(boolean boolean0, float float1) {
        this.f_108568_ = this.options.keyUp.isDown();
        this.f_108569_ = this.options.keyDown.isDown();
        this.f_108570_ = this.options.keyLeft.isDown();
        this.f_108571_ = this.options.keyRight.isDown();
        this.f_108567_ = calculateImpulse(this.f_108568_, this.f_108569_);
        this.f_108566_ = calculateImpulse(this.f_108570_, this.f_108571_);
        this.f_108572_ = this.options.keyJump.isDown();
        this.f_108573_ = this.options.keyShift.isDown();
        if (boolean0) {
            this.f_108566_ *= float1;
            this.f_108567_ *= float1;
        }
    }
}