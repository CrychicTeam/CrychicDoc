package io.github.lightman314.lightmanscurrency.common.commands.arguments;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.common.core.variants.Color;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;

public class ColorArgument implements ArgumentType<Integer> {

    private static final SimpleCommandExceptionType ERROR_NOT_VALID = new SimpleCommandExceptionType(LCText.ARGUMENT_COLOR_INVALID.get());

    private ColorArgument() {
    }

    public static ColorArgument argument() {
        return new ColorArgument();
    }

    public static int getColor(CommandContext<CommandSourceStack> commandContext, String name) {
        return (Integer) commandContext.getArgument(name, Integer.class);
    }

    public Integer parse(StringReader reader) throws CommandSyntaxException {
        String color = reader.readUnquotedString();
        if (color.startsWith("0x")) {
            return Integer.decode(color);
        } else if (isNumerical(color)) {
            return Integer.parseInt(color);
        } else {
            Color c = Color.getFromPrettyName(color);
            if (c != null) {
                return c.hexColor;
            } else {
                throw ERROR_NOT_VALID.createWithContext(reader);
            }
        }
    }

    private static boolean isNumerical(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Color c : Color.values()) {
            builder.suggest(c.toString());
        }
        builder.suggest("0xFFFFFF");
        return builder.buildFuture();
    }

    public Collection<String> getExamples() {
        return ImmutableList.of("0xFFFFFF", "16777215", "WHITE");
    }
}