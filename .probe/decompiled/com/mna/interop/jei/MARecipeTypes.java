package com.mna.interop.jei;

import com.mna.api.tools.RLoc;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import com.mna.recipes.crush.CrushingRecipe;
import com.mna.recipes.eldrin.EldrinAltarRecipe;
import com.mna.recipes.eldrin.FumeFilterRecipe;
import com.mna.recipes.manaweaving.ManaweavingRecipe;
import com.mna.recipes.manaweaving.TransmutationRecipe;
import com.mna.recipes.rituals.RitualRecipe;
import com.mna.recipes.runeforging.RuneforgingRecipe;
import com.mna.recipes.runeforging.RunescribingRecipe;
import com.mna.recipes.spells.ComponentRecipe;
import com.mna.recipes.spells.ModifierRecipe;
import com.mna.recipes.spells.ShapeRecipe;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;

public class MARecipeTypes {

    public static final ResourceLocation RITUAL_RLOC = RLoc.create("jei_ritual");

    public static final ResourceLocation ELDRIN_ALTAR_RLOC = RLoc.create("jei_eldrin_altar");

    public static final ResourceLocation ARCANE_FURNACE_RLOC = RLoc.create("jei_arcane_furnace");

    public static final ResourceLocation MANAWEAVING_RLOC = RLoc.create("jei_manaweaving");

    public static final ResourceLocation RUNEFORGING_RLOC = RLoc.create("jei_runeforging");

    public static final ResourceLocation RUNESCRIBING_RLOC = RLoc.create("jei_runescribing");

    public static final ResourceLocation SHAPE_RLOC = RLoc.create("jei_shape");

    public static final ResourceLocation COMPONENT_RLOC = RLoc.create("jei_component");

    public static final ResourceLocation MODIFIER_RLOC = RLoc.create("jei_modifier");

    public static final ResourceLocation CRUSHING_RLOC = RLoc.create("jei_crushing");

    public static final ResourceLocation TRANSMUTATION_RLOC = RLoc.create("jei_transmutation");

    public static final ResourceLocation FUME_RLOC = RLoc.create("jei_fume");

    public static final RecipeType<RitualRecipe> RITUAL = RecipeType.create("mna", "jei_ritual", RitualRecipe.class);

    public static final RecipeType<EldrinAltarRecipe> ELDRIN_ALTAR = RecipeType.create("mna", "jei_eldrin_altar", EldrinAltarRecipe.class);

    public static final RecipeType<ArcaneFurnaceRecipe> ARCANE_FURNACE = RecipeType.create("mna", "jei_arcane_furnace", ArcaneFurnaceRecipe.class);

    public static final RecipeType<ManaweavingRecipe> MANAWEAVING = RecipeType.create("mna", "jei_manaweaving", ManaweavingRecipe.class);

    public static final RecipeType<RuneforgingRecipe> RUNEFORGING = RecipeType.create("mna", "jei_runeforging", RuneforgingRecipe.class);

    public static final RecipeType<RunescribingRecipe> RUNESCRIBING = RecipeType.create("mna", "jei_runescribing", RunescribingRecipe.class);

    public static final RecipeType<ShapeRecipe> SHAPE = RecipeType.create("mna", "jei_shape", ShapeRecipe.class);

    public static final RecipeType<ComponentRecipe> COMPONENT = RecipeType.create("mna", "jei_component", ComponentRecipe.class);

    public static final RecipeType<ModifierRecipe> MODIFIER = RecipeType.create("mna", "jei_modifier", ModifierRecipe.class);

    public static final RecipeType<CrushingRecipe> CRUSHING = RecipeType.create("mna", "jei_crushing", CrushingRecipe.class);

    public static final RecipeType<TransmutationRecipe> TRANSMUTATION = RecipeType.create("mna", "jei_transmutation", TransmutationRecipe.class);

    public static final RecipeType<FumeFilterRecipe> FUME = RecipeType.create("mna", "jei_fume", FumeFilterRecipe.class);
}