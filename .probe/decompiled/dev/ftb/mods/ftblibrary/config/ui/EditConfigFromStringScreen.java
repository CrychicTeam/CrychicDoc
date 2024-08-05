package dev.ftb.mods.ftblibrary.config.ui;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ConfigFromString;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.TextBox;
import dev.ftb.mods.ftblibrary.ui.Theme;
import dev.ftb.mods.ftblibrary.ui.WidgetType;
import dev.ftb.mods.ftblibrary.ui.input.Key;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class EditConfigFromStringScreen<T> extends BaseScreen {

    private final ConfigFromString<T> config;

    private final ConfigCallback callback;

    private final Button buttonCancel;

    private final Button buttonAccept;

    private final TextBox textBox;

    private T currentValue;

    private Component title = Component.empty();

    public static <E> void open(ConfigFromString<E> type, @Nullable E value, @Nullable E defaultValue, Component title, ConfigCallback callback) {
        ConfigGroup group = new ConfigGroup("group");
        group.add("value", type, value, e -> {
        }, defaultValue);
        new EditConfigFromStringScreen<>(type, callback).setTitle(title).openGui();
    }

    public static <E> void open(ConfigFromString<E> type, @Nullable E value, @Nullable E defaultValue, ConfigCallback callback) {
        open(type, value, defaultValue, Component.empty(), callback);
    }

    public EditConfigFromStringScreen(ConfigFromString<T> c, ConfigCallback cb) {
        this.setSize(230, 54);
        this.config = c;
        this.callback = cb;
        this.currentValue = this.config.getValue() == null ? null : this.config.copy(this.config.getValue());
        int bsize = this.width / 2 - 10;
        this.buttonCancel = new SimpleTextButton(this, Component.translatable("gui.cancel"), Icon.empty()) {

            @Override
            public void onClicked(MouseButton button) {
                this.playClickSound();
                EditConfigFromStringScreen.this.doCancel();
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
        this.buttonCancel.setPosAndSize(8, this.height - 24, bsize, 16);
        this.buttonAccept = new SimpleTextButton(this, Component.translatable("gui.accept"), Icon.empty()) {

            @Override
            public void onClicked(MouseButton button) {
                this.playClickSound();
                EditConfigFromStringScreen.this.doAccept();
            }

            @Override
            public WidgetType getWidgetType() {
                return EditConfigFromStringScreen.this.config.getCanEdit() && EditConfigFromStringScreen.this.textBox.isTextValid() ? super.getWidgetType() : WidgetType.DISABLED;
            }

            @Override
            public boolean renderTitleInCenter() {
                return true;
            }
        };
        this.buttonAccept.setPosAndSize(this.width - bsize - 8, this.height - 24, bsize, 16);
        this.textBox = new TextBox(this) {

            @Override
            public boolean allowInput() {
                return EditConfigFromStringScreen.this.config.getCanEdit();
            }

            @Override
            public boolean isValid(String txt) {
                return EditConfigFromStringScreen.this.config.parse(null, txt);
            }

            @Override
            public void onTextChanged() {
                EditConfigFromStringScreen.this.config.parse(t -> {
                    EditConfigFromStringScreen.this.currentValue = (T) t;
                    this.textColor = EditConfigFromStringScreen.this.config.getColor((T) t);
                }, this.getText());
            }

            @Override
            public void onEnterPressed() {
                if (EditConfigFromStringScreen.this.config.getCanEdit()) {
                    EditConfigFromStringScreen.this.buttonAccept.onClicked(MouseButton.LEFT);
                }
            }

            @Override
            public boolean mouseScrolled(double scroll) {
                return (Boolean) EditConfigFromStringScreen.this.config.scrollValue(EditConfigFromStringScreen.this.currentValue, scroll > 0.0).map(v -> {
                    EditConfigFromStringScreen.this.textBox.setText(EditConfigFromStringScreen.this.config.getStringFromValue((T) v));
                    return true;
                }).orElse(super.mouseScrolled(scroll));
            }
        };
        this.textBox.setPosAndSize(8, 8, this.width - 16, 16);
        this.textBox.setText(this.config.getStringFromValue(this.currentValue));
        this.textBox.textColor = this.config.getColor(this.currentValue);
        this.textBox.setCursorPosition(this.textBox.getText().length());
        this.textBox.setFocused(true);
    }

    @Override
    public void drawForeground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        super.drawForeground(graphics, theme, x, y, w, h);
        if (!this.title.equals(Component.empty())) {
            theme.drawString(graphics, this.title, this.getX() + this.width / 2, this.getY() - theme.getFontHeight() - 2, Color4I.WHITE, 4);
        }
    }

    public EditConfigFromStringScreen<T> setTitle(Component title) {
        this.title = title;
        return this;
    }

    private void doAccept() {
        this.config.setCurrentValue(this.currentValue);
        this.callback.save(true);
    }

    private void doCancel() {
        this.callback.save(false);
    }

    @Override
    public boolean onClosedByKey(Key key) {
        if (super.onClosedByKey(key)) {
            if (key.esc()) {
                this.doCancel();
            } else {
                this.doAccept();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addWidgets() {
        this.add(this.buttonCancel);
        this.add(this.buttonAccept);
        this.add(this.textBox);
    }

    @Override
    public boolean doesGuiPauseGame() {
        Screen screen = this.getPrevScreen();
        return screen != null && screen.isPauseScreen();
    }
}