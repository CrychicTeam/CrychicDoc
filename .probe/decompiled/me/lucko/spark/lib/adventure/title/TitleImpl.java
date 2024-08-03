package me.lucko.spark.lib.adventure.title;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

final class TitleImpl implements Title {

    private final Component title;

    private final Component subtitle;

    @Nullable
    private final Title.Times times;

    TitleImpl(@NotNull final Component title, @NotNull final Component subtitle, @Nullable final Title.Times times) {
        this.title = (Component) Objects.requireNonNull(title, "title");
        this.subtitle = (Component) Objects.requireNonNull(subtitle, "subtitle");
        this.times = times;
    }

    @NotNull
    @Override
    public Component title() {
        return this.title;
    }

    @NotNull
    @Override
    public Component subtitle() {
        return this.subtitle;
    }

    @Nullable
    @Override
    public Title.Times times() {
        return this.times;
    }

    @UnknownNullability
    @Override
    public <T> T part(@NotNull final TitlePart<T> part) {
        Objects.requireNonNull(part, "part");
        if (part == TitlePart.TITLE) {
            return (T) this.title;
        } else if (part == TitlePart.SUBTITLE) {
            return (T) this.subtitle;
        } else if (part == TitlePart.TIMES) {
            return (T) this.times;
        } else {
            throw new IllegalArgumentException("Don't know what " + part + " is.");
        }
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            TitleImpl that = (TitleImpl) other;
            return this.title.equals(that.title) && this.subtitle.equals(that.subtitle) && Objects.equals(this.times, that.times);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.title.hashCode();
        result = 31 * result + this.subtitle.hashCode();
        return 31 * result + Objects.hashCode(this.times);
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("title", this.title), ExaminableProperty.of("subtitle", this.subtitle), ExaminableProperty.of("times", this.times));
    }

    public String toString() {
        return Internals.toString(this);
    }

    static class TimesImpl implements Title.Times {

        private final Duration fadeIn;

        private final Duration stay;

        private final Duration fadeOut;

        TimesImpl(@NotNull final Duration fadeIn, @NotNull final Duration stay, @NotNull final Duration fadeOut) {
            this.fadeIn = (Duration) Objects.requireNonNull(fadeIn, "fadeIn");
            this.stay = (Duration) Objects.requireNonNull(stay, "stay");
            this.fadeOut = (Duration) Objects.requireNonNull(fadeOut, "fadeOut");
        }

        @NotNull
        @Override
        public Duration fadeIn() {
            return this.fadeIn;
        }

        @NotNull
        @Override
        public Duration stay() {
            return this.stay;
        }

        @NotNull
        @Override
        public Duration fadeOut() {
            return this.fadeOut;
        }

        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof TitleImpl.TimesImpl)) {
                return false;
            } else {
                TitleImpl.TimesImpl that = (TitleImpl.TimesImpl) other;
                return this.fadeIn.equals(that.fadeIn) && this.stay.equals(that.stay) && this.fadeOut.equals(that.fadeOut);
            }
        }

        public int hashCode() {
            int result = this.fadeIn.hashCode();
            result = 31 * result + this.stay.hashCode();
            return 31 * result + this.fadeOut.hashCode();
        }

        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(ExaminableProperty.of("fadeIn", this.fadeIn), ExaminableProperty.of("stay", this.stay), ExaminableProperty.of("fadeOut", this.fadeOut));
        }

        public String toString() {
            return Internals.toString(this);
        }
    }
}