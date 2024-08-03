package dev.ftb.mods.ftblibrary.ui;

import java.util.function.BiConsumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface WidgetLayout {

    WidgetLayout.Padding NO_PADDING = new WidgetLayout.Padding(0, 0);

    WidgetLayout NONE = panel -> 0;

    WidgetLayout VERTICAL = new WidgetLayout.Vertical(0, 0, 0);

    WidgetLayout HORIZONTAL = new WidgetLayout.Horizontal(0, 0, 0);

    int align(Panel var1);

    @NotNull
    default WidgetLayout.Padding getLayoutPadding() {
        return NO_PADDING;
    }

    public static class Horizontal extends WidgetLayout._Simple {

        public Horizontal(int _pre, int _spacing, int _post) {
            super(_pre, _spacing, _post, Widget::getWidth, Widget::setX);
        }

        @NotNull
        @Override
        public WidgetLayout.Padding getLayoutPadding() {
            return new WidgetLayout.Padding(0, this.padding);
        }
    }

    public static record Padding(int vertical, int horizontal) {
    }

    public static class Vertical extends WidgetLayout._Simple {

        public Vertical(int _pre, int _spacing, int _post) {
            super(_pre, _spacing, _post, Widget::getHeight, Widget::setY);
        }

        @NotNull
        @Override
        public WidgetLayout.Padding getLayoutPadding() {
            return new WidgetLayout.Padding(this.padding, 0);
        }
    }

    public abstract static class _Simple implements WidgetLayout {

        protected final int pre;

        protected final int spacing;

        protected final int post;

        private final Function<Widget, Integer> sizeGetter;

        private final BiConsumer<Widget, Integer> positionSetter;

        protected int padding;

        public _Simple(int _pre, int _spacing, int _post, Function<Widget, Integer> sizeGetter, BiConsumer<Widget, Integer> positionSetter) {
            this.pre = _pre;
            this.spacing = _spacing;
            this.post = _post;
            this.sizeGetter = sizeGetter;
            this.positionSetter = positionSetter;
        }

        @Override
        public int align(Panel panel) {
            int i = this.pre;
            if (!panel.widgets.isEmpty()) {
                for (Widget widget : panel.widgets) {
                    this.positionSetter.accept(widget, i);
                    i += this.sizeGetter.apply(widget) + this.spacing;
                }
                i -= this.spacing;
            }
            this.padding = this.pre + this.post;
            return i + this.post;
        }
    }
}