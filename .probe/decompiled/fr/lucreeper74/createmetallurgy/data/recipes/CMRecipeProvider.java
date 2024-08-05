package fr.lucreeper74.createmetallurgy.data.recipes;

import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

public abstract class CMRecipeProvider extends RecipeProvider {

    protected final List<CMRecipeProvider.GeneratedRecipe> all = new ArrayList();

    public CMRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        this.all.forEach(c -> c.register(finishedRecipeConsumer));
        CreateMetallurgy.LOGGER.info(this.m_6055_() + " registered " + this.all.size() + " recipe" + (this.all.size() == 1 ? "" : "s"));
    }

    protected CMRecipeProvider.GeneratedRecipe register(CMRecipeProvider.GeneratedRecipe recipe) {
        this.all.add(recipe);
        return recipe;
    }

    @FunctionalInterface
    public interface GeneratedRecipe {

        void register(Consumer<FinishedRecipe> var1);
    }
}