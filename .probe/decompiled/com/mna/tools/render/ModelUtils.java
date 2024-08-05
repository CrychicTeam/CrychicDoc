package com.mna.tools.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class ModelUtils {

    private static final RandomSource rnd = RandomSource.create(1234L);

    public static void renderModel(MultiBufferSource buffer, Level world, BlockPos pos, BlockState state, ResourceLocation modelLoc, PoseStack stack, int combinedLight, int combinedOverlay) {
        VertexConsumer vertexBuilder = buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        ModelData worldModelData = world.getModelDataManager().getAt(pos);
        ModelData data = model.getModelData(world, pos, state, worldModelData == null ? ModelData.EMPTY : worldModelData);
        for (BakedQuad quad : model.getQuads(state, null, rnd, data, null)) {
            vertexBuilder.putBulkData(stack.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay, true);
        }
    }

    public static void renderModel(MultiBufferSource buffer, Level world, BlockPos pos, BlockState state, ResourceLocation modelLoc, PoseStack stack, int combinedLight, int combinedOverlay, float[] rgba) {
        VertexConsumer vertexBuilder = buffer.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        ModelData worldModelData = world.getModelDataManager().getAt(pos);
        ModelData data = model.getModelData(world, pos, state, worldModelData == null ? ModelData.EMPTY : worldModelData);
        for (BakedQuad quad : model.getQuads(state, null, rnd, data, null)) {
            vertexBuilder.putBulkData(stack.last(), quad, rgba[0], rgba[1], rgba[2], rgba[3], combinedLight, combinedOverlay, true);
        }
    }

    public static void renderEntityModel(VertexConsumer vertexBuilder, Level world, ResourceLocation modelLoc, PoseStack stack, int combinedLight, int combinedOverlay) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        ModelData data = ModelData.EMPTY;
        for (BakedQuad quad : model.getQuads(null, null, rnd, data, null)) {
            vertexBuilder.putBulkData(stack.last(), quad, 1.0F, 1.0F, 1.0F, 1.0F, combinedLight, combinedOverlay, true);
        }
    }

    public static void renderEntityModel(VertexConsumer vertexBuilder, Level world, ResourceLocation modelLoc, PoseStack stack, int combinedLight, int combinedOverlay, float[] rgb, float a) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        ModelData data = ModelData.EMPTY;
        for (BakedQuad quad : model.getQuads(null, null, rnd, data, null)) {
            vertexBuilder.putBulkData(stack.last(), quad, rgb[0], rgb[1], rgb[2], a, combinedLight, combinedOverlay, false);
        }
    }

    public static void renderModel(VertexConsumer vertexBuilder, Level world, BlockPos pos, BlockState state, ResourceLocation modelLoc, PoseStack stack, int combinedLight, int combinedOverlay) {
        renderModel(vertexBuilder, world, pos, state, modelLoc, stack, new float[] { 1.0F, 1.0F, 1.0F, 1.0F }, combinedLight, combinedOverlay);
    }

    public static void renderModel(VertexConsumer vertexBuilder, Level world, BlockPos pos, BlockState state, ResourceLocation modelLoc, PoseStack stack, float[] argb, int combinedLight, int combinedOverlay) {
        BakedModel model = Minecraft.getInstance().getModelManager().getModel(modelLoc);
        for (BakedQuad quad : model.getQuads(state, null, rnd, ModelData.EMPTY, null)) {
            vertexBuilder.putBulkData(stack.last(), quad, argb[1], argb[2], argb[3], argb[0], combinedLight, combinedOverlay, true);
        }
    }
}