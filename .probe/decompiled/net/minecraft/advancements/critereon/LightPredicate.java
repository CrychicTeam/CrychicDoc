package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.GsonHelper;

public class LightPredicate {

    public static final LightPredicate ANY = new LightPredicate(MinMaxBounds.Ints.ANY);

    private final MinMaxBounds.Ints composite;

    LightPredicate(MinMaxBounds.Ints minMaxBoundsInts0) {
        this.composite = minMaxBoundsInts0;
    }

    public boolean matches(ServerLevel serverLevel0, BlockPos blockPos1) {
        if (this == ANY) {
            return true;
        } else {
            return !serverLevel0.m_46749_(blockPos1) ? false : this.composite.matches(serverLevel0.m_46803_(blockPos1));
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            $$0.add("light", this.composite.m_55328_());
            return $$0;
        }
    }

    public static LightPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "light");
            MinMaxBounds.Ints $$2 = MinMaxBounds.Ints.fromJson($$1.get("light"));
            return new LightPredicate($$2);
        } else {
            return ANY;
        }
    }

    public static class Builder {

        private MinMaxBounds.Ints composite = MinMaxBounds.Ints.ANY;

        public static LightPredicate.Builder light() {
            return new LightPredicate.Builder();
        }

        public LightPredicate.Builder setComposite(MinMaxBounds.Ints minMaxBoundsInts0) {
            this.composite = minMaxBoundsInts0;
            return this;
        }

        public LightPredicate build() {
            return new LightPredicate(this.composite);
        }
    }
}