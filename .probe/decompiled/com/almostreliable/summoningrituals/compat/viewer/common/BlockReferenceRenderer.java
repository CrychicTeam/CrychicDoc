package com.almostreliable.summoningrituals.compat.viewer.common;

import com.almostreliable.summoningrituals.platform.Platform;
import com.almostreliable.summoningrituals.recipe.component.BlockReference;
import com.almostreliable.summoningrituals.util.TextUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class BlockReferenceRenderer {

    protected final int size;

    private final Minecraft mc;

    private final BlockRenderDispatcher blockRenderer;

    protected BlockReferenceRenderer(int size) {
        this.size = size;
        this.mc = Minecraft.getInstance();
        this.blockRenderer = this.mc.getBlockRenderer();
    }

    public void render(GuiGraphics guiGraphics, @Nullable BlockReference blockReference) {
        if (blockReference != null) {
            PoseStack stack = guiGraphics.pose();
            stack.pushPose();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.defaultBlendFunc();
            stack.translate(0.93F * (float) this.size, 0.77F * (float) this.size, 100.0F);
            stack.scale(0.625F * (float) this.size, 0.625F * (float) this.size, 0.625F * (float) this.size);
            stack.mulPose(Axis.ZN.rotationDegrees(180.0F));
            stack.mulPose(Axis.XN.rotationDegrees(30.0F));
            stack.mulPose(Axis.YP.rotationDegrees(45.0F));
            MultiBufferSource.BufferSource bufferSource = this.mc.renderBuffers().bufferSource();
            Platform.renderSingleBlock(this.blockRenderer, blockReference, stack, bufferSource);
            bufferSource.endBatch();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            stack.popPose();
        }
    }

    public List<Component> getTooltip(BlockReference blockReference, TooltipFlag tooltipFlag) {
        BlockState displayState = blockReference.getDisplayState();
        ItemStack stack = new ItemStack(displayState.m_60734_());
        try {
            List<Component> tooltip = new ArrayList();
            tooltip.add(TextUtils.translate("tooltip", "block_below", ChatFormatting.GOLD).append(": ").append(((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.WHITE)));
            if (tooltipFlag.isAdvanced()) {
                tooltip.add(Component.literal(Platform.getId(stack.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            }
            this.appendStateTooltip(displayState, tooltip);
            return tooltip;
        } catch (Exception var6) {
            return List.of(Component.literal("Error rendering tooltip!").append(var6.getMessage()).withStyle(ChatFormatting.DARK_RED));
        }
    }

    private void appendStateTooltip(BlockState displayState, List<Component> tooltip) {
        BlockState defaultState = displayState.m_60734_().defaultBlockState();
        List<String> modifiedProps = new ArrayList();
        for (Property<?> property : displayState.m_61147_()) {
            if (!displayState.m_61143_(property).equals(defaultState.m_61143_(property))) {
                modifiedProps.add(property.getName() + ": " + displayState.m_61143_(property));
            }
        }
        if (!modifiedProps.isEmpty()) {
            tooltip.add(TextUtils.translate("tooltip", "properties", ChatFormatting.AQUA).append(TextUtils.colorize(":", ChatFormatting.AQUA)));
            for (String prop : modifiedProps) {
                tooltip.add(TextUtils.colorize("» ", ChatFormatting.GRAY).append(TextUtils.colorize(prop, ChatFormatting.WHITE)));
            }
        }
    }
}