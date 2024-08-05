package fuzs.puzzleslib.api.config.v3;

import fuzs.puzzleslib.api.core.v1.Buildable;
import fuzs.puzzleslib.impl.config.ConfigHolderRegistry;
import fuzs.puzzleslib.impl.core.ModContext;
import java.nio.file.Paths;
import java.util.function.UnaryOperator;

public interface ConfigHolder {

    static ConfigHolder.Builder builder(String modId) {
        return ModContext.get(modId).getConfigHolder$Builder();
    }

    <T extends ConfigCore> ConfigDataHolder<T> getHolder(Class<T> var1);

    default <T extends ConfigCore> T get(Class<T> clazz) {
        return this.<T>getHolder(clazz).getConfig();
    }

    static String simpleName(String modId) {
        return String.format("%s.toml", modId);
    }

    static String defaultName(String modId, String type) {
        return String.format("%s-%s.toml", modId, type);
    }

    static String moveToDir(String configDir, String fileName) {
        return Paths.get(configDir, fileName).toString();
    }

    public interface Builder extends ConfigHolderRegistry, Buildable {

        <T extends ConfigCore> ConfigHolder.Builder client(Class<T> var1);

        <T extends ConfigCore> ConfigHolder.Builder common(Class<T> var1);

        <T extends ConfigCore> ConfigHolder.Builder server(Class<T> var1);

        <T extends ConfigCore> ConfigHolder.Builder setFileName(Class<T> var1, UnaryOperator<String> var2);
    }
}