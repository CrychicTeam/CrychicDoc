package vazkii.patchouli.client.book.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import vazkii.patchouli.api.PatchouliConfigAccess;
import vazkii.patchouli.client.book.gui.GuiBook;

public class TextLayouter {

    private final List<Word> words = new ArrayList();

    private final GuiBook gui;

    private final int pageX;

    private final int pageY;

    private final int lineHeight;

    private final int basePageWidth;

    private int y;

    private int pageWidth;

    private List<Word> linkCluster = null;

    private List<Span> spanCluster = null;

    private final PatchouliConfigAccess.TextOverflowMode mode;

    private int smallestOverstep;

    private final List<TextLayouter.SpanTail> pending = new ArrayList();

    private int lineStart = 0;

    private int widthSoFar = 0;

    private Font font;

    public TextLayouter(GuiBook gui, int pageX, int pageY, int lineHeight, int pageWidth, PatchouliConfigAccess.TextOverflowMode mode) {
        this.gui = gui;
        this.pageX = pageX;
        this.lineHeight = lineHeight;
        this.basePageWidth = pageWidth;
        this.mode = mode;
        this.pageY = pageY;
    }

    public void layout(Font font, List<Span> spans) {
        this.pageWidth = this.basePageWidth;
        this.font = font;
        do {
            this.y = this.pageY;
            this.words.clear();
            this.smallestOverstep = Integer.MAX_VALUE;
            List<Span> paragraph = new ArrayList();
            for (Span span : spans) {
                if (span.lineBreaks > 0) {
                    this.layoutParagraph(paragraph);
                    this.widthSoFar = 0;
                    this.y = this.y + span.lineBreaks * this.lineHeight;
                    paragraph.clear();
                }
                paragraph.add(span);
            }
            if (!paragraph.isEmpty()) {
                this.layoutParagraph(paragraph);
            }
        } while (this.mode == PatchouliConfigAccess.TextOverflowMode.RESIZE && this.getOverflow() * this.getScale() > 1.0F && this.adjustScale());
        if (this.mode == PatchouliConfigAccess.TextOverflowMode.TRUNCATE) {
            this.words.removeIf(word -> word.y + this.lineHeight > 156);
        }
    }

    private boolean adjustScale() {
        this.pageWidth = 1 + Math.min(this.smallestOverstep, (int) ((float) this.basePageWidth * this.getOverflow()));
        return true;
    }

    public float getScale() {
        return (float) this.basePageWidth / (float) this.pageWidth;
    }

    private float getOverflow() {
        return (float) (this.y + this.lineHeight - this.pageY) / (float) (156 - this.pageY);
    }

    private void layoutParagraph(List<Span> paragraph) {
        String text = this.toString(paragraph);
        Locale locale = new Locale(Minecraft.getInstance().getLanguageManager().getSelected());
        BreakIterator iterator = BreakIterator.getLineInstance(locale);
        iterator.setText(text);
        this.lineStart = 0;
        for (Span span : paragraph) {
            this.layoutSpan(iterator, span);
        }
        this.flush();
    }

    private void layoutSpan(BreakIterator iterator, Span span) {
        if (this.spanCluster != span.linkCluster) {
            this.linkCluster = span.linkCluster == null ? null : new ArrayList();
            this.spanCluster = span.linkCluster;
        }
        TextLayouter.SpanTail last = new TextLayouter.SpanTail(span, 0, this.linkCluster);
        this.widthSoFar = this.widthSoFar + last.width;
        this.pending.add(last);
        while (this.widthSoFar > this.pageWidth) {
            this.breakLine(iterator);
            this.widthSoFar = 0;
            for (TextLayouter.SpanTail pending : this.pending) {
                this.widthSoFar = this.widthSoFar + pending.width;
            }
        }
    }

    private void breakLine(BreakIterator iterator) {
        int width = 0;
        int offset = 0;
        for (TextLayouter.SpanTail pending : this.pending) {
            width += pending.width;
            offset += pending.length;
        }
        TextLayouter.SpanTail last = (TextLayouter.SpanTail) this.pending.get(this.pending.size() - 1);
        width -= last.width;
        offset -= last.length;
        char[] characters = last.span.text.toCharArray();
        for (int i = last.start; i < characters.length; i++) {
            Component tmp = Component.literal(String.valueOf(characters[i])).setStyle(last.span.style);
            width += this.font.width(tmp);
            if (last.span.bold) {
                width++;
            }
            if (width > this.pageWidth) {
                this.smallestOverstep = Math.min(width, this.smallestOverstep);
                int overflowOffset = this.lineStart + offset + i - last.start;
                int breakOffset = overflowOffset + 1;
                if (!Character.isWhitespace(characters[i])) {
                    breakOffset = iterator.preceding(breakOffset);
                }
                if (breakOffset <= this.lineStart) {
                    breakOffset = overflowOffset - 1;
                }
                this.breakLine(breakOffset);
                return;
            }
        }
        this.flush();
        this.y = this.y + this.lineHeight;
    }

    private String toString(List<Span> paragraph) {
        StringBuilder result = new StringBuilder();
        for (Span span : paragraph) {
            result.append(span.text);
        }
        return result.toString();
    }

    public void flush() {
        if (!this.pending.isEmpty()) {
            int x = this.pageX;
            for (TextLayouter.SpanTail pending : this.pending) {
                this.words.add(pending.position(this.gui, x, this.y, pending.length));
                x += pending.width;
            }
            this.pending.clear();
        }
    }

    private void breakLine(int textOffset) {
        int offset = this.lineStart;
        int x = this.pageX;
        int index;
        for (index = 0; index < this.pending.size(); index++) {
            TextLayouter.SpanTail span = (TextLayouter.SpanTail) this.pending.get(index);
            if (offset + span.length >= textOffset) {
                this.words.add(span.position(this.gui, x, this.y, textOffset - offset));
                this.pending.set(index, span.tail(textOffset - offset));
                break;
            }
            this.words.add(span.position(this.gui, x, this.y, span.length));
            offset += span.length;
            x += span.width;
        }
        for (int i = index - 1; i >= 0; i--) {
            this.pending.remove(i);
        }
        this.lineStart = textOffset;
        this.y = this.y + this.lineHeight;
    }

    public List<Word> getWords() {
        return this.words;
    }

    private class SpanTail {

        private final Span span;

        private final int start;

        private final int width;

        private final List<Word> cluster;

        private final int length;

        public SpanTail(Span span, int start, List<Word> cluster) {
            this.span = span;
            this.start = start;
            this.width = TextLayouter.this.font.width(span.styledSubstring(start)) + span.spacingLeft + span.spacingRight;
            this.cluster = cluster;
            this.length = span.text.length() - start;
        }

        public Word position(GuiBook gui, int x, int y, int length) {
            x += this.span.spacingLeft;
            Word result = new Word(gui, this.span, this.span.styledSubstring(this.start, this.start + length), x, y, this.width, this.cluster);
            if (this.cluster != null) {
                this.cluster.add(result);
            }
            return result;
        }

        public TextLayouter.SpanTail tail(int offset) {
            return TextLayouter.this.new SpanTail(this.span, this.start + offset, this.cluster);
        }
    }
}