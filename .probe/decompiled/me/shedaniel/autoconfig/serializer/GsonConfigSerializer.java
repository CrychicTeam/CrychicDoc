package me.shedaniel.autoconfig.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;

public class GsonConfigSerializer<T extends ConfigData> implements ConfigSerializer<T> {

    private Config definition;

    private Class<T> configClass;

    private Gson gson;

    public GsonConfigSerializer(Config definition, Class<T> configClass, Gson gson) {
        this.definition = definition;
        this.configClass = configClass;
        this.gson = gson;
    }

    public GsonConfigSerializer(Config definition, Class<T> configClass) {
        this(definition, configClass, new GsonBuilder().setPrettyPrinting().create());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(this.definition.name() + ".json");
    }

    @Override
    public void serialize(T config) throws ConfigSerializer.SerializationException {
        Path configPath = this.getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            this.gson.toJson(config, writer);
            writer.close();
        } catch (IOException var4) {
            throw new ConfigSerializer.SerializationException(var4);
        }
    }

    @Override
    public T deserialize() throws ConfigSerializer.SerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                BufferedReader reader = Files.newBufferedReader(configPath);
                T ret = (T) this.gson.fromJson(reader, this.configClass);
                reader.close();
                return ret;
            } catch (JsonParseException | IOException var4) {
                throw new ConfigSerializer.SerializationException(var4);
            }
        } else {
            return this.createDefault();
        }
    }

    @Override
    public T createDefault() {
        return Utils.constructUnsafely(this.configClass);
    }
}