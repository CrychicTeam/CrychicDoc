package de.keksuccino.fancymenu.customization.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElement;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayerHandler;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.threading.MainThreadTaskExecutor;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DebugOverlay implements Renderable, NarratableEntry, ContainerEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final List<DebugOverlay.DebugOverlayLine> lines = new ArrayList();

    @NotNull
    protected Supplier<Integer> topYOffsetSupplier = () -> 0;

    @NotNull
    protected Supplier<Integer> bottomYOffsetSupplier = () -> 0;

    @NotNull
    protected Font font;

    protected int lineSpacerHeight;

    protected int lineBorderWidth;

    @NotNull
    protected DrawableColor lineBackgroundColor;

    @NotNull
    protected DrawableColor lineTextColor;

    protected boolean lineTextShadow;

    protected List<AbstractElement> currentScreenElements;

    @Nullable
    protected ContextMenu rightClickMenu;

    protected final List<GuiEventListener> children;

    public boolean allowRender;

    public DebugOverlay() {
        this.font = Minecraft.getInstance().font;
        this.lineSpacerHeight = 2;
        this.lineBorderWidth = 2;
        this.lineBackgroundColor = DrawableColor.of(new Color(0, 0, 0, 230));
        this.lineTextColor = DrawableColor.WHITE;
        this.lineTextShadow = true;
        this.currentScreenElements = new ArrayList();
        this.rightClickMenu = null;
        this.children = new ArrayList();
        this.allowRender = false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.allowRender) {
            if (Minecraft.getInstance().screen != null) {
                this.renderWidgetOverlays(graphics, Minecraft.getInstance().screen, mouseX, mouseY, partial);
                int leftX = 0;
                int rightX = Minecraft.getInstance().screen.width;
                int topLeftY = (Integer) this.topYOffsetSupplier.get();
                int topRightY = (Integer) this.topYOffsetSupplier.get();
                int bottomLeftY = (Integer) this.bottomYOffsetSupplier.get();
                int bottomRightY = (Integer) this.bottomYOffsetSupplier.get();
                RenderSystem.enableDepthTest();
                RenderSystem.enableBlend();
                graphics.pose().pushPose();
                graphics.pose().translate(0.0F, 0.0F, 400.0F);
                for (DebugOverlay.DebugOverlayLine line : this.lines) {
                    boolean isLeft = line.linePosition == DebugOverlay.LinePosition.TOP_LEFT || line.linePosition == DebugOverlay.LinePosition.BOTTOM_LEFT;
                    Component text = line.textSupplier.get(line);
                    int width = this.font.width(text) + this.lineBorderWidth * 2;
                    int height = 9 + this.lineSpacerHeight * 2;
                    if (line instanceof DebugOverlay.DebugOverlaySpacerLine s) {
                        height = s.height;
                    }
                    int x = isLeft ? leftX : rightX - width;
                    int y = topLeftY;
                    if (line.linePosition == DebugOverlay.LinePosition.TOP_RIGHT) {
                        y = topRightY;
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.BOTTOM_LEFT) {
                        y = bottomLeftY;
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.BOTTOM_RIGHT) {
                        y = bottomRightY;
                    }
                    line.lastX = x;
                    line.lastY = y;
                    line.lastWidth = width;
                    line.lastHeight = height;
                    line.hovered = line.isMouseOver(mouseX, mouseY);
                    if (!(line instanceof DebugOverlay.DebugOverlaySpacerLine)) {
                        this.renderLineBackground(graphics, x, y, width, height);
                        graphics.drawString(this.font, text, x + this.lineBorderWidth, y + this.lineSpacerHeight, this.lineTextColor.getColorInt(), this.lineTextShadow);
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.TOP_LEFT) {
                        topLeftY += height;
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.TOP_RIGHT) {
                        topRightY += height;
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.BOTTOM_LEFT) {
                        bottomLeftY -= height;
                    }
                    if (line.linePosition == DebugOverlay.LinePosition.BOTTOM_RIGHT) {
                        bottomRightY -= height;
                    }
                }
                graphics.pose().popPose();
                RenderSystem.disableDepthTest();
                RenderingUtils.resetShaderColor(graphics);
                if (CustomizationOverlay.getCurrentMenuBarInstance() != null && CustomizationOverlay.getCurrentMenuBarInstance().isEntryContextMenuOpen()) {
                    this.closeRightClickContextMenu();
                }
                if (this.rightClickMenu != null) {
                    RenderSystem.enableBlend();
                    RenderSystem.enableDepthTest();
                    graphics.pose().pushPose();
                    graphics.pose().translate(0.0F, 0.0F, 500.0F);
                    this.rightClickMenu.render(graphics, mouseX, mouseY, partial);
                    graphics.pose().popPose();
                    RenderSystem.disableDepthTest();
                }
                RenderingUtils.resetShaderColor(graphics);
            }
        }
    }

    protected void renderWidgetOverlays(@NotNull GuiGraphics graphics, @NotNull Screen current, int mouseX, int mouseY, float partial) {
        ScreenCustomizationLayer layer = ScreenCustomizationLayerHandler.getLayerOfScreen(current);
        if (layer != null) {
            this.currentScreenElements.clear();
            if (ScreenCustomization.isCustomizationEnabledForScreen(current)) {
                List<AbstractElement> widgets = new ArrayList(layer.deepElements);
                widgets.addAll(layer.vanillaWidgetElements);
                for (AbstractElement e : widgets) {
                    if (!(e instanceof HideableElement h) || !h.isHidden()) {
                        this.currentScreenElements.add(e);
                        UIBase.renderBorder(graphics, e.getAbsoluteX(), e.getAbsoluteY(), e.getAbsoluteX() + e.getAbsoluteWidth(), e.getAbsoluteY() + e.getAbsoluteHeight(), 1, UIBase.getUIColorTheme().layout_editor_element_border_color_normal, true, true, true, true);
                    }
                }
                RenderingUtils.resetShaderColor(graphics);
            }
        }
    }

    protected void renderLineBackground(@NotNull GuiGraphics graphics, int x, int y, int width, int height) {
        RenderSystem.enableBlend();
        graphics.fill(x, y, x + width, y + height, this.lineBackgroundColor.getColorInt());
        RenderingUtils.resetShaderColor(graphics);
    }

    public DebugOverlay setLineTextShadow(boolean shadow) {
        this.lineTextShadow = shadow;
        return this;
    }

    public DebugOverlay setLineTextColor(@NotNull DrawableColor color) {
        this.lineTextColor = color;
        return this;
    }

    public DebugOverlay setLineBackgroundColor(@NotNull DrawableColor color) {
        this.lineBackgroundColor = color;
        return this;
    }

    public DebugOverlay setLineBorderWidth(int width) {
        this.lineBorderWidth = width;
        return this;
    }

    public DebugOverlay setLineSpacerHeight(int height) {
        this.lineSpacerHeight = height;
        return this;
    }

    public DebugOverlay setFont(@NotNull Font font) {
        this.font = font;
        return this;
    }

    public DebugOverlay setTopYOffsetSupplier(@NotNull Supplier<Integer> yOffsetSupplier) {
        this.topYOffsetSupplier = yOffsetSupplier;
        return this;
    }

    public DebugOverlay setBottomYOffsetSupplier(@NotNull Supplier<Integer> yOffsetSupplier) {
        this.bottomYOffsetSupplier = yOffsetSupplier;
        return this;
    }

    public DebugOverlay.DebugOverlaySpacerLine addSpacerLine(@NotNull String identifier, @NotNull DebugOverlay.LinePosition position, int height) {
        return (DebugOverlay.DebugOverlaySpacerLine) this.addLine(new DebugOverlay.DebugOverlaySpacerLine(identifier, height)).setPosition(position);
    }

    public DebugOverlay.DebugOverlaySpacerLine addSpacerLineBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, int height) {
        return this.addLineBefore(addBeforeIdentifier, new DebugOverlay.DebugOverlaySpacerLine(identifier, height).setPosition(position));
    }

    public DebugOverlay.DebugOverlaySpacerLine addSpacerLineAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, int height) {
        return this.addLineAfter(addAfterIdentifier, new DebugOverlay.DebugOverlaySpacerLine(identifier, height).setPosition(position));
    }

    public DebugOverlay.DebugOverlaySpacerLine addSpacerLineAt(int index, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, int height) {
        return this.addLineAt(index, new DebugOverlay.DebugOverlaySpacerLine(identifier, height).setPosition(position));
    }

    public DebugOverlay.DebugOverlayLine addLine(@NotNull String identifier, @NotNull DebugOverlay.LinePosition position, @NotNull ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier) {
        return this.addLine(new DebugOverlay.DebugOverlayLine(identifier).setTextSupplier(textSupplier).setPosition(position));
    }

    public DebugOverlay.DebugOverlayLine addLineBefore(@NotNull String addBeforeIdentifier, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, @NotNull ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier) {
        return this.addLineBefore(addBeforeIdentifier, new DebugOverlay.DebugOverlayLine(identifier).setTextSupplier(textSupplier).setPosition(position));
    }

    public DebugOverlay.DebugOverlayLine addLineAfter(@NotNull String addAfterIdentifier, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, @NotNull ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier) {
        return this.addLineAfter(addAfterIdentifier, new DebugOverlay.DebugOverlayLine(identifier).setTextSupplier(textSupplier).setPosition(position));
    }

    public DebugOverlay.DebugOverlayLine addLineAt(int index, @NotNull String identifier, @NotNull DebugOverlay.LinePosition position, @NotNull ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier) {
        return this.addLineAt(index, new DebugOverlay.DebugOverlayLine(identifier).setTextSupplier(textSupplier).setPosition(position));
    }

    public <T extends DebugOverlay.DebugOverlayLine> T addLineBefore(@NotNull String addBeforeIdentifier, @NotNull T line) {
        if (this.lineExists(line.identifier)) {
            throw new RuntimeException("[FANCYMENU] Line identifier already exists: " + line.identifier);
        } else {
            DebugOverlay.DebugOverlayLine target = this.getLine(addBeforeIdentifier);
            int index = this.indexOfLine(addBeforeIdentifier);
            if (index == -1) {
                index = this.lines.size();
            }
            return this.addLineAt(index, (T) line.setPosition(target != null ? target.linePosition : DebugOverlay.LinePosition.TOP_LEFT));
        }
    }

    public <T extends DebugOverlay.DebugOverlayLine> T addLineAfter(@NotNull String addAfterIdentifier, @NotNull T line) {
        if (this.lineExists(line.identifier)) {
            throw new RuntimeException("[FANCYMENU] Line identifier already exists: " + line.identifier);
        } else {
            DebugOverlay.DebugOverlayLine target = this.getLine(addAfterIdentifier);
            int index = this.indexOfLine(addAfterIdentifier) + 1;
            if (index == 0) {
                index = this.lines.size();
            }
            return this.addLineAt(index, (T) line.setPosition(target != null ? target.linePosition : DebugOverlay.LinePosition.TOP_LEFT));
        }
    }

    public <T extends DebugOverlay.DebugOverlayLine> T addLineAt(int index, @NotNull T line) {
        if (this.lineExists(line.identifier)) {
            throw new RuntimeException("[FANCYMENU] Line identifier already exists: " + line.identifier);
        } else {
            this.lines.add(Math.min(this.lines.size(), Math.max(0, index)), line);
            return line;
        }
    }

    public <T extends DebugOverlay.DebugOverlayLine> T addLine(@NotNull T line) {
        if (this.lineExists(line.identifier)) {
            throw new RuntimeException("[FANCYMENU] Line identifier already exists: " + line.identifier);
        } else {
            this.lines.add(line);
            return line;
        }
    }

    public void removeLine(@NotNull String identifier) {
        DebugOverlay.DebugOverlayLine line = this.getLine(identifier);
        if (line != null) {
            this.lines.remove(line);
        }
    }

    @Nullable
    public DebugOverlay.DebugOverlayLine getLine(@NotNull String identifier) {
        for (DebugOverlay.DebugOverlayLine line : this.lines) {
            if (line.identifier.equals(identifier)) {
                return line;
            }
        }
        return null;
    }

    public int indexOfLine(@NotNull String identifier) {
        DebugOverlay.DebugOverlayLine line = this.getLine(identifier);
        return line != null ? this.lines.indexOf(line) : -1;
    }

    public boolean lineExists(@NotNull String identifier) {
        return this.getLine(identifier) != null;
    }

    public DebugOverlay resetOverlay() {
        this.lines.forEach(line -> line.hovered = false);
        this.closeRightClickContextMenu();
        return this;
    }

    public DebugOverlay openRightClickContextMenu(@NotNull ContextMenu menu) {
        this.closeRightClickContextMenu();
        this.rightClickMenu = (ContextMenu) Objects.requireNonNull(menu);
        this.children.add(0, this.rightClickMenu);
        this.rightClickMenu.openMenuAtMouse();
        return this;
    }

    public DebugOverlay closeRightClickContextMenu() {
        if (this.rightClickMenu != null) {
            this.rightClickMenu.closeMenu();
            this.children.remove(this.rightClickMenu);
            this.rightClickMenu = null;
        }
        return this;
    }

    @NotNull
    protected ContextMenu buildContextMenuForElement(@NotNull AbstractElement element) {
        Objects.requireNonNull(element);
        ContextMenu menu = new ContextMenu();
        if (element instanceof VanillaWidgetElement v) {
            menu.addClickableEntry("copy_vanilla_widget_locator", Component.translatable("fancymenu.helper.editor.items.vanilla_button.copy_locator"), (menu1, entry) -> {
                if (v.widgetMeta != null) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(v.widgetMeta.getLocator());
                }
                MainThreadTaskExecutor.executeInMainThread(() -> this.closeRightClickContextMenu(), MainThreadTaskExecutor.ExecuteTiming.POST_CLIENT_TICK);
            }).setTooltipSupplier((menu1, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.vanilla_button.copy_locator.desc"))).setIcon(ContextMenu.IconFactory.getIcon("notes"));
        }
        menu.addClickableEntry("copy_id", Component.translatable("fancymenu.helper.editor.items.copyid"), (menu1, entry) -> {
            Minecraft.getInstance().keyboardHandler.setClipboard(element.getInstanceIdentifier());
            MainThreadTaskExecutor.executeInMainThread(() -> this.closeRightClickContextMenu(), MainThreadTaskExecutor.ExecuteTiming.POST_CLIENT_TICK);
        }).setTooltipSupplier((menu1, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.copyid.btn.desc"))).setIcon(ContextMenu.IconFactory.getIcon("notes"));
        return menu;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            this.closeRightClickContextMenu();
            for (DebugOverlay.DebugOverlayLine line : this.lines) {
                if (line.onClick(button, (int) mouseX, (int) mouseY)) {
                    return true;
                }
            }
            if (button == 1) {
                for (AbstractElement e : this.currentScreenElements) {
                    if (RenderingUtils.isXYInArea(mouseX, mouseY, (double) e.getAbsoluteX(), (double) e.getAbsoluteY(), (double) e.getAbsoluteWidth(), (double) e.getAbsoluteHeight())) {
                        this.openRightClickContextMenu(this.buildContextMenuForElement(e));
                        return true;
                    }
                }
            }
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

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {
    }

    @Override
    public List<GuiEventListener> children() {
        return this.children;
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean var1) {
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return null;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener var1) {
    }

    public static class DebugOverlayLine {

        protected final String identifier;

        @NotNull
        protected DebugOverlay.LinePosition linePosition = DebugOverlay.LinePosition.TOP_LEFT;

        @NotNull
        protected ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier = consumes -> Component.empty();

        protected boolean clickable = false;

        @NotNull
        protected Consumer<DebugOverlay.DebugOverlayLine> clickAction = line -> {
        };

        protected long lastClicked = -1L;

        protected int lastX;

        protected int lastY;

        protected int lastWidth;

        protected int lastHeight;

        protected boolean hovered = false;

        protected DebugOverlayLine(@NotNull String identifier) {
            this.identifier = (String) Objects.requireNonNull(identifier);
        }

        @NotNull
        public String getIdentifier() {
            return this.identifier;
        }

        public DebugOverlay.DebugOverlayLine setPosition(@NotNull DebugOverlay.LinePosition position) {
            this.linePosition = position;
            return this;
        }

        @NotNull
        public DebugOverlay.LinePosition getPosition() {
            return this.linePosition;
        }

        public DebugOverlay.DebugOverlayLine setTextSupplier(@NotNull ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> textSupplier) {
            this.textSupplier = textSupplier;
            return this;
        }

        @NotNull
        public ConsumingSupplier<DebugOverlay.DebugOverlayLine, Component> getTextSupplier() {
            return this.textSupplier;
        }

        public DebugOverlay.DebugOverlayLine setClickAction(@Nullable Consumer<DebugOverlay.DebugOverlayLine> clickAction) {
            this.clickable = clickAction != null;
            this.clickAction = clickAction != null ? clickAction : line -> {
            };
            return this;
        }

        public boolean recentlyClicked() {
            return System.currentTimeMillis() < this.lastClicked + 2000L;
        }

        public boolean isHovered() {
            return this.hovered;
        }

        protected boolean onClick(int button, int mouseX, int mouseY) {
            if (!this.clickable) {
                return false;
            } else if (this.isMouseOver(mouseX, mouseY) && button == 0) {
                this.clickAction.accept(this);
                this.lastClicked = System.currentTimeMillis();
                return true;
            } else {
                return false;
            }
        }

        protected boolean isMouseOver(int mouseX, int mouseY) {
            return RenderingUtils.isXYInArea(mouseX, mouseY, this.lastX, this.lastY, this.lastWidth, this.lastHeight);
        }
    }

    public static class DebugOverlaySpacerLine extends DebugOverlay.DebugOverlayLine {

        protected final int height;

        protected DebugOverlaySpacerLine(@NotNull String identifier, int height) {
            super(identifier);
            this.height = height;
        }
    }

    public static enum LinePosition {

        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
}