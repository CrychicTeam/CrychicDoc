package me.lucko.spark.lib.adventure.sound;

import java.util.Objects;
import java.util.function.Supplier;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface SoundStop extends Examinable {

    @NotNull
    static SoundStop all() {
        return SoundStopImpl.ALL;
    }

    @NotNull
    static SoundStop named(@NotNull final Key sound) {
        Objects.requireNonNull(sound, "sound");
        return new SoundStopImpl(null) {

            @NotNull
            @Override
            public Key sound() {
                return sound;
            }
        };
    }

    @NotNull
    static SoundStop named(@NotNull final Sound.Type sound) {
        Objects.requireNonNull(sound, "sound");
        return new SoundStopImpl(null) {

            @NotNull
            @Override
            public Key sound() {
                return sound.key();
            }
        };
    }

    @NotNull
    static SoundStop named(@NotNull final Supplier<? extends Sound.Type> sound) {
        Objects.requireNonNull(sound, "sound");
        return new SoundStopImpl(null) {

            @NotNull
            @Override
            public Key sound() {
                return ((Sound.Type) sound.get()).key();
            }
        };
    }

    @NotNull
    static SoundStop source(@NotNull final Sound.Source source) {
        Objects.requireNonNull(source, "source");
        return new SoundStopImpl(source) {

            @Nullable
            @Override
            public Key sound() {
                return null;
            }
        };
    }

    @NotNull
    static SoundStop namedOnSource(@NotNull final Key sound, @NotNull final Sound.Source source) {
        Objects.requireNonNull(sound, "sound");
        Objects.requireNonNull(source, "source");
        return new SoundStopImpl(source) {

            @NotNull
            @Override
            public Key sound() {
                return sound;
            }
        };
    }

    @NotNull
    static SoundStop namedOnSource(@NotNull final Sound.Type sound, @NotNull final Sound.Source source) {
        Objects.requireNonNull(sound, "sound");
        return namedOnSource(sound.key(), source);
    }

    @NotNull
    static SoundStop namedOnSource(@NotNull final Supplier<? extends Sound.Type> sound, @NotNull final Sound.Source source) {
        Objects.requireNonNull(sound, "sound");
        Objects.requireNonNull(source, "source");
        return new SoundStopImpl(source) {

            @NotNull
            @Override
            public Key sound() {
                return ((Sound.Type) sound.get()).key();
            }
        };
    }

    @Nullable
    Key sound();

    @Nullable
    Sound.Source source();
}