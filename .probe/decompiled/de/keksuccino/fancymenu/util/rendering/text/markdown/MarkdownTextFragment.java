package de.keksuccino.fancymenu.util.rendering.text.markdown;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.WebUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.cursor.CursorHandler;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MarkdownTextFragment implements Renderable, GuiEventListener {

    protected static final int BULLET_LIST_SPACE_AFTER_INDENT = 5;

    public final MarkdownRenderer parent;

    public MarkdownTextLine parentLine;

    public String text;

    public float x;

    public float y;

    public float unscaledTextWidth;

    public float unscaledTextHeight;

    public boolean startOfRenderLine = false;

    public boolean naturalLineBreakAfter;

    public boolean autoLineBreakAfter;

    public boolean endOfWord;

    public ResourceSupplier<ITexture> imageSupplier = null;

    public boolean separationLine;

    public DrawableColor textColor = null;

    public boolean bold;

    public boolean italic;

    public boolean strikethrough;

    public boolean bulletListItemStart = false;

    public int bulletListLevel = 0;

    @NotNull
    public MarkdownRenderer.MarkdownLineAlignment alignment = MarkdownRenderer.MarkdownLineAlignment.LEFT;

    public MarkdownTextFragment.Hyperlink hyperlink = null;

    @NotNull
    public MarkdownTextFragment.HeadlineType headlineType = MarkdownTextFragment.HeadlineType.NONE;

    public MarkdownTextFragment.QuoteContext quoteContext = null;

    public MarkdownTextFragment.CodeBlockContext codeBlockContext = null;

    public ResourceLocation font = null;

    public boolean hovered = false;

    public MarkdownTextFragment(@NotNull MarkdownRenderer parent, @NotNull String text) {
        this.parent = parent;
        this.text = text;
        this.unscaledTextHeight = 9.0F;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.hovered = this.isMouseOver((double) mouseX, (double) mouseY);
        if (this.hyperlink != null && this.hovered) {
            CursorHandler.setClientTickCursor(CursorHandler.CURSOR_POINTING_HAND);
        }
        if (this.imageSupplier != null) {
            this.imageSupplier.forRenderable((iTexture, location) -> {
                RenderSystem.enableBlend();
                graphics.setColor(1.0F, 1.0F, 1.0F, this.parent.textOpacity);
                RenderingUtils.blitF(graphics, location, this.x, this.y, 0.0F, 0.0F, this.getRenderWidth(), this.getRenderHeight(), this.getRenderWidth(), this.getRenderHeight());
                RenderingUtils.resetShaderColor(graphics);
            });
        } else if (this.separationLine) {
            RenderSystem.enableBlend();
            RenderingUtils.fillF(graphics, this.parent.x + this.parent.border, this.y, this.parent.x + this.parent.getRealWidth() - this.parent.border, this.y + this.getRenderHeight(), this.parent.separationLineColor.getColorIntWithAlpha(this.parent.textOpacity));
            RenderingUtils.resetShaderColor(graphics);
        } else {
            this.renderCodeBlock(graphics);
            RenderSystem.enableBlend();
            graphics.pose().pushPose();
            graphics.pose().scale(this.getScale(), this.getScale(), this.getScale());
            graphics.drawString(this.parent.font, this.buildRenderComponent(false), (int) this.getTextRenderX(), (int) this.getTextRenderY(), this.parent.textBaseColor.getColorIntWithAlpha(this.parent.textOpacity), this.parent.textShadow && this.codeBlockContext == null);
            graphics.pose().popPose();
            RenderingUtils.resetShaderColor(graphics);
            this.renderQuoteLine(graphics);
            this.renderBulletListDot(graphics);
            this.renderHeadlineUnderline(graphics);
        }
    }

    protected void renderCodeBlock(GuiGraphics graphics) {
        if (this.codeBlockContext != null && this.parentLine != null) {
            MarkdownTextFragment start = this.codeBlockContext.getBlockStart();
            MarkdownTextFragment end = this.codeBlockContext.getBlockEnd();
            if (this.codeBlockContext.singleLine) {
                MarkdownTextLine.SingleLineCodeBlockPart part = (MarkdownTextLine.SingleLineCodeBlockPart) this.parentLine.singleLineCodeBlockStartEndPairs.get(this.codeBlockContext);
                if (part == null) {
                    return;
                }
                start = part.start;
                end = part.end;
            }
            if (start != this) {
                return;
            }
            if (end == null) {
                return;
            }
            if (this.codeBlockContext.singleLine) {
                float xEnd = end.x + end.getRenderWidth();
                if (end.text.endsWith(" ")) {
                    xEnd -= (float) this.parent.font.width(" ") * this.getScale();
                }
                this.renderCodeBlockBackground(graphics, this.x, this.y - 2.0F, xEnd, this.y + this.getTextRenderHeight(), this.parent.codeBlockSingleLineColor.getColorIntWithAlpha(this.parent.textOpacity));
            } else {
                this.renderCodeBlockBackground(graphics, this.parent.x + this.parent.border, this.y, this.parent.x + this.parent.getRealWidth() - this.parent.border - 1.0F, end.y + end.getRenderHeight() - 1.0F, this.parent.codeBlockMultiLineColor.getColorIntWithAlpha(this.parent.textOpacity));
            }
        }
    }

    protected void renderCodeBlockBackground(GuiGraphics graphics, float minX, float minY, float maxX, float maxY, int color) {
        RenderSystem.enableBlend();
        RenderingUtils.fillF(graphics, minX + 1.0F, minY, maxX - 1.0F, minY + 1.0F, color);
        RenderingUtils.fillF(graphics, minX, minY + 1.0F, maxX, maxY - 1.0F, color);
        RenderingUtils.fillF(graphics, minX + 1.0F, maxY - 1.0F, maxX - 1.0F, maxY, color);
        RenderingUtils.resetShaderColor(graphics);
    }

    protected void renderHeadlineUnderline(GuiGraphics graphics) {
        if (this.startOfRenderLine && (this.headlineType == MarkdownTextFragment.HeadlineType.BIGGER || this.headlineType == MarkdownTextFragment.HeadlineType.BIGGEST)) {
            RenderSystem.enableBlend();
            float scale = this.parent.parentRenderScale != null ? this.parent.parentRenderScale : (float) Minecraft.getInstance().getWindow().getGuiScale();
            float lineThickness = scale > 1.0F ? 0.5F : 1.0F;
            float lineY = this.y + this.getTextRenderHeight() + 1.0F;
            RenderingUtils.fillF(graphics, this.parent.x + this.parent.border, lineY, this.parent.x + this.parent.getRealWidth() - this.parent.border - 1.0F, lineY + lineThickness, this.parent.headlineUnderlineColor.getColorIntWithAlpha(this.parent.textOpacity));
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderQuoteLine(GuiGraphics graphics) {
        if (this.quoteContext != null && this.quoteContext.getQuoteEnd() != null && this.quoteContext.getQuoteEnd() == this) {
            float yStart = ((MarkdownTextFragment) Objects.requireNonNull(this.quoteContext.getQuoteStart())).y - 2.0F;
            float yEnd = this.y + this.getRenderHeight() + 1.0F;
            RenderSystem.enableBlend();
            if (this.alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT) {
                RenderingUtils.fillF(graphics, this.parent.x, yStart, this.parent.x + 2.0F, yEnd, this.parent.quoteColor.getColorIntWithAlpha(this.parent.textOpacity));
            } else if (this.alignment == MarkdownRenderer.MarkdownLineAlignment.RIGHT) {
                RenderingUtils.fillF(graphics, this.parent.x + this.parent.getRealWidth() - this.parent.border - 2.0F, yStart, this.parent.x + this.parent.getRealWidth() - this.parent.border - 1.0F, yEnd, this.parent.quoteColor.getColorIntWithAlpha(this.parent.textOpacity));
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderBulletListDot(GuiGraphics graphics) {
        if (this.bulletListLevel > 0 && this.bulletListItemStart) {
            RenderSystem.enableBlend();
            float yStart = this.getTextRenderY() + this.getTextRenderHeight() / 2.0F - 2.0F;
            RenderingUtils.fillF(graphics, this.getTextRenderX() - 5.0F - 3.0F, yStart, this.getTextRenderX() - 5.0F, yStart + 3.0F, this.parent.bulletListDotColor.getColorIntWithAlpha(this.parent.textOpacity));
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    @NotNull
    protected Component buildRenderComponent(boolean forWidthCalculation) {
        Style style = Style.EMPTY;
        if (this.font != null) {
            style = style.withFont(this.font);
        }
        if (this.italic) {
            style = style.withItalic(true);
        }
        if (this.bold) {
            style = style.withBold(true);
        }
        if (this.strikethrough && !forWidthCalculation) {
            style = style.withStrikethrough(true);
        }
        if (this.quoteContext != null) {
            style = style.withColor(this.parent.quoteColor.getColorInt());
            if (this.parent.quoteItalic) {
                style = style.withItalic(true);
            }
        }
        if (this.textColor != null) {
            style = style.withColor(this.textColor.getColorInt());
        }
        if (this.hyperlink != null) {
            style = style.withColor(this.parent.hyperlinkColor.getColorInt());
            if (this.hyperlink.isHovered()) {
                style = style.withUnderlined(true);
            }
        }
        boolean addSpaceComponentAtEnd = false;
        String t = this.text;
        if (this.hyperlink != null && (this.naturalLineBreakAfter || this.autoLineBreakAfter) && t.endsWith(" ")) {
            t = t.substring(0, t.length() - 1);
        } else if (this.hyperlink != null && ListUtils.getLast(this.hyperlink.hyperlinkFragments) == this && t.endsWith(" ")) {
            t = t.substring(0, t.length() - 1);
            addSpaceComponentAtEnd = true;
        }
        if (this.codeBlockContext != null) {
            style = Style.EMPTY;
        }
        if (this.parent.textCase == MarkdownRenderer.TextCase.ALL_UPPER) {
            t = t.toUpperCase();
        }
        if (this.parent.textCase == MarkdownRenderer.TextCase.ALL_LOWER) {
            t = t.toLowerCase();
        }
        MutableComponent comp = Component.literal(t).setStyle(style);
        if (addSpaceComponentAtEnd) {
            comp.append(Component.literal(" ").setStyle(Style.EMPTY.withUnderlined(false)));
        }
        return comp;
    }

    protected void updateWidth() {
        this.unscaledTextWidth = (float) this.parent.font.width(this.buildRenderComponent(true));
    }

    public float getTextRenderX() {
        float f = this.x / this.getScale();
        if (this.quoteContext != null && this.startOfRenderLine && this.alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT) {
            f += this.parent.quoteIndent;
        }
        if (this.bulletListLevel > 0 && this.startOfRenderLine) {
            f += this.parent.bulletListIndent * (float) this.bulletListLevel + 5.0F;
        }
        if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && this.startOfRenderLine) {
            f += 10.0F;
        }
        if (this.codeBlockContext != null && this.codeBlockContext.singleLine && this.codeBlockContext.getBlockStart() == this) {
            f++;
        }
        return (float) ((int) f);
    }

    public float getTextRenderY() {
        float f = this.y / this.getScale();
        if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && this.codeBlockContext.getBlockStart() != null && this.codeBlockContext.getBlockStart().y == this.y) {
            f += 10.0F;
        }
        if (this.bulletListLevel > 0 && this.parentLine != null && this.parentLine.bulletListItemStartLine) {
            f += this.parent.bulletListSpacing;
        }
        return (float) ((int) f);
    }

    public float getRenderWidth() {
        if (this.imageSupplier != null) {
            ITexture t = this.imageSupplier.get();
            if (t == null) {
                return 10.0F;
            } else {
                return (float) t.getWidth() <= this.parent.getRealWidth() - this.parent.border - this.parent.border ? (float) t.getWidth() : this.parent.getRealWidth() - this.parent.border - this.parent.border;
            }
        } else {
            float f = this.getTextRenderWidth();
            if (this.quoteContext != null && this.startOfRenderLine && this.alignment == MarkdownRenderer.MarkdownLineAlignment.LEFT) {
                f += this.parent.quoteIndent;
            }
            if (this.quoteContext != null && (this.naturalLineBreakAfter || this.autoLineBreakAfter) && this.alignment == MarkdownRenderer.MarkdownLineAlignment.RIGHT) {
                f += this.parent.quoteIndent;
            }
            if (this.bulletListLevel > 0 && this.startOfRenderLine) {
                f += this.parent.bulletListIndent * (float) this.bulletListLevel + 5.0F;
            }
            if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && this.startOfRenderLine) {
                f += 10.0F;
            }
            if (this.codeBlockContext != null && this.codeBlockContext.singleLine && this.codeBlockContext.getBlockStart() == this) {
                f++;
            }
            if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && (this.autoLineBreakAfter || this.naturalLineBreakAfter)) {
                f += 10.0F;
            }
            if (this.codeBlockContext != null && this.codeBlockContext.singleLine && this.codeBlockContext.getBlockEnd() == this) {
                f++;
            }
            return f;
        }
    }

    public float getRenderHeight() {
        if (this.imageSupplier != null) {
            ITexture t = this.imageSupplier.get();
            return t == null ? 10.0F : (float) t.getAspectRatio().getAspectRatioHeight((int) this.getRenderWidth());
        } else {
            float f = this.getTextRenderHeight();
            if (this.headlineType == MarkdownTextFragment.HeadlineType.BIGGER || this.headlineType == MarkdownTextFragment.HeadlineType.BIGGEST) {
                f += 8.0F;
            }
            if (this.headlineType == MarkdownTextFragment.HeadlineType.BIG) {
                f += 6.0F;
            }
            if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && this.codeBlockContext.getBlockStart() != null && this.codeBlockContext.getBlockStart().y == this.y) {
                f += 10.0F;
            }
            if (this.codeBlockContext != null && !this.codeBlockContext.singleLine && this.codeBlockContext.getBlockEnd() != null && this.codeBlockContext.getBlockEnd().y == this.y) {
                f += 10.0F;
            }
            if (this.bulletListLevel > 0 && this.bulletListItemStart) {
                f += this.parent.bulletListSpacing;
            }
            return f;
        }
    }

    public float getTextRenderWidth() {
        return this.unscaledTextWidth * this.getScale();
    }

    public float getTextRenderHeight() {
        return this.unscaledTextHeight * this.getScale();
    }

    public float getTextX() {
        float f = this.getTextRenderX();
        f -= this.x / this.getScale();
        return f + this.x;
    }

    public float getTextY() {
        float f = this.getTextRenderY();
        f -= this.y / this.getScale();
        return f + this.y;
    }

    public float getTextWidth() {
        return this.getTextRenderWidth();
    }

    public float getTextHeight() {
        return this.getTextRenderHeight();
    }

    public float getScale() {
        float f = 1.0F;
        if (this.headlineType == MarkdownTextFragment.HeadlineType.BIG) {
            f = 1.2F;
        }
        if (this.headlineType == MarkdownTextFragment.HeadlineType.BIGGER) {
            f = 1.6F;
        }
        if (this.headlineType == MarkdownTextFragment.HeadlineType.BIGGEST) {
            f = 2.0F;
        }
        return f * this.parent.textBaseScale;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.imageSupplier != null && this.imageSupplier.get() != null ? RenderingUtils.isXYInArea(mouseX, mouseY, (double) this.x, (double) this.y, (double) this.getRenderWidth(), (double) this.getRenderHeight()) : RenderingUtils.isXYInArea(mouseX, mouseY, (double) this.getTextX(), (double) this.getTextY(), (double) this.getTextWidth(), (double) this.getTextHeight());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.hyperlink != null && this.hovered) {
            WebUtils.openWebLink(this.hyperlink.link);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public static class CodeBlockContext {

        public final List<MarkdownTextFragment> codeBlockFragments = new ArrayList();

        public boolean singleLine = true;

        @Nullable
        public MarkdownTextFragment getBlockStart() {
            return !this.codeBlockFragments.isEmpty() ? (MarkdownTextFragment) this.codeBlockFragments.get(0) : null;
        }

        @Nullable
        public MarkdownTextFragment getBlockEnd() {
            return !this.codeBlockFragments.isEmpty() ? (MarkdownTextFragment) this.codeBlockFragments.get(this.codeBlockFragments.size() - 1) : null;
        }
    }

    public static enum HeadlineType {

        NONE, BIG, BIGGER, BIGGEST
    }

    public static class Hyperlink {

        public String link = null;

        public final List<MarkdownTextFragment> hyperlinkFragments = new ArrayList();

        public boolean isHovered() {
            for (MarkdownTextFragment f : this.hyperlinkFragments) {
                if (f.hovered) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class QuoteContext {

        public final List<MarkdownTextFragment> quoteFragments = new ArrayList();

        @Nullable
        public MarkdownTextFragment getQuoteStart() {
            return !this.quoteFragments.isEmpty() ? (MarkdownTextFragment) this.quoteFragments.get(0) : null;
        }

        @Nullable
        public MarkdownTextFragment getQuoteEnd() {
            return !this.quoteFragments.isEmpty() ? (MarkdownTextFragment) this.quoteFragments.get(this.quoteFragments.size() - 1) : null;
        }
    }
}