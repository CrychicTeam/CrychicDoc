package net.mehvahdjukaar.moonlight.api.client.util;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.BiConsumer;
import net.mehvahdjukaar.moonlight.api.client.util.forge.RenderUtilImpl;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.core.MoonlightClient;
import net.mehvahdjukaar.moonlight.core.client.MLRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

public class RenderUtil {

    static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");

    static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");

    @ExpectPlatform
    @Transformed
    public static void renderBlock(BakedModel model, long seed, PoseStack poseStack, MultiBufferSource buffer, BlockState state, Level level, BlockPos pos, BlockRenderDispatcher dispatcher) {
        RenderUtilImpl.renderBlock(model, seed, poseStack, buffer, state, level, pos, dispatcher);
    }

    public static void renderBlock(long seed, PoseStack poseStack, MultiBufferSource buffer, BlockState state, Level level, BlockPos pos, BlockRenderDispatcher dispatcher) {
        BakedModel model = dispatcher.getBlockModel(state);
        renderBlock(model, seed, poseStack, buffer, state, level, pos, dispatcher);
    }

    @Deprecated(forRemoval = true)
    public static void renderBlockModel(ResourceLocation modelLocation, PoseStack matrixStack, MultiBufferSource buffer, BlockRenderDispatcher blockRenderer, int light, int overlay, boolean cutout) {
        renderModel(modelLocation, matrixStack, buffer, blockRenderer, light, overlay, cutout);
    }

    public static void renderModel(ResourceLocation modelLocation, PoseStack matrixStack, MultiBufferSource buffer, BlockRenderDispatcher blockRenderer, int light, int overlay, boolean cutout) {
        blockRenderer.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(cutout ? Sheets.cutoutBlockSheet() : Sheets.solidBlockSheet()), null, ClientHelper.getModel(blockRenderer.getBlockModelShaper().getModelManager(), modelLocation), 1.0F, 1.0F, 1.0F, light, overlay);
    }

    public static void renderGuiItemRelative(PoseStack poseStack, ItemStack stack, int x, int y, ItemRenderer renderer, BiConsumer<PoseStack, BakedModel> movement) {
        renderGuiItemRelative(poseStack, stack, x, y, renderer, movement, 15728880, OverlayTexture.NO_OVERLAY);
    }

    public static void renderGuiItemRelative(PoseStack poseStack, ItemStack stack, int x, int y, ItemRenderer renderer, BiConsumer<PoseStack, BakedModel> movement, int combinedLight, int pCombinedOverlay) {
        BakedModel model = renderer.getModel(stack, null, null, 0);
        int l = 0;
        poseStack.pushPose();
        poseStack.translate((float) (x + 8), (float) (y + 8), (float) (150 + (model.isGui3d() ? l : 0)));
        poseStack.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
        poseStack.scale(16.0F, 16.0F, 16.0F);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !model.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        } else {
            Lighting.setupFor3DItems();
        }
        ItemDisplayContext pTransformType = ItemDisplayContext.GUI;
        model = handleCameraTransforms(model, poseStack, pTransformType);
        movement.accept(poseStack, model);
        renderer.render(stack, ItemDisplayContext.NONE, false, poseStack, bufferSource, combinedLight, pCombinedOverlay, model);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }
        poseStack.popPose();
    }

    @ExpectPlatform
    @Transformed
    private static BakedModel handleCameraTransforms(BakedModel model, PoseStack matrixStack, ItemDisplayContext pTransformType) {
        return RenderUtilImpl.handleCameraTransforms(model, matrixStack, pTransformType);
    }

    @Deprecated(forRemoval = true)
    @ExpectPlatform
    @Transformed
    public static void renderGuiItem(BakedModel model, ItemStack stack, ItemRenderer renderer, int combinedLight, int pCombinedOverlay, PoseStack poseStack, MultiBufferSource.BufferSource buffer, boolean flatItem) {
        RenderUtilImpl.renderGuiItem(model, stack, renderer, combinedLight, pCombinedOverlay, poseStack, buffer, flatItem);
    }

    public static GuiGraphics getGuiDummy(PoseStack poseStack) {
        Minecraft mc = Minecraft.getInstance();
        return new GuiGraphics(mc, poseStack, mc.renderBuffers().bufferSource());
    }

    public static void blitSpriteSection(GuiGraphics graphics, int x, int y, int w, int h, float u, float v, int uW, int vH, TextureAtlasSprite sprite) {
        SpriteContents c = sprite.contents();
        int width = (int) ((float) c.width() / (sprite.getU1() - sprite.getU0()));
        int height = (int) ((float) c.height() / (sprite.getV1() - sprite.getV0()));
        graphics.blit(sprite.atlasLocation(), x, y, w, h, sprite.getU((double) u) * (float) width, (float) height * sprite.getV((double) v), uW, vH, width, height);
    }

    public static void renderSprite(PoseStack stack, VertexConsumer vertexBuilder, int light, int index, int b, int g, int r, TextureAtlasSprite sprite) {
        renderSprite(stack, vertexBuilder, light, index, b, g, r, 255, sprite);
    }

    public static void renderSprite(PoseStack stack, VertexConsumer vertexBuilder, int light, int index, int b, int g, int r, int a, TextureAtlasSprite sprite) {
        Matrix4f matrix4f1 = stack.last().pose();
        float u0 = sprite.getU(0.0);
        float u1 = sprite.getU(16.0);
        float h = (u0 + u1) / 2.0F;
        float v0 = sprite.getV(0.0);
        float v1 = sprite.getV(16.0);
        float k = (v0 + v1) / 2.0F;
        float shrink = sprite.uvShrinkRatio();
        float u0s = Mth.lerp(shrink, u0, h);
        float u1s = Mth.lerp(shrink, u1, h);
        float v0s = Mth.lerp(shrink, v0, k);
        float v1s = Mth.lerp(shrink, v1, k);
        vertexBuilder.vertex(matrix4f1, -1.0F, 1.0F, (float) index * -0.001F).color(r, g, b, a).uv(u0s, v1s).uv2(light).endVertex();
        vertexBuilder.vertex(matrix4f1, 1.0F, 1.0F, (float) index * -0.001F).color(r, g, b, a).uv(u1s, v1s).uv2(light).endVertex();
        vertexBuilder.vertex(matrix4f1, 1.0F, -1.0F, (float) index * -0.001F).color(r, g, b, a).uv(u1s, v0s).uv2(light).endVertex();
        vertexBuilder.vertex(matrix4f1, -1.0F, -1.0F, (float) index * -0.001F).color(r, g, b, a).uv(u0s, v0s).uv2(light).endVertex();
    }

    public static RenderType getTextMipmapRenderType(ResourceLocation texture) {
        return (RenderType) MLRenderTypes.TEXT_MIP.apply(texture);
    }

    public static RenderType getEntityCutoutMipmapRenderType(ResourceLocation texture) {
        return (RenderType) MLRenderTypes.ENTITY_CUTOUT_MIP.apply(texture);
    }

    public static RenderType getEntitySolidMipmapRenderType(ResourceLocation texture) {
        return (RenderType) MLRenderTypes.ENTITY_SOLID_MIP.apply(texture);
    }

    public static RenderType getTextColorRenderType(ResourceLocation texture) {
        return (RenderType) MLRenderTypes.COLOR_TEXT.apply(texture);
    }

    public static void setDynamicTexturesToUseMipmap(boolean mipMap) {
        MoonlightClient.setMipMap(mipMap);
    }
}