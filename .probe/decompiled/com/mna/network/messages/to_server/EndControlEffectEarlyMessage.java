package com.mna.network.messages.to_server;

import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class EndControlEffectEarlyMessage extends BaseMessage {

    public EndControlEffectEarlyMessage() {
        this.messageIsValid = true;
    }

    public static final EndControlEffectEarlyMessage decode(FriendlyByteBuf buf) {
        return new EndControlEffectEarlyMessage();
    }

    public static void encode(EndControlEffectEarlyMessage message, FriendlyByteBuf buf) {
    }
}