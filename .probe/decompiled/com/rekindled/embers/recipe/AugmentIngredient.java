package com.rekindled.embers.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.augment.IAugment;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

public class AugmentIngredient extends AbstractIngredient {

    private final Ingredient base;

    private final IAugment augment;

    private final int level;

    private ItemStack[] augmentedMatchingStacks;

    private IntList packedMatchingStacks;

    private boolean inverted;

    public AugmentIngredient(Ingredient base, IAugment augment, int level, boolean inverted) {
        this.base = base;
        this.augment = augment;
        this.level = level;
        this.inverted = inverted;
    }

    public static AugmentIngredient of(Ingredient base, IAugment augment, int level, boolean inverted) {
        return new AugmentIngredient(base, augment, level, inverted);
    }

    public static AugmentIngredient of(Ingredient base, IAugment augment, boolean inverted) {
        return new AugmentIngredient(base, augment, 1, inverted);
    }

    public static AugmentIngredient of(Ingredient base, IAugment augment, int level) {
        return new AugmentIngredient(base, augment, level, false);
    }

    public static AugmentIngredient of(Ingredient base, IAugment augment) {
        return new AugmentIngredient(base, augment, 1, false);
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null && !stack.isEmpty() ? this.base.test(stack) && AugmentUtil.hasHeat(stack) && this.inverted ^ AugmentUtil.getAugmentLevel(stack, this.augment) >= this.level : false;
    }

    @Override
    public ItemStack[] getItems() {
        if (this.augmentedMatchingStacks == null) {
            ItemStack[] items = this.base.getItems();
            this.augmentedMatchingStacks = new ItemStack[items.length];
            for (int i = 0; i < items.length; i++) {
                ItemStack stack = items[i].copy();
                if (this.inverted) {
                    AugmentUtil.setHeat(stack, 0.0F);
                } else {
                    AugmentUtil.setLevel(stack, AugmentUtil.getLevel(stack) + this.level);
                    AugmentUtil.addAugment(stack, ItemStack.EMPTY, this.augment);
                    AugmentUtil.setAugmentLevel(stack, this.augment, this.level);
                }
                this.augmentedMatchingStacks[i] = stack;
            }
        }
        return this.augmentedMatchingStacks;
    }

    @Override
    public boolean isEmpty() {
        return this.base.isEmpty();
    }

    @Override
    public boolean isSimple() {
        return this.base.isSimple();
    }

    protected void invalidate() {
        super.invalidate();
        this.augmentedMatchingStacks = null;
        this.packedMatchingStacks = null;
    }

    @Override
    public IntList getStackingIds() {
        if (this.packedMatchingStacks == null || this.checkInvalidation()) {
            this.markValid();
            ItemStack[] matchingStacks = this.getItems();
            this.packedMatchingStacks = new IntArrayList(matchingStacks.length);
            for (ItemStack stack : matchingStacks) {
                this.packedMatchingStacks.add(StackedContents.getStackingIndex(stack));
            }
            this.packedMatchingStacks.sort(IntComparators.NATURAL_COMPARATOR);
        }
        return this.packedMatchingStacks;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(AugmentIngredient.Serializer.INSTANCE).toString());
        json.add("base", this.base.toJson());
        json.addProperty("augment", this.augment.getName().toString());
        json.addProperty("level", this.level);
        if (this.inverted) {
            json.addProperty("inverted", this.inverted);
        }
        return json;
    }

    @Override
    public IIngredientSerializer<AugmentIngredient> getSerializer() {
        return AugmentIngredient.Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<AugmentIngredient> {

        public static final IIngredientSerializer<AugmentIngredient> INSTANCE = new AugmentIngredient.Serializer();

        public AugmentIngredient parse(JsonObject json) {
            Ingredient base = Ingredient.fromJson(json.get("base"), false);
            IAugment augment = AugmentUtil.getAugment(new ResourceLocation(GsonHelper.getAsString(json, "augment")));
            int level = GsonHelper.getAsInt(json, "level");
            boolean inverted = false;
            if (json.has("inverted")) {
                inverted = GsonHelper.getAsBoolean(json, "inverted");
            }
            return new AugmentIngredient(base, augment, level, inverted);
        }

        public AugmentIngredient parse(FriendlyByteBuf buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            IAugment augment = AugmentUtil.getAugment(buffer.readResourceLocation());
            int level = buffer.readInt();
            boolean inverted = buffer.readBoolean();
            return new AugmentIngredient(base, augment, level, inverted);
        }

        public void write(FriendlyByteBuf buffer, AugmentIngredient ingredient) {
            ingredient.base.toNetwork(buffer);
            buffer.writeResourceLocation(ingredient.augment.getName());
            buffer.writeInt(ingredient.level);
            buffer.writeBoolean(ingredient.inverted);
        }
    }
}