package mezz.jei.common.config.file.serializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mezz.jei.api.runtime.config.IJeiConfigListValueSerializer;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;

public class ListSerializer<T> implements IJeiConfigListValueSerializer<T> {

    private final IJeiConfigValueSerializer<T> valueSerializer;

    public ListSerializer(IJeiConfigValueSerializer<T> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String serialize(List<T> values) {
        return (String) values.stream().map(this.valueSerializer::serialize).collect(Collectors.joining(", "));
    }

    public DeserializeResult<List<T>> deserialize(String string) {
        string = string.trim();
        if (string.startsWith("[")) {
            if (!string.endsWith("]")) {
                String errorMessage = "No closing brace found.\nList must have no braces, or be wrapped in [ and ].";
                return new DeserializeResult<>(null, errorMessage);
            }
            string = string.substring(1, string.length() - 1);
        }
        String[] split = string.split(",");
        List<String> errors = new ArrayList();
        List<T> results = Arrays.stream(split).map(String::trim).map(this.valueSerializer::deserialize).mapMulti((r, c) -> {
            r.getResult().ifPresent(c);
            errors.addAll(r.getErrors());
        }).toList();
        return new DeserializeResult<>(results, errors);
    }

    @Override
    public String getValidValuesDescription() {
        return "A comma-separated list containing values of:\n%s".formatted(this.valueSerializer.getValidValuesDescription());
    }

    public boolean isValid(List<T> value) {
        return value.stream().allMatch(this.valueSerializer::isValid);
    }

    @Override
    public IJeiConfigValueSerializer<T> getListValueSerializer() {
        return this.valueSerializer;
    }

    @Override
    public Optional<Collection<List<T>>> getAllValidValues() {
        return Optional.empty();
    }
}