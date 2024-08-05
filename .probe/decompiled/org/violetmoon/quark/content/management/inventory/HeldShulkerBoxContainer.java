package org.violetmoon.quark.content.management.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.violetmoon.quark.base.handler.SimilarBlockTypeHandler;
import org.violetmoon.quark.content.management.module.ExpandedItemInteractionsModule;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class HeldShulkerBoxContainer implements Container, MenuProvider {

    public final Player player;

    public final ItemStack stack;

    public final ShulkerBoxBlockEntity be;

    public final int slot;

    public HeldShulkerBoxContainer(Player player, int slot) {
        this.player = player;
        this.slot = slot;
        this.stack = player.getInventory().getItem(slot);
        ShulkerBoxBlockEntity gotBe = null;
        if (SimilarBlockTypeHandler.isShulkerBox(this.stack) && ExpandedItemInteractionsModule.getShulkerBoxEntity(this.stack) instanceof ShulkerBoxBlockEntity shulker) {
            gotBe = shulker;
        }
        this.be = gotBe;
    }

    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        return new HeldShulkerBoxMenu(int0, inventory1, this, this.slot);
    }

    @Override
    public Component getDisplayName() {
        return this.be.m_5446_();
    }

    @Override
    public void clearContent() {
        this.be.m_6211_();
    }

    @Override
    public int getContainerSize() {
        return this.be.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.be.m_7983_();
    }

    @Override
    public ItemStack getItem(int int0) {
        return this.be.m_8020_(int0);
    }

    @Override
    public ItemStack removeItem(int int0, int int1) {
        return this.be.m_7407_(int0, int1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int int0) {
        return this.be.m_8016_(int0);
    }

    @Override
    public void setItem(int int0, ItemStack itemStack1) {
        this.be.m_6836_(int0, itemStack1);
    }

    @Override
    public void setChanged() {
        this.be.m_6596_();
        ItemNBTHelper.setCompound(this.stack, "BlockEntityTag", this.be.m_187481_());
    }

    @Override
    public boolean stillValid(Player player) {
        return this.stack != null && player == this.player && player.getInventory().getItem(this.slot) == this.stack;
    }
}