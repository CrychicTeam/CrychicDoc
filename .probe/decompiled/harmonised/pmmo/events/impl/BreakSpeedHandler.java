package harmonised.pmmo.events.impl;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BreakSpeedHandler {

    private static Map<UUID, BreakSpeedHandler.DetailsCache> resultCache = new HashMap();

    public static void handle(PlayerEvent.BreakSpeed event) {
        Core core = Core.get(event.getEntity().m_9236_());
        if (resultCache.containsKey(event.getEntity().m_20148_()) && usingCache(event)) {
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.EVENT, "Cache Used. Supplied: {}", event.getNewSpeed());
        } else if (!core.isActionPermitted(ReqType.TOOL, event.getEntity().m_21205_(), event.getEntity())) {
            event.setCanceled(true);
            resultCache.put(event.getEntity().m_20148_(), new BreakSpeedHandler.DetailsCache(event.getEntity().m_21205_(), (BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0)), event.getState(), true, event.getNewSpeed()));
        } else if (!core.isActionPermitted(ReqType.BREAK, (BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0)), event.getEntity())) {
            event.setCanceled(true);
            resultCache.put(event.getEntity().m_20148_(), new BreakSpeedHandler.DetailsCache(event.getEntity().m_21205_(), (BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0)), event.getState(), true, event.getNewSpeed()));
        } else {
            CompoundTag eventHookOutput = new CompoundTag();
            if (!event.getEntity().m_9236_().isClientSide) {
                eventHookOutput = core.getEventTriggerRegistry().executeEventListeners(EventType.BREAK_SPEED, event, new CompoundTag());
                if (eventHookOutput.getBoolean("is_cancelled")) {
                    event.setCanceled(true);
                    resultCache.put(event.getEntity().m_20148_(), new BreakSpeedHandler.DetailsCache(event.getEntity().m_21205_(), (BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0)), event.getState(), true, event.getNewSpeed()));
                    return;
                }
            }
            eventHookOutput.putFloat("speedIn", event.getNewSpeed());
            eventHookOutput.putLong("block_pos", ((BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0))).asLong());
            CompoundTag perkDataOut = core.getPerkRegistry().executePerk(EventType.BREAK_SPEED, event.getEntity(), eventHookOutput);
            if (perkDataOut.contains("speed")) {
                float newSpeed = event.getNewSpeed() * (1.0F + Math.max(0.0F, perkDataOut.getFloat("speed")));
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.EVENT, "BreakSpeed Original{}, New:{}", event.getOriginalSpeed(), newSpeed);
                event.setNewSpeed(newSpeed);
                resultCache.put(event.getEntity().m_20148_(), new BreakSpeedHandler.DetailsCache(event.getEntity().m_21205_(), (BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0)), event.getState(), event.getEntity().m_20096_(), false, newSpeed));
            }
        }
    }

    private static boolean usingCache(PlayerEvent.BreakSpeed event) {
        BreakSpeedHandler.DetailsCache cachedData = (BreakSpeedHandler.DetailsCache) resultCache.get(event.getEntity().m_20148_());
        if (event.getEntity().m_20096_() == cachedData.isPlayerStanding() && ((BlockPos) event.getPosition().orElse(new BlockPos(0, 0, 0))).equals(cachedData.pos) && event.getState().equals(cachedData.state) && event.getEntity().m_21205_().equals(cachedData.item, false)) {
            if (cachedData.cancelled) {
                event.setCanceled(true);
            } else {
                event.setNewSpeed(cachedData.newSpeed);
            }
            return true;
        } else {
            return false;
        }
    }

    private static record DetailsCache(ItemStack item, BlockPos pos, BlockState state, boolean isPlayerStanding, boolean cancelled, float newSpeed) {

        public DetailsCache(ItemStack item, BlockPos pos, BlockState state, boolean cancelled, float newSpeed) {
            this(item, pos, state, true, cancelled, newSpeed);
        }
    }
}