package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.tterrag.registrate.util.entry.ItemEntry;
import java.util.function.Supplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class WashingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe WOOL = this.create("wool", b -> b.require(ItemTags.WOOL).output(Items.WHITE_WOOL));

    CreateRecipeProvider.GeneratedRecipe STAINED_GLASS = this.create("stained_glass", b -> b.require(Tags.Items.STAINED_GLASS).output(Items.GLASS));

    CreateRecipeProvider.GeneratedRecipe STAINED_GLASS_PANE = this.create("stained_glass_pane", b -> b.require(Tags.Items.STAINED_GLASS_PANES).output(Items.GLASS_PANE));

    CreateRecipeProvider.GeneratedRecipe GRAVEL = this.create(() -> Blocks.GRAVEL, b -> b.output(0.25F, Items.FLINT).output(0.125F, Items.IRON_NUGGET));

    CreateRecipeProvider.GeneratedRecipe SOUL_SAND = this.create(() -> Blocks.SOUL_SAND, b -> b.output(0.125F, Items.QUARTZ, 4).output(0.02F, Items.GOLD_NUGGET));

    CreateRecipeProvider.GeneratedRecipe RED_SAND = this.create(() -> Blocks.RED_SAND, b -> b.output(0.125F, Items.GOLD_NUGGET, 3).output(0.05F, Items.DEAD_BUSH));

    CreateRecipeProvider.GeneratedRecipe SAND = this.create(() -> Blocks.SAND, b -> b.output(0.25F, Items.CLAY_BALL));

    CreateRecipeProvider.GeneratedRecipe CRUSHED_COPPER = this.crushedOre(AllItems.CRUSHED_COPPER, AllItems.COPPER_NUGGET::get, () -> Items.CLAY_BALL, 0.5F);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_ZINC = this.crushedOre(AllItems.CRUSHED_ZINC, AllItems.ZINC_NUGGET::get, () -> Items.GUNPOWDER, 0.25F);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_GOLD = this.crushedOre(AllItems.CRUSHED_GOLD, () -> Items.GOLD_NUGGET, () -> Items.QUARTZ, 0.5F);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_IRON = this.crushedOre(AllItems.CRUSHED_IRON, () -> Items.IRON_NUGGET, () -> Items.REDSTONE, 0.75F);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_OSMIUM = this.moddedCrushedOre(AllItems.CRUSHED_OSMIUM, CompatMetals.OSMIUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_PLATINUM = this.moddedCrushedOre(AllItems.CRUSHED_PLATINUM, CompatMetals.PLATINUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_SILVER = this.moddedCrushedOre(AllItems.CRUSHED_SILVER, CompatMetals.SILVER);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_TIN = this.moddedCrushedOre(AllItems.CRUSHED_TIN, CompatMetals.TIN);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_LEAD = this.moddedCrushedOre(AllItems.CRUSHED_LEAD, CompatMetals.LEAD);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_QUICKSILVER = this.moddedCrushedOre(AllItems.CRUSHED_QUICKSILVER, CompatMetals.QUICKSILVER);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_BAUXITE = this.moddedCrushedOre(AllItems.CRUSHED_BAUXITE, CompatMetals.ALUMINUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_URANIUM = this.moddedCrushedOre(AllItems.CRUSHED_URANIUM, CompatMetals.URANIUM);

    CreateRecipeProvider.GeneratedRecipe CRUSHED_NICKEL = this.moddedCrushedOre(AllItems.CRUSHED_NICKEL, CompatMetals.NICKEL);

    CreateRecipeProvider.GeneratedRecipe ICE = this.convert(Blocks.ICE, Blocks.PACKED_ICE);

    CreateRecipeProvider.GeneratedRecipe MAGMA_BLOCK = this.convert(Blocks.MAGMA_BLOCK, Blocks.OBSIDIAN);

    CreateRecipeProvider.GeneratedRecipe WHITE_CONCRETE = this.convert(Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe ORANGE_CONCRETE = this.convert(Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe MAGENTA_CONCRETE = this.convert(Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe LIGHT_BLUE_CONCRETE = this.convert(Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe LIME_CONCRETE = this.convert(Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe YELLOW_CONCRETE = this.convert(Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe PINK_CONCRETE = this.convert(Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe LIGHT_GRAY_CONCRETE = this.convert(Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe GRAY_CONCRETE = this.convert(Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe PURPLE_CONCRETE = this.convert(Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe GREEN_CONCRETE = this.convert(Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe BROWN_CONCRETE = this.convert(Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe RED_CONCRETE = this.convert(Blocks.RED_CONCRETE_POWDER, Blocks.RED_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe BLUE_CONCRETE = this.convert(Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe CYAN_CONCRETE = this.convert(Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe BLACK_CONCRETE = this.convert(Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE);

    CreateRecipeProvider.GeneratedRecipe FLOUR = this.create("wheat_flour", b -> b.require(CreateRecipeProvider.I.wheatFlour()).output((ItemLike) AllItems.DOUGH.get()));

    public CreateRecipeProvider.GeneratedRecipe convert(Block block, Block result) {
        return this.create(() -> block, b -> b.output(result));
    }

    public CreateRecipeProvider.GeneratedRecipe crushedOre(ItemEntry<Item> crushed, Supplier<ItemLike> nugget, Supplier<ItemLike> secondary, float secondaryChance) {
        return this.create(crushed::get, b -> b.output((ItemLike) nugget.get(), 9).output(secondaryChance, (ItemLike) secondary.get(), 1));
    }

    public CreateRecipeProvider.GeneratedRecipe moddedCrushedOre(ItemEntry<? extends Item> crushed, CompatMetals metal) {
        String metalName = metal.getName();
        for (Mods mod : metal.getMods()) {
            ResourceLocation nugget = mod.nuggetOf(metalName);
            this.create(mod.getId() + "/" + crushed.getId().getPath(), b -> b.withItemIngredients(Ingredient.of(crushed::get)).output(1.0F, nugget, 9).whenModLoaded(mod.getId()));
        }
        return null;
    }

    public WashingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SPLASHING;
    }
}