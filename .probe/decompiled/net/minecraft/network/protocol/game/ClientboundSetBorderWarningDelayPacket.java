package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundSetBorderWarningDelayPacket implements Packet<ClientGamePacketListener> {

    private final int warningDelay;

    public ClientboundSetBorderWarningDelayPacket(WorldBorder worldBorder0) {
        this.warningDelay = worldBorder0.getWarningTime();
    }

    public ClientboundSetBorderWarningDelayPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.warningDelay = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.warningDelay);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetBorderWarningDelay(this);
    }

    public int getWarningDelay() {
        return this.warningDelay;
    }
}