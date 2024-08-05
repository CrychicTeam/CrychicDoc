package com.simibubi.create.foundation.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockTagIngredient extends AbstractIngredient {

    protected final TagKey<Block> tag;

    @Nullable
    protected ItemStack[] itemStacks;

    @Nullable
    protected IntList stackingIds;

    protected BlockTagIngredient(TagKey<Block> tag) {
        this.tag = tag;
    }

    public static BlockTagIngredient create(TagKey<Block> tag) {
        return new BlockTagIngredient(tag);
    }

    protected void dissolve() {
        if (this.itemStacks == null) {
            List<ItemStack> list = new ArrayList();
            for (Block block : ForgeRegistries.BLOCKS.tags().getTag(this.tag)) {
                ItemStack stack = new ItemStack(block);
                if (!stack.isEmpty()) {
                    list.add(stack);
                }
            }
            this.itemStacks = (ItemStack[]) list.toArray(ItemStack[]::new);
        }
    }

    @Override
    public ItemStack[] getItems() {
        this.dissolve();
        return this.itemStacks;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        } else {
            this.dissolve();
            if (this.itemStacks.length == 0) {
                return stack.isEmpty();
            } else {
                for (ItemStack itemStack : this.itemStacks) {
                    if (itemStack.is(stack.getItem())) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Override
    public IntList getStackingIds() {
        if (this.stackingIds == null || this.checkInvalidation()) {
            this.markValid();
            this.dissolve();
            this.stackingIds = new IntArrayList(this.itemStacks.length);
            for (ItemStack stack : this.itemStacks) {
                this.stackingIds.add(StackedContents.getStackingIndex(stack));
            }
            this.stackingIds.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.stackingIds;
    }

    public TagKey<Block> getTag() {
        return this.tag;
    }

    protected void invalidate() {
        this.itemStacks = null;
        this.stackingIds = null;
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return BlockTagIngredient.Serializer.INSTANCE;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(BlockTagIngredient.Serializer.INSTANCE).toString());
        json.addProperty("tag", this.tag.location().toString());
        return json;
    }

    public static class Serializer implements IIngredientSerializer<BlockTagIngredient> {

        public static final BlockTagIngredient.Serializer INSTANCE = new BlockTagIngredient.Serializer();

        public BlockTagIngredient parse(JsonObject json) {
            ResourceLocation rl = new ResourceLocation(GsonHelper.getAsString(json, "tag"));
            TagKey<Block> tag = TagKey.create(Registries.BLOCK, rl);
            return new BlockTagIngredient(tag);
        }

        public BlockTagIngredient parse(FriendlyByteBuf buffer) {
            ResourceLocation rl = buffer.readResourceLocation();
            TagKey<Block> tag = TagKey.create(Registries.BLOCK, rl);
            return new BlockTagIngredient(tag);
        }

        public void write(FriendlyByteBuf buffer, BlockTagIngredient ingredient) {
            TagKey<Block> tag = ingredient.getTag();
            buffer.writeResourceLocation(tag.location());
        }
    }
}