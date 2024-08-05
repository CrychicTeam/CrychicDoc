package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistryEvent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeNamespace;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class RecipeSchemaRegistryEventJS extends EventJS {

    private final Map<String, RecipeNamespace> namespaces;

    private final Map<String, ResourceLocation> mappedRecipes;

    private Map<String, RecipeComponentFactory> components;

    public RecipeSchemaRegistryEventJS(Map<String, RecipeNamespace> namespaces, Map<String, ResourceLocation> mappedRecipes) {
        this.namespaces = namespaces;
        this.mappedRecipes = mappedRecipes;
    }

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

    public Map<String, RecipeComponentFactory> getComponents() {
        if (this.components == null) {
            this.components = new HashMap();
            KubeJSPlugins.forEachPlugin(new RecipeComponentFactoryRegistryEvent(this.components), KubeJSPlugin::registerRecipeComponents);
        }
        return this.components;
    }
}