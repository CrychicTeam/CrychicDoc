package com.mna.gui.radial.components;

import com.mna.gui.radial.GenericRadialMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemStackRadialMenuItem extends TextRadialMenuItem {

    private final ItemStack stack;

    public ItemStack getStack() {
        return this.stack;
    }

    public ItemStackRadialMenuItem(GenericRadialMenu owner, ItemStack stack, Component altText) {
        super(owner, altText, Integer.MAX_VALUE);
        this.stack = stack;
    }

    @Override
    public void draw(DrawingContext context) {
        if (this.stack.getCount() > 0) {
            context.guiGraphics.renderItem(this.stack, (int) context.x - 8, (int) context.y - 8);
        } else {
            super.draw(context);
        }
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (this.stack.getCount() > 0) {
            context.guiGraphics.renderTooltip(context.fontRenderer, this.stack, (int) context.x, (int) context.y);
        } else {
            super.drawTooltips(context);
        }
    }
}