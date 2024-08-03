package de.keksuccino.fancymenu.customization.layout.editor;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoint;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.ScreenUtils;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class AnchorPointOverlay implements Renderable, GuiEventListener {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final LayoutEditorScreen editor;

    protected AnchorPointOverlay.AnchorPointArea lastTickHoveredArea = null;

    protected AnchorPointOverlay.AnchorPointArea currentlyHoveredArea = null;

    protected AnchorPointOverlay.AnchorPointArea lastCompletedHoverArea = null;

    protected boolean lastTickDraggedEmpty = true;

    protected long areaHoverStartTime = -1L;

    protected boolean overlayVisibilityKeybindPressed = false;

    protected AnchorPointOverlay.AnchorPointArea topLeftArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.TOP_LEFT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_RIGHT);

    protected AnchorPointOverlay.AnchorPointArea midLeftArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.MID_LEFT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_RIGHT);

    protected AnchorPointOverlay.AnchorPointArea bottomLeftArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.BOTTOM_LEFT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_RIGHT);

    protected AnchorPointOverlay.AnchorPointArea topCenteredArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.TOP_CENTERED, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_DOWN);

    protected AnchorPointOverlay.AnchorPointArea midCenteredArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.MID_CENTERED, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_TOP);

    protected AnchorPointOverlay.AnchorPointArea bottomCenteredArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.BOTTOM_CENTERED, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_TOP);

    protected AnchorPointOverlay.AnchorPointArea topRightArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.TOP_RIGHT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_LEFT);

    protected AnchorPointOverlay.AnchorPointArea midRightArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.MID_RIGHT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_LEFT);

    protected AnchorPointOverlay.AnchorPointArea bottomRightArea = new AnchorPointOverlay.AnchorPointArea(ElementAnchorPoints.BOTTOM_RIGHT, 30, 30, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_LEFT);

    protected AnchorPointOverlay.AnchorPointArea[] anchorPointAreas = new AnchorPointOverlay.AnchorPointArea[] { this.topLeftArea, this.midLeftArea, this.bottomLeftArea, this.topCenteredArea, this.midCenteredArea, this.bottomCenteredArea, this.topRightArea, this.midRightArea, this.bottomRightArea };

    public AnchorPointOverlay(@NotNull LayoutEditorScreen editor) {
        this.editor = (LayoutEditorScreen) Objects.requireNonNull(editor);
    }

    public void resetAreaHoverCache() {
        this.currentlyHoveredArea = null;
        this.lastTickHoveredArea = null;
        this.areaHoverStartTime = -1L;
    }

    public void resetOverlay() {
        this.resetAreaHoverCache();
        this.setLastCompletedHoverArea(null);
        this.lastTickDraggedEmpty = true;
        this.overlayVisibilityKeybindPressed = false;
    }

    public double getOverlayHoverChargingTimeSeconds() {
        return FancyMenu.getOptions().anchorOverlayHoverChargingTimeSeconds.getValue();
    }

    public long getOverlayHoverChargingTimeMs() {
        return (long) (this.getOverlayHoverChargingTimeSeconds() * 1000.0);
    }

    @NotNull
    public AnchorPointOverlay.AnchorOverlayVisibilityMode getVisibilityMode() {
        AnchorPointOverlay.AnchorOverlayVisibilityMode m = AnchorPointOverlay.AnchorOverlayVisibilityMode.getByName(FancyMenu.getOptions().anchorOverlayVisibilityMode.getValue());
        return m != null ? m : AnchorPointOverlay.AnchorOverlayVisibilityMode.DRAGGING;
    }

    public boolean isOverlayVisible() {
        if (this.getVisibilityMode() == AnchorPointOverlay.AnchorOverlayVisibilityMode.DISABLED) {
            return false;
        } else if (this.getVisibilityMode() == AnchorPointOverlay.AnchorOverlayVisibilityMode.DRAGGING) {
            return !this.editor.getCurrentlyDraggedElements().isEmpty();
        } else {
            return this.getVisibilityMode() == AnchorPointOverlay.AnchorOverlayVisibilityMode.KEYBIND ? this.overlayVisibilityKeybindPressed : true;
        }
    }

    public boolean invertOverlayColors() {
        return FancyMenu.getOptions().invertAnchorOverlayColor.getValue();
    }

    @Nullable
    public DrawableColor getOverlayColorBaseOverride() {
        String override = FancyMenu.getOptions().anchorOverlayColorBaseOverride.getValue();
        if (override.trim().isEmpty()) {
            return null;
        } else {
            return !TextValidators.HEX_COLOR_TEXT_VALIDATOR.get(override) ? null : DrawableColor.of(override);
        }
    }

    @Nullable
    public DrawableColor getOverlayColorBorderOverride() {
        String override = FancyMenu.getOptions().anchorOverlayColorBorderOverride.getValue();
        if (override.trim().isEmpty()) {
            return null;
        } else {
            return !TextValidators.HEX_COLOR_TEXT_VALIDATOR.get(override) ? null : DrawableColor.of(override);
        }
    }

    @NotNull
    public DrawableColor getOverlayColorBase() {
        if (this.invertOverlayColors()) {
            return DrawableColor.WHITE;
        } else {
            DrawableColor override = this.getOverlayColorBaseOverride();
            return override != null ? override : UIBase.getUIColorTheme().layout_editor_anchor_point_overlay_color_base;
        }
    }

    @NotNull
    public DrawableColor getOverlayColorBorder() {
        if (this.invertOverlayColors()) {
            return DrawableColor.WHITE;
        } else {
            DrawableColor override = this.getOverlayColorBorderOverride();
            return override != null ? override : UIBase.getUIColorTheme().layout_editor_anchor_point_overlay_color_border;
        }
    }

    public boolean isOverlayBusy() {
        return !this.editor.getCurrentlyDraggedElements().isEmpty();
    }

    public float getOverlayOpacityNormal() {
        return FancyMenu.getOptions().anchorOverlayOpacityPercentageNormal.getValue();
    }

    public float getOverlayOpacityBusy() {
        return FancyMenu.getOptions().anchorOverlayOpacityPercentageBusy.getValue();
    }

    public int getOverlayOpacity() {
        if (this.invertOverlayColors()) {
            return 255;
        } else {
            float percentage = this.isOverlayBusy() ? this.getOverlayOpacityBusy() : this.getOverlayOpacityNormal();
            if (percentage > 1.0F) {
                percentage = 1.0F;
            }
            if (percentage < 0.0F) {
                percentage = 0.0F;
            }
            return Math.min(255, Math.max(0, (int) (percentage * 255.0F)));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (!this.isOverlayVisible()) {
            this.resetOverlay();
        } else {
            this.tickAreaMouseOver(mouseX, mouseY);
            RenderingUtils.resetShaderColor(graphics);
            RenderSystem.enableBlend();
            if (this.invertOverlayColors()) {
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
            this.renderAreas(graphics, mouseX, mouseY, partial);
            this.renderConnectionLines(graphics);
            RenderSystem.defaultBlendFunc();
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderAreas(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        int menuBarHeight = this.editor.menuBar != null ? (int) ((float) this.editor.menuBar.getHeight() * UIBase.calculateFixedScale(this.editor.menuBar.getScale())) : 0;
        if (this.editor.menuBar != null && !this.editor.menuBar.isExpanded()) {
            menuBarHeight = 0;
        }
        this.topLeftArea.x = -1;
        this.topLeftArea.y = -1 + menuBarHeight;
        this.topLeftArea.render(graphics, mouseX, mouseY, partial);
        this.midLeftArea.x = -1;
        this.midLeftArea.y = ScreenUtils.getScreenHeight() / 2 - this.midLeftArea.getHeight() / 2;
        this.midLeftArea.render(graphics, mouseX, mouseY, partial);
        this.bottomLeftArea.x = -1;
        this.bottomLeftArea.y = ScreenUtils.getScreenHeight() - this.bottomLeftArea.getHeight() + 1;
        this.bottomLeftArea.render(graphics, mouseX, mouseY, partial);
        this.topCenteredArea.x = ScreenUtils.getScreenWidth() / 2 - this.topCenteredArea.getWidth() / 2;
        this.topCenteredArea.y = -1 + menuBarHeight;
        this.topCenteredArea.render(graphics, mouseX, mouseY, partial);
        this.midCenteredArea.x = ScreenUtils.getScreenWidth() / 2 - this.midCenteredArea.getWidth() / 2;
        this.midCenteredArea.y = ScreenUtils.getScreenHeight() / 2 - this.midCenteredArea.getHeight() / 2;
        this.midCenteredArea.render(graphics, mouseX, mouseY, partial);
        this.bottomCenteredArea.x = ScreenUtils.getScreenWidth() / 2 - this.bottomCenteredArea.getWidth() / 2;
        this.bottomCenteredArea.y = ScreenUtils.getScreenHeight() - this.bottomCenteredArea.getHeight() + 1;
        this.bottomCenteredArea.render(graphics, mouseX, mouseY, partial);
        this.topRightArea.x = ScreenUtils.getScreenWidth() - this.topRightArea.getWidth() + 1;
        this.topRightArea.y = -1 + menuBarHeight;
        this.topRightArea.render(graphics, mouseX, mouseY, partial);
        this.midRightArea.x = ScreenUtils.getScreenWidth() - this.midRightArea.getWidth() + 1;
        this.midRightArea.y = ScreenUtils.getScreenHeight() / 2 - this.midRightArea.getHeight() / 2;
        this.midRightArea.render(graphics, mouseX, mouseY, partial);
        this.bottomRightArea.x = ScreenUtils.getScreenWidth() - this.bottomRightArea.getWidth() + 1;
        this.bottomRightArea.y = ScreenUtils.getScreenHeight() - this.bottomRightArea.getHeight() + 1;
        this.bottomRightArea.render(graphics, mouseX, mouseY, partial);
        if (this.currentlyHoveredArea != null) {
            this.currentlyHoveredArea.renderMouseOverProgress(graphics, this.calculateMouseOverProgress());
        }
    }

    protected float calculateMouseOverProgress() {
        if (this.currentlyHoveredArea != null) {
            long now = System.currentTimeMillis();
            if (this.areaHoverStartTime + this.getOverlayHoverChargingTimeMs() > now) {
                long diff = this.areaHoverStartTime + this.getOverlayHoverChargingTimeMs() - now;
                float f = Math.max(0.0F, Math.min(1.0F, Math.max(1.0F, (float) diff) / (float) this.getOverlayHoverChargingTimeMs()));
                return 1.0F - f;
            } else {
                return 1.0F;
            }
        } else {
            return 0.0F;
        }
    }

    protected void renderConnectionLines(@NotNull GuiGraphics graphics) {
        for (AbstractEditorElement e : FancyMenu.getOptions().showAllAnchorOverlayConnections.getValue() ? this.editor.getAllElements() : this.editor.getCurrentlyDraggedElements()) {
            boolean var10000;
            label25: {
                if (e instanceof HideableElement h && h.isHidden()) {
                    var10000 = true;
                    break label25;
                }
                var10000 = false;
            }
            boolean hidden = var10000;
            if (!hidden) {
                this.renderConnectionLineFor(graphics, e);
            }
        }
    }

    protected void renderConnectionLineFor(@NotNull GuiGraphics graphics, @NotNull AbstractEditorElement e) {
        AnchorPointOverlay.AnchorPointArea a = this.getParentAreaOfElement(e);
        if (a != null) {
            int xElement = e.getX() + e.getWidth() / 2;
            int yElement = e.getY() + e.getHeight() / 2;
            int xArea = a.getX() + a.getWidth() / 2;
            int yArea = a.getY() + a.getHeight() / 2;
            this.renderSquareLine(graphics, xElement, yElement, xArea, yArea, 2, RenderingUtils.replaceAlphaInColor(this.getOverlayColorBase().getColorInt(), this.getOverlayOpacity()));
        }
    }

    protected void renderSquareLine(@NotNull GuiGraphics graphics, int xElement, int yElement, int xArea, int yArea, int lineThickness, int color) {
        int horizontalWidth = Math.max(xElement, xArea) - Math.min(xElement, xArea);
        int verticalHeight = Math.max(yElement, yArea) - Math.min(yElement, yArea);
        int horizontalX = Math.min(xElement, xArea);
        int verticalY = Math.min(yElement, yArea);
        if (xArea < xElement) {
            horizontalX += lineThickness;
        }
        RenderSystem.enableBlend();
        UIBase.resetShaderColor(graphics);
        graphics.fill(horizontalX, yArea, horizontalX + horizontalWidth, yArea + lineThickness, color);
        graphics.fill(xElement, verticalY, xElement + lineThickness, verticalY + verticalHeight, color);
        UIBase.resetShaderColor(graphics);
    }

    protected void tickAreaMouseOver(int mouseX, int mouseY) {
        boolean draggedEmpty = this.editor.getCurrentlyDraggedElements().isEmpty();
        if (!draggedEmpty) {
            this.currentlyHoveredArea = FancyMenu.getOptions().anchorOverlayChangeAnchorOnAreaHover.getValue() ? this.getMouseOverArea(mouseX, mouseY) : null;
            if (this.lastTickDraggedEmpty) {
                this.setLastCompletedHoverArea(this.currentlyHoveredArea);
            }
            if (this.currentlyHoveredArea == null) {
                this.setLastCompletedHoverArea(null);
            }
            if (this.isSameArea(this.currentlyHoveredArea, this.lastCompletedHoverArea)) {
                this.currentlyHoveredArea = null;
            }
            if (this.currentlyHoveredArea != null && !this.isSameArea(this.currentlyHoveredArea, this.lastTickHoveredArea)) {
                this.areaHoverStartTime = System.currentTimeMillis();
            }
            this.lastTickHoveredArea = this.currentlyHoveredArea;
            if (this.currentlyHoveredArea != null && this.areaHoverStartTime + this.getOverlayHoverChargingTimeMs() <= System.currentTimeMillis()) {
                for (AbstractEditorElement e : this.editor.getCurrentlyDraggedElements()) {
                    if (this.canChangeAnchorTo(e, this.currentlyHoveredArea)) {
                        e.setAnchorPointViaOverlay(this.currentlyHoveredArea, mouseX, mouseY);
                    }
                }
                this.setLastCompletedHoverArea(this.currentlyHoveredArea);
                this.resetAreaHoverCache();
            }
        } else {
            this.resetAreaHoverCache();
        }
        this.lastTickDraggedEmpty = draggedEmpty;
    }

    protected boolean isSameArea(@Nullable AnchorPointOverlay.AnchorPointArea firstArea, @Nullable AnchorPointOverlay.AnchorPointArea secondArea) {
        if (firstArea == null && secondArea == null) {
            return true;
        } else if (firstArea != null && secondArea != null) {
            if (firstArea instanceof AnchorPointOverlay.ElementAnchorPointArea a1 && secondArea instanceof AnchorPointOverlay.ElementAnchorPointArea a2) {
                return StringUtils.equals(a1.elementIdentifier, a2.elementIdentifier);
            }
            return firstArea.anchorPoint == secondArea.anchorPoint;
        } else {
            return false;
        }
    }

    protected boolean canChangeAnchorTo(@NotNull AbstractEditorElement element, @NotNull AnchorPointOverlay.AnchorPointArea area) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(area);
        if (this.isAttachedToAnchor(element, area)) {
            return false;
        } else {
            if (area instanceof AnchorPointOverlay.ElementAnchorPointArea a) {
                String parentOfElement = element.element.anchorPointElementIdentifier;
                if (parentOfElement != null && parentOfElement.equals(a.elementIdentifier)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Nullable
    protected List<AbstractEditorElement> getChildElementsOfDraggedElements() {
        List<AbstractEditorElement> currentlyDragged = this.editor.getCurrentlyDraggedElements();
        List<AbstractEditorElement> children = new ArrayList();
        for (AbstractEditorElement e : currentlyDragged) {
            List<AbstractEditorElement> childChainOfE = this.editor.getElementChildChainOfExcluding(e);
            if (childChainOfE == null) {
                return null;
            }
            childChainOfE.forEach(element -> {
                if (!currentlyDragged.contains(element)) {
                    children.add(element);
                }
            });
        }
        return children;
    }

    protected boolean isAttachedToAnchor(@NotNull AbstractEditorElement element, @NotNull AnchorPointOverlay.AnchorPointArea area) {
        if (area instanceof AnchorPointOverlay.ElementAnchorPointArea ae) {
            String parentOfElement = element.element.anchorPointElementIdentifier;
            if (parentOfElement != null) {
                return ae.elementIdentifier.equals(parentOfElement);
            }
        }
        return element.element.anchorPoint == area.anchorPoint;
    }

    @Nullable
    protected AnchorPointOverlay.AnchorPointArea getParentAreaOfElement(@NotNull AbstractEditorElement element) {
        if (element.element.anchorPoint == ElementAnchorPoints.ELEMENT) {
            if (element.element.anchorPointElementIdentifier != null) {
                AbstractEditorElement e = this.editor.getElementByInstanceIdentifier(element.element.anchorPointElementIdentifier);
                if (e != null) {
                    return new AnchorPointOverlay.ElementAnchorPointArea(e.element.getInstanceIdentifier());
                }
            }
            return null;
        } else {
            for (AnchorPointOverlay.AnchorPointArea a : this.anchorPointAreas) {
                if (a.anchorPoint == element.element.anchorPoint) {
                    return a;
                }
            }
            return null;
        }
    }

    @Nullable
    protected AbstractEditorElement getTopHoveredNotDraggedElement() {
        List<AbstractEditorElement> childrenOfDragged = this.getChildElementsOfDraggedElements();
        if (childrenOfDragged == null) {
            LOGGER.error("[FANCYMENU] Failed to get hovered element! Error while getting children of dragged elements!", new IllegalStateException());
            return null;
        } else {
            List<AbstractEditorElement> draggedElements = this.editor.getCurrentlyDraggedElements();
            List<AbstractEditorElement> notDraggedElements = this.editor.getHoveredElements();
            notDraggedElements.removeIf(draggedElements::contains);
            notDraggedElements.removeIf(childrenOfDragged::contains);
            return notDraggedElements.isEmpty() ? null : ListUtils.getLast(notDraggedElements);
        }
    }

    @Nullable
    protected AnchorPointOverlay.AnchorPointArea getMouseOverArea(int mouseX, int mouseY) {
        if (FancyMenu.getOptions().anchorOverlayChangeAnchorOnAreaHover.getValue()) {
            for (AnchorPointOverlay.AnchorPointArea a : this.anchorPointAreas) {
                if (a.isMouseOver((double) mouseX, (double) mouseY)) {
                    return a;
                }
            }
        }
        if (FancyMenu.getOptions().anchorOverlayChangeAnchorOnElementHover.getValue()) {
            AbstractEditorElement e = this.getTopHoveredNotDraggedElement();
            if (e != null && !e.isSelected() && !e.isMultiSelected()) {
                return new AnchorPointOverlay.ElementAnchorPointArea(e.element.getInstanceIdentifier());
            }
        }
        return null;
    }

    protected void setLastCompletedHoverArea(@Nullable AnchorPointOverlay.AnchorPointArea area) {
        this.lastCompletedHoverArea = area;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.resetAreaHoverCache();
        this.lastTickDraggedEmpty = true;
        this.setLastCompletedHoverArea(null);
        return GuiEventListener.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        String key = GLFW.glfwGetKeyName(keycode, scancode);
        if (key == null) {
            key = "";
        }
        if (key.equals("o")) {
            this.overlayVisibilityKeybindPressed = true;
        }
        return GuiEventListener.super.keyPressed(keycode, scancode, modifiers);
    }

    @Override
    public boolean keyReleased(int keycode, int scancode, int modifiers) {
        String key = GLFW.glfwGetKeyName(keycode, scancode);
        if (key == null) {
            key = "";
        }
        if (key.equals("o")) {
            this.overlayVisibilityKeybindPressed = false;
        }
        return GuiEventListener.super.keyReleased(keycode, scancode, modifiers);
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public static enum AnchorOverlayVisibilityMode implements LocalizedCycleEnum<AnchorPointOverlay.AnchorOverlayVisibilityMode> {

        DISABLED("disabled"), ALWAYS("always"), DRAGGING("dragging"), KEYBIND("keybind");

        final String name;

        private AnchorOverlayVisibilityMode(@NotNull String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.editor.anchor_overlay.visibility_mode";
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) WARNING_TEXT_STYLE.get();
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public AnchorPointOverlay.AnchorOverlayVisibilityMode[] getValues() {
            return values();
        }

        @Nullable
        public AnchorPointOverlay.AnchorOverlayVisibilityMode getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static AnchorPointOverlay.AnchorOverlayVisibilityMode getByName(@NotNull String name) {
            for (AnchorPointOverlay.AnchorOverlayVisibilityMode m : values()) {
                if (m.name.equals(name)) {
                    return m;
                }
            }
            return null;
        }
    }

    public class AnchorPointArea implements Renderable, GuiEventListener {

        public final ElementAnchorPoint anchorPoint;

        private int x;

        private int y;

        private final int width;

        private final int height;

        private final AnchorPointOverlay.AnchorPointArea.ProgressDirection direction;

        private AnchorPointArea(@NotNull ElementAnchorPoint anchorPoint, int width, int height, @NotNull AnchorPointOverlay.AnchorPointArea.ProgressDirection direction) {
            this.width = width;
            this.height = height;
            this.direction = direction;
            this.anchorPoint = anchorPoint;
        }

        public String toString() {
            return "[anchor_point_area;x=" + this.getX() + ";y=" + this.getY() + ";w=" + this.getWidth() + ";h=" + this.getHeight() + "]";
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            int endX = this.getX() + this.getWidth();
            int endY = this.getY() + this.getHeight();
            graphics.fill(this.getX(), this.getY(), endX, endY, RenderingUtils.replaceAlphaInColor(AnchorPointOverlay.this.getOverlayColorBase().getColorInt(), AnchorPointOverlay.this.getOverlayOpacity()));
            UIBase.renderBorder(graphics, (float) this.getX(), (float) this.getY(), (float) endX, (float) endY, 1.0F, RenderingUtils.replaceAlphaInColor(AnchorPointOverlay.this.getOverlayColorBorder().getColorInt(), AnchorPointOverlay.this.getOverlayOpacity()), true, true, true, true);
            UIBase.resetShaderColor(graphics);
        }

        protected void renderMouseOverProgress(@NotNull GuiGraphics graphics, float progress) {
            int progressWidth = (int) ((float) this.getWidth() * progress);
            int progressHeight = (int) ((float) this.getHeight() * progress);
            int startX = this.getX();
            int startY = this.getY();
            int endX = this.getX() + progressWidth;
            int endY = this.getY() + this.getHeight();
            if (this.direction == AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_LEFT) {
                endX = this.getX() + this.getWidth();
                startX = endX - progressWidth;
            } else if (this.direction == AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_DOWN) {
                endX = this.getX() + this.getWidth();
                endY = this.getY() + progressHeight;
            } else if (this.direction == AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_TOP) {
                endX = this.getX() + this.getWidth();
                startY = endY - progressHeight;
            }
            graphics.fill(startX, startY, endX, endY, RenderingUtils.replaceAlphaInColor(AnchorPointOverlay.this.getOverlayColorBorder().getColorInt(), AnchorPointOverlay.this.getOverlayOpacity()));
            UIBase.resetShaderColor(graphics);
        }

        protected int getWidth() {
            return this.width;
        }

        protected int getHeight() {
            return this.height;
        }

        protected int getX() {
            return this.x;
        }

        protected int getY() {
            return this.y;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return UIBase.isXYInArea((int) mouseX, (int) mouseY, this.getX(), this.getY(), this.getWidth(), this.getWidth());
        }

        @Override
        public void setFocused(boolean var1) {
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        protected static enum ProgressDirection {

            TO_LEFT, TO_RIGHT, TO_TOP, TO_DOWN
        }
    }

    public class ElementAnchorPointArea extends AnchorPointOverlay.AnchorPointArea {

        @NotNull
        public final String elementIdentifier;

        private ElementAnchorPointArea(@NotNull String elementIdentifier) {
            super(ElementAnchorPoints.ELEMENT, 0, 0, AnchorPointOverlay.AnchorPointArea.ProgressDirection.TO_TOP);
            this.elementIdentifier = (String) Objects.requireNonNull(elementIdentifier);
        }

        @Override
        public String toString() {
            return "[element_anchor_point_area;id=" + this.elementIdentifier + ";x=" + this.getX() + ";y=" + this.getY() + ";w=" + this.getWidth() + ";h=" + this.getHeight() + "]";
        }

        @Nullable
        public AbstractEditorElement getElement() {
            AbstractEditorElement element = AnchorPointOverlay.this.editor.getElementByInstanceIdentifier(this.elementIdentifier);
            if (element == null) {
                AnchorPointOverlay.LOGGER.error("[FANCYMENU] Failed to get element instance of ElementAnchorPointArea! Element was NULL!", new NullPointerException());
            }
            return element;
        }

        @Override
        protected int getX() {
            AbstractEditorElement element = this.getElement();
            return element != null ? element.getX() : -100000;
        }

        @Override
        protected int getY() {
            AbstractEditorElement element = this.getElement();
            return element != null ? element.getY() : -100000;
        }

        @Override
        protected int getWidth() {
            AbstractEditorElement element = this.getElement();
            return element != null ? element.getWidth() : 1;
        }

        @Override
        protected int getHeight() {
            AbstractEditorElement element = this.getElement();
            return element != null ? element.getHeight() : 1;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            AbstractEditorElement element = this.getElement();
            return element != null ? element.isMouseOver(mouseX, mouseY) : false;
        }
    }
}