package net.minecraft.network.protocol.game;

import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundServerDataPacket implements Packet<ClientGamePacketListener> {

    private final Component motd;

    private final Optional<byte[]> iconBytes;

    private final boolean enforcesSecureChat;

    public ClientboundServerDataPacket(Component component0, Optional<byte[]> optionalByte1, boolean boolean2) {
        this.motd = component0;
        this.iconBytes = optionalByte1;
        this.enforcesSecureChat = boolean2;
    }

    public ClientboundServerDataPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.motd = friendlyByteBuf0.readComponent();
        this.iconBytes = friendlyByteBuf0.readOptional(FriendlyByteBuf::m_130052_);
        this.enforcesSecureChat = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.motd);
        friendlyByteBuf0.writeOptional(this.iconBytes, FriendlyByteBuf::m_130087_);
        friendlyByteBuf0.writeBoolean(this.enforcesSecureChat);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleServerData(this);
    }

    public Component getMotd() {
        return this.motd;
    }

    public Optional<byte[]> getIconBytes() {
        return this.iconBytes;
    }

    public boolean enforcesSecureChat() {
        return this.enforcesSecureChat;
    }
}