package io.github.lightman314.lightmanscurrency.api.money.value;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.types.CurrencyType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;

public abstract class MoneyValueParser {

    public final String prefix;

    public static final CommandExceptionType EXCEPTION_TYPE = new CommandExceptionType() {

        public int hashCode() {
            return super.hashCode();
        }
    };

    protected MoneyValueParser(@Nonnull String prefix) {
        this.prefix = prefix;
    }

    protected boolean tryParse(@Nullable String prefix) {
        return this.prefix.equals(prefix);
    }

    protected abstract MoneyValue parseValueArgument(@Nonnull StringReader var1) throws CommandSyntaxException;

    @Nullable
    protected final String tryWrite(@Nonnull MoneyValue value) {
        String sub = this.writeValueArgument(value);
        return sub != null ? this.prefix + ";" + sub : null;
    }

    protected abstract String writeValueArgument(@Nonnull MoneyValue var1);

    @Nonnull
    public <S> CompletableFuture<Suggestions> listSuggestions(@Nonnull CommandContext<S> context, @Nonnull SuggestionsBuilder builder, @Nonnull String trail, @Nonnull HolderLookup<Item> items) {
        return Suggestions.empty();
    }

    public void addExamples(@Nonnull List<String> examples) {
    }

    @Nonnull
    public static MoneyValue ParseConfigString(String string, Supplier<MoneyValue> defaultValue) {
        try {
            return parse(new StringReader(string), true);
        } catch (CommandSyntaxException var3) {
            LightmansCurrency.LogError("Error parsing Money Value config input.", var3);
            return (MoneyValue) defaultValue.get();
        }
    }

    @Nonnull
    public static MoneyValue parse(StringReader reader, boolean allowEmpty) throws CommandSyntaxException {
        StringReader inputReader = new StringReader(readArgument(reader));
        String prefix;
        if (inputReader.getString().contains(";")) {
            prefix = readStringUntil(inputReader, ';');
        } else {
            prefix = null;
        }
        for (CurrencyType type : MoneyAPI.API.AllCurrencyTypes()) {
            MoneyValueParser parser = type.getValueParser();
            if (parser != null && parser.tryParse(prefix)) {
                StringReader readerCopy = new StringReader(inputReader);
                MoneyValue value = parser.parseValueArgument(readerCopy);
                if (value != null) {
                    if (allowEmpty) {
                        return value;
                    }
                    if (!value.isEmpty() && !value.isFree()) {
                        return value;
                    }
                    throw NoValueException(reader);
                }
            }
        }
        throw NoValueException(inputReader);
    }

    @Nonnull
    public static String writeParsable(@Nonnull MoneyValue value) {
        for (CurrencyType type : MoneyAPI.API.AllCurrencyTypes()) {
            MoneyValueParser parser = type.getValueParser();
            if (parser != null) {
                String result = parser.tryWrite(value);
                if (result != null) {
                    return result;
                }
            }
        }
        return "ERROR";
    }

    private static String readArgument(@Nonnull StringReader reader) {
        int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    public static String readStringUntil(StringReader reader, char... t) throws CommandSyntaxException {
        List<Character> terminators = new ArrayList();
        for (char c : t) {
            terminators.add(c);
        }
        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (reader.canRead()) {
            char c = reader.read();
            if (escaped) {
                if (!terminators.contains(c) && c != '\\') {
                    reader.setCursor(reader.getCursor() - 1);
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().createWithContext(reader, String.valueOf(c));
                }
                result.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else {
                if (terminators.contains(c)) {
                    return result.toString();
                }
                result.append(c);
            }
        }
        return result.toString();
    }

    public static CommandSyntaxException NoValueException(StringReader reader) {
        return new CommandSyntaxException(EXCEPTION_TYPE, LCText.ARGUMENT_MONEY_VALUE_NO_VALUE.get(), reader.getString(), reader.getCursor());
    }
}