package io.github.lightman314.lightmanscurrency.api.teams;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import io.github.lightman314.lightmanscurrency.common.teams.TeamSaveData;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

public final class TeamAPI {

    private TeamAPI() {
    }

    @Nullable
    public static ITeam getTeam(boolean isClient, long teamID) {
        return TeamSaveData.GetTeam(isClient, teamID);
    }

    @Nonnull
    public static List<? extends ITeam> getAllTeams(boolean isClient) {
        return TeamSaveData.GetAllTeams(isClient);
    }

    @Nonnull
    public static List<ITeam> getAllTeamsForPlayer(@Nonnull Player player) {
        List<ITeam> result = new ArrayList();
        for (ITeam team : getAllTeams(player.m_9236_().isClientSide)) {
            if (team.isMember(player)) {
                result.add(team);
            }
        }
        result.sort(sorterFor(player));
        return ImmutableList.copyOf(result);
    }

    @Nullable
    public static ITeam createTeam(@Nonnull Player owner, @Nonnull String name) {
        return owner.m_9236_().isClientSide ? null : TeamSaveData.RegisterTeam(owner, name);
    }

    @Nonnull
    public static Comparator<ITeam> sorterFor(@Nonnull Player player) {
        return Team.sorterFor(player);
    }
}