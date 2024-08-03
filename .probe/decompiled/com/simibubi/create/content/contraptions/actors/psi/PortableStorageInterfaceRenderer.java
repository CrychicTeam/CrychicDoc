package com.simibubi.create.content.contraptions.actors.psi;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.function.Consumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

public class PortableStorageInterfaceRenderer extends SafeBlockEntityRenderer<PortableStorageInterfaceBlockEntity> {

    public PortableStorageInterfaceRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(PortableStorageInterfaceBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState blockState = be.m_58900_();
            float progress = be.getExtensionDistance(partialTicks);
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            render(blockState, be.isConnected(), progress, null, sbb -> sbb.light(light).renderInto(ms, vb));
        }
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState blockState = context.state;
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        float renderPartialTicks = AnimationTickHolder.getPartialTicks();
        LerpedFloat animation = PortableStorageInterfaceMovement.getAnimation(context);
        float progress = animation.getValue(renderPartialTicks);
        boolean lit = animation.settled();
        render(blockState, lit, progress, matrices.getModel(), sbb -> sbb.light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), vb));
    }

    private static void render(BlockState blockState, boolean lit, float progress, PoseStack local, Consumer<SuperByteBuffer> drawCallback) {
        SuperByteBuffer middle = CachedBufferer.partial(getMiddleForState(blockState, lit), blockState);
        SuperByteBuffer top = CachedBufferer.partial(getTopForState(blockState), blockState);
        if (local != null) {
            middle.transform(local);
            top.transform(local);
        }
        Direction facing = (Direction) blockState.m_61143_(PortableStorageInterfaceBlock.f_52588_);
        rotateToFacing(middle, facing);
        rotateToFacing(top, facing);
        middle.translate(0.0, (double) (progress * 0.5F + 0.375F), 0.0);
        top.translate(0.0, (double) progress, 0.0);
        drawCallback.accept(middle);
        drawCallback.accept(top);
    }

    private static void rotateToFacing(SuperByteBuffer buffer, Direction facing) {
        ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) buffer.centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX(facing == Direction.UP ? 0.0 : (facing == Direction.DOWN ? 180.0 : 90.0))).unCentre();
    }

    static PortableStorageInterfaceBlockEntity getTargetPSI(MovementContext context) {
        String _workingPos_ = "WorkingPos";
        if (!context.data.contains(_workingPos_)) {
            return null;
        } else {
            BlockPos pos = NbtUtils.readBlockPos(context.data.getCompound(_workingPos_));
            if (context.world.getBlockEntity(pos) instanceof PortableStorageInterfaceBlockEntity psi) {
                return !psi.isTransferring() ? null : psi;
            } else {
                return null;
            }
        }
    }

    static PartialModel getMiddleForState(BlockState state, boolean lit) {
        if (AllBlocks.PORTABLE_FLUID_INTERFACE.has(state)) {
            return lit ? AllPartialModels.PORTABLE_FLUID_INTERFACE_MIDDLE_POWERED : AllPartialModels.PORTABLE_FLUID_INTERFACE_MIDDLE;
        } else {
            return lit ? AllPartialModels.PORTABLE_STORAGE_INTERFACE_MIDDLE_POWERED : AllPartialModels.PORTABLE_STORAGE_INTERFACE_MIDDLE;
        }
    }

    static PartialModel getTopForState(BlockState state) {
        return AllBlocks.PORTABLE_FLUID_INTERFACE.has(state) ? AllPartialModels.PORTABLE_FLUID_INTERFACE_TOP : AllPartialModels.PORTABLE_STORAGE_INTERFACE_TOP;
    }
}