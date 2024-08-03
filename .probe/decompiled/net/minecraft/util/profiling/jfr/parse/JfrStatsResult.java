package net.minecraft.util.profiling.jfr.parse;

import com.mojang.datafixers.util.Pair;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.util.profiling.jfr.serialize.JfrResultJsonSerializer;
import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
import net.minecraft.util.profiling.jfr.stats.FileIOStat;
import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
import net.minecraft.util.profiling.jfr.stats.NetworkPacketSummary;
import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
import net.minecraft.world.level.chunk.ChunkStatus;

public record JfrStatsResult(Instant f_185478_, Instant f_185479_, Duration f_185480_, @Nullable Duration f_185481_, List<TickTimeStat> f_185482_, List<CpuLoadStat> f_185483_, GcHeapStat.Summary f_185484_, ThreadAllocationStat.Summary f_185485_, NetworkPacketSummary f_185486_, NetworkPacketSummary f_185487_, FileIOStat.Summary f_185488_, FileIOStat.Summary f_185489_, List<ChunkGenStat> f_185490_) {

    private final Instant recordingStarted;

    private final Instant recordingEnded;

    private final Duration recordingDuration;

    @Nullable
    private final Duration worldCreationDuration;

    private final List<TickTimeStat> tickTimes;

    private final List<CpuLoadStat> cpuLoadStats;

    private final GcHeapStat.Summary heapSummary;

    private final ThreadAllocationStat.Summary threadAllocationSummary;

    private final NetworkPacketSummary receivedPacketsSummary;

    private final NetworkPacketSummary sentPacketsSummary;

    private final FileIOStat.Summary fileWrites;

    private final FileIOStat.Summary fileReads;

    private final List<ChunkGenStat> chunkGenStats;

    public JfrStatsResult(Instant f_185478_, Instant f_185479_, Duration f_185480_, @Nullable Duration f_185481_, List<TickTimeStat> f_185482_, List<CpuLoadStat> f_185483_, GcHeapStat.Summary f_185484_, ThreadAllocationStat.Summary f_185485_, NetworkPacketSummary f_185486_, NetworkPacketSummary f_185487_, FileIOStat.Summary f_185488_, FileIOStat.Summary f_185489_, List<ChunkGenStat> f_185490_) {
        this.recordingStarted = f_185478_;
        this.recordingEnded = f_185479_;
        this.recordingDuration = f_185480_;
        this.worldCreationDuration = f_185481_;
        this.tickTimes = f_185482_;
        this.cpuLoadStats = f_185483_;
        this.heapSummary = f_185484_;
        this.threadAllocationSummary = f_185485_;
        this.receivedPacketsSummary = f_185486_;
        this.sentPacketsSummary = f_185487_;
        this.fileWrites = f_185488_;
        this.fileReads = f_185489_;
        this.chunkGenStats = f_185490_;
    }

    public List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary() {
        Map<ChunkStatus, List<ChunkGenStat>> $$0 = (Map<ChunkStatus, List<ChunkGenStat>>) this.chunkGenStats.stream().collect(Collectors.groupingBy(ChunkGenStat::f_185595_));
        return $$0.entrySet().stream().map(p_185509_ -> Pair.of((ChunkStatus) p_185509_.getKey(), TimedStatSummary.summary((List) p_185509_.getValue()))).sorted(Comparator.comparing(p_185507_ -> ((TimedStatSummary) p_185507_.getSecond()).totalDuration()).reversed()).toList();
    }

    public String asJson() {
        return new JfrResultJsonSerializer().format(this);
    }
}