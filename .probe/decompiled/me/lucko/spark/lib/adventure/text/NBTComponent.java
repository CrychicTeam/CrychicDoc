package me.lucko.spark.lib.adventure.text;

import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NBTComponent<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends BuildableComponent<C, B> {

    @NotNull
    String nbtPath();

    @Contract(pure = true)
    @NotNull
    C nbtPath(@NotNull final String nbtPath);

    boolean interpret();

    @Contract(pure = true)
    @NotNull
    C interpret(final boolean interpret);

    @Nullable
    Component separator();

    @NotNull
    C separator(@Nullable final ComponentLike separator);

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("nbtPath", this.nbtPath()), ExaminableProperty.of("interpret", this.interpret()), ExaminableProperty.of("separator", this.separator())), BuildableComponent.super.examinableProperties());
    }
}