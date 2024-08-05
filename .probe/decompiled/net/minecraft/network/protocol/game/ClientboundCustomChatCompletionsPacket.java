package net.minecraft.network.protocol.game;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundCustomChatCompletionsPacket(ClientboundCustomChatCompletionsPacket.Action f_240661_, List<String> f_240663_) implements Packet<ClientGamePacketListener> {

    private final ClientboundCustomChatCompletionsPacket.Action action;

    private final List<String> entries;

    public ClientboundCustomChatCompletionsPacket(FriendlyByteBuf p_243340_) {
        this(p_243340_.readEnum(ClientboundCustomChatCompletionsPacket.Action.class), p_243340_.readList(FriendlyByteBuf::m_130277_));
    }

    public ClientboundCustomChatCompletionsPacket(ClientboundCustomChatCompletionsPacket.Action f_240661_, List<String> f_240663_) {
        this.action = f_240661_;
        this.entries = f_240663_;
    }

    @Override
    public void write(FriendlyByteBuf p_240782_) {
        p_240782_.writeEnum(this.action);
        p_240782_.writeCollection(this.entries, FriendlyByteBuf::m_130070_);
    }

    public void handle(ClientGamePacketListener p_240794_) {
        p_240794_.handleCustomChatCompletions(this);
    }

    public static enum Action {

        ADD, REMOVE, SET
    }
}