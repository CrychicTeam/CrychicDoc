package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.RegisteredObjects;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public abstract class ProcessingRecipeGen extends CreateRecipeProvider {

    protected static final List<ProcessingRecipeGen> GENERATORS = new ArrayList();

    protected static final int BUCKET = 1000;

    protected static final int BOTTLE = 250;

    public static void registerAll(DataGenerator gen, PackOutput output) {
        GENERATORS.add(new CrushingRecipeGen(output));
        GENERATORS.add(new MillingRecipeGen(output));
        GENERATORS.add(new CuttingRecipeGen(output));
        GENERATORS.add(new WashingRecipeGen(output));
        GENERATORS.add(new PolishingRecipeGen(output));
        GENERATORS.add(new DeployingRecipeGen(output));
        GENERATORS.add(new MixingRecipeGen(output));
        GENERATORS.add(new CompactingRecipeGen(output));
        GENERATORS.add(new PressingRecipeGen(output));
        GENERATORS.add(new FillingRecipeGen(output));
        GENERATORS.add(new EmptyingRecipeGen(output));
        GENERATORS.add(new HauntingRecipeGen(output));
        GENERATORS.add(new ItemApplicationRecipeGen(output));
        gen.addProvider(true, new DataProvider() {

            @Override
            public String getName() {
                return "Create's Processing Recipes";
            }

            @Override
            public CompletableFuture<?> run(CachedOutput dc) {
                return CompletableFuture.allOf((CompletableFuture[]) ProcessingRecipeGen.GENERATORS.stream().map(gen -> gen.m_213708_(dc)).toArray(CompletableFuture[]::new));
            }
        });
    }

    public ProcessingRecipeGen(PackOutput generator) {
        super(generator);
    }

    protected <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe create(String namespace, Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = this.getSerializer();
        CreateRecipeProvider.GeneratedRecipe generatedRecipe = c -> {
            ItemLike itemLike = (ItemLike) singleIngredient.get();
            ((ProcessingRecipeBuilder) transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), new ResourceLocation(namespace, RegisteredObjects.getKeyOrThrow(itemLike.asItem()).getPath())).withItemIngredients(Ingredient.of(itemLike)))).build(c);
        };
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return this.create("create", singleIngredient, transform);
    }

    protected <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe createWithDeferredId(Supplier<ResourceLocation> name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        ProcessingRecipeSerializer<T> serializer = this.getSerializer();
        CreateRecipeProvider.GeneratedRecipe generatedRecipe = c -> ((ProcessingRecipeBuilder) transform.apply(new ProcessingRecipeBuilder<>(serializer.getFactory(), (ResourceLocation) name.get()))).build(c);
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    protected <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe create(ResourceLocation name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return this.createWithDeferredId(() -> name, transform);
    }

    <T extends ProcessingRecipe<?>> CreateRecipeProvider.GeneratedRecipe create(String name, UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        return this.create(Create.asResource(name), transform);
    }

    protected abstract IRecipeTypeInfo getRecipeType();

    protected <T extends ProcessingRecipe<?>> ProcessingRecipeSerializer<T> getSerializer() {
        return this.getRecipeType().getSerializer();
    }

    protected Supplier<ResourceLocation> idWithSuffix(Supplier<ItemLike> item, String suffix) {
        return () -> {
            ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(((ItemLike) item.get()).asItem());
            return Create.asResource(registryName.getPath() + suffix);
        };
    }

    @Override
    public String getName() {
        return "Create's Processing Recipes: " + this.getRecipeType().getId().getPath();
    }
}