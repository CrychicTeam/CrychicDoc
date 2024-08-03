package dev.ftb.mods.ftblibrary.ui.misc;

import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.WidgetLayout;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public abstract class AbstractButtonListScreen extends AbstractThreePanelScreen<AbstractButtonListScreen.ButtonPanel> {

    private Component title = Component.empty();

    private final TextBox searchBox;

    private final TextField titleField;

    private boolean hasSearchBox;

    private int borderH;

    private int borderV;

    private int borderW;

    private static final int SCROLLBAR_WIDTH = 16;

    private static final int GUTTER_SIZE = 5;

    public AbstractButtonListScreen() {
        this.scrollBar.setCanAlwaysScroll(true);
        this.scrollBar.setScrollStep(20.0);
        this.titleField = new TextField(this.topPanel);
        this.searchBox = new TextBox(this.topPanel) {

            @Override
            public void onTextChanged() {
                AbstractButtonListScreen.this.mainPanel.refreshWidgets();
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
    protected int getTopPanelHeight() {
        return this.hasSearchBox ? 15 + this.titleField.getHeight() + this.searchBox.getHeight() : 10 + this.titleField.getHeight();
    }

    @Override
    protected Panel createTopPanel() {
        return new AbstractButtonListScreen.ButtonListTopPanel();
    }

    protected AbstractButtonListScreen.ButtonPanel createMainPanel() {
        return new AbstractButtonListScreen.ButtonPanel();
    }

    @Override
    protected Pair<Integer, Integer> mainPanelInset() {
        return Pair.of(2, 2);
    }

    public abstract void addButtons(Panel var1);

    public void setTitle(Component txt) {
        this.title = txt;
        this.titleField.setText(txt);
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

    public void focus() {
        this.searchBox.setFocused(true);
    }

    protected class ButtonListTopPanel extends AbstractThreePanelScreen<AbstractButtonListScreen.ButtonPanel>.TopPanel {

        @Override
        public void addWidgets() {
            super.addWidgets();
            this.add(AbstractButtonListScreen.this.titleField);
            if (AbstractButtonListScreen.this.hasSearchBox) {
                this.add(AbstractButtonListScreen.this.searchBox);
            }
        }

        @Override
        public void alignWidgets() {
            super.alignWidgets();
            AbstractButtonListScreen.this.titleField.setPosAndSize(5, 5, this.parent.width - 10, AbstractButtonListScreen.this.titleField.getHeight());
            if (AbstractButtonListScreen.this.hasSearchBox) {
                AbstractButtonListScreen.this.searchBox.setPosAndSize(5, AbstractButtonListScreen.this.titleField.getPosY() + AbstractButtonListScreen.this.titleField.getHeight() + 5, this.parent.width - 10, AbstractButtonListScreen.this.getTheme().getFontHeight() + 6);
            }
        }
    }

    protected class ButtonPanel extends Panel {

        public ButtonPanel() {
            super(AbstractButtonListScreen.this);
        }

        @Override
        public void add(Widget widget) {
            if (!AbstractButtonListScreen.this.hasSearchBox || AbstractButtonListScreen.this.searchBox.getText().isEmpty() || AbstractButtonListScreen.this.getFilterText(widget).contains(AbstractButtonListScreen.this.searchBox.getText().toLowerCase())) {
                super.add(widget);
            }
        }

        @Override
        public void addWidgets() {
            AbstractButtonListScreen.this.addButtons(this);
        }

        @Override
        public void alignWidgets() {
            this.align(new WidgetLayout.Vertical(AbstractButtonListScreen.this.borderV, AbstractButtonListScreen.this.borderW, AbstractButtonListScreen.this.borderV));
            this.widgets.forEach(w -> {
                w.setX(AbstractButtonListScreen.this.borderH);
                w.setWidth(this.width - AbstractButtonListScreen.this.borderH * 2);
            });
        }
    }
}