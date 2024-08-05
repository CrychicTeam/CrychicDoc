package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class RadialInventorySlotChangeMessage extends BaseMessage {

    private int slot;

    private boolean offhand;

    public RadialInventorySlotChangeMessage(int slot, boolean offhand) {
        this.slot = slot;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public RadialInventorySlotChangeMessage() {
        this.messageIsValid = false;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static RadialInventorySlotChangeMessage decode(FriendlyByteBuf buf) {
        RadialInventorySlotChangeMessage msg = new RadialInventorySlotChangeMessage();
        try {
            msg.slot = buf.readInt();
            msg.offhand = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading RadialInventorySlotChangeMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(RadialInventorySlotChangeMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getSlot());
        buf.writeBoolean(msg.isOffhand());
    }
}