package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.ExtendedItemStackPacketBuffer;
import com.mna.network.messages.BaseMessage;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ExtendedSlotContainerContentsMessage extends BaseMessage {

    private int containerId;

    private int stateId;

    private List<ItemStack> items;

    private ItemStack carriedItem;

    public ExtendedSlotContainerContentsMessage(int containerId, int stateId, NonNullList<ItemStack> items, ItemStack carriedItem) {
        this.containerId = containerId;
        this.stateId = stateId;
        this.carriedItem = carriedItem.copy();
        this.items = NonNullList.<ItemStack>withSize(items.size(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++) {
            this.items.set(i, items.get(i).copy());
        }
        this.messageIsValid = true;
    }

    public ExtendedSlotContainerContentsMessage() {
        this.messageIsValid = false;
    }

    public int getContainerID() {
        return this.containerId;
    }

    public int getStateID() {
        return this.stateId;
    }

    public ItemStack getCarried() {
        return this.carriedItem;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public static ExtendedSlotContainerContentsMessage decode(FriendlyByteBuf buf) {
        ExtendedSlotContainerContentsMessage msg = new ExtendedSlotContainerContentsMessage();
        ExtendedItemStackPacketBuffer bufferWrapper = new ExtendedItemStackPacketBuffer(buf);
        try {
            msg.containerId = bufferWrapper.readUnsignedByte();
            msg.stateId = bufferWrapper.m_130242_();
            msg.items = bufferWrapper.readExtendedCollection(NonNullList::m_182647_, ExtendedItemStackPacketBuffer::readExtendedItemStack);
            msg.carriedItem = bufferWrapper.readExtendedItemStack();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading ExtendedSlotContentsMessage: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ExtendedSlotContainerContentsMessage msg, FriendlyByteBuf buf) {
        ExtendedItemStackPacketBuffer bufferWrapper = new ExtendedItemStackPacketBuffer(buf);
        bufferWrapper.writeByte(msg.containerId);
        bufferWrapper.m_130130_(msg.stateId);
        bufferWrapper.writeExtendedCollection(msg.items, ExtendedItemStackPacketBuffer::writeExtendedItemStack);
        bufferWrapper.writeExtendedItemStack(msg.carriedItem);
    }
}