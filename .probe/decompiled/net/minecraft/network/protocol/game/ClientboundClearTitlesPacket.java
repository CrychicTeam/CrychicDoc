package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundClearTitlesPacket implements Packet<ClientGamePacketListener> {

    private final boolean resetTimes;

    public ClientboundClearTitlesPacket(boolean boolean0) {
        this.resetTimes = boolean0;
    }

    public ClientboundClearTitlesPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.resetTimes = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBoolean(this.resetTimes);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleTitlesClear(this);
    }

    public boolean shouldResetTimes() {
        return this.resetTimes;
    }
}