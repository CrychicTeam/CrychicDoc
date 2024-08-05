package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.entity.CommandBlockEntity;

public class ServerboundSetCommandBlockPacket implements Packet<ServerGamePacketListener> {

    private static final int FLAG_TRACK_OUTPUT = 1;

    private static final int FLAG_CONDITIONAL = 2;

    private static final int FLAG_AUTOMATIC = 4;

    private final BlockPos pos;

    private final String command;

    private final boolean trackOutput;

    private final boolean conditional;

    private final boolean automatic;

    private final CommandBlockEntity.Mode mode;

    public ServerboundSetCommandBlockPacket(BlockPos blockPos0, String string1, CommandBlockEntity.Mode commandBlockEntityMode2, boolean boolean3, boolean boolean4, boolean boolean5) {
        this.pos = blockPos0;
        this.command = string1;
        this.trackOutput = boolean3;
        this.conditional = boolean4;
        this.automatic = boolean5;
        this.mode = commandBlockEntityMode2;
    }

    public ServerboundSetCommandBlockPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.command = friendlyByteBuf0.readUtf();
        this.mode = friendlyByteBuf0.readEnum(CommandBlockEntity.Mode.class);
        int $$1 = friendlyByteBuf0.readByte();
        this.trackOutput = ($$1 & 1) != 0;
        this.conditional = ($$1 & 2) != 0;
        this.automatic = ($$1 & 4) != 0;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeUtf(this.command);
        friendlyByteBuf0.writeEnum(this.mode);
        int $$1 = 0;
        if (this.trackOutput) {
            $$1 |= 1;
        }
        if (this.conditional) {
            $$1 |= 2;
        }
        if (this.automatic) {
            $$1 |= 4;
        }
        friendlyByteBuf0.writeByte($$1);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetCommandBlock(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public String getCommand() {
        return this.command;
    }

    public boolean isTrackOutput() {
        return this.trackOutput;
    }

    public boolean isConditional() {
        return this.conditional;
    }

    public boolean isAutomatic() {
        return this.automatic;
    }

    public CommandBlockEntity.Mode getMode() {
        return this.mode;
    }
}