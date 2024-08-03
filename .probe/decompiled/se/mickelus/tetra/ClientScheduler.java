package se.mickelus.tetra;

import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.server.TickTask;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ClientScheduler {

    private static final Queue<ClientScheduler.Task> queue = Queues.newConcurrentLinkedQueue();

    private static int counter;

    public static void schedule(int delay, Runnable task) {
        queue.add(new ClientScheduler.Task(counter + delay, task));
    }

    public static void schedule(String id, int delay, Runnable task) {
        queue.removeIf(t -> id.equals(t.id));
        queue.add(new ClientScheduler.Task(id, counter + delay, task));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<ClientScheduler.Task> it = queue.iterator();
            while (it.hasNext()) {
                ClientScheduler.Task task = (ClientScheduler.Task) it.next();
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