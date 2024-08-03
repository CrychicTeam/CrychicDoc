package sereneseasons.handler.season;

import java.util.HashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import sereneseasons.season.SeasonSavedData;

@EventBusSubscriber
public class TimeSkipHandler {

    public static final HashMap<ResourceKey<Level>, Long> lastDayTimes = new HashMap();

    @SubscribeEvent
    public static void onWorldLoaded(LevelEvent.Load event) {
        lastDayTimes.clear();
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            ServerLevel world = (ServerLevel) event.level;
            long dayTime = world.m_6106_().getDayTime();
            if (!lastDayTimes.containsKey(world.m_46472_())) {
                lastDayTimes.put(world.m_46472_(), dayTime);
            }
            long lastDayTime = (Long) lastDayTimes.get(world.m_46472_());
            long difference = dayTime - lastDayTime;
            if (difference < 0L) {
                difference += 24000L;
            }
            if (difference > 1L) {
                SeasonSavedData seasonData = SeasonHandler.getSeasonSavedData(world);
                seasonData.seasonCycleTicks = (int) ((long) seasonData.seasonCycleTicks + difference);
                seasonData.m_77762_();
                SeasonHandler.sendSeasonUpdate(world);
            }
            lastDayTimes.put(world.m_46472_(), dayTime);
        }
    }
}