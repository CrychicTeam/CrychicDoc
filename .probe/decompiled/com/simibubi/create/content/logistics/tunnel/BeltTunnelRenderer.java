package com.simibubi.create.content.logistics.tunnel;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BeltTunnelRenderer extends SmartBlockEntityRenderer<BeltTunnelBlockEntity> {

    public BeltTunnelRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(BeltTunnelBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (!Backend.canUseInstancing(be.m_58904_())) {
            SuperByteBuffer flapBuffer = CachedBufferer.partial(AllPartialModels.BELT_TUNNEL_FLAP, be.m_58900_());
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            Vec3 pivot = VecHelper.voxelSpace(0.0, 10.0, 1.0);
            TransformStack msr = TransformStack.cast(ms);
            for (Direction direction : Iterate.directions) {
                if (be.flaps.containsKey(direction)) {
                    float horizontalAngle = AngleHelper.horizontalAngle(direction.getOpposite());
                    float f = ((LerpedFloat) be.flaps.get(direction)).getValue(partialTicks);
                    ms.pushPose();
                    ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) horizontalAngle)).unCentre();
                    ms.translate(0.0046875F, 0.0F, 0.0F);
                    for (int segment = 0; segment <= 3; segment++) {
                        ms.pushPose();
                        float intensity = segment == 3 ? 1.5F : (float) (segment + 1);
                        float abs = Math.abs(f);
                        float flapAngle = Mth.sin((float) ((double) (1.0F - abs) * Math.PI * (double) intensity)) * 30.0F * f * (float) (direction.getAxis() == Direction.Axis.X ? 1 : -1);
                        if (f > 0.0F) {
                            flapAngle *= 0.5F;
                        }
                        ((TransformStack) ((TransformStack) msr.translate(pivot)).rotateX((double) flapAngle)).translateBack(pivot);
                        flapBuffer.light(light).renderInto(ms, vb);
                        ms.popPose();
                        ms.translate(-0.190625F, 0.0F, 0.0F);
                    }
                    ms.popPose();
                }
            }
        }
    }
}