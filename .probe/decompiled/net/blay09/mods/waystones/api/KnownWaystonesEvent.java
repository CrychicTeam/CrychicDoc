package net.blay09.mods.waystones.api;

import java.util.List;
import net.blay09.mods.balm.api.event.BalmEvent;

public class KnownWaystonesEvent extends BalmEvent {

    private final List<IWaystone> waystones;

    public KnownWaystonesEvent(List<IWaystone> waystones) {
        this.waystones = waystones;
    }

    public List<IWaystone> getWaystones() {
        return this.waystones;
    }
}