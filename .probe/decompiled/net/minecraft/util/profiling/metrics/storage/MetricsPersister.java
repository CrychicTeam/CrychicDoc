package net.minecraft.util.profiling.metrics.storage;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CsvOutput;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.util.profiling.metrics.MetricSampler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

public class MetricsPersister {

    public static final Path PROFILING_RESULTS_DIR = Paths.get("debug/profiling");

    public static final String METRICS_DIR_NAME = "metrics";

    public static final String DEVIATIONS_DIR_NAME = "deviations";

    public static final String PROFILING_RESULT_FILENAME = "profiling.txt";

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String rootFolderName;

    public MetricsPersister(String string0) {
        this.rootFolderName = string0;
    }

    public Path saveReports(Set<MetricSampler> setMetricSampler0, Map<MetricSampler, List<RecordedDeviation>> mapMetricSamplerListRecordedDeviation1, ProfileResults profileResults2) {
        try {
            Files.createDirectories(PROFILING_RESULTS_DIR);
        } catch (IOException var8) {
            throw new UncheckedIOException(var8);
        }
        try {
            Path $$4 = Files.createTempDirectory("minecraft-profiling");
            $$4.toFile().deleteOnExit();
            Files.createDirectories(PROFILING_RESULTS_DIR);
            Path $$5 = $$4.resolve(this.rootFolderName);
            Path $$6 = $$5.resolve("metrics");
            this.saveMetrics(setMetricSampler0, $$6);
            if (!mapMetricSamplerListRecordedDeviation1.isEmpty()) {
                this.saveDeviations(mapMetricSamplerListRecordedDeviation1, $$5.resolve("deviations"));
            }
            this.saveProfilingTaskExecutionResult(profileResults2, $$5);
            return $$4;
        } catch (IOException var7) {
            throw new UncheckedIOException(var7);
        }
    }

    private void saveMetrics(Set<MetricSampler> setMetricSampler0, Path path1) {
        if (setMetricSampler0.isEmpty()) {
            throw new IllegalArgumentException("Expected at least one sampler to persist");
        } else {
            Map<MetricCategory, List<MetricSampler>> $$2 = (Map<MetricCategory, List<MetricSampler>>) setMetricSampler0.stream().collect(Collectors.groupingBy(MetricSampler::m_146021_));
            $$2.forEach((p_146232_, p_146233_) -> this.saveCategory(p_146232_, p_146233_, path1));
        }
    }

    private void saveCategory(MetricCategory metricCategory0, List<MetricSampler> listMetricSampler1, Path path2) {
        Path $$3 = path2.resolve(Util.sanitizeName(metricCategory0.getDescription(), ResourceLocation::m_135828_) + ".csv");
        Writer $$4 = null;
        try {
            Files.createDirectories($$3.getParent());
            $$4 = Files.newBufferedWriter($$3, StandardCharsets.UTF_8);
            CsvOutput.Builder $$5 = CsvOutput.builder();
            $$5.addColumn("@tick");
            for (MetricSampler $$6 : listMetricSampler1) {
                $$5.addColumn($$6.getName());
            }
            CsvOutput $$7 = $$5.build($$4);
            List<MetricSampler.SamplerResult> $$8 = (List<MetricSampler.SamplerResult>) listMetricSampler1.stream().map(MetricSampler::m_146024_).collect(Collectors.toList());
            int $$9 = $$8.stream().mapToInt(MetricSampler.SamplerResult::m_146056_).summaryStatistics().getMin();
            int $$10 = $$8.stream().mapToInt(MetricSampler.SamplerResult::m_146059_).summaryStatistics().getMax();
            for (int $$11 = $$9; $$11 <= $$10; $$11++) {
                int $$12 = $$11;
                Stream<String> $$13 = $$8.stream().map(p_146222_ -> String.valueOf(p_146222_.valueAtTick($$12)));
                Object[] $$14 = Stream.concat(Stream.of(String.valueOf($$11)), $$13).toArray(String[]::new);
                $$7.writeRow($$14);
            }
            LOGGER.info("Flushed metrics to {}", $$3);
        } catch (Exception var18) {
            LOGGER.error("Could not save profiler results to {}", $$3, var18);
        } finally {
            IOUtils.closeQuietly($$4);
        }
    }

    private void saveDeviations(Map<MetricSampler, List<RecordedDeviation>> mapMetricSamplerListRecordedDeviation0, Path path1) {
        DateTimeFormatter $$2 = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss.SSS", Locale.UK).withZone(ZoneId.systemDefault());
        mapMetricSamplerListRecordedDeviation0.forEach((p_146242_, p_146243_) -> p_146243_.forEach(p_146238_ -> {
            String $$4 = $$2.format(p_146238_.timestamp);
            Path $$5 = path1.resolve(Util.sanitizeName(p_146242_.getName(), ResourceLocation::m_135828_)).resolve(String.format(Locale.ROOT, "%d@%s.txt", p_146238_.tick, $$4));
            p_146238_.profilerResultAtTick.saveResults($$5);
        }));
    }

    private void saveProfilingTaskExecutionResult(ProfileResults profileResults0, Path path1) {
        profileResults0.saveResults(path1.resolve("profiling.txt"));
    }
}