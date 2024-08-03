package com.mna.gui.widgets.guide;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class ItemGridWidget extends AbstractWidget {

    public static final int ITEM_SIZE = 16;

    private final int items_per_row;

    private final float scale;

    private final int padding;

    private final ItemStack[] renderStacks;

    private final Consumer<List<Component>> tooltipFunction;

    private ItemStack hoveredStack = ItemStack.EMPTY;

    public ItemGridWidget(int pX, int pY, int pWidth, int pHeight, float scale, ItemStack[] stacks, Consumer<List<Component>> tooltipFunction) {
        this(pX, pY, pWidth, pHeight, 4, 2, scale, stacks, tooltipFunction);
    }

    public ItemGridWidget(int pX, int pY, int pWidth, int pHeight, int items_per_row, int padding, float scale, ItemStack[] stacks, Consumer<List<Component>> tooltipFunction) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.items_per_row = items_per_row;
        this.renderStacks = stacks;
        this.scale = scale;
        this.padding = padding;
        this.tooltipFunction = tooltipFunction;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.renderStacks != null && this.renderStacks.length != 0) {
            this.hoveredStack = ItemStack.EMPTY;
            int row_items = Math.min(this.items_per_row, this.renderStacks.length);
            int width_from_items = row_items * 16;
            int width_from_padding = Math.max(0, row_items - 1) * this.padding;
            int row_width = (int) ((float) (width_from_items + width_from_padding) * this.scale);
            int xPos = this.m_252754_() + (108 - row_width) / 2;
            int yPos = this.m_252907_();
            for (int i = 0; i < this.renderStacks.length; i++) {
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().scale(this.scale, this.scale, this.scale);
                int itemX = (int) ((float) xPos / this.scale);
                int itemY = (int) ((float) yPos / this.scale);
                pGuiGraphics.renderItem(this.renderStacks[i], itemX, itemY);
                if (this.f_93622_ && pMouseX >= itemX && pMouseX <= itemX + 16 + this.padding && pMouseY >= itemY && pMouseY <= itemY + 16 + this.padding) {
                    this.hoveredStack = this.renderStacks[i];
                    if (this.tooltipFunction != null) {
                        Minecraft mc = Minecraft.getInstance();
                        this.tooltipFunction.accept(this.hoveredStack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256730_));
                    }
                }
                if (i % this.items_per_row == 0 && i != 0) {
                    xPos = this.m_252754_() + (108 - row_width) / 2;
                    yPos += 16 + this.padding;
                } else {
                    xPos += 16 + this.padding;
                }
                pGuiGraphics.pose().popPose();
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}