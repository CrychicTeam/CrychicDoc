package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public record ClientboundSetSimulationDistancePacket(int f_195796_) implements Packet<ClientGamePacketListener> {

    private final int simulationDistance;

    public ClientboundSetSimulationDistancePacket(FriendlyByteBuf p_195800_) {
        this(p_195800_.readVarInt());
    }

    public ClientboundSetSimulationDistancePacket(int f_195796_) {
        this.simulationDistance = f_195796_;
    }

    @Override
    public void write(FriendlyByteBuf p_195802_) {
        p_195802_.writeVarInt(this.simulationDistance);
    }

    public void handle(ClientGamePacketListener p_195806_) {
        p_195806_.handleSetSimulationDistance(this);
    }
}