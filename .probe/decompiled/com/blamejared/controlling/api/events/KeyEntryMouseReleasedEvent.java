package com.blamejared.controlling.api.events;

import com.blamejared.controlling.client.NewKeyBindsList;
import net.minecraftforge.eventbus.api.Event;

public class KeyEntryMouseReleasedEvent extends Event implements IKeyEntryMouseReleasedEvent {

    private final NewKeyBindsList.KeyEntry entry;

    private final double mouseX;

    private final double mouseY;

    private final int buttonId;

    private boolean handled;

    public KeyEntryMouseReleasedEvent(NewKeyBindsList.KeyEntry entry, double mouseX, double mouseY, int buttonId) {
        this.entry = entry;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.buttonId = buttonId;
    }

    @Override
    public NewKeyBindsList.KeyEntry getEntry() {
        return this.entry;
    }

    @Override
    public double getMouseX() {
        return this.mouseX;
    }

    @Override
    public double getMouseY() {
        return this.mouseY;
    }

    @Override
    public int getButtonId() {
        return this.buttonId;
    }

    @Override
    public boolean isHandled() {
        return this.handled;
    }

    @Override
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}