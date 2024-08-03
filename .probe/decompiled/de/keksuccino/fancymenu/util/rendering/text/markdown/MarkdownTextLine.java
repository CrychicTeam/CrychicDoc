package de.keksuccino.fancymenu.util.rendering.text.markdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import org.jetbrains.annotations.NotNull;

public class MarkdownTextLine implements Renderable {

    public MarkdownRenderer parent;

    public float offsetX;

    public float offsetY;

    public boolean containsMultilineCodeBlockFragments = false;

    @NotNull
    public MarkdownRenderer.MarkdownLineAlignment alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;

    public boolean bulletListItemStartLine = false;

    public final Map<MarkdownTextFragment.CodeBlockContext, MarkdownTextLine.SingleLineCodeBlockPart> singleLineCodeBlockStartEndPairs = new HashMap();

    public final List<MarkdownTextFragment> fragments = new ArrayList();

    public MarkdownTextLine(@NotNull MarkdownRenderer parent) {
        this.parent = parent;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.onRender(graphics, mouseX, mouseY, partial, true);
    }

    protected void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partial, boolean shouldRender) {
        float textX = this.parent.x + this.offsetX;
        for (MarkdownTextFragment f : this.fragments) {
            f.x = textX;
            f.y = this.parent.y + this.offsetY;
            if (shouldRender) {
                f.render(graphics, mouseX, mouseY, partial);
            }
            textX += f.getRenderWidth();
        }
    }

    public void prepareLine() {
        this.prepareFragments();
        this.onRender(null, 0, 0, 0.0F, false);
    }

    public void prepareFragments() {
        this.containsMultilineCodeBlockFragments = false;
        this.singleLineCodeBlockStartEndPairs.clear();
        this.alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;
        this.bulletListItemStartLine = false;
        for (MarkdownTextFragment f : this.fragments) {
            f.parentLine = this;
            f.startOfRenderLine = false;
            f.autoLineBreakAfter = false;
            if (f.codeBlockContext != null && !f.codeBlockContext.singleLine) {
                this.containsMultilineCodeBlockFragments = true;
            }
            if (f.codeBlockContext != null && f.codeBlockContext.singleLine && !this.singleLineCodeBlockStartEndPairs.containsKey(f.codeBlockContext)) {
                MarkdownTextLine.SingleLineCodeBlockPart part = new MarkdownTextLine.SingleLineCodeBlockPart();
                part.start = f;
                int index = f.codeBlockContext.codeBlockFragments.indexOf(f);
                if (index >= 0) {
                    for (MarkdownTextFragment cf : f.codeBlockContext.codeBlockFragments.subList(index, f.codeBlockContext.codeBlockFragments.size())) {
                        if (!this.fragments.contains(cf)) {
                            break;
                        }
                        if (cf != f) {
                            part.end = cf;
                        }
                    }
                    if (part.end == null) {
                        part.end = f;
                    }
                    this.singleLineCodeBlockStartEndPairs.put(f.codeBlockContext, part);
                }
            }
        }
        if (!this.fragments.isEmpty()) {
            MarkdownTextFragment first = (MarkdownTextFragment) this.fragments.get(0);
            MarkdownTextFragment last = (MarkdownTextFragment) this.fragments.get(this.fragments.size() - 1);
            this.alignment = first.alignment;
            this.bulletListItemStartLine = first.bulletListItemStart;
            first.startOfRenderLine = true;
            last.autoLineBreakAfter = !last.naturalLineBreakAfter;
        }
    }

    public float getLineWidth() {
        float f = 0.0F;
        MarkdownTextFragment last = null;
        for (MarkdownTextFragment frag : this.fragments) {
            f += frag.getRenderWidth();
            last = frag;
        }
        if (last != null && last.text.endsWith(" ")) {
            f -= (float) this.parent.font.width(" ") * last.getScale();
        }
        if (f < 0.0F) {
            f = 0.0F;
        }
        return f;
    }

    public float getLineHeight() {
        float f = 0.0F;
        for (MarkdownTextFragment frag : this.fragments) {
            if (frag.getRenderHeight() > f) {
                f = frag.getRenderHeight();
            }
        }
        return f;
    }

    public boolean isAlignmentAllowed(@NotNull MarkdownRenderer.MarkdownLineAlignment alignment) {
        return alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT ? true : !this.containsMultilineCodeBlockFragments;
    }

    public static class SingleLineCodeBlockPart {

        MarkdownTextFragment start;

        MarkdownTextFragment end;
    }
}