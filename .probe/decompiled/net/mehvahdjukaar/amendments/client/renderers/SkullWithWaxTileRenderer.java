package net.mehvahdjukaar.amendments.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.mehvahdjukaar.amendments.AmendmentsClient;
import net.mehvahdjukaar.amendments.common.block.WallCandleSkullBlock;
import net.mehvahdjukaar.amendments.common.tile.EnhancedSkullBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class SkullWithWaxTileRenderer<T extends EnhancedSkullBlockTile> implements BlockEntityRenderer<T> {

    private final BlockEntityRenderDispatcher dispatcher;

    protected final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

    protected final SkullModelBase overlay;

    protected SkullWithWaxTileRenderer(BlockEntityRendererProvider.Context context) {
        this.overlay = new SkullModel(context.bakeLayer(AmendmentsClient.SKULL_CANDLE_OVERLAY));
        this.dispatcher = context.getBlockEntityRenderDispatcher();
    }

    public void render(T tile, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int pCombinedLight, int pCombinedOverlay) {
        BlockEntity inner = tile.getSkullTile();
        if (inner != null) {
            boolean wall = false;
            BlockState state = tile.m_58900_();
            float yaw;
            if (state.m_61138_(WallCandleSkullBlock.FACING)) {
                yaw = ((Direction) state.m_61143_(WallCandleSkullBlock.FACING)).toYRot();
                wall = true;
            } else {
                yaw = -22.5F * (float) ((Integer) state.m_61143_(SkullBlock.ROTATION) - (Integer) inner.getBlockState().m_61143_(SkullBlock.ROTATION));
            }
            if (!wall) {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                poseStack.translate(-0.5, -0.5, -0.5);
            } else {
                poseStack.translate(0.0, -0.25, 0.0);
            }
            this.renderInner(inner, pPartialTicks, poseStack, buffer, pCombinedLight, pCombinedOverlay);
            if (wall) {
                poseStack.translate(0.5, 0.5, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
                poseStack.translate(-0.5, -0.25, -0.25);
            }
        }
    }

    public <B extends BlockEntity> void renderInner(B tile, float pPartialTicks, PoseStack poseStack, MultiBufferSource buffer, int pCombinedLight, int pCombinedOverlay) {
        BlockEntityRenderer<B> renderer = this.dispatcher.getRenderer(tile);
        if (renderer != null) {
            renderer.render(tile, pPartialTicks, poseStack, buffer, pCombinedLight, pCombinedOverlay);
        }
    }

    public void renderWax(PoseStack poseStack, MultiBufferSource buffer, int pCombinedLight, @Nullable ResourceLocation texture, float yaw) {
        if (texture != null) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.25, 0.5);
            float s = 1.077F;
            poseStack.scale(-s, -s, s);
            poseStack.translate(0.0F, 0.25F, 0.0F);
            RenderType overlayTexture = RenderType.entityCutoutNoCullZOffset(texture);
            VertexConsumer vertexconsumer = buffer.getBuffer(overlayTexture);
            this.overlay.setupAnim(0.0F, -yaw, 0.0F);
            this.overlay.m_7695_(poseStack, vertexconsumer, pCombinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }
}