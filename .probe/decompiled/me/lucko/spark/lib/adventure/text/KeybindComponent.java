package me.lucko.spark.lib.adventure.text;

import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent> {

    @NotNull
    String keybind();

    @Contract(pure = true)
    @NotNull
    KeybindComponent keybind(@NotNull final String keybind);

    @Contract(pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind) {
        return this.keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind());
    }

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("keybind", this.keybind())), BuildableComponent.super.examinableProperties());
    }

    public interface Builder extends ComponentBuilder<KeybindComponent, KeybindComponent.Builder> {

        @Contract("_ -> this")
        @NotNull
        KeybindComponent.Builder keybind(@NotNull final String keybind);

        @Contract(pure = true)
        @NotNull
        default KeybindComponent.Builder keybind(@NotNull final KeybindComponent.KeybindLike keybind) {
            return this.keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind());
        }
    }

    public interface KeybindLike {

        @NotNull
        String asKeybind();
    }
}