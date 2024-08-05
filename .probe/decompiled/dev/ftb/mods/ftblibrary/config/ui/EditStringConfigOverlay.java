package dev.ftb.mods.ftblibrary.config.ui;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ConfigFromString;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.ModalPanel;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleButton;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.TextField;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class EditStringConfigOverlay<T> extends ModalPanel {

    private final EditStringConfigOverlay<T>.EditField textBox;

    private final Button accept;

    private final Button cancel;

    private final ConfigFromString<T> config;

    private final ConfigCallback callback;

    private final TextField titleField;

    private final Component title;

    private T currentValue;

    public EditStringConfigOverlay(Panel panel, ConfigFromString<T> config, ConfigCallback callback) {
        this(panel, config, callback, null);
    }

    public EditStringConfigOverlay(Panel panel, ConfigFromString<T> config, ConfigCallback callback, @Nullable Component title) {
        super(panel);
        this.config = config;
        this.callback = callback;
        this.currentValue = config.getValue() == null ? null : config.copy(config.getValue());
        this.title = title;
        this.width = this.currentValue == null ? 100 : this.getGui().getTheme().getStringWidth(config.getStringFromValue(this.currentValue)) + 86;
        this.titleField = new TextField(this).addFlags(2).setText((Component) Objects.requireNonNullElse(title, Component.empty()));
        this.titleField.setSize(0, 0);
        this.textBox = new EditStringConfigOverlay.EditField();
        this.accept = new SimpleButton(this, Component.translatable("gui.accept"), Icons.ACCEPT, this::onAccepted);
        this.cancel = new SimpleButton(this, Component.translatable("gui.cancel"), Icons.CANCEL, this::onCancelled);
    }

    public EditStringConfigOverlay<T> atPosition(int x, int y) {
        this.setPos(x, y);
        return this;
    }

    public EditStringConfigOverlay<T> atMousePosition() {
        int absX = Math.min(this.getMouseX(), this.getScreen().getGuiScaledWidth() - this.width);
        int absY = Math.min(this.getMouseY(), this.getScreen().getGuiScaledHeight() - this.height);
        return this.atPosition(absX - this.parent.getX(), absY - this.parent.getY() - (int) this.parent.getScrollY());
    }

    @Override
    public void addWidgets() {
        if (this.title != null) {
            this.add(this.titleField);
        }
        this.add(this.textBox);
        this.add(this.accept);
        this.add(this.cancel);
    }

    @Override
    public void alignWidgets() {
        if (this.title != null) {
            this.titleField.setPosAndSize(2, 2, this.width, this.getGui().getTheme().getFontHeight() + 4);
        }
        this.textBox.setPosAndSize(2, this.titleField.getHeight() + 1, this.width - 36, 14);
        this.accept.setPos(this.textBox.width + 2, this.textBox.getPosY());
        this.cancel.setPos(this.accept.getPosX() + this.accept.width + 2, this.textBox.getPosY());
        this.height = this.title == null ? 16 : 30;
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
        theme.drawContextMenuBackground(graphics, x - 1, y - 1, w + 2, h + 2);
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

    private class EditField extends TextBox {

        public EditField() {
            super(EditStringConfigOverlay.this);
            this.setText(EditStringConfigOverlay.this.config.getStringFromValue(EditStringConfigOverlay.this.currentValue));
            this.textColor = Color4I.WHITE;
            this.setCursorPosition(this.getText().length());
            this.setSelectionPos(0);
            this.setFocused(true);
        }

        @Override
        public boolean allowInput() {
            return EditStringConfigOverlay.this.config.getCanEdit();
        }

        @Override
        public boolean isValid(String txt) {
            return EditStringConfigOverlay.this.config.parse(null, txt);
        }

        @Override
        public void onTextChanged() {
            EditStringConfigOverlay.this.config.parse(t -> EditStringConfigOverlay.this.currentValue = (T) t, this.getText());
        }

        @Override
        public void onEnterPressed() {
            if (EditStringConfigOverlay.this.config.getCanEdit()) {
                EditStringConfigOverlay.this.accept.onClicked(MouseButton.LEFT);
            }
        }

        @Override
        public boolean mouseScrolled(double scroll) {
            return (Boolean) EditStringConfigOverlay.this.config.scrollValue(EditStringConfigOverlay.this.currentValue, scroll > 0.0).map(v -> {
                EditStringConfigOverlay.this.textBox.setText(EditStringConfigOverlay.this.config.getStringFromValue((T) v));
                return true;
            }).orElse(super.mouseScrolled(scroll));
        }
    }

    public interface PosProvider {

        EditStringConfigOverlay.PosProvider.Offset getOverlayOffset();

        public static record Offset(int x, int y) {

            public static final EditStringConfigOverlay.PosProvider.Offset NONE = new EditStringConfigOverlay.PosProvider.Offset(0, 0);
        }
    }
}