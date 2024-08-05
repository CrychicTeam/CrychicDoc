package net.minecraft.gametest.framework;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class TestClassNameArgument implements ArgumentType<String> {

    private static final Collection<String> EXAMPLES = Arrays.asList("techtests", "mobtests");

    public String parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        if (GameTestRegistry.isTestClass($$1)) {
            return $$1;
        } else {
            Message $$2 = Component.literal("No such test class: " + $$1);
            throw new CommandSyntaxException(new SimpleCommandExceptionType($$2), $$2);
        }
    }

    public static TestClassNameArgument testClassName() {
        return new TestClassNameArgument();
    }

    public static String getTestClassName(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (String) commandContextCommandSourceStack0.getArgument(string1, String.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(GameTestRegistry.getAllTestClassNames().stream(), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}