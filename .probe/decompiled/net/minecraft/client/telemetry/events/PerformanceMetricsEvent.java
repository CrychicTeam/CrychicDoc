package net.minecraft.client.telemetry.events;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;

public final class PerformanceMetricsEvent extends AggregatedTelemetryEvent {

    private static final long DEDICATED_MEMORY_KB = toKilobytes(Runtime.getRuntime().maxMemory());

    private final LongList fpsSamples = new LongArrayList();

    private final LongList frameTimeSamples = new LongArrayList();

    private final LongList usedMemorySamples = new LongArrayList();

    @Override
    public void tick(TelemetryEventSender telemetryEventSender0) {
        if (Minecraft.getInstance().telemetryOptInExtra()) {
            super.tick(telemetryEventSender0);
        }
    }

    private void resetValues() {
        this.fpsSamples.clear();
        this.frameTimeSamples.clear();
        this.usedMemorySamples.clear();
    }

    @Override
    public void takeSample() {
        this.fpsSamples.add((long) Minecraft.getInstance().getFps());
        this.takeUsedMemorySample();
        this.frameTimeSamples.add(Minecraft.getInstance().getFrameTimeNs());
    }

    private void takeUsedMemorySample() {
        long $$0 = Runtime.getRuntime().totalMemory();
        long $$1 = Runtime.getRuntime().freeMemory();
        long $$2 = $$0 - $$1;
        this.usedMemorySamples.add(toKilobytes($$2));
    }

    @Override
    public void sendEvent(TelemetryEventSender telemetryEventSender0) {
        telemetryEventSender0.send(TelemetryEventType.PERFORMANCE_METRICS, p_261568_ -> {
            p_261568_.put(TelemetryProperty.FRAME_RATE_SAMPLES, new LongArrayList(this.fpsSamples));
            p_261568_.put(TelemetryProperty.RENDER_TIME_SAMPLES, new LongArrayList(this.frameTimeSamples));
            p_261568_.put(TelemetryProperty.USED_MEMORY_SAMPLES, new LongArrayList(this.usedMemorySamples));
            p_261568_.put(TelemetryProperty.NUMBER_OF_SAMPLES, this.m_261091_());
            p_261568_.put(TelemetryProperty.RENDER_DISTANCE, Minecraft.getInstance().options.getEffectiveRenderDistance());
            p_261568_.put(TelemetryProperty.DEDICATED_MEMORY_KB, (int) DEDICATED_MEMORY_KB);
        });
        this.resetValues();
    }

    private static long toKilobytes(long long0) {
        return long0 / 1000L;
    }
}