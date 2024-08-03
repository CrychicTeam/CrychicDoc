package com.simibubi.create.content.kinetics.mixer;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalMixerRenderer extends KineticBlockEntityRenderer<MechanicalMixerBlockEntity> {

    public MechanicalMixerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public boolean shouldRenderOffScreen(MechanicalMixerBlockEntity be) {
        return true;
    }

    protected void renderSafe(MechanicalMixerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState blockState = be.m_58900_();
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.SHAFTLESS_COGWHEEL, blockState);
            standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);
            float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
            float speed = be.getRenderedHeadRotationSpeed(partialTicks);
            float time = AnimationTickHolder.getRenderTime(be.m_58904_());
            float angle = time * speed * 6.0F / 10.0F % 360.0F / 180.0F * (float) Math.PI;
            SuperByteBuffer poleRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_POLE, blockState);
            poleRender.translate(0.0, (double) (-renderedHeadOffset), 0.0).light(light).renderInto(ms, vb);
            VertexConsumer vbCutout = buffer.getBuffer(RenderType.cutoutMipped());
            SuperByteBuffer headRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState);
            headRender.rotateCentered(Direction.UP, angle).translate(0.0, (double) (-renderedHeadOffset), 0.0).light(light).renderInto(ms, vbCutout);
        }
    }
}