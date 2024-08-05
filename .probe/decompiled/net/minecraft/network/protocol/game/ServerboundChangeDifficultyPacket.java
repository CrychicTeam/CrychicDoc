package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.Difficulty;

public class ServerboundChangeDifficultyPacket implements Packet<ServerGamePacketListener> {

    private final Difficulty difficulty;

    public ServerboundChangeDifficultyPacket(Difficulty difficulty0) {
        this.difficulty = difficulty0;
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleChangeDifficulty(this);
    }

    public ServerboundChangeDifficultyPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.difficulty = Difficulty.byId(friendlyByteBuf0.readUnsignedByte());
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.difficulty.getId());
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }
}