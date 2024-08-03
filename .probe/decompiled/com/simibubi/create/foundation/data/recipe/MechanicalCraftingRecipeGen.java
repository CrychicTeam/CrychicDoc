package com.simibubi.create.foundation.data.recipe;

import com.google.common.base.Supplier;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.function.UnaryOperator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

public class MechanicalCraftingRecipeGen extends CreateRecipeProvider {

    CreateRecipeProvider.GeneratedRecipe CRUSHING_WHEEL = this.create(AllBlocks.CRUSHING_WHEEL::get).returns(2).recipe(b -> b.key('P', Ingredient.of(ItemTags.PLANKS)).key('S', Ingredient.of(CreateRecipeProvider.I.stone())).key('A', CreateRecipeProvider.I.andesite()).patternLine(" AAA ").patternLine("AAPAA").patternLine("APSPA").patternLine("AAPAA").patternLine(" AAA ").disallowMirrored());

    CreateRecipeProvider.GeneratedRecipe WAND_OF_SYMMETRY = this.create(AllItems.WAND_OF_SYMMETRY::get).recipe(b -> b.key('E', Ingredient.of(Tags.Items.ENDER_PEARLS)).key('G', Ingredient.of(Tags.Items.GLASS)).key('P', CreateRecipeProvider.I.precisionMechanism()).key('O', Ingredient.of(Tags.Items.OBSIDIAN)).key('B', Ingredient.of(CreateRecipeProvider.I.brass())).patternLine(" G ").patternLine("GEG").patternLine(" P ").patternLine(" B ").patternLine(" O "));

    CreateRecipeProvider.GeneratedRecipe EXTENDO_GRIP = this.create(AllItems.EXTENDO_GRIP::get).returns(1).recipe(b -> b.key('L', Ingredient.of(CreateRecipeProvider.I.brass())).key('R', CreateRecipeProvider.I.precisionMechanism()).key('H', (ItemLike) AllItems.BRASS_HAND.get()).key('S', Ingredient.of(Tags.Items.RODS_WOODEN)).patternLine(" L ").patternLine(" R ").patternLine("SSS").patternLine("SSS").patternLine(" H ").disallowMirrored());

    CreateRecipeProvider.GeneratedRecipe POTATO_CANNON = this.create(AllItems.POTATO_CANNON::get).returns(1).recipe(b -> b.key('L', CreateRecipeProvider.I.andesite()).key('R', CreateRecipeProvider.I.precisionMechanism()).key('S', (ItemLike) AllBlocks.FLUID_PIPE.get()).key('C', Ingredient.of(CreateRecipeProvider.I.copper())).patternLine("LRSSS").patternLine("CC   "));

    public MechanicalCraftingRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    MechanicalCraftingRecipeGen.GeneratedRecipeBuilder create(Supplier<ItemLike> result) {
        return new MechanicalCraftingRecipeGen.GeneratedRecipeBuilder(result);
    }

    @Override
    public String getName() {
        return "Create's Mechanical Crafting Recipes";
    }

    class GeneratedRecipeBuilder {

        private String suffix = "";

        private Supplier<ItemLike> result;

        private int amount;

        public GeneratedRecipeBuilder(Supplier<ItemLike> result) {
            this.result = result;
            this.amount = 1;
        }

        MechanicalCraftingRecipeGen.GeneratedRecipeBuilder returns(int amount) {
            this.amount = amount;
            return this;
        }

        MechanicalCraftingRecipeGen.GeneratedRecipeBuilder withSuffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        CreateRecipeProvider.GeneratedRecipe recipe(UnaryOperator<MechanicalCraftingRecipeBuilder> builder) {
            return MechanicalCraftingRecipeGen.this.register(consumer -> {
                MechanicalCraftingRecipeBuilder b = (MechanicalCraftingRecipeBuilder) builder.apply(MechanicalCraftingRecipeBuilder.shapedRecipe((ItemLike) this.result.get(), this.amount));
                ResourceLocation location = Create.asResource("mechanical_crafting/" + RegisteredObjects.getKeyOrThrow(((ItemLike) this.result.get()).asItem()).getPath() + this.suffix);
                b.build(consumer, location);
            });
        }
    }
}