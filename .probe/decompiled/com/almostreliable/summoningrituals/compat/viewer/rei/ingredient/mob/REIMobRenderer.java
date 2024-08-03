package com.almostreliable.summoningrituals.compat.viewer.rei.ingredient.mob;

import com.almostreliable.summoningrituals.compat.viewer.common.MobIngredient;
import com.almostreliable.summoningrituals.compat.viewer.common.MobRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.gui.GuiGraphics;

public class REIMobRenderer extends MobRenderer implements EntryRenderer<MobIngredient> {

    REIMobRenderer(int size) {
        super(size);
    }

    public void render(EntryStack<MobIngredient> entry, GuiGraphics guiGraphics, Rectangle bounds, int mX, int mY, float delta) {
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate((float) (bounds.x - 1), (float) (bounds.y - 1), 0.0F);
        this.render(guiGraphics, (MobIngredient) entry.getValue());
        stack.popPose();
    }

    @Nullable
    public Tooltip getTooltip(EntryStack<MobIngredient> entry, TooltipContext context) {
        return Tooltip.create(context.getPoint(), this.getTooltip((MobIngredient) entry.getValue(), context.getFlag()));
    }
}