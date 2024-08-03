package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class CuttingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe ANDESITE_ALLOY = this.create(CreateRecipeProvider.I::andesite, b -> b.duration(200).output((ItemLike) AllBlocks.SHAFT.get(), 6));

    CreateRecipeProvider.GeneratedRecipe BAMBOO_PLANKS = this.create(() -> Blocks.BAMBOO_PLANKS, b -> b.duration(20).output(Blocks.BAMBOO_MOSAIC, 1));

    CreateRecipeProvider.GeneratedRecipe OAK_WOOD = this.stripAndMakePlanks(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_PLANKS);

    CreateRecipeProvider.GeneratedRecipe SPRUCE_WOOD = this.stripAndMakePlanks(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe BIRCH_WOOD = this.stripAndMakePlanks(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_PLANKS);

    CreateRecipeProvider.GeneratedRecipe JUNGLE_WOOD = this.stripAndMakePlanks(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe ACACIA_WOOD = this.stripAndMakePlanks(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_PLANKS);

    CreateRecipeProvider.GeneratedRecipe CHERRY_WOOD = this.stripAndMakePlanks(Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_PLANKS);

    CreateRecipeProvider.GeneratedRecipe DARK_OAK_WOOD = this.stripAndMakePlanks(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS);

    CreateRecipeProvider.GeneratedRecipe MANGROVE_WOOD = this.stripAndMakePlanks(Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe CRIMSON_WOOD = this.stripAndMakePlanks(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_PLANKS);

    CreateRecipeProvider.GeneratedRecipe WARPED_WOOD = this.stripAndMakePlanks(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_PLANKS);

    CreateRecipeProvider.GeneratedRecipe OAK_LOG = this.stripAndMakePlanks(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_PLANKS);

    CreateRecipeProvider.GeneratedRecipe SPRUCE_LOG = this.stripAndMakePlanks(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe BIRCH_LOG = this.stripAndMakePlanks(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_PLANKS);

    CreateRecipeProvider.GeneratedRecipe JUNGLE_LOG = this.stripAndMakePlanks(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe ACACIA_LOG = this.stripAndMakePlanks(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_PLANKS);

    CreateRecipeProvider.GeneratedRecipe CHERRY_LOG = this.stripAndMakePlanks(Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_PLANKS);

    CreateRecipeProvider.GeneratedRecipe DARK_OAK_LOG = this.stripAndMakePlanks(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_PLANKS);

    CreateRecipeProvider.GeneratedRecipe MANGROVE_LOG = this.stripAndMakePlanks(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_PLANKS);

    CreateRecipeProvider.GeneratedRecipe BAMBOO_BLOCK = this.stripAndMakePlanks(Blocks.BAMBOO_BLOCK, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_PLANKS, 3);

    CreateRecipeProvider.GeneratedRecipe CRIMSON_LOG = this.stripAndMakePlanks(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_PLANKS);

    CreateRecipeProvider.GeneratedRecipe WARPED_LOG = this.stripAndMakePlanks(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_PLANKS);

    CreateRecipeProvider.GeneratedRecipe ARS_N = this.cuttingCompat(Mods.ARS_N, "blue_archwood", "purple_archwood", "green_archwood", "red_archwood");

    CreateRecipeProvider.GeneratedRecipe BTN = this.cuttingCompat(Mods.BTN, "livingwood", "dreamwood");

    CreateRecipeProvider.GeneratedRecipe BTN_2 = this.stripAndMakePlanks(Mods.BTN, "glimmering_livingwood_log", "glimmering_stripped_livingwood_log", "livingwood_planks");

    CreateRecipeProvider.GeneratedRecipe BTN_3 = this.stripAndMakePlanks(Mods.BTN, "glimmering_livingwood", "glimmering_stripped_livingwood", "livingwood_planks");

    CreateRecipeProvider.GeneratedRecipe BTN_4 = this.stripAndMakePlanks(Mods.BTN, "glimmering_dreamwood_log", "glimmering_stripped_dreamwood_log", "dreamwood_planks");

    CreateRecipeProvider.GeneratedRecipe BTN_5 = this.stripAndMakePlanks(Mods.BTN, "glimmering_dreamwood", "glimmering_stripped_dreamwood", "dreamwood_planks");

    CreateRecipeProvider.GeneratedRecipe FA = this.cuttingCompat(Mods.FA, "cherrywood", "mysterywood");

    CreateRecipeProvider.GeneratedRecipe HEX = this.cuttingCompat(Mods.HEX, "edified");

    CreateRecipeProvider.GeneratedRecipe ID = this.cuttingCompat(Mods.ID, "menril");

    CreateRecipeProvider.GeneratedRecipe BYG = this.cuttingCompat(Mods.BYG, "aspen", "baobab", "blue_enchanted", "cherry", "cika", "cypress", "ebony", "ether", "fir", "green_enchanted", "holly", "jacaranda", "lament", "mahogany", "maple", "nightshade", "palm", "pine", "rainbow_eucalyptus", "redwood", "skyris", "willow", "witch_hazel", "zelkova");

    CreateRecipeProvider.GeneratedRecipe BYG_2 = this.stripAndMakePlanks(Mods.BYG, "bulbis_stem", "stripped_bulbis_stem", "bulbis_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_3 = this.stripAndMakePlanks(Mods.BYG, "bulbis_wood", "stripped_bulbis_wood", "bulbis_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_4 = this.stripAndMakePlanks(Mods.BYG, null, "imparius_stem", "imparius_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_5 = this.stripAndMakePlanks(Mods.BYG, null, "imparius_hyphae", "imparius_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_6 = this.stripAndMakePlanks(Mods.BYG, null, "fungal_imparius_stem", "imparius_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_7 = this.stripAndMakePlanks(Mods.BYG, null, "fungal_imparius_hyphae", "imparius_planks");

    CreateRecipeProvider.GeneratedRecipe BYG_8 = this.stripAndMakePlanks(Mods.BYG, "palo_verde_log", "stripped_palo_verde_log", null);

    CreateRecipeProvider.GeneratedRecipe BYG_9 = this.stripAndMakePlanks(Mods.BYG, "palo_verde_wood", "stripped_palo_verde_wood", null);

    CreateRecipeProvider.GeneratedRecipe SG = this.cuttingCompat(Mods.SG, "netherwood");

    CreateRecipeProvider.GeneratedRecipe TF = this.cuttingCompat(Mods.TF, "twilight_oak", "canopy", "mangrove", "dark", "time", "transformation", "mining", "sorting");

    CreateRecipeProvider.GeneratedRecipe TIC = this.cuttingCompat(Mods.TIC, "greenheart", "skyroot", "bloodshroom");

    CreateRecipeProvider.GeneratedRecipe AP = this.cuttingCompat(Mods.AP, "twisted");

    CreateRecipeProvider.GeneratedRecipe Q = this.cuttingCompat(Mods.Q, "azalea", "blossom");

    CreateRecipeProvider.GeneratedRecipe ECO = this.cuttingCompat(Mods.ECO, "coconut", "walnut", "azalea");

    CreateRecipeProvider.GeneratedRecipe ECO_2 = this.stripAndMakePlanks(Mods.ECO, "flowering_azalea_log", "stripped_azalea_log", null);

    CreateRecipeProvider.GeneratedRecipe ECO_3 = this.stripAndMakePlanks(Mods.ECO, "flowering_azalea_wood", "stripped_azalea_wood", null);

    CreateRecipeProvider.GeneratedRecipe BOP = this.cuttingCompat(Mods.BOP, "fir", "redwood", "cherry", "mahogany", "jacaranda", "palm", "willow", "dead", "magic", "umbran", "hellbark");

    CreateRecipeProvider.GeneratedRecipe BSK = this.cuttingCompat(Mods.BSK, "bluebright", "starlit", "frostbright", "lunar", "dusk", "maple", "cherry");

    CreateRecipeProvider.GeneratedRecipe BSK_2 = this.stripAndMakePlanks(Mods.BSK, null, "crystallized_log", "crystallized_planks");

    CreateRecipeProvider.GeneratedRecipe BSK_3 = this.stripAndMakePlanks(Mods.BSK, null, "crystallized_wood", "crystallized_planks");

    CreateRecipeProvider.GeneratedRecipe stripAndMakePlanks(Block wood, Block stripped, Block planks) {
        return this.stripAndMakePlanks(wood, stripped, planks, 6);
    }

    CreateRecipeProvider.GeneratedRecipe stripAndMakePlanks(Block wood, Block stripped, Block planks, int planksAmount) {
        this.create(() -> wood, b -> b.duration(50).output(stripped));
        return this.create(() -> stripped, b -> b.duration(50).output(planks, planksAmount));
    }

    CreateRecipeProvider.GeneratedRecipe cuttingCompat(Mods mod, String... woodtypes) {
        for (String type : woodtypes) {
            String planks = type + "_planks";
            if (mod == Mods.ARS_N && type.contains("archwood")) {
                planks = "archwood_planks";
            }
            String strippedPre = mod.strippedIsSuffix ? "" : "stripped_";
            String strippedPost = mod.strippedIsSuffix ? "_stripped" : "";
            this.stripAndMakePlanks(mod, type + "_log", strippedPre + type + "_log" + strippedPost, planks);
            String wood = type + (mod.omitWoodSuffix ? "" : "_wood");
            this.stripAndMakePlanks(mod, wood, strippedPre + wood + strippedPost, planks);
        }
        return null;
    }

    CreateRecipeProvider.GeneratedRecipe stripAndMakePlanks(Mods mod, String wood, String stripped, String planks) {
        if (wood != null) {
            this.create("compat/" + mod.getId() + "/" + wood, b -> b.duration(50).require(mod, wood).output(1.0F, mod, stripped, 1).whenModLoaded(mod.getId()));
        }
        if (planks != null) {
            this.create("compat/" + mod.getId() + "/" + stripped, b -> b.duration(50).require(mod, stripped).output(1.0F, mod, planks, 6).whenModLoaded(mod.getId()));
        }
        return null;
    }

    public CuttingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.CUTTING;
    }
}