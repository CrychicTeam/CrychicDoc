package dev.latvian.mods.kubejs.recipe.schema;

import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class JsonRecipeSchemaType extends RecipeSchemaType {

    public JsonRecipeSchemaType(RecipeNamespace namespace, ResourceLocation id, RecipeSerializer<?> serializer) {
        super(namespace, id, JsonRecipeSchema.SCHEMA);
        this.serializer = Optional.ofNullable(serializer);
    }
}