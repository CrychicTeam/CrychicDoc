package me.lucko.spark.lib.adventure.audience;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import me.lucko.spark.lib.adventure.chat.ChatType;
import me.lucko.spark.lib.adventure.chat.SignedMessage;
import me.lucko.spark.lib.adventure.identity.Identified;
import me.lucko.spark.lib.adventure.identity.Identity;
import me.lucko.spark.lib.adventure.inventory.Book;
import me.lucko.spark.lib.adventure.pointer.Pointer;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.ComponentLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

final class EmptyAudience implements Audience {

    static final EmptyAudience INSTANCE = new EmptyAudience();

    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        return Optional.empty();
    }

    @Contract("_, null -> null; _, !null -> !null")
    @Nullable
    @Override
    public <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
        return defaultValue;
    }

    @UnknownNullability
    @Override
    public <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
        return (T) defaultValue.get();
    }

    @NotNull
    @Override
    public Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        return this;
    }

    @Override
    public void forEachAudience(@NotNull final Consumer<? super Audience> action) {
    }

    @Override
    public void sendMessage(@NotNull final ComponentLike message) {
    }

    @Override
    public void sendMessage(@NotNull final Component message) {
    }

    @Deprecated
    @Override
    public void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
    }

    @Deprecated
    @Override
    public void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
    }

    @Override
    public void sendMessage(@NotNull final Component message, @NotNull final ChatType.Bound boundChatType) {
    }

    @Override
    public void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
    }

    @Override
    public void deleteMessage(@NotNull final SignedMessage.Signature signature) {
    }

    @Override
    public void sendActionBar(@NotNull final ComponentLike message) {
    }

    @Override
    public void sendPlayerListHeader(@NotNull final ComponentLike header) {
    }

    @Override
    public void sendPlayerListFooter(@NotNull final ComponentLike footer) {
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull final ComponentLike header, @NotNull final ComponentLike footer) {
    }

    @Override
    public void openBook(@NotNull final Book.Builder book) {
    }

    public boolean equals(final Object that) {
        return this == that;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "EmptyAudience";
    }
}