package net.minecraft.network.protocol.game;

import java.util.Set;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.RelativeMovement;

public class ClientboundPlayerPositionPacket implements Packet<ClientGamePacketListener> {

    private final double x;

    private final double y;

    private final double z;

    private final float yRot;

    private final float xRot;

    private final Set<RelativeMovement> relativeArguments;

    private final int id;

    public ClientboundPlayerPositionPacket(double double0, double double1, double double2, float float3, float float4, Set<RelativeMovement> setRelativeMovement5, int int6) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
        this.yRot = float3;
        this.xRot = float4;
        this.relativeArguments = setRelativeMovement5;
        this.id = int6;
    }

    public ClientboundPlayerPositionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readDouble();
        this.y = friendlyByteBuf0.readDouble();
        this.z = friendlyByteBuf0.readDouble();
        this.yRot = friendlyByteBuf0.readFloat();
        this.xRot = friendlyByteBuf0.readFloat();
        this.relativeArguments = RelativeMovement.unpack(friendlyByteBuf0.readUnsignedByte());
        this.id = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.x);
        friendlyByteBuf0.writeDouble(this.y);
        friendlyByteBuf0.writeDouble(this.z);
        friendlyByteBuf0.writeFloat(this.yRot);
        friendlyByteBuf0.writeFloat(this.xRot);
        friendlyByteBuf0.writeByte(RelativeMovement.pack(this.relativeArguments));
        friendlyByteBuf0.writeVarInt(this.id);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleMovePlayer(this);
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

    public int getId() {
        return this.id;
    }

    public Set<RelativeMovement> getRelativeArguments() {
        return this.relativeArguments;
    }
}