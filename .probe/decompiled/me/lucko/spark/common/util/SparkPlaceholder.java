package me.lucko.spark.common.util;

import java.util.Locale;
import java.util.function.BiFunction;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.serializer.legacy.LegacyComponentSerializer;

public enum SparkPlaceholder {

    TPS((platform, arg) -> {
        TickStatistics tickStatistics = platform.getTickStatistics();
        if (tickStatistics == null) {
            return null;
        } else if (arg == null) {
            return Component.text().append(StatisticFormatter.formatTps(tickStatistics.tps5Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps10Sec())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps1Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps5Min())).append(Component.text(", ")).append(StatisticFormatter.formatTps(tickStatistics.tps15Min())).build();
        } else {
            switch(arg) {
                case "5s":
                    return StatisticFormatter.formatTps(tickStatistics.tps5Sec());
                case "10s":
                    return StatisticFormatter.formatTps(tickStatistics.tps10Sec());
                case "1m":
                    return StatisticFormatter.formatTps(tickStatistics.tps1Min());
                case "5m":
                    return StatisticFormatter.formatTps(tickStatistics.tps5Min());
                case "15m":
                    return StatisticFormatter.formatTps(tickStatistics.tps15Min());
                default:
                    return null;
            }
        }
    }), TICKDURATION((platform, arg) -> {
        TickStatistics tickStatistics = platform.getTickStatistics();
        if (tickStatistics == null || !tickStatistics.isDurationSupported()) {
            return null;
        } else if (arg == null) {
            return Component.text().append(StatisticFormatter.formatTickDurations(tickStatistics.duration10Sec())).append(Component.text(";  ")).append(StatisticFormatter.formatTickDurations(tickStatistics.duration1Min())).build();
        } else {
            switch(arg) {
                case "10s":
                    return StatisticFormatter.formatTickDurations(tickStatistics.duration10Sec());
                case "1m":
                    return StatisticFormatter.formatTickDurations(tickStatistics.duration1Min());
                default:
                    return null;
            }
        }
    }), CPU_SYSTEM((platform, arg) -> {
        if (arg == null) {
            return Component.text().append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad15MinAvg())).build();
        } else {
            switch(arg) {
                case "10s":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad10SecAvg());
                case "1m":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad1MinAvg());
                case "15m":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.systemLoad15MinAvg());
                default:
                    return null;
            }
        }
    }), CPU_PROCESS((platform, arg) -> {
        if (arg == null) {
            return Component.text().append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad10SecAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad1MinAvg())).append(Component.text(", ")).append(StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad15MinAvg())).build();
        } else {
            switch(arg) {
                case "10s":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad10SecAvg());
                case "1m":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad1MinAvg());
                case "15m":
                    return StatisticFormatter.formatCpuUsage(CpuMonitor.processLoad15MinAvg());
                default:
                    return null;
            }
        }
    });

    private final String name = this.name().toLowerCase(Locale.ROOT);

    private final BiFunction<SparkPlatform, String, TextComponent> function;

    private SparkPlaceholder(BiFunction<SparkPlatform, String, TextComponent> function) {
        this.function = function;
    }

    public String getName() {
        return this.name;
    }

    public TextComponent resolve(SparkPlatform platform, String arg) {
        return (TextComponent) this.function.apply(platform, arg);
    }

    public static TextComponent resolveComponent(SparkPlatform platform, String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length == 0) {
            return null;
        } else {
            String label = parts[0];
            if (label.equals("tps")) {
                String arg = parts.length < 2 ? null : parts[1];
                return TPS.resolve(platform, arg);
            } else if (label.equals("tickduration")) {
                String arg = parts.length < 2 ? null : parts[1];
                return TICKDURATION.resolve(platform, arg);
            } else {
                if (label.equals("cpu") && parts.length >= 2) {
                    String type = parts[1];
                    String arg = parts.length < 3 ? null : parts[2];
                    if (type.equals("system")) {
                        return CPU_SYSTEM.resolve(platform, arg);
                    }
                    if (type.equals("process")) {
                        return CPU_PROCESS.resolve(platform, arg);
                    }
                }
                return null;
            }
        }
    }

    public static String resolveFormattingCode(SparkPlatform platform, String placeholder) {
        TextComponent result = resolveComponent(platform, placeholder);
        return result == null ? null : LegacyComponentSerializer.legacySection().serialize(result);
    }
}