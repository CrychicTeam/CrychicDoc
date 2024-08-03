package dev.xkmc.l2complements.content.enchantment.armors;

import dev.xkmc.l2complements.content.enchantment.core.AttributeEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.SingleLevelEnchantment;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;

public class StableBodyEnchantment extends SingleLevelEnchantment implements AttributeEnchantment {

    public static final UUID ID = MathHelper.getUUIDFromString("stable_body");

    public StableBodyEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void addAttributes(int lv, ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.CHEST) {
            event.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ID, "stable_body", 1.0, AttributeModifier.Operation.ADDITION));
        }
    }
}