package net.minecraftforge.event.level;

import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class ChunkTicketLevelUpdatedEvent extends Event {

    private final ServerLevel level;

    private final long chunkPos;

    private final int oldTicketLevel;

    private final int newTicketLevel;

    @Nullable
    private final ChunkHolder chunkHolder;

    public ChunkTicketLevelUpdatedEvent(ServerLevel level, long chunkPos, int oldTicketLevel, int newTicketLevel, @Nullable ChunkHolder chunkHolder) {
        this.level = level;
        this.chunkPos = chunkPos;
        this.oldTicketLevel = oldTicketLevel;
        this.newTicketLevel = newTicketLevel;
        this.chunkHolder = chunkHolder;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    public long getChunkPos() {
        return this.chunkPos;
    }

    public int getOldTicketLevel() {
        return this.oldTicketLevel;
    }

    public int getNewTicketLevel() {
        return this.newTicketLevel;
    }

    @Nullable
    public ChunkHolder getChunkHolder() {
        return this.chunkHolder;
    }
}