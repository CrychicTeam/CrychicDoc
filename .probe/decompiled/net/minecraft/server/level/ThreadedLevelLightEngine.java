package net.minecraft.server.level;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.thread.ProcessorHandle;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.LightChunkGetter;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.slf4j.Logger;

public class ThreadedLevelLightEngine extends LevelLightEngine implements AutoCloseable {

    public static final int DEFAULT_BATCH_SIZE = 1000;

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ProcessorMailbox<Runnable> taskMailbox;

    private final ObjectList<Pair<ThreadedLevelLightEngine.TaskType, Runnable>> lightTasks = new ObjectArrayList();

    private final ChunkMap chunkMap;

    private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> sorterMailbox;

    private final int taskPerBatch = 1000;

    private final AtomicBoolean scheduled = new AtomicBoolean();

    public ThreadedLevelLightEngine(LightChunkGetter lightChunkGetter0, ChunkMap chunkMap1, boolean boolean2, ProcessorMailbox<Runnable> processorMailboxRunnable3, ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> processorHandleChunkTaskPriorityQueueSorterMessageRunnable4) {
        super(lightChunkGetter0, true, boolean2);
        this.chunkMap = chunkMap1;
        this.sorterMailbox = processorHandleChunkTaskPriorityQueueSorterMessageRunnable4;
        this.taskMailbox = processorMailboxRunnable3;
    }

    public void close() {
    }

    @Override
    public int runLightUpdates() {
        throw (UnsupportedOperationException) Util.pauseInIde(new UnsupportedOperationException("Ran automatically on a different thread!"));
    }

