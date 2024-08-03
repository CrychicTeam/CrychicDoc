package journeymap.client.event.forge;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.api.event.forge.PopupMenuEvent;
import journeymap.client.data.DataCache;
import journeymap.client.event.handlers.PopupMenuEventHandler;
import journeymap.client.io.FileHandler;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.MapType;
import journeymap.client.model.NBTChunkMD;
import journeymap.client.model.RegionCoord;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.time.StopWatch;

public class ForgePopupCustomEvents implements ForgeEventHandlerManager.EventHandler {

    private final PopupMenuEventHandler handler = new PopupMenuEventHandler();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadarEntityUpdateEvent(EntityRadarUpdateEvent event) {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onFullscreenPopupMenu(PopupMenuEvent.FullscreenPopupMenuEvent event) {
        if (!event.isCanceled()) {
            this.handler.onFullscreenPopupMenu(event.getPopupMenu(), (Fullscreen) event.getFullscreen());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWaypointPopupMenu(PopupMenuEvent.WaypointPopupMenuEvent event) {
        if (!event.isCanceled()) {
            this.handler.onWaypointPopupMenu(event.getPopupMenu(), event.getWaypoint().getId(), (Fullscreen) event.getFullscreen());
        }
    }

    private ModPopupMenu.Action doRender(Fullscreen fullscreen) {
        return blockPos -> this.renderChunk(blockPos, fullscreen);
    }

    private void saveChunk(BlockPos pos) {
        try {
            Minecraft mc = Minecraft.getInstance();
            ChunkStorage loader = new ChunkStorage(new File(FileHandler.getWorldSaveDir(mc), "region").toPath(), DataFixers.getDataFixer(), true);
            CompletableFuture<Optional<CompoundTag>> chunkFuture = loader.read(new ChunkPos(pos));
            File jmWorldDir = FileHandler.getJMWorldDir(mc);
            chunkFuture.whenCompleteAsync((chunkOptional, throwable) -> {
                if (chunkOptional.isPresent()) {
                    try {
                        NbtIo.writeCompressed((CompoundTag) chunkOptional.get(), new File(jmWorldDir + "/" + new ChunkPos(pos) + ".chunk.dat"));
                    } catch (IOException var5x) {
                        throw new RuntimeException(var5x);
                    }
                }
            });
        } catch (Exception var6) {
        }
    }

    private void renderChunk(BlockPos blockPos, Fullscreen fullscreen) {
        MapType mapType = fullscreen.getMapType();
        CompoundTag chunk = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(blockPos, mapType).getChunkNbt(new ChunkPos(blockPos));
        ChunkPos chunkPos = new ChunkPos(chunk.getLong("pos"));
        File jmWorldDir = FileHandler.getJMWorldDir(Minecraft.getInstance());
        RegionCoord rCoord = RegionCoord.fromChunkPos(jmWorldDir, mapType, chunkPos.x, chunkPos.z);
        RegionDataStorageHandler.Key key = new RegionDataStorageHandler.Key(rCoord, mapType);
        RegionData regionData = RegionDataStorageHandler.getInstance().getRegionData(key);
        try {
            NbtIo.writeCompressed(chunk, new File(jmWorldDir + "/" + mapType.name() + ".chunk.dat"));
        } catch (IOException var12) {
            var12.printStackTrace();
        }
        ChunkMD chunkMd = new NBTChunkMD(new LevelChunk(Minecraft.getInstance().level, chunkPos), chunkPos, chunk, mapType);
        ChunkMD md = DataCache.INSTANCE.getChunkMD(chunkPos.toLong());
        JourneymapClient.getInstance().getChunkRenderController().renderChunk(rCoord, fullscreen.getMapType(), chunkMd, regionData);
    }

    private void renderRegion(BlockPos blockPos, Fullscreen fullscreen) {
        try {
            JourneymapClient.getInstance().queueOneOff(() -> {
                MapType mapType = fullscreen.getMapType();
                ChunkPos pos = new ChunkPos(blockPos);
                File jmWorldDir = FileHandler.getJMWorldDir(Minecraft.getInstance());
                RegionCoord rCoord = RegionCoord.fromChunkPos(jmWorldDir, mapType, pos.x, pos.z);
                RegionDataStorageHandler.Key key = new RegionDataStorageHandler.Key(rCoord, mapType);
                RegionData regionData = RegionDataStorageHandler.getInstance().getRegionData(key);
                StopWatch stopwatch = new StopWatch();
                stopwatch.start();
                for (ChunkPos chunkPos : rCoord.getChunkCoordsInRegion()) {
                    CompoundTag chunk = regionData.getChunkNbt(chunkPos);
                    if (chunk.getAllKeys().size() > 1) {
                        ChunkMD chunkMd = new NBTChunkMD(new LevelChunk(Minecraft.getInstance().level, chunkPos), chunkPos, chunk, mapType);
                        ChunkMD md = DataCache.INSTANCE.getChunkMD(chunkPos.toLong());
                        JourneymapClient.getInstance().getChunkRenderController().renderChunk(rCoord, fullscreen.getMapType(), chunkMd, regionData);
                    }
                }
                stopwatch.stop();
                System.out.println(stopwatch.getTime(TimeUnit.MILLISECONDS));
            });
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }
}