package mezz.jei.common.util;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.Nullable;

public class DeduplicatingRunner {

    private final Runnable runnable;

    private final Duration delay;

    private final String name;

    @Nullable
    private Timer timer;

    @Nullable
    private TimerTask task;

    public DeduplicatingRunner(Runnable runnable, Duration delay, String name) {
        this.runnable = runnable;
        this.delay = delay;
        this.name = name;
    }

    public synchronized void run() {
        if (this.task != null) {
            this.task.cancel();
        }
        this.task = new TimerTask() {

            public void run() {
                DeduplicatingRunner.this.doRun(this);
            }
        };
        if (this.timer == null) {
            this.timer = new Timer(this.name);
        }
        this.timer.schedule(this.task, this.delay.toMillis());
    }

    private synchronized void doRun(TimerTask fromTask) {
        if (this.task == fromTask) {
            this.runnable.run();
            this.task = null;
            if (this.timer != null) {
                this.timer.cancel();
                this.timer = null;
            }
        }
    }
}