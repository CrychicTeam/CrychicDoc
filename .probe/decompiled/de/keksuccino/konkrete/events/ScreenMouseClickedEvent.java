package de.keksuccino.konkrete.events;

import net.minecraftforge.eventbus.api.Event;

public class ScreenMouseClickedEvent extends Event {

    public final double mouseX;

    public final double mouseY;

    public final int mouseButton;

    public ScreenMouseClickedEvent(double mouseX, double mouseY, int mouseButton) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.mouseButton = mouseButton;
    }

    public boolean isCancelable() {
        return false;
    }
}