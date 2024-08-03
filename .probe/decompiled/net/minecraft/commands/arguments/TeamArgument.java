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
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class TeamArgument implements ArgumentType<String> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "123");

    private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(p_112095_ -> Component.translatable("team.notFound", p_112095_));

    public static TeamArgument team() {
        return new TeamArgument();
    }

    public static PlayerTeam getTeam(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        String $$2 = (String) commandContextCommandSourceStack0.getArgument(string1, String.class);
        Scoreboard $$3 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().getScoreboard();
        PlayerTeam $$4 = $$3.getPlayerTeam($$2);
        if ($$4 == null) {
            throw ERROR_TEAM_NOT_FOUND.create($$2);
        } else {
            return $$4;
        }
    }

    public String parse(StringReader stringReader0) throws CommandSyntaxException {
        return stringReader0.readUnquotedString();
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return commandContextS0.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(((SharedSuggestionProvider) commandContextS0.getSource()).getAllTeams(), suggestionsBuilder1) : Suggestions.empty();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}