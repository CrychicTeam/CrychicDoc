package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class ConstructHornMessage extends BaseMessage {

    private int entityID;

    public ConstructHornMessage() {
        this.messageIsValid = false;
    }

    public ConstructHornMessage(int entityID) {
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public int getEntityId() {
        return this.entityID;
    }

    public static ConstructHornMessage decode(FriendlyByteBuf buf) {
        ConstructHornMessage msg = new ConstructHornMessage();
        try {
            msg.entityID = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ConstructHornMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ConstructHornMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityId());
    }
}