package com.blamejared.controlling.client;

import net.minecraft.network.chat.Component;

public class DisplayableBoolean {

    private boolean state;

    private final Component whenTrue;

    private final Component whenFalse;

    public DisplayableBoolean(boolean initialState, Component whenTrue, Component whenFalse) {
        this.state = initialState;
        this.whenTrue = whenTrue;
        this.whenFalse = whenFalse;
    }

    public boolean state() {
        return this.state;
    }

    public boolean toggle() {
        this.state(!this.state());
        return this.state();
    }

    public void state(boolean state) {
        this.state = state;
    }

    public Component currentDisplay() {
        return this.state ? this.whenTrue() : this.whenFalse();
    }

    public Component whenTrue() {
        return this.whenTrue;
    }

    public Component whenFalse() {
        return this.whenFalse;
    }
}