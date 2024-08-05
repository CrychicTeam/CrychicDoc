package dev.xkmc.l2backpack.content.recipe;

import dev.xkmc.l2backpack.content.backpack.BackpackItem;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2backpack.init.data.BackpackConfig;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class BackpackUpgradeRecipe extends AbstractSmithingRecipe<BackpackUpgradeRecipe> {

    public BackpackUpgradeRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
        super(rl, left, right, BackpackItem.setRow(result, BackpackConfig.COMMON.initialRows.get() + 1));
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (!super.m_5818_(container, level)) {
            return false;
        } else {
            ItemStack stack = container.getItem(1);
            BaseBagItem bag = (BaseBagItem) stack.getItem();
            return bag.getRows(stack) < 8;
        }
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        ItemStack stack = super.m_5874_(container, access);
        BaseBagItem bag = (BaseBagItem) stack.getItem();
        BackpackItem.setRow(stack, bag.getRows(stack) + 1);
        return stack;
    }

    @Override
    public AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe> getSerializer() {
        return (AbstractSmithingRecipe.Serializer<BackpackUpgradeRecipe>) BackpackMisc.RSC_BAG_UPGRADE.get();
    }
}