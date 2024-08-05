package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.ItemStack;

public class ServerboundSetCreativeModeSlotPacket implements Packet<ServerGamePacketListener> {

    private final int slotNum;

    private final ItemStack itemStack;

    public ServerboundSetCreativeModeSlotPacket(int int0, ItemStack itemStack1) {
        this.slotNum = int0;
        this.itemStack = itemStack1.copy();
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetCreativeModeSlot(this);
    }

    public ServerboundSetCreativeModeSlotPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slotNum = friendlyByteBuf0.readShort();
        this.itemStack = friendlyByteBuf0.readItem();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeShort(this.slotNum);
        friendlyByteBuf0.writeItem(this.itemStack);
    }

    public int getSlotNum() {
        return this.slotNum;
    }

    public ItemStack getItem() {
        return this.itemStack;
    }
}