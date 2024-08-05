package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class FluidPredicate {

    public static final FluidPredicate ANY = new FluidPredicate(null, null, StatePropertiesPredicate.ANY);

    @Nullable
    private final TagKey<Fluid> tag;

    @Nullable
    private final Fluid fluid;

    private final StatePropertiesPredicate properties;

    public FluidPredicate(@Nullable TagKey<Fluid> tagKeyFluid0, @Nullable Fluid fluid1, StatePropertiesPredicate statePropertiesPredicate2) {
        this.tag = tagKeyFluid0;
        this.fluid = fluid1;
        this.properties = statePropertiesPredicate2;
    }

    public boolean matches(ServerLevel serverLevel0, BlockPos blockPos1) {
        if (this == ANY) {
            return true;
        } else if (!serverLevel0.m_46749_(blockPos1)) {
            return false;
        } else {
            FluidState $$2 = serverLevel0.m_6425_(blockPos1);
            if (this.tag != null && !$$2.is(this.tag)) {
                return false;
            } else {
                return this.fluid != null && !$$2.is(this.fluid) ? false : this.properties.matches($$2);
            }
        }
    }

    public static FluidPredicate fromJson(@Nullable JsonElement jsonElement0) {
        if (jsonElement0 != null && !jsonElement0.isJsonNull()) {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "fluid");
            Fluid $$2 = null;
            if ($$1.has("fluid")) {
                ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString($$1, "fluid"));
                $$2 = BuiltInRegistries.FLUID.get($$3);
            }
            TagKey<Fluid> $$4 = null;
            if ($$1.has("tag")) {
                ResourceLocation $$5 = new ResourceLocation(GsonHelper.getAsString($$1, "tag"));
                $$4 = TagKey.create(Registries.FLUID, $$5);
            }
            StatePropertiesPredicate $$6 = StatePropertiesPredicate.fromJson($$1.get("state"));
            return new FluidPredicate($$4, $$2, $$6);
        } else {
            return ANY;
        }
    }

    public JsonElement serializeToJson() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject $$0 = new JsonObject();
            if (this.fluid != null) {
                $$0.addProperty("fluid", BuiltInRegistries.FLUID.getKey(this.fluid).toString());
            }
            if (this.tag != null) {
                $$0.addProperty("tag", this.tag.location().toString());
            }
            $$0.add("state", this.properties.serializeToJson());
            return $$0;
        }
    }

    public static class Builder {

        @Nullable
        private Fluid fluid;

        @Nullable
        private TagKey<Fluid> fluids;

        private StatePropertiesPredicate properties = StatePropertiesPredicate.ANY;

        private Builder() {
        }

        public static FluidPredicate.Builder fluid() {
            return new FluidPredicate.Builder();
        }

        public FluidPredicate.Builder of(Fluid fluid0) {
            this.fluid = fluid0;
            return this;
        }

        public FluidPredicate.Builder of(TagKey<Fluid> tagKeyFluid0) {
            this.fluids = tagKeyFluid0;
            return this;
        }

        public FluidPredicate.Builder setProperties(StatePropertiesPredicate statePropertiesPredicate0) {
            this.properties = statePropertiesPredicate0;
            return this;
        }

        public FluidPredicate build() {
            return new FluidPredicate(this.fluids, this.fluid, this.properties);
        }
    }
}