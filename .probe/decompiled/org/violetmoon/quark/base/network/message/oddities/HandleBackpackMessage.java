package org.violetmoon.quark.base.network.message.oddities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class HandleBackpackMessage implements IZetaMessage {

    private static final long serialVersionUID = 3474816381329541425L;

    public boolean open;

    public HandleBackpackMessage() {
    }

    public HandleBackpackMessage(boolean open) {
        this.open = open;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            if (this.open) {
                ItemStack stack = player.m_6844_(EquipmentSlot.CHEST);
                if (stack.getItem() instanceof MenuProvider && player.f_36096_ != null) {
                    ItemStack holding = player.f_36096_.getCarried().copy();
                    player.f_36096_.setCarried(ItemStack.EMPTY);
                    NetworkHooks.openScreen(player, (MenuProvider) stack.getItem(), player.m_20183_());
                    player.f_36096_.setCarried(holding);
                }
            } else if (player.f_36096_ != null) {
                ItemStack holding = player.f_36096_.getCarried();
                player.f_36096_.setCarried(ItemStack.EMPTY);
                BackpackMenu.saveCraftingInventory(player);
                player.f_36096_ = player.f_36095_;
                player.f_36095_.m_142503_(holding);
            }
        });
        return true;
    }
}