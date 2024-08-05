package com.mna.recipes.manaweaving;

import com.mna.recipes.RecipeInit;
import com.mna.tools.ContainerTools;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaweavingPatternHelper {

    @Nullable
    public static ManaweavingPattern GetManaweavingRecipe(Level world, ResourceLocation rLoc) {
        if (world != null && rLoc != null) {
            if (rLoc.getNamespace().equals("mna") && !rLoc.getPath().startsWith("manaweave_patterns/")) {
                rLoc = new ResourceLocation(rLoc.getNamespace(), "manaweave_patterns/" + rLoc.getPath());
            }
            List<ManaweavingPattern> mwpList = world.getRecipeManager().getRecipesFor(RecipeInit.MANAWEAVING_PATTERN_TYPE.get(), ContainerTools.createTemporaryContainer(ItemStack.EMPTY), world);
            if (mwpList != null && mwpList.size() != 0) {
                for (ManaweavingPattern pattern : mwpList) {
                    if (pattern.m_6423_().compareTo(rLoc) == 0) {
                        return pattern;
                    }
                }
                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Collection<ManaweavingPattern> getAllPatterns(Level world) {
        return world.getRecipeManager().<CraftingContainer, ManaweavingPattern>getRecipesFor(RecipeInit.MANAWEAVING_PATTERN_TYPE.get(), ContainerTools.createTemporaryContainer(ItemStack.EMPTY), world);
    }
}