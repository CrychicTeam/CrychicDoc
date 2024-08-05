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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class Vec2Argument implements ArgumentType<Coordinates> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "0.1 -0.5", "~1 ~-2");

    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.pos2d.incomplete"));

    private final boolean centerCorrect;

    public Vec2Argument(boolean boolean0) {
        this.centerCorrect = boolean0;
    }

    public static Vec2Argument vec2() {
        return new Vec2Argument(true);
    }

    public static Vec2Argument vec2(boolean boolean0) {
        return new Vec2Argument(boolean0);
    }

    public static Vec2 getVec2(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        Vec3 $$2 = ((Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class)).getPosition((CommandSourceStack) commandContextCommandSourceStack0.getSource());
        return new Vec2((float) $$2.x, (float) $$2.z);
    }

    public Coordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        if (!stringReader0.canRead()) {
            throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        } else {
            WorldCoordinate $$2 = WorldCoordinate.parseDouble(stringReader0, this.centerCorrect);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                WorldCoordinate $$3 = WorldCoordinate.parseDouble(stringReader0, this.centerCorrect);
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
                $$3 = ((SharedSuggestionProvider) commandContextS0.getSource()).getAbsoluteCoordinates();
            }
            return SharedSuggestionProvider.suggest2DCoordinates($$2, $$3, suggestionsBuilder1, Commands.createValidator(this::parse));
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}