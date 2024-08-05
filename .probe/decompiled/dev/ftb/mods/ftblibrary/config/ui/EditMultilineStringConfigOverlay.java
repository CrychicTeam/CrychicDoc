package dev.ftb.mods.ftblibrary.config.ui;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.StringConfig;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ModalPanel;
import dev.ftb.mods.ftblibrary.ui.MultilineTextBox;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.PanelScrollBar;
import dev.ftb.mods.ftblibrary.ui.ScrollBar;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class EditMultilineStringConfigOverlay extends ModalPanel {

    private final MultilineTextBox textBox;

    private final Button accept;

    private final Button cancel;

    private final StringConfig config;

    private final ConfigCallback callback;

    private final PanelScrollBar scrollBar;

    private final Panel textBoxPanel;

    private String currentValue;

    public EditMultilineStringConfigOverlay(Panel panel, StringConfig config, ConfigCallback callback) {
        super(panel);
        this.config = config;
        this.callback = callback;
        this.currentValue = config.getValue() == null ? null : config.copy(config.getValue());
        this.textBoxPanel = new EditMultilineStringConfigOverlay.TextBoxPanel();
        this.textBox = new MultilineTextBox(this.textBoxPanel);
        this.textBox.setText(this.currentValue);
        this.textBox.setValueListener(s -> this.currentValue = s);
        this.textBox.setFocused(true);
        this.scrollBar = new PanelScrollBar(this, ScrollBar.Plane.VERTICAL, this.textBoxPanel);
        this.accept = new SimpleButton(this, Component.translatable("gui.accept"), Icons.ACCEPT, this::onAccepted);
        this.cancel = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL, this::onCancelled);
        this.height = (this.getGui().getTheme().getFontHeight() + 1) * 4;
    }

    @Override
    public void addWidgets() {
        this.add(this.textBoxPanel);
        this.add(this.scrollBar);
        this.add(this.accept);
        this.add(this.cancel);
    }

    @Override
    public void alignWidgets() {
        this.textBoxPanel.setPosAndSize(2, 2, this.width - 20, this.height - 4);
        this.textBoxPanel.addWidgets();
        this.textBoxPanel.alignWidgets();
        this.scrollBar.setPosAndSize(this.width - 30, 0, 12, this.height);
        this.accept.setPosAndSize(this.width - 18, 2, 16, 16);
        this.cancel.setPosAndSize(this.width - 18, 18, 16, 16);
    }

    @Override
    public boolean keyPressed(Key key) {
        if (key.esc()) {
            this.onCancelled(this.cancel, MouseButton.LEFT);
            return true;
        } else {
            return super.keyPressed(key);
        }
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawWidget(graphics, x, y, w, h, WidgetType.NORMAL);
    }

    private void onAccepted(Button btn, MouseButton mb) {
        this.config.setCurrentValue(this.currentValue);
        this.callback.save(true);
        this.getGui().popModalPanel();
    }

    private void onCancelled(Button btn, MouseButton mb) {
        this.callback.save(false);
        this.getGui().popModalPanel();
    }

    private class TextBoxPanel extends Panel {

        public TextBoxPanel() {
            super(EditMultilineStringConfigOverlay.this);
        }

        @Override
        public void addWidgets() {
            this.add(EditMultilineStringConfigOverlay.this.textBox);
        }

        @Override
        public void alignWidgets() {
            EditMultilineStringConfigOverlay.this.textBox.setSize(this.width, this.height);
            this.setScrollY(0.0);
        }

        @Override
        public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
            theme.drawSlot(graphics, x, y, w, h, WidgetType.NORMAL);
        }
    }
}