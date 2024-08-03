package org.embeddedt.modernfix.world;

import com.mojang.logging.LogUtils;
import java.lang.ref.WeakReference;
import java.util.OptionalLong;
import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import org.embeddedt.modernfix.duck.ITimeTrackingServer;
import org.slf4j.Logger;

public class IntegratedWatchdog extends Thread {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final WeakReference<MinecraftServer> server;

    private static final long MAX_TICK_DELTA = 40000L;

    public IntegratedWatchdog(MinecraftServer server) {
        this.server = new WeakReference(server);
        this.setDaemon(true);
        this.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandlerWithName(LOGGER));
        this.setName("ModernFix integrated server watchdog");
    }

    private OptionalLong getLastTickStart() {
        MinecraftServer server = (MinecraftServer) this.server.get();
        return server != null && server.isRunning() ? OptionalLong.of(((ITimeTrackingServer) server).mfix$getLastTickStartTime()) : OptionalLong.empty();
    }

    public void run() {
        while (true) {
            OptionalLong lastTickStart = this.getLastTickStart();
            if (!lastTickStart.isPresent()) {
                return;
            }
            if (lastTickStart.getAsLong() >= 0L) {
                long curTime = Util.getMillis();
                long delta = curTime - lastTickStart.getAsLong();
                if (delta > 40000L) {
                    LOGGER.error("A single server tick has taken {}, more than {} milliseconds", delta, 40000L);
                    LOGGER.error(ThreadDumper.obtainThreadDump());
                }
                try {
                    Thread.sleep(40000L - delta);
                } catch (InterruptedException var7) {
                }
            }
        }
    }
}