package com.mna.network.messages.to_server;

import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class OpenSpellRenameMessage extends BaseMessage {

    public OpenSpellRenameMessage() {
        this.messageIsValid = true;
    }

    public static OpenSpellRenameMessage decode(FriendlyByteBuf buf) {
        return new OpenSpellRenameMessage();
    }

    public static void encode(OpenSpellRenameMessage msg, FriendlyByteBuf buf) {
    }
}