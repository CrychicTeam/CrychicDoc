package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;

public class NuclearBombRenderer extends EntityRenderer<NuclearBombEntity> {

    public NuclearBombRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.f_114477_ = 0.0F;
    }

    public void render(NuclearBombEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int lightIn) {
        super.render(entity, entityYaw, partialTicks, poseStack, source, lightIn);
        float progress = ((float) entity.getTime() + partialTicks) / 300.0F;
        float expandScale = 1.0F + (float) Math.sin((double) (progress * progress) * Math.PI) * 0.5F;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees((float) (Math.cos((double) entity.f_19797_ * 3.25) * 1.2F * (double) progress * Math.PI)));
        poseStack.scale(1.0F + progress * 0.03F, 1.0F, 1.0F + progress * 0.03F);
        poseStack.pushPose();
        poseStack.scale(expandScale, expandScale - progress * 0.3F, expandScale);
        poseStack.translate(-0.5, 0.0, -0.5);
        BlockState state = ACBlockRegistry.NUCLEAR_BOMB.get().defaultBlockState();
        BakedModel bakedmodel = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        float f = 1.0F - progress * 0.5F;
        float f1 = 1.0F + progress;
        float f2 = 1.0F - progress;
        for (RenderType rt : bakedmodel.getRenderTypes(state, RandomSource.create(42L), ModelData.EMPTY)) {
            renderModel(poseStack.last(), source.getBuffer(RenderTypeHelper.getEntityRenderType(rt, false)), state, bakedmodel, f, f1, f2, 240, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt);
        }
        poseStack.popPose();
        poseStack.popPose();
    }

    public static void renderModel(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, @Nullable BlockState blockState2, BakedModel bakedModel3, float float4, float float5, float float6, int int7, int int8, ModelData modelData, RenderType renderType) {
        RandomSource randomsource = RandomSource.create();
        long i = 42L;
        for (Direction direction : Direction.values()) {
            randomsource.setSeed(42L);
            renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, bakedModel3.getQuads(blockState2, direction, randomsource, modelData, renderType), int7, int8);
        }
        randomsource.setSeed(42L);
        renderQuadList(poseStackPose0, vertexConsumer1, float4, float5, float6, bakedModel3.getQuads(blockState2, (Direction) null, randomsource, modelData, renderType), int7, int8);
    }

    private static void renderQuadList(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, List<BakedQuad> listBakedQuad5, int int6, int int7) {
        for (BakedQuad bakedquad : listBakedQuad5) {
            float f = Mth.clamp(float2, 0.0F, 1.0F);
            float f1 = Mth.clamp(float3, 0.0F, 1.0F);
            float f2 = Mth.clamp(float4, 0.0F, 1.0F);
            vertexConsumer1.putBulkData(poseStackPose0, bakedquad, f, f1, f2, int6, int7);
        }
    }

    public ResourceLocation getTextureLocation(NuclearBombEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}