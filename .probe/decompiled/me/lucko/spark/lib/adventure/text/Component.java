package me.lucko.spark.lib.adventure.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.builder.AbstractBuilder;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.event.ClickEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEvent;
import me.lucko.spark.lib.adventure.text.event.HoverEventSource;
import me.lucko.spark.lib.adventure.text.format.Style;
import me.lucko.spark.lib.adventure.text.format.StyleBuilderApplicable;
import me.lucko.spark.lib.adventure.text.format.StyleGetter;
import me.lucko.spark.lib.adventure.text.format.StyleSetter;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;
import me.lucko.spark.lib.adventure.translation.Translatable;
import me.lucko.spark.lib.adventure.util.ForwardingIterator;
import me.lucko.spark.lib.adventure.util.IntFunction2;
import me.lucko.spark.lib.adventure.util.MonkeyBars;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

@NonExtendable
public interface Component extends ComponentBuilderApplicable, ComponentLike, Examinable, HoverEventSource<Component>, StyleGetter, StyleSetter<Component> {

    BiPredicate<? super Component, ? super Component> EQUALS = Objects::equals;

    BiPredicate<? super Component, ? super Component> EQUALS_IDENTITY = (a, b) -> a == b;

    Predicate<? super Component> IS_NOT_EMPTY = component -> component != empty();

    @NotNull
    static TextComponent empty() {
        return TextComponentImpl.EMPTY;
    }

    @NotNull
    static TextComponent newline() {
        return TextComponentImpl.NEWLINE;
    }

