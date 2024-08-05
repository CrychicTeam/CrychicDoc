package net.blay09.mods.craftingtweaks.network;

import net.blay09.mods.craftingtweaks.CraftingTweaksProviderManager;
import net.blay09.mods.craftingtweaks.api.GridTransferHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class TransferStackMessage {

    private final ResourceLocation id;

    private final int slotNumber;

    public TransferStackMessage(ResourceLocation id, int slotNumber) {
        this.id = id;
        this.slotNumber = slotNumber;
    }

    public static void encode(TransferStackMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.id);
        buf.writeInt(message.slotNumber);
    }

    public static TransferStackMessage decode(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        int slotNumber = buf.readInt();
        return new TransferStackMessage(id, slotNumber);
    }

    public static void handle(ServerPlayer player, TransferStackMessage message) {
        if (player != null) {
            AbstractContainerMenu menu = player.f_36096_;
            if (menu != null && message.slotNumber >= 0 && message.slotNumber < menu.slots.size()) {
                CraftingTweaksProviderManager.getCraftingGrid(menu, message.id).ifPresent(grid -> {
                    GridTransferHandler<AbstractContainerMenu> transferHandler = grid.transferHandler();
                    Slot slot = menu.slots.get(message.slotNumber);
                    if (transferHandler.canTransferFrom(player, menu, slot, grid) && !(slot instanceof ResultSlot)) {
                        ItemStack slotStack = slot.getItem();
                        if (!slotStack.isEmpty() && slot.mayPickup(player)) {
                            ItemStack oldStack = slotStack.copy();
                            if (!transferHandler.transferIntoGrid(grid, player, menu, slot)) {
                                return;
                            }
                            slot.onQuickCraft(slotStack, oldStack);
                            if (slotStack.getCount() <= 0) {
                                slot.set(ItemStack.EMPTY);
                            } else {
                                slot.setChanged();
                            }
                            if (slotStack.getCount() != oldStack.getCount()) {
                                slot.onTake(player, slotStack);
                            }
                        }
                    }
                });
            }
        }
    }
}