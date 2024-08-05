package com.mna.blocks.tileentities.renderers;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.SeerStoneTile;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SeerStoneRenderer implements BlockEntityRenderer<SeerStoneTile> {

    public static final ResourceLocation crystal = RLoc.create("block/seer_stone/crystal");

    public static final ResourceLocation crystal_band = RLoc.create("block/seer_stone/crystal_band");

    public SeerStoneRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(SeerStoneTile tile, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = tile.m_58904_();
        BlockPos pos = tile.m_58899_();
        BlockState state = tile.m_58900_();
        long ticks = ManaAndArtifice.instance.proxy.getGameTicks();
        double yRot = 0.0;
        Entity target = tile.getTarget();
        if (target != null) {
            Vec3 myPos = Vec3.atCenterOf(pos);
            Vec3 targetPos = target.getEyePosition();
            double xDelta = targetPos.x - myPos.x;
            double zDelta = targetPos.z - myPos.z;
            yRot = (double) Mth.wrapDegrees((float) (Mth.atan2(zDelta, xDelta) * 180.0F / (float) Math.PI) - 90.0F);
        }
        float angle = MathUtils.clamp((float) tile.getCrystalAngle() + partialTicks, 0.0F, 90.0F);
        float redPct = 1.0F - angle / 90.0F;
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.75 + Math.sin((double) (((float) ticks + partialTicks) / 40.0F)) * 0.1, 0.5);
        matrixStack.mulPose(Axis.XP.rotationDegrees(angle));
        if (target != null) {
            matrixStack.mulPose(Axis.ZP.rotationDegrees((float) yRot));
        }
        matrixStack.mulPose(Axis.YP.rotationDegrees((float) (ticks % 360L)));
        VertexConsumer vertexBuilder = bufferIn.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
        ModelUtils.renderModel(vertexBuilder, world, pos, state, crystal, matrixStack, new float[] { 1.0F, 1.0F, redPct, redPct }, combinedLightIn, combinedOverlayIn);
        ModelUtils.renderModel(bufferIn, world, pos, state, crystal_band, matrixStack, combinedLightIn, combinedOverlayIn);
        matrixStack.popPose();
        if (target != null) {
            float scale = 0.2F;
            matrixStack.pushPose();
            matrixStack.translate(0.5, 0.75 + Math.sin((double) (((float) ticks + partialTicks) / 40.0F)) * 0.1, 0.5);
            matrixStack.scale(scale, scale, scale);
            Minecraft.getInstance().getEntityRenderDispatcher().render(target, 0.0, 0.25 / (double) scale, 0.0, 0.0F, 0.0F, matrixStack, bufferIn, combinedLightIn);
            matrixStack.popPose();
        }
    }
}