package me.lucko.spark.lib.adventure.nbt.api;

import me.lucko.spark.lib.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface BinaryTagHolder {

    @NotNull
    static <T, EX extends Exception> BinaryTagHolder encode(@NotNull final T nbt, @NotNull final Codec<? super T, String, ?, EX> codec) throws EX {
        return new BinaryTagHolderImpl(codec.encode(nbt));
    }

    @NotNull
    static BinaryTagHolder binaryTagHolder(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    static BinaryTagHolder of(@NotNull final String string) {
        return new BinaryTagHolderImpl(string);
    }

    @NotNull
    String string();

    @NotNull
    <T, DX extends Exception> T get(@NotNull final Codec<T, String, DX, ?> codec) throws DX;
}