package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.translation.Translatable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent> {

    @NotNull
    String key();

    @Contract(pure = true)
    @NotNull
    default TranslatableComponent key(@NotNull final Translatable translatable) {
        return this.key(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey());
    }

    @Contract(pure = true)
    @NotNull
    TranslatableComponent key(@NotNull final String key);

    @NotNull
    List<Component> args();

    @Contract(pure = true)
    @NotNull
    TranslatableComponent args(@NotNull final ComponentLike... args);

    @Contract(pure = true)
    @NotNull
    TranslatableComponent args(@NotNull final List<? extends ComponentLike> args);

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(Stream.of(ExaminableProperty.of("key", this.key()), ExaminableProperty.of("args", this.args())), BuildableComponent.super.examinableProperties());
    }

    public interface Builder extends ComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> {

        @Contract(pure = true)
        @NotNull
        default TranslatableComponent.Builder key(@NotNull final Translatable translatable) {
            return this.key(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey());
        }

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder key(@NotNull final String key);

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?> arg);

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?>... args);

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder args(@NotNull final Component arg);

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder args(@NotNull final ComponentLike... args);

        @Contract("_ -> this")
        @NotNull
        TranslatableComponent.Builder args(@NotNull final List<? extends ComponentLike> args);
    }
}