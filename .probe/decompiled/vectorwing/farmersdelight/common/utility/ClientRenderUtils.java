package vectorwing.farmersdelight.common.utility;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ClientRenderUtils {

    public static boolean isCursorInsideBounds(int iconX, int iconY, int iconWidth, int iconHeight, double cursorX, double cursorY) {
        return (double) iconX <= cursorX && cursorX < (double) (iconX + iconWidth) && (double) iconY <= cursorY && cursorY < (double) (iconY + iconHeight);
    }

    public static void renderItemIntoGUIScalable(ItemStack stack, float width, float height, BakedModel bakedmodel, ItemRenderer renderer, TextureManager textureManager) {
        textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.translate(8.0F, 8.0F, 0.0F);
        modelViewStack.scale(1.0F, -1.0F, 1.0F);
        modelViewStack.scale(width, height, 48.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean usesBlockLight = !bakedmodel.usesBlockLight();
        if (usesBlockLight) {
            Lighting.setupForFlatItems();
        }
        renderer.render(stack, ItemDisplayContext.GUI, false, poseStack, multibuffersource$buffersource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (usesBlockLight) {
            Lighting.setupFor3DItems();
        }
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}