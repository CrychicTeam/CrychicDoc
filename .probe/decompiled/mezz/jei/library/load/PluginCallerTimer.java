package mezz.jei.library.load;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class PluginCallerTimer implements AutoCloseable {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Nullable
    private PluginCallerTimerRunnable runnable;

    public PluginCallerTimer() {
        this.executor.scheduleAtFixedRate(this::run, 1L, 1L, TimeUnit.MILLISECONDS);
    }

    private synchronized void run() {
        if (this.runnable != null) {
            this.runnable.check();
        }
    }

    public synchronized void begin(String title, ResourceLocation pluginUid) {
        this.runnable = new PluginCallerTimerRunnable(title, pluginUid);
    }

    public synchronized void end() {
        if (this.runnable != null) {
            this.runnable.stop();
            this.runnable = null;
        }
    }

    public void close() {
        this.executor.shutdown();
    }
}