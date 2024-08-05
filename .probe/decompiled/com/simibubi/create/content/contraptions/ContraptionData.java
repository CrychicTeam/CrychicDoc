package com.simibubi.create.content.contraptions;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.foundation.mixin.accessor.NbtAccounterAccessor;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;

public class ContraptionData {

    public static final int DEFAULT_LIMIT = 2000000;

    public static final int CONNECTIVITY_LIMIT = Integer.MAX_VALUE;

    public static final int PACKET_FIXER_LIMIT = 209715200;

    public static final int XL_PACKETS_LIMIT = 2000000000;

    public static final int PICKUP_LIMIT;

    public static boolean isTooLargeForSync(CompoundTag data) {
        int max = AllConfigs.server().kinetics.maxDataSize.get();
        return max != 0 && packetSize(data) > (long) max;
    }

    public static boolean isTooLargeForPickup(CompoundTag data) {
        return packetSize(data) > (long) PICKUP_LIMIT;
    }

    public static long packetSize(CompoundTag data) {
        FriendlyByteBuf test = new FriendlyByteBuf(Unpooled.buffer());
        test.writeNbt(data);
        NbtAccounter sizeTracker = new NbtAccounter(Long.MAX_VALUE);
        test.readNbt(sizeTracker);
        long size = ((NbtAccounterAccessor) sizeTracker).create$getUsage();
        test.release();
        return size;
    }

    static {
        int limit = 2000000;
        if (Mods.CONNECTIVITY.isLoaded()) {
            limit = Integer.MAX_VALUE;
        }
        if (Mods.XLPACKETS.isLoaded()) {
            limit = 2000000000;
        }
        if (Mods.PACKETFIXER.isLoaded()) {
            limit = 209715200;
        }
        PICKUP_LIMIT = limit;
    }
}