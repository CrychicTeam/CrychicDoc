package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class ColorArgument implements ArgumentType<ChatFormatting> {

    private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");

    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(p_85470_ -> Component.translatable("argument.color.invalid", p_85470_));

    private ColorArgument() {
    }

    public static ColorArgument color() {
        return new ColorArgument();
    }

    public static ChatFormatting getColor(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (ChatFormatting) commandContextCommandSourceStack0.getArgument(string1, ChatFormatting.class);
    }

    public ChatFormatting parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        ChatFormatting $$2 = ChatFormatting.getByName($$1);
        if ($$2 != null && !$$2.isFormat()) {
            return $$2;
        } else {
            throw ERROR_INVALID_VALUE.create($$1);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(ChatFormatting.getNames(true, false), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}