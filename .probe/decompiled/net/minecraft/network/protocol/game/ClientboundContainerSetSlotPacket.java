package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.ItemStack;

public class ClientboundContainerSetSlotPacket implements Packet<ClientGamePacketListener> {

    public static final int CARRIED_ITEM = -1;

    public static final int PLAYER_INVENTORY = -2;

    private final int containerId;

    private final int stateId;

    private final int slot;

    private final ItemStack itemStack;

    public ClientboundContainerSetSlotPacket(int int0, int int1, int int2, ItemStack itemStack3) {
        this.containerId = int0;
        this.stateId = int1;
        this.slot = int2;
        this.itemStack = itemStack3.copy();
    }

    public ClientboundContainerSetSlotPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readByte();
        this.stateId = friendlyByteBuf0.readVarInt();
        this.slot = friendlyByteBuf0.readShort();
        this.itemStack = friendlyByteBuf0.readItem();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeVarInt(this.stateId);
        friendlyByteBuf0.writeShort(this.slot);
        friendlyByteBuf0.writeItem(this.itemStack);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleContainerSetSlot(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getItem() {
        return this.itemStack;
    }

    public int getStateId() {
        return this.stateId;
    }
}