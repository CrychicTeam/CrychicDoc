package io.redspace.ironsspellbooks.compat.apotheosis;

import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class ApotheosisHandler {

    public static LootCategory SPELLBOOK;

    private static EquipmentSlot[] arr(EquipmentSlot... s) {
        return s;
    }

    public static boolean isSpellbook(ItemStack stack) {
        return LootCategory.forItem(stack).equals(SPELLBOOK);
    }

    public static void init() {
        if (!ModList.get().isLoaded("apothiccurios")) {
            SPELLBOOK = LootCategory.register(LootCategory.SWORD, "curios:spellbook", s -> false, arr());
        }
    }
}