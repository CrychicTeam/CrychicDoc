package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.AttributeEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;

public class HardShieldEnchantment extends UnobtainableEnchantment implements AttributeEnchantment {

    private static final String NAME_DEF_MAIN = "hardshield_mainhand";

    private static final String NAME_DEF_OFF = "hardshield_offhand";

    private static final UUID ID_DEF_MAIN = MathHelper.getUUIDFromString("hardshield_mainhand");

    private static final UUID ID_DEF_OFF = MathHelper.getUUIDFromString("hardshield_offhand");

    public HardShieldEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void addAttributes(int level, ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            event.addModifier((Attribute) LWItems.SHIELD_DEFENSE.get(), new AttributeModifier(ID_DEF_MAIN, "hardshield_mainhand", LWConfig.COMMON.hardShieldDefenseBonus.get() * (double) level, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        if (event.getSlotType() == EquipmentSlot.OFFHAND) {
            event.addModifier((Attribute) LWItems.SHIELD_DEFENSE.get(), new AttributeModifier(ID_DEF_OFF, "hardshield_offhand", LWConfig.COMMON.hardShieldDefenseBonus.get() * (double) level, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}