    @NotNull
    static TextComponent space() {
        return TextComponentImpl.SPACE;
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent join(@NotNull final ComponentLike separator, @NotNull final ComponentLike... components) {
        return join(separator, Arrays.asList(components));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent join(@NotNull final ComponentLike separator, final Iterable<? extends ComponentLike> components) {
        Component component = join(JoinConfiguration.separator(separator), components);
        return component instanceof TextComponent ? (TextComponent) component : text().append(component).build();
    }

    @Contract(pure = true)
    @NotNull
    static Component join(@NotNull final JoinConfiguration config, @NotNull final ComponentLike... components) {
        return join(config, Arrays.asList(components));
    }

    @Contract(pure = true)
    @NotNull
    static Component join(@NotNull final JoinConfiguration config, @NotNull final Iterable<? extends ComponentLike> components) {
        return JoinConfigurationImpl.join(config, components);
    }

    @NotNull
    static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent() {
        return toComponent(empty());
    }

    @NotNull
    static Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent(@NotNull final Component separator) {
        return Collector.of(Component::text, (builder, add) -> {
            if (separator != empty() && !builder.children().isEmpty()) {
                builder.append(separator);
            }
            builder.append(add);
        }, (a, b) -> {
            List<Component> aChildren = a.children();
            TextComponent.Builder ret = text().append(aChildren);
            if (!aChildren.isEmpty()) {
                ret.append(separator);
            }
            ret.append(b.children());
            return ret;
        }, ComponentBuilder::build);
    }

    @Contract(pure = true)
    @NotNull
    static BlockNBTComponent.Builder blockNBT() {
        return new BlockNBTComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static BlockNBTComponent blockNBT(@NotNull final Consumer<? super BlockNBTComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(blockNBT(), consumer);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static BlockNBTComponent blockNBT(@NotNull final String nbtPath, @NotNull final BlockNBTComponent.Pos pos) {
        return blockNBT(nbtPath, false, pos);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static BlockNBTComponent blockNBT(@NotNull final String nbtPath, final boolean interpret, @NotNull final BlockNBTComponent.Pos pos) {
        return blockNBT(nbtPath, interpret, null, pos);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static BlockNBTComponent blockNBT(@NotNull final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final BlockNBTComponent.Pos pos) {
        return BlockNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, pos);
    }

    @Contract(pure = true)
    @NotNull
    static EntityNBTComponent.Builder entityNBT() {
        return new EntityNBTComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static EntityNBTComponent entityNBT(@NotNull final Consumer<? super EntityNBTComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(entityNBT(), consumer);
    }

    @Contract("_, _ -> new")
    @NotNull
    static EntityNBTComponent entityNBT(@NotNull final String nbtPath, @NotNull final String selector) {
        return entityNBT().nbtPath(nbtPath).selector(selector).build();
    }

    @Contract(pure = true)
    @NotNull
    static KeybindComponent.Builder keybind() {
        return new KeybindComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static KeybindComponent keybind(@NotNull final Consumer<? super KeybindComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(keybind(), consumer);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final String keybind) {
        return keybind(keybind, Style.empty());
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind) {
        return keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.empty());
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final String keybind, @NotNull final Style style) {
        return KeybindComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), keybind);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind, @NotNull final Style style) {
        return KeybindComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), ((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind());
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color) {
        return keybind(keybind, Style.style(color));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color) {
        return keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return keybind(keybind, Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return keybind(keybind, Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static KeybindComponent keybind(@NotNull final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return keybind(((KeybindComponent.KeybindLike) Objects.requireNonNull(keybind, "keybind")).asKeybind(), Style.style(color, decorations));
    }

    @Contract(pure = true)
    @NotNull
    static ScoreComponent.Builder score() {
        return new ScoreComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static ScoreComponent score(@NotNull final Consumer<? super ScoreComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(score(), consumer);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static ScoreComponent score(@NotNull final String name, @NotNull final String objective) {
        return score(name, objective, null);
    }

    @Deprecated
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static ScoreComponent score(@NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        return ScoreComponentImpl.create(Collections.emptyList(), Style.empty(), name, objective, value);
    }

    @Contract(pure = true)
    @NotNull
    static SelectorComponent.Builder selector() {
        return new SelectorComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static SelectorComponent selector(@NotNull final Consumer<? super SelectorComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(selector(), consumer);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static SelectorComponent selector(@NotNull final String pattern) {
        return selector(pattern, null);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static SelectorComponent selector(@NotNull final String pattern, @Nullable final ComponentLike separator) {
        return SelectorComponentImpl.create(Collections.emptyList(), Style.empty(), pattern, separator);
    }

    @Contract(pure = true)
    @NotNull
    static StorageNBTComponent.Builder storageNBT() {
        return new StorageNBTComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static StorageNBTComponent storageNBT(@NotNull final Consumer<? super StorageNBTComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(storageNBT(), consumer);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static StorageNBTComponent storageNBT(@NotNull final String nbtPath, @NotNull final Key storage) {
        return storageNBT(nbtPath, false, storage);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static StorageNBTComponent storageNBT(@NotNull final String nbtPath, final boolean interpret, @NotNull final Key storage) {
        return storageNBT(nbtPath, interpret, null, storage);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static StorageNBTComponent storageNBT(@NotNull final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final Key storage) {
        return StorageNBTComponentImpl.create(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, storage);
    }

    @Contract(pure = true)
    @NotNull
    static TextComponent.Builder text() {
        return new TextComponentImpl.BuilderImpl();
    }

    @NotNull
    static TextComponent textOfChildren(@NotNull final ComponentLike... components) {
        return components.length == 0 ? empty() : TextComponentImpl.create(Arrays.asList(components), Style.empty(), "");
    }

    @Contract("_ -> new")
    @NotNull
    static TextComponent text(@NotNull final Consumer<? super TextComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(text(), consumer);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(@NotNull final String content) {
        return content.isEmpty() ? empty() : text(content, Style.empty());
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(@NotNull final String content, @NotNull final Style style) {
        return TextComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), content);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(@NotNull final String content, @Nullable final TextColor color) {
        return text(content, Style.style(color));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(@NotNull final String content, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(content, Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(@NotNull final String content, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(content, Style.style(color, decorations));
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(final boolean value) {
        return text(String.valueOf(value));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final boolean value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final boolean value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final boolean value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final boolean value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(pure = true)
    @NotNull
    static TextComponent text(final char value) {
        if (value == '\n') {
            return newline();
        } else {
            return value == ' ' ? space() : text(String.valueOf(value));
        }
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final char value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final char value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final char value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final char value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(final double value) {
        return text(String.valueOf(value));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final double value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final double value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final double value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final double value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(final float value) {
        return text(String.valueOf(value));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final float value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final float value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final float value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final float value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(final int value) {
        return text(String.valueOf(value));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final int value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final int value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final int value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final int value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TextComponent text(final long value) {
        return text(String.valueOf(value));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final long value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final long value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final long value, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TextComponent text(final long value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }

    @Contract(pure = true)
    @NotNull
    static TranslatableComponent.Builder translatable() {
        return new TranslatableComponentImpl.BuilderImpl();
    }

    @Contract("_ -> new")
    @NotNull
    static TranslatableComponent translatable(@NotNull final Consumer<? super TranslatableComponent.Builder> consumer) {
        return AbstractBuilder.configureAndBuild(translatable(), consumer);
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key) {
        return translatable(key, Style.empty());
    }

    @Contract(value = "_ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), Style.empty());
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style) {
        return TranslatableComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), key, Collections.emptyList());
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), style);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color) {
        return translatable(key, Style.style(color));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return translatable(key, Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final TextDecoration... decorations) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return translatable(key, Style.style(color, decorations));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @NotNull final ComponentLike... args) {
        return translatable(key, Style.empty(), args);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final ComponentLike... args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style, @NotNull final ComponentLike... args) {
        return TranslatableComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), key, (ComponentLike[]) Objects.requireNonNull(args, "args"));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style, @NotNull final ComponentLike... args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), style, args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final ComponentLike... args) {
        return translatable(key, Style.style(color), args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final ComponentLike... args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, args);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final ComponentLike... args) {
        return translatable(key, Style.style(color, decorations), args);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final ComponentLike... args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @NotNull final List<? extends ComponentLike> args) {
        return TranslatableComponentImpl.create(Collections.emptyList(), Style.empty(), key, (List<? extends ComponentLike>) Objects.requireNonNull(args, "args"));
    }

    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final List<? extends ComponentLike> args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style, @NotNull final List<? extends ComponentLike> args) {
        return TranslatableComponentImpl.create(Collections.emptyList(), (Style) Objects.requireNonNull(style, "style"), key, (List<? extends ComponentLike>) Objects.requireNonNull(args, "args"));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style, @NotNull final List<? extends ComponentLike> args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), style, args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final List<? extends ComponentLike> args) {
        return translatable(key, Style.style(color), args);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final List<? extends ComponentLike> args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, args);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final List<? extends ComponentLike> args) {
        return translatable(key, Style.style(color, decorations), args);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    static TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final List<? extends ComponentLike> args) {
        return translatable(((Translatable) Objects.requireNonNull(translatable, "translatable")).translationKey(), color, decorations, args);
    }

    @NotNull
    @Unmodifiable
    List<Component> children();

    @Contract(pure = true)
    @NotNull
    Component children(@NotNull final List<? extends ComponentLike> children);

    default boolean contains(@NotNull final Component that) {
        return this.contains(that, EQUALS_IDENTITY);
    }

    default boolean contains(@NotNull final Component that, @NotNull final BiPredicate<? super Component, ? super Component> equals) {
        if (equals.test(this, that)) {
            return true;
        } else {
            for (Component child : this.children()) {
                if (child.contains(that, equals)) {
                    return true;
                }
            }
            HoverEvent<?> hoverEvent = this.hoverEvent();
            if (hoverEvent != null) {
                Object value = hoverEvent.value();
                Component component = null;
                if (value instanceof Component) {
                    component = (Component) hoverEvent.value();
                } else if (value instanceof HoverEvent.ShowEntity) {
                    component = ((HoverEvent.ShowEntity) value).name();
                }
                if (component != null) {
                    if (equals.test(that, component)) {
                        return true;
                    }
                    for (Component childx : component.children()) {
                        if (childx.contains(that, equals)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    default void detectCycle(@NotNull final Component that) {
        if (that.contains(this)) {
            throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
        }
    }

    @Contract(pure = true)
    @NotNull
    default Component append(@NotNull final Component component) {
        return this.append((ComponentLike) component);
    }

    @NotNull
    default Component append(@NotNull final ComponentLike like) {
        Objects.requireNonNull(like, "like");
        Component component = like.asComponent();
        Objects.requireNonNull(component, "component");
        if (component == empty()) {
            return this;
        } else {
            List<Component> oldChildren = this.children();
            return this.children(MonkeyBars.addOne(oldChildren, component));
        }
    }

    @Contract(pure = true)
    @NotNull
    default Component append(@NotNull final ComponentBuilder<?, ?> builder) {
        return this.append((Component) builder.build());
    }

    @Contract(pure = true)
    @NotNull
    default Component appendNewline() {
        return this.append((Component) newline());
    }

    @Contract(pure = true)
    @NotNull
    default Component appendSpace() {
        return this.append((Component) space());
    }

    @Contract(pure = true)
    @NotNull
    default Component applyFallbackStyle(@NotNull final Style style) {
        Objects.requireNonNull(style, "style");
        return this.style(this.style().merge(style, Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
    }

    @Contract(pure = true)
    @NotNull
    default Component applyFallbackStyle(@NotNull final StyleBuilderApplicable... style) {
        return this.applyFallbackStyle(Style.style(style));
    }

    @NotNull
    Style style();

    @Contract(pure = true)
    @NotNull
    Component style(@NotNull final Style style);

    @Contract(pure = true)
    @NotNull
    default Component style(@NotNull final Consumer<Style.Builder> consumer) {
        return this.style(this.style().edit(consumer));
    }

    @Contract(pure = true)
    @NotNull
    default Component style(@NotNull final Consumer<Style.Builder> consumer, @NotNull final Style.Merge.Strategy strategy) {
        return this.style(this.style().edit(consumer, strategy));
    }

    @Contract(pure = true)
    @NotNull
    default Component style(@NotNull final Style.Builder style) {
        return this.style(style.build());
    }

    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that) {
        return this.mergeStyle(that, Style.Merge.all());
    }

    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that, @NotNull final Style.Merge... merges) {
        return this.mergeStyle(that, Style.Merge.merges(merges));
    }

    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        return this.style(this.style().merge(that.style(), merges));
    }

    @Nullable
    @Override
    default Key font() {
        return this.style().font();
    }

    @NotNull
    default Component font(@Nullable final Key key) {
        return this.style(this.style().font(key));
    }

    @Nullable
    @Override
    default TextColor color() {
        return this.style().color();
    }

    @Contract(pure = true)
    @NotNull
    default Component color(@Nullable final TextColor color) {
        return this.style(this.style().color(color));
    }

    @Contract(pure = true)
    @NotNull
    default Component colorIfAbsent(@Nullable final TextColor color) {
        return this.color() == null ? this.color(color) : this;
    }

    @Override
    default boolean hasDecoration(@NotNull final TextDecoration decoration) {
        return StyleGetter.super.hasDecoration(decoration);
    }

    @Contract(pure = true)
    @NotNull
    default Component decorate(@NotNull final TextDecoration decoration) {
        return (Component) StyleSetter.super.decorate(decoration);
    }

    @NotNull
    @Override
    default TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
        return this.style().decoration(decoration);
    }

    @Contract(pure = true)
    @NotNull
    default Component decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return (Component) StyleSetter.super.decoration(decoration, flag);
    }

    @Contract(pure = true)
    @NotNull
    default Component decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
        return this.style(this.style().decoration(decoration, state));
    }

    @NotNull
    default Component decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        TextDecoration.State oldState = this.decoration(decoration);
        return oldState == TextDecoration.State.NOT_SET ? this.style(this.style().decoration(decoration, state)) : this;
    }

    @NotNull
    @Override
    default Map<TextDecoration, TextDecoration.State> decorations() {
        return this.style().decorations();
    }

    @Contract(pure = true)
    @NotNull
    default Component decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return this.style(this.style().decorations(decorations));
    }

    @Nullable
    @Override
    default ClickEvent clickEvent() {
        return this.style().clickEvent();
    }

    @Contract(pure = true)
    @NotNull
    default Component clickEvent(@Nullable final ClickEvent event) {
        return this.style(this.style().clickEvent(event));
    }

    @Nullable
    @Override
    default HoverEvent<?> hoverEvent() {
        return this.style().hoverEvent();
    }

    @Contract(pure = true)
    @NotNull
    default Component hoverEvent(@Nullable final HoverEventSource<?> source) {
        return this.style(this.style().hoverEvent(source));
    }

    @Nullable
    @Override
    default String insertion() {
        return this.style().insertion();
    }

    @Contract(pure = true)
    @NotNull
    default Component insertion(@Nullable final String insertion) {
        return this.style(this.style().insertion(insertion));
    }

    default boolean hasStyling() {
        return !this.style().isEmpty();
    }

    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Consumer<TextReplacementConfig.Builder> configurer) {
        Objects.requireNonNull(configurer, "configurer");
        return this.replaceText(AbstractBuilder.configureAndBuild(TextReplacementConfig.builder(), configurer));
    }

    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final TextReplacementConfig config) {
        Objects.requireNonNull(config, "replacement");
        if (!(config instanceof TextReplacementConfigImpl)) {
            throw new IllegalArgumentException("Provided replacement was a custom TextReplacementConfig implementation, which is not supported.");
        } else {
            return TextReplacementRenderer.INSTANCE.render(this, ((TextReplacementConfigImpl) config).createState());
        }
    }

    @NotNull
    default Component compact() {
        return ComponentCompaction.compact(this, null);
    }

    @NotNull
    default Iterable<Component> iterable(@NotNull final ComponentIteratorType type, @NotNull @Nullable final ComponentIteratorFlag... flags) {
        return this.iterable(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }

    @NotNull
    default Iterable<Component> iterable(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(flags, "flags");
        return new ForwardingIterator<>(() -> this.iterator(type, flags), () -> this.spliterator(type, flags));
    }

    @NotNull
    default Iterator<Component> iterator(@NotNull final ComponentIteratorType type, @NotNull @Nullable final ComponentIteratorFlag... flags) {
        return this.iterator(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }

    @NotNull
    default Iterator<Component> iterator(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        return new ComponentIterator(this, (ComponentIteratorType) Objects.requireNonNull(type, "type"), (Set<ComponentIteratorFlag>) Objects.requireNonNull(flags, "flags"));
    }

    @NotNull
    default Spliterator<Component> spliterator(@NotNull final ComponentIteratorType type, @NotNull @Nullable final ComponentIteratorFlag... flags) {
        return this.spliterator(type, flags == null ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }

    @NotNull
    default Spliterator<Component> spliterator(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        return Spliterators.spliteratorUnknownSize(this.iterator(type, flags), 1296);
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement) {
        return this.replaceText(b -> b.matchLiteral(search).replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
        return this.replaceText(b -> b.match(pattern).replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceFirstText(@NotNull final String search, @Nullable final ComponentLike replacement) {
        return this.replaceText(b -> b.matchLiteral(search).once().replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceFirstText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
        return this.replaceText(b -> b.match(pattern).once().replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement, final int numberOfReplacements) {
        return this.replaceText(b -> b.matchLiteral(search).times(numberOfReplacements).replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement, final int numberOfReplacements) {
        return this.replaceText(b -> b.match(pattern).times(numberOfReplacements).replacement(replacement));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement, @NotNull final IntFunction2<PatternReplacementResult> fn) {
        return this.replaceText(b -> b.matchLiteral(search).replacement(replacement).condition(fn));
    }

    @Deprecated
    @ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement, @NotNull final IntFunction2<PatternReplacementResult> fn) {
        return this.replaceText(b -> b.match(pattern).replacement(replacement).condition(fn));
    }

    @Override
    default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
        component.append(this);
    }

    @NotNull
    @Override
    default Component asComponent() {
        return this;
    }

    @NotNull
    @Override
    default HoverEvent<Component> asHoverEvent(@NotNull final UnaryOperator<Component> op) {
        return HoverEvent.showText((Component) op.apply(this));
    }

    @NotNull
    @Override
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("style", this.style()), ExaminableProperty.of("children", this.children()));
    }
}