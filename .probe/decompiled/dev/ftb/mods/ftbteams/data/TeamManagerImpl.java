package dev.ftb.mods.ftbteams.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.snbt.SNBT;
import dev.ftb.mods.ftblibrary.snbt.SNBTCompoundTag;
import dev.ftb.mods.ftbteams.FTBTeams;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamManager;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.event.PlayerLoggedInAfterTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamManagerEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.net.SyncMessageHistoryMessage;
import dev.ftb.mods.ftbteams.net.SyncTeamsMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class TeamManagerImpl implements TeamManager {

    public static final LevelResource FOLDER_NAME = new LevelResource("ftbteams");

    public static TeamManagerImpl INSTANCE;

    private final MinecraftServer server;

    private UUID id;

    private boolean shouldSave;

    private final Map<UUID, PlayerTeam> knownPlayers;

    private final Map<UUID, AbstractTeam> teamMap;

    Map<String, Team> nameMap;

    private CompoundTag extraData;

    public TeamManagerImpl(MinecraftServer s) {
        this.server = s;
        this.knownPlayers = new LinkedHashMap();
        this.teamMap = new LinkedHashMap();
        this.extraData = new CompoundTag();
    }

    @Override
    public MinecraftServer getServer() {
        return this.server;
    }

    @Override
    public UUID getId() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return this.id;
    }

    @Override
    public Map<UUID, ? extends Team> getKnownPlayerTeams() {
        return Collections.unmodifiableMap(this.knownPlayers);
    }

    public Map<UUID, AbstractTeam> getTeamMap() {
        return this.teamMap;
    }

    @Override
    public Collection<Team> getTeams() {
        Builder<Team> b = ImmutableList.builderWithExpectedSize(this.getTeamMap().size());
        this.teamMap.values().forEach(b::add);
        return b.build();
    }

    public Map<String, Team> getTeamNameMap() {
        if (this.nameMap == null) {
            this.nameMap = new HashMap();
            for (AbstractTeam team : this.teamMap.values()) {
                this.nameMap.put(team.getShortName(), team);
            }
        }
        return this.nameMap;
    }

    @Override
    public Optional<Team> getTeamByID(UUID teamId) {
        return Optional.of((Team) this.teamMap.get(teamId));
    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return Optional.ofNullable((Team) this.getTeamNameMap().get(name));
    }

    @Override
    public Optional<Team> getPlayerTeamForPlayerID(UUID uuid) {
        return Optional.ofNullable(this.getPersonalTeamForPlayerID(uuid));
    }

    public PlayerTeam getPersonalTeamForPlayerID(UUID uuid) {
        return (PlayerTeam) this.knownPlayers.get(uuid);
    }

    @Override
    public Optional<Team> getTeamForPlayerID(UUID uuid) {
        PlayerTeam t = (PlayerTeam) this.knownPlayers.get(uuid);
        return t == null ? Optional.empty() : Optional.ofNullable(t.getEffectiveTeam());
    }

    @Override
    public Optional<Team> getTeamForPlayer(ServerPlayer player) {
        return this.getTeamForPlayerID(player.m_20148_());
    }

    @Override
    public boolean arePlayersInSameTeam(UUID id1, UUID id2) {
        return (Boolean) this.getTeamForPlayerID(id1).map(team1 -> (Boolean) this.getTeamForPlayerID(id2).map(team2 -> team1.getId().equals(team2.getId())).orElse(false)).orElse(false);
    }

    public void load() {
        this.id = null;
        Path directory = this.server.getWorldPath(FOLDER_NAME);
        if (!Files.notExists(directory, new LinkOption[0]) && Files.isDirectory(directory, new LinkOption[0])) {
            CompoundTag dataFileTag = SNBT.read(directory.resolve("ftbteams.snbt"));
            if (dataFileTag != null) {
                if (dataFileTag.contains("id")) {
                    this.id = UUID.fromString(dataFileTag.getString("id"));
                }
                this.extraData = dataFileTag.getCompound("extra");
                TeamManagerEvent.LOADED.invoker().accept(new TeamManagerEvent(this));
            }
            for (TeamType type : TeamType.values()) {
                Path dir = directory.resolve(type.getSerializedName());
                if (Files.exists(dir, new LinkOption[0]) && Files.isDirectory(dir, new LinkOption[0])) {
                    try {
                        Stream<Path> s = Files.list(dir);
                        try {
                            s.filter(path -> path.getFileName().toString().endsWith(".snbt")).forEach(file -> {
                                CompoundTag nbt = SNBT.read(file);
                                if (nbt != null) {
                                    AbstractTeam team = type.createTeam(this, UUID.fromString(nbt.getString("id")));
                                    this.teamMap.put(team.id, team);
                                    team.deserializeNBT(nbt);
                                }
                            });
                        } catch (Throwable var12) {
                            if (s != null) {
                                try {
                                    s.close();
                                } catch (Throwable var11) {
                                    var12.addSuppressed(var11);
                                }
                            }
                            throw var12;
                        }
                        if (s != null) {
                            s.close();
                        }
                    } catch (Exception var13) {
                        FTBTeams.LOGGER.error("can't list directory {}: {}", dir, var13.getMessage());
                    }
                }
            }
            for (AbstractTeam team : this.teamMap.values()) {
                if (team instanceof PlayerTeam) {
                    this.knownPlayers.put(team.id, (PlayerTeam) team);
                }
            }
            for (AbstractTeam teamx : this.teamMap.values()) {
                if (teamx instanceof PartyTeam) {
                    for (UUID member : teamx.getMembers()) {
                        PlayerTeam t = (PlayerTeam) this.knownPlayers.get(member);
                        if (t != null) {
                            t.setEffectiveTeam(teamx);
                        }
                    }
                }
            }
            FTBTeams.LOGGER.info("loaded team data: {} known players, {} teams total", this.knownPlayers.size(), this.teamMap.size());
        }
    }

    @Override
    public void markDirty() {
        this.shouldSave = true;
        this.nameMap = null;
    }

    public void saveNow() {
        Path directory = this.server.getWorldPath(FOLDER_NAME);
        if (Files.notExists(directory, new LinkOption[0])) {
            try {
                Files.createDirectories(directory);
            } catch (Exception var9) {
                FTBTeams.LOGGER.error("can't create directory {}: {}", directory, var9.getMessage());
            }
        }
        if (this.shouldSave) {
            TeamManagerEvent.SAVED.invoker().accept(new TeamManagerEvent(this));
            SNBT.write(directory.resolve("ftbteams.snbt"), this.serializeNBT());
            this.shouldSave = false;
        }
        for (TeamType type : TeamType.values()) {
            Path path = directory.resolve(type.getSerializedName());
            if (Files.notExists(path, new LinkOption[0])) {
                try {
                    Files.createDirectories(path);
                } catch (Exception var8) {
                    FTBTeams.LOGGER.error("can't create directory {}: {}", path, var8.getMessage());
                }
            }
        }
        for (AbstractTeam team : this.teamMap.values()) {
            team.saveIfNeeded(directory);
        }
    }

    public SNBTCompoundTag serializeNBT() {
        SNBTCompoundTag nbt = new SNBTCompoundTag();
        nbt.m_128359_("id", this.getId().toString());
        nbt.m_128365_("extra", this.extraData);
        return nbt;
    }

    private ServerTeam createServerTeam(UUID playerId, ServerPlayer player, String name) {
        ServerTeam team = new ServerTeam(this, UUID.randomUUID());
        this.teamMap.put(team.id, team);
        team.setProperty(TeamProperties.DISPLAY_NAME, name.isEmpty() ? team.id.toString().substring(0, 8) : name);
        team.setProperty(TeamProperties.COLOR, FTBTUtils.randomColor());
        team.onCreated(player, playerId);
        return team;
    }

    private PartyTeam createPartyTeamInternal(UUID playerId, @Nullable ServerPlayer player, String name) {
        PartyTeam team = new PartyTeam(this, UUID.randomUUID());
        team.owner = playerId;
        this.teamMap.put(team.id, team);
        team.setProperty(TeamProperties.DISPLAY_NAME, name.isEmpty() ? FTBTUtils.getDefaultPartyName(this.server, playerId, player) : name);
        team.setProperty(TeamProperties.COLOR, FTBTUtils.randomColor());
        team.onCreated(player, playerId);
        return team;
    }

    private PlayerTeam createPlayerTeam(UUID playerId, String playerName) {
        PlayerTeam team = new PlayerTeam(this, playerId);
        team.setPlayerName(playerName);
        team.setProperty(TeamProperties.DISPLAY_NAME, playerName);
        team.setProperty(TeamProperties.COLOR, FTBTUtils.randomColor());
        team.addMember(playerId, TeamRank.OWNER);
        return team;
    }

    public void playerLoggedIn(@Nullable ServerPlayer player, UUID id, String name) {
        PlayerTeam team = (PlayerTeam) this.knownPlayers.get(id);
        boolean syncToAll = false;
        FTBTeams.LOGGER.debug("player {} logged in, player team = {}", id, team);
        if (team == null) {
            FTBTeams.LOGGER.debug("creating new player team for player {}", id);
            team = this.createPlayerTeam(id, name);
            this.teamMap.put(id, team);
            this.knownPlayers.put(id, team);
            team.onCreated(player, id);
            syncToAll = true;
            team.onPlayerChangeTeam(null, id, player, false);
            FTBTeams.LOGGER.debug("  - team created");
        } else if (!team.getPlayerName().equals(name)) {
            FTBTeams.LOGGER.debug("updating player name: {} -> {}", team.getPlayerName(), name);
            team.setPlayerName(name);
            team.markDirty();
            this.markDirty();
            syncToAll = true;
        }
        FTBTeams.LOGGER.debug("syncing player team data, all = {}", syncToAll);
        if (player != null) {
            this.syncAllToPlayer(player, team.getEffectiveTeam());
        }
        if (syncToAll) {
            this.syncToAll(team.getEffectiveTeam());
        }
        FTBTeams.LOGGER.debug("updating team presence");
        team.setOnline(true);
        team.updatePresence();
        if (player != null) {
            FTBTeams.LOGGER.debug("sending team login event for {}...", player.m_20148_());
            TeamEvent.PLAYER_LOGGED_IN.invoker().accept(new PlayerLoggedInAfterTeamEvent(team.getEffectiveTeam(), player));
            FTBTeams.LOGGER.debug("team login event for {} sent", player.m_20148_());
        }
    }

    public void playerLoggedOut(ServerPlayer player) {
        PlayerTeam team = (PlayerTeam) this.knownPlayers.get(player.m_20148_());
        if (team != null) {
            team.setOnline(false);
            team.updatePresence();
        }
    }

    public void syncAllToPlayer(ServerPlayer player, AbstractTeam selfTeam) {
        ClientTeamManagerImpl manager = ClientTeamManagerImpl.forSyncing(this, this.teamMap.values());
        new SyncTeamsMessage(manager, selfTeam, true).sendTo(player);
        new SyncMessageHistoryMessage(selfTeam).sendTo(player);
        this.server.getPlayerList().sendPlayerPermissionLevel(player);
    }

    public void syncToAll(Team... teams) {
        if (teams.length != 0) {
            ClientTeamManagerImpl manager = ClientTeamManagerImpl.forSyncing(this, Arrays.stream(teams).toList());
            for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
                this.getTeamForPlayer(player).ifPresent(selfTeam -> {
                    new SyncTeamsMessage(manager, selfTeam, false).sendTo(player);
                    if (teams.length > 1) {
                        new SyncMessageHistoryMessage(selfTeam).sendTo(player);
                    }
                });
            }
        }
    }

    @Override
    public Team createPartyTeam(ServerPlayer player, String name, @Nullable String description, @Nullable Color4I color) throws CommandSyntaxException {
        Pair<Integer, PartyTeam> res = this.createParty(player.m_20148_(), player, name, description, color);
        return (Team) res.getRight();
    }

    public Pair<Integer, PartyTeam> createParty(ServerPlayer player, String name) throws CommandSyntaxException {
        return this.createParty(player.m_20148_(), player, name, null, null);
    }

    public Pair<Integer, PartyTeam> createParty(UUID playerId, @Nullable ServerPlayer player, String name, @Nullable String description, @Nullable Color4I color) throws CommandSyntaxException {
        if (player != null && !FTBTUtils.canPlayerUseCommand(player, "ftbteams.party.create")) {
            throw TeamArgument.NO_PERMISSION.create();
        } else {
            Team oldTeam = (Team) this.getTeamForPlayerID(playerId).orElseThrow(() -> TeamArgument.TEAM_NOT_FOUND.create(playerId));
            if (oldTeam instanceof PlayerTeam playerTeam) {
                PartyTeam team = this.createPartyTeamInternal(playerId, player, name);
                if (description != null) {
                    team.setProperty(TeamProperties.DESCRIPTION, description);
                }
                if (color != null) {
                    team.setProperty(TeamProperties.COLOR, color);
                }
                playerTeam.setEffectiveTeam(team);
                Component playerName = (Component) (player != null ? player.m_7755_() : Component.literal(playerId.toString()));
                team.addMember(playerId, TeamRank.OWNER);
                team.sendMessage(Util.NIL_UUID, Component.translatable("ftbteams.message.joined", playerName).withStyle(ChatFormatting.YELLOW));
                team.markDirty();
                playerTeam.removeMember(playerId);
                playerTeam.markDirty();
                playerTeam.updatePresence();
                this.syncToAll(team, playerTeam);
                team.onPlayerChangeTeam(playerTeam, playerId, player, false);
                return Pair.of(1, team);
            } else {
                throw TeamArgument.ALREADY_IN_PARTY.create();
            }
        }
    }

    public Pair<Integer, ServerTeam> createServer(CommandSourceStack source, String name) throws CommandSyntaxException {
        if (name.length() < 3) {
            throw TeamArgument.NAME_TOO_SHORT.create();
        } else {
            ServerPlayer player = source.getPlayer();
            UUID playerId = player == null ? Util.NIL_UUID : player.m_20148_();
            ServerTeam team = this.createServerTeam(playerId, source.getPlayer(), name);
            source.sendSuccess(() -> Component.translatable("ftbteams.message.created_server_team", team.getName()), true);
            this.syncToAll(team);
            return Pair.of(1, team);
        }
    }

    public Component getPlayerName(@Nullable UUID id) {
        if (id != null && !id.equals(Util.NIL_UUID)) {
            PlayerTeam team = (PlayerTeam) this.knownPlayers.get(id);
            return Component.literal(team == null ? "Unknown" : team.getPlayerName()).withStyle(ChatFormatting.YELLOW);
        } else {
            return Component.literal("System").withStyle(ChatFormatting.LIGHT_PURPLE);
        }
    }

    @Override
    public CompoundTag getExtraData() {
        return this.extraData;
    }

    void deleteTeam(Team team) {
        this.teamMap.remove(team.getId());
        this.markDirty();
    }

    void tryDeleteTeamFile(String teamFileName, String subfolderName) {
        Path deletedPath = this.getServer().getWorldPath(FOLDER_NAME).resolve("deleted");
        Path teamFilePath = this.getServer().getWorldPath(FOLDER_NAME).resolve(subfolderName).resolve(teamFileName);
        try {
            Files.createDirectories(deletedPath);
            Files.move(teamFilePath, deletedPath.resolve(teamFileName));
        } catch (IOException var8) {
            FTBTeams.LOGGER.error("can't move {} to {}: {}", teamFileName, deletedPath, var8.getMessage());
            try {
                Files.deleteIfExists(teamFilePath);
            } catch (IOException var7) {
                FTBTeams.LOGGER.error("can't delete directory {}: {}", teamFilePath, var7.getMessage());
            }
        }
    }
}