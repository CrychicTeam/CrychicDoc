package me.shedaniel.autoconfig.serializer;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

public interface ConfigSerializer<T extends ConfigData> {

    void serialize(T var1) throws ConfigSerializer.SerializationException;

    T deserialize() throws ConfigSerializer.SerializationException;

    T createDefault();

    @FunctionalInterface
    public interface Factory<T extends ConfigData> {

        ConfigSerializer<T> create(Config var1, Class<T> var2);
    }

    public static class SerializationException extends Exception {

        public SerializationException(Throwable cause) {
            super(cause);
        }
    }
}