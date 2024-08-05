package net.minecraft.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Arrays;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AngleArgument implements ArgumentType<AngleArgument.SingleAngle> {

    private static final Collection<String> EXAMPLES = Arrays.asList("0", "~", "~-5");

    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(Component.translatable("argument.angle.incomplete"));

    public static final SimpleCommandExceptionType ERROR_INVALID_ANGLE = new SimpleCommandExceptionType(Component.translatable("argument.angle.invalid"));

    public static AngleArgument angle() {
        return new AngleArgument();
    }

    public static float getAngle(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return ((AngleArgument.SingleAngle) commandContextCommandSourceStack0.getArgument(string1, AngleArgument.SingleAngle.class)).getAngle((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public AngleArgument.SingleAngle parse(StringReader stringReader0) throws CommandSyntaxException {
        if (!stringReader0.canRead()) {
            throw ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        } else {
            boolean $$1 = WorldCoordinate.isRelative(stringReader0);
            float $$2 = stringReader0.canRead() && stringReader0.peek() != ' ' ? stringReader0.readFloat() : 0.0F;
            if (!Float.isNaN($$2) && !Float.isInfinite($$2)) {
                return new AngleArgument.SingleAngle($$2, $$1);
            } else {
                throw ERROR_INVALID_ANGLE.createWithContext(stringReader0);
            }
        }
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static final class SingleAngle {

        private final float angle;

        private final boolean isRelative;

        SingleAngle(float float0, boolean boolean1) {
            this.angle = float0;
            this.isRelative = boolean1;
        }

        public float getAngle(CommandSourceStack commandSourceStack0) {
            return Mth.wrapDegrees(this.isRelative ? this.angle + commandSourceStack0.getRotation().y : this.angle);
        }
    }
}