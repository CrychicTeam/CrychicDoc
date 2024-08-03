package com.mna.items.artifice.curio;

import com.mna.api.items.ChargeableItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCritImmunityAmulet extends ChargeableItem {

    public ItemCritImmunityAmulet(float maxMana) {
        super(new Item.Properties().stacksTo(1).setNoRepair(), maxMana);
    }

    @Override
    protected float manaPerRechargeTick() {
        return 10.0F;
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return this.getMana(pStack) == this.getMaxMana();
    }
}