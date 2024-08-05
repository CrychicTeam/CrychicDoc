package net.minecraft.network.protocol.game;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.item.Item;

public class ClientboundCooldownPacket implements Packet<ClientGamePacketListener> {

    private final Item item;

    private final int duration;

    public ClientboundCooldownPacket(Item item0, int int1) {
        this.item = item0;
        this.duration = int1;
    }

    public ClientboundCooldownPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.item = friendlyByteBuf0.readById(BuiltInRegistries.ITEM);
        this.duration = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeId(BuiltInRegistries.ITEM, this.item);
        friendlyByteBuf0.writeVarInt(this.duration);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleItemCooldown(this);
    }

    public Item getItem() {
        return this.item;
    }

    public int getDuration() {
        return this.duration;
    }
}