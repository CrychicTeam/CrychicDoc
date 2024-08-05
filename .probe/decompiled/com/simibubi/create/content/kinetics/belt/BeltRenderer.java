package com.simibubi.create.content.kinetics.belt;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.ShadowRenderHelper;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BeltRenderer extends SafeBlockEntityRenderer<BeltBlockEntity> {

    public BeltRenderer(BlockEntityRendererProvider.Context context) {
    }

    public boolean shouldRenderOffScreen(BeltBlockEntity be) {
        return be.isController();
    }

    protected void renderSafe(BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState blockState = be.m_58900_();
            if (!AllBlocks.BELT.has(blockState)) {
                return;
            }
            BeltSlope beltSlope = (BeltSlope) blockState.m_61143_(BeltBlock.SLOPE);
            BeltPart part = (BeltPart) blockState.m_61143_(BeltBlock.PART);
            Direction facing = (Direction) blockState.m_61143_(BeltBlock.HORIZONTAL_FACING);
            Direction.AxisDirection axisDirection = facing.getAxisDirection();
            boolean downward = beltSlope == BeltSlope.DOWNWARD;
            boolean upward = beltSlope == BeltSlope.UPWARD;
            boolean diagonal = downward || upward;
            boolean start = part == BeltPart.START;
            boolean end = part == BeltPart.END;
            boolean sideways = beltSlope == BeltSlope.SIDEWAYS;
            boolean alongX = facing.getAxis() == Direction.Axis.X;
            PoseStack localTransforms = new PoseStack();
            TransformStack msr = TransformStack.cast(localTransforms);
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            float renderTick = AnimationTickHolder.getRenderTime(be.m_58904_());
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) (AngleHelper.horizontalAngle(facing) + (float) (upward ? 180 : 0) + (float) (sideways ? 270 : 0)))).rotateZ(sideways ? 90.0 : 0.0)).rotateX(!diagonal && beltSlope != BeltSlope.HORIZONTAL ? 90.0 : 0.0)).unCentre();
            if (downward || beltSlope == BeltSlope.VERTICAL && axisDirection == Direction.AxisDirection.POSITIVE) {
                boolean b = start;
                start = end;
                end = b;
            }
            DyeColor color = (DyeColor) be.color.orElse(null);
            for (boolean bottom : Iterate.trueAndFalse) {
                PartialModel beltPartial = getBeltPartial(diagonal, start, end, bottom);
                SuperByteBuffer beltBuffer = CachedBufferer.partial(beltPartial, blockState).light(light);
                SpriteShiftEntry spriteShift = getSpriteShiftEntry(color, diagonal, bottom);
                float speed = be.getSpeed();
                if (speed != 0.0F || be.color.isPresent()) {
                    float time = renderTick * (float) axisDirection.getStep();
                    if (diagonal && downward ^ alongX || !sideways && !diagonal && alongX || sideways && axisDirection == Direction.AxisDirection.NEGATIVE) {
                        speed = -speed;
                    }
                    float scrollMult = diagonal ? 0.375F : 0.5F;
                    float spriteSize = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
                    double scroll = (double) (speed * time) / 504.0 + (bottom ? 0.5 : 0.0);
                    scroll -= Math.floor(scroll);
                    scroll = scroll * (double) spriteSize * (double) scrollMult;
                    beltBuffer.shiftUVScrolling(spriteShift, (float) scroll);
                }
                beltBuffer.transform(localTransforms).renderInto(ms, vb);
                if (diagonal) {
                    break;
                }
            }
            if (be.hasPulley()) {
                Direction dir = sideways ? Direction.UP : ((Direction) blockState.m_61143_(BeltBlock.HORIZONTAL_FACING)).getClockWise();
                Supplier<PoseStack> matrixStackSupplier = () -> {
                    PoseStack stack = new PoseStack();
                    TransformStack stacker = TransformStack.cast(stack);
                    stacker.centre();
                    if (dir.getAxis() == Direction.Axis.X) {
                        stacker.rotateY(90.0);
                    }
                    if (dir.getAxis() == Direction.Axis.Y) {
                        stacker.rotateX(90.0);
                    }
                    stacker.rotateX(90.0);
                    stacker.unCentre();
                    return stack;
                };
                SuperByteBuffer superBuffer = CachedBufferer.partialDirectional(AllPartialModels.BELT_PULLEY, blockState, dir, matrixStackSupplier);
                KineticBlockEntityRenderer.standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
            }
        }
        this.renderItems(be, partialTicks, ms, buffer, light, overlay);
    }

    public static SpriteShiftEntry getSpriteShiftEntry(DyeColor color, boolean diagonal, boolean bottom) {
        if (color != null) {
            return (SpriteShiftEntry) (diagonal ? AllSpriteShifts.DYED_DIAGONAL_BELTS : (bottom ? AllSpriteShifts.DYED_OFFSET_BELTS : AllSpriteShifts.DYED_BELTS)).get(color);
        } else {
            return diagonal ? AllSpriteShifts.BELT_DIAGONAL : (bottom ? AllSpriteShifts.BELT_OFFSET : AllSpriteShifts.BELT);
        }
    }

    public static PartialModel getBeltPartial(boolean diagonal, boolean start, boolean end, boolean bottom) {
        if (diagonal) {
            if (start) {
                return AllPartialModels.BELT_DIAGONAL_START;
            } else {
                return end ? AllPartialModels.BELT_DIAGONAL_END : AllPartialModels.BELT_DIAGONAL_MIDDLE;
            }
        } else if (bottom) {
            if (start) {
                return AllPartialModels.BELT_START_BOTTOM;
            } else {
                return end ? AllPartialModels.BELT_END_BOTTOM : AllPartialModels.BELT_MIDDLE_BOTTOM;
            }
        } else if (start) {
            return AllPartialModels.BELT_START;
        } else {
            return end ? AllPartialModels.BELT_END : AllPartialModels.BELT_MIDDLE;
        }
    }

    protected void renderItems(BeltBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.isController()) {
            if (be.beltLength != 0) {
                ms.pushPose();
                Direction beltFacing = be.getBeltFacing();
                Vec3i directionVec = beltFacing.getNormal();
                Vec3 beltStartOffset = Vec3.atLowerCornerOf(directionVec).scale(-0.5).add(0.5, 0.9375, 0.5);
                ms.translate(beltStartOffset.x, beltStartOffset.y, beltStartOffset.z);
                BeltSlope slope = (BeltSlope) be.m_58900_().m_61143_(BeltBlock.SLOPE);
                int verticality = slope == BeltSlope.DOWNWARD ? -1 : (slope == BeltSlope.UPWARD ? 1 : 0);
                boolean slopeAlongX = beltFacing.getAxis() == Direction.Axis.X;
                boolean onContraption = be.m_58904_() instanceof WrappedWorld;
                for (TransportedItemStack transported : be.getInventory().getTransportedItems()) {
                    ms.pushPose();
                    TransformStack.cast(ms).nudge(transported.angle);
                    float offset;
                    float sideOffset;
                    if (be.getSpeed() == 0.0F) {
                        offset = transported.beltPosition;
                        sideOffset = transported.sideOffset;
                    } else {
                        offset = Mth.lerp(partialTicks, transported.prevBeltPosition, transported.beltPosition);
                        sideOffset = Mth.lerp(partialTicks, transported.prevSideOffset, transported.sideOffset);
                    }
                    float verticalMovement;
                    if ((double) offset < 0.5) {
                        verticalMovement = 0.0F;
                    } else {
                        verticalMovement = (float) verticality * (Math.min(offset, (float) be.beltLength - 0.5F) - 0.5F);
                    }
                    Vec3 offsetVec = Vec3.atLowerCornerOf(directionVec).scale((double) offset);
                    if (verticalMovement != 0.0F) {
                        offsetVec = offsetVec.add(0.0, (double) verticalMovement, 0.0);
                    }
                    boolean onSlope = slope != BeltSlope.HORIZONTAL && Mth.clamp(offset, 0.5F, (float) be.beltLength - 0.5F) == offset;
                    boolean tiltForward = (slope == BeltSlope.DOWNWARD ^ beltFacing.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (beltFacing.getAxis() == Direction.Axis.Z);
                    float slopeAngle = onSlope ? (tiltForward ? -45.0F : 45.0F) : 0.0F;
                    ms.translate(offsetVec.x, offsetVec.y, offsetVec.z);
                    boolean alongX = beltFacing.getClockWise().getAxis() == Direction.Axis.X;
                    if (!alongX) {
                        sideOffset *= -1.0F;
                    }
                    ms.translate(alongX ? sideOffset : 0.0F, 0.0F, alongX ? 0.0F : sideOffset);
                    int stackLight = onContraption ? light : this.getPackedLight(be, offset);
                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    boolean renderUpright = BeltHelper.isItemUpright(transported.stack);
                    boolean blockItem = itemRenderer.getModel(transported.stack, be.m_58904_(), null, 0).isGui3d();
                    int count = Mth.log2(transported.stack.getCount()) / 2;
                    Random r = new Random((long) transported.angle);
                    boolean slopeShadowOnly = renderUpright && onSlope;
                    float slopeOffset = 0.125F;
                    if (slopeShadowOnly) {
                        ms.pushPose();
                    }
                    if (!renderUpright || slopeShadowOnly) {
                        ms.mulPose((slopeAlongX ? Axis.ZP : Axis.XP).rotationDegrees(slopeAngle));
                    }
                    if (onSlope) {
                        ms.translate(0.0F, slopeOffset, 0.0F);
                    }
                    ms.pushPose();
                    ms.translate(0.0F, -0.12F, 0.0F);
                    ShadowRenderHelper.renderShadow(ms, buffer, 0.75F, 0.2F);
                    ms.popPose();
                    if (slopeShadowOnly) {
                        ms.popPose();
                        ms.translate(0.0F, slopeOffset, 0.0F);
                    }
                    if (renderUpright) {
                        Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
                        if (renderViewEntity != null) {
                            Vec3 positionVec = renderViewEntity.position();
                            Vec3 vectorForOffset = BeltHelper.getVectorForOffset(be, offset);
                            Vec3 diff = vectorForOffset.subtract(positionVec);
                            float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
                            ms.mulPose(Axis.YP.rotation(yRot));
                        }
                        ms.translate(0.0, 0.09375, 0.0625);
                    }
                    for (int i = 0; i <= count; i++) {
                        ms.pushPose();
                        ms.mulPose(Axis.YP.rotationDegrees((float) transported.angle));
                        if (!blockItem && !renderUpright) {
                            ms.translate(0.0, -0.09375, 0.0);
                            ms.mulPose(Axis.XP.rotationDegrees(90.0F));
                        }
                        if (blockItem) {
                            ms.translate(r.nextFloat() * 0.0625F * (float) i, 0.0F, r.nextFloat() * 0.0625F * (float) i);
                        }
                        ms.scale(0.5F, 0.5F, 0.5F);
                        itemRenderer.renderStatic(null, transported.stack, ItemDisplayContext.FIXED, false, ms, buffer, be.m_58904_(), stackLight, overlay, 0);
                        ms.popPose();
                        if (!renderUpright) {
                            if (!blockItem) {
                                ms.mulPose(Axis.YP.rotationDegrees(10.0F));
                            }
                            ms.translate(0.0, blockItem ? 0.015625 : 0.0625, 0.0);
                        } else {
                            ms.translate(0.0F, 0.0F, -0.0625F);
                        }
                    }
                    ms.popPose();
                }
                ms.popPose();
            }
        }
    }

    protected int getPackedLight(BeltBlockEntity controller, float beltPos) {
        int segment = (int) Math.floor((double) beltPos);
        return controller.lighter != null && segment < controller.lighter.lightSegments() && segment >= 0 ? controller.lighter.getPackedLight(segment) : 0;
    }
}