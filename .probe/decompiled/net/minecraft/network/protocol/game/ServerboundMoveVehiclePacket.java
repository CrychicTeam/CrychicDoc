package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class ServerboundMoveVehiclePacket implements Packet<ServerGamePacketListener> {

    private final double x;

    private final double y;

    private final double z;

    private final float yRot;

    private final float xRot;

    public ServerboundMoveVehiclePacket(Entity entity0) {
        this.x = entity0.getX();
        this.y = entity0.getY();
        this.z = entity0.getZ();
        this.yRot = entity0.getYRot();
        this.xRot = entity0.getXRot();
    }

    public ServerboundMoveVehiclePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.yRot = friendlyByteBuf0.readFloat();
        this.xRot = friendlyByteBuf0.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeFloat(this.yRot);
        friendlyByteBuf0.writeFloat(this.xRot);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleMoveVehicle(this);
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

    public float getYRot() {
        return this.yRot;
    }

    public float getXRot() {
        return this.xRot;
    }
}