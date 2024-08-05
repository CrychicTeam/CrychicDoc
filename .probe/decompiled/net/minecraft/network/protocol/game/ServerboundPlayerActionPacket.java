package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerActionPacket implements Packet<ServerGamePacketListener> {

    private final BlockPos pos;

    private final Direction direction;

    private final ServerboundPlayerActionPacket.Action action;

    private final int sequence;

    public ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action serverboundPlayerActionPacketAction0, BlockPos blockPos1, Direction direction2, int int3) {
        this.action = serverboundPlayerActionPacketAction0;
        this.pos = blockPos1.immutable();
        this.direction = direction2;
        this.sequence = int3;
    }

    public ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action serverboundPlayerActionPacketAction0, BlockPos blockPos1, Direction direction2) {
        this(serverboundPlayerActionPacketAction0, blockPos1, direction2, 0);
    }

    public ServerboundPlayerActionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.action = friendlyByteBuf0.readEnum(ServerboundPlayerActionPacket.Action.class);
        this.pos = friendlyByteBuf0.readBlockPos();
        this.direction = Direction.from3DDataValue(friendlyByteBuf0.readUnsignedByte());
        this.sequence = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeEnum(this.action);
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeByte(this.direction.get3DDataValue());
        friendlyByteBuf0.writeVarInt(this.sequence);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePlayerAction(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public ServerboundPlayerActionPacket.Action getAction() {
        return this.action;
    }

    public int getSequence() {
        return this.sequence;
    }

    public static enum Action {

        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_ITEM_WITH_OFFHAND
    }
}