package me.lucko.spark.lib.adventure.text;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import me.lucko.spark.lib.adventure.examination.ExaminableProperty;
import me.lucko.spark.lib.adventure.examination.string.StringExaminer;
import me.lucko.spark.lib.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Debug.Renderer;

@Deprecated
@ScheduledForRemoval(inVersion = "5.0.0")
@Renderer(text = "this.debuggerString()", childrenArray = "this.children().toArray()", hasChildren = "!this.children().isEmpty()")
public abstract class AbstractComponent implements Component {

    protected final List<Component> children;

    protected final Style style;

    protected AbstractComponent(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style) {
        this.children = ComponentLike.asComponents(children, IS_NOT_EMPTY);
        this.style = style;
    }

    @NotNull
    @Override
    public final List<Component> children() {
        return this.children;
    }

    @NotNull
    @Override
    public final Style style() {
        return this.style;
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof AbstractComponent)) {
            return false;
        } else {
            AbstractComponent that = (AbstractComponent) other;
            return Objects.equals(this.children, that.children) && Objects.equals(this.style, that.style);
        }
    }

    public int hashCode() {
        int result = this.children.hashCode();
        return 31 * result + this.style.hashCode();
    }

    public abstract String toString();

    private String debuggerString() {
        Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren = this.examinableProperties().filter(property -> !property.name().equals("children"));
        return StringExaminer.simpleEscaping().examine(this.examinableName(), examinablePropertiesWithoutChildren);
    }
}