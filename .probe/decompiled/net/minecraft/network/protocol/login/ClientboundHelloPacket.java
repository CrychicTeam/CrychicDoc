package net.minecraft.network.protocol.login;

import java.security.PublicKey;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;

public class ClientboundHelloPacket implements Packet<ClientLoginPacketListener> {

    private final String serverId;

    private final byte[] publicKey;

    private final byte[] challenge;

    public ClientboundHelloPacket(String string0, byte[] byte1, byte[] byte2) {
        this.serverId = string0;
        this.publicKey = byte1;
        this.challenge = byte2;
    }

    public ClientboundHelloPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.serverId = friendlyByteBuf0.readUtf(20);
        this.publicKey = friendlyByteBuf0.readByteArray();
        this.challenge = friendlyByteBuf0.readByteArray();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeUtf(this.serverId);
        friendlyByteBuf0.writeByteArray(this.publicKey);
        friendlyByteBuf0.writeByteArray(this.challenge);
    }

    public void handle(ClientLoginPacketListener clientLoginPacketListener0) {
        clientLoginPacketListener0.handleHello(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() throws CryptException {
        return Crypt.byteToPublicKey(this.publicKey);
    }

    public byte[] getChallenge() {
        return this.challenge;
    }
}