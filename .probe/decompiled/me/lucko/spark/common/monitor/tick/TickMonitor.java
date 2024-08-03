package me.lucko.spark.common.monitor.tick;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.text.DecimalFormat;
import java.util.DoubleSummaryStatistics;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.monitor.memory.GarbageCollectionMonitor;
import me.lucko.spark.common.tick.TickHook;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;

public abstract class TickMonitor implements TickHook.Callback, GarbageCollectionMonitor.Listener, AutoCloseable {

    private static final DecimalFormat DF = new DecimalFormat("#.##");

    private final SparkPlatform platform;

    private final TickHook tickHook;

    private final int zeroTick;

    private final GarbageCollectionMonitor garbageCollectionMonitor;

    private final ReportPredicate reportPredicate;

    private TickMonitor.Phase phase = null;

    private volatile double lastTickTime = 0.0;

    private final DoubleSummaryStatistics averageTickTimeCalc = new DoubleSummaryStatistics();

    private double averageTickTime;

    public TickMonitor(SparkPlatform platform, TickHook tickHook, ReportPredicate reportPredicate, boolean monitorGc) {
        this.platform = platform;
        this.tickHook = tickHook;
        this.zeroTick = tickHook.getCurrentTick();
        this.reportPredicate = reportPredicate;
        if (monitorGc) {
            this.garbageCollectionMonitor = new GarbageCollectionMonitor();
            this.garbageCollectionMonitor.addListener(this);
        } else {
            this.garbageCollectionMonitor = null;
        }
    }

    public int getCurrentTick() {
        return this.tickHook.getCurrentTick() - this.zeroTick;
    }

    protected abstract void sendMessage(Component var1);

    public void start() {
        this.tickHook.addCallback(this);
    }

    public void close() {
        this.tickHook.removeCallback(this);
        if (this.garbageCollectionMonitor != null) {
            this.garbageCollectionMonitor.close();
        }
    }

    @Override
    public void onTick(int currentTick) {
        double now = (double) System.nanoTime() / 1000000.0;
        if (this.phase == null) {
            this.phase = TickMonitor.Phase.SETUP;
            this.lastTickTime = now;
            this.sendMessage(Component.text("Tick monitor started. Before the monitor becomes fully active, the server's average tick rate will be calculated over a period of 120 ticks (approx 6 seconds)."));
        } else {
            double last = this.lastTickTime;
            double tickDuration = now - last;
            this.lastTickTime = now;
            if (last != 0.0) {
                if (this.phase == TickMonitor.Phase.SETUP) {
                    this.averageTickTimeCalc.accept(tickDuration);
                    if (this.averageTickTimeCalc.getCount() >= 120L) {
                        this.platform.getPlugin().executeAsync(() -> {
                            this.sendMessage(Component.text("Analysis is now complete.", NamedTextColor.GOLD));
                            this.sendMessage(Component.text().color(NamedTextColor.GRAY).append(Component.text(">", NamedTextColor.WHITE)).append(Component.space()).append(Component.text("Max: ")).append(Component.text(DF.format(this.averageTickTimeCalc.getMax()))).append(Component.text("ms")).build());
                            this.sendMessage(Component.text().color(NamedTextColor.GRAY).append(Component.text(">", NamedTextColor.WHITE)).append(Component.space()).append(Component.text("Min: ")).append(Component.text(DF.format(this.averageTickTimeCalc.getMin()))).append(Component.text("ms")).build());
                            this.sendMessage(Component.text().color(NamedTextColor.GRAY).append(Component.text(">", NamedTextColor.WHITE)).append(Component.space()).append(Component.text("Average: ")).append(Component.text(DF.format(this.averageTickTimeCalc.getAverage()))).append(Component.text("ms")).build());
                            this.sendMessage(this.reportPredicate.monitoringStartMessage());
                        });
                        this.averageTickTime = this.averageTickTimeCalc.getAverage();
                        this.phase = TickMonitor.Phase.MONITORING;
                    }
                }
                if (this.phase == TickMonitor.Phase.MONITORING) {
                    double increase = tickDuration - this.averageTickTime;
                    double percentageChange = increase * 100.0 / this.averageTickTime;
                    if (this.reportPredicate.shouldReport(tickDuration, increase, percentageChange)) {
                        this.platform.getPlugin().executeAsync(() -> this.sendMessage(Component.text().color(NamedTextColor.GRAY).append(Component.text("Tick ")).append(Component.text("#" + this.getCurrentTick(), NamedTextColor.DARK_GRAY)).append(Component.text(" lasted ")).append(Component.text(DF.format(tickDuration), NamedTextColor.GOLD)).append(Component.text(" ms. ")).append(Component.text("(")).append(Component.text(DF.format(percentageChange) + "%", NamedTextColor.GOLD)).append(Component.text(" increase from avg)")).build()));
                    }
                }
            }
        }
    }

    @Override
    public void onGc(GarbageCollectionNotificationInfo data) {
        if (this.phase == TickMonitor.Phase.SETUP) {
            this.lastTickTime = 0.0;
        } else {
            this.platform.getPlugin().executeAsync(() -> this.sendMessage(Component.text().color(NamedTextColor.GRAY).append(Component.text("Tick ")).append(Component.text("#" + this.getCurrentTick(), NamedTextColor.DARK_GRAY)).append(Component.text(" included ")).append(Component.text("GC", NamedTextColor.RED)).append(Component.text(" lasting ")).append(Component.text(DF.format(data.getGcInfo().getDuration()), NamedTextColor.GOLD)).append(Component.text(" ms. (type = " + GarbageCollectionMonitor.getGcType(data) + ")")).build()));
        }
    }

    private static enum Phase {

        SETUP, MONITORING
    }
}