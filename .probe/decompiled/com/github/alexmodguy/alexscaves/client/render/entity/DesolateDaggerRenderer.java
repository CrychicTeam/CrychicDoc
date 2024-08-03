package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.server.entity.item.DesolateDaggerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class DesolateDaggerRenderer extends EntityRenderer<DesolateDaggerEntity> {

    public DesolateDaggerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.0F;
    }

    public void render(DesolateDaggerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn) {
        super.render(entity, entityYaw, partialTicks, poseStack, source, lightIn);
        float ageInTicks = partialTicks + (float) entity.f_19797_;
        double stab = Math.max((double) entity.getStab(partialTicks), Math.sin((double) (ageInTicks * 0.1F)) * 0.2F);
        poseStack.pushPose();
        poseStack.translate(0.0, 0.5, 0.0);
        poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_()) + 90.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees((float) ((double) Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_()) + 5.0 * Math.sin((double) (ageInTicks * 0.2F)))));
        poseStack.mulPose(Axis.ZN.rotationDegrees(45.0F));
        poseStack.translate(-0.5, -0.5, -0.5);
        BakedModel bakedmodel = Minecraft.getInstance().getItemRenderer().getModel(entity.daggerRenderStack, entity.m_9236_(), null, 0);
        float f = 1.0F;
        float f1 = 0.0F;
        float f2 = 0.0F;
        float startAlpha = ageInTicks < 3.0F ? 0.0F : (ageInTicks - 3.0F) / 6.0F;
        float alpha = (float) Math.min(0.6F + stab, (double) Math.min(1.0F, startAlpha));
        int redOverlay = OverlayTexture.pack(OverlayTexture.u(0.0F), OverlayTexture.v(true));
        poseStack.translate(stab, stab + Math.cos((double) (ageInTicks * 0.1F)) * 0.2F, 0.0);
        for (RenderType rt : bakedmodel.getRenderTypes(entity.daggerRenderStack, false)) {
            renderModel(poseStack.last(), source.getBuffer(Sheets.translucentItemSheet()), alpha, null, bakedmodel, f, f1, f2, 240, redOverlay, ModelData.EMPTY, rt);
        }
        poseStack.popPose();
    }

    public static void renderModel(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float alpha, @Nullable BlockState blockState2, BakedModel bakedModel3, float float4, float float5, float float6, int int7, int int8, ModelData modelData, RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;
        for (Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, alpha, bakedModel3.getQuads(blockState2, direction, randomsource, modelData, renderType), int7, int8);
        }
        randomsource.setSeed(42L);
        renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, alpha, bakedModel3.getQuads(blockState2, (Direction) null, randomsource, modelData, renderType), int7, int8);
    }

    private static void renderQuadList(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float alpha, List<BakedQuad> listBakedQuad5, int int6, int int7) {
        for (BakedQuad bakedquad : listBakedQuad5) {
            float f = Mth.clamp(float2, 0.0F, 1.0F);
            float f1 = Mth.clamp(float3, 0.0F, 1.0F);
            float f2 = Mth.clamp(float4, 0.0F, 1.0F);
            vertexConsumer1.putBulkData(poseStackPose0, bakedquad, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, f, f1, f2, alpha, new int[] { int6, int6, int6, int6 }, int7, false);
        }
    }

    public ResourceLocation getTextureLocation(DesolateDaggerEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}