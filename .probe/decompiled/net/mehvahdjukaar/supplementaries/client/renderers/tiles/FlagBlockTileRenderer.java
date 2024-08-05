package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import java.util.List;
import java.util.Map;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FlagBlockTile;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.joml.Quaternionf;

public class FlagBlockTileRenderer implements BlockEntityRenderer<FlagBlockTile> {

    private final Minecraft minecraft = Minecraft.getInstance();

    private final ModelPart flag;

    public FlagBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(ModelLayers.BANNER);
        this.flag = modelpart.getChild("flag");
    }

    @Override
    public int getViewDistance() {
        return 128;
    }

    private void renderBanner(float ang, PoseStack matrixStack, MultiBufferSource bufferSource, int light, int pPackedOverlay, List<Pair<Holder<BannerPattern>, DyeColor>> list) {
        matrixStack.pushPose();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        matrixStack.mulPose(Axis.YP.rotationDegrees(0.05F * ang));
        this.flag.xRot = (float) (Math.PI / 2);
        this.flag.yRot = (float) Math.PI;
        this.flag.zRot = (float) (Math.PI / 2);
        this.flag.y = -12.0F;
        this.flag.x = 1.5F;
        BannerRenderer.renderPatterns(matrixStack, bufferSource, light, pPackedOverlay, this.flag, ModelBakery.BANNER_BASE, true, list);
        matrixStack.popPose();
    }

    public void render(FlagBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        List<Pair<Holder<BannerPattern>, DyeColor>> list = tile.getPatterns();
        if (list != null) {
            int lu = combinedLightIn & 65535;
            int lv = combinedLightIn >> 16 & 65535;
            int w = 24;
            int h = 16;
            poseStack.pushPose();
            poseStack.translate(0.5, 0.0, 0.5);
            poseStack.mulPose(RotHlpr.rot(tile.getDirection().getOpposite()));
            poseStack.translate(0.0F, 0.0F, 0.0625F);
            long time = tile.m_58904_().getGameTime();
            double l = (Double) ClientConfigs.Blocks.FLAG_WAVELENGTH.get();
            long period = (long) ((Integer) ClientConfigs.Blocks.FLAG_PERIOD.get()).intValue();
            double wavyness = (Double) ClientConfigs.Blocks.FLAG_AMPLITUDE.get();
            double invdamping = (Double) ClientConfigs.Blocks.FLAG_AMPLITUDE_INCREMENT.get();
            BlockPos bp = tile.m_58899_();
            float t = ((float) Math.floorMod((long) bp.m_123341_() * 7L + (long) bp.m_123343_() * 13L + time, period) + partialTicks) / (float) period;
            if ((Boolean) ClientConfigs.Blocks.FLAG_BANNER.get()) {
                float ang = (float) ((wavyness + invdamping * (double) w) * (double) Mth.sin((float) ((double) w / l - (double) (t * 2.0F * (float) Math.PI))));
                this.renderBanner(ang, poseStack, bufferIn, combinedLightIn, combinedOverlayIn, list);
            } else {
                int segmentLen = this.minecraft.options.graphicsMode().get().getId() >= ((ClientConfigs.GraphicsFanciness) ClientConfigs.Blocks.FLAG_FANCINESS.get()).ordinal() ? 1 : w;
                float oldAng = 0.0F;
                for (int dX = 0; dX < w; dX += segmentLen) {
                    float ang = (float) ((wavyness + invdamping * (double) dX) * (double) Mth.sin((float) ((double) dX / l - (double) (t * 2.0F * (float) Math.PI))));
                    renderPatterns(bufferIn, poseStack, list, lu, lv, dX, w, h, segmentLen, ang, oldAng);
                    poseStack.mulPose(Axis.YP.rotationDegrees(ang));
                    poseStack.translate(0.0F, 0.0F, (float) segmentLen / 16.0F);
                    poseStack.mulPose(Axis.YP.rotationDegrees(-ang));
                    oldAng = ang;
                }
            }
            poseStack.popPose();
        }
    }

    public static void renderPatterns(PoseStack matrixStackIn, MultiBufferSource bufferIn, List<Pair<Holder<BannerPattern>, DyeColor>> list, int combinedLightIn) {
        int lu = combinedLightIn & 65535;
        int lv = combinedLightIn >> 16 & 65535;
        renderPatterns(bufferIn, matrixStackIn, list, lu, lv, 0, 24, 16, 24, 0.0F, 0.0F);
    }

    private static void renderPatterns(MultiBufferSource bufferIn, PoseStack matrixStackIn, List<Pair<Holder<BannerPattern>, DyeColor>> list, int lu, int lv, int dX, int w, int h, int segmentlen, float ang, float oldAng) {
        for (int p = 0; p < list.size(); p++) {
            Material material = (Material) ((Map) ModMaterials.FLAG_MATERIALS.get()).get(((Holder) ((Pair) list.get(p)).getFirst()).value());
            if (material != null) {
                VertexConsumer builder = material.buffer(bufferIn, p == 0 ? RenderType::m_110446_ : RenderType::m_110482_);
                matrixStackIn.pushPose();
                float[] color = ((DyeColor) ((Pair) list.get(p)).getSecond()).getTextureDiffuseColors();
                float b = color[2];
                float g = color[1];
                float r = color[0];
                renderCurvedSegment(builder, matrixStackIn, ang, oldAng, dX, segmentlen, h, lu, lv, dX + segmentlen >= w, r, g, b);
                matrixStackIn.popPose();
            }
        }
    }

    private static void renderCurvedSegment(VertexConsumer builder, PoseStack matrixStack, float angle, float oldAng, int dX, int length, int height, int lu, int lv, boolean end, float r, float g, float b) {
        float textW = 32.0F;
        float textH = 16.0F;
        float u = (float) dX / textW;
        float v = 0.0F;
        float maxV = v + (float) height / textH;
        float maxU = u + (float) length / textW;
        float w = 0.0625F;
        float hw = w / 2.0F;
        float l = (float) length / 16.0F;
        float h = (float) height / 16.0F;
        float pU = maxU - 1.0F / textW;
        float pV = maxV - w;
        Quaternionf rot = Axis.YP.rotationDegrees(angle);
        Quaternionf oldRot = Axis.YP.rotationDegrees(oldAng);
        Quaternionf rotInc = Axis.YP.rotationDegrees(angle - oldAng);
        Quaternionf rotInv = Axis.YP.rotationDegrees(-angle);
        int nx = 1;
        int nz = 0;
        int ny = 0;
        matrixStack.pushPose();
        matrixStack.translate(hw, 0.0F, 0.0F);
        matrixStack.mulPose(oldRot);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, u, maxV, r, g, b, 1.0F, lu, lv, (float) nx, (float) ny, (float) nz);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, u, v, r, g, b, 1.0F, lu, lv, (float) nx, (float) ny, (float) nz);
        matrixStack.mulPose(rotInc);
        matrixStack.translate(0.0F, 0.0F, l);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, maxU, v, r, g, b, 1.0F, lu, lv, (float) nx, (float) ny, (float) nz);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, maxU, maxV, r, g, b, 1.0F, lu, lv, (float) nx, (float) ny, (float) nz);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-hw, 0.0F, 0.0F);
        matrixStack.mulPose(oldRot);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, u, v, r, g, b, 1.0F, lu, lv, (float) (-nx), (float) ny, (float) nz);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, u, maxV, r, g, b, 1.0F, lu, lv, (float) (-nx), (float) ny, (float) nz);
        matrixStack.mulPose(rotInc);
        matrixStack.translate(0.0F, 0.0F, l);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, maxU, maxV, r, g, b, 1.0F, lu, lv, (float) (-nx), (float) ny, (float) nz);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, maxU, v, r, g, b, 1.0F, lu, lv, (float) (-nx), (float) ny, (float) nz);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(hw, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, u, v, r, g, b, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
        matrixStack.translate(-w, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, u, w, r, g, b, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
        matrixStack.mulPose(rot);
        matrixStack.translate(0.0F, 0.0F, l);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, maxU, w, r, g, b, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
        matrixStack.mulPose(rotInv);
        matrixStack.translate(w, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, maxU, v, r, g, b, 1.0F, lu, lv, 0.0F, 1.0F, 0.0F);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-hw, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, u, pV, r, g, b, 1.0F, lu, lv, 0.0F, -1.0F, 0.0F);
        matrixStack.translate(w, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, u, maxV, r, g, b, 1.0F, lu, lv, 0.0F, -1.0F, 0.0F);
        matrixStack.mulPose(rot);
        matrixStack.translate(0.0F, 0.0F, l);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, maxU, maxV, r, g, b, 1.0F, lu, lv, 0.0F, -1.0F, 0.0F);
        matrixStack.mulPose(rotInv);
        matrixStack.translate(-w, 0.0F, 0.0F);
        VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, maxU, pV, r, g, b, 1.0F, lu, lv, 0.0F, -1.0F, 0.0F);
        matrixStack.popPose();
        if (end) {
            matrixStack.pushPose();
            matrixStack.mulPose(rot);
            matrixStack.translate(0.0F, 0.0F, l);
            matrixStack.mulPose(rotInv);
            matrixStack.translate(-hw, 0.0F, 0.0F);
            VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, pU, v, r, g, b, 1.0F, lu, lv, 0.0F, 0.0F, 1.0F);
            VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, pU, maxV, r, g, b, 1.0F, lu, lv, 0.0F, 0.0F, 1.0F);
            matrixStack.translate(w, 0.0F, 0.0F);
            VertexUtil.vert(builder, matrixStack, 0.0F, 0.0F, 0.0F, maxU, maxV, r, g, b, 1.0F, lu, lv, 0.0F, 0.0F, 1.0F);
            VertexUtil.vert(builder, matrixStack, 0.0F, h, 0.0F, maxU, v, r, g, b, 1.0F, lu, lv, 0.0F, 0.0F, 1.0F);
            matrixStack.popPose();
        }
    }
}