package com.mna.enchantments.framework;

import com.mna.items.sorcery.ItemStaff;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class EnchantmentEnumExtender {

    private static EnchantmentCategory STAFF_OR_WEAPON = null;

    private static EnchantmentCategory STAVES = null;

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        STAFF_OR_WEAPON = EnchantmentCategory.create("StaffOrWeapon", i -> i instanceof ItemStaff || EnchantmentCategory.WEAPON.canEnchant(i));
        STAVES = EnchantmentCategory.create("Staves", i -> i instanceof ItemStaff);
    }

    public static EnchantmentCategory StaffOrWeapon() {
        return STAFF_OR_WEAPON;
    }

    public static EnchantmentCategory Staves() {
        return STAVES;
    }
}