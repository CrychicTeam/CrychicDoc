package com.mna.interop.jei.ingredients;

import com.mna.tools.render.GuiRenderUtils;
import java.util.Arrays;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

public class ManaweavePatternIngredientRenderer implements IIngredientRenderer<ManaweavePatternIngredient> {

    public void render(GuiGraphics pGuiGraphics, ManaweavePatternIngredient ingredient) {
        if (ingredient != null) {
            float scale = 0.1F;
            Minecraft mc = Minecraft.getInstance();
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().translate(14.0F, 1.0F, 0.0F);
            GuiRenderUtils.renderManaweavePattern(pGuiGraphics, 0, 0, scale, ingredient.getPattern(mc.level));
            pGuiGraphics.pose().popPose();
        }
    }

    public List<Component> getTooltip(ManaweavePatternIngredient ingredient, TooltipFlag tooltipFlag) {
        if (ingredient == null) {
            return Arrays.asList();
        } else {
            Minecraft mc = Minecraft.getInstance();
            return Arrays.asList(Component.translatable(ingredient.getPattern(mc.level).m_6423_().toString()));
        }
    }
}