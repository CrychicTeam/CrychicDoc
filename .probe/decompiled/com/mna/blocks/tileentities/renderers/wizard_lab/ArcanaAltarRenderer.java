package com.mna.blocks.tileentities.renderers.wizard_lab;

import com.mna.ManaAndArtifice;
import com.mna.blocks.artifice.ArcanaAltarBlock;
import com.mna.blocks.tileentities.models.ArcanaAltarModel;
import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.ModelUtils;
import com.mna.tools.render.WorldRenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class ArcanaAltarRenderer extends WizardLabRenderer<ArcanaAltarTile> {

    private final ItemRenderer itemRenderer;

    private final Minecraft mc = Minecraft.getInstance();

    public ArcanaAltarRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ArcanaAltarModel());
        this.itemRenderer = this.mc.getItemRenderer();
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        ArcanaAltarTile tile = this.getAnimatable();
        for (int i = 0; i < 6; i++) {
            Vector3f offset = tile.getCandlePos(i, partialTick);
            poseStack.pushPose();
            poseStack.translate(offset.x(), offset.y(), offset.z());
            ModelUtils.renderModel(bufferSource.getBuffer(RenderType.cutout()), tile.m_58904_(), tile.m_58899_(), tile.m_58900_(), i % 2 == 0 ? ArcanaAltarModel.candle_short : ArcanaAltarModel.candle_long, poseStack, packedLight, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        float beamPct = Math.max(tile.getBeamPct(partialTick), 0.0F);
        float completionPct = Math.max((float) (tile.getCompletionTicks() - 60 - 20) / 20.0F, 0.0F);
        if (tile.isActive() && tile.getState().ordinal() >= ArcanaAltarTile.States.FINALIZING.ordinal()) {
            int[] color = new int[] { 30, 60, 170 };
            poseStack.pushPose();
            poseStack.translate(0.0, 2.5, 0.0);
            WorldRenderUtils.renderRadiant((float) tile.m_58904_().getGameTime(), poseStack, bufferSource, color, color, 255, 3.0F - 3.0F * completionPct, false);
            poseStack.popPose();
        }
        if (tile.isActive()) {
            Quaternionf cameraRotation = Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation();
            Quaternionf orbRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            VertexConsumer vertexBuilder = bufferSource.getBuffer(MARenderTypes.ORB_RENDER);
            int[] colors = new int[] { 255, 255, 255 };
            float orbPct = tile.getItemCollectPct(partialTick) - beamPct;
            for (int i = 0; i < 4; i++) {
                float rotationDegrees = 90.0F * (float) i;
                poseStack.pushPose();
                poseStack.mulPose(orbRotation);
                Direction facing = (Direction) tile.m_58900_().m_61143_(ArcanaAltarBlock.f_54117_);
                switch(facing) {
                    case WEST:
                        poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
                        break;
                    case EAST:
                        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
                        break;
                    case SOUTH:
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    case NORTH:
                }
                poseStack.translate(0.0, 2.5, 0.0);
                poseStack.scale(0.25F * orbPct, 0.25F * orbPct, 0.25F * orbPct);
                poseStack.mulPose(Axis.ZP.rotationDegrees(rotationDegrees));
                poseStack.translate(0.5, -0.25, 0.0);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                float nrmV = (float) Math.cos((double) (rotationDegrees - 180.0F) * Math.PI / 180.0);
                float nrmH = (float) Math.cos((double) (rotationDegrees - 180.0F - 90.0F) * Math.PI / 180.0);
                PoseStack.Pose matrixstack$entry = poseStack.last();
                Matrix4f renderMatrix = matrixstack$entry.pose();
                Matrix3f normalMatrix = matrixstack$entry.normal();
                addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, colors);
                addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, colors);
                addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, colors);
                addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLight, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, colors);
                poseStack.popPose();
            }
            if (tile.getRequestingPattern() != null) {
                Quaternionf patternRotation = Axis.YP.rotationDegrees(0.0F);
                float scale = 0.3F;
                poseStack.pushPose();
                poseStack.translate(0.0, 1.3, 0.0);
                poseStack.scale(scale, scale, scale);
                WorldRenderUtils.renderManaweavePattern(tile.getRequestingPattern(), patternRotation, poseStack, bufferSource, false);
                poseStack.popPose();
            } else if (tile.getRequestingStack().size() > 0) {
                int index = (int) (ManaAndArtifice.instance.proxy.getGameTicks() / 20L % (long) tile.getRequestingStack().size());
                ItemStack stack = new ItemStack((ItemLike) tile.getRequestingStack().get(index));
                float bob = (float) Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTick) / 60.0F)) / 40.0F;
                float rotation = (float) ManaAndArtifice.instance.proxy.getGameTicks();
                poseStack.pushPose();
                poseStack.translate(0.0F, 1.9F + bob, 0.0F);
                poseStack.scale(0.25F, 0.25F, 0.25F);
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, this.mc.level, 0);
                poseStack.popPose();
            }
            if (tile.getState() == ArcanaAltarTile.States.FINALIZING) {
                poseStack.pushPose();
                Vec3 start = Vec3.upFromBottomCenterOf(tile.m_58899_(), 1.0);
                Vec3 end = Vec3.upFromBottomCenterOf(tile.m_58899_(), 2.5);
                poseStack.translate(0.0, 2.5, 0.0);
                WorldRenderUtils.renderBeam(tile.m_58904_(), partialTick, poseStack, bufferSource, packedLight, end, start, beamPct, new int[] { 200, 200, 255 }, 255, 0.065F * (1.0F - completionPct), MARenderTypes.RITUAL_BEAM_RENDER_TYPE);
                poseStack.popPose();
            }
        }
        super.firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] rgb) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(rgb[0], rgb[1], rgb[2], 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }
}