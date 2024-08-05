package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerCombatEnterPacket implements Packet<ClientGamePacketListener> {

    public ClientboundPlayerCombatEnterPacket() {
    }

    public ClientboundPlayerCombatEnterPacket(FriendlyByteBuf friendlyByteBuf0) {
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlayerCombatEnter(this);
    }
}