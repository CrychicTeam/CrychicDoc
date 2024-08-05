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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class TestFunctionArgument implements ArgumentType<TestFunction> {

    private static final Collection<String> EXAMPLES = Arrays.asList("techtests.piston", "techtests");

    public TestFunction parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        Optional<TestFunction> $$2 = GameTestRegistry.findTestFunction($$1);
        if ($$2.isPresent()) {
            return (TestFunction) $$2.get();
        } else {
            Message $$3 = Component.literal("No such test: " + $$1);
            throw new CommandSyntaxException(new SimpleCommandExceptionType($$3), $$3);
        }
    }

    public static TestFunctionArgument testFunctionArgument() {
        return new TestFunctionArgument();
    }

    public static TestFunction getTestFunction(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (TestFunction) commandContextCommandSourceStack0.getArgument(string1, TestFunction.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        Stream<String> $$2 = GameTestRegistry.getAllTestFunctions().stream().map(TestFunction::m_128075_);
        return SharedSuggestionProvider.suggest($$2, suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}