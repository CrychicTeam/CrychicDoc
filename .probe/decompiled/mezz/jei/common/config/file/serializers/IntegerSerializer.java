package mezz.jei.common.config.file.serializers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;

public class IntegerSerializer implements IJeiConfigValueSerializer<Integer> {

    private final int min;

    private final int max;

    public IntegerSerializer(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public String serialize(Integer value) {
        return value.toString();
    }

    public DeserializeResult<Integer> deserialize(String string) {
        string = string.trim();
        try {
            int value = Integer.parseInt(string);
            if (!this.isValid(value)) {
                String errorMessage = "Invalid integer. Must be: " + this.getValidValuesDescription();
                return new DeserializeResult<>(null, errorMessage);
            } else {
                return new DeserializeResult<>(value);
            }
        } catch (NumberFormatException var4) {
            String errorMessage = "Unable to parse int: '%s' with error:\n%s".formatted(string, var4.getMessage());
            return new DeserializeResult<>(null, errorMessage);
        }
    }

    @Override
    public String getValidValuesDescription() {
        if (this.min == Integer.MIN_VALUE && this.max == Integer.MAX_VALUE) {
            return "Any integer";
        } else {
            return this.max == Integer.MAX_VALUE ? "Any integer greater than or equal to %s".formatted(this.min) : "An integer in the range [%s, %s] (inclusive)".formatted(this.min, this.max);
        }
    }

    public boolean isValid(Integer value) {
        return value >= this.min && value <= this.max;
    }

    @Override
    public Optional<Collection<Integer>> getAllValidValues() {
        int count = this.max - this.min + 1;
        if (count > 0 && count < 20) {
            List<Integer> values = IntStream.rangeClosed(this.min, this.max).boxed().toList();
            return Optional.of(values);
        } else {
            return Optional.empty();
        }
    }
}