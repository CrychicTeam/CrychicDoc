package net.minecraft.util.profiling.metrics;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class MetricsRegistry {

    public static final MetricsRegistry INSTANCE = new MetricsRegistry();

    private final WeakHashMap<ProfilerMeasured, Void> measuredInstances = new WeakHashMap();

    private MetricsRegistry() {
    }

    public void add(ProfilerMeasured profilerMeasured0) {
        this.measuredInstances.put(profilerMeasured0, null);
    }

    public List<MetricSampler> getRegisteredSamplers() {
        Map<String, List<MetricSampler>> $$0 = (Map<String, List<MetricSampler>>) this.measuredInstances.keySet().stream().flatMap(p_146079_ -> p_146079_.profiledMetrics().stream()).collect(Collectors.groupingBy(MetricSampler::m_146020_));
        return aggregateDuplicates($$0);
    }

    private static List<MetricSampler> aggregateDuplicates(Map<String, List<MetricSampler>> mapStringListMetricSampler0) {
        return (List<MetricSampler>) mapStringListMetricSampler0.entrySet().stream().map(p_146075_ -> {
            String $$1 = (String) p_146075_.getKey();
            List<MetricSampler> $$2 = (List<MetricSampler>) p_146075_.getValue();
            return (MetricSampler) ($$2.size() > 1 ? new MetricsRegistry.AggregatedMetricSampler($$1, $$2) : (MetricSampler) $$2.get(0));
        }).collect(Collectors.toList());
    }

    static class AggregatedMetricSampler extends MetricSampler {

        private final List<MetricSampler> delegates;

        AggregatedMetricSampler(String string0, List<MetricSampler> listMetricSampler1) {
            super(string0, ((MetricSampler) listMetricSampler1.get(0)).getCategory(), () -> averageValueFromDelegates(listMetricSampler1), () -> beforeTick(listMetricSampler1), thresholdTest(listMetricSampler1));
            this.delegates = listMetricSampler1;
        }

        private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> listMetricSampler0) {
            return p_146091_ -> listMetricSampler0.stream().anyMatch(p_146086_ -> p_146086_.thresholdTest != null ? p_146086_.thresholdTest.test(p_146091_) : false);
        }

        private static void beforeTick(List<MetricSampler> listMetricSampler0) {
            for (MetricSampler $$1 : listMetricSampler0) {
                $$1.onStartTick();
            }
        }

        private static double averageValueFromDelegates(List<MetricSampler> listMetricSampler0) {
            double $$1 = 0.0;
            for (MetricSampler $$2 : listMetricSampler0) {
                $$1 += $$2.getSampler().getAsDouble();
            }
            return $$1 / (double) listMetricSampler0.size();
        }

        @Override
        public boolean equals(@Nullable Object object0) {
            if (this == object0) {
                return true;
            } else if (object0 == null || this.getClass() != object0.getClass()) {
                return false;
            } else if (!super.equals(object0)) {
                return false;
            } else {
                MetricsRegistry.AggregatedMetricSampler $$1 = (MetricsRegistry.AggregatedMetricSampler) object0;
                return this.delegates.equals($$1.delegates);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { super.hashCode(), this.delegates });
        }
    }
}