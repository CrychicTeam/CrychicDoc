package net.minecraftforge.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class TransformationHelper {

    private static final double THRESHOLD = 0.9995;

    public static Quaternionf quatFromXYZ(Vector3f xyz, boolean degrees) {
        return quatFromXYZ(xyz.x, xyz.y, xyz.z, degrees);
    }

    public static Quaternionf quatFromXYZ(float[] xyz, boolean degrees) {
        return quatFromXYZ(xyz[0], xyz[1], xyz[2], degrees);
    }

    public static Quaternionf quatFromXYZ(float x, float y, float z, boolean degrees) {
        float conversionFactor = degrees ? (float) (Math.PI / 180.0) : 1.0F;
        return new Quaternionf().rotationXYZ(x * conversionFactor, y * conversionFactor, z * conversionFactor);
    }

    public static Quaternionf makeQuaternion(float[] values) {
        return new Quaternionf(values[0], values[1], values[2], values[3]);
    }

    public static Vector3f lerp(Vector3f from, Vector3f to, float progress) {
        Vector3f res = new Vector3f(from);
        res.lerp(to, progress);
        return res;
    }

    public static Quaternionf slerp(Quaternionfc v0, Quaternionfc v1, float t) {
        float dot = v0.x() * v1.x() + v0.y() * v1.y() + v0.z() * v1.z() + v0.w() * v1.w();
        if (dot < 0.0F) {
            v1 = new Quaternionf(-v1.x(), -v1.y(), -v1.z(), -v1.w());
            dot = -dot;
        }
        if ((double) dot > 0.9995) {
            float x = Mth.lerp(t, v0.x(), v1.x());
            float y = Mth.lerp(t, v0.y(), v1.y());
            float z = Mth.lerp(t, v0.z(), v1.z());
            float w = Mth.lerp(t, v0.w(), v1.w());
            return new Quaternionf(x, y, z, w);
        } else {
            float angle01 = (float) Math.acos((double) dot);
            float angle0t = angle01 * t;
            float sin0t = Mth.sin(angle0t);
            float sin01 = Mth.sin(angle01);
            float sin1t = Mth.sin(angle01 - angle0t);
            float s1 = sin0t / sin01;
            float s0 = sin1t / sin01;
            return new Quaternionf(s0 * v0.x() + s1 * v1.x(), s0 * v0.y() + s1 * v1.y(), s0 * v0.z() + s1 * v1.z(), s0 * v0.w() + s1 * v1.w());
        }
    }

    public static Transformation slerp(Transformation one, Transformation that, float progress) {
        return new Transformation(lerp(one.getTranslation(), that.getTranslation(), progress), slerp(one.getLeftRotation(), that.getLeftRotation(), progress), lerp(one.getScale(), that.getScale(), progress), slerp(one.getRightRotation(), that.getRightRotation(), progress));
    }

    public static boolean epsilonEquals(Vector4f v1, Vector4f v2, float epsilon) {
        return Mth.abs(v1.x() - v2.x()) < epsilon && Mth.abs(v1.y() - v2.y()) < epsilon && Mth.abs(v1.z() - v2.z()) < epsilon && Mth.abs(v1.w() - v2.w()) < epsilon;
    }

    public static class Deserializer implements JsonDeserializer<Transformation> {

        public Transformation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                String transform = json.getAsString();
                if (transform.equals("identity")) {
                    return Transformation.identity();
                } else {
                    throw new JsonParseException("TRSR: unknown default string: " + transform);
                }
            } else if (json.isJsonArray()) {
                return new Transformation(parseMatrix(json));
            } else if (!json.isJsonObject()) {
                throw new JsonParseException("TRSR: expected array or object, got: " + json);
            } else {
                JsonObject obj = json.getAsJsonObject();
                if (obj.has("matrix")) {
                    Transformation ret = new Transformation(parseMatrix(obj.get("matrix")));
                    if (obj.entrySet().size() > 1) {
                        throw new JsonParseException("TRSR: can't combine matrix and other keys");
                    } else {
                        return ret;
                    }
                } else {
                    Vector3f translation = null;
                    Quaternionf leftRot = null;
                    Vector3f scale = null;
                    Quaternionf rightRot = null;
                    Vector3f origin = TransformationHelper.TransformOrigin.OPPOSING_CORNER.getVector();
                    Set<String> elements = new HashSet(obj.keySet());
                    if (obj.has("translation")) {
                        translation = new Vector3f(parseFloatArray(obj.get("translation"), 3, "Translation"));
                        elements.remove("translation");
                    }
                    if (obj.has("rotation")) {
                        leftRot = parseRotation(obj.get("rotation"));
                        elements.remove("rotation");
                    } else if (obj.has("left_rotation")) {
                        leftRot = parseRotation(obj.get("left_rotation"));
                        elements.remove("left_rotation");
                    }
                    if (obj.has("scale")) {
                        if (!obj.get("scale").isJsonArray()) {
                            try {
                                float s = obj.get("scale").getAsNumber().floatValue();
                                scale = new Vector3f(s, s, s);
                            } catch (ClassCastException var13) {
                                throw new JsonParseException("TRSR scale: expected number or array, got: " + obj.get("scale"));
                            }
                        } else {
                            scale = new Vector3f(parseFloatArray(obj.get("scale"), 3, "Scale"));
                        }
                        elements.remove("scale");
                    }
                    if (obj.has("right_rotation")) {
                        rightRot = parseRotation(obj.get("right_rotation"));
                        elements.remove("right_rotation");
                    } else if (obj.has("post-rotation")) {
                        rightRot = parseRotation(obj.get("post-rotation"));
                        elements.remove("post-rotation");
                    }
                    if (obj.has("origin")) {
                        origin = parseOrigin(obj);
                        elements.remove("origin");
                    }
                    if (!elements.isEmpty()) {
                        throw new JsonParseException("TRSR: can either have single 'matrix' key, or a combination of 'translation', 'rotation' OR 'left_rotation', 'scale', 'post-rotation' (legacy) OR 'right_rotation', 'origin'. Found: " + String.join(", ", elements));
                    } else {
                        Transformation matrix = new Transformation(translation, leftRot, scale, rightRot);
                        return matrix.applyOrigin(new Vector3f(origin));
                    }
                }
            }
        }

        private static Vector3f parseOrigin(JsonObject obj) {
            Vector3f origin = null;
            JsonElement originElement = obj.get("origin");
            if (originElement.isJsonArray()) {
                origin = new Vector3f(parseFloatArray(originElement, 3, "Origin"));
            } else {
                if (!originElement.isJsonPrimitive()) {
                    throw new JsonParseException("Origin: expected an array or one of 'center', 'corner', 'opposing-corner'");
                }
                String originString = originElement.getAsString();
                TransformationHelper.TransformOrigin originEnum = TransformationHelper.TransformOrigin.fromString(originString);
                if (originEnum == null) {
                    throw new JsonParseException("Origin: expected one of 'center', 'corner', 'opposing-corner'");
                }
                origin = originEnum.getVector();
            }
            return origin;
        }

        public static Matrix4f parseMatrix(JsonElement e) {
            if (!e.isJsonArray()) {
                throw new JsonParseException("Matrix: expected an array, got: " + e);
            } else {
                JsonArray m = e.getAsJsonArray();
                if (m.size() != 3) {
                    throw new JsonParseException("Matrix: expected an array of length 3, got: " + m.size());
                } else {
                    Matrix4f matrix = new Matrix4f();
                    for (int rowIdx = 0; rowIdx < 3; rowIdx++) {
                        if (!m.get(rowIdx).isJsonArray()) {
                            throw new JsonParseException("Matrix row: expected an array, got: " + m.get(rowIdx));
                        }
                        JsonArray r = m.get(rowIdx).getAsJsonArray();
                        if (r.size() != 4) {
                            throw new JsonParseException("Matrix row: expected an array of length 4, got: " + r.size());
                        }
                        for (int columnIdx = 0; columnIdx < 4; columnIdx++) {
                            try {
                                matrix.set(columnIdx, rowIdx, r.get(columnIdx).getAsNumber().floatValue());
                            } catch (ClassCastException var7) {
                                throw new JsonParseException("Matrix element: expected number, got: " + r.get(columnIdx));
                            }
                        }
                    }
                    matrix.determineProperties();
                    return matrix;
                }
            }
        }

        public static float[] parseFloatArray(JsonElement e, int length, String prefix) {
            if (!e.isJsonArray()) {
                throw new JsonParseException(prefix + ": expected an array, got: " + e);
            } else {
                JsonArray t = e.getAsJsonArray();
                if (t.size() != length) {
                    throw new JsonParseException(prefix + ": expected an array of length " + length + ", got: " + t.size());
                } else {
                    float[] ret = new float[length];
                    for (int i = 0; i < length; i++) {
                        try {
                            ret[i] = t.get(i).getAsNumber().floatValue();
                        } catch (ClassCastException var7) {
                            throw new JsonParseException(prefix + " element: expected number, got: " + t.get(i));
                        }
                    }
                    return ret;
                }
            }
        }

        public static Quaternionf parseAxisRotation(JsonElement e) {
            if (!e.isJsonObject()) {
                throw new JsonParseException("Axis rotation: object expected, got: " + e);
            } else {
                JsonObject obj = e.getAsJsonObject();
                if (obj.entrySet().size() != 1) {
                    throw new JsonParseException("Axis rotation: expected single axis object, got: " + e);
                } else {
                    Entry<String, JsonElement> entry = (Entry<String, JsonElement>) obj.entrySet().iterator().next();
                    try {
                        Quaternionf ret;
                        if (((String) entry.getKey()).equals("x")) {
                            ret = Axis.XP.rotationDegrees(((JsonElement) entry.getValue()).getAsNumber().floatValue());
                        } else if (((String) entry.getKey()).equals("y")) {
                            ret = Axis.YP.rotationDegrees(((JsonElement) entry.getValue()).getAsNumber().floatValue());
                        } else {
                            if (!((String) entry.getKey()).equals("z")) {
                                throw new JsonParseException("Axis rotation: expected single axis key, got: " + (String) entry.getKey());
                            }
                            ret = Axis.ZP.rotationDegrees(((JsonElement) entry.getValue()).getAsNumber().floatValue());
                        }
                        return ret;
                    } catch (ClassCastException var5) {
                        throw new JsonParseException("Axis rotation value: expected number, got: " + entry.getValue());
                    }
                }
            }
        }

        public static Quaternionf parseRotation(JsonElement e) {
            if (!e.isJsonArray()) {
                if (e.isJsonObject()) {
                    return parseAxisRotation(e);
                } else {
                    throw new JsonParseException("Rotation: expected array or object, got: " + e);
                }
            } else if (!e.getAsJsonArray().get(0).isJsonObject()) {
                if (e.isJsonArray()) {
                    JsonArray array = e.getAsJsonArray();
                    return array.size() == 3 ? TransformationHelper.quatFromXYZ(parseFloatArray(e, 3, "Rotation"), true) : TransformationHelper.makeQuaternion(parseFloatArray(e, 4, "Rotation"));
                } else {
                    throw new JsonParseException("Rotation: expected array or object, got: " + e);
                }
            } else {
                Quaternionf ret = new Quaternionf();
                for (JsonElement a : e.getAsJsonArray()) {
                    ret.mul(parseAxisRotation(a));
                }
                return ret;
            }
        }
    }

    public static enum TransformOrigin implements StringRepresentable {

        CENTER(new Vector3f(0.5F, 0.5F, 0.5F), "center"), CORNER(new Vector3f(), "corner"), OPPOSING_CORNER(new Vector3f(1.0F, 1.0F, 1.0F), "opposing-corner");

        private final Vector3f vec;

        private final String name;

        private TransformOrigin(Vector3f vec, String name) {
            this.vec = vec;
            this.name = name;
        }

        public Vector3f getVector() {
            return this.vec;
        }

        @NotNull
        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Nullable
        public static TransformationHelper.TransformOrigin fromString(String originName) {
            if (CENTER.getSerializedName().equals(originName)) {
                return CENTER;
            } else if (CORNER.getSerializedName().equals(originName)) {
                return CORNER;
            } else {
                return OPPOSING_CORNER.getSerializedName().equals(originName) ? OPPOSING_CORNER : null;
            }
        }
    }
}