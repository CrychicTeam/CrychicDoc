package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class CloudstepJumpMessage extends BaseMessage {

    private int entityID;

    public CloudstepJumpMessage(int entityID) {
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public CloudstepJumpMessage() {
        this.messageIsValid = false;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static CloudstepJumpMessage decode(FriendlyByteBuf buf) {
        CloudstepJumpMessage msg = new CloudstepJumpMessage();
        try {
            msg.entityID = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading CloudstepJumpMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(CloudstepJumpMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
    }
}