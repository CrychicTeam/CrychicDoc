package me.shedaniel.autoconfig.serializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;

public class Toml4jConfigSerializer<T extends ConfigData> implements ConfigSerializer<T> {

    private Config definition;

    private Class<T> configClass;

    private TomlWriter tomlWriter;

    public Toml4jConfigSerializer(Config definition, Class<T> configClass, TomlWriter tomlWriter) {
        this.definition = definition;
        this.configClass = configClass;
        this.tomlWriter = tomlWriter;
    }

    public Toml4jConfigSerializer(Config definition, Class<T> configClass) {
        this(definition, configClass, new TomlWriter());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(this.definition.name() + ".toml");
    }

    @Override
    public void serialize(T config) throws ConfigSerializer.SerializationException {
        Path configPath = this.getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            this.tomlWriter.write(config, configPath.toFile());
        } catch (IOException var4) {
            throw new ConfigSerializer.SerializationException(var4);
        }
    }

    @Override
    public T deserialize() throws ConfigSerializer.SerializationException {
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            try {
                return new Toml().read(configPath.toFile()).to(this.configClass);
            } catch (IllegalStateException var3) {
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