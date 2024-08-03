package net.minecraft.util.profiling.jfr.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.LongSerializationPolicy;
import com.mojang.datafixers.util.Pair;
import java.time.Duration;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;
import net.minecraft.Util;
import net.minecraft.util.profiling.jfr.Percentiles;
import net.minecraft.util.profiling.jfr.parse.JfrStatsResult;
import net.minecraft.util.profiling.jfr.stats.ChunkGenStat;
import net.minecraft.util.profiling.jfr.stats.CpuLoadStat;
import net.minecraft.util.profiling.jfr.stats.FileIOStat;
import net.minecraft.util.profiling.jfr.stats.GcHeapStat;
import net.minecraft.util.profiling.jfr.stats.NetworkPacketSummary;
import net.minecraft.util.profiling.jfr.stats.ThreadAllocationStat;
import net.minecraft.util.profiling.jfr.stats.TickTimeStat;
import net.minecraft.util.profiling.jfr.stats.TimedStatSummary;
import net.minecraft.world.level.chunk.ChunkStatus;

public class JfrResultJsonSerializer {

    private static final String BYTES_PER_SECOND = "bytesPerSecond";

    private static final String COUNT = "count";

    private static final String DURATION_NANOS_TOTAL = "durationNanosTotal";

    private static final String TOTAL_BYTES = "totalBytes";

    private static final String COUNT_PER_SECOND = "countPerSecond";

