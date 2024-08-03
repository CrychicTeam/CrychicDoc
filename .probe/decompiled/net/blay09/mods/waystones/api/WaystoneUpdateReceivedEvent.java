package net.blay09.mods.waystones.api;

import net.blay09.mods.balm.api.event.BalmEvent;

public class WaystoneUpdateReceivedEvent extends BalmEvent {

    private final IWaystone waystone;

    public WaystoneUpdateReceivedEvent(IWaystone waystone) {
        this.waystone = waystone;
    }

    public IWaystone getWaystone() {
        return this.waystone;
    }
}