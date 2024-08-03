package journeymap.client.task.multi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import journeymap.client.log.StatTimer;
import journeymap.client.thread.RunnableTask;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.thread.JMThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

public class TaskController {

    static final Logger logger = Journeymap.getLogger();

    final ArrayBlockingQueue<Future> queue = new ArrayBlockingQueue(1);

    final List<ITaskManager> managers = new LinkedList();

    final Minecraft minecraft = Minecraft.getInstance();

    final ReentrantLock lock = new ReentrantLock();

    private volatile ScheduledExecutorService taskExecutor;

    public TaskController() {
        this.managers.add(new MapRegionTask.Manager());
        this.managers.add(new SaveMapTask.Manager());
        this.managers.add(new MapPlayerTask.Manager());
        this.managers.add(new InitColorManagerTask.Manager());
    }

    private void ensureExecutor() {
        if (this.taskExecutor == null || this.taskExecutor.isShutdown()) {
            this.taskExecutor = Executors.newScheduledThreadPool(1, new JMThreadFactory("task"));
            this.queue.clear();
        }
    }

    public Boolean isActive() {
        return this.taskExecutor != null && !this.taskExecutor.isShutdown();
    }

    public void enableTasks() {
        this.queue.clear();
        this.ensureExecutor();
        new LinkedList(this.managers);
        for (ITaskManager manager : this.managers) {
            boolean enabled = manager.enableTask(this.minecraft, null);
            if (!enabled) {
                logger.debug("Task not initially enabled: " + manager.getTaskClass().getSimpleName());
            } else {
                logger.debug("Task ready: " + manager.getTaskClass().getSimpleName());
            }
        }
    }

    public void clear() {
        this.managers.clear();
        this.queue.clear();
        if (this.taskExecutor != null && !this.taskExecutor.isShutdown()) {
            this.taskExecutor.shutdownNow();
            try {
                this.taskExecutor.awaitTermination(5L, TimeUnit.SECONDS);
            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
            this.taskExecutor = null;
        }
    }

    private ITaskManager getManager(Class<? extends ITaskManager> managerClass) {
        ITaskManager taskManager = null;
        for (ITaskManager manager : this.managers) {
            if (manager.getClass() == managerClass) {
                taskManager = manager;
                break;
            }
        }
        return taskManager;
    }

    public boolean isTaskManagerEnabled(Class<? extends ITaskManager> managerClass) {
        ITaskManager taskManager = this.getManager(managerClass);
        if (taskManager != null) {
            return taskManager.isEnabled(Minecraft.getInstance());
        } else {
            logger.warn("1 Couldn't toggle task; manager not in controller: " + managerClass.getClass().getName());
            return false;
        }
    }

    public void toggleTask(Class<? extends ITaskManager> managerClass, boolean enable, Object params) {
        ITaskManager taskManager = null;
        for (ITaskManager manager : this.managers) {
            if (manager.getClass() == managerClass) {
                taskManager = manager;
                break;
            }
        }
        if (taskManager != null) {
            this.toggleTask(taskManager, enable, params);
        } else {
            logger.warn("1 Couldn't toggle task; manager not in controller: " + managerClass.getClass().getName());
        }
    }

    public void toggleTask(ITaskManager manager, boolean enable, Object params) {
        Minecraft minecraft = Minecraft.getInstance();
        if (manager.isEnabled(minecraft)) {
            if (!enable) {
                logger.debug("Disabling task: " + manager.getTaskClass().getSimpleName());
                manager.disableTask(minecraft);
            } else {
                logger.debug("Task already enabled: " + manager.getTaskClass().getSimpleName());
            }
        } else if (enable) {
            logger.debug("Enabling task: " + manager.getTaskClass().getSimpleName());
            manager.enableTask(minecraft, params);
        } else {
            logger.debug("Task already disabled: " + manager.getTaskClass().getSimpleName());
        }
    }

    public void disableTasks() {
        for (ITaskManager manager : this.managers) {
            if (manager.isEnabled(this.minecraft)) {
                manager.disableTask(this.minecraft);
                logger.debug("Task disabled: " + manager.getTaskClass().getSimpleName());
            }
        }
    }

    public boolean hasRunningTask() {
        return !this.queue.isEmpty();
    }

    public void queueOneOff(Runnable runnable) throws Exception {
        try {
            this.ensureExecutor();
            if (this.taskExecutor != null && !this.taskExecutor.isShutdown()) {
                this.taskExecutor.submit(runnable);
            } else {
                throw new IllegalStateException("TaskExecutor isn't running");
            }
        } catch (Exception var3) {
            logger.error("TaskController couldn't queueOneOff(): " + LogFormatter.toString(var3));
            throw var3;
        }
    }

    public void performTasks() {
        ProfilerFiller profiler = Minecraft.getInstance().getProfiler();
        profiler.push("journeymapTask");
        StatTimer totalTimer = StatTimer.get("TaskController.performMultithreadTasks", 1, 500).start();
        try {
            if (!this.lock.tryLock()) {
                logger.warn("TaskController appears to have multiple threads trying to use it");
            } else {
                if (!this.queue.isEmpty() && ((Future) this.queue.peek()).isDone()) {
                    try {
                        this.queue.take();
                    } catch (InterruptedException var11) {
                        logger.warn("Interrupted" + var11.getMessage());
                    }
                }
                if (this.queue.isEmpty()) {
                    ITask task = null;
                    ITaskManager manager = this.getNextManager(this.minecraft);
                    if (manager == null) {
                        logger.warn("No task managers enabled!");
                        return;
                    }
                    boolean accepted = false;
                    StatTimer timer = StatTimer.get(manager.getTaskClass().getSimpleName() + ".Manager.getTask").start();
                    task = manager.getTask(this.minecraft);
                    if (task == null) {
                        timer.cancel();
                    } else {
                        timer.stop();
                        this.ensureExecutor();
                        if (this.taskExecutor != null && !this.taskExecutor.isShutdown()) {
                            RunnableTask runnableTask = new RunnableTask(this.taskExecutor, task);
                            this.queue.add(this.taskExecutor.submit(runnableTask));
                            accepted = true;
                            if (logger.isTraceEnabled()) {
                                logger.debug("Scheduled " + manager.getTaskClass().getSimpleName());
                            }
                        } else {
                            logger.warn("TaskExecutor isn't running");
                        }
                        manager.taskAccepted(task, accepted);
                    }
                }
                this.lock.unlock();
            }
        } finally {
            totalTimer.stop();
            profiler.pop();
        }
    }

    private ITaskManager getNextManager(Minecraft minecraft) {
        for (ITaskManager manager : this.managers) {
            if (manager.isEnabled(minecraft)) {
                return manager;
            }
        }
        return null;
    }
}