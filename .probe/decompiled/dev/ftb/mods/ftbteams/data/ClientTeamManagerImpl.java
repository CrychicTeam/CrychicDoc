package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.FTBTeams;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.client.ClientTeamManager;
import dev.ftb.mods.ftbteams.api.client.KnownClientPlayer;
import dev.ftb.mods.ftbteams.client.KnownClientPlayerNet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ClientTeamManagerImpl implements ClientTeamManager {

    private static ClientTeamManagerImpl INSTANCE;

    private final UUID managerId;

    private final Map<UUID, ClientTeam> teamMap;

    private final Map<UUID, KnownClientPlayer> knownPlayers;

    private boolean valid;

    private ClientTeam selfTeam;

    private KnownClientPlayer selfKnownPlayer;

    public static ClientTeamManagerImpl getInstance() {
        return INSTANCE;
    }

    private ClientTeamManagerImpl(UUID managerId) {
        this.managerId = managerId;
        this.valid = true;
        this.teamMap = new HashMap();
        this.knownPlayers = new HashMap();
    }

    public static ClientTeamManagerImpl fromNetwork(FriendlyByteBuf buffer) {
        ClientTeamManagerImpl manager = new ClientTeamManagerImpl(buffer.readUUID());
        int nTeams = buffer.readVarInt();
        for (int i = 0; i < nTeams; i++) {
            ClientTeam t = ClientTeam.fromNetwork(buffer);
            manager.teamMap.put(t.getId(), t);
        }
        int nPlayers = buffer.readVarInt();
        for (int i = 0; i < nPlayers; i++) {
            KnownClientPlayer knownClientPlayer = KnownClientPlayerNet.fromNetwork(buffer);
            manager.knownPlayers.put(knownClientPlayer.id(), knownClientPlayer);
        }
        return manager;
    }

    public static ClientTeamManagerImpl forSyncing(TeamManagerImpl manager, Collection<? extends Team> teams) {
        ClientTeamManagerImpl clientManager = new ClientTeamManagerImpl(manager.getId());
        for (Team team : teams) {
            if (team instanceof AbstractTeam abstractTeam) {
                ClientTeam clientTeam = manager.getTeamMap().containsKey(team.getId()) ? ClientTeam.copyOf(abstractTeam) : ClientTeam.invalidTeam(abstractTeam);
                clientManager.addTeam(clientTeam);
            }
            if (team instanceof PlayerTeam playerTeam) {
                clientManager.knownPlayers.put(team.getId(), playerTeam.createClientPlayer());
            }
        }
        return clientManager;
    }

    @Override
    public UUID getManagerId() {
        return this.managerId;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    @Override
    public Collection<KnownClientPlayer> knownClientPlayers() {
        return Collections.unmodifiableCollection(this.knownPlayers.values());
    }

    @Override
    public Collection<Team> getTeams() {
        return Collections.unmodifiableCollection(this.teamMap.values());
    }

    @Override
    public Optional<Team> getTeamByID(UUID teamId) {
        return Optional.ofNullable((Team) this.teamMap.get(teamId));
    }

    public ClientTeam selfTeam() {
        return this.selfTeam;
    }

    @Override
    public KnownClientPlayer self() {
        return this.selfKnownPlayer;
    }

    public void write(FriendlyByteBuf buffer, UUID selfTeamID) {
        buffer.writeUUID(this.getManagerId());
        buffer.writeVarInt(this.teamMap.size());
        this.teamMap.values().forEach(clientTeam -> clientTeam.write(buffer, selfTeamID.equals(clientTeam.getId())));
        buffer.writeVarInt(this.knownPlayers.size());
        for (KnownClientPlayer knownClientPlayer : this.knownPlayers.values()) {
            KnownClientPlayerNet.write(knownClientPlayer, buffer);
        }
    }

    public void initSelfDetails(UUID selfTeamID) {
        this.selfTeam = (ClientTeam) this.teamMap.get(selfTeamID);
        UUID userId = Minecraft.getInstance().getUser().getGameProfile().getId();
        this.selfKnownPlayer = (KnownClientPlayer) this.knownPlayers.get(userId);
        if (this.selfKnownPlayer == null) {
            FTBTeams.LOGGER.error("Local player id {} was not found in the known players list [{}]! FTB Teams will not be able to function correctly!", userId, String.join(",", this.knownPlayers.keySet().stream().map(UUID::toString).toList()));
        }
    }

    @Override
    public Optional<KnownClientPlayer> getKnownPlayer(UUID id) {
        return Optional.ofNullable((KnownClientPlayer) this.knownPlayers.get(id));
    }

    public Optional<ClientTeam> getTeam(UUID id) {
        return Optional.ofNullable((ClientTeam) this.teamMap.get(id));
    }

    @Override
    public Component formatName(@Nullable UUID id) {
        if (id != null && !id.equals(Util.NIL_UUID)) {
            KnownClientPlayer p = (KnownClientPlayer) this.knownPlayers.get(id);
            return Component.literal(p == null ? "Unknown" : p.name()).withStyle(ChatFormatting.YELLOW);
        } else {
            return Component.literal("System").withStyle(ChatFormatting.LIGHT_PURPLE);
        }
    }

    public void addTeam(ClientTeam team) {
        this.teamMap.put(team.getId(), team);
    }

    private void invalidate() {
        this.teamMap.clear();
        this.valid = false;
    }

    public static void syncFromServer(ClientTeamManagerImpl syncedData, UUID selfTeamID, boolean fullSync) {
        if (fullSync) {
            syncedData.initSelfDetails(selfTeamID);
            if (INSTANCE != null) {
                INSTANCE.invalidate();
            }
            INSTANCE = syncedData;
        } else if (INSTANCE != null) {
            syncedData.teamMap.forEach((teamID, clientTeam) -> {
                if (clientTeam.toBeRemoved()) {
                    FTBTeams.LOGGER.debug("remove {} from client team map", teamID);
                    INSTANCE.teamMap.remove(teamID);
                } else {
                    ClientTeam existing = (ClientTeam) INSTANCE.teamMap.get(teamID);
                    if (existing != null) {
                        FTBTeams.LOGGER.debug("update {} in client team map", teamID);
                    } else {
                        FTBTeams.LOGGER.debug("insert {} into client team map", teamID);
                    }
                    INSTANCE.teamMap.put(teamID, clientTeam);
                }
            });
            INSTANCE.knownPlayers.putAll(syncedData.knownPlayers);
            INSTANCE.initSelfDetails(selfTeamID);
        }
    }

    public void updatePresence(KnownClientPlayer newPlayer) {
        KnownClientPlayer existing = (KnownClientPlayer) INSTANCE.knownPlayers.get(newPlayer.id());
        KnownClientPlayer toUpdate = existing == null ? newPlayer : this.updateFrom(existing.id(), newPlayer);
        this.knownPlayers.put(toUpdate.id(), newPlayer);
        FTBTeams.LOGGER.debug("Updated presence of " + newPlayer.name());
    }

    private KnownClientPlayer updateFrom(UUID id, KnownClientPlayer other) {
        return new KnownClientPlayer(id, other.name(), other.online(), other.teamId(), other.profile(), other.extraData());
    }
}