package com.github.alexthe666.citadel.server.message;

import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.Validate;

public class PacketBufferUtils {

    public static int varIntByteCount(int toCount) {
        return (toCount & -128) == 0 ? 1 : ((toCount & -16384) == 0 ? 2 : ((toCount & -2097152) == 0 ? 3 : ((toCount & -268435456) == 0 ? 4 : 5)));
    }

    public static int readVarInt(ByteBuf buf, int maxSize) {
        Validate.isTrue(maxSize < 6 && maxSize > 0, "Varint length is between 1 and 5, not %d", (long) maxSize);
        int i = 0;
        int j = 0;
        byte b0;
        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > maxSize) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);
        return i;
    }

    public static int readVarShort(ByteBuf buf) {
        int low = buf.readUnsignedShort();
        int high = 0;
        if ((low & 32768) != 0) {
            low &= 32767;
            high = buf.readUnsignedByte();
        }
        return (high & 0xFF) << 15 | low;
    }

    public static void writeVarShort(ByteBuf buf, int toWrite) {
        int low = toWrite & 32767;
        int high = (toWrite & 8355840) >> 15;
        if (high != 0) {
            low |= 32768;
        }
        buf.writeShort(low);
        if (high != 0) {
            buf.writeByte(high);
        }
    }

    public static void writeVarInt(ByteBuf to, int toWrite, int maxSize) {
        Validate.isTrue(varIntByteCount(toWrite) <= maxSize, "Integer is too big for %d bytes", (long) maxSize);
        while ((toWrite & -128) != 0) {
            to.writeByte(toWrite & 127 | 128);
            toWrite >>>= 7;
        }
        to.writeByte(toWrite);
    }

    public static String readUTF8String(ByteBuf from) {
        int len = readVarInt(from, 2);
        String str = from.toString(from.readerIndex(), len, StandardCharsets.UTF_8);
        from.readerIndex(from.readerIndex() + len);
        return str;
    }

    public static void writeUTF8String(ByteBuf to, String string) {
        byte[] utf8Bytes = string.getBytes(StandardCharsets.UTF_8);
        Validate.isTrue(varIntByteCount(utf8Bytes.length) < 3, "The string is too long for this encoding.", new Object[0]);
        writeVarInt(to, utf8Bytes.length, 2);
        to.writeBytes(utf8Bytes);
    }

    public static void writeItemStack(ByteBuf to, ItemStack stack) {
        FriendlyByteBuf pb = new FriendlyByteBuf(to);
        pb.writeItem(stack);
    }

    public static ItemStack readItemStack(ByteBuf from) {
        FriendlyByteBuf pb = new FriendlyByteBuf(from);
        try {
            return pb.readItem();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public static void writeTag(ByteBuf to, CompoundTag tag) {
        FriendlyByteBuf pb = new FriendlyByteBuf(to);
        pb.writeNbt(tag);
    }

    @Nullable
    public static CompoundTag readTag(ByteBuf from) {
        FriendlyByteBuf pb = new FriendlyByteBuf(from);
        try {
            return pb.readNbt();
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}