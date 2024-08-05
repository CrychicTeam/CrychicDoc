package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEventSource;
import me.lucko.spark.lib.adventure.text.format.MutableStyleSetter;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import me.lucko.spark.lib.adventure.util.Buildable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface ComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> extends AbstractBuilder<C>, Buildable.Builder<C>, ComponentBuilderApplicable, ComponentLike, MutableStyleSetter<B> {

    @Contract("_ -> this")
    @NotNull
    B append(@NotNull final Component component);

    @Contract("_ -> this")
    @NotNull
    default B append(@NotNull final ComponentLike component) {
        return this.append(component.asComponent());
    }

    @Contract("_ -> this")
    @NotNull
    default B append(@NotNull final ComponentBuilder<?, ?> builder) {
        return this.append((Component) builder.build());
    }

    @Contract("_ -> this")
    @NotNull
    B append(@NotNull final Component... components);

    @Contract("_ -> this")
    @NotNull
    B append(@NotNull final ComponentLike... components);

    @Contract("_ -> this")
    @NotNull
    B append(@NotNull final Iterable<? extends ComponentLike> components);

    @NotNull
    default B appendNewline() {
        return this.append((Component) Component.newline());
    }

    @NotNull
    default B appendSpace() {
        return this.append((Component) Component.space());
    }

    @Contract("_ -> this")
    @NotNull
    default B apply(@NotNull final Consumer<? super ComponentBuilder<?, ?>> consumer) {
        consumer.accept(this);
        return (B) this;
    }

    @Contract("_ -> this")
    @NotNull
    B applyDeep(@NotNull final Consumer<? super ComponentBuilder<?, ?>> action);

    @Contract("_ -> this")
    @NotNull
    B mapChildren(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

    @Contract("_ -> this")
    @NotNull
    B mapChildrenDeep(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function);

    @NotNull
    List<Component> children();

    @Contract("_ -> this")
    @NotNull
    B style(@NotNull final Style style);

    @Contract("_ -> this")
    @NotNull
    B style(@NotNull final Consumer<Style.Builder> consumer);

    @Contract("_ -> this")
    @NotNull
    B font(@Nullable final Key font);

    @Contract("_ -> this")
    @NotNull
    B color(@Nullable final TextColor color);

    @Contract("_ -> this")
    @NotNull
    B colorIfAbsent(@Nullable final TextColor color);

    @Contract("_, _ -> this")
    @NotNull
    default B decorations(@NotNull final Set<TextDecoration> decorations, final boolean flag) {
        return (B) MutableStyleSetter.super.decorations(decorations, flag);
    }

    @Contract("_ -> this")
    @NotNull
    default B decorate(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration, TextDecoration.State.TRUE);
    }

    @Contract("_ -> this")
    @NotNull
    default B decorate(@NotNull final TextDecoration... decorations) {
        return (B) MutableStyleSetter.super.decorate(decorations);
    }

    @Contract("_, _ -> this")
    @NotNull
    default B decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }

    @Contract("_ -> this")
    @NotNull
    default B decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return (B) MutableStyleSetter.super.decorations(decorations);
    }

    @Contract("_, _ -> this")
    @NotNull
    B decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

    @Contract("_, _ -> this")
    @NotNull
    B decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

    @Contract("_ -> this")
    @NotNull
    B clickEvent(@Nullable final ClickEvent event);

    @Contract("_ -> this")
    @NotNull
    B hoverEvent(@Nullable final HoverEventSource<?> source);

    @Contract("_ -> this")
    @NotNull
    B insertion(@Nullable final String insertion);

    @Contract("_ -> this")
    @NotNull
    default B mergeStyle(@NotNull final Component that) {
        return this.mergeStyle(that, Style.Merge.all());
    }

    @Contract("_, _ -> this")
    @NotNull
    default B mergeStyle(@NotNull final Component that, @NotNull final Style.Merge... merges) {
        return this.mergeStyle(that, Style.Merge.merges(merges));
    }

    @Contract("_, _ -> this")
    @NotNull
    B mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges);

    @NotNull
    B resetStyle();

    @NotNull
    C build();

    @Contract("_ -> this")
    @NotNull
    default B applicableApply(@NotNull final ComponentBuilderApplicable applicable) {
        applicable.componentBuilderApply(this);
        return (B) this;
    }

    @Override
    default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
        component.append(this);
    }

    @NotNull
    @Override
    default Component asComponent() {
        return this.build();
    }
}