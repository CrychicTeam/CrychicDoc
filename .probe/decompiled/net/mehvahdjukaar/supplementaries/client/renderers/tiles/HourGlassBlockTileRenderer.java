package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.HourGlassBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.HourGlassBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class HourGlassBlockTileRenderer implements BlockEntityRenderer<HourGlassBlockTile> {

    public HourGlassBlockTileRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public int getViewDistance() {
        return 48;
    }

    public static void renderSand(PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, ResourceLocation texture, float height, Direction dir) {
        int color = 16777215;
        if (MiscUtils.FESTIVITY.isAprilsFool()) {
            color = ColorHelper.getRainbowColor(1.0F);
            texture = ModTextures.WHITE_CONCRETE_TEXTURE;
        }
        Material mat = ModMaterials.get(texture);
        VertexConsumer builder = mat.buffer(bufferIn, RenderType::m_110470_);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Quaternionf q = dir.getRotation();
        poseStack.mulPose(q);
        q = new Quaternionf(q);
        q.conjugate();
        if (height != 0.0F) {
            poseStack.pushPose();
            poseStack.translate(0.0, -0.25, 0.0);
            poseStack.mulPose(q);
            poseStack.translate(0.0, -0.125, 0.0);
            float h1 = height * 0.25F;
            VertexUtil.addCube(builder, poseStack, 0.375F, 0.3125F, 0.25F, h1, combinedLightIn, color);
            if (dir == Direction.DOWN) {
                poseStack.translate(0.0F, -h1 - 0.25F, 0.0F);
                VertexUtil.addCube(builder, poseStack, 0.375F, 0.3125F, 0.0625F, h1 + 0.25F, combinedLightIn, color, 1.0F, false, false, false);
            }
            poseStack.popPose();
        }
        if (height != 1.0F) {
            poseStack.pushPose();
            poseStack.translate(0.0, 0.25, 0.0);
            poseStack.mulPose(q);
            poseStack.translate(0.0, -0.125, 0.0);
            float h2 = (1.0F - height) * 0.25F;
            VertexUtil.addCube(builder, poseStack, 0.375F, 0.3125F, 0.25F, h2, combinedLightIn, color);
            if (dir == Direction.UP) {
                poseStack.translate(0.0, (double) (-h2) - 0.25, 0.0);
                VertexUtil.addCube(builder, poseStack, 0.375F, 0.3125F, 0.0625F, h2 + 0.25F, combinedLightIn, color, 1.0F, false, false, false);
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public void render(HourGlassBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tile.getSandData().isEmpty()) {
            Direction dir = (Direction) tile.m_58900_().m_61143_(HourGlassBlock.FACING);
            renderSand(poseStack, bufferIn, combinedLightIn, tile.getTexture(), tile.getProgress(partialTicks), dir);
        }
    }
}