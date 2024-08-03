package snownee.kiwi.customization.builder;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import snownee.kiwi.util.NotNullByDefault;

@NotNullByDefault
public class ItemButton extends Button {

    private final ItemStack itemStack;

    private final boolean inContainer;

    private ClientTooltipPositioner tooltipPositioner;

    private float hoverProgress;

    protected ItemButton(ItemButton.Builder builder) {
        super(builder);
        this.itemStack = builder.itemStack;
        this.inContainer = builder.inContainer;
    }

    public static ItemButton.Builder builder(ItemStack itemStack, boolean inContainer, Button.OnPress pOnPress) {
        return new ItemButton.Builder(itemStack, inContainer, pOnPress);
    }

    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int x = this.m_252754_();
        int y = this.m_252907_();
        int width = this.m_5711_() - 1;
        int height = this.m_93694_() - 1;
        if (this.inContainer) {
            pGuiGraphics.fill(x + 1, y + 1, x + width - 1, y + height - 1, 2236962 | (int) (this.f_93625_ * 255.0F) << 24);
        } else {
            pGuiGraphics.fill(x, y, x + width, y + height, 2236962 | (int) (this.f_93625_ * 255.0F) << 24);
        }
        this.hoverProgress = this.hoverProgress + (this.m_198029_() ? pPartialTick * 0.2F : -pPartialTick * 0.2F);
        this.hoverProgress = Mth.clamp(this.hoverProgress, this.inContainer ? 0.0F : 0.4F, 1.0F);
        int lineColor = 16777215 | (int) (this.hoverProgress * 255.0F) << 24;
        pGuiGraphics.fill(x, y, x + 1, y + height, lineColor);
        pGuiGraphics.fill(x + width - 1, y, x + width, y + height, lineColor);
        pGuiGraphics.fill(x + 1, y, x + width - 1, y + 1, lineColor);
        pGuiGraphics.fill(x + 1, y + height - 1, x + width - 1, y + height, lineColor);
        pGuiGraphics.renderItem(this.itemStack, x + 2, y + 2);
    }

    public void setTooltipPositioner(ClientTooltipPositioner tooltipPositioner) {
        this.tooltipPositioner = tooltipPositioner;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return this.tooltipPositioner != null ? this.tooltipPositioner : super.m_262860_();
    }

    public static class Builder extends Button.Builder {

        private final ItemStack itemStack;

        private final boolean inContainer;

        protected Builder(ItemStack itemStack, boolean inContainer, Button.OnPress pOnPress) {
            super(itemStack.getHoverName(), pOnPress);
            this.itemStack = itemStack;
            this.inContainer = inContainer;
        }

        public ItemButton build() {
            return new ItemButton(this);
        }
    }
}