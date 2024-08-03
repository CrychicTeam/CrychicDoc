package net.minecraft.network.protocol.game;

import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class ClientboundSetPassengersPacket implements Packet<ClientGamePacketListener> {

    private final int vehicle;

    private final int[] passengers;

    public ClientboundSetPassengersPacket(Entity entity0) {
        this.vehicle = entity0.getId();
        List<Entity> $$1 = entity0.getPassengers();
        this.passengers = new int[$$1.size()];
        for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
            this.passengers[$$2] = ((Entity) $$1.get($$2)).getId();
        }
    }

    public ClientboundSetPassengersPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.vehicle = friendlyByteBuf0.readVarInt();
        this.passengers = friendlyByteBuf0.readVarIntArray();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.vehicle);
        friendlyByteBuf0.writeVarIntArray(this.passengers);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetEntityPassengersPacket(this);
    }

    public int[] getPassengers() {
        return this.passengers;
    }

    public int getVehicle() {
        return this.vehicle;
    }
}