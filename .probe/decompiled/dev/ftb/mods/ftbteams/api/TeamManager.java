package dev.ftb.mods.ftbteams.api;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public interface TeamManager {

    MinecraftServer getServer();

    UUID getId();

    Collection<Team> getTeams();

    Optional<Team> getTeamForPlayerID(UUID var1);

    Optional<Team> getTeamForPlayer(ServerPlayer var1);

    Optional<Team> getPlayerTeamForPlayerID(UUID var1);

    Optional<Team> getTeamByName(String var1);

    Optional<Team> getTeamByID(UUID var1);

    boolean arePlayersInSameTeam(UUID var1, UUID var2);

    Map<UUID, ? extends Team> getKnownPlayerTeams();

    CompoundTag getExtraData();

    void markDirty();

    Team createPartyTeam(ServerPlayer var1, String var2, @Nullable String var3, @Nullable Color4I var4) throws CommandSyntaxException;
}