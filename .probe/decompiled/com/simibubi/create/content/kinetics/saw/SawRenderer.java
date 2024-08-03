package com.simibubi.create.content.kinetics.saw;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class SawRenderer extends SafeBlockEntityRenderer<SawBlockEntity> {

    public SawRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(SawBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        this.renderBlade(be, ms, buffer, light);
        this.renderItems(be, partialTicks, ms, buffer, light, overlay);
        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
        if (!Backend.canUseInstancing(be.m_58904_())) {
            this.renderShaft(be, ms, buffer, light, overlay);
        }
    }

    protected void renderBlade(SawBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState blockState = be.m_58900_();
        float speed = be.getSpeed();
        boolean rotate = false;
        PartialModel partial;
        if (SawBlock.isHorizontal(blockState)) {
            if (speed > 0.0F) {
                partial = AllPartialModels.SAW_BLADE_HORIZONTAL_ACTIVE;
            } else if (speed < 0.0F) {
                partial = AllPartialModels.SAW_BLADE_HORIZONTAL_REVERSED;
            } else {
                partial = AllPartialModels.SAW_BLADE_HORIZONTAL_INACTIVE;
            }
        } else {
            if (be.getSpeed() > 0.0F) {
                partial = AllPartialModels.SAW_BLADE_VERTICAL_ACTIVE;
            } else if (speed < 0.0F) {
                partial = AllPartialModels.SAW_BLADE_VERTICAL_REVERSED;
            } else {
                partial = AllPartialModels.SAW_BLADE_VERTICAL_INACTIVE;
            }
            if ((Boolean) blockState.m_61143_(SawBlock.AXIS_ALONG_FIRST_COORDINATE)) {
                rotate = true;
            }
        }
        SuperByteBuffer superBuffer = CachedBufferer.partialFacing(partial, blockState);
        if (rotate) {
            superBuffer.rotateCentered(Direction.UP, AngleHelper.rad(90.0));
        }
        superBuffer.color(16777215).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    protected void renderShaft(SawBlockEntity be, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        KineticBlockEntityRenderer.renderRotatingBuffer(be, this.getRotatedModel(be), ms, buffer.getBuffer(RenderType.solid()), light);
    }

    protected void renderItems(SawBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        boolean processingMode = be.m_58900_().m_61143_(SawBlock.FACING) == Direction.UP;
        if (processingMode && !be.inventory.isEmpty()) {
            boolean alongZ = !(Boolean) be.m_58900_().m_61143_(SawBlock.AXIS_ALONG_FIRST_COORDINATE);
            ms.pushPose();
            boolean moving = be.inventory.recipeDuration != 0.0F;
            float offset = moving ? be.inventory.remainingTime / be.inventory.recipeDuration : 0.0F;
            float processingSpeed = Mth.clamp(Math.abs(be.getSpeed()) / 32.0F, 1.0F, 128.0F);
            if (moving) {
                offset = Mth.clamp(offset + (-partialTicks + 0.5F) * processingSpeed / be.inventory.recipeDuration, 0.125F, 1.0F);
                if (!be.inventory.appliedRecipe) {
                    offset++;
                }
                offset /= 2.0F;
            }
            if (be.getSpeed() == 0.0F) {
                offset = 0.5F;
            }
            if (be.getSpeed() < 0.0F ^ alongZ) {
                offset = 1.0F - offset;
            }
            for (int i = 0; i < be.inventory.getSlots(); i++) {
                ItemStack stack = be.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    BakedModel modelWithOverrides = itemRenderer.getModel(stack, be.m_58904_(), null, 0);
                    boolean blockItem = modelWithOverrides.isGui3d();
                    ms.translate(alongZ ? (double) offset : 0.5, blockItem ? 0.925F : 0.8125, alongZ ? 0.5 : (double) offset);
                    ms.scale(0.5F, 0.5F, 0.5F);
                    if (alongZ) {
                        ms.mulPose(Axis.YP.rotationDegrees(90.0F));
                    }
                    ms.mulPose(Axis.XP.rotationDegrees(90.0F));
                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
                    break;
                }
            }
            ms.popPose();
        }
    }

    protected SuperByteBuffer getRotatedModel(KineticBlockEntity be) {
        BlockState state = be.m_58900_();
        return ((Direction) state.m_61143_(BlockStateProperties.FACING)).getAxis().isHorizontal() ? CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state.rotate(be.m_58904_(), be.m_58899_(), Rotation.CLOCKWISE_180)) : CachedBufferer.block(KineticBlockEntityRenderer.KINETIC_BLOCK, this.getRenderedBlockState(be));
    }

    protected BlockState getRenderedBlockState(KineticBlockEntity be) {
        return KineticBlockEntityRenderer.shaft(KineticBlockEntityRenderer.getRotationAxisOf(be));
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;
        Direction facing = (Direction) state.m_61143_(SawBlock.FACING);
        Vec3 facingVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(SawBlock.FACING)).getNormal());
        facingVec = (Vec3) context.rotation.apply(facingVec);
        Direction closestToFacing = Direction.getNearest(facingVec.x, facingVec.y, facingVec.z);
        boolean horizontal = closestToFacing.getAxis().isHorizontal();
        boolean backwards = VecHelper.isVecPointingTowards(context.relativeMotion, facing.getOpposite());
        boolean moving = context.getAnimationSpeed() != 0.0F;
        boolean shouldAnimate = context.contraption.stalled && horizontal || !context.contraption.stalled && !backwards && moving;
        SuperByteBuffer superBuffer;
        if (SawBlock.isHorizontal(state)) {
            if (shouldAnimate) {
                superBuffer = CachedBufferer.partial(AllPartialModels.SAW_BLADE_HORIZONTAL_ACTIVE, state);
            } else {
                superBuffer = CachedBufferer.partial(AllPartialModels.SAW_BLADE_HORIZONTAL_INACTIVE, state);
            }
        } else if (shouldAnimate) {
            superBuffer = CachedBufferer.partial(AllPartialModels.SAW_BLADE_VERTICAL_ACTIVE, state);
        } else {
            superBuffer = CachedBufferer.partial(AllPartialModels.SAW_BLADE_VERTICAL_INACTIVE, state);
        }
        ((SuperByteBuffer) ((SuperByteBuffer) superBuffer.transform(matrices.getModel()).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) AngleHelper.verticalAngle(facing));
        if (!SawBlock.isHorizontal(state)) {
            superBuffer.rotateZ(state.m_61143_(SawBlock.AXIS_ALONG_FIRST_COORDINATE) ? 90.0 : 0.0);
        }
        ((SuperByteBuffer) superBuffer.unCentre()).light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.cutoutMipped()));
    }
}