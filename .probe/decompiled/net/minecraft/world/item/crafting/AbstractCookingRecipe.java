package net.minecraft.world.item.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractCookingRecipe implements Recipe<Container> {

    protected final RecipeType<?> type;

    protected final ResourceLocation id;

    private final CookingBookCategory category;

    protected final String group;

    protected final Ingredient ingredient;

    protected final ItemStack result;

    protected final float experience;

    protected final int cookingTime;

    public AbstractCookingRecipe(RecipeType<?> recipeType0, ResourceLocation resourceLocation1, String string2, CookingBookCategory cookingBookCategory3, Ingredient ingredient4, ItemStack itemStack5, float float6, int int7) {
        this.type = recipeType0;
        this.category = cookingBookCategory3;
        this.id = resourceLocation1;
        this.group = string2;
        this.ingredient = ingredient4;
        this.result = itemStack5;
        this.experience = float6;
        this.cookingTime = int7;
    }

    @Override
    public boolean matches(Container container0, Level level1) {
        return this.ingredient.test(container0.getItem(0));
    }

    @Override
    public ItemStack assemble(Container container0, RegistryAccess registryAccess1) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int int0, int int1) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> $$0 = NonNullList.create();
        $$0.add(this.ingredient);
        return $$0;
    }

    public float getExperience() {
        return this.experience;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return this.result;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    public CookingBookCategory category() {
        return this.category;
    }
}