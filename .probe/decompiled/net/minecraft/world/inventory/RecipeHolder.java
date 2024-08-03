package net.minecraft.world.inventory;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public interface RecipeHolder {

    void setRecipeUsed(@Nullable Recipe<?> var1);

    @Nullable
    Recipe<?> getRecipeUsed();

    default void awardUsedRecipes(Player player0, List<ItemStack> listItemStack1) {
        Recipe<?> $$2 = this.getRecipeUsed();
        if ($$2 != null) {
            player0.triggerRecipeCrafted($$2, listItemStack1);
            if (!$$2.isSpecial()) {
                player0.awardRecipes(Collections.singleton($$2));
                this.setRecipeUsed(null);
            }
        }
    }

    default boolean setRecipeUsed(Level level0, ServerPlayer serverPlayer1, Recipe<?> recipe2) {
        if (!recipe2.isSpecial() && level0.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING) && !serverPlayer1.getRecipeBook().m_12709_(recipe2)) {
            return false;
        } else {
            this.setRecipeUsed(recipe2);
            return true;
        }
    }
}