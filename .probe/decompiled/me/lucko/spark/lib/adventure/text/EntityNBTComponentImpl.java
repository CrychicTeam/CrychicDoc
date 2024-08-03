package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EntityNBTComponentImpl extends NBTComponentImpl<EntityNBTComponent, EntityNBTComponent.Builder> implements EntityNBTComponent {

    private final String selector;

    static EntityNBTComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, final String selector) {
        return new EntityNBTComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(nbtPath, "nbtPath"), interpret, ComponentLike.unbox(separator), (String) Objects.requireNonNull(selector, "selector"));
    }

    EntityNBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator, final String selector) {
        super(children, style, nbtPath, interpret, separator);
        this.selector = selector;
    }

    @NotNull
    public EntityNBTComponent nbtPath(@NotNull final String nbtPath) {
        return (EntityNBTComponent) (Objects.equals(this.nbtPath, nbtPath) ? this : create(this.children, this.style, nbtPath, this.interpret, this.separator, this.selector));
    }

    @NotNull
    public EntityNBTComponent interpret(final boolean interpret) {
        return (EntityNBTComponent) (this.interpret == interpret ? this : create(this.children, this.style, this.nbtPath, interpret, this.separator, this.selector));
    }

    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }

    @NotNull
    public EntityNBTComponent separator(@Nullable final ComponentLike separator) {
        return create(this.children, this.style, this.nbtPath, this.interpret, separator, this.selector);
    }

    @NotNull
    @Override
    public String selector() {
        return this.selector;
    }

    @NotNull
    @Override
    public EntityNBTComponent selector(@NotNull final String selector) {
        return (EntityNBTComponent) (Objects.equals(this.selector, selector) ? this : create(this.children, this.style, this.nbtPath, this.interpret, this.separator, selector));
    }

    @NotNull
    public EntityNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.nbtPath, this.interpret, this.separator, this.selector);
    }

    @NotNull
    public EntityNBTComponent style(@NotNull final Style style) {
        return create(this.children, style, this.nbtPath, this.interpret, this.separator, this.selector);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof EntityNBTComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            EntityNBTComponentImpl that = (EntityNBTComponentImpl) other;
            return Objects.equals(this.selector, that.selector());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.selector.hashCode();
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public EntityNBTComponent.Builder toBuilder() {
        return new EntityNBTComponentImpl.BuilderImpl(this);
    }

    static final class BuilderImpl extends AbstractNBTComponentBuilder<EntityNBTComponent, EntityNBTComponent.Builder> implements EntityNBTComponent.Builder {

        @Nullable
        private String selector;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final EntityNBTComponent component) {
            super(component);
            this.selector = component.selector();
        }

        @NotNull
        @Override
        public EntityNBTComponent.Builder selector(@NotNull final String selector) {
            this.selector = (String) Objects.requireNonNull(selector, "selector");
            return this;
        }

        @NotNull
        public EntityNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            } else if (this.selector == null) {
                throw new IllegalStateException("selector must be set");
            } else {
                return EntityNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.selector);
            }
        }
    }
}