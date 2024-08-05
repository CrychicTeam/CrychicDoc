package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.blockentity.AtmosphericBellowsBlockEntity;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class AtmosphericBellowsBlockEntityRenderer implements BlockEntityRenderer<AtmosphericBellowsBlockEntity> {

    public static float length = 120.0F;

    public static float blowLength = length / 3.0F;

    public static float suckLength = length - blowLength;

    public static BakedModel top;

    public static BakedModel leather;

    public static Random random = new Random();

    public AtmosphericBellowsBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(AtmosphericBellowsBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockState blockState = blockEntity.m_58904_().getBlockState(blockEntity.m_58899_());
        random.setSeed(blockEntity.m_58899_().asLong());
        float ticks = ((float) EmbersClientEvents.ticks + partialTick + random.nextFloat(length)) % length;
        double magnitude = 1.0;
        if (ticks < blowLength) {
            magnitude = (double) (ticks / blowLength);
        } else {
            magnitude = 1.0 - (double) ((ticks - blowLength) / suckLength);
        }
        poseStack.pushPose();
        poseStack.translate(0.0, magnitude * -0.1875, 0.0);
        if (leather != null) {
            blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, leather, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
        }
        poseStack.translate(0.0, magnitude * -0.1875, 0.0);
        if (top != null) {
            blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, top, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
        }
        poseStack.popPose();
    }
}