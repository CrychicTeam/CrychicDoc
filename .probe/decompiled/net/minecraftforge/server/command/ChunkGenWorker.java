package net.minecraftforge.server.command;

import java.util.ArrayDeque;
import java.util.Queue;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraftforge.common.WorldWorkerManager;

public class ChunkGenWorker implements WorldWorkerManager.IWorker {

    private final CommandSourceStack listener;

    protected final BlockPos start;

    protected final int total;

    private final ServerLevel dim;

    private final Queue<BlockPos> queue;

    private final int notificationFrequency;

    private int lastNotification = 0;

    private long lastNotifcationTime = 0L;

    private int genned = 0;

    private Boolean keepingLoaded;

    public ChunkGenWorker(CommandSourceStack listener, BlockPos start, int total, ServerLevel dim, int interval) {
        this.listener = listener;
        this.start = start;
        this.total = total;
        this.dim = dim;
        this.queue = this.buildQueue();
        this.notificationFrequency = interval != -1 ? interval : Math.max(total / 20, 100);
        this.lastNotifcationTime = System.currentTimeMillis();
    }

    protected Queue<BlockPos> buildQueue() {
        Queue<BlockPos> ret = new ArrayDeque();
        ret.add(this.start);
        for (int radius = 1; ret.size() < this.total; radius++) {
            for (int q = -radius + 1; q <= radius && ret.size() < this.total; q++) {
                ret.add(this.start.offset(radius, 0, q));
            }
            for (int q = radius - 1; q >= -radius && ret.size() < this.total; q--) {
                ret.add(this.start.offset(q, 0, radius));
            }
            for (int q = radius - 1; q >= -radius && ret.size() < this.total; q--) {
                ret.add(this.start.offset(-radius, 0, q));
            }
            for (int q = -radius + 1; q <= radius && ret.size() < this.total; q++) {
                ret.add(this.start.offset(q, 0, -radius));
            }
        }
        return ret;
    }

    public MutableComponent getStartMessage(CommandSourceStack sender) {
        return Component.translatable("commands.forge.gen.start", this.total, this.start.m_123341_(), this.start.m_123343_(), this.dim);
    }

    @Override
    public boolean hasWork() {
        return this.queue.size() > 0;
    }

    @Override
    public boolean doWork() {
        BlockPos next = (BlockPos) this.queue.poll();
        if (next != null) {
            if (++this.lastNotification >= this.notificationFrequency || this.lastNotifcationTime < System.currentTimeMillis() - 60000L) {
                this.listener.sendSuccess(() -> Component.translatable("commands.forge.gen.progress", this.total - this.queue.size(), this.total), true);
                this.lastNotification = 0;
                this.lastNotifcationTime = System.currentTimeMillis();
            }
            int x = next.m_123341_();
            int z = next.m_123343_();
            if (!this.dim.m_7232_(x, z)) {
                ChunkAccess chunk = this.dim.m_6522_(x, z, ChunkStatus.EMPTY, true);
                if (!chunk.getStatus().isOrAfter(ChunkStatus.FULL)) {
                    chunk = this.dim.m_46819_(x, z, ChunkStatus.FULL);
                    this.genned++;
                }
            }
        }
        if (this.queue.size() == 0) {
            this.listener.sendSuccess(() -> Component.translatable("commands.forge.gen.complete", this.genned, this.total, this.dim.m_46472_().location()), true);
            return false;
        } else {
            return true;
        }
    }
}