package me.lucko.spark.lib.adventure.text;

import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface StorageNBTComponent extends NBTComponent<StorageNBTComponent, StorageNBTComponent.Builder>, ScopedComponent<StorageNBTComponent> {

    @NotNull
    Key storage();

    @Contract(pure = true)
    @NotNull
    StorageNBTComponent storage(@NotNull final Key storage);

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("storage", this.storage())), NBTComponent.super.examinableProperties());
    }

    public interface Builder extends NBTComponentBuilder<StorageNBTComponent, StorageNBTComponent.Builder> {

        @Contract("_ -> this")
        @NotNull
        StorageNBTComponent.Builder storage(@NotNull final Key storage);
    }
}