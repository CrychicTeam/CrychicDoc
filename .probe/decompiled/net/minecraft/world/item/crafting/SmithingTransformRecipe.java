package net.minecraft.world.item.crafting;

import com.google.gson.JsonObject;
import java.util.stream.Stream;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SmithingTransformRecipe implements SmithingRecipe {

    private final ResourceLocation id;

    final Ingredient template;

    final Ingredient base;

    final Ingredient addition;

    final ItemStack result;

    public SmithingTransformRecipe(ResourceLocation resourceLocation0, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3, ItemStack itemStack4) {
        this.id = resourceLocation0;
        this.template = ingredient1;
        this.base = ingredient2;
        this.addition = ingredient3;
        this.result = itemStack4;
    }

    @Override
    public boolean matches(Container container0, Level level1) {
        return this.template.test(container0.getItem(0)) && this.base.test(container0.getItem(1)) && this.addition.test(container0.getItem(2));
    }

    @Override
    public ItemStack assemble(Container container0, RegistryAccess registryAccess1) {
        ItemStack $$2 = this.result.copy();
        CompoundTag $$3 = container0.getItem(1).getTag();
        if ($$3 != null) {
            $$2.setTag($$3.copy());
        }
        return $$2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.result;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack itemStack0) {
        return this.template.test(itemStack0);
    }

    @Override
    public boolean isBaseIngredient(ItemStack itemStack0) {
        return this.base.test(itemStack0);
    }

    @Override
    public boolean isAdditionIngredient(ItemStack itemStack0) {
        return this.addition.test(itemStack0);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMITHING_TRANSFORM;
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::m_43947_);
    }

    public static class Serializer implements RecipeSerializer<SmithingTransformRecipe> {

        public SmithingTransformRecipe fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
            Ingredient $$2 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "template"));
            Ingredient $$3 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "base"));
            Ingredient $$4 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "addition"));
            ItemStack $$5 = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject1, "result"));
            return new SmithingTransformRecipe(resourceLocation0, $$2, $$3, $$4, $$5);
        }

        public SmithingTransformRecipe fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
            Ingredient $$2 = Ingredient.fromNetwork(friendlyByteBuf1);
            Ingredient $$3 = Ingredient.fromNetwork(friendlyByteBuf1);
            Ingredient $$4 = Ingredient.fromNetwork(friendlyByteBuf1);
            ItemStack $$5 = friendlyByteBuf1.readItem();
            return new SmithingTransformRecipe(resourceLocation0, $$2, $$3, $$4, $$5);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf0, SmithingTransformRecipe smithingTransformRecipe1) {
            smithingTransformRecipe1.template.toNetwork(friendlyByteBuf0);
            smithingTransformRecipe1.base.toNetwork(friendlyByteBuf0);
            smithingTransformRecipe1.addition.toNetwork(friendlyByteBuf0);
            friendlyByteBuf0.writeItem(smithingTransformRecipe1.result);
        }
    }
}