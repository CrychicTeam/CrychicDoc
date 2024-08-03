package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class MindVisionMessage extends BaseMessage {

    int entityID;

    public MindVisionMessage(int entityID) {
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public MindVisionMessage() {
        this.messageIsValid = false;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static MindVisionMessage decode(FriendlyByteBuf buf) {
        MindVisionMessage msg = new MindVisionMessage();
        try {
            msg.entityID = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading getEntityID: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MindVisionMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
    }
}