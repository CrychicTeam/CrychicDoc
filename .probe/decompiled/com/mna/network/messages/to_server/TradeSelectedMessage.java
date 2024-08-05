package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class TradeSelectedMessage extends BaseMessage {

    private int index;

    public TradeSelectedMessage() {
        this.messageIsValid = false;
    }

    public TradeSelectedMessage(int index) {
        this.index = index;
        this.messageIsValid = true;
    }

    public static final TradeSelectedMessage decode(FriendlyByteBuf buf) {
        TradeSelectedMessage msg = new TradeSelectedMessage();
        try {
            msg.index = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading TradeSelectedMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(TradeSelectedMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getIndex());
    }

    public int getIndex() {
        return this.index;
    }
}