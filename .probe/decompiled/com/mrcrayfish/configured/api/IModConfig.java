package com.mrcrayfish.configured.api;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;
import javax.annotation.Nullable;

public interface IModConfig {

    void update(IConfigEntry var1);

    IConfigEntry getRoot();

    ConfigType getType();

    String getFileName();

    String getModId();

    default boolean isReadOnly() {
        return false;
    }

    @Nullable
    default String getTranslationKey() {
        return null;
    }

    void loadWorldConfig(Path var1, Consumer<IModConfig> var2) throws IOException;

    default boolean isChanged() {
        return false;
    }

    default void restoreDefaults() {
    }

    default void startEditing() {
    }

    default void stopEditing() {
    }

    default void requestFromServer() {
    }
}