package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ScoreComponentImpl extends AbstractComponent implements ScoreComponent {

    private final String name;

    private final String objective;

    @Deprecated
    @Nullable
    private final String value;

    static ScoreComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        return new ScoreComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(name, "name"), (String) Objects.requireNonNull(objective, "objective"), value);
    }

    ScoreComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        super(children, style);
        this.name = name;
        this.objective = objective;
        this.value = value;
    }

    @NotNull
    @Override
    public String name() {
        return this.name;
    }

    @NotNull
    @Override
    public ScoreComponent name(@NotNull final String name) {
        return (ScoreComponent) (Objects.equals(this.name, name) ? this : create(this.children, this.style, name, this.objective, this.value));
    }

    @NotNull
    @Override
    public String objective() {
        return this.objective;
    }

    @NotNull
    @Override
    public ScoreComponent objective(@NotNull final String objective) {
        return (ScoreComponent) (Objects.equals(this.objective, objective) ? this : create(this.children, this.style, this.name, objective, this.value));
    }

    @Deprecated
    @Nullable
    @Override
    public String value() {
        return this.value;
    }

    @Deprecated
    @NotNull
    @Override
    public ScoreComponent value(@Nullable final String value) {
        return (ScoreComponent) (Objects.equals(this.value, value) ? this : create(this.children, this.style, this.name, this.objective, value));
    }

    @NotNull
    public ScoreComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.name, this.objective, this.value);
    }

    @NotNull
    public ScoreComponent style(@NotNull final Style style) {
        return create(this.children, style, this.name, this.objective, this.value);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof ScoreComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            ScoreComponent that = (ScoreComponent) other;
            return Objects.equals(this.name, that.name()) && Objects.equals(this.objective, that.objective()) && Objects.equals(this.value, that.value());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.name.hashCode();
        result = 31 * result + this.objective.hashCode();
        return 31 * result + Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public ScoreComponent.Builder toBuilder() {
        return new ScoreComponentImpl.BuilderImpl(this);
    }

    static final class BuilderImpl extends AbstractComponentBuilder<ScoreComponent, ScoreComponent.Builder> implements ScoreComponent.Builder {

        @Nullable
        private String name;

        @Nullable
        private String objective;

        @Nullable
        private String value;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final ScoreComponent component) {
            super(component);
            this.name = component.name();
            this.objective = component.objective();
            this.value = component.value();
        }

        @NotNull
        @Override
        public ScoreComponent.Builder name(@NotNull final String name) {
            this.name = (String) Objects.requireNonNull(name, "name");
            return this;
        }

        @NotNull
        @Override
        public ScoreComponent.Builder objective(@NotNull final String objective) {
            this.objective = (String) Objects.requireNonNull(objective, "objective");
            return this;
        }

        @Deprecated
        @NotNull
        @Override
        public ScoreComponent.Builder value(@Nullable final String value) {
            this.value = value;
            return this;
        }

        @NotNull
        public ScoreComponent build() {
            if (this.name == null) {
                throw new IllegalStateException("name must be set");
            } else if (this.objective == null) {
                throw new IllegalStateException("objective must be set");
            } else {
                return ScoreComponentImpl.create(this.children, this.buildStyle(), this.name, this.objective, this.value);
            }
        }
    }
}