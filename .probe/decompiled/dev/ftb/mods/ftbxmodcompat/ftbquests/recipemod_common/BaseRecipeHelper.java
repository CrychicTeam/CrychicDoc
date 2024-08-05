package dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common;

import dev.ftb.mods.ftbquests.integration.RecipeModHelper;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;

public abstract class BaseRecipeHelper implements RecipeModHelper {

    @Override
    public void refreshAll(RecipeModHelper.Components component) {
        switch(component) {
            case QUESTS:
                this.refreshQuests();
                break;
            case LOOT_CRATES:
                this.refreshLootcrates();
        }
    }

    @Override
    public void refreshRecipes(QuestObjectBase object) {
        if (object != null) {
            object.componentsToRefresh().forEach(this::refreshAll);
        }
    }

    @Override
    public boolean isRecipeModAvailable() {
        return true;
    }

    protected abstract void refreshQuests();

    protected abstract void refreshLootcrates();
}