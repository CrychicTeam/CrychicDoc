package net.mehvahdjukaar.moonlight.api.resources.recipe.forge;

import com.google.gson.JsonObject;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class OptionalRecipeCondition implements IConditionSerializer<OptionalRecipeCondition.Instance> {

    private final ResourceLocation id;

    private final Predicate<String> predicate;

    public OptionalRecipeCondition(ResourceLocation id, Predicate<String> predicate) {
        this.id = id;
        this.predicate = predicate;
    }

    public void write(JsonObject json, OptionalRecipeCondition.Instance value) {
        json.addProperty(this.id.getPath(), value.condition);
    }

    public OptionalRecipeCondition.Instance read(JsonObject json) {
        return new OptionalRecipeCondition.Instance(json.getAsJsonPrimitive(this.id.getPath()).getAsString());
    }

    @Override
    public ResourceLocation getID() {
        return this.id;
    }

    protected final class Instance implements ICondition {

        private final String condition;

        private Instance(String condition) {
            this.condition = condition;
        }

        @Override
        public ResourceLocation getID() {
            return OptionalRecipeCondition.this.getID();
        }

        @Override
        public boolean test(ICondition.IContext context) {
            return OptionalRecipeCondition.this.predicate.test(this.condition);
        }
    }
}