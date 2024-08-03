package dev.ftb.mods.ftbxmodcompat.ftbquests.jei.helper;

import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.ftbquests.jei.FTBQuestsJEIIntegration;
import dev.ftb.mods.ftbxmodcompat.ftbquests.jei.LootCrateRecipeManagerPlugin;
import dev.ftb.mods.ftbxmodcompat.ftbquests.jei.QuestRecipeManagerPlugin;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.BaseRecipeHelper;
import java.util.Collection;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.ItemStack;

public class JEIRecipeHelper extends BaseRecipeHelper {

    @Override
    public void showRecipes(ItemStack itemStack) {
        FTBQuestsJEIIntegration.showRecipes(itemStack);
    }

    @Override
    public String getHelperName() {
        return "JEI";
    }

    @Override
    protected void refreshQuests() {
        QuestRecipeManagerPlugin.INSTANCE.refresh();
    }

    @Override
    protected void refreshLootcrates() {
        LootCrateRecipeManagerPlugin.INSTANCE.refresh();
    }

    @Override
    public void updateItemsDynamic(Collection<ItemStack> toRemove, Collection<ItemStack> toAdd) {
        if (FTBQuestsJEIIntegration.runtime != null) {
            IIngredientManager manager = FTBQuestsJEIIntegration.runtime.getIngredientManager();
            if (!toRemove.isEmpty()) {
                manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, toRemove);
            }
            if (!toAdd.isEmpty()) {
                manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, toAdd);
            }
            FTBXModCompat.LOGGER.debug("removed {} items from JEI, added {} items", toRemove.size(), toAdd.size());
        }
    }
}