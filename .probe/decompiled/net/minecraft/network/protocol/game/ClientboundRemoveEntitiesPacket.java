package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundRemoveEntitiesPacket implements Packet<ClientGamePacketListener> {

    private final IntList entityIds;

    public ClientboundRemoveEntitiesPacket(IntList intList0) {
        this.entityIds = new IntArrayList(intList0);
    }

    public ClientboundRemoveEntitiesPacket(int... int0) {
        this.entityIds = new IntArrayList(int0);
    }

    public ClientboundRemoveEntitiesPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityIds = friendlyByteBuf0.readIntIdList();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeIntIdList(this.entityIds);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleRemoveEntities(this);
    }

    public IntList getEntityIds() {
        return this.entityIds;
    }
}