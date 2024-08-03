package dev.xkmc.l2library.init.events;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class FineScrollEvent extends Event {

    public int diff;

    public FineScrollEvent(int diff) {
        this.diff = diff;
    }
}