package me.lucko.spark.lib.adventure.chat;

import java.time.Instant;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.identity.Identified;
import me.lucko.spark.lib.adventure.identity.Identity;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.ComponentLike;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface SignedMessage extends Identified, Examinable {

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static SignedMessage.Signature signature(final byte[] signature) {
        return new SignedMessageImpl.SignatureImpl(signature);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static SignedMessage system(@NotNull final String message, @Nullable final ComponentLike unsignedContent) {
        return new SignedMessageImpl(message, ComponentLike.unbox(unsignedContent));
    }

    @Contract(pure = true)
    @NotNull
    Instant timestamp();

    @Contract(pure = true)
    long salt();

    @Contract(pure = true)
    @Nullable
    SignedMessage.Signature signature();

    @Contract(pure = true)
    @Nullable
    Component unsignedContent();

    @Contract(pure = true)
    @NotNull
    String message();

    @Contract(pure = true)
    default boolean isSystem() {
        return this.identity() == Identity.nil();
    }

    @Contract(pure = true)
    default boolean canDelete() {
        return this.signature() != null;
    }

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("timestamp", this.timestamp()), ExaminableProperty.of("salt", this.salt()), ExaminableProperty.of("signature", this.signature()), ExaminableProperty.of("unsignedContent", this.unsignedContent()), ExaminableProperty.of("message", this.message()));
    }

    @NonExtendable
    public interface Signature extends Examinable {

        @Contract(pure = true)
        byte[] bytes();

        @NotNull
        @Override
        default Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("bytes", this.bytes()));
        }
    }
}