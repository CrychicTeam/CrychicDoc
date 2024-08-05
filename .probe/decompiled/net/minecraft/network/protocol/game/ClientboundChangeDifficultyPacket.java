package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.Difficulty;

public class ClientboundChangeDifficultyPacket implements Packet<ClientGamePacketListener> {

    private final Difficulty difficulty;

    private final boolean locked;

    public ClientboundChangeDifficultyPacket(Difficulty difficulty0, boolean boolean1) {
        this.difficulty = difficulty0;
        this.locked = boolean1;
    }

    public ClientboundChangeDifficultyPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.difficulty = Difficulty.byId(friendlyByteBuf0.readUnsignedByte());
        this.locked = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.difficulty.getId());
        friendlyByteBuf0.writeBoolean(this.locked);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleChangeDifficulty(this);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }
}