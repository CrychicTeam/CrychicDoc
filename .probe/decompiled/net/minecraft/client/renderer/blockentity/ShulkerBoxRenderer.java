package net.minecraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ShulkerBoxRenderer implements BlockEntityRenderer<ShulkerBoxBlockEntity> {

    private final ShulkerModel<?> model;

    public ShulkerBoxRenderer(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        this.model = new ShulkerModel(blockEntityRendererProviderContext0.bakeLayer(ModelLayers.SHULKER));
    }

    public void render(ShulkerBoxBlockEntity shulkerBoxBlockEntity0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        Direction $$6 = Direction.UP;
        if (shulkerBoxBlockEntity0.m_58898_()) {
            BlockState $$7 = shulkerBoxBlockEntity0.m_58904_().getBlockState(shulkerBoxBlockEntity0.m_58899_());
            if ($$7.m_60734_() instanceof ShulkerBoxBlock) {
                $$6 = (Direction) $$7.m_61143_(ShulkerBoxBlock.FACING);
            }
        }
        DyeColor $$8 = shulkerBoxBlockEntity0.getColor();
        Material $$9;
        if ($$8 == null) {
            $$9 = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
        } else {
            $$9 = (Material) Sheets.SHULKER_TEXTURE_LOCATION.get($$8.getId());
        }
        poseStack2.pushPose();
        poseStack2.translate(0.5F, 0.5F, 0.5F);
        float $$11 = 0.9995F;
        poseStack2.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack2.mulPose($$6.getRotation());
        poseStack2.scale(1.0F, -1.0F, -1.0F);
        poseStack2.translate(0.0F, -1.0F, 0.0F);
        ModelPart $$12 = this.model.getLid();
        $$12.setPos(0.0F, 24.0F - shulkerBoxBlockEntity0.getProgress(float1) * 0.5F * 16.0F, 0.0F);
        $$12.yRot = 270.0F * shulkerBoxBlockEntity0.getProgress(float1) * (float) (Math.PI / 180.0);
        VertexConsumer $$13 = $$9.buffer(multiBufferSource3, RenderType::m_110458_);
        this.model.m_7695_(poseStack2, $$13, int4, int5, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack2.popPose();
    }
}