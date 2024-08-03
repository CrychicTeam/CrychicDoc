package net.minecraft.client.telemetry;

import java.util.function.Consumer;

@FunctionalInterface
public interface TelemetryEventSender {

    TelemetryEventSender DISABLED = (p_261883_, p_261730_) -> {
    };

    default TelemetryEventSender decorate(Consumer<TelemetryPropertyMap.Builder> consumerTelemetryPropertyMapBuilder0) {
        return (p_261694_, p_261504_) -> this.send(p_261694_, p_261539_ -> {
            p_261504_.accept(p_261539_);
            consumerTelemetryPropertyMapBuilder0.accept(p_261539_);
        });
    }

    void send(TelemetryEventType var1, Consumer<TelemetryPropertyMap.Builder> var2);
}