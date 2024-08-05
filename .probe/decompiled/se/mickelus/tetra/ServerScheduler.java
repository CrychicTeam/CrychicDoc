package se.mickelus.tetra;

import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ServerScheduler {

    private static final Queue<ServerScheduler.Task> queue = Queues.newConcurrentLinkedQueue();

    private static int counter;

    public static void schedule(int delay, Runnable task) {
        queue.add(new ServerScheduler.Task(counter + delay, task));
    }

    public static void schedule(String id, int delay, Runnable task) {
        queue.removeIf(t -> id.equals(t.id));
        queue.add(new ServerScheduler.Task(id, counter + delay, task));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<ServerScheduler.Task> it = queue.iterator();
            while (it.hasNext()) {
                ServerScheduler.Task task = (ServerScheduler.Task) it.next();
                if (task.m_136254_() < counter) {
                    task.run();
                    it.remove();
                }
            }
            counter++;
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