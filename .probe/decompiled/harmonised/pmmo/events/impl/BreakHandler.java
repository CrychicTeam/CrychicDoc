package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.features.veinmining.VeinMiningLogic;
import harmonised.pmmo.storage.ChunkDataHandler;
import harmonised.pmmo.storage.ChunkDataProvider;
import harmonised.pmmo.storage.IChunkData;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;

public class BreakHandler {

    public static void handle(BlockEvent.BreakEvent event) {
        Core core = Core.get(event.getPlayer().m_9236_());
        boolean serverSide = !event.getPlayer().m_9236_().isClientSide;
        if (!core.isActionPermitted(ReqType.BREAK, event.getPos(), event.getPlayer())) {
            event.setCanceled(true);
            Messenger.sendDenialMsg(ReqType.BREAK, event.getPlayer(), event.getState().m_60734_().getName());
        } else {
            CompoundTag eventHookOutput = new CompoundTag();
            if (serverSide) {
                eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.BLOCK_BREAK, event, new CompoundTag());
                if (eventHookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    return;
                }
            }
            CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.BLOCK_BREAK, event.getPlayer(), eventHookOutput));
            if (serverSide) {
                Map<String, Long> xpAward = calculateXpAward(core, event, perkOutput);
                List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) event.getPlayer());
                core.awardXP(partyMembersInRange, xpAward);
                LevelChunk chunk = (LevelChunk) event.getLevel().m_46865_(event.getPos());
                chunk.getCapability(ChunkDataProvider.CHUNK_CAP).ifPresent(cap -> {
                    cap.delPos(event.getPos());
                    if (event.getLevel().m_8055_(event.getPos()).m_204336_(Reference.CASCADING_BREAKABLES)) {
                        cap.setBreaker(event.getPos(), event.getPlayer().m_20148_());
                    }
                });
                chunk.m_8092_(true);
            }
            if (core.getMarkedPos(event.getPlayer().m_20148_()).equals(event.getPos())) {
                BlockState block = event.getLevel().m_8055_(event.getPos());
                Item tool = event.getPlayer().m_21205_().getItem();
                if (!Config.VEIN_BLACKLIST.get().contains(RegistryUtil.getId(tool).toString())) {
                    VeinMiningLogic.applyVein((ServerPlayer) event.getPlayer(), event.getPos());
                }
            }
        }
    }

    private static Map<String, Long> calculateXpAward(Core core, BlockEvent.BreakEvent event, CompoundTag dataIn) {
        Map<String, Long> outMap = core.getExperienceAwards(EventType.BLOCK_BREAK, event.getPos(), (Level) event.getLevel(), event.getPlayer(), dataIn);
        LevelChunk chunk = (LevelChunk) event.getLevel().m_46865_(event.getPos());
        IChunkData cap = chunk.getCapability(ChunkDataProvider.CHUNK_CAP).orElseGet(ChunkDataHandler::new);
        if (cap.playerMatchesPos(event.getPlayer(), event.getPos())) {
            BlockState cropState = event.getLevel().m_8055_(event.getPos());
            if (cropState.m_60734_() instanceof CropBlock && ((CropBlock) cropState.m_60734_()).isMaxAge(cropState)) {
                return outMap;
            } else {
                double xpModifier = Config.REUSE_PENALTY.get();
                if (xpModifier == 0.0) {
                    return new HashMap();
                } else {
                    Map<String, Long> modifiedOutMap = new HashMap();
                    outMap.forEach((k, v) -> modifiedOutMap.put(k, (long) ((double) v.longValue() * xpModifier)));
                    return modifiedOutMap;
                }
            }
        } else {
            return outMap;
        }
    }
}