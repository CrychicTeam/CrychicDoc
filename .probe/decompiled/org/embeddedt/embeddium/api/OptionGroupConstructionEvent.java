package org.embeddedt.embeddium.api;

import java.util.List;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

public class OptionGroupConstructionEvent extends EmbeddiumEvent {

    public static final EventHandlerRegistrar<OptionGroupConstructionEvent> BUS = new EventHandlerRegistrar<>();

    private final OptionIdentifier<Void> id;

    private final List<Option<?>> options;

    public OptionGroupConstructionEvent(OptionIdentifier<Void> id, List<Option<?>> options) {
        this.id = id;
        this.options = options;
    }

    public List<Option<?>> getOptions() {
        return this.options;
    }

    public OptionIdentifier<Void> getId() {
        return this.id;
    }
}