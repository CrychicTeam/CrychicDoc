package dev.ftb.mods.ftbxmodcompat.ftbchunks.ftbranks;

import com.mojang.authlib.GameProfile;
import dev.ftb.mods.ftbchunks.api.ChunkTeamData;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbranks.api.RankManager;
import dev.ftb.mods.ftbranks.api.event.ConditionChangedEvent;
import dev.ftb.mods.ftbranks.api.event.PermissionNodeChangedEvent;
import dev.ftb.mods.ftbranks.api.event.PlayerAddedToRankEvent;
import dev.ftb.mods.ftbranks.api.event.PlayerRemovedFromRankEvent;
import dev.ftb.mods.ftbranks.api.event.RankEvent;
import dev.ftb.mods.ftbranks.api.event.RanksReloadedEvent;
import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import net.minecraft.server.level.ServerPlayer;

public class FTBRanksEventHandler {

    public static void registerEvents() {
        RankEvent.ADD_PLAYER.register(FTBRanksEventHandler::playerAdded);
        RankEvent.REMOVE_PLAYER.register(FTBRanksEventHandler::playerRemoved);
        RankEvent.PERMISSION_CHANGED.register(FTBRanksEventHandler::permissionSet);
        RankEvent.RELOADED.register(FTBRanksEventHandler::ranksReloaded);
        RankEvent.CONDITION_CHANGED.register(FTBRanksEventHandler::conditionChanged);
        FTBXModCompat.LOGGER.info("[FTB Chunks] FTB Ranks detected, listening for ranks events");
    }

    private static void playerAdded(PlayerAddedToRankEvent event) {
        updateForPlayer(event.getManager(), event.getPlayer());
    }

    private static void playerRemoved(PlayerRemovedFromRankEvent event) {
        updateForPlayer(event.getManager(), event.getPlayer());
    }

    private static void permissionSet(PermissionNodeChangedEvent event) {
        String node = event.getNode();
        if (node.equals("ftbchunks.max_claimed") || node.equals("ftbchunks.max_force_loaded") || node.equals("ftbchunks.chunk_load_offline")) {
            updateAll(event.getManager());
        }
    }

    private static void ranksReloaded(RanksReloadedEvent event) {
        updateAll(event.getManager());
    }

    private static void conditionChanged(ConditionChangedEvent event) {
        updateAll(event.getManager());
    }

    private static void updateAll(RankManager manager) {
        if (FTBChunksAPI.api().isManagerLoaded()) {
            manager.getServer().getPlayerList().getPlayers().forEach(player -> {
                ChunkTeamData teamData = FTBChunksAPI.api().getManager().getOrCreateData(player);
                teamData.checkMemberForceLoading(player.m_20148_());
            });
        }
    }

    private static void updateForPlayer(RankManager manager, GameProfile profile) {
        FTBTeamsAPI.api().getManager().getTeamForPlayerID(profile.getId()).ifPresent(team -> {
            ChunkTeamData teamData = FTBChunksAPI.api().getManager().getOrCreateData(team);
            ServerPlayer player = manager.getServer().getPlayerList().getPlayer(profile.getId());
            if (player != null) {
                teamData.checkMemberForceLoading(player.m_20148_());
            }
        });
    }
}