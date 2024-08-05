package dev.xkmc.l2backpack.content.recipe;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.init.registrate.BackpackMisc;
import dev.xkmc.l2library.serial.recipe.AbstractSmithingRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class DrawerUpgradeRecipe extends AbstractSmithingRecipe<DrawerUpgradeRecipe> {

    public DrawerUpgradeRecipe(ResourceLocation rl, Ingredient left, Ingredient right, ItemStack result) {
        super(rl, left, right, BaseDrawerItem.setStackingFactor(result, 2));
    }

    @Override
    public boolean matches(Container container, Level level) {
        if (!super.m_5818_(container, level)) {
            return false;
        } else {
            ItemStack stack = container.getItem(1);
            return BaseDrawerItem.getStackingFactor(stack) < 8;
        }
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess access) {
        ItemStack stack = super.m_5874_(container, access);
        BaseDrawerItem.setStackingFactor(stack, BaseDrawerItem.getStackingFactor(stack) + 1);
        return stack;
    }

    @Override
    public AbstractSmithingRecipe.Serializer<DrawerUpgradeRecipe> getSerializer() {
        return (AbstractSmithingRecipe.Serializer<DrawerUpgradeRecipe>) BackpackMisc.RSC_DRAWER_UPGRADE.get();
    }
}