package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.entities.constructs.animated.Construct;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class AnimatedConstructSyncRequestMessage extends BaseMessage {

    private int entityID;

    private boolean diagnosticsOnly;

    private AnimatedConstructSyncRequestMessage() {
        this.messageIsValid = false;
    }

    public AnimatedConstructSyncRequestMessage(int entityID, boolean diagnosticsOnly) {
        this();
        this.entityID = entityID;
        this.diagnosticsOnly = diagnosticsOnly;
        this.messageIsValid = true;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public boolean getDiagnosticsOnly() {
        return this.diagnosticsOnly;
    }

    public static AnimatedConstructSyncRequestMessage decode(FriendlyByteBuf buf) {
        AnimatedConstructSyncRequestMessage msg = new AnimatedConstructSyncRequestMessage();
        try {
            msg.entityID = buf.readInt();
            msg.diagnosticsOnly = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading AnimatedConstructSyncMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(AnimatedConstructSyncRequestMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeBoolean(msg.diagnosticsOnly);
    }

    public static AnimatedConstructSyncRequestMessage fromConstruct(Construct construct, boolean diagnosticsOnly) {
        return new AnimatedConstructSyncRequestMessage(construct.m_19879_(), diagnosticsOnly);
    }
}