package dev.architectury.registry.registries;

import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;

public interface RegistrarBuilder<T> {

    Registrar<T> build();

    RegistrarBuilder<T> option(RegistrarOption var1);

    default RegistrarBuilder<T> saveToDisc() {
        return this.option(StandardRegistrarOption.SAVE_TO_DISC);
    }

    default RegistrarBuilder<T> syncToClients() {
        return this.option(StandardRegistrarOption.SYNC_TO_CLIENTS);
    }
}