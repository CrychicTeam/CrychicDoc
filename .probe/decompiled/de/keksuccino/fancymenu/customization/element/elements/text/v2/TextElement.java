package de.keksuccino.fancymenu.customization.element.elements.text.v2;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.text.markdown.MarkdownRenderer;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v2.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.konkrete.input.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextElement extends AbstractElement {

    @NotNull
    protected TextElement.SourceMode sourceMode = TextElement.SourceMode.DIRECT;

    @Nullable
    protected String source;

    protected volatile String text;

    protected String lastText;

    @Nullable
    public ResourceSupplier<IText> textResourceSupplier;

    public ResourceSupplier<ITexture> verticalScrollGrabberTextureNormal;

    public ResourceSupplier<ITexture> verticalScrollGrabberTextureHover;

    public ResourceSupplier<ITexture> horizontalScrollGrabberTextureNormal;

    public ResourceSupplier<ITexture> horizontalScrollGrabberTextureHover;

    public String scrollGrabberColorHexNormal;

    public String scrollGrabberColorHexHover;

    public boolean enableScrolling = true;

    public boolean interactable = true;

    @NotNull
    public volatile MarkdownRenderer markdownRenderer = new MarkdownRenderer();

    @NotNull
    public volatile ScrollArea scrollArea = new ScrollArea(0.0F, 0.0F, (float) this.getAbsoluteWidth(), (float) this.getAbsoluteHeight()) {

        @Override
        public void updateScrollArea() {
            super.updateScrollArea();
            if (Minecraft.getInstance().screen != null) {
                this.verticalScrollBar.scrollAreaEndX = (float) (TextElement.this.getAbsoluteX() + TextElement.this.getAbsoluteWidth() + 12);
                this.horizontalScrollBar.scrollAreaEndY = (float) (TextElement.this.getAbsoluteY() + TextElement.this.getAbsoluteHeight() + 12);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (TextElement.isEditor()) {
                return this.verticalScrollBar.mouseClicked(mouseX, mouseY, button) ? true : this.horizontalScrollBar.mouseClicked(mouseX, mouseY, button);
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return TextElement.isEditor() ? false : super.isMouseOver(mouseX, mouseY);
        }
    };

    protected List<String> lastLines;

    protected IText lastIText;

    public TextElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
        this.scrollArea.minimumEntryWidthIsAreaWidth = false;
        this.scrollArea.makeEntriesWidthOfArea = false;
        this.scrollArea.makeAllEntriesWidthOfWidestEntry = false;
        this.scrollArea.verticalScrollBar.grabberWidth = 10.0F;
        this.scrollArea.verticalScrollBar.grabberHeight = 20.0F;
        this.scrollArea.horizontalScrollBar.grabberWidth = 20.0F;
        this.scrollArea.horizontalScrollBar.grabberHeight = 10.0F;
        this.scrollArea.backgroundColor = () -> DrawableColor.of(0, 0, 0, 0);
        this.scrollArea.borderColor = () -> DrawableColor.of(0, 0, 0, 0);
        this.scrollArea.addEntry(new TextElement.MarkdownRendererEntry(this.scrollArea, this.markdownRenderer));
        this.markdownRenderer.addLineRenderValidator(line -> {
            if (line.parent.getY() + line.offsetY + line.getLineHeight() < (float) this.getAbsoluteY()) {
                return false;
            } else {
                return line.parent.getY() + line.offsetY > (float) (this.getAbsoluteY() + this.getAbsoluteHeight()) ? false : true;
            }
        });
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            try {
                this.renderTick();
                this.markdownRenderer.setTextOpacity(this.opacity);
                RenderSystem.enableBlend();
                this.scrollArea.setX((float) this.getAbsoluteX(), true);
                this.scrollArea.setY((float) this.getAbsoluteY(), true);
                this.scrollArea.setWidth((float) this.getAbsoluteWidth(), true);
                this.scrollArea.setHeight((float) this.getAbsoluteHeight(), true);
                this.scrollArea.render(graphics, mouseX, mouseY, partial);
            } catch (Exception var6) {
                var6.printStackTrace();
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    @Nullable
    @Override
    public List<GuiEventListener> getWidgetsToRegister() {
        return this.interactable ? ListUtils.of(this.markdownRenderer, this.scrollArea) : null;
    }

    protected void renderTick() {
        if (this.sourceMode == TextElement.SourceMode.RESOURCE) {
            IText iText = this.textResourceSupplier != null ? this.textResourceSupplier.get() : null;
            List<String> lines = iText != null ? iText.getTextLines() : null;
            if (lines != null) {
                lines = new ArrayList(lines);
            }
            if (!Objects.equals(this.lastIText, iText) || !Objects.equals(this.lastLines, lines)) {
                this.updateContent();
            }
            this.lastLines = lines;
            this.lastIText = iText;
        }
        String t = this.text;
        if (t != null && (this.lastText == null || !this.lastText.equals(t))) {
            this.markdownRenderer.setText(t);
        }
        this.lastText = t;
        this.scrollArea.verticalScrollBar.setScrollWheelAllowed(this.enableScrolling);
        this.scrollArea.verticalScrollBar.active = this.scrollArea.getTotalEntryHeight() > this.scrollArea.getInnerHeight() && this.enableScrolling;
        this.scrollArea.horizontalScrollBar.active = this.scrollArea.getTotalEntryWidth() > this.scrollArea.getInnerWidth() && this.enableScrolling;
        if (this.scrollGrabberColorHexNormal != null) {
            DrawableColor c = DrawableColor.of(this.scrollGrabberColorHexNormal);
            this.scrollArea.verticalScrollBar.idleBarColor = () -> c;
            this.scrollArea.horizontalScrollBar.idleBarColor = () -> c;
        }
        if (this.scrollGrabberColorHexHover != null) {
            DrawableColor c = DrawableColor.of(this.scrollGrabberColorHexHover);
            this.scrollArea.verticalScrollBar.hoverBarColor = () -> c;
            this.scrollArea.horizontalScrollBar.hoverBarColor = () -> c;
        }
        if (this.verticalScrollGrabberTextureNormal != null) {
            ITexture r = this.verticalScrollGrabberTextureNormal.get();
            if (r != null) {
                this.scrollArea.verticalScrollBar.idleBarTexture = r.getResourceLocation();
            }
        } else {
            this.scrollArea.verticalScrollBar.idleBarTexture = null;
        }
        if (this.verticalScrollGrabberTextureHover != null) {
            ITexture r = this.verticalScrollGrabberTextureHover.get();
            if (r != null) {
                this.scrollArea.verticalScrollBar.hoverBarTexture = r.getResourceLocation();
            }
        } else {
            this.scrollArea.verticalScrollBar.hoverBarTexture = null;
        }
        if (this.horizontalScrollGrabberTextureNormal != null) {
            ITexture r = this.horizontalScrollGrabberTextureNormal.get();
            if (r != null) {
                this.scrollArea.horizontalScrollBar.idleBarTexture = r.getResourceLocation();
            }
        } else {
            this.scrollArea.horizontalScrollBar.idleBarTexture = null;
        }
        if (this.horizontalScrollGrabberTextureHover != null) {
            ITexture r = this.horizontalScrollGrabberTextureHover.get();
            if (r != null) {
                this.scrollArea.horizontalScrollBar.hoverBarTexture = r.getResourceLocation();
            }
        } else {
            this.scrollArea.horizontalScrollBar.hoverBarTexture = null;
        }
    }

    public void updateContent() {
        if (this.source == null) {
            this.markdownRenderer.setText(I18n.get("fancymenu.customization.items.text.placeholder"));
        } else {
            new Thread(() -> {
                List<String> linesRaw = new ArrayList();
                try {
                    if (this.source == null || this.source.equals("")) {
                        linesRaw.add(I18n.get("fancymenu.customization.items.text.placeholder"));
                    } else if (this.sourceMode == TextElement.SourceMode.DIRECT) {
                        String s = this.source.replace("%n%", "\n").replace("\r", "\n");
                        if (s.contains("\n")) {
                            linesRaw.addAll(Arrays.asList(StringUtils.splitLines(s, "\n")));
                        } else {
                            linesRaw.add(s);
                        }
                    } else if (this.textResourceSupplier != null) {
                        IText iText = this.textResourceSupplier.get();
                        if (iText != null) {
                            linesRaw = iText.getTextLines();
                        }
                        linesRaw = linesRaw != null ? new ArrayList(linesRaw) : new ArrayList();
                    }
                } catch (Exception var5) {
                    if (linesRaw == null) {
                        linesRaw = new ArrayList();
                    }
                    linesRaw.clear();
                }
                if (linesRaw.isEmpty()) {
                    if (isEditor()) {
                        linesRaw.add(I18n.get("fancymenu.customization.items.text.status.unable_to_load"));
                    } else {
                        linesRaw.add("");
                    }
                }
                StringBuilder text = new StringBuilder();
                for (String s : linesRaw) {
                    if (text.length() > 0) {
                        text.append("\n");
                    }
                    text.append(s);
                }
                this.text = text.toString();
            }).start();
        }
    }

    public void setSource(@NotNull TextElement.SourceMode sourceMode, @Nullable String source) {
        this.sourceMode = (TextElement.SourceMode) Objects.requireNonNull(sourceMode);
        this.source = source;
        this.textResourceSupplier = null;
        if (sourceMode == TextElement.SourceMode.RESOURCE && this.source != null) {
            this.textResourceSupplier = ResourceSupplier.text(this.source);
        }
        this.text = null;
        this.lastText = null;
        this.lastIText = null;
        this.lastLines = null;
        this.updateContent();
    }

    protected static class MarkdownRendererEntry extends ScrollAreaEntry {

        protected MarkdownRenderer markdownRenderer;

        public MarkdownRendererEntry(ScrollArea parent, MarkdownRenderer markdownRenderer) {
            super(parent, 20.0F, 20.0F);
            this.markdownRenderer = markdownRenderer;
            this.selectable = false;
            this.playClickSound = false;
            this.backgroundColorNormal = () -> DrawableColor.of(0, 0, 0, 0);
            this.backgroundColorHover = () -> DrawableColor.of(0, 0, 0, 0);
        }

        @Override
        public void renderEntry(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.markdownRenderer.setOptimalWidth(this.parent.getInnerWidth());
            this.markdownRenderer.setX(this.x);
            this.markdownRenderer.setY(this.y);
            this.setWidth(this.markdownRenderer.getRealWidth());
            this.setHeight(this.markdownRenderer.getRealHeight());
            this.markdownRenderer.render(graphics, mouseX, mouseY, partial);
        }

        @Override
        public void onClick(ScrollAreaEntry entry, double mouseX, double mouseY, int button) {
        }
    }

    public static enum SourceMode implements LocalizedCycleEnum<TextElement.SourceMode> {

        DIRECT("direct"), RESOURCE("resource");

        final String name;

        private SourceMode(@NotNull String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public TextElement.SourceMode[] getValues() {
            return values();
        }

        @Nullable
        public TextElement.SourceMode getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static TextElement.SourceMode getByName(String name) {
            for (TextElement.SourceMode i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.elements.text.v2.source_mode";
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) WARNING_TEXT_STYLE.get();
        }
    }
}