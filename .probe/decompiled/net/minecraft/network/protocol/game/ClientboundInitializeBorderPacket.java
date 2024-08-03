package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundInitializeBorderPacket implements Packet<ClientGamePacketListener> {

    private final double newCenterX;

    private final double newCenterZ;

    private final double oldSize;

    private final double newSize;

    private final long lerpTime;

    private final int newAbsoluteMaxSize;

    private final int warningBlocks;

    private final int warningTime;

    public ClientboundInitializeBorderPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.newCenterX = friendlyByteBuf0.readDouble();
        this.newCenterZ = friendlyByteBuf0.readDouble();
        this.oldSize = friendlyByteBuf0.readDouble();
        this.newSize = friendlyByteBuf0.readDouble();
        this.lerpTime = friendlyByteBuf0.readVarLong();
        this.newAbsoluteMaxSize = friendlyByteBuf0.readVarInt();
        this.warningBlocks = friendlyByteBuf0.readVarInt();
        this.warningTime = friendlyByteBuf0.readVarInt();
    }

    public ClientboundInitializeBorderPacket(WorldBorder worldBorder0) {
        this.newCenterX = worldBorder0.getCenterX();
        this.newCenterZ = worldBorder0.getCenterZ();
        this.oldSize = worldBorder0.getSize();
        this.newSize = worldBorder0.getLerpTarget();
        this.lerpTime = worldBorder0.getLerpRemainingTime();
        this.newAbsoluteMaxSize = worldBorder0.getAbsoluteMaxSize();
        this.warningBlocks = worldBorder0.getWarningBlocks();
        this.warningTime = worldBorder0.getWarningTime();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.newCenterX);
        friendlyByteBuf0.writeDouble(this.newCenterZ);
        friendlyByteBuf0.writeDouble(this.oldSize);
        friendlyByteBuf0.writeDouble(this.newSize);
        friendlyByteBuf0.writeVarLong(this.lerpTime);
        friendlyByteBuf0.writeVarInt(this.newAbsoluteMaxSize);
        friendlyByteBuf0.writeVarInt(this.warningBlocks);
        friendlyByteBuf0.writeVarInt(this.warningTime);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleInitializeBorder(this);
    }

    public double getNewCenterX() {
        return this.newCenterX;
    }

    public double getNewCenterZ() {
        return this.newCenterZ;
    }

    public double getNewSize() {
        return this.newSize;
    }

    public double getOldSize() {
        return this.oldSize;
    }

    public long getLerpTime() {
        return this.lerpTime;
    }

    public int getNewAbsoluteMaxSize() {
        return this.newAbsoluteMaxSize;
    }

    public int getWarningTime() {
        return this.warningTime;
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }
}