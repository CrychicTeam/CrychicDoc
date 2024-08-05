package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.AttributeEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;

public class HeavyShieldEnchantment extends UnobtainableEnchantment implements AttributeEnchantment {

    private static final String NAME_DEF = "heavyshield_defense";

    private static final String NAME_SPEED = "heavyshield_speed";

    private static final UUID ID_DEF = MathHelper.getUUIDFromString("heavyshield_defense");

    private static final UUID ID_SPEED = MathHelper.getUUIDFromString("heavyshield_speed");

    public HeavyShieldEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void addAttributes(int level, ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            event.addModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(ID_SPEED, "heavyshield_speed", -LWConfig.COMMON.heavyShieldSpeedReduction.get() * (double) level, AttributeModifier.Operation.MULTIPLY_BASE));
            event.addModifier((Attribute) LWItems.SHIELD_DEFENSE.get(), new AttributeModifier(ID_DEF, "heavyshield_defense", LWConfig.COMMON.heavyShieldDefenseBonus.get() * (double) level, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public ChatFormatting getColor() {
        return ChatFormatting.LIGHT_PURPLE;
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