package se.mickelus.tetra.gui;

import java.util.Arrays;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiItem;

@ParametersAreNonnullByDefault
public class GuiItemRolling extends GuiElement {

    private boolean showTooltip = true;

    private GuiItem.CountMode countMode = GuiItem.CountMode.normal;

    private GuiItem[] items = new GuiItem[0];

    public GuiItemRolling(int x, int y) {
        super(x, y, 16, 16);
    }

    public GuiItemRolling setTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    public GuiItemRolling setCountVisibility(GuiItem.CountMode mode) {
        this.countMode = mode;
        return this;
    }

    public GuiItemRolling setItems(ItemStack[] itemStacks) {
        this.items = (GuiItem[]) Arrays.stream(itemStacks).map(itemStack -> new GuiItem(0, 0).setItem(itemStack).setCountVisibility(this.countMode).setResetDepthTest(false)).toArray(GuiItem[]::new);
        return this;
    }

    @Override
    protected void drawChildren(GuiGraphics guiGraphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.items.length > 0) {
            int offset = (int) (System.currentTimeMillis() / 1000L) % this.items.length;
            this.items[offset].draw(guiGraphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity * this.getOpacity());
        }
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        super.updateFocusState(refX, refY, mouseX, mouseY);
        if (this.items.length > 0) {
            this.items[(int) (System.currentTimeMillis() / 1000L) % this.items.length].updateFocusState(refX + this.x, refY + this.y, mouseX, mouseY);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.showTooltip && this.hasFocus() && this.items.length > 0) {
            int offset = (int) (System.currentTimeMillis() / 1000L) % this.items.length;
            return this.items[offset].getTooltipLines();
        } else {
            return null;
        }
    }
}