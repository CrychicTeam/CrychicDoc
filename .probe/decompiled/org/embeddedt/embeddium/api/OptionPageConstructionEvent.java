package org.embeddedt.embeddium.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import net.minecraft.network.chat.Component;
import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

public class OptionPageConstructionEvent extends EmbeddiumEvent {

    public static final EventHandlerRegistrar<OptionPageConstructionEvent> BUS = new EventHandlerRegistrar<>();

    private final OptionIdentifier<Void> id;

    private final Component name;

    private final List<OptionGroup> additionalGroups = new ArrayList();

    public OptionPageConstructionEvent(OptionIdentifier<Void> id, Component name) {
        this.id = id;
        this.name = name;
    }

    public OptionIdentifier<Void> getId() {
        return this.id;
    }

    public Component getName() {
        return this.name;
    }

    public void addGroup(OptionGroup group) {
        this.additionalGroups.add(group);
    }

    public List<OptionGroup> getAdditionalGroups() {
        return Collections.unmodifiableList(this.additionalGroups);
    }
}