package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class KeybindComponentImpl extends AbstractComponent implements KeybindComponent {

    private final String keybind;

    static KeybindComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String keybind) {
        return new KeybindComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(keybind, "keybind"));
    }

    KeybindComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String keybind) {
        super(children, style);
        this.keybind = keybind;
    }

    @NotNull
    @Override
    public String keybind() {
        return this.keybind;
    }

    @NotNull
    @Override
    public KeybindComponent keybind(@NotNull final String keybind) {
        return (KeybindComponent) (Objects.equals(this.keybind, keybind) ? this : create(this.children, this.style, keybind));
    }

    @NotNull
    public KeybindComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.keybind);
    }

    @NotNull
    public KeybindComponent style(@NotNull final Style style) {
        return create(this.children, style, this.keybind);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof KeybindComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            KeybindComponent that = (KeybindComponent) other;
            return Objects.equals(this.keybind, that.keybind());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.keybind.hashCode();
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public KeybindComponent.Builder toBuilder() {
        return new KeybindComponentImpl.BuilderImpl(this);
    }

    static final class BuilderImpl extends AbstractComponentBuilder<KeybindComponent, KeybindComponent.Builder> implements KeybindComponent.Builder {

        @Nullable
        private String keybind;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final KeybindComponent component) {
            super(component);
            this.keybind = component.keybind();
        }

        @NotNull
        @Override
        public KeybindComponent.Builder keybind(@NotNull final String keybind) {
            this.keybind = (String) Objects.requireNonNull(keybind, "keybind");
            return this;
        }

        @NotNull
        public KeybindComponent build() {
            if (this.keybind == null) {
                throw new IllegalStateException("keybind must be set");
            } else {
                return KeybindComponentImpl.create(this.children, this.buildStyle(), this.keybind);
            }
        }
    }
}