    final Gson gson = new GsonBuilder().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.DEFAULT).create();

    public String format(JfrStatsResult jfrStatsResult0) {
        JsonObject $$1 = new JsonObject();
        $$1.addProperty("startedEpoch", jfrStatsResult0.recordingStarted().toEpochMilli());
        $$1.addProperty("endedEpoch", jfrStatsResult0.recordingEnded().toEpochMilli());
        $$1.addProperty("durationMs", jfrStatsResult0.recordingDuration().toMillis());
        Duration $$2 = jfrStatsResult0.worldCreationDuration();
        if ($$2 != null) {
            $$1.addProperty("worldGenDurationMs", $$2.toMillis());
        }
        $$1.add("heap", this.heap(jfrStatsResult0.heapSummary()));
        $$1.add("cpuPercent", this.cpu(jfrStatsResult0.cpuLoadStats()));
        $$1.add("network", this.network(jfrStatsResult0));
        $$1.add("fileIO", this.fileIO(jfrStatsResult0));
        $$1.add("serverTick", this.serverTicks(jfrStatsResult0.tickTimes()));
        $$1.add("threadAllocation", this.threadAllocations(jfrStatsResult0.threadAllocationSummary()));
        $$1.add("chunkGen", this.chunkGen(jfrStatsResult0.chunkGenSummary()));
        return this.gson.toJson($$1);
    }

    private JsonElement heap(GcHeapStat.Summary gcHeapStatSummary0) {
        JsonObject $$1 = new JsonObject();
        $$1.addProperty("allocationRateBytesPerSecond", gcHeapStatSummary0.allocationRateBytesPerSecond());
        $$1.addProperty("gcCount", gcHeapStatSummary0.totalGCs());
        $$1.addProperty("gcOverHeadPercent", gcHeapStatSummary0.gcOverHead());
        $$1.addProperty("gcTotalDurationMs", gcHeapStatSummary0.gcTotalDuration().toMillis());
        return $$1;
    }

    private JsonElement chunkGen(List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> listPairChunkStatusTimedStatSummaryChunkGenStat0) {
        JsonObject $$1 = new JsonObject();
        $$1.addProperty("durationNanosTotal", listPairChunkStatusTimedStatSummaryChunkGenStat0.stream().mapToDouble(p_185567_ -> (double) ((TimedStatSummary) p_185567_.getSecond()).totalDuration().toNanos()).sum());
        JsonArray $$2 = Util.make(new JsonArray(), p_185558_ -> $$1.add("status", p_185558_));
        for (Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>> $$3 : listPairChunkStatusTimedStatSummaryChunkGenStat0) {
            TimedStatSummary<ChunkGenStat> $$4 = (TimedStatSummary<ChunkGenStat>) $$3.getSecond();
            JsonObject $$5 = Util.make(new JsonObject(), $$2::add);
            $$5.addProperty("state", ((ChunkStatus) $$3.getFirst()).toString());
            $$5.addProperty("count", $$4.count());
            $$5.addProperty("durationNanosTotal", $$4.totalDuration().toNanos());
            $$5.addProperty("durationNanosAvg", $$4.totalDuration().toNanos() / (long) $$4.count());
            JsonObject $$6 = Util.make(new JsonObject(), p_185561_ -> $$5.add("durationNanosPercentiles", p_185561_));
            $$4.percentilesNanos().forEach((p_185584_, p_185585_) -> $$6.addProperty("p" + p_185584_, p_185585_));
            Function<ChunkGenStat, JsonElement> $$7 = p_185538_ -> {
                JsonObject $$1x = new JsonObject();
                $$1x.addProperty("durationNanos", p_185538_.duration().toNanos());
                $$1x.addProperty("level", p_185538_.level());
                $$1x.addProperty("chunkPosX", p_185538_.chunkPos().x);
                $$1x.addProperty("chunkPosZ", p_185538_.chunkPos().z);
                $$1x.addProperty("worldPosX", p_185538_.worldPos().x());
                $$1x.addProperty("worldPosZ", p_185538_.worldPos().z());
                return $$1x;
            };
            $$5.add("fastest", (JsonElement) $$7.apply($$4.fastest()));
            $$5.add("slowest", (JsonElement) $$7.apply($$4.slowest()));
            $$5.add("secondSlowest", (JsonElement) ($$4.secondSlowest() != null ? (JsonElement) $$7.apply($$4.secondSlowest()) : JsonNull.INSTANCE));
        }
        return $$1;
    }

    private JsonElement threadAllocations(ThreadAllocationStat.Summary threadAllocationStatSummary0) {
        JsonArray $$1 = new JsonArray();
        threadAllocationStatSummary0.allocationsPerSecondByThread().forEach((p_185554_, p_185555_) -> $$1.add(Util.make(new JsonObject(), p_185571_ -> {
            p_185571_.addProperty("thread", p_185554_);
            p_185571_.addProperty("bytesPerSecond", p_185555_);
        })));
        return $$1;
    }

    private JsonElement serverTicks(List<TickTimeStat> listTickTimeStat0) {
        if (listTickTimeStat0.isEmpty()) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$1 = new JsonObject();
            double[] $$2 = listTickTimeStat0.stream().mapToDouble(p_185548_ -> (double) p_185548_.currentAverage().toNanos() / 1000000.0).toArray();
            DoubleSummaryStatistics $$3 = DoubleStream.of($$2).summaryStatistics();
            $$1.addProperty("minMs", $$3.getMin());
            $$1.addProperty("averageMs", $$3.getAverage());
            $$1.addProperty("maxMs", $$3.getMax());
            Map<Integer, Double> $$4 = Percentiles.evaluate($$2);
            $$4.forEach((p_185564_, p_185565_) -> $$1.addProperty("p" + p_185564_, p_185565_));
            return $$1;
        }
    }

    private JsonElement fileIO(JfrStatsResult jfrStatsResult0) {
        JsonObject $$1 = new JsonObject();
        $$1.add("write", this.fileIoSummary(jfrStatsResult0.fileWrites()));
        $$1.add("read", this.fileIoSummary(jfrStatsResult0.fileReads()));
        return $$1;
    }

    private JsonElement fileIoSummary(FileIOStat.Summary fileIOStatSummary0) {
        JsonObject $$1 = new JsonObject();
        $$1.addProperty("totalBytes", fileIOStatSummary0.totalBytes());
        $$1.addProperty("count", fileIOStatSummary0.counts());
        $$1.addProperty("bytesPerSecond", fileIOStatSummary0.bytesPerSecond());
        $$1.addProperty("countPerSecond", fileIOStatSummary0.countsPerSecond());
        JsonArray $$2 = new JsonArray();
        $$1.add("topContributors", $$2);
        fileIOStatSummary0.topTenContributorsByTotalBytes().forEach(p_185581_ -> {
            JsonObject $$2x = new JsonObject();
            $$2.add($$2x);
            $$2x.addProperty("path", (String) p_185581_.getFirst());
            $$2x.addProperty("totalBytes", (Number) p_185581_.getSecond());
        });
        return $$1;
    }

    private JsonElement network(JfrStatsResult jfrStatsResult0) {
        JsonObject $$1 = new JsonObject();
        $$1.add("sent", this.packets(jfrStatsResult0.sentPacketsSummary()));
        $$1.add("received", this.packets(jfrStatsResult0.receivedPacketsSummary()));
        return $$1;
    }

    private JsonElement packets(NetworkPacketSummary networkPacketSummary0) {
        JsonObject $$1 = new JsonObject();
        $$1.addProperty("totalBytes", networkPacketSummary0.getTotalSize());
        $$1.addProperty("count", networkPacketSummary0.getTotalCount());
        $$1.addProperty("bytesPerSecond", networkPacketSummary0.getSizePerSecond());
        $$1.addProperty("countPerSecond", networkPacketSummary0.getCountsPerSecond());
        JsonArray $$2 = new JsonArray();
        $$1.add("topContributors", $$2);
        networkPacketSummary0.largestSizeContributors().forEach(p_185551_ -> {
            JsonObject $$2x = new JsonObject();
            $$2.add($$2x);
            NetworkPacketSummary.PacketIdentification $$3 = (NetworkPacketSummary.PacketIdentification) p_185551_.getFirst();
            NetworkPacketSummary.PacketCountAndSize $$4 = (NetworkPacketSummary.PacketCountAndSize) p_185551_.getSecond();
            $$2x.addProperty("protocolId", $$3.protocolId());
            $$2x.addProperty("packetId", $$3.packetId());
            $$2x.addProperty("packetName", $$3.packetName());
            $$2x.addProperty("totalBytes", $$4.totalSize());
            $$2x.addProperty("count", $$4.totalCount());
        });
        return $$1;
    }

    private JsonElement cpu(List<CpuLoadStat> listCpuLoadStat0) {
        JsonObject $$1 = new JsonObject();
        BiFunction<List<CpuLoadStat>, ToDoubleFunction<CpuLoadStat>, JsonObject> $$2 = (p_185575_, p_185576_) -> {
            JsonObject $$2x = new JsonObject();
            DoubleSummaryStatistics $$3 = p_185575_.stream().mapToDouble(p_185576_).summaryStatistics();
            $$2x.addProperty("min", $$3.getMin());
            $$2x.addProperty("average", $$3.getAverage());
            $$2x.addProperty("max", $$3.getMax());
            return $$2x;
        };
        $$1.add("jvm", (JsonElement) $$2.apply(listCpuLoadStat0, CpuLoadStat::f_185614_));
        $$1.add("userJvm", (JsonElement) $$2.apply(listCpuLoadStat0, CpuLoadStat::f_185615_));
        $$1.add("system", (JsonElement) $$2.apply(listCpuLoadStat0, CpuLoadStat::f_185616_));
        return $$1;
    }
}