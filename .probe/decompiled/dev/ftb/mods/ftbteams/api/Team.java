package dev.ftb.mods.ftbteams.api;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface Team {

    UUID getId();

    UUID getTeamId();

    String getShortName();

    UUID getOwner();

    TeamPropertyCollection getProperties();

    <T> T getProperty(TeamProperty<T> var1);

    <T> void setProperty(TeamProperty<T> var1, T var2);

    default boolean isPlayerTeam() {
        return false;
    }

    default boolean isPartyTeam() {
        return false;
    }

    default boolean isServerTeam() {
        return false;
    }

    List<TeamMessage> getMessageHistory();

    TeamRank getRankForPlayer(UUID var1);

    Component getName();

    void sendMessage(UUID var1, String var2);

    List<Component> getTeamInfo();

    Map<UUID, TeamRank> getPlayersByRank(TeamRank var1);

    Set<UUID> getMembers();

    String getTypeTranslationKey();

    default boolean isClientTeam() {
        return false;
    }

    CompoundTag getExtraData();

    void markDirty();

    Collection<ServerPlayer> getOnlineMembers();

    Component getColoredName();

    boolean isValid();

    Team createParty(String var1, @Nullable Color4I var2);
}