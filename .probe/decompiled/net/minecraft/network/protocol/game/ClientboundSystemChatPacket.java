package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public record ClientboundSystemChatPacket(Component f_237849_, boolean f_240374_) implements Packet<ClientGamePacketListener> {

    private final Component content;

    private final boolean overlay;

    public ClientboundSystemChatPacket(FriendlyByteBuf p_237852_) {
        this(p_237852_.readComponent(), p_237852_.readBoolean());
    }

    public ClientboundSystemChatPacket(Component f_237849_, boolean f_240374_) {
        this.content = f_237849_;
        this.overlay = f_240374_;
    }

    @Override
    public void write(FriendlyByteBuf p_237860_) {
        p_237860_.writeComponent(this.content);
        p_237860_.writeBoolean(this.overlay);
    }

    public void handle(ClientGamePacketListener p_237864_) {
        p_237864_.handleSystemChat(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }
}