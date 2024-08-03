package team.lodestar.lodestone.helpers;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class AngleHelper {

    public static float horizontalAngle(Direction facing) {
        if (facing.getAxis().isVertical()) {
            return 0.0F;
        } else {
            float angle = facing.toYRot();
            if (facing.getAxis() == Direction.Axis.X) {
                angle = -angle;
            }
            return angle;
        }
    }

    public static float verticalAngle(Direction facing) {
        return facing == Direction.UP ? -90.0F : (facing == Direction.DOWN ? 90.0F : 0.0F);
    }

    public static float rad(double angle) {
        return angle == 0.0 ? 0.0F : (float) (angle / 180.0 * Math.PI);
    }

    public static float deg(double angle) {
        return angle == 0.0 ? 0.0F : (float) (angle * 180.0 / Math.PI);
    }

    public static float angleLerp(double pct, double current, double target) {
        return (float) (current + (double) getShortestAngleDiff(current, target) * pct);
    }

    public static float getShortestAngleDiff(double current, double target) {
        current %= 360.0;
        target %= 360.0;
        return (float) (((target - current) % 360.0 + 540.0) % 360.0 - 180.0);
    }

    public static float getShortestAngleDiff(double current, double target, float hint) {
        float diff = getShortestAngleDiff(current, target);
        return Mth.equal(Math.abs(diff), 180.0F) && Math.signum(diff) != Math.signum(hint) ? diff + 360.0F * Math.signum(hint) : diff;
    }
}