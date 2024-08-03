package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.MechanicalPumpBottomBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class MechanicalPumpBlockEntityRenderer implements BlockEntityRenderer<MechanicalPumpBottomBlockEntity> {

    public static BakedModel pistonBottom;

    public static BakedModel pistonTop;

    public MechanicalPumpBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(MechanicalPumpBottomBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockPos pos = blockEntity.m_58899_().below();
        Level level = blockEntity.m_58904_();
        double progress = (double) ((float) blockEntity.totalProgress * partialTick + (float) blockEntity.lastProgress * (1.0F - partialTick));
        double amountUp = Math.abs(Math.sin(Math.PI * progress / 400.0));
        if (level.getBlockState(pos).m_60795_()) {
            packedLight = LightTexture.pack(level.m_45517_(LightLayer.BLOCK, pos), level.m_45517_(LightLayer.SKY, pos));
        }
        BlockState blockState = level.getBlockState(blockEntity.m_58899_());
        if (blockState.m_60734_() == RegistryManager.MECHANICAL_PUMP.get()) {
            poseStack.pushPose();
            poseStack.translate(0.0, 1.0 + amountUp * 0.25, 0.0);
            if (pistonBottom != null) {
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, pistonBottom, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
            }
            poseStack.translate(0.0, amountUp * 0.25, 0.0);
            if (pistonTop != null) {
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, pistonTop, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
            }
            poseStack.popPose();
        }
    }
}