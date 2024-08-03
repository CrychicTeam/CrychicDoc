package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;

public class ClientboundOpenBookPacket implements Packet<ClientGamePacketListener> {

    private final InteractionHand hand;

    public ClientboundOpenBookPacket(InteractionHand interactionHand0) {
        this.hand = interactionHand0;
    }

    public ClientboundOpenBookPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.hand);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleOpenBook(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}