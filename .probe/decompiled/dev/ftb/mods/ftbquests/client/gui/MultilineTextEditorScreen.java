package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.ImageResourceConfig;
import dev.ftb.mods.ftblibrary.config.ListConfig;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.config.ui.EditConfigScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ContextMenu;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftblibrary.ui.MultilineTextBox;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.ScrollBar;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.KeyModifiers;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.NordColors;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftblibrary.util.client.ImageComponent;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.client.gui.quests.ViewQuestPanel;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectType;
import dev.ftb.mods.ftbquests.util.ConfigQuestObject;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Whence;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class MultilineTextEditorScreen extends BaseScreen {

    private static final Pattern STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");

    private static final int MAX_UNDO = 10;

    protected static final String LINK_TEXT_TEMPLATE = "{ \"text\": \"%s\", \"underlined\": \"true\", \"clickEvent\": { \"action\": \"change_page\", \"value\": \"%016X\" } }";

    private final Component title;

    private final ListConfig<String, StringConfig> config;

    private final ConfigCallback callback;

    private final Panel outerPanel;

    private final Panel toolbarPanel;

    private final Panel textBoxPanel;

    private final MultilineTextBox textBox;

    private final PanelScrollBar scrollBar;

    private long ticksOpen = 0L;

    private final String initialText;

    private long lastChange = 0L;

    private final Deque<MultilineTextEditorScreen.HistoryElement> redoStack = new ArrayDeque();

    private final Map<Integer, Runnable> hotKeys = Map.of(66, (Runnable) () -> this.insertFormatting(ChatFormatting.BOLD), 73, (Runnable) () -> this.insertFormatting(ChatFormatting.ITALIC), 85, (Runnable) () -> this.insertFormatting(ChatFormatting.UNDERLINE), 83, (Runnable) () -> this.insertFormatting(ChatFormatting.STRIKETHROUGH), 82, this::resetFormatting, 80, (Runnable) () -> this.insertAtEndOfLine("\n{@pagebreak}"), 77, this::openImageSelector, 90, this::undoLast, 76, this::openLinkInsert);

    public MultilineTextEditorScreen(Component title, ListConfig<String, StringConfig> config, ConfigCallback callback) {
        this.title = title;
        this.config = config;
        this.callback = callback;
        this.outerPanel = new MultilineTextEditorScreen.OuterPanel(this);
        this.toolbarPanel = new MultilineTextEditorScreen.ToolbarPanel(this.outerPanel);
        this.textBoxPanel = new MultilineTextEditorScreen.TextBoxPanel(this.outerPanel);
        this.textBox = new MultilineTextBox(this.textBoxPanel);
        this.textBox.setText(String.join("\n", (Iterable) config.getValue()));
        this.textBox.setFocused(true);
        this.textBox.setValueListener(this::onValueChanged);
        this.textBox.seekCursor(Whence.ABSOLUTE, 0);
        this.redoStack.addLast(new MultilineTextEditorScreen.HistoryElement(this.textBox.getText(), this.textBox.cursorPos()));
        this.scrollBar = new PanelScrollBar(this.outerPanel, ScrollBar.Plane.VERTICAL, this.textBoxPanel);
        this.scrollBar.setScrollStep((double) this.getTheme().getFontHeight());
        this.initialText = this.textBox.getText();
    }

    private void onValueChanged(String newValue) {
        this.lastChange = Minecraft.getInstance().level.m_46467_();
    }

    @Override
    public void tick() {
        super.tick();
        this.ticksOpen++;
        if (this.lastChange > 0L && Minecraft.getInstance().level.m_46467_() - this.lastChange > 5L) {
            this.redoStack.addLast(new MultilineTextEditorScreen.HistoryElement(this.textBox.getText(), this.textBox.cursorPos()));
            while (this.redoStack.size() > 10) {
                this.redoStack.removeFirst();
            }
            this.lastChange = 0L;
        }
    }

    @Override
    public boolean onInit() {
        this.setWidth(this.getScreen().getGuiScaledWidth() / 5 * 4);
        this.setHeight(this.getScreen().getGuiScaledHeight() / 5 * 4);
        this.ticksOpen = 0L;
        return true;
    }

    @Override
    public void addWidgets() {
        this.add(this.outerPanel);
    }

    @Override
    public void alignWidgets() {
        this.outerPanel.setPosAndSize(0, 0, this.width, this.height);
        this.toolbarPanel.setPosAndSize(2, 2, this.width - 4, 18);
        this.toolbarPanel.alignWidgets();
        this.textBoxPanel.setPosAndSize(2, this.toolbarPanel.height + 4, this.width - 18, this.height - this.toolbarPanel.height - 6);
        this.textBoxPanel.alignWidgets();
        this.scrollBar.setPosAndSize(this.width - 14, this.textBoxPanel.posY, 12, this.height - this.textBoxPanel.posY - 4);
    }

    @Override
    public void drawBackground(GuiGraphics matrixStack, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(matrixStack, theme, x, y, w, h);
        theme.drawString(matrixStack, this.title, x + (this.width - theme.getStringWidth(this.title)) / 2, y - theme.getFontHeight() - 2, 2);
    }

    @Override
    public boolean keyPressed(Key key) {
        if (key.esc()) {
            this.cancel();
            return true;
        } else if (key.enter() && Screen.hasShiftDown()) {
            this.saveAndExit();
            return true;
        } else if (this.textBox.isFocused()) {
            this.textBox.keyPressed(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void keyReleased(Key key) {
        this.executeHotkey(key.keyCode, true);
    }

    private void executeHotkey(int keycode, boolean checkModifier) {
        if (this.hotKeys.containsKey(keycode) && (!checkModifier || isHotKeyModifierPressed(keycode))) {
            ((Runnable) this.hotKeys.get(keycode)).run();
            this.textBox.setFocused(true);
        }
    }

    @Override
    public boolean charTyped(char c, KeyModifiers modifiers) {
        if (this.ticksOpen < 2L) {
            return true;
        } else {
            int keyCode = Character.toUpperCase(c);
            return isHotKeyModifierPressed(keyCode) && this.hotKeys.containsKey(keyCode) ? false : super.charTyped(c, modifiers);
        }
    }

    private static boolean isHotKeyModifierPressed(int keycode) {
        return keycode == 90 ? Screen.hasControlDown() : Screen.hasAltDown();
    }

    @Override
    public Theme getTheme() {
        return FTBQuestsTheme.INSTANCE;
    }

    private void openLinkInsert() {
        ConfigQuestObject<QuestObject> config = new ConfigQuestObject<>(QuestObjectType.QUEST.or(QuestObjectType.QUEST_LINK));
        new SelectQuestObjectScreen<>(config, accepted -> {
            int pos = this.textBox.cursorPos();
            if (accepted) {
                this.doLinkInsertion(config.getValue().id);
            }
            this.run();
            this.textBox.seekCursor(Whence.ABSOLUTE, pos);
        }).openGui();
    }

    private void doLinkInsertion(long questID) {
        if (this.textBox.hasSelection()) {
            String text = this.textBox.getSelectedText();
            if (!text.contains("\n")) {
                MultilineTextBox.StringExtents lineExtents = this.textBox.getLineView();
                MultilineTextBox.StringExtents selectionExtents = this.textBox.getSelected();
                List<String> parts = new ArrayList();
                parts.add(this.textBox.getText().substring(lineExtents.start(), selectionExtents.start()));
                parts.add(this.textBox.getText().substring(selectionExtents.start(), selectionExtents.end()));
                parts.add(this.textBox.getText().substring(selectionExtents.end(), lineExtents.end()));
                StringBuilder builder = new StringBuilder("[ ");
                if (!((String) parts.get(0)).isEmpty()) {
                    builder.append("\"").append((String) parts.get(0)).append("\", ");
                }
                builder.append(String.format("{ \"text\": \"%s\", \"underlined\": \"true\", \"clickEvent\": { \"action\": \"change_page\", \"value\": \"%016X\" } }", parts.get(1), questID)).append(", ");
                if (!((String) parts.get(2)).isEmpty()) {
                    builder.append("\"").append((String) parts.get(2)).append("\"");
                }
                builder.append(" ]");
                this.textBox.selectCurrentLine();
                this.textBox.insertText(builder.toString());
                return;
            }
        }
        this.insertAtEndOfLine(String.format("\n{ \"text\": \"%s\", \"underlined\": \"true\", \"clickEvent\": { \"action\": \"change_page\", \"value\": \"%016X\" } }", "EDIT HERE", questID));
    }

    private void errorToPlayer(String msg, Object... args) {
        QuestScreen.displayError(Component.literal(String.format(msg, args)).withStyle(ChatFormatting.RED));
    }

    private void openImageSelector() {
        int cursor = this.textBox.cursorPos();
        ImageComponent component = new ImageComponent();
        ConfigGroup group = new ConfigGroup("ftbquests", accepted -> {
            this.openGui();
            if (accepted) {
                this.textBox.seekCursor(Whence.ABSOLUTE, cursor);
                this.insertAtEndOfLine("\n" + component);
            }
        });
        group.add("image", new ImageResourceConfig(), ImageResourceConfig.getResourceLocation(component.image), v -> component.image = Icon.getIcon(v), ImageResourceConfig.NONE);
        group.addInt("width", component.width, v -> component.width = v, 0, 1, 1000);
        group.addInt("height", component.height, v -> component.height = v, 0, 1, 1000);
        group.addInt("align", component.align, v -> component.align = v, 0, 1, 2);
        group.addBool("fit", component.fit, v -> component.fit = v, false);
        new EditConfigScreen(group).openGui();
    }

    private void cancel() {
        if (!this.textBox.getText().equals(this.initialText)) {
            this.getGui().openYesNo(Component.translatable("ftbquests.gui.confirm_esc"), Component.empty(), () -> this.callback.save(false));
        } else {
            this.callback.save(false);
        }
    }

    private void saveAndExit() {
        this.config.getValue().clear();
        Collections.addAll((Collection) this.config.getValue(), this.textBox.getText().split("\n"));
        this.closeGui();
        this.callback.save(true);
    }

    private void insertFormatting(ChatFormatting c) {
        if (this.textBox.hasSelection()) {
            this.textBox.insertText("&" + c.getChar() + this.textBox.getSelectedText() + "&r");
        } else {
            this.textBox.insertText("&" + c.getChar());
        }
        this.textBox.setFocused(true);
    }

    private void resetFormatting() {
        if (this.textBox.hasSelection()) {
            this.textBox.insertText(stripFormatting(this.textBox.getSelectedText()));
        } else {
            this.textBox.insertText("&r");
        }
    }

    private static String stripFormatting(@NotNull String selectedText) {
        return STRIP_FORMATTING_PATTERN.matcher(selectedText).replaceAll("");
    }

    private void insertAtEndOfLine(String toInsert) {
        this.textBox.keyPressed(new Key(269, -1, 0));
        this.textBox.insertText(toInsert);
    }

    private void undoLast() {
        if (this.redoStack.size() > 1) {
            this.redoStack.removeLast();
            MultilineTextEditorScreen.HistoryElement h = (MultilineTextEditorScreen.HistoryElement) this.redoStack.peekLast();
            this.textBox.setValueListener(s -> {
            });
            this.textBox.setText(((MultilineTextEditorScreen.HistoryElement) Objects.requireNonNull(h)).text());
            this.textBox.setValueListener(this::onValueChanged);
            this.textBox.setSelecting(false);
            this.textBox.seekCursor(Whence.ABSOLUTE, h.cursorPos());
        }
    }

    private static record HistoryElement(@NotNull String text, int cursorPos) {
    }

    private class OuterPanel extends Panel {

        public OuterPanel(MultilineTextEditorScreen screen) {
            super(screen);
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawPanelBackground(graphics, x, y, w, h);
        }

        @Override
        public void addWidgets() {
            this.addAll(List.of(MultilineTextEditorScreen.this.toolbarPanel, MultilineTextEditorScreen.this.textBoxPanel, MultilineTextEditorScreen.this.scrollBar));
        }

        @Override
        public void alignWidgets() {
        }
    }

    private class TextBoxPanel extends Panel {

        private int cursorPos;

        public TextBoxPanel(Panel outerPanel) {
            super(outerPanel);
        }

        @Override
        public void addWidgets() {
            this.add(MultilineTextEditorScreen.this.textBox);
        }

        @Override
        public void refreshWidgets() {
            this.cursorPos = MultilineTextEditorScreen.this.textBox.cursorPos();
            super.refreshWidgets();
        }

        @Override
        public void alignWidgets() {
            MultilineTextEditorScreen.this.textBox.setWidth(this.width - 3);
            this.setScrollY(0.0);
            MultilineTextEditorScreen.this.textBox.seekCursor(Whence.ABSOLUTE, this.cursorPos);
        }

        @Override
        public boolean mousePressed(MouseButton button) {
            boolean res = super.mousePressed(button);
            MultilineTextEditorScreen.this.textBox.setFocused(this.isMouseOver());
            return res;
        }
    }

    private static class ToolbarButton extends SimpleTextButton {

        private final Runnable onClick;

        private final List<Component> tooltip = new ArrayList();

        private boolean visible = true;

        public ToolbarButton(Panel panel, Component txt, Icon icon, Runnable onClick) {
            super(panel, txt, icon);
            this.onClick = onClick;
        }

        public ToolbarButton(Panel panel, Component txt, Runnable onClick) {
            this(panel, txt, Color4I.empty(), onClick);
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public void onClicked(MouseButton button) {
            if (this.visible) {
                this.onClick.run();
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (this.getGui().getTheme().getStringWidth(this.title) > 0) {
                super.addMouseOverText(list);
            }
            if (this.visible) {
                this.tooltip.forEach(list::add);
            }
        }

        @Override
        public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            if (this.visible) {
                super.draw(graphics, theme, x, y, w, h);
            }
        }

        public MultilineTextEditorScreen.ToolbarButton withTooltip(Component... lines) {
            this.tooltip.addAll(Arrays.asList(lines));
            return this;
        }
    }

    private class ToolbarPanel extends Panel {

        private final MultilineTextEditorScreen.ToolbarButton acceptButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.translatable("gui.accept"), Icons.ACCEPT, MultilineTextEditorScreen.this::saveAndExit).withTooltip(hotkey("Shift + Enter"));

        private final MultilineTextEditorScreen.ToolbarButton cancelButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.translatable("gui.cancel"), Icons.CANCEL, MultilineTextEditorScreen.this::cancel).withTooltip(hotkey("Escape"));

        private final MultilineTextEditorScreen.ToolbarButton boldButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("B").withStyle(ChatFormatting.BOLD), () -> MultilineTextEditorScreen.this.executeHotkey(66, false)).withTooltip(hotkey("Alt + B"));

        private final MultilineTextEditorScreen.ToolbarButton italicButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("I").withStyle(ChatFormatting.ITALIC), () -> MultilineTextEditorScreen.this.executeHotkey(73, false)).withTooltip(hotkey("Alt + I"));

        private final MultilineTextEditorScreen.ToolbarButton underlineButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("U").withStyle(ChatFormatting.UNDERLINE), () -> MultilineTextEditorScreen.this.executeHotkey(85, false)).withTooltip(hotkey("Alt + U"));

        private final MultilineTextEditorScreen.ToolbarButton strikethroughButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("S").withStyle(ChatFormatting.STRIKETHROUGH), () -> MultilineTextEditorScreen.this.executeHotkey(83, false)).withTooltip(hotkey("Alt + S"));

        private final MultilineTextEditorScreen.ToolbarButton colorButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.empty(), Icons.COLOR_RGB, this::openColorContextMenu);

        private final MultilineTextEditorScreen.ToolbarButton resetButton;

        private final MultilineTextEditorScreen.ToolbarButton pageBreakButton;

        private final MultilineTextEditorScreen.ToolbarButton imageButton;

        private final MultilineTextEditorScreen.ToolbarButton undoButton;

        private final MultilineTextEditorScreen.ToolbarButton linkButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("L"), () -> MultilineTextEditorScreen.this.executeHotkey(76, false)).withTooltip(Component.translatable("ftbquests.gui.insert_link"), hotkey("Alt + L"));

        public ToolbarPanel(Panel outerPanel) {
            super(outerPanel);
            this.resetButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.literal("r"), () -> MultilineTextEditorScreen.this.executeHotkey(82, false)).withTooltip(Component.translatable("ftbquests.gui.clear_formatting"), hotkey("Alt + R"));
            this.pageBreakButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.empty(), ViewQuestPanel.PAGEBREAK_ICON, () -> MultilineTextEditorScreen.this.executeHotkey(80, false)).withTooltip(Component.translatable("ftbquests.gui.page_break"), hotkey("Alt + P"));
            this.imageButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.empty(), Icons.ART, () -> MultilineTextEditorScreen.this.executeHotkey(77, false)).withTooltip(Component.translatable("ftbquests.chapter.image"), hotkey("Alt + M"));
            this.undoButton = new MultilineTextEditorScreen.ToolbarButton(this, Component.empty(), Icons.REFRESH, () -> MultilineTextEditorScreen.this.executeHotkey(90, false)).withTooltip(Component.translatable("ftbquests.gui.undo"), hotkey("Ctrl + Z"));
        }

        private static Component hotkey(String str) {
            return Component.literal("[" + str + "]").withStyle(ChatFormatting.DARK_GRAY);
        }

        private void openColorContextMenu() {
            List<ContextMenuItem> items = new ArrayList();
            for (ChatFormatting cf : ChatFormatting.values()) {
                if (cf.getColor() != null) {
                    items.add(new ContextMenuItem(Component.empty(), Color4I.rgb(cf.getColor()), b -> MultilineTextEditorScreen.this.insertFormatting(cf)));
                }
            }
            ContextMenu cMenu = new ContextMenu(MultilineTextEditorScreen.this, items);
            cMenu.setMaxRows(4);
            cMenu.setDrawVerticalSeparators(false);
            MultilineTextEditorScreen.this.openContextMenu(cMenu);
        }

        @Override
        public void tick() {
            this.undoButton.setVisible(MultilineTextEditorScreen.this.redoStack.size() > 1);
        }

        @Override
        public void addWidgets() {
            this.addAll(List.of(this.acceptButton, this.cancelButton, this.boldButton, this.italicButton, this.underlineButton, this.strikethroughButton, this.colorButton, this.linkButton, this.resetButton, this.pageBreakButton, this.imageButton, this.undoButton));
        }

        @Override
        public void alignWidgets() {
            this.acceptButton.setPosAndSize(1, 1, 16, 16);
            this.boldButton.setPosAndSize(27, 1, 16, 16);
            this.italicButton.setPosAndSize(43, 1, 16, 16);
            this.underlineButton.setPosAndSize(59, 1, 16, 16);
            this.strikethroughButton.setPosAndSize(75, 1, 16, 16);
            this.colorButton.setPosAndSize(91, 1, 16, 16);
            this.linkButton.setPosAndSize(107, 1, 16, 16);
            this.resetButton.setPosAndSize(123, 1, 16, 16);
            this.pageBreakButton.setPosAndSize(149, 1, 16, 16);
            this.imageButton.setPosAndSize(165, 1, 16, 16);
            this.undoButton.setPosAndSize(191, 1, 16, 16);
            this.cancelButton.setPosAndSize(this.width - 17, 1, 16, 16);
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            NordColors.POLAR_NIGHT_0.draw(graphics, x, y, w, h);
            theme.drawPanelBackground(graphics, x, y, w, h);
        }
    }
}