package com.simibubi.create.content.kinetics.crafter;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class MechanicalCrafterRenderer extends SafeBlockEntityRenderer<MechanicalCrafterBlockEntity> {

    public MechanicalCrafterRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(MechanicalCrafterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ms.pushPose();
        Direction facing = (Direction) be.m_58900_().m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING);
        Vec3 vec = Vec3.atLowerCornerOf(facing.getNormal()).scale(0.58).add(0.5, 0.5, 0.5);
        if (be.phase == MechanicalCrafterBlockEntity.Phase.EXPORTING) {
            Direction targetDirection = MechanicalCrafterBlock.getTargetDirection(be.m_58900_());
            float progress = Mth.clamp(((float) (1000 - be.countDown) + (float) be.getCountDownSpeed() * partialTicks) / 1000.0F, 0.0F, 1.0F);
            vec = vec.add(Vec3.atLowerCornerOf(targetDirection.getNormal()).scale((double) (progress * 0.75F)));
        }
        ms.translate(vec.x, vec.y, vec.z);
        ms.scale(0.5F, 0.5F, 0.5F);
        float yRot = AngleHelper.horizontalAngle(facing);
        ms.mulPose(Axis.YP.rotationDegrees(yRot));
        this.renderItems(be, partialTicks, ms, buffer, light, overlay);
        ms.popPose();
        this.renderFast(be, partialTicks, ms, buffer, light);
    }

    public void renderItems(MechanicalCrafterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.phase == MechanicalCrafterBlockEntity.Phase.IDLE) {
            ItemStack stack = be.getInventory().m_8020_(0);
            if (!stack.isEmpty()) {
                ms.pushPose();
                ms.translate(0.0F, 0.0F, -0.00390625F);
                ms.mulPose(Axis.YP.rotationDegrees(180.0F));
                Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
                ms.popPose();
            }
        } else {
            RecipeGridHandler.GroupedItems items = be.groupedItems;
            float distance = 0.5F;
            ms.pushPose();
            if (be.phase == MechanicalCrafterBlockEntity.Phase.CRAFTING) {
                items = be.groupedItemsBeforeCraft;
                items.calcStats();
                float progress = Mth.clamp(((float) (2000 - be.countDown) + (float) be.getCountDownSpeed() * partialTicks) / 1000.0F, 0.0F, 1.0F);
                float earlyProgress = Mth.clamp(progress * 2.0F, 0.0F, 1.0F);
                float lateProgress = Mth.clamp(progress * 2.0F - 1.0F, 0.0F, 1.0F);
                ms.scale(1.0F - lateProgress, 1.0F - lateProgress, 1.0F - lateProgress);
                Vec3 centering = new Vec3((double) ((float) (-items.minX) + (float) (-items.width + 1) / 2.0F), (double) ((float) (-items.minY) + (float) (-items.height + 1) / 2.0F), 0.0).scale((double) earlyProgress);
                ms.translate(centering.x * 0.5, centering.y * 0.5, 0.0);
                distance += (-4.0F * (progress - 0.5F) * (progress - 0.5F) + 1.0F) * 0.25F;
            }
            boolean onlyRenderFirst = be.phase == MechanicalCrafterBlockEntity.Phase.INSERTING || be.phase == MechanicalCrafterBlockEntity.Phase.CRAFTING && be.countDown < 1000;
            float spacing = distance;
            items.grid.forEach((pair, stack) -> {
                if (!onlyRenderFirst || (Integer) pair.getLeft() == 0 && (Integer) pair.getRight() == 0) {
                    ms.pushPose();
                    Integer x = (Integer) pair.getKey();
                    Integer y = (Integer) pair.getValue();
                    ms.translate((float) x.intValue() * spacing, (float) y.intValue() * spacing, 0.0F);
                    int offset = 0;
                    if (be.phase == MechanicalCrafterBlockEntity.Phase.EXPORTING && be.m_58900_().m_61138_(MechanicalCrafterBlock.POINTING)) {
                        Pointing value = (Pointing) be.m_58900_().m_61143_(MechanicalCrafterBlock.POINTING);
                        offset = value == Pointing.UP ? -1 : (value == Pointing.LEFT ? 2 : (value == Pointing.RIGHT ? -2 : 1));
                    }
                    ((TransformStack) TransformStack.cast(ms).rotateY(180.0)).translate(0.0, 0.0, (double) ((float) (x + y * 3 + offset * 9) / 1024.0F));
                    Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
                    ms.popPose();
                }
            });
            ms.popPose();
            if (be.phase == MechanicalCrafterBlockEntity.Phase.CRAFTING) {
                items = be.groupedItems;
                float progress = Mth.clamp(((float) (1000 - be.countDown) + (float) be.getCountDownSpeed() * partialTicks) / 1000.0F, 0.0F, 1.0F);
                float earlyProgress = Mth.clamp(progress * 2.0F, 0.0F, 1.0F);
                float lateProgress = Mth.clamp(progress * 2.0F - 1.0F, 0.0F, 1.0F);
                ms.mulPose(Axis.ZP.rotationDegrees(earlyProgress * 2.0F * 360.0F));
                float upScaling = earlyProgress * 1.125F;
                float downScaling = 1.0F + (1.0F - lateProgress) * 0.125F;
                ms.scale(upScaling, upScaling, upScaling);
                ms.scale(downScaling, downScaling, downScaling);
                items.grid.forEach((pair, stack) -> {
                    if ((Integer) pair.getLeft() == 0 && (Integer) pair.getRight() == 0) {
                        ms.pushPose();
                        ms.mulPose(Axis.YP.rotationDegrees(180.0F));
                        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
                        ms.popPose();
                    }
                });
            }
        }
    }

    public void renderFast(MechanicalCrafterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        BlockState blockState = be.m_58900_();
        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        if (!Backend.canUseInstancing(be.m_58904_())) {
            SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.SHAFTLESS_COGWHEEL, blockState);
            KineticBlockEntityRenderer.standardKineticRotationTransform(superBuffer, be, light);
            superBuffer.rotateCentered(Direction.UP, (float) (((Direction) blockState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)).getAxis() != Direction.Axis.X ? 0.0 : Math.PI / 2));
            superBuffer.rotateCentered(Direction.EAST, (float) (Math.PI / 2));
            superBuffer.renderInto(ms, vb);
        }
        Direction targetDirection = MechanicalCrafterBlock.getTargetDirection(blockState);
        BlockPos pos = be.m_58899_();
        if ((be.covered || be.phase != MechanicalCrafterBlockEntity.Phase.IDLE) && be.phase != MechanicalCrafterBlockEntity.Phase.CRAFTING && be.phase != MechanicalCrafterBlockEntity.Phase.INSERTING) {
            SuperByteBuffer lidBuffer = this.renderAndTransform(AllPartialModels.MECHANICAL_CRAFTER_LID, blockState);
            lidBuffer.light(light).renderInto(ms, vb);
        }
        if (MechanicalCrafterBlock.isValidTarget(be.m_58904_(), pos.relative(targetDirection), blockState)) {
            SuperByteBuffer beltBuffer = this.renderAndTransform(AllPartialModels.MECHANICAL_CRAFTER_BELT, blockState);
            SuperByteBuffer beltFrameBuffer = this.renderAndTransform(AllPartialModels.MECHANICAL_CRAFTER_BELT_FRAME, blockState);
            if (be.phase == MechanicalCrafterBlockEntity.Phase.EXPORTING) {
                int textureIndex = (int) ((float) be.getCountDownSpeed() / 128.0F * (float) AnimationTickHolder.getTicks());
                beltBuffer.shiftUVtoSheet(AllSpriteShifts.CRAFTER_THINGIES, (float) (textureIndex % 4) / 4.0F, 0.0F, 1);
            }
            beltBuffer.light(light).renderInto(ms, vb);
            beltFrameBuffer.light(light).renderInto(ms, vb);
        } else {
            SuperByteBuffer arrowBuffer = this.renderAndTransform(AllPartialModels.MECHANICAL_CRAFTER_ARROW, blockState);
            arrowBuffer.light(light).renderInto(ms, vb);
        }
    }

    private SuperByteBuffer renderAndTransform(PartialModel renderBlock, BlockState crafterState) {
        SuperByteBuffer buffer = CachedBufferer.partial(renderBlock, crafterState);
        float xRot = (float) ((Pointing) crafterState.m_61143_(MechanicalCrafterBlock.POINTING)).getXRotation();
        float yRot = AngleHelper.horizontalAngle((Direction) crafterState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING));
        buffer.rotateCentered(Direction.UP, (float) ((double) ((yRot + 90.0F) / 180.0F) * Math.PI));
        buffer.rotateCentered(Direction.EAST, (float) ((double) (xRot / 180.0F) * Math.PI));
        return buffer;
    }
}