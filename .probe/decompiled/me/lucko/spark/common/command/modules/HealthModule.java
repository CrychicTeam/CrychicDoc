package me.lucko.spark.common.command.modules;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.command.Arguments;
import me.lucko.spark.common.command.Command;
import me.lucko.spark.common.command.CommandModule;
import me.lucko.spark.common.command.CommandResponseHandler;
import me.lucko.spark.common.command.sender.CommandSender;
import me.lucko.spark.common.command.tabcomplete.TabCompleter;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.disk.DiskUsage;
import me.lucko.spark.common.monitor.net.Direction;
import me.lucko.spark.common.monitor.net.NetworkInterfaceAverages;
import me.lucko.spark.common.monitor.net.NetworkMonitor;
import me.lucko.spark.common.monitor.ping.PingStatistics;
import me.lucko.spark.common.monitor.ping.PingSummary;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.common.util.FormatUtil;
import me.lucko.spark.common.util.RollingAverage;
import me.lucko.spark.common.util.StatisticFormatter;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;

public class HealthModule implements CommandModule {

    @Override
    public void registerCommands(Consumer<Command> consumer) {
        consumer.accept(Command.builder().aliases("tps", "cpu").executor(HealthModule::tps).tabCompleter(Command.TabCompleter.empty()).build());
        consumer.accept(Command.builder().aliases("ping").argumentUsage("player", "username").executor(HealthModule::ping).tabCompleter((platform, sender, arguments) -> TabCompleter.completeForOpts(arguments, "--player")).build());
        consumer.accept(Command.builder().aliases("healthreport", "health", "ht").argumentUsage("memory", null).argumentUsage("network", null).executor(HealthModule::healthReport).tabCompleter((platform, sender, arguments) -> TabCompleter.completeForOpts(arguments, "--memory", "--network")).build());
    }

