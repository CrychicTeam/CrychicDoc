package dev.ftb.mods.ftbteams.api.event;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftbteams.api.Team;
import java.util.Collection;
import java.util.List;

public class TeamAllyEvent extends TeamEvent {

    private final List<GameProfile> players;

    private final boolean adding;

    public TeamAllyEvent(Team team, List<GameProfile> players, boolean adding) {
        super(team);
        this.players = players;
        this.adding = adding;
    }

    public Collection<GameProfile> getPlayers() {
        return this.players;
    }

    public boolean isAdding() {
        return this.adding;
    }
}