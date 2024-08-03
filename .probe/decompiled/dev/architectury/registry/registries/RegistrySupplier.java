package dev.architectury.registry.registries;

import java.util.function.Consumer;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface RegistrySupplier<T> extends DeferredSupplier<T> {

    RegistrarManager getRegistrarManager();

    Registrar<T> getRegistrar();

    default void listen(Consumer<T> callback) {
        this.getRegistrar().listen(this, callback);
    }
}