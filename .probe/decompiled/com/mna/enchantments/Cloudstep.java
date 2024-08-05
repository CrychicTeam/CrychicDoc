package com.mna.enchantments;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.enchantments.base.MAEnchantmentBase;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class Cloudstep extends MAEnchantmentBase {

    public Cloudstep(Enchantment.Rarity rarityIn) {
        super(rarityIn, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    @Override
    public boolean canEnchant(ItemStack pStack) {
        return this.f_44672_.canEnchant(pStack.getItem());
    }

    @Override
    protected boolean checkCompatibility(Enchantment ench) {
        return true;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public static void apply(Player source, int enchantmentLevel) {
        if (!source.m_20069_()) {
            source.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (m.getAirJumps() < enchantmentLevel + 1 && source.f_20954_ == 0) {
                    m.incrementAirJumps(source);
                    source.jumpFromGround();
                }
            });
        }
    }
}