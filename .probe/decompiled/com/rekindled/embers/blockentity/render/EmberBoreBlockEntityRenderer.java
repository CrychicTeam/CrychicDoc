package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.EmberBoreBladeRenderEvent;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.blockentity.EmberBoreBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.ModelData;

public class EmberBoreBlockEntityRenderer implements BlockEntityRenderer<EmberBoreBlockEntity> {

    public static BakedModel blades;

    public EmberBoreBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(EmberBoreBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        float angle = blockEntity.angle;
        float lastAngle = blockEntity.lastAngle;
        BlockPos pos = blockEntity.m_58899_().below();
        Level level = blockEntity.m_58904_();
        if (level.getBlockState(pos).m_60795_()) {
            packedLight = LightTexture.pack(level.m_45517_(LightLayer.BLOCK, pos), level.m_45517_(LightLayer.SKY, pos));
        }
        BlockState blockState = level.getBlockState(blockEntity.m_58899_());
        if (blockState.m_60734_() == RegistryManager.EMBER_BORE.get()) {
            poseStack.pushPose();
            poseStack.translate(0.5, -0.5, 0.5);
            if (blockState.m_61143_(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.X) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(partialTick * angle + (1.0F - partialTick) * lastAngle));
            } else {
                poseStack.mulPose(Axis.XP.rotationDegrees(partialTick * angle + (1.0F - partialTick) * lastAngle));
                poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            poseStack.translate(-0.5, -0.5, -0.5);
            if (blades != null) {
                blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, blades, 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
            }
            UpgradeUtil.throwEvent(blockEntity, new EmberBoreBladeRenderEvent(blockEntity, poseStack, blockState, bufferSource, packedLight, packedOverlay), blockEntity.upgrades);
            poseStack.popPose();
        }
    }
}