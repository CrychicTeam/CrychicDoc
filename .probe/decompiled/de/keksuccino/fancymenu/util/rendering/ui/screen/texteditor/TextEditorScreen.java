package de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderRegistry;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinEditBox;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.contextmenu.v2.ContextMenu;
import de.keksuccino.fancymenu.util.rendering.ui.screen.texteditor.formattingrules.TextEditorFormattingRules;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollbar.ScrollBar;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.MouseInput;
import java.awt.Color;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class TextEditorScreen extends Screen {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final CharacterFilter characterFilter;

    protected final Consumer<String> callback;

    protected List<TextEditorLine> textFieldLines = new ArrayList();

    protected ScrollBar verticalScrollBar = new ScrollBar(ScrollBar.ScrollBarDirection.VERTICAL, 5, 40, 0, 0, 0, 0, (Color) null, null);

    protected ScrollBar horizontalScrollBar = new ScrollBar(ScrollBar.ScrollBarDirection.HORIZONTAL, 40, 5, 0, 0, 0, 0, (Color) null, null);

    protected ScrollBar verticalScrollBarPlaceholderMenu = new ScrollBar(ScrollBar.ScrollBarDirection.VERTICAL, 5, 40, 0, 0, 0, 0, (Color) null, null);

    protected ScrollBar horizontalScrollBarPlaceholderMenu = new ScrollBar(ScrollBar.ScrollBarDirection.HORIZONTAL, 40, 5, 0, 0, 0, 0, (Color) null, null);

    protected ContextMenu rightClickContextMenu;

    protected ExtendedButton cancelButton;

    protected ExtendedButton doneButton;

    protected ExtendedButton placeholderButton;

    protected int lastCursorPosSetByUser = 0;

    protected boolean justSwitchedLineByWordDeletion = false;

    protected boolean triggeredFocusedLineWasTooHighInCursorPosMethod = false;

    protected int headerHeight = 50;

    protected int footerHeight = 50;

    protected int borderLeft = 40;

    protected int borderRight = 20;

    protected int lineHeight = 14;

    protected Color screenBackgroundColor = UIBase.getUIColorTheme().screen_background_color.getColor();

    protected Color editorAreaBorderColor = UIBase.getUIColorTheme().element_border_color_normal.getColor();

    protected Color editorAreaBackgroundColor = UIBase.getUIColorTheme().area_background_color.getColor();

    protected Color textColor = UIBase.getUIColorTheme().text_editor_text_color.getColor();

    protected Color focusedLineColor = UIBase.getUIColorTheme().list_entry_color_selected_hovered.getColor();

    protected Color scrollGrabberIdleColor = UIBase.getUIColorTheme().scroll_grabber_color_normal.getColor();

    protected Color scrollGrabberHoverColor = UIBase.getUIColorTheme().scroll_grabber_color_hover.getColor();

    protected Color sideBarColor = UIBase.getUIColorTheme().text_editor_sidebar_color.getColor();

    protected Color lineNumberTextColorNormal = UIBase.getUIColorTheme().text_editor_line_number_text_color_normal.getColor();

    protected Color lineNumberTextColorFocused = UIBase.getUIColorTheme().text_editor_line_number_text_color_selected.getColor();

    protected Color multilineNotSupportedNotificationColor = UIBase.getUIColorTheme().error_text_color.getColor();

    protected Color placeholderEntryBackgroundColorIdle = UIBase.getUIColorTheme().area_background_color.getColor();

    protected Color placeholderEntryBackgroundColorHover = UIBase.getUIColorTheme().list_entry_color_selected_hovered.getColor();

    protected Color placeholderEntryDotColorPlaceholder = UIBase.getUIColorTheme().listing_dot_color_1.getColor();

    protected Color placeholderEntryDotColorCategory = UIBase.getUIColorTheme().listing_dot_color_2.getColor();

    protected Color placeholderEntryLabelColor = UIBase.getUIColorTheme().description_area_text_color.getColor();

    protected Color placeholderEntryBackToCategoriesLabelColor = UIBase.getUIColorTheme().warning_text_color.getColor();

    protected int currentLineWidth;

    protected int lastTickFocusedLineIndex = -1;

    protected TextEditorLine startHighlightLine = null;

    protected int startHighlightLineIndex = -1;

    protected int endHighlightLineIndex = -1;

    protected int overriddenTotalScrollHeight = -1;

    protected List<Runnable> lineNumberRenderQueue = new ArrayList();

    public List<TextEditorFormattingRule> formattingRules = new ArrayList();

    protected int currentRenderCharacterIndexTotal = 0;

    protected static boolean extendedPlaceholderMenu = false;

    protected int placeholderMenuWidth = 120;

    protected int placeholderMenuEntryHeight = 16;

    protected List<TextEditorScreen.PlaceholderMenuEntry> placeholderMenuEntries = new ArrayList();

    protected boolean multilineMode = true;

    protected boolean allowPlaceholders = true;

    protected long multilineNotSupportedNotificationDisplayStart = -1L;

    protected boolean boldTitle = true;

    protected ConsumingSupplier<TextEditorScreen, Boolean> textValidator = null;

    protected Tooltip textValidatorFeedbackTooltip = null;

    protected boolean selectedHoveredOnRightClickMenuOpen = false;

    protected final TextEditorHistory history = new TextEditorHistory(this);

    @NotNull
    public static TextEditorScreen build(@Nullable Component title, @Nullable CharacterFilter characterFilter, @NotNull Consumer<String> callback) {
        return new TextEditorScreen(title, characterFilter, callback);
    }

    public TextEditorScreen(@Nullable CharacterFilter characterFilter, @NotNull Consumer<String> callback) {
        this(null, characterFilter, callback);
    }

    public TextEditorScreen(@Nullable Component title, @Nullable CharacterFilter characterFilter, @NotNull Consumer<String> callback) {
        super((Component) (title != null ? title : Component.literal("")));
        this.f_96541_ = Minecraft.getInstance();
        this.f_96547_ = Minecraft.getInstance().font;
        this.characterFilter = characterFilter;
        this.callback = callback;
        this.addLine();
        this.getLine(0).m_93692_(true);
        this.verticalScrollBar.setScrollWheelAllowed(true);
        this.verticalScrollBarPlaceholderMenu.setScrollWheelAllowed(true);
        this.formattingRules.addAll(TextEditorFormattingRules.getRules());
        this.updatePlaceholderEntries(null, true, true);
        this.updateCurrentLineWidth();
    }

    @Override
    public void init() {
        this.updateRightClickContextMenu();
        this.m_7787_(this.rightClickContextMenu);
        this.verticalScrollBar.scrollAreaStartX = this.getEditorAreaX() + 1;
        this.verticalScrollBar.scrollAreaStartY = this.getEditorAreaY() + 1;
        this.verticalScrollBar.scrollAreaEndX = this.getEditorAreaX() + this.getEditorAreaWidth() - 2;
        this.verticalScrollBar.scrollAreaEndY = this.getEditorAreaY() + this.getEditorAreaHeight() - this.horizontalScrollBar.grabberHeight - 2;
        this.horizontalScrollBar.scrollAreaStartX = this.getEditorAreaX() + 1;
        this.horizontalScrollBar.scrollAreaStartY = this.getEditorAreaY() + 1;
        this.horizontalScrollBar.scrollAreaEndX = this.getEditorAreaX() + this.getEditorAreaWidth() - this.verticalScrollBar.grabberWidth - 2;
        this.horizontalScrollBar.scrollAreaEndY = this.getEditorAreaY() + this.getEditorAreaHeight() - 1;
        int placeholderAreaX = this.f_96543_ - this.borderRight - this.placeholderMenuWidth;
        int placeholderAreaY = this.getEditorAreaY();
        this.verticalScrollBarPlaceholderMenu.scrollAreaStartX = placeholderAreaX + 1;
        this.verticalScrollBarPlaceholderMenu.scrollAreaStartY = placeholderAreaY + 1;
        this.verticalScrollBarPlaceholderMenu.scrollAreaEndX = placeholderAreaX + this.placeholderMenuWidth - 2;
        this.verticalScrollBarPlaceholderMenu.scrollAreaEndY = placeholderAreaY + this.getEditorAreaHeight() - this.horizontalScrollBarPlaceholderMenu.grabberHeight - 2;
        this.horizontalScrollBarPlaceholderMenu.scrollAreaStartX = placeholderAreaX + 1;
        this.horizontalScrollBarPlaceholderMenu.scrollAreaStartY = placeholderAreaY + 1;
        this.horizontalScrollBarPlaceholderMenu.scrollAreaEndX = placeholderAreaX + this.placeholderMenuWidth - this.verticalScrollBarPlaceholderMenu.grabberWidth - 2;
        this.horizontalScrollBarPlaceholderMenu.scrollAreaEndY = placeholderAreaY + this.getEditorAreaHeight() - 1;
        this.verticalScrollBar.idleBarColor = this.scrollGrabberIdleColor;
        this.verticalScrollBar.hoverBarColor = this.scrollGrabberHoverColor;
        this.horizontalScrollBar.idleBarColor = this.scrollGrabberIdleColor;
        this.horizontalScrollBar.hoverBarColor = this.scrollGrabberHoverColor;
        this.verticalScrollBarPlaceholderMenu.idleBarColor = this.scrollGrabberIdleColor;
        this.verticalScrollBarPlaceholderMenu.hoverBarColor = this.scrollGrabberHoverColor;
        this.horizontalScrollBarPlaceholderMenu.idleBarColor = this.scrollGrabberIdleColor;
        this.horizontalScrollBarPlaceholderMenu.hoverBarColor = this.scrollGrabberHoverColor;
        this.cancelButton = new ExtendedButton(this.f_96543_ - this.borderRight - 100 - 5 - 100, this.f_96544_ - 35, 100, 20, I18n.get("fancymenu.guicomponents.cancel"), button -> this.onClose());
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
        this.doneButton = new ExtendedButton(this.f_96543_ - this.borderRight - 100, this.f_96544_ - 35, 100, 20, I18n.get("fancymenu.guicomponents.done"), button -> {
            if (this.isTextValid()) {
                this.callback.accept(this.getText());
            }
        });
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        if (this.allowPlaceholders) {
            this.placeholderButton = new ExtendedButton(this.f_96543_ - this.borderRight - 100, this.headerHeight / 2 - 10, 100, 20, I18n.get("fancymenu.ui.text_editor.placeholders"), button -> {
                if (extendedPlaceholderMenu) {
                    extendedPlaceholderMenu = false;
                } else {
                    extendedPlaceholderMenu = true;
                }
                this.m_232761_();
            }).setTooltip(Tooltip.of(LocalizationUtils.splitLocalizedStringLines("fancymenu.editor.dynamicvariabletextfield.variables.desc")).setDefaultStyle());
            this.m_7787_(this.placeholderButton);
            UIBase.applyDefaultWidgetSkinTo(this.placeholderButton);
            if (extendedPlaceholderMenu) {
                this.placeholderButton.setBackgroundColor(UIBase.getUIColorTheme().element_background_color_normal, UIBase.getUIColorTheme().element_background_color_hover, UIBase.getUIColorTheme().element_background_color_normal, DrawableColor.of(this.editorAreaBorderColor), DrawableColor.of(this.editorAreaBorderColor), DrawableColor.of(this.editorAreaBorderColor));
                ((IMixinAbstractWidget) this.placeholderButton).setHeightFancyMenu(this.getEditorAreaY() - (this.headerHeight / 2 - 10));
            }
        } else {
            this.placeholderButton = null;
            extendedPlaceholderMenu = false;
        }
    }

    public void updateRightClickContextMenu() {
        if (this.rightClickContextMenu != null) {
            this.rightClickContextMenu.closeMenu();
        }
        this.rightClickContextMenu = new ContextMenu();
        this.rightClickContextMenu.addClickableEntry("copy", Component.translatable("fancymenu.ui.text_editor.copy"), (menu, entry) -> {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlightedText());
            menu.closeMenu();
        }).setIsActiveSupplier((menu, entry) -> !menu.isOpen() ? false : this.selectedHoveredOnRightClickMenuOpen).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.copy")).setIcon(ContextMenu.IconFactory.getIcon("copy"));
        this.rightClickContextMenu.addClickableEntry("paste", Component.translatable("fancymenu.ui.text_editor.paste"), (menu, entry) -> {
            this.pasteText(Minecraft.getInstance().keyboardHandler.getClipboard());
            menu.closeMenu();
        }).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.paste")).setIcon(ContextMenu.IconFactory.getIcon("paste"));
        this.rightClickContextMenu.addSeparatorEntry("separator_after_paste");
        this.rightClickContextMenu.addClickableEntry("cut", Component.translatable("fancymenu.ui.text_editor.cut"), (menu, entry) -> {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.cutHighlightedText());
            menu.closeMenu();
        }).setIsActiveSupplier((menu, entry) -> !menu.isOpen() ? false : this.selectedHoveredOnRightClickMenuOpen).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.cut")).setIcon(ContextMenu.IconFactory.getIcon("cut"));
        this.rightClickContextMenu.addSeparatorEntry("separator_after_cut");
        this.rightClickContextMenu.addClickableEntry("select_all", Component.translatable("fancymenu.ui.text_editor.select_all"), (menu, entry) -> {
            for (TextEditorLine t : this.textFieldLines) {
                t.m_94208_(0);
                t.setCursorPosition(t.m_94155_().length());
            }
            this.setFocusedLine(this.getLineCount() - 1);
            this.startHighlightLineIndex = 0;
            this.endHighlightLineIndex = this.getLineCount() - 1;
            menu.closeMenu();
        }).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.select_all")).setIcon(ContextMenu.IconFactory.getIcon("select"));
        this.rightClickContextMenu.addSeparatorEntry("separator_after_select_all");
        this.rightClickContextMenu.addClickableEntry("undo", Component.translatable("fancymenu.editor.edit.undo"), (menu, entry) -> this.history.stepBack()).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.undo")).setIcon(ContextMenu.IconFactory.getIcon("undo"));
        this.rightClickContextMenu.addClickableEntry("redo", Component.translatable("fancymenu.editor.edit.redo"), (menu, entry) -> this.history.stepForward()).setShortcutTextSupplier((menu, entry) -> Component.translatable("fancymenu.editor.shortcuts.redo")).setIcon(ContextMenu.IconFactory.getIcon("redo"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.currentLineWidth <= this.getEditorAreaWidth()) {
            this.horizontalScrollBar.setScroll(0.0F);
        }
        if (this.getTotalLineHeight() <= this.getEditorAreaHeight()) {
            this.verticalScrollBar.setScroll(0.0F);
        }
        this.justSwitchedLineByWordDeletion = false;
        this.updateCurrentLineWidth();
        this.verticalScrollBar.setWheelScrollSpeed(1.0F / ((float) this.getTotalScrollHeight() / 500.0F));
        this.renderScreenBackground(graphics);
        this.renderEditorAreaBackground(graphics);
        Window win = Minecraft.getInstance().getWindow();
        double scale = win.getGuiScale();
        int sciBottom = this.f_96544_ - this.footerHeight;
        RenderSystem.enableScissor((int) ((double) this.borderLeft * scale), (int) ((double) win.getHeight() - (double) sciBottom * scale), (int) ((double) this.getEditorAreaWidth() * scale), (int) ((double) this.getEditorAreaHeight() * scale));
        this.formattingRules.forEach(rule -> rule.resetRule(this));
        this.currentRenderCharacterIndexTotal = 0;
        this.lineNumberRenderQueue.clear();
        this.updateLines(line -> {
            if (line.isInEditorArea()) {
                this.lineNumberRenderQueue.add((Runnable) () -> this.renderLineNumber(graphics, line));
            }
            line.render(graphics, mouseX, mouseY, partial);
        });
        RenderSystem.disableScissor();
        this.renderLineNumberBackground(graphics, this.borderLeft);
        RenderSystem.enableScissor(0, (int) ((double) win.getHeight() - (double) sciBottom * scale), (int) ((double) this.borderLeft * scale), (int) ((double) this.getEditorAreaHeight() * scale));
        for (Runnable r : this.lineNumberRenderQueue) {
            r.run();
        }
        RenderSystem.disableScissor();
        this.lastTickFocusedLineIndex = this.getFocusedLineIndex();
        this.triggeredFocusedLineWasTooHighInCursorPosMethod = false;
        UIBase.renderBorder(graphics, this.borderLeft - 1, this.headerHeight - 1, this.getEditorAreaX() + this.getEditorAreaWidth(), this.f_96544_ - this.footerHeight + 1, 1, this.editorAreaBorderColor, true, true, true, true);
        this.verticalScrollBar.render(graphics);
        this.horizontalScrollBar.render(graphics);
        this.renderPlaceholderMenu(graphics, mouseX, mouseY, partial);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        this.doneButton.f_93623_ = this.isTextValid();
        this.doneButton.setTooltip(this.textValidatorFeedbackTooltip);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.renderMultilineNotSupportedNotification(graphics, mouseX, mouseY, partial);
        this.rightClickContextMenu.render(graphics, mouseX, mouseY, partial);
        this.tickMouseHighlighting();
        MutableComponent t = this.f_96539_.copy();
        t.setStyle(t.getStyle().withBold(this.boldTitle));
        graphics.drawString(this.f_96547_, t, this.borderLeft, this.headerHeight / 2 - 9 / 2, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
    }

    protected void renderMultilineNotSupportedNotification(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        long now = System.currentTimeMillis();
        if (!this.multilineMode && this.multilineNotSupportedNotificationDisplayStart + 3000L >= now) {
            int a = 255;
            int diff = (int) (this.multilineNotSupportedNotificationDisplayStart + 3000L - now);
            if (diff <= 1000) {
                float f = (float) diff / 1000.0F;
                a = Math.max(10, (int) (255.0F * f));
            }
            Color c = new Color(this.multilineNotSupportedNotificationColor.getRed(), this.multilineNotSupportedNotificationColor.getGreen(), this.multilineNotSupportedNotificationColor.getBlue(), a);
            graphics.drawString(this.f_96547_, I18n.get("fancymenu.ui.text_editor.error.multiline_support"), this.borderLeft, this.headerHeight - 9 - 5, c.getRGB(), false);
        }
    }

    protected void renderPlaceholderMenu(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (extendedPlaceholderMenu) {
            if (this.getTotalPlaceholderEntriesWidth() <= this.placeholderMenuWidth) {
                this.horizontalScrollBarPlaceholderMenu.setScroll(0.0F);
            }
            if (this.getTotalPlaceholderEntriesHeight() <= this.getEditorAreaHeight()) {
                this.verticalScrollBarPlaceholderMenu.setScroll(0.0F);
            }
            graphics.fill(this.f_96543_ - this.borderRight - this.placeholderMenuWidth, this.getEditorAreaY(), this.f_96543_ - this.borderRight, this.getEditorAreaY() + this.getEditorAreaHeight(), this.editorAreaBackgroundColor.getRGB());
            Window win = Minecraft.getInstance().getWindow();
            double scale = win.getGuiScale();
            int sciBottom = this.f_96544_ - this.footerHeight;
            RenderSystem.enableScissor((int) ((double) (this.f_96543_ - this.borderRight - this.placeholderMenuWidth) * scale), (int) ((double) win.getHeight() - (double) sciBottom * scale), (int) ((double) this.placeholderMenuWidth * scale), (int) ((double) this.getEditorAreaHeight() * scale));
            List<TextEditorScreen.PlaceholderMenuEntry> entries = new ArrayList();
            entries.addAll(this.placeholderMenuEntries);
            int index = 0;
            for (TextEditorScreen.PlaceholderMenuEntry e : entries) {
                e.x = this.f_96543_ - this.borderRight - this.placeholderMenuWidth + this.getPlaceholderEntriesRenderOffsetX();
                e.y = this.getEditorAreaY() + this.placeholderMenuEntryHeight * index + this.getPlaceholderEntriesRenderOffsetY();
                e.render(graphics, mouseX, mouseY, partial);
                index++;
            }
            RenderSystem.disableScissor();
            UIBase.renderBorder(graphics, this.f_96543_ - this.borderRight - this.placeholderMenuWidth - 1, this.headerHeight - 1, this.f_96543_ - this.borderRight, this.f_96544_ - this.footerHeight + 1, 1, this.editorAreaBorderColor, true, true, true, true);
            this.verticalScrollBarPlaceholderMenu.render(graphics);
            this.horizontalScrollBarPlaceholderMenu.render(graphics);
        }
        if (this.placeholderButton != null) {
            this.placeholderButton.render(graphics, mouseX, mouseY, partial);
        }
    }

    public int getTotalPlaceholderEntriesHeight() {
        return this.placeholderMenuEntryHeight * this.placeholderMenuEntries.size();
    }

    public int getTotalPlaceholderEntriesWidth() {
        int i = this.placeholderMenuWidth;
        for (TextEditorScreen.PlaceholderMenuEntry e : this.placeholderMenuEntries) {
            if (e.getWidth() > i) {
                i = e.getWidth();
            }
        }
        return i;
    }

    public int getPlaceholderEntriesRenderOffsetX() {
        int totalScrollWidth = Math.max(0, this.getTotalPlaceholderEntriesWidth() - this.placeholderMenuWidth);
        return -((int) ((float) totalScrollWidth / 100.0F * this.horizontalScrollBarPlaceholderMenu.getScroll() * 100.0F));
    }

    public int getPlaceholderEntriesRenderOffsetY() {
        int totalScrollHeight = Math.max(0, this.getTotalPlaceholderEntriesHeight() - this.getEditorAreaHeight());
        return -((int) ((float) totalScrollHeight / 100.0F * this.verticalScrollBarPlaceholderMenu.getScroll() * 100.0F));
    }

    public void updatePlaceholderEntries(@Nullable String category, boolean clearList, boolean addBackButton) {
        if (clearList) {
            this.placeholderMenuEntries.clear();
        }
        Map<String, List<Placeholder>> categories = this.getPlaceholdersOrderedByCategories();
        if (!categories.isEmpty()) {
            List<Placeholder> otherCategory = (List<Placeholder>) categories.get(I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other"));
            if (otherCategory != null) {
                if (category != null) {
                    if (addBackButton) {
                        TextEditorScreen.PlaceholderMenuEntry backToCategoriesEntry = new TextEditorScreen.PlaceholderMenuEntry(this, Component.literal(I18n.get("fancymenu.ui.text_editor.placeholders.back_to_categories")), () -> this.updatePlaceholderEntries(null, true, true));
                        backToCategoriesEntry.dotColor = this.placeholderEntryDotColorCategory;
                        backToCategoriesEntry.entryLabelColor = this.placeholderEntryBackToCategoriesLabelColor;
                        this.placeholderMenuEntries.add(backToCategoriesEntry);
                    }
                    List<Placeholder> placeholders = (List<Placeholder>) categories.get(category);
                    if (placeholders != null) {
                        for (Placeholder p : placeholders) {
                            TextEditorScreen.PlaceholderMenuEntry entry = new TextEditorScreen.PlaceholderMenuEntry(this, Component.literal(p.getDisplayName()), () -> this.pasteText(p.getDefaultPlaceholderString().toString()));
                            List<String> desc = p.getDescription();
                            if (desc != null) {
                                entry.setDescription((String[]) desc.toArray(new String[0]));
                            }
                            entry.dotColor = this.placeholderEntryDotColorPlaceholder;
                            entry.entryLabelColor = this.placeholderEntryLabelColor;
                            this.placeholderMenuEntries.add(entry);
                        }
                    }
                } else {
                    for (Entry<String, List<Placeholder>> m : categories.entrySet()) {
                        if (m.getValue() != otherCategory) {
                            TextEditorScreen.PlaceholderMenuEntry entry = new TextEditorScreen.PlaceholderMenuEntry(this, Component.literal((String) m.getKey()), () -> this.updatePlaceholderEntries((String) m.getKey(), true, true));
                            entry.dotColor = this.placeholderEntryDotColorCategory;
                            entry.entryLabelColor = this.placeholderEntryLabelColor;
                            this.placeholderMenuEntries.add(entry);
                        }
                    }
                    this.updatePlaceholderEntries(I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other"), false, false);
                }
                for (TextEditorScreen.PlaceholderMenuEntry e : this.placeholderMenuEntries) {
                    e.backgroundColorIdle = this.placeholderEntryBackgroundColorIdle;
                    e.backgroundColorHover = this.placeholderEntryBackgroundColorHover;
                }
                this.verticalScrollBarPlaceholderMenu.setScroll(0.0F);
                this.horizontalScrollBarPlaceholderMenu.setScroll(0.0F);
            }
        }
    }

    protected Map<String, List<Placeholder>> getPlaceholdersOrderedByCategories() {
        Map<String, List<Placeholder>> categories = new LinkedHashMap();
        for (Placeholder p : PlaceholderRegistry.getPlaceholders()) {
            if (p.shouldShowUpInPlaceholderMenu(LayoutEditorScreen.getCurrentInstance())) {
                String cat = p.getCategory();
                if (cat == null) {
                    cat = I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other");
                }
                List<Placeholder> l = (List<Placeholder>) categories.get(cat);
                if (l == null) {
                    l = new ArrayList();
                    categories.put(cat, l);
                }
                l.add(p);
            }
        }
        List<Placeholder> otherCategory = (List<Placeholder>) categories.get(I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other"));
        if (otherCategory != null) {
            categories.remove(I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other"));
            categories.put(I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other"), otherCategory);
        }
        return categories;
    }

    protected void renderLineNumberBackground(GuiGraphics graphics, int width) {
        graphics.fill(this.getEditorAreaX(), this.getEditorAreaY() - 1, this.getEditorAreaX() - width - 1, this.getEditorAreaY() + this.getEditorAreaHeight() + 1, this.sideBarColor.getRGB());
    }

    protected void renderLineNumber(GuiGraphics graphics, TextEditorLine line) {
        String lineNumberString = line.lineIndex + 1 + "";
        int lineNumberWidth = this.f_96547_.width(lineNumberString);
        graphics.drawString(this.f_96547_, lineNumberString, this.getEditorAreaX() - 3 - lineNumberWidth, line.m_252907_() + line.m_93694_() / 2 - 9 / 2, line.m_93696_() ? this.lineNumberTextColorFocused.getRGB() : this.lineNumberTextColorNormal.getRGB(), false);
    }

    protected void renderEditorAreaBackground(GuiGraphics graphics) {
        graphics.fill(this.getEditorAreaX(), this.getEditorAreaY(), this.getEditorAreaX() + this.getEditorAreaWidth(), this.getEditorAreaY() + this.getEditorAreaHeight(), this.editorAreaBackgroundColor.getRGB());
    }

    protected void renderScreenBackground(GuiGraphics graphics) {
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, this.screenBackgroundColor.getRGB());
    }

    protected void tickMouseHighlighting() {
        if (!MouseInput.isLeftMouseDown()) {
            this.startHighlightLine = null;
            for (TextEditorLine t : this.textFieldLines) {
                t.isInMouseHighlightingMode = false;
            }
        } else {
            if (this.isInMouseHighlightingMode()) {
                int mX = MouseInput.getMouseX();
                int mY = MouseInput.getMouseY();
                float speedMult = 0.008F;
                if (mX < this.borderLeft) {
                    float f = Math.max(0.01F, (float) (this.borderLeft - mX) * speedMult);
                    this.horizontalScrollBar.setScroll(this.horizontalScrollBar.getScroll() - f);
                } else if (mX > this.getEditorAreaX() + this.getEditorAreaWidth()) {
                    float f = Math.max(0.01F, (float) (mX - (this.getEditorAreaX() + this.getEditorAreaWidth())) * speedMult);
                    this.horizontalScrollBar.setScroll(this.horizontalScrollBar.getScroll() + f);
                }
                if (mY < this.headerHeight) {
                    float f = Math.max(0.01F, (float) (this.headerHeight - mY) * speedMult);
                    this.verticalScrollBar.setScroll(this.verticalScrollBar.getScroll() - f);
                } else if (mY > this.f_96544_ - this.footerHeight) {
                    float f = Math.max(0.01F, (float) (mY - (this.f_96544_ - this.footerHeight)) * speedMult);
                    this.verticalScrollBar.setScroll(this.verticalScrollBar.getScroll() + f);
                }
            }
            if (this.isMouseInsideEditorArea()) {
                TextEditorLine first = this.startHighlightLine;
                TextEditorLine hovered = this.getHoveredLine();
                if (hovered != null && !hovered.m_93696_() && first != null) {
                    int firstIndex = this.getLineIndex(first);
                    int hoveredIndex = this.getLineIndex(hovered);
                    boolean firstIsBeforeHovered = hoveredIndex > firstIndex;
                    boolean firstIsAfterHovered = hoveredIndex < firstIndex;
                    if (first.isInMouseHighlightingMode) {
                        if (firstIsAfterHovered) {
                            this.setFocusedLine(this.getLineIndex(hovered));
                            if (!hovered.isInMouseHighlightingMode) {
                                hovered.isInMouseHighlightingMode = true;
                                hovered.getAsAccessor().setShiftPressedFancyMenu(false);
                                hovered.m_94192_(hovered.m_94155_().length());
                            }
                        } else if (firstIsBeforeHovered) {
                            this.setFocusedLine(this.getLineIndex(hovered));
                            if (!hovered.isInMouseHighlightingMode) {
                                hovered.isInMouseHighlightingMode = true;
                                hovered.getAsAccessor().setShiftPressedFancyMenu(false);
                                hovered.m_94192_(0);
                            }
                        } else if (first == hovered) {
                            this.setFocusedLine(this.getLineIndex(first));
                        }
                    }
                    int startIndex = Math.min(hoveredIndex, firstIndex);
                    int endIndex = Math.max(hoveredIndex, firstIndex);
                    int index = 0;
                    for (TextEditorLine t : this.textFieldLines) {
                        if (t != hovered && t != first) {
                            if (index <= startIndex || index >= endIndex) {
                                t.getAsAccessor().setShiftPressedFancyMenu(false);
                                t.m_94192_(0);
                                t.isInMouseHighlightingMode = false;
                            } else if (firstIsAfterHovered) {
                                t.setCursorPosition(0);
                                t.m_94208_(t.m_94155_().length());
                            } else if (firstIsBeforeHovered) {
                                t.setCursorPosition(t.m_94155_().length());
                                t.m_94208_(0);
                            }
                        }
                        index++;
                    }
                    this.startHighlightLineIndex = startIndex;
                    this.endHighlightLineIndex = endIndex;
                    if (first != hovered) {
                        first.getAsAccessor().setShiftPressedFancyMenu(true);
                        if (firstIsAfterHovered) {
                            first.m_94192_(0);
                        } else if (firstIsBeforeHovered) {
                            first.m_94192_(first.m_94155_().length());
                        }
                        first.getAsAccessor().setShiftPressedFancyMenu(false);
                    }
                }
                TextEditorLine focused = this.getFocusedLine();
                if (focused != null && focused.isInMouseHighlightingMode) {
                    if (this.startHighlightLineIndex == -1 && this.endHighlightLineIndex == -1) {
                        this.startHighlightLineIndex = this.getLineIndex(focused);
                        this.endHighlightLineIndex = this.startHighlightLineIndex;
                    }
                    int i = Mth.floor((float) MouseInput.getMouseX()) - focused.m_252754_();
                    if (focused.getAsAccessor().getBorderedFancyMenu()) {
                        i -= 4;
                    }
                    String s = this.f_96547_.plainSubstrByWidth(focused.m_94155_().substring(focused.getAsAccessor().getDisplayPosFancyMenu()), focused.m_94210_());
                    focused.getAsAccessor().setShiftPressedFancyMenu(true);
                    focused.m_94192_(this.f_96547_.plainSubstrByWidth(s, i).length() + focused.getAsAccessor().getDisplayPosFancyMenu());
                    focused.getAsAccessor().setShiftPressedFancyMenu(false);
                    if (focused.getAsAccessor().getHighlightPosFancyMenu() == focused.m_94207_() && this.startHighlightLineIndex == this.endHighlightLineIndex) {
                        this.resetHighlighting();
                    }
                }
            }
        }
    }

    public void updateLines(@Nullable Consumer<TextEditorLine> doAfterEachLineUpdate) {
        try {
            int index = 0;
            for (TextEditorLine line : this.textFieldLines) {
                line.lineIndex = index;
                line.m_253211_(this.headerHeight + this.lineHeight * index + this.getLineRenderOffsetY());
                line.m_252865_(this.borderLeft + this.getLineRenderOffsetX());
                line.m_93674_(this.currentLineWidth);
                ((IMixinAbstractWidget) line).setHeightFancyMenu(this.lineHeight);
                line.getAsAccessor().setDisplayPosFancyMenu(0);
                if (doAfterEachLineUpdate != null) {
                    doAfterEachLineUpdate.accept(line);
                }
                index++;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
    }

    public void updateCurrentLineWidth() {
        int longestTextWidth = 0;
        for (TextEditorLine f : this.textFieldLines) {
            if (f.textWidth > longestTextWidth) {
                longestTextWidth = f.textWidth;
            }
        }
        this.currentLineWidth = longestTextWidth + 30;
    }

    public int getLineRenderOffsetX() {
        return -((int) ((float) this.getTotalScrollWidth() / 100.0F * this.horizontalScrollBar.getScroll() * 100.0F));
    }

    public int getLineRenderOffsetY() {
        return -((int) ((float) this.getTotalScrollHeight() / 100.0F * this.verticalScrollBar.getScroll() * 100.0F));
    }

    public int getTotalLineHeight() {
        return this.lineHeight * this.textFieldLines.size();
    }

    @Nullable
    public TextEditorLine addLineAtIndex(int index) {
        if (!this.multilineMode && this.getLineCount() > 0) {
            this.multilineNotSupportedNotificationDisplayStart = System.currentTimeMillis();
            return null;
        } else {
            TextEditorLine f = new TextEditorLine(Minecraft.getInstance().font, 0, 0, 50, this.lineHeight, false, this.characterFilter, this);
            f.setMaxLength(Integer.MAX_VALUE);
            f.lineIndex = index;
            if (index > 0) {
                TextEditorLine before = this.getLine(index - 1);
                if (before != null) {
                    f.m_253211_(before.m_252907_() + this.lineHeight);
                }
            }
            this.textFieldLines.add(index, f);
            return f;
        }
    }

    @Nullable
    public TextEditorLine addLine() {
        return this.addLineAtIndex(this.getLineCount());
    }

    public void removeLineAtIndex(int index) {
        if (index >= 1) {
            if (index <= this.getLineCount() - 1) {
                this.textFieldLines.remove(index);
            }
        }
    }

    public void removeLastLine() {
        this.removeLineAtIndex(this.getLineCount() - 1);
    }

    public int getLineCount() {
        return this.textFieldLines.size();
    }

    @Nullable
    public TextEditorLine getLine(int index) {
        return (TextEditorLine) this.textFieldLines.get(index);
    }

    public void setFocusedLine(int index) {
        if (index <= this.getLineCount() - 1) {
            for (TextEditorLine f : this.textFieldLines) {
                f.m_93692_(false);
            }
            this.getLine(index).m_93692_(true);
        }
    }

    public int getFocusedLineIndex() {
        int index = 0;
        for (TextEditorLine f : this.textFieldLines) {
            if (f.m_93696_()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Nullable
    public TextEditorLine getFocusedLine() {
        int index = this.getFocusedLineIndex();
        return index != -1 ? this.getLine(index) : null;
    }

    public boolean isLineFocused() {
        return this.getFocusedLineIndex() > -1;
    }

    @Nullable
    public TextEditorLine getLineAfter(TextEditorLine line) {
        int index = this.getLineIndex(line);
        return index > -1 && index < this.getLineCount() - 1 ? this.getLine(index + 1) : null;
    }

    @Nullable
    public TextEditorLine getLineBefore(TextEditorLine line) {
        int index = this.getLineIndex(line);
        return index > 0 ? this.getLine(index - 1) : null;
    }

    public boolean isAtLeastOneLineInHighlightMode() {
        for (TextEditorLine t : this.textFieldLines) {
            if (t.isInMouseHighlightingMode) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public List<TextEditorLine> getLinesBetweenIndexes(int startIndex, int endIndex) {
        startIndex = Math.min(Math.max(startIndex, 0), this.textFieldLines.size() - 1);
        endIndex = Math.min(Math.max(endIndex, 0), this.textFieldLines.size() - 1);
        List<TextEditorLine> l = new ArrayList();
        l.addAll(this.textFieldLines.subList(startIndex, endIndex));
        if (!l.isEmpty()) {
            l.remove(0);
        }
        return l;
    }

    @Nullable
    public TextEditorLine getHoveredLine() {
        for (TextEditorLine t : this.textFieldLines) {
            if (t.m_274382_()) {
                return t;
            }
        }
        return null;
    }

    public int getLineIndex(TextEditorLine inputBox) {
        return this.textFieldLines.indexOf(inputBox);
    }

    public void goUpLine() {
        if (this.isLineFocused()) {
            int current = Math.max(0, this.getFocusedLineIndex());
            if (current > 0) {
                TextEditorLine currentLine = this.getLine(current);
                this.setFocusedLine(current - 1);
                if (currentLine != null) {
                    this.getFocusedLine().m_94192_(this.lastCursorPosSetByUser);
                }
            }
        }
    }

    public void goDownLine(boolean isNewLine) {
        if (this.isLineFocused()) {
            int current = Math.max(0, this.getFocusedLineIndex());
            if (isNewLine) {
                this.addLineAtIndex(current + 1);
            }
            TextEditorLine currentLine = this.getLine(current);
            this.setFocusedLine(current + 1);
            if (currentLine != null) {
                TextEditorLine nextLine = this.getFocusedLine();
                if (isNewLine) {
                    String textBeforeCursor = currentLine.m_94155_().substring(0, currentLine.m_94207_());
                    String textAfterCursor = currentLine.m_94155_().substring(currentLine.m_94207_());
                    currentLine.setValue(textBeforeCursor);
                    nextLine.setValue(textAfterCursor);
                    nextLine.m_94192_(0);
                    if (textBeforeCursor.startsWith(" ")) {
                        int spaces = 0;
                        for (char c : textBeforeCursor.toCharArray()) {
                            if (!String.valueOf(c).equals(" ")) {
                                break;
                            }
                            spaces++;
                        }
                        nextLine.setValue(textBeforeCursor.substring(0, spaces) + nextLine.m_94155_());
                        nextLine.m_94192_(spaces);
                    }
                } else {
                    nextLine.m_94192_(this.lastCursorPosSetByUser);
                }
            }
        }
    }

    public List<TextEditorLine> getCopyOfLines() {
        List<TextEditorLine> l = new ArrayList();
        for (TextEditorLine t : this.textFieldLines) {
            TextEditorLine n = new TextEditorLine(this.f_96547_, 0, 0, 0, 0, false, this.characterFilter, this);
            n.setValue(t.m_94155_());
            n.m_93692_(t.m_93696_());
            n.m_94192_(t.m_94207_());
            l.add(n);
        }
        return l;
    }

    public List<TextEditorLine> getLines() {
        return this.textFieldLines;
    }

    public boolean isTextHighlighted() {
        return this.startHighlightLineIndex != -1 && this.endHighlightLineIndex != -1;
    }

    public boolean isHighlightedTextHovered() {
        if (this.isTextHighlighted()) {
            List<TextEditorLine> highlightedLines = new ArrayList();
            if (this.endHighlightLineIndex <= this.getLineCount() - 1) {
                highlightedLines.addAll(this.textFieldLines.subList(this.startHighlightLineIndex, this.endHighlightLineIndex + 1));
            }
            for (TextEditorLine t : highlightedLines) {
                if (t.isHighlightedHovered()) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public String getHighlightedText() {
        try {
            if (this.startHighlightLineIndex != -1 && this.endHighlightLineIndex != -1) {
                List<TextEditorLine> lines = new ArrayList();
                lines.add(this.getLine(this.startHighlightLineIndex));
                if (this.startHighlightLineIndex != this.endHighlightLineIndex) {
                    lines.addAll(this.getLinesBetweenIndexes(this.startHighlightLineIndex, this.endHighlightLineIndex));
                    lines.add(this.getLine(this.endHighlightLineIndex));
                }
                StringBuilder s = new StringBuilder();
                boolean b = false;
                for (TextEditorLine t : lines) {
                    if (b) {
                        s.append("\n");
                    }
                    s.append(t.m_94173_());
                    b = true;
                }
                return s.toString();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return "";
    }

    @NotNull
    public String cutHighlightedText() {
        String highlighted = this.getHighlightedText();
        this.deleteHighlightedText();
        return highlighted;
    }

    public void deleteHighlightedText() {
        int linesRemoved = 0;
        try {
            if (this.startHighlightLineIndex != -1 && this.endHighlightLineIndex != -1) {
                if (this.startHighlightLineIndex == this.endHighlightLineIndex) {
                    this.getLine(this.startHighlightLineIndex).insertText("");
                } else {
                    TextEditorLine start = this.getLine(this.startHighlightLineIndex);
                    start.insertText("");
                    TextEditorLine end = this.getLine(this.endHighlightLineIndex);
                    end.insertText("");
                    if (this.endHighlightLineIndex - this.startHighlightLineIndex > 1) {
                        for (TextEditorLine line : this.getLinesBetweenIndexes(this.startHighlightLineIndex, this.endHighlightLineIndex)) {
                            this.removeLineAtIndex(this.getLineIndex(line));
                            linesRemoved++;
                        }
                    }
                    String oldStartValue = start.m_94155_();
                    start.setCursorPosition(start.m_94155_().length());
                    start.m_94208_(start.m_94207_());
                    start.insertText(end.m_94155_());
                    start.setCursorPosition(oldStartValue.length());
                    start.m_94208_(start.m_94207_());
                    this.removeLineAtIndex(this.getLineIndex(end));
                    linesRemoved++;
                    this.setFocusedLine(this.startHighlightLineIndex);
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        this.correctYScroll(-linesRemoved);
        this.resetHighlighting();
    }

    public void resetHighlighting() {
        this.startHighlightLineIndex = -1;
        this.endHighlightLineIndex = -1;
        for (TextEditorLine t : this.textFieldLines) {
            t.m_94208_(t.m_94207_());
        }
    }

    public boolean isInMouseHighlightingMode() {
        return MouseInput.isLeftMouseDown() && this.startHighlightLine != null;
    }

    public void pasteText(String text) {
        try {
            if (text != null && !text.equals("")) {
                int addedLinesCount = 0;
                if (this.isTextHighlighted()) {
                    this.deleteHighlightedText();
                }
                if (!this.isLineFocused()) {
                    this.setFocusedLine(this.getLineCount() - 1);
                    this.getFocusedLine().m_94201_();
                }
                TextEditorLine focusedLine = this.getFocusedLine();
                String textBeforeCursor = "";
                String textAfterCursor = "";
                if (focusedLine.m_94155_().length() > 0) {
                    textBeforeCursor = focusedLine.m_94155_().substring(0, focusedLine.m_94207_());
                    if (focusedLine.m_94207_() < focusedLine.m_94155_().length()) {
                        textAfterCursor = this.getFocusedLine().m_94155_().substring(focusedLine.m_94207_(), focusedLine.m_94155_().length());
                    }
                }
                focusedLine.setValue(textBeforeCursor);
                focusedLine.setCursorPosition(textBeforeCursor.length());
                String[] lines = new String[] { text };
                if (text.contains("\n")) {
                    lines = text.split("\n", -1);
                }
                if (!this.multilineMode && lines.length > 1) {
                    lines = new String[] { lines[0] };
                    this.multilineNotSupportedNotificationDisplayStart = System.currentTimeMillis();
                }
                Array.set(lines, lines.length - 1, lines[lines.length - 1] + textAfterCursor);
                if (lines.length == 1) {
                    this.getFocusedLine().insertText(lines[0]);
                } else if (lines.length > 1) {
                    int index = -1;
                    for (String s : lines) {
                        if (index == -1) {
                            index = this.getFocusedLineIndex();
                        } else {
                            this.addLineAtIndex(index);
                            addedLinesCount++;
                        }
                        this.getLine(index).insertText(s);
                        index++;
                    }
                    this.setFocusedLine(index - 1);
                    this.getFocusedLine().setCursorPosition(Math.max(0, this.getFocusedLine().m_94155_().length() - textAfterCursor.length()));
                    this.getFocusedLine().m_94208_(this.getFocusedLine().m_94207_());
                }
                this.correctYScroll(addedLinesCount);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        this.resetHighlighting();
    }

    public TextEditorScreen setText(@Nullable String text) {
        if (text == null) {
            text = "";
        }
        TextEditorLine t = this.getLine(0);
        this.textFieldLines.clear();
        this.textFieldLines.add(t);
        this.setFocusedLine(0);
        t.setValue("");
        t.m_94192_(0);
        this.pasteText(text);
        this.setFocusedLine(0);
        t.m_94192_(0);
        this.verticalScrollBar.setScroll(0.0F);
        return this;
    }

    @NotNull
    public String getText() {
        StringBuilder s = new StringBuilder();
        boolean b = false;
        for (TextEditorLine t : this.textFieldLines) {
            if (b) {
                s.append("\n");
            }
            s.append(t.m_94155_());
            b = true;
        }
        return s.toString();
    }

    protected boolean isTextValid() {
        return this.textValidator != null ? this.textValidator.get(this) : true;
    }

    public TextEditorScreen setTextValidator(@Nullable ConsumingSupplier<TextEditorScreen, Boolean> textValidator) {
        this.textValidator = textValidator;
        return this;
    }

    public TextEditorScreen setTextValidatorUserFeedback(@Nullable Tooltip feedback) {
        this.textValidatorFeedbackTooltip = feedback;
        return this;
    }

    public boolean placeholdersAllowed() {
        return this.allowPlaceholders;
    }

    public TextEditorScreen setPlaceholdersAllowed(boolean allowed) {
        this.allowPlaceholders = allowed;
        this.init();
        return this;
    }

    public boolean isMultilineMode() {
        return this.multilineMode;
    }

    public TextEditorScreen setMultilineMode(boolean multilineMode) {
        this.multilineMode = multilineMode;
        return this;
    }

    public boolean isBoldTitle() {
        return this.boldTitle;
    }

    public TextEditorScreen setBoldTitle(boolean boldTitle) {
        this.boldTitle = boldTitle;
        return this;
    }

    @Nullable
    public String getTextBeforeCursor() {
        if (!this.isLineFocused()) {
            return null;
        } else {
            int focusedLineIndex = this.getFocusedLineIndex();
            List<TextEditorLine> lines = new ArrayList();
            if (focusedLineIndex == 0) {
                lines.add(this.getLine(0));
            } else if (focusedLineIndex > 0) {
                lines.addAll(this.textFieldLines.subList(0, focusedLineIndex + 1));
            }
            TextEditorLine lastLine = (TextEditorLine) lines.get(lines.size() - 1);
            StringBuilder s = new StringBuilder();
            boolean b = false;
            for (TextEditorLine t : lines) {
                if (b) {
                    s.append("\n");
                }
                if (t != lastLine) {
                    s.append(t.m_94155_());
                } else {
                    s.append(t.m_94155_().substring(0, t.m_94207_()));
                }
                b = true;
            }
            return s.toString();
        }
    }

    @Nullable
    public String getTextAfterCursor() {
        if (!this.isLineFocused()) {
            return null;
        } else {
            int focusedLineIndex = this.getFocusedLineIndex();
            List<TextEditorLine> lines = new ArrayList();
            if (focusedLineIndex == this.getLineCount() - 1) {
                lines.add(this.getLine(this.getLineCount() - 1));
            } else if (focusedLineIndex < this.getLineCount() - 1) {
                lines.addAll(this.textFieldLines.subList(focusedLineIndex, this.getLineCount()));
            }
            TextEditorLine firstLine = (TextEditorLine) lines.get(0);
            StringBuilder s = new StringBuilder();
            boolean b = false;
            for (TextEditorLine t : lines) {
                if (b) {
                    s.append("\n");
                }
                if (t != firstLine) {
                    s.append(t.m_94155_());
                } else {
                    s.append(t.m_94155_().substring(t.m_94207_(), t.m_94155_().length()));
                }
                b = true;
            }
            return s.toString();
        }
    }

    @Override
    public boolean charTyped(char character, int modifiers) {
        if (this.isLineFocused()) {
            this.history.saveSnapshot();
        }
        for (TextEditorLine l : this.textFieldLines) {
            l.m_5534_(character, modifiers);
        }
        return super.m_5534_(character, modifiers);
    }

    @Override
    public boolean keyPressed(int keycode, int scancode, int modifiers) {
        for (TextEditorLine l : new ArrayList(this.textFieldLines)) {
            l.keyPressed(keycode, scancode, modifiers);
        }
        String key = GLFW.glfwGetKeyName(keycode, scancode);
        if (key == null) {
            key = "";
        }
        if (Screen.hasControlDown() && key.equals("z")) {
            this.history.stepBack();
            return true;
        } else if (Screen.hasControlDown() && key.equals("y")) {
            this.history.stepForward();
            return true;
        } else if (keycode == 257) {
            if (!this.isInMouseHighlightingMode() && this.multilineMode && this.isLineFocused()) {
                this.history.saveSnapshot();
                this.resetHighlighting();
                this.goDownLine(true);
                this.correctYScroll(1);
            }
            if (!this.multilineMode) {
                this.multilineNotSupportedNotificationDisplayStart = System.currentTimeMillis();
            }
            return true;
        } else if (keycode == 265) {
            if (!this.isInMouseHighlightingMode()) {
                this.resetHighlighting();
                this.goUpLine();
                this.correctYScroll(0);
            }
            return true;
        } else if (keycode == 264) {
            if (!this.isInMouseHighlightingMode()) {
                this.resetHighlighting();
                this.goDownLine(false);
                this.correctYScroll(0);
            }
            return true;
        } else if (keycode == 259) {
            if (!this.isInMouseHighlightingMode()) {
                if (this.isTextHighlighted()) {
                    this.history.saveSnapshot();
                    this.deleteHighlightedText();
                } else if (this.isLineFocused()) {
                    if (!this.getText().isEmpty()) {
                        this.history.saveSnapshot();
                    }
                    TextEditorLine focused = this.getFocusedLine();
                    focused.getAsAccessor().setShiftPressedFancyMenu(false);
                    focused.getAsAccessor().invokeDeleteTextFancyMenu(-1);
                    focused.getAsAccessor().setShiftPressedFancyMenu(Screen.hasShiftDown());
                }
                this.resetHighlighting();
            }
            return true;
        } else if (Screen.isCopy(keycode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlightedText());
            return true;
        } else if (Screen.isPaste(keycode)) {
            this.history.saveSnapshot();
            this.pasteText(Minecraft.getInstance().keyboardHandler.getClipboard());
            return true;
        } else if (!Screen.isSelectAll(keycode)) {
            if (Screen.isCut(keycode)) {
                this.history.saveSnapshot();
                Minecraft.getInstance().keyboardHandler.setClipboard(this.cutHighlightedText());
                this.resetHighlighting();
                return true;
            } else if (keycode != 262 && keycode != 263) {
                return super.keyPressed(keycode, scancode, modifiers);
            } else {
                this.resetHighlighting();
                return true;
            }
        } else {
            for (TextEditorLine t : new ArrayList(this.textFieldLines)) {
                t.m_94208_(0);
                t.setCursorPosition(t.m_94155_().length());
            }
            this.setFocusedLine(this.getLineCount() - 1);
            this.startHighlightLineIndex = 0;
            this.endHighlightLineIndex = this.getLineCount() - 1;
            return true;
        }
    }

    @Override
    public boolean keyReleased(int i1, int i2, int i3) {
        for (TextEditorLine l : this.textFieldLines) {
            l.m_7920_(i1, i2, i3);
        }
        return super.m_7920_(i1, i2, i3);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.m_7522_(null);
        if (super.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else {
            this.selectedHoveredOnRightClickMenuOpen = false;
            if (!this.isMouseInteractingWithGrabbers()) {
                for (TextEditorLine l : this.textFieldLines) {
                    l.mouseClicked(mouseX, mouseY, button);
                }
                if (this.isMouseInsideEditorArea()) {
                    if (button == 1) {
                        this.rightClickContextMenu.closeMenu();
                    }
                    if (button == 0 || button == 1) {
                        boolean isHighlightedHovered = this.isHighlightedTextHovered();
                        TextEditorLine hoveredLine = this.getHoveredLine();
                        if (!this.rightClickContextMenu.isOpen()) {
                            if (button == 0 || !isHighlightedHovered) {
                                this.resetHighlighting();
                            }
                            if (hoveredLine != null) {
                                if (button == 1 && !isHighlightedHovered) {
                                    this.setFocusedLine(this.getLineIndex(hoveredLine));
                                    String s = this.f_96547_.plainSubstrByWidth(hoveredLine.m_94155_().substring(hoveredLine.getAsAccessor().getDisplayPosFancyMenu()), hoveredLine.m_94210_());
                                    hoveredLine.m_94192_(this.f_96547_.plainSubstrByWidth(s, MouseInput.getMouseX() - hoveredLine.m_252754_()).length() + hoveredLine.getAsAccessor().getDisplayPosFancyMenu());
                                }
                            } else {
                                TextEditorLine focus = this.getLine(this.getLineCount() - 1);
                                for (TextEditorLine t : this.textFieldLines) {
                                    if (MouseInput.getMouseY() >= t.m_252907_() && MouseInput.getMouseY() <= t.m_252907_() + t.m_93694_()) {
                                        focus = t;
                                        break;
                                    }
                                }
                                this.setFocusedLine(this.getLineIndex(focus));
                                this.getFocusedLine().m_94201_();
                                this.correctYScroll(0);
                            }
                        }
                        if (button == 1) {
                            this.selectedHoveredOnRightClickMenuOpen = this.isHighlightedTextHovered();
                            this.rightClickContextMenu.openMenuAtMouse();
                        } else if (this.rightClickContextMenu.isOpen() && !this.rightClickContextMenu.isHovered()) {
                            this.rightClickContextMenu.closeMenu();
                            this.textFieldLines.forEach(line -> line.mouseClicked(mouseX, mouseY, button));
                            this.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                }
            }
            for (TextEditorScreen.PlaceholderMenuEntry e : new ArrayList(this.placeholderMenuEntries)) {
                e.buttonBase.m_6375_(mouseX, mouseY, button);
            }
            return false;
        }
    }

    @Override
    public void tick() {
        for (TextEditorLine l : this.textFieldLines) {
            l.tick();
        }
        super.tick();
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    public boolean isMouseInteractingWithGrabbers() {
        return this.verticalScrollBar.isGrabberGrabbed() || this.verticalScrollBar.isGrabberHovered() || this.horizontalScrollBar.isGrabberGrabbed() || this.horizontalScrollBar.isGrabberHovered();
    }

    public boolean isMouseInteractingWithPlaceholderGrabbers() {
        return this.verticalScrollBarPlaceholderMenu.isGrabberGrabbed() || this.verticalScrollBarPlaceholderMenu.isGrabberHovered() || this.horizontalScrollBarPlaceholderMenu.isGrabberGrabbed() || this.horizontalScrollBarPlaceholderMenu.isGrabberHovered();
    }

    public int getEditBoxCursorX(EditBox editBox) {
        try {
            IMixinEditBox b = (IMixinEditBox) editBox;
            String s = this.f_96547_.plainSubstrByWidth(editBox.getValue().substring(b.getDisplayPosFancyMenu()), editBox.getInnerWidth());
            int j = editBox.getCursorPosition() - b.getDisplayPosFancyMenu();
            boolean flag = j >= 0 && j <= s.length();
            boolean flag2 = editBox.getCursorPosition() < editBox.getValue().length() || editBox.getValue().length() >= b.getMaxLengthFancyMenu();
            int l = b.getBorderedFancyMenu() ? editBox.m_252754_() + 4 : editBox.m_252754_();
            int j1 = l;
            if (!s.isEmpty()) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = l + this.f_96547_.width((FormattedCharSequence) b.getFormatterFancyMenu().apply(s1, b.getDisplayPosFancyMenu()));
            }
            int k1 = j1;
            if (!flag) {
                k1 = j > 0 ? l + editBox.m_5711_() : l;
            } else if (flag2) {
                k1 = j1 - 1;
                j1--;
            }
            return k1;
        } catch (Exception var10) {
            var10.printStackTrace();
            return 0;
        }
    }

    public void scrollToLine(int lineIndex, boolean bottom) {
        if (bottom) {
            this.scrollToLine(lineIndex, -Math.max(0, this.getEditorAreaHeight() - this.lineHeight));
        } else {
            this.scrollToLine(lineIndex, 0);
        }
    }

    public void scrollToLine(int lineIndex, int offset) {
        int totalLineHeight = this.getTotalScrollHeight();
        float f = (float) Math.max(0, (lineIndex + 1) * this.lineHeight - this.lineHeight) / (float) totalLineHeight;
        if (offset != 0) {
            if (offset > 0) {
                f += (float) offset / (float) totalLineHeight;
            } else {
                f -= (float) Math.abs(offset) / (float) totalLineHeight;
            }
        }
        this.verticalScrollBar.setScroll(f);
    }

    public int getTotalScrollHeight() {
        return this.overriddenTotalScrollHeight != -1 ? this.overriddenTotalScrollHeight : this.getTotalLineHeight();
    }

    public int getTotalScrollWidth() {
        return this.currentLineWidth;
    }

    public void correctYScroll(int lineCountOffsetAfterRemovingAdding) {
        if (!this.isInMouseHighlightingMode() && this.isLineFocused()) {
            int minY = this.getEditorAreaY();
            int maxY = this.getEditorAreaY() + this.getEditorAreaHeight();
            int currentLineY = this.getFocusedLine().m_252907_();
            if (currentLineY < minY) {
                this.scrollToLine(this.getFocusedLineIndex(), false);
            } else if (currentLineY + this.lineHeight > maxY) {
                this.scrollToLine(this.getFocusedLineIndex(), true);
            } else if (lineCountOffsetAfterRemovingAdding != 0) {
                this.overriddenTotalScrollHeight = -1;
                int removedAddedLineCount = Math.abs(lineCountOffsetAfterRemovingAdding);
                if (lineCountOffsetAfterRemovingAdding > 0) {
                    this.overriddenTotalScrollHeight = this.getTotalScrollHeight() - this.lineHeight * removedAddedLineCount;
                } else if (lineCountOffsetAfterRemovingAdding < 0) {
                    this.overriddenTotalScrollHeight = this.getTotalScrollHeight() + this.lineHeight * removedAddedLineCount;
                }
                this.updateLines(null);
                this.overriddenTotalScrollHeight = -1;
                int diffToTop = Math.max(0, this.getFocusedLine().m_252907_() - this.getEditorAreaY());
                this.scrollToLine(this.getFocusedLineIndex(), -diffToTop);
                this.correctYScroll(0);
            }
            if (this.getTotalLineHeight() <= this.getEditorAreaHeight()) {
                this.verticalScrollBar.setScroll(0.0F);
            }
        }
    }

    public void correctXScroll(TextEditorLine line) {
        if (!this.isInMouseHighlightingMode()) {
            if (this.isLineFocused() && this.getFocusedLine() == line) {
                int oldX = line.m_252754_();
                this.updateCurrentLineWidth();
                this.updateLines(null);
                int newX = line.m_252754_();
                String oldValue = line.lastTickValue;
                String newValue = line.m_94155_();
                int cursorWidth = 2;
                if (line.m_94207_() >= newValue.length()) {
                    cursorWidth = 6;
                }
                int editorAreaCenterX = this.getEditorAreaX() + this.getEditorAreaWidth() / 2;
                int cursorX = this.getEditBoxCursorX(line);
                if (cursorX > editorAreaCenterX) {
                    cursorX += cursorWidth + 5;
                } else if (cursorX < editorAreaCenterX) {
                    cursorX -= cursorWidth + 5;
                }
                int maxToRight = this.getEditorAreaX() + this.getEditorAreaWidth();
                int maxToLeft = this.getEditorAreaX();
                float currentScrollX = this.horizontalScrollBar.getScroll();
                int currentLineW = this.getTotalScrollWidth();
                boolean textGotDeleted = oldValue.length() > newValue.length();
                boolean textGotAdded = oldValue.length() < newValue.length();
                if (cursorX > maxToRight) {
                    float f = (float) (cursorX - maxToRight) / (float) currentLineW;
                    this.horizontalScrollBar.setScroll(currentScrollX + f);
                } else if (cursorX < maxToLeft) {
                    float f = (float) (maxToLeft - cursorX) / (float) currentLineW;
                    if (textGotDeleted) {
                        f = (float) (maxToRight - maxToLeft) / (float) currentLineW;
                    }
                    this.horizontalScrollBar.setScroll(currentScrollX - f);
                } else if (textGotDeleted && oldX < newX) {
                    float f = (float) (newX - oldX) / (float) currentLineW;
                    this.horizontalScrollBar.setScroll(currentScrollX + f);
                } else if (textGotAdded && oldX > newX) {
                    float f = (float) (oldX - newX) / (float) currentLineW;
                    this.horizontalScrollBar.setScroll(currentScrollX - f);
                }
                if (line.m_94207_() == 0) {
                    this.horizontalScrollBar.setScroll(0.0F);
                }
            }
        }
    }

    public boolean isMouseInsideEditorArea() {
        int xStart = this.borderLeft;
        int yStart = this.headerHeight;
        int xEnd = this.getEditorAreaX() + this.getEditorAreaWidth();
        int yEnd = this.f_96544_ - this.footerHeight;
        int mX = MouseInput.getMouseX();
        int mY = MouseInput.getMouseY();
        return mX >= xStart && mX <= xEnd && mY >= yStart && mY <= yEnd;
    }

    public int getEditorAreaWidth() {
        int i = this.f_96543_ - this.borderRight - this.borderLeft;
        if (extendedPlaceholderMenu) {
            i = i - this.placeholderMenuWidth - 15;
        }
        return i;
    }

    public int getEditorAreaHeight() {
        return this.f_96544_ - this.footerHeight - this.headerHeight;
    }

    public int getEditorAreaX() {
        return this.borderLeft;
    }

    public int getEditorAreaY() {
        return this.headerHeight;
    }

    public class PlaceholderMenuEntry extends UIBase {

        public TextEditorScreen parent;

        public final Component label;

        public Runnable clickAction;

        public int x;

        public int y;

        public final int labelWidth;

        protected Color backgroundColorIdle = Color.GRAY;

        protected Color backgroundColorHover = Color.LIGHT_GRAY;

        protected Color dotColor = Color.BLUE;

        protected Color entryLabelColor = Color.WHITE;

        public ExtendedButton buttonBase;

        public Font font;

        public PlaceholderMenuEntry(@NotNull TextEditorScreen parent, @NotNull Component label, @NotNull Runnable clickAction) {
            this.font = Minecraft.getInstance().font;
            this.parent = parent;
            this.label = label;
            this.clickAction = clickAction;
            this.labelWidth = this.font.width(this.label);
            this.buttonBase = new ExtendedButton(0, 0, this.getWidth(), this.getHeight(), "", button -> this.clickAction.run()) {

                @Override
                public boolean isHoveredOrFocused() {
                    return PlaceholderMenuEntry.this.parent.isMouseInteractingWithPlaceholderGrabbers() ? false : super.m_198029_();
                }

                @Override
                public void onClick(double p_93371_, double p_93372_) {
                    if (!PlaceholderMenuEntry.this.parent.isMouseInteractingWithPlaceholderGrabbers()) {
                        super.m_5716_(p_93371_, p_93372_);
                    }
                }

                @Override
                public void render(@NotNull GuiGraphics graphics, int p_93658_, int p_93659_, float p_93660_) {
                    if (PlaceholderMenuEntry.this.parent.isMouseInteractingWithPlaceholderGrabbers()) {
                        this.f_93622_ = false;
                    }
                    super.render(graphics, p_93658_, p_93659_, p_93660_);
                }
            };
        }

        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
            this.buttonBase.setBackgroundColor(DrawableColor.of(this.backgroundColorIdle), DrawableColor.of(this.backgroundColorHover), DrawableColor.of(this.backgroundColorIdle), DrawableColor.of(this.backgroundColorIdle), DrawableColor.of(this.backgroundColorHover), DrawableColor.of(this.backgroundColorIdle));
            this.buttonBase.m_252865_(this.x);
            this.buttonBase.m_253211_(this.y);
            int yCenter = this.y + this.getHeight() / 2;
            this.buttonBase.render(graphics, mouseX, mouseY, partial);
            renderListingDot(graphics, this.x + 5, yCenter - 2, this.dotColor);
            graphics.drawString(this.font, this.label, this.x + 5 + 4 + 3, yCenter - 9 / 2, this.entryLabelColor.getRGB(), false);
        }

        public int getWidth() {
            return Math.max(this.parent.placeholderMenuWidth, 12 + this.labelWidth + 5);
        }

        public int getHeight() {
            return this.parent.placeholderMenuEntryHeight;
        }

        public boolean isHovered() {
            return this.buttonBase.m_198029_();
        }

        public void setDescription(String... desc) {
            this.buttonBase.setTooltip(Tooltip.of(desc).setDefaultStyle());
        }
    }
}