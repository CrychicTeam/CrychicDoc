package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityFlagsPredicate {

    public static final EntityFlagsPredicate ANY = new EntityFlagsPredicate.Builder().build();

    @Nullable
    private final Boolean isOnFire;

    @Nullable
    private final Boolean isCrouching;

    @Nullable
    private final Boolean isSprinting;

    @Nullable
    private final Boolean isSwimming;

    @Nullable
    private final Boolean isBaby;

    public EntityFlagsPredicate(@Nullable Boolean boolean0, @Nullable Boolean boolean1, @Nullable Boolean boolean2, @Nullable Boolean boolean3, @Nullable Boolean boolean4) {
        this.isOnFire = boolean0;
        this.isCrouching = boolean1;
        this.isSprinting = boolean2;
        this.isSwimming = boolean3;
        this.isBaby = boolean4;
    }

    public boolean matches(Entity entity0) {
        if (this.isOnFire != null && entity0.isOnFire() != this.isOnFire) {
            return false;
        } else if (this.isCrouching != null && entity0.isCrouching() != this.isCrouching) {
            return false;
        } else if (this.isSprinting != null && entity0.isSprinting() != this.isSprinting) {
            return false;
        } else {
            return this.isSwimming != null && entity0.isSwimming() != this.isSwimming ? false : this.isBaby == null || !(entity0 instanceof LivingEntity) || ((LivingEntity) entity0).isBaby() == this.isBaby;
        }
    }

    @Nullable
    private static Boolean getOptionalBoolean(JsonObject jsonObject0, String string1) {
        return jsonObject0.has(string1) ? GsonHelper.getAsBoolean(jsonObject0, string1) : null;
    }

    public static EntityFlagsPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "entity flags");
            Boolean $$2 = getOptionalBoolean($$1, "is_on_fire");
            Boolean $$3 = getOptionalBoolean($$1, "is_sneaking");
            Boolean $$4 = getOptionalBoolean($$1, "is_sprinting");
            Boolean $$5 = getOptionalBoolean($$1, "is_swimming");
            Boolean $$6 = getOptionalBoolean($$1, "is_baby");
            return new EntityFlagsPredicate($$2, $$3, $$4, $$5, $$6);
        } else {
            return ANY;
        }
    }

    private void addOptionalBoolean(JsonObject jsonObject0, String string1, @Nullable Boolean boolean2) {
        if (boolean2 != null) {
            jsonObject0.addProperty(string1, boolean2);
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            this.addOptionalBoolean($$0, "is_on_fire", this.isOnFire);
            this.addOptionalBoolean($$0, "is_sneaking", this.isCrouching);
            this.addOptionalBoolean($$0, "is_sprinting", this.isSprinting);
            this.addOptionalBoolean($$0, "is_swimming", this.isSwimming);
            this.addOptionalBoolean($$0, "is_baby", this.isBaby);
            return $$0;
        }
    }

    public static class Builder {

        @Nullable
        private Boolean isOnFire;

        @Nullable
        private Boolean isCrouching;

        @Nullable
        private Boolean isSprinting;

        @Nullable
        private Boolean isSwimming;

        @Nullable
        private Boolean isBaby;

        public static EntityFlagsPredicate.Builder flags() {
            return new EntityFlagsPredicate.Builder();
        }

        public EntityFlagsPredicate.Builder setOnFire(@Nullable Boolean boolean0) {
            this.isOnFire = boolean0;
            return this;
        }

        public EntityFlagsPredicate.Builder setCrouching(@Nullable Boolean boolean0) {
            this.isCrouching = boolean0;
            return this;
        }

        public EntityFlagsPredicate.Builder setSprinting(@Nullable Boolean boolean0) {
            this.isSprinting = boolean0;
            return this;
        }

        public EntityFlagsPredicate.Builder setSwimming(@Nullable Boolean boolean0) {
            this.isSwimming = boolean0;
            return this;
        }

        public EntityFlagsPredicate.Builder setIsBaby(@Nullable Boolean boolean0) {
            this.isBaby = boolean0;
            return this;
        }

        public EntityFlagsPredicate build() {
            return new EntityFlagsPredicate(this.isOnFire, this.isCrouching, this.isSprinting, this.isSwimming, this.isBaby);
        }
    }
}