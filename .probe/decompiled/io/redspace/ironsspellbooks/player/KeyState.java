package io.redspace.ironsspellbooks.player;

import net.minecraft.client.KeyMapping;

public class KeyState {

    private boolean isHeld;

    private final KeyMapping key;

    private int heldTicks;

    public KeyState(KeyMapping key) {
        this.key = key;
    }

    public boolean wasPressed() {
        return !this.isHeld && this.key.isDown();
    }

    public boolean wasReleased() {
        return this.isHeld && !this.key.isDown();
    }

    public boolean wasHeldMoreThan(int ticks) {
        return this.heldTicks >= ticks;
    }

    public boolean isHeld() {
        return this.isHeld;
    }

    public void update() {
        if (this.key.isDown()) {
            this.heldTicks++;
            this.isHeld = true;
        } else {
            this.heldTicks = 0;
            this.isHeld = false;
        }
    }
}