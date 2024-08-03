package se.mickelus.tetra.blocks.workbench.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiButtonOutlined extends GuiClickable {

    private final GuiTexture borderLeft;

    private final GuiTexture borderRight;

    private final GuiRect borderTop;

    private final GuiRect borderBottom;

    public GuiButtonOutlined(int x, int y, String label, Runnable onClickHandler) {
        this(x, y, label, GuiAlignment.left, onClickHandler);
    }

    public GuiButtonOutlined(int x, int y, String label, GuiAlignment alignment, Runnable onClickHandler) {
        super(x, y, 0, 11, onClickHandler);
        this.width = Minecraft.getInstance().font.width(label) + 18;
        this.addChild(new GuiRect(9, 0, this.width - 18, 11, 0));
        this.borderLeft = new GuiTexture(0, 0, 9, 11, 79, 0, GuiTextures.workbench).setColor(8355711);
        this.addChild(this.borderLeft);
        this.borderRight = new GuiTexture(this.width - 9, 0, 9, 11, 88, 0, GuiTextures.workbench).setColor(8355711);
        this.addChild(this.borderRight);
        this.borderTop = new GuiRect(9, 1, this.width - 18, 1, 8355711);
        this.addChild(this.borderTop);
        this.borderBottom = new GuiRect(9, 9, this.width - 18, 1, 8355711);
        this.addChild(this.borderBottom);
        this.addChild(new GuiStringOutline(9, 1, label));
    }

    private void setBorderColors(int color) {
        this.borderLeft.setColor(color);
        this.borderRight.setColor(color);
        this.borderTop.setColor(color);
        this.borderBottom.setColor(color);
    }

    @Override
    protected void onFocus() {
        this.setBorderColors(9408367);
    }

    @Override
    protected void onBlur() {
        this.setBorderColors(8355711);
    }
}