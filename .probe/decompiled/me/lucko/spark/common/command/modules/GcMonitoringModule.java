package me.lucko.spark.common.command.modules;

import com.sun.management.GarbageCollectionNotificationInfo;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.command.Command;
import me.lucko.spark.common.command.CommandModule;
import me.lucko.spark.common.command.CommandResponseHandler;
import me.lucko.spark.common.monitor.memory.GarbageCollectionMonitor;
import me.lucko.spark.common.monitor.memory.GarbageCollectorStatistics;
import me.lucko.spark.common.util.FormatUtil;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;

public class GcMonitoringModule implements CommandModule {

    private static final DecimalFormat DF = new DecimalFormat("#.##");

    private GcMonitoringModule.ReportingGcMonitor activeGcMonitor = null;

    @Override
    public void close() {
        if (this.activeGcMonitor != null) {
            this.activeGcMonitor.close();
            this.activeGcMonitor = null;
        }
    }

    @Override
    public void registerCommands(Consumer<Command> consumer) {
        consumer.accept(Command.builder().aliases("gc").executor((platform, sender, resp, arguments) -> {
            resp.replyPrefixed(Component.text("Calculating GC statistics..."));
            List<Component> report = new LinkedList();
            report.add(Component.empty());
            report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Garbage Collector statistics", NamedTextColor.GOLD)).build());
            long serverUptime = System.currentTimeMillis() - platform.getServerNormalOperationStartTime();
            Map<String, GarbageCollectorStatistics> collectorStats = GarbageCollectorStatistics.pollStatsSubtractInitial(platform.getStartupGcStatistics());
            for (Entry<String, GarbageCollectorStatistics> collector : collectorStats.entrySet()) {
                String collectorName = (String) collector.getKey();
                double collectionTime = (double) ((GarbageCollectorStatistics) collector.getValue()).getCollectionTime();
                long collectionCount = ((GarbageCollectorStatistics) collector.getValue()).getCollectionCount();
                report.add(Component.empty());
                if (collectionCount == 0L) {
                    report.add(Component.text().content("    ").append(Component.text(collectorName + " collector:", NamedTextColor.GRAY)).build());
                    report.add(Component.text().content("      ").append(Component.text(0, NamedTextColor.WHITE)).append(Component.text(" collections", NamedTextColor.GRAY)).build());
                } else {
                    double averageCollectionTime = collectionTime / (double) collectionCount;
                    double averageFrequency = ((double) serverUptime - collectionTime) / (double) collectionCount;
                    report.add(Component.text().content("    ").append(Component.text(collectorName + " collector:", NamedTextColor.GRAY)).build());
                    report.add(Component.text().content("      ").append(Component.text(DF.format(averageCollectionTime), NamedTextColor.GOLD)).append(Component.text(" ms avg", NamedTextColor.GRAY)).append(Component.text(", ", NamedTextColor.DARK_GRAY)).append(Component.text(collectionCount, NamedTextColor.WHITE)).append(Component.text(" total collections", NamedTextColor.GRAY)).build());
                    report.add(Component.text().content("      ").append(Component.text(FormatUtil.formatSeconds((long) averageFrequency / 1000L), NamedTextColor.WHITE)).append(Component.text(" avg frequency", NamedTextColor.GRAY)).build());
                }
            }
            if (collectorStats.isEmpty()) {
                resp.replyPrefixed(Component.text("No garbage collectors are reporting data."));
            } else {
                resp.reply(report);
            }
        }).build());
        consumer.accept(Command.builder().aliases("gcmonitor", "gcmonitoring").executor((platform, sender, resp, arguments) -> {
            if (this.activeGcMonitor == null) {
                this.activeGcMonitor = new GcMonitoringModule.ReportingGcMonitor(platform, resp);
                resp.broadcastPrefixed(Component.text("GC monitor enabled."));
            } else {
                this.close();
                resp.broadcastPrefixed(Component.text("GC monitor disabled."));
            }
        }).build());
    }

    private static class ReportingGcMonitor extends GarbageCollectionMonitor implements GarbageCollectionMonitor.Listener {

        private final SparkPlatform platform;

        private final CommandResponseHandler resp;

        ReportingGcMonitor(SparkPlatform platform, CommandResponseHandler resp) {
            this.platform = platform;
            this.resp = resp;
            this.addListener(this);
        }

        @Override
        public void onGc(GarbageCollectionNotificationInfo data) {
            String gcType = GarbageCollectionMonitor.getGcType(data);
            String gcCause = data.getGcCause() != null ? " (cause = " + data.getGcCause() + ")" : "";
            Map<String, MemoryUsage> beforeUsages = data.getGcInfo().getMemoryUsageBeforeGc();
            Map<String, MemoryUsage> afterUsages = data.getGcInfo().getMemoryUsageAfterGc();
            this.platform.getPlugin().executeAsync(() -> {
                List<Component> report = new LinkedList();
                report.add(CommandResponseHandler.applyPrefix(Component.text().color(NamedTextColor.GRAY).append(Component.text(gcType + " ")).append(Component.text("GC", NamedTextColor.RED)).append(Component.text(" lasting ")).append(Component.text(GcMonitoringModule.DF.format(data.getGcInfo().getDuration()), NamedTextColor.GOLD)).append(Component.text(" ms." + gcCause)).build()));
                for (Entry<String, MemoryUsage> entry : afterUsages.entrySet()) {
                    String type = (String) entry.getKey();
                    MemoryUsage after = (MemoryUsage) entry.getValue();
                    MemoryUsage before = (MemoryUsage) beforeUsages.get(type);
                    if (before != null) {
                        long diff = before.getUsed() - after.getUsed();
                        if (diff != 0L) {
                            if (diff > 0L) {
                                report.add(Component.text().content("  ").append(Component.text(FormatUtil.formatBytes(diff), NamedTextColor.GOLD)).append(Component.text(" freed from ", NamedTextColor.DARK_GRAY)).append(Component.text(type, NamedTextColor.GRAY)).build());
                                report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(before.getUsed()), NamedTextColor.GRAY)).append(Component.text(" → ", NamedTextColor.DARK_GRAY)).append(Component.text(FormatUtil.formatBytes(after.getUsed()), NamedTextColor.GRAY)).append(Component.space()).append(Component.text("(", NamedTextColor.DARK_GRAY)).append(Component.text(FormatUtil.percent((double) diff, (double) before.getUsed()), NamedTextColor.WHITE)).append(Component.text(")", NamedTextColor.DARK_GRAY)).build());
                            } else {
                                report.add(Component.text().content("  ").append(Component.text(FormatUtil.formatBytes(-diff), NamedTextColor.GOLD)).append(Component.text(" moved to ", NamedTextColor.DARK_GRAY)).append(Component.text(type, NamedTextColor.GRAY)).build());
                                report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(before.getUsed()), NamedTextColor.GRAY)).append(Component.text(" → ", NamedTextColor.DARK_GRAY)).append(Component.text(FormatUtil.formatBytes(after.getUsed()), NamedTextColor.GRAY)).build());
                            }
                        }
                    }
                }
                this.resp.broadcast(report);
            });
        }
    }
}