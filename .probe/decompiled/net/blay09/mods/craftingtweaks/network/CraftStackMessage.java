package net.blay09.mods.craftingtweaks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CraftStackMessage {

    private final int slotNumber;

    public CraftStackMessage(int slotId) {
        this.slotNumber = slotId;
    }

    public static CraftStackMessage decode(FriendlyByteBuf buf) {
        int slotNumber = buf.readByte();
        return new CraftStackMessage(slotNumber);
    }

    public static void encode(CraftStackMessage message, FriendlyByteBuf buf) {
        buf.writeByte(message.slotNumber);
    }

    public static void handle(ServerPlayer player, CraftStackMessage message) {
        if (player != null) {
            AbstractContainerMenu menu = player.f_36096_;
            if (menu != null && message.slotNumber >= 0 && message.slotNumber < menu.slots.size()) {
                Slot mouseSlot = menu.slots.get(message.slotNumber);
                ItemStack mouseStack = menu.getCarried();
                for (int maxTries = 64; maxTries > 0 && mouseSlot.hasItem() && (mouseStack.isEmpty() || mouseStack.getCount() + mouseSlot.getItem().getCount() <= mouseStack.getMaxStackSize()); maxTries--) {
                    menu.clicked(mouseSlot.index, 0, ClickType.PICKUP, player);
                    mouseStack = menu.getCarried();
                }
                player.connection.send(new ClientboundContainerSetSlotPacket(-1, -1, -1, menu.getCarried()));
            }
        }
    }
}