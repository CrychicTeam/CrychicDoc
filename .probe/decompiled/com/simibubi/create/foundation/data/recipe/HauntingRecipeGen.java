package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.function.Supplier;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class HauntingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe BRASS_BELL = this.convert(() -> Ingredient.of((ItemLike) AllBlocks.PECULIAR_BELL.get()), AllBlocks.HAUNTED_BELL::get);

    CreateRecipeProvider.GeneratedRecipe HAUNT_STONE = this.convert(Items.STONE, Items.INFESTED_STONE);

    CreateRecipeProvider.GeneratedRecipe HAUNT_DEEPSLATE = this.convert(Items.DEEPSLATE, Items.INFESTED_DEEPSLATE);

    CreateRecipeProvider.GeneratedRecipe HAUNT_STONE_BRICKS = this.convert(Items.STONE_BRICKS, Items.INFESTED_STONE_BRICKS);

    CreateRecipeProvider.GeneratedRecipe HAUNT_MOSSY_STONE_BRICKS = this.convert(Items.MOSSY_STONE_BRICKS, Items.INFESTED_MOSSY_STONE_BRICKS);

    CreateRecipeProvider.GeneratedRecipe HAUNT_CRACKED_STONE_BRICKS = this.convert(Items.CRACKED_STONE_BRICKS, Items.INFESTED_CRACKED_STONE_BRICKS);

    CreateRecipeProvider.GeneratedRecipe HAUNT_CHISELED_STONE_BRICKS = this.convert(Items.CHISELED_STONE_BRICKS, Items.INFESTED_CHISELED_STONE_BRICKS);

    CreateRecipeProvider.GeneratedRecipe SOUL_TORCH = this.convert(Items.TORCH, Items.SOUL_TORCH);

    CreateRecipeProvider.GeneratedRecipe SOUL_CAMPFIRE = this.convert(Items.CAMPFIRE, Items.SOUL_CAMPFIRE);

    CreateRecipeProvider.GeneratedRecipe SOUL_LANTERN = this.convert(Items.LANTERN, Items.SOUL_LANTERN);

    CreateRecipeProvider.GeneratedRecipe POISON_POTATO = this.convert(Items.POTATO, Items.POISONOUS_POTATO);

    CreateRecipeProvider.GeneratedRecipe GLOW_INK = this.convert(Items.INK_SAC, Items.GLOW_INK_SAC);

    CreateRecipeProvider.GeneratedRecipe GLOW_BERRIES = this.convert(Items.SWEET_BERRIES, Items.GLOW_BERRIES);

    CreateRecipeProvider.GeneratedRecipe NETHER_BRICK = this.convert(Items.BRICK, Items.NETHER_BRICK);

    CreateRecipeProvider.GeneratedRecipe PRISMARINE = this.create(Create.asResource("lapis_recycling"), b -> b.require(Tags.Items.GEMS_LAPIS).output(0.75F, Items.PRISMARINE_SHARD).output(0.125F, Items.PRISMARINE_CRYSTALS));

    CreateRecipeProvider.GeneratedRecipe SOUL_SAND = this.convert(() -> Ingredient.of(ItemTags.SAND), () -> Blocks.SOUL_SAND);

    CreateRecipeProvider.GeneratedRecipe SOUL_DIRT = this.convert(() -> Ingredient.of(ItemTags.DIRT), () -> Blocks.SOUL_SOIL);

    CreateRecipeProvider.GeneratedRecipe BLACK_STONE = this.convert(() -> Ingredient.of(Tags.Items.COBBLESTONE), () -> Blocks.BLACKSTONE);

    CreateRecipeProvider.GeneratedRecipe CRIMSON_FUNGUS = this.convert(Items.RED_MUSHROOM, Items.CRIMSON_FUNGUS);

    CreateRecipeProvider.GeneratedRecipe WARPED_FUNGUS = this.convert(Items.BROWN_MUSHROOM, Items.WARPED_FUNGUS);

    public CreateRecipeProvider.GeneratedRecipe convert(ItemLike input, ItemLike result) {
        return this.convert(() -> Ingredient.of(input), () -> result);
    }

    public CreateRecipeProvider.GeneratedRecipe convert(Supplier<Ingredient> input, Supplier<ItemLike> result) {
        return this.create(Create.asResource(RegisteredObjects.getKeyOrThrow(((ItemLike) result.get()).asItem()).getPath()), p -> p.withItemIngredients((Ingredient) input.get()).output((ItemLike) result.get()));
    }

    public HauntingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.HAUNTING;
    }
}