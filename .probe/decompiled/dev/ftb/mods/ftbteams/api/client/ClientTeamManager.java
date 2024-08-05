package dev.ftb.mods.ftbteams.api.client;

import dev.ftb.mods.ftbteams.api.Team;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface ClientTeamManager {

    UUID getManagerId();

    boolean isValid();

    Collection<KnownClientPlayer> knownClientPlayers();

    Collection<Team> getTeams();

    Optional<Team> getTeamByID(UUID var1);

    Team selfTeam();

    KnownClientPlayer self();

    Optional<KnownClientPlayer> getKnownPlayer(UUID var1);

    Component formatName(@Nullable UUID var1);
}