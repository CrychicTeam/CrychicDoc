package com.blamejared.controlling.api.events;

import com.blamejared.controlling.client.NewKeyBindsList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.eventbus.api.Event;

public class KeyEntryListenersEvent extends Event implements IKeyEntryListenersEvent {

    private final NewKeyBindsList.KeyEntry entry;

    private final List<GuiEventListener> listeners;

    public KeyEntryListenersEvent(NewKeyBindsList.KeyEntry entry) {
        this.entry = entry;
        this.listeners = new ArrayList();
        this.getListeners().add(entry.getBtnChangeKeyBinding());
        this.getListeners().add(entry.getBtnResetKeyBinding());
    }

    @Override
    public List<GuiEventListener> getListeners() {
        return this.listeners;
    }

    @Override
    public NewKeyBindsList.KeyEntry getEntry() {
        return this.entry;
    }
}