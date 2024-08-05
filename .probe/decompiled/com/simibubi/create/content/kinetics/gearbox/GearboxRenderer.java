package com.simibubi.create.content.kinetics.gearbox;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GearboxRenderer extends KineticBlockEntityRenderer<GearboxBlockEntity> {

    public GearboxRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(GearboxBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            Direction.Axis boxAxis = (Direction.Axis) be.m_58900_().m_61143_(BlockStateProperties.AXIS);
            BlockPos pos = be.m_58899_();
            float time = AnimationTickHolder.getRenderTime(be.m_58904_());
            for (Direction direction : Iterate.directions) {
                Direction.Axis axis = direction.getAxis();
                if (boxAxis != axis) {
                    SuperByteBuffer shaft = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.m_58900_(), direction);
                    float offset = getRotationOffsetForPosition(be, pos, axis);
                    float angle = time * be.getSpeed() * 3.0F / 10.0F % 360.0F;
                    if (be.getSpeed() != 0.0F && be.hasSource()) {
                        BlockPos source = be.source.subtract(be.m_58899_());
                        Direction sourceFacing = Direction.getNearest((float) source.m_123341_(), (float) source.m_123342_(), (float) source.m_123343_());
                        if (sourceFacing.getAxis() == direction.getAxis()) {
                            angle *= sourceFacing == direction ? 1.0F : -1.0F;
                        } else if (sourceFacing.getAxisDirection() == direction.getAxisDirection()) {
                            angle *= -1.0F;
                        }
                    }
                    angle += offset;
                    angle = angle / 180.0F * (float) Math.PI;
                    kineticRotationTransform(shaft, be, axis, angle, light);
                    shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
                }
            }
        }
    }
}