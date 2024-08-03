package dev.ftb.mods.ftblibrary.util;

import java.io.DataOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

public class NBTUtils {

    public static long getSizeInBytes(CompoundTag nbt, boolean compressed) {
        try {
            ByteCounterOutputStream byteCounter = new ByteCounterOutputStream();
            if (compressed) {
                NbtIo.writeCompressed(nbt, byteCounter);
            } else {
                NbtIo.write(nbt, new DataOutputStream(byteCounter));
            }
            return byteCounter.getSize();
        } catch (Exception var3) {
            return -1L;
        }
    }
}