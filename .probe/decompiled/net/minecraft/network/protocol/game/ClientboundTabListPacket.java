package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundTabListPacket implements Packet<ClientGamePacketListener> {

    private final Component header;

    private final Component footer;

    public ClientboundTabListPacket(Component component0, Component component1) {
        this.header = component0;
        this.footer = component1;
    }

    public ClientboundTabListPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.header = friendlyByteBuf0.readComponent();
        this.footer = friendlyByteBuf0.readComponent();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.header);
        friendlyByteBuf0.writeComponent(this.footer);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleTabListCustomisation(this);
    }

    public Component getHeader() {
        return this.header;
    }

    public Component getFooter() {
        return this.footer;
    }
}