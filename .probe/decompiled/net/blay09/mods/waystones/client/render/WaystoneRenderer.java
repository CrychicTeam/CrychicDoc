package net.blay09.mods.waystones.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Objects;
import net.blay09.mods.waystones.block.WaystoneBlock;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntity;
import net.blay09.mods.waystones.client.ModRenderers;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class WaystoneRenderer implements BlockEntityRenderer<WaystoneBlockEntity> {

    private static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("minecraft", "waystone_overlays/waystone_active"));

    private final SharestoneModel model;

    public WaystoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new SharestoneModel(context.bakeLayer(ModRenderers.waystoneModel));
    }

    public void render(WaystoneBlockEntity tileEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        BlockState state = tileEntity.m_58900_();
        if (state.m_61143_(WaystoneBlock.HALF) == DoubleBlockHalf.LOWER) {
            float angle = ((Direction) state.m_61143_(WaystoneBlock.FACING)).toYRot();
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 0.0F, 0.5F);
            matrixStack.mulPose(Axis.YP.rotationDegrees(angle));
            matrixStack.mulPose(Axis.XN.rotationDegrees(180.0F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            Player player = Minecraft.getInstance().player;
            boolean isActivated = PlayerWaystoneManager.isWaystoneActivated((Player) Objects.requireNonNull(player), tileEntity.getWaystone());
            if (isActivated) {
                matrixStack.scale(1.05F, 1.05F, 1.05F);
                VertexConsumer vertexBuilder = MATERIAL.buffer(buffer, RenderType::m_110452_);
                int light = WaystonesConfig.getActive().client.disableTextGlow ? combinedLightIn : 15728880;
                int overlay = WaystonesConfig.getActive().client.disableTextGlow ? combinedOverlayIn : OverlayTexture.NO_OVERLAY;
                this.model.renderToBuffer(matrixStack, vertexBuilder, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            matrixStack.popPose();
        }
    }
}