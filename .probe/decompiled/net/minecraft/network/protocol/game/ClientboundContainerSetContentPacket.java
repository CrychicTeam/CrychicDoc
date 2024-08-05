package net.minecraft.network.protocol.game;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.ItemStack;

public class ClientboundContainerSetContentPacket implements Packet<ClientGamePacketListener> {

    private final int containerId;

    private final int stateId;

    private final List<ItemStack> items;

    private final ItemStack carriedItem;

    public ClientboundContainerSetContentPacket(int int0, int int1, NonNullList<ItemStack> nonNullListItemStack2, ItemStack itemStack3) {
        this.containerId = int0;
        this.stateId = int1;
        this.items = NonNullList.<ItemStack>withSize(nonNullListItemStack2.size(), ItemStack.EMPTY);
        for (int $$4 = 0; $$4 < nonNullListItemStack2.size(); $$4++) {
            this.items.set($$4, nonNullListItemStack2.get($$4).copy());
        }
        this.carriedItem = itemStack3.copy();
    }

    public ClientboundContainerSetContentPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readUnsignedByte();
        this.stateId = friendlyByteBuf0.readVarInt();
        this.items = friendlyByteBuf0.readCollection(NonNullList::m_182647_, FriendlyByteBuf::m_130267_);
        this.carriedItem = friendlyByteBuf0.readItem();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeVarInt(this.stateId);
        friendlyByteBuf0.writeCollection(this.items, FriendlyByteBuf::m_130055_);
        friendlyByteBuf0.writeItem(this.carriedItem);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleContainerContent(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public ItemStack getCarriedItem() {
        return this.carriedItem;
    }

    public int getStateId() {
        return this.stateId;
    }
}