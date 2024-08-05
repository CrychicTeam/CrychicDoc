package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.schema.DynamicRecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public record TagKeyComponent<T>(ResourceKey<? extends Registry<T>> registry, Class<?> registryType) implements RecipeComponent<TagKey<T>> {

    public static final RecipeComponent<TagKey<Block>> BLOCK = new TagKeyComponent<>(Registries.BLOCK, Block.class);

    public static final RecipeComponent<TagKey<Item>> ITEM = new TagKeyComponent<>(Registries.ITEM, Item.class);

    public static final RecipeComponent<TagKey<EntityType<?>>> ENTITY_TYPE = new TagKeyComponent<>(Registries.ENTITY_TYPE, EntityType.class);

    public static final RecipeComponent<TagKey<Biome>> BIOME = new TagKeyComponent<>(Registries.BIOME, Biome.class);

    public static final RecipeComponent<TagKey<Fluid>> FLUID = new TagKeyComponent<>(Registries.FLUID, Fluid.class);

    public static final DynamicRecipeComponent DYNAMIC = new DynamicRecipeComponent(TypeDescJS.object().add("registry", TypeDescJS.STRING).add("class", TypeDescJS.STRING, true), (cx, scope, args) -> {
        ResourceKey<Registry<Object>> registry = ResourceKey.createRegistryKey(UtilsJS.getMCID(cx, Wrapper.unwrapped(args.get("registry"))));
        Class<?> type = Object.class;
        if (args.containsKey("class")) {
            try {
                type = Class.forName(String.valueOf(Wrapper.unwrapped(args.get("class"))));
            } catch (ClassNotFoundException var6) {
                var6.printStackTrace();
            }
        }
        return new TagKeyComponent<>(registry, type);
    });

    @Override
    public String componentType() {
        return "tag_key";
    }

    @Override
    public Class<?> componentClass() {
        return TagKey.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.STRING.or(ctx.javaType(TagKey.class).withGenerics(ctx.javaType(this.registryType)));
    }

    public JsonPrimitive write(RecipeJS recipe, TagKey<T> value) {
        return new JsonPrimitive(value.location().toString());
    }

    public TagKey<T> read(RecipeJS recipe, Object from) {
        if (from instanceof TagKey) {
            return (TagKey<T>) from;
        } else {
            String s = from instanceof JsonPrimitive json ? json.getAsString() : String.valueOf(from);
            if (s.startsWith("#")) {
                s = s.substring(1);
            }
            return TagKey.create(this.registry, new ResourceLocation(s));
        }
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        if (from instanceof TagKey || from instanceof CharSequence && from.toString().startsWith("#")) {
            return true;
        } else {
            if (from instanceof JsonPrimitive json && json.isString() && json.getAsString().startsWith("#")) {
                return true;
            }
            return false;
        }
    }

    public String toString() {
        return this.componentType();
    }
}