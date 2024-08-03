package org.violetmoon.zeta.config;

import com.google.gson.JsonObject;
import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceLocation;
import org.violetmoon.zeta.recipe.IZetaCondition;
import org.violetmoon.zeta.recipe.IZetaConditionSerializer;
import org.violetmoon.zeta.util.BooleanSuppliers;

public record FlagCondition(ConfigFlagManager cfm, String flag, ResourceLocation loc, BooleanSupplier extraCondition) implements IZetaCondition {

    @Override
    public ResourceLocation getID() {
        return this.loc;
    }

    @Override
    public boolean test(IZetaCondition.IContext context) {
        if (this.flag.contains("%")) {
            throw new RuntimeException("Illegal flag: " + this.flag);
        } else {
            if (!this.cfm.isValidFlag(this.flag)) {
                this.cfm.zeta.log.warn("Non-existent flag " + this.flag + " being used");
            }
            return this.extraCondition.getAsBoolean() && this.cfm.getFlag(this.flag);
        }
    }

    public static class Serializer implements IZetaConditionSerializer<FlagCondition> {

        private final ConfigFlagManager cfm;

        private final ResourceLocation location;

        private final BooleanSupplier extraCondition;

        public Serializer(ConfigFlagManager cfm, ResourceLocation location, BooleanSupplier extraCondition) {
            this.cfm = cfm;
            this.location = location;
            this.extraCondition = extraCondition;
        }

        public Serializer(ConfigFlagManager cfm, ResourceLocation location) {
            this(cfm, location, BooleanSuppliers.TRUE);
        }

        public void write(JsonObject json, FlagCondition value) {
            json.addProperty("flag", value.flag);
        }

        public FlagCondition read(JsonObject json) {
            return new FlagCondition(this.cfm, json.getAsJsonPrimitive("flag").getAsString(), this.location, this.extraCondition);
        }

        @Override
        public ResourceLocation getID() {
            return this.location;
        }
    }
}