package de.keksuccino.fancymenu.customization.element.elements.text.v1;

import com.google.common.collect.LinkedListMultimap;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextScrollAreaEntry;
import de.keksuccino.fancymenu.util.resource.ResourceHandlers;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.threading.MainThreadTaskExecutor;
import de.keksuccino.konkrete.file.FileUtils;
import de.keksuccino.konkrete.input.MouseInput;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.rendering.RenderUtils;
import de.keksuccino.konkrete.web.WebUtils;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class TextElement extends AbstractElement {

    public TextElement.SourceMode sourceMode = TextElement.SourceMode.DIRECT;

    public String source;

    public boolean shadow = true;

    public TextElement.CaseMode caseMode = TextElement.CaseMode.NORMAL;

    public float scale = 1.0F;

    public AbstractElement.Alignment alignment = AbstractElement.Alignment.LEFT;

    public String baseColorHex = null;

    public int textBorder = 0;

    public int lineSpacing = 1;

    public String scrollGrabberTextureNormal = null;

    public String scrollGrabberTextureHover = null;

    public String scrollGrabberColorHexNormal = null;

    public String scrollGrabberColorHexHover = null;

    public boolean enableScrolling = true;

    protected Font font;

    public volatile ScrollArea scrollArea;

    public volatile LinkedListMultimap<String, Float> lines;

    public volatile boolean updating;

    public TextElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
        this.font = Minecraft.getInstance().font;
        this.lines = LinkedListMultimap.create();
        this.updating = false;
    }

    public void updateScrollArea() {
        this.scrollArea = new ScrollArea(0, 0, this.getAbsoluteWidth(), this.getAbsoluteHeight()) {

            @Override
            public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                super.render(graphics, mouseX, mouseY, partial);
                this.verticalScrollBar.active = this.getTotalEntryHeight() > this.getInnerHeight() && TextElement.this.enableScrolling;
            }

            @Override
            public void updateScrollArea() {
                super.updateScrollArea();
                if (Minecraft.getInstance().screen != null) {
                    this.verticalScrollBar.scrollAreaEndX = TextElement.this.getAbsoluteX() + TextElement.this.getAbsoluteWidth() + 12;
                    this.horizontalScrollBar.scrollAreaEndY = TextElement.this.getAbsoluteY() + TextElement.this.getAbsoluteHeight() + 12;
                }
            }
        };
        this.scrollArea.verticalScrollBar.grabberWidth = 10;
        this.scrollArea.verticalScrollBar.grabberHeight = 20;
        this.scrollArea.horizontalScrollBar.grabberWidth = 20;
        this.scrollArea.horizontalScrollBar.grabberHeight = 10;
        this.scrollArea.backgroundColor = new Color(0, 0, 0, 0);
        this.scrollArea.borderColor = new Color(0, 0, 0, 0);
        this.scrollArea.verticalScrollBar.setScrollWheelAllowed(this.enableScrolling);
        this.scrollArea.verticalScrollBar.active = false;
        this.scrollArea.horizontalScrollBar.active = false;
        if (this.scrollGrabberColorHexNormal != null) {
            Color c = RenderUtils.getColorFromHexString(this.scrollGrabberColorHexNormal);
            if (c != null) {
                this.scrollArea.verticalScrollBar.idleBarColor = c;
                this.scrollArea.horizontalScrollBar.idleBarColor = c;
            }
        }
        if (this.scrollGrabberColorHexHover != null) {
            Color c = RenderUtils.getColorFromHexString(this.scrollGrabberColorHexHover);
            if (c != null) {
                this.scrollArea.verticalScrollBar.hoverBarColor = c;
                this.scrollArea.horizontalScrollBar.hoverBarColor = c;
            }
        }
        if (this.scrollGrabberTextureNormal != null) {
            ITexture r = ResourceHandlers.getImageHandler().get(this.scrollGrabberTextureNormal);
            if (r != null) {
                this.scrollArea.verticalScrollBar.idleBarTexture = r.getResourceLocation();
                this.scrollArea.horizontalScrollBar.idleBarTexture = r.getResourceLocation();
            }
        }
        if (this.scrollGrabberTextureHover != null) {
            ITexture r = ResourceHandlers.getImageHandler().get(this.scrollGrabberTextureHover);
            if (r != null) {
                this.scrollArea.verticalScrollBar.hoverBarTexture = r.getResourceLocation();
                this.scrollArea.horizontalScrollBar.hoverBarTexture = r.getResourceLocation();
            }
        }
        TextElement.LineScrollEntry borderTop = new TextElement.LineScrollEntry(this.scrollArea, " ", false, 1.0F, this);
        borderTop.setHeight(this.textBorder);
        this.scrollArea.addEntry(borderTop);
        for (Entry<String, Float> m : this.lines.entries()) {
            this.scrollArea.addEntry(new TextElement.LineScrollEntry(this.scrollArea, (String) m.getKey(), false, this.scale, this));
        }
        TextElement.LineScrollEntry borderBottom = new TextElement.LineScrollEntry(this.scrollArea, " ", false, 1.0F, this);
        borderBottom.setHeight(this.textBorder);
        this.scrollArea.addEntry(borderBottom);
    }

    public Color getBaseColor() {
        return this.baseColorHex != null ? RenderUtils.getColorFromHexString(this.baseColorHex) : null;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        try {
            if (this.shouldRender()) {
                if (!this.updating) {
                    RenderSystem.enableBlend();
                    if (this.scrollArea != null) {
                        this.scrollArea.customGuiScale = this.customGuiScale;
                        this.scrollArea.setX(this.getAbsoluteX(), true);
                        this.scrollArea.setY(this.getAbsoluteY(), true);
                        this.scrollArea.setWidth(this.getAbsoluteWidth(), true);
                        this.scrollArea.setHeight(this.getAbsoluteHeight(), true);
                        this.scrollArea.render(graphics, MouseInput.getMouseX(), MouseInput.getMouseY(), RenderingUtils.getPartialTick());
                    }
                } else if (isEditor()) {
                    graphics.fill(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getAbsoluteWidth(), this.getAbsoluteY() + this.getAbsoluteHeight(), Color.MAGENTA.getRGB());
                    graphics.drawCenteredString(this.font, Component.translatable("fancymenu.customization.items.text.status.loading"), this.getAbsoluteX() + this.getAbsoluteWidth() / 2, this.getAbsoluteY() + this.getAbsoluteHeight() / 2 - 9 / 2, -1);
                }
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void updateContent() {
        if (!this.updating) {
            this.updating = true;
            new Thread(() -> {
                List<String> linesRaw = new ArrayList();
                try {
                    if (this.source == null || this.source.equals("")) {
                        linesRaw.add(I18n.get("fancymenu.customization.items.text.placeholder"));
                    } else if (this.sourceMode == TextElement.SourceMode.DIRECT) {
                        if (this.source.replace("\\n", "%n%").replace("\\r", "%n%").contains("%n%")) {
                            linesRaw.addAll(Arrays.asList(StringUtils.splitLines(this.source.replace("\\n", "%n%").replace("\\r", "%n%"), "%n%")));
                        } else {
                            linesRaw.add(this.source);
                        }
                    } else if (this.sourceMode == TextElement.SourceMode.LOCAL_SOURCE) {
                        File f = new File(PlaceholderParser.replacePlaceholders(this.source));
                        if (!f.exists() || !f.getAbsolutePath().replace("\\", "/").startsWith(Minecraft.getInstance().gameDirectory.getAbsolutePath().replace("\\", "/"))) {
                            f = new File(Minecraft.getInstance().gameDirectory.getAbsolutePath().replace("\\", "/") + "/" + PlaceholderParser.replacePlaceholders(this.source));
                        }
                        if (f.isFile()) {
                            linesRaw.addAll(FileUtils.getFileLines(f));
                        }
                    } else if (this.sourceMode == TextElement.SourceMode.WEB_SOURCE && WebUtils.isValidUrl(StringUtils.convertFormatCodes(PlaceholderParser.replacePlaceholders(this.source), "§", "&"))) {
                        String fixedSource = StringUtils.convertFormatCodes(PlaceholderParser.replacePlaceholders(this.source), "§", "&");
                        if (fixedSource.toLowerCase().contains("/blob/") && (fixedSource.toLowerCase().startsWith("http://github.com/") || fixedSource.toLowerCase().startsWith("https://github.com/") || fixedSource.toLowerCase().startsWith("http://www.github.com/") || fixedSource.toLowerCase().startsWith("https://www.github.com/"))) {
                            String path = fixedSource.replace("//", "").split("/", 2)[1].replace("/blob/", "/");
                            fixedSource = "https://raw.githubusercontent.com/" + path;
                        }
                        if (!fixedSource.toLowerCase().contains("/raw/") && (fixedSource.toLowerCase().startsWith("http://pastebin.com/") || fixedSource.toLowerCase().startsWith("https://pastebin.com/") || fixedSource.toLowerCase().startsWith("http://www.pastebin.com/") || fixedSource.toLowerCase().startsWith("https://www.pastebin.com/"))) {
                            String path = fixedSource.replace("//", "").split("/", 2)[1];
                            fixedSource = "https://pastebin.com/raw/" + path;
                        }
                        BufferedReader r = null;
                        try {
                            URL url = new URL(fixedSource);
                            r = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
                            for (String s = r.readLine(); s != null; s = r.readLine()) {
                                linesRaw.add(s);
                            }
                            r.close();
                        } catch (Exception var7) {
                            if (r != null) {
                                try {
                                    r.close();
                                } catch (Exception var6) {
                                    var6.printStackTrace();
                                }
                            }
                            linesRaw.clear();
                        }
                    }
                } catch (Exception var8) {
                    linesRaw.clear();
                }
                this.lines.clear();
                if (linesRaw.isEmpty()) {
                    if (isEditor()) {
                        linesRaw.add(I18n.get("fancymenu.customization.items.text.status.unable_to_load"));
                    } else {
                        linesRaw.add("");
                    }
                }
                for (String s : linesRaw) {
                    float sc = getScaleMultiplicator(s);
                    s = getWithoutHeadlineCodes(s);
                    if (this.caseMode == TextElement.CaseMode.ALL_LOWER) {
                        s = s.toLowerCase();
                    }
                    if (this.caseMode == TextElement.CaseMode.ALL_UPPER) {
                        s = s.toUpperCase();
                    }
                    this.lines.put(s, sc);
                }
                MainThreadTaskExecutor.executeInMainThread(this::updateScrollArea, MainThreadTaskExecutor.ExecuteTiming.POST_CLIENT_TICK);
                this.updating = false;
            }).start();
        }
    }

    protected static float getScaleMultiplicator(String s) {
        if (s.startsWith("### ")) {
            return 1.1F;
        } else if (s.startsWith("## ")) {
            return 1.3F;
        } else {
            return s.startsWith("# ") ? 1.5F : 1.0F;
        }
    }

    protected static String getWithoutHeadlineCodes(String s) {
        if (s.startsWith("### ")) {
            return s.substring(4);
        } else if (s.startsWith("## ")) {
            return s.substring(3);
        } else {
            return s.startsWith("# ") ? s.substring(2) : s;
        }
    }

    public static enum CaseMode {

        NORMAL("normal"), ALL_LOWER("lower"), ALL_UPPER("upper");

        final String name;

        private CaseMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static TextElement.CaseMode getByName(String name) {
            for (TextElement.CaseMode i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }
    }

    public static class LineScrollEntry extends TextScrollAreaEntry {

        public final String textRaw;

        public final boolean bold;

        public final float scale;

        public final TextElement parentItem;

        protected String lastTextToRender;

        public LineScrollEntry(ScrollArea parent, String textRaw, boolean bold, float scale, TextElement parentItem) {
            super(parent, Component.literal(""), entry -> {
            });
            this.textRaw = textRaw;
            this.bold = bold;
            this.scale = scale;
            this.parentItem = parentItem;
            this.selectable = false;
            this.playClickSound = false;
            this.backgroundColorIdle = new Color(0, 0, 0, 0);
            this.backgroundColorHover = new Color(0, 0, 0, 0);
            this.buttonBase.m_93650_(0.0F);
            this.setHeight((int) (9.0F * scale) + parentItem.lineSpacing);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            RenderSystem.enableBlend();
            String textToRender = PlaceholderParser.replacePlaceholders(this.textRaw, true);
            if (this.parentItem.caseMode == TextElement.CaseMode.ALL_LOWER) {
                textToRender = textToRender.toLowerCase();
            } else if (this.parentItem.caseMode == TextElement.CaseMode.ALL_UPPER) {
                textToRender = textToRender.toUpperCase();
            }
            if (this.lastTextToRender == null || !this.lastTextToRender.equals(textToRender)) {
                this.setTextOfLine(Component.literal(textToRender));
                ((MutableComponent) this.getText()).withStyle(Style.EMPTY.withBold(this.bold));
            }
            this.lastTextToRender = textToRender;
            this.updateEntry();
            this.buttonBase.m_88315_(graphics, mouseX, mouseY, partial);
            int textX = (int) ((float) this.getX() / this.scale);
            if (this.parentItem.alignment == AbstractElement.Alignment.LEFT) {
                textX += this.parentItem.textBorder;
            } else if (this.parentItem.alignment == AbstractElement.Alignment.RIGHT) {
                textX = (int) ((float) textX + (float) (this.getWidth() - this.textWidth) / this.scale);
                textX -= this.parentItem.textBorder;
            } else if (this.parentItem.alignment == AbstractElement.Alignment.CENTERED) {
                textX = (int) ((float) textX + (float) (this.getWidth() - this.textWidth) / this.scale / 2.0F);
            }
            int centerY = (int) ((float) this.getY() / this.scale) + this.getHeight() / 2;
            int textY = centerY - (int) ((float) (9 / 2) * this.scale);
            graphics.pose().pushPose();
            graphics.pose().scale(this.scale, this.scale, this.scale);
            Color c = this.parentItem.getBaseColor();
            int textColor;
            if (c != null) {
                textColor = FastColor.ARGB32.color(Math.max(0, Math.min(255, (int) (this.parentItem.opacity * 255.0F))), c.getRed(), c.getGreen(), c.getBlue());
            } else {
                textColor = FastColor.ARGB32.color(Math.max(0, Math.min(255, (int) (this.parentItem.opacity * 255.0F))), 255, 255, 255);
            }
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            graphics.drawString(this.font, this.text, textX, textY, textColor, this.parentItem.shadow);
            graphics.pose().popPose();
        }

        public void setTextOfLine(MutableComponent text) {
            this.text = text;
            this.textWidth = (int) ((float) this.font.width(this.text) * this.scale);
            this.setWidth(this.parentItem.textBorder + this.textWidth + this.parentItem.textBorder);
        }
    }

    public static enum SourceMode {

        DIRECT("direct"), LOCAL_SOURCE("local"), WEB_SOURCE("web");

        final String name;

        private SourceMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static TextElement.SourceMode getByName(String name) {
            for (TextElement.SourceMode i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }
    }
}