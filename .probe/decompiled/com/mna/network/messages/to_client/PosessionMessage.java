package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class PosessionMessage extends BaseMessage {

    int entityID;

    public PosessionMessage(int entityID) {
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public PosessionMessage() {
        this.messageIsValid = false;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static PosessionMessage decode(FriendlyByteBuf buf) {
        PosessionMessage msg = new PosessionMessage();
        try {
            msg.entityID = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading getEntityID: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PosessionMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
    }
}