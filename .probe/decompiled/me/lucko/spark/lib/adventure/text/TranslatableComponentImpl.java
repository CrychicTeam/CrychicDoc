package me.lucko.spark.lib.adventure.text;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent {

    private final String key;

    private final List<Component> args;

    static TranslatableComponent create(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @NotNull final ComponentLike[] args) {
        Objects.requireNonNull(args, "args");
        return create(children, style, key, Arrays.asList(args));
    }

    static TranslatableComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String key, @NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(key, "key"), ComponentLike.asComponents(args));
    }

    TranslatableComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @NotNull final List<Component> args) {
        super(children, style);
        this.key = key;
        this.args = args;
    }

    @NotNull
    @Override
    public String key() {
        return this.key;
    }

    @NotNull
    @Override
    public TranslatableComponent key(@NotNull final String key) {
        return (TranslatableComponent) (Objects.equals(this.key, key) ? this : create(this.children, this.style, key, this.args));
    }

    @NotNull
    @Override
    public List<Component> args() {
        return this.args;
    }

    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final ComponentLike... args) {
        return create(this.children, this.style, this.key, args);
    }

    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final List<? extends ComponentLike> args) {
        return create(this.children, this.style, this.key, args);
    }

    @NotNull
    public TranslatableComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.key, this.args);
    }

    @NotNull
    public TranslatableComponent style(@NotNull final Style style) {
        return create(this.children, style, this.key, this.args);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof TranslatableComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            TranslatableComponent that = (TranslatableComponent) other;
            return Objects.equals(this.key, that.key()) && Objects.equals(this.args, that.args());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.key.hashCode();
        return 31 * result + this.args.hashCode();
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public TranslatableComponent.Builder toBuilder() {
        return new TranslatableComponentImpl.BuilderImpl(this);
    }

    static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder {

        @Nullable
        private String key;

        private List<? extends Component> args = Collections.emptyList();

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final TranslatableComponent component) {
            super(component);
            this.key = component.key();
            this.args = component.args();
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder key(@NotNull final String key) {
            this.key = key;
            return this;
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?> arg) {
            return this.args(Collections.singletonList(((ComponentBuilder) Objects.requireNonNull(arg, "arg")).build()));
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?>... args) {
            Objects.requireNonNull(args, "args");
            return args.length == 0 ? this.args(Collections.emptyList()) : this.args((List<? extends ComponentLike>) Stream.of(args).map(ComponentBuilder::build).collect(Collectors.toList()));
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final Component arg) {
            return this.args(Collections.singletonList((Component) Objects.requireNonNull(arg, "arg")));
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentLike... args) {
            Objects.requireNonNull(args, "args");
            return args.length == 0 ? this.args(Collections.emptyList()) : this.args(Arrays.asList(args));
        }

        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final List<? extends ComponentLike> args) {
            this.args = ComponentLike.asComponents((List<? extends ComponentLike>) Objects.requireNonNull(args, "args"));
            return this;
        }

        @NotNull
        public TranslatableComponent build() {
            if (this.key == null) {
                throw new IllegalStateException("key must be set");
            } else {
                return TranslatableComponentImpl.create(this.children, this.buildStyle(), this.key, this.args);
            }
        }
    }
}