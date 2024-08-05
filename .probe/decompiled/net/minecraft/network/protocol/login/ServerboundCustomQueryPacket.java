package net.minecraft.network.protocol.login;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundCustomQueryPacket implements Packet<ServerLoginPacketListener> {

    private static final int MAX_PAYLOAD_SIZE = 1048576;

    private final int transactionId;

    @Nullable
    private final FriendlyByteBuf data;

    public ServerboundCustomQueryPacket(int int0, @Nullable FriendlyByteBuf friendlyByteBuf1) {
        this.transactionId = int0;
        this.data = friendlyByteBuf1;
    }

    public ServerboundCustomQueryPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.transactionId = friendlyByteBuf0.readVarInt();
        this.data = friendlyByteBuf0.readNullable(p_238039_ -> {
            int $$1 = p_238039_.readableBytes();
            if ($$1 >= 0 && $$1 <= 1048576) {
                return new FriendlyByteBuf(p_238039_.readBytes($$1));
            } else {
                throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.transactionId);
        friendlyByteBuf0.writeNullable(this.data, (p_238036_, p_238037_) -> p_238036_.writeBytes(p_238037_.slice()));
    }

    public void handle(ServerLoginPacketListener serverLoginPacketListener0) {
        serverLoginPacketListener0.handleCustomQueryPacket(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    @Nullable
    public FriendlyByteBuf getData() {
        return this.data;
    }
}