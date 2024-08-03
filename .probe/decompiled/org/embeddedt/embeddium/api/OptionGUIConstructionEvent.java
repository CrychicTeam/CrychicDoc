package org.embeddedt.embeddium.api;

import java.util.List;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;

public class OptionGUIConstructionEvent extends EmbeddiumEvent {

    public static final EventHandlerRegistrar<OptionGUIConstructionEvent> BUS = new EventHandlerRegistrar<>();

    private final List<OptionPage> pages;

    public OptionGUIConstructionEvent(List<OptionPage> pages) {
        this.pages = pages;
    }

    public List<OptionPage> getPages() {
        return this.pages;
    }

    public void addPage(OptionPage page) {
        this.pages.add(page);
    }
}