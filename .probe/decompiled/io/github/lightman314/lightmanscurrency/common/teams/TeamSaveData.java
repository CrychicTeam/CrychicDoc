package io.github.lightman314.lightmanscurrency.common.teams;

import io.github.lightman314.lightmanscurrency.api.misc.player.PlayerReference;
import io.github.lightman314.lightmanscurrency.client.data.ClientTeamData;
import io.github.lightman314.lightmanscurrency.network.LightmansCurrencyPacketHandler;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketClearClientTeams;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketRemoveClientTeam;
import io.github.lightman314.lightmanscurrency.network.message.data.team.SPacketUpdateClientTeam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

@EventBusSubscriber(modid = "lightmanscurrency")
public class TeamSaveData extends SavedData {

    private long nextID = 0L;

    private final Map<Long, Team> teams = new HashMap();

    private long getNextID() {
        long id = this.nextID++;
        this.m_77762_();
        return id;
    }

    private TeamSaveData() {
    }

    private TeamSaveData(CompoundTag compound) {
        this.nextID = compound.getLong("NextID");
        ListTag teamList = compound.getList("Teams", 10);
        for (int i = 0; i < teamList.size(); i++) {
            Team team = Team.load(teamList.getCompound(i));
            if (team != null) {
                this.teams.put(team.getID(), team);
            }
        }
    }

    @Nonnull
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putLong("NextID", this.nextID);
        ListTag teamList = new ListTag();
        this.teams.forEach((teamID, team) -> {
            if (team != null) {
                teamList.add(team.save());
            }
        });
        compound.put("Teams", teamList);
        return compound;
    }

    private static TeamSaveData get() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            ServerLevel level = server.getLevel(Level.OVERWORLD);
            if (level != null) {
                return level.getDataStorage().computeIfAbsent(TeamSaveData::new, TeamSaveData::new, "lightmanscurrency_team_data");
            }
        }
        return null;
    }

    public static List<Team> GetAllTeams(boolean isClient) {
        if (isClient) {
            return ClientTeamData.GetAllTeams();
        } else {
            TeamSaveData tsd = get();
            return tsd != null ? new ArrayList(tsd.teams.values()) : new ArrayList();
        }
    }

    public static Team GetTeam(boolean isClient, long teamID) {
        if (isClient) {
            return ClientTeamData.GetTeam(teamID);
        } else {
            TeamSaveData tsd = get();
            return tsd != null && tsd.teams.containsKey(teamID) ? (Team) tsd.teams.get(teamID) : null;
        }
    }

    public static void MarkTeamDirty(long teamID) {
        TeamSaveData tsd = get();
        if (tsd != null) {
            tsd.m_77762_();
            Team team = GetTeam(false, teamID);
            if (team != null) {
                CompoundTag compound = team.save();
                new SPacketUpdateClientTeam(compound).sendToAll();
            }
        }
    }

    public static Team RegisterTeam(Player owner, String teamName) {
        TeamSaveData tsd = get();
        if (tsd != null) {
            long teamID = tsd.getNextID();
            Team newTeam = Team.of(teamID, PlayerReference.of(owner), teamName);
            tsd.teams.put(teamID, newTeam);
            MarkTeamDirty(teamID);
            return newTeam;
        } else {
            return null;
        }
    }

    public static void RemoveTeam(long teamID) {
        TeamSaveData tsd = get();
        if (tsd != null && tsd.teams.containsKey(teamID)) {
            tsd.teams.remove(teamID);
            tsd.m_77762_();
            new SPacketRemoveClientTeam(teamID).sendToAll();
        }
    }

    @SubscribeEvent
    public static void OnPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        PacketDistributor.PacketTarget target = LightmansCurrencyPacketHandler.getTarget(event.getEntity());
        TeamSaveData tsd = get();
        SPacketClearClientTeams.INSTANCE.sendToTarget(target);
        tsd.teams.forEach((id, team) -> new SPacketUpdateClientTeam(team.save()).sendToTarget(target));
    }
}