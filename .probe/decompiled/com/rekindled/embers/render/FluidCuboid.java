package com.rekindled.embers.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import org.joml.Vector3f;

public class FluidCuboid {

    public static final Map<Direction, FluidCuboid.FluidFace> DEFAULT_FACES = new EnumMap(Direction.class);

    public static final Map<Direction, FluidCuboid.FluidFace> FLOWING_DOWN_FACES = new EnumMap(Direction.class);

    private final Vector3f from;

    private final Vector3f to;

    private final Map<Direction, FluidCuboid.FluidFace> faces;

    @Nullable
    private Vector3f fromScaled;

    @Nullable
    private Vector3f toScaled;

    public FluidCuboid(Vector3f from, Vector3f to, Map<Direction, FluidCuboid.FluidFace> faces) {
        this.from = from;
        this.to = to;
        this.faces = faces;
    }

    public Vector3f getFrom() {
        return this.from;
    }

    public Vector3f getTo() {
        return this.to;
    }

    public Map<Direction, FluidCuboid.FluidFace> getFaces() {
        return this.faces;
    }

    @Nullable
    public FluidCuboid.FluidFace getFace(Direction face) {
        return (FluidCuboid.FluidFace) this.faces.get(face);
    }

    public Vector3f getFromScaled() {
        if (this.fromScaled == null) {
            this.fromScaled = new Vector3f(this.from);
            this.fromScaled.mul(0.0625F);
        }
        return this.fromScaled;
    }

    public Vector3f getToScaled() {
        if (this.toScaled == null) {
            this.toScaled = new Vector3f(this.to);
            this.toScaled.mul(0.0625F);
        }
        return this.toScaled;
    }

    public static FluidCuboid fromJson(JsonObject json) {
        Vector3f from = arrayToVector(json, "from");
        Vector3f to = arrayToVector(json, "to");
        Map<Direction, FluidCuboid.FluidFace> faces = getFaces(json);
        return new FluidCuboid(from, to, faces);
    }

    public static List<FluidCuboid> listFromJson(JsonObject parent, String key) {
        JsonElement json = parent.get(key);
        if (json.isJsonObject()) {
            return Collections.singletonList(fromJson(json.getAsJsonObject()));
        } else if (json.isJsonArray()) {
            return parseList(json.getAsJsonArray(), key, FluidCuboid::fromJson);
        } else {
            throw new JsonSyntaxException("Invalid fluid '" + key + "', must be an array or an object");
        }
    }

    protected static Map<Direction, FluidCuboid.FluidFace> getFaces(JsonObject json) {
        if (!json.has("faces")) {
            return DEFAULT_FACES;
        } else {
            Map<Direction, FluidCuboid.FluidFace> faces = new EnumMap(Direction.class);
            JsonObject object = GsonHelper.getAsJsonObject(json, "faces");
            for (Entry<String, JsonElement> entry : object.entrySet()) {
                String name = (String) entry.getKey();
                Direction dir = Direction.byName(name);
                if (dir == null) {
                    throw new JsonSyntaxException("Unknown face '" + name + "'");
                }
                JsonObject face = GsonHelper.convertToJsonObject((JsonElement) entry.getValue(), name);
                boolean flowing = GsonHelper.getAsBoolean(face, "flowing", false);
                int rotation = getRotation(face, "rotation");
                faces.put(dir, new FluidCuboid.FluidFace(flowing, rotation));
            }
            return faces;
        }
    }

    public static int getRotation(JsonObject json, String key) {
        int i = GsonHelper.getAsInt(json, key, 0);
        if (i >= 0 && i % 90 == 0 && i / 90 <= 3) {
            return i;
        } else {
            throw new JsonParseException("Invalid '" + key + "' " + i + " found, only 0/90/180/270 allowed");
        }
    }

    public static Vector3f arrayToVector(JsonObject json, String name) {
        return arrayToObject(json, name, 3, arr -> new Vector3f(arr[0], arr[1], arr[2]));
    }

    public static <T> T arrayToObject(JsonObject json, String name, int size, Function<float[], T> mapper) {
        JsonArray array = GsonHelper.getAsJsonArray(json, name);
        if (array.size() != size) {
            throw new JsonParseException("Expected " + size + " " + name + " values, found: " + array.size());
        } else {
            float[] vec = new float[size];
            for (int i = 0; i < size; i++) {
                vec[i] = GsonHelper.convertToFloat(array.get(i), name + "[" + i + "]");
            }
            return (T) mapper.apply(vec);
        }
    }

    public static <T> List<T> parseList(JsonArray array, String name, BiFunction<JsonElement, String, T> mapper) {
        if (array.size() == 0) {
            throw new JsonSyntaxException(name + " must have at least 1 element");
        } else {
            Builder<T> builder = ImmutableList.builder();
            for (int i = 0; i < array.size(); i++) {
                builder.add(mapper.apply(array.get(i), name + "[" + i + "]"));
            }
            return builder.build();
        }
    }

    public static <T> List<T> parseList(JsonArray array, String name, Function<JsonObject, T> mapper) {
        return parseList(array, name, (BiFunction<JsonElement, String, T>) ((element, s) -> mapper.apply(GsonHelper.convertToJsonObject(element, s))));
    }

    static {
        for (Direction direction : Direction.values()) {
            DEFAULT_FACES.put(direction, FluidCuboid.FluidFace.NORMAL);
            if (direction.getAxis() == Direction.Axis.Y) {
                FLOWING_DOWN_FACES.put(direction, FluidCuboid.FluidFace.NORMAL);
            } else {
                FLOWING_DOWN_FACES.put(direction, FluidCuboid.FluidFace.DOWN);
            }
        }
    }

    public static record FluidFace(boolean isFlowing, int rotation) {

        public static final FluidCuboid.FluidFace NORMAL = new FluidCuboid.FluidFace(false, 0);

        public static final FluidCuboid.FluidFace DOWN = new FluidCuboid.FluidFace(true, 0);
    }
}