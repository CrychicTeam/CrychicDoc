package net.minecraft.network.protocol.login;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ClientboundCustomQueryPacket implements Packet<ClientLoginPacketListener> {

    private static final int MAX_PAYLOAD_SIZE = 1048576;

    private final int transactionId;

    private final ResourceLocation identifier;

    private final FriendlyByteBuf data;

    public ClientboundCustomQueryPacket(int int0, ResourceLocation resourceLocation1, FriendlyByteBuf friendlyByteBuf2) {
        this.transactionId = int0;
        this.identifier = resourceLocation1;
        this.data = friendlyByteBuf2;
    }

    public ClientboundCustomQueryPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.transactionId = friendlyByteBuf0.readVarInt();
        this.identifier = friendlyByteBuf0.readResourceLocation();
        int $$1 = friendlyByteBuf0.readableBytes();
        if ($$1 >= 0 && $$1 <= 1048576) {
            this.data = new FriendlyByteBuf(friendlyByteBuf0.readBytes($$1));
        } else {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.transactionId);
        friendlyByteBuf0.writeResourceLocation(this.identifier);
        friendlyByteBuf0.writeBytes(this.data.copy());
    }

    public void handle(ClientLoginPacketListener clientLoginPacketListener0) {
        clientLoginPacketListener0.handleCustomQuery(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public FriendlyByteBuf getData() {
        return this.data;
    }
}