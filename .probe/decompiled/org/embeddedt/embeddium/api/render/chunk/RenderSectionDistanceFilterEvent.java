package org.embeddedt.embeddium.api.render.chunk;

import org.embeddedt.embeddium.api.eventbus.EmbeddiumEvent;
import org.embeddedt.embeddium.api.eventbus.EventHandlerRegistrar;

public class RenderSectionDistanceFilterEvent extends EmbeddiumEvent {

    public static final EventHandlerRegistrar<RenderSectionDistanceFilterEvent> BUS = new EventHandlerRegistrar<>();

    private RenderSectionDistanceFilter filter = RenderSectionDistanceFilter.DEFAULT;

    public RenderSectionDistanceFilter getFilter() {
        return this.filter;
    }

    public void setFilter(RenderSectionDistanceFilter filter) {
        this.filter = filter;
    }
}