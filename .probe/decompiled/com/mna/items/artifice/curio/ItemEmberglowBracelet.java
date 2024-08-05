package com.mna.items.artifice.curio;

import com.mna.api.faction.IFaction;
import com.mna.api.items.ChargeableItem;
import com.mna.factions.Factions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemEmberglowBracelet extends ChargeableItem implements IPreEnchantedItem<ItemEmberglowBracelet> {

    public ItemEmberglowBracelet(Item.Properties properties, float maxMana) {
        super(properties, maxMana);
    }

    @Override
    protected boolean tickEffect(ItemStack stack, Player player, Level world, int slot, float mana, boolean selected) {
        return false;
    }

    @Override
    protected boolean tickCurio() {
        return false;
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }
}