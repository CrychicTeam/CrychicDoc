package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.AttributeEnchantment;
import dev.xkmc.l2complements.content.enchantment.core.UnobtainableEnchantment;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.BaseThrowableWeaponItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;

public class HeavyEnchantment extends UnobtainableEnchantment implements AttributeEnchantment {

    private static final String NAME_CRIT = "heavy_enchantment_crit";

    private static final String NAME_SPEED = "heavy_enchantment_speed";

    private static final UUID ID_CRIT = MathHelper.getUUIDFromString("heavy_enchantment_crit");

    private static final UUID ID_SPEED = MathHelper.getUUIDFromString("heavy_enchantment_speed");

    public HeavyEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void addAttributes(int level, ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ID_SPEED, "heavy_enchantment_speed", -LWConfig.COMMON.heavySpeedReduction.get() * (double) level, AttributeModifier.Operation.ADDITION));
            event.addModifier((Attribute) L2DamageTracker.CRIT_DMG.get(), new AttributeModifier(ID_CRIT, "heavy_enchantment_crit", LWConfig.COMMON.heavyCritBonus.get() * (double) level, AttributeModifier.Operation.ADDITION));
            if (event.getItemStack().getItem() instanceof BaseThrowableWeaponItem) {
                event.addModifier((Attribute) L2DamageTracker.BOW_STRENGTH.get(), new AttributeModifier(ID_CRIT, "heavy_enchantment_crit", LWConfig.COMMON.heavyCritBonus.get() * (double) level, AttributeModifier.Operation.ADDITION));
            }
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