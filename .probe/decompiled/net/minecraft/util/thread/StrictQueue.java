package net.minecraft.util.thread;

import com.google.common.collect.Queues;
import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

public interface StrictQueue<T, F> {

    @Nullable
    F pop();

    boolean push(T var1);

    boolean isEmpty();

    int size();

    public static final class FixedPriorityQueue implements StrictQueue<StrictQueue.IntRunnable, Runnable> {

        private final Queue<Runnable>[] queues;

        private final AtomicInteger size = new AtomicInteger();

        public FixedPriorityQueue(int int0) {
            this.queues = new Queue[int0];
            for (int $$1 = 0; $$1 < int0; $$1++) {
                this.queues[$$1] = Queues.newConcurrentLinkedQueue();
            }
        }

        @Nullable
        public Runnable pop() {
            for (Queue<Runnable> $$0 : this.queues) {
                Runnable $$1 = (Runnable) $$0.poll();
                if ($$1 != null) {
                    this.size.decrementAndGet();
                    return $$1;
                }
            }
            return null;
        }

        public boolean push(StrictQueue.IntRunnable strictQueueIntRunnable0) {
            int $$1 = strictQueueIntRunnable0.priority;
            if ($$1 < this.queues.length && $$1 >= 0) {
                this.queues[$$1].add(strictQueueIntRunnable0);
                this.size.incrementAndGet();
                return true;
            } else {
                throw new IndexOutOfBoundsException(String.format(Locale.ROOT, "Priority %d not supported. Expected range [0-%d]", $$1, this.queues.length - 1));
            }
        }

        @Override
        public boolean isEmpty() {
            return this.size.get() == 0;
        }

        @Override
        public int size() {
            return this.size.get();
        }
    }

    public static final class IntRunnable implements Runnable {

        final int priority;

        private final Runnable task;

        public IntRunnable(int int0, Runnable runnable1) {
            this.priority = int0;
            this.task = runnable1;
        }

        public void run() {
            this.task.run();
        }

        public int getPriority() {
            return this.priority;
        }
    }

    public static final class QueueStrictQueue<T> implements StrictQueue<T, T> {

        private final Queue<T> queue;

        public QueueStrictQueue(Queue<T> queueT0) {
            this.queue = queueT0;
        }

        @Nullable
        @Override
        public T pop() {
            return (T) this.queue.poll();
        }

        @Override
        public boolean push(T t0) {
            return this.queue.add(t0);
        }

        @Override
        public boolean isEmpty() {
            return this.queue.isEmpty();
        }

        @Override
        public int size() {
            return this.queue.size();
        }
    }
}