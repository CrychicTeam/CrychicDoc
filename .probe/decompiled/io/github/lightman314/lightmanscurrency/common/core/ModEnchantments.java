package io.github.lightman314.lightmanscurrency.common.core;

import io.github.lightman314.lightmanscurrency.common.enchantments.CoinMagnetEnchantment;
import io.github.lightman314.lightmanscurrency.common.enchantments.MoneyMendingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    public static final RegistryObject<MoneyMendingEnchantment> MONEY_MENDING = ModRegistries.ENCHANTMENTS.register("money_mending", () -> new MoneyMendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.values()));

    public static final RegistryObject<CoinMagnetEnchantment> COIN_MAGNET = ModRegistries.ENCHANTMENTS.register("coin_magnet", () -> new CoinMagnetEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.values()));

    public static void init() {
    }
}