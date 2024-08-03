package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.storage.ChunkDataProvider;
import harmonised.pmmo.util.Messenger;
import harmonised.pmmo.util.TagUtils;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;

public class PlaceHandler {

    public static void handle(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Core core = Core.get(event.getEntity().level());
            if (!core.isActionPermitted(ReqType.PLACE, event.getPos(), player)) {
                event.setCanceled(true);
                Messenger.sendDenialMsg(ReqType.PLACE, player, event.getPlacedBlock().m_60734_().getName());
            } else {
                boolean serverSide = !player.m_9236_().isClientSide;
                CompoundTag eventHookOutput = new CompoundTag();
                if (serverSide) {
                    eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.BLOCK_PLACE, event, new CompoundTag());
                    if (eventHookOutput.getBoolean("is_cancelled")) {
                        event.setCanceled(true);
                        return;
                    }
                }
                CompoundTag perkOutput = TagUtils.mergeTags(eventHookOutput, core.getPerkRegistry().executePerk(EventType.BLOCK_PLACE, player, eventHookOutput));
                if (serverSide) {
                    Map<String, Long> xpAward = core.getExperienceAwards(EventType.BLOCK_PLACE, event.getPos(), (Level) event.getLevel(), (Player) event.getEntity(), perkOutput);
                    List<ServerPlayer> partyMembersInRange = PartyUtils.getPartyMembersInRange((ServerPlayer) player);
                    core.awardXP(partyMembersInRange, xpAward);
                    LevelChunk chunk = (LevelChunk) event.getLevel().m_46865_(event.getPos());
                    chunk.getCapability(ChunkDataProvider.CHUNK_CAP).ifPresent(cap -> cap.addPos(event.getPos(), player.m_20148_()));
                    chunk.m_8092_(true);
                }
            }
        }
    }
}