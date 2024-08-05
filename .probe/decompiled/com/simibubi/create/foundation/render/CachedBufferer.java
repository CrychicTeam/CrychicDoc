package com.simibubi.create.foundation.render;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.AngleHelper;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.tuple.Pair;

public class CachedBufferer {

    public static final SuperByteBufferCache.Compartment<BlockState> GENERIC_BLOCK = new SuperByteBufferCache.Compartment<>();

    public static final SuperByteBufferCache.Compartment<PartialModel> PARTIAL = new SuperByteBufferCache.Compartment<>();

    public static final SuperByteBufferCache.Compartment<Pair<Direction, PartialModel>> DIRECTIONAL_PARTIAL = new SuperByteBufferCache.Compartment<>();

    public static SuperByteBuffer block(BlockState toRender) {
        return block(GENERIC_BLOCK, toRender);
    }

    public static SuperByteBuffer block(SuperByteBufferCache.Compartment<BlockState> compartment, BlockState toRender) {
        return CreateClient.BUFFER_CACHE.get(compartment, toRender, () -> BakedModelRenderHelper.standardBlockRender(toRender));
    }

    public static SuperByteBuffer partial(PartialModel partial, BlockState referenceState) {
        return CreateClient.BUFFER_CACHE.get(PARTIAL, partial, () -> BakedModelRenderHelper.standardModelRender(partial.get(), referenceState));
    }

    public static SuperByteBuffer partial(PartialModel partial, BlockState referenceState, Supplier<PoseStack> modelTransform) {
        return CreateClient.BUFFER_CACHE.get(PARTIAL, partial, () -> BakedModelRenderHelper.standardModelRender(partial.get(), referenceState, (PoseStack) modelTransform.get()));
    }

    public static SuperByteBuffer partialFacing(PartialModel partial, BlockState referenceState) {
        Direction facing = (Direction) referenceState.m_61143_(BlockStateProperties.FACING);
        return partialFacing(partial, referenceState, facing);
    }

    public static SuperByteBuffer partialFacing(PartialModel partial, BlockState referenceState, Direction facing) {
        return partialDirectional(partial, referenceState, facing, rotateToFace(facing));
    }

    public static SuperByteBuffer partialFacingVertical(PartialModel partial, BlockState referenceState, Direction facing) {
        return partialDirectional(partial, referenceState, facing, rotateToFaceVertical(facing));
    }

    public static SuperByteBuffer partialDirectional(PartialModel partial, BlockState referenceState, Direction dir, Supplier<PoseStack> modelTransform) {
        return CreateClient.BUFFER_CACHE.get(DIRECTIONAL_PARTIAL, Pair.of(dir, partial), () -> BakedModelRenderHelper.standardModelRender(partial.get(), referenceState, (PoseStack) modelTransform.get()));
    }

    public static Supplier<PoseStack> rotateToFace(Direction facing) {
        return () -> {
            PoseStack stack = new PoseStack();
            ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(stack).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) AngleHelper.verticalAngle(facing))).unCentre();
            return stack;
        };
    }

    public static Supplier<PoseStack> rotateToFaceVertical(Direction facing) {
        return () -> {
            PoseStack stack = new PoseStack();
            ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(stack).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) (AngleHelper.verticalAngle(facing) + 90.0F))).unCentre();
            return stack;
        };
    }
}