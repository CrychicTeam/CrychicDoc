package me.lucko.spark.lib.adventure.text.format;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEventSource;
import me.lucko.spark.lib.adventure.util.Buildable;
import me.lucko.spark.lib.adventure.util.MonkeyBars;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@NonExtendable
public interface Style extends Buildable<Style, Style.Builder>, Examinable, StyleGetter, StyleSetter<Style> {

    Key DEFAULT_FONT = Key.key("default");

    @NotNull
    static Style empty() {
        return StyleImpl.EMPTY;
    }

    @NotNull
    static Style.Builder style() {
        return new StyleImpl.BuilderImpl();
    }

    @NotNull
    static Style style(@NotNull final Consumer<Style.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(style(), consumer);
    }

    @NotNull
    static Style style(@Nullable final TextColor color) {
        return empty().color(color);
    }

    @NotNull
    static Style style(@NotNull final TextDecoration decoration) {
        return style().decoration(decoration, true).build();
    }

    @NotNull
    static Style style(@Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        Style.Builder builder = style();
        builder.color(color);
        builder.decorate(decorations);
        return builder.build();
    }

    @NotNull
    static Style style(@Nullable final TextColor color, final Set<TextDecoration> decorations) {
        Style.Builder builder = style();
        builder.color(color);
        if (!decorations.isEmpty()) {
            for (TextDecoration decoration : decorations) {
                builder.decoration(decoration, true);
            }
        }
        return builder.build();
    }

    @NotNull
    static Style style(@NotNull final StyleBuilderApplicable... applicables) {
        int length = applicables.length;
        if (length == 0) {
            return empty();
        } else {
            Style.Builder builder = style();
            for (int i = 0; i < length; i++) {
                StyleBuilderApplicable applicable = applicables[i];
                if (applicable != null) {
                    applicable.styleApply(builder);
                }
            }
            return builder.build();
        }
    }

    @NotNull
    static Style style(@NotNull final Iterable<? extends StyleBuilderApplicable> applicables) {
        Style.Builder builder = style();
        for (StyleBuilderApplicable applicable : applicables) {
            applicable.styleApply(builder);
        }
        return builder.build();
    }

    @NotNull
    default Style edit(@NotNull final Consumer<Style.Builder> consumer) {
        return this.edit(consumer, Style.Merge.Strategy.ALWAYS);
    }

    @NotNull
    default Style edit(@NotNull final Consumer<Style.Builder> consumer, @NotNull final Style.Merge.Strategy strategy) {
        return style((Consumer<Style.Builder>) (style -> {
            if (strategy == Style.Merge.Strategy.ALWAYS) {
                style.merge(this, strategy);
            }
            consumer.accept(style);
            if (strategy == Style.Merge.Strategy.IF_ABSENT_ON_TARGET) {
                style.merge(this, strategy);
            }
        }));
    }

    @Nullable
    @Override
    Key font();

    @NotNull
    Style font(@Nullable final Key font);

    @Nullable
    @Override
    TextColor color();

    @NotNull
    Style color(@Nullable final TextColor color);

    @NotNull
    Style colorIfAbsent(@Nullable final TextColor color);

    @Override
    default boolean hasDecoration(@NotNull final TextDecoration decoration) {
        return StyleGetter.super.hasDecoration(decoration);
    }

    @NotNull
    @Override
    TextDecoration.State decoration(@NotNull final TextDecoration decoration);

    @NotNull
    default Style decorate(@NotNull final TextDecoration decoration) {
        return (Style) StyleSetter.super.decorate(decoration);
    }

