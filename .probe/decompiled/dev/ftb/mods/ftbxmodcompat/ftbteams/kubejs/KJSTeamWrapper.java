package dev.ftb.mods.ftbxmodcompat.ftbteams.kubejs;

import dev.ftb.mods.ftbteams.api.Team;
import java.util.UUID;

public class KJSTeamWrapper {

    private final Team team;

    public KJSTeamWrapper(Team team) {
        this.team = team;
    }

    public UUID getId() {
        return this.team.getId();
    }

    public String getName() {
        return this.team.getShortName();
    }

    public boolean isPartyTeam() {
        return this.team.isPartyTeam();
    }
}