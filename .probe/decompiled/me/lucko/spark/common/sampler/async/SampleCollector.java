package me.lucko.spark.common.sampler.async;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.Collection;
import java.util.Objects;
import me.lucko.spark.common.sampler.SamplerMode;
import me.lucko.spark.common.sampler.async.jfr.JfrReader;

public interface SampleCollector<E extends JfrReader.Event> {

    Collection<String> initArguments(AsyncProfilerAccess var1);

    Class<E> eventClass();

    long measure(E var1);

    SamplerMode getMode();

    public static final class Allocation implements SampleCollector<JfrReader.AllocationSample> {

        private final int intervalBytes;

        private final boolean liveOnly;

        public Allocation(int intervalBytes, boolean liveOnly) {
            this.intervalBytes = intervalBytes;
            this.liveOnly = liveOnly;
        }

        public boolean isLiveOnly() {
            return this.liveOnly;
        }

        @Override
        public Collection<String> initArguments(AsyncProfilerAccess access) {
            AsyncProfilerAccess.ProfilingEvent event = access.getAllocationProfilingEvent();
            Objects.requireNonNull(event, "event");
            Builder<String> builder = ImmutableList.builder();
            builder.add("event=" + event);
            builder.add("alloc=" + this.intervalBytes);
            if (this.liveOnly) {
                builder.add("live");
            }
            return builder.build();
        }

        @Override
        public Class<JfrReader.AllocationSample> eventClass() {
            return JfrReader.AllocationSample.class;
        }

        public long measure(JfrReader.AllocationSample event) {
            return event.value();
        }

        @Override
        public SamplerMode getMode() {
            return SamplerMode.ALLOCATION;
        }
    }

    public static final class Execution implements SampleCollector<JfrReader.ExecutionSample> {

        private final int interval;

        public Execution(int interval) {
            this.interval = interval;
        }

        @Override
        public Collection<String> initArguments(AsyncProfilerAccess access) {
            AsyncProfilerAccess.ProfilingEvent event = access.getProfilingEvent();
            Objects.requireNonNull(event, "event");
            return ImmutableList.of("event=" + event, "interval=" + this.interval + "us");
        }

        @Override
        public Class<JfrReader.ExecutionSample> eventClass() {
            return JfrReader.ExecutionSample.class;
        }

        public long measure(JfrReader.ExecutionSample event) {
            return event.value() * (long) this.interval;
        }

        @Override
        public SamplerMode getMode() {
            return SamplerMode.EXECUTION;
        }
    }
}