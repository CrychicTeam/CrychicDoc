package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class RitualKitIndexSetMessage extends BaseMessage {

    private int patternIndex;

    private boolean offhand;

    public RitualKitIndexSetMessage(int patternIndex, boolean offhand) {
        this.patternIndex = patternIndex;
        this.offhand = offhand;
        this.messageIsValid = true;
    }

    public RitualKitIndexSetMessage() {
        this.messageIsValid = false;
    }

    public int getIndex() {
        return this.patternIndex;
    }

    public boolean isOffhand() {
        return this.offhand;
    }

    public static RitualKitIndexSetMessage decode(FriendlyByteBuf buf) {
        RitualKitIndexSetMessage msg = new RitualKitIndexSetMessage();
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

    public static void encode(RitualKitIndexSetMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.getIndex());
        buf.writeBoolean(msg.offhand);
    }
}