package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class RotationArgument implements ArgumentType<Coordinates> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~-5 ~5");

    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.rotation.incomplete"));

    public static RotationArgument rotation() {
        return new RotationArgument();
    }

    public static Coordinates getRotation(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (Coordinates) commandContextCommandSourceStack0.getArgument(string1, Coordinates.class);
    }

    public Coordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        if (!stringReader0.canRead()) {
            throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        } else {
            WorldCoordinate $$2 = WorldCoordinate.parseDouble(stringReader0, false);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                WorldCoordinate $$3 = WorldCoordinate.parseDouble(stringReader0, false);
                return new WorldCoordinates($$3, $$2, new WorldCoordinate(true, 0.0));
            } else {
                stringReader0.setCursor($$1);
                throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
            }
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}