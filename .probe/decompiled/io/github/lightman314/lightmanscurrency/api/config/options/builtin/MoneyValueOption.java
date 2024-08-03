package io.github.lightman314.lightmanscurrency.api.config.options.builtin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.lightman314.lightmanscurrency.api.config.options.ConfigOption;
import io.github.lightman314.lightmanscurrency.api.config.options.basic.StringOption;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParser;
import io.github.lightman314.lightmanscurrency.api.config.options.parsing.ConfigParsingException;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.util.NonNullSupplier;

public class MoneyValueOption extends ConfigOption<MoneyValue> {

    public static final String bonusComment = "See the wiki for Money Value format: https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments";

    private final ConfigParser<MoneyValue> parser;

    public static ConfigParser<MoneyValue> createParser(@Nonnull Predicate<MoneyValue> allowed) {
        return new MoneyValueOption.Parser(allowed);
    }

    protected MoneyValueOption(@Nonnull NonNullSupplier<MoneyValue> defaultValue, @Nonnull Predicate<MoneyValue> allowed) {
        super(defaultValue);
        this.parser = createParser(allowed);
    }

    @Nonnull
    @Override
    protected ConfigParser<MoneyValue> getParser() {
        return this.parser;
    }

    @Nullable
    @Override
    protected String bonusComment() {
        return "See the wiki for Money Value format: https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments";
    }

    public static MoneyValueOption create(@Nonnull NonNullSupplier<MoneyValue> defaultValue) {
        return create(defaultValue, v -> true);
    }

    public static MoneyValueOption createNonEmpty(@Nonnull NonNullSupplier<MoneyValue> defaultValue) {
        return create(defaultValue, v -> !v.isEmpty());
    }

    public static MoneyValueOption create(@Nonnull NonNullSupplier<MoneyValue> defaultValue, @Nonnull Predicate<MoneyValue> allowed) {
        return new MoneyValueOption(defaultValue, allowed);
    }

    private static class Parser implements ConfigParser<MoneyValue> {

        private final Predicate<MoneyValue> allowed;

        private Parser(@Nonnull Predicate<MoneyValue> allowed) {
            this.allowed = allowed;
        }

        @Nonnull
        public MoneyValue tryParse(@Nonnull String cleanLine) throws ConfigParsingException {
            try {
                MoneyValue result = MoneyValueParser.parse(new StringReader(StringOption.PARSER.tryParse(cleanLine)), true);
                if (!this.allowed.test(result)) {
                    throw new ConfigParsingException(cleanLine + " is not an allowed Money Value input!");
                } else {
                    return result;
                }
            } catch (CommandSyntaxException var3) {
                throw new ConfigParsingException(var3);
            }
        }

        @Nonnull
        public String write(@Nonnull MoneyValue value) {
            return StringOption.PARSER.write(MoneyValueParser.writeParsable(value));
        }
    }
}