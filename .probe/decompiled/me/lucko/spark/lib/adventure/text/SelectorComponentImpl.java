package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class SelectorComponentImpl extends AbstractComponent implements SelectorComponent {

    private final String pattern;

    @Nullable
    private final Component separator;

    static SelectorComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String pattern, @Nullable final ComponentLike separator) {
        return new SelectorComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(pattern, "pattern"), ComponentLike.unbox(separator));
    }

    SelectorComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String pattern, @Nullable final Component separator) {
        super(children, style);
        this.pattern = pattern;
        this.separator = separator;
    }

    @NotNull
    @Override
    public String pattern() {
        return this.pattern;
    }

    @NotNull
    @Override
    public SelectorComponent pattern(@NotNull final String pattern) {
        return (SelectorComponent) (Objects.equals(this.pattern, pattern) ? this : create(this.children, this.style, pattern, this.separator));
    }

    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }

    @NotNull
    @Override
    public SelectorComponent separator(@Nullable final ComponentLike separator) {
        return create(this.children, this.style, this.pattern, separator);
    }

    @NotNull
    public SelectorComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.pattern, this.separator);
    }

    @NotNull
    public SelectorComponent style(@NotNull final Style style) {
        return create(this.children, style, this.pattern, this.separator);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof SelectorComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            SelectorComponent that = (SelectorComponent) other;
            return Objects.equals(this.pattern, that.pattern()) && Objects.equals(this.separator, that.separator());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.pattern.hashCode();
        return 31 * result + Objects.hashCode(this.separator);
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public SelectorComponent.Builder toBuilder() {
        return new SelectorComponentImpl.BuilderImpl(this);
    }

    static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, SelectorComponent.Builder> implements SelectorComponent.Builder {

        @Nullable
        private String pattern;

        @Nullable
        private Component separator;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final SelectorComponent component) {
            super(component);
            this.pattern = component.pattern();
            this.separator = component.separator();
        }

        @NotNull
        @Override
        public SelectorComponent.Builder pattern(@NotNull final String pattern) {
            this.pattern = (String) Objects.requireNonNull(pattern, "pattern");
            return this;
        }

        @NotNull
        @Override
        public SelectorComponent.Builder separator(@Nullable final ComponentLike separator) {
            this.separator = ComponentLike.unbox(separator);
            return this;
        }

        @NotNull
        public SelectorComponent build() {
            if (this.pattern == null) {
                throw new IllegalStateException("pattern must be set");
            } else {
                return SelectorComponentImpl.create(this.children, this.buildStyle(), this.pattern, this.separator);
            }
        }
    }
}