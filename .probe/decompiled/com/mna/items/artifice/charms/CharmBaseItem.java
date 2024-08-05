package com.mna.items.artifice.charms;

import com.mna.api.items.TieredItem;
import com.mna.tools.InventoryUtilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class CharmBaseItem extends TieredItem {

    public CharmBaseItem(Item.Properties itemProperties) {
        super(itemProperties);
    }

    public CharmBaseItem() {
        super(new Item.Properties().setNoRepair().stacksTo(1).durability(1));
    }

    public boolean consume(ServerPlayer player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.getItem() != this) {
            return false;
        } else {
            stack.hurtAndBreak(1, player, e -> e.m_21190_(hand));
            return stack.isEmpty() || player.isCreative();
        }
    }

    public boolean consume(ServerPlayer player) {
        return InventoryUtilities.removeItemFromInventory(new ItemStack(this), true, true, new InvWrapper(player.m_150109_()));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}