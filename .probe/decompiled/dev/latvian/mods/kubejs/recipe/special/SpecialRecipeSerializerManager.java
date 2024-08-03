package dev.latvian.mods.kubejs.recipe.special;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SpecialRecipeSerializerManager extends EventJS {

    public static final SpecialRecipeSerializerManager INSTANCE = new SpecialRecipeSerializerManager();

    public static final Event<Runnable> EVENT = EventFactory.createLoop();

    private final Map<ResourceLocation, Boolean> data = new HashMap();

    public void reset() {
        synchronized (this.data) {
            this.data.clear();
        }
    }

    @Override
    protected void afterPosted(EventResult result) {
        EVENT.invoker().run();
    }

    public boolean isSpecial(Recipe<?> recipe) {
        return (Boolean) this.data.getOrDefault(RegistryInfo.RECIPE_SERIALIZER.getId(recipe.getSerializer()), recipe.isSpecial());
    }

    public void ignoreSpecialFlag(ResourceLocation id) {
        synchronized (this.data) {
            this.data.put(id, false);
        }
    }

    public void addSpecialFlag(ResourceLocation id) {
        synchronized (this.data) {
            this.data.put(id, true);
        }
    }

    public void ignoreSpecialMod(String modid) {
        synchronized (this.data) {
            for (Entry<ResourceKey<RecipeSerializer>, RecipeSerializer> entry : RegistryInfo.RECIPE_SERIALIZER.entrySet()) {
                if (((ResourceKey) entry.getKey()).location().getNamespace().equals(modid)) {
                    this.data.put(((ResourceKey) entry.getKey()).location(), false);
                }
            }
        }
    }

    public void addSpecialMod(String modid) {
        synchronized (this.data) {
            for (Entry<ResourceKey<RecipeSerializer>, RecipeSerializer> entry : RegistryInfo.RECIPE_SERIALIZER.entrySet()) {
                if (((ResourceKey) entry.getKey()).location().getNamespace().equals(modid)) {
                    this.data.put(((ResourceKey) entry.getKey()).location(), true);
                }
            }
        }
    }
}