package top.theillusivec4.curios.common.network.server.sync;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;

public class SPacketSyncData {

    private final ListTag slotData;

    private final ListTag entityData;

    public SPacketSyncData(ListTag slotData, ListTag entityData) {
        this.slotData = slotData;
        this.entityData = entityData;
    }

    public static void encode(SPacketSyncData msg, FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.put("SlotData", msg.slotData);
        tag.put("EntityData", msg.entityData);
        buf.writeNbt(tag);
    }

    public static SPacketSyncData decode(FriendlyByteBuf buf) {
        CompoundTag tag = buf.readNbt();
        return tag != null ? new SPacketSyncData(tag.getList("SlotData", 10), tag.getList("EntityData", 10)) : new SPacketSyncData(new ListTag(), new ListTag());
    }

    public static void handle(SPacketSyncData msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            CuriosSlotManager.applySyncPacket(msg.slotData);
            CuriosEntityManager.applySyncPacket(msg.entityData);
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}