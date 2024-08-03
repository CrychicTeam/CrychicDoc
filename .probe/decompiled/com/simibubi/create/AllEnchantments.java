package com.simibubi.create;

import com.simibubi.create.content.equipment.armor.CapacityEnchantment;
import com.simibubi.create.content.equipment.potatoCannon.PotatoRecoveryEnchantment;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class AllEnchantments {

    public static final RegistryEntry<PotatoRecoveryEnchantment> POTATO_RECOVERY = ((CreateRegistrate) Create.REGISTRATE.object("potato_recovery")).enchantment(EnchantmentCategory.BOW, PotatoRecoveryEnchantment::new).addSlots(new EquipmentSlot[] { EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND }).lang("Potato Recovery").rarity(Enchantment.Rarity.UNCOMMON).register();

    public static final RegistryEntry<CapacityEnchantment> CAPACITY = ((CreateRegistrate) Create.REGISTRATE.object("capacity")).enchantment(EnchantmentCategory.ARMOR_CHEST, CapacityEnchantment::new).addSlots(new EquipmentSlot[] { EquipmentSlot.CHEST }).lang("Capacity").rarity(Enchantment.Rarity.COMMON).register();

    public static void register() {
    }
}