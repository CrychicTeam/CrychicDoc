package me.lucko.spark.lib.adventure.text;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class JoinConfigurationImpl implements JoinConfiguration {

    static final Function<ComponentLike, Component> DEFAULT_CONVERTOR = ComponentLike::asComponent;

    static final Predicate<ComponentLike> DEFAULT_PREDICATE = componentLike -> true;

    static final JoinConfigurationImpl NULL = new JoinConfigurationImpl();

    static final JoinConfiguration STANDARD_NEW_LINES = JoinConfiguration.separator(Component.newline());

    static final JoinConfiguration STANDARD_COMMA_SEPARATED = JoinConfiguration.separator(Component.text(","));

    static final JoinConfiguration STANDARD_COMMA_SPACE_SEPARATED = JoinConfiguration.separator(Component.text(", "));

    static final JoinConfiguration STANDARD_ARRAY_LIKE = JoinConfiguration.builder().separator(Component.text(", ")).prefix(Component.text("[")).suffix(Component.text("]")).build();

    private final Component prefix;

    private final Component suffix;

    private final Component separator;

    private final Component lastSeparator;

    private final Component lastSeparatorIfSerial;

    private final Function<ComponentLike, Component> convertor;

    private final Predicate<ComponentLike> predicate;

    private final Style rootStyle;

    private JoinConfigurationImpl() {
        this.prefix = null;
        this.suffix = null;
        this.separator = null;
        this.lastSeparator = null;
        this.lastSeparatorIfSerial = null;
        this.convertor = DEFAULT_CONVERTOR;
        this.predicate = DEFAULT_PREDICATE;
        this.rootStyle = Style.empty();
    }

    private JoinConfigurationImpl(@NotNull final JoinConfigurationImpl.BuilderImpl builder) {
        this.prefix = ComponentLike.unbox(builder.prefix);
        this.suffix = ComponentLike.unbox(builder.suffix);
        this.separator = ComponentLike.unbox(builder.separator);
        this.lastSeparator = ComponentLike.unbox(builder.lastSeparator);
        this.lastSeparatorIfSerial = ComponentLike.unbox(builder.lastSeparatorIfSerial);
        this.convertor = builder.convertor;
        this.predicate = builder.predicate;
        this.rootStyle = builder.rootStyle;
    }

    @Nullable
    @Override
    public Component prefix() {
        return this.prefix;
    }

    @Nullable
    @Override
    public Component suffix() {
        return this.suffix;
    }

    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }

    @Nullable
    @Override
    public Component lastSeparator() {
        return this.lastSeparator;
    }

    @Nullable
    @Override
    public Component lastSeparatorIfSerial() {
        return this.lastSeparatorIfSerial;
    }

    @NotNull
    @Override
    public Function<ComponentLike, Component> convertor() {
        return this.convertor;
    }

    @NotNull
    @Override
    public Predicate<ComponentLike> predicate() {
        return this.predicate;
    }

    @NotNull
    @Override
    public Style parentStyle() {
        return this.rootStyle;
    }

    @NotNull
    public JoinConfiguration.Builder toBuilder() {
        return new JoinConfigurationImpl.BuilderImpl(this);
    }

    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("prefix", this.prefix), ExaminableProperty.of("suffix", this.suffix), ExaminableProperty.of("separator", this.separator), ExaminableProperty.of("lastSeparator", this.lastSeparator), ExaminableProperty.of("lastSeparatorIfSerial", this.lastSeparatorIfSerial), ExaminableProperty.of("convertor", this.convertor), ExaminableProperty.of("predicate", this.predicate), ExaminableProperty.of("rootStyle", this.rootStyle));
    }

    public String toString() {
        return Internals.toString(this);
    }

    @Contract(pure = true)
    @NotNull
    static Component join(@NotNull final JoinConfiguration config, @NotNull final Iterable<? extends ComponentLike> components) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(components, "components");
        Iterator<? extends ComponentLike> it = components.iterator();
        if (!it.hasNext()) {
            return singleElementJoin(config, null);
        } else {
            ComponentLike component = (ComponentLike) Objects.requireNonNull((ComponentLike) it.next(), "Null elements in \"components\" are not allowed");
            int componentsSeen = 0;
            if (!it.hasNext()) {
                return singleElementJoin(config, component);
            } else {
                Component prefix = config.prefix();
                Component suffix = config.suffix();
                Function<ComponentLike, Component> convertor = config.convertor();
                Predicate<ComponentLike> predicate = config.predicate();
                Style rootStyle = config.parentStyle();
                boolean hasRootStyle = rootStyle != Style.empty();
                Component separator = config.separator();
                boolean hasSeparator = separator != null;
                TextComponent.Builder builder = hasRootStyle ? Component.text().style(rootStyle) : Component.text();
                if (prefix != null) {
                    builder.append(prefix);
                }
                while (component != null) {
                    if (!predicate.test(component)) {
                        if (!it.hasNext()) {
                            break;
                        }
                        component = (ComponentLike) it.next();
                    } else {
                        builder.append((Component) Objects.requireNonNull((Component) convertor.apply(component), "Null output from \"convertor\" is not allowed"));
                        componentsSeen++;
                        if (!it.hasNext()) {
                            component = null;
                        } else {
                            component = (ComponentLike) Objects.requireNonNull((ComponentLike) it.next(), "Null elements in \"components\" are not allowed");
                            if (it.hasNext()) {
                                if (hasSeparator) {
                                    builder.append(separator);
                                }
                            } else {
                                Component lastSeparator = null;
                                if (componentsSeen > 1) {
                                    lastSeparator = config.lastSeparatorIfSerial();
                                }
                                if (lastSeparator == null) {
                                    lastSeparator = config.lastSeparator();
                                }
                                if (lastSeparator == null) {
                                    lastSeparator = config.separator();
                                }
                                if (lastSeparator != null) {
                                    builder.append(lastSeparator);
                                }
                            }
                        }
                    }
                }
                if (suffix != null) {
                    builder.append(suffix);
                }
                return builder.build();
            }
        }
    }

    @NotNull
    static Component singleElementJoin(@NotNull final JoinConfiguration config, @Nullable final ComponentLike component) {
        Component prefix = config.prefix();
        Component suffix = config.suffix();
        Function<ComponentLike, Component> convertor = config.convertor();
        Predicate<ComponentLike> predicate = config.predicate();
        Style rootStyle = config.parentStyle();
        boolean hasRootStyle = rootStyle != Style.empty();
        if (prefix == null && suffix == null) {
            Component result;
            if (component != null && predicate.test(component)) {
                result = (Component) convertor.apply(component);
            } else {
                result = Component.empty();
            }
            return (Component) (hasRootStyle ? Component.text().style(rootStyle).append(result).build() : result);
        } else {
            TextComponent.Builder builder = Component.text();
            if (prefix != null) {
                builder.append(prefix);
            }
            if (component != null && predicate.test(component)) {
                builder.append((Component) convertor.apply(component));
            }
            if (suffix != null) {
                builder.append(suffix);
            }
            return hasRootStyle ? Component.text().style(rootStyle).append(builder).build() : builder.build();
        }
    }

    static final class BuilderImpl implements JoinConfiguration.Builder {

        private ComponentLike prefix;

        private ComponentLike suffix;

        private ComponentLike separator;

        private ComponentLike lastSeparator;

        private ComponentLike lastSeparatorIfSerial;

        private Function<ComponentLike, Component> convertor;

        private Predicate<ComponentLike> predicate;

        private Style rootStyle;

        BuilderImpl() {
            this(JoinConfigurationImpl.NULL);
        }

        private BuilderImpl(@NotNull final JoinConfigurationImpl joinConfig) {
            this.separator = joinConfig.separator;
            this.lastSeparator = joinConfig.lastSeparator;
            this.prefix = joinConfig.prefix;
            this.suffix = joinConfig.suffix;
            this.convertor = joinConfig.convertor;
            this.lastSeparatorIfSerial = joinConfig.lastSeparatorIfSerial;
            this.predicate = joinConfig.predicate;
            this.rootStyle = joinConfig.rootStyle;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder prefix(@Nullable final ComponentLike prefix) {
            this.prefix = prefix;
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder suffix(@Nullable final ComponentLike suffix) {
            this.suffix = suffix;
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder separator(@Nullable final ComponentLike separator) {
            this.separator = separator;
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder lastSeparator(@Nullable final ComponentLike lastSeparator) {
            this.lastSeparator = lastSeparator;
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder lastSeparatorIfSerial(@Nullable final ComponentLike lastSeparatorIfSerial) {
            this.lastSeparatorIfSerial = lastSeparatorIfSerial;
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder convertor(@NotNull final Function<ComponentLike, Component> convertor) {
            this.convertor = (Function<ComponentLike, Component>) Objects.requireNonNull(convertor, "convertor");
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder predicate(@NotNull final Predicate<ComponentLike> predicate) {
            this.predicate = (Predicate<ComponentLike>) Objects.requireNonNull(predicate, "predicate");
            return this;
        }

        @NotNull
        @Override
        public JoinConfiguration.Builder parentStyle(@NotNull final Style parentStyle) {
            this.rootStyle = (Style) Objects.requireNonNull(parentStyle, "rootStyle");
            return this;
        }

        @NotNull
        public JoinConfiguration build() {
            return new JoinConfigurationImpl(this);
        }
    }
}