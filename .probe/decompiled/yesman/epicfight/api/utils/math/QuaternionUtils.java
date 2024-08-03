package yesman.epicfight.api.utils.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class QuaternionUtils {

    public static QuaternionUtils.Axis XN = new QuaternionUtils.Axis(-1.0F, 0.0F, 0.0F);

    public static QuaternionUtils.Axis XP = new QuaternionUtils.Axis(1.0F, 0.0F, 0.0F);

    public static QuaternionUtils.Axis YN = new QuaternionUtils.Axis(0.0F, -1.0F, 0.0F);

    public static QuaternionUtils.Axis YP = new QuaternionUtils.Axis(0.0F, 1.0F, 0.0F);

    public static QuaternionUtils.Axis ZN = new QuaternionUtils.Axis(0.0F, 0.0F, -1.0F);

    public static QuaternionUtils.Axis ZP = new QuaternionUtils.Axis(0.0F, 0.0F, 1.0F);

    public static Quaternionf rotationDegrees(Vector3f axis, float degress) {
        float angle = degress * (float) Math.PI / 180.0F;
        return rotation(axis, angle);
    }

    public static Quaternionf rotation(Vector3f axis, float angle) {
        Quaternionf quat = new Quaternionf();
        quat.setAngleAxis(angle, axis.x, axis.y, axis.z);
        return quat;
    }

    public static class Axis {

        private final Vector3f axis;

        public Axis(float x, float y, float z) {
            this.axis = new Vector3f(x, y, z);
        }

        public Quaternionf rotation(float angle) {
            return QuaternionUtils.rotation(this.axis, angle);
        }

        public Quaternionf rotationDegrees(float degrees) {
            return QuaternionUtils.rotationDegrees(this.axis, degrees);
        }
    }
}