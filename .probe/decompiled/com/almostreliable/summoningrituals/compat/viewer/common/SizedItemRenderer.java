package com.almostreliable.summoningrituals.compat.viewer.common;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class SizedItemRenderer {

    protected final int size;

    final Minecraft mc = Minecraft.getInstance();

    SizedItemRenderer(int size) {
        this.size = size;
    }

    public void render(GuiGraphics guiGraphics, @Nullable ItemStack item) {
        if (item != null) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            float scale = (float) this.size / 16.0F;
            stack.scale(scale, scale, scale);
            RenderSystem.enableDepthTest();
            guiGraphics.renderFakeItem(item, 0, 0);
            guiGraphics.renderItemDecorations(this.mc.font, item, 0, 0);
            RenderSystem.disableBlend();
            stack.popPose();
        }
    }

    public List<Component> getTooltip(ItemStack stack, TooltipFlag tooltipFlag) {
        return List.of();
    }
}