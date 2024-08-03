package dev.xkmc.modulargolems.compat.materials.create.automation;

import java.util.function.Predicate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class DummyFurnace extends ProjectileWeaponItem {

    public DummyFurnace() {
        super(new Item.Properties());
    }

    private static boolean isValid(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return DummyFurnace::isValid;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 0;
    }
}