package fuzs.puzzleslib.api.config.v3;

import java.util.function.Consumer;

public interface ConfigDataHolder<T extends ConfigCore> {

    T getConfig();

    boolean isAvailable();

    default void accept(Runnable callback) {
        this.accept((Consumer<T>) (config -> callback.run()));
    }

    void accept(Consumer<T> var1);
}