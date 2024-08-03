package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ColumnPos;

public class ColumnPosArgument implements ArgumentType<Coordinates> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~1 ~-2", "^ ^", "^-1 ^0");

    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos2d.incomplete"));

    public static ColumnPosArgument columnPos() {
        return new ColumnPosArgument();
    }

    public static ColumnPos getColumnPos(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        BlockPos $$2 = ((Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class)).getBlockPos((CommandSourceStack) commandContextCommandSourceStack0.getSource());
        return new ColumnPos($$2.m_123341_(), $$2.m_123343_());
    }

    public Coordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        if (!stringReader0.canRead()) {
            throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        } else {
            WorldCoordinate $$2 = WorldCoordinate.parseInt(stringReader0);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                WorldCoordinate $$3 = WorldCoordinate.parseInt(stringReader0);
                return new WorldCoordinates($$2, new WorldCoordinate(true, 0.0), $$3);
            } else {
                stringReader0.setCursor($$1);
                throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
            }
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        if (!(commandContextS0.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        } else {
            String $$2 = suggestionsBuilder1.getRemaining();
            Collection<SharedSuggestionProvider.TextCoordinates> $$3;
            if (!$$2.isEmpty() && $$2.charAt(0) == '^') {
                $$3 = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
            } else {
                $$3 = ((SharedSuggestionProvider) commandContextS0.getSource()).getRelevantCoordinates();
            }
            return SharedSuggestionProvider.suggest2DCoordinates($$2, $$3, suggestionsBuilder1, Commands.createValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}