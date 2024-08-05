package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B> {

    static final boolean INTERPRET_DEFAULT = false;

    final String nbtPath;

    final boolean interpret;

    @Nullable
    final Component separator;

    NBTComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final Component separator) {
        super(children, style);
        this.nbtPath = nbtPath;
        this.interpret = interpret;
        this.separator = separator;
    }

    @NotNull
    @Override
    public String nbtPath() {
        return this.nbtPath;
    }

    @Override
    public boolean interpret() {
        return this.interpret;
    }

    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof NBTComponent)) {
            return false;
        } else if (!super.equals(other)) {
            return false;
        } else {
            NBTComponent<?, ?> that = (NBTComponent<?, ?>) other;
            return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret() && Objects.equals(this.separator, that.separator());
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.nbtPath.hashCode();
        result = 31 * result + Boolean.hashCode(this.interpret);
        return 31 * result + Objects.hashCode(this.separator);
    }
}