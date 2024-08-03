package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

public class WorldCoordinate {

    private static final char PREFIX_RELATIVE = '~';

    public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.double"));

    public static final SimpleCommandExceptionType ERROR_EXPECTED_INT = new SimpleCommandExceptionType(Component.translatable("argument.pos.missing.int"));

    private final boolean relative;

    private final double value;

    public WorldCoordinate(boolean boolean0, double double1) {
        this.relative = boolean0;
        this.value = double1;
    }

    public double get(double double0) {
        return this.relative ? this.value + double0 : this.value;
    }

    public static WorldCoordinate parseDouble(StringReader stringReader0, boolean boolean1) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader0);
        } else if (!stringReader0.canRead()) {
            throw ERROR_EXPECTED_DOUBLE.createWithContext(stringReader0);
        } else {
            boolean $$2 = isRelative(stringReader0);
            int $$3 = stringReader0.getCursor();
            double $$4 = stringReader0.canRead() && stringReader0.peek() != ' ' ? stringReader0.readDouble() : 0.0;
            String $$5 = stringReader0.getString().substring($$3, stringReader0.getCursor());
            if ($$2 && $$5.isEmpty()) {
                return new WorldCoordinate(true, 0.0);
            } else {
                if (!$$5.contains(".") && !$$2 && boolean1) {
                    $$4 += 0.5;
                }
                return new WorldCoordinate($$2, $$4);
            }
        }
    }

    public static WorldCoordinate parseInt(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '^') {
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader0);
        } else if (!stringReader0.canRead()) {
            throw ERROR_EXPECTED_INT.createWithContext(stringReader0);
        } else {
            boolean $$1 = isRelative(stringReader0);
            double $$2;
            if (stringReader0.canRead() && stringReader0.peek() != ' ') {
                $$2 = $$1 ? stringReader0.readDouble() : (double) stringReader0.readInt();
            } else {
                $$2 = 0.0;
            }
            return new WorldCoordinate($$1, $$2);
        }
    }

    public static boolean isRelative(StringReader stringReader0) {
        boolean $$1;
        if (stringReader0.peek() == '~') {
            $$1 = true;
            stringReader0.skip();
        } else {
            $$1 = false;
        }
        return $$1;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (!(object0 instanceof WorldCoordinate $$1)) {
            return false;
        } else {
            return this.relative != $$1.relative ? false : Double.compare($$1.value, this.value) == 0;
        }
    }

    public int hashCode() {
        int $$0 = this.relative ? 1 : 0;
        long $$1 = Double.doubleToLongBits(this.value);
        return 31 * $$0 + (int) ($$1 ^ $$1 >>> 32);
    }

    public boolean isRelative() {
        return this.relative;
    }
}