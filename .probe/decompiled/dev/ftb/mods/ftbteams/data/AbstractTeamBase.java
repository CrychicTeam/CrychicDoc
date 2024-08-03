package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.api.TeamMessage;
import dev.ftb.mods.ftbteams.api.TeamRank;
import dev.ftb.mods.ftbteams.api.property.TeamProperties;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTeamBase implements Team {

    protected final UUID id;

    protected final TeamPropertyCollectionImpl properties;

    protected final Map<UUID, TeamRank> ranks;

    protected CompoundTag extraData;

    protected final List<TeamMessage> messageHistory;

    private boolean valid;

    public AbstractTeamBase(UUID id) {
        this.id = id;
        this.ranks = new HashMap();
        this.properties = new TeamPropertyCollectionImpl();
        this.extraData = new CompoundTag();
        this.messageHistory = new LinkedList();
        this.valid = true;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public UUID getTeamId() {
        return this.id;
    }

    @Override
    public TeamPropertyCollection getProperties() {
        return this.properties;
    }

    public abstract TeamType getType();

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else {
            return o instanceof AbstractTeam ? this.id.equals(((AbstractTeam) o).getId()) : false;
        }
    }

    public String toString() {
        return this.getShortName();
    }

    @Override
    public CompoundTag getExtraData() {
        return this.extraData;
    }

    @Override
    public <T> T getProperty(TeamProperty<T> property) {
        return this.properties.get(property);
    }

    @Override
    public <T> void setProperty(TeamProperty<T> property, T value) {
        this.properties.set(property, value);
        this.markDirty();
    }

    public String getDisplayName() {
        return this.getProperty(TeamProperties.DISPLAY_NAME);
    }

    public String getDescription() {
        return this.getProperty(TeamProperties.DESCRIPTION);
    }

    public int getColor() {
        return this.getProperty(TeamProperties.COLOR).rgb();
    }

    public boolean isFreeToJoin() {
        return this.getProperty(TeamProperties.FREE_TO_JOIN);
    }

    public int getMaxMessageHistorySize() {
        return this.getProperty(TeamProperties.MAX_MSG_HISTORY_SIZE);
    }

    @Override
    public String getShortName() {
        String s = this.getDisplayName().replaceAll("\\W", "_");
        return (s.length() > 50 ? s.substring(0, 50) : s) + "#" + this.getId().toString().substring(0, 8);
    }

    @Override
    public Component getName() {
        MutableComponent text = Component.literal(this.getDisplayName());
        if (this.getType().isPlayer()) {
            text.withStyle(ChatFormatting.GRAY);
        } else if (this.getType().isServer()) {
            text.withStyle(ChatFormatting.RED);
        } else {
            text.withStyle(ChatFormatting.AQUA);
        }
        text.setStyle(text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftbteams info " + this.getShortName())));
        return text;
    }

    @Override
    public Component getColoredName() {
        MutableComponent text = Component.literal(this.getDisplayName());
        text.withStyle(this.getProperty(TeamProperties.COLOR).toStyle().withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftbteams info " + this.getShortName())));
        return text;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public TeamRank getRankForPlayer(UUID playerId) {
        TeamRank rank = (TeamRank) this.ranks.get(playerId);
        if (rank != null) {
            return rank;
        } else {
            return this.isFreeToJoin() ? TeamRank.INVITED : TeamRank.NONE;
        }
    }

    public boolean isMember(UUID uuid) {
        return this.getRankForPlayer(uuid).isMemberOrBetter();
    }

    @Override
    public Map<UUID, TeamRank> getPlayersByRank(TeamRank minRank) {
        if (minRank == TeamRank.NONE) {
            return Collections.unmodifiableMap(this.ranks);
        } else {
            Map<UUID, TeamRank> map = new HashMap();
            this.ranks.forEach((id, rank) -> {
                if (rank.isAtLeast(minRank)) {
                    map.put(id, rank);
                }
            });
            return Collections.unmodifiableMap(map);
        }
    }

    @Override
    public String getTypeTranslationKey() {
        return "ftbteams.team_type." + this.getType().getSerializedName();
    }

    @Override
    public Set<UUID> getMembers() {
        return this.getPlayersByRank(TeamRank.MEMBER).keySet();
    }

    @Override
    public Team createParty(String description, @Nullable Color4I color) {
        if (this instanceof PlayerTeam playerTeam) {
            UUID playerId = playerTeam.getId();
            FTBTeamsAPI.api().getManager().getTeamForPlayerID(playerId).ifPresent(team -> {
                if (team instanceof PartyTeam) {
                    throw new IllegalStateException("player " + playerId + " is already in a party team: " + team.getTeamId());
                }
            });
            if (color == null) {
                color = FTBTUtils.randomColor();
            }
            MinecraftServer server = TeamManagerImpl.INSTANCE.getServer();
            ServerPlayer player = server.getPlayerList().getPlayer(playerId);
            return playerTeam.createParty(playerId, player, FTBTUtils.getDefaultPartyName(server, playerId, player), description, color.rgb(), Set.of());
        } else {
            throw new IllegalStateException("team is not a player team: " + this.getTeamId());
        }
    }

    public boolean isAllyOrBetter(UUID profile) {
        return this.getRankForPlayer(profile).isAllyOrBetter();
    }

    public boolean isOfficerOrBetter(UUID profile) {
        return this.getRankForPlayer(profile).isOfficerOrBetter();
    }

    public boolean isInvited(UUID profile) {
        return this.getRankForPlayer(profile).isInvitedOrBetter();
    }

    public void addMessage(TeamMessage message) {
        this.addMessages(List.of(message));
    }

    public void addMessages(Collection<TeamMessage> messages) {
        this.messageHistory.addAll(messages);
        while (this.messageHistory.size() > this.getMaxMessageHistorySize()) {
            this.messageHistory.remove(0);
        }
        this.markDirty();
    }

    @Override
    public List<TeamMessage> getMessageHistory() {
        return Collections.unmodifiableList(this.messageHistory);
    }

    public void addMember(UUID id, TeamRank rank) {
        this.ranks.put(id, rank);
    }

    public void removeMember(UUID id) {
        this.ranks.remove(id);
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }

    public void invalidateTeam() {
        this.valid = false;
    }
}