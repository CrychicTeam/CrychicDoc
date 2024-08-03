package de.keksuccino.fancymenu.customization.layout.editor;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Window;
import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.customization.ScreenCustomization;
import de.keksuccino.fancymenu.customization.deep.AbstractDeepEditorElement;
import de.keksuccino.fancymenu.customization.deep.AbstractDeepElement;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.HideableElement;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.element.editor.AbstractEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetEditorElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElement;
import de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget.VanillaWidgetElementBuilder;
import de.keksuccino.fancymenu.customization.layer.ElementFactory;
import de.keksuccino.fancymenu.customization.layer.ScreenCustomizationLayer;
import de.keksuccino.fancymenu.customization.layout.Layout;
import de.keksuccino.fancymenu.customization.layout.LayoutHandler;
import de.keksuccino.fancymenu.customization.layout.editor.widget.AbstractLayoutEditorWidget;
import de.keksuccino.fancymenu.customization.layout.editor.widget.LayoutEditorWidgetRegistry;
import de.keksuccino.fancymenu.customization.screen.identifier.ScreenIdentifierHandler;
import de.keksuccino.fancymenu.customization.widget.ScreenWidgetDiscoverer;
import de.keksuccino.fancymenu.customization.widget.WidgetMeta;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinScreen;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.ObjectUtils;
import de.keksuccino.fancymenu.util.ScreenTitleUtils;
import de.keksuccino.fancymenu.util.file.FileUtils;
import de.keksuccino.fancymenu.util.file.type.groups.FileTypeGroup;
import de.keksuccino.fancymenu.util.file.type.types.FileTypes;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.menubar.v2.MenuBar;
import de.keksuccino.fancymenu.util.rendering.ui.screen.NotificationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.filebrowser.SaveFileScreen;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableWidget;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class LayoutEditorScreen extends Screen implements ElementFactory {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static final Map<SerializedElement, ElementBuilder<?, ?>> COPIED_ELEMENTS_CLIPBOARD = new LinkedHashMap();

    public static final int ELEMENT_DRAG_CRUMPLE_ZONE = 5;

    @Nullable
    protected static LayoutEditorScreen currentInstance = null;

    @Nullable
    public Screen layoutTargetScreen;

    @NotNull
    public Layout layout;

    public List<AbstractEditorElement> normalEditorElements = new ArrayList();

    public List<VanillaWidgetEditorElement> vanillaWidgetEditorElements = new ArrayList();

    public List<AbstractDeepEditorElement> deepEditorElements = new ArrayList();

    public LayoutEditorHistory history = new LayoutEditorHistory(this);

    public MenuBar menuBar;

    public AnchorPointOverlay anchorPointOverlay = new AnchorPointOverlay(this);

    public ContextMenu rightClickMenu;

    public ContextMenu activeElementContextMenu = null;

    public List<AbstractLayoutEditorWidget> layoutEditorWidgets = new ArrayList();

    protected boolean isMouseSelection = false;

    protected int mouseSelectionStartX = 0;

    protected int mouseSelectionStartY = 0;

    public int leftMouseDownPosX = 0;

    public int leftMouseDownPosY = 0;

    protected boolean elementMovingStarted = false;

    protected boolean elementResizingStarted = false;

    protected boolean mouseDraggingStarted = false;

    protected List<AbstractEditorElement> currentlyDraggedElements = new ArrayList();

    protected int rightClickMenuOpenPosX = -1000;

    protected int rightClickMenuOpenPosY = -1000;

    protected LayoutEditorHistory.Snapshot preDragElementSnapshot;

    public final List<WidgetMeta> cachedVanillaWidgetMetas = new ArrayList();

    public LayoutEditorScreen(@NotNull Layout layout) {
        this(null, layout);
    }

    public LayoutEditorScreen(@Nullable Screen layoutTargetScreen, @NotNull Layout layout) {
        super(Component.literal(""));
        this.layoutTargetScreen = layoutTargetScreen;
        layout.updateLastEditedTime();
        layout.saveToFileIfPossible();
        this.layout = layout.copy();
        if (this.layoutTargetScreen != null) {
            Component cachedOriTitle = (Component) ScreenCustomizationLayer.cachedOriginalMenuTitles.get(this.layoutTargetScreen.getClass());
            if (cachedOriTitle != null) {
                ScreenTitleUtils.setScreenTitle(this.layoutTargetScreen, cachedOriTitle);
            }
        }
        this.constructElementInstances();
    }

    @Override
    protected void init() {
        this.currentlyDraggedElements.clear();
        this.anchorPointOverlay.resetOverlay();
        for (WidgetMeta m : this.cachedVanillaWidgetMetas) {
            if (m.getWidget() instanceof CustomizableWidget w) {
                w.resetWidgetCustomizationsFancyMenu();
            }
        }
        if (this.layoutEditorWidgets == null || this.layoutEditorWidgets.isEmpty()) {
            this.layoutEditorWidgets = LayoutEditorWidgetRegistry.buildWidgetInstances(this);
        }
        this.closeRightClickMenu();
        this.rightClickMenu = LayoutEditorUI.buildRightClickContextMenu(this);
        this.m_7787_(this.rightClickMenu);
        if (this.menuBar != null) {
            this.menuBar.closeAllContextMenus();
        }
        this.menuBar = LayoutEditorUI.buildMenuBar(this, this.menuBar == null || this.menuBar.isExpanded());
        this.m_7787_(this.menuBar);
        for (AbstractLayoutEditorWidget w : Lists.reverse(new ArrayList(this.layoutEditorWidgets))) {
            this.m_7787_(w);
        }
        this.isMouseSelection = false;
        this.preDragElementSnapshot = null;
        this.closeActiveElementMenu(true);
        this.serializeElementInstancesToLayoutInstance();
        if (this.layout.forcedScale != 0.0F) {
            float newscale = this.layout.forcedScale;
            if (newscale <= 0.0F) {
                newscale = 1.0F;
            }
            Window mx = Minecraft.getInstance().getWindow();
            mx.setGuiScale((double) newscale);
            this.f_96543_ = mx.getGuiScaledWidth();
            this.f_96544_ = mx.getGuiScaledHeight();
        }
        if (this.layout.autoScalingWidth != 0 && this.layout.autoScalingHeight != 0) {
            Window mx = Minecraft.getInstance().getWindow();
            double guiWidth = (double) this.f_96543_ * mx.getGuiScale();
            double guiHeight = (double) this.f_96544_ * mx.getGuiScale();
            double percentX = guiWidth / (double) this.layout.autoScalingWidth * 100.0;
            double percentY = guiHeight / (double) this.layout.autoScalingHeight * 100.0;
            double newScaleX = percentX / 100.0 * mx.getGuiScale();
            double newScaleY = percentY / 100.0 * mx.getGuiScale();
            double newScale = Math.min(newScaleX, newScaleY);
            mx.setGuiScale(newScale);
            this.f_96543_ = mx.getGuiScaledWidth();
            this.f_96544_ = mx.getGuiScaledHeight();
        }
        this.constructElementInstances();
        for (AbstractLayoutEditorWidget w : this.layoutEditorWidgets) {
            w.refresh();
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void tick() {
        for (AbstractLayoutEditorWidget w : this.layoutEditorWidgets) {
            w.tick();
        }
        for (AbstractEditorElement e : this.getAllElements()) {
            e.element.tick();
        }
        if (this.layout.menuBackground != null) {
            this.layout.menuBackground.tick();
        }
    }

    @Override
    public void removed() {
        for (AbstractEditorElement e : this.getAllElements()) {
            e.element.onCloseScreen();
        }
        if (this.layout.menuBackground != null) {
            this.layout.menuBackground.onCloseScreen();
        }
    }

    @Override
    public void added() {
        for (AbstractEditorElement e : this.getAllElements()) {
            e.element.onOpenScreen();
        }
        if (this.layout.menuBackground != null) {
            this.layout.menuBackground.onOpenScreen();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.activeElementContextMenu != null && !this.activeElementContextMenu.isOpen()) {
            this.activeElementContextMenu = null;
        }
        this.renderBackground(graphics, mouseX, mouseY, partial);
        this.renderElements(graphics, mouseX, mouseY, partial);
        this.renderMouseSelectionRectangle(graphics, mouseX, mouseY);
        this.anchorPointOverlay.render(graphics, mouseX, mouseY, partial);
        this.renderLayoutEditorWidgets(graphics, mouseX, mouseY, partial);
        this.menuBar.render(graphics, mouseX, mouseY, partial);
        this.rightClickMenu.render(graphics, mouseX, mouseY, partial);
        if (this.activeElementContextMenu != null) {
            this.activeElementContextMenu.render(graphics, mouseX, mouseY, partial);
        }
    }

    protected void renderLayoutEditorWidgets(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        for (AbstractLayoutEditorWidget w : this.layoutEditorWidgets) {
            if (w.isVisible()) {
                w.m_88315_(graphics, mouseX, mouseY, partial);
            }
        }
    }

    protected void renderMouseSelectionRectangle(GuiGraphics graphics, int mouseX, int mouseY) {
        if (this.isMouseSelection) {
            int startX = Math.min(this.mouseSelectionStartX, mouseX);
            int startY = Math.min(this.mouseSelectionStartY, mouseY);
            int endX = Math.max(this.mouseSelectionStartX, mouseX);
            int endY = Math.max(this.mouseSelectionStartY, mouseY);
            graphics.fill(startX, startY, endX, endY, RenderingUtils.replaceAlphaInColor(UIBase.getUIColorTheme().layout_editor_mouse_selection_rectangle_color.getColorInt(), 70));
            UIBase.renderBorder(graphics, startX, startY, endX, endY, 1, UIBase.getUIColorTheme().layout_editor_mouse_selection_rectangle_color.getColor(), true, true, true, true);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected void renderElements(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.layout.renderElementsBehindVanilla) {
            for (AbstractEditorElement e : new ArrayList(this.normalEditorElements)) {
                if (!e.isSelected()) {
                    e.render(graphics, mouseX, mouseY, partial);
                }
            }
        }
        for (VanillaWidgetEditorElement ex : new ArrayList(this.vanillaWidgetEditorElements)) {
            if (!ex.isSelected() && !ex.isHidden()) {
                ex.m_88315_(graphics, mouseX, mouseY, partial);
            }
        }
        for (AbstractDeepEditorElement exx : new ArrayList(this.deepEditorElements)) {
            if (!exx.isSelected() && !exx.isHidden()) {
                exx.m_88315_(graphics, mouseX, mouseY, partial);
            }
        }
        if (!this.layout.renderElementsBehindVanilla) {
            for (AbstractEditorElement exxx : new ArrayList(this.normalEditorElements)) {
                if (!exxx.isSelected()) {
                    exxx.render(graphics, mouseX, mouseY, partial);
                }
            }
        }
        for (AbstractEditorElement exxxx : this.getSelectedElements()) {
            exxxx.render(graphics, mouseX, mouseY, partial);
        }
    }

    protected void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.layout.menuBackground != null) {
            this.layout.menuBackground.keepBackgroundAspectRatio = this.layout.preserveBackgroundAspectRatio;
            this.layout.menuBackground.opacity = 1.0F;
            this.layout.menuBackground.render(graphics, mouseX, mouseY, partial);
        } else {
            graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color_darker.getColorInt());
        }
        RenderingUtils.resetShaderColor(graphics);
        this.renderScrollListHeaderFooterPreview(graphics, mouseX, mouseY, partial);
        this.renderGrid(graphics);
    }

    protected void renderScrollListHeaderFooterPreview(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.layout.showScrollListHeaderFooterPreviewInEditor) {
            int x0 = 0;
            int x1 = this.f_96543_;
            int y0 = 48;
            int y1 = this.f_96544_ - 64;
            ITexture headerTexture = this.layout.scrollListHeaderTexture != null ? this.layout.scrollListHeaderTexture.get() : null;
            ITexture footerTexture = this.layout.scrollListFooterTexture != null ? this.layout.scrollListFooterTexture.get() : null;
            if (headerTexture != null) {
                ResourceLocation loc = headerTexture.getResourceLocation();
                if (loc != null) {
                    RenderingUtils.resetShaderColor(graphics);
                    if (this.layout.preserveScrollListHeaderFooterAspectRatio) {
                        int[] headerSize = headerTexture.getAspectRatio().getAspectRatioSizeByMinimumSize(this.f_96543_, y0);
                        int headerWidth = headerSize[0];
                        int headerHeight = headerSize[1];
                        int headerX = x0 + this.f_96543_ / 2 - headerWidth / 2;
                        int headerY = y0 / 2 - headerHeight / 2;
                        graphics.enableScissor(x0, 0, x0 + this.f_96543_, y0);
                        graphics.blit(loc, headerX, headerY, 0.0F, 0.0F, headerWidth, headerHeight, headerWidth, headerHeight);
                        graphics.disableScissor();
                    } else if (this.layout.repeatScrollListHeaderTexture) {
                        RenderingUtils.blitRepeat(graphics, loc, x0, 0, this.f_96543_, y0, headerTexture.getWidth(), headerTexture.getHeight());
                    } else {
                        graphics.blit(loc, x0, 0, 0.0F, 0.0F, this.f_96543_, y0, this.f_96543_, y0);
                    }
                }
            } else {
                graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
                graphics.blit(Screen.BACKGROUND_LOCATION, x0, 0, 0.0F, 0.0F, this.f_96543_, y0, 32, 32);
            }
            if (footerTexture != null) {
                ResourceLocation loc = footerTexture.getResourceLocation();
                if (loc != null) {
                    RenderingUtils.resetShaderColor(graphics);
                    if (this.layout.preserveScrollListHeaderFooterAspectRatio) {
                        int footerOriginalHeight = this.f_96544_ - y1;
                        int[] footerSize = footerTexture.getAspectRatio().getAspectRatioSizeByMinimumSize(this.f_96543_, footerOriginalHeight);
                        int footerWidth = footerSize[0];
                        int footerHeight = footerSize[1];
                        int footerX = x0 + this.f_96543_ / 2 - footerWidth / 2;
                        int footerY = y1 + footerOriginalHeight / 2 - footerHeight / 2;
                        graphics.enableScissor(x0, y1, x0 + this.f_96543_, y1 + footerOriginalHeight);
                        graphics.blit(loc, footerX, footerY, 0.0F, 0.0F, footerWidth, footerHeight, footerWidth, footerHeight);
                        graphics.disableScissor();
                    } else if (this.layout.repeatScrollListFooterTexture) {
                        int footerHeight = this.f_96544_ - y1;
                        RenderingUtils.blitRepeat(graphics, loc, x0, y1, this.f_96543_, footerHeight, footerTexture.getWidth(), footerTexture.getHeight());
                    } else {
                        int footerHeight = this.f_96544_ - y1;
                        graphics.blit(loc, x0, y1, 0.0F, 0.0F, this.f_96543_, footerHeight, this.f_96543_, footerHeight);
                    }
                }
            } else {
                graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
                graphics.blit(Screen.BACKGROUND_LOCATION, x0, y1, 0.0F, (float) y1, this.f_96543_, this.f_96544_ - y1, 32, 32);
            }
            RenderingUtils.resetShaderColor(graphics);
            if (this.layout.renderScrollListHeaderShadow) {
                graphics.fillGradient(x0, y0, x1, y0 + 4, -16777216, 0);
            }
            if (this.layout.renderScrollListFooterShadow) {
                graphics.fillGradient(x0, y1 - 4, x1, y1, 0, -16777216);
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderGrid(@NotNull GuiGraphics graphics) {
        if (FancyMenu.getOptions().showLayoutEditorGrid.getValue()) {
            float scale = UIBase.calculateFixedScale(1.0F);
            int scaledWidth = (int) ((float) this.f_96543_ / scale);
            int scaledHeight = (int) ((float) this.f_96544_ / scale);
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            int gridSize = FancyMenu.getOptions().layoutEditorGridSize.getValue();
            int lineThickness = 1;
            graphics.fill(scaledWidth / 2 - 1, 0, scaledWidth / 2 + 1, scaledHeight, UIBase.getUIColorTheme().layout_editor_grid_color_center.getColorInt());
            for (int linesVerticalToLeftPosX = scaledWidth / 2 - gridSize - 1; linesVerticalToLeftPosX > 0; linesVerticalToLeftPosX -= gridSize) {
                int minY = 0;
                int maxX = linesVerticalToLeftPosX + lineThickness;
                graphics.fill(linesVerticalToLeftPosX, minY, maxX, scaledHeight, UIBase.getUIColorTheme().layout_editor_grid_color_normal.getColorInt());
            }
            for (int linesVerticalToRightPosX = scaledWidth / 2 + gridSize; linesVerticalToRightPosX < scaledWidth; linesVerticalToRightPosX += gridSize) {
                int minY = 0;
                int maxX = linesVerticalToRightPosX + lineThickness;
                graphics.fill(linesVerticalToRightPosX, minY, maxX, scaledHeight, UIBase.getUIColorTheme().layout_editor_grid_color_normal.getColorInt());
            }
            graphics.fill(0, scaledHeight / 2 - 1, scaledWidth, scaledHeight / 2 + 1, UIBase.getUIColorTheme().layout_editor_grid_color_center.getColorInt());
            for (int linesHorizontalToTopPosY = scaledHeight / 2 - gridSize - 1; linesHorizontalToTopPosY > 0; linesHorizontalToTopPosY -= gridSize) {
                int minX = 0;
                int maxY = linesHorizontalToTopPosY + lineThickness;
                graphics.fill(minX, linesHorizontalToTopPosY, scaledWidth, maxY, UIBase.getUIColorTheme().layout_editor_grid_color_normal.getColorInt());
            }
            for (int linesHorizontalToBottomPosY = scaledHeight / 2 + gridSize; linesHorizontalToBottomPosY < scaledHeight; linesHorizontalToBottomPosY += gridSize) {
                int minX = 0;
                int maxY = linesHorizontalToBottomPosY + lineThickness;
                graphics.fill(minX, linesHorizontalToBottomPosY, scaledWidth, maxY, UIBase.getUIColorTheme().layout_editor_grid_color_normal.getColorInt());
            }
            graphics.pose().popPose();
        }
    }

    protected void constructElementInstances() {
        for (AbstractEditorElement e : this.getAllElements()) {
            e.resetElementStates();
        }
        this.normalEditorElements.clear();
        this.vanillaWidgetEditorElements.clear();
        this.deepEditorElements.clear();
        Layout.OrderedElementCollection normalElements = new Layout.OrderedElementCollection();
        List<VanillaWidgetElement> vanillaWidgetElements = this.layoutTargetScreen != null ? new ArrayList() : null;
        List<AbstractDeepElement> deepElements = this.layoutTargetScreen != null ? new ArrayList() : null;
        this.cachedVanillaWidgetMetas.clear();
        if (this.layoutTargetScreen != null) {
            this.cachedVanillaWidgetMetas.addAll(ScreenWidgetDiscoverer.getWidgetsOfScreen(this.layoutTargetScreen, true));
        }
        for (WidgetMeta m : this.cachedVanillaWidgetMetas) {
            if (m.getWidget() instanceof CustomizableWidget w) {
                w.resetWidgetCustomizationsFancyMenu();
            }
        }
        this.constructElementInstances(this.layout.screenIdentifier, this.cachedVanillaWidgetMetas, this.layout, normalElements, vanillaWidgetElements, deepElements);
        for (AbstractElement e : ListUtils.mergeLists(normalElements.backgroundElements, normalElements.foregroundElements)) {
            AbstractEditorElement editorElement = e.builder.wrapIntoEditorElementInternal(e, this);
            if (editorElement != null) {
                this.normalEditorElements.add(editorElement);
            }
        }
        if (deepElements != null) {
            for (AbstractElement ex : deepElements) {
                if (ex.builder.wrapIntoEditorElementInternal(ex, this) instanceof AbstractDeepEditorElement d) {
                    this.deepEditorElements.add(d);
                }
            }
        }
        if (vanillaWidgetElements != null) {
            for (VanillaWidgetElement exx : vanillaWidgetElements) {
                VanillaWidgetEditorElement editorElement = (VanillaWidgetEditorElement) VanillaWidgetElementBuilder.INSTANCE.wrapIntoEditorElementInternal(exx, this);
                if (editorElement != null) {
                    this.vanillaWidgetEditorElements.add(editorElement);
                }
            }
        }
    }

    protected void serializeElementInstancesToLayoutInstance() {
        this.layout.serializedElements.clear();
        this.layout.serializedVanillaButtonElements.clear();
        this.layout.serializedDeepElements.clear();
        for (AbstractEditorElement e : this.normalEditorElements) {
            SerializedElement serialized = e.element.builder.serializeElementInternal(e.element);
            if (serialized != null) {
                this.layout.serializedElements.add(serialized);
            }
        }
        for (AbstractEditorElement ex : this.deepEditorElements) {
            SerializedElement serialized = ex.element.builder.serializeElementInternal(ex.element);
            if (serialized != null) {
                this.layout.serializedDeepElements.add(serialized);
            }
        }
        for (VanillaWidgetEditorElement exx : this.vanillaWidgetEditorElements) {
            SerializedElement serialized = VanillaWidgetElementBuilder.INSTANCE.serializeElementInternal(exx.element);
            if (serialized != null) {
                this.layout.serializedVanillaButtonElements.add(serialized);
            }
        }
    }

    @NotNull
    public List<AbstractEditorElement> getAllElements() {
        List<AbstractEditorElement> elements = new ArrayList();
        List<AbstractEditorElement> selected = new ArrayList();
        List<AbstractEditorElement> elementsFinal = new ArrayList();
        if (this.layout.renderElementsBehindVanilla) {
            elements.addAll(this.normalEditorElements);
        }
        elements.addAll(this.vanillaWidgetEditorElements);
        elements.addAll(this.deepEditorElements);
        if (!this.layout.renderElementsBehindVanilla) {
            elements.addAll(this.normalEditorElements);
        }
        for (AbstractEditorElement e : elements) {
            if (!e.isSelected()) {
                elementsFinal.add(e);
            } else {
                selected.add(e);
            }
        }
        elementsFinal.addAll(selected);
        return elementsFinal;
    }

    @NotNull
    public List<AbstractEditorElement> getHoveredElements() {
        List<AbstractEditorElement> elements = new ArrayList();
        for (AbstractEditorElement e : this.getAllElements()) {
            if (e.isHovered()) {
                boolean var10000;
                label21: {
                    if (e instanceof HideableElement h && h.isHidden()) {
                        var10000 = true;
                        break label21;
                    }
                    var10000 = false;
                }
                boolean hidden = var10000;
                if (!hidden) {
                    elements.add(e);
                }
            }
        }
        return elements;
    }

    @Nullable
    public AbstractEditorElement getTopHoveredElement() {
        List<AbstractEditorElement> hoveredElements = this.getHoveredElements();
        return !hoveredElements.isEmpty() ? (AbstractEditorElement) hoveredElements.get(hoveredElements.size() - 1) : null;
    }

    @NotNull
    public List<AbstractEditorElement> getSelectedElements() {
        List<AbstractEditorElement> l = new ArrayList();
        this.getAllElements().forEach(element -> {
            if (element.isSelected()) {
                l.add(element);
            }
        });
        return l;
    }

    @NotNull
    protected <E extends AbstractEditorElement> List<E> getSelectedElementsOfType(@NotNull Class<E> type) {
        List<E> l = new ArrayList();
        for (AbstractEditorElement e : this.getSelectedElements()) {
            if (type.isAssignableFrom(e.getClass())) {
                l.add(e);
            }
        }
        return l;
    }

    @Nullable
    public AbstractEditorElement getElementByInstanceIdentifier(@NotNull String instanceIdentifier) {
        instanceIdentifier = instanceIdentifier.replace("vanillabtn:", "").replace("button_compatibility_id:", "");
        for (AbstractEditorElement e : this.getAllElements()) {
            if (e.element.getInstanceIdentifier().equals(instanceIdentifier)) {
                return e;
            }
        }
        return null;
    }

    public void selectAllElements() {
        for (AbstractEditorElement e : this.getAllElements()) {
            e.setSelected(true);
        }
    }

    public void deselectAllElements() {
        for (AbstractEditorElement e : this.getAllElements()) {
            e.setSelected(false);
        }
    }

    public boolean deleteElement(@NotNull AbstractEditorElement element) {
        if (element.settings.isDestroyable()) {
            if (!element.settings.shouldHideInsteadOfDestroy()) {
                this.normalEditorElements.remove(element);
                this.vanillaWidgetEditorElements.remove(element);
                this.deepEditorElements.remove(element);
                for (AbstractLayoutEditorWidget w : this.layoutEditorWidgets) {
                    w.editorElementRemovedOrHidden(element);
                }
                return true;
            }
            if (element instanceof HideableElement hideable) {
                hideable.setHidden(true);
                return true;
            }
        }
        return false;
    }

    protected boolean isElementOverlappingArea(@NotNull AbstractEditorElement element, int xStart, int yStart, int xEnd, int yEnd) {
        int elementStartX = element.getX();
        int elementStartY = element.getY();
        int elementEndX = element.getX() + element.getWidth();
        int elementEndY = element.getY() + element.getHeight();
        return xEnd > elementStartX && yEnd > elementStartY && yStart < elementEndY && xStart < elementEndX;
    }

    public boolean allSelectedElementsMovable() {
        for (AbstractEditorElement e : this.getSelectedElements()) {
            if (!e.settings.isMovable()) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveLayerUp(AbstractEditorElement element) {
        int index = this.normalEditorElements.indexOf(element);
        return index == -1 ? false : index < this.normalEditorElements.size() - 1;
    }

    public boolean canMoveLayerDown(AbstractEditorElement element) {
        int index = this.normalEditorElements.indexOf(element);
        return index > 0;
    }

    @Nullable
    public AbstractEditorElement moveLayerUp(@NotNull AbstractEditorElement element) {
        AbstractEditorElement movedAbove = null;
        try {
            if (this.normalEditorElements.contains(element)) {
                List<AbstractEditorElement> newNormalEditorElements = new ArrayList();
                int index = this.normalEditorElements.indexOf(element);
                int i = 0;
                if (index < this.normalEditorElements.size() - 1) {
                    for (AbstractEditorElement e : this.normalEditorElements) {
                        if (e != element) {
                            newNormalEditorElements.add(e);
                            if (i == index + 1) {
                                movedAbove = e;
                                newNormalEditorElements.add(element);
                            }
                        }
                        i++;
                    }
                    this.normalEditorElements = newNormalEditorElements;
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return movedAbove;
    }

    @Nullable
    public AbstractEditorElement moveLayerDown(AbstractEditorElement element) {
        AbstractEditorElement movedBehind = null;
        try {
            if (this.normalEditorElements.contains(element)) {
                List<AbstractEditorElement> newNormalEditorElements = new ArrayList();
                int index = this.normalEditorElements.indexOf(element);
                int i = 0;
                if (index > 0) {
                    for (AbstractEditorElement e : this.normalEditorElements) {
                        if (e != element) {
                            if (i == index - 1) {
                                newNormalEditorElements.add(element);
                                movedBehind = e;
                            }
                            newNormalEditorElements.add(e);
                        }
                        i++;
                    }
                    this.normalEditorElements = newNormalEditorElements;
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return movedBehind;
    }

    public void copyElementsToClipboard(AbstractEditorElement... elements) {
        if (elements != null && elements.length > 0) {
            COPIED_ELEMENTS_CLIPBOARD.clear();
            for (AbstractEditorElement e : elements) {
                if (e.settings.isCopyable()) {
                    SerializedElement serialized = e.element.builder.serializeElementInternal(e.element);
                    if (serialized != null) {
                        serialized.putProperty("instance_identifier", ScreenCustomization.generateUniqueIdentifier());
                        COPIED_ELEMENTS_CLIPBOARD.put(serialized, e.element.builder);
                    }
                }
            }
        }
    }

    public void pasteElementsFromClipboard() {
        if (!COPIED_ELEMENTS_CLIPBOARD.isEmpty()) {
            this.deselectAllElements();
            for (Entry<SerializedElement, ElementBuilder<?, ?>> m : COPIED_ELEMENTS_CLIPBOARD.entrySet()) {
                ((SerializedElement) m.getKey()).putProperty("instance_identifier", ScreenCustomization.generateUniqueIdentifier());
                AbstractElement deserialized = ((ElementBuilder) m.getValue()).deserializeElementInternal((SerializedElement) m.getKey());
                if (deserialized != null) {
                    AbstractEditorElement deserializedEditorElement = ((ElementBuilder) m.getValue()).wrapIntoEditorElementInternal(deserialized, this);
                    if (deserializedEditorElement != null) {
                        this.normalEditorElements.add(deserializedEditorElement);
                        this.layoutEditorWidgets.forEach(widget -> widget.editorElementAdded(deserializedEditorElement));
                        deserializedEditorElement.setSelected(true);
                    }
                }
            }
        }
    }

    public void saveLayout() {
        if (this.layout.layoutFile != null) {
            this.layout.updateLastEditedTime();
            this.serializeElementInstancesToLayoutInstance();
            if (!this.layout.saveToFileIfPossible()) {
                Minecraft.getInstance().setScreen(NotificationScreen.error(call -> Minecraft.getInstance().setScreen(this), LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.saving_failed.generic")));
            } else {
                LayoutHandler.reloadLayouts();
            }
        } else {
            this.saveLayoutAs();
        }
    }

    public void saveLayoutAs() {
        String fileNamePreset = "universal_layout";
        if (this.layoutTargetScreen != null) {
            fileNamePreset = ScreenIdentifierHandler.getIdentifierOfScreen(this.layoutTargetScreen) + "_layout";
        }
        fileNamePreset = fileNamePreset.toLowerCase();
        fileNamePreset = CharacterFilter.buildOnlyLowercaseFileNameFilter().filterForAllowedChars(fileNamePreset);
        fileNamePreset = FileUtils.generateAvailableFilename(LayoutHandler.LAYOUT_DIR.getAbsolutePath(), fileNamePreset, "txt");
        if (this.layout.layoutFile != null) {
            fileNamePreset = this.layout.layoutFile.getName();
        }
        SaveFileScreen s = (SaveFileScreen) SaveFileScreen.build(LayoutHandler.LAYOUT_DIR, fileNamePreset, "txt", call -> {
            if (call != null) {
                try {
                    this.layout.updateLastEditedTime();
                    this.serializeElementInstancesToLayoutInstance();
                    this.layout.layoutFile = call.getAbsoluteFile();
                    if (this.layout.layoutFile.isFile()) {
                        Layout old = LayoutHandler.getLayout(this.layout.getLayoutName());
                        if (old != null) {
                            old.delete(false);
                        }
                    }
                    if (!this.layout.saveToFileIfPossible()) {
                        Minecraft.getInstance().setScreen(NotificationScreen.error(call2 -> Minecraft.getInstance().setScreen(this), LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.saving_failed.generic")));
                    } else {
                        LayoutHandler.reloadLayouts();
                    }
                } catch (Exception var3x) {
                    var3x.printStackTrace();
                    Minecraft.getInstance().setScreen(NotificationScreen.error(call2 -> Minecraft.getInstance().setScreen(this), LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.saving_failed.generic")));
                }
            }
            Minecraft.getInstance().setScreen(this);
        }).setVisibleDirectoryLevelsAboveRoot(2).setShowSubDirectories(false);
        FileTypeGroup<?> fileTypeGroup = FileTypeGroup.of(FileTypes.TXT_TEXT);
        fileTypeGroup.setDisplayName(Component.translatable("fancymenu.file_types.groups.text"));
        s.setFileTypes(fileTypeGroup);
        Minecraft.getInstance().setScreen(s);
    }

    public void onUpdateSelectedElements() {
        List<AbstractEditorElement> selected = this.getSelectedElements();
        if (selected.size() > 1) {
            for (AbstractEditorElement e : selected) {
                e.setMultiSelected(true);
            }
        } else if (selected.size() == 1) {
            ((AbstractEditorElement) selected.get(0)).setMultiSelected(false);
        }
    }

    public void openRightClickMenuAtMouse(int mouseX, int mouseY) {
        if (this.rightClickMenu != null) {
            this.rightClickMenuOpenPosX = mouseX;
            this.rightClickMenuOpenPosY = mouseY;
            this.rightClickMenu.openMenuAtMouse();
        }
    }

    public void closeRightClickMenu() {
        if (this.rightClickMenu != null) {
            if (this.rightClickMenu.isUserNavigatingInMenu()) {
                return;
            }
            this.rightClickMenuOpenPosX = -1000;
            this.rightClickMenuOpenPosY = -1000;
            this.rightClickMenu.closeMenu();
        }
    }

    public void openElementContextMenuAtMouseIfPossible() {
        this.closeActiveElementMenu();
        List<AbstractEditorElement> selectedElements = this.getSelectedElements();
        if (selectedElements.size() == 1) {
            this.activeElementContextMenu = ((AbstractEditorElement) selectedElements.get(0)).rightClickMenu;
            ((IMixinScreen) this).getChildrenFancyMenu().add(0, this.activeElementContextMenu);
            this.activeElementContextMenu.openMenuAtMouse();
        } else if (selectedElements.size() > 1) {
            List<ContextMenu> menus = ObjectUtils.getOfAll(ContextMenu.class, selectedElements, consumes -> consumes.rightClickMenu);
            this.activeElementContextMenu = ContextMenu.stackContextMenus(menus);
            ((IMixinScreen) this).getChildrenFancyMenu().add(0, this.activeElementContextMenu);
            this.activeElementContextMenu.openMenuAtMouse();
        }
    }

    public void closeActiveElementMenu(boolean forceClose) {
        if (this.activeElementContextMenu != null) {
            if (!forceClose && this.activeElementContextMenu.isUserNavigatingInMenu()) {
                return;
            }
            this.activeElementContextMenu.closeMenu();
            this.m_169411_(this.activeElementContextMenu);
        }
        this.activeElementContextMenu = null;
    }

    public void closeActiveElementMenu() {
        this.closeActiveElementMenu(false);
    }

    public boolean isUserNavigatingInRightClickMenu() {
        return this.rightClickMenu != null && this.rightClickMenu.isUserNavigatingInMenu();
    }

    public boolean isUserNavigatingInElementMenu() {
        return this.activeElementContextMenu != null && this.activeElementContextMenu.isUserNavigatingInMenu();
    }

    public void saveWidgetSettings() {
        for (AbstractLayoutEditorWidget w : this.layoutEditorWidgets) {
            w.getBuilder().writeSettingsInternal(w);
        }
    }

    @NotNull
    public List<AbstractEditorElement> getCurrentlyDraggedElements() {
        return this.currentlyDraggedElements;
    }

    @Nullable
    protected List<AbstractEditorElement> getElementChildChainOfExcluding(@NotNull AbstractEditorElement element) {
        Objects.requireNonNull(element);
        List<AbstractEditorElement> chain = new ArrayList();
        try {
            AbstractEditorElement e = element;
            while (true) {
                e = this.getChildElementOf(e);
                if (e == null) {
                    return chain;
                }
                if (e == element) {
                    throw new IllegalStateException("Child of origin element is its own child. This shouldn't be possible and comes from an invalid ELEMENT anchor point. You need to manually fix this.");
                }
                if (chain.contains(e)) {
                    throw new IllegalStateException("Chain already contains element! This shouldn't be possible and probably comes from an invalid ELEMENT anchor who's child is its parent or similar scenarios (sweet home Alabama). You need to manually fix this.");
                }
                chain.add(e);
            }
        } catch (Exception var4) {
            LOGGER.error("[FANCYMENU] There was an error while trying to get the element chain!", var4);
            return null;
        }
    }

    @Nullable
    protected AbstractEditorElement getChildElementOf(@NotNull AbstractEditorElement element) {
        for (AbstractEditorElement e : this.getAllElements()) {
            String parentOfE = e.element.anchorPointElementIdentifier;
            if (parentOfE != null && parentOfE.equals(element.element.getInstanceIdentifier())) {
                return e;
            }
        }
        return null;
    }

    protected void moveSelectedElementsByXYOffset(int offsetX, int offsetY) {
        List<AbstractEditorElement> selected = this.getSelectedElements();
        if (!selected.isEmpty() && this.allSelectedElementsMovable()) {
            this.history.saveSnapshot();
        }
        boolean multiSelect = selected.size() > 1;
        for (AbstractEditorElement e : selected) {
            if (this.allSelectedElementsMovable()) {
                if (!multiSelect || !e.isElementAnchorAndParentIsSelected()) {
                    e.element.posOffsetX += offsetX;
                    e.element.posOffsetY += offsetY;
                }
            } else if (!e.settings.isMovable()) {
                e.renderMovingNotAllowedTime = System.currentTimeMillis() + 800L;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.leftMouseDownPosX = (int) mouseX;
        this.leftMouseDownPosY = (int) mouseY;
        boolean menuBarContextOpen = this.menuBar != null && this.menuBar.isEntryContextMenuOpen();
        if (PopupHandler.isPopupActive()) {
            this.closeRightClickMenu();
            this.closeActiveElementMenu();
            return false;
        } else if (super.m_6375_(mouseX, mouseY, button)) {
            this.closeRightClickMenu();
            this.closeActiveElementMenu();
            return true;
        } else if (menuBarContextOpen) {
            return true;
        } else {
            AbstractEditorElement topHoverElement = this.getTopHoveredElement();
            boolean topHoverGotSelected = false;
            if (topHoverElement != null && !this.rightClickMenu.isUserNavigatingInMenu() && (this.activeElementContextMenu == null || !this.activeElementContextMenu.isUserNavigatingInMenu()) && !topHoverElement.isSelected()) {
                topHoverElement.setSelected(true);
                topHoverElement.recentlyLeftClickSelected = true;
                topHoverGotSelected = true;
            }
            boolean canStartMouseSelection = true;
            for (AbstractEditorElement e : this.getAllElements()) {
                e.mouseClicked(mouseX, mouseY, button);
                if (e.isHovered() || e.isGettingResized() || e.getHoveredResizeGrabber() != null) {
                    canStartMouseSelection = false;
                }
            }
            if (button == 0 && canStartMouseSelection) {
                this.isMouseSelection = true;
                this.mouseSelectionStartX = (int) mouseX;
                this.mouseSelectionStartY = (int) mouseY;
            }
            if (!this.rightClickMenu.isUserNavigatingInMenu() && (this.activeElementContextMenu == null || !this.activeElementContextMenu.isUserNavigatingInMenu()) && !m_96637_() && (button == 0 || button == 1 && (topHoverElement == null || topHoverGotSelected))) {
                for (AbstractEditorElement ex : this.getAllElements()) {
                    if (!ex.isGettingResized() && (topHoverElement == null || ex != topHoverElement)) {
                        ex.setSelected(false);
                    }
                }
            }
            this.closeActiveElementMenu();
            if (button == 0 && !this.rightClickMenu.isUserNavigatingInMenu()) {
                this.closeRightClickMenu();
            }
            if (topHoverElement == null) {
                if (button == 1) {
                    this.openRightClickMenuAtMouse((int) mouseX, (int) mouseY);
                }
            } else {
                this.closeRightClickMenu();
                if (button == 1) {
                    this.openElementContextMenuAtMouseIfPossible();
                }
            }
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.anchorPointOverlay.mouseReleased(mouseX, mouseY, button);
        this.elementMovingStarted = false;
        this.elementResizingStarted = false;
        this.currentlyDraggedElements.clear();
        boolean mouseWasInDraggingMode = this.mouseDraggingStarted;
        this.mouseDraggingStarted = false;
        boolean cachedMouseSelection = this.isMouseSelection;
        if (button == 0) {
            this.isMouseSelection = false;
        }
        if (PopupHandler.isPopupActive()) {
            return false;
        } else {
            this.m_7897_(false);
            for (GuiEventListener child : this.m_6702_()) {
                if (child.mouseReleased(mouseX, mouseY, button)) {
                    return true;
                }
            }
            List<AbstractEditorElement> hoveredElements = this.getHoveredElements();
            AbstractEditorElement topHoverElement = !hoveredElements.isEmpty() ? (AbstractEditorElement) hoveredElements.get(hoveredElements.size() - 1) : null;
            if (!mouseWasInDraggingMode && !cachedMouseSelection && button == 0 && topHoverElement != null && topHoverElement.isSelected() && !topHoverElement.recentlyMovedByDragging && !topHoverElement.recentlyLeftClickSelected && m_96637_()) {
                topHoverElement.setSelected(false);
            }
            boolean elementRecentlyMovedByDragging = false;
            for (AbstractEditorElement e : this.getAllElements()) {
                if (e.recentlyMovedByDragging) {
                    elementRecentlyMovedByDragging = true;
                }
                e.mouseReleased(mouseX, mouseY, button);
                e.recentlyLeftClickSelected = false;
            }
            if (elementRecentlyMovedByDragging && this.preDragElementSnapshot != null) {
                this.history.saveSnapshot(this.preDragElementSnapshot);
            }
            this.preDragElementSnapshot = null;
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double $$3, double $$4) {
        if (PopupHandler.isPopupActive()) {
            return false;
        } else if (super.m_7979_(mouseX, mouseY, button, $$3, $$4)) {
            return true;
        } else {
            if (this.isMouseSelection) {
                for (AbstractEditorElement e : this.getAllElements()) {
                    boolean b = this.isElementOverlappingArea(e, Math.min(this.mouseSelectionStartX, (int) mouseX), Math.min(this.mouseSelectionStartY, (int) mouseY), Math.max(this.mouseSelectionStartX, (int) mouseX), Math.max(this.mouseSelectionStartY, (int) mouseY));
                    if (b || !m_96637_()) {
                        e.setSelected(b);
                    }
                }
            }
            if (this.preDragElementSnapshot == null) {
                this.preDragElementSnapshot = this.history.createSnapshot();
            }
            int draggingDiffX = (int) (mouseX - (double) this.leftMouseDownPosX);
            int draggingDiffY = (int) (mouseY - (double) this.leftMouseDownPosY);
            if (draggingDiffX != 0 || draggingDiffY != 0) {
                this.mouseDraggingStarted = true;
            }
            List<AbstractEditorElement> allElements = this.getAllElements();
            if (!this.elementResizingStarted) {
                allElements.forEach(element -> element.updateResizingStartPos((int) mouseX, (int) mouseY));
            }
            this.elementResizingStarted = true;
            boolean movingCrumpleZonePassed = Math.abs(draggingDiffX) >= 5 || Math.abs(draggingDiffY) >= 5;
            if (movingCrumpleZonePassed) {
                if (!this.elementMovingStarted) {
                    allElements.forEach(element -> {
                        element.updateMovingStartPos((int) mouseX, (int) mouseY);
                        element.movingCrumpleZonePassed = true;
                    });
                    if (this.allSelectedElementsMovable()) {
                        this.currentlyDraggedElements.addAll(this.getSelectedElements());
                    }
                }
                this.elementMovingStarted = true;
            }
            for (AbstractEditorElement ex : allElements) {
                if (ex.mouseDragged(mouseX, mouseY, button, $$3, $$4)) {
                    break;
                }
            }
            return false;
        }
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        if (PopupHandler.isPopupActive()) {
            return false;
        } else {
            this.anchorPointOverlay.keyPressed(keycode, scancode, modifiers);
            if (super.keyPressed(keycode, scancode, modifiers)) {
                return true;
            } else {
                String key = GLFW.glfwGetKeyName(keycode, scancode);
                if (key == null) {
                    key = "";
                }
                if (keycode == 263) {
                    this.moveSelectedElementsByXYOffset(-1, 0);
                    return true;
                } else if (keycode == 265) {
                    this.moveSelectedElementsByXYOffset(0, -1);
                    return true;
                } else if (keycode == 262) {
                    this.moveSelectedElementsByXYOffset(1, 0);
                    return true;
                } else if (keycode == 264) {
                    this.moveSelectedElementsByXYOffset(0, 1);
                    return true;
                } else {
                    if (key.equals("a") && m_96637_()) {
                        this.selectAllElements();
                    }
                    if (key.equals("c") && m_96637_()) {
                        this.copyElementsToClipboard((AbstractEditorElement[]) this.getSelectedElements().toArray(new AbstractEditorElement[0]));
                        return true;
                    } else if (key.equals("v") && m_96637_()) {
                        this.pasteElementsFromClipboard();
                        return true;
                    } else if (key.equals("s") && m_96637_()) {
                        this.saveLayout();
                        return true;
                    } else if (key.equals("z") && m_96637_()) {
                        this.history.stepBack();
                        return true;
                    } else if (key.equals("y") && m_96637_()) {
                        this.history.stepForward();
                        return true;
                    } else if (key.equals("g") && m_96637_()) {
                        try {
                            FancyMenu.getOptions().showLayoutEditorGrid.setValue(!FancyMenu.getOptions().showLayoutEditorGrid.getValue());
                        } catch (Exception var7) {
                            var7.printStackTrace();
                        }
                        return true;
                    } else if (keycode != 261) {
                        return super.keyPressed(keycode, scancode, modifiers);
                    } else {
                        this.history.saveSnapshot();
                        for (AbstractEditorElement e : this.getSelectedElements()) {
                            e.deleteElement();
                        }
                        return true;
                    }
                }
            }
        }
    }

    @Override
    public boolean keyReleased(int keycode, int scancode, int modifiers) {
        this.anchorPointOverlay.keyReleased(keycode, scancode, modifiers);
        return super.m_7920_(keycode, scancode, modifiers);
    }

    public void closeEditor() {
        this.saveWidgetSettings();
        currentInstance = null;
        if (this.layoutTargetScreen != null) {
            if (!((IMixinScreen) this.layoutTargetScreen).get_initialized_FancyMenu()) {
                Minecraft.getInstance().setScreen(this.layoutTargetScreen);
            } else {
                Minecraft.getInstance().setScreen(new GenericDirtMessageScreen(Component.literal("Closing editor..")));
                Minecraft.getInstance().screen = this.layoutTargetScreen;
                ScreenCustomization.reInitCurrentScreen();
            }
        } else {
            Minecraft.getInstance().setScreen(null);
        }
    }

    public LayoutEditorScreen setAsCurrentInstance() {
        currentInstance = this;
        return this;
    }

    @Nullable
    public static LayoutEditorScreen getCurrentInstance() {
        return currentInstance;
    }
}