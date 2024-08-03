package dev.ftb.mods.ftbteams.api.event;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.PartyTeam;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class PlayerTransferredTeamOwnershipEvent extends TeamEvent {

    private final ServerPlayer from;

    private final ServerPlayer to;

    private final GameProfile toProfile;

    public PlayerTransferredTeamOwnershipEvent(Team team, ServerPlayer prevOwner, ServerPlayer newOwner) {
        super(team);
        this.from = prevOwner;
        this.to = newOwner;
        this.toProfile = null;
    }

    public PlayerTransferredTeamOwnershipEvent(PartyTeam t, ServerPlayer from, GameProfile toProfile) {
        super(t);
        this.from = from;
        this.to = null;
        this.toProfile = toProfile;
    }

    @Nullable
    public ServerPlayer getFrom() {
        return this.from;
    }

    @Nullable
    public ServerPlayer getTo() {
        return this.to;
    }

    public GameProfile getToProfile() {
        return this.to == null ? this.toProfile : this.to.m_36316_();
    }
}