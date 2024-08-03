package me.shedaniel.autoconfig.serializer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;

public class JanksonConfigSerializer<T extends ConfigData> implements ConfigSerializer<T> {

    private Config definition;

    private Class<T> configClass;

    private Jankson jankson;

    public JanksonConfigSerializer(Config definition, Class<T> configClass, Jankson jankson) {
        this.definition = definition;
        this.configClass = configClass;
        this.jankson = jankson;
    }

    public JanksonConfigSerializer(Config definition, Class<T> configClass) {
        this(definition, configClass, Jankson.builder().build());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(this.definition.name() + ".json5");
    }

    @Override
    public void serialize(T config) throws ConfigSerializer.SerializationException {
        Path configPath = this.getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            writer.write(this.jankson.toJson(config).toJson(true, true));
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
                return this.jankson.fromJson(this.jankson.load(this.getConfigPath().toFile()), this.configClass);
            } catch (Throwable var3) {
                throw new ConfigSerializer.SerializationException(var3);
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