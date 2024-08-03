package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

public class ServerboundUseItemOnPacket implements Packet<ServerGamePacketListener> {

    private final BlockHitResult blockHit;

    private final InteractionHand hand;

    private final int sequence;

    public ServerboundUseItemOnPacket(InteractionHand interactionHand0, BlockHitResult blockHitResult1, int int2) {
        this.hand = interactionHand0;
        this.blockHit = blockHitResult1;
        this.sequence = int2;
    }

    public ServerboundUseItemOnPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
        this.blockHit = friendlyByteBuf0.readBlockHitResult();
        this.sequence = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.hand);
        friendlyByteBuf0.writeBlockHitResult(this.blockHit);
        friendlyByteBuf0.writeVarInt(this.sequence);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleUseItemOn(this);
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    public BlockHitResult getHitResult() {
        return this.blockHit;
    }

    public int getSequence() {
        return this.sequence;
    }
}