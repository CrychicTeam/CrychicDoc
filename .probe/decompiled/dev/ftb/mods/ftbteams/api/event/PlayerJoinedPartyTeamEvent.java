package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinedPartyTeamEvent extends TeamEvent {

    private final Team previousTeam;

    private final ServerPlayer player;

    public PlayerJoinedPartyTeamEvent(Team team, Team previousTeam, ServerPlayer player) {
        super(team);
        this.previousTeam = previousTeam;
        this.player = player;
    }

    public Team getPreviousTeam() {
        return this.previousTeam;
    }

    @NotNull
    public ServerPlayer getPlayer() {
        return this.player;
    }
}