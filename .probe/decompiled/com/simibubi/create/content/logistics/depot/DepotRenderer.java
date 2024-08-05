package com.simibubi.create.content.logistics.depot;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DepotRenderer extends SafeBlockEntityRenderer<DepotBlockEntity> {

    public DepotRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(DepotBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        renderItemsOf(be, partialTicks, ms, buffer, light, overlay, be.depotBehaviour);
    }

    public static void renderItemsOf(SmartBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay, DepotBehaviour depotBehaviour) {
        TransportedItemStack transported = depotBehaviour.heldItem;
        TransformStack msr = TransformStack.cast(ms);
        Vec3 itemPosition = VecHelper.getCenterOf(be.m_58899_());
        ms.pushPose();
        ms.translate(0.5F, 0.9375F, 0.5F);
        if (transported != null) {
            depotBehaviour.incoming.add(transported);
        }
        for (TransportedItemStack tis : depotBehaviour.incoming) {
            ms.pushPose();
            msr.nudge(0);
            float offset = Mth.lerp(partialTicks, tis.prevBeltPosition, tis.beltPosition);
            float sideOffset = Mth.lerp(partialTicks, tis.prevSideOffset, tis.sideOffset);
            if (tis.insertedFrom.getAxis().isHorizontal()) {
                Vec3 offsetVec = Vec3.atLowerCornerOf(tis.insertedFrom.getOpposite().getNormal()).scale((double) (0.5F - offset));
                ms.translate(offsetVec.x, offsetVec.y, offsetVec.z);
                boolean alongX = tis.insertedFrom.getClockWise().getAxis() == Direction.Axis.X;
                if (!alongX) {
                    sideOffset *= -1.0F;
                }
                ms.translate(alongX ? sideOffset : 0.0F, 0.0F, alongX ? 0.0F : sideOffset);
            }
            ItemStack itemStack = tis.stack;
            int angle = tis.angle;
            Random r = new Random(0L);
            renderItem(be.m_58904_(), ms, buffer, light, overlay, itemStack, angle, r, itemPosition);
            ms.popPose();
        }
        if (transported != null) {
            depotBehaviour.incoming.remove(transported);
        }
        for (int i = 0; i < depotBehaviour.processingOutputBuffer.getSlots(); i++) {
            ItemStack stack = depotBehaviour.processingOutputBuffer.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ms.pushPose();
                msr.nudge(i);
                boolean renderUpright = BeltHelper.isItemUpright(stack);
                msr.rotateY((double) (45.0F * (float) i));
                ms.translate(0.35F, 0.0F, 0.0F);
                if (renderUpright) {
                    msr.rotateY((double) (-(45.0F * (float) i)));
                }
                Random r = new Random((long) (i + 1));
                int angle = (int) (360.0F * r.nextFloat());
                renderItem(be.m_58904_(), ms, buffer, light, overlay, stack, renderUpright ? angle + 90 : angle, r, itemPosition);
                ms.popPose();
            }
        }
        ms.popPose();
    }

    public static void renderItem(Level level, PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack itemStack, int angle, Random r, Vec3 itemPosition) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        TransformStack msr = TransformStack.cast(ms);
        int count = Mth.log2(itemStack.getCount()) / 2;
        boolean renderUpright = BeltHelper.isItemUpright(itemStack);
        boolean blockItem = itemRenderer.getModel(itemStack, null, null, 0).isGui3d();
        ms.pushPose();
        msr.rotateY((double) angle);
        if (renderUpright) {
            Entity renderViewEntity = Minecraft.getInstance().cameraEntity;
            if (renderViewEntity != null) {
                Vec3 positionVec = renderViewEntity.position();
                Vec3 diff = itemPosition.subtract(positionVec);
                float yRot = (float) (Mth.atan2(diff.x, diff.z) + Math.PI);
                ms.mulPose(Axis.YP.rotation(yRot));
            }
            ms.translate(0.0, 0.09375, -0.0625);
        }
        for (int i = 0; i <= count; i++) {
            ms.pushPose();
            if (blockItem) {
                ms.translate(r.nextFloat() * 0.0625F * (float) i, 0.0F, r.nextFloat() * 0.0625F * (float) i);
            }
            ms.scale(0.5F, 0.5F, 0.5F);
            if (!blockItem && !renderUpright) {
                ms.translate(0.0F, -0.1875F, 0.0F);
                msr.rotateX(90.0);
            }
            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, level, 0);
            ms.popPose();
            if (!renderUpright) {
                if (!blockItem) {
                    msr.rotateY(10.0);
                }
                ms.translate(0.0, blockItem ? 0.015625 : 0.0625, 0.0);
            } else {
                ms.translate(0.0F, 0.0F, -0.0625F);
            }
        }
        ms.popPose();
    }
}