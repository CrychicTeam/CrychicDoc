package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.key.Key;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class StorageNBTComponentImpl extends NBTComponentImpl<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent {

    private final Key storage;

    @NotNull
    static StorageNBTComponent create(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final Key storage) {
        return new StorageNBTComponentImpl(ComponentLike.asComponents(children, IS_NOT_EMPTY), (Style) Objects.requireNonNull(style, "style"), (String) Objects.requireNonNull(nbtPath, "nbtPath"), interpret, ComponentLike.unbox(separator), (Key) Objects.requireNonNull(storage, "storage"));
    }

    StorageNBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator, final Key storage) {
        super(children, style, nbtPath, interpret, separator);
        this.storage = storage;
    }

    @NotNull
    public StorageNBTComponent nbtPath(@NotNull final String nbtPath) {
        return (StorageNBTComponent) (Objects.equals(this.nbtPath, nbtPath) ? this : create(this.children, this.style, nbtPath, this.interpret, this.separator, this.storage));
    }

    @NotNull
    public StorageNBTComponent interpret(final boolean interpret) {
        return (StorageNBTComponent) (this.interpret == interpret ? this : create(this.children, this.style, this.nbtPath, interpret, this.separator, this.storage));
    }

    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }

    @NotNull
    public StorageNBTComponent separator(@Nullable final ComponentLike separator) {
        return create(this.children, this.style, this.nbtPath, this.interpret, separator, this.storage);
    }

    @NotNull
    @Override
    public Key storage() {
        return this.storage;
    }

    @NotNull
    @Override
    public StorageNBTComponent storage(@NotNull final Key storage) {
        return (StorageNBTComponent) (Objects.equals(this.storage, storage) ? this : create(this.children, this.style, this.nbtPath, this.interpret, this.separator, storage));
    }

    @NotNull
    public StorageNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
        return create(children, this.style, this.nbtPath, this.interpret, this.separator, this.storage);
    }

    @NotNull
    public StorageNBTComponent style(@NotNull final Style style) {
        return create(this.children, style, this.nbtPath, this.interpret, this.separator, this.storage);
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof StorageNBTComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            StorageNBTComponentImpl that = (StorageNBTComponentImpl) other;
            return Objects.equals(this.storage, that.storage());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        return 31 * result + this.storage.hashCode();
    }

    @Override
    public String toString() {
        return Internals.toString(this);
    }

    @NotNull
    public StorageNBTComponent.Builder toBuilder() {
        return new StorageNBTComponentImpl.BuilderImpl(this);
    }

    static class BuilderImpl extends AbstractNBTComponentBuilder<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent.Builder {

        @Nullable
        private Key storage;

        BuilderImpl() {
        }

        BuilderImpl(@NotNull final StorageNBTComponent component) {
            super(component);
            this.storage = component.storage();
        }

        @NotNull
        @Override
        public StorageNBTComponent.Builder storage(@NotNull final Key storage) {
            this.storage = (Key) Objects.requireNonNull(storage, "storage");
            return this;
        }

        @NotNull
        public StorageNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            } else if (this.storage == null) {
                throw new IllegalStateException("storage must be set");
            } else {
                return StorageNBTComponentImpl.create(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.storage);
            }
        }
    }
}