package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import java.lang.reflect.Type;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ItemTransform {

    public static final ItemTransform NO_TRANSFORM = new ItemTransform(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));

    public final Vector3f rotation;

    public final Vector3f translation;

    public final Vector3f scale;

    public ItemTransform(Vector3f vectorF0, Vector3f vectorF1, Vector3f vectorF2) {
        this.rotation = new Vector3f(vectorF0);
        this.translation = new Vector3f(vectorF1);
        this.scale = new Vector3f(vectorF2);
    }

    public void apply(boolean boolean0, PoseStack poseStack1) {
        if (this != NO_TRANSFORM) {
            float $$2 = this.rotation.x();
            float $$3 = this.rotation.y();
            float $$4 = this.rotation.z();
            if (boolean0) {
                $$3 = -$$3;
                $$4 = -$$4;
            }
            int $$5 = boolean0 ? -1 : 1;
            poseStack1.translate((float) $$5 * this.translation.x(), this.translation.y(), this.translation.z());
            poseStack1.mulPose(new Quaternionf().rotationXYZ($$2 * (float) (Math.PI / 180.0), $$3 * (float) (Math.PI / 180.0), $$4 * (float) (Math.PI / 180.0)));
            poseStack1.scale(this.scale.x(), this.scale.y(), this.scale.z());
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (this.getClass() != object0.getClass()) {
            return false;
        } else {
            ItemTransform $$1 = (ItemTransform) object0;
            return this.rotation.equals($$1.rotation) && this.scale.equals($$1.scale) && this.translation.equals($$1.translation);
        }
    }

    public int hashCode() {
        int $$0 = this.rotation.hashCode();
        $$0 = 31 * $$0 + this.translation.hashCode();
        return 31 * $$0 + this.scale.hashCode();
    }

    protected static class Deserializer implements JsonDeserializer<ItemTransform> {

        private static final Vector3f DEFAULT_ROTATION = new Vector3f(0.0F, 0.0F, 0.0F);

        private static final Vector3f DEFAULT_TRANSLATION = new Vector3f(0.0F, 0.0F, 0.0F);

        private static final Vector3f DEFAULT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);

        public static final float MAX_TRANSLATION = 5.0F;

        public static final float MAX_SCALE = 4.0F;

        public ItemTransform deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            Vector3f $$4 = this.getVector3f($$3, "rotation", DEFAULT_ROTATION);
            Vector3f $$5 = this.getVector3f($$3, "translation", DEFAULT_TRANSLATION);
            $$5.mul(0.0625F);
            $$5.set(Mth.clamp($$5.x, -5.0F, 5.0F), Mth.clamp($$5.y, -5.0F, 5.0F), Mth.clamp($$5.z, -5.0F, 5.0F));
            Vector3f $$6 = this.getVector3f($$3, "scale", DEFAULT_SCALE);
            $$6.set(Mth.clamp($$6.x, -4.0F, 4.0F), Mth.clamp($$6.y, -4.0F, 4.0F), Mth.clamp($$6.z, -4.0F, 4.0F));
            return new ItemTransform($$4, $$5, $$6);
        }

        private Vector3f getVector3f(JsonObject jsonObject0, String string1, Vector3f vectorF2) {
            if (!jsonObject0.has(string1)) {
                return vectorF2;
            } else {
                JsonArray $$3 = GsonHelper.getAsJsonArray(jsonObject0, string1);
                if ($$3.size() != 3) {
                    throw new JsonParseException("Expected 3 " + string1 + " values, found: " + $$3.size());
                } else {
                    float[] $$4 = new float[3];
                    for (int $$5 = 0; $$5 < $$4.length; $$5++) {
                        $$4[$$5] = GsonHelper.convertToFloat($$3.get($$5), string1 + "[" + $$5 + "]");
                    }
                    return new Vector3f($$4[0], $$4[1], $$4[2]);
                }
            }
        }
    }
}