package com.rekindled.embers.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class MetalCoefficientRecipe implements IMetalCoefficientRecipe {

    public static final MetalCoefficientRecipe.Serializer SERIALIZER = new MetalCoefficientRecipe.Serializer();

    public final ResourceLocation id;

    public final TagKey<Block> blockTag;

    public final double coefficient;

    public MetalCoefficientRecipe(ResourceLocation id, TagKey<Block> blockTag, double coefficient) {
        this.id = id;
        this.blockTag = blockTag;
        this.coefficient = coefficient;
    }

    public boolean matches(BlockStateContext context, Level pLevel) {
        return context.state.m_204336_(this.blockTag);
    }

    @Override
    public double getCoefficient(BlockStateContext context) {
        return this.coefficient;
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
    public List<ItemStack> getDisplayInput() {
        List<ItemStack> list = Lists.newArrayList();
        for (Holder<Block> holder : BuiltInRegistries.BLOCK.m_206058_(this.blockTag)) {
            list.add(new ItemStack((ItemLike) holder.get()));
        }
        return list;
    }

    @Override
    public double getDisplayCoefficient() {
        return this.coefficient;
    }

    public static class Serializer implements RecipeSerializer<MetalCoefficientRecipe> {

        public MetalCoefficientRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ResourceLocation blockTag = new ResourceLocation(json.get("block_tag").getAsString());
            double coefficient = GsonHelper.getAsDouble(json, "coefficient");
            return new MetalCoefficientRecipe(recipeId, BlockTags.create(blockTag), coefficient);
        }

        @Nullable
        public MetalCoefficientRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ResourceLocation blockTag = buffer.readResourceLocation();
            double coefficient = buffer.readDouble();
            return new MetalCoefficientRecipe(recipeId, BlockTags.create(blockTag), coefficient);
        }

        public void toNetwork(FriendlyByteBuf buffer, MetalCoefficientRecipe recipe) {
            buffer.writeResourceLocation(recipe.blockTag.location());
            buffer.writeDouble(recipe.coefficient);
        }
    }
}