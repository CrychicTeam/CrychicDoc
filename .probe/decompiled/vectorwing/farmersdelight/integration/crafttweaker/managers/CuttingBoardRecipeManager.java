package vectorwing.farmersdelight.integration.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;
import org.openzen.zencode.java.ZenCodeType.OptionalString;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.ListUtils;
import vectorwing.farmersdelight.integration.crafttweaker.actions.ActionRemoveCuttingBoardRecipe;

@Document("mods/FarmersDelight/CuttingBoard")
@ZenRegister
@Name("mods.farmersdelight.CuttingBoard")
public class CuttingBoardRecipeManager implements IRecipeManager {

    @Method
    public void addRecipe(String name, IIngredient input, Percentaged<IItemStack>[] results, IIngredient tool, @OptionalString String sound) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this, new CuttingBoardRecipe(CraftTweakerConstants.rl(name), "", input.asVanillaIngredient(), tool.asVanillaIngredient(), ListUtils.mapArrayIndexSet(results, stack -> new ChanceResult(((IItemStack) stack.getData()).getInternal(), (float) stack.getPercentage()), NonNullList.withSize(results.length, ChanceResult.EMPTY)), sound), ""));
    }

    @Method
    public void removeRecipe(IItemStack[] outputs) {
        CraftTweakerAPI.apply(new ActionRemoveCuttingBoardRecipe(this, outputs));
    }

    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return ModRecipeTypes.CUTTING.get();
    }
}