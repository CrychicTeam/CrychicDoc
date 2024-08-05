package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class MAPFXMessage extends BaseMessage {

    private long flags;

    private int entityID;

    public MAPFXMessage(int entityID, long flags) {
        this.flags = flags;
        this.entityID = entityID;
        this.messageIsValid = true;
    }

    public MAPFXMessage() {
        this.messageIsValid = false;
    }

    public long getFlags() {
        return this.flags;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public static MAPFXMessage decode(FriendlyByteBuf buf) {
        MAPFXMessage msg = new MAPFXMessage();
        try {
            msg.flags = buf.readLong();
            msg.entityID = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading MAPFXMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MAPFXMessage msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.getFlags());
        buf.writeInt(msg.getEntityID());
    }
}