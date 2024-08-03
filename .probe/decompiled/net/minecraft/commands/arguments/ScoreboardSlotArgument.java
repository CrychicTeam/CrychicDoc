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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardSlotArgument implements ArgumentType<Integer> {

    private static final Collection<String> EXAMPLES = Arrays.asList("sidebar", "foo.bar");

    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(p_109203_ -> Component.translatable("argument.scoreboardDisplaySlot.invalid", p_109203_));

    private ScoreboardSlotArgument() {
    }

    public static ScoreboardSlotArgument displaySlot() {
        return new ScoreboardSlotArgument();
    }

    public static int getDisplaySlot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Integer) commandContextCommandSourceStack0.getArgument(string1, Integer.class);
    }

    public Integer parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        int $$2 = Scoreboard.getDisplaySlotByName($$1);
        if ($$2 == -1) {
            throw ERROR_INVALID_VALUE.create($$1);
        } else {
            return $$2;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(Scoreboard.getDisplaySlotNames(), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}