package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class PlayerChangedTeamEvent extends TeamEvent {

    private final Team previousTeam;

    private final UUID playerId;

    private final ServerPlayer player;

    public PlayerChangedTeamEvent(Team newTeam, @Nullable Team previousTeam, UUID playerId, @Nullable ServerPlayer player) {
        super(newTeam);
        this.previousTeam = previousTeam;
        this.playerId = playerId;
        this.player = player;
    }

    public Optional<Team> getPreviousTeam() {
        return Optional.ofNullable(this.previousTeam);
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.player;
    }
}