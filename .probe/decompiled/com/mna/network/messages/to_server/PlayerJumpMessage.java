package com.mna.network.messages.to_server;

import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;

public class PlayerJumpMessage extends BaseMessage {

    public PlayerJumpMessage() {
        this.messageIsValid = true;
    }

    public static final PlayerJumpMessage decode(FriendlyByteBuf buf) {
        PlayerJumpMessage msg = new PlayerJumpMessage();
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(PlayerJumpMessage message, FriendlyByteBuf buf) {
    }
}