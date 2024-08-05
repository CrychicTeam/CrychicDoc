package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;

public class TeamPropertiesChangedEvent extends TeamEvent {

    private final TeamPropertyCollection prevProps;

    public TeamPropertiesChangedEvent(Team team, TeamPropertyCollection prevProps) {
        super(team);
        this.prevProps = prevProps;
    }

    public TeamPropertyCollection getPreviousProperties() {
        return this.prevProps;
    }
}