package de.keksuccino.fancymenu.events.screen;

import de.keksuccino.fancymenu.util.event.acara.EventBase;
import net.minecraft.client.gui.screens.Screen;

public class CloseScreenEvent extends EventBase {

    private final Screen screen;

    public CloseScreenEvent(Screen closedScreen) {
        this.screen = closedScreen;
    }

    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}