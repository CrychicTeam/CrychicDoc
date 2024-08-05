package dev.ftb.mods.ftbxmodcompat.ftbchunks.ftbessentials;

import dev.architectury.event.EventResult;
import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftbessentials.FTBEssentialsEvents;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class EssentialsCompat {

    public static void init() {
        FTBEssentialsEvents.RTP_EVENT.register(EssentialsCompat::onRTP);
    }

    private static EventResult onRTP(ServerLevel serverLevel, ServerPlayer player, BlockPos pos, int attempt) {
        ClaimedChunk cc = FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(serverLevel, pos));
        return cc != null && !cc.getTeamData().isTeamMember(player.m_20148_()) ? EventResult.interruptFalse() : EventResult.pass();
    }
}