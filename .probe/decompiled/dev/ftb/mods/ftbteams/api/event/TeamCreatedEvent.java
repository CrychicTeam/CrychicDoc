package dev.ftb.mods.ftbteams.api.event;

import dev.ftb.mods.ftbteams.api.Team;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamCreatedEvent extends TeamEvent {

    private final ServerPlayer creator;

    private final UUID creatorId;

    public TeamCreatedEvent(Team team, @Nullable ServerPlayer creator, @NotNull UUID creatorId) {
        super(team);
        this.creator = creator;
        this.creatorId = creatorId;
    }

    public ServerPlayer getCreator() {
        return this.creator;
    }

    public UUID getCreatorId() {
        return this.creatorId;
    }
}