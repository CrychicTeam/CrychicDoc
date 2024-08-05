package me.lucko.spark.lib.adventure.bossbar;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.util.Services;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

final class BossBarImpl extends HackyBossBarPlatformBridge implements BossBar {

    private final List<BossBar.Listener> listeners = new CopyOnWriteArrayList();

    private Component name;

    private float progress;

    private BossBar.Color color;

    private BossBar.Overlay overlay;

    private final Set<BossBar.Flag> flags = EnumSet.noneOf(BossBar.Flag.class);

    @Nullable
    BossBarImplementation implementation;

    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay) {
        this.name = (Component) Objects.requireNonNull(name, "name");
        this.progress = progress;
        this.color = (BossBar.Color) Objects.requireNonNull(color, "color");
        this.overlay = (BossBar.Overlay) Objects.requireNonNull(overlay, "overlay");
    }

    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay, @NotNull final Set<BossBar.Flag> flags) {
        this(name, progress, color, overlay);
        this.flags.addAll(flags);
    }

    @NotNull
    @Override
    public Component name() {
        return this.name;
    }

    @NotNull
    @Override
    public BossBar name(@NotNull final Component newName) {
        Objects.requireNonNull(newName, "name");
        Component oldName = this.name;
        if (!Objects.equals(newName, oldName)) {
            this.name = newName;
            this.forEachListener(listener -> listener.bossBarNameChanged(this, oldName, newName));
        }
        return this;
    }

    @Override
    public float progress() {
        return this.progress;
    }

    @NotNull
    @Override
    public BossBar progress(final float newProgress) {
        checkProgress(newProgress);
        float oldProgress = this.progress;
        if (newProgress != oldProgress) {
            this.progress = newProgress;
            this.forEachListener(listener -> listener.bossBarProgressChanged(this, oldProgress, newProgress));
        }
        return this;
    }

    static void checkProgress(final float progress) {
        if (progress < 0.0F || progress > 1.0F) {
            throw new IllegalArgumentException("progress must be between 0.0 and 1.0, was " + progress);
        }
    }

    @NotNull
    @Override
    public BossBar.Color color() {
        return this.color;
    }

    @NotNull
    @Override
    public BossBar color(@NotNull final BossBar.Color newColor) {
        Objects.requireNonNull(newColor, "color");
        BossBar.Color oldColor = this.color;
        if (newColor != oldColor) {
            this.color = newColor;
            this.forEachListener(listener -> listener.bossBarColorChanged(this, oldColor, newColor));
        }
        return this;
    }

    @NotNull
    @Override
    public BossBar.Overlay overlay() {
        return this.overlay;
    }

    @NotNull
    @Override
    public BossBar overlay(@NotNull final BossBar.Overlay newOverlay) {
        Objects.requireNonNull(newOverlay, "overlay");
        BossBar.Overlay oldOverlay = this.overlay;
        if (newOverlay != oldOverlay) {
            this.overlay = newOverlay;
            this.forEachListener(listener -> listener.bossBarOverlayChanged(this, oldOverlay, newOverlay));
        }
        return this;
    }

    @NotNull
    @Override
    public Set<BossBar.Flag> flags() {
        return Collections.unmodifiableSet(this.flags);
    }

    @NotNull
    @Override
    public BossBar flags(@NotNull final Set<BossBar.Flag> newFlags) {
        if (newFlags.isEmpty()) {
            Set<BossBar.Flag> oldFlags = EnumSet.copyOf(this.flags);
            this.flags.clear();
            this.forEachListener(listener -> listener.bossBarFlagsChanged(this, Collections.emptySet(), oldFlags));
        } else if (!this.flags.equals(newFlags)) {
            Set<BossBar.Flag> oldFlags = EnumSet.copyOf(this.flags);
            this.flags.clear();
            this.flags.addAll(newFlags);
            Set<BossBar.Flag> added = EnumSet.copyOf(newFlags);
            added.removeIf(oldFlags::contains);
            Set<BossBar.Flag> removed = EnumSet.copyOf(oldFlags);
            removed.removeIf(this.flags::contains);
            this.forEachListener(listener -> listener.bossBarFlagsChanged(this, added, removed));
        }
        return this;
    }

    @Override
    public boolean hasFlag(@NotNull final BossBar.Flag flag) {
        return this.flags.contains(flag);
    }

    @NotNull
    @Override
    public BossBar addFlag(@NotNull final BossBar.Flag flag) {
        return this.editFlags(flag, Set::add, BossBarImpl::onFlagsAdded);
    }

    @NotNull
    @Override
    public BossBar removeFlag(@NotNull final BossBar.Flag flag) {
        return this.editFlags(flag, Set::remove, BossBarImpl::onFlagsRemoved);
    }

    @NotNull
    private BossBar editFlags(@NotNull final BossBar.Flag flag, @NotNull final BiPredicate<Set<BossBar.Flag>, BossBar.Flag> predicate, final BiConsumer<BossBarImpl, Set<BossBar.Flag>> onChange) {
        if (predicate.test(this.flags, flag)) {
            onChange.accept(this, Collections.singleton(flag));
        }
        return this;
    }

    @NotNull
    @Override
    public BossBar addFlags(@NotNull final BossBar.Flag... flags) {
        return this.editFlags(flags, Set::add, BossBarImpl::onFlagsAdded);
    }

    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final BossBar.Flag... flags) {
        return this.editFlags(flags, Set::remove, BossBarImpl::onFlagsRemoved);
    }

    @NotNull
    private BossBar editFlags(final BossBar.Flag[] flags, final BiPredicate<Set<BossBar.Flag>, BossBar.Flag> predicate, final BiConsumer<BossBarImpl, Set<BossBar.Flag>> onChange) {
        if (flags.length == 0) {
            return this;
        } else {
            Set<BossBar.Flag> changes = null;
            int i = 0;
            for (int length = flags.length; i < length; i++) {
                if (predicate.test(this.flags, flags[i])) {
                    if (changes == null) {
                        changes = EnumSet.noneOf(BossBar.Flag.class);
                    }
                    changes.add(flags[i]);
                }
            }
            if (changes != null) {
                onChange.accept(this, changes);
            }
            return this;
        }
    }

    @NotNull
    @Override
    public BossBar addFlags(@NotNull final Iterable<BossBar.Flag> flags) {
        return this.editFlags(flags, Set::add, BossBarImpl::onFlagsAdded);
    }

    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final Iterable<BossBar.Flag> flags) {
        return this.editFlags(flags, Set::remove, BossBarImpl::onFlagsRemoved);
    }

    @NotNull
    private BossBar editFlags(final Iterable<BossBar.Flag> flags, final BiPredicate<Set<BossBar.Flag>, BossBar.Flag> predicate, final BiConsumer<BossBarImpl, Set<BossBar.Flag>> onChange) {
        Set<BossBar.Flag> changes = null;
        for (BossBar.Flag flag : flags) {
            if (predicate.test(this.flags, flag)) {
                if (changes == null) {
                    changes = EnumSet.noneOf(BossBar.Flag.class);
                }
                changes.add(flag);
            }
        }
        if (changes != null) {
            onChange.accept(this, changes);
        }
        return this;
    }

    @NotNull
    @Override
    public BossBar addListener(@NotNull final BossBar.Listener listener) {
        this.listeners.add(listener);
        return this;
    }

    @NotNull
    @Override
    public BossBar removeListener(@NotNull final BossBar.Listener listener) {
        this.listeners.remove(listener);
        return this;
    }

    private void forEachListener(@NotNull final Consumer<BossBar.Listener> consumer) {
        for (BossBar.Listener listener : this.listeners) {
            consumer.accept(listener);
        }
    }

    private static void onFlagsAdded(final BossBarImpl bar, final Set<BossBar.Flag> flagsAdded) {
        bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, flagsAdded, Collections.emptySet()));
    }

    private static void onFlagsRemoved(final BossBarImpl bar, final Set<BossBar.Flag> flagsRemoved) {
        bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, Collections.emptySet(), flagsRemoved));
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("name", this.name), ExaminableProperty.of("progress", this.progress), ExaminableProperty.of("color", this.color), ExaminableProperty.of("overlay", this.overlay), ExaminableProperty.of("flags", this.flags));
    }

    public String toString() {
        return Internals.toString(this);
    }

    @Internal
    static final class ImplementationAccessor {

        private static final Optional<BossBarImplementation.Provider> SERVICE = Services.service(BossBarImplementation.Provider.class);

        private ImplementationAccessor() {
        }

        @NotNull
        static <I extends BossBarImplementation> I get(@NotNull final BossBar bar, @NotNull final Class<I> type) {
            BossBarImplementation implementation = ((BossBarImpl) bar).implementation;
            if (implementation == null) {
                implementation = ((BossBarImplementation.Provider) SERVICE.get()).create(bar);
                ((BossBarImpl) bar).implementation = implementation;
            }
            return (I) type.cast(implementation);
        }
    }
}