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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;

public class GameModeArgument implements ArgumentType<GameType> {

    private static final Collection<String> EXAMPLES = (Collection<String>) Stream.of(GameType.SURVIVAL, GameType.CREATIVE).map(GameType::m_46405_).collect(Collectors.toList());

    private static final GameType[] VALUES = GameType.values();

    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(p_260119_ -> Component.translatable("argument.gamemode.invalid", p_260119_));

    public GameType parse(StringReader stringReader0) throws CommandSyntaxException {
        String $$1 = stringReader0.readUnquotedString();
        GameType $$2 = GameType.byName($$1, null);
        if ($$2 == null) {
            throw ERROR_INVALID.createWithContext(stringReader0, $$1);
        } else {
            return $$2;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return commandContextS0.getSource() instanceof SharedSuggestionProvider ? SharedSuggestionProvider.suggest(Arrays.stream(VALUES).map(GameType::m_46405_), suggestionsBuilder1) : Suggestions.empty();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static GameModeArgument gameMode() {
        return new GameModeArgument();
    }

    public static GameType getGameMode(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return (GameType) commandContextCommandSourceStack0.getArgument(string1, GameType.class);
    }
}