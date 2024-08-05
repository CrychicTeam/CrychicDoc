package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetHealthPacket implements Packet<ClientGamePacketListener> {

    private final float health;

    private final int food;

    private final float saturation;

    public ClientboundSetHealthPacket(float float0, int int1, float float2) {
        this.health = float0;
        this.food = int1;
        this.saturation = float2;
    }

    public ClientboundSetHealthPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.health = friendlyByteBuf0.readFloat();
        this.food = friendlyByteBuf0.readVarInt();
        this.saturation = friendlyByteBuf0.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeFloat(this.health);
        friendlyByteBuf0.writeVarInt(this.food);
        friendlyByteBuf0.writeFloat(this.saturation);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetHealth(this);
    }

    public float getHealth() {
        return this.health;
    }

    public int getFood() {
        return this.food;
    }

    public float getSaturation() {
        return this.saturation;
    }
}