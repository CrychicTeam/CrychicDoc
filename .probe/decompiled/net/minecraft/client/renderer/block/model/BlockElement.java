package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

public class BlockElement {

    private static final boolean DEFAULT_RESCALE = false;

    private static final float MIN_EXTENT = -16.0F;

    private static final float MAX_EXTENT = 32.0F;

    public final Vector3f from;

    public final Vector3f to;

    public final Map<Direction, BlockElementFace> faces;

    public final BlockElementRotation rotation;

    public final boolean shade;

    public BlockElement(Vector3f vectorF0, Vector3f vectorF1, Map<Direction, BlockElementFace> mapDirectionBlockElementFace2, @Nullable BlockElementRotation blockElementRotation3, boolean boolean4) {
        this.from = vectorF0;
        this.to = vectorF1;
        this.faces = mapDirectionBlockElementFace2;
        this.rotation = blockElementRotation3;
        this.shade = boolean4;
        this.fillUvs();
    }

    private void fillUvs() {
        for (Entry<Direction, BlockElementFace> $$0 : this.faces.entrySet()) {
            float[] $$1 = this.uvsByFace((Direction) $$0.getKey());
            ((BlockElementFace) $$0.getValue()).uv.setMissingUv($$1);
        }
    }

    private float[] uvsByFace(Direction direction0) {
        switch(direction0) {
            case DOWN:
                return new float[] { this.from.x(), 16.0F - this.to.z(), this.to.x(), 16.0F - this.from.z() };
            case UP:
                return new float[] { this.from.x(), this.from.z(), this.to.x(), this.to.z() };
            case NORTH:
            default:
                return new float[] { 16.0F - this.to.x(), 16.0F - this.to.y(), 16.0F - this.from.x(), 16.0F - this.from.y() };
            case SOUTH:
                return new float[] { this.from.x(), 16.0F - this.to.y(), this.to.x(), 16.0F - this.from.y() };
            case WEST:
                return new float[] { this.from.z(), 16.0F - this.to.y(), this.to.z(), 16.0F - this.from.y() };
            case EAST:
                return new float[] { 16.0F - this.to.z(), 16.0F - this.to.y(), 16.0F - this.from.z(), 16.0F - this.from.y() };
        }
    }

    protected static class Deserializer implements JsonDeserializer<BlockElement> {

        private static final boolean DEFAULT_SHADE = true;

        public BlockElement deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            Vector3f $$4 = this.getFrom($$3);
            Vector3f $$5 = this.getTo($$3);
            BlockElementRotation $$6 = this.getRotation($$3);
            Map<Direction, BlockElementFace> $$7 = this.getFaces(jsonDeserializationContext2, $$3);
            if ($$3.has("shade") && !GsonHelper.isBooleanValue($$3, "shade")) {
                throw new JsonParseException("Expected shade to be a Boolean");
            } else {
                boolean $$8 = GsonHelper.getAsBoolean($$3, "shade", true);
                return new BlockElement($$4, $$5, $$7, $$6, $$8);
            }
        }

        @Nullable
        private BlockElementRotation getRotation(JsonObject jsonObject0) {
            BlockElementRotation $$1 = null;
            if (jsonObject0.has("rotation")) {
                JsonObject $$2 = GsonHelper.getAsJsonObject(jsonObject0, "rotation");
                Vector3f $$3 = this.getVector3f($$2, "origin");
                $$3.mul(0.0625F);
                Direction.Axis $$4 = this.getAxis($$2);
                float $$5 = this.getAngle($$2);
                boolean $$6 = GsonHelper.getAsBoolean($$2, "rescale", false);
                $$1 = new BlockElementRotation($$3, $$4, $$5, $$6);
            }
            return $$1;
        }

        private float getAngle(JsonObject jsonObject0) {
            float $$1 = GsonHelper.getAsFloat(jsonObject0, "angle");
            if ($$1 != 0.0F && Mth.abs($$1) != 22.5F && Mth.abs($$1) != 45.0F) {
                throw new JsonParseException("Invalid rotation " + $$1 + " found, only -45/-22.5/0/22.5/45 allowed");
            } else {
                return $$1;
            }
        }

        private Direction.Axis getAxis(JsonObject jsonObject0) {
            String $$1 = GsonHelper.getAsString(jsonObject0, "axis");
            Direction.Axis $$2 = Direction.Axis.byName($$1.toLowerCase(Locale.ROOT));
            if ($$2 == null) {
                throw new JsonParseException("Invalid rotation axis: " + $$1);
            } else {
                return $$2;
            }
        }

        private Map<Direction, BlockElementFace> getFaces(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            Map<Direction, BlockElementFace> $$2 = this.filterNullFromFaces(jsonDeserializationContext0, jsonObject1);
            if ($$2.isEmpty()) {
                throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
            } else {
                return $$2;
            }
        }

        private Map<Direction, BlockElementFace> filterNullFromFaces(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            Map<Direction, BlockElementFace> $$2 = Maps.newEnumMap(Direction.class);
            JsonObject $$3 = GsonHelper.getAsJsonObject(jsonObject1, "faces");
            for (Entry<String, JsonElement> $$4 : $$3.entrySet()) {
                Direction $$5 = this.getFacing((String) $$4.getKey());
                $$2.put($$5, (BlockElementFace) jsonDeserializationContext0.deserialize((JsonElement) $$4.getValue(), BlockElementFace.class));
            }
            return $$2;
        }

        private Direction getFacing(String string0) {
            Direction $$1 = Direction.byName(string0);
            if ($$1 == null) {
                throw new JsonParseException("Unknown facing: " + string0);
            } else {
                return $$1;
            }
        }

        private Vector3f getTo(JsonObject jsonObject0) {
            Vector3f $$1 = this.getVector3f(jsonObject0, "to");
            if (!($$1.x() < -16.0F) && !($$1.y() < -16.0F) && !($$1.z() < -16.0F) && !($$1.x() > 32.0F) && !($$1.y() > 32.0F) && !($$1.z() > 32.0F)) {
                return $$1;
            } else {
                throw new JsonParseException("'to' specifier exceeds the allowed boundaries: " + $$1);
            }
        }

        private Vector3f getFrom(JsonObject jsonObject0) {
            Vector3f $$1 = this.getVector3f(jsonObject0, "from");
            if (!($$1.x() < -16.0F) && !($$1.y() < -16.0F) && !($$1.z() < -16.0F) && !($$1.x() > 32.0F) && !($$1.y() > 32.0F) && !($$1.z() > 32.0F)) {
                return $$1;
            } else {
                throw new JsonParseException("'from' specifier exceeds the allowed boundaries: " + $$1);
            }
        }

        private Vector3f getVector3f(JsonObject jsonObject0, String string1) {
            JsonArray $$2 = GsonHelper.getAsJsonArray(jsonObject0, string1);
            if ($$2.size() != 3) {
                throw new JsonParseException("Expected 3 " + string1 + " values, found: " + $$2.size());
            } else {
                float[] $$3 = new float[3];
                for (int $$4 = 0; $$4 < $$3.length; $$4++) {
                    $$3[$$4] = GsonHelper.convertToFloat($$2.get($$4), string1 + "[" + $$4 + "]");
                }
                return new Vector3f($$3[0], $$3[1], $$3[2]);
            }
        }
    }
}