package com.rekindled.embers.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rekindled.embers.util.WeightedItemStack;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BoringRecipe implements IBoringRecipe {

    public static final BoringRecipe.Serializer SERIALIZER = new BoringRecipe.Serializer();

    public final ResourceLocation id;

    public final WeightedItemStack result;

    public final int minHeight;

    public final int maxHeight;

    public final HashSet<ResourceLocation> dimensions;

    public final HashSet<ResourceLocation> biomes;

    public final TagKey<Block> requiredBlock;

    public final int amountRequired;

    public final double chance;

    public BoringRecipe(ResourceLocation id, WeightedItemStack result, int minHeight, int maxHeight, HashSet<ResourceLocation> dimensions, HashSet<ResourceLocation> biomes, TagKey<Block> requiredBlock, int amountRequired, double chance) {
        this.id = id;
        this.result = result;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.dimensions = dimensions;
        this.biomes = biomes;
        this.requiredBlock = requiredBlock;
        this.amountRequired = amountRequired;
        this.chance = chance;
    }

    public boolean matches(BoringContext context, Level pLevel) {
        if (!this.dimensions.isEmpty() && !this.dimensions.contains(context.dimension)) {
            return false;
        } else if (!this.biomes.isEmpty() && !this.biomes.contains(context.biome)) {
            return false;
        } else {
            if (this.amountRequired > 0) {
                int amountLeft = this.amountRequired;
                for (BlockState state : context.blocks) {
                    if (state.m_204336_(this.requiredBlock)) {
                        amountLeft--;
                    }
                    if (amountLeft < 1) {
                        break;
                    }
                }
                if (amountLeft > 0) {
                    return false;
                }
            }
            return context.height >= this.minHeight && context.height <= this.maxHeight;
        }
    }

    @Override
    public WeightedItemStack getOutput(BoringContext context) {
        return this.result;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registry) {
        return this.result.getStack();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public int getMinHeight() {
        return this.minHeight;
    }

    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }

    @Override
    public Collection<ResourceLocation> getDimensions() {
        return this.dimensions;
    }

    @Override
    public Collection<ResourceLocation> getBiomes() {
        return this.biomes;
    }

    @Override
    public double getChance() {
        return this.chance;
    }

    @Override
    public WeightedItemStack getDisplayOutput() {
        return this.result;
    }

    @Override
    public List<ItemStack> getDisplayInput() {
        List<ItemStack> list = Lists.newArrayList();
        for (Holder<Block> holder : BuiltInRegistries.BLOCK.m_206058_(this.requiredBlock)) {
            list.add(new ItemStack((ItemLike) holder.get(), this.amountRequired));
        }
        return list;
    }

    public static class Serializer implements RecipeSerializer<BoringRecipe> {

        public BoringRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int weight = GsonHelper.getAsInt(json, "weight");
            int minHeight = GsonHelper.getAsInt(json, "min_height", Integer.MIN_VALUE);
            int maxHeight = GsonHelper.getAsInt(json, "max_height", Integer.MAX_VALUE);
            HashSet<ResourceLocation> dimensions = new HashSet();
            HashSet<ResourceLocation> biomes = new HashSet();
            double chance = GsonHelper.getAsDouble(json, "chance", -1.0);
            JsonArray dimJson = GsonHelper.getAsJsonArray(json, "dimensions", null);
            if (dimJson != null) {
                for (JsonElement element : dimJson) {
                    dimensions.add(new ResourceLocation(element.getAsString()));
                }
            }
            JsonArray biomeJson = GsonHelper.getAsJsonArray(json, "biomes", null);
            if (biomeJson != null) {
                for (JsonElement element : biomeJson) {
                    biomes.add(new ResourceLocation(element.getAsString()));
                }
            }
            ResourceLocation requiredBlock = null;
            int amountRequired = 0;
            JsonObject blockJson = json.getAsJsonObject("required_block");
            if (blockJson != null) {
                requiredBlock = new ResourceLocation(blockJson.get("block_tag").getAsString());
                amountRequired = GsonHelper.getAsInt(blockJson, "amount", 0);
            }
            return new BoringRecipe(recipeId, new WeightedItemStack(stack, weight), minHeight, maxHeight, dimensions, biomes, BlockTags.create(requiredBlock), amountRequired, chance);
        }

        @Nullable
        public BoringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ItemStack stack = buffer.readItem();
            int weight = buffer.readVarInt();
            int minHeight = buffer.readVarInt();
            int maxHeight = buffer.readVarInt();
            HashSet<ResourceLocation> dimensions = buffer.readCollection(i -> new HashSet(), FriendlyByteBuf::m_130281_);
            HashSet<ResourceLocation> biomes = buffer.readCollection(i -> new HashSet(), FriendlyByteBuf::m_130281_);
            ResourceLocation requiredBlock = buffer.readResourceLocation();
            int amountRequired = buffer.readVarInt();
            double chance = buffer.readDouble();
            return new BoringRecipe(recipeId, new WeightedItemStack(stack, weight), minHeight, maxHeight, dimensions, biomes, BlockTags.create(requiredBlock), amountRequired, chance);
        }

        public void toNetwork(FriendlyByteBuf buffer, BoringRecipe recipe) {
            buffer.writeItemStack(recipe.result.getStack(), false);
            buffer.writeVarInt(recipe.result.m_142631_().asInt());
            buffer.writeVarInt(recipe.minHeight);
            buffer.writeVarInt(recipe.maxHeight);
            buffer.writeCollection(recipe.dimensions, FriendlyByteBuf::m_130085_);
            buffer.writeCollection(recipe.biomes, FriendlyByteBuf::m_130085_);
            buffer.writeResourceLocation(recipe.requiredBlock.location());
            buffer.writeVarInt(recipe.amountRequired);
            buffer.writeDouble(recipe.chance);
        }
    }
}