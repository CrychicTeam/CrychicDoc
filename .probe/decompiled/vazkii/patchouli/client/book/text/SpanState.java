package vazkii.patchouli.client.book.text;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IStyleStack;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;

public class SpanState implements IStyleStack {

    public final GuiBook gui;

    public final Book book;

    private Style baseStyle;

    private final Deque<SpanState.SpanPartialState> stateStack = new ArrayDeque();

    public MutableComponent tooltip = BookTextParser.EMPTY_STRING_COMPONENT;

    public Supplier<Boolean> onClick = null;

    public List<Span> cluster = null;

    public boolean isExternalLink = false;

    public boolean endingExternal = false;

    public int lineBreaks = 0;

    public int spacingLeft = 0;

    public int spacingRight = 0;

    public final int spaceWidth;

    public SpanState(GuiBook gui, Book book, Style baseStyle) {
        this.gui = gui;
        this.book = book;
        this.baseStyle = baseStyle;
        this.stateStack.push(new SpanState.SpanPartialState(baseStyle, null));
        this.spaceWidth = Minecraft.getInstance().font.width(Component.literal(" ").setStyle(baseStyle));
    }

    public Style getBase() {
        return this.baseStyle;
    }

    public void changeBaseStyle(Style newStyle) {
        this.baseStyle = newStyle;
        for (SpanState.SpanPartialState state : this.stateStack) {
            state.replaceBase(newStyle);
            newStyle = state.getCurrentStyle();
        }
    }

    public void color(TextColor color) {
        this.modifyStyle(s -> s.withColor(color));
    }

    public void baseColor() {
        this.color(this.baseStyle.getColor());
    }

    @Override
    public void modifyStyle(UnaryOperator<Style> f) {
        ((SpanState.SpanPartialState) this.stateStack.peek()).addModification(f);
    }

    @Override
    public void pushStyle(Style style) {
        this.stateStack.push(new SpanState.SpanPartialState(style.applyTo(this.peekStyle()), style));
    }

    @Override
    public Style popStyle() {
        if (this.stateStack.size() <= 1) {
            throw new IllegalStateException("Underflow in style stack");
        } else {
            return ((SpanState.SpanPartialState) this.stateStack.pop()).getCurrentStyle();
        }
    }

    @Override
    public void reset() {
        this.endingExternal = this.isExternalLink;
        this.stateStack.clear();
        this.stateStack.push(new SpanState.SpanPartialState(this.baseStyle, null));
        this.cluster = null;
        this.tooltip = BookTextParser.EMPTY_STRING_COMPONENT;
        this.onClick = null;
        this.isExternalLink = false;
    }

    @Override
    public Style peekStyle() {
        return ((SpanState.SpanPartialState) this.stateStack.peek()).getCurrentStyle();
    }

    private static class SpanPartialState {

        private Style currentStyle;

        @Nullable
        private final Style mergeStyle;

        @Nullable
        private List<UnaryOperator<Style>> transformations = null;

        public SpanPartialState(Style currentStyle, Style mergeStyle) {
            this.currentStyle = currentStyle;
            this.mergeStyle = mergeStyle;
        }

        public Style getCurrentStyle() {
            return this.currentStyle;
        }

        public void addModification(UnaryOperator<Style> f) {
            if (this.transformations == null) {
                this.transformations = new LinkedList();
            }
            this.transformations.add(f);
            this.currentStyle = (Style) f.apply(this.currentStyle);
        }

        public void replaceBase(Style style) {
            if (this.mergeStyle != null) {
                style = this.mergeStyle.applyTo(style);
            }
            if (this.transformations != null) {
                for (UnaryOperator<Style> f : this.transformations) {
                    style = (Style) f.apply(style);
                }
            }
            this.currentStyle = style;
        }
    }
}