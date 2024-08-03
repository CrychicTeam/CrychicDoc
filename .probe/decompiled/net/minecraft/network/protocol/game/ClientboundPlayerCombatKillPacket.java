package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerCombatKillPacket implements Packet<ClientGamePacketListener> {

    private final int playerId;

    private final Component message;

    public ClientboundPlayerCombatKillPacket(int int0, Component component1) {
        this.playerId = int0;
        this.message = component1;
    }

    public ClientboundPlayerCombatKillPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.playerId = friendlyByteBuf0.readVarInt();
        this.message = friendlyByteBuf0.readComponent();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.playerId);
        friendlyByteBuf0.writeComponent(this.message);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlayerCombatKill(this);
    }

    @Override
    public boolean isSkippable() {
        return true;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public Component getMessage() {
        return this.message;
    }
}