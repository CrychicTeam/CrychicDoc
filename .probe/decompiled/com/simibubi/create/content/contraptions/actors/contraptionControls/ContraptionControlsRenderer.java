package com.simibubi.create.content.contraptions.actors.contraptionControls;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ContraptionControlsRenderer extends SmartBlockEntityRenderer<ContraptionControlsBlockEntity> {

    private static Random r = new Random();

    public ContraptionControlsRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(ContraptionControlsBlockEntity blockEntity, float pt, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = blockEntity.m_58900_();
        Direction facing = ((Direction) blockState.m_61143_(ContraptionControlsBlock.f_54117_)).getOpposite();
        Vec3 buttonMovementAxis = VecHelper.rotate(new Vec3(0.0, 1.0, -0.325), (double) AngleHelper.horizontalAngle(facing), Direction.Axis.Y);
        Vec3 buttonMovement = buttonMovementAxis.scale((double) (-0.07F + -0.041666668F * blockEntity.button.getValue(pt)));
        Vec3 buttonOffset = buttonMovementAxis.scale(0.07F);
        ms.pushPose();
        ms.translate(buttonMovement.x, buttonMovement.y, buttonMovement.z);
        super.renderSafe(blockEntity, pt, ms, buffer, light, overlay);
        ms.translate(buttonOffset.x, buttonOffset.y, buttonOffset.z);
        VertexConsumer vc = buffer.getBuffer(RenderType.solid());
        CachedBufferer.partialFacing(AllPartialModels.CONTRAPTION_CONTROLS_BUTTON, blockState, facing).light(light).renderInto(ms, vc);
        ms.popPose();
        int i = (int) blockEntity.indicator.getValue(pt) / 45 % 8 + 8;
        CachedBufferer.partialFacing((PartialModel) AllPartialModels.CONTRAPTION_CONTROLS_INDICATOR.get(i % 8), blockState, facing).light(light).renderInto(ms, vc);
    }

    public static void renderInContraption(MovementContext ctx, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (ctx.temporaryData instanceof ContraptionControlsMovement.ElevatorFloorSelection efs) {
            if (AllBlocks.CONTRAPTION_CONTROLS.has(ctx.state)) {
                Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
                float playerDistance = (float) (ctx.position != null && cameraEntity != null ? ctx.position.distanceToSqr(cameraEntity.getEyePosition()) : 0.0);
                float flicker = r.nextFloat();
                Couple<Integer> couple = (Couple<Integer>) DyeHelper.DYE_TABLE.get(efs.targetYEqualsSelection ? DyeColor.WHITE : DyeColor.ORANGE);
                int brightColor = couple.getFirst();
                int darkColor = couple.getSecond();
                int flickeringBrightColor = Color.mixColors(brightColor, darkColor, flicker / 4.0F);
                Font fontRenderer = Minecraft.getInstance().font;
                float shadowOffset = 0.5F;
                String text = efs.currentShortName;
                String description = efs.currentLongName;
                PoseStack ms = matrices.getViewProjection();
                TransformStack msr = TransformStack.cast(ms);
                ms.pushPose();
                msr.translate(ctx.localPos);
                msr.rotateCentered(Direction.UP, AngleHelper.rad((double) AngleHelper.horizontalAngle((Direction) ctx.state.m_61143_(ContraptionControlsBlock.f_54117_))));
                ms.translate(0.4F, 1.0F, 0.5F);
                msr.rotate(Direction.WEST, AngleHelper.rad(67.5));
                float buttondepth = -0.25F;
                if (ctx.contraption.presentBlockEntities.get(ctx.localPos) instanceof ContraptionControlsBlockEntity cbe) {
                    buttondepth += -0.041666668F * cbe.button.getValue(AnimationTickHolder.getPartialTicks(renderWorld));
                }
                if (!text.isBlank() && playerDistance < 100.0F) {
                    int actualWidth = fontRenderer.width(text);
                    int width = Math.max(actualWidth, 12);
                    float scale = 1.0F / (5.0F * ((float) width - 0.5F));
                    float heightCentering = ((float) width - 8.0F) / 2.0F;
                    ms.pushPose();
                    ms.translate(0.0F, 0.15F, buttondepth);
                    ms.scale(scale, -scale, scale);
                    ms.translate((float) (Math.max(0, width - actualWidth) / 2), heightCentering, 0.0F);
                    NixieTubeRenderer.drawInWorldString(ms, buffer, text, flickeringBrightColor);
                    ms.translate(shadowOffset, shadowOffset, -0.0625F);
                    NixieTubeRenderer.drawInWorldString(ms, buffer, text, Color.mixColors(darkColor, 0, 0.35F));
                    ms.popPose();
                }
                if (!description.isBlank() && playerDistance < 20.0F) {
                    int actualWidth = fontRenderer.width(description);
                    int width = Math.max(actualWidth, 55);
                    float scale = 1.0F / (3.0F * ((float) width - 0.5F));
                    float heightCentering = ((float) width - 8.0F) / 2.0F;
                    ms.pushPose();
                    ms.translate(-0.0635F, 0.06F, buttondepth);
                    ms.scale(scale, -scale, scale);
                    ms.translate((float) (Math.max(0, width - actualWidth) / 2), heightCentering, 0.0F);
                    NixieTubeRenderer.drawInWorldString(ms, buffer, description, flickeringBrightColor);
                    ms.popPose();
                }
                ms.popPose();
            }
        }
    }
}