package me.lucko.spark.lib.adventure.bossbar;

import java.util.Set;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.ComponentLike;
import me.lucko.spark.lib.adventure.util.Index;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@NonExtendable
public interface BossBar extends Examinable {

    float MIN_PROGRESS = 0.0F;

    float MAX_PROGRESS = 1.0F;

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    float MIN_PERCENT = 0.0F;

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    float MAX_PERCENT = 1.0F;

    @NotNull
    static BossBar bossBar(@NotNull final ComponentLike name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay) {
        BossBarImpl.checkProgress(progress);
        return bossBar(name.asComponent(), progress, color, overlay);
    }

    @NotNull
    static BossBar bossBar(@NotNull final Component name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay) {
        BossBarImpl.checkProgress(progress);
        return new BossBarImpl(name, progress, color, overlay);
    }

    @NotNull
    static BossBar bossBar(@NotNull final ComponentLike name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay, @NotNull final Set<BossBar.Flag> flags) {
        BossBarImpl.checkProgress(progress);
        return bossBar(name.asComponent(), progress, color, overlay, flags);
    }

    @NotNull
    static BossBar bossBar(@NotNull final Component name, final float progress, @NotNull final BossBar.Color color, @NotNull final BossBar.Overlay overlay, @NotNull final Set<BossBar.Flag> flags) {
        BossBarImpl.checkProgress(progress);
        return new BossBarImpl(name, progress, color, overlay, flags);
    }

    @NotNull
    Component name();

    @Contract("_ -> this")
    @NotNull
    default BossBar name(@NotNull final ComponentLike name) {
        return this.name(name.asComponent());
    }

    @Contract("_ -> this")
    @NotNull
    BossBar name(@NotNull final Component name);

    float progress();

    @Contract("_ -> this")
    @NotNull
    BossBar progress(final float progress);

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    default float percent() {
        return this.progress();
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract("_ -> this")
    @NotNull
    default BossBar percent(final float progress) {
        return this.progress(progress);
    }

    @NotNull
    BossBar.Color color();

    @Contract("_ -> this")
    @NotNull
    BossBar color(@NotNull final BossBar.Color color);

    @NotNull
    BossBar.Overlay overlay();

    @Contract("_ -> this")
    @NotNull
    BossBar overlay(@NotNull final BossBar.Overlay overlay);

    @NotNull
    @UnmodifiableView
    Set<BossBar.Flag> flags();

    @Contract("_ -> this")
    @NotNull
    BossBar flags(@NotNull final Set<BossBar.Flag> flags);

    boolean hasFlag(@NotNull final BossBar.Flag flag);

    @Contract("_ -> this")
    @NotNull
    BossBar addFlag(@NotNull final BossBar.Flag flag);

    @Contract("_ -> this")
    @NotNull
    BossBar removeFlag(@NotNull final BossBar.Flag flag);

    @Contract("_ -> this")
    @NotNull
    BossBar addFlags(@NotNull final BossBar.Flag... flags);

    @Contract("_ -> this")
    @NotNull
    BossBar removeFlags(@NotNull final BossBar.Flag... flags);

    @Contract("_ -> this")
    @NotNull
    BossBar addFlags(@NotNull final Iterable<BossBar.Flag> flags);

    @Contract("_ -> this")
    @NotNull
    BossBar removeFlags(@NotNull final Iterable<BossBar.Flag> flags);

    @Contract("_ -> this")
    @NotNull
    BossBar addListener(@NotNull final BossBar.Listener listener);

    @Contract("_ -> this")
    @NotNull
    BossBar removeListener(@NotNull final BossBar.Listener listener);

    public static enum Color {

        PINK("pink"),
        BLUE("blue"),
        RED("red"),
        GREEN("green"),
        YELLOW("yellow"),
        PURPLE("purple"),
        WHITE("white");

        public static final Index<String, BossBar.Color> NAMES = Index.create(BossBar.Color.class, color -> color.name);

        private final String name;

        private Color(final String name) {
            this.name = name;
        }
    }

    public static enum Flag {

        DARKEN_SCREEN("darken_screen"), PLAY_BOSS_MUSIC("play_boss_music"), CREATE_WORLD_FOG("create_world_fog");

        public static final Index<String, BossBar.Flag> NAMES = Index.create(BossBar.Flag.class, flag -> flag.name);

        private final String name;

        private Flag(final String name) {
            this.name = name;
        }
    }

    @OverrideOnly
    public interface Listener {

        default void bossBarNameChanged(@NotNull final BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
        }

        default void bossBarProgressChanged(@NotNull final BossBar bar, final float oldProgress, final float newProgress) {
            this.bossBarPercentChanged(bar, oldProgress, newProgress);
        }

        @Deprecated
        @ScheduledForRemoval(inVersion = "5.0.0")
        default void bossBarPercentChanged(@NotNull final BossBar bar, final float oldProgress, final float newProgress) {
        }

        default void bossBarColorChanged(@NotNull final BossBar bar, @NotNull final BossBar.Color oldColor, @NotNull final BossBar.Color newColor) {
        }

        default void bossBarOverlayChanged(@NotNull final BossBar bar, @NotNull final BossBar.Overlay oldOverlay, @NotNull final BossBar.Overlay newOverlay) {
        }

        default void bossBarFlagsChanged(@NotNull final BossBar bar, @NotNull final Set<BossBar.Flag> flagsAdded, @NotNull final Set<BossBar.Flag> flagsRemoved) {
        }
    }

    public static enum Overlay {

        PROGRESS("progress"), NOTCHED_6("notched_6"), NOTCHED_10("notched_10"), NOTCHED_12("notched_12"), NOTCHED_20("notched_20");

        public static final Index<String, BossBar.Overlay> NAMES = Index.create(BossBar.Overlay.class, overlay -> overlay.name);

        private final String name;

        private Overlay(final String name) {
            this.name = name;
        }
    }
}