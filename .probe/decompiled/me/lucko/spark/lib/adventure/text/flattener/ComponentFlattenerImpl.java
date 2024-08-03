package me.lucko.spark.lib.adventure.text.flattener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import me.lucko.spark.lib.adventure.text.Component;
import me.lucko.spark.lib.adventure.text.KeybindComponent;
import me.lucko.spark.lib.adventure.text.ScoreComponent;
import me.lucko.spark.lib.adventure.text.SelectorComponent;
import me.lucko.spark.lib.adventure.text.TextComponent;
import me.lucko.spark.lib.adventure.text.TranslatableComponent;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ComponentFlattenerImpl implements ComponentFlattener {

    static final ComponentFlattener BASIC = new ComponentFlattenerImpl.BuilderImpl().mapper(KeybindComponent.class, component -> component.keybind()).mapper(ScoreComponent.class, ScoreComponent::value).mapper(SelectorComponent.class, SelectorComponent::pattern).mapper(TextComponent.class, TextComponent::content).mapper(TranslatableComponent.class, TranslatableComponent::key).build();

    static final ComponentFlattener TEXT_ONLY = new ComponentFlattenerImpl.BuilderImpl().mapper(TextComponent.class, TextComponent::content).build();

    private static final int MAX_DEPTH = 512;

    private final Map<Class<?>, Function<?, String>> flatteners;

    private final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners;

    private final ConcurrentMap<Class<?>, ComponentFlattenerImpl.Handler> propagatedFlatteners = new ConcurrentHashMap();

    private final Function<Component, String> unknownHandler;

    ComponentFlattenerImpl(final Map<Class<?>, Function<?, String>> flatteners, final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners, @Nullable final Function<Component, String> unknownHandler) {
        this.flatteners = Collections.unmodifiableMap(new HashMap(flatteners));
        this.complexFlatteners = Collections.unmodifiableMap(new HashMap(complexFlatteners));
        this.unknownHandler = unknownHandler;
    }

    @Override
    public void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener) {
        this.flatten0(input, listener, 0);
    }

    private void flatten0(@NotNull final Component input, @NotNull final FlattenerListener listener, final int depth) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(listener, "listener");
        if (input != Component.empty()) {
            if (depth > 512) {
                throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
            } else {
                ComponentFlattenerImpl.Handler flattener = this.flattener(input);
                Style inputStyle = input.style();
                listener.pushStyle(inputStyle);
                try {
                    if (flattener != null) {
                        flattener.handle(input, listener, depth + 1);
                    }
                    if (!input.children().isEmpty()) {
                        for (Component child : input.children()) {
                            this.flatten0(child, listener, depth + 1);
                        }
                    }
                } finally {
                    listener.popStyle(inputStyle);
                }
            }
        }
    }

    @Nullable
    private <T extends Component> ComponentFlattenerImpl.Handler flattener(final T test) {
        ComponentFlattenerImpl.Handler flattener = (ComponentFlattenerImpl.Handler) this.propagatedFlatteners.computeIfAbsent(test.getClass(), key -> {
            Function<Component, String> value = (Function<Component, String>) this.flatteners.get(key);
            if (value != null) {
                return (component, listener, depth) -> listener.component((String) value.apply(component));
            } else {
                for (Entry<Class<?>, Function<?, String>> entry : this.flatteners.entrySet()) {
                    if (((Class) entry.getKey()).isAssignableFrom(key)) {
                        return (component, listener, depth) -> listener.component((String) ((Function) entry.getValue()).apply(component));
                    }
                }
                BiConsumer<Component, Consumer<Component>> complexValue = (BiConsumer<Component, Consumer<Component>>) this.complexFlatteners.get(key);
                if (complexValue != null) {
                    return (component, listener, depth) -> complexValue.accept(component, (Consumer) c -> this.flatten0(c, listener, depth));
                } else {
                    for (Entry<Class<?>, BiConsumer<?, Consumer<Component>>> entryx : this.complexFlatteners.entrySet()) {
                        if (((Class) entryx.getKey()).isAssignableFrom(key)) {
                            return (component, listener, depth) -> ((BiConsumer) entry.getValue()).accept(component, (Consumer) c -> this.flatten0(c, listener, depth));
                        }
                    }
                    return ComponentFlattenerImpl.Handler.NONE;
                }
            }
        });
        if (flattener == ComponentFlattenerImpl.Handler.NONE) {
            return this.unknownHandler == null ? null : (component, listener, depth) -> listener.component((String) this.unknownHandler.apply(component));
        } else {
            return flattener;
        }
    }

    @NotNull
    public ComponentFlattener.Builder toBuilder() {
        return new ComponentFlattenerImpl.BuilderImpl(this.flatteners, this.complexFlatteners, this.unknownHandler);
    }

    static final class BuilderImpl implements ComponentFlattener.Builder {

        private final Map<Class<?>, Function<?, String>> flatteners;

        private final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners;

        @Nullable
        private Function<Component, String> unknownHandler;

        BuilderImpl() {
            this.flatteners = new HashMap();
            this.complexFlatteners = new HashMap();
        }

        BuilderImpl(final Map<Class<?>, Function<?, String>> flatteners, final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners, @Nullable final Function<Component, String> unknownHandler) {
            this.flatteners = new HashMap(flatteners);
            this.complexFlatteners = new HashMap(complexFlatteners);
            this.unknownHandler = unknownHandler;
        }

        @NotNull
        public ComponentFlattener build() {
            return new ComponentFlattenerImpl(this.flatteners, this.complexFlatteners, this.unknownHandler);
        }

        @NotNull
        @Override
        public <T extends Component> ComponentFlattener.Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter) {
            this.validateNoneInHierarchy((Class<? extends Component>) Objects.requireNonNull(type, "type"));
            this.flatteners.put(type, (Function) Objects.requireNonNull(converter, "converter"));
            this.complexFlatteners.remove(type);
            return this;
        }

        @NotNull
        @Override
        public <T extends Component> ComponentFlattener.Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter) {
            this.validateNoneInHierarchy((Class<? extends Component>) Objects.requireNonNull(type, "type"));
            this.complexFlatteners.put(type, (BiConsumer) Objects.requireNonNull(converter, "converter"));
            this.flatteners.remove(type);
            return this;
        }

        private void validateNoneInHierarchy(final Class<? extends Component> beingRegistered) {
            for (Class<?> clazz : this.flatteners.keySet()) {
                testHierarchy(clazz, beingRegistered);
            }
            for (Class<?> clazz : this.complexFlatteners.keySet()) {
                testHierarchy(clazz, beingRegistered);
            }
        }

        private static void testHierarchy(final Class<?> existing, final Class<?> beingRegistered) {
            if (!existing.equals(beingRegistered) && (existing.isAssignableFrom(beingRegistered) || beingRegistered.isAssignableFrom(existing))) {
                throw new IllegalArgumentException("Conflict detected between already registered type " + existing + " and newly registered type " + beingRegistered + "! Types in a component flattener must not share a common hierarchy!");
            }
        }

        @NotNull
        @Override
        public ComponentFlattener.Builder unknownMapper(@Nullable final Function<Component, String> converter) {
            this.unknownHandler = converter;
            return this;
        }
    }

    @FunctionalInterface
    interface Handler {

        ComponentFlattenerImpl.Handler NONE = (input, listener, depth) -> {
        };

        void handle(final Component input, final FlattenerListener listener, final int depth);
    }
}