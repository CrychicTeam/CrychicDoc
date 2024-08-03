package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import java.lang.reflect.Type;
import java.util.Objects;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class Variant implements ModelState {

    private final ResourceLocation modelLocation;

    private final Transformation rotation;

    private final boolean uvLock;

    private final int weight;

    public Variant(ResourceLocation resourceLocation0, Transformation transformation1, boolean boolean2, int int3) {
        this.modelLocation = resourceLocation0;
        this.rotation = transformation1;
        this.uvLock = boolean2;
        this.weight = int3;
    }

    public ResourceLocation getModelLocation() {
        return this.modelLocation;
    }

    @Override
    public Transformation getRotation() {
        return this.rotation;
    }

    @Override
    public boolean isUvLocked() {
        return this.uvLock;
    }

    public int getWeight() {
        return this.weight;
    }

    public String toString() {
        return "Variant{modelLocation=" + this.modelLocation + ", rotation=" + this.rotation + ", uvLock=" + this.uvLock + ", weight=" + this.weight + "}";
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof Variant $$1) ? false : this.modelLocation.equals($$1.modelLocation) && Objects.equals(this.rotation, $$1.rotation) && this.uvLock == $$1.uvLock && this.weight == $$1.weight;
        }
    }

    public int hashCode() {
        int $$0 = this.modelLocation.hashCode();
        $$0 = 31 * $$0 + this.rotation.hashCode();
        $$0 = 31 * $$0 + Boolean.valueOf(this.uvLock).hashCode();
        return 31 * $$0 + this.weight;
    }

    public static class Deserializer implements JsonDeserializer<Variant> {

        @VisibleForTesting
        static final boolean DEFAULT_UVLOCK = false;

        @VisibleForTesting
        static final int DEFAULT_WEIGHT = 1;

        @VisibleForTesting
        static final int DEFAULT_X_ROTATION = 0;

        @VisibleForTesting
        static final int DEFAULT_Y_ROTATION = 0;

        public Variant deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            ResourceLocation $$4 = this.getModel($$3);
            BlockModelRotation $$5 = this.getBlockRotation($$3);
            boolean $$6 = this.getUvLock($$3);
            int $$7 = this.getWeight($$3);
            return new Variant($$4, $$5.getRotation(), $$6, $$7);
        }

        private boolean getUvLock(JsonObject jsonObject0) {
            return GsonHelper.getAsBoolean(jsonObject0, "uvlock", false);
        }

        protected BlockModelRotation getBlockRotation(JsonObject jsonObject0) {
            int $$1 = GsonHelper.getAsInt(jsonObject0, "x", 0);
            int $$2 = GsonHelper.getAsInt(jsonObject0, "y", 0);
            BlockModelRotation $$3 = BlockModelRotation.by($$1, $$2);
            if ($$3 == null) {
                throw new JsonParseException("Invalid BlockModelRotation x: " + $$1 + ", y: " + $$2);
            } else {
                return $$3;
            }
        }

        protected ResourceLocation getModel(JsonObject jsonObject0) {
            return new ResourceLocation(GsonHelper.getAsString(jsonObject0, "model"));
        }

        protected int getWeight(JsonObject jsonObject0) {
            int $$1 = GsonHelper.getAsInt(jsonObject0, "weight", 1);
            if ($$1 < 1) {
                throw new JsonParseException("Invalid weight " + $$1 + " found, expected integer >= 1");
            } else {
                return $$1;
            }
        }
    }
}