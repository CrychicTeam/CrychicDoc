package io.github.lightman314.lightmanscurrency.api.config.options.basic;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import javax.annotation.Nonnull;
import net.minecraftforge.common.util.NonNullSupplier;

public class StringOption extends ConfigOption<String> {

    public static final ConfigParser<String> PARSER = new StringOption.Parser();

    protected StringOption(@Nonnull NonNullSupplier<String> defaultValue) {
        super(defaultValue);
    }

    @Nonnull
    public static StringOption create(@Nonnull String defaultValue) {
        return new StringOption(() -> defaultValue);
    }

    public static StringOption create(@Nonnull NonNullSupplier<String> defaultValue) {
        return new StringOption(defaultValue);
    }

    @Nonnull
    @Override
    protected ConfigParser<String> getParser() {
        return PARSER;
    }

    private static class Parser implements ConfigParser<String> {

        @Nonnull
        public String tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            return cleanLine.startsWith("\"") && cleanLine.endsWith("\"") ? cleanLine.substring(1, cleanLine.length() - 1) : cleanLine;
        }

        @Nonnull
        public String write(@Nonnull String value) {
            return "\"" + value + "\"";
        }
    }
}