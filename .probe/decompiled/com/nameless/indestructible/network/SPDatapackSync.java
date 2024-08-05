package com.nameless.indestructible.network;

import com.nameless.indestructible.data.AdvancedMobpatchReloader;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SPDatapackSync {

    protected int count;

    protected int index;

    protected CompoundTag[] tags;

    public SPDatapackSync(int count) {
        this.count = count;
        this.index = 0;
        this.tags = new CompoundTag[count];
    }

    public void write(CompoundTag tag) {
        this.tags[this.index] = tag;
        this.index++;
    }

    public CompoundTag[] getTags() {
        return this.tags;
    }

    public static SPDatapackSync fromBytes(FriendlyByteBuf buf) {
        SPDatapackSync msg = new SPDatapackSync(buf.readInt());
        for (int i = 0; i < msg.count; i++) {
            msg.tags[i] = buf.readNbt();
        }
        return msg;
    }

    public static void toBytes(SPDatapackSync msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.count);
        for (CompoundTag tag : msg.tags) {
            buf.writeNbt(tag);
        }
    }

    public static void handle(SPDatapackSync msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> AdvancedMobpatchReloader.processServerPacket(msg));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}