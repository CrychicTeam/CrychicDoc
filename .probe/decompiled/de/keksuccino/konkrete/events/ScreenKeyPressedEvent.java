package de.keksuccino.konkrete.events;

import net.minecraftforge.eventbus.api.Event;

public class ScreenKeyPressedEvent extends Event {

    public final int keyCode;

    public final int scanCode;

    public final int modifiers;

    public ScreenKeyPressedEvent(int keyCode, int scanCode, int modifiers) {
        this.keyCode = keyCode;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
    }

    public boolean isCancelable() {
        return false;
    }
}