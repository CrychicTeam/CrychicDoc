package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;

public class ServerboundSwingPacket implements Packet<ServerGamePacketListener> {

    private final InteractionHand hand;

    public ServerboundSwingPacket(InteractionHand interactionHand0) {
        this.hand = interactionHand0;
    }

    public ServerboundSwingPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.hand);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleAnimate(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }
}