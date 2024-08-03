package club.iananderson.seasonhud.client.gui.components.buttons;

import club.iananderson.seasonhud.client.gui.components.boxes.ColorEditBox;
import club.iananderson.seasonhud.impl.seasons.SeasonList;
import club.iananderson.seasonhud.util.Rgb;
import java.util.HashMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class DefaultColorButton extends Button {

    private static final Component DEFAULT = Component.translatable("menu.seasonhud.color.button.default");

    private ColorEditBox colorEditBox;

    private int defaultColor;

    private DefaultColorButton(int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        super(x, y, width, height, component, onPress, f_252438_);
    }

    public DefaultColorButton(int x, int y, SeasonList season, ColorEditBox colorEditBox, Button.OnPress onPress) {
        this(x, y, colorEditBox.m_5711_() + 2, colorEditBox.m_93694_() - 2, DEFAULT, onPress);
        this.colorEditBox = colorEditBox;
        this.defaultColor = season.getDefaultColor();
    }

    private boolean inBounds(int color) {
        int minColor = 0;
        int maxColor = 16777215;
        return color >= minColor && color <= maxColor;
    }

    public boolean validate(String colorString) {
        try {
            int colorInt = Integer.parseInt(colorString);
            return this.inBounds(colorInt);
        } catch (NumberFormatException var3) {
            return false;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.refresh();
        String boxValue = this.colorEditBox.m_94155_();
        if (this.validate(boxValue) && Integer.parseInt(boxValue) == this.defaultColor) {
            this.f_93623_ = false;
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    private void refresh() {
        HashMap<String, Integer> defaultColors = Rgb.defaultSeasonMap(this.colorEditBox.getSeason());
        HashMap<String, Integer> currentColors = this.colorEditBox.getSeason().getRgbMap();
        this.f_93623_ = defaultColors != currentColors;
    }
}