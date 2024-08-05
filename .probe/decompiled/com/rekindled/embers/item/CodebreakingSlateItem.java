package com.rekindled.embers.item;

import com.rekindled.embers.gui.SlateMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CodebreakingSlateItem extends Item implements MenuProvider {

    public CodebreakingSlateItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, this, buf -> buf.writeItem(player.m_21120_(hand)));
        }
        return InteractionResultHolder.success(player.m_21120_(hand));
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSameItem(oldStack, newStack);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        ItemStack heldItem = player.m_21205_();
        return new SlateMenu(id, inv, heldItem);
    }

    @Override
    public Component getDisplayName() {
        return this.m_41466_();
    }
}