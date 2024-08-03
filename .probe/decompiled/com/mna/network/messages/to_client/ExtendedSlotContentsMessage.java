package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.ExtendedItemStackPacketBuffer;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ExtendedSlotContentsMessage extends BaseMessage {

    private int screenId = 0;

    private int slot = 0;

    private int stateID = 0;

    private ItemStack stack = ItemStack.EMPTY;

    public ExtendedSlotContentsMessage(int screenId, int stateId, int slot, ItemStack stack) {
        this.screenId = screenId;
        this.slot = slot;
        this.stack = stack.copy();
        this.stateID = stateId;
        this.messageIsValid = true;
    }

    public ExtendedSlotContentsMessage() {
        this.messageIsValid = false;
    }

    public int getScreenID() {
        return this.screenId;
    }

    public int getSlotIndex() {
        return this.slot;
    }

    public int getStateID() {
        return this.stateID;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public static ExtendedSlotContentsMessage decode(FriendlyByteBuf buf) {
        ExtendedSlotContentsMessage msg = new ExtendedSlotContentsMessage();
        ExtendedItemStackPacketBuffer bufferWrapper = new ExtendedItemStackPacketBuffer(buf);
        try {
            msg.screenId = bufferWrapper.readInt();
            msg.slot = bufferWrapper.readInt();
            msg.stateID = bufferWrapper.readInt();
            msg.stack = bufferWrapper.readExtendedItemStack();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading ExtendedSlotContentsMessage: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ExtendedSlotContentsMessage msg, FriendlyByteBuf buf) {
        ExtendedItemStackPacketBuffer bufferWrapper = new ExtendedItemStackPacketBuffer(buf);
        bufferWrapper.writeInt(msg.getScreenID());
        bufferWrapper.writeInt(msg.getSlotIndex());
        bufferWrapper.writeInt(msg.getStateID());
        bufferWrapper.writeExtendedItemStack(msg.getStack());
    }
}