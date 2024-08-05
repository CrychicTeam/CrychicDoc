package io.github.lightman314.lightmanscurrency.client.data;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class ClientTeamData {

    private static final Map<Long, Team> loadedTeams = new HashMap();

    public static List<Team> GetAllTeams() {
        return ImmutableList.copyOf(loadedTeams.values());
    }

    @Nullable
    public static Team GetTeam(long teamID) {
        return (Team) loadedTeams.get(teamID);
    }

    public static void ClearTeams() {
        loadedTeams.clear();
    }

    public static void UpdateTeam(CompoundTag compound) {
        Team updatedTeam = Team.load(compound);
        loadedTeams.put(updatedTeam.getID(), updatedTeam.flagAsClient());
    }

    public static void RemoveTeam(long teamID) {
        loadedTeams.remove(teamID);
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClearTeams();
    }
}