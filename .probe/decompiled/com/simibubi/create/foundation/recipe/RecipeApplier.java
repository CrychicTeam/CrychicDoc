package com.simibubi.create.foundation.recipe;

import com.simibubi.create.content.kinetics.deployer.ManualApplicationRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.item.ItemHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

public class RecipeApplier {

    public static void applyRecipeOn(ItemEntity entity, Recipe<?> recipe) {
        List<ItemStack> stacks = applyRecipeOn(entity.m_9236_(), entity.getItem(), recipe);
        if (stacks != null) {
            if (stacks.isEmpty()) {
                entity.m_146870_();
            } else {
                entity.setItem((ItemStack) stacks.remove(0));
                for (ItemStack additional : stacks) {
                    ItemEntity entityIn = new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), additional);
                    entityIn.m_20256_(entity.m_20184_());
                    entity.m_9236_().m_7967_(entityIn);
                }
            }
        }
    }

    public static List<ItemStack> applyRecipeOn(Level level, ItemStack stackIn, Recipe<?> recipe) {
        List<ItemStack> stacks;
        if (recipe instanceof ProcessingRecipe<?> pr) {
            stacks = new ArrayList();
            for (int i = 0; i < stackIn.getCount(); i++) {
                List<ProcessingOutput> outputs = pr instanceof ManualApplicationRecipe mar ? mar.getRollableResults() : pr.getRollableResults();
                for (ItemStack stack : pr.rollResults(outputs)) {
                    for (ItemStack previouslyRolled : stacks) {
                        if (!stack.isEmpty() && ItemHandlerHelper.canItemStacksStack(stack, previouslyRolled)) {
                            int amount = Math.min(previouslyRolled.getMaxStackSize() - previouslyRolled.getCount(), stack.getCount());
                            previouslyRolled.grow(amount);
                            stack.shrink(amount);
                        }
                    }
                    if (!stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
            }
        } else {
            ItemStack out = recipe.getResultItem(level.registryAccess()).copy();
            stacks = ItemHelper.multipliedOutput(stackIn, out);
        }
        return stacks;
    }
}