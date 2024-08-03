package net.mehvahdjukaar.moonlight.api.client.util;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.mojang.math.Axis;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class RotHlpr {

    public static final Quaternionf Y180 = Axis.YP.rotationDegrees(180.0F);

    public static final Quaternionf Y90 = Axis.YP.rotationDegrees(90.0F);

    public static final Quaternionf Y45 = Axis.YP.rotationDegrees(45.0F);

    public static final Quaternionf YN45 = Axis.YP.rotationDegrees(-45.0F);

    public static final Quaternionf YN90 = Axis.YP.rotationDegrees(-90.0F);

    public static final Quaternionf YN180 = Axis.YP.rotationDegrees(-180.0F);

    public static final Quaternionf X180 = Axis.XP.rotationDegrees(180.0F);

    public static final Quaternionf X90 = Axis.XP.rotationDegrees(90.0F);

    public static final Quaternionf X22 = Axis.XP.rotationDegrees(22.5F);

    public static final Quaternionf XN22 = Axis.XP.rotationDegrees(-22.5F);

    public static final Quaternionf XN90 = Axis.XP.rotationDegrees(-90.0F);

    public static final Quaternionf XN180 = Axis.XP.rotationDegrees(-180.0F);

    public static final Quaternionf Z180 = Axis.ZP.rotationDegrees(180.0F);

    public static final Quaternionf Z135 = Axis.ZP.rotationDegrees(135.0F);

    public static final Quaternionf Z90 = Axis.ZP.rotationDegrees(90.0F);

    public static final Quaternionf ZN45 = Axis.ZP.rotationDegrees(-45.0F);

    public static final Quaternionf ZN90 = Axis.ZP.rotationDegrees(-90.0F);

    public static final Quaternionf ZN180 = Axis.ZP.rotationDegrees(-180.0F);

    private static final Map<Direction, Quaternionf> DIR2ROT = Maps.newEnumMap((Map) Arrays.stream(Direction.values()).collect(Collectors.toMap(Functions.identity(), d -> d.getOpposite().getRotation().mul(XN90))));

    private static final Map<Integer, Quaternionf> YAW2ROT = (Map<Integer, Quaternionf>) Arrays.stream(Direction.values()).filter(d -> d.getAxis() != Direction.Axis.Y).map(d -> (int) (-d.toYRot())).collect(Collectors.toMap(Functions.identity(), y -> Axis.YP.rotationDegrees((float) y.intValue())));

    private static final Quaternionf def = Axis.YP.rotationDegrees(0.0F);

    public static Quaternionf rot(Direction dir) {
        return (Quaternionf) DIR2ROT.get(dir);
    }

    public static Quaternionf rot(int rot) {
        return (Quaternionf) YAW2ROT.getOrDefault(rot, def);
    }

    @Deprecated(forRemoval = true)
    public static Vector3f rotateVertexOnCenterBy(float x, float y, float z, Matrix4f pTransform) {
        Vector3f v = new Vector3f(x, y, z);
        rotateVertexBy(v, new Vector3f(0.5F, 0.5F, 0.5F), pTransform);
        return v;
    }

    @Deprecated(forRemoval = true)
    public static void rotateVertexBy(Vector3f pPos, Vector3f pOrigin, Matrix4f pTransform) {
        Vector4f vector4f = new Vector4f(pPos.x() - pOrigin.x(), pPos.y() - pOrigin.y(), pPos.z() - pOrigin.z(), 1.0F);
        vector4f.mul(pTransform);
        pPos.set(vector4f.x() + pOrigin.x(), vector4f.y() + pOrigin.y(), vector4f.z() + pOrigin.z());
    }

    @Deprecated(forRemoval = true)
    public static Direction rotateDirection(Direction direction, Matrix4f transform) {
        Vec3i d = direction.getNormal();
        Vector3f normal = new Vector3f((float) d.getX(), (float) d.getY(), (float) d.getZ());
        rotateVertexBy(normal, new Vector3f(), transform);
        return Direction.getNearest(normal.x(), normal.y(), normal.z());
    }
}