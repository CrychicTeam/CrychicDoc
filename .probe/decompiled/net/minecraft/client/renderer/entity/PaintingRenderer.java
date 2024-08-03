package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PaintingRenderer extends EntityRenderer<Painting> {

    public PaintingRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public void render(Painting painting0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.mulPose(Axis.YP.rotationDegrees(180.0F - float1));
        PaintingVariant $$6 = painting0.getVariant().value();
        float $$7 = 0.0625F;
        poseStack3.scale(0.0625F, 0.0625F, 0.0625F);
        VertexConsumer $$8 = multiBufferSource4.getBuffer(RenderType.entitySolid(this.getTextureLocation(painting0)));
        PaintingTextureManager $$9 = Minecraft.getInstance().getPaintingTextures();
        this.renderPainting(poseStack3, $$8, painting0, $$6.getWidth(), $$6.getHeight(), $$9.get($$6), $$9.getBackSprite());
        poseStack3.popPose();
        super.render(painting0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(Painting painting0) {
        return Minecraft.getInstance().getPaintingTextures().getBackSprite().atlasLocation();
    }

    private void renderPainting(PoseStack poseStack0, VertexConsumer vertexConsumer1, Painting painting2, int int3, int int4, TextureAtlasSprite textureAtlasSprite5, TextureAtlasSprite textureAtlasSprite6) {
        PoseStack.Pose $$7 = poseStack0.last();
        Matrix4f $$8 = $$7.pose();
        Matrix3f $$9 = $$7.normal();
        float $$10 = (float) (-int3) / 2.0F;
        float $$11 = (float) (-int4) / 2.0F;
        float $$12 = 0.5F;
        float $$13 = textureAtlasSprite6.getU0();
        float $$14 = textureAtlasSprite6.getU1();
        float $$15 = textureAtlasSprite6.getV0();
        float $$16 = textureAtlasSprite6.getV1();
        float $$17 = textureAtlasSprite6.getU0();
        float $$18 = textureAtlasSprite6.getU1();
        float $$19 = textureAtlasSprite6.getV0();
        float $$20 = textureAtlasSprite6.getV(1.0);
        float $$21 = textureAtlasSprite6.getU0();
        float $$22 = textureAtlasSprite6.getU(1.0);
        float $$23 = textureAtlasSprite6.getV0();
        float $$24 = textureAtlasSprite6.getV1();
        int $$25 = int3 / 16;
        int $$26 = int4 / 16;
        double $$27 = 16.0 / (double) $$25;
        double $$28 = 16.0 / (double) $$26;
        for (int $$29 = 0; $$29 < $$25; $$29++) {
            for (int $$30 = 0; $$30 < $$26; $$30++) {
                float $$31 = $$10 + (float) (($$29 + 1) * 16);
                float $$32 = $$10 + (float) ($$29 * 16);
                float $$33 = $$11 + (float) (($$30 + 1) * 16);
                float $$34 = $$11 + (float) ($$30 * 16);
                int $$35 = painting2.m_146903_();
                int $$36 = Mth.floor(painting2.m_20186_() + (double) (($$33 + $$34) / 2.0F / 16.0F));
                int $$37 = painting2.m_146907_();
                Direction $$38 = painting2.m_6350_();
                if ($$38 == Direction.NORTH) {
                    $$35 = Mth.floor(painting2.m_20185_() + (double) (($$31 + $$32) / 2.0F / 16.0F));
                }
                if ($$38 == Direction.WEST) {
                    $$37 = Mth.floor(painting2.m_20189_() - (double) (($$31 + $$32) / 2.0F / 16.0F));
                }
                if ($$38 == Direction.SOUTH) {
                    $$35 = Mth.floor(painting2.m_20185_() - (double) (($$31 + $$32) / 2.0F / 16.0F));
                }
                if ($$38 == Direction.EAST) {
                    $$37 = Mth.floor(painting2.m_20189_() + (double) (($$31 + $$32) / 2.0F / 16.0F));
                }
                int $$39 = LevelRenderer.getLightColor(painting2.m_9236_(), new BlockPos($$35, $$36, $$37));
                float $$40 = textureAtlasSprite5.getU($$27 * (double) ($$25 - $$29));
                float $$41 = textureAtlasSprite5.getU($$27 * (double) ($$25 - ($$29 + 1)));
                float $$42 = textureAtlasSprite5.getV($$28 * (double) ($$26 - $$30));
                float $$43 = textureAtlasSprite5.getV($$28 * (double) ($$26 - ($$30 + 1)));
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$41, $$42, -0.5F, 0, 0, -1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$40, $$42, -0.5F, 0, 0, -1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$40, $$43, -0.5F, 0, 0, -1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$41, $$43, -0.5F, 0, 0, -1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$14, $$15, 0.5F, 0, 0, 1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$13, $$15, 0.5F, 0, 0, 1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$13, $$16, 0.5F, 0, 0, 1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$14, $$16, 0.5F, 0, 0, 1, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$17, $$19, -0.5F, 0, 1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$18, $$19, -0.5F, 0, 1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$18, $$20, 0.5F, 0, 1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$17, $$20, 0.5F, 0, 1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$17, $$19, 0.5F, 0, -1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$18, $$19, 0.5F, 0, -1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$18, $$20, -0.5F, 0, -1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$17, $$20, -0.5F, 0, -1, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$22, $$23, 0.5F, -1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$22, $$24, 0.5F, -1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$34, $$21, $$24, -0.5F, -1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$31, $$33, $$21, $$23, -0.5F, -1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$22, $$23, -0.5F, 1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$22, $$24, -0.5F, 1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$34, $$21, $$24, 0.5F, 1, 0, 0, $$39);
                this.vertex($$8, $$9, vertexConsumer1, $$32, $$33, $$21, $$23, 0.5F, 1, 0, 0, $$39);
            }
        }
    }

    private void vertex(Matrix4f matrixF0, Matrix3f matrixF1, VertexConsumer vertexConsumer2, float float3, float float4, float float5, float float6, float float7, int int8, int int9, int int10, int int11) {
        vertexConsumer2.vertex(matrixF0, float3, float4, float7).color(255, 255, 255, 255).uv(float5, float6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int11).normal(matrixF1, (float) int8, (float) int9, (float) int10).endVertex();
    }
}