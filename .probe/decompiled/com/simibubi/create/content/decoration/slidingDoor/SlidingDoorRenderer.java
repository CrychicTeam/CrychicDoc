package com.simibubi.create.content.decoration.slidingDoor;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class SlidingDoorRenderer extends SafeBlockEntityRenderer<SlidingDoorBlockEntity> {

    public SlidingDoorRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(SlidingDoorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.m_58900_();
        if (be.shouldRenderSpecial(blockState)) {
            Direction facing = (Direction) blockState.m_61143_(DoorBlock.FACING);
            Direction movementDirection = facing.getClockWise();
            if (blockState.m_61143_(DoorBlock.HINGE) == DoorHingeSide.LEFT) {
                movementDirection = movementDirection.getOpposite();
            }
            float value = be.animation.getValue(partialTicks);
            float value2 = Mth.clamp(value * 10.0F, 0.0F, 1.0F);
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
            Vec3 offset = Vec3.atLowerCornerOf(movementDirection.getNormal()).scale((double) (value * value * 13.0F / 16.0F)).add(Vec3.atLowerCornerOf(facing.getNormal()).scale((double) (value2 * 1.0F / 32.0F)));
            if (((SlidingDoorBlock) blockState.m_60734_()).isFoldingDoor()) {
                Couple<PartialModel> partials = (Couple<PartialModel>) AllPartialModels.FOLDING_DOORS.get(ForgeRegistries.BLOCKS.getKey(blockState.m_60734_()));
                boolean flip = blockState.m_61143_(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
                for (boolean left : Iterate.trueAndFalse) {
                    SuperByteBuffer partial = CachedBufferer.partial(partials.get(left ^ flip), blockState);
                    float f = flip ? -1.0F : 1.0F;
                    partial.translate(0.0, -0.001953125, 0.0).translate(Vec3.atLowerCornerOf(facing.getNormal()).scale((double) (value2 * 1.0F / 32.0F)));
                    partial.rotateCentered(Direction.UP, (float) (Math.PI / 180.0) * AngleHelper.horizontalAngle(facing.getClockWise()));
                    if (flip) {
                        partial.translate(0.0, 0.0, 1.0);
                    }
                    partial.rotateY((double) (91.0F * f * value * value));
                    if (!left) {
                        partial.translate(0.0, 0.0, (double) (f / 2.0F)).rotateY((double) (-181.0F * f * value * value));
                    }
                    if (flip) {
                        partial.translate(0.0, 0.0, -0.5);
                    }
                    partial.light(light).renderInto(ms, vb);
                }
            } else {
                for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                    ((SuperByteBuffer) CachedBufferer.block((BlockState) ((BlockState) blockState.m_61124_(DoorBlock.OPEN, false)).m_61124_(DoorBlock.HALF, half)).translate(0.0, half == DoubleBlockHalf.UPPER ? 0.9980469F : 0.0, 0.0).translate(offset)).light(light).renderInto(ms, vb);
                }
            }
        }
    }
}