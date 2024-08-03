package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloFilterButton extends GuiElement {

    private final List<Component> tooltip;

    private final GuiString label;

    private final Consumer<String> onChange;

    private final GuiTexture icon;

    private boolean inputFocused = false;

    private String filter = "";

    public HoloFilterButton(int x, int y, Consumer<String> onChange) {
        super(x, y, 11, 9);
        this.onChange = onChange;
        this.icon = new GuiTexture(-3, -3, 16, 16, 32, 0, GuiTextures.holo);
        this.icon.setColor(8355711);
        this.addChild(this.icon);
        this.label = new GuiString(11, 0, "");
        this.addChild(this.label);
        this.tooltip = Collections.singletonList(Component.translatable("tetra.holo.craft.variants_filter"));
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        this.setInputFocused(this.hasFocus());
        return this.hasFocus();
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }

    private void setInputFocused(boolean focused) {
        this.inputFocused = focused;
        if (this.inputFocused) {
            this.icon.setColor(16777164);
        } else if (!this.filter.isEmpty()) {
            this.icon.setColor(16777215);
        } else {
            this.icon.setColor(8355711);
        }
    }

    @Override
    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        switch(keyCode) {
            case 256:
                if (this.inputFocused) {
                    this.setInputFocused(false);
                    return true;
                }
            case 258:
            default:
                return false;
            case 257:
                this.setInputFocused(!this.inputFocused);
                return true;
            case 259:
                if (Screen.hasControlDown()) {
                    this.updateFilter("");
                }
                if (!this.filter.isEmpty()) {
                    this.updateFilter(StringUtils.chop(this.filter));
                }
                return true;
        }
    }

    @Override
    public boolean onCharType(char character, int modifiers) {
        if (this.inputFocused) {
            this.updateFilter(this.filter = this.filter + character);
            return true;
        } else if (character == 'f') {
            this.setInputFocused(true);
            return true;
        } else {
            return false;
        }
    }

    public void updateFilter(String newValue) {
        this.filter = newValue;
        this.label.setString(this.filter);
        this.onChange.accept(this.filter);
        this.setWidth(11 + this.label.getWidth());
    }

    public void reset() {
        this.filter = "";
        this.label.setString(this.filter);
        this.setWidth(11);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.inputFocused && System.currentTimeMillis() % 800L < 400L) {
            drawRect(graphics, refX + this.x + 12 + this.label.getWidth(), refY + this.y + 7, refX + this.x + 17 + this.label.getWidth(), refY + this.y + 8, 16777215, 1.0F);
        }
    }
}