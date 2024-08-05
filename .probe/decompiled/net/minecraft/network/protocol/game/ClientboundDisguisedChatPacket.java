package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public record ClientboundDisguisedChatPacket(Component f_244491_, ChatType.BoundNetwork f_244252_) implements Packet<ClientGamePacketListener> {

    private final Component message;

    private final ChatType.BoundNetwork chatType;

    public ClientboundDisguisedChatPacket(FriendlyByteBuf p_249018_) {
        this(p_249018_.readComponent(), new ChatType.BoundNetwork(p_249018_));
    }

    public ClientboundDisguisedChatPacket(Component f_244491_, ChatType.BoundNetwork f_244252_) {
        this.message = f_244491_;
        this.chatType = f_244252_;
    }

    @Override
    public void write(FriendlyByteBuf p_250975_) {
        p_250975_.writeComponent(this.message);
        this.chatType.write(p_250975_);
    }

    public void handle(ClientGamePacketListener p_251953_) {
        p_251953_.handleDisguisedChat(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}