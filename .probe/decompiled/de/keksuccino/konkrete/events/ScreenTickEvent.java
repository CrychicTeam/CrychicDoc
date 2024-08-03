package de.keksuccino.konkrete.events;

import net.minecraftforge.eventbus.api.Event;

public class ScreenTickEvent extends Event {

    public boolean isCancelable() {
        return false;
    }
}