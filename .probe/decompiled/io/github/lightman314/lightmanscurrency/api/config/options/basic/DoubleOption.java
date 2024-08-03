package io.github.lightman314.lightmanscurrency.api.config.options.basic;

import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import javax.annotation.Nonnull;
import net.minecraftforge.common.util.NonNullSupplier;
import org.jetbrains.annotations.Nullable;

public class DoubleOption extends ConfigOption<Double> {

    private final double lowerLimit;

    private final double upperLimit;

    private final ConfigParser<Double> parser;

    public static ConfigParser<Double> makeParser(double lowerLimit, double upperLimit) {
        return new DoubleOption.Parser(lowerLimit, upperLimit);
    }

    protected DoubleOption(@Nonnull NonNullSupplier<Double> defaultValue, double lowerLimit, double upperLimit) {
        super(defaultValue);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.parser = makeParser(lowerLimit, upperLimit);
    }

    @Nonnull
    @Override
    protected ConfigParser<Double> getParser() {
        return this.parser;
    }

    @Nullable
    @Override
    protected String bonusComment() {
        return "Range: " + this.lowerLimit + " -> " + this.upperLimit;
    }

    public static DoubleOption create(double defaultValue) {
        return new DoubleOption(() -> defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static DoubleOption create(double defaultValue, double lowerLimit) {
        return new DoubleOption(() -> defaultValue, lowerLimit, Double.MAX_VALUE);
    }

    public static DoubleOption create(double defaultValue, double lowerLimit, double upperLimit) {
        return new DoubleOption(() -> defaultValue, lowerLimit, upperLimit);
    }

    public static DoubleOption create(@Nonnull NonNullSupplier<Double> defaultValue) {
        return new DoubleOption(defaultValue, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    public static DoubleOption create(@Nonnull NonNullSupplier<Double> defaultValue, double lowerLimit) {
        return new DoubleOption(defaultValue, lowerLimit, Double.MAX_VALUE);
    }

    public static DoubleOption create(@Nonnull NonNullSupplier<Double> defaultValue, double lowerLimit, double upperLimit) {
        return new DoubleOption(defaultValue, lowerLimit, upperLimit);
    }

    private static class Parser implements ConfigParser<Double> {

        private final double lowerLimit;

        private final double upperLimit;

        private Parser(double lowerLimit, double upperLimit) {
            this.lowerLimit = lowerLimit;
            this.upperLimit = upperLimit;
        }

        @Nonnull
        public Double tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            try {
                return MathUtil.clamp(Double.parseDouble(cleanLine), this.lowerLimit, this.upperLimit);
            } catch (NumberFormatException var3) {
                throw new ConfigParsingException("Error parsing double!", var3);
            }
        }

        @Nonnull
        public String write(@Nonnull Double value) {
            return value.toString();
        }
    }
}