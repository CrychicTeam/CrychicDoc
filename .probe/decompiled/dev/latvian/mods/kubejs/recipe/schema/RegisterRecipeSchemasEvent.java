package dev.latvian.mods.kubejs.recipe.schema;

import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public record RegisterRecipeSchemasEvent(Map<String, RecipeNamespace> namespaces, Map<String, ResourceLocation> mappedRecipes) {

    public RecipeNamespace namespace(String namespace) {
        return (RecipeNamespace) this.namespaces.computeIfAbsent(namespace, RecipeNamespace::new);
    }

    public void register(ResourceLocation id, RecipeSchema schema) {
        this.namespace(id.getNamespace()).register(id.getPath(), schema);
    }

    public void mapRecipe(String name, ResourceLocation type) {
        this.mappedRecipes.put(name, type);
    }

    public void mapRecipe(String name, String type) {
        this.mapRecipe(name, new ResourceLocation(type));
    }
}