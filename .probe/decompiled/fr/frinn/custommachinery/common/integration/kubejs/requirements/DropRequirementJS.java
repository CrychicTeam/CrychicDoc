package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.integration.kubejs.KubeJSIntegration;
import fr.frinn.custommachinery.common.requirement.DropRequirement;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface DropRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder checkDrop(ItemStack item, int amount, int radius) {
        return this.checkDrops(new ItemStack[] { item }, amount, radius, true);
    }

    default RecipeJSBuilder checkAnyDrop(int amount, int radius) {
        return this.checkDrops(new ItemStack[0], amount, radius, false);
    }

    default RecipeJSBuilder checkDrops(ItemStack[] items, int amount, int radius) {
        return this.checkDrops(items, amount, radius, true);
    }

    default RecipeJSBuilder checkDrops(ItemStack[] items, int amount, int radius, boolean whitelist) {
        if (items.length == 0) {
            return this.error("Invalid Drop requirement, checkDrop method must have at least 1 item defined when using whitelist mode", new Object[0]);
        } else {
            List<IIngredient<Item>> input = (List<IIngredient<Item>>) Arrays.stream(items).map(ItemStack::m_41720_).map(ItemIngredient::new).collect(Collectors.toList());
            return this.addRequirement(new DropRequirement(RequirementIOMode.INPUT, DropRequirement.Action.CHECK, input, whitelist, Items.AIR, KubeJSIntegration.nbtFromStack(items[0]), amount, radius));
        }
    }

    default RecipeJSBuilder consumeDropOnStart(ItemStack item, int amount, int radius) {
        return this.consumeDropsOnStart(new ItemStack[] { item }, amount, radius, true);
    }

    default RecipeJSBuilder consumeAnyDropOnStart(int amount, int radius) {
        return this.consumeDropsOnStart(new ItemStack[0], amount, radius, false);
    }

    default RecipeJSBuilder consumeDropsOnStart(ItemStack[] items, int amount, int radius) {
        return this.consumeDropsOnStart(items, amount, radius, true);
    }

    default RecipeJSBuilder consumeDropsOnStart(ItemStack[] items, int amount, int radius, boolean whitelist) {
        if (items.length == 0) {
            return this.error("Invalid Drop requirement, consumeDropOnStart method must have at least 1 item defined when using whitelist mode", new Object[0]);
        } else {
            List<IIngredient<Item>> input = (List<IIngredient<Item>>) Arrays.stream(items).map(ItemStack::m_41720_).map(ItemIngredient::new).collect(Collectors.toList());
            return this.addRequirement(new DropRequirement(RequirementIOMode.INPUT, DropRequirement.Action.CONSUME, input, whitelist, Items.AIR, KubeJSIntegration.nbtFromStack(items[0]), amount, radius));
        }
    }

    default RecipeJSBuilder consumeDropOnEnd(ItemStack item, int amount, int radius) {
        return this.consumeDropsOnEnd(new ItemStack[] { item }, amount, radius, true);
    }

    default RecipeJSBuilder consumeAnyDropOnEnd(int amount, int radius) {
        return this.consumeDropsOnEnd(new ItemStack[0], amount, radius, false);
    }

    default RecipeJSBuilder consumeDropsOnEnd(ItemStack[] items, int amount, int radius) {
        return this.consumeDropsOnEnd(items, amount, radius, true);
    }

    default RecipeJSBuilder consumeDropsOnEnd(ItemStack[] items, int amount, int radius, boolean whitelist) {
        if (items.length == 0) {
            return this.error("Invalid Drop requirement, consumeDropOnEnd method must have at least 1 item defined when using whitelist mode", new Object[0]);
        } else {
            List<IIngredient<Item>> input = (List<IIngredient<Item>>) Arrays.stream(items).map(ItemStack::m_41720_).map(ItemIngredient::new).collect(Collectors.toList());
            return this.addRequirement(new DropRequirement(RequirementIOMode.OUTPUT, DropRequirement.Action.CONSUME, input, whitelist, Items.AIR, KubeJSIntegration.nbtFromStack(items[0]), amount, radius));
        }
    }

    default RecipeJSBuilder dropItemOnStart(ItemStack stack) {
        return this.addRequirement(new DropRequirement(RequirementIOMode.INPUT, DropRequirement.Action.PRODUCE, Collections.emptyList(), true, stack.getItem(), KubeJSIntegration.nbtFromStack(stack), stack.getCount(), 1));
    }

    default RecipeJSBuilder dropItemOnEnd(ItemStack stack) {
        return this.addRequirement(new DropRequirement(RequirementIOMode.OUTPUT, DropRequirement.Action.PRODUCE, Collections.emptyList(), true, stack.getItem(), KubeJSIntegration.nbtFromStack(stack), stack.getCount(), 1));
    }
}