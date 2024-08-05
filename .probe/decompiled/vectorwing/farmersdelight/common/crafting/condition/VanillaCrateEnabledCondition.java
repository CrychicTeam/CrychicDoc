package vectorwing.farmersdelight.common.crafting.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import vectorwing.farmersdelight.common.Configuration;

public class VanillaCrateEnabledCondition implements ICondition {

    private final ResourceLocation location;

    public VanillaCrateEnabledCondition(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation getID() {
        return this.location;
    }

    @Override
    public boolean test(ICondition.IContext context) {
        return Configuration.ENABLE_VANILLA_CROP_CRATES.get();
    }

    public static class Serializer implements IConditionSerializer<VanillaCrateEnabledCondition> {

        private final ResourceLocation location = new ResourceLocation("farmersdelight", "vanilla_crates_enabled");

        @Override
        public ResourceLocation getID() {
            return this.location;
        }

        public VanillaCrateEnabledCondition read(JsonObject json) {
            return new VanillaCrateEnabledCondition(this.location);
        }

        public void write(JsonObject json, VanillaCrateEnabledCondition value) {
        }
    }
}