package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.utility.Lang;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;

public class CrushingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe BLAZE_ROD = this.create(() -> Items.BLAZE_ROD, b -> b.duration(100).output(Items.BLAZE_POWDER, 3).output(0.25F, Items.BLAZE_POWDER, 3));

    CreateRecipeProvider.GeneratedRecipe PRISMARINE_CRYSTALS = this.create(() -> Items.PRISMARINE_CRYSTALS, b -> b.duration(150).output(1.0F, Items.QUARTZ, 1).output(0.5F, Items.QUARTZ, 2).output(0.1F, Items.GLOWSTONE_DUST, 2));

    CreateRecipeProvider.GeneratedRecipe OBSIDIAN = this.create(() -> Blocks.OBSIDIAN, b -> b.duration(500).output((ItemLike) AllItems.POWDERED_OBSIDIAN.get()).output(0.75F, Blocks.OBSIDIAN));

    CreateRecipeProvider.GeneratedRecipe WOOL = this.create("wool", b -> b.duration(100).require(ItemTags.WOOL).output(Items.STRING, 2).output(0.5F, Items.STRING));

    CreateRecipeProvider.GeneratedRecipe DIORITE = this.mineralRecycling(AllPaletteStoneTypes.DIORITE, b -> b.duration(350).output(0.25F, Items.QUARTZ, 1));

    CreateRecipeProvider.GeneratedRecipe CRIMSITE = this.mineralRecycling(AllPaletteStoneTypes.CRIMSITE, AllItems.CRUSHED_IRON::get, () -> Items.IRON_NUGGET, 0.4F);

    CreateRecipeProvider.GeneratedRecipe VERIDIUM = this.mineralRecycling(AllPaletteStoneTypes.VERIDIUM, AllItems.CRUSHED_COPPER::get, () -> AllItems.COPPER_NUGGET::get, 0.8F);

    CreateRecipeProvider.GeneratedRecipe ASURINE = this.mineralRecycling(AllPaletteStoneTypes.ASURINE, AllItems.CRUSHED_ZINC::get, () -> AllItems.ZINC_NUGGET::get, 0.3F);

    CreateRecipeProvider.GeneratedRecipe OCHRUM = this.mineralRecycling(AllPaletteStoneTypes.OCHRUM, AllItems.CRUSHED_GOLD::get, () -> Items.GOLD_NUGGET, 0.2F);

    CreateRecipeProvider.GeneratedRecipe TUFF = this.mineralRecycling(AllPaletteStoneTypes.TUFF, b -> b.duration(350).output(0.25F, Items.FLINT, 1).output(0.1F, Items.GOLD_NUGGET, 1).output(0.1F, (ItemLike) AllItems.COPPER_NUGGET.get(), 1).output(0.1F, (ItemLike) AllItems.ZINC_NUGGET.get(), 1).output(0.1F, Items.IRON_NUGGET, 1));

    CreateRecipeProvider.GeneratedRecipe COPPER_ORE = this.stoneOre(() -> Items.COPPER_ORE, AllItems.CRUSHED_COPPER::get, 5.25F, 250);

    CreateRecipeProvider.GeneratedRecipe ZINC_ORE = this.stoneOre(AllBlocks.ZINC_ORE::get, AllItems.CRUSHED_ZINC::get, 1.75F, 250);

    CreateRecipeProvider.GeneratedRecipe IRON_ORE = this.stoneOre(() -> Items.IRON_ORE, () -> AllItems.CRUSHED_IRON::get, 1.75F, 250);

    CreateRecipeProvider.GeneratedRecipe GOLD_ORE = this.stoneOre(() -> Items.GOLD_ORE, AllItems.CRUSHED_GOLD::get, 1.75F, 250);

    CreateRecipeProvider.GeneratedRecipe DIAMOND_ORE = this.stoneOre(() -> Items.DIAMOND_ORE, () -> Items.DIAMOND, 1.75F, 350);

    CreateRecipeProvider.GeneratedRecipe EMERALD_ORE = this.stoneOre(() -> Items.EMERALD_ORE, () -> Items.EMERALD, 1.75F, 350);

    CreateRecipeProvider.GeneratedRecipe COAL_ORE = this.stoneOre(() -> Items.COAL_ORE, () -> Items.COAL, 1.75F, 150);

    CreateRecipeProvider.GeneratedRecipe REDSTONE_ORE = this.stoneOre(() -> Items.REDSTONE_ORE, () -> Items.REDSTONE, 6.5F, 250);

    CreateRecipeProvider.GeneratedRecipe LAPIS_ORE = this.stoneOre(() -> Items.LAPIS_ORE, () -> Items.LAPIS_LAZULI, 10.5F, 250);

    CreateRecipeProvider.GeneratedRecipe DEEP_COPPER_ORE = this.deepslateOre(() -> Items.DEEPSLATE_COPPER_ORE, AllItems.CRUSHED_COPPER::get, 7.25F, 350);

    CreateRecipeProvider.GeneratedRecipe DEEP_ZINC_ORE = this.deepslateOre(AllBlocks.DEEPSLATE_ZINC_ORE::get, AllItems.CRUSHED_ZINC::get, 2.25F, 350);

    CreateRecipeProvider.GeneratedRecipe DEEP_IRON_ORE = this.deepslateOre(() -> Items.DEEPSLATE_IRON_ORE, AllItems.CRUSHED_IRON::get, 2.25F, 350);

    CreateRecipeProvider.GeneratedRecipe DEEP_GOLD_ORE = this.deepslateOre(() -> Items.DEEPSLATE_GOLD_ORE, AllItems.CRUSHED_GOLD::get, 2.25F, 350);

    CreateRecipeProvider.GeneratedRecipe DEEP_DIAMOND_ORE = this.deepslateOre(() -> Items.DEEPSLATE_DIAMOND_ORE, () -> Items.DIAMOND, 2.25F, 450);

    CreateRecipeProvider.GeneratedRecipe DEEP_EMERALD_ORE = this.deepslateOre(() -> Items.DEEPSLATE_EMERALD_ORE, () -> Items.EMERALD, 2.25F, 450);

    CreateRecipeProvider.GeneratedRecipe DEEP_COAL_ORE = this.deepslateOre(() -> Items.DEEPSLATE_COAL_ORE, () -> Items.COAL, 1.75F, 250);

    CreateRecipeProvider.GeneratedRecipe DEEP_REDSTONE_ORE = this.deepslateOre(() -> Items.DEEPSLATE_REDSTONE_ORE, () -> Items.REDSTONE, 7.5F, 350);

    CreateRecipeProvider.GeneratedRecipe DEEP_LAPIS_ORE = this.deepslateOre(() -> Items.DEEPSLATE_LAPIS_ORE, () -> Items.LAPIS_LAZULI, 12.5F, 350);

    CreateRecipeProvider.GeneratedRecipe NETHER_GOLD_ORE = this.netherOre(() -> Items.NETHER_GOLD_ORE, () -> Items.GOLD_NUGGET, 18.0F, 350);

    CreateRecipeProvider.GeneratedRecipe NETHER_QUARTZ_ORE = this.netherOre(() -> Items.NETHER_QUARTZ_ORE, () -> Items.QUARTZ, 2.25F, 350);

    CreateRecipeProvider.GeneratedRecipe RAW_COPPER_ORE = this.rawOre(() -> Items.RAW_COPPER, AllItems.CRUSHED_COPPER::get, 1);

    CreateRecipeProvider.GeneratedRecipe RAW_ZINC_ORE = this.rawOre(AllItems.RAW_ZINC::get, AllItems.CRUSHED_ZINC::get, 1);

    CreateRecipeProvider.GeneratedRecipe RAW_IRON_ORE = this.rawOre(() -> Items.RAW_IRON, AllItems.CRUSHED_IRON::get, 1);

    CreateRecipeProvider.GeneratedRecipe RAW_GOLD_ORE = this.rawOre(() -> Items.RAW_GOLD, AllItems.CRUSHED_GOLD::get, 1);

    CreateRecipeProvider.GeneratedRecipe OSMIUM_ORE = this.moddedOre(CompatMetals.OSMIUM, AllItems.CRUSHED_OSMIUM::get);

    CreateRecipeProvider.GeneratedRecipe PLATINUM_ORE = this.moddedOre(CompatMetals.PLATINUM, AllItems.CRUSHED_PLATINUM::get);

    CreateRecipeProvider.GeneratedRecipe SILVER_ORE = this.moddedOre(CompatMetals.SILVER, AllItems.CRUSHED_SILVER::get);

    CreateRecipeProvider.GeneratedRecipe TIN_ORE = this.moddedOre(CompatMetals.TIN, AllItems.CRUSHED_TIN::get);

    CreateRecipeProvider.GeneratedRecipe QUICKSILVER_ORE = this.moddedOre(CompatMetals.QUICKSILVER, AllItems.CRUSHED_QUICKSILVER::get);

    CreateRecipeProvider.GeneratedRecipe LEAD_ORE = this.moddedOre(CompatMetals.LEAD, AllItems.CRUSHED_LEAD::get);

    CreateRecipeProvider.GeneratedRecipe ALUMINUM_ORE = this.moddedOre(CompatMetals.ALUMINUM, AllItems.CRUSHED_BAUXITE::get);

    CreateRecipeProvider.GeneratedRecipe URANIUM_ORE = this.moddedOre(CompatMetals.URANIUM, AllItems.CRUSHED_URANIUM::get);

    CreateRecipeProvider.GeneratedRecipe NICKEL_ORE = this.moddedOre(CompatMetals.NICKEL, AllItems.CRUSHED_NICKEL::get);

    CreateRecipeProvider.GeneratedRecipe OSMIUM_RAW_ORE = this.moddedRawOre(CompatMetals.OSMIUM, AllItems.CRUSHED_OSMIUM::get, 1);

    CreateRecipeProvider.GeneratedRecipe PLATINUM_RAW_ORE = this.moddedRawOre(CompatMetals.PLATINUM, AllItems.CRUSHED_PLATINUM::get, 1);

    CreateRecipeProvider.GeneratedRecipe SILVER_RAW_ORE = this.moddedRawOre(CompatMetals.SILVER, AllItems.CRUSHED_SILVER::get, 1);

    CreateRecipeProvider.GeneratedRecipe TIN_RAW_ORE = this.moddedRawOre(CompatMetals.TIN, AllItems.CRUSHED_TIN::get, 1);

    CreateRecipeProvider.GeneratedRecipe QUICKSILVER_RAW_ORE = this.moddedRawOre(CompatMetals.QUICKSILVER, AllItems.CRUSHED_QUICKSILVER::get, 1);

    CreateRecipeProvider.GeneratedRecipe LEAD_RAW_ORE = this.moddedRawOre(CompatMetals.LEAD, AllItems.CRUSHED_LEAD::get, 1);

    CreateRecipeProvider.GeneratedRecipe ALUMINUM_RAW_ORE = this.moddedRawOre(CompatMetals.ALUMINUM, AllItems.CRUSHED_BAUXITE::get, 1);

    CreateRecipeProvider.GeneratedRecipe URANIUM_RAW_ORE = this.moddedRawOre(CompatMetals.URANIUM, AllItems.CRUSHED_URANIUM::get, 1);

    CreateRecipeProvider.GeneratedRecipe NICKEL_RAW_ORE = this.moddedRawOre(CompatMetals.NICKEL, AllItems.CRUSHED_NICKEL::get, 1);

    CreateRecipeProvider.GeneratedRecipe RAW_COPPER_BLOCK = this.rawOre(() -> Items.RAW_COPPER_BLOCK, AllItems.CRUSHED_COPPER::get, 9);

    CreateRecipeProvider.GeneratedRecipe RAW_ZINC_BLOCK = this.rawOre(AllBlocks.RAW_ZINC_BLOCK::get, AllItems.CRUSHED_ZINC::get, 9);

    CreateRecipeProvider.GeneratedRecipe RAW_IRON_BLOCK = this.rawOre(() -> Items.RAW_IRON_BLOCK, AllItems.CRUSHED_IRON::get, 9);

    CreateRecipeProvider.GeneratedRecipe RAW_GOLD_BLOCK = this.rawOre(() -> Items.RAW_GOLD_BLOCK, AllItems.CRUSHED_GOLD::get, 9);

    CreateRecipeProvider.GeneratedRecipe OSMIUM_RAW_BLOCK = this.moddedRawOre(CompatMetals.OSMIUM, AllItems.CRUSHED_OSMIUM::get, 9);

    CreateRecipeProvider.GeneratedRecipe PLATINUM_RAW_BLOCK = this.moddedRawOre(CompatMetals.PLATINUM, AllItems.CRUSHED_PLATINUM::get, 9);

    CreateRecipeProvider.GeneratedRecipe SILVER_RAW_BLOCK = this.moddedRawOre(CompatMetals.SILVER, AllItems.CRUSHED_SILVER::get, 9);

    CreateRecipeProvider.GeneratedRecipe TIN_RAW_BLOCK = this.moddedRawOre(CompatMetals.TIN, AllItems.CRUSHED_TIN::get, 9);

    CreateRecipeProvider.GeneratedRecipe QUICKSILVER_RAW_BLOCK = this.moddedRawOre(CompatMetals.QUICKSILVER, AllItems.CRUSHED_QUICKSILVER::get, 9);

    CreateRecipeProvider.GeneratedRecipe LEAD_RAW_BLOCK = this.moddedRawOre(CompatMetals.LEAD, AllItems.CRUSHED_LEAD::get, 9);

    CreateRecipeProvider.GeneratedRecipe ALUMINUM_RAW_BLOCK = this.moddedRawOre(CompatMetals.ALUMINUM, AllItems.CRUSHED_BAUXITE::get, 9);

    CreateRecipeProvider.GeneratedRecipe URANIUM_RAW_BLOCK = this.moddedRawOre(CompatMetals.URANIUM, AllItems.CRUSHED_URANIUM::get, 9);

    CreateRecipeProvider.GeneratedRecipe NICKEL_RAW_BLOCK = this.moddedRawOre(CompatMetals.NICKEL, AllItems.CRUSHED_NICKEL::get, 9);

    CreateRecipeProvider.GeneratedRecipe NETHER_WART = this.create("nether_wart_block", b -> b.duration(150).require(Blocks.NETHER_WART_BLOCK).output(0.25F, Items.NETHER_WART, 1));

    CreateRecipeProvider.GeneratedRecipe AMETHYST_CLUSTER = this.create(() -> Blocks.AMETHYST_CLUSTER, b -> b.duration(150).output(Items.AMETHYST_SHARD, 7).output(0.5F, Items.AMETHYST_SHARD));

    CreateRecipeProvider.GeneratedRecipe GLOWSTONE = this.create(() -> Blocks.GLOWSTONE, b -> b.duration(150).output(Items.GLOWSTONE_DUST, 3).output(0.5F, Items.GLOWSTONE_DUST));

    CreateRecipeProvider.GeneratedRecipe AMETHYST_BLOCK = this.create(() -> Blocks.AMETHYST_BLOCK, b -> b.duration(150).output(Items.AMETHYST_SHARD, 3).output(0.5F, Items.AMETHYST_SHARD));

    CreateRecipeProvider.GeneratedRecipe LEATHER_HORSE_ARMOR = this.create(() -> Items.LEATHER_HORSE_ARMOR, b -> b.duration(200).output(Items.LEATHER, 2).output(0.5F, Items.LEATHER, 2));

    CreateRecipeProvider.GeneratedRecipe IRON_HORSE_ARMOR = this.create(() -> Items.IRON_HORSE_ARMOR, b -> b.duration(200).output(Items.IRON_INGOT, 2).output(0.5F, Items.LEATHER, 1).output(0.5F, Items.IRON_INGOT, 1).output(0.25F, Items.STRING, 2).output(0.25F, Items.IRON_NUGGET, 4));

    CreateRecipeProvider.GeneratedRecipe GOLDEN_HORSE_ARMOR = this.create(() -> Items.GOLDEN_HORSE_ARMOR, b -> b.duration(200).output(Items.GOLD_INGOT, 2).output(0.5F, Items.LEATHER, 2).output(0.5F, Items.GOLD_INGOT, 2).output(0.25F, Items.STRING, 2).output(0.25F, Items.GOLD_NUGGET, 8));

    CreateRecipeProvider.GeneratedRecipe DIAMOND_HORSE_ARMOR = this.create(() -> Items.DIAMOND_HORSE_ARMOR, b -> b.duration(200).output(Items.DIAMOND, 1).output(0.5F, Items.LEATHER, 2).output(0.1F, Items.DIAMOND, 3).output(0.25F, Items.STRING, 2));

    CreateRecipeProvider.GeneratedRecipe GRAVEL = this.create(() -> Blocks.GRAVEL, b -> b.duration(250).output(Blocks.SAND).output(0.1F, Items.FLINT).output(0.05F, Items.CLAY_BALL));

    CreateRecipeProvider.GeneratedRecipe NETHERRACK = this.create(() -> Blocks.NETHERRACK, b -> b.duration(250).output((ItemLike) AllItems.CINDER_FLOUR.get()).output(0.5F, (ItemLike) AllItems.CINDER_FLOUR.get()));

    protected CreateRecipeProvider.GeneratedRecipe stoneOre(Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount, int duration) {
        return this.ore(Blocks.COBBLESTONE, ore, raw, expectedAmount, duration);
    }

    protected CreateRecipeProvider.GeneratedRecipe deepslateOre(Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount, int duration) {
        return this.ore(Blocks.COBBLED_DEEPSLATE, ore, raw, expectedAmount, duration);
    }

    protected CreateRecipeProvider.GeneratedRecipe netherOre(Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount, int duration) {
        return this.ore(Blocks.NETHERRACK, ore, raw, expectedAmount, duration);
    }

    protected CreateRecipeProvider.GeneratedRecipe mineralRecycling(AllPaletteStoneTypes type, Supplier<ItemLike> crushed, Supplier<ItemLike> nugget, float chance) {
        return this.mineralRecycling(type, b -> b.duration(250).output(chance, (ItemLike) crushed.get(), 1).output(chance, (ItemLike) nugget.get(), 1));
    }

    protected CreateRecipeProvider.GeneratedRecipe mineralRecycling(AllPaletteStoneTypes type, UnaryOperator<ProcessingRecipeBuilder<ProcessingRecipe<?>>> transform) {
        this.create(Lang.asId(type.name()) + "_recycling", b -> (ProcessingRecipeBuilder) transform.apply(b.require(type.materialTag)));
        return this.create(type.getBaseBlock()::get, transform);
    }

    protected CreateRecipeProvider.GeneratedRecipe ore(ItemLike stoneType, Supplier<ItemLike> ore, Supplier<ItemLike> raw, float expectedAmount, int duration) {
        return this.create(ore, b -> {
            ProcessingRecipeBuilder<ProcessingRecipe<?>> builder = b.duration(duration).output((ItemLike) raw.get(), Mth.floor(expectedAmount));
            float extra = expectedAmount - (float) Mth.floor(expectedAmount);
            if (extra > 0.0F) {
                builder.output(extra, (ItemLike) raw.get(), 1);
            }
            builder.output(0.75F, (ItemLike) AllItems.EXP_NUGGET.get(), raw.get() == AllItems.CRUSHED_GOLD.get() ? 2 : 1);
            return builder.output(0.125F, stoneType);
        });
    }

    protected CreateRecipeProvider.GeneratedRecipe rawOre(Supplier<ItemLike> input, Supplier<ItemLike> result, int amount) {
        return this.create(input, b -> b.duration(400).output((ItemLike) result.get(), amount).output(0.75F, (ItemLike) AllItems.EXP_NUGGET.get(), (result.get() == AllItems.CRUSHED_GOLD.get() ? 2 : 1) * amount));
    }

    protected CreateRecipeProvider.GeneratedRecipe moddedRawOre(CompatMetals metal, Supplier<ItemLike> result, int amount) {
        String name = metal.getName();
        return this.create("raw_" + name + (amount == 1 ? "_ore" : "_block"), b -> {
            String prefix = amount == 1 ? "raw_materials/" : "storage_blocks/raw_";
            return b.duration(400).withCondition(new NotCondition(new TagEmptyCondition("forge", prefix + name))).require(AllTags.forgeItemTag(prefix + name)).output((ItemLike) result.get(), amount).output(0.75F, (ItemLike) AllItems.EXP_NUGGET.get(), amount);
        });
    }

    protected CreateRecipeProvider.GeneratedRecipe moddedOre(CompatMetals metal, Supplier<ItemLike> result) {
        String name = metal.getName();
        return this.create(name + "_ore", b -> {
            String prefix = "ores/";
            return b.duration(400).withCondition(new NotCondition(new TagEmptyCondition("forge", prefix + name))).require(AllTags.forgeItemTag(prefix + name)).output((ItemLike) result.get(), 1).output(0.75F, (ItemLike) result.get(), 1).output(0.75F, (ItemLike) AllItems.EXP_NUGGET.get(), 1);
        });
    }

    public CrushingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CRUSHING;
    }
}