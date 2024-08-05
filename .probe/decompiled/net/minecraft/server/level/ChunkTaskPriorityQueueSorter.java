package net.minecraft.server.level;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.util.Unit;
import net.minecraft.util.thread.ProcessorHandle;
import net.minecraft.util.thread.ProcessorMailbox;
import net.minecraft.util.thread.StrictQueue;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;

public class ChunkTaskPriorityQueueSorter implements ChunkHolder.LevelChangeListener, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>> queues;

    private final Set<ProcessorHandle<?>> sleeping;

    private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;

    public ChunkTaskPriorityQueueSorter(List<ProcessorHandle<?>> listProcessorHandle0, Executor executor1, int int2) {
        this.queues = (Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>>) listProcessorHandle0.stream().collect(Collectors.toMap(Function.identity(), p_140561_ -> new ChunkTaskPriorityQueue(p_140561_.name() + "_queue", int2)));
        this.sleeping = Sets.newHashSet(listProcessorHandle0);
        this.mailbox = new ProcessorMailbox<>(new StrictQueue.FixedPriorityQueue(4), executor1, "sorter");
    }

    public boolean hasWork() {
        return this.mailbox.hasWork() || this.queues.values().stream().anyMatch(ChunkTaskPriorityQueue::m_201908_);
    }

    public static <T> ChunkTaskPriorityQueueSorter.Message<T> message(Function<ProcessorHandle<Unit>, T> functionProcessorHandleUnitT0, long long1, IntSupplier intSupplier2) {
        return new ChunkTaskPriorityQueueSorter.Message<>(functionProcessorHandleUnitT0, long1, intSupplier2);
    }

    public static ChunkTaskPriorityQueueSorter.Message<Runnable> message(Runnable runnable0, long long1, IntSupplier intSupplier2) {
        return new ChunkTaskPriorityQueueSorter.Message<>(p_140634_ -> () -> {
            runnable0.run();
            p_140634_.tell(Unit.INSTANCE);
        }, long1, intSupplier2);
    }

    public static ChunkTaskPriorityQueueSorter.Message<Runnable> message(ChunkHolder chunkHolder0, Runnable runnable1) {
        return message(runnable1, chunkHolder0.getPos().toLong(), chunkHolder0::m_140094_);
    }

    public static <T> ChunkTaskPriorityQueueSorter.Message<T> message(ChunkHolder chunkHolder0, Function<ProcessorHandle<Unit>, T> functionProcessorHandleUnitT1) {
        return message(functionProcessorHandleUnitT1, chunkHolder0.getPos().toLong(), chunkHolder0::m_140094_);
    }

    public static ChunkTaskPriorityQueueSorter.Release release(Runnable runnable0, long long1, boolean boolean2) {
        return new ChunkTaskPriorityQueueSorter.Release(runnable0, long1, boolean2);
    }

    public <T> ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<T>> getProcessor(ProcessorHandle<T> processorHandleT0, boolean boolean1) {
        return (ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<T>>) this.mailbox.m_18720_(p_140610_ -> new StrictQueue.IntRunnable(0, () -> {
            this.getQueue(processorHandleT0);
            p_140610_.tell(ProcessorHandle.of("chunk priority sorter around " + processorHandleT0.name(), p_143176_ -> this.submit(processorHandleT0, p_143176_.task, p_143176_.pos, p_143176_.level, boolean1)));
        })).join();
    }

    public ProcessorHandle<ChunkTaskPriorityQueueSorter.Release> getReleaseProcessor(ProcessorHandle<Runnable> processorHandleRunnable0) {
        return (ProcessorHandle<ChunkTaskPriorityQueueSorter.Release>) this.mailbox.m_18720_(p_140581_ -> new StrictQueue.IntRunnable(0, () -> p_140581_.tell(ProcessorHandle.of("chunk priority sorter around " + processorHandleRunnable0.name(), p_143165_ -> this.release(processorHandleRunnable0, p_143165_.pos, p_143165_.task, p_143165_.clearQueue))))).join();
    }

    @Override
    public void onLevelChange(ChunkPos chunkPos0, IntSupplier intSupplier1, int int2, IntConsumer intConsumer3) {
        this.mailbox.tell(new StrictQueue.IntRunnable(0, () -> {
            int $$4 = intSupplier1.getAsInt();
            this.queues.values().forEach(p_143155_ -> p_143155_.resortChunkTasks($$4, chunkPos0, int2));
            intConsumer3.accept(int2);
        }));
    }

    private <T> void release(ProcessorHandle<T> processorHandleT0, long long1, Runnable runnable2, boolean boolean3) {
        this.mailbox.tell(new StrictQueue.IntRunnable(1, () -> {
            ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> $$4 = this.getQueue(processorHandleT0);
            $$4.release(long1, boolean3);
            if (this.sleeping.remove(processorHandleT0)) {
                this.pollTask($$4, processorHandleT0);
            }
            runnable2.run();
        }));
    }

    private <T> void submit(ProcessorHandle<T> processorHandleT0, Function<ProcessorHandle<Unit>, T> functionProcessorHandleUnitT1, long long2, IntSupplier intSupplier3, boolean boolean4) {
        this.mailbox.tell(new StrictQueue.IntRunnable(2, () -> {
            ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> $$5 = this.getQueue(processorHandleT0);
            int $$6 = intSupplier3.getAsInt();
            $$5.submit(Optional.of(functionProcessorHandleUnitT1), long2, $$6);
            if (boolean4) {
                $$5.submit(Optional.empty(), long2, $$6);
            }
            if (this.sleeping.remove(processorHandleT0)) {
                this.pollTask($$5, processorHandleT0);
            }
        }));
    }

    private <T> void pollTask(ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> chunkTaskPriorityQueueFunctionProcessorHandleUnitT0, ProcessorHandle<T> processorHandleT1) {
        this.mailbox.tell(new StrictQueue.IntRunnable(3, () -> {
            Stream<Either<Function<ProcessorHandle<Unit>, T>, Runnable>> $$2 = chunkTaskPriorityQueueFunctionProcessorHandleUnitT0.pop();
            if ($$2 == null) {
                this.sleeping.add(processorHandleT1);
            } else {
                CompletableFuture.allOf((CompletableFuture[]) $$2.map(p_143172_ -> (CompletableFuture) p_143172_.map(processorHandleT1::m_18720_, p_143180_ -> {
                    p_143180_.run();
                    return CompletableFuture.completedFuture(Unit.INSTANCE);
                })).toArray(CompletableFuture[]::new)).thenAccept(p_212894_ -> this.pollTask(chunkTaskPriorityQueueFunctionProcessorHandleUnitT0, processorHandleT1));
            }
        }));
    }

    private <T> ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> getQueue(ProcessorHandle<T> processorHandleT0) {
        ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>> $$1 = (ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>) this.queues.get(processorHandleT0);
        if ($$1 == null) {
            throw (IllegalArgumentException) Util.pauseInIde((T) (new IllegalArgumentException("No queue for: " + processorHandleT0)));
        } else {
            return (ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>>) $$1;
        }
    }

    @VisibleForTesting
    public String getDebugStatus() {
        return (String) this.queues.entrySet().stream().map(p_212898_ -> ((ProcessorHandle) p_212898_.getKey()).name() + "=[" + (String) ((ChunkTaskPriorityQueue) p_212898_.getValue()).getAcquired().stream().map(p_212896_ -> p_212896_ + ":" + new ChunkPos(p_212896_)).collect(Collectors.joining(",")) + "]").collect(Collectors.joining(",")) + ", s=" + this.sleeping.size();
    }

    public void close() {
        this.queues.keySet().forEach(ProcessorHandle::close);
    }

    public static final class Message<T> {

        final Function<ProcessorHandle<Unit>, T> task;

        final long pos;

        final IntSupplier level;

        Message(Function<ProcessorHandle<Unit>, T> functionProcessorHandleUnitT0, long long1, IntSupplier intSupplier2) {
            this.task = functionProcessorHandleUnitT0;
            this.pos = long1;
            this.level = intSupplier2;
        }
    }

    public static final class Release {

        final Runnable task;

        final long pos;

        final boolean clearQueue;

        Release(Runnable runnable0, long long1, boolean boolean2) {
            this.task = runnable0;
            this.pos = long1;
            this.clearQueue = boolean2;
        }
    }
}