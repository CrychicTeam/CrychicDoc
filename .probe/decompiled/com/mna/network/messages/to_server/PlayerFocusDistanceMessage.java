package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class PlayerFocusDistanceMessage extends BaseMessage {

    private float delta;

    private float maximum;

    public PlayerFocusDistanceMessage() {
        this.messageIsValid = false;
    }

    public PlayerFocusDistanceMessage(float delta, float maximum) {
        this.messageIsValid = true;
        this.delta = delta;
        this.maximum = maximum;
    }

    public float getDelta() {
        return this.delta;
    }

    public float getMaximum() {
        return this.maximum;
    }

    public static final PlayerFocusDistanceMessage decode(FriendlyByteBuf buf) {
        PlayerFocusDistanceMessage msg = new PlayerFocusDistanceMessage();
        try {
            msg.delta = buf.readFloat();
            msg.maximum = buf.readFloat();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading PlayerBouncePacket: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PlayerFocusDistanceMessage message, FriendlyByteBuf buf) {
        buf.writeFloat(message.getDelta());
        buf.writeFloat(message.getMaximum());
    }
}