package com.mna.items.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.models.constructs.ConstructModel;
import com.mna.entities.renderers.construct.ConstructRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ConstructPartRenderer extends BlockEntityWithoutLevelRenderer {

    public ConstructPartRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof ItemConstructPart) {
            ItemConstructPart p = (ItemConstructPart) stack.getItem();
            matrixStack.pushPose();
            Construct dummy = ManaAndArtifice.instance.proxy.getDummyConstructForRender();
            dummy.getConstructData().resetParts();
            dummy.getConstructData().setPart(new ItemStack(p), true);
            ConstructRenderer geoRenderer = ConstructRenderer.instance;
            ConstructModel model = ConstructRenderer.constructModel;
            model.getBakedModel(model.getModelResource(dummy));
            model.setActiveMaterial(p.getMaterial());
            try {
                model.setVisibleParts(null, p.getSlot() == ConstructSlot.HEAD ? p.getModelTypeMutex() : 0, p.getSlot() == ConstructSlot.TORSO ? p.getModelTypeMutex() : 0, p.getSlot() == ConstructSlot.LEGS ? p.getModelTypeMutex() : 0, p.getSlot() == ConstructSlot.LEFT_ARM ? p.getModelTypeMutex() : 0, p.getSlot() == ConstructSlot.RIGHT_ARM ? p.getModelTypeMutex() : 0);
            } catch (Exception var12) {
            }
            switch(p.getSlot()) {
                case HEAD:
                    matrixStack.translate(0.51, -1.7, 0.45);
                    matrixStack.scale(2.0F, 2.0F, 2.0F);
                    break;
                case LEFT_ARM:
                    matrixStack.translate(0.4, -0.2, 0.45);
                    matrixStack.mulPose(Axis.ZP.rotationDegrees(22.0F));
                    matrixStack.mulPose(Axis.YP.rotationDegrees(22.0F));
                    break;
                case LEGS:
                    matrixStack.translate(0.51, 0.2, 0.55);
                    matrixStack.mulPose(Axis.YP.rotationDegrees(22.0F));
                    break;
                case RIGHT_ARM:
                    matrixStack.translate(1.0, 0.0, 0.45);
                    matrixStack.mulPose(Axis.ZP.rotationDegrees(22.0F));
                    matrixStack.mulPose(Axis.YP.rotationDegrees(22.0F));
                    break;
                case TORSO:
                    matrixStack.translate(0.51, -0.35, 0.45);
            }
            Lighting.setupForFlatItems();
            if (itemDisplayContext != ItemDisplayContext.GUI) {
                geoRenderer.render(dummy, dummy.m_146909_(), 0.0F, matrixStack, buffer, combinedLight);
            } else {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                Lighting.setupForFlatItems();
                geoRenderer.render(dummy, dummy.m_146909_(), 0.0F, matrixStack, buffer, combinedLight);
                bufferSource.endBatch();
                RenderSystem.enableDepthTest();
                Lighting.setupFor3DItems();
            }
            matrixStack.popPose();
        }
    }
}