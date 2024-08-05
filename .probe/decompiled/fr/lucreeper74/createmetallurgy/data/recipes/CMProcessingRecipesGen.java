package fr.lucreeper74.createmetallurgy.data.recipes;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import fr.lucreeper74.createmetallurgy.CreateMetallurgy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class CMProcessingRecipesGen extends CMRecipeProvider {

    protected static final List<CMProcessingRecipesGen> GENS = new ArrayList();

    public static void registerAll(DataGenerator gen, PackOutput output) {
        GENS.add(new GrindingRecipeGen(output));
        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Create: Metallurgy's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf((CompletableFuture[]) CMProcessingRecipesGen.GENS.stream().map(gen -> gen.m_213708_(dc)).toArray(CompletableFuture[]::new));
            }
        });
    }

    public CMProcessingRecipesGen(PackOutput output) {
        super(output);
    }

    protected <T extends ProcessingRecipe<?>> CMRecipeProvider.GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return this.create(CreateMetallurgy.genRL(name), transform);
    }

    protected <T extends ProcessingRecipe<?>> CMRecipeProvider.GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return this.createWithDeferredId(() -> name, transform);
    }

    protected <T extends ProcessingRecipe<?>> CMRecipeProvider.GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = this.getSerializer();
        CMRecipeProvider.GeneratedRecipe generatedRecipe = c -> ((ProcessingRecipeBuilder) transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), (ResourceLocation) name.get()))).build(c);
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return this.getRecipeType().getSerializer();
    }
}