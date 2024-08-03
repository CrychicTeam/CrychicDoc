package com.simibubi.create.content.processing.burner;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BlazeBurnerRenderer extends SafeBlockEntityRenderer<BlazeBurnerBlockEntity> {

    public BlazeBurnerRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(BlazeBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        BlazeBurnerBlock.HeatLevel heatLevel = be.getHeatLevelFromBlock();
        if (heatLevel != BlazeBurnerBlock.HeatLevel.NONE) {
            Level level = be.m_58904_();
            BlockState blockState = be.m_58900_();
            float animation = be.headAnimation.getValue(partialTicks) * 0.175F;
            float horizontalAngle = AngleHelper.rad((double) be.headAngle.getValue(partialTicks));
            boolean canDrawFlame = heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING);
            boolean drawGoggles = be.goggles;
            boolean drawHat = be.hat;
            int hashCode = be.hashCode();
            renderShared(ms, null, bufferSource, level, blockState, heatLevel, animation, horizontalAngle, canDrawFlame, drawGoggles, drawHat, hashCode);
        }
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
        BlockState state = context.state;
        BlazeBurnerBlock.HeatLevel heatLevel = BlazeBurnerBlock.getHeatLevelOf(state);
        if (heatLevel != BlazeBurnerBlock.HeatLevel.NONE) {
            if (!heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
                heatLevel = BlazeBurnerBlock.HeatLevel.FADING;
            }
            Level level = context.world;
            float horizontalAngle = AngleHelper.rad((double) headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
            boolean drawGoggles = context.blockEntityData.contains("Goggles");
            boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
            int hashCode = context.hashCode();
            renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource, level, state, heatLevel, 0.0F, horizontalAngle, false, drawGoggles, drawHat, hashCode);
        }
    }

    private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource, Level level, BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel, float animation, float horizontalAngle, boolean canDrawFlame, boolean drawGoggles, boolean drawHat, int hashCode) {
        boolean blockAbove = animation > 0.125F;
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (float) (hashCode % 13) * 16.0F;
        float offsetMult = heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) ? 64.0F : 16.0F;
        float offset = Mth.sin((float) ((double) (renderTick / 16.0F) % (Math.PI * 2))) / offsetMult;
        float offset1 = Mth.sin((float) (((double) (renderTick / 16.0F) + Math.PI) % (Math.PI * 2))) / offsetMult;
        float offset2 = Mth.sin((float) (((double) (renderTick / 16.0F) + (Math.PI / 2)) % (Math.PI * 2))) / offsetMult;
        float headY = offset - animation * 0.75F;
        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        ms.pushPose();
        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;
            float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
            float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
            float speed = 0.03125F + 0.015625F * (float) heatLevel.ordinal();
            double vScroll = (double) (speed * time);
            vScroll -= Math.floor(vScroll);
            vScroll = vScroll * (double) spriteHeight / 2.0;
            double uScroll = (double) (speed * time / 2.0F);
            uScroll -= Math.floor(uScroll);
            uScroll = uScroll * (double) spriteWidth / 2.0;
            SuperByteBuffer flameBuffer = CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState);
            if (modelTransform != null) {
                flameBuffer.transform(modelTransform);
            }
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }
        PartialModel blazeModel;
        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING)) {
            blazeModel = blockAbove ? AllPartialModels.BLAZE_SUPER_ACTIVE : AllPartialModels.BLAZE_SUPER;
        } else if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            blazeModel = blockAbove && heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.KINDLED) ? AllPartialModels.BLAZE_ACTIVE : AllPartialModels.BLAZE_IDLE;
        } else {
            blazeModel = AllPartialModels.BLAZE_INERT;
        }
        SuperByteBuffer blazeBuffer = CachedBufferer.partial(blazeModel, blockState);
        if (modelTransform != null) {
            blazeBuffer.transform(modelTransform);
        }
        blazeBuffer.translate(0.0, (double) headY, 0.0);
        draw(blazeBuffer, horizontalAngle, ms, solid);
        if (drawGoggles) {
            PartialModel gogglesModel = blazeModel == AllPartialModels.BLAZE_INERT ? AllPartialModels.BLAZE_GOGGLES_SMALL : AllPartialModels.BLAZE_GOGGLES;
            SuperByteBuffer gogglesBuffer = CachedBufferer.partial(gogglesModel, blockState);
            if (modelTransform != null) {
                gogglesBuffer.transform(modelTransform);
            }
            gogglesBuffer.translate(0.0, (double) (headY + 0.5F), 0.0);
            draw(gogglesBuffer, horizontalAngle, ms, solid);
        }
        if (drawHat) {
            SuperByteBuffer hatBuffer = CachedBufferer.partial(AllPartialModels.TRAIN_HAT, blockState);
            if (modelTransform != null) {
                hatBuffer.transform(modelTransform);
            }
            hatBuffer.translate(0.0, (double) headY, 0.0);
            if (blazeModel == AllPartialModels.BLAZE_INERT) {
                ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) hatBuffer.translateY(0.5)).centre()).scale(0.75F)).unCentre();
            } else {
                hatBuffer.translateY(0.75);
            }
            hatBuffer.rotateCentered(Direction.UP, horizontalAngle + (float) Math.PI).translate(0.5, 0.0, 0.5).light(15728880).renderInto(ms, solid);
        }
        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            PartialModel rodsModel = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS : AllPartialModels.BLAZE_BURNER_RODS;
            PartialModel rodsModel2 = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2 : AllPartialModels.BLAZE_BURNER_RODS_2;
            SuperByteBuffer rodsBuffer = CachedBufferer.partial(rodsModel, blockState);
            if (modelTransform != null) {
                rodsBuffer.transform(modelTransform);
            }
            rodsBuffer.translate(0.0, (double) (offset1 + animation + 0.125F), 0.0).light(15728880).renderInto(ms, solid);
            SuperByteBuffer rodsBuffer2 = CachedBufferer.partial(rodsModel2, blockState);
            if (modelTransform != null) {
                rodsBuffer2.transform(modelTransform);
            }
            rodsBuffer2.translate(0.0, (double) (offset2 + animation - 0.1875F), 0.0).light(15728880).renderInto(ms, solid);
        }
        ms.popPose();
    }

    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(Direction.UP, horizontalAngle).light(15728880).renderInto(ms, vc);
    }
}