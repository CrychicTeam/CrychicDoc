package com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.block;

import com.almostreliable.summoningrituals.compat.viewer.common.BlockReferenceRenderer;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.GuiGraphics;

public class REIBlockReferenceRenderer extends BlockReferenceRenderer implements EntryRenderer<BlockReference> {

    REIBlockReferenceRenderer(int size) {
        super(size);
    }

    public void render(EntryStack<BlockReference> entry, GuiGraphics guiGraphics, Rectangle bounds, int mX, int mY, float delta) {
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate((float) (bounds.x - 1), (float) (bounds.y - 1), 0.0F);
        this.render(guiGraphics, (BlockReference) entry.getValue());
        stack.popPose();
    }

    @Nullable
    public Tooltip getTooltip(EntryStack<BlockReference> entry, TooltipContext context) {
        return Tooltip.create(context.getPoint(), this.getTooltip((BlockReference) entry.getValue(), context.getFlag()));
    }
}