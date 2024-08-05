package me.lucko.spark.common.platform;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.util.Map;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.common.SparkPlatform;
import me.lucko.spark.common.monitor.cpu.CpuInfo;
import me.lucko.spark.common.monitor.cpu.CpuMonitor;
import me.lucko.spark.common.monitor.disk.DiskUsage;
import me.lucko.spark.common.monitor.memory.GarbageCollectorStatistics;
import me.lucko.spark.common.monitor.memory.MemoryInfo;
import me.lucko.spark.common.monitor.net.NetworkInterfaceAverages;
import me.lucko.spark.common.monitor.net.NetworkMonitor;
import me.lucko.spark.common.monitor.os.OperatingSystemInfo;
import me.lucko.spark.common.monitor.ping.PingStatistics;
import me.lucko.spark.common.monitor.tick.TickStatistics;
import me.lucko.spark.common.platform.world.AsyncWorldInfoProvider;
import me.lucko.spark.common.platform.world.WorldStatisticsProvider;
import me.lucko.spark.proto.SparkProtos;

public class PlatformStatisticsProvider {

    private final SparkPlatform platform;

    public PlatformStatisticsProvider(SparkPlatform platform) {
        this.platform = platform;
    }

    public SparkProtos.SystemStatistics getSystemStatistics() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        OperatingSystemInfo osInfo = OperatingSystemInfo.poll();
        SparkProtos.SystemStatistics.Builder builder = SparkProtos.SystemStatistics.newBuilder().setCpu(SparkProtos.SystemStatistics.Cpu.newBuilder().setThreads(Runtime.getRuntime().availableProcessors()).setProcessUsage(SparkProtos.SystemStatistics.Cpu.Usage.newBuilder().setLast1M(CpuMonitor.processLoad1MinAvg()).setLast15M(CpuMonitor.processLoad15MinAvg()).build()).setSystemUsage(SparkProtos.SystemStatistics.Cpu.Usage.newBuilder().setLast1M(CpuMonitor.systemLoad1MinAvg()).setLast15M(CpuMonitor.systemLoad15MinAvg()).build()).setModelName(CpuInfo.queryCpuModel()).build()).setMemory(SparkProtos.SystemStatistics.Memory.newBuilder().setPhysical(SparkProtos.SystemStatistics.Memory.MemoryPool.newBuilder().setUsed(MemoryInfo.getUsedPhysicalMemory()).setTotal(MemoryInfo.getTotalPhysicalMemory()).build()).setSwap(SparkProtos.SystemStatistics.Memory.MemoryPool.newBuilder().setUsed(MemoryInfo.getUsedSwap()).setTotal(MemoryInfo.getTotalSwap()).build()).build()).setDisk(SparkProtos.SystemStatistics.Disk.newBuilder().setTotal(DiskUsage.getTotal()).setUsed(DiskUsage.getUsed()).build()).setOs(SparkProtos.SystemStatistics.Os.newBuilder().setArch(osInfo.arch()).setName(osInfo.name()).setVersion(osInfo.version()).build()).setJava(SparkProtos.SystemStatistics.Java.newBuilder().setVendor(System.getProperty("java.vendor", "unknown")).setVersion(System.getProperty("java.version", "unknown")).setVendorVersion(System.getProperty("java.vendor.version", "unknown")).setVmArgs(String.join(" ", runtimeBean.getInputArguments())).build());
        long uptime = runtimeBean.getUptime();
        builder.setUptime(uptime);
        Map<String, GarbageCollectorStatistics> gcStats = GarbageCollectorStatistics.pollStats();
        gcStats.forEach((name, statistics) -> builder.putGc(name, SparkProtos.SystemStatistics.Gc.newBuilder().setTotal(statistics.getCollectionCount()).setAvgTime(statistics.getAverageCollectionTime()).setAvgFrequency((double) statistics.getAverageCollectionFrequency(uptime)).build()));
        Map<String, NetworkInterfaceAverages> networkInterfaceStats = NetworkMonitor.systemAverages();
        networkInterfaceStats.forEach((name, statistics) -> builder.putNet(name, SparkProtos.SystemStatistics.NetInterface.newBuilder().setRxBytesPerSecond(rollingAvgProto(statistics.rxBytesPerSecond())).setRxPacketsPerSecond(rollingAvgProto(statistics.rxPacketsPerSecond())).setTxBytesPerSecond(rollingAvgProto(statistics.txBytesPerSecond())).setTxPacketsPerSecond(rollingAvgProto(statistics.txPacketsPerSecond())).build()));
        return builder.build();
    }

    public SparkProtos.PlatformStatistics getPlatformStatistics(Map<String, GarbageCollectorStatistics> startingGcStatistics, boolean includeWorld) {
        SparkProtos.PlatformStatistics.Builder builder = SparkProtos.PlatformStatistics.newBuilder();
        MemoryUsage memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        builder.setMemory(SparkProtos.PlatformStatistics.Memory.newBuilder().setHeap(SparkProtos.PlatformStatistics.Memory.MemoryPool.newBuilder().setUsed(memoryUsage.getUsed()).setTotal(memoryUsage.getCommitted()).build()).build());
        long uptime = System.currentTimeMillis() - this.platform.getServerNormalOperationStartTime();
        builder.setUptime(uptime);
        if (startingGcStatistics != null) {
            Map<String, GarbageCollectorStatistics> gcStats = GarbageCollectorStatistics.pollStatsSubtractInitial(startingGcStatistics);
            gcStats.forEach((name, statistics) -> builder.putGc(name, SparkProtos.PlatformStatistics.Gc.newBuilder().setTotal(statistics.getCollectionCount()).setAvgTime(statistics.getAverageCollectionTime()).setAvgFrequency((double) statistics.getAverageCollectionFrequency(uptime)).build()));
        }
        TickStatistics tickStatistics = this.platform.getTickStatistics();
        if (tickStatistics != null) {
            builder.setTps(SparkProtos.PlatformStatistics.Tps.newBuilder().setLast1M(tickStatistics.tps1Min()).setLast5M(tickStatistics.tps5Min()).setLast15M(tickStatistics.tps15Min()).build());
            if (tickStatistics.isDurationSupported()) {
                builder.setMspt(SparkProtos.PlatformStatistics.Mspt.newBuilder().setLast1M(rollingAvgProto(tickStatistics.duration1Min())).setLast5M(rollingAvgProto(tickStatistics.duration5Min())).build());
            }
        }
        PingStatistics pingStatistics = this.platform.getPingStatistics();
        if (pingStatistics != null && pingStatistics.getPingAverage().getSamples() != 0) {
            builder.setPing(SparkProtos.PlatformStatistics.Ping.newBuilder().setLast15M(rollingAvgProto(pingStatistics.getPingAverage())).build());
        }
        PlatformInfo.Type platformType = this.platform.getPlugin().getPlatformInfo().getType();
        if (platformType != PlatformInfo.Type.CLIENT) {
            long playerCount = this.platform.getPlugin().getCommandSenders().count() - 1L;
            builder.setPlayerCount(playerCount);
        }
        if (includeWorld) {
            try {
                WorldStatisticsProvider worldStatisticsProvider = new WorldStatisticsProvider(new AsyncWorldInfoProvider(this.platform, this.platform.getPlugin().createWorldInfoProvider()));
                SparkProtos.WorldStatistics worldStatistics = worldStatisticsProvider.getWorldStatistics();
                if (worldStatistics != null) {
                    builder.setWorld(worldStatistics);
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        }
        return builder.build();
    }

    public static SparkProtos.RollingAverageValues rollingAvgProto(DoubleAverageInfo info) {
        return SparkProtos.RollingAverageValues.newBuilder().setMean(info.mean()).setMax(info.max()).setMin(info.min()).setMedian(info.median()).setPercentile95(info.percentile95th()).build();
    }
}