package mezz.jei.common.config.file.serializers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;

public class BooleanSerializer implements IJeiConfigValueSerializer<Boolean> {

    public static final BooleanSerializer INSTANCE = new BooleanSerializer();

    private BooleanSerializer() {
    }

    public String serialize(Boolean value) {
        return value.toString();
    }

    public DeserializeResult<Boolean> deserialize(String string) {
        string = string.trim();
        if ("true".equalsIgnoreCase(string)) {
            return new DeserializeResult<>(true);
        } else {
            return "false".equalsIgnoreCase(string) ? new DeserializeResult<>(false) : new DeserializeResult<>(null, "string must be 'true' or 'false'");
        }
    }

    @Override
    public String getValidValuesDescription() {
        return "[true, false]";
    }

    public boolean isValid(Boolean value) {
        return true;
    }

    @Override
    public Optional<Collection<Boolean>> getAllValidValues() {
        return Optional.of(List.of(true, false));
    }
}