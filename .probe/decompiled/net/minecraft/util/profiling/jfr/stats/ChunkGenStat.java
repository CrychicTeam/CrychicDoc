package net.minecraft.util.profiling.jfr.stats;

import java.time.Duration;
import jdk.jfr.consumer.RecordedEvent;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;

public record ChunkGenStat(Duration f_185592_, ChunkPos f_185593_, ColumnPos f_185594_, ChunkStatus f_185595_, String f_185596_) implements TimedStat {

    private final Duration duration;

    private final ChunkPos chunkPos;

    private final ColumnPos worldPos;

    private final ChunkStatus status;

    private final String level;

    public ChunkGenStat(Duration f_185592_, ChunkPos f_185593_, ColumnPos f_185594_, ChunkStatus f_185595_, String f_185596_) {
        this.duration = f_185592_;
        this.chunkPos = f_185593_;
        this.worldPos = f_185594_;
        this.status = f_185595_;
        this.level = f_185596_;
    }

    public static ChunkGenStat from(RecordedEvent p_185605_) {
        return new ChunkGenStat(p_185605_.getDuration(), new ChunkPos(p_185605_.getInt("chunkPosX"), p_185605_.getInt("chunkPosX")), new ColumnPos(p_185605_.getInt("worldPosX"), p_185605_.getInt("worldPosZ")), ChunkStatus.byName(p_185605_.getString("status")), p_185605_.getString("level"));
    }
}