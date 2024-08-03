package com.rekindled.embers.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.util.WeightedItemStack;
import java.util.HashSet;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BoringRecipeBuilder {

    public ResourceLocation id;

    public ItemStack result;

    public int weight = 20;

    public int minHeight = Integer.MIN_VALUE;

    public int maxHeight = Integer.MAX_VALUE;

    public HashSet<ResourceLocation> dimensions = new HashSet();

    public HashSet<ResourceLocation> biomes = new HashSet();

    public TagKey<Block> requiredBlock = null;

    public int amountRequired = 0;

    public double chance = -1.0;

    public RecipeSerializer<?> type = RegistryManager.BORING_SERIALIZER.get();

    public static BoringRecipeBuilder create(ItemStack itemStack) {
        BoringRecipeBuilder builder = new BoringRecipeBuilder();
        builder.result = itemStack;
        builder.id = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
        return builder;
    }

    public static BoringRecipeBuilder create(Item item) {
        return create(new ItemStack(item));
    }

    public BoringRecipeBuilder id(ResourceLocation id) {
        this.id = id;
        return this;
    }

    public BoringRecipeBuilder domain(String domain) {
        this.id = new ResourceLocation(domain, this.id.getPath());
        return this;
    }

    public BoringRecipeBuilder folder(String folder) {
        this.id = new ResourceLocation(this.id.getNamespace(), folder + "/" + this.id.getPath());
        return this;
    }

    public BoringRecipeBuilder type(RecipeSerializer<?> type) {
        this.type = type;
        return this;
    }

    public BoringRecipeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public BoringRecipeBuilder minHeight(int minHeight) {
        this.minHeight = minHeight;
        return this;
    }

    public BoringRecipeBuilder maxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public BoringRecipeBuilder dimensions(HashSet<ResourceLocation> dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public BoringRecipeBuilder biomes(HashSet<ResourceLocation> biomes) {
        this.biomes = biomes;
        return this;
    }

    public BoringRecipeBuilder dimension(ResourceLocation... dimensions) {
        for (ResourceLocation dimension : dimensions) {
            this.dimensions.add(dimension);
        }
        return this;
    }

    public BoringRecipeBuilder biome(ResourceLocation... biomes) {
        for (ResourceLocation biome : biomes) {
            this.biomes.add(biome);
        }
        return this;
    }

    public BoringRecipeBuilder require(TagKey<Block> requiredBlock, int amountRequired) {
        this.requiredBlock = requiredBlock;
        this.amountRequired = amountRequired;
        return this;
    }

    public BoringRecipeBuilder chance(double chance) {
        this.chance = chance;
        return this;
    }

    public BoringRecipe build() {
        return new BoringRecipe(this.id, new WeightedItemStack(this.result, this.weight), this.minHeight, this.maxHeight, this.dimensions, this.biomes, this.requiredBlock, this.amountRequired, this.chance);
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new BoringRecipeBuilder.Finished(this.build(), this.type));
    }

    public static class Finished implements FinishedRecipe {

        public final BoringRecipe recipe;

        public final RecipeSerializer<?> type;

        public Finished(BoringRecipe recipe, RecipeSerializer<?> type) {
            this.recipe = recipe;
            this.type = type;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (this.recipe.minHeight != Integer.MIN_VALUE) {
                json.addProperty("min_height", this.recipe.minHeight);
            }
            if (this.recipe.minHeight != Integer.MAX_VALUE) {
                json.addProperty("max_height", this.recipe.maxHeight);
            }
            if (!this.recipe.dimensions.isEmpty()) {
                JsonArray dimJson = new JsonArray();
                for (ResourceLocation dimension : this.recipe.dimensions) {
                    dimJson.add(dimension.toString());
                }
                json.add("dimensions", dimJson);
            }
            if (!this.recipe.biomes.isEmpty()) {
                JsonArray biomeJson = new JsonArray();
                for (ResourceLocation biome : this.recipe.biomes) {
                    biomeJson.add(biome.toString());
                }
                json.add("biomes", biomeJson);
            }
            if (this.recipe.amountRequired != 0) {
                JsonObject blockJson = new JsonObject();
                blockJson.addProperty("block_tag", this.recipe.requiredBlock.location().toString());
                blockJson.addProperty("amount", this.recipe.amountRequired);
                json.add("required_block", blockJson);
            }
            if (this.recipe.chance != -1.0) {
                json.addProperty("chance", this.recipe.chance);
            }
            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", ForgeRegistries.ITEMS.getKey(this.recipe.result.getStack().getItem()).toString());
            int count = this.recipe.result.getStack().getCount();
            if (count > 1) {
                resultJson.addProperty("count", count);
            }
            json.add("output", resultJson);
            json.addProperty("weight", this.recipe.result.m_142631_().asInt());
        }

        @Override
        public ResourceLocation getId() {
            return this.recipe.getId();
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.type;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}