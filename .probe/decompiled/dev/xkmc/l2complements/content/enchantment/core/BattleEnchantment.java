package dev.xkmc.l2complements.content.enchantment.core;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;

public class BattleEnchantment extends UnobtainableEnchantment {

    protected BattleEnchantment(Enchantment.Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot[] pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Nullable
    protected LivingEntity getTarget(Entity target) {
        if (target instanceof LivingEntity) {
            return (LivingEntity) target;
        } else if (target instanceof PartEntity<?> part) {
            return part.getParent() == target ? null : this.getTarget(part.getParent());
        } else {
            return null;
        }
    }

    @Override
    public ChatFormatting getColor() {
        return ChatFormatting.GOLD;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}