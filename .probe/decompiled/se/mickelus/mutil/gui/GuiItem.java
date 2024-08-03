package se.mickelus.mutil.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class GuiItem extends GuiElement {

    private Minecraft mc;

    private ItemStack itemStack;

    private boolean showTooltip = true;

    private GuiItem.CountMode countMode = GuiItem.CountMode.normal;

    private float opacityThreshold = 1.0F;

    private boolean resetDepthTest = true;

    private boolean renderDecoration = true;

    public GuiItem(int x, int y) {
        super(x, y, 16, 16);
        this.mc = Minecraft.getInstance();
        this.setVisible(false);
    }

    public GuiItem setOpacityThreshold(float opacityThreshold) {
        this.opacityThreshold = opacityThreshold;
        return this;
    }

    public GuiItem setTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    public GuiItem setCountVisibility(GuiItem.CountMode mode) {
        this.countMode = mode;
        return this;
    }

    public GuiItem setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.setVisible(itemStack != null);
        return this;
    }

    public GuiItem setResetDepthTest(boolean shouldReset) {
        this.resetDepthTest = shouldReset;
        return this;
    }

    public GuiItem setRenderDecoration(boolean shouldRender) {
        this.renderDecoration = shouldRender;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (opacity * this.getOpacity() >= this.opacityThreshold) {
            RenderSystem.applyModelViewMatrix();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            graphics.renderItem(this.itemStack, refX + this.x, refY + this.y);
            if (this.renderDecoration) {
            }
            if (this.resetDepthTest) {
                RenderSystem.disableDepthTest();
            }
        }
    }

    protected String getCountString() {
        switch(this.countMode) {
            case normal:
                return null;
            case always:
                return String.valueOf(this.itemStack.getCount());
            case never:
                return "";
            default:
                return null;
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.showTooltip && this.itemStack != null && this.hasFocus() ? new ArrayList(this.itemStack.getTooltipLines(Minecraft.getInstance().player, this.mc.options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_)) : null;
    }

    public static enum CountMode {

        normal, always, never
    }
}