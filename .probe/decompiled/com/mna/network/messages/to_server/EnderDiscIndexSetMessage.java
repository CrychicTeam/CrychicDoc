package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class EnderDiscIndexSetMessage extends BaseMessage {

    private int patternIndex;

    private boolean offhand;

    public EnderDiscIndexSetMessage(int patternIndex, boolean offhand) {
        this.patternIndex = patternIndex;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public EnderDiscIndexSetMessage() {
        this.messageIsValid = false;
    }

    public int getIndex() {
        return this.patternIndex;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static EnderDiscIndexSetMessage decode(FriendlyByteBuf buf) {
        EnderDiscIndexSetMessage msg = new EnderDiscIndexSetMessage();
        try {
            msg.patternIndex = buf.readInt();
            msg.offhand = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading EnderDiscIndexSetMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(EnderDiscIndexSetMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getIndex());
        buf.writeBoolean(msg.isOffhand());
    }
}