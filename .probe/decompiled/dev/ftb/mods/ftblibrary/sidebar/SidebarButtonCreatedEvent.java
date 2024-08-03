package dev.ftb.mods.ftblibrary.sidebar;

import dev.architectury.annotations.ForgeEvent;
import dev.architectury.event.EventFactory;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.Event;

@ForgeEvent
public class SidebarButtonCreatedEvent extends Event {

    public static final dev.architectury.event.Event<Consumer<SidebarButtonCreatedEvent>> EVENT = EventFactory.createConsumerLoop(SidebarButtonCreatedEvent.class);

    private final SidebarButton button;

    public SidebarButtonCreatedEvent(SidebarButton b) {
        this.button = b;
    }

    public SidebarButton getButton() {
        return this.button;
    }
}