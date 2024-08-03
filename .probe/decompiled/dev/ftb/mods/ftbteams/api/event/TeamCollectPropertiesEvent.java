package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import java.util.function.Consumer;

public class TeamCollectPropertiesEvent {

    private final Consumer<TeamProperty<?>> callback;

    public TeamCollectPropertiesEvent(Consumer<TeamProperty<?>> c) {
        this.callback = c;
    }

    public void add(TeamProperty<?> property) {
        this.callback.accept(property);
    }
}