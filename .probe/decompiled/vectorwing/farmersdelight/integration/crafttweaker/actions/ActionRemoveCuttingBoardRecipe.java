package vectorwing.farmersdelight.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.recipe.ActionRecipeBase;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.script.ScriptException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

public class ActionRemoveCuttingBoardRecipe extends ActionRecipeBase {

    private final IItemStack[] outputs;

    public ActionRemoveCuttingBoardRecipe(IRecipeManager manager, IItemStack[] outputs) {
        super(manager);
        this.outputs = outputs;
    }

    public void apply() {
        Iterator<Entry<ResourceLocation, Recipe<?>>> it = this.getManager().getRecipes().entrySet().iterator();
        label30: while (it.hasNext()) {
            CuttingBoardRecipe recipe = (CuttingBoardRecipe) ((Entry) it.next()).getValue();
            if (recipe.getResults().size() == this.outputs.length) {
                int i = 0;
                for (ItemStack result : recipe.getResults()) {
                    if (!this.outputs[i++].matches(new MCItemStackMutable(result))) {
                        continue label30;
                    }
                }
                it.remove();
            }
        }
    }

    public String describe() {
        return "Removing \"" + ForgeRegistries.RECIPE_TYPES.getKey(this.getManager().getRecipeType()) + "\" recipes with output: " + Arrays.toString(this.outputs) + "\"";
    }

    public boolean validate(Logger logger) {
        if (this.outputs == null) {
            logger.throwing(Level.WARN, new ScriptException("Output IItemStacks cannot be null!"));
            return false;
        } else {
            return true;
        }
    }
}