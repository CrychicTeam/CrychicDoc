package dev.xkmc.l2backpack.compat;

import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2library.compat.patchouli.PatchouliHelper;
import net.minecraft.world.item.Items;

public class PatchouliCompat {

    public static void gen() {
        new PatchouliHelper(L2Backpack.REGISTRATE, "backpack_guide").buildModel().buildShapelessRecipe(e -> e.m_126209_(Items.BOOK).requires(Items.IRON_NUGGET).requires(Items.PAPER), () -> Items.BOOK).buildBook("L2Backpack Guide", "Welcome to L2Backpack, a recursive storage mod", 1, BackpackItems.TAB.getKey());
    }
}