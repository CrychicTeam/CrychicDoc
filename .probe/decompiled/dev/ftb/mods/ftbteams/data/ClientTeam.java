package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.ClientTeamPropertiesChangedEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ClientTeam extends AbstractTeamBase {

    private static final List<TeamProperty<?>> SYNCABLE_PROPS = List.of(TeamProperties.DISPLAY_NAME, TeamProperties.COLOR);

    private final TeamType type;

    private final UUID ownerID;

    private final boolean toBeRemoved;

    private ClientTeam(UUID id, UUID ownerId, TeamType type, boolean toBeRemoved) {
        super(id);
        this.ownerID = ownerId;
        this.type = type;
        this.toBeRemoved = toBeRemoved;
    }

    public static ClientTeam invalidTeam(AbstractTeam team) {
        return new ClientTeam(team.getId(), Util.NIL_UUID, team.getType(), true);
    }

    public static ClientTeam fromNetwork(FriendlyByteBuf buffer) {
        UUID id = buffer.readUUID();
        UUID ownerID = buffer.readBoolean() ? buffer.readUUID() : Util.NIL_UUID;
        TeamType type = buffer.readEnum(TeamType.class);
        boolean mustRemove = buffer.readBoolean();
        ClientTeam clientTeam = new ClientTeam(id, ownerID, type, mustRemove);
        clientTeam.properties.read(buffer);
        int nMembers = buffer.readVarInt();
        for (int i = 0; i < nMembers; i++) {
            clientTeam.addMember(buffer.readUUID(), buffer.readEnum(TeamRank.class));
        }
        clientTeam.extraData = buffer.readNbt();
        return clientTeam;
    }

    public static ClientTeam copyOf(AbstractTeam team) {
        ClientTeam clientTeam = new ClientTeam(team.id, team.getOwner(), team.getType(), false);
        clientTeam.properties.updateFrom(team.properties);
        clientTeam.ranks.putAll(team.ranks);
        clientTeam.extraData = team.extraData == null ? null : team.extraData.copy();
        return clientTeam;
    }

    @Override
    public TeamType getType() {
        return this.type;
    }

    @Override
    public UUID getOwner() {
        return this.ownerID;
    }

    @Override
    public void sendMessage(UUID senderId, String message) {
    }

    @Override
    public List<Component> getTeamInfo() {
        return List.of();
    }

    @Override
    public boolean isClientTeam() {
        return true;
    }

    @Override
    public Collection<ServerPlayer> getOnlineMembers() {
        return List.of();
    }

    @Override
    public boolean isValid() {
        return !this.toBeRemoved();
    }

    @Override
    public boolean isPlayerTeam() {
        return this.type == TeamType.PLAYER;
    }

    @Override
    public boolean isPartyTeam() {
        return this.type == TeamType.PARTY;
    }

    @Override
    public boolean isServerTeam() {
        return this.type == TeamType.SERVER;
    }

    public void write(FriendlyByteBuf buffer, boolean writeAllProperties) {
        buffer.writeUUID(this.id);
        boolean hasOwner = !this.ownerID.equals(Util.NIL_UUID);
        buffer.writeBoolean(hasOwner);
        if (hasOwner) {
            buffer.writeUUID(this.ownerID);
        }
        buffer.writeEnum(this.type);
        buffer.writeBoolean(this.toBeRemoved);
        if (writeAllProperties) {
            this.properties.write(buffer);
        } else {
            this.properties.writeSyncableOnly(buffer, SYNCABLE_PROPS);
        }
        buffer.writeVarInt(this.ranks.size());
        for (Entry<UUID, TeamRank> entry : this.ranks.entrySet()) {
            buffer.writeUUID((UUID) entry.getKey());
            buffer.writeEnum((Enum<?>) entry.getValue());
        }
        buffer.writeNbt(this.extraData);
    }

    public void setMessageHistory(List<TeamMessage> messages) {
        this.messageHistory.clear();
        this.messageHistory.addAll(messages);
    }

    public boolean toBeRemoved() {
        return this.toBeRemoved;
    }

    public void updateProperties(TeamPropertyCollection newProps) {
        TeamPropertyCollection old = this.properties.copy();
        this.properties.updateFrom(newProps);
        TeamEvent.CLIENT_PROPERTIES_CHANGED.invoker().accept(new ClientTeamPropertiesChangedEvent(this, old));
    }
}