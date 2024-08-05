package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.ItemStaff;
import com.mna.network.ClientMessageDispatcher;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ContainerSpellName extends AbstractContainerMenu {

    public ItemStack heldItem;

    public InteractionHand hand;

    public ContainerSpellName(@Nullable MenuType<?> type, int id, Inventory playerInv, InteractionHand hand) {
        super(type, id);
        this.heldItem = playerInv.player.m_21120_(hand);
        this.hand = hand;
    }

    public ContainerSpellName(int i, Inventory playerInv, FriendlyByteBuf buffer) {
        this(ContainerInit.SPELL_CUSTOMIZATION.get(), i, playerInv, playerInv.player.m_21205_().getItem() != ItemInit.SPELL.get() && !(playerInv.player.m_21205_().getItem() instanceof ItemBookOfRote) && !(playerInv.player.m_21205_().getItem() instanceof ItemStaff) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public String getName() {
        return this.heldItem.getHoverName().getString();
    }

    public int getIconIndex() {
        return ItemSpell.getCustomIcon(this.heldItem);
    }

    public void setName(String name) {
        this.heldItem.setHoverName(Component.literal(name));
    }

    public void setIconIndex(int index) {
        ItemSpell.setCustomIcon(this.heldItem, index);
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (playerIn.m_9236_().isClientSide()) {
            ClientMessageDispatcher.sendSpellCustomizationValues(this.getName(), this.getIconIndex(), this.hand);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}