package journeymap.client.event.handlers;

import journeymap.client.JourneymapClient;
import journeymap.client.feature.FeatureManager;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;

public class WorldEventHandler {

    public void onUnload(LevelAccessor world) {
        try {
            ChunkMonitorHandler.getInstance().onWorldUnload(world);
            Player player = Minecraft.getInstance().player;
            if (player != null && player.m_9236_().dimensionType().equals(world.m_6042_())) {
                JourneymapClient.getInstance().stopMapping();
                FeatureManager.getInstance().reset();
            }
        } catch (Exception var3) {
            Journeymap.getLogger().error("Error handling WorldEvent.Unload", var3);
        }
    }
}