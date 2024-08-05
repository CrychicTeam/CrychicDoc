package journeymap.client.thread;

import java.io.File;
import java.util.concurrent.ExecutorService;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.task.multi.ITask;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class RunnableTask implements Runnable {

    static final JourneymapClient jm = JourneymapClient.getInstance();

    static final Logger logger = Journeymap.getLogger();

    static final Minecraft mc = Minecraft.getInstance();

    static final boolean threadLogging = jm.isThreadLogging();

    private final ExecutorService taskExecutor;

    private final Runnable innerRunnable;

    private final ITask task;

    private final int timeout;

    public RunnableTask(ExecutorService taskExecutor, ITask task) {
        this.taskExecutor = taskExecutor;
        this.task = task;
        this.timeout = task.getMaxRuntime();
        this.innerRunnable = new RunnableTask.Inner();
    }

    public void run() {
        try {
            this.taskExecutor.submit(this.innerRunnable);
        } catch (Throwable var2) {
            Journeymap.getLogger().warn("Interrupted task that ran too long:" + this.task);
        }
    }

    class Inner implements Runnable {

        public final void run() {
            try {
                if (!RunnableTask.jm.isMapping()) {
                    RunnableTask.logger.debug("JM not mapping, aborting");
                    return;
                }
                File jmWorldDir = FileHandler.getJMWorldDir(RunnableTask.mc);
                if (jmWorldDir == null) {
                    RunnableTask.logger.debug("JM world dir not found, aborting");
                    return;
                }
                RunnableTask.this.task.performTask(RunnableTask.mc, RunnableTask.jm, jmWorldDir, RunnableTask.threadLogging);
            } catch (InterruptedException var3) {
                RunnableTask.logger.debug("Task interrupted: " + LogFormatter.toPartialString(var3));
            } catch (Throwable var4) {
                String error = "Unexpected error during RunnableTask: " + LogFormatter.toString(var4);
                RunnableTask.logger.error(error);
            }
        }
    }
}