    @NotNull
    default Style decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return (Style) StyleSetter.super.decoration(decoration, flag);
    }

    @NotNull
    Style decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

    @NotNull
    Style decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

    @NotNull
    @Unmodifiable
    @Override
    default Map<TextDecoration, TextDecoration.State> decorations() {
        return StyleGetter.super.decorations();
    }

    @NotNull
    Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations);

    @Nullable
    @Override
    ClickEvent clickEvent();

    @NotNull
    Style clickEvent(@Nullable final ClickEvent event);

    @Nullable
    @Override
    HoverEvent<?> hoverEvent();

    @NotNull
    Style hoverEvent(@Nullable final HoverEventSource<?> source);

    @Nullable
    @Override
    String insertion();

    @NotNull
    Style insertion(@Nullable final String insertion);

    @NotNull
    default Style merge(@NotNull final Style that) {
        return this.merge(that, Style.Merge.all());
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy) {
        return this.merge(that, strategy, Style.Merge.all());
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Style.Merge merge) {
        return this.merge(that, Collections.singleton(merge));
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Style.Merge merge) {
        return this.merge(that, strategy, Collections.singleton(merge));
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Style.Merge... merges) {
        return this.merge(that, Style.Merge.merges(merges));
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Style.Merge... merges) {
        return this.merge(that, strategy, Style.Merge.merges(merges));
    }

    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Set<Style.Merge> merges) {
        return this.merge(that, Style.Merge.Strategy.ALWAYS, merges);
    }

    @NotNull
    Style merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Set<Style.Merge> merges);

    @NotNull
    Style unmerge(@NotNull final Style that);

    boolean isEmpty();

    @NotNull
    Style.Builder toBuilder();

    public interface Builder extends AbstractBuilder<Style>, Buildable.Builder<Style>, MutableStyleSetter<Style.Builder> {

        @Contract("_ -> this")
        @NotNull
        Style.Builder font(@Nullable final Key font);

        @Contract("_ -> this")
        @NotNull
        Style.Builder color(@Nullable final TextColor color);

        @Contract("_ -> this")
        @NotNull
        Style.Builder colorIfAbsent(@Nullable final TextColor color);

        @Contract("_ -> this")
        @NotNull
        default Style.Builder decorate(@NotNull final TextDecoration decoration) {
            return (Style.Builder) MutableStyleSetter.super.decorate(decoration);
        }

        @Contract("_ -> this")
        @NotNull
        default Style.Builder decorate(@NotNull final TextDecoration... decorations) {
            return (Style.Builder) MutableStyleSetter.super.decorate(decorations);
        }

        @Contract("_, _ -> this")
        @NotNull
        default Style.Builder decoration(@NotNull final TextDecoration decoration, final boolean flag) {
            return (Style.Builder) MutableStyleSetter.super.decoration(decoration, flag);
        }

        @Contract("_ -> this")
        @NotNull
        default Style.Builder decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
            return (Style.Builder) MutableStyleSetter.super.decorations(decorations);
        }

        @Contract("_, _ -> this")
        @NotNull
        Style.Builder decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

        @Contract("_, _ -> this")
        @NotNull
        Style.Builder decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

        @Contract("_ -> this")
        @NotNull
        Style.Builder clickEvent(@Nullable final ClickEvent event);

        @Contract("_ -> this")
        @NotNull
        Style.Builder hoverEvent(@Nullable final HoverEventSource<?> source);

        @Contract("_ -> this")
        @NotNull
        Style.Builder insertion(@Nullable final String insertion);

        @Contract("_ -> this")
        @NotNull
        default Style.Builder merge(@NotNull final Style that) {
            return this.merge(that, Style.Merge.all());
        }

        @Contract("_, _ -> this")
        @NotNull
        default Style.Builder merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy) {
            return this.merge(that, strategy, Style.Merge.all());
        }

        @Contract("_, _ -> this")
        @NotNull
        default Style.Builder merge(@NotNull final Style that, @NotNull final Style.Merge... merges) {
            return merges.length == 0 ? this : this.merge(that, Style.Merge.merges(merges));
        }

        @Contract("_, _, _ -> this")
        @NotNull
        default Style.Builder merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Style.Merge... merges) {
            return merges.length == 0 ? this : this.merge(that, strategy, Style.Merge.merges(merges));
        }

        @Contract("_, _ -> this")
        @NotNull
        default Style.Builder merge(@NotNull final Style that, @NotNull final Set<Style.Merge> merges) {
            return this.merge(that, Style.Merge.Strategy.ALWAYS, merges);
        }

        @Contract("_, _, _ -> this")
        @NotNull
        Style.Builder merge(@NotNull final Style that, @NotNull final Style.Merge.Strategy strategy, @NotNull final Set<Style.Merge> merges);

        @Contract("_ -> this")
        @NotNull
        default Style.Builder apply(@NotNull final StyleBuilderApplicable applicable) {
            applicable.styleApply(this);
            return this;
        }

        @NotNull
        Style build();
    }

    public static enum Merge {

        COLOR, DECORATIONS, EVENTS, INSERTION, FONT;

        static final Set<Style.Merge> ALL = merges(values());

        static final Set<Style.Merge> COLOR_AND_DECORATIONS = merges(COLOR, DECORATIONS);

        @NotNull
        @Unmodifiable
        public static Set<Style.Merge> all() {
            return ALL;
        }

        @NotNull
        @Unmodifiable
        public static Set<Style.Merge> colorAndDecorations() {
            return COLOR_AND_DECORATIONS;
        }

        @NotNull
        @Unmodifiable
        public static Set<Style.Merge> merges(@NotNull final Style.Merge... merges) {
            return MonkeyBars.enumSet(Style.Merge.class, merges);
        }

        @Deprecated
        @ScheduledForRemoval(inVersion = "5.0.0")
        @NotNull
        @Unmodifiable
        public static Set<Style.Merge> of(@NotNull final Style.Merge... merges) {
            return MonkeyBars.enumSet(Style.Merge.class, merges);
        }

        static boolean hasAll(@NotNull final Set<Style.Merge> merges) {
            return merges.size() == ALL.size();
        }

        public static enum Strategy {

            ALWAYS, NEVER, IF_ABSENT_ON_TARGET
        }
    }
}