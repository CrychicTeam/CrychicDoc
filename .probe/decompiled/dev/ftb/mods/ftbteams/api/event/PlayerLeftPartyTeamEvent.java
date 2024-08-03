package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class PlayerLeftPartyTeamEvent extends TeamEvent {

    private final Team playerTeam;

    private final UUID playerId;

    private final ServerPlayer player;

    private final boolean teamDeleted;

    public PlayerLeftPartyTeamEvent(Team team, Team playerTeam, UUID playerId, @Nullable ServerPlayer player, boolean teamDeleted) {
        super(team);
        this.playerTeam = playerTeam;
        this.playerId = playerId;
        this.player = player;
        this.teamDeleted = teamDeleted;
    }

    public Team getPlayerTeam() {
        return this.playerTeam;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.player;
    }

    public boolean getTeamDeleted() {
        return this.teamDeleted;
    }
}