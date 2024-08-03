package com.mna.items.artifice;

import com.mna.api.items.ChargeableItem;
import com.mna.items.artifice.curio.IPreEnchantedItem;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemWitherguardAmulet extends ChargeableItem implements IPreEnchantedItem<ItemWitherguardAmulet> {

    public ItemWitherguardAmulet(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        if (player.m_21124_(MobEffects.WITHER) != null) {
            player.m_6234_(MobEffects.WITHER);
            return true;
        } else {
            return false;
        }
    }
}