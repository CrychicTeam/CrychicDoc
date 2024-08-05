package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class SwizzleArgument implements ArgumentType<EnumSet<Direction.Axis>> {

    private static final Collection<String> EXAMPLES = Arrays.asList("xyz", "x");

    private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType(Component.translatable("arguments.swizzle.invalid"));

    public static SwizzleArgument swizzle() {
        return new SwizzleArgument();
    }

    public static EnumSet<Direction.Axis> getSwizzle(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (EnumSet<Direction.Axis>) commandContextCommandSourceStack0.getArgument(string1, EnumSet.class);
    }

    public EnumSet<Direction.Axis> parse(StringReader stringReader0) throws CommandSyntaxException {
        EnumSet<Direction.Axis> $$1 = EnumSet.noneOf(Direction.Axis.class);
        while (stringReader0.canRead() && stringReader0.peek() != ' ') {
            char $$2 = stringReader0.read();
            Direction.Axis $$6 = switch($$2) {
                case 'x' ->
                    Direction.Axis.X;
                case 'y' ->
                    Direction.Axis.Y;
                case 'z' ->
                    Direction.Axis.Z;
                default ->
                    throw ERROR_INVALID.create();
            };
            if ($$1.contains($$6)) {
                throw ERROR_INVALID.create();
            }
            $$1.add($$6);
        }
        return $$1;
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}