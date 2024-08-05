package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class UIModifierPress extends BaseMessage {

    boolean pressed;

    public UIModifierPress(boolean pressed) {
        this.pressed = pressed;
        this.messageIsValid = true;
    }

    public UIModifierPress() {
        this.messageIsValid = false;
    }

    public boolean getPressed() {
        return this.pressed;
    }

    public static UIModifierPress decode(FriendlyByteBuf buf) {
        UIModifierPress msg = new UIModifierPress();
        try {
            msg.pressed = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading UIModifierPress: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(UIModifierPress msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.getPressed());
    }
}