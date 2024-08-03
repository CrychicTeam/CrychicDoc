package me.shedaniel.autoconfig.serializer;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;

public class DummyConfigSerializer<T extends ConfigData> implements ConfigSerializer<T> {

    private final Class<T> configClass;

    public DummyConfigSerializer(Config definition, Class<T> configClass) {
        this.configClass = configClass;
    }

    @Override
    public void serialize(T config) {
    }

    @Override
    public T deserialize() {
        return this.createDefault();
    }

    @Override
    public T createDefault() {
        return Utils.constructUnsafely(this.configClass);
    }
}