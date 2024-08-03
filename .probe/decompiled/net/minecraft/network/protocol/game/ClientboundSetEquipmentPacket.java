package net.minecraft.network.protocol.game;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ClientboundSetEquipmentPacket implements Packet<ClientGamePacketListener> {

    private static final byte CONTINUE_MASK = -128;

    private final int entity;

    private final List<Pair<EquipmentSlot, ItemStack>> slots;

    public ClientboundSetEquipmentPacket(int int0, List<Pair<EquipmentSlot, ItemStack>> listPairEquipmentSlotItemStack1) {
        this.entity = int0;
        this.slots = listPairEquipmentSlotItemStack1;
    }

    public ClientboundSetEquipmentPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entity = friendlyByteBuf0.readVarInt();
        EquipmentSlot[] $$1 = EquipmentSlot.values();
        this.slots = Lists.newArrayList();
        int $$2;
        do {
            $$2 = friendlyByteBuf0.readByte();
            EquipmentSlot $$3 = $$1[$$2 & 127];
            ItemStack $$4 = friendlyByteBuf0.readItem();
            this.slots.add(Pair.of($$3, $$4));
        } while (($$2 & -128) != 0);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entity);
        int $$1 = this.slots.size();
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            Pair<EquipmentSlot, ItemStack> $$3 = (Pair<EquipmentSlot, ItemStack>) this.slots.get($$2);
            EquipmentSlot $$4 = (EquipmentSlot) $$3.getFirst();
            boolean $$5 = $$2 != $$1 - 1;
            int $$6 = $$4.ordinal();
            friendlyByteBuf0.writeByte($$5 ? $$6 | -128 : $$6);
            friendlyByteBuf0.writeItem((ItemStack) $$3.getSecond());
        }
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetEquipment(this);
    }

    public int getEntity() {
        return this.entity;
    }

    public List<Pair<EquipmentSlot, ItemStack>> getSlots() {
        return this.slots;
    }
}