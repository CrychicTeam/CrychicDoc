package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents.Input;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents.Metadata;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents.Output;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler.For;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

@For(CuttingBoardRecipe.class)
public class CuttingBoardRecipeHandler implements IRecipeHandler<CuttingBoardRecipe> {

    public String dumpToCommandString(IRecipeManager manager, CuttingBoardRecipe recipe) {
        return String.format("%s.addRecipe(%s, %s, %s, %s, %s);", manager.getCommandString(), StringUtil.quoteAndEscape(recipe.getId()), IIngredient.fromIngredient(recipe.getIngredients().get(0)).getCommandString(), recipe.getResults().stream().map(MCItemStackMutable::new).map(IItemStack::getCommandString).collect(Collectors.joining(", ", "[", "]")), IIngredient.fromIngredient(recipe.getTool()).getCommandString(), recipe.getSoundEventID());
    }

    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe firstRecipe, U secondRecipe) {
        return firstRecipe.equals(secondRecipe);
    }

    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe recipe) {
        IDecomposedRecipe decomposedRecipe = IDecomposedRecipe.builder().with(Input.INGREDIENTS, recipe.getIngredients().stream().map(IIngredient::fromIngredient).toList()).with(RecipeHandlerUtils.TOOL_COMPONENT, IIngredient.fromIngredient(recipe.getTool())).with(Metadata.GROUP, recipe.getGroup()).with(Output.CHANCED_ITEMS, recipe.getRollableResults().stream().map(chanceResult -> new MCItemStack(chanceResult.getStack()).percent((double) chanceResult.getChance())).toList()).build();
        if (!recipe.getSoundEventID().equals("")) {
            decomposedRecipe.set(RecipeHandlerUtils.SOUND_COMPONENT, recipe.getSoundEventID());
        }
        return Optional.of(decomposedRecipe);
    }

    public Optional<CuttingBoardRecipe> recompose(IRecipeManager<? super CuttingBoardRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        String group = (String) recipe.getOrThrowSingle(Metadata.GROUP);
        List<IIngredient> ingredients = recipe.getOrThrow(Input.INGREDIENTS);
        IIngredient tool = (IIngredient) recipe.getOrThrowSingle(RecipeHandlerUtils.TOOL_COMPONENT);
        IIngredient[] ingredientArray = (IIngredient[]) ingredients.toArray(IIngredient[]::new);
        List<Percentaged<IItemStack>> results = recipe.getOrThrow(Output.CHANCED_ITEMS);
        NonNullList<ChanceResult> stackedResults = NonNullList.create();
        stackedResults.addAll(results.stream().map(iItemStackPercentaged -> new ChanceResult(((IItemStack) iItemStackPercentaged.getData()).getInternal(), (float) iItemStackPercentaged.getPercentage())).toList());
        List<String> soundList = recipe.get(RecipeHandlerUtils.SOUND_COMPONENT);
        String sound = soundList == null ? "" : (String) soundList.get(0);
        Ingredient input = ingredientArray[0].asVanillaIngredient();
        return Optional.of(new CuttingBoardRecipe(name, group, input, tool.asVanillaIngredient(), stackedResults, sound));
    }
}