package net.minecraft.server.level.progress;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.slf4j.Logger;

public class LoggerChunkProgressListener implements ChunkProgressListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final int maxCount;

    private int count;

    private long startTime;

    private long nextTickTime = Long.MAX_VALUE;

    public LoggerChunkProgressListener(int int0) {
        int $$1 = int0 * 2 + 1;
        this.maxCount = $$1 * $$1;
    }

    @Override
    public void updateSpawnPos(ChunkPos chunkPos0) {
        this.nextTickTime = Util.getMillis();
        this.startTime = this.nextTickTime;
    }

    @Override
    public void onStatusChange(ChunkPos chunkPos0, @Nullable ChunkStatus chunkStatus1) {
        if (chunkStatus1 == ChunkStatus.FULL) {
            this.count++;
        }
        int $$2 = this.getProgress();
        if (Util.getMillis() > this.nextTickTime) {
            this.nextTickTime += 500L;
            LOGGER.info(Component.translatable("menu.preparingSpawn", Mth.clamp($$2, 0, 100)).getString());
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        LOGGER.info("Time elapsed: {} ms", Util.getMillis() - this.startTime);
        this.nextTickTime = Long.MAX_VALUE;
    }

    public int getProgress() {
        return Mth.floor((float) this.count * 100.0F / (float) this.maxCount);
    }
}