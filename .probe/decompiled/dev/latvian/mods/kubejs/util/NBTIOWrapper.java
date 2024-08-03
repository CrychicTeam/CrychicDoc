package dev.latvian.mods.kubejs.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import org.jetbrains.annotations.Nullable;

public interface NBTIOWrapper {

    @Nullable
    static CompoundTag read(Path path) throws IOException {
        return !Files.isRegularFile(path, new LinkOption[0]) ? null : NbtIo.readCompressed(Files.newInputStream(path));
    }

    static void write(Path path, CompoundTag nbt) throws IOException {
        if (nbt == null) {
            Files.deleteIfExists(path);
        } else {
            NbtIo.writeCompressed(nbt, Files.newOutputStream(path));
        }
    }
}