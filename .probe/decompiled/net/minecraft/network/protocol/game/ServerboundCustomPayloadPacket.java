package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

public class ServerboundCustomPayloadPacket implements Packet<ServerGamePacketListener> {

    private static final int MAX_PAYLOAD_SIZE = 32767;

    public static final ResourceLocation BRAND = new ResourceLocation("brand");

    private final ResourceLocation identifier;

    private final FriendlyByteBuf data;

    public ServerboundCustomPayloadPacket(ResourceLocation resourceLocation0, FriendlyByteBuf friendlyByteBuf1) {
        this.identifier = resourceLocation0;
        this.data = friendlyByteBuf1;
    }

    public ServerboundCustomPayloadPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.identifier = friendlyByteBuf0.readResourceLocation();
        int $$1 = friendlyByteBuf0.readableBytes();
        if ($$1 >= 0 && $$1 <= 32767) {
            this.data = new FriendlyByteBuf(friendlyByteBuf0.readBytes($$1));
        } else {
            throw new IllegalArgumentException("Payload may not be larger than 32767 bytes");
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeResourceLocation(this.identifier);
        friendlyByteBuf0.writeBytes(this.data);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleCustomPayload(this);
        this.data.release();
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public FriendlyByteBuf getData() {
        return this.data;
    }
}