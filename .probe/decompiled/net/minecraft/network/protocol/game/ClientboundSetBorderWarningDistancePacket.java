package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundSetBorderWarningDistancePacket implements Packet<ClientGamePacketListener> {

    private final int warningBlocks;

    public ClientboundSetBorderWarningDistancePacket(WorldBorder worldBorder0) {
        this.warningBlocks = worldBorder0.getWarningBlocks();
    }

    public ClientboundSetBorderWarningDistancePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.warningBlocks = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.warningBlocks);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetBorderWarningDistance(this);
    }

    public int getWarningBlocks() {
        return this.warningBlocks;
    }
}