package mezz.jei.common.config.file.serializers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;

public class EnumSerializer<T extends Enum<T>> implements IJeiConfigValueSerializer<T> {

    private final Class<T> enumClass;

    private final Collection<T> validValues;

    public EnumSerializer(Class<T> enumClass) {
        this.enumClass = enumClass;
        this.validValues = List.of((Enum[]) enumClass.getEnumConstants());
    }

    public String serialize(T value) {
        return value.name();
    }

    public DeserializeResult<T> deserialize(String string) {
        string = string.trim();
        if (string.startsWith("\"") && string.endsWith("\"")) {
            string = string.substring(1, string.length() - 1);
        }
        try {
            T value = (T) Enum.valueOf(this.enumClass, string);
            return new DeserializeResult<>(value);
        } catch (IllegalArgumentException var3) {
            return new DeserializeResult<>(null, "Invalid enum name: %s".formatted(var3.getMessage()));
        }
    }

    @Override
    public String getValidValuesDescription() {
        String names = (String) this.validValues.stream().map(Enum::name).collect(Collectors.joining(", "));
        return "[%s]".formatted(names);
    }

    public boolean isValid(T value) {
        return this.validValues.contains(value);
    }

    @Override
    public Optional<Collection<T>> getAllValidValues() {
        return Optional.of(this.validValues);
    }
}