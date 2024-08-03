package net.minecraft.util.profiling.metrics;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;
import javax.annotation.Nullable;

public class MetricSampler {

    private final String name;

    private final MetricCategory category;

    private final DoubleSupplier sampler;

    private final ByteBuf ticks;

    private final ByteBuf values;

    private volatile boolean isRunning;

    @Nullable
    private final Runnable beforeTick;

    @Nullable
    final MetricSampler.ThresholdTest thresholdTest;

    private double currentValue;

    protected MetricSampler(String string0, MetricCategory metricCategory1, DoubleSupplier doubleSupplier2, @Nullable Runnable runnable3, @Nullable MetricSampler.ThresholdTest metricSamplerThresholdTest4) {
        this.name = string0;
        this.category = metricCategory1;
        this.beforeTick = runnable3;
        this.sampler = doubleSupplier2;
        this.thresholdTest = metricSamplerThresholdTest4;
        this.values = ByteBufAllocator.DEFAULT.buffer();
        this.ticks = ByteBufAllocator.DEFAULT.buffer();
        this.isRunning = true;
    }

    public static MetricSampler create(String string0, MetricCategory metricCategory1, DoubleSupplier doubleSupplier2) {
        return new MetricSampler(string0, metricCategory1, doubleSupplier2, null, null);
    }

    public static <T> MetricSampler create(String string0, MetricCategory metricCategory1, T t2, ToDoubleFunction<T> toDoubleFunctionT3) {
        return builder(string0, metricCategory1, toDoubleFunctionT3, t2).build();
    }

    public static <T> MetricSampler.MetricSamplerBuilder<T> builder(String string0, MetricCategory metricCategory1, ToDoubleFunction<T> toDoubleFunctionT2, T t3) {
        return new MetricSampler.MetricSamplerBuilder<>(string0, metricCategory1, toDoubleFunctionT2, t3);
    }

    public void onStartTick() {
        if (!this.isRunning) {
            throw new IllegalStateException("Not running");
        } else {
            if (this.beforeTick != null) {
                this.beforeTick.run();
            }
        }
    }

    public void onEndTick(int int0) {
        this.verifyRunning();
        this.currentValue = this.sampler.getAsDouble();
        this.values.writeDouble(this.currentValue);
        this.ticks.writeInt(int0);
    }

    public void onFinished() {
        this.verifyRunning();
        this.values.release();
        this.ticks.release();
        this.isRunning = false;
    }

    private void verifyRunning() {
        if (!this.isRunning) {
            throw new IllegalStateException(String.format(Locale.ROOT, "Sampler for metric %s not started!", this.name));
        }
    }

    DoubleSupplier getSampler() {
        return this.sampler;
    }

    public String getName() {
        return this.name;
    }

    public MetricCategory getCategory() {
        return this.category;
    }

    public MetricSampler.SamplerResult result() {
        Int2DoubleMap $$0 = new Int2DoubleOpenHashMap();
        int $$1 = Integer.MIN_VALUE;
        int $$2 = Integer.MIN_VALUE;
        while (this.values.isReadable(8)) {
            int $$3 = this.ticks.readInt();
            if ($$1 == Integer.MIN_VALUE) {
                $$1 = $$3;
            }
            $$0.put($$3, this.values.readDouble());
            $$2 = $$3;
        }
        return new MetricSampler.SamplerResult($$1, $$2, $$0);
    }

    public boolean triggersThreshold() {
        return this.thresholdTest != null && this.thresholdTest.test(this.currentValue);
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            MetricSampler $$1 = (MetricSampler) object0;
            return this.name.equals($$1.name) && this.category.equals($$1.category);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public static class MetricSamplerBuilder<T> {

        private final String name;

        private final MetricCategory category;

        private final DoubleSupplier sampler;

        private final T context;

        @Nullable
        private Runnable beforeTick;

        @Nullable
        private MetricSampler.ThresholdTest thresholdTest;

        public MetricSamplerBuilder(String string0, MetricCategory metricCategory1, ToDoubleFunction<T> toDoubleFunctionT2, T t3) {
            this.name = string0;
            this.category = metricCategory1;
            this.sampler = () -> toDoubleFunctionT2.applyAsDouble(t3);
            this.context = t3;
        }

        public MetricSampler.MetricSamplerBuilder<T> withBeforeTick(Consumer<T> consumerT0) {
            this.beforeTick = () -> consumerT0.accept(this.context);
            return this;
        }

        public MetricSampler.MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest metricSamplerThresholdTest0) {
            this.thresholdTest = metricSamplerThresholdTest0;
            return this;
        }

        public MetricSampler build() {
            return new MetricSampler(this.name, this.category, this.sampler, this.beforeTick, this.thresholdTest);
        }
    }

    public static class SamplerResult {

        private final Int2DoubleMap recording;

        private final int firstTick;

        private final int lastTick;

        public SamplerResult(int int0, int int1, Int2DoubleMap intDoubleMap2) {
            this.firstTick = int0;
            this.lastTick = int1;
            this.recording = intDoubleMap2;
        }

        public double valueAtTick(int int0) {
            return this.recording.get(int0);
        }

        public int getFirstTick() {
            return this.firstTick;
        }

        public int getLastTick() {
            return this.lastTick;
        }
    }

    public interface ThresholdTest {

        boolean test(double var1);
    }

    public static class ValueIncreasedByPercentage implements MetricSampler.ThresholdTest {

        private final float percentageIncreaseThreshold;

        private double previousValue = Double.MIN_VALUE;

        public ValueIncreasedByPercentage(float float0) {
            this.percentageIncreaseThreshold = float0;
        }

        @Override
        public boolean test(double double0) {
            boolean $$2;
            if (this.previousValue != Double.MIN_VALUE && !(double0 <= this.previousValue)) {
                $$2 = (double0 - this.previousValue) / this.previousValue >= (double) this.percentageIncreaseThreshold;
            } else {
                $$2 = false;
            }
            this.previousValue = double0;
            return $$2;
        }
    }
}