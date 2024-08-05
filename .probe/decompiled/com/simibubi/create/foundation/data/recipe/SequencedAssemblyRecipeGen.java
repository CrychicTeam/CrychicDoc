package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;

public class SequencedAssemblyRecipeGen extends CreateRecipeProvider {

    CreateRecipeProvider.GeneratedRecipe PRECISION_MECHANISM = this.create("precision_mechanism", b -> b.require(CreateRecipeProvider.I.goldSheet()).transitionTo((ItemLike) AllItems.INCOMPLETE_PRECISION_MECHANISM.get()).addOutput((ItemLike) AllItems.PRECISION_MECHANISM.get(), 120.0F).addOutput((ItemLike) AllItems.GOLDEN_SHEET.get(), 8.0F).addOutput((ItemLike) AllItems.ANDESITE_ALLOY.get(), 8.0F).addOutput((ItemLike) AllBlocks.COGWHEEL.get(), 5.0F).addOutput(Items.GOLD_NUGGET, 3.0F).addOutput((ItemLike) AllBlocks.SHAFT.get(), 2.0F).addOutput((ItemLike) AllItems.CRUSHED_GOLD.get(), 2.0F).addOutput(Items.IRON_INGOT, 1.0F).addOutput(Items.CLOCK, 1.0F).loops(5).addStep(DeployerApplicationRecipe::new, rb -> rb.require(CreateRecipeProvider.I.cog())).addStep(DeployerApplicationRecipe::new, rb -> rb.require(CreateRecipeProvider.I.largeCog())).addStep(DeployerApplicationRecipe::new, rb -> rb.require(CreateRecipeProvider.I.ironNugget())));

    CreateRecipeProvider.GeneratedRecipe REINFORCED_SHEET = this.create("sturdy_sheet", b -> b.require((ItemLike) AllItems.POWDERED_OBSIDIAN.get()).transitionTo((ItemLike) AllItems.INCOMPLETE_REINFORCED_SHEET.get()).addOutput((ItemLike) AllItems.STURDY_SHEET.get(), 1.0F).loops(1).addStep(FillingRecipe::new, rb -> rb.require(Fluids.LAVA, 500)).addStep(PressingRecipe::new, rb -> rb).addStep(PressingRecipe::new, rb -> rb));

    CreateRecipeProvider.GeneratedRecipe TRACK = this.create("track", b -> b.require(AllTags.AllItemTags.SLEEPERS.tag).transitionTo((ItemLike) AllItems.INCOMPLETE_TRACK.get()).addOutput((ItemLike) AllBlocks.TRACK.get(), 1.0F).loops(1).addStep(DeployerApplicationRecipe::new, rb -> rb.require(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(CreateRecipeProvider.I.ironNugget()), new Ingredient.TagValue(CreateRecipeProvider.I.zincNugget()))))).addStep(DeployerApplicationRecipe::new, rb -> rb.require(Ingredient.fromValues(Stream.of(new Ingredient.TagValue(CreateRecipeProvider.I.ironNugget()), new Ingredient.TagValue(CreateRecipeProvider.I.zincNugget()))))).addStep(PressingRecipe::new, rb -> rb));

    public SequencedAssemblyRecipeGen(PackOutput p_i48262_1_) {
        super(p_i48262_1_);
    }

    protected CreateRecipeProvider.GeneratedRecipe create(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        CreateRecipeProvider.GeneratedRecipe generatedRecipe = c -> ((SequencedAssemblyRecipeBuilder) transform.apply(new SequencedAssemblyRecipeBuilder(Create.asResource(name)))).build(c);
        this.all.add(generatedRecipe);
        return generatedRecipe;
    }

    @Override
    public String getName() {
        return "Create's Sequenced Assembly Recipes";
    }
}