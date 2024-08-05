package net.minecraft.commands.arguments.coordinates;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class WorldCoordinates implements Coordinates {

    private final WorldCoordinate x;

    private final WorldCoordinate y;

    private final WorldCoordinate z;

    public WorldCoordinates(WorldCoordinate worldCoordinate0, WorldCoordinate worldCoordinate1, WorldCoordinate worldCoordinate2) {
        this.x = worldCoordinate0;
        this.y = worldCoordinate1;
        this.z = worldCoordinate2;
    }

    @Override
    public Vec3 getPosition(CommandSourceStack commandSourceStack0) {
        Vec3 $$1 = commandSourceStack0.getPosition();
        return new Vec3(this.x.get($$1.x), this.y.get($$1.y), this.z.get($$1.z));
    }

    @Override
    public Vec2 getRotation(CommandSourceStack commandSourceStack0) {
        Vec2 $$1 = commandSourceStack0.getRotation();
        return new Vec2((float) this.x.get((double) $$1.x), (float) this.y.get((double) $$1.y));
    }

    @Override
    public boolean isXRelative() {
        return this.x.isRelative();
    }

    @Override
    public boolean isYRelative() {
        return this.y.isRelative();
    }

    @Override
    public boolean isZRelative() {
        return this.z.isRelative();
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (!(object0 instanceof WorldCoordinates $$1)) {
            return false;
        } else if (!this.x.equals($$1.x)) {
            return false;
        } else {
            return !this.y.equals($$1.y) ? false : this.z.equals($$1.z);
        }
    }

    public static WorldCoordinates parseInt(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        WorldCoordinate $$2 = WorldCoordinate.parseInt(stringReader0);
        if (stringReader0.canRead() && stringReader0.peek() == ' ') {
            stringReader0.skip();
            WorldCoordinate $$3 = WorldCoordinate.parseInt(stringReader0);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                WorldCoordinate $$4 = WorldCoordinate.parseInt(stringReader0);
                return new WorldCoordinates($$2, $$3, $$4);
            } else {
                stringReader0.setCursor($$1);
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
            }
        } else {
            stringReader0.setCursor($$1);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        }
    }

    public static WorldCoordinates parseDouble(StringReader stringReader0, boolean boolean1) throws CommandSyntaxException {
        int $$2 = stringReader0.getCursor();
        WorldCoordinate $$3 = WorldCoordinate.parseDouble(stringReader0, boolean1);
        if (stringReader0.canRead() && stringReader0.peek() == ' ') {
            stringReader0.skip();
            WorldCoordinate $$4 = WorldCoordinate.parseDouble(stringReader0, false);
            if (stringReader0.canRead() && stringReader0.peek() == ' ') {
                stringReader0.skip();
                WorldCoordinate $$5 = WorldCoordinate.parseDouble(stringReader0, boolean1);
                return new WorldCoordinates($$3, $$4, $$5);
            } else {
                stringReader0.setCursor($$2);
                throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
            }
        } else {
            stringReader0.setCursor($$2);
            throw Vec3Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader0);
        }
    }

    public static WorldCoordinates absolute(double double0, double double1, double double2) {
        return new WorldCoordinates(new WorldCoordinate(false, double0), new WorldCoordinate(false, double1), new WorldCoordinate(false, double2));
    }

    public static WorldCoordinates absolute(Vec2 vec0) {
        return new WorldCoordinates(new WorldCoordinate(false, (double) vec0.x), new WorldCoordinate(false, (double) vec0.y), new WorldCoordinate(true, 0.0));
    }

    public static WorldCoordinates current() {
        return new WorldCoordinates(new WorldCoordinate(true, 0.0), new WorldCoordinate(true, 0.0), new WorldCoordinate(true, 0.0));
    }

    public int hashCode() {
        int $$0 = this.x.hashCode();
        $$0 = 31 * $$0 + this.y.hashCode();
        return 31 * $$0 + this.z.hashCode();
    }
}