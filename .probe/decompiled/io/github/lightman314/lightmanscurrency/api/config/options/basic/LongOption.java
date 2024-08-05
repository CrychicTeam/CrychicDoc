package io.github.lightman314.lightmanscurrency.api.config.options.basic;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import javax.annotation.Nonnull;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Nullable;

public class LongOption extends ConfigOption<Long> {

    private final long lowerLimit;

    private final long upperLimit;

    private final ConfigParser<Long> parser;

    public static ConfigParser<Long> makeParser(long lowerLimit, long upperLimit) {
        return new LongOption.Parser(lowerLimit, upperLimit);
    }

    protected LongOption(@Nonnull NonNullSupplier<Long> defaultValue, long lowerLimit, long upperLimit) {
        super(defaultValue);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.parser = makeParser(lowerLimit, upperLimit);
    }

    @Nonnull
    @Override
    protected ConfigParser<Long> getParser() {
        return this.parser;
    }

    @Nullable
    @Override
    protected String bonusComment() {
        return "Range: " + this.lowerLimit + " -> " + this.upperLimit;
    }

    public static LongOption create(long defaultValue) {
        return new LongOption(() -> defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static LongOption create(long defaultValue, long lowerLimit) {
        return new LongOption(() -> defaultValue, lowerLimit, Long.MAX_VALUE);
    }

    public static LongOption create(long defaultValue, long lowerLimit, long upperLimit) {
        return new LongOption(() -> defaultValue, lowerLimit, upperLimit);
    }

    public static LongOption create(@Nonnull NonNullSupplier<Long> defaultValue) {
        return new LongOption(defaultValue, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static LongOption create(@Nonnull NonNullSupplier<Long> defaultValue, long lowerLimit) {
        return new LongOption(defaultValue, lowerLimit, Long.MAX_VALUE);
    }

    public static LongOption create(@Nonnull NonNullSupplier<Long> defaultValue, long lowerLimit, long upperLimit) {
        return new LongOption(defaultValue, lowerLimit, upperLimit);
    }

    private static class Parser implements ConfigParser<Long> {

        private final long lowerLimit;

        private final long upperLimit;

        private Parser(long lowerLimit, long upperLimit) {
            this.lowerLimit = lowerLimit;
            this.upperLimit = upperLimit;
        }

        @Nonnull
        public Long tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            try {
                return MathUtil.clamp(Long.parseLong(cleanLine), this.lowerLimit, this.upperLimit);
            } catch (NumberFormatException var3) {
                throw new ConfigParsingException("Error parsing long!", var3);
            }
        }

        @Nonnull
        public String write(@Nonnull Long value) {
            return Long.toString(value);
        }
    }
}