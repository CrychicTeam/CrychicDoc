package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ClientboundTeleportEntityPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    private final double x;

    private final double y;

    private final double z;

    private final byte yRot;

    private final byte xRot;

    private final boolean onGround;

    public ClientboundTeleportEntityPacket(Entity entity0) {
        this.id = entity0.getId();
        Vec3 $$1 = entity0.trackingPosition();
        this.x = $$1.x;
        this.y = $$1.y;
        this.z = $$1.z;
        this.yRot = (byte) ((int) (entity0.getYRot() * 256.0F / 360.0F));
        this.xRot = (byte) ((int) (entity0.getXRot() * 256.0F / 360.0F));
        this.onGround = entity0.onGround();
    }

    public ClientboundTeleportEntityPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.yRot = friendlyByteBuf0.readByte();
        this.xRot = friendlyByteBuf0.readByte();
        this.onGround = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeByte(this.yRot);
        friendlyByteBuf0.writeByte(this.xRot);
        friendlyByteBuf0.writeBoolean(this.onGround);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleTeleportEntity(this);
    }

    public int getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public byte getyRot() {
        return this.yRot;
    }

    public byte getxRot() {
        return this.xRot;
    }

    public boolean isOnGround() {
        return this.onGround;
    }
}