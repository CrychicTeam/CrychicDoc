package net.minecraft.network.protocol.game;

import net.minecraft.network.protocol.BundlePacket;
import net.minecraft.network.protocol.Packet;

public class ClientboundBundlePacket extends BundlePacket<ClientGamePacketListener> {

    public ClientboundBundlePacket(Iterable<Packet<ClientGamePacketListener>> iterablePacketClientGamePacketListener0) {
        super(iterablePacketClientGamePacketListener0);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBundlePacket(this);
    }
}