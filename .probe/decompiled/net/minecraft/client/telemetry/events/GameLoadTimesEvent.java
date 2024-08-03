package net.minecraft.client.telemetry.events;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;
import org.slf4j.Logger;

public class GameLoadTimesEvent {

    public static final GameLoadTimesEvent INSTANCE = new GameLoadTimesEvent(Ticker.systemTicker());

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Ticker timeSource;

    private final Map<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch> measurements = new HashMap();

    private OptionalLong bootstrapTime = OptionalLong.empty();

    protected GameLoadTimesEvent(Ticker ticker0) {
        this.timeSource = ticker0;
    }

    public synchronized void beginStep(TelemetryProperty<GameLoadTimesEvent.Measurement> telemetryPropertyGameLoadTimesEventMeasurement0) {
        this.beginStep(telemetryPropertyGameLoadTimesEventMeasurement0, (Function<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch>) (p_286494_ -> Stopwatch.createStarted(this.timeSource)));
    }

    public synchronized void beginStep(TelemetryProperty<GameLoadTimesEvent.Measurement> telemetryPropertyGameLoadTimesEventMeasurement0, Stopwatch stopwatch1) {
        this.beginStep(telemetryPropertyGameLoadTimesEventMeasurement0, (Function<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch>) (p_286421_ -> stopwatch1));
    }

    private synchronized void beginStep(TelemetryProperty<GameLoadTimesEvent.Measurement> telemetryPropertyGameLoadTimesEventMeasurement0, Function<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch> functionTelemetryPropertyGameLoadTimesEventMeasurementStopwatch1) {
        this.measurements.computeIfAbsent(telemetryPropertyGameLoadTimesEventMeasurement0, functionTelemetryPropertyGameLoadTimesEventMeasurementStopwatch1);
    }

    public synchronized void endStep(TelemetryProperty<GameLoadTimesEvent.Measurement> telemetryPropertyGameLoadTimesEventMeasurement0) {
        Stopwatch $$1 = (Stopwatch) this.measurements.get(telemetryPropertyGameLoadTimesEventMeasurement0);
        if ($$1 == null) {
            LOGGER.warn("Attempted to end step for {} before starting it", telemetryPropertyGameLoadTimesEventMeasurement0.id());
        } else {
            if ($$1.isRunning()) {
                $$1.stop();
            }
        }
    }

    public void send(TelemetryEventSender telemetryEventSender0) {
        telemetryEventSender0.send(TelemetryEventType.GAME_LOAD_TIMES, p_286285_ -> {
            synchronized (this) {
                this.measurements.forEach((p_286804_, p_286275_) -> {
                    if (!p_286275_.isRunning()) {
                        long $$3 = p_286275_.elapsed(TimeUnit.MILLISECONDS);
                        p_286285_.put(p_286804_, new GameLoadTimesEvent.Measurement((int) $$3));
                    } else {
                        LOGGER.warn("Measurement {} was discarded since it was still ongoing when the event {} was sent.", p_286804_.id(), TelemetryEventType.GAME_LOAD_TIMES.id());
                    }
                });
                this.bootstrapTime.ifPresent(p_286872_ -> p_286285_.put(TelemetryProperty.LOAD_TIME_BOOTSTRAP_MS, new GameLoadTimesEvent.Measurement((int) p_286872_)));
                this.measurements.clear();
            }
        });
    }

    public synchronized void setBootstrapTime(long long0) {
        this.bootstrapTime = OptionalLong.of(long0);
    }

    public static record Measurement(int f_285578_) {

        private final int millis;

        public static final Codec<GameLoadTimesEvent.Measurement> CODEC = Codec.INT.xmap(GameLoadTimesEvent.Measurement::new, p_286736_ -> p_286736_.millis);

        public Measurement(int f_285578_) {
            this.millis = f_285578_;
        }
    }
}