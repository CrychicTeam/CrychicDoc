package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

public class StampingRecipe implements IStampingRecipe {

    public static final StampingRecipe.Serializer SERIALIZER = new StampingRecipe.Serializer();

    public final ResourceLocation id;

    public final Ingredient stamp;

    public final Ingredient input;

    public final FluidIngredient fluid;

    public final Either<ItemStack, StampingRecipe.TagAmount> output;

    public StampingRecipe(ResourceLocation id, Ingredient stamp, Ingredient input, FluidIngredient fluid, StampingRecipe.TagAmount output) {
        this(id, stamp, input, fluid, Either.right(output));
    }

    public StampingRecipe(ResourceLocation id, Ingredient stamp, Ingredient input, FluidIngredient fluid, ItemStack output) {
        this(id, stamp, input, fluid, Either.left(output));
    }

    public StampingRecipe(ResourceLocation id, Ingredient stamp, Ingredient input, FluidIngredient fluid, Either<ItemStack, StampingRecipe.TagAmount> output) {
        this.id = id;
        this.stamp = stamp;
        this.input = input;
        this.fluid = fluid;
        this.output = output;
    }

    public boolean matches(StampingContext context, Level pLevel) {
        for (int i = 0; i < context.m_6643_(); i++) {
            if (this.input.test(context.m_8020_(i))) {
                if (this.stamp.test(context.stamp) && this.fluid.test(context.fluids.getFluidInTank(0))) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public ItemStack getOutput(RecipeWrapper context) {
        return this.getResultItem();
    }

    public ItemStack assemble(StampingContext context, RegistryAccess registry) {
        for (int i = 0; i < context.m_6643_(); i++) {
            if (this.input.test(context.m_8020_(i))) {
                context.m_7407_(i, 1);
                break;
            }
        }
        for (FluidStack stack : this.fluid.getAllFluids()) {
            if (this.fluid.test(context.fluids.drain(stack, IFluidHandler.FluidAction.SIMULATE))) {
                context.fluids.drain(stack, IFluidHandler.FluidAction.EXECUTE);
                break;
            }
        }
        return this.getOutput(context);
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
    public ItemStack getResultItem() {
        return this.output.left().isPresent() ? ((ItemStack) this.output.left().get()).copy() : new ItemStack(Misc.getTaggedItem(((StampingRecipe.TagAmount) this.output.right().get()).tag), ((StampingRecipe.TagAmount) this.output.right().get()).amount);
    }

    @Override
    public FluidIngredient getDisplayInputFluid() {
        return this.fluid;
    }

    @Override
    public Ingredient getDisplayInput() {
        return this.input;
    }

    @Override
    public Ingredient getDisplayStamp() {
        return this.stamp;
    }

    public static class Serializer implements RecipeSerializer<StampingRecipe> {

        public StampingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient stamp = Ingredient.fromJson(json.get("stamp"));
            Ingredient input = Ingredient.EMPTY;
            FluidIngredient fluid = FluidIngredient.EMPTY;
            if (json.has("input")) {
                input = Ingredient.fromJson(json.get("input"));
            }
            if (json.has("fluid")) {
                fluid = FluidIngredient.deserialize(json, "fluid");
            }
            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            if (outputJson.has("tag")) {
                StampingRecipe.TagAmount output = new StampingRecipe.TagAmount(ItemTags.create(new ResourceLocation(GsonHelper.getAsString(outputJson, "tag"))), GsonHelper.getAsInt(outputJson, "count", 1));
                return new StampingRecipe(recipeId, stamp, input, fluid, output);
            } else {
                ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
                return new StampingRecipe(recipeId, stamp, input, fluid, output);
            }
        }

        @Nullable
        public StampingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient stamp = Ingredient.fromNetwork(buffer);
            Ingredient input = Ingredient.fromNetwork(buffer);
            FluidIngredient fluid = FluidIngredient.read(buffer);
            if (buffer.readBoolean()) {
                StampingRecipe.TagAmount output = new StampingRecipe.TagAmount(ItemTags.create(buffer.readResourceLocation()), buffer.readInt());
                return new StampingRecipe(recipeId, stamp, input, fluid, output);
            } else {
                ItemStack output = buffer.readItem();
                return new StampingRecipe(recipeId, stamp, input, fluid, output);
            }
        }

        public void toNetwork(FriendlyByteBuf buffer, StampingRecipe recipe) {
            recipe.stamp.toNetwork(buffer);
            recipe.input.toNetwork(buffer);
            recipe.fluid.write(buffer);
            if (recipe.output.right().isPresent()) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(((StampingRecipe.TagAmount) recipe.output.right().get()).tag.location());
                buffer.writeInt(((StampingRecipe.TagAmount) recipe.output.right().get()).amount);
            } else {
                buffer.writeBoolean(false);
                buffer.writeItemStack((ItemStack) recipe.output.left().get(), false);
            }
        }
    }

    public static class TagAmount {

        public TagKey<Item> tag;

        public int amount;

        public TagAmount(TagKey<Item> tag, int amount) {
            this.tag = tag;
            this.amount = amount;
        }
    }
}