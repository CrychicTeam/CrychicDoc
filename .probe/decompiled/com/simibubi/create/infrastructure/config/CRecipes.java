package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CRecipes extends ConfigBase {

    public final ConfigBase.ConfigBool bulkPressing = this.b(false, "bulkPressing", new String[] { CRecipes.Comments.bulkPressing });

    public final ConfigBase.ConfigBool bulkCutting = this.b(false, "bulkCutting", new String[] { CRecipes.Comments.bulkCutting });

    public final ConfigBase.ConfigBool allowBrewingInMixer = this.b(true, "allowBrewingInMixer", new String[] { CRecipes.Comments.allowBrewingInMixer });

    public final ConfigBase.ConfigBool allowShapelessInMixer = this.b(true, "allowShapelessInMixer", new String[] { CRecipes.Comments.allowShapelessInMixer });

    public final ConfigBase.ConfigBool allowShapedSquareInPress = this.b(true, "allowShapedSquareInPress", new String[] { CRecipes.Comments.allowShapedSquareInPress });

    public final ConfigBase.ConfigBool allowRegularCraftingInCrafter = this.b(true, "allowRegularCraftingInCrafter", new String[] { CRecipes.Comments.allowRegularCraftingInCrafter });

    public final ConfigBase.ConfigInt maxFireworkIngredientsInCrafter = this.i(9, 1, "maxFireworkIngredientsInCrafter", new String[] { CRecipes.Comments.maxFireworkIngredientsInCrafter });

    public final ConfigBase.ConfigBool allowStonecuttingOnSaw = this.b(true, "allowStonecuttingOnSaw", new String[] { CRecipes.Comments.allowStonecuttingOnSaw });

    public final ConfigBase.ConfigBool allowWoodcuttingOnSaw = this.b(true, "allowWoodcuttingOnSaw", new String[] { CRecipes.Comments.allowWoodcuttingOnSaw });

    public final ConfigBase.ConfigBool allowCastingBySpout = this.b(true, "allowCastingBySpout", new String[] { CRecipes.Comments.allowCastingBySpout });

    public final ConfigBase.ConfigBool displayLogStrippingRecipes = this.b(true, "displayLogStrippingRecipes", new String[] { CRecipes.Comments.displayLogStrippingRecipes });

    public final ConfigBase.ConfigInt lightSourceCountForRefinedRadiance = this.i(10, 1, "lightSourceCountForRefinedRadiance", new String[] { CRecipes.Comments.refinedRadiance });

    public final ConfigBase.ConfigBool enableRefinedRadianceRecipe = this.b(true, "enableRefinedRadianceRecipe", new String[] { CRecipes.Comments.refinedRadianceRecipe });

    public final ConfigBase.ConfigBool enableShadowSteelRecipe = this.b(true, "enableShadowSteelRecipe", new String[] { CRecipes.Comments.shadowSteelRecipe });

    @Override
    public String getName() {
        return "recipes";
    }

    private static class Comments {

        static String bulkPressing = "Allow the Mechanical Press to process entire stacks at a time.";

        static String bulkCutting = "Allow the Mechanical Saw to process entire stacks at a time.";

        static String allowBrewingInMixer = "Allow supported potions to be brewed by a Mechanical Mixer + Basin.";

        static String allowShapelessInMixer = "Allow any shapeless crafting recipes to be processed by a Mechanical Mixer + Basin.";

        static String allowShapedSquareInPress = "Allow any single-ingredient 2x2 or 3x3 crafting recipes to be processed by a Mechanical Press + Basin.";

        static String allowRegularCraftingInCrafter = "Allow any standard crafting recipes to be processed by Mechanical Crafters.";

        static String maxFireworkIngredientsInCrafter = "The Maximum amount of ingredients that can be used to craft Firework Rockets using Mechanical Crafters.";

        static String allowStonecuttingOnSaw = "Allow any stonecutting recipes to be processed by a Mechanical Saw.";

        static String allowWoodcuttingOnSaw = "Allow any Druidcraft woodcutter recipes to be processed by a Mechanical Saw.";

        static String allowCastingBySpout = "Allow Spouts to interact with Casting Tables and Basins from Tinkers' Construct.";

        static String refinedRadiance = "The amount of Light sources destroyed before Chromatic Compound turns into Refined Radiance.";

        static String refinedRadianceRecipe = "Allow the standard in-world Refined Radiance recipes.";

        static String shadowSteelRecipe = "Allow the standard in-world Shadow Steel recipe.";

        static String displayLogStrippingRecipes = "Display vanilla Log-stripping interactions in JEI.";
    }
}