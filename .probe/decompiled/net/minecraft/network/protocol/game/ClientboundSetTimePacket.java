package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetTimePacket implements Packet<ClientGamePacketListener> {

    private final long gameTime;

    private final long dayTime;

    public ClientboundSetTimePacket(long long0, long long1, boolean boolean2) {
        this.gameTime = long0;
        long $$3 = long1;
        if (!boolean2) {
            $$3 = -long1;
            if ($$3 == 0L) {
                $$3 = -1L;
            }
        }
        this.dayTime = $$3;
    }

    public ClientboundSetTimePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.gameTime = friendlyByteBuf0.readLong();
        this.dayTime = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.gameTime);
        friendlyByteBuf0.writeLong(this.dayTime);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetTime(this);
    }

    public long getGameTime() {
        return this.gameTime;
    }

    public long getDayTime() {
        return this.dayTime;
    }
}