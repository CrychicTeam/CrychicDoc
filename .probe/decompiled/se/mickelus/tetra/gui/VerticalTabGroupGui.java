package se.mickelus.tetra.gui;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.gui.GuiElement;

@ParametersAreNonnullByDefault
public class VerticalTabGroupGui extends GuiElement {

    private static final char[] keybindings = new char[] { 'a', 's', 'd', 'f', 'g' };

    private final VerticalTabButtonGui[] buttons;

    private final Consumer<Integer> clickHandler;

    public VerticalTabGroupGui(int x, int y, Consumer<Integer> clickHandler, String... labels) {
        super(x, y, 3, labels.length * 16 + 1);
        this.buttons = new VerticalTabButtonGui[labels.length];
        for (int i = 0; i < labels.length; i++) {
            int index = i;
            this.buttons[i] = new VerticalTabButtonGui(1, 1 + 16 * i, labels[i], i < keybindings.length ? keybindings[i] + "" : null, () -> {
                clickHandler.accept(index);
                this.setActive(index);
            }, i == 0);
            this.addChild(this.buttons[i]);
        }
        this.clickHandler = clickHandler;
    }

    public VerticalTabGroupGui(int x, int y, Consumer<Integer> clickHandler, ResourceLocation texture, int textureX, int textureY, String... labels) {
        super(x, y, 3, labels.length * 16 + 1);
        this.buttons = new VerticalTabButtonGui[labels.length];
        for (int i = 0; i < labels.length; i++) {
            int index = i;
            this.buttons[i] = new VerticalTabIconButtonGui(1, 1 + 16 * i, texture, textureX + 16 * i, textureY, labels[i], i < keybindings.length ? keybindings[i] + "" : null, () -> {
                clickHandler.accept(index);
                this.setActive(index);
            }, i == 0);
            this.addChild(this.buttons[i]);
        }
        this.clickHandler = clickHandler;
    }

    public void setActive(int index) {
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].setActive(i == index);
        }
    }

    public void setHasContent(int index, boolean hasContent) {
        this.buttons[index].setHasContent(hasContent);
    }

    public void keyTyped(char typedChar) {
        for (int i = 0; i < this.buttons.length; i++) {
            if (i < keybindings.length && keybindings[i] == typedChar) {
                this.setActive(i);
                this.clickHandler.accept(i);
            }
        }
    }
}