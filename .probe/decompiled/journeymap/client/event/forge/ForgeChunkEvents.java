package journeymap.client.event.forge;

import journeymap.client.event.handlers.ChunkMonitorHandler;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeChunkEvents implements ForgeEventHandlerManager.EventHandler {

    @SubscribeEvent
    public void onBlockUpdate(BlockEvent event) {
        ChunkMonitorHandler.getInstance().onBlockUpdate(event.getLevel(), event.getPos());
    }

    @SubscribeEvent
    public void onChunkUpdate(ChunkWatchEvent event) {
        ChunkMonitorHandler.getInstance().onChunkUpdate(event.getLevel(), event.getPos());
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        ChunkMonitorHandler.getInstance().onChunkLoad(event.getLevel(), event.getChunk());
    }
}