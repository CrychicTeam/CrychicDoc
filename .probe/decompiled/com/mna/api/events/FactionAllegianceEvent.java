package com.mna.api.events;

import com.mna.api.faction.IFaction;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.eventbus.api.Event;

public class FactionAllegianceEvent extends Event {

    private final IFaction forFaction;

    private final ArrayList<IFaction> default_allegiances;

    public FactionAllegianceEvent(IFaction forFaction, List<IFaction> defaultAllegiances) {
        this.forFaction = forFaction;
        this.default_allegiances = new ArrayList(defaultAllegiances);
    }

    public IFaction getFaction() {
        return this.forFaction;
    }

    public ArrayList<IFaction> getAllegiances() {
        return this.default_allegiances;
    }
}