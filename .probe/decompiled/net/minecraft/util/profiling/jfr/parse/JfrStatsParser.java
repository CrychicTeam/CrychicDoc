package net.minecraft.util.profiling.jfr.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
import net.minecraft.util.profiling.jfr.stats.FileIOStat;
import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
import net.minecraft.util.profiling.jfr.stats.NetworkPacketSummary;
import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
import net.minecraft.util.profiling.jfr.stats.TickTimeStat;

public class JfrStatsParser {

    private Instant recordingStarted = Instant.EPOCH;

    private Instant recordingEnded = Instant.EPOCH;

    private final List<ChunkGenStat> chunkGenStats = Lists.newArrayList();

    private final List<CpuLoadStat> cpuLoadStat = Lists.newArrayList();

    private final Map<NetworkPacketSummary.PacketIdentification, JfrStatsParser.MutableCountAndSize> receivedPackets = Maps.newHashMap();

    private final Map<NetworkPacketSummary.PacketIdentification, JfrStatsParser.MutableCountAndSize> sentPackets = Maps.newHashMap();

    private final List<FileIOStat> fileWrites = Lists.newArrayList();

    private final List<FileIOStat> fileReads = Lists.newArrayList();

    private int garbageCollections;

    private Duration gcTotalDuration = Duration.ZERO;

    private final List<GcHeapStat> gcHeapStats = Lists.newArrayList();

    private final List<ThreadAllocationStat> threadAllocationStats = Lists.newArrayList();

    private final List<TickTimeStat> tickTimes = Lists.newArrayList();

    @Nullable
    private Duration worldCreationDuration = null;

    private JfrStatsParser(Stream<RecordedEvent> streamRecordedEvent0) {
        this.capture(streamRecordedEvent0);
    }

    public static JfrStatsResult parse(Path path0) {
        try {
            final RecordingFile $$1 = new RecordingFile(path0);
            JfrStatsResult var4;
            try {
                Iterator<RecordedEvent> $$2 = new Iterator<RecordedEvent>() {

                    public boolean hasNext() {
                        return $$1.hasMoreEvents();
                    }

                    public RecordedEvent next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        } else {
                            try {
                                return $$1.readEvent();
                            } catch (IOException var2) {
                                throw new UncheckedIOException(var2);
                            }
                        }
                    }
                };
                Stream<RecordedEvent> $$3 = StreamSupport.stream(Spliterators.spliteratorUnknownSize($$2, 1297), false);
                var4 = new JfrStatsParser($$3).results();
            } catch (Throwable var6) {
                try {
                    $$1.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            $$1.close();
            return var4;
        } catch (IOException var7) {
            throw new UncheckedIOException(var7);
        }
    }

    private JfrStatsResult results() {
        Duration $$0 = Duration.between(this.recordingStarted, this.recordingEnded);
        return new JfrStatsResult(this.recordingStarted, this.recordingEnded, $$0, this.worldCreationDuration, this.tickTimes, this.cpuLoadStat, GcHeapStat.summary($$0, this.gcHeapStats, this.gcTotalDuration, this.garbageCollections), ThreadAllocationStat.summary(this.threadAllocationStats), collectPacketStats($$0, this.receivedPackets), collectPacketStats($$0, this.sentPackets), FileIOStat.summary($$0, this.fileWrites), FileIOStat.summary($$0, this.fileReads), this.chunkGenStats);
    }

    private void capture(Stream<RecordedEvent> streamRecordedEvent0) {
        streamRecordedEvent0.forEach(p_185457_ -> {
            if (p_185457_.getEndTime().isAfter(this.recordingEnded) || this.recordingEnded.equals(Instant.EPOCH)) {
                this.recordingEnded = p_185457_.getEndTime();
            }
            if (p_185457_.getStartTime().isBefore(this.recordingStarted) || this.recordingStarted.equals(Instant.EPOCH)) {
                this.recordingStarted = p_185457_.getStartTime();
            }
            String var2 = p_185457_.getEventType().getName();
            switch(var2) {
                case "minecraft.ChunkGeneration":
                    this.chunkGenStats.add(ChunkGenStat.from(p_185457_));
                    break;
                case "minecraft.LoadWorld":
                    this.worldCreationDuration = p_185457_.getDuration();
                    break;
                case "minecraft.ServerTickTime":
                    this.tickTimes.add(TickTimeStat.from(p_185457_));
                    break;
                case "minecraft.PacketReceived":
                    this.incrementPacket(p_185457_, p_185457_.getInt("bytes"), this.receivedPackets);
                    break;
                case "minecraft.PacketSent":
                    this.incrementPacket(p_185457_, p_185457_.getInt("bytes"), this.sentPackets);
                    break;
                case "jdk.ThreadAllocationStatistics":
                    this.threadAllocationStats.add(ThreadAllocationStat.from(p_185457_));
                    break;
                case "jdk.GCHeapSummary":
                    this.gcHeapStats.add(GcHeapStat.from(p_185457_));
                    break;
                case "jdk.CPULoad":
                    this.cpuLoadStat.add(CpuLoadStat.from(p_185457_));
                    break;
                case "jdk.FileWrite":
                    this.appendFileIO(p_185457_, this.fileWrites, "bytesWritten");
                    break;
                case "jdk.FileRead":
                    this.appendFileIO(p_185457_, this.fileReads, "bytesRead");
                    break;
                case "jdk.GarbageCollection":
                    this.garbageCollections++;
                    this.gcTotalDuration = this.gcTotalDuration.plus(p_185457_.getDuration());
            }
        });
    }

    private void incrementPacket(RecordedEvent recordedEvent0, int int1, Map<NetworkPacketSummary.PacketIdentification, JfrStatsParser.MutableCountAndSize> mapNetworkPacketSummaryPacketIdentificationJfrStatsParserMutableCountAndSize2) {
        ((JfrStatsParser.MutableCountAndSize) mapNetworkPacketSummaryPacketIdentificationJfrStatsParserMutableCountAndSize2.computeIfAbsent(NetworkPacketSummary.PacketIdentification.from(recordedEvent0), p_185446_ -> new JfrStatsParser.MutableCountAndSize())).increment(int1);
    }

    private void appendFileIO(RecordedEvent recordedEvent0, List<FileIOStat> listFileIOStat1, String string2) {
        listFileIOStat1.add(new FileIOStat(recordedEvent0.getDuration(), recordedEvent0.getString("path"), recordedEvent0.getLong(string2)));
    }

    private static NetworkPacketSummary collectPacketStats(Duration duration0, Map<NetworkPacketSummary.PacketIdentification, JfrStatsParser.MutableCountAndSize> mapNetworkPacketSummaryPacketIdentificationJfrStatsParserMutableCountAndSize1) {
        List<Pair<NetworkPacketSummary.PacketIdentification, NetworkPacketSummary.PacketCountAndSize>> $$2 = mapNetworkPacketSummaryPacketIdentificationJfrStatsParserMutableCountAndSize1.entrySet().stream().map(p_185453_ -> Pair.of((NetworkPacketSummary.PacketIdentification) p_185453_.getKey(), ((JfrStatsParser.MutableCountAndSize) p_185453_.getValue()).toCountAndSize())).toList();
        return new NetworkPacketSummary(duration0, $$2);
    }

    public static final class MutableCountAndSize {

        private long count;

        private long totalSize;

        public void increment(int int0) {
            this.totalSize += (long) int0;
            this.count++;
        }

        public NetworkPacketSummary.PacketCountAndSize toCountAndSize() {
            return new NetworkPacketSummary.PacketCountAndSize(this.count, this.totalSize);
        }
    }
}