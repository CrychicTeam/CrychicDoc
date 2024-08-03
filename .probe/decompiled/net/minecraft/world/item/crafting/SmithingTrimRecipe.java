package net.minecraft.world.item.crafting;

import com.google.gson.JsonObject;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;

public class SmithingTrimRecipe implements SmithingRecipe {

    private final ResourceLocation id;

    final Ingredient template;

    final Ingredient base;

    final Ingredient addition;

    public SmithingTrimRecipe(ResourceLocation resourceLocation0, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3) {
        this.id = resourceLocation0;
        this.template = ingredient1;
        this.base = ingredient2;
        this.addition = ingredient3;
    }

    @Override
    public boolean matches(Container container0, Level level1) {
        return this.template.test(container0.getItem(0)) && this.base.test(container0.getItem(1)) && this.addition.test(container0.getItem(2));
    }

    @Override
    public ItemStack assemble(Container container0, RegistryAccess registryAccess1) {
        ItemStack $$2 = container0.getItem(1);
        if (this.base.test($$2)) {
            Optional<Holder.Reference<TrimMaterial>> $$3 = TrimMaterials.getFromIngredient(registryAccess1, container0.getItem(2));
            Optional<Holder.Reference<TrimPattern>> $$4 = TrimPatterns.getFromTemplate(registryAccess1, container0.getItem(0));
            if ($$3.isPresent() && $$4.isPresent()) {
                Optional<ArmorTrim> $$5 = ArmorTrim.getTrim(registryAccess1, $$2);
                if ($$5.isPresent() && ((ArmorTrim) $$5.get()).hasPatternAndMaterial((Holder<TrimPattern>) $$4.get(), (Holder<TrimMaterial>) $$3.get())) {
                    return ItemStack.EMPTY;
                }
                ItemStack $$6 = $$2.copy();
                $$6.setCount(1);
                ArmorTrim $$7 = new ArmorTrim((Holder<TrimMaterial>) $$3.get(), (Holder<TrimPattern>) $$4.get());
                if (ArmorTrim.setTrim(registryAccess1, $$6, $$7)) {
                    return $$6;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        ItemStack $$1 = new ItemStack(Items.IRON_CHESTPLATE);
        Optional<Holder.Reference<TrimPattern>> $$2 = registryAccess0.registryOrThrow(Registries.TRIM_PATTERN).holders().findFirst();
        if ($$2.isPresent()) {
            Optional<Holder.Reference<TrimMaterial>> $$3 = registryAccess0.registryOrThrow(Registries.TRIM_MATERIAL).getHolder(TrimMaterials.REDSTONE);
            if ($$3.isPresent()) {
                ArmorTrim $$4 = new ArmorTrim((Holder<TrimMaterial>) $$3.get(), (Holder<TrimPattern>) $$2.get());
                ArmorTrim.setTrim(registryAccess0, $$1, $$4);
            }
        }
        return $$1;
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
        return RecipeSerializer.SMITHING_TRIM;
    }

    @Override
    public boolean isIncomplete() {
        return Stream.of(this.template, this.base, this.addition).anyMatch(Ingredient::m_43947_);
    }

    public static class Serializer implements RecipeSerializer<SmithingTrimRecipe> {

        public SmithingTrimRecipe fromJson(ResourceLocation resourceLocation0, JsonObject jsonObject1) {
            Ingredient $$2 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "template"));
            Ingredient $$3 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "base"));
            Ingredient $$4 = Ingredient.fromJson(GsonHelper.getNonNull(jsonObject1, "addition"));
            return new SmithingTrimRecipe(resourceLocation0, $$2, $$3, $$4);
        }

        public SmithingTrimRecipe fromNetwork(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
            Ingredient $$2 = Ingredient.fromNetwork(friendlyByteBuf1);
            Ingredient $$3 = Ingredient.fromNetwork(friendlyByteBuf1);
            Ingredient $$4 = Ingredient.fromNetwork(friendlyByteBuf1);
            return new SmithingTrimRecipe(resourceLocation0, $$2, $$3, $$4);
        }

        public void toNetwork(FriendlyByteBuf friendlyByteBuf0, SmithingTrimRecipe smithingTrimRecipe1) {
            smithingTrimRecipe1.template.toNetwork(friendlyByteBuf0);
            smithingTrimRecipe1.base.toNetwork(friendlyByteBuf0);
            smithingTrimRecipe1.addition.toNetwork(friendlyByteBuf0);
        }
    }
}