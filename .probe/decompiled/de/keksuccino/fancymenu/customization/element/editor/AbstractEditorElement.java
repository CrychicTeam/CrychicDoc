package de.keksuccino.fancymenu.customization.element.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoint;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.layout.editor.AnchorPointOverlay;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.layout.editor.loadingrequirements.ManageRequirementsScreen;
import de.keksuccino.fancymenu.customization.loadingrequirement.internal.LoadingRequirementContainer;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.ListUtils;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.ObjectUtils;
import de.keksuccino.fancymenu.util.cycle.ValueCycle;
import de.keksuccino.fancymenu.util.file.FileFilter;
import de.keksuccino.fancymenu.util.file.GameDirectoryUtils;
import de.keksuccino.fancymenu.util.file.type.FileType;
import de.keksuccino.fancymenu.util.file.type.groups.FileTypeGroup;
import de.keksuccino.fancymenu.util.file.type.groups.FileTypeGroups;
import de.keksuccino.fancymenu.util.input.CharacterFilter;
import de.keksuccino.fancymenu.util.input.TextValidators;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.cursor.CursorHandler;
import de.keksuccino.fancymenu.util.rendering.ui.screen.ConfirmationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.NotificationScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.TextInputScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.resource.ResourceChooserScreen;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.TextEditorScreen;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.resource.Resource;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.fancymenu.util.resource.resources.video.IVideo;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractEditorElement implements Renderable, GuiEventListener {

    private static final Logger LOGGER = LogManager.getLogger();

    protected static final ResourceLocation DRAGGING_NOT_ALLOWED_TEXTURE = new ResourceLocation("fancymenu", "textures/not_allowed.png");

    protected static final ResourceLocation DEPRECATED_WARNING_TEXTURE = new ResourceLocation("fancymenu", "textures/warning_20x20.png");

    protected static final ConsumingSupplier<AbstractEditorElement, Integer> BORDER_COLOR = editorElement -> editorElement.isSelected() ? UIBase.getUIColorTheme().layout_editor_element_border_color_selected.getColorInt() : UIBase.getUIColorTheme().layout_editor_element_border_color_normal.getColorInt();

    public AbstractElement element;

    public final EditorElementSettings settings;

    public ContextMenu rightClickMenu;

    public EditorElementBorderDisplay topLeftDisplay = new EditorElementBorderDisplay(this, EditorElementBorderDisplay.DisplayPosition.TOP_LEFT, EditorElementBorderDisplay.DisplayPosition.LEFT_TOP, EditorElementBorderDisplay.DisplayPosition.BOTTOM_LEFT);

    public EditorElementBorderDisplay bottomRightDisplay = new EditorElementBorderDisplay(this, EditorElementBorderDisplay.DisplayPosition.BOTTOM_RIGHT, EditorElementBorderDisplay.DisplayPosition.RIGHT_BOTTOM, EditorElementBorderDisplay.DisplayPosition.TOP_RIGHT);

    public LayoutEditorScreen editor;

    protected boolean selected = false;

    protected boolean multiSelected = false;

    protected boolean hovered = false;

    protected boolean leftMouseDown = false;

    protected double leftMouseDownMouseX = 0.0;

    protected double leftMouseDownMouseY = 0.0;

    protected int leftMouseDownBaseX = 0;

    protected int leftMouseDownBaseY = 0;

    protected int leftMouseDownBaseWidth = 0;

    protected int leftMouseDownBaseHeight = 0;

    protected int movingStartPosX = 0;

    protected int movingStartPosY = 0;

    protected int resizingStartPosX = 0;

    protected int resizingStartPosY = 0;

    protected AbstractEditorElement.ResizeGrabber[] resizeGrabbers = new AbstractEditorElement.ResizeGrabber[] { new AbstractEditorElement.ResizeGrabber(AbstractEditorElement.ResizeGrabberType.TOP), new AbstractEditorElement.ResizeGrabber(AbstractEditorElement.ResizeGrabberType.RIGHT), new AbstractEditorElement.ResizeGrabber(AbstractEditorElement.ResizeGrabberType.BOTTOM), new AbstractEditorElement.ResizeGrabber(AbstractEditorElement.ResizeGrabberType.LEFT) };

    protected AbstractEditorElement.ResizeGrabber activeResizeGrabber = null;

    protected AspectRatio resizeAspectRatio = new AspectRatio(10, 10);

    public long renderMovingNotAllowedTime = -1L;

    public boolean recentlyMovedByDragging = false;

    public boolean recentlyLeftClickSelected = false;

    public boolean movingCrumpleZonePassed = false;

    private final List<AbstractEditorElement> cachedHoveredElementsOnRightClickMenuOpen = new ArrayList();

    public AbstractEditorElement(@NotNull AbstractElement element, @NotNull final LayoutEditorScreen editor, @Nullable EditorElementSettings settings) {
        this.settings = settings != null ? settings : new EditorElementSettings();
        this.settings.editorElement = this;
        this.editor = editor;
        this.element = element;
        this.rightClickMenu = new ContextMenu() {

            @Override
            public ContextMenu openMenuAt(float x, float y, @Nullable List<String> entryPath) {
                AbstractEditorElement.this.cachedHoveredElementsOnRightClickMenuOpen.clear();
                AbstractEditorElement.this.cachedHoveredElementsOnRightClickMenuOpen.addAll(editor.getHoveredElements());
                return super.openMenuAt(x, y, entryPath);
            }
        };
        this.init();
    }

    public AbstractEditorElement(@Nonnull AbstractElement element, @Nonnull LayoutEditorScreen editor) {
        this(element, editor, new EditorElementSettings());
    }

    public void init() {
        this.rightClickMenu.closeMenu();
        this.rightClickMenu.clearEntries();
        this.topLeftDisplay.clearLines();
        this.bottomRightDisplay.clearLines();
        this.topLeftDisplay.addLine("anchor_point", () -> Component.translatable("fancymenu.element.border_display.anchor_point", this.element.anchorPoint.getDisplayName()));
        this.topLeftDisplay.addLine("pos_x", () -> Component.translatable("fancymenu.element.border_display.pos_x", this.getX() + ""));
        this.topLeftDisplay.addLine("width", () -> Component.translatable("fancymenu.element.border_display.width", this.getWidth() + ""));
        if (this.element.builder.isDeprecated()) {
            this.topLeftDisplay.addLine("deprecated_warning_line0", Component::m_237119_);
            this.topLeftDisplay.addLine("deprecated_warning_line1", () -> Component.translatable("fancymenu.editor.elements.deprecated_warning.line1").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().warning_text_color.getColorInt())));
            this.topLeftDisplay.addLine("deprecated_warning_line2", () -> Component.translatable("fancymenu.editor.elements.deprecated_warning.line2").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().warning_text_color.getColorInt())));
            this.topLeftDisplay.addLine("deprecated_warning_line3", () -> Component.translatable("fancymenu.editor.elements.deprecated_warning.line3").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().warning_text_color.getColorInt())));
        }
        this.bottomRightDisplay.addLine("pos_y", () -> Component.translatable("fancymenu.element.border_display.pos_y", this.getY() + ""));
        this.bottomRightDisplay.addLine("height", () -> Component.translatable("fancymenu.element.border_display.height", this.getHeight() + ""));
        ContextMenu pickElementMenu = new ContextMenu() {

            @NotNull
            @Override
            public ContextMenu openMenuAt(float x, float y) {
                this.clearEntries();
                int i = 0;
                for (AbstractEditorElement e : AbstractEditorElement.this.cachedHoveredElementsOnRightClickMenuOpen) {
                    this.addClickableEntry("element_" + i, e.element.getDisplayName(), (menu, entry) -> {
                        AbstractEditorElement.this.editor.getAllElements().forEach(AbstractEditorElement::resetElementStates);
                        e.setSelected(true);
                    });
                    i++;
                }
                return super.openMenuAt(x, y);
            }
        };
        this.rightClickMenu.addSubMenuEntry("pick_element", Component.translatable("fancymenu.element.general.pick_element"), pickElementMenu).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.element.general.pick_element.desc")));
        this.rightClickMenu.addSeparatorEntry("separator_1");
        if (this.settings.isIdentifierCopyable()) {
            this.rightClickMenu.addClickableEntry("copy_id", Component.translatable("fancymenu.helper.editor.items.copyid"), (menu, entry) -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(this.element.getInstanceIdentifier());
                menu.closeMenu();
            }).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.copyid.btn.desc"))).setIcon(ContextMenu.IconFactory.getIcon("notes"));
        }
        this.rightClickMenu.addSeparatorEntry("separator_2");
        if (this.settings.isAnchorPointChangeable()) {
            ContextMenu anchorPointMenu = new ContextMenu();
            this.rightClickMenu.addSubMenuEntry("anchor_point", Component.translatable("fancymenu.editor.items.setorientation"), anchorPointMenu).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.editor.items.orientation.btndesc"))).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("anchor"));
            if (this.settings.isElementAnchorPointAllowed()) {
                anchorPointMenu.addClickableEntry("anchor_point_element", ElementAnchorPoints.ELEMENT.getDisplayName(), (menu, entry) -> {
                    if (entry.getStackMeta().isFirstInStack()) {
                        TextInputScreen s = new TextInputScreen(Component.translatable("fancymenu.helper.editor.items.orientation.element.setidentifier"), null, call -> {
                            if (call != null) {
                                AbstractEditorElement editorElement = this.editor.getElementByInstanceIdentifier(call);
                                if (editorElement != null) {
                                    this.editor.history.saveSnapshot();
                                    for (AbstractEditorElement e : this.editor.getSelectedElements()) {
                                        if (e.settings.isAnchorPointChangeable() && e.settings.isElementAnchorPointAllowed()) {
                                            e.element.anchorPointElementIdentifier = editorElement.element.getInstanceIdentifier();
                                            e.element.setElementAnchorPointParent(editorElement.element);
                                            e.setAnchorPoint(ElementAnchorPoints.ELEMENT, false, e.getX(), e.getY(), true);
                                        }
                                    }
                                    Minecraft.getInstance().setScreen(this.editor);
                                } else {
                                    Minecraft.getInstance().setScreen(NotificationScreen.error(b -> Minecraft.getInstance().setScreen(this.editor), LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.orientation.element.setidentifier.identifiernotfound")));
                                }
                            } else {
                                Minecraft.getInstance().setScreen(this.editor);
                            }
                        });
                        if (!entry.getStackMeta().isPartOfStack()) {
                            s.setText(this.element.anchorPointElementIdentifier);
                        }
                        Minecraft.getInstance().setScreen(s);
                        menu.closeMenu();
                    }
                }).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.orientation.element.btn.desc"))).setStackable(true);
            }
            anchorPointMenu.addSeparatorEntry("separator_1").setStackable(true);
            for (ElementAnchorPoint p : ElementAnchorPoints.getAnchorPoints()) {
                if (p != ElementAnchorPoints.ELEMENT && (this.settings.isVanillaAnchorPointAllowed() || p != ElementAnchorPoints.VANILLA)) {
                    anchorPointMenu.addClickableEntry("anchor_point_" + p.getName().replace("-", "_"), p.getDisplayName(), (menu, entry) -> {
                        if (entry.getStackMeta().isFirstInStack()) {
                            this.editor.history.saveSnapshot();
                            for (AbstractEditorElement e : this.editor.getSelectedElements()) {
                                if (e.settings.isAnchorPointChangeable()) {
                                    e.setAnchorPoint(p, false, e.getX(), e.getY(), true);
                                }
                            }
                            menu.closeMenu();
                        }
                    }).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("anchor_" + p.getName().replace("-", "_")));
                }
            }
        }
        this.addToggleContextMenuEntryTo(this.rightClickMenu, "stay_on_screen", AbstractEditorElement.class, consumes -> consumes.element.stayOnScreen, (element1, aBoolean) -> element1.element.stayOnScreen = aBoolean, "fancymenu.elements.element.stay_on_screen").setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.elements.element.stay_on_screen.tooltip"))).setIcon(ContextMenu.IconFactory.getIcon("screen")).setStackable(true);
        if (this.settings.isAdvancedPositioningSupported()) {
            ContextMenu advancedPositioningMenu = new ContextMenu();
            this.rightClickMenu.addSubMenuEntry("advanced_positioning", Component.translatable("fancymenu.helper.editor.items.features.advanced_positioning"), advancedPositioningMenu).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.features.advanced_positioning.desc"))).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("move"));
            this.addGenericStringInputContextMenuEntryTo(advancedPositioningMenu, "advanced_positioning_x", element -> element.settings.isAdvancedPositioningSupported(), consumes -> consumes.element.advancedX, (element, input) -> element.element.advancedX = input, null, false, true, Component.translatable("fancymenu.helper.editor.items.features.advanced_positioning.posx"), true, null, TextValidators.NO_EMPTY_STRING_TEXT_VALIDATOR, null).setStackable(true);
            this.addGenericStringInputContextMenuEntryTo(advancedPositioningMenu, "advanced_positioning_y", element -> element.settings.isAdvancedPositioningSupported(), consumes -> consumes.element.advancedY, (element, input) -> element.element.advancedY = input, null, false, true, Component.translatable("fancymenu.helper.editor.items.features.advanced_positioning.posy"), true, null, TextValidators.NO_EMPTY_STRING_TEXT_VALIDATOR, null).setStackable(true);
        }
        if (this.settings.isAdvancedSizingSupported()) {
            ContextMenu advancedSizingMenu = new ContextMenu();
            this.rightClickMenu.addSubMenuEntry("advanced_sizing", Component.translatable("fancymenu.helper.editor.items.features.advanced_sizing"), advancedSizingMenu).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.helper.editor.items.features.advanced_sizing.desc"))).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("resize"));
            this.addGenericStringInputContextMenuEntryTo(advancedSizingMenu, "advanced_sizing_width", element -> element.settings.isAdvancedSizingSupported(), consumes -> consumes.element.advancedWidth, (element, input) -> {
                element.element.advancedWidth = input;
                element.element.baseWidth = 50;
            }, null, false, true, Component.translatable("fancymenu.helper.editor.items.features.advanced_sizing.width"), true, null, TextValidators.NO_EMPTY_STRING_TEXT_VALIDATOR, null).setStackable(true);
            this.addGenericStringInputContextMenuEntryTo(advancedSizingMenu, "advanced_sizing_height", element -> element.settings.isAdvancedSizingSupported(), consumes -> consumes.element.advancedHeight, (element, input) -> {
                element.element.advancedHeight = input;
                element.element.baseHeight = 50;
            }, null, false, true, Component.translatable("fancymenu.helper.editor.items.features.advanced_sizing.height"), true, null, TextValidators.NO_EMPTY_STRING_TEXT_VALIDATOR, null).setStackable(true);
        }
        this.rightClickMenu.addSeparatorEntry("separator_after_advanced_sizing_positioning").setStackable(true);
        if (this.settings.isStretchable()) {
            this.addToggleContextMenuEntryTo(this.rightClickMenu, "stretch_x", AbstractEditorElement.class, consumes -> consumes.element.stretchX, (element1, aBoolean) -> element1.element.stretchX = aBoolean, "fancymenu.editor.elements.stretch.x").setStackable(true).setIsActiveSupplier((menu, entry) -> this.element.advancedWidth == null).setIcon(ContextMenu.IconFactory.getIcon("arrow_horizontal"));
            this.addToggleContextMenuEntryTo(this.rightClickMenu, "stretch_y", AbstractEditorElement.class, consumes -> consumes.element.stretchY, (element1, aBoolean) -> element1.element.stretchY = aBoolean, "fancymenu.editor.elements.stretch.y").setStackable(true).setIsActiveSupplier((menu, entry) -> this.element.advancedHeight == null).setIcon(ContextMenu.IconFactory.getIcon("arrow_vertical"));
        }
        this.rightClickMenu.addSeparatorEntry("separator_after_stretch_xy").setStackable(true);
        if (this.settings.isLoadingRequirementsEnabled()) {
            this.rightClickMenu.addClickableEntry("loading_requirements", Component.translatable("fancymenu.editor.loading_requirement.elements.loading_requirements"), (menu, entry) -> {
                if (!entry.getStackMeta().isPartOfStack()) {
                    ManageRequirementsScreen s = new ManageRequirementsScreen(this.element.loadingRequirementContainer.copy(false), call -> {
                        if (call != null) {
                            this.editor.history.saveSnapshot();
                            this.element.loadingRequirementContainer = call;
                        }
                        Minecraft.getInstance().setScreen(this.editor);
                    });
                    Minecraft.getInstance().setScreen(s);
                } else if (entry.getStackMeta().isFirstInStack()) {
                    List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(element -> element.settings.isLoadingRequirementsEnabled());
                    List<LoadingRequirementContainer> containers = ObjectUtils.getOfAll(LoadingRequirementContainer.class, selectedElements, consumes -> consumes.element.loadingRequirementContainer);
                    LoadingRequirementContainer containerToUseInManager = new LoadingRequirementContainer();
                    boolean allEqual = ListUtils.allInListEqual(containers);
                    if (allEqual) {
                        containerToUseInManager = ((LoadingRequirementContainer) containers.get(0)).copy(true);
                    }
                    ManageRequirementsScreen s = new ManageRequirementsScreen(containerToUseInManager, call -> {
                        if (call != null) {
                            this.editor.history.saveSnapshot();
                            for (AbstractEditorElement e : selectedElements) {
                                e.element.loadingRequirementContainer = call.copy(true);
                            }
                        }
                        Minecraft.getInstance().setScreen(this.editor);
                    });
                    if (allEqual) {
                        Minecraft.getInstance().setScreen(s);
                    } else {
                        Minecraft.getInstance().setScreen(ConfirmationScreen.ofStrings(call -> {
                            if (call) {
                                Minecraft.getInstance().setScreen(s);
                            } else {
                                Minecraft.getInstance().setScreen(this.editor);
                            }
                        }, LocalizationUtils.splitLocalizedStringLines("fancymenu.elements.multiselect.loading_requirements.warning.override")));
                    }
                }
            }).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.editor.loading_requirement.elements.loading_requirements.desc"))).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("check_list"));
        }
        this.rightClickMenu.addSeparatorEntry("separator_5");
        if (this.settings.isOrderable()) {
            this.rightClickMenu.addClickableEntry("move_up_element", Component.translatable("fancymenu.editor.object.moveup"), (menu, entry) -> {
                this.editor.moveLayerUp(this);
                this.editor.layoutEditorWidgets.forEach(widget -> widget.editorElementOrderChanged(this, true));
            }).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.editor.object.moveup.desc"))).setIsActiveSupplier((menu, entry) -> this.editor.canMoveLayerUp(this)).setIcon(ContextMenu.IconFactory.getIcon("arrow_up"));
            this.rightClickMenu.addClickableEntry("move_down_element", Component.translatable("fancymenu.editor.object.movedown"), (menu, entry) -> {
                this.editor.moveLayerDown(this);
                this.editor.layoutEditorWidgets.forEach(widget -> widget.editorElementOrderChanged(this, false));
            }).setTooltipSupplier((menu, entry) -> Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.editor.object.movedown.desc"))).setIsActiveSupplier((menu, entry) -> this.editor.canMoveLayerDown(this)).setIcon(ContextMenu.IconFactory.getIcon("arrow_down"));
        }
        this.rightClickMenu.addSeparatorEntry("separator_6").setStackable(true);
        if (this.settings.isCopyable()) {
            this.rightClickMenu.addClickableEntry("copy_element", Component.translatable("fancymenu.editor.edit.copy"), (menu, entry) -> {
                if (!entry.getStackMeta().isPartOfStack()) {
                    this.editor.copyElementsToClipboard(this);
                } else {
                    this.editor.copyElementsToClipboard((AbstractEditorElement[]) this.editor.getSelectedElements().toArray(new AbstractEditorElement[0]));
                }
                menu.closeMenu();
            }).setStackable(true).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.copy")).setIcon(ContextMenu.IconFactory.getIcon("copy"));
        }
        if (this.settings.isDestroyable()) {
            this.rightClickMenu.addClickableEntry("delete_element", Component.translatable("fancymenu.editor.items.delete"), (menu, entry) -> {
                this.editor.history.saveSnapshot();
                for (AbstractEditorElement e : this.editor.getSelectedElements()) {
                    e.deleteElement();
                }
                menu.closeMenu();
            }).setStackable(true).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.delete")).setIcon(ContextMenu.IconFactory.getIcon("delete"));
        }
        this.rightClickMenu.addSeparatorEntry("separator_7").setStackable(true);
        if (this.settings.isDelayable()) {
            ContextMenu appearanceDelayMenu = new ContextMenu();
            this.rightClickMenu.addSubMenuEntry("appearance_delay", Component.translatable("fancymenu.element.general.appearance_delay"), appearanceDelayMenu).setStackable(true).setIcon(ContextMenu.IconFactory.getIcon("timer"));
            this.addGenericCycleContextMenuEntryTo(appearanceDelayMenu, "appearance_delay_type", ListUtils.of(AbstractElement.AppearanceDelay.NO_DELAY, AbstractElement.AppearanceDelay.FIRST_TIME, AbstractElement.AppearanceDelay.EVERY_TIME), consumes -> consumes.settings.isDelayable(), consumes -> consumes.element.appearanceDelay, (element, switcherValue) -> element.element.appearanceDelay = switcherValue, (menu, entry, switcherValue) -> Component.translatable("fancymenu.element.general.appearance_delay." + switcherValue.name)).setStackable(true);
            Supplier<Boolean> appearanceDelayIsActive = () -> {
                List<AbstractEditorElement> selected = this.editor.getSelectedElements();
                selected.removeIf(ex -> !ex.settings.isDelayable());
                if (selected.size() > 1) {
                    return true;
                } else {
                    for (AbstractEditorElement e : selected) {
                        if (e.element.appearanceDelay == AbstractElement.AppearanceDelay.NO_DELAY) {
                            return false;
                        }
                    }
                    return true;
                }
            };
            this.addGenericFloatInputContextMenuEntryTo(appearanceDelayMenu, "appearance_delay_seconds", element -> element.settings.isDelayable(), element -> element.element.appearanceDelayInSeconds, (element, input) -> element.element.appearanceDelayInSeconds = input, Component.translatable("fancymenu.element.general.appearance_delay.seconds"), true, 1.0F, null, null).setIsActiveSupplier((menu, entry) -> (Boolean) appearanceDelayIsActive.get()).setStackable(true);
            appearanceDelayMenu.addSeparatorEntry("separator_1").setStackable(true);
            this.addGenericBooleanSwitcherContextMenuEntryTo(appearanceDelayMenu, "appearance_delay_fade_in", consumes -> consumes.settings.isDelayable(), consumes -> consumes.element.fadeIn, (element, switcherValue) -> element.element.fadeIn = switcherValue, "fancymenu.element.general.appearance_delay.fade_in").setIsActiveSupplier((menu, entry) -> (Boolean) appearanceDelayIsActive.get()).setStackable(true);
            this.addGenericFloatInputContextMenuEntryTo(appearanceDelayMenu, "appearance_delay_fade_in_speed", element -> element.settings.isDelayable(), element -> element.element.fadeInSpeed, (element, input) -> element.element.fadeInSpeed = input, Component.translatable("fancymenu.element.general.appearance_delay.fade_in.speed"), true, 1.0F, null, null).setIsActiveSupplier((menu, entry) -> (Boolean) appearanceDelayIsActive.get()).setStackable(true);
        }
        this.rightClickMenu.addSeparatorEntry("separator_8").setStackable(true);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.tick();
        this.hovered = this.isMouseOver((double) mouseX, (double) mouseY);
        this.element.render(graphics, mouseX, mouseY, partial);
        this.renderDraggingNotAllowedOverlay(graphics);
        this.renderDeprecatedIndicator(graphics);
        AbstractEditorElement.ResizeGrabber hoveredGrabber = this.getHoveredResizeGrabber();
        if (hoveredGrabber != null) {
            CursorHandler.setClientTickCursor(hoveredGrabber.getCursor());
        }
        this.renderBorder(graphics, mouseX, mouseY, partial);
    }

    protected void tick() {
        if (this.element.advancedWidth != null || this.element.advancedHeight != null && !this.topLeftDisplay.hasLine("advanced_sizing_enabled")) {
            this.topLeftDisplay.addLine("advanced_sizing_enabled", () -> Component.translatable("fancymenu.elements.advanced_sizing.enabled_notification"));
        }
        if (this.element.advancedWidth == null && this.element.advancedHeight == null && this.topLeftDisplay.hasLine("advanced_sizing_enabled")) {
            this.topLeftDisplay.removeLine("advanced_sizing_enabled");
        }
        if (this.element.advancedX != null || this.element.advancedY != null && !this.topLeftDisplay.hasLine("advanced_positioning_enabled")) {
            this.topLeftDisplay.addLine("advanced_positioning_enabled", () -> Component.translatable("fancymenu.elements.advanced_positioning.enabled_notification"));
        }
        if (this.element.advancedX == null && this.element.advancedY == null && this.topLeftDisplay.hasLine("advanced_positioning_enabled")) {
            this.topLeftDisplay.removeLine("advanced_positioning_enabled");
        }
    }

    protected void renderDraggingNotAllowedOverlay(GuiGraphics graphics) {
        if (this.renderMovingNotAllowedTime >= System.currentTimeMillis()) {
            RenderSystem.enableBlend();
            graphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), UIBase.getUIColorTheme().layout_editor_element_dragging_not_allowed_color.getColorInt());
            AspectRatio ratio = new AspectRatio(32, 32);
            int[] size = ratio.getAspectRatioSizeByMaximumSize(this.getWidth(), this.getHeight());
            int texW = size[0];
            int texH = size[1];
            int texX = this.getX() + this.getWidth() / 2 - texW / 2;
            int texY = this.getY() + this.getHeight() / 2 - texH / 2;
            graphics.blit(DRAGGING_NOT_ALLOWED_TEXTURE, texX, texY, 0.0F, 0.0F, texW, texH, texW, texH);
        }
    }

    protected void renderDeprecatedIndicator(GuiGraphics graphics) {
        if (this.element.builder.isDeprecated()) {
            RenderSystem.enableBlend();
            AspectRatio ratio = new AspectRatio(32, 32);
            int[] size = ratio.getAspectRatioSizeByMaximumSize(this.getWidth() / 3, this.getHeight() / 3);
            int texW = size[0];
            int texH = size[1];
            int texX = this.getX() + this.getWidth() - texW;
            int texY = this.getY();
            UIBase.setShaderColor(graphics, UIBase.getUIColorTheme().warning_text_color);
            graphics.blit(DEPRECATED_WARNING_TEXTURE, texX, texY, 0.0F, 0.0F, texW, texH, texW, texH);
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderBorder(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.editor.getTopHoveredElement() == this && !this.editor.isUserNavigatingInRightClickMenu() && !this.editor.isUserNavigatingInElementMenu() || this.isSelected() || this.isMultiSelected()) {
            graphics.fill(this.getX() + 1, this.getY(), this.getX() + this.getWidth() - 1, this.getY() + 1, BORDER_COLOR.get(this));
            graphics.fill(this.getX() + 1, this.getY() + this.getHeight() - 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight(), BORDER_COLOR.get(this));
            graphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), BORDER_COLOR.get(this));
            graphics.fill(this.getX() + this.getWidth() - 1, this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), BORDER_COLOR.get(this));
            for (AbstractEditorElement.ResizeGrabber g : this.resizeGrabbers) {
                g.render(graphics, mouseX, mouseY, partial);
            }
        }
        if (this.isSelected()) {
            this.topLeftDisplay.render(graphics, mouseX, mouseY, partial);
            this.bottomRightDisplay.render(graphics, mouseX, mouseY, partial);
        }
    }

    public void setAnchorPoint(ElementAnchorPoint p, boolean keepAbsolutePosition, int oldAbsoluteX, int oldAbsoluteY, boolean resetElementStates) {
        if (resetElementStates) {
            this.resetElementStates();
        }
        if (p == null) {
            p = ElementAnchorPoints.MID_CENTERED;
        }
        if (p != ElementAnchorPoints.ELEMENT) {
            this.element.anchorPointElementIdentifier = null;
            this.element.setElementAnchorPointParent(null);
        }
        if (keepAbsolutePosition) {
            this.element.posOffsetX = this.calcNewBaseX(p, oldAbsoluteX);
            this.element.posOffsetY = this.calcNewBaseY(p, oldAbsoluteY);
        } else {
            this.element.posOffsetX = p.getDefaultElementBaseX(this.element);
            this.element.posOffsetY = p.getDefaultElementBaseY(this.element);
        }
        this.element.anchorPoint = p;
    }

    public void setAnchorPointViaOverlay(AnchorPointOverlay.AnchorPointArea anchor, int mouseX, int mouseY) {
        int oldAbsoluteX = this.getX();
        int oldAbsoluteY = this.getY();
        if (this.settings.isAnchorPointChangeable()) {
            if (anchor.anchorPoint != ElementAnchorPoints.ELEMENT || this.settings.isElementAnchorPointAllowed()) {
                if (anchor instanceof AnchorPointOverlay.ElementAnchorPointArea ea) {
                    this.element.anchorPointElementIdentifier = ea.elementIdentifier;
                    AbstractEditorElement ee = ea.getElement();
                    if (ee != null) {
                        this.element.setElementAnchorPointParent(ee.element);
                    } else {
                        this.element.setElementAnchorPointParent(null);
                        LOGGER.error("[FANCYMENU] Failed to get parent element for ELEMENT anchor! Element was NULL!", new NullPointerException());
                    }
                }
                this.setAnchorPoint(anchor.anchorPoint, true, oldAbsoluteX, oldAbsoluteY, false);
                this.updateLeftMouseDownCachedValues(mouseX, mouseY);
                this.updateMovingStartPos(mouseX, mouseY);
            }
        }
    }

    protected int calcNewBaseX(ElementAnchorPoint newAnchor, int oldAbsoluteX) {
        int originX = newAnchor.getOriginX(this.element);
        return oldAbsoluteX - originX;
    }

    protected int calcNewBaseY(ElementAnchorPoint newAnchor, int oldAbsoluteY) {
        int originY = newAnchor.getOriginY(this.element);
        return oldAbsoluteY - originY;
    }

    public void resetElementStates() {
        this.selected = false;
        this.multiSelected = false;
        this.leftMouseDown = false;
        this.activeResizeGrabber = null;
        this.rightClickMenu.closeMenu();
    }

    public void onSettingsChanged() {
        this.resetElementStates();
        this.init();
    }

    public void updateLeftMouseDownCachedValues(int mouseX, int mouseY) {
        this.leftMouseDownMouseX = (double) mouseX;
        this.leftMouseDownMouseY = (double) mouseY;
        this.leftMouseDownBaseX = this.element.posOffsetX;
        this.leftMouseDownBaseY = this.element.posOffsetY;
        this.leftMouseDownBaseWidth = this.element.baseWidth;
        this.leftMouseDownBaseHeight = this.element.baseHeight;
    }

    public void updateMovingStartPos(int mouseX, int mouseY) {
        this.movingStartPosX = mouseX;
        this.movingStartPosY = mouseY;
    }

    public void updateResizingStartPos(int mouseX, int mouseY) {
        this.resizingStartPosX = mouseX;
        this.resizingStartPosY = mouseY;
    }

    public boolean isElementAnchorAndParentIsSelected() {
        if (this.element.anchorPoint != ElementAnchorPoints.ELEMENT) {
            return false;
        } else if (this.element.anchorPointElementIdentifier == null) {
            return false;
        } else {
            AbstractEditorElement parent = this.editor.getElementByInstanceIdentifier(this.element.anchorPointElementIdentifier);
            return parent == null ? false : parent.isSelected() || parent.isMultiSelected();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isSelected()) {
            return false;
        } else {
            if (button == 0 && !this.rightClickMenu.isUserNavigatingInMenu()) {
                this.activeResizeGrabber = !this.isMultiSelected() ? this.getHoveredResizeGrabber() : null;
                if (this.isHovered() || this.isMultiSelected() && !this.editor.getHoveredElements().isEmpty() || this.isGettingResized()) {
                    this.leftMouseDown = true;
                    this.updateLeftMouseDownCachedValues((int) mouseX, (int) mouseY);
                    this.resizeAspectRatio = new AspectRatio(this.getWidth(), this.getHeight());
                }
            }
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.leftMouseDown = false;
            this.activeResizeGrabber = null;
            this.recentlyMovedByDragging = false;
            this.movingCrumpleZonePassed = false;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double $$3, double $$4) {
        if (!this.isSelected()) {
            return false;
        } else {
            if (button == 0) {
                if (this.leftMouseDown && !this.isGettingResized() && this.movingCrumpleZonePassed) {
                    int diffX = (int) (-((double) this.movingStartPosX - mouseX));
                    int diffY = (int) (-((double) this.movingStartPosY - mouseY));
                    if (!this.editor.allSelectedElementsMovable()) {
                        if (!this.settings.isMovable()) {
                            this.renderMovingNotAllowedTime = System.currentTimeMillis() + 800L;
                        }
                    } else {
                        if (!this.isMultiSelected() || !this.isElementAnchorAndParentIsSelected()) {
                            this.element.posOffsetX = this.leftMouseDownBaseX + diffX;
                            this.element.posOffsetY = this.leftMouseDownBaseY + diffY;
                        }
                        if (diffX > 0 || diffY > 0) {
                            this.recentlyMovedByDragging = true;
                        }
                    }
                }
                if (this.leftMouseDown && this.isGettingResized()) {
                    int diffX = (int) (-((double) this.resizingStartPosX - mouseX));
                    int diffY = (int) (-((double) this.resizingStartPosY - mouseY));
                    if (this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.LEFT || this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.RIGHT) {
                        int i = this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.LEFT ? this.leftMouseDownBaseWidth - diffX : this.leftMouseDownBaseWidth + diffX;
                        if (i >= 2) {
                            this.element.baseWidth = i;
                            this.element.posOffsetX = this.leftMouseDownBaseX + this.element.anchorPoint.getResizePositionOffsetX(this.element, diffX, this.activeResizeGrabber.type);
                            if (Screen.hasShiftDown()) {
                                this.element.baseHeight = this.resizeAspectRatio.getAspectRatioHeight(this.element.baseWidth);
                            }
                        }
                    }
                    if (this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.TOP || this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.BOTTOM) {
                        int i = this.activeResizeGrabber.type == AbstractEditorElement.ResizeGrabberType.TOP ? this.leftMouseDownBaseHeight - diffY : this.leftMouseDownBaseHeight + diffY;
                        if (i >= 2) {
                            this.element.baseHeight = i;
                            this.element.posOffsetY = this.leftMouseDownBaseY + this.element.anchorPoint.getResizePositionOffsetY(this.element, diffY, this.activeResizeGrabber.type);
                            if (Screen.hasShiftDown()) {
                                this.element.baseWidth = this.resizeAspectRatio.getAspectRatioWidth(this.element.baseHeight);
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return UIBase.isXYInArea((int) mouseX, (int) mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (!this.selected) {
            this.resetElementStates();
        }
        this.editor.onUpdateSelectedElements();
    }

    public boolean isMultiSelected() {
        return this.multiSelected;
    }

    public void setMultiSelected(boolean multiSelected) {
        this.multiSelected = multiSelected;
    }

    public boolean isHovered() {
        return this.hovered || this.rightClickMenu.isUserNavigatingInMenu() || this.getHoveredResizeGrabber() != null;
    }

    public int getX() {
        return this.element.getAbsoluteX();
    }

    public int getY() {
        return this.element.getAbsoluteY();
    }

    public int getWidth() {
        return this.element.getAbsoluteWidth();
    }

    public int getHeight() {
        return this.element.getAbsoluteHeight();
    }

    public boolean deleteElement() {
        return this.editor.deleteElement(this);
    }

    public boolean isGettingResized() {
        return !this.settings.isResizeable() ? false : this.activeResizeGrabber != null;
    }

    public boolean isDragged() {
        return this.recentlyMovedByDragging;
    }

    public boolean isPressed() {
        return this.leftMouseDown;
    }

    @Nullable
    public AbstractEditorElement.ResizeGrabber getHoveredResizeGrabber() {
        if (!this.settings.isResizeable()) {
            return null;
        } else if (this.activeResizeGrabber != null) {
            return this.activeResizeGrabber;
        } else {
            for (AbstractEditorElement.ResizeGrabber g : this.resizeGrabbers) {
                if (g.hovered) {
                    return g;
                }
            }
            return null;
        }
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addImageResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, ResourceSupplier<ITexture> defaultValue, @NotNull ConsumingSupplier<E, ResourceSupplier<ITexture>> targetFieldGetter, @NotNull BiConsumer<E, ResourceSupplier<ITexture>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        return this.addGenericResourceChooserContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), () -> ResourceChooserScreen.image(null, file -> {
        }), ResourceSupplier::image, defaultValue, targetFieldGetter, targetFieldSetter, label, addResetOption, FileTypeGroups.IMAGE_TYPES, fileFilter, allowLocation, allowLocal, allowWeb);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addAudioResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, ResourceSupplier<IAudio> defaultValue, @NotNull ConsumingSupplier<E, ResourceSupplier<IAudio>> targetFieldGetter, @NotNull BiConsumer<E, ResourceSupplier<IAudio>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        return this.addGenericResourceChooserContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), () -> ResourceChooserScreen.audio(null, file -> {
        }), ResourceSupplier::audio, defaultValue, targetFieldGetter, targetFieldSetter, label, addResetOption, FileTypeGroups.AUDIO_TYPES, fileFilter, allowLocation, allowLocal, allowWeb);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addVideoResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, ResourceSupplier<IVideo> defaultValue, @NotNull ConsumingSupplier<E, ResourceSupplier<IVideo>> targetFieldGetter, @NotNull BiConsumer<E, ResourceSupplier<IVideo>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        return this.addGenericResourceChooserContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), () -> ResourceChooserScreen.video(null, file -> {
        }), ResourceSupplier::video, defaultValue, targetFieldGetter, targetFieldSetter, label, addResetOption, FileTypeGroups.VIDEO_TYPES, fileFilter, allowLocation, allowLocal, allowWeb);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addTextResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, ResourceSupplier<IText> defaultValue, @NotNull ConsumingSupplier<E, ResourceSupplier<IText>> targetFieldGetter, @NotNull BiConsumer<E, ResourceSupplier<IText>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        return this.addGenericResourceChooserContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), () -> ResourceChooserScreen.text(null, file -> {
        }), ResourceSupplier::text, defaultValue, targetFieldGetter, targetFieldSetter, label, addResetOption, FileTypeGroups.TEXT_TYPES, fileFilter, allowLocation, allowLocal, allowWeb);
    }

    protected <R extends Resource, F extends FileType<R>, E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addGenericResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull Supplier<ResourceChooserScreen<R, F>> resourceChooserScreenBuilder, @NotNull ConsumingSupplier<String, ResourceSupplier<R>> resourceSupplierBuilder, ResourceSupplier<R> defaultValue, @NotNull ConsumingSupplier<E, ResourceSupplier<R>> targetFieldGetter, @NotNull BiConsumer<E, ResourceSupplier<R>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileTypeGroup<F> fileTypes, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        return this.addGenericResourceChooserContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), resourceChooserScreenBuilder, resourceSupplierBuilder, defaultValue, targetFieldGetter, targetFieldSetter, label, addResetOption, fileTypes, fileFilter, allowLocation, allowLocal, allowWeb);
    }

    protected <R extends Resource, F extends FileType<R>> ContextMenu.ClickableContextMenuEntry<?> addGenericResourceChooserContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull Supplier<ResourceChooserScreen<R, F>> resourceChooserScreenBuilder, @NotNull ConsumingSupplier<String, ResourceSupplier<R>> resourceSupplierBuilder, ResourceSupplier<R> defaultValue, @NotNull ConsumingSupplier<AbstractEditorElement, ResourceSupplier<R>> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, ResourceSupplier<R>> targetFieldSetter, @NotNull Component label, boolean addResetOption, @Nullable FileTypeGroup<F> fileTypes, @Nullable FileFilter fileFilter, boolean allowLocation, boolean allowLocal, boolean allowWeb) {
        ContextMenu subMenu = new ContextMenu();
        subMenu.addClickableEntry("choose_file", Component.translatable("fancymenu.ui.resources.choose"), (menu, entry) -> {
            List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
            if (entry.getStackMeta().isFirstInStack() && !selectedElements.isEmpty()) {
                String preSelectedSource = null;
                List<String> allPaths = ObjectUtils.getOfAll(String.class, selectedElements, consumes -> {
                    ResourceSupplier<R> supplier = targetFieldGetter.get(consumes);
                    return supplier != null ? supplier.getSourceWithPrefix() : null;
                });
                if (!allPaths.isEmpty() && ListUtils.allInListEqual(allPaths)) {
                    preSelectedSource = (String) allPaths.get(0);
                }
                ResourceChooserScreen<R, F> chooserScreen = (ResourceChooserScreen<R, F>) resourceChooserScreenBuilder.get();
                chooserScreen.setFileFilter(fileFilter);
                chooserScreen.setAllowedFileTypes(fileTypes);
                chooserScreen.setSource(preSelectedSource, false);
                chooserScreen.setLocationSourceAllowed(allowLocation);
                chooserScreen.setLocalSourceAllowed(allowLocal);
                chooserScreen.setWebSourceAllowed(allowWeb);
                chooserScreen.setResourceSourceCallback(source -> {
                    if (source != null) {
                        this.editor.history.saveSnapshot();
                        for (AbstractEditorElement e : selectedElements) {
                            targetFieldSetter.accept(e, resourceSupplierBuilder.get(source));
                        }
                    }
                    Minecraft.getInstance().setScreen(this.editor);
                });
                Minecraft.getInstance().setScreen(chooserScreen);
            }
        }).setStackable(true);
        if (addResetOption) {
            subMenu.addClickableEntry("reset_to_default", Component.translatable("fancymenu.ui.resources.reset"), (menu, entry) -> {
                if (entry.getStackMeta().isFirstInStack()) {
                    List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
                    this.editor.history.saveSnapshot();
                    for (AbstractEditorElement e : selectedElements) {
                        targetFieldSetter.accept(e, defaultValue);
                    }
                }
            }).setStackable(true);
        }
        Supplier<Component> currentValueDisplayLabelSupplier = () -> {
            List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
            if (selectedElements.size() == 1) {
                ResourceSupplier<R> supplier = targetFieldGetter.get((AbstractEditorElement) selectedElements.get(0));
                String val = supplier != null ? supplier.getSourceWithoutPrefix() : null;
                Component valueComponent;
                if (val == null) {
                    valueComponent = Component.literal("---").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt()));
                } else {
                    val = GameDirectoryUtils.getPathWithoutGameDirectory(val);
                    if (Minecraft.getInstance().font.width(val) > 150) {
                        val = new StringBuilder(val).reverse().toString();
                        val = Minecraft.getInstance().font.plainSubstrByWidth(val, 150);
                        val = new StringBuilder(val).reverse().toString();
                        val = ".." + val;
                    }
                    valueComponent = Component.literal(val).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().success_text_color.getColorInt()));
                }
                return Component.translatable("fancymenu.ui.resources.current", valueComponent);
            } else {
                return Component.empty();
            }
        };
        subMenu.addSeparatorEntry("separator_before_current_value_display").setIsVisibleSupplier((menu, entry) -> this.getFilteredSelectedElementList(selectedElementsFilter).size() == 1);
        subMenu.addClickableEntry("current_value_display", Component.empty(), (menu, entry) -> {
        }).setLabelSupplier((menu, entry) -> (Component) currentValueDisplayLabelSupplier.get()).setClickSoundEnabled(false).setChangeBackgroundColorOnHover(false).setIsVisibleSupplier((menu, entry) -> this.getFilteredSelectedElementList(selectedElementsFilter).size() == 1).setIcon(ContextMenu.IconFactory.getIcon("info"));
        return addTo.addSubMenuEntry(entryIdentifier, label, subMenu).setStackable(true);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, String> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, String> targetFieldSetter, @Nullable CharacterFilter inputCharacterFilter, boolean multiLineInput, boolean allowPlaceholders, @NotNull Component label, boolean addResetOption, String defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        ContextMenu subMenu = new ContextMenu();
        ContextMenu.ClickableContextMenuEntry<?> inputEntry = subMenu.addClickableEntry("input_value", Component.translatable("fancymenu.guicomponents.set"), (menu, entry) -> {
            if (entry.getStackMeta().isFirstInStack()) {
                List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
                String defaultText = null;
                List<String> targetValuesOfSelected = new ArrayList();
                for (AbstractEditorElement e : selectedElements) {
                    targetValuesOfSelected.add(targetFieldGetter.get(e));
                }
                if (!entry.getStackMeta().isPartOfStack() || ListUtils.allInListEqual(targetValuesOfSelected)) {
                    defaultText = targetFieldGetter.get(this);
                }
                Screen inputScreen;
                if (!multiLineInput && !allowPlaceholders) {
                    TextInputScreen s = TextInputScreen.build(label, inputCharacterFilter, call -> {
                        if (call != null) {
                            this.editor.history.saveSnapshot();
                            for (AbstractEditorElement e : selectedElements) {
                                targetFieldSetter.accept(e, call);
                            }
                        }
                        menu.closeMenu();
                        Minecraft.getInstance().setScreen(this.editor);
                    });
                    if (textValidator != null) {
                        s.setTextValidator(consumes -> {
                            if (textValidatorUserFeedback != null) {
                                consumes.setTextValidatorUserFeedback(textValidatorUserFeedback.get(consumes.getText()));
                            }
                            return textValidator.get(consumes.getText());
                        });
                    }
                    s.setText(defaultText);
                    inputScreen = s;
                } else {
                    TextEditorScreen s = new TextEditorScreen(label, inputCharacterFilter != null ? inputCharacterFilter.convertToLegacyFilter() : null, call -> {
                        if (call != null) {
                            this.editor.history.saveSnapshot();
                            for (AbstractEditorElement e : selectedElements) {
                                targetFieldSetter.accept(e, call);
                            }
                        }
                        menu.closeMenu();
                        Minecraft.getInstance().setScreen(this.editor);
                    });
                    if (textValidator != null) {
                        s.setTextValidator(consumes -> {
                            if (textValidatorUserFeedback != null) {
                                consumes.setTextValidatorUserFeedback(textValidatorUserFeedback.get(consumes.getText()));
                            }
                            return textValidator.get(consumes.getText());
                        });
                    }
                    s.setText(defaultText);
                    s.setMultilineMode(multiLineInput);
                    s.setPlaceholdersAllowed(allowPlaceholders);
                    inputScreen = s;
                }
                Minecraft.getInstance().setScreen(inputScreen);
            }
        }).setStackable(true);
        if (addResetOption) {
            subMenu.addClickableEntry("reset_to_default", Component.translatable("fancymenu.guicomponents.reset"), (menu, entry) -> {
                if (entry.getStackMeta().isFirstInStack()) {
                    List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
                    this.editor.history.saveSnapshot();
                    for (AbstractEditorElement e : selectedElements) {
                        targetFieldSetter.accept(e, defaultValue);
                    }
                }
            }).setStackable(true);
        }
        Supplier<Component> currentValueDisplayLabelSupplier = () -> {
            List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
            if (selectedElements.size() == 1) {
                String val = targetFieldGetter.get((AbstractEditorElement) selectedElements.get(0));
                Component valueComponent;
                if (val == null) {
                    valueComponent = Component.literal("---").setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt()));
                } else {
                    val = GameDirectoryUtils.getPathWithoutGameDirectory(val);
                    if (Minecraft.getInstance().font.width(val) > 150) {
                        val = new StringBuilder(val).reverse().toString();
                        val = Minecraft.getInstance().font.plainSubstrByWidth(val, 150);
                        val = new StringBuilder(val).reverse().toString();
                        val = ".." + val;
                    }
                    valueComponent = Component.literal(val).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().success_text_color.getColorInt()));
                }
                return Component.translatable("fancymenu.context_menu.entries.choose_or_set.current", valueComponent);
            } else {
                return Component.empty();
            }
        };
        subMenu.addSeparatorEntry("separator_before_current_value_display").setIsVisibleSupplier((menu, entry) -> this.getFilteredSelectedElementList(selectedElementsFilter).size() == 1);
        subMenu.addClickableEntry("current_value_display", Component.empty(), (menu, entry) -> {
        }).setLabelSupplier((menu, entry) -> (Component) currentValueDisplayLabelSupplier.get()).setClickSoundEnabled(false).setChangeBackgroundColorOnHover(false).setIsVisibleSupplier((menu, entry) -> this.getFilteredSelectedElementList(selectedElementsFilter).size() == 1).setIcon(ContextMenu.IconFactory.getIcon("info"));
        return addTo.addSubMenuEntry(entryIdentifier, label, subMenu).setStackable(true);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, String> targetFieldGetter, @NotNull BiConsumer<E, String> targetFieldSetter, @Nullable CharacterFilter inputCharacterFilter, boolean multiLineInput, boolean allowPlaceholders, @NotNull Component label, boolean addResetOption, String defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, (ConsumingSupplier<AbstractEditorElement, Boolean>) (consumes -> elementType.isAssignableFrom(consumes.getClass())), targetFieldGetter, targetFieldSetter, inputCharacterFilter, multiLineInput, allowPlaceholders, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addGenericStringInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, String> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, String> targetFieldSetter, @Nullable CharacterFilter inputCharacterFilter, boolean multiLineInput, boolean allowPlaceholders, @NotNull Component label, boolean addResetOption, String defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, selectedElementsFilter, targetFieldGetter, targetFieldSetter, inputCharacterFilter, multiLineInput, allowPlaceholders, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addStringInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, String> targetFieldGetter, @NotNull BiConsumer<E, String> targetFieldSetter, @Nullable CharacterFilter inputCharacterFilter, boolean multiLineInput, boolean allowPlaceholders, @NotNull Component label, boolean addResetOption, String defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addGenericStringInputContextMenuEntryTo(addTo, entryIdentifier, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, inputCharacterFilter, multiLineInput, allowPlaceholders, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addGenericIntegerInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, Integer> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, Integer> targetFieldSetter, @NotNull Component label, boolean addResetOption, int defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        ConsumingSupplier<String, Boolean> defaultIntegerValidator = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && MathUtils.isInteger(consumes);
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, selectedElementsFilter, consumes -> {
            Integer i = targetFieldGetter.get(consumes);
            if (i == null) {
                i = 0;
            }
            return i + "";
        }, (e, s) -> {
            if (MathUtils.isInteger(s)) {
                targetFieldSetter.accept(e, Integer.valueOf(s));
            }
        }, CharacterFilter.buildIntegerFiler(), false, false, label, addResetOption, defaultValue + "", textValidator != null ? textValidator : defaultIntegerValidator, textValidatorUserFeedback);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addIntegerInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, Integer> targetFieldGetter, @NotNull BiConsumer<E, Integer> targetFieldSetter, @NotNull Component label, boolean addResetOption, int defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addGenericIntegerInputContextMenuEntryTo(addTo, entryIdentifier, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addGenericLongInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, Long> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, Long> targetFieldSetter, @NotNull Component label, boolean addResetOption, long defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        ConsumingSupplier<String, Boolean> defaultLongValidator = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && MathUtils.isLong(consumes);
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, selectedElementsFilter, consumes -> {
            Long l = targetFieldGetter.get(consumes);
            if (l == null) {
                l = 0L;
            }
            return l + "";
        }, (e, s) -> {
            if (MathUtils.isLong(s)) {
                targetFieldSetter.accept(e, Long.valueOf(s));
            }
        }, CharacterFilter.buildIntegerFiler(), false, false, label, addResetOption, defaultValue + "", textValidator != null ? textValidator : defaultLongValidator, textValidatorUserFeedback);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addLongInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, Long> targetFieldGetter, @NotNull BiConsumer<E, Long> targetFieldSetter, @NotNull Component label, boolean addResetOption, long defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addGenericLongInputContextMenuEntryTo(addTo, entryIdentifier, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addGenericDoubleInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, Double> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, Double> targetFieldSetter, @NotNull Component label, boolean addResetOption, double defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        ConsumingSupplier<String, Boolean> defaultDoubleValidator = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && MathUtils.isDouble(consumes);
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, selectedElementsFilter, consumes -> {
            Double d = targetFieldGetter.get(consumes);
            if (d == null) {
                d = 0.0;
            }
            return d + "";
        }, (e, s) -> {
            if (MathUtils.isDouble(s)) {
                targetFieldSetter.accept(e, Double.valueOf(s));
            }
        }, CharacterFilter.buildDecimalFiler(), false, false, label, addResetOption, defaultValue + "", textValidator != null ? textValidator : defaultDoubleValidator, textValidatorUserFeedback);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addDoubleInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, Double> targetFieldGetter, @NotNull BiConsumer<E, Double> targetFieldSetter, @NotNull Component label, boolean addResetOption, double defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addGenericDoubleInputContextMenuEntryTo(addTo, entryIdentifier, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected ContextMenu.ClickableContextMenuEntry<?> addGenericFloatInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, Float> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, Float> targetFieldSetter, @NotNull Component label, boolean addResetOption, float defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        ConsumingSupplier<String, Boolean> defaultFloatValidator = consumes -> consumes != null && !consumes.replace(" ", "").isEmpty() && MathUtils.isFloat(consumes);
        return this.addInputContextMenuEntryTo(addTo, entryIdentifier, selectedElementsFilter, consumes -> {
            Float f = targetFieldGetter.get(consumes);
            if (f == null) {
                f = 0.0F;
            }
            return f + "";
        }, (e, s) -> {
            if (MathUtils.isFloat(s)) {
                targetFieldSetter.accept(e, Float.valueOf(s));
            }
        }, CharacterFilter.buildDecimalFiler(), false, false, label, addResetOption, defaultValue + "", textValidator != null ? textValidator : defaultFloatValidator, textValidatorUserFeedback);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addFloatInputContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, Float> targetFieldGetter, @NotNull BiConsumer<E, Float> targetFieldSetter, @NotNull Component label, boolean addResetOption, float defaultValue, @Nullable ConsumingSupplier<String, Boolean> textValidator, @Nullable ConsumingSupplier<String, Tooltip> textValidatorUserFeedback) {
        return this.addGenericFloatInputContextMenuEntryTo(addTo, entryIdentifier, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, label, addResetOption, defaultValue, textValidator, textValidatorUserFeedback);
    }

    protected <V> ContextMenu.ClickableContextMenuEntry<?> addGenericCycleContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, List<V> switcherValues, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, V> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, V> targetFieldSetter, @NotNull AbstractEditorElement.SwitcherContextMenuEntryLabelSupplier<V> labelSupplier) {
        return addTo.addClickableEntry(entryIdentifier, Component.literal(""), (menu, entry) -> {
            List<AbstractEditorElement> selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
            ValueCycle<V> cycle = this.setupValueCycle("switcher", ValueCycle.fromList(switcherValues), selectedElements, entry.getStackMeta(), targetFieldGetter);
            this.editor.history.saveSnapshot();
            if (!selectedElements.isEmpty() && entry.getStackMeta().isFirstInStack()) {
                V next = cycle.next();
                for (AbstractEditorElement e : selectedElements) {
                    targetFieldSetter.accept(e, next);
                }
            }
        }).setLabelSupplier((menu, entry) -> {
            List<AbstractEditorElement> selectedElements = new ArrayList();
            if (!entry.getStackMeta().getProperties().hasProperty("switcher")) {
                selectedElements = this.getFilteredSelectedElementList(selectedElementsFilter);
            }
            ValueCycle<V> switcher = this.setupValueCycle("switcher", ValueCycle.fromList(switcherValues), selectedElements, entry.getStackMeta(), targetFieldGetter);
            return labelSupplier.get(menu, (ContextMenu.ClickableContextMenuEntry<?>) entry, switcher.current());
        }).setStackable(true);
    }

    protected <V, E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addCycleContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, List<V> switcherValues, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, V> targetFieldGetter, @NotNull BiConsumer<E, V> targetFieldSetter, @NotNull AbstractEditorElement.SwitcherContextMenuEntryLabelSupplier<V> labelSupplier) {
        return this.addGenericCycleContextMenuEntryTo(addTo, entryIdentifier, switcherValues, consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, labelSupplier);
    }

    protected <E extends AbstractEditorElement> ContextMenu.ClickableContextMenuEntry<?> addToggleContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @NotNull Class<E> elementType, @NotNull ConsumingSupplier<E, Boolean> targetFieldGetter, @NotNull BiConsumer<E, Boolean> targetFieldSetter, @NotNull String labelLocalizationKeyBase) {
        return this.addGenericCycleContextMenuEntryTo(addTo, entryIdentifier, ListUtils.of(false, true), consumes -> elementType.isAssignableFrom(consumes.getClass()), targetFieldGetter, targetFieldSetter, (menu, entry, switcherValue) -> {
            if (switcherValue && entry.isActive()) {
                MutableComponent enabled = Component.translatable("fancymenu.general.cycle.enabled_disabled.enabled").withStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().success_text_color.getColorInt()));
                return Component.translatable(labelLocalizationKeyBase, enabled);
            } else {
                MutableComponent disabled = Component.translatable("fancymenu.general.cycle.enabled_disabled.disabled").withStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().error_text_color.getColorInt()));
                return Component.translatable(labelLocalizationKeyBase, disabled);
            }
        }).setStackable(true);
    }

    @Deprecated
    protected ContextMenu.ClickableContextMenuEntry<?> addGenericBooleanSwitcherContextMenuEntryTo(@NotNull ContextMenu addTo, @NotNull String entryIdentifier, @Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter, @NotNull ConsumingSupplier<AbstractEditorElement, Boolean> targetFieldGetter, @NotNull BiConsumer<AbstractEditorElement, Boolean> targetFieldSetter, @NotNull String labelLocalizationKeyBase) {
        return this.addGenericCycleContextMenuEntryTo(addTo, entryIdentifier, ListUtils.of(false, true), selectedElementsFilter, targetFieldGetter, targetFieldSetter, (menu, entry, switcherValue) -> switcherValue && entry.isActive() ? Component.translatable(labelLocalizationKeyBase + ".on") : Component.translatable(labelLocalizationKeyBase + ".off")).setStackable(true);
    }

    protected <T, E extends AbstractEditorElement> ValueCycle<T> setupValueCycle(String toggleIdentifier, ValueCycle<T> cycle, List<E> elements, ContextMenu.ContextMenuStackMeta stackMeta, ConsumingSupplier<E, T> defaultValue) {
        boolean hasProperty = stackMeta.getProperties().hasProperty(toggleIdentifier);
        ValueCycle<T> t = stackMeta.getProperties().putPropertyIfAbsentAndGet(toggleIdentifier, cycle);
        if (!elements.isEmpty()) {
            E firstElement = (E) elements.get(0);
            if (!stackMeta.isPartOfStack()) {
                t.setCurrentValue(defaultValue.get(firstElement));
            } else if (!hasProperty && ListUtils.allInListEqual(ObjectUtils.getOfAllUnsafe(elements, defaultValue))) {
                t.setCurrentValue(defaultValue.get(firstElement));
            }
        }
        return t;
    }

    protected List<AbstractEditorElement> getFilteredSelectedElementList(@Nullable ConsumingSupplier<AbstractEditorElement, Boolean> selectedElementsFilter) {
        return ListUtils.filterList(this.editor.getSelectedElements(), consumes -> selectedElementsFilter == null ? true : selectedElementsFilter.get(consumes));
    }

    public class ResizeGrabber implements Renderable {

        protected int width = 4;

        protected int height = 4;

        protected final AbstractEditorElement.ResizeGrabberType type;

        protected boolean hovered = false;

        protected ResizeGrabber(AbstractEditorElement.ResizeGrabberType type) {
            this.type = type;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.hovered = AbstractEditorElement.this.isSelected() && this.isGrabberEnabled() && this.isMouseOver((double) mouseX, (double) mouseY);
            if (AbstractEditorElement.this.isSelected() && this.isGrabberEnabled()) {
                graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, AbstractEditorElement.BORDER_COLOR.get(AbstractEditorElement.this));
            }
        }

        protected int getX() {
            int x = AbstractEditorElement.this.getX();
            if (this.type == AbstractEditorElement.ResizeGrabberType.TOP || this.type == AbstractEditorElement.ResizeGrabberType.BOTTOM) {
                x += AbstractEditorElement.this.getWidth() / 2 - this.width / 2;
            }
            if (this.type == AbstractEditorElement.ResizeGrabberType.RIGHT) {
                x += AbstractEditorElement.this.getWidth() - this.width / 2;
            }
            if (this.type == AbstractEditorElement.ResizeGrabberType.LEFT) {
                x -= this.width / 2;
            }
            return x;
        }

        protected int getY() {
            int y = AbstractEditorElement.this.getY();
            if (this.type == AbstractEditorElement.ResizeGrabberType.TOP) {
                y -= this.height / 2;
            }
            if (this.type == AbstractEditorElement.ResizeGrabberType.RIGHT || this.type == AbstractEditorElement.ResizeGrabberType.LEFT) {
                y += AbstractEditorElement.this.getHeight() / 2 - this.height / 2;
            }
            if (this.type == AbstractEditorElement.ResizeGrabberType.BOTTOM) {
                y += AbstractEditorElement.this.getHeight() - this.height / 2;
            }
            return y;
        }

        protected long getCursor() {
            return this.type != AbstractEditorElement.ResizeGrabberType.TOP && this.type != AbstractEditorElement.ResizeGrabberType.BOTTOM ? CursorHandler.CURSOR_RESIZE_HORIZONTAL : CursorHandler.CURSOR_RESIZE_VERTICAL;
        }

        protected boolean isGrabberEnabled() {
            if (AbstractEditorElement.this.isMultiSelected()) {
                return false;
            } else if (this.type != AbstractEditorElement.ResizeGrabberType.TOP && this.type != AbstractEditorElement.ResizeGrabberType.BOTTOM) {
                if (this.type != AbstractEditorElement.ResizeGrabberType.LEFT && this.type != AbstractEditorElement.ResizeGrabberType.RIGHT) {
                    return false;
                } else {
                    return this.type == AbstractEditorElement.ResizeGrabberType.LEFT && AbstractEditorElement.this.element.advancedX != null ? false : AbstractEditorElement.this.settings.isResizeable() && AbstractEditorElement.this.settings.isResizeableX() && AbstractEditorElement.this.element.advancedWidth == null;
                }
            } else {
                return this.type == AbstractEditorElement.ResizeGrabberType.TOP && AbstractEditorElement.this.element.advancedY != null ? false : AbstractEditorElement.this.settings.isResizeable() && AbstractEditorElement.this.settings.isResizeableY() && AbstractEditorElement.this.element.advancedHeight == null;
            }
        }

        protected boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= (double) this.getX() && mouseX <= (double) (this.getX() + this.width) && mouseY >= (double) this.getY() && mouseY <= (double) (this.getY() + this.height);
        }
    }

    public static enum ResizeGrabberType {

        TOP, RIGHT, BOTTOM, LEFT
    }

    @FunctionalInterface
    protected interface SwitcherContextMenuEntryLabelSupplier<V> {

        Component get(ContextMenu var1, ContextMenu.ClickableContextMenuEntry<?> var2, V var3);
    }
}