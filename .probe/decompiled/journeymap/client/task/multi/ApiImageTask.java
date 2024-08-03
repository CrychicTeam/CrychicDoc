package journeymap.client.task.multi;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.util.function.Consumer;
import journeymap.client.api.display.Context;
import journeymap.client.io.FileHandler;
import journeymap.client.model.MapType;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ApiImageTask implements Runnable {

    final String modId;

    final ResourceKey<Level> dimension;

    final MapType mapType;

    final ChunkPos startChunk;

    final ChunkPos endChunk;

    final Integer vSlice;

    final int zoom;

    final boolean showGrid;

    final File jmWorldDir;

    final Consumer<NativeImage> callback;

    public ApiImageTask(String modId, ResourceKey<Level> dimension, Context.MapType apiMapType, ChunkPos startChunk, ChunkPos endChunk, Integer vSlice, int zoom, boolean showGrid, Consumer<NativeImage> callback) {
        this.modId = modId;
        this.dimension = dimension;
        this.startChunk = startChunk;
        this.endChunk = endChunk;
        this.zoom = zoom;
        this.showGrid = showGrid;
        this.callback = callback;
        this.vSlice = vSlice;
        this.mapType = MapType.fromApiContextMapType(apiMapType, vSlice, dimension);
        this.jmWorldDir = FileHandler.getJMWorldDir(Minecraft.getInstance());
    }

    public void run() {
        NativeImage image = null;
        try {
            int finalImage = (int) Math.pow(2.0, (double) this.zoom);
        } catch (Throwable var3) {
            Journeymap.getLogger().error("Error in ApiImageTask: {}" + var3, LogFormatter.toString(var3));
        }
        Minecraft.getInstance().m_18707_(() -> this.callback.accept(image));
    }
}