package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundLockDifficultyPacket implements Packet<ServerGamePacketListener> {

    private final boolean locked;

    public ServerboundLockDifficultyPacket(boolean boolean0) {
        this.locked = boolean0;
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleLockDifficulty(this);
    }

    public ServerboundLockDifficultyPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.locked = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBoolean(this.locked);
    }

    public boolean isLocked() {
        return this.locked;
    }
}