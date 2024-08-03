package me.lucko.spark.lib.adventure.sound;

import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class SoundStopImpl implements SoundStop {

    static final SoundStop ALL = new SoundStopImpl(null) {

        @Nullable
        @Override
        public Key sound() {
            return null;
        }
    };

    @Nullable
    private final Sound.Source source;

    SoundStopImpl(@Nullable final Sound.Source source) {
        this.source = source;
    }

    @Nullable
    @Override
    public Sound.Source source() {
        return this.source;
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof SoundStopImpl)) {
            return false;
        } else {
            SoundStopImpl that = (SoundStopImpl) other;
            return Objects.equals(this.sound(), that.sound()) && Objects.equals(this.source, that.source);
        }
    }

    public int hashCode() {
        int result = Objects.hashCode(this.sound());
        return 31 * result + Objects.hashCode(this.source);
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("name", this.sound()), ExaminableProperty.of("source", this.source));
    }

    public String toString() {
        return Internals.toString(this);
    }
}