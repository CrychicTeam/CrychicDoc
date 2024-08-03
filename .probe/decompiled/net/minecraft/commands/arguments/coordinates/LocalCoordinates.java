package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Objects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class LocalCoordinates implements Coordinates {

    public static final char PREFIX_LOCAL_COORDINATE = '^';

    private final double left;

    private final double up;

    private final double forwards;

    public LocalCoordinates(double double0, double double1, double double2) {
        this.left = double0;
        this.up = double1;
        this.forwards = double2;
    }

    @Override
    public Vec3 getPosition(CommandSourceStack commandSourceStack0) {
        Vec2 $$1 = commandSourceStack0.getRotation();
        Vec3 $$2 = commandSourceStack0.getAnchor().apply(commandSourceStack0);
        float $$3 = Mth.cos(($$1.y + 90.0F) * (float) (Math.PI / 180.0));
        float $$4 = Mth.sin(($$1.y + 90.0F) * (float) (Math.PI / 180.0));
        float $$5 = Mth.cos(-$$1.x * (float) (Math.PI / 180.0));
        float $$6 = Mth.sin(-$$1.x * (float) (Math.PI / 180.0));
        float $$7 = Mth.cos((-$$1.x + 90.0F) * (float) (Math.PI / 180.0));
        float $$8 = Mth.sin((-$$1.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3 $$9 = new Vec3((double) ($$3 * $$5), (double) $$6, (double) ($$4 * $$5));
        Vec3 $$10 = new Vec3((double) ($$3 * $$7), (double) $$8, (double) ($$4 * $$7));
        Vec3 $$11 = $$9.cross($$10).scale(-1.0);
        double $$12 = $$9.x * this.forwards + $$10.x * this.up + $$11.x * this.left;
        double $$13 = $$9.y * this.forwards + $$10.y * this.up + $$11.y * this.left;
        double $$14 = $$9.z * this.forwards + $$10.z * this.up + $$11.z * this.left;
        return new Vec3($$2.x + $$12, $$2.y + $$13, $$2.z + $$14);
    }

    @Override
    public Vec2 getRotation(CommandSourceStack commandSourceStack0) {
        return Vec2.ZERO;
    }

    @Override
    public boolean isXRelative() {
        return true;
    }

    @Override
    public boolean isYRelative() {
        return true;
    }

    @Override
    public boolean isZRelative() {
        return true;
    }

    public static LocalCoordinates parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        double $$2 = readDouble(stringReader0, $$1);
        if (stringReader0.canRead() && stringReader0.peek() == ' ') {
            stringReader0.skip();
            double $$3 = readDouble(stringReader0, $$1);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                double $$4 = readDouble(stringReader0, $$1);
                return new LocalCoordinates($$2, $$3, $$4);
            } else {
                stringReader0.setCursor($$1);
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
            }
        } else {
            stringReader0.setCursor($$1);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        }
    }

    private static double readDouble(StringReader stringReader0, int int1) throws CommandSyntaxException {
        if (!stringReader0.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(stringReader0);
        } else if (stringReader0.peek() != '^') {
            stringReader0.setCursor(int1);
            throw Vec3Argument.ERROR_MIXED_TYPE.createWithContext(stringReader0);
        } else {
            stringReader0.skip();
            return stringReader0.canRead() && stringReader0.peek() != ' ' ? stringReader0.readDouble() : 0.0;
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof LocalCoordinates $$1) ? false : this.left == $$1.left && this.up == $$1.up && this.forwards == $$1.forwards;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.left, this.up, this.forwards });
    }
}