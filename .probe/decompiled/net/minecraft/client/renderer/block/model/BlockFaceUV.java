package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;

public class BlockFaceUV {

    public float[] uvs;

    public final int rotation;

    public BlockFaceUV(@Nullable float[] float0, int int1) {
        this.uvs = float0;
        this.rotation = int1;
    }

    public float getU(int int0) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        } else {
            int $$1 = this.getShiftedIndex(int0);
            return this.uvs[$$1 != 0 && $$1 != 1 ? 2 : 0];
        }
    }

    public float getV(int int0) {
        if (this.uvs == null) {
            throw new NullPointerException("uvs");
        } else {
            int $$1 = this.getShiftedIndex(int0);
            return this.uvs[$$1 != 0 && $$1 != 3 ? 3 : 1];
        }
    }

    private int getShiftedIndex(int int0) {
        return (int0 + this.rotation / 90) % 4;
    }

    public int getReverseIndex(int int0) {
        return (int0 + 4 - this.rotation / 90) % 4;
    }

    public void setMissingUv(float[] float0) {
        if (this.uvs == null) {
            this.uvs = float0;
        }
    }

    protected static class Deserializer implements JsonDeserializer<BlockFaceUV> {

        private static final int DEFAULT_ROTATION = 0;

        public BlockFaceUV deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            float[] $$4 = this.getUVs($$3);
            int $$5 = this.getRotation($$3);
            return new BlockFaceUV($$4, $$5);
        }

        protected int getRotation(JsonObject jsonObject0) {
            int $$1 = GsonHelper.getAsInt(jsonObject0, "rotation", 0);
            if ($$1 >= 0 && $$1 % 90 == 0 && $$1 / 90 <= 3) {
                return $$1;
            } else {
                throw new JsonParseException("Invalid rotation " + $$1 + " found, only 0/90/180/270 allowed");
            }
        }

        @Nullable
        private float[] getUVs(JsonObject jsonObject0) {
            if (!jsonObject0.has("uv")) {
                return null;
            } else {
                JsonArray $$1 = GsonHelper.getAsJsonArray(jsonObject0, "uv");
                if ($$1.size() != 4) {
                    throw new JsonParseException("Expected 4 uv values, found: " + $$1.size());
                } else {
                    float[] $$2 = new float[4];
                    for (int $$3 = 0; $$3 < $$2.length; $$3++) {
                        $$2[$$3] = GsonHelper.convertToFloat($$1.get($$3), "uv[" + $$3 + "]");
                    }
                    return $$2;
                }
            }
        }
    }
}