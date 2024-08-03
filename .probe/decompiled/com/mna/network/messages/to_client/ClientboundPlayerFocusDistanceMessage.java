package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class ClientboundPlayerFocusDistanceMessage extends BaseMessage {

    private float delta;

    private float maximum;

    public ClientboundPlayerFocusDistanceMessage() {
        this.messageIsValid = false;
    }

    public ClientboundPlayerFocusDistanceMessage(float delta, float maximum) {
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

    public static final ClientboundPlayerFocusDistanceMessage decode(FriendlyByteBuf buf) {
        ClientboundPlayerFocusDistanceMessage msg = new ClientboundPlayerFocusDistanceMessage();
        try {
            msg.delta = buf.readFloat();
            msg.maximum = buf.readFloat();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading ClientboundPlayerFocusDistanceMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(ClientboundPlayerFocusDistanceMessage message, FriendlyByteBuf buf) {
        buf.writeFloat(message.getDelta());
        buf.writeFloat(message.getMaximum());
    }
}