package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;

public class ServerboundUseItemPacket implements Packet<ServerGamePacketListener> {

    private final InteractionHand hand;

    private final int sequence;

    public ServerboundUseItemPacket(InteractionHand interactionHand0, int int1) {
        this.hand = interactionHand0;
        this.sequence = int1;
    }

    public ServerboundUseItemPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
        this.sequence = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.hand);
        friendlyByteBuf0.writeVarInt(this.sequence);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleUseItem(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public int getSequence() {
        return this.sequence;
    }
}