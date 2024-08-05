package dev.xkmc.l2weaponry.content.enchantments;

import dev.xkmc.l2complements.content.enchantment.core.AttributeEnchantment;
import dev.xkmc.l2library.util.math.MathHelper;
import dev.xkmc.l2weaponry.content.item.base.BaseClawItem;
import dev.xkmc.l2weaponry.content.item.types.MacheteItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.event.ItemAttributeModifierEvent;

public class RaisedSpiritEnchantment extends BaseMacheteEnchantment implements AttributeEnchantment {

    private static final String NAME_SPEED = "raised_spirit_speed";

    private static final UUID ID_SPEED = MathHelper.getUUIDFromString("raised_spirit_speed");

    public RaisedSpiritEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public void addAttributes(int level, ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND && event.getItemStack().getItem() instanceof MacheteItem) {
            double bonus = LWConfig.COMMON.raisedSpiritSpeedBonus.get() * (double) BaseClawItem.getHitCount(event.getItemStack()) * (double) level;
            event.addModifier(Attributes.ATTACK_SPEED, new AttributeModifier(ID_SPEED, "raised_spirit_speed", bonus, AttributeModifier.Operation.ADDITION));
        }
    }
}