package net.minecraft.server.dedicated;

import com.google.common.collect.Streams;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.Util;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;

public class ServerWatchdog implements Runnable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final long MAX_SHUTDOWN_TIME = 10000L;

    private static final int SHUTDOWN_STATUS = 1;

    private final DedicatedServer server;

    private final long maxTickTime;

    public ServerWatchdog(DedicatedServer dedicatedServer0) {
        this.server = dedicatedServer0;
        this.maxTickTime = dedicatedServer0.getMaxTickLength();
    }

    public void run() {
        while (this.server.m_130010_()) {
            long $$0 = this.server.m_129932_();
            long $$1 = Util.getMillis();
            long $$2 = $$1 - $$0;
            if ($$2 > this.maxTickTime) {
                LOGGER.error(LogUtils.FATAL_MARKER, "A single server tick took {} seconds (should be max {})", String.format(Locale.ROOT, "%.2f", (float) $$2 / 1000.0F), String.format(Locale.ROOT, "%.2f", 0.05F));
                LOGGER.error(LogUtils.FATAL_MARKER, "Considering it to be crashed, server will forcibly shutdown.");
                ThreadMXBean $$3 = ManagementFactory.getThreadMXBean();
                ThreadInfo[] $$4 = $$3.dumpAllThreads(true, true);
                StringBuilder $$5 = new StringBuilder();
                Error $$6 = new Error("Watchdog");
                for (ThreadInfo $$7 : $$4) {
                    if ($$7.getThreadId() == this.server.m_6304_().getId()) {
                        $$6.setStackTrace($$7.getStackTrace());
                    }
                    $$5.append($$7);
                    $$5.append("\n");
                }
                CrashReport $$8 = new CrashReport("Watching Server", $$6);
                this.server.m_177935_($$8.getSystemReport());
                CrashReportCategory $$9 = $$8.addCategory("Thread Dump");
                $$9.setDetail("Threads", $$5);
                CrashReportCategory $$10 = $$8.addCategory("Performance stats");
                $$10.setDetail("Random tick rate", (CrashReportDetail<String>) (() -> this.server.m_129910_().getGameRules().getRule(GameRules.RULE_RANDOMTICKING).toString()));
                $$10.setDetail("Level stats", (CrashReportDetail<String>) (() -> (String) Streams.stream(this.server.m_129785_()).map(p_288758_ -> p_288758_.m_46472_() + ": " + p_288758_.getWatchdogStats()).collect(Collectors.joining(",\n"))));
                Bootstrap.realStdoutPrintln("Crash report:\n" + $$8.getFriendlyReport());
                File $$11 = new File(new File(this.server.m_6237_(), "crash-reports"), "crash-" + Util.getFilenameFormattedDateTime() + "-server.txt");
                if ($$8.saveToFile($$11)) {
                    LOGGER.error("This crash report has been saved to: {}", $$11.getAbsolutePath());
                } else {
                    LOGGER.error("We were unable to save this crash report to disk.");
                }
                this.exit();
            }
            try {
                Thread.sleep($$0 + this.maxTickTime - $$1);
            } catch (InterruptedException var15) {
            }
        }
    }

    private void exit() {
        try {
            Timer $$0 = new Timer();
            $$0.schedule(new TimerTask() {

                public void run() {
                    Runtime.getRuntime().halt(1);
                }
            }, 10000L);
            System.exit(1);
        } catch (Throwable var2) {
            Runtime.getRuntime().halt(1);
        }
    }
}