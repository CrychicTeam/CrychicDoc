package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.storage.ChunkDataHandler;
import harmonised.pmmo.storage.ChunkDataProvider;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

public class CropGrowHandler {

    public static void handle(BlockEvent.CropGrowEvent.Post event) {
        Level level = (Level) event.getLevel();
        if (!level.isClientSide) {
            Core core = Core.get(level);
            BlockPos sourcePos = getParentPos(level, event.getState(), event.getPos());
            ChunkDataHandler cap = level.getChunkAt(sourcePos).getCapability(ChunkDataProvider.CHUNK_CAP).orElseGet(ChunkDataHandler::new);
            UUID placerID = cap.checkPos(sourcePos);
            ServerPlayer player = event.getLevel().getServer().getPlayerList().getPlayer(placerID);
            if (player == null) {
                return;
            }
            cap.addPos(event.getPos(), placerID);
            CompoundTag hookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.GROW, event, new CompoundTag());
            hookOutput = TagUtils.mergeTags(hookOutput, core.getPerkRegistry().executePerk(EventType.GROW, player, new CompoundTag()));
            Map<String, Long> xpAward = core.getExperienceAwards(EventType.GROW, event.getPos(), level, player, hookOutput);
            List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange(player);
            core.awardXP(partyMembersInRange, xpAward);
        }
    }

    private static BlockPos getParentPos(Level level, BlockState state, BlockPos posIn) {
        if (!state.m_204336_(Reference.CASCADING_BREAKABLES)) {
            return posIn;
        } else {
            return level.getBlockState(posIn.above()).m_60795_() && level.getBlockState(posIn.below()).m_204336_(Reference.CASCADING_BREAKABLES) ? posIn.below() : (level.getBlockState(posIn.below()).m_60795_() && level.getBlockState(posIn.above()).m_204336_(Reference.CASCADING_BREAKABLES) ? posIn.above() : posIn);
        }
    }
}