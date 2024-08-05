package se.mickelus.mutil.scheduling;

import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraftforge.event.TickEvent;

@ParametersAreNonnullByDefault
public class AbstractScheduler {

    private final Queue<AbstractScheduler.Task> queue = Queues.newConcurrentLinkedQueue();

    private int counter;

    public void schedule(int delay, Runnable task) {
        this.queue.add(new AbstractScheduler.Task(this.counter + delay, task));
    }

    public void schedule(String id, int delay, Runnable task) {
        this.queue.removeIf(t -> id.equals(t.id));
        this.queue.add(new AbstractScheduler.Task(id, this.counter + delay, task));
    }

    public void tick(TickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<AbstractScheduler.Task> it = this.queue.iterator();
            while (it.hasNext()) {
                AbstractScheduler.Task task = (AbstractScheduler.Task) it.next();
                if (task.m_136254_() < this.counter) {
                    task.run();
                    it.remove();
                }
            }
            this.counter++;
        }
    }

    static class Task extends TickTask {

        private String id;

        public Task(int timestamp, Runnable task) {
            super(timestamp, task);
        }

        public Task(String id, int timestamp, Runnable task) {
            this(timestamp, task);
            this.id = id;
        }
    }
}