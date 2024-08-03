package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.entities.constructs.animated.Construct;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class AnimatedConstructDropItemMessage extends BaseMessage {

    private int entityID;

    private int slot;

    private AnimatedConstructDropItemMessage() {
        this.messageIsValid = false;
    }

    public AnimatedConstructDropItemMessage(int entityID, int slot) {
        this();
        this.entityID = entityID;
        this.slot = slot;
        this.messageIsValid = true;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public int getSlot() {
        return this.slot;
    }

    public static AnimatedConstructDropItemMessage decode(FriendlyByteBuf buf) {
        AnimatedConstructDropItemMessage msg = new AnimatedConstructDropItemMessage();
        try {
            msg.entityID = buf.readInt();
            msg.slot = buf.readInt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading AnimatedConstructDropItemMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(AnimatedConstructDropItemMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getEntityID());
        buf.writeInt(msg.slot);
    }

    public static AnimatedConstructDropItemMessage fromConstruct(Construct construct, int slot) {
        return new AnimatedConstructDropItemMessage(construct.m_19879_(), slot);
    }
}