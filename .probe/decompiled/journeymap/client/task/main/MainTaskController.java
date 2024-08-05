package journeymap.client.task.main;

import com.google.common.collect.Queues;
import java.util.concurrent.ConcurrentLinkedQueue;
import journeymap.client.JourneymapClient;
import journeymap.client.log.StatTimer;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;

public class MainTaskController {

    private final ConcurrentLinkedQueue<IMainThreadTask> currentQueue = Queues.newConcurrentLinkedQueue();

    private final ConcurrentLinkedQueue<IMainThreadTask> deferredQueue = Queues.newConcurrentLinkedQueue();

    public void addTask(IMainThreadTask task) {
        synchronized (this.currentQueue) {
            this.currentQueue.add(task);
        }
    }

    public boolean isActive() {
        return this.currentQueue.isEmpty() ? false : this.currentQueue.size() != 1 || !(this.currentQueue.peek() instanceof MappingMonitorTask);
    }

    public void performTasks() {
        try {
            synchronized (this.currentQueue) {
                if (this.currentQueue.isEmpty()) {
                    this.currentQueue.add(new MappingMonitorTask());
                }
                Minecraft minecraft = Minecraft.getInstance();
                JourneymapClient journeymapClient = JourneymapClient.getInstance();
                while (!this.currentQueue.isEmpty()) {
                    IMainThreadTask task = (IMainThreadTask) this.currentQueue.poll();
                    if (task != null) {
                        StatTimer timer = StatTimer.get(task.getName());
                        timer.start();
                        IMainThreadTask deferred = task.perform(minecraft, journeymapClient);
                        timer.stop();
                        if (deferred != null) {
                            this.deferredQueue.add(deferred);
                        }
                    }
                }
                this.currentQueue.addAll(this.deferredQueue);
                this.deferredQueue.clear();
            }
        } catch (Throwable var9) {
            String error = "Error in TickTaskController.performMainThreadTasks(): " + var9.getMessage();
            Journeymap.getLogger().error(error);
            Journeymap.getLogger().error(LogFormatter.toString(var9));
        }
    }
}