package dev.ftb.mods.ftblibrary.ui.misc;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.ThemeManager;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;

@Deprecated
public abstract class ButtonListBaseScreen extends BaseScreen {

    private final Panel buttonPanel;

    private final PanelScrollBar scrollBar;

    private Component title = Component.empty();

    private final TextBox searchBox;

    private boolean hasSearchBox;

    private int borderH;

    private int borderV;

    private int borderW;

    private static final int SCROLLBAR_WIDTH = 16;

    private static final int GUTTER_SIZE = 6;

    @Deprecated
    public ButtonListBaseScreen() {
        this.buttonPanel = new ButtonListBaseScreen.ButtonPanel();
        this.scrollBar = new PanelScrollBar(this, this.buttonPanel);
        this.scrollBar.setCanAlwaysScroll(true);
        this.scrollBar.setScrollStep(20.0);
        this.searchBox = new TextBox(this) {

            @Override
            public void onTextChanged() {
                ButtonListBaseScreen.this.buttonPanel.refreshWidgets();
            }
        };
        this.searchBox.ghostText = I18n.get("gui.search_box");
        this.hasSearchBox = false;
    }

    public void setHasSearchBox(boolean newVal) {
        if (this.hasSearchBox != newVal) {
            this.hasSearchBox = newVal;
            this.refreshWidgets();
        }
    }

    public String getFilterText(Widget widget) {
        return widget.getTitle().getString().toLowerCase();
    }

    @Override
    public void addWidgets() {
        this.add(this.buttonPanel);
        this.add(this.scrollBar);
        if (this.hasSearchBox) {
            this.add(this.searchBox);
        }
    }

    @Override
    public void alignWidgets() {
        int buttonPanelWidth = this.getGui().width - 18 - 16;
        if (this.hasSearchBox) {
            this.searchBox.setPosAndSize(6, 6, this.getGui().width - 12, this.getTheme().getFontHeight() + 2);
            this.buttonPanel.setPosAndSize(6, 12 + this.searchBox.getHeight(), buttonPanelWidth, this.getGui().height - this.searchBox.height - 18);
        } else {
            this.buttonPanel.setPosAndSize(6, 6, buttonPanelWidth, this.getGui().height - 12);
        }
        this.buttonPanel.alignWidgets();
        this.scrollBar.setPosAndSize(this.getGui().width - 6 - 16, this.buttonPanel.getPosY(), 16, this.buttonPanel.getHeight());
    }

    public abstract void addButtons(Panel var1);

    public void setTitle(Component txt) {
        this.title = txt;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    public void setBorder(int h, int v, int w) {
        this.borderH = h;
        this.borderV = v;
        this.borderW = w;
    }

    @Override
    public Theme getTheme() {
        return ThemeManager.INSTANCE.getActiveTheme();
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawBackground(graphics, theme, x, y, w, h);
        Component title = this.getTitle();
        if (title.getContents() != ComponentContents.EMPTY) {
            theme.drawString(graphics, title, x + (this.width - theme.getStringWidth(title)) / 2, y - theme.getFontHeight() - 2, 2);
        }
    }

    public void focus() {
        this.searchBox.setFocused(true);
    }

    private class ButtonPanel extends Panel {

        public ButtonPanel() {
            super(ButtonListBaseScreen.this);
        }

        @Override
        public void add(Widget widget) {
            if (!ButtonListBaseScreen.this.hasSearchBox || ButtonListBaseScreen.this.searchBox.getText().isEmpty() || ButtonListBaseScreen.this.getFilterText(widget).contains(ButtonListBaseScreen.this.searchBox.getText().toLowerCase())) {
                super.add(widget);
            }
        }

        @Override
        public void addWidgets() {
            ButtonListBaseScreen.this.addButtons(this);
        }

        @Override
        public void alignWidgets() {
            this.align(new WidgetLayout.Vertical(ButtonListBaseScreen.this.borderV, ButtonListBaseScreen.this.borderW, ButtonListBaseScreen.this.borderV));
            this.widgets.forEach(w -> {
                w.setX(ButtonListBaseScreen.this.borderH);
                w.setWidth(this.width - ButtonListBaseScreen.this.borderH * 2);
            });
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawPanelBackground(graphics, x, y, w, h);
        }
    }
}