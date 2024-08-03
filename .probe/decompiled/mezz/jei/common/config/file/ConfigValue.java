package mezz.jei.common.config.file;

import java.util.List;
import mezz.jei.api.runtime.config.IJeiConfigValue;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ConfigValue<T> implements IJeiConfigValue<T> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String name;

    private final String description;

    private final T defaultValue;

    private final IJeiConfigValueSerializer<T> serializer;

    private volatile T currentValue;

    @Nullable
    private IConfigSchema schema;

    public ConfigValue(String name, T defaultValue, IJeiConfigValueSerializer<T> serializer, String description) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.currentValue = defaultValue;
        this.serializer = serializer;
    }

    public void setSchema(IConfigSchema schema) {
        this.schema = schema;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public T getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public T getValue() {
        if (this.schema != null) {
            this.schema.loadIfNeeded();
        }
        return this.currentValue;
    }

    @Override
    public IJeiConfigValueSerializer<T> getSerializer() {
        return this.serializer;
    }

    public List<String> setFromSerializedValue(String value) {
        IJeiConfigValueSerializer.IDeserializeResult<T> deserializeResult = this.serializer.deserialize(value);
        deserializeResult.getResult().ifPresent(t -> this.currentValue = (T) t);
        return deserializeResult.getErrors();
    }

    @Override
    public boolean set(T value) {
        if (!this.serializer.isValid(value)) {
            LOGGER.error("Tried to set invalid value : {}\n{}", value, this.serializer.getValidValuesDescription());
            return false;
        } else if (!this.currentValue.equals(value)) {
            this.currentValue = value;
            if (this.schema != null) {
                this.schema.markDirty();
            }
            return true;
        } else {
            return false;
        }
    }
}