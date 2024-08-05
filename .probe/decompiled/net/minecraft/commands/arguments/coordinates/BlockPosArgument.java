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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class BlockPosArgument implements ArgumentType<Coordinates> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");

    public static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType(Component.translatable("argument.pos.unloaded"));

    public static final SimpleCommandExceptionType ERROR_OUT_OF_WORLD = new SimpleCommandExceptionType(Component.translatable("argument.pos.outofworld"));

    public static final SimpleCommandExceptionType ERROR_OUT_OF_BOUNDS = new SimpleCommandExceptionType(Component.translatable("argument.pos.outofbounds"));

    public static BlockPosArgument blockPos() {
        return new BlockPosArgument();
    }

    public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        ServerLevel $$2 = ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getLevel();
        return getLoadedBlockPos(commandContextCommandSourceStack0, $$2, string1);
    }

    public static BlockPos getLoadedBlockPos(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ServerLevel serverLevel1, String string2) throws CommandSyntaxException {
        BlockPos $$3 = getBlockPos(commandContextCommandSourceStack0, string2);
        if (!serverLevel1.m_46805_($$3)) {
            throw ERROR_NOT_LOADED.create();
        } else if (!serverLevel1.m_46739_($$3)) {
            throw ERROR_OUT_OF_WORLD.create();
        } else {
            return $$3;
        }
    }

    public static BlockPos getBlockPos(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return ((Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class)).getBlockPos((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static BlockPos getSpawnablePos(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        BlockPos $$2 = getBlockPos(commandContextCommandSourceStack0, string1);
        if (!Level.isInSpawnableBounds($$2)) {
            throw ERROR_OUT_OF_BOUNDS.create();
        } else {
            return $$2;
        }
    }

    public Coordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        return (Coordinates) (stringReader0.canRead() && stringReader0.peek() == '^' ? LocalCoordinates.parse(stringReader0) : WorldCoordinates.parseInt(stringReader0));
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
            return SharedSuggestionProvider.suggestCoordinates($$2, $$3, suggestionsBuilder1, Commands.createValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}