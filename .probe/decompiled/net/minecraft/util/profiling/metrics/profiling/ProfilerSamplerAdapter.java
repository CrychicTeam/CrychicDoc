package net.minecraft.util.profiling.metrics.profiling;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.profiling.ActiveProfiler;
import net.minecraft.util.profiling.ProfileCollector;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.util.profiling.metrics.MetricSampler;

public class ProfilerSamplerAdapter {

    private final Set<String> previouslyFoundSamplerNames = new ObjectOpenHashSet();

    public Set<MetricSampler> newSamplersFoundInProfiler(Supplier<ProfileCollector> supplierProfileCollector0) {
        Set<MetricSampler> $$1 = (Set<MetricSampler>) ((ProfileCollector) supplierProfileCollector0.get()).getChartedPaths().stream().filter(p_146176_ -> !this.previouslyFoundSamplerNames.contains(p_146176_.getLeft())).map(p_146174_ -> samplerForProfilingPath(supplierProfileCollector0, (String) p_146174_.getLeft(), (MetricCategory) p_146174_.getRight())).collect(Collectors.toSet());
        for (MetricSampler $$2 : $$1) {
            this.previouslyFoundSamplerNames.add($$2.getName());
        }
        return $$1;
    }

    private static MetricSampler samplerForProfilingPath(Supplier<ProfileCollector> supplierProfileCollector0, String string1, MetricCategory metricCategory2) {
        return MetricSampler.create(string1, metricCategory2, () -> {
            ActiveProfiler.PathEntry $$2 = ((ProfileCollector) supplierProfileCollector0.get()).getEntry(string1);
            return $$2 == null ? 0.0 : (double) $$2.getMaxDuration() / (double) TimeUtil.NANOSECONDS_PER_MILLISECOND;
        });
    }
}