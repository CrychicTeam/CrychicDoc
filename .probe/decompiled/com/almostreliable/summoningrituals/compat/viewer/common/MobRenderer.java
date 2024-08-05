package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.util.GameUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;

public class MobRenderer {

    private static final float CREEPER_HEIGHT = 1.7F;

    private static final float CREEPER_SCALE = 0.5F;

    protected final int size;

    private final Minecraft mc;

    protected MobRenderer(int size) {
        this.size = size;
        this.mc = Minecraft.getInstance();
    }

    public void render(GuiGraphics guiGraphics, @Nullable MobIngredient mob) {
        if (this.mc.level != null && this.mc.player != null && mob != null) {
            PoseStack stack = guiGraphics.pose();
            if (mob.getEntity() != null && mob.getEntity() instanceof LivingEntity entity) {
                stack.pushPose();
                entity.f_19797_ = this.mc.player.f_19797_;
                stack.translate(0.5F * (float) this.size, 0.9F * (float) this.size, 0.0F);
                float entityHeight = entity.m_20206_();
                float entityScale = Math.min(1.7F / entityHeight, 1.0F);
                float scaleFactor = 0.5F * (float) this.size * entityScale;
                this.renderEntity(guiGraphics, entity, scaleFactor);
                stack.popPose();
            }
            if (mob.getCount() > 1) {
                String count = String.valueOf(mob.getCount());
                GameUtils.renderCount(guiGraphics, count, this.size, this.size);
            }
        }
    }

    private void renderEntity(GuiGraphics guiGraphics, LivingEntity entity, float scaleFactor) {
        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.mulPoseMatrix(guiGraphics.pose().last().pose());
        InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, 0, 0, (int) scaleFactor, 75.0F, -20.0F, entity);
        modelView.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public List<Component> getTooltip(MobIngredient mob, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList();
        tooltip.add(mob.getDisplayName());
        if (tooltipFlag.isAdvanced()) {
            tooltip.add(mob.getRegistryName().withStyle(ChatFormatting.DARK_GRAY));
        }
        return tooltip;
    }
}