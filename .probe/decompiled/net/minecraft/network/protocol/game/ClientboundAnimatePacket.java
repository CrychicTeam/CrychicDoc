package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class ClientboundAnimatePacket implements Packet<ClientGamePacketListener> {

    public static final int SWING_MAIN_HAND = 0;

    public static final int WAKE_UP = 2;

    public static final int SWING_OFF_HAND = 3;

    public static final int CRITICAL_HIT = 4;

    public static final int MAGIC_CRITICAL_HIT = 5;

    private final int id;

    private final int action;

    public ClientboundAnimatePacket(Entity entity0, int int1) {
        this.id = entity0.getId();
        this.action = int1;
    }

    public ClientboundAnimatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.action = friendlyByteBuf0.readUnsignedByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeByte(this.action);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleAnimate(this);
    }

    public int getId() {
        return this.id;
    }

    public int getAction() {
        return this.action;
    }
}