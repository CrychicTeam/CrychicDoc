package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetSubtitleTextPacket implements Packet<ClientGamePacketListener> {

    private final Component text;

    public ClientboundSetSubtitleTextPacket(Component component0) {
        this.text = component0;
    }

    public ClientboundSetSubtitleTextPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.text = friendlyByteBuf0.readComponent();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.text);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.setSubtitleText(this);
    }

    public Component getText() {
        return this.text;
    }
}