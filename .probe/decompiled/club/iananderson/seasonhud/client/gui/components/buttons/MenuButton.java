package club.iananderson.seasonhud.client.gui.components.buttons;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class MenuButton extends Button {

    private static final int BUTTON_WIDTH = 150;

    private static final int BUTTON_HEIGHT = 20;

    private MenuButton(int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        super(x, y, width, height, component, onPress, f_252438_);
    }

    public MenuButton(int x, int y, int width, int height, MenuButton.MenuButtons button, Button.OnPress onPress) {
        super(x, y, width, height, button.getButtonText(), onPress, f_252438_);
    }

    public MenuButton(int x, int y, MenuButton.MenuButtons button, Button.OnPress onPress) {
        this(x, y, 150, 20, button.getButtonText(), onPress);
    }

    public static enum MenuButtons {

        DONE(CommonComponents.GUI_DONE), CANCEL(CommonComponents.GUI_CANCEL), COLORS(Component.translatable("menu.seasonhud.color.title"));

        private final Component buttonText;

        private MenuButtons(Component buttonText) {
            this.buttonText = buttonText;
        }

        public Component getButtonText() {
            return this.buttonText;
        }
    }
}