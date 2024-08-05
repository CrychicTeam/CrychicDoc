package me.lucko.spark.lib.adventure.sound;

import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Supplier;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.util.ShadyPines;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

abstract class SoundImpl implements Sound {

    static final Sound.Emitter EMITTER_SELF = new Sound.Emitter() {

        public String toString() {
            return "SelfSoundEmitter";
        }
    };

    private final Sound.Source source;

    private final float volume;

    private final float pitch;

    private final OptionalLong seed;

    private SoundStop stop;

    SoundImpl(@NotNull final Sound.Source source, final float volume, final float pitch, final OptionalLong seed) {
        this.source = source;
        this.volume = volume;
        this.pitch = pitch;
        this.seed = seed;
    }

    @NotNull
    @Override
    public Sound.Source source() {
        return this.source;
    }

    @Override
    public float volume() {
        return this.volume;
    }

    @Override
    public float pitch() {
        return this.pitch;
    }

    @Override
    public OptionalLong seed() {
        return this.seed;
    }

    @NotNull
    @Override
    public SoundStop asStop() {
        if (this.stop == null) {
            this.stop = SoundStop.namedOnSource(this.name(), this.source());
        }
        return this.stop;
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof SoundImpl)) {
            return false;
        } else {
            SoundImpl that = (SoundImpl) other;
            return this.name().equals(that.name()) && this.source == that.source && ShadyPines.equals(this.volume, that.volume) && ShadyPines.equals(this.pitch, that.pitch) && this.seed.equals(that.seed);
        }
    }

    public int hashCode() {
        int result = this.name().hashCode();
        result = 31 * result + this.source.hashCode();
        result = 31 * result + Float.hashCode(this.volume);
        result = 31 * result + Float.hashCode(this.pitch);
        return 31 * result + this.seed.hashCode();
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("name", this.name()), ExaminableProperty.of("source", this.source), ExaminableProperty.of("volume", this.volume), ExaminableProperty.of("pitch", this.pitch), ExaminableProperty.of("seed", this.seed));
    }

    public String toString() {
        return Internals.toString(this);
    }

    static final class BuilderImpl implements Sound.Builder {

        private static final float DEFAULT_VOLUME = 1.0F;

        private static final float DEFAULT_PITCH = 1.0F;

        private Key eagerType;

        private Supplier<? extends Sound.Type> lazyType;

        private Sound.Source source = Sound.Source.MASTER;

        private float volume = 1.0F;

        private float pitch = 1.0F;

        private OptionalLong seed = OptionalLong.empty();

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final Sound existing) {
            if (existing instanceof SoundImpl.Eager) {
                this.type(((SoundImpl.Eager) existing).name);
            } else {
                if (!(existing instanceof SoundImpl.Lazy)) {
                    throw new IllegalArgumentException("Unknown sound type " + existing + ", must be Eager or Lazy");
                }
                this.type(((SoundImpl.Lazy) existing).supplier);
            }
            this.source(existing.source()).volume(existing.volume()).pitch(existing.pitch()).seed(existing.seed());
        }

        @NotNull
        @Override
        public Sound.Builder type(@NotNull final Key type) {
            this.eagerType = (Key) Objects.requireNonNull(type, "type");
            this.lazyType = null;
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder type(@NotNull final Sound.Type type) {
            this.eagerType = (Key) Objects.requireNonNull(((Sound.Type) Objects.requireNonNull(type, "type")).key(), "type.key()");
            this.lazyType = null;
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder type(@NotNull final Supplier<? extends Sound.Type> typeSupplier) {
            this.lazyType = (Supplier<? extends Sound.Type>) Objects.requireNonNull(typeSupplier, "typeSupplier");
            this.eagerType = null;
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder source(@NotNull final Sound.Source source) {
            this.source = (Sound.Source) Objects.requireNonNull(source, "source");
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder source(@NotNull final Sound.Source.Provider source) {
            return this.source(source.soundSource());
        }

        @NotNull
        @Override
        public Sound.Builder volume(@Range(from = 0L, to = 2147483647L) final float volume) {
            this.volume = volume;
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder pitch(@Range(from = -1L, to = 1L) final float pitch) {
            this.pitch = pitch;
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder seed(final long seed) {
            this.seed = OptionalLong.of(seed);
            return this;
        }

        @NotNull
        @Override
        public Sound.Builder seed(@NotNull final OptionalLong seed) {
            this.seed = (OptionalLong) Objects.requireNonNull(seed, "seed");
            return this;
        }

        @NotNull
        public Sound build() {
            if (this.eagerType != null) {
                return new SoundImpl.Eager(this.eagerType, this.source, this.volume, this.pitch, this.seed);
            } else if (this.lazyType != null) {
                return new SoundImpl.Lazy(this.lazyType, this.source, this.volume, this.pitch, this.seed);
            } else {
                throw new IllegalStateException("A sound type must be provided to build a sound");
            }
        }
    }

    static final class Eager extends SoundImpl {

        final Key name;

        Eager(@NotNull final Key name, @NotNull final Sound.Source source, final float volume, final float pitch, final OptionalLong seed) {
            super(source, volume, pitch, seed);
            this.name = name;
        }

        @NotNull
        @Override
        public Key name() {
            return this.name;
        }
    }

    static final class Lazy extends SoundImpl {

        final Supplier<? extends Sound.Type> supplier;

        Lazy(@NotNull final Supplier<? extends Sound.Type> supplier, @NotNull final Sound.Source source, final float volume, final float pitch, final OptionalLong seed) {
            super(source, volume, pitch, seed);
            this.supplier = supplier;
        }

        @NotNull
        @Override
        public Key name() {
            return ((Sound.Type) this.supplier.get()).key();
        }
    }
}