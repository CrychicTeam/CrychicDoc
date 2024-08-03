package net.blay09.mods.waystones.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blay09.mods.waystones.block.PortstoneBlock;
import net.blay09.mods.waystones.block.entity.PortstoneBlockEntity;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class PortstoneRenderer implements BlockEntityRenderer<PortstoneBlockEntity> {

    private static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft", "waystone_overlays/portstone"));

    private static ItemStack warpStoneItem;

    private final PortstoneModel model;

    public PortstoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new PortstoneModel(context.bakeLayer(ModRenderers.portstoneModel));
    }

    public void render(PortstoneBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        Level level = tileEntity.m_58904_();
        BlockState state = tileEntity.m_58900_();
        if (level != null && state.m_61143_(PortstoneBlock.HALF) == DoubleBlockHalf.LOWER) {
            Direction facing = (Direction) state.m_61143_(PortstoneBlock.FACING);
            if (warpStoneItem == null) {
                warpStoneItem = new ItemStack(ModItems.warpStone);
                warpStoneItem.enchant(Enchantments.UNBREAKING, 1);
            }
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.0F, 0.5F);
            poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
            poseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
            poseStack.translate(0.0F, -2.0F, 0.0F);
            float scale = 1.01F;
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.scale(scale, scale, scale);
            VertexConsumer vertexBuilder = MATERIAL.buffer(buffer, RenderType::m_110452_);
            int light = WaystonesConfig.getActive().client.disableTextGlow ? combinedLightIn : 15728880;
            int overlay = WaystonesConfig.getActive().client.disableTextGlow ? combinedOverlayIn : OverlayTexture.NO_OVERLAY;
            long gameTime = level.getGameTime();
            float min = 0.7F;
            float color = (float) Math.max((double) min, (double) min + Math.abs(Math.sin((double) ((float) gameTime / 32.0F))) * (double) (1.0F - min));
            this.model.renderToBuffer(poseStack, vertexBuilder, light, overlay, color, color, color, 1.0F);
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.0F, 0.5F);
            poseStack.mulPose(Axis.YN.rotationDegrees(facing.toYRot()));
            poseStack.translate(0.0F, 0.0F, 0.15F);
            poseStack.mulPose(Axis.XN.rotationDegrees(25.0F));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.03125F, 0.0F, 0.0F);
            Minecraft.getInstance().getItemRenderer().renderStatic(warpStoneItem, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, poseStack, buffer, level, 0);
            poseStack.popPose();
        }
    }
}