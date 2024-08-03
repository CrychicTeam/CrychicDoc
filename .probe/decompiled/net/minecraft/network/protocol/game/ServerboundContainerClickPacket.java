package net.minecraft.network.protocol.game;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.function.IntFunction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public class ServerboundContainerClickPacket implements Packet<ServerGamePacketListener> {

    private static final int MAX_SLOT_COUNT = 128;

    private final int containerId;

    private final int stateId;

    private final int slotNum;

    private final int buttonNum;

    private final ClickType clickType;

    private final ItemStack carriedItem;

    private final Int2ObjectMap<ItemStack> changedSlots;

    public ServerboundContainerClickPacket(int int0, int int1, int int2, int int3, ClickType clickType4, ItemStack itemStack5, Int2ObjectMap<ItemStack> intObjectMapItemStack6) {
        this.containerId = int0;
        this.stateId = int1;
        this.slotNum = int2;
        this.buttonNum = int3;
        this.clickType = clickType4;
        this.carriedItem = itemStack5;
        this.changedSlots = Int2ObjectMaps.unmodifiable(intObjectMapItemStack6);
    }

    public ServerboundContainerClickPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.containerId = friendlyByteBuf0.readByte();
        this.stateId = friendlyByteBuf0.readVarInt();
        this.slotNum = friendlyByteBuf0.readShort();
        this.buttonNum = friendlyByteBuf0.readByte();
        this.clickType = friendlyByteBuf0.readEnum(ClickType.class);
        IntFunction<Int2ObjectOpenHashMap<ItemStack>> $$1 = FriendlyByteBuf.limitValue(Int2ObjectOpenHashMap::new, 128);
        this.changedSlots = Int2ObjectMaps.unmodifiable(friendlyByteBuf0.readMap($$1, p_179580_ -> Integer.valueOf(p_179580_.readShort()), FriendlyByteBuf::m_130267_));
        this.carriedItem = friendlyByteBuf0.readItem();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.containerId);
        friendlyByteBuf0.writeVarInt(this.stateId);
        friendlyByteBuf0.writeShort(this.slotNum);
        friendlyByteBuf0.writeByte(this.buttonNum);
        friendlyByteBuf0.writeEnum(this.clickType);
        friendlyByteBuf0.writeMap(this.changedSlots, FriendlyByteBuf::writeShort, FriendlyByteBuf::m_130055_);
        friendlyByteBuf0.writeItem(this.carriedItem);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleContainerClick(this);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public int getSlotNum() {
        return this.slotNum;
    }

    public int getButtonNum() {
        return this.buttonNum;
    }

    public ItemStack getCarriedItem() {
        return this.carriedItem;
    }

    public Int2ObjectMap<ItemStack> getChangedSlots() {
        return this.changedSlots;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public int getStateId() {
        return this.stateId;
    }
}