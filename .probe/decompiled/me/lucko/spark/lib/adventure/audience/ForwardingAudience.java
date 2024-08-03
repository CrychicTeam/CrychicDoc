package me.lucko.spark.lib.adventure.audience;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import me.lucko.spark.lib.adventure.bossbar.BossBar;
import me.lucko.spark.lib.adventure.chat.ChatType;
import me.lucko.spark.lib.adventure.chat.SignedMessage;
import me.lucko.spark.lib.adventure.identity.Identified;
import me.lucko.spark.lib.adventure.identity.Identity;
import me.lucko.spark.lib.adventure.inventory.Book;
import me.lucko.spark.lib.adventure.pointer.Pointer;
import me.lucko.spark.lib.adventure.pointer.Pointers;
import me.lucko.spark.lib.adventure.sound.Sound;
import me.lucko.spark.lib.adventure.sound.SoundStop;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.title.TitlePart;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;

@FunctionalInterface
public interface ForwardingAudience extends Audience {

    @OverrideOnly
    @NotNull
    Iterable<? extends Audience> audiences();

    @NotNull
    @Override
    default Pointers pointers() {
        return Pointers.empty();
    }

    @NotNull
    @Override
    default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
        List<Audience> audiences = null;
        for (Audience audience : this.audiences()) {
            if (filter.test(audience)) {
                Audience filtered = audience.filterAudience(filter);
                if (filtered != Audience.empty()) {
                    if (audiences == null) {
                        audiences = new ArrayList();
                    }
                    audiences.add(filtered);
                }
            }
        }
        return (Audience) (audiences != null ? Audience.audience(audiences) : Audience.empty());
    }

    @Override
    default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
        for (Audience audience : this.audiences()) {
            audience.forEachAudience(action);
        }
    }

    @Override
    default void sendMessage(@NotNull final Component message) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(message);
        }
    }

    @Override
    default void sendMessage(@NotNull final Component message, @NotNull final ChatType.Bound boundChatType) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(message, boundChatType);
        }
    }

    @Override
    default void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(signedMessage, boundChatType);
        }
    }

    @Override
    default void deleteMessage(@NotNull final SignedMessage.Signature signature) {
        for (Audience audience : this.audiences()) {
            audience.deleteMessage(signature);
        }
    }

    @Deprecated
    @Override
    default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }

    @Deprecated
    @Override
    default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
        for (Audience audience : this.audiences()) {
            audience.sendMessage(source, message, type);
        }
    }

    @Override
    default void sendActionBar(@NotNull final Component message) {
        for (Audience audience : this.audiences()) {
            audience.sendActionBar(message);
        }
    }

    @Override
    default void sendPlayerListHeader(@NotNull final Component header) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListHeader(header);
        }
    }

    @Override
    default void sendPlayerListFooter(@NotNull final Component footer) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListFooter(footer);
        }
    }

    @Override
    default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
        for (Audience audience : this.audiences()) {
            audience.sendPlayerListHeaderAndFooter(header, footer);
        }
    }

    @Override
    default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
        for (Audience audience : this.audiences()) {
            audience.sendTitlePart(part, value);
        }
    }

    @Override
    default void clearTitle() {
        for (Audience audience : this.audiences()) {
            audience.clearTitle();
        }
    }

    @Override
    default void resetTitle() {
        for (Audience audience : this.audiences()) {
            audience.resetTitle();
        }
    }

    @Override
    default void showBossBar(@NotNull final BossBar bar) {
        for (Audience audience : this.audiences()) {
            audience.showBossBar(bar);
        }
    }

    @Override
    default void hideBossBar(@NotNull final BossBar bar) {
        for (Audience audience : this.audiences()) {
            audience.hideBossBar(bar);
        }
    }

    @Override
    default void playSound(@NotNull final Sound sound) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound);
        }
    }

    @Override
    default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound, x, y, z);
        }
    }

    @Override
    default void playSound(@NotNull final Sound sound, @NotNull final Sound.Emitter emitter) {
        for (Audience audience : this.audiences()) {
            audience.playSound(sound, emitter);
        }
    }

    @Override
    default void stopSound(@NotNull final SoundStop stop) {
        for (Audience audience : this.audiences()) {
            audience.stopSound(stop);
        }
    }

    @Override
    default void openBook(@NotNull final Book book) {
        for (Audience audience : this.audiences()) {
            audience.openBook(book);
        }
    }

    public interface Single extends ForwardingAudience {

        @OverrideOnly
        @NotNull
        Audience audience();

        @Deprecated
        @NotNull
        @Override
        default Iterable<? extends Audience> audiences() {
            return Collections.singleton(this.audience());
        }

        @NotNull
        @Override
        default <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
            return this.audience().get(pointer);
        }

        @Contract("_, null -> null; _, !null -> !null")
        @Nullable
        @Override
        default <T> T getOrDefault(@NotNull final Pointer<T> pointer, @Nullable final T defaultValue) {
            return this.audience().getOrDefault(pointer, defaultValue);
        }

        @UnknownNullability
        @Override
        default <T> T getOrDefaultFrom(@NotNull final Pointer<T> pointer, @NotNull final Supplier<? extends T> defaultValue) {
            return this.audience().getOrDefaultFrom(pointer, defaultValue);
        }

        @NotNull
        @Override
        default Audience filterAudience(@NotNull final Predicate<? super Audience> filter) {
            Audience audience = this.audience();
            return (Audience) (filter.test(audience) ? this : Audience.empty());
        }

        @Override
        default void forEachAudience(@NotNull final Consumer<? super Audience> action) {
            this.audience().forEachAudience(action);
        }

        @NotNull
        @Override
        default Pointers pointers() {
            return this.audience().pointers();
        }

        @Override
        default void sendMessage(@NotNull final Component message) {
            this.audience().sendMessage(message);
        }

        @Override
        default void sendMessage(@NotNull final Component message, @NotNull final ChatType.Bound boundChatType) {
            this.audience().sendMessage(message, boundChatType);
        }

        @Override
        default void sendMessage(@NotNull final SignedMessage signedMessage, @NotNull final ChatType.Bound boundChatType) {
            this.audience().sendMessage(signedMessage, boundChatType);
        }

        @Override
        default void deleteMessage(@NotNull final SignedMessage.Signature signature) {
            this.audience().deleteMessage(signature);
        }

        @Deprecated
        @Override
        default void sendMessage(@NotNull final Identified source, @NotNull final Component message, @NotNull final MessageType type) {
            this.audience().sendMessage(source, message, type);
        }

        @Deprecated
        @Override
        default void sendMessage(@NotNull final Identity source, @NotNull final Component message, @NotNull final MessageType type) {
            this.audience().sendMessage(source, message, type);
        }

        @Override
        default void sendActionBar(@NotNull final Component message) {
            this.audience().sendActionBar(message);
        }

        @Override
        default void sendPlayerListHeader(@NotNull final Component header) {
            this.audience().sendPlayerListHeader(header);
        }

        @Override
        default void sendPlayerListFooter(@NotNull final Component footer) {
            this.audience().sendPlayerListFooter(footer);
        }

        @Override
        default void sendPlayerListHeaderAndFooter(@NotNull final Component header, @NotNull final Component footer) {
            this.audience().sendPlayerListHeaderAndFooter(header, footer);
        }

        @Override
        default <T> void sendTitlePart(@NotNull final TitlePart<T> part, @NotNull final T value) {
            this.audience().sendTitlePart(part, value);
        }

        @Override
        default void clearTitle() {
            this.audience().clearTitle();
        }

        @Override
        default void resetTitle() {
            this.audience().resetTitle();
        }

        @Override
        default void showBossBar(@NotNull final BossBar bar) {
            this.audience().showBossBar(bar);
        }

        @Override
        default void hideBossBar(@NotNull final BossBar bar) {
            this.audience().hideBossBar(bar);
        }

        @Override
        default void playSound(@NotNull final Sound sound) {
            this.audience().playSound(sound);
        }

        @Override
        default void playSound(@NotNull final Sound sound, final double x, final double y, final double z) {
            this.audience().playSound(sound, x, y, z);
        }

        @Override
        default void playSound(@NotNull final Sound sound, @NotNull final Sound.Emitter emitter) {
            this.audience().playSound(sound, emitter);
        }

        @Override
        default void stopSound(@NotNull final SoundStop stop) {
            this.audience().stopSound(stop);
        }

        @Override
        default void openBook(@NotNull final Book book) {
            this.audience().openBook(book);
        }
    }
}