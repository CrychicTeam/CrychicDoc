package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.CombatTracker;

public class ClientboundPlayerCombatEndPacket implements Packet<ClientGamePacketListener> {

    private final int duration;

    public ClientboundPlayerCombatEndPacket(CombatTracker combatTracker0) {
        this(combatTracker0.getCombatDuration());
    }

    public ClientboundPlayerCombatEndPacket(int int0) {
        this.duration = int0;
    }

    public ClientboundPlayerCombatEndPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.duration = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.duration);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePlayerCombatEnd(this);
    }
}