    private static void tps(SparkPlatform platform, CommandSender sender, CommandResponseHandler resp, Arguments arguments) {
        TickStatistics tickStatistics = platform.getTickStatistics();
        if (tickStatistics != null) {
            resp.replyPrefixed(Component.text("TPS from last 5s, 10s, 1m, 5m, 15m:"));
            resp.replyPrefixed(Component.text().content(" ").append(StatisticFormatter.formatTps(tickStatistics.tps5Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps10Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps1Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps5Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps15Min())).build());
            resp.replyPrefixed(Component.empty());
            if (tickStatistics.isDurationSupported()) {
                resp.replyPrefixed(Component.text("Tick durations (min/med/95%ile/max ms) from last 10s, 1m:"));
                resp.replyPrefixed(Component.text().content(" ").append(StatisticFormatter.formatTickDurations(tickStatistics.duration10Sec())).append(Component.text(";  ")).append(StatisticFormatter.formatTickDurations(tickStatistics.duration1Min())).build());
                resp.replyPrefixed(Component.empty());
            }
        }
        resp.replyPrefixed(Component.text("CPU usage from last 10s, 1m, 15m:"));
        resp.replyPrefixed(Component.text().content(" ").append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad15MinAvg())).append(Component.text("  (system)", NamedTextColor.DARK_GRAY)).build());
        resp.replyPrefixed(Component.text().content(" ").append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad15MinAvg())).append(Component.text("  (process)", NamedTextColor.DARK_GRAY)).build());
    }

    private static void ping(SparkPlatform platform, CommandSender sender, CommandResponseHandler resp, Arguments arguments) {
        PingStatistics pingStatistics = platform.getPingStatistics();
        if (pingStatistics == null) {
            resp.replyPrefixed(Component.text("Ping data is not available on this platform."));
        } else {
            Set<String> players = arguments.stringFlag("player");
            if (!players.isEmpty()) {
                for (String player : players) {
                    PingStatistics.PlayerPing playerPing = pingStatistics.query(player);
                    if (playerPing == null) {
                        resp.replyPrefixed(Component.text("Ping data is not available for '" + player + "'."));
                    } else {
                        resp.replyPrefixed(Component.text().content("Player ").append(Component.text(playerPing.name(), NamedTextColor.WHITE)).append(Component.text(" has ")).append(StatisticFormatter.formatPingRtt((double) playerPing.ping())).append(Component.text(" ms ping.")).build());
                    }
                }
            } else {
                PingSummary summary = pingStatistics.currentSummary();
                RollingAverage average = pingStatistics.getPingAverage();
                if (summary.total() == 0 && average.getSamples() == 0) {
                    resp.replyPrefixed(Component.text("There is not enough data to show ping averages yet. Please try again later."));
                } else {
                    resp.replyPrefixed(Component.text("Average Pings (min/med/95%ile/max ms) from now, last 15m:"));
                    resp.replyPrefixed(Component.text().content(" ").append(StatisticFormatter.formatPingRtts((double) summary.min(), summary.median(), summary.percentile95th(), (double) summary.max())).append(Component.text(";  ")).append(StatisticFormatter.formatPingRtts(average.min(), average.median(), average.percentile95th(), average.max())).build());
                }
            }
        }
    }

    private static void healthReport(SparkPlatform platform, CommandSender sender, CommandResponseHandler resp, Arguments arguments) {
        resp.replyPrefixed(Component.text("Generating server health report..."));
        List<Component> report = new LinkedList();
        report.add(Component.empty());
        TickStatistics tickStatistics = platform.getTickStatistics();
        if (tickStatistics != null) {
            addTickStats(report, tickStatistics);
        }
        addCpuStats(report);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        addBasicMemoryStats(report, memoryMXBean);
        if (arguments.boolFlag("memory")) {
            addDetailedMemoryStats(report, memoryMXBean);
        }
        addNetworkStats(report, arguments.boolFlag("network"));
        addDiskStats(report);
        resp.reply(report);
    }

    private static void addTickStats(List<Component> report, TickStatistics tickStatistics) {
        report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("TPS from last 5s, 10s, 1m, 5m, 15m:", NamedTextColor.GOLD)).build());
        report.add(Component.text().content("    ").append(StatisticFormatter.formatTps(tickStatistics.tps5Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps10Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps1Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps5Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps15Min())).build());
        report.add(Component.empty());
        if (tickStatistics.isDurationSupported()) {
            report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Tick durations (min/med/95%ile/max ms) from last 10s, 1m:", NamedTextColor.GOLD)).build());
            report.add(Component.text().content("    ").append(StatisticFormatter.formatTickDurations(tickStatistics.duration10Sec())).append(Component.text("; ")).append(StatisticFormatter.formatTickDurations(tickStatistics.duration1Min())).build());
            report.add(Component.empty());
        }
    }

    private static void addCpuStats(List<Component> report) {
        report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("CPU usage from last 10s, 1m, 15m:", NamedTextColor.GOLD)).build());
        report.add(Component.text().content("    ").append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad15MinAvg())).append(Component.text("  (system)", NamedTextColor.DARK_GRAY)).build());
        report.add(Component.text().content("    ").append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad15MinAvg())).append(Component.text("  (process)", NamedTextColor.DARK_GRAY)).build());
        report.add(Component.empty());
    }

    private static void addBasicMemoryStats(List<Component> report, MemoryMXBean memoryMXBean) {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Memory usage:", NamedTextColor.GOLD)).build());
        report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(heapUsage.getUsed()), NamedTextColor.WHITE)).append(Component.space()).append(Component.text("/", NamedTextColor.GRAY)).append(Component.space()).append(Component.text(FormatUtil.formatBytes(heapUsage.getMax()), NamedTextColor.WHITE)).append(Component.text("   ")).append(Component.text("(", NamedTextColor.GRAY)).append(Component.text(FormatUtil.percent((double) heapUsage.getUsed(), (double) heapUsage.getMax()), NamedTextColor.GREEN)).append(Component.text(")", NamedTextColor.GRAY)).build());
        report.add(Component.text().content("    ").append(StatisticFormatter.generateMemoryUsageDiagram(heapUsage, 60)).build());
        report.add(Component.empty());
    }

    private static void addDetailedMemoryStats(List<Component> report, MemoryMXBean memoryMXBean) {
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();
        report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Non-heap memory usage:", NamedTextColor.GOLD)).build());
        report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(nonHeapUsage.getUsed()), NamedTextColor.WHITE)).build());
        report.add(Component.empty());
        for (MemoryPoolMXBean memoryPool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (memoryPool.getType() == MemoryType.HEAP) {
                MemoryUsage usage = memoryPool.getUsage();
                MemoryUsage collectionUsage = memoryPool.getCollectionUsage();
                if (usage.getMax() == -1L) {
                    usage = new MemoryUsage(usage.getInit(), usage.getUsed(), usage.getCommitted(), usage.getCommitted());
                }
                report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text(memoryPool.getName() + " pool usage:", NamedTextColor.GOLD)).build());
                report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(usage.getUsed()), NamedTextColor.WHITE)).append(Component.space()).append(Component.text("/", NamedTextColor.GRAY)).append(Component.space()).append(Component.text(FormatUtil.formatBytes(usage.getMax()), NamedTextColor.WHITE)).append(Component.text("   ")).append(Component.text("(", NamedTextColor.GRAY)).append(Component.text(FormatUtil.percent((double) usage.getUsed(), (double) usage.getMax()), NamedTextColor.GREEN)).append(Component.text(")", NamedTextColor.GRAY)).build());
                report.add(Component.text().content("    ").append(StatisticFormatter.generateMemoryPoolDiagram(usage, collectionUsage, 60)).build());
                if (collectionUsage != null) {
                    report.add(Component.text().content("     ").append(Component.text("-", NamedTextColor.RED)).append(Component.space()).append(Component.text("Usage at last GC:", NamedTextColor.GRAY)).append(Component.space()).append(Component.text(FormatUtil.formatBytes(collectionUsage.getUsed()), NamedTextColor.WHITE)).build());
                }
                report.add(Component.empty());
            }
        }
    }

    private static void addNetworkStats(List<Component> report, boolean detailed) {
        List<Component> averagesReport = new LinkedList();
        for (Entry<String, NetworkInterfaceAverages> ent : NetworkMonitor.systemAverages().entrySet()) {
            String interfaceName = (String) ent.getKey();
            NetworkInterfaceAverages averages = (NetworkInterfaceAverages) ent.getValue();
            for (Direction direction : Direction.values()) {
                long bytesPerSec = (long) averages.bytesPerSecond(direction).mean();
                long packetsPerSec = (long) averages.packetsPerSecond(direction).mean();
                if (detailed || bytesPerSec > 0L || packetsPerSec > 0L) {
                    averagesReport.add(Component.text().color(NamedTextColor.GRAY).content("    ").append(FormatUtil.formatBytes(bytesPerSec, NamedTextColor.GREEN, "/s")).append(Component.text(" / ")).append(Component.text(String.format(Locale.ENGLISH, "%,d", packetsPerSec), NamedTextColor.WHITE)).append(Component.text(" pps ")).append(Component.text().color(NamedTextColor.DARK_GRAY).append(Component.text('(')).append(Component.text(interfaceName + " " + direction.abbrev(), NamedTextColor.WHITE)).append(Component.text(')'))).build());
                }
            }
        }
        if (!averagesReport.isEmpty()) {
            report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Network usage: (system, last 15m)", NamedTextColor.GOLD)).build());
            report.addAll(averagesReport);
            report.add(Component.empty());
        }
    }

    private static void addDiskStats(List<Component> report) {
        long total = DiskUsage.getTotal();
        long used = DiskUsage.getUsed();
        if (total != 0L && used != 0L) {
            report.add(Component.text().append(Component.text(">", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)).append(Component.space()).append(Component.text("Disk usage:", NamedTextColor.GOLD)).build());
            report.add(Component.text().content("    ").append(Component.text(FormatUtil.formatBytes(used), NamedTextColor.WHITE)).append(Component.space()).append(Component.text("/", NamedTextColor.GRAY)).append(Component.space()).append(Component.text(FormatUtil.formatBytes(total), NamedTextColor.WHITE)).append(Component.text("   ")).append(Component.text("(", NamedTextColor.GRAY)).append(Component.text(FormatUtil.percent((double) used, (double) total), NamedTextColor.GREEN)).append(Component.text(")", NamedTextColor.GRAY)).build());
            report.add(Component.text().content("    ").append(StatisticFormatter.generateDiskUsageDiagram((double) used, (double) total, 60)).build());
            report.add(Component.empty());
        }
    }
}