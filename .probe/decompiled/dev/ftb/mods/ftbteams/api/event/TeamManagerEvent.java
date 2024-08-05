package dev.ftb.mods.ftbteams.api.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.ftb.mods.ftbteams.api.TeamManager;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;

public class TeamManagerEvent {

    public static final Event<Consumer<TeamManagerEvent>> CREATED = EventFactory.createConsumerLoop(TeamManagerEvent.class);

    public static final Event<Consumer<TeamManagerEvent>> LOADED = EventFactory.createConsumerLoop(TeamManagerEvent.class);

    public static final Event<Consumer<TeamManagerEvent>> SAVED = EventFactory.createConsumerLoop(TeamManagerEvent.class);

    public static final Event<Consumer<TeamManagerEvent>> DESTROYED = EventFactory.createConsumerLoop(TeamManagerEvent.class);

    private final TeamManager manager;

    public TeamManagerEvent(TeamManager t) {
        this.manager = t;
    }

    public TeamManager getManager() {
        return this.manager;
    }

    public CompoundTag getExtraData() {
        return this.manager.getExtraData();
    }
}