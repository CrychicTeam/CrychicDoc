package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSignUpdatePacket implements Packet<ServerGamePacketListener> {

    private static final int MAX_STRING_LENGTH = 384;

    private final BlockPos pos;

    private final String[] lines;

    private final boolean isFrontText;

    public ServerboundSignUpdatePacket(BlockPos blockPos0, boolean boolean1, String string2, String string3, String string4, String string5) {
        this.pos = blockPos0;
        this.isFrontText = boolean1;
        this.lines = new String[] { string2, string3, string4, string5 };
    }

    public ServerboundSignUpdatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.isFrontText = friendlyByteBuf0.readBoolean();
        this.lines = new String[4];
        for (int $$1 = 0; $$1 < 4; $$1++) {
            this.lines[$$1] = friendlyByteBuf0.readUtf(384);
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeBoolean(this.isFrontText);
        for (int $$1 = 0; $$1 < 4; $$1++) {
            friendlyByteBuf0.writeUtf(this.lines[$$1]);
        }
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSignUpdate(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public boolean isFrontText() {
        return this.isFrontText;
    }

    public String[] getLines() {
        return this.lines;
    }
}