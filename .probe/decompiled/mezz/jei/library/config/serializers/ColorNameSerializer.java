package mezz.jei.library.config.serializers;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;
import mezz.jei.common.config.file.serializers.DeserializeResult;
import mezz.jei.library.color.ColorName;
import org.apache.commons.lang3.StringUtils;

public class ColorNameSerializer implements IJeiConfigValueSerializer<ColorName> {

    public static final ColorNameSerializer INSTANCE = new ColorNameSerializer();

    private ColorNameSerializer() {
    }

    public String serialize(ColorName value) {
        String name = value.name();
        String color = Integer.toHexString(value.color()).toUpperCase(Locale.ROOT);
        return "%s:%s".formatted(name, StringUtils.leftPad(color, 6, '0'));
    }

    public DeserializeResult<ColorName> deserialize(String string) {
        string = string.trim();
        if (string.startsWith("\"") && string.endsWith("\"")) {
            string = string.substring(1, string.length() - 1);
        }
        String[] values = string.split(":");
        if (values.length == 2) {
            String name = values[0];
            try {
                Integer color = Integer.decode("0x" + values[1]);
                ColorName colorName = new ColorName(name, color);
                return new DeserializeResult<>(colorName);
            } catch (NumberFormatException var6) {
                String errorMsg = "Color entry must have an RGB hex color.\nGot an exception when parsing:\n%s".formatted(var6.getMessage());
                return new DeserializeResult<>(null, errorMsg);
            }
        } else {
            String errorMsg = "Color entry must be a name and an RGB hex color, separated by a ':'.";
            return new DeserializeResult<>(null, errorMsg);
        }
    }

    @Override
    public String getValidValuesDescription() {
        return "Any color name and an RGB hex color, separated by a ':'";
    }

    public boolean isValid(ColorName value) {
        return true;
    }

    @Override
    public Optional<Collection<ColorName>> getAllValidValues() {
        return Optional.empty();
    }
}