package mezz.jei.api.constants;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IJeiCompostingRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.recipe.vanilla.IJeiIngredientInfoRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;

public final class RecipeTypes {

    public static final RecipeType<CraftingRecipe> CRAFTING = RecipeType.create("minecraft", "crafting", CraftingRecipe.class);

    public static final RecipeType<StonecutterRecipe> STONECUTTING = RecipeType.create("minecraft", "stonecutting", StonecutterRecipe.class);

    public static final RecipeType<SmeltingRecipe> SMELTING = RecipeType.create("minecraft", "furnace", SmeltingRecipe.class);

    public static final RecipeType<SmokingRecipe> SMOKING = RecipeType.create("minecraft", "smoking", SmokingRecipe.class);

    public static final RecipeType<BlastingRecipe> BLASTING = RecipeType.create("minecraft", "blasting", BlastingRecipe.class);

    public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = RecipeType.create("minecraft", "campfire", CampfireCookingRecipe.class);

    public static final RecipeType<IJeiFuelingRecipe> FUELING = RecipeType.create("minecraft", "fuel", IJeiFuelingRecipe.class);

    public static final RecipeType<IJeiBrewingRecipe> BREWING = RecipeType.create("minecraft", "brewing", IJeiBrewingRecipe.class);

    public static final RecipeType<IJeiAnvilRecipe> ANVIL = RecipeType.create("minecraft", "anvil", IJeiAnvilRecipe.class);

    public static final RecipeType<SmithingRecipe> SMITHING = RecipeType.create("minecraft", "smithing", SmithingRecipe.class);

    public static final RecipeType<IJeiCompostingRecipe> COMPOSTING = RecipeType.create("minecraft", "compostable", IJeiCompostingRecipe.class);

    public static final RecipeType<IJeiIngredientInfoRecipe> INFORMATION = RecipeType.create("jei", "information", IJeiIngredientInfoRecipe.class);

    private RecipeTypes() {
    }
}