    @Override
    public void checkBlock(BlockPos blockPos0) {
        BlockPos $$1 = blockPos0.immutable();
        this.addTask(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()), ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.checkBlock($$1)), () -> "checkBlock " + $$1));
    }

    protected void updateChunkStatus(ChunkPos chunkPos0) {
        this.addTask(chunkPos0.x, chunkPos0.z, () -> 0, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> {
            super.retainData(chunkPos0, false);
            super.setLightEnabled(chunkPos0, false);
            for (int $$1 = this.m_164447_(); $$1 < this.m_164448_(); $$1++) {
                super.queueSectionData(LightLayer.BLOCK, SectionPos.of(chunkPos0, $$1), null);
                super.queueSectionData(LightLayer.SKY, SectionPos.of(chunkPos0, $$1), null);
            }
            for (int $$2 = this.f_164445_.getMinSection(); $$2 < this.f_164445_.getMaxSection(); $$2++) {
                super.updateSectionStatus(SectionPos.of(chunkPos0, $$2), true);
            }
        }), () -> "updateChunkStatus " + chunkPos0 + " true"));
    }

    @Override
    public void updateSectionStatus(SectionPos sectionPos0, boolean boolean1) {
        this.addTask(sectionPos0.x(), sectionPos0.z(), () -> 0, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.updateSectionStatus(sectionPos0, boolean1)), () -> "updateSectionStatus " + sectionPos0 + " " + boolean1));
    }

    @Override
    public void propagateLightSources(ChunkPos chunkPos0) {
        this.addTask(chunkPos0.x, chunkPos0.z, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.propagateLightSources(chunkPos0)), () -> "propagateLight " + chunkPos0));
    }

    @Override
    public void setLightEnabled(ChunkPos chunkPos0, boolean boolean1) {
        this.addTask(chunkPos0.x, chunkPos0.z, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.setLightEnabled(chunkPos0, boolean1)), () -> "enableLight " + chunkPos0 + " " + boolean1));
    }

    @Override
    public void queueSectionData(LightLayer lightLayer0, SectionPos sectionPos1, @Nullable DataLayer dataLayer2) {
        this.addTask(sectionPos1.x(), sectionPos1.z(), () -> 0, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.queueSectionData(lightLayer0, sectionPos1, dataLayer2)), () -> "queueData " + sectionPos1));
    }

    private void addTask(int int0, int int1, ThreadedLevelLightEngine.TaskType threadedLevelLightEngineTaskType2, Runnable runnable3) {
        this.addTask(int0, int1, this.chunkMap.getChunkQueueLevel(ChunkPos.asLong(int0, int1)), threadedLevelLightEngineTaskType2, runnable3);
    }

    private void addTask(int int0, int int1, IntSupplier intSupplier2, ThreadedLevelLightEngine.TaskType threadedLevelLightEngineTaskType3, Runnable runnable4) {
        this.sorterMailbox.tell(ChunkTaskPriorityQueueSorter.message((Runnable) (() -> {
            this.lightTasks.add(Pair.of(threadedLevelLightEngineTaskType3, runnable4));
            if (this.lightTasks.size() >= 1000) {
                this.runUpdate();
            }
        }), ChunkPos.asLong(int0, int1), intSupplier2));
    }

    @Override
    public void retainData(ChunkPos chunkPos0, boolean boolean1) {
        this.addTask(chunkPos0.x, chunkPos0.z, () -> 0, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> super.retainData(chunkPos0, boolean1)), () -> "retainData " + chunkPos0));
    }

    public CompletableFuture<ChunkAccess> initializeLight(ChunkAccess chunkAccess0, boolean boolean1) {
        ChunkPos $$2 = chunkAccess0.getPos();
        this.addTask($$2.x, $$2.z, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> {
            LevelChunkSection[] $$2x = chunkAccess0.getSections();
            for (int $$3 = 0; $$3 < chunkAccess0.m_151559_(); $$3++) {
                LevelChunkSection $$4 = $$2x[$$3];
                if (!$$4.hasOnlyAir()) {
                    int $$5 = this.f_164445_.getSectionYFromSectionIndex($$3);
                    super.updateSectionStatus(SectionPos.of($$2, $$5), false);
                }
            }
        }), () -> "initializeLight: " + $$2));
        return CompletableFuture.supplyAsync(() -> {
            super.setLightEnabled($$2, boolean1);
            super.retainData($$2, false);
            return chunkAccess0;
        }, p_215135_ -> this.addTask($$2.x, $$2.z, ThreadedLevelLightEngine.TaskType.POST_UPDATE, p_215135_));
    }

    public CompletableFuture<ChunkAccess> lightChunk(ChunkAccess chunkAccess0, boolean boolean1) {
        ChunkPos $$2 = chunkAccess0.getPos();
        chunkAccess0.setLightCorrect(false);
        this.addTask($$2.x, $$2.z, ThreadedLevelLightEngine.TaskType.PRE_UPDATE, Util.name((Runnable) (() -> {
            if (!boolean1) {
                super.propagateLightSources($$2);
            }
        }), () -> "lightChunk " + $$2 + " " + boolean1));
        return CompletableFuture.supplyAsync(() -> {
            chunkAccess0.setLightCorrect(true);
            this.chunkMap.releaseLightTicket($$2);
            return chunkAccess0;
        }, p_280982_ -> this.addTask($$2.x, $$2.z, ThreadedLevelLightEngine.TaskType.POST_UPDATE, p_280982_));
    }

    public void tryScheduleUpdate() {
        if ((!this.lightTasks.isEmpty() || super.hasLightWork()) && this.scheduled.compareAndSet(false, true)) {
            this.taskMailbox.tell(() -> {
                this.runUpdate();
                this.scheduled.set(false);
            });
        }
    }

    private void runUpdate() {
        int $$0 = Math.min(this.lightTasks.size(), 1000);
        ObjectListIterator<Pair<ThreadedLevelLightEngine.TaskType, Runnable>> $$1 = this.lightTasks.iterator();
        int $$2;
        for ($$2 = 0; $$1.hasNext() && $$2 < $$0; $$2++) {
            Pair<ThreadedLevelLightEngine.TaskType, Runnable> $$3 = (Pair<ThreadedLevelLightEngine.TaskType, Runnable>) $$1.next();
            if ($$3.getFirst() == ThreadedLevelLightEngine.TaskType.PRE_UPDATE) {
                ((Runnable) $$3.getSecond()).run();
            }
        }
        $$1.back($$2);
        super.runLightUpdates();
        for (int var5 = 0; $$1.hasNext() && var5 < $$0; var5++) {
            Pair<ThreadedLevelLightEngine.TaskType, Runnable> $$4 = (Pair<ThreadedLevelLightEngine.TaskType, Runnable>) $$1.next();
            if ($$4.getFirst() == ThreadedLevelLightEngine.TaskType.POST_UPDATE) {
                ((Runnable) $$4.getSecond()).run();
            }
            $$1.remove();
        }
    }

    static enum TaskType {

        PRE_UPDATE, POST_UPDATE
    }
}