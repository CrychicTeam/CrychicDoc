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
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class Vec3Argument implements ArgumentType<Coordinates> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5");

    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos3d.incomplete"));

    public static final SimpleCommandExceptionType ERROR_MIXED_TYPE = new SimpleCommandExceptionType(Component.translatable("argument.pos.mixed"));

    private final boolean centerCorrect;

    public Vec3Argument(boolean boolean0) {
        this.centerCorrect = boolean0;
    }

    public static Vec3Argument vec3() {
        return new Vec3Argument(true);
    }

    public static Vec3Argument vec3(boolean boolean0) {
        return new Vec3Argument(boolean0);
    }

    public static Vec3 getVec3(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return ((Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class)).getPosition((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static Coordinates getCoordinates(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class);
    }

    public Coordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        return (Coordinates) (stringReader0.canRead() && stringReader0.peek() == '^' ? LocalCoordinates.parse(stringReader0) : WorldCoordinates.parseDouble(stringReader0, this.centerCorrect));
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
                $$3 = ((SharedSuggestionProvider) commandContextS0.getSource()).getAbsoluteCoordinates();
            }
            return SharedSuggestionProvider.suggestCoordinates($$2, $$3, suggestionsBuilder1, Commands.createValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}