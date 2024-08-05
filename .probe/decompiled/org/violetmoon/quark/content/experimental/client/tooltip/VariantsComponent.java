package org.violetmoon.quark.content.experimental.client.tooltip;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.experimental.module.VariantSelectorModule;

public class VariantsComponent implements ClientTooltipComponent, TooltipComponent {

    final ItemStack stack;

    boolean computed = false;

    List<ItemStack> variants;

    int height;

    int width;

    public VariantsComponent(ItemStack stack) {
        this.stack = stack;
    }

    private List<ItemStack> getVariants() {
        if (this.computed) {
            return this.variants;
        } else {
            List<ItemStack> setVariants = new ArrayList();
            if (this.stack.getItem() instanceof BlockItem bi) {
                Block block = bi.getBlock();
                for (Block b : VariantSelectorModule.variants.getAllVariants(block)) {
                    setVariants.add(new ItemStack(b));
                }
            }
            this.computed = true;
            this.variants = setVariants;
            int size = this.variants.size();
            this.height = size == 0 ? 0 : 20;
            this.width = size * 18 - 2;
            return this.variants;
        }
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        List<ItemStack> variants = this.getVariants();
        for (int i = 0; i < variants.size(); i++) {
            ItemStack variant = (ItemStack) variants.get(i);
            guiGraphics.renderItem(variant, x + i * 18, y);
        }
    }

    @Override
    public int getHeight() {
        this.getVariants();
        return this.height;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        this.getVariants();
        return this.width;
    }
}