package dev.latvian.mods.kubejs.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerMainInvWrapper extends RangedWrapper {

    private final Inventory inventoryPlayer;

    public PlayerMainInvWrapper(Inventory inv) {
        super(inv, 0, inv.items.size());
        this.inventoryPlayer = inv;
    }

    @NotNull
    @Override
    public ItemStack kjs$insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack rest = super.kjs$insertItem(slot, stack, simulate);
        if (rest.getCount() != stack.getCount()) {
            ItemStack inSlot = this.kjs$getStackInSlot(slot);
            if (!inSlot.isEmpty()) {
                if (this.getInventoryPlayer().player.m_9236_().isClientSide) {
                    inSlot.setPopTime(5);
                } else if (this.getInventoryPlayer().player instanceof ServerPlayer) {
                    this.getInventoryPlayer().player.containerMenu.broadcastChanges();
                }
            }
        }
        return rest;
    }

    public Inventory getInventoryPlayer() {
        return this.inventoryPlayer;
    }
}