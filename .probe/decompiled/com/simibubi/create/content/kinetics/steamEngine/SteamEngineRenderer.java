package com.simibubi.create.content.kinetics.steamEngine;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class SteamEngineRenderer extends SafeBlockEntityRenderer<SteamEngineBlockEntity> {

    public SteamEngineRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(SteamEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            Float angle = be.getTargetAngle();
            if (angle != null) {
                BlockState blockState = be.m_58900_();
                Direction facing = SteamEngineBlock.getFacing(blockState);
                Direction.Axis facingAxis = facing.getAxis();
                Direction.Axis axis = Direction.Axis.Y;
                PoweredShaftBlockEntity shaft = be.getShaft();
                if (shaft != null) {
                    axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
                }
                boolean roll90 = facingAxis.isHorizontal() && axis == Direction.Axis.Y || facingAxis.isVertical() && axis == Direction.Axis.Z;
                float sine = Mth.sin(angle);
                float sine2 = Mth.sin(angle - (float) (Math.PI / 2));
                float piston = (1.0F - sine) / 4.0F * 24.0F / 16.0F;
                VertexConsumer vb = buffer.getBuffer(RenderType.solid());
                this.transformed(AllPartialModels.ENGINE_PISTON, blockState, facing, roll90).translate(0.0, (double) piston, 0.0).light(light).renderInto(ms, vb);
                ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) this.transformed(AllPartialModels.ENGINE_LINKAGE, blockState, facing, roll90).centre()).translate(0.0, 1.0, 0.0).unCentre()).translate(0.0, (double) piston, 0.0).translate(0.0, 0.25, 0.5).rotateX((double) (sine2 * 23.0F))).translate(0.0, -0.25, -0.5).light(light).renderInto(ms, vb);
                ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) this.transformed(AllPartialModels.ENGINE_CONNECTOR, blockState, facing, roll90).translate(0.0, 2.0, 0.0).centre()).rotateXRadians((double) (-angle + (float) (Math.PI / 2)))).unCentre()).light(light).renderInto(ms, vb);
            }
        }
    }

    private SuperByteBuffer transformed(PartialModel model, BlockState blockState, Direction facing, boolean roll90) {
        return (SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(model, blockState).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) (AngleHelper.verticalAngle(facing) + 90.0F))).rotateY(roll90 ? -90.0 : 0.0)).unCentre();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}