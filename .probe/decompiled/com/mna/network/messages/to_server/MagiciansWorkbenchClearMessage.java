package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class MagiciansWorkbenchClearMessage extends BaseMessage {

    private boolean second;

    public MagiciansWorkbenchClearMessage(boolean second) {
        this.second = second;
        this.messageIsValid = true;
    }

    public MagiciansWorkbenchClearMessage() {
        this.messageIsValid = false;
    }

    public boolean isSecond() {
        return this.second;
    }

    public static MagiciansWorkbenchClearMessage decode(FriendlyByteBuf buf) {
        MagiciansWorkbenchClearMessage msg = new MagiciansWorkbenchClearMessage();
        try {
            msg.second = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading EnderDiscIndexSetMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(MagiciansWorkbenchClearMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isSecond());
    }
}