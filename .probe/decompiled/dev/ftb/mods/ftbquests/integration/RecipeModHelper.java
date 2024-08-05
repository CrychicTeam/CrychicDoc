package dev.ftb.mods.ftbquests.integration;

import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import java.util.Collection;
import net.minecraft.world.item.ItemStack;

public interface RecipeModHelper {

    void refreshAll(RecipeModHelper.Components var1);

    void refreshRecipes(QuestObjectBase var1);

    void showRecipes(ItemStack var1);

    default boolean isRecipeModAvailable() {
        return false;
    }

    default void updateItemsDynamic(Collection<ItemStack> toRemove, Collection<ItemStack> toAdd) {
    }

    String getHelperName();

    public static enum Components {

        QUESTS, LOOT_CRATES
    }

    public static class NoOp implements RecipeModHelper {

        @Override
        public void refreshAll(RecipeModHelper.Components component) {
        }

        @Override
        public void refreshRecipes(QuestObjectBase object) {
        }

        @Override
        public void showRecipes(ItemStack object) {
        }

        @Override
        public String getHelperName() {
            return "NO-OP";
        }
    }
}